// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.shared.overlay.LoadGameLayer
import com.jojo.game.presentation.shared.overlay.SaveLayer
/**
 * `BattleSaveLoadOverlayController`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class BattleSaveLoadOverlayController(
    saveRepository: SaveLayer.Repository,
    loadRepository: LoadGameLayer.Repository,
) {
    /** 현재 저장 목록을 조작하는지 불러오기 목록을 조작하는지 구분한다. */
    enum class Mode { SAVE, LOAD }

    /** 저장·불러오기 목록의 누름, 스크롤, 취소 동작을 전달하는 입력이다. */
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
         * `Scroll`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Scroll(val rows: Int) : Intent
        /**
         * `Cancel`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Cancel : Intent
    }

    /** 저장·불러오기 오버레이의 닫힘과 저장 성공 여부를 보고한다. */
    sealed interface Effect {
        /**
         * `None`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object None : Effect
        /**
         * `Closed`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Closed(val mode: Mode, val saved: Boolean) : Effect
    }
    /**
     * `DispatchResult`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class DispatchResult(val consumed: Boolean, val effect: Effect = Effect.None)

    /** 열린 목록의 종류, 스크롤 위치, 누른 행과 저장 완료 여부를 보관한다. */
    private sealed interface State {
        /**
         * `Hidden`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Hidden : State
        /**
         * `Open`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Open(
            /**
             * `mode` (Mode,): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val mode: Mode,
            /**
             * `scrollRow` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val scrollRow: Int = 0,
            /**
             * `press` (Press): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val press: Press = Press.None,
            /**
             * `saveCommitted` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val saveCommitted: Boolean = false,
        ) : State
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
         * `Row`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Row(val index: Int) : Press
        /**
         * `Confirmation`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Confirmation(val answer: Int) : Press
    }

    /**
     * `saveLayer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val saveLayer = SaveLayer(saveRepository)
    /**
     * `loadLayer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val loadLayer = LoadGameLayer(loadRepository)
    /**
     * `state` (State): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var state: State = State.Hidden

    /**
     * `openSave`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openSave(savedPage: Int = 0) {
        saveLayer.onCreate(savedPage = savedPage)
        state = State.Open(Mode.SAVE)
    }

    /**
     * `openLoad`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openLoad() {
        loadLayer.onCreate()
        state = State.Open(Mode.LOAD)
    }

    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(mode: Mode): BattleSaveLoadOverlayView? {
        val open = state as? State.Open ?: return null
        if (open.mode != mode) return null
        return when (mode) {
            Mode.SAVE -> saveView(open)
            Mode.LOAD -> loadView(open)
        }
    }

    /**
     * `dispatch`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `pointerDown`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun pointerDown(open: State.Open, intent: Intent.PointerDown): DispatchResult {
        val confirmation = confirmationAt(intent.x, intent.y)
        val press = confirmation?.let(Press::Confirmation) ?: slotAt(open, intent.x, intent.y)?.let(Press::Row) ?: Press.None
        state = open.copy(press = press)
        return DispatchResult(consumed = true)
    }

    /**
     * `pointerUp`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun pointerUp(open: State.Open, intent: Intent.PointerUp): DispatchResult = when (open.mode) {
        Mode.SAVE -> savePointerUp(open, intent)
        Mode.LOAD -> loadPointerUp(open, intent)
    }

    /**
     * `savePointerUp`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `loadPointerUp`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `settleSave`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun settleSave(open: State.Open): DispatchResult {
        if (saveLayer.view().attached) {
            state = open
            return DispatchResult(consumed = true)
        }
        state = State.Hidden
        return DispatchResult(consumed = true, effect = Effect.Closed(Mode.SAVE, saved = open.saveCommitted))
    }

    /**
     * `close`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun close(open: State.Open): DispatchResult {
        when (open.mode) {
            Mode.SAVE -> saveLayer.onCancel(SaveLayer.TOUCH_END)
            Mode.LOAD -> loadLayer.onCancel(LoadGameLayer.TOUCH_END)
        }
        state = State.Hidden
        return DispatchResult(consumed = true, effect = Effect.Closed(open.mode, saved = false))
    }

    /**
     * `saveView`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `loadView`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `hide`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun hide(): Nothing? {
        state = State.Hidden
        return null
    }

    /**
     * `slotAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun slotAt(open: State.Open, x: Float, y: Float): Int? = when (open.mode) {
        Mode.SAVE -> if (x !in ROW_LEFT..ROW_RIGHT || y !in SAVE_ROW_BOTTOM..ROW_TOP || saveLayer.pendingSlot() != null || saveLayer.completionTipOpen()) null
        else saveLayer.view().rows.getOrNull(((SAVE_ROW_TOP - y) / ROW_HEIGHT).toInt() + open.scrollRow)?.index
        Mode.LOAD -> if (x !in ROW_LEFT..ROW_RIGHT || y !in LOAD_ROW_BOTTOM..ROW_TOP || loadLayer.pendingSlot() != null) null
        else loadLayer.view().rows.getOrNull(((LOAD_ROW_TOP - y) / ROW_HEIGHT).toInt() + open.scrollRow)?.index
    }

    /**
     * `confirmationAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun confirmationAt(x: Float, y: Float): Int? = when {
        x in CONFIRM_OK_LEFT..CONFIRM_OK_RIGHT && y in CONFIRM_BOTTOM..CONFIRM_TOP -> 0
        x in CONFIRM_CANCEL_LEFT..CONFIRM_CANCEL_RIGHT && y in CONFIRM_BOTTOM..CONFIRM_TOP -> 1
        else -> null
    }

    /**
     * `saveCloseAt`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun saveCloseAt(x: Float, y: Float): Boolean =
        x !in SAVE_PANEL_LEFT..SAVE_PANEL_RIGHT || y !in SAVE_PANEL_BOTTOM..SAVE_PANEL_TOP ||
            (x in SAVE_CLOSE_LEFT..SAVE_CLOSE_RIGHT && y in SAVE_CLOSE_BOTTOM..SAVE_CLOSE_TOP)

    /**
     * `loadCloseAt`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun loadCloseAt(x: Float, y: Float): Boolean =
        x !in LOAD_PANEL_LEFT..LOAD_PANEL_RIGHT || y !in LOAD_PANEL_BOTTOM..LOAD_PANEL_TOP ||
            (x in LOAD_CLOSE_LEFT..LOAD_CLOSE_RIGHT && y in LOAD_CLOSE_BOTTOM..LOAD_CLOSE_TOP)

    private companion object {
        /**
         * `VISIBLE_ROWS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val VISIBLE_ROWS = 8
        /**
         * `ROW_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROW_LEFT = 289f
        /**
         * `ROW_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROW_RIGHT = 1197f
        /**
         * `ROW_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROW_TOP = 600f
        /**
         * `SAVE_ROW_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_ROW_TOP = 608f
        /**
         * `SAVE_ROW_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_ROW_BOTTOM = 182f
        /**
         * `LOAD_ROW_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_ROW_TOP = 600f
        /**
         * `LOAD_ROW_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_ROW_BOTTOM = 184f
        /**
         * `ROW_HEIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROW_HEIGHT = 52f
        /**
         * `CONFIRM_OK_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_OK_LEFT = 570f
        /**
         * `CONFIRM_OK_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_OK_RIGHT = 720f
        /**
         * `CONFIRM_CANCEL_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_CANCEL_LEFT = 770f
        /**
         * `CONFIRM_CANCEL_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_CANCEL_RIGHT = 920f
        /**
         * `CONFIRM_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_BOTTOM = 305f
        /**
         * `CONFIRM_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CONFIRM_TOP = 353f
        /**
         * `SAVE_PANEL_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_PANEL_LEFT = 278f
        /**
         * `SAVE_PANEL_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_PANEL_RIGHT = 1210f
        /**
         * `SAVE_PANEL_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_PANEL_BOTTOM = 100f
        /**
         * `SAVE_PANEL_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_PANEL_TOP = 600f
        /**
         * `SAVE_CLOSE_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_CLOSE_LEFT = 1046f
        /**
         * `SAVE_CLOSE_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_CLOSE_RIGHT = 1194f
        /**
         * `SAVE_CLOSE_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_CLOSE_BOTTOM = 100f
        /**
         * `SAVE_CLOSE_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SAVE_CLOSE_TOP = 156f
        /**
         * `LOAD_PANEL_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_PANEL_LEFT = 278f
        /**
         * `LOAD_PANEL_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_PANEL_RIGHT = 1210f
        /**
         * `LOAD_PANEL_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_PANEL_BOTTOM = 110f
        /**
         * `LOAD_PANEL_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_PANEL_TOP = 600f
        /**
         * `LOAD_CLOSE_LEFT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_CLOSE_LEFT = 1051f
        /**
         * `LOAD_CLOSE_RIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_CLOSE_RIGHT = 1199f
        /**
         * `LOAD_CLOSE_BOTTOM` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_CLOSE_BOTTOM = 110f
        /**
         * `LOAD_CLOSE_TOP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LOAD_CLOSE_TOP = 170f
    }
}
