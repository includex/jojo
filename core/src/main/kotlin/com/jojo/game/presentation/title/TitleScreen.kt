// Presentation
package com.jojo.game.presentation.title
import com.jojo.game.infrastructure.audio.GameAudioPlayer
import com.jojo.game.infrastructure.audio.UiSound
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
    /**
     * `registrationCheckPending` (Boolean?): 등록 확인 대기 여부를 강제한다.
     * `null`이면 저장된 `CHECK_REGISTER` 설정을 그대로 따른다. 응답이 오기 전의
     * LoadLayer 화면을 그대로 붙잡아 두어야 하는 캡처 경로가 이 자리를 쓴다.
     */
    private val registrationCheckPending: Boolean? = null,
    /**
     * `registrationTransport` (((Boolean) -> Unit) -> Unit)?): 등록 확인 요청 경로다.
     * `null`이면 운영 경로인 `JojoGame.requestRegistrationCheck`를 쓴다.
     */
    private val registrationTransport: ((((Boolean) -> Unit) -> Unit))? = null,
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
     * 시작 화면의 소리다.
     *
     * 원본 `Login._launch`는 `playBackgroundSound(BG_SOUND_IDX.START)`로 시작 화면
     * 배경음을 튼다. 설정 창의 두 스위치를 그대로 본다.
     */
    private val audio = GameAudioPlayer(
        enabled = game.audioEnabled(),
        musicOn = { settingEnabled(SettingLayer.BG_SOUND) },
        effectOn = { settingEnabled(SettingLayer.EFFECT_SOUND) },
    )

    /** 설정 값: 원본 `GAME_SETTING` 비트를 읽는다. 기본값은 원본과 같이 셋을 켠 상태다. */
    private fun settingEnabled(bit: Int) = settingsPreferences.getInteger(
        SettingLayer.GAME_SETTING,
        SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
    ) and bit != 0
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
        pending = registrationCheckPending ?: (settingsPreferences.getInteger(CHECK_REGISTER, 0) != 0),
        clearPending = { settingsPreferences.remove(CHECK_REGISTER); settingsPreferences.flush() },
        requestCheck = registrationTransport ?: game::requestRegistrationCheck,
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
        if (elapsed == 0f) audio.sync(TITLE_BACKGROUND_SOUND, emptyList())
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
        audio.dispose()
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
                // 원본 `Login._launch`는 배경(`bg`) 전체를 깃발 1로 등록해, 시작 화면을
                // 누르면 클릭음이 난다.
                TitleMode.LOGIN -> TitleInteraction.mainActionAt(x, y)?.let {
                    audio.playUiSound(UiSound.CLICK)
                    activate(it)
                }

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
        // 원본 `LoadGameLayer`는 단추와 저장칸 줄을 모두 깃발 1로 등록한다. 닫기도 단추라
        // 취소음이 아니라 클릭음이 난다.
        when (val action = TitleInteraction.loadActionAt(x, y, loadLayer.pendingSlot() != null)) {
            TitleInteraction.LoadAction.ConfirmLoad -> {
                audio.playUiSound(UiSound.CLICK); loadLayer.onConfirm(0)
            }

            TitleInteraction.LoadAction.CancelConfirmation -> {
                audio.playUiSound(UiSound.CLICK); loadLayer.onConfirm(1)
            }

            TitleInteraction.LoadAction.CloseOverlay -> {
                audio.playUiSound(UiSound.CLICK); closeOverlay()
            }

            is TitleInteraction.LoadAction.SelectVisualRow -> {
                val slot = loadLayer.view().rows.getOrNull(action.index)?.index ?: return
                audio.playUiSound(UiSound.CLICK)
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
        // 원본 `SettingLayer`는 닫기 단추만 깃발 2(취소음)이고, 확인칸·라디오는
        // `cc.Toggle` 사건이라 소리가 없다.
        if (action == TitleInteraction.SettingAction.Confirm) audio.playUiSound(UiSound.CANCEL)
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
        /** 시작 화면 배경음 번호다. 원본 `BG_SOUND_IDX.START`가 16이다. */
        const val TITLE_BACKGROUND_SOUND = 16

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
