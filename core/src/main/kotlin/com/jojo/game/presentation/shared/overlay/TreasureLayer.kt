// Shared
package com.jojo.game.presentation.shared.overlay

/** TreasureLayer: ui/TreasureLayer.js를 Kotlin으로 구현한다. 원본 Model.itemIter 순서와 모든 정의에 번호를 먼저 부여하는 규칙을 보존하고, 발견된 보물만 행으로 표시한다. */

class TreasureLayer(items: List<Item>, discovered: Set<Int>) {

    /**
     * `Item`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Item(val id: Int, val name: String, val icon: Int, val property: Boolean, val description: String)


    /**
     * `Row`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Row(val item: Item, val number: Int?, val discovered: Boolean, val label0: String?, val label1: String?)

    /**
     * `all` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val all = items
    /**
     * `rows` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val rows = run {
        all.mapIndexed { index, item ->
            /**
             * `found` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val found = item.id in discovered
            Row(
                item, if (found) index + 1 else null, found,
                if (found) "No.${index + 1}：${item.name}" else null,
                if (found) item.description else null
            )
        }
    }
    /**
     * `title` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val title = "지금까지 발견한 보물 ${discovered.size.toString().padStart(2, '0')} / ${all.size}"


    /**
     * `select`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun select(id: Int): Item? = rows.firstOrNull { it.item.id == id && it.discovered }?.item
}
