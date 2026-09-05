package com.jojo.game

/** Immutable state required by the equipment-change confirmation overlay. */
internal data class HallEquipConfirmationView(
    val values: List<Int>,
    val actionLabel: String,
) {
    companion object {
        fun from(values: List<Int>, actionLabel: String): HallEquipConfirmationView = HallEquipConfirmationView(
            values = List(8) { index -> values.getOrElse(index) { 0 } },
            actionLabel = actionLabel,
        )
    }
}
