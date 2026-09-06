// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** ChoiceCommandTraceHarness: ChooseLayer·Choose2Layer·CommandLayer 공용 픽스처의 Kotlin 실행부이다. */
object ChoiceCommandTraceHarness {
    /** Case: case 관련 검증 상태와 동작을 제공하는 타입이다. */
    private data class Case(
        /** name: 이름을 담는다. */
        val name: String,
        /** kind: 종류를 담는다. */
        val kind: String,
        /** info: 설명 정보를 담는다. */
        val info: String,
        /** replace: 문자열 치환 규칙을 담는다. */
        val replace: List<Pair<String, String>>,
        /** face: 얼굴 리소스 식별자를 담는다. */
        val face: Int,
        /** mask: 표시 마스크 값을 담는다. */
        val mask: Int,
        /** events: 검증 이벤트 또는 추적 결과를 담는다. */
        val events: List<String>
    )

    /** esc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")

    /** unesc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun unesc(s: String): String {
        val out = StringBuilder()
        var i = 0; while (i < s.length) {
            if (s[i] == '\\' && i + 1 < s.length) {
                when (s[i + 1]) {
                    'n' -> out.append('\n'); '"' -> out.append('"'); '\\' -> out.append('\\'); else -> {
                    out.append(s[i + 1])
                }
                }; i += 2
            } else {
                out.append(s[i]); i++
            }
        }; return out.toString()
    }

    /** balanced: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun balanced(s: String, at: Int): String {
        val open = s[at]
        val close = if (open == '{') '}' else ']'
        var d = 0
        var q = false
        var e = false; for (i in at until s.length) {
            val c = s[i]; if (q) {
                if (e) e = false else if (c == '\\') e = true else if (c == '\"') q = false
            } else if (c == '\"') q = true else if (c == open) d++ else if (c == close && --d == 0) return s.substring(
                at,
                i + 1
            )
        }; error("unclosed")
    }

    /** objects: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun objects(array: String): List<String> {
        val r = mutableListOf<String>()
        var i = 0; while (i < array.length) {
            if (array[i] == '{') {
                val x = balanced(array, i); r += x; i += x.length
            } else i++
        }; return r
    }

    /** string: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun string(obj: String, key: String): String {
        val m = Regex("\\\"$key\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"").find(obj)
            ?: error(key); return unesc(m.groupValues[1])
    }

    /** int: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun int(obj: String, key: String) =
        Regex("\\\"$key\\\"\\s*:\\s*(-?\\d+)").find(obj)!!.groupValues[1].toInt()

    /** block: JSON 입력에서 지정한 필드 블록을 추출한다. */
    private fun block(obj: String, key: String): String {
        val at = obj.indexOf("\"$key\"")
        val p = obj.indexOfAny(charArrayOf('{', '['), at); return balanced(obj, p)
    }

    /** parse: 외부 입력을 검증용 값으로 변환한다. */
    private fun parse(raw: String): List<Case> = objects(block(raw, "cases")).map { obj ->
        val replace =
            Regex("\\\"((?:\\\\.|[^\\\"])*)\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"").findAll(block(obj, "replace"))
                .map { unesc(it.groupValues[1]) to unesc(it.groupValues[2]) }.toList()
        val events =
            Regex("\\\"((?:\\\\.|[^\\\"])*)\\\"").findAll(block(obj, "events")).map { unesc(it.groupValues[1]) }
                .toList()
        Case(
            string(obj, "name"),
            string(obj, "kind"),
            string(obj, "info"),
            replace,
            int(obj, "face"),
            int(obj, "mask"),
            events
        )
    }

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    fun main(args: Array<String>) {
        /** run: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
        fun run(c: Case): String {
            val calls = mutableListOf<Int>()
            var removals = 0
            val choice = if (c.kind == "command") null else ChoiceLayer(c.kind == "choose2")
            val command = if (c.kind == "command") CommandLayer() else null
            val replaced = c.replace.fold(c.info) { a, (from, to) -> a.replace(from, to) }
            if (choice != null) choice.onCreate(
                replaced,
                c.face
            ) { calls += it; removals++ } else command!!.onCreate(c.mask) { calls += it; removals++ }
            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
            fun snap(step: String): String {
                val rows = if (choice != null) choice.rows().joinToString(
                    ",",
                    "[",
                    "]"
                ) { "[${it.tag},\"${esc(it.text)}\",${it.listenerPriority}]" } else command!!.buttons().joinToString(
                    ",",
                    "[",
                    "]"
                ) { "[${it.tag},${it.interactable},${it.priority},${it.tag < 5 && !it.interactable}]" }
                val face = if (c.kind == "choose") {
                    val f =
                        choice!!.requestedFace; "{\"request\":${if (f == null) "[]" else "[\"head/$f\"]"},\"size\":${if (f == null) "[0,0]" else "[60,120]"}}"
                } else "null"
                val attached = choice?.attached() ?: command!!.attached()
                val callsJson = calls.joinToString(",", "[", "]")
                return "{\"step\":\"${esc(step)}\",\"zIndex\":${choice?.zIndex ?: 0},\"rows\":$rows,\"face\":$face,\"attached\":$attached,\"removeCount\":$removals,\"calls\":$callsJson,\"cancelPriority\":${if (command == null) "null" else "2"},\"keyboardBindings\":0}"
            }

            val trace = mutableListOf(snap("create")); c.events.forEach { event ->
                val p = event.split(':'); when (p[0]) {
                "row" -> choice!!.onRowTouch(
                    p[1].toInt(),
                    p[2].toInt()
                ); "button" -> command!!.onButtonTouch(p[1].toInt(), p[2].toInt()); "cancel" -> command!!.onCancelTouch(
                    p[1].toInt()
                )
            }; trace += snap(event)
            }
            return trace.joinToString(",", "[", "]")
        }

        /** result: 검증 실행 결과를 담는다. */
        val result = parse(Files.readString(Path.of(args[0]))).joinToString(
            ",",
            "{",
            "}"
        ) { "\"${esc(it.name)}\":${run(it)}" }; Files.createDirectories(Path.of(args[1]).parent); Files.writeString(
            Path.of(
                args[1]
            ), result
        ); println(result)
    }
}
