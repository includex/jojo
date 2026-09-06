// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** MsgBox3LayerTest: MsgBox3Layer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class MsgBox3LayerTest {
    @Test
    fun `buy caller contract initializes prefab labels and value`() {
        val layer = MsgBox3Layer(98.0, "구매 수량(1 - %d):", "구매하기", {})
        assertEquals("구매 수량(1 - 98):", layer.title)
        assertEquals("구매하기", layer.confirmLabel)
        assertEquals("1", layer.editText)
        assertEquals(1.0, layer.value)
        assertTrue(layer.attached)
    }

    @Test
    fun `text changed uses JavaScript Number clamp writeback and dispatch`() {
        val changes = mutableListOf<Double>()
        val layer = MsgBox3Layer(5.0, "판매 수량(1 - %d):", "판매하기", {}, changes::add)
        layer.textChanged("9")
        layer.textChanged("0")
        layer.textChanged("2.5")
        assertEquals("2.5", layer.editText)
        assertEquals(listOf(5.0, 1.0, 2.5), changes)
    }

    @Test
    fun `empty and invalid input preserve source normalization including NaN`() {
        val changes = mutableListOf<Double>()
        val layer = MsgBox3Layer(8.0, "x%d", "ok", {}, changes::add)
        layer.textChanged("   ")
        assertEquals("1", layer.editText)
        assertTrue(changes.isEmpty())
        layer.textChanged("abc")
        assertEquals("NaN", layer.editText)
        assertTrue(layer.value.isNaN())
        assertTrue(changes.single().isNaN())
    }

    @Test
    fun `confirm removes before callback and cancel returns numeric zero`() {
        val observations = mutableListOf<Pair<Boolean, Double>>()
        lateinit var confirm: MsgBox3Layer
        confirm = MsgBox3Layer(9.0, "x%d", "ok", { observations += confirm.attached to it })
        confirm.textChanged("4")
        confirm.touchButton(0, 1)
        assertTrue(observations.isEmpty())
        confirm.touchButton(0, 2)
        assertEquals(listOf(false to 4.0), observations)

        lateinit var cancel: MsgBox3Layer
        cancel = MsgBox3Layer(9.0, "x%d", "ok", { observations += cancel.attached to it })
        cancel.touchButton(1, 2)
        assertEquals(false to 0.0, observations.last())
    }

    @Test
    fun `inactive outside blocker is inert while direct listeners retain source repeat behavior`() {
        val calls = mutableListOf<Double>()
        val layer = MsgBox3Layer(3.0, "x%d", "ok", calls::add)
        layer.touchOutside()
        assertTrue(layer.attached)
        layer.touchButton(0, 2)
        layer.touchButton(1, 2)
        assertFalse(layer.attached)
        assertEquals(listOf(1.0, 0.0), calls)
    }

    @Test
    fun `actual buy and sell property adapters open MsgBox3 and apply callbacks`() {
        val item = ShopItem(150, "회복의 콩", "property", price = 10, sell = 5)
        val buyChanges = mutableListOf<Double>()
        val buy = ShopPurchaseModel(listOf(item), money = 70, owned = 0, capacity = 99)
        val buyModal = buy.openPropertyQuantity(150, buyChanges::add)
        assertEquals("구매 수량(1 - 7):", buyModal.title)
        buyModal.textChanged("3")
        buyModal.touchButton(0, 2)
        assertEquals(listOf(3.0), buyChanges)
        assertEquals(3, buy.owned)
        assertEquals(40, buy.money)

        val sellChanges = mutableListOf<Double>()
        val sell = ShopSaleModel(listOf(item), money = buy.money, owned = buy.owned)
        val sellModal = sell.openPropertyQuantity(150, sellChanges::add)
        sellModal.textChanged("12")
        assertEquals("3", sellModal.editText)
        assertEquals("판매 수량(1 - 3):", sellModal.title)
        assertEquals("판매하기", sellModal.confirmLabel)
        assertEquals(listOf(3.0), sellChanges)
        sellModal.touchButton(0, 2)
        assertEquals(0, sell.owned)
        assertEquals(55, sell.money)
    }

    @Test
    fun `strict render events are deterministic and preserve state fields`() {
        val buy = MsgBox3Layer(7.0, "구매 수량(1 - %d):", "구매하기", {})
        val buyLog = MsgBox3RenderEvents.jsonl("quantity-buy-initial", buy)
        assertEquals(buyLog, MsgBox3RenderEvents.jsonl("quantity-buy-initial", buy))
        assertEquals(12, buyLog.trimEnd().lineSequence().count())
        assertTrue(buyLog.contains("\"text\":\"구매 수량(1 - 7):\""))
        assertTrue(buyLog.contains("\"text\":\"구매하기\""))

        val sell = MsgBox3Layer(3.0, "판매 수량(1 - %d):", "판매하기", {})
        sell.textChanged("12")
        val sellLog = MsgBox3RenderEvents.jsonl("quantity-sell-edited", sell)
        assertEquals(12, sellLog.trimEnd().lineSequence().count())
        assertTrue(sellLog.contains("\"text\":\"판매 수량(1 - 3):\""))
        assertTrue(sellLog.contains("\"text\":\"3\""))
        assertTrue(sellLog.contains("\"text\":\"판매하기\""))
    }
}
