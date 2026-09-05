package com.jojo.game.verification

import com.jojo.game.*

import java.nio.file.Files
import java.nio.file.Path

/** Kotlin side of the recovered RoundLayer timer/lifecycle trace fixture. */
object RoundLayerTraceHarness {
    private data class Spec(
        val name: String,
        val roundPresent: Boolean,
        val round: Int,
        val maxPresent: Boolean,
        val max: Int?,
        val events: List<String>
    )

    private fun block(text: String, from: Int): String {
        val open = text[from]
        val close = if (open == '{') '}' else ']'
        var n = 0
        var quote = false; for (i in from until text.length) {
            val c = text[i]; if (c == '\"') quote =
                !quote; else if (!quote && c == open) n++; else if (!quote && c == close && --n == 0) return text.substring(
                from,
                i + 1
            )
        }; error("unclosed")
    }

    private fun objects(array: String): List<String> {
        val out = mutableListOf<String>()
        var p = 0; while (p < array.length) {
            if (array[p] == '{') {
                val x = block(array, p); out += x; p += x.length
            } else p++
        }; return out
    }

    private fun field(o: String, k: String) = o.indexOf("\"$k\"").let { if (it < 0) error(k); else it }
    private fun bool(o: String, k: String) =
        Regex("\\\"$k\\\"\\s*:\\s*(true|false)").find(o)!!.groupValues[1].toBoolean()

    private fun intNull(o: String, k: String): Int? =
        Regex("\\\"$k\\\"\\s*:\\s*(null|-?\\d+)").find(o)?.groupValues?.get(1)?.takeUnless { it == "null" }?.toInt()

    @JvmStatic
    fun main(args: Array<String>) {
        val raw = Files.readString(Path.of(args[0]))
        val casesBlock = block(raw, raw.indexOf('[', field(raw, "cases")))
        val specs = objects(casesBlock).map { o ->
            val name = Regex("\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").find(o)!!.groupValues[1]
            val ab = block(o, o.indexOf('{', field(o, "args")))
            val eb = block(o, o.indexOf('[', field(o, "events"))); Spec(
            name,
            bool(ab, "roundPresent"),
            intNull(ab, "round") ?: 0,
            bool(ab, "maxPresent"),
            intNull(ab, "max"),
            Regex("\\\"([^\\\"]+)\\\"").findAll(eb).map { it.groupValues[1] }.toList()
        )
        }; fun run(s: Spec): String {
            var removed = 0
            val events = mutableListOf<String>()
            val layer = RoundLayer({ removed++ },
                { events += "complete" }); layer.onCreate(
                RoundLayer.CreateArgs(
                    s.roundPresent,
                    s.round,
                    s.max,
                    s.maxPresent
                )
            ); fun snap(step: String): String {
                val v =
                    layer.view; return "{\"step\":\"$step\",\"roundLabelsVisible\":${v.roundLabelsVisible},\"campLabelsVisible\":${v.campLabelsVisible},\"roundText\":\"${v.roundText}\",\"scheduled\":[2],\"touchBindings\":0,\"removed\":$removed,\"events\":[${
                    events.joinToString(
                        ","
                    ) { "\"$it\"" }
                }]}"
            }

            val trace = mutableListOf(snap("create")); s.events.forEach { event ->
                val p = event.split(':'); if (p[0] == "elapsed") layer.elapsed(p[1].toFloat()); trace += snap(event)
            }; return trace.joinToString(",", "[", "]")
        }

        val output = specs.joinToString(",", "{", "}") { "\"${it.name}\":${run(it)}" }; Files.createDirectories(
            Path.of(
                args[1]
            ).parent
        ); Files.writeString(Path.of(args[1]), output); println(output)
    }
}
