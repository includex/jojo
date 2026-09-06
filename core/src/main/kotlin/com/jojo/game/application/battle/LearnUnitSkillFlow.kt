// Battle
package com.jojo.game.application.battle

/** LearnUnitSkillFlow: 유닛 학습 스킬 선택 흐름으로, 선택값을 임시 보관하고 저장 효과를 발행한다. */
class LearnUnitSkillFlow(initialUnit0: Int = 1024) {
    sealed interface Effect {

        data class OpenSelectList(val selected: Int, val page: Int, val pageCount: Int = 50) : Effect

        data class SetUnit0(val value: Int) : Effect
        data object Close : Effect
    }

    var selectedSkill = 0; private set
    var unit0 = initialUnit0; private set
    var pendingUnit0: Int? = null; private set

    fun selectSkill(id: Int) {
        selectedSkill = id; pendingUnit0 = null
    }

    fun panelButton(panel: Int, button: Int): List<Effect> = if (panel == 0 && button in 0..2) listOf(
        Effect.OpenSelectList(
            unit0.coerceIn(0, 1024),
            unit0.coerceIn(0, 1024) / 50
        )
    ) else emptyList()

    fun selectListResult(value: Int) {
        if (value >= 0) pendingUnit0 = value
    }

    fun save(): List<Effect> =
        pendingUnit0?.let { unit0 = it; pendingUnit0 = null; listOf(Effect.SetUnit0(it)) }.orEmpty()

    fun close() = listOf(Effect.Close)
}

/** EditRosterLearnRoute: 편집 화면에서 학습 화면을 열고 닫는 라우팅 상태를 관리한다. */
class EditRosterLearnRoute(private val editEnabled: Boolean) {

    enum class State { EDIT4, LEARN, CLOSED }

    var state = State.EDIT4; private set

    fun button(tag: Int, touchEnd: Boolean): Boolean {
        val open = editEnabled && touchEnd && state == State.EDIT4 && tag == 4; if (open) state =
            State.LEARN; return open
    }

    fun close() {
        state = State.CLOSED
    }
}

/** LearnUnitSkillRoute: 학습 화면의 하위 경로를 식별하는 열거형으로, 저장된 경로 문자열을 해석한다. */
enum class LearnUnitSkillRoute(val key: String) {
    DEFAULT("default"), SELECT("select"), APPLY("apply"), CANCEL("cancel");

    companion object {

        fun parse(state: String?): LearnUnitSkillRoute? {
            val normalized = state?.removeSuffix("-fixture")
                ?: return null; if (!normalized.startsWith("hall-learn-")) return null; return entries.firstOrNull {
                it.key == normalized.removePrefix(
                    "hall-learn-"
                )
            }
        }
    }
}
