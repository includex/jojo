// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.presentation.scenario.hall.*

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** HallUiTraceHarness: 공용 HallMenuLayer·HallCommandLayer 팩토리 픽스처의 Kotlin 실행부이다. */
object HallUiTraceHarness {
    /** Case: case 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(
        /** id: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val id: String,
        /** kind: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val kind: String,
        /** edit: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val edit: Boolean,
        /** eventName: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val eventName: String,
        /** stageName: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val stageName: String,
        /** ambition: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val ambition: Pair<Int, Int>?,
        /** flag: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val flag: Int,
        /** events: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        val events: List<String>
    )

    /** esc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    /** unesc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun unesc(s: String): String {
        val out = StringBuilder()
        var i = 0; while (i < s.length) {
            if (s[i] == '\\' && i + 1 < s.length) {
                when (s[++i]) {
                    'n' -> out.append('\n'); else -> out.append(s[i])
                }; i++
            } else out.append(s[i++])
        }; return out.toString()
    }

    /** balanced: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun balanced(s: String, at: Int): String {
        val open = s[at]
        val close = if (open == '{') '}' else ']'
        var depth = 0
        var quote = false
        var escaped = false; for (i in at until s.length) {
            val c = s[i]; if (quote) {
                if (escaped) escaped = false else if (c == '\\') escaped = true else if (c == '\"') quote = false
            } else if (c == '\"') quote =
                true else if (c == open) depth++ else if (c == close && --depth == 0) return s.substring(at, i + 1)
        }; error("unclosed JSON")
    }

    /** objects: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun objects(s: String): List<String> {
        val out = mutableListOf<String>()
        var i = 0; while (i < s.length) {
            if (s[i] == '{') {
                val o = balanced(s, i); out += o; i += o.length
            } else i++
        }; return out
    }

    /** str: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun str(o: String, key: String, default: String = ""): String {
        val m = Regex("\\\"$key\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"").find(o)
            ?: return default; return unesc(m.groupValues[1])
    }

    /** int: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun int(o: String, key: String, default: Int = 0) =
        Regex("\\\"$key\\\"\\s*:\\s*(-?\\d+)").find(o)?.groupValues?.get(1)?.toInt() ?: default

    /** bool: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun bool(o: String, key: String) = Regex("\\\"$key\\\"\\s*:\\s*true").containsMatchIn(o)
    /** block: 입력 데이터에서 지정한 블록을 추출한다. */
    private fun block(o: String, key: String): String? {
        val p = o.indexOf("\"$key\""); if (p < 0) return null
        var q = o.indexOf(':', p) + 1; while (q < o.length && o[q].isWhitespace()) q++
        return if (q < o.length && (o[q] == '{' || o[q] == '[')) balanced(o, q) else null
    }

    /** parse: 외부 입력을 검증용 값으로 변환한다. */
    private fun parse(raw: String): List<Case> = objects(block(raw, "cases")!!).map { o ->
        val ambition = block(o, "ambition")?.takeIf { it.startsWith("[") }
            ?.let { Regex("-?\\d+").findAll(it).map { m -> m.value.toInt() }.toList() }?.let { it[0] to it[1] }
        val events = block(o, "events")?.let {
            Regex("\\\"((?:\\\\.|[^\\\"])*)\\\"").findAll(it).map { m -> unesc(m.groupValues[1]) }.toList()
        } ?: emptyList()
        Case(
            str(o, "id"),
            str(o, "kind"),
            bool(o, "edit"),
            str(o, "eventName"),
            str(o, "stageName"),
            ambition,
            int(o, "flag"),
            events
        )
    }

    /** jsonList: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun jsonList(items: List<String>) = items.joinToString(",", "[", "]")
    /** menuSnap: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun menuSnap(x: HallMenuFlow, step: String): String {
        val buttons = jsonList((0..9).map { "[${x.active[it]},${x.tags[it]},${x.listeners[it]}]" })
        val labels = jsonList(x.labels.map { "\"${esc(it)}\"" })
        val routes = jsonList(x.routes.map { "{\"layer\":\"${esc(it.layer)}\",\"payload\":${it.payload ?: "null"}}" })
        val frames =
            "{\"outer\":${x.outerFrame?.let { "\"$it\"" } ?: "null"},\"inner\":${x.innerFrame?.let { "\"$it\"" } ?: "null"},\"startWidth\":${x.startWidth},\"endWidth\":${x.endWidth},\"flagActions\":${
                jsonList(x.flagActions.map { it.toString() })
            }}"
        return "{\"step\":\"${esc(step)}\",\"zIndex\":${x.zIndex},\"labels\":$labels,\"buttons\":$buttons,\"bar\":$frames,\"attached\":${x.attached},\"layers\":$routes,\"callbackCount\":${x.callbackCount},\"cancelPriority\":${x.cancelPriority ?: "null"},\"toasts\":[]}"
    }

    /** commandSnap: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun commandSnap(x: HallCommandFlow, step: String): String {

        /** bools: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun bools(v: List<Boolean>) = jsonList(v.map { it.toString() })


        /** ints: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun ints(v: List<Int>) = jsonList(v.map { it.toString() })


        /** nullableInts: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun nullableInts(v: List<Int?>) = jsonList(v.map { it?.toString() ?: "null" })
        return "{\"step\":\"${esc(step)}\",\"active\":${bools(x.active)},\"tags\":${ints(x.tags)},\"priorities\":${
            nullableInts(
                x.priorities
            )
        },\"listeners\":${bools(x.listeners)},\"events\":${ints(x.events)},\"callbackCount\":${x.callbackCount},\"attached\":${x.attached}}"
    }

    /** menu: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun menu(c: Case): String {
        val x = HallMenuFlow(c.edit); x.onCreate(c.eventName, c.stageName, c.ambition)
        val trace = mutableListOf(menuSnap(x, "create")); for (e in c.events) {
            val p = e.split(':'); when (p[0]) {
                "button" -> {
                    val id = p[1].toInt(); if (x.canDeliverButton(id)) x.button(id, p[2].toInt())
                }; "cancel" -> if (x.canDeliverCancel()) x.cancel(p[1].toInt()); "msgbox" -> x.msgBox(p[1].toInt())
            }; trace += menuSnap(x, e)
        }; return jsonList(trace)
    }

    /** command: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun command(c: Case): String {
        val x = HallCommandFlow(); x.onCreate(c.flag)
        val trace = mutableListOf(commandSnap(x, "create")); for (e in c.events) {
            val p = e.split(':'); if (p[0] == "menu") {
                if (x.canDeliverMenu()) x.menu(p[1].toInt())
            } else {
                val id = p[1].toInt(); if (x.canDeliverButton(id)) x.button(id, p[2].toInt())
            }; trace += commandSnap(x, e)
        }; return jsonList(trace)
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        val output = parse(Files.readString(Path.of(args[0]))).joinToString(
            ",",
            "{",
            "}"
        ) { "\"${esc(it.id)}\":${if (it.kind == "menu") menu(it) else command(it)}" }; Files.createDirectories(
            Path.of(
                args[1]
            ).parent
        ); Files.writeString(Path.of(args[1]), output); println(output)
    }
}
