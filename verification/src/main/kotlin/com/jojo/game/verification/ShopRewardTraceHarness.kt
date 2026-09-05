package com.jojo.game.verification

import java.nio.file.Files
import java.nio.file.Path

/** Entry point for the verification-only shop, sell, and reward trace. */
object ShopRewardTraceHarness {
    @JvmStatic
    fun main(args: Array<String>) {
        val result = linkedMapOf<String, String>()
        ShopRewardFixtureParser.parse(Files.readString(Path.of(args[0]))).forEach { fixture ->
            result[fixture.name] = ShopRewardTraceScenario.run(fixture)
        }
        val output = ShopRewardJson.objectValue(result.entries.map { it.key to it.value })
        val destination = Path.of(args[1])
        Files.createDirectories(destination.parent)
        Files.writeString(destination, output)
        println(output)
    }
}
