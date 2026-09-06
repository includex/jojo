// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioScriptPresentationRequest

/** ScriptPresentationTimeline: 스크립트 강조와 아이템 획득 효과를 시간 순서로 재생하고, 완료 뒤 다음 명령을 허용한다. */
internal class ScriptPresentationTimeline {
    /** Phase: 전투 화면 흐름에서 현재 처리 종류를 구분한다. */
    enum class Phase { TIMED, ITEM_ACTION, ITEM_ICON, ITEM_MODAL }

    /** Snapshot: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
    data class Snapshot(
        /**
         * `request` (ScenarioScriptPresentationRequest,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val request: ScenarioScriptPresentationRequest,
        /**
         * `phase` (Phase,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val phase: Phase,
        /**
         * `startedAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val startedAt: Float,
        /**
         * `endsAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val endsAt: Float,
        /**
         * `battleUnitId` (String?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleUnitId: String?,
    )

    /** Effect: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
    sealed interface Effect {
        /**
         * `FinishUnitAction`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class FinishUnitAction(val battleUnitId: String) : Effect
        /**
         * `PlayGetItemSound`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object PlayGetItemSound : Effect
        /**
         * `PresentItemMessage`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class PresentItemMessage(val message: String) : Effect
        /**
         * `DismissUnitInfo`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object DismissUnitInfo : Effect
        /**
         * `ResumeScript`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object ResumeScript : Effect
    }
    /**
     * `Advance`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Advance(val effects: List<Effect>, val acceptsNewRequest: Boolean)

    /**
     * `active` (Snapshot?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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
        /**
         * `ITEM_ICON_DURATION` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ITEM_ICON_DURATION = .8f
    }
}
