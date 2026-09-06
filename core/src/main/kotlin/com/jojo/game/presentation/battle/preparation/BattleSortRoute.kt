// Battle
package com.jojo.game.presentation.battle.preparation
class StartBattleSortRoute {
    /** Effect: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
    sealed interface Effect {
        data class Open(val x: Float, val y: Float) : Effect
        data class Sort(val field: Int, val descending: Boolean) : Effect
        data object Close : Effect
    }

    var open = false
        private set
    var selectedField = 0
        private set
    var descending = true
        private set


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


    fun cancel(touchEnd: Boolean): List<Effect> {
        if (!open || !touchEnd) return emptyList()
        open = false
        return listOf(Effect.Close)
    }
}
