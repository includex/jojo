package com.jojo.port

import java.nio.file.Files
import java.nio.file.Path

object TerrainLayerTraceHarness {
    @JvmStatic fun main(args: Array<String>) {
        val events = Regex("\"(DOWN|END|SCROLL):(\\d+)\"")
            .findAll(Files.readString(Path.of(args.single())))
            .map { "${it.groupValues[1]}:${it.groupValues[2]}" }.toList()
        val terrain = (0..27).map { TerrainLayer.Terrain(it, "T$it", if (it == 0) 5 else 0, if (it == 0) 10 else 0) }
        val arms = (0..12).map { i -> TerrainLayer.Arm(i, "A$i", mapOf(0 to when(i) { 0->80;1->100;2->110;else->131 }), mapOf(0 to when(i) { 0->1;1->5;2->201;else->7 })) }
        val layer = TerrainLayer(terrain, arms)
        var selected = TerrainLayer.Tab.RISE
        val rise = layer.select(selected)
        val expend = TerrainLayer(terrain, arms).select(TerrainLayer.Tab.EXPEND)
        var removed = false
        fun state(tag: String, scroll: Int? = null): String {
            val buttons = if (selected == TerrainLayer.Tab.RISE) "[false,true,true]" else "[true,false,true]"
            val suffix = scroll?.let { ",\"scroll\":$it" } ?: ""
            return "{\"tag\":\"$tag\",\"selected\":${if(selected==TerrainLayer.Tab.RISE)0 else 1},\"initialized\":[${layer.isInitialized(TerrainLayer.Tab.RISE)},${layer.isInitialized(TerrainLayer.Tab.EXPEND)}],\"buttons\":$buttons,\"rows\":[${payload(rise)},${payload(expend)}],\"removed\":$removed$suffix}"
        }
        val trace = mutableListOf(state("create"))
        for (event in events) when {
            event.startsWith("DOWN:") -> trace += state(event)
            event == "END:1" -> { selected = TerrainLayer.Tab.EXPEND; layer.select(selected); trace += state(event) }
            event == "END:2" -> { removed = true; trace += state(event) }
            event.startsWith("SCROLL:") -> trace += state(event, event.substringAfter(':').toInt())
        }
        print(trace.joinToString(prefix = "[", postfix = "]"))
    }

    private fun payload(panel: TerrainLayer.Panel): String = panel.rows.joinToString(prefix = "[", postfix = "]") { row ->
        val skills = row.enabledSkills.joinToString(prefix = "[", postfix = "]")
        val values = row.values.joinToString(prefix = "[", postfix = "]") { "\"${it.text}\"" }
        "{\"id\":${row.terrainId},\"name\":\"${row.terrainName}\",\"icon\":${row.iconIndex},\"skills\":$skills,\"arms\":$values}"
    }
}
