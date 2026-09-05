package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** Kotlin half of the common end-flow fixture.  Trace keys deliberately match the JS factory harness. */
object EndFlowTraceHarness {
    private data class Case(val name: String, val kind: String, val info: String, val events: List<String>)

    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    private fun block(s: String, at: Int): String {
        var n = 0
        var q = false
        var e = false; for (i in at until s.length) {
            val c = s[i]; if (q) {
                if (e) e = false else if (c == '\\') e = true else if (c == '\"') q = false
            } else if (c == '\"') q = true else if (c == '{') n++ else if (c == '}' && --n == 0) return s.substring(
                at,
                i + 1
            )
        }; error("unclosed")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val raw = Files.readString(Path.of(args[0]))
        val cases = Regex("\\{\\\"name\\\"").findAll(raw).map { m ->
            val o = block(raw, m.range.first); Case(
            Regex("\"name\":\"([^\"]+)\"").find(o)!!.groupValues[1],
            Regex("\"kind\":\"([^\"]+)\"").find(o)!!.groupValues[1],
            Regex("\"info\":\"((?:\\\\.|[^\"])*)\"").find(o)?.groupValues?.get(1)?.replace("\\\\n", "\\n") ?: "",
            Regex("\"events\":\\[(.*?)\\]").find(o)!!.groupValues[1].let {
                Regex("\"([^\"]+)\"").findAll(it).map { x -> x.groupValues[1] }.toList()
            })
        }.toList()

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
            val bg = mutableListOf<String>()
            var label = ""
            var top = 0
            var removed = 0
            var callbacks = 0
            var panel: Boolean? = null
            var button: Boolean? = null
            var z = 0
            val sounds = mutableListOf<String>()
            val scheduled = mutableListOf<Int>()
            val helpers = mutableListOf<String>()
            val layers = mutableListOf<String>()
            val dispatches = mutableListOf<String>()
            val scenes = mutableListOf<String>()
            val cmds = mutableListOf<String>()
            var pending: ((Int) -> Unit)? = null
            val win = if (c.kind == "wincon") WinConBoxLayer() else null

            /**
             * 공개 메서드 `msg`
             *
             * ### 파라미터
            - `t` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `fn` (`(Int`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun msg(t: String, fn: (Int) -> Unit) {
                layers += "MsgBox"; pending = fn
            }

            val lose = if (c.kind == "lose") LossFlow(object : LossFlow.Sink {
                override fun sound(id: Int) {
                    sounds += "LOSE"
                }

                override fun schedule(seconds: Int, block: () -> Unit) {
                    scheduled += seconds; scheduledBlocks[seconds] = block
                }

                override fun helper(cmd: String) {
                    helpers += cmd
                }

                override fun msgBox(text: String, reply: (Int) -> Unit) = msg(text, reply)
                override fun login() {
                    scenes += "LOGIN"
                }

                override fun endGame() {
                    cmds += "END_GAME"
                }
            }) else null
            val end = if (c.kind == "end") TerminalFlow { scenes += "LOGIN" } else null
            val skip = if (c.kind == "skip") StorySkipFlow(object : StorySkipFlow.Sink {
                override fun msgBox(text: String, reply: (Int) -> Unit) = msg(text, reply)
                override fun dispatch(name: String) {
                    dispatches += name
                }
            }) else null
            scheduledBlocks.clear(); when (c.kind) {
                "wincon" -> {
                    win!!.onCreate(WinConBoxLayer.CreateData(c.info) { callbacks++ }); bg += "bg0"; label =
                        c.info; top = 1
                }; "lose" -> lose!!.onCreate(); "end" -> end!!.onCreate(); "skip" -> skip!!.onCreate()
            }
            /**
             * 공개 메서드 `js`
             *
             * ### 파라미터
            - `a` (`List<String>`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `String`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun js(a: List<String>): String = a.joinToString(",", "[", "]") { "\"${esc(it)}\"" }

            /**
             * 공개 메서드 `snap`
             *
             * ### 파라미터
            - `step` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `String`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun snap(step: String): String {
                if (skip != null) {
                    panel = skip.panel; button = skip.button; z = skip.zIndex
                }
                val attached = if (win != null) win.view().attached else removed == 0
                if (win != null) { /* source reports each direct removeFromParent invocation */
                }
                val helperJson = helpers.joinToString(",", "[", "]") { "{\"cmd\":\"${esc(it)}\"}" }
                return "{\"step\":\"${esc(step)}\",\"kind\":\"${c.kind}\",\"backgrounds\":${js(bg)},\"label\":\"${
                    esc(
                        label
                    )
                }\",\"scrollTop\":$top,\"attached\":$attached,\"removed\":$removed,\"callbacks\":$callbacks,\"panel\":${panel ?: "null"},\"button\":${button ?: "null"},\"z\":$z,\"sounds\":${
                    js(
                        sounds
                    )
                },\"scheduled\":${
                    scheduled.joinToString(
                        ",",
                        "[",
                        "]"
                    )
                },\"helpers\":$helperJson,\"layers\":${js(layers)},\"dispatches\":${js(dispatches)},\"scenes\":${
                    js(
                        scenes
                    )
                },\"cmds\":${js(cmds)}}"
            }

            val out = mutableListOf(snap("create"))
            for (ev in c.events) {
                val p = ev.split(':')
                when (p[0]) {
                    "button" -> if (win != null) {
                        win.onButtonTouch(p[1].toInt()); if (p[1].toInt() == WinConBoxLayer.TOUCH_END) removed++
                    } else skip!!.touch(p[1].toInt())

                    "time" -> {
                        val n = p[1].toInt(); scheduledBlocks.filter { it.key <= n }.toMap()
                            .forEach { it.value() }; scheduledBlocks.keys.removeIf { it <= n }; scheduled.removeIf { it <= n }
                    }

                    "msg" -> pending?.invoke(p[1].toInt())
                    "event" -> end!!.onEvent(p[1].toInt())
                    "swap" -> skip!!.swap()
                }
                out += snap(ev)
            }
            return out.joinToString(",", "[", "]")
        }

        val out = cases.joinToString(",", "{", "}") { "\"${it.name}\":${run(it)}" }; Files.createDirectories(
            Path.of(
                args[1]
            ).parent
        ); Files.writeString(Path.of(args[1]), out); println(out)
    }

    private val scheduledBlocks = linkedMapOf<Int, () -> Unit>()
}
