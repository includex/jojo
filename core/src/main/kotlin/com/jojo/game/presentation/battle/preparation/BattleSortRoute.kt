// Battle
package com.jojo.game.presentation.battle.preparation
/**
 * `StartBattleSortRoute`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class StartBattleSortRoute {
    /** Effect: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
    sealed interface Effect {
        /**
         * `Open`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Open(val x: Float, val y: Float) : Effect
        /**
         * `Sort`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Sort(val field: Int, val descending: Boolean) : Effect
        /**
         * `Close`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Close : Effect
    }

    /**
     * `open` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var open = false
        private set
    /**
     * `selectedField` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var selectedField = 0
        private set
    /**
     * `descending` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var descending = true
        private set


    /**
     * `openFromButton`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openFromButton(worldX: Float, worldY: Float, buttonHeight: Float, touchEnd: Boolean): List<Effect> {
        if (!touchEnd || open) return emptyList()
        open = true
        return listOf(Effect.Open(worldX, worldY - buttonHeight / 2f))
    }

    /** select: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    fun select(field: Int, touchEnd: Boolean): List<Effect> {
        if (!open || !touchEnd || field !in 0..4) return emptyList()
        if (selectedField == field) descending = !descending
        selectedField = field
        open = false
        return listOf(Effect.Sort(field, descending))
    }


    /**
     * `cancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun cancel(touchEnd: Boolean): List<Effect> {
        if (!open || !touchEnd) return emptyList()
        open = false
        return listOf(Effect.Close)
    }
}
