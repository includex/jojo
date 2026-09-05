package com.jojo.game

/**
 * Direct Kotlin implementation of ui/TreasureLayer.js.  In particular the source retains the
 * Model.itemIter order and increments the displayed No. for every definition;
 * undiscovered definitions simply do not receive a visible row node.
 */
/**
 * class  `TreasureLayer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class TreasureLayer(items: List<Item>, discovered: Set<Int>) {
    /**
     * data class  `Item`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Item(val id: Int, val name: String, val icon: Int, val property: Boolean, val description: String)

    /**
     * data class  `Row`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * 공개 메서드 `select`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Item?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun select(id: Int): Item? = rows.firstOrNull { it.item.id == id && it.discovered }?.item
}
