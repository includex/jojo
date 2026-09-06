package com.jojo.game

import com.jojo.game.presentation.scenario.overlay.*

/** 보상·구매·판매 화면에서 파생한 상점 상태를 관리한다. */
data class ShopItem(val id: Int, val name: String, val type: String, val price: Int, val sell: Int)


class RewardFlow(
    private val end: Boolean,
    private val money: Int,
    private val flag: Int,
    private val items: List<Int>
) {

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


class ShopPurchaseModel(private val items: List<ShopItem>, var money: Int, var owned: Int, private val capacity: Int) {

    fun rows() = items.filter { it.type == "property" && it.price != 255 }.sortedBy { it.id }


    fun confirm(id: Int, q: Int): String? {
        val x = items.first { it.id == id }; return when {
            x.price == 255 -> "unsellable"; money < x.price -> "money"; owned + q > capacity -> "full"; else -> {
                money -= x.price * q; owned += q; null
            }
        }
    }

    /** 구매 화면에서 아이템 상세 화면으로 전환한다. */
    fun openPropertyQuantity(id: Int, inputChanged: (Double) -> Unit = {}): MsgBox3Layer {
        val item = rows().first { it.id == id }
        val limit = minOf(money / item.price, capacity - owned).coerceAtLeast(0)
        require(limit > 0) { "BuyLayer.onClick2 would show its error DialogueLayer" }
        return MsgBox3Layer(limit.toDouble(), "구매 수량(1 - %d):", "구매하기", { quantity ->
            if (quantity != 0.0) confirm(id, quantity.toInt())
        }, inputChanged)
    }
}


class ShopSaleModel(private val items: List<ShopItem>, var money: Int, var owned: Int) {

    fun rows(tab: String) =
        items.filter { if (tab == "weapon") it.type != "property" else it.type == "property" }.sortedBy { it.id }


    fun confirm(id: Int, q: Int): String? {
        val x = items.first { it.id == id }; return if (x.sell == 255) "unsellable" else {
            money += x.sell * q; owned = (owned - q).coerceAtLeast(0); null
        }
    }

    /** 판매 화면에서 아이템 상세 화면으로 전환한다. */
    fun openPropertyQuantity(id: Int, inputChanged: (Double) -> Unit = {}): MsgBox3Layer {
        val item = rows("property").first { it.id == id }
        require(item.sell != 255 && owned > 0) { "SellLayer.onClick would not open MsgBox3" }
        return MsgBox3Layer(owned.toDouble(), "판매 수량(1 - %d):", "판매하기", { quantity ->
            if (quantity != 0.0) confirm(id, quantity.toInt())
        }, inputChanged)
    }
}
