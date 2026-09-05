package com.jojo.game.presentation.title

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.jojo.game.LoginOptionalOverlayRoute
import com.jojo.game.presentation.title.assets.TitleSceneAssets

/** Draws immutable title snapshots without mutating title flows or navigating the game. */
internal class TitleSceneRenderer(private val assets: TitleSceneAssets) {
    private val viewport = ScreenViewport(OrthographicCamera())
    private val batch = SpriteBatch()

    /**
     * 공개 메서드 `render`
     *
     * ### 파라미터
    - `state` (`TitleViewState`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun render(state: TitleViewState) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.enableBlending()
        batch.begin()
        drawLogin()
        batch.end()
        if (state.mode != TitleMode.LOGIN) drawDim(if (state.mode == TitleMode.LOAD) 100f / 255f else 30f / 255f)
        batch.enableBlending()
        batch.begin()
        when (state.mode) {
            TitleMode.LOGIN -> Unit
            TitleMode.LOAD -> drawLoadOverlay(state)
            TitleMode.SETTING -> drawSettingOverlay(state)
        }
        state.optionalOverlayRoute?.let(::drawOptionalOverlay)
        state.loadConfirmationMessage?.takeIf { state.mode == TitleMode.LOAD }?.let(::drawLoadConfirmation)
        state.registrationLoading?.let { drawRegistrationLoading(it, state.elapsedSeconds) }
        batch.end()
    }

    /**
     * 공개 메서드 `resize`
     *
     * ### 파라미터
    - `width` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `height` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun resize(width: Int, height: Int) = viewport.update(width, height, true)

    /**
     * 공개 메서드 `dispose`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun dispose() = batch.dispose()

    private fun drawLogin() {
        batch.draw(assets.loginBackground, 0f, 0f, viewport.worldWidth, viewport.worldHeight)
        assets.loginButtons.forEachIndexed { index, texture ->
            val sourceY = floatArrayOf(582f, 456f, 329f, 203f)[index]
            batch.draw(texture, 945.46f, sourceY * SCALE - 37.84f, 302.72f, 75.68f)
        }
    }

    private fun drawDim(alpha: Float) {
        batch.projectionMatrix = viewport.camera.combined
        batch.enableBlending()
        batch.begin()
        batch.color = Color(0f, 0f, 0f, alpha)
        batch.draw(assets.dimPixel, 0f, 0f, viewport.worldWidth, viewport.worldHeight)
        batch.color = Color.WHITE
        batch.end()
    }

    private fun drawLoadOverlay(state: TitleViewState) {
        drawTiled(assets.loadLogo9, 239.24f, 83.85f, 801.52f, 520.3f)
        assets.loadOuterPatch.draw(batch, 239.24f, 83.85f, 801.52f, 520.3f)
        assets.loadTitlePatch.draw(batch, 239.24f, 561.15f, 801.52f, 43f)
        drawLabelSource("진행도 불러오기", 409.841f, 677.5f, 253.31f)
        assets.uiFont.color = Color.BLACK
        assets.uiFont.draw(batch, "읽을 진행 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다.", 246.64f, 554.91f)
        assets.loadBoxPatch.draw(batch, 246.56f, 149.64f, 784.32f, 368.08f)
        state.loadRows.take(8).forEachIndexed { index, row ->
            val cy = 574f - index * 52f
            assets.loadRowPatch.draw(batch, 248.49f, (cy - 25f) * SCALE, 780.88f, 43f)
            assets.uiFont.color = if (row.occupied) Color.BLACK else Color(146f / 255f, 146f / 255f, 146f / 255f, 1f)
            drawLabelSource(row.number, 354.858f, cy, 117.85f)
            drawLabelSource(row.stage, 495.686f, cy, 137.6f)
            assets.uiFont.draw(batch, row.name, 578.186f * SCALE, cy * SCALE + 13f)
        }
        assets.loadVlinePatch.draw(batch, (421.75f - 3f) * SCALE, (388f - 211.9f) * SCALE, 5.16f, 364.47f)
        assets.loadVlinePatch.draw(batch, (567.515f - 3f) * SCALE, (388f - 211.9f) * SCALE, 5.16f, 364.47f)
        assets.loadOuterPatch.draw(batch, (1125.186f - 73.8f) * SCALE, (137f - 30f) * SCALE, 126.94f, 51.6f)
        assets.uiFont.color = Color.BLACK
        drawCenteredLabelSource("취소", 1125.186f, 139.764f)
    }

    private fun drawLoadConfirmation(messageText: String) {
        drawTiled(assets.loadLogo9, 366.95f, 216.72f, 546.1f, 254.56f)
        assets.loadOuterPatch.draw(batch, 366.95f, 216.72f, 546.1f, 254.56f)
        drawSourceNode(assets.loadEagle, 506.005f, 435.951f, 106f, 124f)
        val message = messageText.replace("있나요?", "있\n나요?")
        assets.uiFont.color = Color(0f, 4f / 255f, 196f / 255f, 1f)
        assets.uiFont.draw(batch, message, (805.186f - 231.5f) * SCALE, 404f, 463f * SCALE, Align.left, true)
        assets.loadOuterPatch.draw(batch, (644.186f - 90f) * SCALE, (296.285f - 25f) * SCALE, 154.8f, 43f)
        assets.uiFont.color = Color.RED
        drawCenteredLabelSource("취소", 644.186f, 299.085f)
        assets.loadOuterPatch.draw(batch, (844.186f - 90f) * SCALE, (296.285f - 25f) * SCALE, 154.8f, 43f)
        assets.uiFont.color = Color(10f / 255f, 105f / 255f, 0f, 1f)
        drawCenteredLabelSource("불러오기", 844.186f, 299.085f)
        assets.uiFont.color = Color.BLACK
    }

    private fun drawSettingOverlay(state: TitleViewState) {
        val view = requireNotNull(state.settings)
        drawTiled(assets.settingLogo9, 195.686f * SCALE, 41f * SCALE, 1097f * SCALE, 718f * SCALE)
        drawTiled(assets.settingBox1, 195.686f * SCALE, 41f * SCALE, 1097f * SCALE, 718f * SCALE)
        batch.draw(assets.settingTitle, 195.686f * SCALE, 709f * SCALE, 1097f * SCALE, 50f * SCALE)
        assets.uiFont.color = Color.BLACK
        drawLabelSource("환경 설정", 275.441f, 734f, 149.51f)
        assets.settingBox2Patch.draw(batch, 203.686f * SCALE, 110f * SCALE, 1081f * SCALE, 596f * SCALE)
        assets.uiFont.draw(batch, "항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.", 65.851f * SCALE, 650.189f * SCALE + 13f)
        val toggleCenters =
            listOf(232.29f to 625f, 232.29f to 560f, 232.186f to 496f, 232.186f to 431f, 232.186f to 367f)
        val labels = listOf("배경 음악 듣기", "효과음 듣기", "전투 시 전장 축소 이미지가 자동으로 표시됩니다.", "대화창 자동 닫힘", "체력 바가 유닛 위에 있습니다")
        toggleCenters.forEachIndexed { index, (cx, cy) ->
            drawSourceNode(assets.settingToggle, cx, cy, 28f, 28f)
            if (view.flags and (1 shl index) != 0) drawSourceNode(assets.settingCheck, cx, cy, 28f, 28f)
            drawLabelSource(labels[index], if (index < 2) 515.29f else 515.186f, cy, 526f)
        }
        drawSettingPanel(570.389f, "텍스트 속도", 620.681f, 166f)
        drawRadios(561.638f, view.messageSpeed, listOf("느림", "중", "빠르게"))
        drawSettingPanel(438.389f, "게임 속도", 488.681f, 166f)
        assets.settingSliderPatch.draw(batch, (1033.186f - 217f) * SCALE, (428.346f - 10f) * SCALE, 373.24f, 17.2f)
        drawSourceNode(assets.settingRadioOff, 816.186f + 434f * view.gameSpeed, 428.346f, 32f, 32f)
        drawSettingPanel(306.389f, "정보 설명", 356.681f, 166f)
        drawRadios(297.638f, view.notificationLevel, listOf("자세히", "보통", "요약"))
        assets.settingBox1Patch.draw(batch, (1033.186f - 239.85f) * SCALE, (152.389f - 71f) * SCALE, 412.54f, 122.12f)
        batch.draw(assets.settingTitle, 833.325f * SCALE, 198.167f * SCALE, 210f * SCALE, 50f * SCALE)
        drawLabelSource("대화창 색상", 938.325f, 223.167f, 184.11f)
        listOf(881.12f, 983.12f, 1085.12f, 1187.12f).forEachIndexed { index, cx ->
            assets.settingBox1Patch.draw(batch, (cx - 50f) * SCALE, (142.703f - 50f) * SCALE, 86f, 86f)
            drawSourceNode(assets.settingStyles[index], cx, 142.703f, 96f, 96f)
            if (view.background == index) {
                batch.color = Color(1f, 1f, 1f, if (state.optionalOverlayRoute != null) 1f else .333f)
                drawTiled(assets.settingBox6, (cx - 50f) * SCALE, (142.703f - 50f) * SCALE, 100f * SCALE, 100f * SCALE)
                batch.color = Color.WHITE
            }
        }
        assets.settingButtonPatch.draw(batch, (1208.186f - 78f) * SCALE, (75f - 28f) * SCALE, 134.16f, 48.16f)
        drawCenteredLabelSource("확인", 1208.186f, 77.261f)
    }

    private fun drawSettingPanel(cy: Float, title: String, titleCy: Float, titleWidth: Float) {
        assets.settingBox1Patch.draw(batch, (1033.186f - 239.85f) * SCALE, (cy - 50f) * SCALE, 412.54f, 86f)
        batch.draw(
            assets.settingTitle,
            (914.428f - titleWidth / 2f) * SCALE,
            (titleCy - 25f) * SCALE,
            titleWidth * SCALE,
            50f * SCALE
        )
        drawLabelSource(title, 914.428f, titleCy, if (title == "텍스트 속도") 184.11f else 149.51f)
    }

    private fun drawRadios(cy: Float, selected: Int, labels: List<String>) {
        val centers = listOf(834.346f, 990.286f, 1135.419f)
        val labelCenters = listOf(900.731f, 1069.671f, 1214.804f)
        centers.forEachIndexed { index, cx ->
            drawSourceNode(assets.settingRadioOff, cx, cy, 32f, 32f)
            if (index == selected) drawSourceNode(assets.settingRadioOn, cx, cy, 32f, 32f)
            drawLabelSource(labels[index], labelCenters[index], cy, 90f)
        }
    }

    private fun drawOptionalOverlay(route: LoginOptionalOverlayRoute) {
        batch.color = Color(0f, 0f, 0f, .392f)
        batch.draw(assets.dimPixel, 0f, 0f, 1280f, 688f)
        batch.color = Color.WHITE
        val signIn = route == LoginOptionalOverlayRoute.SIGNIN_OPEN
        val x = if (signIn) 127f else 259f
        val y = if (signIn) 21f else 73f
        val w = if (signIn) 1026f else 762f
        val h = if (signIn) 646f else 543f
        drawTiled(assets.settingLogo9, x, y, w, h)
        assets.settingBox1Patch.draw(batch, x, y, w, h)
        assets.settingBox1Patch.draw(batch, x, y + h - 52f, w, 52f)
    }

    private fun drawRegistrationLoading(view: TitleLoadingView, elapsed: Float) {
        batch.color = Color(0f, 0f, 0f, view.blockerOpacity)
        batch.draw(assets.dimPixel, 0f, 0f, viewport.worldWidth, viewport.worldHeight)
        batch.color = Color.WHITE
        if (view.imageVisible) batch.draw(
            assets.loadingSpinner, 605f, 309f, 35f, 35f, 70f, 70f, 1f, 1f,
            (elapsed * 360f) % 360f, 0, 0, assets.loadingSpinner.width, assets.loadingSpinner.height, false, false
        )
    }

    private fun drawSourceNode(texture: Texture, cx: Float, cy: Float, width: Float, height: Float) =
        batch.draw(texture, (cx - width / 2f) * SCALE, (cy - height / 2f) * SCALE, width * SCALE, height * SCALE)

    private fun drawLabelSource(text: String, cx: Float, cy: Float, width: Float) =
        assets.uiFont.draw(batch, text, (cx - width / 2f) * SCALE, cy * SCALE + 13f)

    private fun drawCenteredLabelSource(text: String, cx: Float, cy: Float) =
        assets.uiFont.draw(batch, text, (cx - 100f) * SCALE, cy * SCALE + 13f, 200f * SCALE, Align.center, false)

    private fun drawTiled(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
        val tileWidth = texture.width * SCALE
        val tileHeight = texture.height * SCALE
        var dy = 0f
        while (dy < height - .01f) {
            val drawnHeight = minOf(tileHeight, height - dy)
            val sourceHeight = (drawnHeight / SCALE).toInt().coerceIn(1, texture.height)
            var dx = 0f
            while (dx < width - .01f) {
                val drawnWidth = minOf(tileWidth, width - dx)
                val sourceWidth = (drawnWidth / SCALE).toInt().coerceIn(1, texture.width)
                batch.draw(
                    texture,
                    x + dx,
                    y + dy,
                    drawnWidth,
                    drawnHeight,
                    0,
                    0,
                    sourceWidth,
                    sourceHeight,
                    false,
                    false
                )
                dx += tileWidth
            }
            dy += tileHeight
        }
    }

    private companion object {
        const val SCALE = .86f
    }
}
