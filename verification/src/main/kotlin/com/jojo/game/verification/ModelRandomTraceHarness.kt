package com.jojo.game.verification

import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.domain.scenario.ScenarioRandomSequence
import java.nio.file.Files
import java.nio.file.Path

/** Emits the deterministic scenario-random contract for external comparison. */
object ModelRandomTraceHarness {
    @JvmStatic
    fun main(args: Array<String>) {
        val cases = JsonReader().parse(Files.readString(Path.of(args[0]))).get("cases")
        val result = buildString {
            append('{')
            var firstCase = true
            var entry = cases.child
            while (entry != null) {
                if (!firstCase) append(',')
                firstCase = false
                var seed = entry.getDouble("seed")
                val values = mutableListOf<Int>()
                repeat(entry.getInt("draws")) {
                    val next = ScenarioRandomSequence.nextFromSeed(seed)
                    seed = next.first
                    values += next.second
                }
                append('"').append(entry.getString("id")).append("\":{\"values\":[")
                append(values.joinToString(","))
                append("]}")
                entry = entry.next
            }
            append('}')
        }
        val output = Path.of(args[1])
        Files.createDirectories(output.parent)
        Files.writeString(output, result)
    }
}
