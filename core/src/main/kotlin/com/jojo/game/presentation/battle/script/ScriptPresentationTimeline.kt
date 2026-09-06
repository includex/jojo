package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioScriptPresentationRequest

/** 스크립트 강조 표시와 아이템 획득 효과의 생명주기를 관리합니다. */
internal class ScriptPresentationTimeline {
    enum class Phase { TIMED, ITEM_ACTION, ITEM_ICON, ITEM_MODAL }

    data class Snapshot(
        val request: ScenarioScriptPresentationRequest,
        val phase: Phase,
        val startedAt: Float,
        val endsAt: Float,
        val battleUnitId: String?,
    )

    sealed interface Effect {
        data class FinishUnitAction(val battleUnitId: String) : Effect
        data object PlayGetItemSound : Effect
        data class PresentItemMessage(val message: String) : Effect
        data object DismissUnitInfo : Effect
        data object ResumeScript : Effect
    }

    data class Advance(val effects: List<Effect>, val acceptsNewRequest: Boolean)

    private var active: Snapshot? = null

    /** 현재 활성화된 표시 요청의 스냅샷을 반환합니다. */
    fun snapshot(): Snapshot? = active

    /** 활성화된 표시 요청이 있는지 반환합니다. */
    fun isActive(): Boolean = active != null

    /** 일반 시간 제한 표시를 시작합니다. */
    fun startTimed(
        request: ScenarioScriptPresentationRequest,
        now: Float,
        duration: Float,
        battleUnitId: String? = null,
    ) {
        check(active == null) { "A scripted presentation is already active" }
        active = Snapshot(request, Phase.TIMED, now, now + duration, battleUnitId)
    }

    /** 아이템 획득 표시 흐름을 시작합니다. */
    fun startItem(
        request: ScenarioScriptPresentationRequest.GetItem,
        now: Float,
        actionDuration: Float,
        battleUnitId: String,
    ) {
        check(active == null) { "A scripted presentation is already active" }
        active = Snapshot(request, Phase.ITEM_ACTION, now, now + actionDuration, battleUnitId)
    }

    /** 현재 시각에 맞춰 표시 단계와 후속 효과를 진행합니다. */
    fun advance(now: Float, modalActive: Boolean): Advance {
        val current = active ?: return Advance(emptyList(), acceptsNewRequest = true)
        return when (current.phase) {
            Phase.ITEM_MODAL -> {
                if (modalActive) Advance(emptyList(), acceptsNewRequest = false)
                else {
                    active = null
                    Advance(emptyList(), acceptsNewRequest = true)
                }
            }

            Phase.ITEM_ACTION -> {
                if (now < current.endsAt) return Advance(emptyList(), acceptsNewRequest = false)
                active = current.copy(phase = Phase.ITEM_ICON, startedAt = now, endsAt = now + ITEM_ICON_DURATION)
                Advance(
                    listOfNotNull(
                        current.battleUnitId?.let(Effect::FinishUnitAction),
                        Effect.PlayGetItemSound,
                    ),
                    acceptsNewRequest = false,
                )
            }

            Phase.ITEM_ICON -> {
                if (now < current.endsAt) return Advance(emptyList(), acceptsNewRequest = false)
                val request = current.request as ScenarioScriptPresentationRequest.GetItem
                active = current.copy(phase = Phase.ITEM_MODAL)
                Advance(listOf(Effect.PresentItemMessage(request.completionMessage)), acceptsNewRequest = false)
            }

            Phase.TIMED -> {
                if (now < current.endsAt) return Advance(emptyList(), acceptsNewRequest = false)
                active = null
                Advance(
                    buildList {
                        if (current.request is ScenarioScriptPresentationRequest.UnitHighlight) add(Effect.DismissUnitInfo)
                        add(Effect.ResumeScript)
                    },
                    acceptsNewRequest = false,
                )
            }
        }
    }

    private companion object {
        const val ITEM_ICON_DURATION = .8f
    }
}
