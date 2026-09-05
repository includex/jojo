package com.jojo.port

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.ScreenViewport

/** Source-compatible Login scene with the original authored sprites. */
class TitleScreen(
    private val game: JojoGame,
    initialSettingOpen: Boolean = false,
    initialLoadOpen: Boolean = false,
    initialLoadRow: Int? = null,
    private val optionalOverlayRoute: LoginOptionalOverlayRoute? = null,
    private val settingSceneName: String = "Login",
    private val settingReturnScenario: String? = null,
) : ScreenAdapter() {
    private val viewport = ScreenViewport(OrthographicCamera())
    private val batch = SpriteBatch()
    private val shapes = ShapeRenderer()
    private val dimPixel = Texture(Pixmap(1, 1, Pixmap.Format.RGBA8888).also {
        it.setColor(Color.WHITE)
        it.fill()
    })
    private val loginBackground = Texture(Gdx.files.internal("maps/ui/title/background.jpg")).also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }
    private val loginButtons = (0..3).map { index ->
        Texture(Gdx.files.internal("maps/ui/title/button$index.png")).also {
            it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
    }
    private val uiTextures = mutableListOf<Texture>()
    private fun uiTexture(path: String): Texture = Texture(Gdx.files.internal(path)).also {
        it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        uiTextures += it
    }
    private val loadLogo9 = uiTexture("maps/ui/title/load/logo9.png")
    private val loadButton = uiTexture("maps/ui/title/load/button.png")
    private val loadTitle = uiTexture("maps/ui/title/load/title.png")
    private val loadBox2 = uiTexture("maps/ui/title/load/box2.png")
    private val loadRow = uiTexture("maps/ui/title/load/row.png")
    private val loadVline = uiTexture("maps/ui/title/load/vline.png")
    private val loadEagle = uiTexture("maps/ui/title/load/eagle.png")
    private val loadOuterPatch = NinePatch(loadButton, 9, 42, 42, 7)
    private val loadTitlePatch = NinePatch(loadTitle, 5, 10, 10, 5)
    private val loadBoxPatch = NinePatch(loadBox2, 3, 14, 14, 3)
    private val loadRowPatch = NinePatch(loadRow, 1, 18, 18, 1)
    private val loadVlinePatch = NinePatch(loadVline, 0, 6, 37, 2)

    private val settingLogo9 = uiTexture("maps/ui/title/setting/logo9.png")
    private val settingBox1 = uiTexture("maps/ui/title/setting/box1.png")
    private val settingTitle = uiTexture("maps/ui/title/setting/title.png")
    private val settingBox2 = uiTexture("maps/ui/title/setting/box2.png")
    private val settingToggle = uiTexture("maps/ui/title/setting/toggle.png")
    private val settingCheck = uiTexture("maps/ui/title/setting/check.png")
    private val settingRadioOff = uiTexture("maps/ui/title/setting/radio-off.png")
    private val settingRadioOn = uiTexture("maps/ui/title/setting/radio-on.png")
    private val settingSlider = uiTexture("maps/ui/title/setting/slider.png")
    private val settingBox6 = uiTexture("maps/ui/title/setting/box6.png")
    private val settingStyles = (0..3).map { uiTexture("maps/ui/title/setting/style$it.png") }
    private val settingButton = uiTexture("maps/ui/title/setting/button.png")
    private val loadingSpinner = uiTexture("maps/ui/system-overlay/uiloading.png")
    private val settingBox1Patch = NinePatch(settingBox1, 3, 14, 14, 3)
    private val settingBox2Patch = NinePatch(settingBox2, 3, 14, 14, 3)
    private val settingSliderPatch = NinePatch(settingSlider, 10, 10, 7, 4)
    private val settingButtonPatch = NinePatch(settingButton, 9, 42, 42, 7)
    private val uiGlyphs = "진행도 불러오기읽을 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다.취소환경 설정항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.배경 음악 듣기효과음 듣기전투 시 전장 축소 이미지가 자동으로 표시됩니다.대화창 자동 닫힘체력 바가 유닛 위에 있습니다텍스트 속도느림중빠르게게임 속도정보 설명자세히보통요약대화창 색상확인No.---전역영천의 전투불러올 수 있나요?0123456789:()제턴 "
    private val uiFont: BitmapFont = KoreanFont.create(34, uiGlyphs, fillColor = Color.WHITE)
    private var elapsed = 0f
    private var mode = when { initialLoadOpen -> Mode.LOAD; initialSettingOpen || optionalOverlayRoute != null -> Mode.SETTING; else -> Mode.LOGIN }
    private var pressedLoadSlot: Int? = null
    private var settingChanged = false
    private val loadLayer = game.titleLoadGameLayer()
    private val titlePreferences = game.preferences("jojo-original-settings")
    private val settingLayer = SettingLayer(object : SettingLayer.Store {
        override fun getInt(key: String, default: Int): Int =
            if (optionalOverlayRoute != null || game.requestedCaptureState() == "login-setting") {
                default
            } else {
                titlePreferences.getInteger(key, default)
            }
        override fun putInt(key: String, value: Int) { titlePreferences.putInteger(key, value).flush() }
    }, featureEnvironment = { game.settingFeatureEnvironment(settingSceneName) })
    private val registrationCheck = LoginRegistrationCheckFlow(
        pending = titlePreferences.getInteger(CHECK_REGISTER, 0) != 0,
        clearPending = { titlePreferences.remove(CHECK_REGISTER); titlePreferences.flush() },
        requestCheck = game::requestRegistrationCheck,
        onRegistered = { Gdx.app.log("JojoPort", "registration check accepted") },
    )

    init {
        registrationCheck.start()
        if (mode == Mode.LOAD) {
            loadLayer.onCreate()
            initialLoadRow?.let { loadLayer.onRowTouch(it, LoadGameLayer.TOUCH_END) }
        }
        if (mode == Mode.SETTING) settingLayer.onCreate()
    }

    init {
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun keyDown(keycode: Int): Boolean {
                if (registrationCheck.loading != null) return true
                when {
                    keycode == Input.Keys.ESCAPE && mode == Mode.LOGIN -> Gdx.app.exit()
                    keycode == Input.Keys.ESCAPE && mode == Mode.LOAD && loadLayer.pendingSlot() != null -> loadLayer.onConfirm(1)
                    keycode == Input.Keys.ESCAPE -> closeOverlay()
                    (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) && mode == Mode.LOGIN ->
                        activate(TitleInteraction.MainAction.NEW_GAME)
                    (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) && mode == Mode.SETTING -> closeOverlay()
                    (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) && mode == Mode.LOAD && loadLayer.pendingSlot() != null -> loadLayer.onConfirm(0)
                }
                return true
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                if (registrationCheck.loading != null) return true
                // Input and rendering share the 1280x688 logical Cocos-port
                // space. macOS may expose a 2560x1376 Retina framebuffer, but
                // InputAdapter still reports logical window coordinates.
                val x = screenX * LOGICAL_WIDTH / Gdx.graphics.width
                val y = (Gdx.graphics.height - screenY) * LOGICAL_HEIGHT / Gdx.graphics.height
                when (mode) {
                    Mode.LOGIN -> TitleInteraction.mainActionAt(x, y)?.let(::activate)
                    Mode.SETTING -> handleSettingTap(x, y)
                    Mode.LOAD -> handleLoadTap(x, y)
                }
                return true
            }
        }
    }

    override fun render(delta: Float) {
        elapsed += delta
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        // Cocos Login sprites use the normal SRC_ALPHA / ONE_MINUS_SRC_ALPHA
        // blend state even though the background itself is opaque.
        batch.enableBlending()
        batch.begin()
        drawLogin()
        batch.end()
        if (mode != Mode.LOGIN) drawDim(if (mode == Mode.LOAD) 100f / 255f else 30f / 255f)
        batch.enableBlending()
        batch.begin()
        when (mode) {
            Mode.LOGIN -> Unit
            Mode.LOAD -> drawLoadOverlay()
            Mode.SETTING -> drawSettingOverlay()
        }
        optionalOverlayRoute?.let(::drawOptionalOverlay)
        if (mode == Mode.LOAD && loadLayer.pendingSlot() != null) drawLoadConfirmation()
        registrationCheck.loading?.let(::drawRegistrationLoading)
        batch.end()
        // Let the launcher capture the fully rendered title scene using the same
        // framebuffer path as the other parity fixtures.
        if (elapsed > 1f && game.writeRenderEventLogIfRequested()) return
        if (elapsed > 1f && game.captureFrameIfRequested()) return
    }

    /** Events mirror the draw order above; coordinates are the submitted 1280x688 quads. */
    fun renderEventLog(): String {
        if (game.requestedCaptureState() == "start-item-fixture") return StartItemRenderEvents.jsonl()
        val log = RenderEventLog()
        log.draw("login-main-stable", "HallLayer", "Canvas/bg", "sprite", 0f, 0f, 1280f, 688f,
            "assets/resources/native/4d/4debf9ca-54d9-48e2-855c-34ef06c80bc4.5e28d.jpg#Logo_1-1")
        if (optionalOverlayRoute == null) {
            log.draw("login-main-stable", "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1280f, 688f,
                "default_sprite_splash", opacity = 0f, visible = false)
        }
        val sourceY = floatArrayOf(582f, 456f, 329f, 203f)
        sourceY.forEachIndexed { index, sy ->
            log.draw("login-main-stable", "Login", "Canvas/Layer/bg1/button$index/Background", "sliced-sprite", 945.46f, sy * .86f - 37.84f,
                302.72f, 75.68f, "U_select_12-1_$index")
        }
        if (mode != Mode.LOGIN) {
            val opacity = if (mode == Mode.LOAD) .392f else .118f
            log.draw("overlay", mode.name, "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1280f, 688f,
                "default_sprite_splash", opacity = opacity)
        }
        when (mode) {
            Mode.LOGIN -> Unit
            Mode.LOAD -> appendLoadRenderEvents(log)
            Mode.SETTING -> appendSettingRenderEvents(log)
        }
        optionalOverlayRoute?.let { LoginOptionalOverlayRenderEvents.append(log, it) }
        return log.jsonl()
    }

    private fun drawOptionalOverlay(route: LoginOptionalOverlayRoute) {
        batch.color = Color(0f, 0f, 0f, .392f)
        batch.draw(dimPixel, 0f, 0f, 1280f, 688f)
        batch.color = Color.WHITE
        val signIn = route == LoginOptionalOverlayRoute.SIGNIN_OPEN
        val x = if (signIn) 127f else 259f
        val y = if (signIn) 21f else 73f
        val w = if (signIn) 1026f else 762f
        val h = if (signIn) 646f else 543f
        drawTiled(settingLogo9, x, y, w, h)
        settingBox1Patch.draw(batch, x, y, w, h)
        settingBox1Patch.draw(batch, x, y + h - 52f, w, 52f)
    }

    private fun drawRegistrationLoading(model: LoadingLayer) {
        batch.color = Color(0f, 0f, 0f, model.blockerOpacity)
        batch.draw(dimPixel, 0f, 0f, viewport.worldWidth, viewport.worldHeight)
        batch.color = Color.WHITE
        if (model.imageVisible) {
            val angle = (elapsed * 360f) % 360f
            batch.draw(loadingSpinner, 605f, 309f, 35f, 35f, 70f, 70f, 1f, 1f, angle,
                0, 0, loadingSpinner.width, loadingSpinner.height, false, false)
        }
    }

    private fun appendLoadRenderEvents(log: RenderEventLog) {
        val layer = "LoadGameLayer"
        val scale = .86f
        val spriteBlend = listOf(770, 771)
        val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun event(path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                  asset: String? = null, text: String = "", visible: Boolean = true,
                  opacity: Float = 1f, eventLayer: String = layer) =
            log.draw("login-load-stable", eventLayer, path, type, x * scale, y * scale, w * scale, h * scale,
                asset, opacity = opacity, blend = if (type == "label") labelBlend else spriteBlend,
                visible = visible, text = text)
        fun label(path: String, value: String, x: Float, y: Float, w: Float, h: Float = 50.4f, visible: Boolean = true) =
            event(path, "label", x, y, w, h, text = value, visible = visible)

        event("Canvas/Layer/bg1", "tiled-sprite", 278.186f, 97.5f, 932f, 605f, "Logo_9-1")
        event("Canvas/Layer/bg1/box2", "sliced-sprite", 278.186f, 97.5f, 932f, 605f, "box3")
        event("Canvas/Layer/bg1/bg1", "sprite", 278.186f, 652.5f, 932f, 50f, "bg1")
        label("Canvas/Layer/bg1/bg1/label", "진행도 불러오기", 283.186f, 652.3f, 253.31f)
        label("Canvas/Layer/bg1/label", "읽을 진행 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다.",
            286.785f, 603.763f, 1102.16f)
        event("Canvas/Layer/bg1/box2", "sliced-sprite", 287.186f, 174f, 912f, 428f, "box2")
        loadLayer.view().rows.take(22).forEachIndexed { index, row ->
            val y = 549f - index * 52f
            val visible = index < 12
            val path = "Canvas/Layer/bg1/box2/scrollview/view/content/item"
            event(path, "sprite", 289.186f, y, 908f, 50f, "885a69b4-08ed-4c78-8896-ffb04eb2bd20", visible = visible)
            label("$path/label0", row.number, 295.933f, y - .2f, 117.85f, visible = visible)
            label("$path/label1", row.stage, 426.886f, y - .2f, 137.6f, visible = visible)
            label("$path/label2", row.name, 578.186f, y, 615f, 50f, visible = visible)
        }
        event("Canvas/Layer/bg1/box2/vline", "sliced-sprite", 418.75f, 176.1f, 6f, 423.8f, "vline")
        event("Canvas/Layer/bg1/box2/vline", "sliced-sprite", 564.515f, 176.1f, 6f, 423.8f, "vline")
        event("Canvas/Layer/bg1/button0/Background", "sliced-sprite", 1051.386f, 107f, 147.6f, 60f, "box3")
        label("Canvas/Layer/bg1/button0/Background/Label", "취소", 1075.186f, 119.764f, 100f, 40f)
        if (loadLayer.pendingSlot() != null) {
            event("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                "default_sprite_splash", visible = false, opacity = 0f, eventLayer = "HallLayer")
            event("Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
            event("Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
            event("Canvas/Layer/bg0/Logo_3-1", "sprite", 453.005f, 373.951f, 106f, 124f, "Logo_3-1")
            label("Canvas/Layer/bg0/label", loadLayer.view().confirmation?.message.orEmpty(), 573.686f, 335f, 463f, 190f)
            event("Canvas/Layer/bg0/btns/button1/Background", "sliced-sprite", 554.186f, 271.285f, 180f, 50f, "box3")
            label("Canvas/Layer/bg0/btns/button1/Background/Label", "취소", 557.336f, 279.085f, 168.1f, 40f)
            event("Canvas/Layer/bg0/btns/button0/Background", "sliced-sprite", 754.186f, 271.285f, 180f, 50f, "box3")
            label("Canvas/Layer/bg0/btns/button0/Background/Label", "불러오기", 757.586f, 279.085f, 169.4f, 40f)
        }
    }

    private fun appendSettingRenderEvents(log: RenderEventLog) {
        val layer = "SettingLayer"
        val scale = .86f
        val spriteBlend = listOf(770, 771)
        val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun event(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            visible: Boolean = true,
            opacity: Float = 1f,
        ) = log.draw(
            "login-setting-stable", layer, path, type,
            x * scale, y * scale, w * scale, h * scale, asset,
            opacity = opacity,
            blend = if (type == "label") labelBlend else spriteBlend,
            visible = visible,
            text = text,
        )
        fun label(path: String, value: String, x: Float, y: Float, w: Float, h: Float = 50.4f) =
            event(path, "label", x, y, w, h, text = value)

        event("Canvas/Layer/bg", "tiled-sprite", 195.686f, 41f, 1097f, 718f, "Logo_9-1")
        event("Canvas/Layer/bg/box1", "tiled-sprite", 195.686f, 41f, 1097f, 718f, "box1")
        event("Canvas/Layer/bg/bg1", "sprite", 195.686f, 709f, 1097f, 50f, "bg1")
        label("Canvas/Layer/bg/bg1/label", "환경 설정", 200.686f, 708.8f, 149.51f)
        event("Canvas/Layer/bg/scrollview", "sliced-sprite", 203.686f, 110f, 1081f, 596f, "box2")
        label(
            "Canvas/Layer/bg/scrollview/view/content/label",
            "항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.",
            65.851f, 650.189f, 1078.67f,
        )
        val view = settingLayer.view()
        val toggleRects = listOf(
            floatArrayOf(218.29f, 611f),
            floatArrayOf(218.29f, 546f),
            floatArrayOf(218.29f, 482f),
            floatArrayOf(218.186f, 417f),
            floatArrayOf(218.186f, 353f),
        )
        val labels = listOf("배경 음악 듣기", "효과음 듣기", "전투 시 전장 축소 이미지가 자동으로 표시됩니다.", "대화창 자동 닫힘", "체력 바가 유닛 위에 있습니다")
        val bits = listOf(0, 1, 2, 3, 4)
        toggleRects.forEachIndexed { index, rect ->
            val path = "Canvas/Layer/bg/scrollview/view/content/button$index/toggle"
            event("$path/Background", "sprite", rect[0], rect[1], 28f, 28f, "default_toggle_normal")
            if (view.flags and (1 shl bits[index]) != 0) {
                val checkX = if (index == 2) 218.186f else rect[0]
                event("$path/checkmark", "sprite", checkX, rect[1], 28f, 28f, "default_toggle_checkmark")
            }
            label("$path/Label", labels[index], if (index < 2) 252.29f else 252.186f, rect[1] - 6f, 526f, 40f)
        }

        fun panel(index: Int, y: Float, titleX: Float, titleY: Float, titleW: Float, title: String) {
            val path = "Canvas/Layer/bg/scrollview/view/content/panel$index"
            event(path, "sliced-sprite", 793.336f, y, 479.7f, 100f, "box1")
            event("$path/bg1", "sprite", 831.428f, titleY + .2f, 166f, 50f, "bg1")
            label("$path/bg1/label", title, titleX, titleY, titleW)
        }
        fun radios(panelIndex: Int, y: Float, selected: Int, values: List<String>) {
            val xs = floatArrayOf(818.346f, 974.286f, 1119.419f)
            val labelXs = floatArrayOf(855.731f, 1024.671f, 1169.804f)
            values.forEachIndexed { index, value ->
                val path = "Canvas/Layer/bg/scrollview/view/content/panel$panelIndex/toggleContainer/toggle$index"
                event("$path/Background", "sprite", xs[index], y, 32f, 32f, "default_radio_button_off")
                if (index == selected) event("$path/checkmark", "sprite", xs[index], y, 32f, 32f, "default_radio_button_on")
                label("$path/Label", value, labelXs[index], y - 4f, 90f, 40f)
            }
        }
        panel(0, 520.389f, 822.373f, 595.481f, 184.11f, "텍스트 속도")
        radios(0, 545.638f, view.msgSpeed, listOf("느림", "중", "빠르게"))
        panel(1, 388.389f, 839.673f, 463.481f, 149.51f, "게임 속도")
        event("Canvas/Layer/bg/scrollview/view/content/panel1/slider/Background", "sliced-sprite", 816.186f, 418.346f, 434f, 20f, "default_scrollbar")
        event("Canvas/Layer/bg/scrollview/view/content/panel1/slider/Handle", "sliced-sprite", 800.186f + 434f * view.speed, 412.346f, 32f, 32f, "default_radio_button_off")
        panel(2, 256.389f, 839.673f, 331.481f, 149.51f, "정보 설명")
        radios(2, 281.638f, view.notifyLevel, listOf("자세히", "보통", "요약"))

        val panel3 = "Canvas/Layer/bg/scrollview/view/content/panel3"
        event(panel3, "sliced-sprite", 793.336f, 81.389f, 479.7f, 142f, "box1")
        event("$panel3/bg1", "sprite", 833.325f, 198.167f, 210f, 50f, "bg1")
        label("$panel3/bg1/label", "대화창 색상", 846.27f, 197.967f, 184.11f)
        val itemXs = floatArrayOf(831.12f, 933.12f, 1035.12f, 1137.12f)
        itemXs.forEachIndexed { index, x ->
            val path = "$panel3/item$index"
            event(path, "sliced-sprite", x, 92.703f, 100f, 100f, "box1")
            event("$path/Logo_9-1", "sprite", x + 2f, 94.703f, 96f, 96f, "Logo_${9 + index}-1")
            if (view.background == index) {
                event(
                    "$path/box6",
                    "tiled-sprite",
                    x,
                    92.703f,
                    100f,
                    100f,
                    "box6",
                    opacity = if (optionalOverlayRoute != null) 1f else .333f,
                )
            }
        }
        event("Canvas/Layer/bg/button1/Background", "sliced-sprite", 1130.186f, 47f, 156f, 56f, "box3")
        label("Canvas/Layer/bg/button1/Background/Label", "확인", 1158.186f, 57.261f, 100f, 40f)
    }

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)

    override fun dispose() {
        loginBackground.dispose(); loginButtons.forEach(Texture::dispose); uiTextures.forEach(Texture::dispose)
        uiFont.dispose(); dimPixel.dispose(); batch.dispose(); shapes.dispose()
    }

    private fun activate(action: TitleInteraction.MainAction) {
        TitleInteraction.dispatch(action, object : TitleInteraction.MainRoutes {
            override fun newGame(moduleName: String) {
                check(moduleName == "R_00")
                game.startNewGame()
            }
            override fun openLoad() {
                loadLayer.onCreate(); mode = Mode.LOAD
                Gdx.app.log("JojoPort", "Login -> LoadGameLayer")
            }
            override fun openSettings() {
                settingLayer.onCreate(); settingChanged = false; mode = Mode.SETTING
                Gdx.app.log("JojoPort", "Login -> SettingLayer")
            }
            override fun requestExit() = Gdx.app.exit()
        })
    }

    private fun closeOverlay() {
        val returnScenario = if (mode == Mode.SETTING) settingReturnScenario else null
        when (mode) {
            Mode.LOAD -> loadLayer.onCancel(LoadGameLayer.TOUCH_END)
            Mode.SETTING -> settingLayer.close(SettingLayer.TOUCH_END)
            Mode.LOGIN -> return
        }
        mode = Mode.LOGIN
        pressedLoadSlot = null
        if (returnScenario != null) game.showScenario(returnScenario)
    }

    private fun handleLoadTap(x: Int, y: Int) {
        val confirmation = loadLayer.pendingSlot()
        when (val action = TitleInteraction.loadActionAt(x, y, confirmation != null)) {
            TitleInteraction.LoadAction.ConfirmLoad -> loadLayer.onConfirm(0)
            TitleInteraction.LoadAction.CancelConfirmation -> loadLayer.onConfirm(1)
            TitleInteraction.LoadAction.CloseOverlay -> closeOverlay()
            is TitleInteraction.LoadAction.SelectVisualRow -> {
                val slot = loadLayer.view().rows.getOrNull(action.index)?.index ?: return
                pressedLoadSlot = slot
                loadLayer.onRowTouch(slot, LoadGameLayer.TOUCH_END)
            }
            null -> Unit
        }
    }

    private fun handleSettingTap(x: Int, y: Int) {
        val action = TitleInteraction.settingActionAt(x, y) ?: return
        if (TitleInteraction.applySetting(action, settingLayer)) {
            mode = Mode.LOGIN
            pressedLoadSlot = null
            settingReturnScenario?.let(game::showScenario)
        } else {
            settingChanged = true
        }
    }

    private fun drawLogin() {
        batch.draw(loginBackground, 0f, 0f, viewport.worldWidth, viewport.worldHeight)
        loginButtons.forEachIndexed { index, texture ->
            val sourceY = floatArrayOf(582f, 456f, 329f, 203f)[index]
            batch.draw(texture, 945.46f, sourceY * .86f - 37.84f, 302.72f, 75.68f)
        }
    }

    private fun drawDim(alpha: Float) {
        batch.projectionMatrix = viewport.camera.combined
        batch.enableBlending()
        batch.begin()
        batch.color = Color(0f, 0f, 0f, alpha)
        batch.draw(dimPixel, 0f, 0f, viewport.worldWidth, viewport.worldHeight)
        batch.color = Color.WHITE
        batch.end()
    }

    private fun drawLoadOverlay() {
        drawTiled(loadLogo9, 239.24f, 83.85f, 801.52f, 520.3f)
        loadOuterPatch.draw(batch, 239.24f, 83.85f, 801.52f, 520.3f)
        loadTitlePatch.draw(batch, 239.24f, 561.15f, 801.52f, 43f)
        drawLabelSource("진행도 불러오기", 409.841f, 677.5f, 253.31f)
        uiFont.color = Color.BLACK
        uiFont.draw(batch, "읽을 진행 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다.", 246.64f, 554.91f)
        loadBoxPatch.draw(batch, 246.56f, 149.64f, 784.32f, 368.08f)
        val rows = loadLayer.view().rows.take(8)
        rows.forEachIndexed { index, row ->
            val cy = 574f - index * 52f
            loadRowPatch.draw(batch, 248.49f, (cy - 25f) * .86f, 780.88f, 43f)
            uiFont.color = if (row.occupied) Color.BLACK else Color(146f / 255f, 146f / 255f, 146f / 255f, 1f)
            drawLabelSource(row.number, 354.858f, cy, 117.85f)
            drawLabelSource(row.stage, 495.686f, cy, 137.6f)
            uiFont.draw(batch, row.name, 578.186f * .86f, cy * .86f + 13f)
        }
        loadVlinePatch.draw(batch, (421.75f - 3f) * .86f, (388f - 211.9f) * .86f, 5.16f, 364.47f)
        loadVlinePatch.draw(batch, (567.515f - 3f) * .86f, (388f - 211.9f) * .86f, 5.16f, 364.47f)
        loadOuterPatch.draw(batch, (1125.186f - 73.8f) * .86f, (137f - 30f) * .86f, 126.94f, 51.6f)
        uiFont.color = Color.BLACK
        drawCenteredLabelSource("취소", 1125.186f, 139.764f)
    }

    private fun drawLoadConfirmation() {
        drawTiled(loadLogo9, 366.95f, 216.72f, 546.1f, 254.56f)
        loadOuterPatch.draw(batch, 366.95f, 216.72f, 546.1f, 254.56f)
        // Logo_3-1 is authored at 53x62 with node scale 2.
        drawSourceNode(loadEagle, 506.005f, 435.951f, 106f, 124f)
        // The source cc.Label wraps the Korean confirmation immediately after
        // "있" at its authored 463px width. FreeType's different advances
        // otherwise keep the whole sentence on one line, changing the layout
        // even though font rasterisation itself is outside the parity target.
        val message = loadLayer.view().confirmation?.message.orEmpty()
            .replace("있나요?", "있\n나요?")
        uiFont.color = Color(0f, 4f / 255f, 196f / 255f, 1f)
        uiFont.draw(batch, message, (805.186f - 231.5f) * .86f, 404f, 463f * .86f, Align.left, true)
        loadOuterPatch.draw(batch, (644.186f - 90f) * .86f, (296.285f - 25f) * .86f, 154.8f, 43f)
        uiFont.color = Color.RED
        drawCenteredLabelSource("취소", 644.186f, 299.085f)
        loadOuterPatch.draw(batch, (844.186f - 90f) * .86f, (296.285f - 25f) * .86f, 154.8f, 43f)
        uiFont.color = Color(10f / 255f, 105f / 255f, 0f, 1f)
        drawCenteredLabelSource("불러오기", 844.186f, 299.085f)
        uiFont.color = Color.BLACK
    }

    private fun drawSettingOverlay() {
        val view = settingLayer.view()
        drawTiled(settingLogo9, 195.686f * .86f, 41f * .86f, 1097f * .86f, 718f * .86f)
        drawTiled(settingBox1, 195.686f * .86f, 41f * .86f, 1097f * .86f, 718f * .86f)
        batch.draw(settingTitle, 195.686f * .86f, 709f * .86f, 1097f * .86f, 50f * .86f)
        uiFont.color = Color.BLACK
        drawLabelSource("환경 설정", 275.441f, 734f, 149.51f)
        settingBox2Patch.draw(batch, 203.686f * .86f, 110f * .86f, 1081f * .86f, 596f * .86f)
        // The very wide source Label is clipped by the ScrollView's left
        // boundary before its first glyph becomes visible.
        uiFont.draw(batch, "항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.", 65.851f * .86f, 650.189f * .86f + 13f)

        val toggleCenters = listOf(232.29f to 625f, 232.29f to 560f, 232.186f to 496f, 232.186f to 431f, 232.186f to 367f)
        val toggleLabels = listOf("배경 음악 듣기", "효과음 듣기", "전투 시 전장 축소 이미지가 자동으로 표시됩니다.", "대화창 자동 닫힘", "체력 바가 유닛 위에 있습니다")
        val bits = listOf(0, 1, 2, 3, 4)
        toggleCenters.forEachIndexed { index, (cx, cy) ->
            drawSourceNode(settingToggle, cx, cy, 28f, 28f)
            if (view.flags and (1 shl bits[index]) != 0) drawSourceNode(settingCheck, cx, cy, 28f, 28f)
            drawLabelSource(toggleLabels[index], if (index < 2) 515.29f else 515.186f, cy, 526f)
        }

        drawSettingPanel(570.389f, 100f, "텍스트 속도", 620.681f, 166f)
        drawRadios(561.638f, view.msgSpeed, listOf("느림", "중", "빠르게"))
        drawSettingPanel(438.389f, 100f, "게임 속도", 488.681f, 166f)
        settingSliderPatch.draw(batch, (1033.186f - 217f) * .86f, (428.346f - 10f) * .86f, 373.24f, 17.2f)
        drawSourceNode(settingRadioOff, 816.186f + 434f * view.speed, 428.346f, 32f, 32f)
        drawSettingPanel(306.389f, 100f, "정보 설명", 356.681f, 166f)
        drawRadios(297.638f, view.notifyLevel, listOf("자세히", "보통", "요약"))

        settingBox1Patch.draw(batch, (1033.186f - 239.85f) * .86f, (152.389f - 71f) * .86f, 412.54f, 122.12f)
        batch.draw(settingTitle, 833.325f * .86f, 198.167f * .86f, 210f * .86f, 50f * .86f)
        drawLabelSource("대화창 색상", 938.325f, 223.167f, 184.11f)
        listOf(881.12f, 983.12f, 1085.12f, 1187.12f).forEachIndexed { index, cx ->
            settingBox1Patch.draw(batch, (cx - 50f) * .86f, (142.703f - 50f) * .86f, 86f, 86f)
            drawSourceNode(settingStyles[index], cx, 142.703f, 96f, 96f)
            if (view.background == index) {
                batch.color = Color(1f, 1f, 1f, if (optionalOverlayRoute != null) 1f else .333f)
                drawTiled(settingBox6, (cx - 50f) * .86f, (142.703f - 50f) * .86f, 100f * .86f, 100f * .86f)
                batch.color = Color.WHITE
            }
        }
        settingButtonPatch.draw(batch, (1208.186f - 78f) * .86f, (75f - 28f) * .86f, 134.16f, 48.16f)
        drawCenteredLabelSource("확인", 1208.186f, 77.261f)
    }

    private fun drawSettingPanel(cy: Float, height: Float, titleText: String, titleCy: Float, titleWidth: Float) {
        settingBox1Patch.draw(batch, (1033.186f - 239.85f) * .86f, (cy - height / 2f) * .86f, 412.54f, height * .86f)
        batch.draw(settingTitle, (914.428f - titleWidth / 2f) * .86f, (titleCy - 25f) * .86f, titleWidth * .86f, 50f * .86f)
        drawLabelSource(titleText, 914.428f, titleCy, if (titleText == "텍스트 속도") 184.11f else 149.51f)
    }

    private fun drawRadios(cy: Float, selected: Int, labels: List<String>) {
        val centers = listOf(834.346f, 990.286f, 1135.419f)
        val labelCenters = listOf(900.731f, 1069.671f, 1214.804f)
        centers.forEachIndexed { index, cx ->
            drawSourceNode(settingRadioOff, cx, cy, 32f, 32f)
            if (index == selected) drawSourceNode(settingRadioOn, cx, cy, 32f, 32f)
            drawLabelSource(labels[index], labelCenters[index], cy, 90f)
        }
    }

    private fun drawSourceNode(texture: Texture, cx: Float, cy: Float, sourceWidth: Float, sourceHeight: Float) {
        batch.draw(texture, (cx - sourceWidth / 2f) * .86f, (cy - sourceHeight / 2f) * .86f, sourceWidth * .86f, sourceHeight * .86f)
    }

    private fun drawLabelSource(text: String, sourceCx: Float, sourceCy: Float, sourceNodeWidth: Float) {
        // All Login overlay cc.Label components use horizontalAlign=LEFT;
        // their node position is centred, but glyphs begin at the left edge.
        uiFont.draw(batch, text, (sourceCx - sourceNodeWidth / 2f) * .86f, sourceCy * .86f + 13f)
    }

    private fun drawCenteredLabelSource(text: String, sourceCx: Float, sourceCy: Float) {
        uiFont.draw(batch, text, (sourceCx - 100f) * .86f, sourceCy * .86f + 13f, 200f * .86f, Align.center, false)
    }

    private fun drawTiled(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
        val tileWidth = texture.width * .86f
        val tileHeight = texture.height * .86f
        var dy = 0f
        while (dy < height - .01f) {
            val drawnHeight = minOf(tileHeight, height - dy)
            val sourceHeight = (drawnHeight / .86f).toInt().coerceIn(1, texture.height)
            var dx = 0f
            while (dx < width - .01f) {
                val drawnWidth = minOf(tileWidth, width - dx)
                val sourceWidth = (drawnWidth / .86f).toInt().coerceIn(1, texture.width)
                batch.draw(texture, x + dx, y + dy, drawnWidth, drawnHeight, 0, 0, sourceWidth, sourceHeight, false, false)
                dx += tileWidth
            }
            dy += tileHeight
        }
    }

    private data class Bounds(val left: Int, val bottom: Int, val right: Int, val top: Int)
    private enum class Mode { LOGIN, LOAD, SETTING }

    private companion object {
        const val LOGICAL_WIDTH = 1280
        const val LOGICAL_HEIGHT = 688
        const val CHECK_REGISTER = "CHECK_REGISTER"
        val SETTING_CONFIRM_BOUNDS = Bounds(965, 37, 1112, 93)
    }

    private fun Bounds.contains(x: Int, y: Int) = x in left..right && y in bottom..top
}
