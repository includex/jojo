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
    /** `game` (JojoGame): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val game: JojoGame,
    initialSettingOpen: Boolean = false,
    initialLoadOpen: Boolean = false,
    initialLoadRow: Int? = null,
    /** `useInitialSettings` (Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val useInitialSettings: Boolean = false,
    /** `optionalOverlayRoute` (LoginOptionalOverlayRoute?): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val optionalOverlayRoute: LoginOptionalOverlayRoute? = null,
    /** `settingSceneName` (String): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val settingSceneName: String = "Login",
    /** `settingReturnScenario` (String?): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val settingReturnScenario: String? = null,
) : ScreenAdapter() {
    /**
     * `assets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val assets = TitleSceneAssets()
    /**
     * `renderer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val renderer = TitleSceneRenderer(assets)
    /**
     * `elapsed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var elapsed = 0f
    /**
     * `mode` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var mode = when {
        initialLoadOpen -> TitleMode.LOAD
        initialSettingOpen || optionalOverlayRoute != null -> TitleMode.SETTING
        else -> TitleMode.LOGIN
    }
    /**
     * `loadLayer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val loadLayer = game.titleLoadGameLayer()
    /**
     * `settingsPreferences` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val settingsPreferences = game.settingsPreferences()
    /**
     * `settingLayer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val settingLayer = SettingLayer(object : SettingLayer.Store {
        /**
         * `getInt`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        override fun getInt(key: String, default: Int): Int =
            if (optionalOverlayRoute != null || useInitialSettings) default
            else settingsPreferences.getInteger(key, default)

        /**
         * `putInt`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        override fun putInt(key: String, value: Int) {
            settingsPreferences.putInteger(key, value).flush()
        }
    }, featureEnvironment = { game.settingFeatureEnvironment(settingSceneName) })
    /**
     * `registrationCheck` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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

    /**
     * `render`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun render(delta: Float) {
        elapsed += delta
        renderer.render(viewState())
    }

    /** runtimeProbe: 현재 타이틀 화면 상태를 런타임 검증용 관측값으로 반환한다. */
    internal fun runtimeProbe() = TitleRuntimeProbe(viewState())

    /**
     * `resize`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun resize(width: Int, height: Int) = renderer.resize(width, height)

    /**
     * `dispose`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun dispose() {
        assets.dispose()
        renderer.dispose()
    }

    /**
     * `inputProcessor`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun inputProcessor() = object : InputAdapter() {
        /**
         * `keyDown`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

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

        /**
         * `touchDown`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

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

    /**
     * `activate`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun activate(action: TitleInteraction.MainAction) {
        TitleInteraction.dispatch(action, object : TitleInteraction.MainRoutes {
            /**
             * `newGame`: 타입의 핵심 동작을 수행한다.
             * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
             */

            override fun newGame(moduleName: String) {
                check(moduleName == "R_00")
                game.startNewGame()
            }

            /**
             * `openLoad`: 상태나 데이터를 조회한다.
             * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
             */

            override fun openLoad() {
                loadLayer.onCreate()
                mode = TitleMode.LOAD
                Gdx.app.log("JojoGame", "Login -> LoadGameLayer")
            }

            /**
             * `openSettings`: 현재 상태를 갱신한다.
             * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
             */

            override fun openSettings() {
                settingLayer.onCreate()
                mode = TitleMode.SETTING
                Gdx.app.log("JojoGame", "Login -> SettingLayer")
            }

            /**
             * `requestExit`: 타입의 핵심 동작을 수행한다.
             * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
             */

            override fun requestExit() = Gdx.app.exit()
        })
    }

    /**
     * `closeOverlay`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `handleLoadTap`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `handleSettingTap`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun handleSettingTap(x: Int, y: Int) {
        val action = TitleInteraction.settingActionAt(x, y) ?: return
        if (TitleInteraction.applySetting(action, settingLayer)) {
            mode = TitleMode.LOGIN
            settingReturnScenario?.let(game::showScenario)
        }
    }

    /**
     * `viewState`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `isConfirmKey`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun isConfirmKey(keycode: Int) = keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE

    private companion object {
        /**
         * `LOGICAL_WIDTH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOGICAL_WIDTH = 1280
        /**
         * `LOGICAL_HEIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOGICAL_HEIGHT = 688
        /**
         * `CHECK_REGISTER` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHECK_REGISTER = "CHECK_REGISTER"
    }
}
