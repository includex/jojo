// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.infrastructure.audio.UiSound
import com.jojo.game.presentation.shared.overlay.UnitInfoLayer
/**
 * `BattleUnitInfoOverlayController`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class BattleUnitInfoOverlayController(
    /**
     * 기치 확률을 얼려 두는 검증 경로의 값이다.
     *
     * 원본은 전투를 차릴 때 유닛마다 난수 여덟 개를 뽑으므로 실제 값은 실행마다 다르다.
     * 원본 하네스도 이 화면을 비교할 때는 유닛의 `rates()`를 같은 여덟 값으로 얼려 둔다.
     * 평소에는 `null`이고, 그때는 보고 있는 무장의 값을 그대로 쓴다.
     */
    private val frozenRates: () -> List<Int>? = { null },
) {
    /** 유닛 정보의 버튼 누름, 닫기, 기기 목록 열기를 요청하는 입력이다. */
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
         * `OpenJiqi`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object OpenJiqi : Intent
        /**
         * `Dismiss`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Dismiss : Intent
    }

    /** 유닛 정보가 닫혔거나 기기 목록으로 전환됐음을 보고한다. */
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
        /**
         * `JiqiOpened`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class JiqiOpened(val layer: JiQiLayer) : Effect
    }
    /**
     * `DispatchResult`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class DispatchResult(
        val consumed: Boolean,
        val effect: Effect = Effect.None,
        /** 이 누름에 원본이 내는 단추 소리다. */
        val uiSound: UiSound? = null,
    )

    /** 선택된 유닛 정보 레이어와 눌린 버튼을 보관한다. */
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

        data class Visible(val layer: UnitInfoLayer, val pressedButton: Int? = null) : State
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

    fun open(units: List<UnitInfoLayer.Unit>, index: Int) {
        val layer = UnitInfoLayer(units, flag = UnitInfoLayer.BATTLE_FLAG, editEnabled = true).also { it.onCreate(index) }
        state = State.Visible(layer)
    }

    /**
     * `isVisible`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun isVisible(): Boolean = state is State.Visible

    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(): BattleUnitInfoOverlayView? {
        val visible = state as? State.Visible ?: return null
        val source = visible.layer.ref()
        if (!source.attached) {
            state = State.Hidden
            return null
        }
        val unit = source.unit
        return BattleUnitInfoOverlayView(
            tab = source.tab,
            unit = BattleUnitInfoUnitView(
                name = unit.name, post = unit.post, level = unit.level, hp = unit.hp, maxHp = unit.maxHp,
                mp = unit.mp, maxMp = unit.maxMp, attack = unit.attack, defense = unit.defense,
                spirit = unit.spirit, critical = unit.critical, morale = unit.morale,
                mine = unit.mine, battleCount = unit.battleCount, retreatCount = unit.retreatCount,
                skillIntro = unit.skillIntro, unitIntro = unit.unitIntro,
            ),
            buttons = source.buttons.toList(),
            magicRows = source.magicRows.toList(),
        )
    }

    /**
     * `dispatch`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispatch(intent: Intent): DispatchResult {
        val visible = state as? State.Visible ?: return DispatchResult(consumed = false)
        return when (intent) {
            is Intent.PointerDown -> {
                state = visible.copy(pressedButton = buttonAt(intent.x, intent.y))
                DispatchResult(consumed = true)
            }
            is Intent.PointerUp -> pointerUp(visible, buttonAt(intent.x, intent.y))
            Intent.OpenJiqi -> openJiqi(visible)
            Intent.Dismiss -> {
                state = State.Hidden
                // 뒤쪽 막(`Panel_cancel`)만 깃발 2라 취소음이 난다.
                DispatchResult(consumed = true, effect = Effect.Closed, uiSound = UiSound.CANCEL)
            }
        }
    }

    /**
     * `pointerUp`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun pointerUp(visible: State.Visible, releasedButton: Int?): DispatchResult {
        state = visible.copy(pressedButton = null)
        if (releasedButton == null || releasedButton != visible.pressedButton) return DispatchResult(consumed = true)
        // 원본 `UnitInfoLayer`는 `bg1/button<i>`를 모두 깃발 1로 등록한다. 뒤쪽 막만
        // 깃발 2인데, 그쪽은 `Intent.Dismiss`로 들어온다.
        return if (releasedButton == JIQI_BUTTON) openJiqi(visible) else {
            visible.layer.onButton(releasedButton, UnitInfoLayer.TOUCH_END)
            closeIfDetached().copy(uiSound = UiSound.CLICK)
        }
    }

    /**
     * `openJiqi`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun openJiqi(visible: State.Visible): DispatchResult {
        // 여덟 확률은 유닛마다 다르다. 예전에는 원본 고정 자료에서 잰 여덟 값을 모든
        // 유닛에 똑같이 보여 주었다.
        val rates = frozenRates() ?: visible.layer.currentRates().takeIf { it.size == 8 } ?: FIXTURE_JIQI_RATES
        val jiqi = BattleUnitInfoJiqiRoute.open(visible.layer, rates, UnitInfoLayer.TOUCH_END)
        val after = closeIfDetached()
        return jiqi?.let {
            DispatchResult(consumed = true, effect = Effect.JiqiOpened(it), uiSound = UiSound.CLICK)
        } ?: after.copy(uiSound = UiSound.CLICK)
    }

    /**
     * `closeIfDetached`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun closeIfDetached(): DispatchResult {
        val visible = state as? State.Visible ?: return DispatchResult(consumed = true, effect = Effect.Closed)
        return if (!visible.layer.ref().attached) {
            state = State.Hidden
            DispatchResult(consumed = true, effect = Effect.Closed)
        } else DispatchResult(consumed = true)
    }

    /**
     * `buttonAt`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun buttonAt(x: Float, y: Float): Int? = when {
        x in JIQI_LEFT..JIQI_RIGHT && y in JIQI_BOTTOM..JIQI_TOP -> JIQI_BUTTON
        x in PREVIOUS_LEFT..PREVIOUS_RIGHT && y in NAV_BOTTOM..NAV_TOP -> 5
        x in NEXT_LEFT..NEXT_RIGHT && y in NAV_BOTTOM..NAV_TOP -> 6
        x in CLOSE_LEFT..CLOSE_RIGHT && y in NAV_BOTTOM..NAV_TOP -> 7
        x in TAB_LEFT..TAB_RIGHT && y in TAB_BOTTOM..TAB_TOP -> ((x - TAB_LEFT) / TAB_WIDTH).toInt()
        else -> null
    }

    companion object {
        /**
         * 기치 확률 비교 경로가 쓰는 여덟 값이다.
         *
         * 원본 하네스가 `--render-battle-jiqi-route`에서 유닛의 `rates()`를 이 값으로
         * 얼려 두므로, 이식본도 그 경로에서만 같은 값을 쓴다. 실제 놀이에서는 유닛마다
         * 전투를 차릴 때 뽑은 값이 그대로 나온다.
         */
        val FIXTURE_JIQI_RATES = listOf(85, 57, 39, 95, 24, 22, 99, 48)
        /**
         * `JIQI_BUTTON` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val JIQI_BUTTON = 9
        /**
         * `JIQI_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val JIQI_LEFT = 700.71f
        /**
         * `JIQI_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val JIQI_RIGHT = 810.71f
        /**
         * `JIQI_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val JIQI_BOTTOM = 17.207f
        /**
         * `JIQI_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val JIQI_TOP = 67.207f
        /**
         * `PREVIOUS_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val PREVIOUS_LEFT = 970f
        /**
         * `PREVIOUS_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val PREVIOUS_RIGHT = 1140f
        /**
         * `NEXT_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val NEXT_LEFT = 1140f
        /**
         * `NEXT_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val NEXT_RIGHT = 1280f
        /**
         * `CLOSE_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CLOSE_LEFT = 760f
        /**
         * `CLOSE_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CLOSE_RIGHT = 870f
        /**
         * `NAV_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val NAV_BOTTOM = 35f
        /**
         * `NAV_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val NAV_TOP = 95f
        /**
         * `TAB_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TAB_LEFT = 80f
        /**
         * `TAB_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TAB_RIGHT = 850f
        /**
         * `TAB_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TAB_BOTTOM = 650f
        /**
         * `TAB_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TAB_TOP = 720f
        /**
         * `TAB_WIDTH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TAB_WIDTH = 150f
    }
}
