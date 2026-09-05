package com.jojo.game.presentation.scenario.hall

/** Source-authored EquipLayer geometry and paint-order contract. */
internal object HallEquipmentRenderPlan {
    fun inventoryRowY(index: Int): Float = 515f - index * 68f

    fun slotY(index: Int): Float = listOf(20.97f, -114.91f, -250.79f)[index]

    fun paintOrder(inventoryCount: Int, slotCount: Int): List<String> = buildList {
        addAll(listOf("background", "frame", "title", "footer", "tabs", "inventory-frame", "unit-header"))
        repeat(inventoryCount) { add("inventory-$it") }
        add("unit-summary")
        repeat(slotCount) { add("slot-$it") }
        add("notice")
    }
}
