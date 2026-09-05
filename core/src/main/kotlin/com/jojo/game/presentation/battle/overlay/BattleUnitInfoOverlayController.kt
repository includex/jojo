package com.jojo.game.presentation.battle.overlay

import com.jojo.game.BattleUnitInfoJiqiRoute
import com.jojo.game.JiQiLayer
import com.jojo.game.UnitInfoLayer
import com.jojo.game.presentation.battle.BattleUnitInfoOverlayView
import com.jojo.game.presentation.battle.BattleUnitInfoUnitView

/** Owns UnitInfoLayer lifecycle and source-compatible button press gestures. */
internal class BattleUnitInfoOverlayController(
    private val jiqiRates: List<Int> = DEFAULT_JIQI_RATES,
) {
    sealed interface Intent {
        data class PointerDown(val x: Float, val y: Float) : Intent
        data class PointerUp(val x: Float, val y: Float) : Intent
        data object OpenJiqi : Intent
        data object Dismiss : Intent
    }

    sealed interface Effect {
        data object None : Effect
        data object Closed : Effect
        data class JiqiOpened(val layer: JiQiLayer) : Effect
    }

    data class DispatchResult(val consumed: Boolean, val effect: Effect = Effect.None)

    private sealed interface State {
        data object Hidden : State
        data class Visible(val layer: UnitInfoLayer, val pressedButton: Int? = null) : State
    }

    private var state: State = State.Hidden

    fun open(units: List<UnitInfoLayer.Unit>, index: Int) {
        val layer = UnitInfoLayer(units, flag = UnitInfoLayer.BATTLE_FLAG, editEnabled = true).also { it.onCreate(index) }
        state = State.Visible(layer)
    }

    fun isVisible(): Boolean = state is State.Visible

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
            ),
            buttons = source.buttons.toList(),
            magicRows = source.magicRows.toList(),
        )
    }

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
                DispatchResult(consumed = true, effect = Effect.Closed)
            }
        }
    }

    private fun pointerUp(visible: State.Visible, releasedButton: Int?): DispatchResult {
        state = visible.copy(pressedButton = null)
        if (releasedButton == null || releasedButton != visible.pressedButton) return DispatchResult(consumed = true)
        return if (releasedButton == JIQI_BUTTON) openJiqi(visible) else {
            visible.layer.onButton(releasedButton, UnitInfoLayer.TOUCH_END)
            closeIfDetached()
        }
    }

    private fun openJiqi(visible: State.Visible): DispatchResult {
        val jiqi = BattleUnitInfoJiqiRoute.open(visible.layer, jiqiRates, UnitInfoLayer.TOUCH_END)
        val after = closeIfDetached()
        return jiqi?.let { DispatchResult(consumed = true, effect = Effect.JiqiOpened(it)) } ?: after
    }

    private fun closeIfDetached(): DispatchResult {
        val visible = state as? State.Visible ?: return DispatchResult(consumed = true, effect = Effect.Closed)
        return if (!visible.layer.ref().attached) {
            state = State.Hidden
            DispatchResult(consumed = true, effect = Effect.Closed)
        } else DispatchResult(consumed = true)
    }

    private fun buttonAt(x: Float, y: Float): Int? = when {
        x in JIQI_LEFT..JIQI_RIGHT && y in JIQI_BOTTOM..JIQI_TOP -> JIQI_BUTTON
        x in PREVIOUS_LEFT..PREVIOUS_RIGHT && y in NAV_BOTTOM..NAV_TOP -> 5
        x in NEXT_LEFT..NEXT_RIGHT && y in NAV_BOTTOM..NAV_TOP -> 6
        x in CLOSE_LEFT..CLOSE_RIGHT && y in NAV_BOTTOM..NAV_TOP -> 7
        x in TAB_LEFT..TAB_RIGHT && y in TAB_BOTTOM..TAB_TOP -> ((x - TAB_LEFT) / TAB_WIDTH).toInt()
        else -> null
    }

    private companion object {
        val DEFAULT_JIQI_RATES = listOf(85, 57, 39, 95, 24, 22, 99, 48)
        const val JIQI_BUTTON = 9
        const val JIQI_LEFT = 700.71f
        const val JIQI_RIGHT = 810.71f
        const val JIQI_BOTTOM = 17.207f
        const val JIQI_TOP = 67.207f
        const val PREVIOUS_LEFT = 970f
        const val PREVIOUS_RIGHT = 1140f
        const val NEXT_LEFT = 1140f
        const val NEXT_RIGHT = 1280f
        const val CLOSE_LEFT = 760f
        const val CLOSE_RIGHT = 870f
        const val NAV_BOTTOM = 35f
        const val NAV_TOP = 95f
        const val TAB_LEFT = 80f
        const val TAB_RIGHT = 850f
        const val TAB_BOTTOM = 650f
        const val TAB_TOP = 720f
        const val TAB_WIDTH = 150f
    }
}
