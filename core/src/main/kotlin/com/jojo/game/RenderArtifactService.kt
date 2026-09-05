package com.jojo.game
import com.jojo.game.presentation.battle.edit.*

import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.presentation.title.TitleScreen
import com.jojo.game.presentation.battle.BattleScreen
import com.jojo.game.presentation.battle.preparation.BattlePreparationScreen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.ScreenUtils

/**
 * Owns the desktop-only rendering evidence boundary.
 *
 * Game flow may request an artifact through [JojoGame], but it must not need
 * to know how a framebuffer is read, serialized, or normalized. Keeping that
 * responsibility here also makes capture paths explicitly opt-in rather than
 * part of ordinary gameplay state.
 */
internal class RenderArtifactService(
    private val configuration: RenderCaptureConfiguration,
) {
    private val screenshotPath get() = configuration.screenshotPath
    private val rawCapturePath get() = configuration.rawCapturePath
    private val compositionTracePath get() = configuration.compositionTracePath
    private val renderEventLogPath get() = configuration.renderEventLogPath
    private val captureState get() = configuration.state
    private val mapTextureDumpPath get() = configuration.mapTextureDumpPath
    private val mapDither get() = configuration.mapDither
    private val mapFilter get() = configuration.mapFilter
    private val mapSampler get() = configuration.mapSampler
    private val mapSampleOffset get() = configuration.mapSampleOffset

    /**
     * 공개 메서드 `hasFrameCaptureRequest`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun hasFrameCaptureRequest(): Boolean = screenshotPath != null || rawCapturePath != null

    /**
     * 공개 메서드 `hasRenderEventLogRequest`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun hasRenderEventLogRequest(): Boolean = renderEventLogPath != null

    /**
     * 공개 메서드 `requestedMapTextureDumpPath`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedMapTextureDumpPath(): String? = mapTextureDumpPath

    /**
     * 공개 메서드 `requestedMapDither`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedMapDither(): Boolean? = when (mapDither) {
        null -> null
        "enabled" -> true
        "disabled" -> false
        else -> error("--map-dither must be enabled or disabled")
    }

    /**
     * 공개 메서드 `requestedMapFilter`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture.TextureFilter?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedMapFilter(): Texture.TextureFilter? = when (mapFilter) {
        null -> null
        "linear" -> Texture.TextureFilter.Linear
        "nearest" -> Texture.TextureFilter.Nearest
        else -> error("--map-filter must be linear or nearest")
    }

    /**
     * 공개 메서드 `requestedCocos8MapSampler`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedCocos8MapSampler(): Boolean = when (mapSampler) {
        null, "cocos8", "frag8" -> true
        "linear" -> false
        else -> error("--map-sampler must be linear, cocos8, or frag8")
    }

    /**
     * 공개 메서드 `requestedFragmentCoordinateMapSampler`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedFragmentCoordinateMapSampler(): Boolean = mapSampler == null || mapSampler == "frag8"

    /**
     * 공개 메서드 `requestedMapSampleOffset`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Pair<Float, Float>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestedMapSampleOffset(): Pair<Float, Float> = mapSampleOffset ?: (0f to 0f)

    /**
     * 공개 메서드 `writeRenderEventLogIfRequested`
     *
     * ### 파라미터
    - `screen` (`Screen?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun writeRenderEventLogIfRequested(screen: Screen?): Boolean {
        val path = renderEventLogPath ?: return false
        val jsonl = screen.renderEventLog()
        Gdx.files.absolute(path).also { it.parent().mkdirs() }.writeString(jsonl, false)
        Gdx.app.log("JojoGame", "RENDER_EVENT_LOG_OK: $path")
        Gdx.app.exit()
        return true
    }

    /**
     * 공개 메서드 `captureFrameIfRequested`
     *
     * ### 파라미터
    - `screen` (`Screen?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun captureFrameIfRequested(screen: Screen?): Boolean {
        val path = screenshotPath ?: return false
        normalizeOpaqueDialogueAlpha()
        val raw = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight)
        rawCapturePath?.let { rawPath -> writeRawCapture(raw, rawPath) }
        compositionTracePath?.let { tracePath ->
            Gdx.files.absolute(tracePath).writeString(screen.compositionTrace(), false)
        }
        writeTopDownPng(raw, path)
        writeUnitInfoStackIfNeeded(path)
        Gdx.app.log("JojoGame", "RENDER_CAPTURE_OK: $path")
        Gdx.app.exit()
        return true
    }

    /**
     * 공개 메서드 `writeMapQuadCandidateSidecar`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun writeMapQuadCandidateSidecar() {
        val png = screenshotPath ?: return
        if (captureState != "map-only") return
        Gdx.files.absolute(png.removeSuffix(".png") + ".sidecar.json").writeString(
            """{"fixtureVersion":1,"state":"R_00-postload-map-only","candidate":"libgdx-cocos-map-quad-final-draw","mapTexture":{"uuid":"4afa0804-1ac2-4d59-97e4-1549a9425953","nativePath":"assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg","size":[960,960]},"mapSpriteFrame":{"rect":[0,0,960,960],"uv":[0,1,1,1,0,0,1,0]},"mapMaterial":{"name":"builtin-2d-sprite","blend":"SRC_ALPHA,ONE_MINUS_SRC_ALPHA","filter":"LINEAR","wrap":"CLAMP_TO_EDGE","sampler":"cocos-8bit-rounded-bilinear/physical-pixel-centre","sampleCenterOffset":[0.0,0.0]},"runtime":{"visible":[1488.3721,800.0],"design":[1280.0,800.0],"drawingBuffer":[${Gdx.graphics.backBufferWidth},${Gdx.graphics.backBufferHeight}],"contentPosition":[-104.18605,0.0],"map":{"active":true,"position":[0.0,0.0],"size":[960.0,960.0],"scale":[2.0,2.0],"anchor":[0.5,0.5]},"mapAncestorTransforms":[{"name":"Canvas","projection":"SHOW_ALL/orthographic"},{"name":"ScrollView","stencilClip":true,"view":[1488.3721,800.0]},{"name":"content","position":[-104.18605,0.0]},{"name":"map","draw":[-320.0,-560.0,1920.0,1920.0],"rasterSampleOffset":[0.0,0.0]}],"activeVisualPaths":["Battle/Canvas/Layer/ScrollView/view/content/map"]}}""",
            false,
        )
    }

    /**
     * 공개 메서드 `writeCaptureStack`
     *
     * ### 파라미터
    - `requested` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `requestedPresent` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `dialogue` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `choice` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `modalCount` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun writeCaptureStack(
        requested: String,
        requestedPresent: Boolean,
        dialogue: Boolean,
        choice: Boolean,
        modalCount: Int
    ) {
        val png = screenshotPath ?: return
        val overlayCount = (if (dialogue) 1 else 0) + (if (choice) 1 else 0) + modalCount
        Gdx.files.absolute(png.removeSuffix(".png") + "-stack.json").writeString(
            """{"requested":"$requested","state":"open","requestedPresent":$requestedPresent,"activeDialogueOverlayCount":${if (dialogue) 1 else 0},"activeChoiceOverlayCount":${if (choice) 1 else 0},"activeModalOverlayCount":$modalCount,"activeOverlayCountAfter":$overlayCount}""",
            false,
        )
    }

    private fun normalizeOpaqueDialogueAlpha() {
        if (captureState != "yingchuan-dialogue-1") return
        Gdx.gl.glColorMask(false, false, false, true)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glColorMask(true, true, true, true)
    }

    private fun writeRawCapture(raw: Pixmap, path: String) {
        val bytes = ByteArray(raw.width * raw.height * 4)
        raw.pixels.rewind()
        raw.pixels.get(bytes)
        raw.pixels.rewind()
        Gdx.files.absolute(path).writeBytes(bytes, false)
    }

    private fun writeTopDownPng(raw: Pixmap, path: String) {
        val pixmap = Pixmap(raw.width, raw.height, raw.format)
        for (y in 0 until raw.height) for (x in 0 until raw.width) {
            pixmap.drawPixel(x, raw.height - 1 - y, raw.getPixel(x, y))
        }
        raw.dispose()
        PixmapIO.writePNG(Gdx.files.absolute(path), pixmap)
        pixmap.dispose()
    }

    private fun writeUnitInfoStackIfNeeded(path: String) {
        if (captureState != "yingchuan-unit-info") return
        Gdx.files.absolute(path.removeSuffix(".png") + "-stack.json").writeString(
            "{\"captureState\":\"yingchuan-unit-info\",\"root\":{\"width\":1488.3720930232557,\"height\":800},\"bg1\":{\"width\":1094,\"height\":776,\"position\":[0,0]},\"selectedRoute\":\"ForcesListLayer.content.children[0] TOUCH_END\",\"game\":true}",
            false,
        )
    }
}

private fun Screen?.renderEventLog(): String = when (this) {
    is TitleScreen -> renderEventLog()
    is ScenarioScreen -> renderEventLog()
    is BattlePreparationScreen -> renderEventLog()
    is RewardFixtureScreen -> renderEventLog()
    is SystemOverlayFixtureScreen -> renderEventLog()
    is DialogueFixtureScreen -> renderEventLog()
    is Choose2FixtureScreen -> renderEventLog()
    is InputBoxFixtureScreen -> renderEventLog()
    is MsgBox3FixtureScreen -> renderEventLog()
    is EditRosterRouteScreen -> renderEventLog()
    is BattleUnitEditRouteScreen -> renderEventLog()
    is LearnUnitSkillRouteScreen -> renderEventLog()
    is DefineUnitRouteScreen -> renderEventLog()
    is CmdRouteScreen -> renderEventLog()
    is ModalLoadRouteScreen -> renderEventLog()
    is NoticeInfoFixtureScreen -> renderEventLog()
    is TerminalSceneRouteScreen -> renderEventLog()
    is RaffleGateRouteScreen -> renderEventLog()
    is AchievementsFixtureScreen -> renderEventLog()
    is AttributeFixtureScreen -> renderEventLog()
    is GenericListFixtureScreen -> renderEventLog()
    is BattleScreen -> renderEventLog()
    else -> RenderEventLog().apply {
        draw(
            "state",
            this@renderEventLog?.javaClass?.simpleName ?: "none",
            "Canvas",
            "none",
            0f,
            0f,
            0f,
            0f,
            visible = false
        )
    }.jsonl()
}

private fun Screen?.compositionTrace(): String = when (this) {
    is BattleScreen -> compositionTrace()
    is InfoLayerFixtureScreen -> compositionTrace()
    is ScenarioScreen -> compositionTrace()
    is BattlePreparationScreen -> compositionTrace()
    else -> "{\"state\":\"unavailable\",\"records\":[]}"
}
