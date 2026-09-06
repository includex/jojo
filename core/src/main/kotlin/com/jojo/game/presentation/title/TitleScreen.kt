// Presentation
package com.jojo.game.presentation.title
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.TitleRuntimeProbe
import com.jojo.game.presentation.title.assets.TitleSceneAssets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter

/** TitleScreen: 게임 시작 화면의 입력·메뉴·배경·선택 오버레이를 수명주기와 함께 조정한다. */
class TitleScreen(
    private val game: JojoGame,
    initialSettingOpen: Boolean = false,
    initialLoadOpen: Boolean = false,
    initialLoadRow: Int? = null,
    private val useInitialSettings: Boolean = false,
    private val optionalOverlayRoute: LoginOptionalOverlayRoute? = null,
    private val settingSceneName: String = "Login",
    private val settingReturnScenario: String? = null,
) : ScreenAdapter() {
    private val assets = TitleSceneAssets()
    private val renderer = TitleSceneRenderer(assets)
    private var elapsed = 0f
    private var mode = when {
        initialLoadOpen -> TitleMode.LOAD
        initialSettingOpen || optionalOverlayRoute != null -> TitleMode.SETTING
        else -> TitleMode.LOGIN
    }
    private val loadLayer = game.titleLoadGameLayer()
    private val settingsPreferences = game.settingsPreferences()
    private val settingLayer = SettingLayer(object : SettingLayer.Store {
        override fun getInt(key: String, default: Int): Int =
            if (optionalOverlayRoute != null || useInitialSettings) default
            else settingsPreferences.getInteger(key, default)

        override fun putInt(key: String, value: Int) {
            settingsPreferences.putInteger(key, value).flush()
        }
    }, featureEnvironment = { game.settingFeatureEnvironment(settingSceneName) })
    private val registrationCheck = LoginRegistrationCheckFlow(
        pending = settingsPreferences.getInteger(CHECK_REGISTER, 0) != 0,
        clearPending = { settingsPreferences.remove(CHECK_REGISTER); settingsPreferences.flush() },
        requestCheck = game::requestRegistrationCheck,
        onRegistered = { Gdx.app.log("JojoGame", "registration check accepted") },
    )

    init {
        registrationCheck.start()
        if (mode == TitleMode.LOAD) {
            loadLayer.onCreate()
            initialLoadRow?.let { loadLayer.onRowTouch(it, LoadGameLayer.TOUCH_END) }
        }
        if (mode == TitleMode.SETTING) settingLayer.onCreate()
        Gdx.input.inputProcessor = inputProcessor()
    }

    override fun render(delta: Float) {
        elapsed += delta
        renderer.render(viewState())
    }

    /** runtimeProbe: 현재 타이틀 화면 상태를 런타임 검증용 관측값으로 반환한다. */
    internal fun runtimeProbe() = TitleRuntimeProbe(viewState())

    override fun resize(width: Int, height: Int) = renderer.resize(width, height)

    override fun dispose() {
        assets.dispose()
        renderer.dispose()
    }

    private fun inputProcessor() = object : InputAdapter() {
        override fun keyDown(keycode: Int): Boolean {
            if (registrationCheck.loading != null) return true
            when {
                keycode == Input.Keys.ESCAPE && mode == TitleMode.LOGIN -> Gdx.app.exit()
                keycode == Input.Keys.ESCAPE && mode == TitleMode.LOAD && loadLayer.pendingSlot() != null ->
                    loadLayer.onConfirm(1)

                keycode == Input.Keys.ESCAPE -> closeOverlay()
                isConfirmKey(keycode) && mode == TitleMode.LOGIN -> activate(TitleInteraction.MainAction.NEW_GAME)
                isConfirmKey(keycode) && mode == TitleMode.SETTING -> closeOverlay()
                isConfirmKey(keycode) && mode == TitleMode.LOAD && loadLayer.pendingSlot() != null ->
                    loadLayer.onConfirm(0)
            }
            return true
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            if (registrationCheck.loading != null) return true
            val x = screenX * LOGICAL_WIDTH / Gdx.graphics.width
            val y = (Gdx.graphics.height - screenY) * LOGICAL_HEIGHT / Gdx.graphics.height
            when (mode) {
                TitleMode.LOGIN -> TitleInteraction.mainActionAt(x, y)?.let(::activate)
                TitleMode.SETTING -> handleSettingTap(x, y)
                TitleMode.LOAD -> handleLoadTap(x, y)
            }
            return true
        }
    }

    private fun activate(action: TitleInteraction.MainAction) {
        TitleInteraction.dispatch(action, object : TitleInteraction.MainRoutes {
            override fun newGame(moduleName: String) {
                check(moduleName == "R_00")
                game.startNewGame()
            }

            override fun openLoad() {
                loadLayer.onCreate()
                mode = TitleMode.LOAD
                Gdx.app.log("JojoGame", "Login -> LoadGameLayer")
            }

            override fun openSettings() {
                settingLayer.onCreate()
                mode = TitleMode.SETTING
                Gdx.app.log("JojoGame", "Login -> SettingLayer")
            }

            override fun requestExit() = Gdx.app.exit()
        })
    }

    private fun closeOverlay() {
        val returnScenario = if (mode == TitleMode.SETTING) settingReturnScenario else null
        when (mode) {
            TitleMode.LOAD -> loadLayer.onCancel(LoadGameLayer.TOUCH_END)
            TitleMode.SETTING -> settingLayer.close(SettingLayer.TOUCH_END)
            TitleMode.LOGIN -> return
        }
        mode = TitleMode.LOGIN
        if (returnScenario != null) game.showScenario(returnScenario)
    }

    private fun handleLoadTap(x: Int, y: Int) {
        when (val action = TitleInteraction.loadActionAt(x, y, loadLayer.pendingSlot() != null)) {
            TitleInteraction.LoadAction.ConfirmLoad -> loadLayer.onConfirm(0)
            TitleInteraction.LoadAction.CancelConfirmation -> loadLayer.onConfirm(1)
            TitleInteraction.LoadAction.CloseOverlay -> closeOverlay()
            is TitleInteraction.LoadAction.SelectVisualRow -> {
                val slot = loadLayer.view().rows.getOrNull(action.index)?.index ?: return
                loadLayer.onRowTouch(slot, LoadGameLayer.TOUCH_END)
            }

            null -> Unit
        }
    }

    private fun handleSettingTap(x: Int, y: Int) {
        val action = TitleInteraction.settingActionAt(x, y) ?: return
        if (TitleInteraction.applySetting(action, settingLayer)) {
            mode = TitleMode.LOGIN
            settingReturnScenario?.let(game::showScenario)
        }
    }

    private fun viewState(): TitleViewState {
        val loadView = if (mode == TitleMode.LOAD) loadLayer.view() else null
        val settingView = if (mode == TitleMode.SETTING) settingLayer.view() else null
        return TitleViewState(
            mode = mode,
            optionalOverlayRoute = optionalOverlayRoute,
            loadRows = loadView?.rows.orEmpty().map { TitleLoadRow(it.number, it.stage, it.name, it.occupied) },
            loadConfirmationMessage = loadView?.confirmation?.message,
            settings = settingView?.let {
                TitleSettingsView(it.flags, it.msgSpeed, it.notifyLevel, it.background, it.speed)
            },
            registrationLoading = registrationCheck.loading?.let {
                TitleLoadingView(it.blockerOpacity, it.imageVisible)
            },
            elapsedSeconds = elapsed,
        )
    }

    private fun isConfirmKey(keycode: Int) = keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE

    private companion object {
        const val LOGICAL_WIDTH = 1280
        const val LOGICAL_HEIGHT = 688
        const val CHECK_REGISTER = "CHECK_REGISTER"
    }
}
