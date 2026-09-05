package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.overlay.SettingLayer
import com.jojo.game.presentation.battle.BattleSettingsOverlayView
import kotlin.math.abs

/** Owns the settings overlay lifecycle and source-prefab press gesture. */
internal class BattleSettingsOverlayController(private val layer: SettingLayer) {
    sealed interface Intent {
        data class PointerDown(val x: Float, val y: Float) : Intent
        data class PointerUp(val x: Float, val y: Float) : Intent
        data object Close : Intent
    }

    sealed interface Effect {
        data object None : Effect
        data object Closed : Effect
    }

    data class DispatchResult(val consumed: Boolean, val effect: Effect = Effect.None)

    private sealed interface State {
        data object Hidden : State
        data class Visible(val press: Point? = null) : State
    }

    private data class Point(val x: Float, val y: Float)

    private var state: State = State.Hidden

    fun open() {
        layer.onCreate()
        state = State.Visible()
    }

    fun view(): BattleSettingsOverlayView? {
        if (state !is State.Visible) return null
        val view = layer.view()
        if (!view.attached) {
            state = State.Hidden
            return null
        }
        return BattleSettingsOverlayView(view.flags, view.msgSpeed, view.notifyLevel, view.background)
    }

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

    private fun pointerUp(visible: State.Visible, intent: Intent.PointerUp): DispatchResult {
        val pressed = visible.press
        state = visible.copy(press = null)
        if (pressed != null && abs(pressed.x - intent.x) < MAX_CLICK_DRIFT && abs(pressed.y - intent.y) < MAX_CLICK_DRIFT) {
            applyTap(intent.x, intent.y)
        }
        return if (state is State.Hidden) DispatchResult(consumed = true, effect = Effect.Closed)
        else DispatchResult(consumed = true)
    }

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

    private fun close(): DispatchResult {
        layer.close(TOUCH_END)
        state = State.Hidden
        return DispatchResult(consumed = true, effect = Effect.Closed)
    }

    private companion object {
        const val TOUCH_END = SettingLayer.TOUCH_END
        const val MAX_CLICK_DRIFT = 20f
        const val PANEL_LEFT = 196f
        const val PANEL_RIGHT = 1293f
        const val PANEL_BOTTOM = 41f
        const val PANEL_TOP = 759f
        const val CLOSE_LEFT = 1130f
        const val CLOSE_RIGHT = 1286f
        const val CLOSE_BOTTOM = 47f
        const val CLOSE_TOP = 103f
        const val CHECK_LEFT = 214f
        const val CHECK_RIGHT = 726f
        const val CHECK_BOTTOM = 360f
        const val CHECK_TOP = 665f
        const val CHECK_BASELINE = 663f
        const val CHECK_STEP = 65f
        const val RADIO_LEFT = 793f
        const val RADIO_RIGHT = 1273f
        const val RADIO_START = 816f
        const val RADIO_STEP = 145f
        const val MESSAGE_RADIO_BOTTOM = 535f
        const val MESSAGE_RADIO_TOP = 605f
        const val NOTICE_RADIO_BOTTOM = 271f
        const val NOTICE_RADIO_TOP = 341f
        const val BACKGROUND_BOTTOM = 81f
        const val BACKGROUND_TOP = 170f
        const val BACKGROUND_STEP = 105f
        const val SLIDER_LEFT = 230f
        const val SLIDER_RIGHT = 570f
        const val SLIDER_BOTTOM = 120f
        const val SLIDER_TOP = 165f
    }
}
