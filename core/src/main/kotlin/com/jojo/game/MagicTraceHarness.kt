package com.jojo.game

/**
 * object  `MagicTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object MagicTraceHarness {
    private fun q(s: String) = "\"$s\""
    @JvmStatic
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
