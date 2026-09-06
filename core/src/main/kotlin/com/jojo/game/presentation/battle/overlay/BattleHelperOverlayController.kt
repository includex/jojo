// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.overlay.HelperLayer
/**
 * `BattleHelperOverlayController`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class BattleHelperOverlayController {
    /** 도움말 레이어의 연결 상태와 확인 버튼 누름 상태를 보관한다. */
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

        data class Visible(
            /**
             * `layer` (HelperLayer,): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val layer: HelperLayer,
            /**
             * `confirmPressed` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val confirmPressed: Boolean = false,
        ) : State
    }

    /** 도움말 오버레이의 확인 버튼 좌표를 전달하는 포인터 입력이다. */
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
    }

    /**
     * `state` (State): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var state: State = State.Hidden

    /**
     * `open`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun open(model: HelperLayer.Model) {
        state = State.Visible(HelperLayer(model).also { it.onCreate() })
    }

    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `Intent`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun Intent.isConfirmButton(): Boolean = when (this) {
        is Intent.PointerDown -> x in CONFIRM_LEFT..CONFIRM_RIGHT && y in CONFIRM_BOTTOM..CONFIRM_TOP
        is Intent.PointerUp -> x in CONFIRM_LEFT..CONFIRM_RIGHT && y in CONFIRM_BOTTOM..CONFIRM_TOP
    }

    private companion object {
        /**
         * `CONFIRM_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_LEFT = 1172.451f
        /**
         * `CONFIRM_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_RIGHT = 1320.051f
        /**
         * `CONFIRM_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_BOTTOM = 33.187f
        /**
         * `CONFIRM_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_TOP = 89.187f
    }
}
