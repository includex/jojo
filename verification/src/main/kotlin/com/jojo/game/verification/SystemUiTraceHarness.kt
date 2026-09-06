// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.shared.overlay.ChoiceDialogState
import com.jojo.game.presentation.shared.overlay.DialogState
import com.jojo.game.presentation.shared.overlay.LoadingState
import com.jojo.game.presentation.shared.overlay.ProgressState
import com.jojo.game.presentation.shared.overlay.QuantityDialogState
import com.jojo.game.presentation.shared.overlay.ToastQueue
import com.jojo.game.presentation.shared.overlay.ToggleDialogState

import java.nio.file.Files
import java.nio.file.Path

/** SystemUiTraceHarness: MsgBox 계열과 Toast·Progress·Loading 화면의 기준 추적을 실행한다. */
object SystemUiTraceHarness {
    /** Case: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(val name: String, val kind: String, val data: String, val events: List<String>)

    /** esc: JSON 특수 문자를 이스케이프해 안전한 문자열을 만든다. */
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    /** balanced: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
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

    /** cases: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun cases(raw: String): List<Case> {
        val root = raw.indexOf("\"cases\"")
        val arr = balanced(raw, raw.indexOf('[', root))
        val out = mutableListOf<Case>()
        var p = 0
        while (p < arr.length) {
            if (arr[p] == '{') {
                val o = balanced(arr, p)


                /** field: 입력 데이터에서 지정한 블록을 추출한다. */
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

    /** str: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun str(data: String, key: String, default: String = "") =
        Regex("\\\"$key\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"").find(data)?.groupValues?.get(1) ?: default

    /** int: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
    private fun int(data: String, key: String, default: Int = 0) =
        Regex("\\\"$key\\\"\\s*:\\s*(\\d+)").find(data)?.groupValues?.get(1)?.toInt() ?: default

    /** texts: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun texts(data: String) = Regex("\\\"texts\\\"\\s*:\\s*\\[([^]]*)]").find(data)?.groupValues?.get(1)
        ?.let { Regex("\\\"([^\\\"]*)\\\"").findAll(it).map { m -> m.groupValues[1] }.toList() } ?: emptyList()

    /** bool: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
    private fun bool(v: Boolean) = if (v) "true" else "false"
    /** nullable: JSON 입력의 지정된 값 형식을 읽어 반환한다. */
    private fun nullable(v: Any?) = v?.toString() ?: "null"
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {

        /** run: 검증 시나리오 입력을 적용하고 추적 결과를 반환한다. */
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

            /** snapshot: 현재 추적 상태를 스냅샷으로 만든다. */
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
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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
