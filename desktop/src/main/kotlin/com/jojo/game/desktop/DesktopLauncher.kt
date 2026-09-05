package com.jojo.game.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.GameEntryPoint
import com.jojo.game.GameLaunchConfiguration
import com.jojo.game.JojoGame

/** Production desktop entry point. Verification and capture flags live in :verification. */
object DesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val options = ProductionLaunchOptions.parse(args)
        val configuration = Lwjgl3ApplicationConfiguration().apply {
            setTitle("삼국지 조조전")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
        }
        Lwjgl3Application(
            JojoGame(
                GameLaunchConfiguration(
                    entryPoint = if (options.battle) GameEntryPoint.BATTLE else if (options.explicitScenario) GameEntryPoint.SCENARIO else GameEntryPoint.TITLE,
                    initialScenario = options.scenario,
                    battleReturnScenario = options.battleReturnScenario,
                    initialScenarioExplicit = options.explicitScenario,
                ),
            ),
            configuration,
        )
    }
}

/** The interactive launcher accepts gameplay navigation only. */
internal data class ProductionLaunchOptions(
    val scenario: String,
    val battleReturnScenario: String?,
    val explicitScenario: Boolean,
    val battle: Boolean,
) {
    companion object {
        fun parse(args: Array<String>): ProductionLaunchOptions {
            val allowed = setOf("--battle")
            args.forEach { argument ->
                require(argument in allowed || argument.startsWith("--scenario=") || argument.startsWith("--battle-return=")) {
                    "unsupported production launch argument: $argument"
                }
            }
            return ProductionLaunchOptions(
                scenario = args.firstOrNull { it.startsWith("--scenario=") }?.substringAfter('=')?.uppercase() ?: "R_00",
                battleReturnScenario = args.firstOrNull { it.startsWith("--battle-return=") }?.substringAfter('=')?.uppercase(),
                explicitScenario = args.any { it.startsWith("--scenario=") },
                battle = "--battle" in args,
            )
        }
    }
}
