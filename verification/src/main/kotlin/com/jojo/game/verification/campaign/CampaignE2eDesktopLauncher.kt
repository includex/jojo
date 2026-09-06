// Verification
package com.jojo.game.verification.campaign

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.application.runtime.GameEntryPoint
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.JojoGame
import java.nio.file.Path

/** CampaignE2eDesktopLauncher: 실제 캠페인 입력 수명 주기를 실행하는 검증 측 데스크톱 진입점이다. */
object CampaignE2eDesktopLauncher {
    /** main: 검증 실행 환경을 초기화하고 진입 흐름을 시작한다. */
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

/** CampaignE2eLaunchOptions: 창을 열지 않고도 런처 연결을 검증할 수 있도록 독립적으로 해석한다. */
internal data class CampaignE2eLaunchOptions(
    /** traceConfig: 검증 추적 결과를 담는다. */
    val traceConfig: CampaignE2eTraceConfig,
    /** visible: 해당 검증 조건의 현재 여부를 나타낸다. */
    val visible: Boolean,
) {
    companion object {
        /** parse: 외부 입력을 검증 모델로 해석한다. */
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
