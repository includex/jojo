// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.overlay.SettingLayer
import kotlin.math.abs
/**
 * `BattleSettingsOverlayController`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class BattleSettingsOverlayController(private val layer: SettingLayer) {
    /** 설정 패널의 누름과 닫기 요청을 화면 좌표로 전달하는 입력이다. */
    sealed interface Intent {
        /**
         * `PointerDown`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class PointerDown(val x: Float, val y: Float) : Intent
        /**
         * `PointerUp`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class PointerUp(val x: Float, val y: Float) : Intent
        /**
         * `Close`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Close : Intent
    }

    /** 설정 패널이 닫혔는지 알려 주는 처리 결과이다. */
    sealed interface Effect {
        /**
         * `None`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object None : Effect
        /**
         * `Closed`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Closed : Effect
    }
    /**
     * `DispatchResult`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class DispatchResult(val consumed: Boolean, val effect: Effect = Effect.None)

    /** 설정 패널의 표시 여부와 마지막으로 누른 좌표를 보관한다. */
    private sealed interface State {
        /**
         * `Hidden`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Hidden : State
        /**
         * `Visible`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Visible(val press: Point? = null) : State
    }
    /**
     * `Point`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class Point(val x: Float, val y: Float)

    /**
     * `state` (State): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var state: State = State.Hidden

    /**
     * `open`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun open() {
        layer.onCreate()
        state = State.Visible()
    }

    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(): BattleSettingsOverlayView? {
        if (state !is State.Visible) return null
        val view = layer.view()
        if (!view.attached) {
            state = State.Hidden
            return null
        }
        return BattleSettingsOverlayView(view.flags, view.msgSpeed, view.notifyLevel, view.background)
    }

    /**
     * `dispatch`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispatch(intent: Intent): DispatchResult {
        val visible = state as? State.Visible ?: return DispatchResult(consumed = false)
        return when (intent) {
            is Intent.PointerDown -> {
                state = visible.copy(press = Point(intent.x, intent.y))
                DispatchResult(consumed = true)
            }
            is Intent.PointerUp -> pointerUp(visible, intent)
            Intent.Close -> close()
        }
    }

    /**
     * `pointerUp`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun pointerUp(visible: State.Visible, intent: Intent.PointerUp): DispatchResult {
        val pressed = visible.press
        state = visible.copy(press = null)
        if (pressed != null && abs(pressed.x - intent.x) < MAX_CLICK_DRIFT && abs(pressed.y - intent.y) < MAX_CLICK_DRIFT) {
            applyTap(intent.x, intent.y)
        }
        return if (state is State.Hidden) DispatchResult(consumed = true, effect = Effect.Closed)
        else DispatchResult(consumed = true)
    }

    /**
     * `applyTap`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun applyTap(x: Float, y: Float) {
        if (x !in PANEL_LEFT..PANEL_RIGHT || y !in PANEL_BOTTOM..PANEL_TOP) return
        if (x in CLOSE_LEFT..CLOSE_RIGHT && y in CLOSE_BOTTOM..CLOSE_TOP) {
            close()
            return
        }
        if (x in CHECK_LEFT..CHECK_RIGHT && y in CHECK_BOTTOM..CHECK_TOP) {
            val bit = ((CHECK_BASELINE - y) / CHECK_STEP).toInt().coerceIn(0, 4)
            val flags = layer.view().flags
            layer.check(bit, flags and (1 shl bit) == 0)
            return
        }
        if (x in RADIO_LEFT..RADIO_RIGHT && y in MESSAGE_RADIO_BOTTOM..MESSAGE_RADIO_TOP) {
            layer.check2(0, ((x - RADIO_START) / RADIO_STEP).toInt().coerceIn(0, 2))
            return
        }
        if (x in RADIO_LEFT..RADIO_RIGHT && y in NOTICE_RADIO_BOTTOM..NOTICE_RADIO_TOP) {
            layer.check2(2, ((x - RADIO_START) / RADIO_STEP).toInt().coerceIn(0, 2))
            return
        }
        if (x in RADIO_LEFT..RADIO_RIGHT && y in BACKGROUND_BOTTOM..BACKGROUND_TOP) {
            layer.selectBackground(((x - RADIO_START) / BACKGROUND_STEP).toInt().coerceIn(0, 3))
            return
        }
        if (x in SLIDER_LEFT..SLIDER_RIGHT && y in SLIDER_BOTTOM..SLIDER_TOP) layer.onSlider((x - SLIDER_LEFT) / (SLIDER_RIGHT - SLIDER_LEFT))
    }

    /**
     * `close`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun close(): DispatchResult {
        layer.close(TOUCH_END)
        state = State.Hidden
        return DispatchResult(consumed = true, effect = Effect.Closed)
    }

    private companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = SettingLayer.TOUCH_END
        /**
         * `MAX_CLICK_DRIFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MAX_CLICK_DRIFT = 20f
        /**
         * `PANEL_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val PANEL_LEFT = 196f
        /**
         * `PANEL_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val PANEL_RIGHT = 1293f
        /**
         * `PANEL_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val PANEL_BOTTOM = 41f
        /**
         * `PANEL_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val PANEL_TOP = 759f
        /**
         * `CLOSE_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CLOSE_LEFT = 1130f
        /**
         * `CLOSE_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CLOSE_RIGHT = 1286f
        /**
         * `CLOSE_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CLOSE_BOTTOM = 47f
        /**
         * `CLOSE_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CLOSE_TOP = 103f
        /**
         * `CHECK_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHECK_LEFT = 214f
        /**
         * `CHECK_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHECK_RIGHT = 726f
        /**
         * `CHECK_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHECK_BOTTOM = 360f
        /**
         * `CHECK_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHECK_TOP = 665f
        /**
         * `CHECK_BASELINE` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHECK_BASELINE = 663f
        /**
         * `CHECK_STEP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHECK_STEP = 65f
        /**
         * `RADIO_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val RADIO_LEFT = 793f
        /**
         * `RADIO_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val RADIO_RIGHT = 1273f
        /**
         * `RADIO_START` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val RADIO_START = 816f
        /**
         * `RADIO_STEP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val RADIO_STEP = 145f
        /**
         * `MESSAGE_RADIO_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MESSAGE_RADIO_BOTTOM = 535f
        /**
         * `MESSAGE_RADIO_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MESSAGE_RADIO_TOP = 605f
        /**
         * `NOTICE_RADIO_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val NOTICE_RADIO_BOTTOM = 271f
        /**
         * `NOTICE_RADIO_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val NOTICE_RADIO_TOP = 341f
        /**
         * `BACKGROUND_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val BACKGROUND_BOTTOM = 81f
        /**
         * `BACKGROUND_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val BACKGROUND_TOP = 170f
        /**
         * `BACKGROUND_STEP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val BACKGROUND_STEP = 105f
        /**
         * `SLIDER_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SLIDER_LEFT = 230f
        /**
         * `SLIDER_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SLIDER_RIGHT = 570f
        /**
         * `SLIDER_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SLIDER_BOTTOM = 120f
        /**
         * `SLIDER_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SLIDER_TOP = 165f
    }
}
