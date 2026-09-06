// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.presentation.battle.overlay.ForcesListLayer
/**
 * `BattleForcesOverlayController`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class BattleForcesOverlayController {
    /** 부대 목록에서 탭 전환과 닫기 동작을 전달하는 포인터 입력이다. */
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

    /** 부대 목록이 닫혔거나 선택된 유닛을 화면에 알리는 결과이다. */
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
         * `UnitSelected`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class UnitSelected(val unit: SelectedUnit) : Effect
    }
    /**
     * `SelectedUnit`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class SelectedUnit(
        /**
         * `characterId` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val characterId: Int,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String,
        /**
         * `post` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val post: String,
        /**
         * `level` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val level: Int,
        /**
         * `hp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hp: Int,
        /**
         * `maxHp` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxHp: Int,
    )
    /**
     * `DispatchResult`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class DispatchResult(val consumed: Boolean, val effect: Effect = Effect.None)

    /** 부대 목록의 연결 상태와 현재 누른 탭·행을 보관한다. */
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

        data class Visible(val layer: ForcesListLayer, val press: Press = Press.None) : State
    }
    /**
     * `Press`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private sealed interface Press {
        /**
         * `None`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object None : Press
        /**
         * `Close`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Close : Press
        /**
         * `Tab`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Tab(val index: Int) : Press
        /**
         * `Row`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Row(val index: Int) : Press
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

    fun open(mine: List<ForcesListLayer.Unit>, enemy: List<ForcesListLayer.Unit>, flag: Int) {
        val layer = ForcesListLayer().also { it.onCreate(mine, enemy, flag) }
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

    fun view(): BattleForcesOverlayView? {
        val visible = state as? State.Visible ?: return null
        val source = visible.layer.view()
        if (!source.attached) {
            state = State.Hidden
            return null
        }
        return BattleForcesOverlayView(
            selectedTab = source.selectedTab,
            tabsVisible = source.tabsVisible,
            rows = source.rows.map { row ->
                val unit = row.unit
                BattleForcesRowView(
                    values = listOf(
                        unit.name, unit.post, unit.level.toString(), "${unit.hp}/${unit.maxHp}",
                        "${unit.mp}/${unit.maxMp}", unit.attack.toString(), unit.defense.toString(),
                        unit.spirit.toString(), unit.critical.toString(), unit.morale.toString(),
                    ),
                )
            },
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
                state = visible.copy(press = pressAt(visible.layer, intent.x, intent.y))
                DispatchResult(consumed = true)
            }
            is Intent.PointerUp -> pointerUp(visible, intent.x, intent.y)
        }
    }

    /**
     * `pointerUp`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun pointerUp(visible: State.Visible, x: Float, y: Float): DispatchResult {
        val released = pressAt(visible.layer, x, y)
        state = visible.copy(press = Press.None)
        return when {
            visible.press == Press.Close && released == Press.Close -> {
                visible.layer.onClose(ForcesListLayer.TOUCH_END)
                state = State.Hidden
                DispatchResult(consumed = true, effect = Effect.Closed)
            }
            visible.press is Press.Tab && released == visible.press -> {
                visible.layer.changeSel(visible.press.index)
                DispatchResult(consumed = true)
            }
            visible.press is Press.Row && released == visible.press -> {
                val unit = visible.layer.onRowTouch(visible.press.index, ForcesListLayer.TOUCH_END)
                val effect = unit?.let {
                    Effect.UnitSelected(SelectedUnit(it.id, it.name, it.post, it.level, it.hp, it.maxHp))
                } ?: Effect.None
                DispatchResult(consumed = true, effect = effect)
            }
            else -> DispatchResult(consumed = true)
        }
    }

    /**
     * `pressAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun pressAt(layer: ForcesListLayer, x: Float, y: Float): Press = when {
        x in CLOSE_LEFT..CLOSE_RIGHT && y in HEADER_BOTTOM..HEADER_TOP -> Press.Close
        layer.view().tabsVisible && y in HEADER_BOTTOM..HEADER_TOP && x in MINE_TAB_LEFT..MINE_TAB_RIGHT -> Press.Tab(0)
        layer.view().tabsVisible && y in HEADER_BOTTOM..HEADER_TOP && x in ENEMY_TAB_LEFT..ENEMY_TAB_RIGHT -> Press.Tab(1)
        x in ROW_LEFT..ROW_RIGHT && y in ROW_BOTTOM..ROW_TOP -> Press.Row(((ROW_TOP - y) / ROW_HEIGHT).toInt())
        else -> Press.None
    }

    private companion object {
        /**
         * `CLOSE_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CLOSE_LEFT = 1135f
        /**
         * `CLOSE_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CLOSE_RIGHT = 1310f
        /**
         * `HEADER_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val HEADER_BOTTOM = 88f
        /**
         * `HEADER_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val HEADER_TOP = 145f
        /**
         * `MINE_TAB_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MINE_TAB_LEFT = 215f
        /**
         * `MINE_TAB_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MINE_TAB_RIGHT = 345f
        /**
         * `ENEMY_TAB_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ENEMY_TAB_LEFT = 360f
        /**
         * `ENEMY_TAB_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ENEMY_TAB_RIGHT = 495f
        /**
         * `ROW_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROW_LEFT = 170f
        /**
         * `ROW_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROW_RIGHT = 1318f
        /**
         * `ROW_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROW_BOTTOM = 150f
        /**
         * `ROW_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROW_TOP = 614f
        /**
         * `ROW_HEIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROW_HEIGHT = 60f
    }
}
