package com.jojo.game.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.application.runtime.GameEntryPoint
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.JojoGame

/** 실제 게임 실행을 담당하는 데스크톱 진입점입니다. */
object DesktopLauncher {
    /** 실행 인자를 해석하고 데스크톱 게임 창을 생성합니다. */
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

/** 데스크톱 실행에 필요한 게임 진입 옵션입니다. */
internal data class ProductionLaunchOptions(
    val scenario: String,
    val battleReturnScenario: String?,
    val explicitScenario: Boolean,
    val battle: Boolean,
) {
    companion object {
        /** 명령행 인자를 실행 옵션으로 변환합니다. */
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
