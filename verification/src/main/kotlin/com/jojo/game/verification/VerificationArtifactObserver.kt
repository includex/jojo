package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.RenderCaptureConfiguration
import com.jojo.game.application.runtime.RuntimeArtifactEvent
import com.jojo.game.application.runtime.RuntimeArtifactObserver
import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.RuntimeScreenProbe
import com.jojo.game.application.runtime.TitleRuntimeProbe
import com.jojo.game.presentation.battle.BattleScreen
import com.jojo.game.presentation.battle.preparation.BattlePreparationScreen
import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.verification.title.evidence.TitleRenderEventRecorder

/** Verification-owned filesystem sink for renderer observations. */
internal class VerificationArtifactObserver(
    private val output: RenderCaptureConfiguration,
) : RuntimeArtifactObserver, RuntimeScreenObserver {
    private val titleEvents = TitleRenderEventRecorder()

    override val wantsFrame get() = output.screenshotPath != null || output.rawCapturePath != null
    override val wantsEventLog get() = output.renderEventLogPath != null

    override fun onArtifact(event: RuntimeArtifactEvent) {
        when (event) {
            is RuntimeArtifactEvent.Frame -> writeFrame(event.screen)
            is RuntimeArtifactEvent.EventLog -> output.renderEventLogPath?.let { writeText(it, event.screen.eventLog()) }
            is RuntimeArtifactEvent.MapSidecar -> writeMapSidecar(event.state)
            is RuntimeArtifactEvent.OverlayStack -> writeStack(event)
        }
    }

    /** Title capture policy belongs to the verification runtime, after the frame is rendered. */
    override fun update(delta: Float, screen: RuntimeScreenProbe) {
        val title = screen as? TitleRuntimeProbe ?: return
        if (title.view.elapsedSeconds <= TITLE_ARTIFACT_DELAY_SECONDS) return
        output.renderEventLogPath?.let { path ->
            writeText(path, titleEvents.record(title.view, startItemFixture = output.state == START_ITEM_ROUTE))
            return
        }
        if (wantsFrame) writeFrame(null)
    }

    private fun writeFrame(screen: Screen?) {
        val target = output.screenshotPath ?: return
        val raw = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight)
        output.rawCapturePath?.let { path ->
            val bytes = ByteArray(raw.width * raw.height * 4)
            raw.pixels.rewind(); raw.pixels.get(bytes); raw.pixels.rewind()
            Gdx.files.absolute(path).also { it.parent().mkdirs() }.writeBytes(bytes, false)
        }
        output.compositionTracePath?.let { writeText(it, screen.compositionTrace()) }
        val topDown = Pixmap(raw.width, raw.height, raw.format)
        for (y in 0 until raw.height) for (x in 0 until raw.width) topDown.drawPixel(x, raw.height - 1 - y, raw.getPixel(x, y))
        raw.dispose(); Gdx.files.absolute(target).also { it.parent().mkdirs() }.let { PixmapIO.writePNG(it, topDown) }; topDown.dispose()
        Gdx.app.exit()
    }

    private fun writeMapSidecar(state: String?) {
        val target = output.screenshotPath ?: return
        if (state != "map-only") return
        writeText(target.removeSuffix(".png") + ".sidecar.json", "{\"state\":\"map-only\",\"observer\":\"verification\"}")
    }

    private fun writeStack(event: RuntimeArtifactEvent.OverlayStack) {
        val target = output.screenshotPath ?: return
        val overlays = (if (event.dialogue) 1 else 0) + (if (event.choice) 1 else 0) + event.modalCount
        writeText(target.removeSuffix(".png") + "-stack.json", "{\"requested\":\"${event.requested}\",\"requestedPresent\":${event.requestedPresent},\"activeOverlayCountAfter\":$overlays}")
    }

    private fun writeText(path: String, text: String) {
        Gdx.files.absolute(path).also { it.parent().mkdirs() }.writeString(text, false)
        Gdx.app.exit()
    }

    private companion object {
        const val TITLE_ARTIFACT_DELAY_SECONDS = 1f
        const val START_ITEM_ROUTE = "start-item-fixture"
    }
}

private fun Screen?.eventLog(): String = when (this) {
    is ScenarioScreen -> renderEventLog()
    is BattleScreen -> renderEventLog()
    is BattlePreparationScreen -> renderEventLog()
    else -> "{\"state\":\"unavailable\"}\n"
}

private fun Screen?.compositionTrace(): String = when (this) {
    is ScenarioScreen -> compositionTrace()
    is BattleScreen -> compositionTrace()
    is BattlePreparationScreen -> compositionTrace()
    else -> "{\"state\":\"unavailable\",\"records\":[]}"
}
