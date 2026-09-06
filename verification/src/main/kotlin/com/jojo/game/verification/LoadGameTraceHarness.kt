// Verification
package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

/** LoadGameTraceHarness: 공용 LoadGameLayer 원본·게임 픽스처의 Kotlin 실행부이다. */
object LoadGameTraceHarness {
    /** J: j 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private class J(private val s: String) {
        var p = 0


        /** v: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun v(): Any {
            w(); return when (s[p]) {
                '{' -> o(); '[' -> a(); '"' -> q(); 't' -> {
                    p += 4; true
                }; 'f' -> {
                    p += 5; false
                }; else -> n()
            }
        }


        /** w: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun w() {
            while (p < s.length && s[p].isWhitespace()) p++
        }

        /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun q(): String {
            p++
            val x = p; while (s[p] != '"') p++; return s.substring(x, p++)
        }


        /** o: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun o(): Map<String, Any?> {
            p++
            val r = linkedMapOf<String, Any?>(); w(); while (s[p] != '}') {
                val k = q(); w(); p++; r[k] = v(); w(); if (s[p] == ',') {
                    p++; w()
                }
            }; p++; return r
        }


        /** a: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun a(): List<Any?> {
            p++
            val r = mutableListOf<Any?>(); w(); while (s[p] != ']') {
                r += v(); w(); if (s[p] == ',') {
                    p++; w()
                }
            }; p++; return r
        }


        /** n: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun n(): Int {
            val x = p; while (p < s.length && (s[p].isDigit() || s[p] == '-')) p++; return s.substring(x, p).toInt()
        }
    }

    /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun q(s: String) = "\"$s\""
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(a: Array<String>) {
        @Suppress("UNCHECKED_CAST") val f =
            J(java.nio.file.Files.readString(java.nio.file.Path.of(a[0]))).v() as Map<String, Any?>
        val out = (f["cases"] as List<Map<String, Any?>>).joinToString(
            prefix = "[",
            postfix = "]"
        ) { run(it) }; java.nio.file.Files.writeString(java.nio.file.Path.of(a[1]), out); println(out)
    }

    /** run: 검증 실행에 필요한 상태를 구성한다. */
    @Suppress("UNCHECKED_CAST")
    private fun run(c: Map<String, Any?>): String {
        val slots = (c["slots"] as List<Map<String, Any?>>).associateBy { it["index"] as Int }
        val events = mutableListOf<String>()
        val repo = object : LoadGameLayer.Repository {
            var page = c["savedPage"] as Int
            /** load: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun load(i: Int): String? {
                val x = slots[i] ?: return null; if (x["invalid"] != null) return "broken"
                val battle = x["battle"] as? Int
                    ?: 0; return "{\"time\":${x["time"]},\"name\":\"${x["name"]}\",\"model\":{\"version\":${x["version"] ?: 0},\"stage\":${x["stage"]}},\"battle\":$battle}"
            }

            /** savedPage: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun savedPage() = page
            /** savePage: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun savePage(p: Int) {
                page = p
            }

            /** featureEnabled: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun featureEnabled(n: String) = c["feature"] as Boolean
            /** versionCode: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun versionCode() = c["versionCode"] as Int
            /** restore: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun restore(i: Int, raw: String, r: LoadGameLayer.RestoreRoute): Boolean {
                events += "loadModel"; if (r == LoadGameLayer.RestoreRoute.HALL_AFTER_BATTLE) events += "incStage"; events += "scene:" + if (r == LoadGameLayer.RestoreRoute.BATTLE) "BATTLE" else "HALL"; return true
            }
        }
        val l = LoadGameLayer(repo); l.onCreate()
        val trace = mutableListOf<String>()


        /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
        fun snap(step: String) {
            val v = l.view()
            val rows = v.rows.joinToString(
                prefix = "[",
                postfix = "]"
            ) { "{\"index\":${it.index},\"number\":${q(it.number)},\"stage\":${q(it.stage)},\"name\":${q(it.name)}}" }
            val es = events.joinToString(
                prefix = "[",
                postfix = "]"
            ) { q(it) }; events.clear(); trace += "{\"step\":${q(step)},\"page\":${v.page},\"rows\":$rows,\"toggles\":${v.pageTogglesVisible},\"attached\":${v.attached},\"confirmation\":${v.confirmation?.index ?: "null"},\"events\":$es}"
        }


        /** fire: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun fire(raw: String) {
            val (t, e) = raw.split(':')
            val n = e.toInt(); when {
                t.startsWith("row") -> if (l.onRowTouch(
                        t.removePrefix("row").toInt(),
                        n
                    )
                ) events += "confirm:" + t.removePrefix("row"); t.startsWith("page") -> l.onPageTouch(
                    t.removePrefix("page").toInt(), n
                ); t == "confirm" -> l.onConfirm(n); t == "cancel" -> l.onCancel(n)
            }; snap("$t-$n")
        }
        snap("create"); (c["events"] as List<String>).forEach(::fire); return "{\"case\":${q(c["id"] as String)},\"trace\":[${trace.joinToString()}]}"
    }
}
