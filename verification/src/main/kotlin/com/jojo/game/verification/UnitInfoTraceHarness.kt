// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.shared.overlay.UnitInfoLayer

/** UnitInfoTraceHarness: tools/unit_info_source_trace_harness.js에 대응하며 두 실행기가 하나의 픽스처를 사용한다. */
object UnitInfoTraceHarness {
    /** Json: 검증 데이터를 JSON 형식으로 변환하는 타입이다. */
    private class Json(private val s: String) {
        var p = 0
        /** value: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun value(): Any? {
            ws(); return when (s[p]) {
                '{' -> obj(); '[' -> arr(); '"' -> str(); 't' -> {
                    p += 4; true
                }; 'f' -> {
                    p += 5; false
                }; 'n' -> {
                    p += 4; null
                }; else -> num()
            }
        }

        /** ws: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun ws() {
            while (p < s.length && s[p].isWhitespace()) p++
        }

        /** str: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun str(): String {
            p++
            val a = p; while (s[p] != '"') p++; return s.substring(a, p++)
        }

        /** obj: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun obj(): Map<String, Any?> {
            p++
            val r = linkedMapOf<String, Any?>(); ws(); while (s[p] != '}') {
                val k = str(); ws(); p++; r[k] = value(); ws(); if (s[p] == ',') {
                    p++; ws()
                }
            }; p++; return r
        }

        /** arr: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun arr(): List<Any?> {
            p++
            val r = mutableListOf<Any?>(); ws(); while (s[p] != ']') {
                r += value(); ws(); if (s[p] == ',') {
                    p++; ws()
                }
            }; p++; return r
        }

        /** num: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
        fun num(): Int {
            val a = p; while (p < s.length && (s[p].isDigit() || s[p] == '-')) p++; return s.substring(a, p).toInt()
        }
    }

    /** q: 문자열을 JSON 인용 형식으로 변환한다. */
    private fun q(x: String) =
        buildString { append('"'); x.forEach { if (it == '"' || it == '\\') append('\\'); append(it) }; append('"') }

    /** routeJson: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun routeJson(r: UnitInfoLayer.RouteRequest, u: Int) =
        "{\"route\":${q(r.route.name)},\"index\":${r.index},\"value\":${q(r.value)},\"unit\":$u}"

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(args: Array<String>) {
        @Suppress("UNCHECKED_CAST") val root =
            Json(java.nio.file.Files.readString(java.nio.file.Path.of(args[0]))).value() as Map<String, Any?>
        val result = (root["cases"] as List<*>).map { @Suppress("UNCHECKED_CAST") run(it as Map<String, Any?>) }
            .joinToString(prefix = "[", postfix = "]"); java.nio.file.Files.writeString(
            java.nio.file.Path.of(args[1]),
            result
        ); println(result)
    }

    /** run: 검증 시나리오 입력을 적용하고 추적 결과를 반환한다. */
    @Suppress("UNCHECKED_CAST")
    private fun run(c: Map<String, Any?>): String {
        val flag = c["flag"] as Int
        val edit = c["editEnabled"] as Boolean
        val feats = c["featsEnabled"] as Boolean
        val units = (c["units"] as List<Map<String, Any?>>).mapIndexed { id, x ->
            val equipment = ((x["equipment"] as? List<*>) ?: emptyList<Any?>()).let { raw ->
                List(3) { i ->
                    raw.getOrNull(i)?.let { UnitInfoLayer.Equipment(it as String) }
                }
            }
            UnitInfoLayer.Unit(
                id,
                "U$id",
                "보병",
                1,
                10,
                20,
                3,
                5,
                1,
                2,
                3,
                4,
                5,
                (x["magic"] as? List<String>) ?: emptyList(),
                mine = (x["mine"] as? Boolean) ?: true,
                equipment = equipment
            )
        }
        val l = UnitInfoLayer(units, flag, edit, c["defaultTab"] as Int, feats); l.onCreate(c["index"] as Int)
        val trace = mutableListOf<String>()


        /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
        fun snap(step: String) {
            val v = l.ref()
            val routes = l.takeRoutes().joinToString(prefix = "[", postfix = "]") {
                routeJson(
                    it,
                    v.unit.id
                )
            }; trace += "{\"step\":${q(step)},\"index\":${v.index},\"selected\":${v.tab},\"attached\":${v.attached},\"panels\":${v.panels},\"tabs\":${v.interactable},\"active\":${
                (0 until if (edit) 11 else 9).map {
                    buttonActive(
                        l,
                        it,
                        flag,
                        edit,
                        feats
                    )
                }
            },\"routes\":$routes}"
        }


        /** fire: 검증 입력을 처리하고 관련 상태를 갱신한다. */
        fun fire(raw: String) {
            val (a, b) = raw.split(':')
            val ev = b.toInt(); when {
                a == "Panel_cancel" -> l.onCancel(ev); a.startsWith("equip") -> if (l.ref().attached) l.onEquipment(
                    a.removePrefix(
                        "equip"
                    ).toInt(), ev
                ); a.startsWith("magic") -> if (l.ref().attached) l.onMagic(
                    a.removePrefix("magic").toInt(),
                    ev
                ); a.startsWith("button") -> {
                    val n = a.removePrefix("button").toInt(); if (buttonActive(l, n, flag, edit, feats)) l.onButton(
                        n,
                        ev
                    )
                }
            }; snap("$a-$ev")
        }
        snap("create"); (c["events"] as List<String>).forEach(::fire); return "{\"case\":${q(c["id"] as String)},\"trace\":[${trace.joinToString()}]}"
    }

    /** buttonActive: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun buttonActive(l: UnitInfoLayer, n: Int, flag: Int, edit: Boolean, feats: Boolean) = when (n) {
        in 0..7 -> true; 8 -> feats && l.ref().unit.mine; 9 -> edit && (flag and UnitInfoLayer.BATTLE_FLAG) != 0; 10 -> edit; else -> false
    }
}
