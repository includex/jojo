package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** Canonical trace for MsgBox{,2,3,4}, Toast, Progress and Loading games. */
object SystemUiTraceHarness {
    private data class Case(val name: String, val kind: String, val data: String, val events: List<String>)

    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    private fun balanced(s: String, start: Int): String {
        val open = s[start]
        val close = if (open == '{') '}' else ']'
        var d = 0
        var q = false
        var e = false; for (i in start until s.length) {
            val c = s[i]; if (q) {
                if (e) e = false else if (c == '\\') e = true else if (c == '\"') q = false
            } else if (c == '\"') q = true else if (c == open) d++ else if (c == close && --d == 0) return s.substring(
                start,
                i + 1
            )
        }; error("unclosed fixture")
    }

    private fun cases(raw: String): List<Case> {
        val root = raw.indexOf("\"cases\"")
        val arr = balanced(raw, raw.indexOf('[', root))
        val out = mutableListOf<Case>()
        var p = 0
        while (p < arr.length) {
            if (arr[p] == '{') {
                val o = balanced(arr, p)

                /**
                 * 공개 메서드 `field`
                 *
                 * ### 파라미터
                - `key` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `String`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun field(key: String): String =
                    Regex("\\\"$key\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"").find(o)?.groupValues?.get(1) ?: ""

                val dataAt = o.indexOf("\"data\"")
                val data = if (dataAt < 0) "{}" else balanced(o, o.indexOf('{', dataAt))
                val eventAt = o.indexOf("\"events\"")
                val events = if (eventAt < 0) emptyList() else Regex("\\\"([^\\\"]+)\\\"").findAll(
                    balanced(
                        o,
                        o.indexOf('[', eventAt)
                    )
                ).map { it.groupValues[1] }.toList()
                out += Case(field("name"), field("kind"), data, events); p += o.length
            } else p++
        }
        return out
    }

    private fun str(data: String, key: String, default: String = "") =
        Regex("\\\"$key\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"").find(data)?.groupValues?.get(1) ?: default

    private fun int(data: String, key: String, default: Int = 0) =
        Regex("\\\"$key\\\"\\s*:\\s*(\\d+)").find(data)?.groupValues?.get(1)?.toInt() ?: default

    private fun texts(data: String) = Regex("\\\"texts\\\"\\s*:\\s*\\[([^]]*)]").find(data)?.groupValues?.get(1)
        ?.let { Regex("\\\"([^\\\"]*)\\\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    private fun bool(v: Boolean) = if (v) "true" else "false"
    private fun nullable(v: Any?) = v?.toString() ?: "null"
    @JvmStatic
    fun main(args: Array<String>) {
        /**
         * 공개 메서드 `run`
         *
         * ### 파라미터
        - `c` (`Case`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun run(c: Case): String {
            val calls = mutableListOf<Int>()
            var attached = true
            var labels = emptyList<String>()
            var visible = listOf<String>()
            var panel: String = "null"
            var n: String = "null"
            var checked: String = "null"
            var items: List<String> = mutableListOf()
            var eventLog: List<String> = emptyList()
            var spinner = false
            var image: String = "null"
            var opacity: String = "null"
            var scheduled = emptyList<Int>()
            var slotLabels = mutableListOf<String>()
            val d = c.data
            val msg = if (c.kind == "msg") DialogState { calls += it } else null
            val msg2 = if (c.kind == "msg2") ChoiceDialogState { calls += it } else null
            val msg3 = if (c.kind == "msg3") QuantityDialogState { calls += it } else null
            val msg4 = if (c.kind == "msg4") ToggleDialogState({ calls += it }) else null
            val toast = if (c.kind == "toast") ToastQueue() else null
            val progress = if (c.kind == "progress") ProgressState() else null
            val loading = if (c.kind == "loading") LoadingState() else null
            when (c.kind) {
                "msg" -> {
                    msg!!.create(str(d, "txt"), int(d, "flag", 7)); labels = listOf(msg.label); visible =
                        msg.visible.map { bool(it) }; panel = if (msg.panel) "1" else "null"
                }

                "msg2" -> visible = listOf("true", "true", "null")
                "msg3" -> {
                    val count = int(d, "count"); labels =
                        listOf(str(d, "lab0"), str(d, "txt").replace("{0}", count.toString())); visible =
                        listOf("true", "true", "null"); n = "1"
                }

                "msg4" -> {
                    msg4!!.create(int(d, "flag", 7)); visible = msg4.visible.map { bool(it) }; checked =
                        bool(msg4.checked)
                }

                "toast" -> {
                    toast!!.create(""); for (t in texts(d)) {
                        if (t.isNotEmpty()) {
                            if (slotLabels.size == 5) slotLabels[0] = t else slotLabels += t
                        }; toast.create(t)
                    }; labels = slotLabels.toList(); items = toast.items; visible = listOf("null", "null", "null")
                }

                "progress" -> {
                    progress!!.create(); labels = listOf(""); spinner = true; visible = listOf("null", "null", "null")
                }

                "loading" -> {
                    loading!!.create(int(d, "flag")); image = bool(loading.image); opacity =
                        nullable(loading.panelOpacity); spinner = false; visible =
                        listOf("null", "null", "null"); scheduled = loading.delay?.let { listOf(it) } ?: emptyList()
                }
            }
            /**
             * 공개 메서드 `snapshot`
             *
             * ### 파라미터
            - `step` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `String`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun snapshot(step: String): String {
                attached = when (c.kind) {
                    "msg" -> msg!!.attached; "msg2" -> msg2!!.attached; "msg3" -> msg3!!.attached; "msg4" -> msg4!!.attached; "toast" -> toast!!.attached; else -> true
                }
                if (c.kind == "msg3") {
                    n = msg3!!.n.toString(); eventLog = msg3.events.map { "[\"${it[0]}\",${it[1]}]" }
                }
                if (c.kind == "msg4") checked = bool(msg4!!.checked)
                if (c.kind == "toast") {
                    items = toast!!.items; labels = slotLabels.toList()
                }
                if (c.kind == "progress") labels = listOf(progress!!.label)
                if (c.kind == "loading") {
                    image = bool(loading!!.image); opacity = nullable(loading.panelOpacity); spinner = false
                }
                return "{\"step\":\"${esc(step)}\",\"kind\":\"${c.kind}\",\"attached\":${bool(attached)},\"calls\":[${
                    calls.joinToString(
                        ","
                    )
                }],\"labels\":[${labels.joinToString(",") { "\"${esc(it)}\"" }}],\"visible\":[${visible.joinToString(",")}],\"panel\":$panel,\"n\":$n,\"checked\":$checked,\"items\":[${
                    items.joinToString(
                        ","
                    ) { "\"${esc(it)}\"" }
                }],\"events\":[${eventLog.joinToString(",")}],\"spinner\":${bool(spinner)},\"image\":$image,\"opacity\":$opacity,\"scheduled\":[${
                    scheduled.joinToString(
                        ","
                    )
                }] }".replace("] }", "]}")
            }

            val out = mutableListOf(snapshot("create"))
            for (e in c.events) {
                val p = e.split(':'); when (p[0]) {
                    "button" -> when (c.kind) {
                        "msg" -> msg!!.touch(p[1].toInt(), p[2].toInt()); "msg2" -> msg2!!.touch(
                            p[1].toInt(),
                            p[2].toInt()
                        ); "msg3" -> msg3!!.touch(p[1].toInt(), p[2].toInt()); "msg4" -> msg4!!.touch(
                            p[1].toInt(),
                            p[2].toInt()
                        )
                    }; "panel" -> msg!!.touch(1, p[1].toInt()); "input" -> msg3!!.input(
                        p[1],
                        int(d, "count")
                    ); "toggle" -> msg4!!.checked =
                        p[1] == "1"; "progress" -> progress!!.set(p[1].toDouble()); "time" -> loading!!.time(p[1].toInt()); "expire" -> {
                        toast!!.expireAll(); items = emptyList()
                    }
                }; out += snapshot(e)
            }
            return out.joinToString(",", "[", "]")
        }

        val output =
            cases(Files.readString(Path.of(args[0]))).joinToString(",", "{", "}") { "\"${esc(it.name)}\":${run(it)}" }
        Files.createDirectories(Path.of(args[1]).parent); Files.writeString(Path.of(args[1]), output); println(output)
    }
}
