package com.jojo.game

/** Kotlin half of the shared LoadGameLayer source/game fixture. */
object LoadGameTraceHarness {
    private class J(private val s: String) {
        var p = 0

        /**
         * 공개 메서드 `v`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Any?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun v(): Any {
            w(); return when (s[p]) {
                '{' -> o(); '[' -> a(); '"' -> q(); 't' -> {
                    p += 4; true
                }; 'f' -> {
                    p += 5; false
                }; else -> n()
            }
        }

        /**
         * 공개 메서드 `w`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun w() {
            while (p < s.length && s[p].isWhitespace()) p++
        }

        fun q(): String {
            p++
            val x = p; while (s[p] != '"') p++; return s.substring(x, p++)
        }

        /**
         * 공개 메서드 `o`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Map<String,Any?>`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun o(): Map<String, Any?> {
            p++
            val r = linkedMapOf<String, Any?>(); w(); while (s[p] != '}') {
                val k = q(); w(); p++; r[k] = v(); w(); if (s[p] == ',') {
                    p++; w()
                }
            }; p++; return r
        }

        /**
         * 공개 메서드 `a`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `List<Any?>`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun a(): List<Any?> {
            p++
            val r = mutableListOf<Any?>(); w(); while (s[p] != ']') {
                r += v(); w(); if (s[p] == ',') {
                    p++; w()
                }
            }; p++; return r
        }

        /**
         * 공개 메서드 `n`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Int`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun n(): Int {
            val x = p; while (p < s.length && (s[p].isDigit() || s[p] == '-')) p++; return s.substring(x, p).toInt()
        }
    }

    private fun q(s: String) = "\"$s\""
    @JvmStatic
    fun main(a: Array<String>) {
        @Suppress("UNCHECKED_CAST") val f =
            J(java.nio.file.Files.readString(java.nio.file.Path.of(a[0]))).v() as Map<String, Any?>
        val out = (f["cases"] as List<Map<String, Any?>>).joinToString(
            prefix = "[",
            postfix = "]"
        ) { run(it) }; java.nio.file.Files.writeString(java.nio.file.Path.of(a[1]), out); println(out)
    }

    @Suppress("UNCHECKED_CAST")
    private fun run(c: Map<String, Any?>): String {
        val slots = (c["slots"] as List<Map<String, Any?>>).associateBy { it["index"] as Int }
        val events = mutableListOf<String>()
        val repo = object : LoadGameLayer.Repository {
            var page = c["savedPage"] as Int
            override fun load(i: Int): String? {
                val x = slots[i] ?: return null; if (x["invalid"] != null) return "broken"
                val battle = x["battle"] as? Int
                    ?: 0; return "{\"time\":${x["time"]},\"name\":\"${x["name"]}\",\"model\":{\"version\":${x["version"] ?: 0},\"stage\":${x["stage"]}},\"battle\":$battle}"
            }

            override fun savedPage() = page
            override fun savePage(p: Int) {
                page = p
            }

            override fun featureEnabled(n: String) = c["feature"] as Boolean
            override fun versionCode() = c["versionCode"] as Int
            override fun restore(i: Int, raw: String, r: LoadGameLayer.RestoreRoute): Boolean {
                events += "loadModel"; if (r == LoadGameLayer.RestoreRoute.HALL_AFTER_BATTLE) events += "incStage"; events += "scene:" + if (r == LoadGameLayer.RestoreRoute.BATTLE) "BATTLE" else "HALL"; return true
            }
        }
        val l = LoadGameLayer(repo); l.onCreate()
        val trace = mutableListOf<String>()

        /**
         * 공개 메서드 `snap`
         *
         * ### 파라미터
        - `step` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

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

        /**
         * 공개 메서드 `fire`
         *
         * ### 파라미터
        - `raw` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

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
