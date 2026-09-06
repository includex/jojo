package com.jojo.game

/**
 * Direct Kotlin implementation of ui/TreasureLayer.js.  In particular the source retains the
 * Model.itemIter order and increments the displayed No. for every definition;
 * undiscovered definitions simply do not receive a visible row node.
 */

class TreasureLayer(items: List<Item>, discovered: Set<Int>) {

    data class Item(val id: Int, val name: String, val icon: Int, val property: Boolean, val description: String)


    data class Row(val item: Item, val number: Int?, val discovered: Boolean, val label0: String?, val label1: String?)

    val all = items
    val rows = run {
        all.mapIndexed { index, item ->
            val found = item.id in discovered
            Row(
                item, if (found) index + 1 else null, found,
                if (found) "No.${index + 1}：${item.name}" else null,
                if (found) item.description else null
            )
        }
    }
    val title = "지금까지 발견한 보물 ${discovered.size.toString().padStart(2, '0')} / ${all.size}"


    fun select(id: Int): Item? = rows.firstOrNull { it.item.id == id && it.discovered }?.item
}
