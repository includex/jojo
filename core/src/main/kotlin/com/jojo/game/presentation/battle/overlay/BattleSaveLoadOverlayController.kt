package com.jojo.game.presentation.battle.overlay

import com.jojo.game.LoadGameLayer
import com.jojo.game.SaveLayer
import com.jojo.game.presentation.battle.BattleSaveLoadOverlayKind
import com.jojo.game.presentation.battle.BattleSaveLoadOverlayView
import com.jojo.game.presentation.battle.BattleSaveLoadRowView

/**
 * Coordinates the source SaveLayer and LoadGameLayer protocols with one
 * mutually-exclusive overlay state.  It owns hit testing, scroll position,
 * press tracking, and source-layer lifecycle; callers observe only a view and
 * closure effects.
 */
internal class BattleSaveLoadOverlayController(
    saveRepository: SaveLayer.Repository,
    loadRepository: LoadGameLayer.Repository,
) {
    enum class Mode { SAVE, LOAD }

    sealed interface Intent {
        data class PointerDown(val x: Float, val y: Float) : Intent
        data class PointerUp(val x: Float, val y: Float) : Intent
        data class Scroll(val rows: Int) : Intent
        data object Cancel : Intent
    }

    sealed interface Effect {
        data object None : Effect
        data class Closed(val mode: Mode, val saved: Boolean) : Effect
    }

    data class DispatchResult(val consumed: Boolean, val effect: Effect = Effect.None)

    private sealed interface State {
        data object Hidden : State
        data class Open(
            val mode: Mode,
            val scrollRow: Int = 0,
            val press: Press = Press.None,
            val saveCommitted: Boolean = false,
        ) : State
    }

    private sealed interface Press {
        data object None : Press
        data class Row(val index: Int) : Press
        data class Confirmation(val answer: Int) : Press
    }

    private val saveLayer = SaveLayer(saveRepository)
    private val loadLayer = LoadGameLayer(loadRepository)
    private var state: State = State.Hidden

    fun openSave(savedPage: Int = 0) {
        saveLayer.onCreate(savedPage = savedPage)
        state = State.Open(Mode.SAVE)
    }

    fun openLoad() {
        loadLayer.onCreate()
        state = State.Open(Mode.LOAD)
    }

    fun view(mode: Mode): BattleSaveLoadOverlayView? {
        val open = state as? State.Open ?: return null
        if (open.mode != mode) return null
        return when (mode) {
            Mode.SAVE -> saveView(open)
            Mode.LOAD -> loadView(open)
        }
    }

    fun dispatch(intent: Intent): DispatchResult {
        val open = state as? State.Open ?: return DispatchResult(consumed = false)
        return when (intent) {
            is Intent.PointerDown -> pointerDown(open, intent)
            is Intent.PointerUp -> pointerUp(open, intent)
            is Intent.Scroll -> {
                if (open.mode != Mode.SAVE) return DispatchResult(consumed = false)
                state = open.copy(scrollRow = (open.scrollRow + intent.rows).coerceAtLeast(0))
                DispatchResult(consumed = true)
            }
            Intent.Cancel -> close(open)
        }
    }

    private fun pointerDown(open: State.Open, intent: Intent.PointerDown): DispatchResult {
        val confirmation = confirmationAt(intent.x, intent.y)
        val press = confirmation?.let(Press::Confirmation) ?: slotAt(open, intent.x, intent.y)?.let(Press::Row) ?: Press.None
        state = open.copy(press = press)
        return DispatchResult(consumed = true)
    }

    private fun pointerUp(open: State.Open, intent: Intent.PointerUp): DispatchResult = when (open.mode) {
        Mode.SAVE -> savePointerUp(open, intent)
        Mode.LOAD -> loadPointerUp(open, intent)
    }

    private fun savePointerUp(open: State.Open, intent: Intent.PointerUp): DispatchResult {
        val confirmation = confirmationAt(intent.x, intent.y)
        val press = open.press
        if (press is Press.Confirmation && press.answer == confirmation) {
            val committed = if (saveLayer.completionTipOpen()) {
                saveLayer.onCompletionTip(SaveLayer.TOUCH_END)
                open.saveCommitted
            } else {
                open.saveCommitted || saveLayer.onConfirm(confirmation ?: 1)
            }
            return settleSave(open.copy(press = Press.None, saveCommitted = committed))
        }
        val slot = slotAt(open, intent.x, intent.y)
        if (press is Press.Row && press.index == slot) saveLayer.onRowTouch(press.index, SaveLayer.TOUCH_END)
        else if (press == Press.None && saveCloseAt(intent.x, intent.y)) return close(open)
        state = open.copy(press = Press.None)
        return DispatchResult(consumed = true)
    }

    private fun loadPointerUp(open: State.Open, intent: Intent.PointerUp): DispatchResult {
        val confirmation = confirmationAt(intent.x, intent.y)
        val press = open.press
        if (press is Press.Confirmation && press.answer == confirmation) {
            loadLayer.onConfirm(confirmation ?: 1)
        } else {
            val slot = slotAt(open, intent.x, intent.y)
            if (press is Press.Row && press.index == slot) loadLayer.onRowTouch(press.index, LoadGameLayer.TOUCH_END)
            else if (press == Press.None && loadCloseAt(intent.x, intent.y)) return close(open)
        }
        state = open.copy(press = Press.None)
        return DispatchResult(consumed = true)
    }

    private fun settleSave(open: State.Open): DispatchResult {
        if (saveLayer.view().attached) {
            state = open
            return DispatchResult(consumed = true)
        }
        state = State.Hidden
        return DispatchResult(consumed = true, effect = Effect.Closed(Mode.SAVE, saved = open.saveCommitted))
    }

    private fun close(open: State.Open): DispatchResult {
        when (open.mode) {
            Mode.SAVE -> saveLayer.onCancel(SaveLayer.TOUCH_END)
            Mode.LOAD -> loadLayer.onCancel(LoadGameLayer.TOUCH_END)
        }
        state = State.Hidden
        return DispatchResult(consumed = true, effect = Effect.Closed(open.mode, saved = false))
    }

    private fun saveView(open: State.Open): BattleSaveLoadOverlayView? {
        val layerView = saveLayer.view()
        if (!layerView.attached) return hide()
        val first = open.scrollRow.coerceIn(0, (layerView.rows.size - VISIBLE_ROWS).coerceAtLeast(0))
        state = open.copy(scrollRow = first)
        return BattleSaveLoadOverlayView(
            kind = BattleSaveLoadOverlayKind.SAVE,
            rows = layerView.rows.map { BattleSaveLoadRowView(it.number, it.stage, it.name) },
            firstRow = first,
            pendingSave = saveLayer.pendingSlot() != null,
            saveConfirmation = saveLayer.pendingPrompt(),
            saveCompletionTip = saveLayer.completionTipOpen(),
        )
    }

    private fun loadView(open: State.Open): BattleSaveLoadOverlayView? {
        val layerView = loadLayer.view()
        if (!layerView.attached) return hide()
        val first = open.scrollRow.coerceIn(0, (layerView.rows.size - VISIBLE_ROWS).coerceAtLeast(0))
        state = open.copy(scrollRow = first)
        return BattleSaveLoadOverlayView(
            kind = BattleSaveLoadOverlayKind.LOAD,
            rows = layerView.rows.map { BattleSaveLoadRowView(it.number, it.stage, it.name) },
            firstRow = first,
            loadConfirmation = layerView.confirmation?.message,
            loadNotice = layerView.notice,
        )
    }

    private fun hide(): Nothing? {
        state = State.Hidden
        return null
    }

    private fun slotAt(open: State.Open, x: Float, y: Float): Int? = when (open.mode) {
        Mode.SAVE -> if (x !in ROW_LEFT..ROW_RIGHT || y !in SAVE_ROW_BOTTOM..ROW_TOP || saveLayer.pendingSlot() != null || saveLayer.completionTipOpen()) null
        else saveLayer.view().rows.getOrNull(((SAVE_ROW_TOP - y) / ROW_HEIGHT).toInt() + open.scrollRow)?.index
        Mode.LOAD -> if (x !in ROW_LEFT..ROW_RIGHT || y !in LOAD_ROW_BOTTOM..ROW_TOP || loadLayer.pendingSlot() != null) null
        else loadLayer.view().rows.getOrNull(((LOAD_ROW_TOP - y) / ROW_HEIGHT).toInt() + open.scrollRow)?.index
    }

    private fun confirmationAt(x: Float, y: Float): Int? = when {
        x in CONFIRM_OK_LEFT..CONFIRM_OK_RIGHT && y in CONFIRM_BOTTOM..CONFIRM_TOP -> 0
        x in CONFIRM_CANCEL_LEFT..CONFIRM_CANCEL_RIGHT && y in CONFIRM_BOTTOM..CONFIRM_TOP -> 1
        else -> null
    }

    private fun saveCloseAt(x: Float, y: Float): Boolean =
        x !in SAVE_PANEL_LEFT..SAVE_PANEL_RIGHT || y !in SAVE_PANEL_BOTTOM..SAVE_PANEL_TOP ||
            (x in SAVE_CLOSE_LEFT..SAVE_CLOSE_RIGHT && y in SAVE_CLOSE_BOTTOM..SAVE_CLOSE_TOP)

    private fun loadCloseAt(x: Float, y: Float): Boolean =
        x !in LOAD_PANEL_LEFT..LOAD_PANEL_RIGHT || y !in LOAD_PANEL_BOTTOM..LOAD_PANEL_TOP ||
            (x in LOAD_CLOSE_LEFT..LOAD_CLOSE_RIGHT && y in LOAD_CLOSE_BOTTOM..LOAD_CLOSE_TOP)

    private companion object {
        const val VISIBLE_ROWS = 8
        const val ROW_LEFT = 289f
        const val ROW_RIGHT = 1197f
        const val ROW_TOP = 600f
        const val SAVE_ROW_TOP = 608f
        const val SAVE_ROW_BOTTOM = 182f
        const val LOAD_ROW_TOP = 600f
        const val LOAD_ROW_BOTTOM = 184f
        const val ROW_HEIGHT = 52f
        const val CONFIRM_OK_LEFT = 570f
        const val CONFIRM_OK_RIGHT = 720f
        const val CONFIRM_CANCEL_LEFT = 770f
        const val CONFIRM_CANCEL_RIGHT = 920f
        const val CONFIRM_BOTTOM = 305f
        const val CONFIRM_TOP = 353f
        const val SAVE_PANEL_LEFT = 278f
        const val SAVE_PANEL_RIGHT = 1210f
        const val SAVE_PANEL_BOTTOM = 100f
        const val SAVE_PANEL_TOP = 600f
        const val SAVE_CLOSE_LEFT = 1046f
        const val SAVE_CLOSE_RIGHT = 1194f
        const val SAVE_CLOSE_BOTTOM = 100f
        const val SAVE_CLOSE_TOP = 156f
        const val LOAD_PANEL_LEFT = 278f
        const val LOAD_PANEL_RIGHT = 1210f
        const val LOAD_PANEL_BOTTOM = 110f
        const val LOAD_PANEL_TOP = 600f
        const val LOAD_CLOSE_LEFT = 1051f
        const val LOAD_CLOSE_RIGHT = 1199f
        const val LOAD_CLOSE_BOTTOM = 110f
        const val LOAD_CLOSE_TOP = 170f
    }
}
