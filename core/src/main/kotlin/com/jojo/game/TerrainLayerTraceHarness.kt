package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/**
 * object  `TerrainLayerTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object TerrainLayerTraceHarness {
    @JvmStatic
    fun main(args: Array<String>) {
        val events = Regex("\"(DOWN|END|SCROLL):(\\d+)\"")
            .findAll(Files.readString(Path.of(args.single())))
            .map { "${it.groupValues[1]}:${it.groupValues[2]}" }.toList()
        val terrain = (0..27).map { TerrainLayer.Terrain(it, "T$it", if (it == 0) 5 else 0, if (it == 0) 10 else 0) }
        val arms = (0..12).map { i ->
            TerrainLayer.Arm(
                i, "A$i", mapOf(
                    0 to when (i) {
                        0 -> 80; 1 -> 100; 2 -> 110; else -> 131
                    }
                ), mapOf(
                    0 to when (i) {
                        0 -> 1; 1 -> 5; 2 -> 201; else -> 7
                    }
                )
            )
        }
        val layer = TerrainLayer(terrain, arms)
        var selected = TerrainLayer.Tab.RISE
        val rise = layer.select(selected)
        val expend = TerrainLayer(terrain, arms).select(TerrainLayer.Tab.EXPEND)
        var removed = false

        /**
         * 공개 메서드 `state`
         *
         * ### 파라미터
        - `tag` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `scroll` (`Int? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun state(tag: String, scroll: Int? = null): String {
            val buttons = if (selected == TerrainLayer.Tab.RISE) "[false,true,true]" else "[true,false,true]"
            val suffix = scroll?.let { ",\"scroll\":$it" } ?: ""
            return "{\"tag\":\"$tag\",\"selected\":${if (selected == TerrainLayer.Tab.RISE) 0 else 1},\"initialized\":[${
                layer.isInitialized(
                    TerrainLayer.Tab.RISE
                )
            },${layer.isInitialized(TerrainLayer.Tab.EXPEND)}],\"buttons\":$buttons,\"rows\":[${payload(rise)},${
                payload(
                    expend
                )
            }],\"removed\":$removed$suffix}"
        }

        val trace = mutableListOf(state("create"))
        for (event in events) when {
            event.startsWith("DOWN:") -> trace += state(event)
            event == "END:1" -> {
                selected = TerrainLayer.Tab.EXPEND; layer.select(selected); trace += state(event)
            }

            event == "END:2" -> {
                removed = true; trace += state(event)
            }

            event.startsWith("SCROLL:") -> trace += state(event, event.substringAfter(':').toInt())
        }
        print(trace.joinToString(prefix = "[", postfix = "]"))
    }

    private fun payload(panel: TerrainLayer.Panel): String =
        panel.rows.joinToString(prefix = "[", postfix = "]") { row ->
            val skills = row.enabledSkills.joinToString(prefix = "[", postfix = "]")
            val values = row.values.joinToString(prefix = "[", postfix = "]") { "\"${it.text}\"" }
            "{\"id\":${row.terrainId},\"name\":\"${row.terrainName}\",\"icon\":${row.iconIndex},\"skills\":$skills,\"arms\":$values}"
        }
}
