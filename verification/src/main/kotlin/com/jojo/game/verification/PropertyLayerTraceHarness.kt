package com.jojo.game.verification

import com.jojo.game.*

/** Kotlin half of the shared PropertyLayer recovered-source conformance fixture. */
object PropertyLayerTraceHarness {
    private data class Case(val name: String, val events: List<String>)

    @JvmStatic
    fun main(args: Array<String>) {
        val text = java.nio.file.Files.readString(java.nio.file.Path.of(args[0]))

        /**
         * 공개 메서드 `field`
         *
         * ### 파라미터
        - `s` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `key` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun field(s: String, key: String) =
            Regex("\\\"$key\\\":(null|\\\"[^\\\"]*\\\"|-?\\d+|true|false)").find(s)?.groupValues?.get(1)

        /**
         * 공개 메서드 `str`
         *
         * ### 파라미터
        - `s` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `key` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun str(s: String, key: String) = field(s, key)?.removeSurrounding("\"")
        val itemObjects = Regex("\\{[^{}]*\\\"id\\\":\\d+[^{}]*}").findAll(
            text.substringAfter("\"items\": [").substringBefore("],\n  \"inventory\"")
        ).map { it.value }.toList()
        val items = itemObjects.map { s ->
            PropertyLayer.Item(
                str(s, "id")!!.toInt(),
                str(s, "name")!!,
                str(s, "itemType")!!.toInt(),
                str(s, "icon")!!.toInt(),
                str(s, "level")!!.toInt(),
                str(s, "owner")?.takeUnless { it == "null" },
                str(s, "exp")!!.toInt(),
                str(s, "expLimit")!!.toInt(),
                str(s, "typeName")
            )
        }
        val inventoryBlock = text.substringAfter("\"inventory\": {").substringBefore("},\n  \"cases\"")
        val inventory = Regex("\\\"(\\d+)\\\":(\\d+)").findAll(inventoryBlock)
            .associate { it.groupValues[1].toInt() to it.groupValues[2].toInt() }
        val cases = Regex("\\{\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"events\\\":\\[(.*?)\\]}").findAll(
            text.replace(
                Regex("\\s+"),
                ""
            )
        ).map { m ->
            Case(
                m.groupValues[1],
                Regex("\\\"([^\\\"]+)\\\"").findAll(m.groupValues[2]).map { it.groupValues[1] }.toList()
            )
        }.toList()

        /**
         * 공개 메서드 `esc`
         *
         * ### 파라미터
        - `s` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")

        /**
         * 공개 메서드 `trace`
         *
         * ### 파라미터
        - `spec` (`Case`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun trace(spec: Case): String {
            val layer = PropertyLayer(items, inventory)
            var routes = mutableListOf<Int>(); fun snap(step: String): String {
                val rows = layer.rows()
                val scroll = layer.scrollRow.coerceAtMost((rows.size - 1).coerceAtLeast(0))
                val rs = rows.joinToString(
                    ",",
                    prefix = "[",
                    postfix = "]"
                ) { r -> "{\"id\":${r.item.id},\"labels\":[${r.labels.joinToString(",") { "\"${esc(it)}\"" }}]}" }; return "{\"step\":\"${
                    esc(
                        step
                    )
                }\",\"selected\":${layer.selected.ordinal},\"panels\":[${layer.selected != PropertyLayer.Tab.PROPERTY},${layer.selected == PropertyLayer.Tab.PROPERTY}],\"propertyInitialized\":${layer.panelInitialized()},\"attached\":${layer.attached},\"rows\":$rs,\"boundary\":{\"count\":${rows.size},\"scroll\":$scroll,\"first\":${if (rows.isEmpty()) -1 else scroll},\"last\":${
                    if (rows.isEmpty()) -1 else minOf(
                        rows.size - 1,
                        scroll + 4
                    )
                }},\"routes\":[${routes.joinToString(",") { "{\"layer\":\"ItemLayer\",\"item\":$it}" }}]}"
            }

            val out = mutableListOf<String>(); layer.onCreate(); out += snap("create"); for (event in spec.events) {
                val p = event.split(':'); when (p[0]) {
                    "tab" -> layer.onTabTouch(
                        PropertyLayer.Tab.entries[p[1].toInt()],
                        p[2].toInt()
                    ); "cancel" -> layer.onCancel(p[1].toInt()); "row" -> layer.onRowTouch(p[1].toInt(), p[2].toInt())
                        ?.let { routes += it }; "scroll" -> layer.onScroll(p[1].toInt())
                }; out += snap(event)
            }; return out.joinToString(",", prefix = "[", postfix = "]")
        }

        val json = cases.joinToString(
            ",",
            prefix = "{",
            postfix = "}"
        ) { "\"${esc(it.name)}\":${trace(it)}" }; java.nio.file.Files.createDirectories(java.nio.file.Path.of(args[1]).parent); java.nio.file.Files.writeString(
            java.nio.file.Path.of(args[1]),
            json
        ); println(json)
    }
}
