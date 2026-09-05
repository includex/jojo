package com.jojo.game.verification.campaign

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.application.runtime.GameEntryPoint
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.JojoGame
import java.nio.file.Path

/** Verification-side desktop entry point for the real campaign input lifecycle. */
object CampaignE2eDesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val options = CampaignE2eLaunchOptions.parse(args)
        val configuration = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo campaign E2E verification")
            setWindowedMode(1280, 688)
            setInitialVisible(options.visible)
            setWindowPosition(0, 0)
            setForegroundFPS(60)
            setIdleFPS(60)
            setPauseWhenMinimized(false)
            setPauseWhenLostFocus(false)
            disableAudio(true)
        }
        Lwjgl3Application(
            JojoGame(
                GameLaunchConfiguration(
                    entryPoint = GameEntryPoint.TITLE,
                    runtimeScreenObserver = CampaignE2eRuntimeObserver(options.traceConfig),
                    automatedRun = true,
                ),
            ),
            configuration,
        )
    }
}

/** Parsed independently so launcher wiring can be verified without opening a window. */
internal data class CampaignE2eLaunchOptions(
    val traceConfig: CampaignE2eTraceConfig,
    val visible: Boolean,
) {
    companion object {
        fun parse(args: Array<String>): CampaignE2eLaunchOptions {
            val values = args.associate { argument ->
                require(argument.startsWith("--")) { "unknown campaign E2E argument: $argument" }
                val (key, value) = argument.removePrefix("--").split('=', limit = 2).let {
                    it.first() to it.getOrElse(1) { "true" }
                }
                key to value
            }
            val stop = values["stop"]?.split(':', limit = 2)?.let { fields ->
                require(fields.size == 2 && fields[0].matches(Regex("R_[0-9]{2}"))) {
                    "--stop uses R_NN:sceneIndex"
                }
                CampaignE2eStopPoint(fields[0], fields[1].toInt())
            } ?: CampaignE2eStopPoint("R_00", 1)
            val fullContract = values["assert-bootstrap"] == "true"
            val output = values["output"] ?: Path.of("build", "reports", "campaign-e2e-smoke.json").toString()
            return CampaignE2eLaunchOptions(
                traceConfig = CampaignE2eTraceConfig(
                    outputPath = output,
                    maxSeconds = values["max-seconds"]?.toFloat() ?: 60f,
                    inputIntervalSeconds = values["input-interval"]?.toFloat() ?: .12f,
                    stopAt = stop,
                    requireYingchuanBootstrapContract = fullContract,
                ),
                visible = values["visible"] == "true",
            )
        }
    }
}
