// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.shared.overlay.CmdLayer

import java.nio.file.Files
import java.nio.file.Path

/** CmdLayerTraceHarness: 복원된 CmdLayer 기능과 저장 부수효과 픽스처의 Kotlin 실행부이다. */
object CmdLayerTraceHarness {
    /** Case: case 관련 검증 상태와 동작을 제공하는 타입이다. */
    private data class Case(
        /** name: 이름을 담는다. */
        val name: String,
        /** rFlag: r flag 값을 보관해 검증 흐름에서 사용한다. */
        val rFlag: Int,
        /** eFlag: e flag 값을 보관해 검증 흐름에서 사용한다. */
        val eFlag: Int,
        /** deviceId: device id 값을 보관해 검증 흐름에서 사용한다. */
        val deviceId: String,
        /** units: 무장 수를 담는다. */
        val units: Int,
        /** inventory: 보유 아이템 목록을 담는다. */
        val inventory: List<CmdLayer.Item>,
        /** events: 검증 이벤트 또는 추적 결과를 담는다. */
        val events: List<String>
    )

    /** block: JSON 입력에서 지정한 필드 블록을 추출한다. */
    private fun block(s: String, at: Int): String {
        val open = s[at]
        val close = if (open == '{') '}' else ']'
        var depth = 0
        var quote = false
        var escaped = false; for (i in at until s.length) {
            val c = s[i]; if (quote) {
                if (escaped) escaped = false else if (c == '\\') escaped = true else if (c == '"') quote = false
            } else if (c == '"') quote =
                true else if (c == open) depth++ else if (c == close && --depth == 0) return s.substring(at, i + 1)
        }; error("unclosed")
    }

    /** objs: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun objs(a: String): List<String> {
        val result = mutableListOf<String>()
        var i = 0; while (i < a.length) {
            if (a[i] == '{') {
                val x = block(a, i); result += x; i += x.length
            } else i++
        }; return result
    }

    /** str: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun str(s: String, key: String) =
        Regex("\\\"$key\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"").find(s)!!.groupValues[1].replace("\\\"", "\"")
            .replace("\\n", "\n").replace("\\\\", "\\")

    /** int: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun int(s: String, key: String) = Regex("\\\"$key\\\"\\s*:\\s*(-?\\d+)").find(s)!!.groupValues[1].toInt()
    /** field: JSON 입력에서 지정한 필드 블록을 추출한다. */
    private fun field(s: String, key: String): String {
        val p = s.indexOf("\"$key\"")
        val a = s.indexOfAny(charArrayOf('[', '{'), p); return block(s, a)
    }

    /** parse: 외부 입력을 검증용 값으로 변환한다. */
    private fun parse(raw: String): List<Case> = objs(field(raw, "cases")).map { o ->
        val inv = objs(field(o, "inventory")).map {
            CmdLayer.Item(
                int(it, "id"),
                Regex("\\\"treasure\\\"\\s*:\\s*true").containsMatchIn(it),
                Regex("\\\"property\\\"\\s*:\\s*true").containsMatchIn(it)
            )
        }
        val events =
            Regex("\\\"((?:\\\\.|[^\\\"])*)\\\"").findAll(field(o, "events")).map { it.groupValues[1] }.toList()
        Case(
            str(o, "name"),
            int(o, "rFlag"),
            int(o, "eFlag"),
            str(o, "deviceId"),
            objs(field(o, "units")).size,
            inv,
            events
        )
    }

    /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun q(s: String) = buildString {
        append('"'); s.forEach {
        when (it) {
            '\\' -> append("\\\\"); '"' -> append("\\\""); '\n' -> append("\\n"); else -> append(it)
        }
    }; append('"')
    }

    /** any: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun any(x: Any?): String = when (x) {
        null -> "null"; is String -> q(x); is Boolean, is Int, is Long -> x.toString(); is Double -> if (x % 1.0 == 0.0) x.toInt()
            .toString() else x.toString(); is List<*> -> x.joinToString(
            ",",
            "[",
            "]"
        ) { any(it) }; is Map<*, *> -> x.entries.joinToString(
            ",",
            "{",
            "}"
        ) { q(it.key.toString()) + ":" + any(it.value) }; else -> q(x.toString())
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
        fun snap(l: CmdLayer, step: String): String {
            val fields = linkedMapOf<String, Any?>(
                "step" to step,
                "eFlag" to l.eFlag,
                "rFlag" to l.rFlag,
                "sFlag" to l.sFlag,
                "label" to l.label,
                "selected" to l.selected,
                "checked" to l.checked,
                "buttons" to listOf(true, true, true, true, true),
                "toasts" to l.toasts,
                "writes" to l.writes,
                "props" to l.props,
                "weapons" to l.weapons,
                "urls" to l.urls,
                "dispatch" to l.dispatch.map { listOf(it[0], it[1]) },
                "layers" to l.layers.map {
                    linkedMapOf(
                        "layer" to it.layer,
                        "args" to linkedMapOf("flag" to it.flag, "txt" to it.txt)
                    )
                },
                "events" to l.events,
                "restart" to l.restart
            )
            return any(fields)
        }

        /**
         * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val result = linkedMapOf<String, String>()
        parse(Files.readString(Path.of(args[0]))).forEach { c ->
            /**
             * `l` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val l = CmdLayer(c.rFlag, c.eFlag, c.deviceId, c.units, c.inventory); l.onCreate()
            /**
             * `trace` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val trace = mutableListOf(snap(l, "create"))
            c.events.forEach { e ->
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = e.split(':'); when (p[0]) {
                "item" -> l.item(p[1].toInt(), p[2].toInt()); "button" -> l.button(
                    p[1].toInt(),
                    p[2].toInt()
                ); "prompt", "cancelPrompt" -> l.answer(p[1].toInt())
            }; trace += snap(l, e)
            }
            result[c.name] = trace.joinToString(",", "[", "]")
        }
        /**
         * `output` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val output = result.entries.joinToString(",", "{", "}") { q(it.key) + ":" + it.value }; Files.createDirectories(
            Path.of(args[1]).parent
        ); Files.writeString(Path.of(args[1]), output); println(output)
    }
}
