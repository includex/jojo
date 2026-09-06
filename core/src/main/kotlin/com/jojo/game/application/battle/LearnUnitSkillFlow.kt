// Battle
package com.jojo.game.application.battle

/** LearnUnitSkillFlow: 유닛 학습 스킬 선택 흐름으로, 선택값을 임시 보관하고 저장 효과를 발행한다. */
class LearnUnitSkillFlow(initialUnit0: Int = 1024) {
    /**
     * `Effect` 계약 인터페이스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    sealed interface Effect {

        /**
         * `OpenSelectList` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class OpenSelectList(val selected: Int, val page: Int, val pageCount: Int = 50) : Effect

        /**
         * `SetUnit0` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class SetUnit0(val value: Int) : Effect
        /**
         * `Close` 싱글턴 객체: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data object Close : Effect
    }

    /**
     * `selectedSkill` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var selectedSkill = 0; private set
    /**
     * `unit0` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var unit0 = initialUnit0; private set
    /**
     * `pendingUnit0` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var pendingUnit0: Int? = null; private set

    /**
     * `selectSkill`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun selectSkill(id: Int) {
        selectedSkill = id; pendingUnit0 = null
    }

    /**
     * `panelButton`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun panelButton(panel: Int, button: Int): List<Effect> = if (panel == 0 && button in 0..2) listOf(
        Effect.OpenSelectList(
            unit0.coerceIn(0, 1024),
            unit0.coerceIn(0, 1024) / 50
        )
    ) else emptyList()

    /**
     * `selectListResult`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun selectListResult(value: Int) {
        if (value >= 0) pendingUnit0 = value
    }

    /**
     * `save`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun save(): List<Effect> =
        pendingUnit0?.let { unit0 = it; pendingUnit0 = null; listOf(Effect.SetUnit0(it)) }.orEmpty()

    /**
     * `close`: 사용한 상태와 자원을 정리한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun close() = listOf(Effect.Close)
}

/** EditRosterLearnRoute: 편집 화면에서 학습 화면을 열고 닫는 라우팅 상태를 관리한다. */
class EditRosterLearnRoute(private val editEnabled: Boolean) {

    /**
     * `State` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    enum class State { EDIT4, LEARN, CLOSED }

    /**
     * `state` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var state = State.EDIT4; private set

    /**
     * `button`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun button(tag: Int, touchEnd: Boolean): Boolean {
        val open = editEnabled && touchEnd && state == State.EDIT4 && tag == 4; if (open) state =
            State.LEARN; return open
    }

    /**
     * `close`: 사용한 상태와 자원을 정리한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun close() {
        state = State.CLOSED
    }
}

/** LearnUnitSkillRoute: 학습 화면의 하위 경로를 식별하는 열거형으로, 저장된 경로 문자열을 해석한다. */
enum class LearnUnitSkillRoute(val key: String) {
    DEFAULT("default"), SELECT("select"), APPLY("apply"), CANCEL("cancel");

    companion object {

        /**
         * `parse`: 입력을 규칙에 따라 계산·변환한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

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
