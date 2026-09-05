package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/**
 * Kotlin half of the source-factory Shop contract.  The state machine below is
 * the desktop game of BuyLayer.onClick2/SellLayer.onClick and RewardLayer's
 * coroutine gate: a press is distinct from a held press, confirmations run
 * only through the MsgBox callback, and store changes are recorded after it.
 */
/**
 * object  `ShopRewardTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object ShopRewardTraceHarness {
    private data class Item(
        val id: Int,
        val name: String,
        val type: String,
        val price: Int,
        val sell: Int,
        val lv: Int,
        val exp: Int
    )

    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"")
    private fun str(s: String) = "\"${esc(s)}\""
    private fun obj(vararg x: Pair<String, String>) = "{${x.joinToString(",") { str(it.first) + ":" + it.second }}}"
    private fun arr(x: List<String>) = "[${x.joinToString(",")}]"
    private fun parseCases(all: String): List<Pair<String, String>> {
        val starts = Regex("\\{\\s*\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").findAll(all).toList()
        return starts.mapIndexed { i, m ->
            m.groupValues[1] to all.substring(
                m.range.first,
                if (i + 1 < starts.size) starts[i + 1].range.first else all.length
            )
        }
    }

    private fun field(s: String, n: String) = Regex("\\\"$n\\\"\\s*:\\s*(\\d+)").find(s)!!.groupValues[1].toInt()
    private fun quote(s: String, n: String) = Regex("\\\"$n\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"").find(s)!!.groupValues[1]
    private fun events(s: String) = Regex("\\\"events\\\"\\s*:\\s*\\[(.*?)]").find(s)?.groupValues?.get(1)
        ?.let { Regex("\\\"([^\\\"]+)\\\"").findAll(it).map { q -> q.groupValues[1] }.toList() } ?: emptyList()

    @JvmStatic
    fun main(args: Array<String>) {
        val result = linkedMapOf<String, String>()
        parseCases(Files.readString(Path.of(args[0]))).forEach { (name, b) ->
            val money0 = field(b, "money")
            val owned = field(b, "owned")
            field(b, "capacity")
            val unit = field(b, "unitId")
            val rewardEnd =
                Regex("\\\"reward\\\"\\s*:\\s*\\{\\s*\\\"end\\\"\\s*:\\s*(true|false)").find(b)!!.groupValues[1] == "true"
            val rewardMoney =
                Regex("\\\"reward\\\"\\s*:\\s*\\{.*?\\\"money\\\"\\s*:\\s*(\\d+)").find(b)!!.groupValues[1].toInt()
            val rewardItems = Regex(
                "\\\"reward\\\"\\s*:\\s*\\{.*?\\\"items\\\"\\s*:\\s*\\[([^]]*)]",
                RegexOption.DOT_MATCHES_ALL
            ).find(b)?.groupValues?.get(1)?.let { Regex("\\d+").findAll(it).count() } ?: 0
            val items =
                Regex("\\{\\s*\\\"id\\\"\\s*:\\s*(\\d+).*?\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*?\\\"type\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*?\\\"price\\\"\\s*:\\s*(\\d+).*?\\\"sell\\\"\\s*:\\s*(\\d+).*?\\\"lv\\\"\\s*:\\s*(\\d+).*?\\\"exp\\\"\\s*:\\s*(\\d+)").findAll(
                    b
                ).map {
                    Item(
                        it.groupValues[1].toInt(),
                        it.groupValues[2],
                        it.groupValues[3],
                        it.groupValues[4].toInt(),
                        it.groupValues[5].toInt(),
                        it.groupValues[6].toInt(),
                        it.groupValues[7].toInt()
                    )
                }.toList()
            val allEvents = events(b)
            val fullCards = allEvents.contains("reward:cards")
            val by = items.associateBy { it.id }
            var money = money0
            val props = linkedMapOf<Int, Int>(); items.filter { it.type == "property" }
            .forEach { props[it.id] = owned }
            val deleted = mutableListOf<Int>()
            val actions = mutableListOf<String>()
            val overlays = mutableListOf<String>()
            var buyRows = emptyList<Int>()
            var sellTab = 0
            var helper = false
            var cardRuns = 0

            /**
             * 공개 메서드 `a`
             *
             * ### 파라미터
            - `v` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun a(vararg v: String) {
                actions += arr(v.toList())
            }; fun layer(kind: String, arg: String) {
            a(str("addLayer"), str(kind), arg); overlays += obj("kind" to str(kind), "arg" to arg)
        }

            /**
             * 공개 메서드 `propsJson`
             *
             * ### 파라미터
            - 입력 파라미터: 없음
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun propsJson() = obj(*props.map { it.key.toString() to it.value.toString() }.toTypedArray())

            /**
             * 공개 메서드 `ids`
             *
             * ### 파라미터
            - `xs` (`List<Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun ids(xs: List<Int>) = arr(xs.sorted().map { it.toString() })

            /**
             * 공개 메서드 `sellRows`
             *
             * ### 파라미터
            - 입력 파라미터: 없음
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun sellRows() = ids(items.filter { if (sellTab == 0) it.type != "property" else it.type == "property" }
                .filter { if (sellTab == 0) true else (props[it.id] ?: 0) > 0 }.map { it.id })

            val finalDeleted =
                if (allEvents.any { it == "sell:msgbox2:0" }) items.firstOrNull { it.type != "property" && it.sell != 255 }
                    ?.let { listOf(it.id) } ?: emptyList() else emptyList()

            /**
             * 공개 메서드 `snap`
             *
             * ### 파라미터
            - `step` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `extra` (`List<Pair<String, String>> = emptyList(`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun snap(step: String, extra: List<Pair<String, String>> = emptyList()): String {
                // Source trace keeps the mutable weaponDeleted array by reference;
                // JSON.stringify therefore observes its eventual callback result.
                val base = listOf(
                    "step" to str(step),
                    "money" to money.toString(),
                    "properties" to propsJson(),
                    "weaponDeleted" to ids(finalDeleted),
                    "actions" to arr(actions),
                    "overlays" to arr(overlays)
                ) + extra
                return obj(*base.toTypedArray())
            }

            val trace = mutableListOf<String>(); a(str("bg"), str("bg1")); trace += snap(
            "buy:create",
            listOf("factory" to str("BuyLayer"), "baseLifecycle" to "true", "buyRows" to "[]")
        ); a(str("bg"), str("bg1")); trace += snap(
            "sell:create",
            listOf("factory" to str("SellLayer"), "sellTab" to "0")
        ); a(str("bgSound"), str("REWARD")); if (fullCards && rewardItems > 0) {
            listOf("bg0", "bg1", "bg2", "bg0").forEach { a(str("active"), str(it), "false") }; a(
                str("active"),
                str("bg1"),
                "true"
            ); listOf("item0", "item1", "item2").forEach { a(str("active"), str(it), "false") }; a(
                str("active"),
                str("item0"),
                "true"
            ); a(str("assetComplete"), items.first { it.type == "property" }.id.toString())
        }; a(
            str("schedule"), when {
                rewardEnd && rewardMoney == 0 -> "4"; !rewardEnd && rewardMoney == 0 && rewardItems > 0 -> "1"; else -> "2"
            }
        ); a(str("helper"), str("showInterstitial")); trace += snap(
            "reward:create",
            listOf("factory" to str("RewardLayer"), "helper" to "false")
        )
            events(b).forEach { ev ->
                val p = ev.split(':'); when {
                ev == "reward:advance" -> {
                    helper = true; if (!rewardEnd && rewardMoney == 0 && rewardItems > 0) a(str("schedule"), "4")
                }

                ev == "reward:cards" -> {
                    if (cardRuns++ == 0) {
                        a(str("schedule"), "4"); a(str("removed")); a(str("rewardCallback"))
                    }; helper = false
                }

                p[0] == "buy" && p[1] == "equip" -> {
                    val it = by.getValue(p[2].toInt()); layer(
                        "DialogueLayer",
                        obj("flag" to "0", "txt" to str("&$unit\nLv.${it.lv} ${it.name}의 가격은${it.price}.\n구매하시겠습니까?"))
                    ); layer("MsgBox2", str("[callback]"))
                }

                p[0] == "buy" && p[1] == "equipMsgBox2" -> if (p[2] == "0") {
                    val it = items.first { it.type != "property" }; money -= it.price; a(
                        str("money"),
                        (-it.price).toString(),
                        money.toString()
                    ); a(str("sound"), str("PUSH_STORE")); a(
                        str("equipItem"),
                        it.id.toString(),
                        it.lv.toString(),
                        "3",
                        "1",
                        "null"
                    )
                }

                p[0] == "buy" && (p[1] == "press" || p[1] == "hold") -> {
                    buyRows =
                        items.filter { it.type == "property" && it.price != 255 }.sortedBy { it.id }.map { it.id }
                    val it = by.getValue(p[2].toInt()); a(str("schedule"), "1"); trace += snap(
                        "$ev:begin",
                        listOf("delayed" to "true")
                    ); if (p[1] == "hold") layer("ItemLayer", obj("item" to it.id.toString())) else when {
                        money < it.price -> layer(
                            "DialogueLayer",
                            obj("txt" to str("&$unit\n금액이 부족하여\n계속 매입할 수 없습니다${it.name}。"))
                        ); else -> {
                            val count = minOf(money / it.price, 99 - (props[it.id] ?: 0)); layer(
                                "DialogueLayer",
                                obj("flag" to "0", "txt" to str("&$unit\n1개${it.name}필요함${it.price}.\n구매하시겠습니까?"))
                            ); layer(
                                "MsgBox3",
                                obj(
                                    "lab0" to str("구매하기"),
                                    "txt" to str("구매 수량(1 - %d):"),
                                    "count" to count.toString(),
                                    "func" to str("[callback]")
                                )
                            )
                        }
                    }
                }

                p[0] == "buy" && p[1] == "msgbox3" -> {
                    val q = p[2].toInt(); if (q != 0) {
                        a(
                            str("sound"),
                            str("PUSH_STORE")
                        ); money -= by.getValue(buyRows.first()).price * q; a(
                            str("money"),
                            (-by.getValue(buyRows.first()).price * q).toString(),
                            money.toString()
                        ); props[buyRows.first()] = (props[buyRows.first()] ?: 0) + q; a(
                            str("property"),
                            buyRows.first().toString(),
                            q.toString()
                        )
                    }
                }

                p[0] == "sell" && p[1] == "tab" -> sellTab = p[2].toInt()
                p[0] == "sell" && (p[1] == "press" || p[1] == "hold") -> {
                    val it = by.getValue(p[2].toInt()); a(str("schedule"), "1"); trace += snap(
                        "$ev:begin",
                        listOf("delayed" to "true")
                    ); if (p[1] == "hold") layer(
                        "ItemLayer",
                        obj("item" to it.id.toString())
                    ) else if (it.sell != 255) {
                        if (it.type != "property") {
                            layer(
                                "DialogueLayer",
                                obj(
                                    "txt" to str("&$unit\nLv.${it.lv}의${it.name}판매 가능${it.sell}.\n판매하시겠습니까?"),
                                    "flag" to "0"
                                )
                            ); layer("MsgBox2", str("[callback]"))
                        } else {
                            layer(
                                "DialogueLayer",
                                obj("flag" to "0", "txt" to str("&$unit\n1개${it.name}판매 가능${it.sell}.\n판매하시겠습니까?"))
                            ); layer(
                                "MsgBox3",
                                obj(
                                    "lab0" to str("판매하기"),
                                    "txt" to str("판매 수량(1 - %d):"),
                                    "count" to (props[it.id] ?: 0).toString(),
                                    "func" to str("[callback]")
                                )
                            )
                        }
                    }
                }

                p[0] == "sell" && p[1] == "msgbox2" -> {
                    if (p[2] == "0") {
                        val it = items.first { it.type != "property" && it.sell != 255 }; a(
                            str("sound"),
                            str("ADD_MONEY")
                        ); money += it.sell; a(str("money"), it.sell.toString(), money.toString()); a(
                            str("event"),
                            str("ITEM_SELL"),
                            obj(
                                "id" to it.id.toString(),
                                "lv" to it.lv.toString(),
                                "exp" to it.exp.toString(),
                                "unitId" to unit.toString()
                            )
                        ); deleted += it.id; a(str("weaponDelete"), it.id.toString())
                    }
                }

                p[0] == "sell" && p[1] == "msgbox3" -> {
                    val q = p[2].toInt(); if (q != 0) {
                        val it = items.first { it.type == "property" && it.sell != 255 }; props[it.id] =
                            (props[it.id] ?: 0) - q; a(
                            str("property"),
                            it.id.toString(),
                            (-q).toString()
                        ); a(str("sound"), str("ADD_MONEY")); money += it.sell * q; a(
                            str("money"),
                            (it.sell * q).toString(),
                            money.toString()
                        )
                    }
                }

                ev == "sell:cancel" -> a(str("removed"))
            }; trace += snap(
                ev,
                listOf("rewardHelper" to helper.toString(), "buyRows" to ids(buyRows), "sellRows" to sellRows())
            )
            }
            result[name] = arr(trace)
        }
        val out = obj(*result.entries.map { it.key to it.value }.toTypedArray())
        val dest = Path.of(args[1]); Files.createDirectories(dest.parent); Files.writeString(dest, out); println(out)
    }
}
