package com.jojo.game.verification
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*


object SaveLayerTraceHarness {
    @JvmStatic
    fun main(a: Array<String>) {
        val f = java.nio.file.Files.readString(java.nio.file.Path.of(a[0])); fun q(x: String) = "\"$x\""
        val saves =
            Regex("\\{\\\"index\\\":(\\d+),\\\"time\\\":(\\d+),\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"stage\\\":(\\d+),\\\"game\\\":(true|false)}").findAll(
                f
            ).associate { it.groupValues[1].toInt() to it.groupValues }
        val cases =
            Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"savedPage\\\":(\\d+),\\\"pagesEnabled\\\":(true|false),\\\"tip\\\":(true|false),\\\"events\\\":\\[(.*?)]}").findAll(
                f.replace(Regex("\\s+"), "")
            ).toList()


        fun one(c: MatchResult): String {
            val g = c.groupValues
            val repo = object : SaveLayer.Repository {
                override fun load(i: Int): String? {
                    val x = saves[i]
                        ?: return null; return "{\"time\":${x[2]},\"name\":\"${x[3]}\",\"model\":${if (x[5] == "true") "{\"game\":{\"stage\":${x[4]}}}" else "{\"property2\":[0,${x[4]}]}"}}"
                }

                override fun save(i: Int) {}
            }
            var cb = 0
            val l = SaveLayer(repo, g[3] == "true"); l.onCreate({ cb++ }, g[4] == "true", g[2].toInt())
            val t = mutableListOf<String>(); fun snap(s: String) {
                val v = l.view()
                val rows = v.rows.joinToString(
                    ",",
                    "[",
                    "]"
                ) { r -> "{\"index\":${r.index},\"number\":${q(r.number)},\"stage\":${q(r.stage)},\"name\":${q(r.name)}}" }
                val p = if (l.completionTipOpen()) q("저장 완료.") else l.pendingPrompt()?.let(::q) ?: "null"
                val ev = l.takeLifecycle()
                    .joinToString(",") { q(it) }; t += "{\"step\":${q(s)},\"page\":${v.page},\"storedPage\":${l.storedPage()},\"toggles\":${v.pageTogglesVisible},\"attached\":${v.attached},\"pending\":$p,\"rows\":$rows,\"events\":[$ev],\"callbacks\":$cb}"
            }; snap("create"); Regex("\\\"([^\\\"]+)\\\"").findAll(g[5]).forEach { x ->
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
