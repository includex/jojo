package com.jojo.game.presentation.title
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.overlay.SettingLayer

/** Renderer-independent hit and route contract for the title scene. */
object TitleInteraction {
    /**
     * enum class  `MainAction`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class MainAction { NEW_GAME, LOAD, SETTINGS, EXIT }
    sealed interface LoadAction {
        data object CloseOverlay : LoadAction
        data object ConfirmLoad : LoadAction
        data object CancelConfirmation : LoadAction

        /**
         * data class  `SelectVisualRow`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class SelectVisualRow(val index: Int) : LoadAction
    }

    sealed interface SettingAction {
        /**
         * data class  `Toggle`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Toggle(val bit: Int) : SettingAction

        /**
         * data class  `TextSpeed`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class TextSpeed(val index: Int) : SettingAction

        /**
         * data class  `NotifyLevel`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class NotifyLevel(val index: Int) : SettingAction

        /**
         * data class  `Background`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Background(val index: Int) : SettingAction

        /**
         * data class  `GameSpeed`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class GameSpeed(val progress: Float) : SettingAction
        data object Confirm : SettingAction
    }

    /**
     * data class  `Bounds`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Bounds(val left: Int, val bottom: Int, val right: Int, val top: Int) {
        /**
         * 공개 메서드 `contains`
         *
         * ### 파라미터
        - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun contains(x: Int, y: Int) = x in left..right && y in bottom..top
    }

    private val mainButtons = listOf(
        Bounds(945, 462, 1249, 539) to MainAction.NEW_GAME,
        Bounds(945, 354, 1249, 430) to MainAction.LOAD,
        Bounds(945, 245, 1249, 322) to MainAction.SETTINGS,
        Bounds(945, 136, 1249, 213) to MainAction.EXIT,
    )
    private val loadCancel = Bounds(897, 87, 1040, 152)
    private val confirmCancel = Bounds(475, 227, 632, 282)
    private val confirmLoad = Bounds(645, 227, 807, 282)

    /**
     * 공개 메서드 `mainActionAt`
     *
     * ### 파라미터
    - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `MainAction?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun mainActionAt(x: Int, y: Int): MainAction? = mainButtons.firstOrNull { it.first.contains(x, y) }?.second

    /**
     * 공개 메서드 `loadActionAt`
     *
     * ### 파라미터
    - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `confirmationOpen` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `LoadAction?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun loadActionAt(x: Int, y: Int, confirmationOpen: Boolean): LoadAction? {
        if (confirmationOpen) return when {
            confirmLoad.contains(x, y) -> LoadAction.ConfirmLoad
            confirmCancel.contains(x, y) -> LoadAction.CancelConfirmation
            else -> null
        }
        if (loadCancel.contains(x, y)) return LoadAction.CloseOverlay
        if (x !in 245..1035 || y !in 157..517) return null
        return LoadAction.SelectVisualRow(((517 - y) / 45).coerceAtLeast(0))
    }

    /** Coordinate contract used by the real SettingLayer overlay, not the title framebuffer oracle. */
    fun settingActionAt(x: Int, y: Int): SettingAction? {
        if (x in 965..1112 && y in 37..93) return SettingAction.Confirm
        if (x in 180..630) {
            val bit = when (y) {
                in 530..570 -> 0
                in 475..514 -> 1
                in 420..459 -> 4
                in 365..404 -> 5
                in 310..349 -> 6
                else -> null
            }
            if (bit != null) return SettingAction.Toggle(bit)
        }
        if (y in 460..507 && x in 685..1025) return SettingAction.TextSpeed(((x - 685) / 112).coerceIn(0, 2))
        if (y in 235..277 && x in 685..1025) return SettingAction.NotifyLevel(((x - 685) / 112).coerceIn(0, 2))
        if (y in 85..175 && x in 700..1075) return SettingAction.Background(((x - 700) / 94).coerceIn(0, 3))
        if (y in 345..395 && x in 685..1085) return SettingAction.GameSpeed(((x - 700f) / 375f).coerceIn(0f, 1f))
        return null
    }

    /** Applies the same action that TitleScreen receives from natural pointer input. */
    fun applySetting(action: SettingAction, layer: SettingLayer): Boolean = when (action) {
        SettingAction.Confirm -> layer.close(SettingLayer.TOUCH_END)
        is SettingAction.Toggle -> {
            val enabled = layer.view().flags and (1 shl action.bit) != 0
            layer.check(action.bit, !enabled)
            false
        }

        is SettingAction.TextSpeed -> {
            layer.check2(0, action.index); false
        }

        is SettingAction.NotifyLevel -> {
            layer.check2(2, action.index); false
        }

        is SettingAction.Background -> {
            layer.selectBackground(action.index); false
        }

        is SettingAction.GameSpeed -> {
            layer.onSlider(action.progress); false
        }
    }

    /**
     * interface  `MainRoutes`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface MainRoutes {
        /**
         * 공개 메서드 `newGame`
         *
         * ### 파라미터
        - `moduleName` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun newGame(moduleName: String)

        /**
         * 공개 메서드 `openLoad`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun openLoad()

        /**
         * 공개 메서드 `openSettings`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun openSettings()

        /**
         * 공개 메서드 `requestExit`
         *
         * ### 파라미터
        - 입력 파라미터: 없음
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun requestExit()
    }

    /** Keeps the source button ordering and the R_00 new-game destination explicit and testable. */
    fun dispatch(action: MainAction, routes: MainRoutes) = when (action) {
        MainAction.NEW_GAME -> routes.newGame("R_00")
        MainAction.LOAD -> routes.openLoad()
        MainAction.SETTINGS -> routes.openSettings()
        MainAction.EXIT -> routes.requestExit()
    }
}
