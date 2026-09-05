package com.jojo.game

import com.jojo.game.presentation.scenario.overlay.*

/** Desktop reward and shop models derived from RewardLayer, BuyLayer and SellLayer. */
data class ShopItem(val id: Int, val name: String, val type: String, val price: Int, val sell: Int)

/**
 * class  `RewardFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class RewardFlow(
    private val end: Boolean,
    private val money: Int,
    private val flag: Int,
    private val items: List<Int>
) {
    /**
     * 공개 메서드 `advance`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Map<String, Any>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun advance(): Map<String, Any> = if (end) mapOf(
        "phase" to "end",
        "panels" to if (money > 0) listOf("bg2", "label0", "label1") else listOf("bg2")
    )
    else if (money > 0) mapOf(
        "phase" to "money",
        "panels" to listOf("bg0"),
        "stars" to (0..2).map { if (flag and (1 shl it) != 0) "★" else "☆" })
    else mapOf("phase" to "items", "panels" to listOf("bg1"), "items" to items)
}

/**
 * class  `ShopPurchaseModel`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ShopPurchaseModel(private val items: List<ShopItem>, var money: Int, var owned: Int, private val capacity: Int) {
    /**
     * 공개 메서드 `rows`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun rows() = items.filter { it.type == "property" && it.price != 255 }.sortedBy { it.id }

    /**
     * 공개 메서드 `confirm`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `q` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun confirm(id: Int, q: Int): String? {
        val x = items.first { it.id == id }; return when {
            x.price == 255 -> "unsellable"; money < x.price -> "money"; owned + q > capacity -> "full"; else -> {
                money -= x.price * q; owned += q; null
            }
        }
    }

    /** Recovered BuyLayer.onClick2 property branch into Global118. */
    fun openPropertyQuantity(id: Int, inputChanged: (Double) -> Unit = {}): MsgBox3Layer {
        val item = rows().first { it.id == id }
        val limit = minOf(money / item.price, capacity - owned).coerceAtLeast(0)
        require(limit > 0) { "BuyLayer.onClick2 would show its error DialogueLayer" }
        return MsgBox3Layer(limit.toDouble(), "구매 수량(1 - %d):", "구매하기", { quantity ->
            if (quantity != 0.0) confirm(id, quantity.toInt())
        }, inputChanged)
    }
}

/**
 * class  `ShopSaleModel`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ShopSaleModel(private val items: List<ShopItem>, var money: Int, var owned: Int) {
    /**
     * 공개 메서드 `rows`
     *
     * ### 파라미터
    - `tab` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun rows(tab: String) =
        items.filter { if (tab == "weapon") it.type != "property" else it.type == "property" }.sortedBy { it.id }

    /**
     * 공개 메서드 `confirm`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `q` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun confirm(id: Int, q: Int): String? {
        val x = items.first { it.id == id }; return if (x.sell == 255) "unsellable" else {
            money += x.sell * q; owned = (owned - q).coerceAtLeast(0); null
        }
    }

    /** Recovered SellLayer.onClick property branch into Global118. */
    fun openPropertyQuantity(id: Int, inputChanged: (Double) -> Unit = {}): MsgBox3Layer {
        val item = rows("property").first { it.id == id }
        require(item.sell != 255 && owned > 0) { "SellLayer.onClick would not open MsgBox3" }
        return MsgBox3Layer(owned.toDouble(), "판매 수량(1 - %d):", "판매하기", { quantity ->
            if (quantity != 0.0) confirm(id, quantity.toInt())
        }, inputChanged)
    }
}
