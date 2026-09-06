// Verification
package com.jojo.game.verification

import com.jojo.game.*
import com.jojo.game.presentation.shared.overlay.PropertyLayer

/** PropertyLayerTraceHarness: 공용 PropertyLayer 복원 원본 적합성 픽스처의 Kotlin 실행부이다. */
object PropertyLayerTraceHarness {
    /** Case: case 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Case(val name: String, val events: List<String>)

    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun main(args: Array<String>) {
        val text = java.nio.file.Files.readString(java.nio.file.Path.of(args[0]))


        /** field: 입력 데이터에서 지정한 블록을 추출한다. */
        fun field(s: String, key: String) =
            Regex("\\\"$key\\\":(null|\\\"[^\\\"]*\\\"|-?\\d+|true|false)").find(s)?.groupValues?.get(1)


        /** str: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
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


        /** esc: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")


        /** trace: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
        fun trace(spec: Case): String {
            val layer = PropertyLayer(items, inventory)
            /** snap: 현재 추적 상태를 스냅샷으로 만든다. */
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
                /**
                 * `p` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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
