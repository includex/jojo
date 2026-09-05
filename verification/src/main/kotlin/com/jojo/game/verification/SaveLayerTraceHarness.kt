package com.jojo.game.verification

import com.jojo.game.*

/**
 * object  `SaveLayerTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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

        /**
         * 공개 메서드 `one`
         *
         * ### 파라미터
        - `c` (`MatchResult`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

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
