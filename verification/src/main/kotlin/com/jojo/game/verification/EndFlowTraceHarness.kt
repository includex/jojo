// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.battle.overlay.WinConBoxLayer

import com.jojo.game.*
import com.jojo.game.presentation.shared.LossFlow
import com.jojo.game.presentation.shared.StorySkipFlow
import com.jojo.game.presentation.shared.TerminalFlow

import java.nio.file.Files
import java.nio.file.Path

/** EndFlowTraceHarness: 공용 종료 흐름 픽스처의 Kotlin 실행부이며 JS 기준 실행기와 키를 맞춘다. */
object EndFlowTraceHarness {
    /** Case: case 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(val name: String, val kind: String, val info: String, val events: List<String>)

    /** esc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    /** block: 입력 데이터에서 지정한 블록을 추출한다. */
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

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

        /** run: 하나의 종료 흐름 사례를 실행해 상태 문자열을 만든다. */
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


            /** msg: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            fun msg(t: String, fn: (Int) -> Unit) {
                layers += "MsgBox"; pending = fn
            }

            /**
             * `lose` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val lose = if (c.kind == "lose") LossFlow(object : LossFlow.Sink {
                /** sound: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun sound(id: Int) {
                    sounds += "LOSE"
                }

                /** schedule: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun schedule(seconds: Int, block: () -> Unit) {
                    scheduled += seconds; scheduledBlocks[seconds] = block
                }

                /** helper: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun helper(cmd: String) {
                    helpers += cmd
                }

                /** msgBox: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun msgBox(text: String, reply: (Int) -> Unit) = msg(text, reply)
                /** login: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun login() {
                    scenes += "LOGIN"
                }

                /** endGame: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun endGame() {
                    cmds += "END_GAME"
                }
            }) else null
            /**
             * `end` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val end = if (c.kind == "end") TerminalFlow { scenes += "LOGIN" } else null
            /**
             * `skip` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val skip = if (c.kind == "skip") StorySkipFlow(object : StorySkipFlow.Sink {
                /** msgBox: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun msgBox(text: String, reply: (Int) -> Unit) = msg(text, reply)
                /** dispatch: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
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

            /** js: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            fun js(a: List<String>): String = a.joinToString(",", "[", "]") { "\"${esc(it)}\"" }


            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
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

            /**
             * `out` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val out = mutableListOf(snap("create"))
            for (ev in c.events) {
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = ev.split(':')
                when (p[0]) {
                    "button" -> if (win != null) {
                        win.onButtonTouch(p[1].toInt()); if (p[1].toInt() == WinConBoxLayer.TOUCH_END) removed++
                    } else skip!!.touch(p[1].toInt())

                    "time" -> {
                        /**
                         * `n` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                         */

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

        /**
         * `out` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val out = cases.joinToString(",", "{", "}") { "\"${it.name}\":${run(it)}" }; Files.createDirectories(
            Path.of(
                args[1]
            ).parent
        ); Files.writeString(Path.of(args[1]), out); println(out)
    }

    /** scheduledBlocks: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private val scheduledBlocks = linkedMapOf<Int, () -> Unit>()
}
