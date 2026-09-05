package com.jojo.game

/** StartBattleScreen button1_0 -> Hall BattleSortLayer id19 production route. */
class StartBattleSortRoute {
    sealed interface Effect {
        /**
         * data class  `Open`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Open(val x: Float, val y: Float) : Effect

        /**
         * data class  `Sort`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Sort(val field: Int, val descending: Boolean) : Effect
        data object Close : Effect
    }

    var open = false
        private set
    var selectedField = 0
        private set
    var descending = true
        private set

    /**
     * 공개 메서드 `openFromButton`
     *
     * ### 파라미터
    - `worldX` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `worldY` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `buttonHeight` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Effect>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun openFromButton(worldX: Float, worldY: Float, buttonHeight: Float, touchEnd: Boolean): List<Effect> {
        if (!touchEnd || open) return emptyList()
        open = true
        return listOf(Effect.Open(worldX, worldY - buttonHeight / 2f))
    }

    /** Selecting the current field toggles direction exactly as StartBattleScreen does. */
    fun select(field: Int, touchEnd: Boolean): List<Effect> {
        if (!open || !touchEnd || field !in 0..4) return emptyList()
        if (selectedField == field) descending = !descending
        selectedField = field
        open = false
        return listOf(Effect.Sort(field, descending))
    }

    /**
     * 공개 메서드 `cancel`
     *
     * ### 파라미터
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Effect>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun cancel(touchEnd: Boolean): List<Effect> {
        if (!open || !touchEnd) return emptyList()
        open = false
        return listOf(Effect.Close)
    }
}
