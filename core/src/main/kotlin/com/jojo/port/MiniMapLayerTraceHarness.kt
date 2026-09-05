package com.jojo.port

import java.nio.file.Files
import java.nio.file.Path

/** Compile-stable port trace companion for the recovered MiniMap factory harness. */
object MiniMapLayerTraceHarness {
    private fun quote(value: String?) = value?.let { "\"${it.replace("\\", "\\\\").replace("\"", "\\\"")}\"" } ?: "null"

    @JvmStatic fun main(args: Array<String>) {
        val fixture = Files.readString(Path.of(args[0])).replace(Regex("\\s+"), "")
        val cases = Regex("""\{"name":"([^"]+)","setting":(\d+),"weather":(null|\d+),"initialPoolNodes":(\d+),"events":\[(.*?)]}""").findAll(fixture)
        fun state(layer: MiniMapLayer, step: String, callbacks: Int): String {
            val size = layer.size?.let { "{\"width\":${it.first},\"height\":${it.second}}" } ?: "null"
            val bg = if (layer.size == null) "null" else "{\"x\":${layer.bgX},\"y\":${layer.bgY}}"
            val box = if (layer.size == null) "null" else "{\"x\":${layer.boxX},\"y\":${layer.boxY}}"
            val map = layer.map.entries.joinToString(",", "{", "}") { (id, marker) ->
                "\"$id\":{\"x\":${marker.x},\"y\":${marker.y},\"frame\":${quote(marker.frame)}}"
            }
            val highlights = layer.highlights.joinToString(",", "[", "]") { h ->
                "{\"x\":${h[0]},\"y\":${h[1]},\"w\":${h[2]},\"h\":${h[3]},\"actions\":${h[4]}}"
            }
            return "{\"step\":\"$step\",\"show\":${layer.shown},\"loaded\":${layer.size != null},\"size\":$size,\"bg\":$bg,\"box\":$box,\"weather\":${quote(layer.weatherFrame)},\"pool\":${layer.pool},\"map\":$map,\"highlights\":$highlights,\"baseDestroyed\":${layer.baseDestroyed},\"callbackCount\":$callbacks}"
        }
        val output = cases.joinToString(",", "{", "}") { match ->
            var callbacks = 0
            val layer = MiniMapLayer(match.groupValues[2].toInt()) { callbacks++ }
            layer.onCreate(match.groupValues[3].takeUnless { it == "null" }?.toInt(), match.groupValues[4].toInt())
            val trace = mutableListOf(state(layer, "create", callbacks))
            Regex("\"([^\"]+)\"").findAll(match.groupValues[5]).forEach { event ->
                val parts = event.groupValues[1].split(':')
                when (parts[0]) {
                    "load" -> layer.load(parts[1].toInt(), parts[2].toInt())
                    "scroll" -> layer.scroll(parts[1].toInt(), parts[2].toInt())
                    "weather" -> layer.setWeather(parts[1].toInt())
                    "highlight" -> layer.highlight(parts[1].toInt(), parts[2].toInt(), parts[3].toInt(), parts[4].toInt())
                    "visible" -> layer.visible(parts[1].toInt(), parts[2], parts[3], parts[4], parts[5].toBoolean(), parts[6].toInt(), parts[7].toInt())
                    "hide" -> layer.hide(parts[1].toInt())
                    "move" -> layer.move(parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
                    "ref" -> layer.ref(parts[1].toInt(), parts[2], parts[3], parts[4], parts[5].toBoolean())
                    "pos" -> layer.move(parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
                    "possame" -> Unit
                    "touch" -> layer.touch(parts[1].toInt())
                    "destroy" -> layer.destroy()
                }
                trace += state(layer, event.groupValues[1], callbacks)
            }
            "\"${match.groupValues[1]}\":[${trace.joinToString(",")}]"
        }
        Files.createDirectories(Path.of(args[1]).parent)
        Files.writeString(Path.of(args[1]), output)
        println(output)
    }
}
