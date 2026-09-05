package com.jojo.game.verification

import java.nio.file.Files
import java.nio.file.Path

/** Entry point for the verification-only EditLayer mutation trace. */
object EditMutationTraceHarness {
    @JvmStatic
    fun main(args: Array<String>) {
        val cases = EditMutationFixtureParser.parse(Files.readString(Path.of(args[0])))
        val out = cases.joinToString(",", "{", "}") { scenario ->
            EditMutationTraceJson.quote(scenario.id) + ":" + EditMutationScenarioRunner.run(scenario)
        }
        val destination = Path.of(args[1])
        Files.createDirectories(destination.parent)
        Files.writeString(destination, out)
    }
}
