package com.jojo.game.verification

import com.jojo.game.*

/** Kotlin side of tools/unit_info_source_trace_harness.js. Both consume one fixture. */
object UnitInfoTraceHarness {
    private class Json(private val s: String) {
        var p = 0
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

        fun ws() {
            while (p < s.length && s[p].isWhitespace()) p++
        }

        fun str(): String {
            p++
            val a = p; while (s[p] != '"') p++; return s.substring(a, p++)
        }

        fun obj(): Map<String, Any?> {
            p++
            val r = linkedMapOf<String, Any?>(); ws(); while (s[p] != '}') {
                val k = str(); ws(); p++; r[k] = value(); ws(); if (s[p] == ',') {
                    p++; ws()
                }
            }; p++; return r
        }

        fun arr(): List<Any?> {
            p++
            val r = mutableListOf<Any?>(); ws(); while (s[p] != ']') {
                r += value(); ws(); if (s[p] == ',') {
                    p++; ws()
                }
            }; p++; return r
        }

        fun num(): Int {
            val a = p; while (p < s.length && (s[p].isDigit() || s[p] == '-')) p++; return s.substring(a, p).toInt()
        }
    }

    private fun q(x: String) =
        buildString { append('"'); x.forEach { if (it == '"' || it == '\\') append('\\'); append(it) }; append('"') }

    private fun routeJson(r: UnitInfoLayer.RouteRequest, u: Int) =
        "{\"route\":${q(r.route.name)},\"index\":${r.index},\"value\":${q(r.value)},\"unit\":$u}"

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

    private fun buttonActive(l: UnitInfoLayer, n: Int, flag: Int, edit: Boolean, feats: Boolean) = when (n) {
        in 0..7 -> true; 8 -> feats && l.ref().unit.mine; 9 -> edit && (flag and UnitInfoLayer.BATTLE_FLAG) != 0; 10 -> edit; else -> false
    }
}
