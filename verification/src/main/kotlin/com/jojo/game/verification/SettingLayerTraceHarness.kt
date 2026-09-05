package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** Kotlin half of the SettingLayer recovered-JS isolated trace fixture. */
object SettingLayerTraceHarness {
    private data class Case(val name: String, val values: LinkedHashMap<String, Int>, val events: List<String>)

    @JvmStatic
    fun main(args: Array<String>) {
        val raw = Files.readString(Path.of(args[0]))
        val cases =
            Regex("""(?s)\{\s*"name"\s*:\s*"([^"]+)"\s*,\s*"initial"\s*:\s*\{([^}]*)}\s*,\s*"events"\s*:\s*\[([^]]*)]""")
                .findAll(raw).map { m ->
                    val values = LinkedHashMap<String, Int>()
                    Regex(""""([^"]+)"\s*:\s*(-?\d+)""").findAll(m.groupValues[2])
                        .forEach { v -> values[v.groupValues[1]] = v.groupValues[2].toInt() }
                    Case(
                        m.groupValues[1],
                        values,
                        Regex("\\\"([^\\\"]+)\\\"").findAll(m.groupValues[3]).map { it.groupValues[1] }.toList()
                    )
                }.toList()

        /**
         * 공개 메서드 `json`
         *
         * ### 파라미터
        - `s` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun json(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")

        /**
         * 공개 메서드 `run`
         *
         * ### 파라미터
        - `spec` (`Case`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun run(spec: Case): String {
            val writes = mutableListOf<Pair<String, Int>>()
            val events = mutableListOf<String>()
            val store = object : SettingLayer.Store {
                override fun getInt(key: String, default: Int) = spec.values[key] ?: default
                override fun putInt(key: String, value: Int) {
                    spec.values[key] = value; writes += key to value
                }
            }
            val sound = object : SettingLayer.Sound {
                override fun music(on: Boolean) {
                    events += "music:$on"
                }

                override fun effect(on: Boolean) {
                    events += "effect:$on"
                }
            }
            val layer = SettingLayer(store, sound) { events += "applySpeed" }
            layer.onCreate()
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
                val values = spec.values.entries.joinToString(",") { "\"${json(it.key)}\":${it.value}" }
                val writeText = writes.joinToString(",", "[", "]") { "[\"${json(it.first)}\",${it.second}]" }
                val eventText = events.joinToString(",", "[", "]") { "\"${json(it)}\"" }
                return "{\"step\":\"${json(step)}\",\"attached\":${v.attached},\"flags\":${v.flags},\"speed\":${v.speed},\"values\":{$values},\"writes\":$writeText,\"events\":$eventText}"
            }

            val trace = mutableListOf(snap("create"))
            spec.events.forEach { event ->
                val p = event.split(':')
                when (p[0]) {
                    "flag" -> layer.check(p[1].toInt(), p[2].toBoolean())
                    "radio" -> layer.check2(p[1].toInt(), p[2].toInt())
                    "background" -> layer.selectBackground(p[1].toInt())
                    "slider" -> layer.onSlider(p[1].toFloat())
                    "close" -> layer.dismiss(p[1].toInt())
                    "destroy" -> layer.onDestroy()
                }
                trace += snap(event)
            }
            return trace.joinToString(",", "[", "]")
        }

        val output = cases.joinToString(",", "{", "}") { "\"${json(it.name)}\":${run(it)}" }
        Files.createDirectories(Path.of(args[1]).parent)
        Files.writeString(Path.of(args[1]), output)
        println(output)
    }
}
