// Scenario
package com.jojo.game.presentation.scenario.overlay

import com.jojo.game.presentation.i18n.SystemMessage


/** ShopItem: 상점·보상 화면이 표시하는 아이템의 식별자·이름·분류·구매·판매 가격을 나타낸다. */
data class ShopItem(val id: Int, val name: String, val type: String, val price: Int, val sell: Int)


/** ShopPurchaseModel: 소지금·가방 용량을 확인해 구매 가능한 아이템과 결과 메시지를 계산한다. */
class ShopPurchaseModel(private val items: List<ShopItem>, var money: Int, var owned: Int, private val capacity: Int) {

    /**
     * `rows`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun rows() = items.filter { it.type == "property" && it.price != 255 }.sortedBy { it.id }


    /**
     * `confirm`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun confirm(id: Int, q: Int): String? {
        val x = items.first { it.id == id }; return when {
            x.price == 255 -> "unsellable"; money < x.price -> "money"; owned + q > capacity -> "full"; else -> {
                money -= x.price * q; owned += q; null
            }
        }
    }

    /** openPropertyQuantity: 선택한 소모품의 수량 입력 모달을 열고 변경된 수량을 외부 화면에 알린다. */
    fun openPropertyQuantity(id: Int, inputChanged: (Double) -> Unit = {}): MsgBox3Layer {
        /**
         * `item` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val item = rows().first { it.id == id }
        /**
         * `limit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val limit = minOf(money / item.price, capacity - owned).coerceAtLeast(0)
        require(limit > 0) { "BuyLayer.onClick2 would show its error DialogueLayer" }
        return MsgBox3Layer(limit.toDouble(), SystemMessage.S_45EDDC7A28, SystemMessage.S_620D5989B0, { quantity ->
            if (quantity != 0.0) confirm(id, quantity.toInt())
        }, inputChanged)
    }
}


/** ShopSaleModel: 보유 아이템을 분류별로 정렬하고 판매 수량에 따른 소지금 변화를 계산한다. */
class ShopSaleModel(private val items: List<ShopItem>, var money: Int, var owned: Int) {

    /**
     * `rows`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun rows(tab: String) =
        items.filter { if (tab == "weapon") it.type != "property" else it.type == "property" }.sortedBy { it.id }


    /**
     * `confirm`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun confirm(id: Int, q: Int): String? {
        val x = items.first { it.id == id }; return if (x.sell == 255) "unsellable" else {
            money += x.sell * q; owned = (owned - q).coerceAtLeast(0); null
        }
    }

    /** openPropertyQuantity: 판매할 소모품의 수량 입력 모달을 열고 변경된 수량을 외부 화면에 알린다. */
    fun openPropertyQuantity(id: Int, inputChanged: (Double) -> Unit = {}): MsgBox3Layer {
        /**
         * `item` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val item = rows("property").first { it.id == id }
        require(item.sell != 255 && owned > 0) { "SellLayer.onClick would not open MsgBox3" }
        return MsgBox3Layer(owned.toDouble(), SystemMessage.S_B9A391DF6A, SystemMessage.S_F1BC97987B, { quantity ->
            if (quantity != 0.0) confirm(id, quantity.toInt())
        }, inputChanged)
    }
}
