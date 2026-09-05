package com.jojo.game.presentation.battle.overlay

import com.jojo.game.ForcesListLayer
import com.jojo.game.presentation.battle.BattleForcesOverlayView
import com.jojo.game.presentation.battle.BattleForcesRowView

/** Owns ForcesListLayer lifecycle, press matching, and its immutable renderer view. */
internal class BattleForcesOverlayController {
    sealed interface Intent {
        data class PointerDown(val x: Float, val y: Float) : Intent
        data class PointerUp(val x: Float, val y: Float) : Intent
    }

    sealed interface Effect {
        data object None : Effect
        data object Closed : Effect
        data class UnitSelected(val unit: SelectedUnit) : Effect
    }

    data class SelectedUnit(
        val characterId: Int,
        val name: String,
        val post: String,
        val level: Int,
        val hp: Int,
        val maxHp: Int,
    )

    data class DispatchResult(val consumed: Boolean, val effect: Effect = Effect.None)

    private sealed interface State {
        data object Hidden : State
        data class Visible(val layer: ForcesListLayer, val press: Press = Press.None) : State
    }

    private sealed interface Press {
        data object None : Press
        data object Close : Press
        data class Tab(val index: Int) : Press
        data class Row(val index: Int) : Press
    }

    private var state: State = State.Hidden

    fun open(mine: List<ForcesListLayer.Unit>, enemy: List<ForcesListLayer.Unit>, flag: Int) {
        val layer = ForcesListLayer().also { it.onCreate(mine, enemy, flag) }
        state = State.Visible(layer)
    }

    fun isVisible(): Boolean = state is State.Visible

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

    private fun pressAt(layer: ForcesListLayer, x: Float, y: Float): Press = when {
        x in CLOSE_LEFT..CLOSE_RIGHT && y in HEADER_BOTTOM..HEADER_TOP -> Press.Close
        layer.view().tabsVisible && y in HEADER_BOTTOM..HEADER_TOP && x in MINE_TAB_LEFT..MINE_TAB_RIGHT -> Press.Tab(0)
        layer.view().tabsVisible && y in HEADER_BOTTOM..HEADER_TOP && x in ENEMY_TAB_LEFT..ENEMY_TAB_RIGHT -> Press.Tab(1)
        x in ROW_LEFT..ROW_RIGHT && y in ROW_BOTTOM..ROW_TOP -> Press.Row(((ROW_TOP - y) / ROW_HEIGHT).toInt())
        else -> Press.None
    }

    private companion object {
        const val CLOSE_LEFT = 1135f
        const val CLOSE_RIGHT = 1310f
        const val HEADER_BOTTOM = 88f
        const val HEADER_TOP = 145f
        const val MINE_TAB_LEFT = 215f
        const val MINE_TAB_RIGHT = 345f
        const val ENEMY_TAB_LEFT = 360f
        const val ENEMY_TAB_RIGHT = 495f
        const val ROW_LEFT = 170f
        const val ROW_RIGHT = 1318f
        const val ROW_BOTTOM = 150f
        const val ROW_TOP = 614f
        const val ROW_HEIGHT = 60f
    }
}
