// Verification
package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path


/** GameDataTraceHarness: A/F 인벤토리 픽스처의 아이템 규칙과 저장 상태를 직접 검증한다. */
object GameDataTraceHarness {
    /** I: i 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class I(
        /** id: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val id: Int,
        /** name: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val name: String,
        /** treasure: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val treasure: Int,
        /** kind: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val kind: Int,
        /** value: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val value: Int,
        /** eff: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val eff: Int,
        /** price: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val price: Int,
        /** icon: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val icon: Int
    )

    /** j: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun j(s: String) = "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\""
    /** ar: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun ar(xs: List<String>) = "[${xs.joinToString(",")}]"
    /** ob: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun ob(vararg xs: Pair<String, String>) = "{${xs.joinToString(",") { j(it.first) + ":" + it.second }}}"
    /** ints: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun ints(s: String, key: String) =
        Regex("\\\"$key\\\"\\s*:\\s*\\[([^]]*)]").find(s)!!.groupValues[1].split(',').filter { it.isNotBlank() }
            .map { it.trim().toInt() }

    /** events: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun events(s: String) = Regex("\\\"events\\\"\\s*:\\s*\\[(.*?)]").find(s)!!.groupValues[1].split(',')
        .map { it.trim().removeSurrounding("\"") }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(args: Array<String>) {
        val text = Files.readString(Path.of(args[0]))
        val name = Regex("\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)").find(text)!!.groupValues[1]
        val items =
            Regex("\\{\\\"id\\\":(\\d+),\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"treasure\\\":(\\d+),\\\"a\\\":\\{\\\"0\\\":(\\d+),\\\"2\\\":(\\d+),\\\"3\\\":(\\d+),\\\"5\\\":(\\d+),\\\"6\\\":(\\d+)").findAll(
                text
            ).map {
                I(
                    it.groupValues[1].toInt(),
                    it.groupValues[2],
                    it.groupValues[3].toInt(),
                    it.groupValues[4].toInt(),
                    it.groupValues[5].toInt(),
                    it.groupValues[6].toInt(),
                    it.groupValues[7].toInt(),
                    it.groupValues[8].toInt()
                )
            }.associateBy { it.id }
        val propIds = ints(text, "propertyIds")
        val props = propIds.associateWith { 0 }.toMutableMap()
        val weapons = mutableListOf<Triple<Int, Int, Int>>()
        val out = mutableListOf<String>()

        /** type: 아이템 분류 규칙에 따라 타입 번호를 계산한다. */
        fun type(x: I) = when {
            x.kind <= 9 -> 0; x.kind <= 19 -> 1; x.id in 1000..1003 -> 3; else -> 2
        }

        /** snapshot: 아이템 상태를 검증 결과 목록에 추가한다. */
        fun snapshot(step: String) = out.add(
            ob(
                "s" to j(step),
                "props" to ar(propIds.map { id -> ar(listOf(id.toString(), props.getValue(id).toString())) }),
                "weapons" to ar(weapons.map {
                    ar(
                        listOf(
                            it.first.toString(),
                            it.second.toString(),
                            it.third.toString()
                        )
                    )
                }),
                "hall" to ob("AUTO_CLOSE" to "1", "QI_PAO" to "2", "MOVE" to "20")
            )
        )


        /** itemRow: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun itemRow(id: Int, pos: Int): String {
            val x = items.getValue(id)
            val t = type(x)
            val w = weapons.firstOrNull { it.first == id }
            val lv = w?.second ?: 1
            val exp = w?.third ?: 0
            val phase = if (t < 2 && x.kind % 2 == 0) items.values.count { it.id < id && it.kind == x.kind } else -1
            val flag = if (t >= 2) 0 else when (x.kind - x.kind % 2) {
                2, 4 -> 2; 6 -> 3; 10, 12, 14 -> 4; else -> 1
            }
            val price = if (x.price == 255) 255 else x.price * 100
            val value = if (t == 3) x.value or (if (x.eff <= 1) x.eff shl 8 else 0) else x.value
            val prop = when (t) {
                0, 1 -> "type${x.kind}"; 2 -> "보조"; else -> "아이템"
            }; return ob(
                "id" to id.toString(),
                "type" to t.toString(),
                "itemType" to x.kind.toString(),
                "phase" to phase.toString(),
                "flag" to flag.toString(),
                "name" to j(x.name),
                "price" to price.toString(),
                "sell" to (if (price == 255) 255 else price * 3 / 4).toString(),
                "value" to value.toString(),
                "icon" to (x.icon + 1).toString(),
                "canDrop" to (t != 3 && pos != 255).toString(),
                "lv" to lv.toString(),
                "exp" to exp.toString(),
                "nameProperty" to j(prop)
            )
        }
        snapshot("clear")
        for (event in events(text)) {
            val parts = event.split(':')
            when (parts[0]) {
                "prop" -> {
                    val id = parts[1].toInt()
                    props[id] = ((props[id] ?: 0) + parts[2].toInt()).coerceIn(0, 99)
                }

                "weapon" -> weapons.add(Triple(parts[1].toInt(), parts[2].toInt(), parts[3].toInt()))
                "delete" -> weapons.removeAt(parts[1].toInt())
                "item" -> out.add(ob("s" to j(event), "item" to itemRow(parts[1].toInt(), parts[2].toInt())))
            }
            snapshot(event)
        }
        val dest = Path.of(args[1]); Files.createDirectories(dest.parent); Files.writeString(dest, ob(name to ar(out)))
    }
}
