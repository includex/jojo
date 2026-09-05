package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/**
 * object  `MapInfoLayerTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object MapInfoLayerTraceHarness {
    private data class Case(
        val name: String,
        val setting: Int,
        val text: String,
        val changePage: Boolean,
        val weapon: Boolean,
        val wait: Boolean,
        val events: List<String>
    )

    private fun escape(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    @JvmStatic
    fun main(args: Array<String>) {
        val source = Files.readString(Path.of(args[0])).replace(Regex("\\s+"), "")
        val cases =
            Regex("""\{"name":"([^"]+)","setting":(\d+),"data":\{"txt":"((?:\\.|[^"])*)","changePage":(true|false),"wepon":(true|false),"wait":(true|false)},"events":\[(.*?)]}""").findAll(
                source
            ).map { m ->
                Case(
                    m.groupValues[1],
                    m.groupValues[2].toInt(),
                    m.groupValues[3].replace("\\n", "\n"),
                    m.groupValues[4].toBoolean(),
                    m.groupValues[5].toBoolean(),
                    m.groupValues[6].toBoolean(),
                    Regex("\"([^\"]+)\"").findAll(m.groupValues[7]).map { it.groupValues[1] }.toList()
                )
            }.toList()

        /**
         * 공개 메서드 `trace`
         *
         * ### 파라미터
        - `c` (`Case`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun trace(c: Case): String {
            var complete = 0
            var removed = 0
            val layer = MapInfoLayer(c.setting, { it }, { complete++ }, { removed++ })
            layer.onCreate(MapInfoLayer.Data(c.text, c.changePage, c.weapon, c.wait))
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
                val v = layer.view()
                val events =
                    (List(complete) { "\"complete\"" } + List(removed) { "\"removeFromParent\"" }).joinToString(",")
                val delay = v.autoCloseDelay?.toString() ?: "null"
                return "{\"step\":\"${escape(step)}\",\"text\":\"${escape(v.text)}\",\"typing\":${v.typing},\"autoCloseDelay\":$delay,\"attached\":${v.attached},\"events\":[$events]}"
            }

            val out = mutableListOf(snap("create"))
            c.events.forEach { event ->
                val p = event.split(':')
                when (p[0]) {
                    "tick" -> layer.tick(); "cancel" -> layer.cancel(p[1].toInt()); "skip" -> layer.skip(); "elapse" -> layer.elapse(
                    p[1].toInt()
                )
                }
                out += snap(event)
            }
            return out.joinToString(",", "[", "]")
        }

        val output = cases.joinToString(",", "{", "}") { "\"${escape(it.name)}\":${trace(it)}" }
        Files.createDirectories(Path.of(args[1]).parent); Files.writeString(Path.of(args[1]), output); println(output)
    }
}
