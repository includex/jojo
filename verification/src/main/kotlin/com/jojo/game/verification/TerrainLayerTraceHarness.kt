// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.shared.overlay.TerrainLayer

import java.nio.file.Files
import java.nio.file.Path


/** TerrainLayerTraceHarness: 검증 실행을 시작하고 추적 결과를 수집하는 타입이다. */
object TerrainLayerTraceHarness {
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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


        /** state: 검증 입력을 처리하고 관련 상태를 갱신한다. */
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

    /** payload: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun payload(panel: TerrainLayer.Panel): String =
        panel.rows.joinToString(prefix = "[", postfix = "]") { row ->
            /**
             * `skills` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val skills = row.enabledSkills.joinToString(prefix = "[", postfix = "]")
            /**
             * `values` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val values = row.values.joinToString(prefix = "[", postfix = "]") { "\"${it.text}\"" }
            "{\"id\":${row.terrainId},\"name\":\"${row.terrainName}\",\"icon\":${row.iconIndex},\"skills\":$skills,\"arms\":$values}"
        }
}
