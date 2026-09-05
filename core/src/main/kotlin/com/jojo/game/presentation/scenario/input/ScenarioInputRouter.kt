package com.jojo.game.presentation.scenario.input

/** Pure priority rules for ScenarioScreen keyboard, pointer, and wheel input. */
object ScenarioInputRouter {
    enum class HallLayer { FEATS, UNIT_INFO, MAGIC, ITEM, SAVE, INFO, EXCLUSIVE, MANAGEMENT, MAIN }
    sealed interface Touch {
        data class Hall(val layer: HallLayer, val closesManagement: Boolean = false) : Touch
        data class SelectAndConfirm(val index: Int) : Touch
        data object Advance : Touch
        data object None : Touch
    }

    data class HallState(
        val completeMenu: Boolean,
        val feats: Boolean,
        val unitInfo: Boolean,
        val magic: Boolean,
        val item: Boolean,
        val save: Boolean,
        val info: Boolean,
        val exclusive: Boolean,
        val management: Management?,
        val unitListOpen: Boolean,
    )

    enum class Management { EQUIP, BUY, SELL }

    fun hallTouch(state: HallState, x: Float, y: Float): Touch {
        if (!state.completeMenu) return Touch.None
        when {
            state.feats -> return Touch.Hall(HallLayer.FEATS)
            state.unitInfo -> return Touch.Hall(HallLayer.UNIT_INFO)
            state.magic -> return Touch.Hall(HallLayer.MAGIC)
            state.item -> return Touch.Hall(HallLayer.ITEM)
            state.save -> return Touch.Hall(HallLayer.SAVE)
            state.info -> return Touch.Hall(HallLayer.INFO)
            state.exclusive -> return Touch.Hall(HallLayer.EXCLUSIVE)
        }
        val management = state.management ?: return Touch.Hall(HallLayer.MAIN)
        if (management == Management.EQUIP && state.unitListOpen) return Touch.Hall(HallLayer.MANAGEMENT)
        val closes = when (management) {
            Management.EQUIP -> x in 642f..730f && y in 35f..84f
            Management.BUY -> x in 529f..653f && y in 35f..86f
            Management.SELL -> x in 869f..1000f && y in 75f..130f
        }
        return Touch.Hall(HallLayer.MANAGEMENT, closes)
    }

    fun choiceTouch(ask: Boolean, optionCount: Int, x: Float, y: Float): Touch {
        if (x !in 463f..1059f) return Touch.Advance
        if (ask) {
            val index = when {
                x in 482.84f..628.18f && y in 306.16f..349.16f -> 0
                x in 646.27f..791.61f && y in 306.16f..349.16f -> 1
                else -> -1
            }
            return if (index >= 0) Touch.SelectAndConfirm(index) else Touch.None
        }
        val row = ((401f - y) / 44f).toInt()
        return if (row in 0 until minOf(optionCount, 3)) Touch.SelectAndConfirm(row) else Touch.None
    }
}
