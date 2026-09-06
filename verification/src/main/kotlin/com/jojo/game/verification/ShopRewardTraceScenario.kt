// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.scenario.overlay.*

/** ShopRewardTraceScenario: 하나의 Buy·Sell·Reward 픽스처를 끝까지 실행하고 관찰 가능한 추적을 보존한다. */
object ShopRewardTraceScenario {
    /** run: 검증 시나리오 입력을 적용하고 추적 결과를 반환한다. */
    fun run(fixture: ShopRewardFixture): String = ShopRewardTraceState(fixture).run()
}

/** ShopRewardTraceState: 검증 추적 데이터와 증거를 표현하는 타입이다. */
private class ShopRewardTraceState(private val fixture: ShopRewardFixture) {
    /** byId: 검증 대상의 현재 상태 값을 담는다. */
    private val byId = fixture.items.associateBy { it.id }
    /** money: 보유 금액을 담는다. */
    private var money = fixture.money
    /** properties: 검증 대상 목록을 담는다. */
    private val properties = linkedMapOf<Int, Int>()
    /** actions: 검증 대상 목록을 담는다. */
    private val actions = mutableListOf<String>()
    /** overlays: 검증 대상 목록을 담는다. */
    private val overlays = mutableListOf<String>()
    /** buyRows: 검증 대상의 현재 상태 값을 담는다. */
    private var buyRows = emptyList<Int>()
    /** sellTab: 검증 대상의 현재 상태 값을 담는다. */
    private var sellTab = 0
    /** helper: 검증 대상의 현재 상태 값을 담는다. */
    private var helper = false
    /** cardRuns: 검증 대상의 현재 상태 값을 담는다. */
    private var cardRuns = 0
    /** finalDeleted: 검증 대상의 현재 상태 값을 담는다. */
    private val finalDeleted = if (fixture.events.any { it == "sell:msgbox2:0" }) {
        fixture.items.firstOrNull { it.type != "property" && it.sell != 255 }?.let { listOf(it.id) } ?: emptyList()
    } else {
        emptyList()
    }

    init {
        fixture.items.filter { it.type == "property" }.forEach { properties[it.id] = fixture.owned }
    }

    /** run: 검증 시나리오 입력을 적용하고 추적 결과를 반환한다. */
    fun run(): String {
        val trace = mutableListOf<String>()
        initialize(trace)
        fixture.events.forEach { event ->
            handle(event, trace)
            trace += snapshot(
                event,
                listOf(
                    "rewardHelper" to helper.toString(),
                    "buyRows" to ids(buyRows),
                    "sellRows" to sellRows()
                )
            )
        }
        return ShopRewardJson.array(trace)
    }

    /** initialize: 검증 실행에 필요한 초기 상태를 준비한다. */
    private fun initialize(trace: MutableList<String>) {
        action(ShopRewardJson.string("bg"), ShopRewardJson.string("bg1"))
        trace += snapshot(
            "buy:create",
            listOf("factory" to ShopRewardJson.string("BuyLayer"), "baseLifecycle" to "true", "buyRows" to "[]")
        )
        action(ShopRewardJson.string("bg"), ShopRewardJson.string("bg1"))
        trace += snapshot(
            "sell:create",
            listOf("factory" to ShopRewardJson.string("SellLayer"), "sellTab" to "0")
        )
        action(ShopRewardJson.string("bgSound"), ShopRewardJson.string("REWARD"))
        if (fixture.events.contains("reward:cards") && fixture.rewardItems > 0) {
            listOf("bg0", "bg1", "bg2", "bg0").forEach { action(ShopRewardJson.string("active"), ShopRewardJson.string(it), "false") }
            action(ShopRewardJson.string("active"), ShopRewardJson.string("bg1"), "true")
            listOf("item0", "item1", "item2").forEach { action(ShopRewardJson.string("active"), ShopRewardJson.string(it), "false") }
            action(ShopRewardJson.string("active"), ShopRewardJson.string("item0"), "true")
            action(ShopRewardJson.string("assetComplete"), fixture.items.first { it.type == "property" }.id.toString())
        }
        action(
            ShopRewardJson.string("schedule"),
            when {
                fixture.rewardEnd && fixture.rewardMoney == 0 -> "4"
                !fixture.rewardEnd && fixture.rewardMoney == 0 && fixture.rewardItems > 0 -> "1"
                else -> "2"
            }
        )
        action(ShopRewardJson.string("helper"), ShopRewardJson.string("showInterstitial"))
        trace += snapshot("reward:create", listOf("factory" to ShopRewardJson.string("RewardLayer"), "helper" to "false"))
    }

    /** handle: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun handle(event: String, trace: MutableList<String>) {
        val parts = event.split(':')
        when {
            event == "reward:advance" -> {
                helper = true
                if (!fixture.rewardEnd && fixture.rewardMoney == 0 && fixture.rewardItems > 0) action(ShopRewardJson.string("schedule"), "4")
            }
            event == "reward:cards" -> {
                if (cardRuns++ == 0) {
                    action(ShopRewardJson.string("schedule"), "4")
                    action(ShopRewardJson.string("removed"))
                    action(ShopRewardJson.string("rewardCallback"))
                }
                helper = false
            }
            parts[0] == "buy" && parts[1] == "equip" -> buyEquip(parts)
            parts[0] == "buy" && parts[1] == "equipMsgBox2" -> buyEquipConfirm(parts)
            parts[0] == "buy" && (parts[1] == "press" || parts[1] == "hold") -> buyProperty(event, parts, trace)
            parts[0] == "buy" && parts[1] == "msgbox3" -> buyPropertyConfirm(parts)
            parts[0] == "sell" && parts[1] == "tab" -> sellTab = parts[2].toInt()
            parts[0] == "sell" && (parts[1] == "press" || parts[1] == "hold") -> sell(event, parts, trace)
            parts[0] == "sell" && parts[1] == "msgbox2" -> sellWeaponConfirm(parts)
            parts[0] == "sell" && parts[1] == "msgbox3" -> sellPropertyConfirm(parts)
            event == "sell:cancel" -> action(ShopRewardJson.string("removed"))
        }
    }

    /** buyEquip: 구매 검증 입력을 적용하고 결과 상태를 반환한다. */
    private fun buyEquip(parts: List<String>) {
        val item = byId.getValue(parts[2].toInt())
        layer("DialogueLayer", ShopRewardJson.objectValue(
            listOf("flag" to "0", "txt" to ShopRewardJson.string("&${fixture.unitId}\nLv.${item.level} ${item.name}의 가격은${item.price}.\n구매하시겠습니까?"))
        ))
        layer("MsgBox2", ShopRewardJson.string("[callback]"))
    }

    /** buyEquipConfirm: 구매 검증 입력을 적용하고 결과 상태를 반환한다. */
    private fun buyEquipConfirm(parts: List<String>) {
        if (parts[2] != "0") return
        val item = fixture.items.first { it.type != "property" }
        money -= item.price
        action(ShopRewardJson.string("money"), (-item.price).toString(), money.toString())
        action(ShopRewardJson.string("sound"), ShopRewardJson.string("PUSH_STORE"))
        action(ShopRewardJson.string("equipItem"), item.id.toString(), item.level.toString(), "3", "1", "null")
    }

    /** buyProperty: 구매 검증 입력을 적용하고 결과 상태를 반환한다. */
    private fun buyProperty(event: String, parts: List<String>, trace: MutableList<String>) {
        buyRows = fixture.items.filter { it.type == "property" && it.price != 255 }.sortedBy { it.id }.map { it.id }
        val item = byId.getValue(parts[2].toInt())
        action(ShopRewardJson.string("schedule"), "1")
        trace += snapshot("$event:begin", listOf("delayed" to "true"))
        if (parts[1] == "hold") {
            layer("ItemLayer", ShopRewardJson.objectValue(listOf("item" to item.id.toString())))
        } else if (money < item.price) {
            layer("DialogueLayer", ShopRewardJson.objectValue(listOf(
                "txt" to ShopRewardJson.string("&${fixture.unitId}\n금액이 부족하여\n계속 매입할 수 없습니다${item.name}。")
            )))
        } else {
            val count = minOf(money / item.price, 99 - (properties[item.id] ?: 0))
            layer("DialogueLayer", ShopRewardJson.objectValue(listOf(
                "flag" to "0", "txt" to ShopRewardJson.string("&${fixture.unitId}\n1개${item.name}필요함${item.price}.\n구매하시겠습니까?")
            )))
            layer("MsgBox3", ShopRewardJson.objectValue(listOf(
                "lab0" to ShopRewardJson.string("구매하기"),
                "txt" to ShopRewardJson.string("구매 수량(1 - %d):"),
                "count" to count.toString(),
                "func" to ShopRewardJson.string("[callback]")
            )))
        }
    }

    /** buyPropertyConfirm: 구매 검증 입력을 적용하고 결과 상태를 반환한다. */
    private fun buyPropertyConfirm(parts: List<String>) {
        val quantity = parts[2].toInt()
        if (quantity == 0) return
        val item = byId.getValue(buyRows.first())
        action(ShopRewardJson.string("sound"), ShopRewardJson.string("PUSH_STORE"))
        money -= item.price * quantity
        action(ShopRewardJson.string("money"), (-item.price * quantity).toString(), money.toString())
        properties[item.id] = (properties[item.id] ?: 0) + quantity
        action(ShopRewardJson.string("property"), item.id.toString(), quantity.toString())
    }

    /** sell: 판매 검증 입력을 적용하고 결과 상태를 반환한다. */
    private fun sell(event: String, parts: List<String>, trace: MutableList<String>) {
        val item = byId.getValue(parts[2].toInt())
        action(ShopRewardJson.string("schedule"), "1")
        trace += snapshot("$event:begin", listOf("delayed" to "true"))
        if (parts[1] == "hold") {
            layer("ItemLayer", ShopRewardJson.objectValue(listOf("item" to item.id.toString())))
        } else if (item.sell != 255) {
            if (item.type != "property") {
                layer("DialogueLayer", ShopRewardJson.objectValue(listOf(
                    "txt" to ShopRewardJson.string("&${fixture.unitId}\nLv.${item.level}의${item.name}판매 가능${item.sell}.\n판매하시겠습니까?"),
                    "flag" to "0"
                )))
                layer("MsgBox2", ShopRewardJson.string("[callback]"))
            } else {
                layer("DialogueLayer", ShopRewardJson.objectValue(listOf(
                    "flag" to "0", "txt" to ShopRewardJson.string("&${fixture.unitId}\n1개${item.name}판매 가능${item.sell}.\n판매하시겠습니까?")
                )))
                layer("MsgBox3", ShopRewardJson.objectValue(listOf(
                    "lab0" to ShopRewardJson.string("판매하기"),
                    "txt" to ShopRewardJson.string("판매 수량(1 - %d):"),
                    "count" to (properties[item.id] ?: 0).toString(),
                    "func" to ShopRewardJson.string("[callback]")
                )))
            }
        }
    }

    /** sellWeaponConfirm: 판매 검증 입력을 적용하고 결과 상태를 반환한다. */
    private fun sellWeaponConfirm(parts: List<String>) {
        if (parts[2] != "0") return
        val item = fixture.items.first { it.type != "property" && it.sell != 255 }
        action(ShopRewardJson.string("sound"), ShopRewardJson.string("ADD_MONEY"))
        money += item.sell
        action(ShopRewardJson.string("money"), item.sell.toString(), money.toString())
        action(ShopRewardJson.string("event"), ShopRewardJson.string("ITEM_SELL"), ShopRewardJson.objectValue(listOf(
            "id" to item.id.toString(), "lv" to item.level.toString(), "exp" to item.experience.toString(), "unitId" to fixture.unitId.toString()
        )))
        action(ShopRewardJson.string("weaponDelete"), item.id.toString())
    }

    /** sellPropertyConfirm: 판매 검증 입력을 적용하고 결과 상태를 반환한다. */
    private fun sellPropertyConfirm(parts: List<String>) {
        val quantity = parts[2].toInt()
        if (quantity == 0) return
        val item = fixture.items.first { it.type == "property" && it.sell != 255 }
        properties[item.id] = (properties[item.id] ?: 0) - quantity
        action(ShopRewardJson.string("property"), item.id.toString(), (-quantity).toString())
        action(ShopRewardJson.string("sound"), ShopRewardJson.string("ADD_MONEY"))
        money += item.sell * quantity
        action(ShopRewardJson.string("money"), (item.sell * quantity).toString(), money.toString())
    }

    /** action: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun action(vararg values: String) {
        actions += ShopRewardJson.array(values.toList())
    }

    /** layer: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun layer(kind: String, argument: String) {
        action(ShopRewardJson.string("addLayer"), ShopRewardJson.string(kind), argument)
        overlays += ShopRewardJson.objectValue(listOf("kind" to ShopRewardJson.string(kind), "arg" to argument))
    }

    /** propertiesJson: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun propertiesJson() = ShopRewardJson.objectValue(properties.map { it.key.toString() to it.value.toString() })

    /** ids: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun ids(values: List<Int>) = ShopRewardJson.array(values.sorted().map(Int::toString))

    /** sellRows: 판매 검증 입력을 적용하고 결과 상태를 반환한다. */
    private fun sellRows() = ids(fixture.items.filter {
        if (sellTab == 0) it.type != "property" else it.type == "property"
    }.filter { if (sellTab == 0) true else (properties[it.id] ?: 0) > 0 }.map { it.id })

    /** snapshot: 현재 추적 상태를 스냅샷으로 만든다. */
    private fun snapshot(step: String, extra: List<Pair<String, String>> = emptyList()): String {
        val fields = listOf(
            "step" to ShopRewardJson.string(step),
            "money" to money.toString(),
            "properties" to propertiesJson(),
            "weaponDeleted" to ids(finalDeleted),
            "actions" to ShopRewardJson.array(actions),
            "overlays" to ShopRewardJson.array(overlays)
        ) + extra
        return ShopRewardJson.objectValue(fields)
    }
}
