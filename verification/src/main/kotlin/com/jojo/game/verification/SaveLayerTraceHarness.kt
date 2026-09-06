// Verification
package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*


/** SaveLayerTraceHarness: 검증 실행을 시작하고 추적 결과를 수집하는 타입이다. */
object SaveLayerTraceHarness {
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(a: Array<String>) {
        /** q: 문자열을 JSON 인용 형식으로 변환한다. */
        val f = java.nio.file.Files.readString(java.nio.file.Path.of(a[0])); fun q(x: String) = "\"$x\""
        val saves =
            Regex("\\{\\\"index\\\":(\\d+),\\\"time\\\":(\\d+),\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"stage\\\":(\\d+),\\\"game\\\":(true|false)}").findAll(
                f
            ).associate { it.groupValues[1].toInt() to it.groupValues }
        val cases =
            Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"savedPage\\\":(\\d+),\\\"pagesEnabled\\\":(true|false),\\\"tip\\\":(true|false),\\\"events\\\":\\[(.*?)]}").findAll(
                f.replace(Regex("\\s+"), "")
            ).toList()


        /** one: 런타임 이벤트를 받아 검증 산출물을 갱신한다. */
        fun one(c: MatchResult): String {
            val g = c.groupValues
            val repo = object : SaveLayer.Repository {
                /** load: 검증 리소스를 읽어 실행 상태를 구성한다. */
                override fun load(i: Int): String? {
                    val x = saves[i]
                        ?: return null; return "{\"time\":${x[2]},\"name\":\"${x[3]}\",\"model\":${if (x[5] == "true") "{\"game\":{\"stage\":${x[4]}}}" else "{\"property2\":[0,${x[4]}]}"}}"
                }

                /** save: 검증 산출물을 지정한 경로에 기록한다. */
                override fun save(i: Int) {}
            }
            /**
             * `cb` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            var cb = 0
            /**
             * `l` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val l = SaveLayer(repo, g[3] == "true"); l.onCreate({ cb++ }, g[4] == "true", g[2].toInt())
            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
            val t = mutableListOf<String>(); fun snap(s: String) {
                /**
                 * `v` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val v = l.view()
                /**
                 * `rows` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val rows = v.rows.joinToString(
                    ",",
                    "[",
                    "]"
                ) { r -> "{\"index\":${r.index},\"number\":${q(r.number)},\"stage\":${q(r.stage)},\"name\":${q(r.name)}}" }
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val p = if (l.completionTipOpen()) q("저장 완료.") else l.pendingPrompt()?.let(::q) ?: "null"
                /**
                 * `ev` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val ev = l.takeLifecycle()
                    .joinToString(",") { q(it) }; t += "{\"step\":${q(s)},\"page\":${v.page},\"storedPage\":${l.storedPage()},\"toggles\":${v.pageTogglesVisible},\"attached\":${v.attached},\"pending\":$p,\"rows\":$rows,\"events\":[$ev],\"callbacks\":$cb}"
            }; snap("create"); Regex("\\\"([^\\\"]+)\\\"").findAll(g[5]).forEach { x ->
                /**
                 * `e` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val e = x.groupValues[1].split(':'); when (e[0]) {
                "row" -> l.onRowTouch(e[1].toInt(), e[2].toInt()); "page" -> l.onPageTouch(
                    e[1].toInt(),
                    e[2].toInt()
                ); "cancel" -> l.onCancel(e[1].toInt()); "confirm" -> l.onConfirm(e[1].toInt()); else -> l.onCompletionTip(
                    e[1].toInt()
                )
            }; snap(x.groupValues[1])
            }; return "{\"id\":${q(g[1])},\"trace\":[${t.joinToString()}]}"
        }

        /**
         * `o` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val o = cases.joinToString(
            ",",
            "[",
            "]"
        ) { one(it) }; java.nio.file.Files.createDirectories(java.nio.file.Path.of(a[1]).parent); java.nio.file.Files.writeString(
            java.nio.file.Path.of(a[1]),
            o
        )
    }
}
