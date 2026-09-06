// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.shared.overlay.MagicInfoLayer
import com.jojo.game.presentation.shared.overlay.MagicUiList


/** MagicTraceHarness: 검증 실행을 시작하고 추적 결과를 수집하는 타입이다. */
object MagicTraceHarness {
    /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun q(s: String) = "\"$s\""
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(a: Array<String>) {
        val text = java.nio.file.Files.readString(java.nio.file.Path.of(a[0]))
        val cases =
            Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"mp\\\":(\\d+),\\\"maxMp\\\":(\\d+),\\\"uses\\\":(\\{.*?}),\\\"magics\\\":\\[(.*?)]\\,\\\"events\\\":\\[(.*?)]}").findAll(
                text
            )
        val o = cases.joinToString(
            prefix = "[",
            postfix = "]"
        ) { run(it) }; java.nio.file.Files.writeString(java.nio.file.Path.of(a[1]), o); println(o)
    }

    /** run: 검증 실행에 필요한 상태를 구성한다. */
    private fun run(m: MatchResult): String {
        val id = m.groupValues[1]
        val mp = m.groupValues[2].toInt()
        val max = m.groupValues[3].toInt()
        val uses = Regex("\\\"(\\d+)\\\":(\\d+)").findAll(m.groupValues[4])
            .associate { it.groupValues[1].toInt() to it.groupValues[2].toInt() }
        val magics =
            Regex("\\{\\\"id\\\":(\\d+),\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"cost\\\":(\\d+),\\\"power\\\":(null|\\d+),\\\"icon\\\":(\\d+),\\\"hit\\\":(\\d+),\\\"eff\\\":(\\d+),\\\"intro\\\":\\\"([^\\\"]*)\\\"}").findAll(
                m.groupValues[5]
            ).map { v ->
                MagicUiList.Magic(
                    v.groupValues[1].toInt(),
                    v.groupValues[2],
                    v.groupValues[3].toInt(),
                    v.groupValues[4].takeUnless { it == "null" }?.toInt(),
                    v.groupValues[5].toInt(),
                    v.groupValues[6].toInt(),
                    v.groupValues[7].toInt(),
                    v.groupValues[8]
                )
            }.toList()
        val list = MagicUiList(
            mp,
            max,
            magics,
            uses
        ); list.rows.forEach { list.events += "asset:Game/Magic/${it.icon + 1}-1" }
        var info: MagicInfoLayer? = null
        /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
        val trace = mutableListOf<String>(); fun snap(s: String) {
            val rows = list.rows.joinToString(
                prefix = "[",
                postfix = "]"
            ) { "{\"id\":${it.id},\"enabled\":${mp >= it.cost}}" }
            val us = list.uses.entries.sortedBy { it.key }
                .joinToString(prefix = "{", postfix = "}") { q(it.key.toString()) + ":" + it.value }
            val layer =
                info?.let { "{\"name\":${q(it.magic.name)},\"assets\":[${it.assets.joinToString { q(it) }}],\"removed\":${!it.attached}}" }
                    ?: "null"; trace += "{\"step\":${q(s)},\"rows\":$rows,\"preview\":${list.preview},\"removed\":${!list.attached},\"uses\":$us,\"layer\":$layer,\"events\":[${
                list.events.joinToString {
                    q(
                        it
                    )
                }
            }]}"; list.events.clear()
        }; Regex("\\\"([^\\\"]+)\\\"").findAll(m.groupValues[6]).forEach { e ->
            val p = e.groupValues[1].split(':'); when (p[0]) {
            "START" -> list.rows.getOrNull(p[1].toInt())?.takeIf { mp >= it.cost }
                ?.let { list.start(p[1].toInt()) }; "END" -> list.end(p[1].toInt()); "tick" -> list.tick()?.let {
                info = MagicInfoLayer(it)
            }; "cancel" -> list.cancel(if (p[1] == "END") 2 else 1); "magic" -> info?.close(if (p[1] == "END") 2 else 1)
        }; snap(e.groupValues[1])
        }; return "{\"case\":${q(id)},\"trace\":[${trace.joinToString()}]}"
    }
}
