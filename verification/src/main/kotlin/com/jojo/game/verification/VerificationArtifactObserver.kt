// Verification
package com.jojo.game.verification

import com.jojo.game.presentation.scenario.hall.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.application.runtime.RenderCaptureConfiguration
import com.jojo.game.application.runtime.RuntimeArtifactEvent
import com.jojo.game.application.runtime.RuntimeArtifactObserver
import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.RuntimeScreenProbe
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.application.runtime.BattlePreparationRuntimeProbe
import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider
import com.jojo.game.application.runtime.TitleRuntimeProbe
import com.jojo.game.presentation.battle.BattleScreen
import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.verification.scenario.evidence.ScenarioCompositionEvidenceRecorder
import com.jojo.game.verification.scenario.evidence.ScenarioEquipConfirmationEvidenceRecorder
import com.jojo.game.verification.scenario.evidence.ScenarioFrameEvidenceRecorder
import com.jojo.game.verification.scenario.evidence.ScenarioPropertyEvidenceRecorder
import com.jojo.game.verification.scenario.evidence.ScenarioStaticHallInfoEvidenceRecorder
import com.jojo.game.verification.scenario.evidence.ScenarioStoryEvidenceRecorder
import com.jojo.game.verification.scenario.evidence.ScenarioTerrainEvidenceRecorder
import com.jojo.game.verification.scenario.evidence.ScenarioTreasureEvidenceRecorder
import com.jojo.game.verification.preparation.BattlePreparationTraceRecorder
import com.jojo.game.verification.cmd.CmdRouteScreen
import com.jojo.game.verification.terminal.TerminalSceneRouteScreen
import com.jojo.game.verification.title.evidence.TitleRenderEventRecorder

/** VerificationArtifactObserver: 렌더러 관찰 결과를 파일로 저장하는 검증 전용 수집기이다. */
internal class VerificationArtifactObserver(
    /** output: 검증 산출물을 저장할 경로를 담는다. */
    private val output: RenderCaptureConfiguration,
) : RuntimeArtifactObserver, RuntimeScreenObserver {
    /** titleEvents: 검증 이벤트 목록을 담는다. */
    private val titleEvents = TitleRenderEventRecorder()
    /** preparationEvents: 검증 이벤트 목록을 담는다. */
    private val preparationEvents = BattlePreparationTraceRecorder()

    /** wantsFrame: 검증 실행 조건을 나타낸다. */
    override val wantsFrame get() = output.screenshotPath != null || output.rawCapturePath != null
    /** wantsEventLog: 검증 이벤트 목록을 담는다. */
    override val wantsEventLog get() = output.renderEventLogPath != null
    /** keepsScenarioOpen: 검증 시나리오 식별자를 담는다. */
    override val keepsScenarioOpen get() = wantsFrame || wantsEventLog
    /** scenarioArtifactSent: 검증 시나리오 식별자를 담는다. */
    private var scenarioArtifactSent = false

    /** 직전 프레임의 렌더 이벤트다. 같은 값이 이어져야 화면이 자리를 잡은 것으로 본다. */
    private var settledEventLog: String? = null

    /** onArtifact: 런타임 이벤트를 받아 검증 산출물을 갱신한다. */
    override fun onArtifact(event: RuntimeArtifactEvent) {
        when (event) {
            is RuntimeArtifactEvent.Frame -> writeFrame(event.screen)
            is RuntimeArtifactEvent.EventLog -> output.renderEventLogPath?.let { writeText(it, event.screen.eventLog(output.state)) }
            is RuntimeArtifactEvent.MapSidecar -> writeMapSidecar(event.state)
            is RuntimeArtifactEvent.OverlayStack -> writeStack(event)
        }
    }

    /**
     * onFrame: 런타임 이벤트를 받아 검증 산출물을 갱신한다.
     *
     * 예전에는 1초가 지나면 바로 적었다. 기계가 바쁘면 그 사이에 요청한 창이 아직 붙지
     * 않아, 창이 통째로 빠진 로그가 남았다(`hall-menu` 등이 잇달아 돌릴 때만 어긋난
     * 까닭이다). 이제 같은 로그가 두 프레임 이어질 때까지 기다린다. 스스로 계속 움직이는
     * 화면도 멈추지 않도록 [SCENARIO_ARTIFACT_TIMEOUT_SECONDS]가 지나면 그대로 적는다.
     */
    override fun onFrame(screen: Screen?, probe: RuntimeScreenProbe) {
        val scenario = probe as? ScenarioRuntimeProbe ?: return
        if (scenario.elapsedSeconds <= TITLE_ARTIFACT_DELAY_SECONDS || scenarioArtifactSent) return
        if (wantsEventLog && scenario.elapsedSeconds <= SCENARIO_ARTIFACT_TIMEOUT_SECONDS) {
            val current = screen.eventLog(output.state)
            if (current != settledEventLog) {
                settledEventLog = current
                return
            }
        }
        scenarioArtifactSent = true
        if (wantsEventLog) onArtifact(RuntimeArtifactEvent.EventLog(output.state, screen))
        else if (wantsFrame) onArtifact(RuntimeArtifactEvent.Frame(output.state, screen))
    }

    /** update: 화면별 산출물 정책은 렌더링 이후 검증 런타임이 소유한다. */
    override fun update(delta: Float, screen: RuntimeScreenProbe) {
        when (screen) {
            is TitleRuntimeProbe -> emitTitleArtifact(screen)
            is BattlePreparationRuntimeProbe -> emitPreparationArtifact(screen)
            else -> Unit
        }
    }

    /** emitTitleArtifact: 검증 화면 이벤트를 수집해 산출물로 전달한다. */
    private fun emitTitleArtifact(title: TitleRuntimeProbe) {
        if (title.view.elapsedSeconds <= TITLE_ARTIFACT_DELAY_SECONDS) return
        output.renderEventLogPath?.let { path ->
            writeText(path, titleEvents.record(title.view, startItemFixture = output.state == START_ITEM_ROUTE))
            return
        }
        if (wantsFrame) writeFrame(null)
    }

    /** emitPreparationArtifact: 검증 화면 이벤트를 수집해 산출물로 전달한다. */
    private fun emitPreparationArtifact(preparation: BattlePreparationRuntimeProbe) {
        output.renderEventLogPath?.let { path ->
            writeText(path, preparationEvents.renderEvents(preparation.view, output.state))
            return
        }
        if (wantsFrame) writeFrame(null, preparationEvents.composition(preparation.view))
    }

    /** writeFrame: 검증 산출물을 지정한 경로에 기록한다. */
    private fun writeFrame(screen: Screen?, composition: String? = null) {
        val target = output.screenshotPath ?: return
        val raw = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight)
        output.rawCapturePath?.let { path ->
            val bytes = ByteArray(raw.width * raw.height * 4)
            raw.pixels.rewind(); raw.pixels.get(bytes); raw.pixels.rewind()
            Gdx.files.absolute(path).also { it.parent().mkdirs() }.writeBytes(bytes, false)
        }
        output.compositionTracePath?.let { persistText(it, composition ?: screen.compositionTrace()) }
        val topDown = Pixmap(raw.width, raw.height, raw.format)
        for (y in 0 until raw.height) for (x in 0 until raw.width) topDown.drawPixel(x, raw.height - 1 - y, raw.getPixel(x, y))
        raw.dispose(); Gdx.files.absolute(target).also { it.parent().mkdirs() }.let { PixmapIO.writePNG(it, topDown) }
        // 캡처 검증기들은 프레임이 실제로 기록됐는지 이 표준 출력 신호로 판단한다.
        Gdx.app.log(
            "JojoGame",
            "RENDER_CAPTURE_OK: state=${output.state ?: ""} size=${topDown.width}x${topDown.height} path=$target",
        )
        topDown.dispose()
        Gdx.app.exit()
    }

    /** writeMapSidecar: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun writeMapSidecar(state: String?) {
        val target = output.screenshotPath ?: return
        if (state != "map-only") return
        writeText(target.removeSuffix(".png") + ".sidecar.json", "{\"state\":\"map-only\",\"observer\":\"verification\"}")
    }

    /** writeStack: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun writeStack(event: RuntimeArtifactEvent.OverlayStack) {
        val target = output.screenshotPath ?: return
        val overlays = (if (event.dialogue) 1 else 0) + (if (event.choice) 1 else 0) + event.modalCount
        writeText(target.removeSuffix(".png") + "-stack.json", "{\"requested\":\"${event.requested}\",\"requestedPresent\":${event.requestedPresent},\"activeOverlayCountAfter\":$overlays}")
    }

    /** writeText: 검증 산출물을 지정한 경로에 기록한다. */
    private fun writeText(path: String, text: String) {
        persistText(path, text)
        Gdx.app.exit()
    }

    /** persistText: 검증 입력을 처리하고 관련 상태를 갱신한다. */
    private fun persistText(path: String, text: String) {
        Gdx.files.absolute(path).also { it.parent().mkdirs() }.writeString(text, false)
    }

    private companion object {
        /** TITLE_ARTIFACT_DELAY_SECONDS: 검증 대상의 현재 상태 값을 담는다. */
        const val TITLE_ARTIFACT_DELAY_SECONDS = 1f

        /**
         * 렌더 이벤트가 잦아들기를 기다리는 한도다.
         *
         * 원본이 스스로 움직이는 화면(설정 창의 깜빡이는 상자 등)은 두 프레임이 같아지지
         * 않는다. 이 시각이 지나면 그대로 적어 검증이 멈추지 않게 한다.
         */
        const val SCENARIO_ARTIFACT_TIMEOUT_SECONDS = 4f
        /** START_ITEM_ROUTE: 검증 화면 경로를 담는다. */
        const val START_ITEM_ROUTE = "start-item-fixture"
    }
}

/**
 * `Screen`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

private fun Screen?.eventLog(state: String?): String = when (this) {
    is CmdRouteScreen -> renderEventLog()
    is TerminalSceneRouteScreen -> renderEventLog()
    is ScenarioScreen -> scenarioEventLog(runtimeSnapshot(), state)
    is BattleScreen -> renderEventLog()
    // 픽스처 화면 15종은 `RuntimeRenderEventLogProvider`로 자기 렌더 이벤트를 내놓는데
    // 여기서 그 계약을 묻지 않아 전부 `{"state":"unavailable"}`만 남기고 있었다.
    is RuntimeRenderEventLogProvider -> runtimeRenderEventLog()
    else -> "{\"state\":\"unavailable\"}\n"
}

/**
 * `Screen`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

private fun Screen?.compositionTrace(): String = when (this) {
    is ScenarioScreen -> ScenarioCompositionEvidenceRecorder().record(runtimeSnapshot().composition)
    is BattleScreen -> compositionTrace()
    else -> "{\"state\":\"unavailable\",\"records\":[]}"
}

/** scenarioEventLog: 검증 입력을 처리하고 관련 상태를 갱신한다. */
private fun scenarioEventLog(snapshot: com.jojo.game.presentation.scenario.ScenarioRuntimeSnapshot, state: String?): String = when (state) {
    "street-walk-direction-fixture" -> com.jojo.game.presentation.scenario.hall.HallUnitRender.walkingRenderEventLog()
    "street-walk-motion-fixture" -> com.jojo.game.presentation.scenario.hall.HallUnitRender.walkingMotionRenderEventLog()
    else -> ScenarioFrameEvidenceRecorder(
        ScenarioStoryEvidenceRecorder(),
        ScenarioStaticHallInfoEvidenceRecorder(),
        ScenarioPropertyEvidenceRecorder(),
        ScenarioTerrainEvidenceRecorder(),
        ScenarioTreasureEvidenceRecorder(),
    ).record(snapshot.frame)
}
