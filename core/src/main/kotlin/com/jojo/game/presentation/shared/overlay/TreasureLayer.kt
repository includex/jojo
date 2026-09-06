// Shared
package com.jojo.game.presentation.shared.overlay

/** TreasureLayer: ui/TreasureLayer.js를 Kotlin으로 구현한다. 원본 Model.itemIter 순서와 모든 정의에 번호를 먼저 부여하는 규칙을 보존하고, 발견된 보물만 행으로 표시한다. */

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
