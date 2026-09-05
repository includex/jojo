package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.overlay.HelperLayer
import com.jojo.game.presentation.battle.BattleHelperOverlayView

/**
 * Owns the HelperLayer lifecycle and its confirmation-button gesture.
 *
 * The screen only consumes the immutable [BattleHelperOverlayView] projection
 * and sends pointer intents, so the transient pressed state cannot leak into
 * the rest of BattleScreen's overlay state.
 */
internal class BattleHelperOverlayController {
    private sealed interface State {
        data object Hidden : State
        data class Visible(
            val layer: HelperLayer,
            val confirmPressed: Boolean = false,
        ) : State
    }

    sealed interface Intent {
        data class PointerDown(val x: Float, val y: Float) : Intent
        data class PointerUp(val x: Float, val y: Float) : Intent
    }

    private var state: State = State.Hidden

    fun open(model: HelperLayer.Model) {
        state = State.Visible(HelperLayer(model).also { it.onCreate() })
    }

    fun view(): BattleHelperOverlayView? {
        val visible = state as? State.Visible ?: return null
        val layerView = visible.layer.view()
        if (!layerView.attached) {
            state = State.Hidden
            return null
        }
        return BattleHelperOverlayView(
            richText = layerView.richText,
            buttonText = layerView.prefab.buttonText,
        )
    }

    /** Returns true when the helper overlay owns this input event. */
    fun dispatch(intent: Intent): Boolean {
        val visible = state as? State.Visible ?: return false
        when (intent) {
            is Intent.PointerDown -> {
                state = visible.copy(confirmPressed = intent.isConfirmButton())
            }

            is Intent.PointerUp -> {
                if (visible.confirmPressed && intent.isConfirmButton()) {
                    visible.layer.onButtonTouch(HelperLayer.TOUCH_END)
                }
                state = if (visible.layer.view().attached) {
                    visible.copy(confirmPressed = false)
                } else {
                    State.Hidden
                }
            }
        }
        return true
    }

    private fun Intent.isConfirmButton(): Boolean = when (this) {
        is Intent.PointerDown -> x in CONFIRM_LEFT..CONFIRM_RIGHT && y in CONFIRM_BOTTOM..CONFIRM_TOP
        is Intent.PointerUp -> x in CONFIRM_LEFT..CONFIRM_RIGHT && y in CONFIRM_BOTTOM..CONFIRM_TOP
    }

    private companion object {
        const val CONFIRM_LEFT = 1172.451f
        const val CONFIRM_RIGHT = 1320.051f
        const val CONFIRM_BOTTOM = 33.187f
        const val CONFIRM_TOP = 89.187f
    }
}
