// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.overlay.HelperLayer
internal class BattleHelperOverlayController {
    /** 도움말 레이어의 연결 상태와 확인 버튼 누름 상태를 보관한다. */
    private sealed interface State {
        data object Hidden : State
        data class Visible(
            val layer: HelperLayer,
            val confirmPressed: Boolean = false,
        ) : State
    }

    /** 도움말 오버레이의 확인 버튼 좌표를 전달하는 포인터 입력이다. */
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

    /** 확인 버튼 입력을 레이어에 전달하고 닫힘 여부를 내부 상태에 반영한다. */
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
