package com.jojo.port

import com.badlogic.gdx.utils.JsonReader

/**
 * Injectable state port of `ui/SaveLayer.js`.
 *
 * The Cocos layer itself does not serialize a game: it lists Manager slots,
 * asks MsgBox for confirmation, then dispatches SAVE_GAME with the selected
 * zero-based slot index.  Keeping that protocol explicit prevents a menu tap
 * from silently saving to an unrelated "current" slot.
 */
class SaveLayer(private val repository: Repository, private val pageTogglesEnabled: Boolean = true) {
    interface Repository {
        /** Manager.loadGame(index), or null when no slot has been written. */
        fun load(index: Int): String?
        /** Battle/Hall's SAVE_GAME listener. */
        fun save(index: Int)
    }

    data class Row(
        val index: Int,
        val time: Long,
        val number: String,
        val stage: String,
        val name: String,
        val occupied: Boolean,
    )
    data class View(val page: Int, val rows: List<Row>, val pageTogglesVisible: Boolean, val attached: Boolean)

    private var page = -1
    private var tip = true
    private var callback: (() -> Unit)? = null
    private var attached = false
    private var pendingIndex: Int? = null
    /** The source passes this exact label text to MsgBox. */
    private var pendingPrompt: String? = null
    private var completionTipOpen = false
    private var view: View? = null
    private var storedPage = 0
    private val lifecycle = mutableListOf<String>()

    /** Source `onCreate({ func, tip })`; omitted data uses tip=true. */
    fun onCreate(onComplete: (() -> Unit)? = null, showCompleteTip: Boolean = true, savedPage: Int = 0): View {
        callback = onComplete
        tip = showCompleteTip
        attached = true
        lifecycle.clear()
        return refPage(savedPage)
    }

    /** `_refPage`: page zero has 22 slots, later pages have 20 and begin +2. */
    fun refPage(nextPage: Int): View {
        require(nextPage >= 0) { "SAVE_PAGE must be non-negative" }
        if (page == nextPage && view != null) return view()
        page = nextPage
        storedPage = nextPage
        val start = if (page == 0) 0 else page * 20 + 2
        val count = if (page == 0) 22 else 20
        val rows = (start until start + count).map { decodeRow(it, page) }.sortedBy(Row::time)
        return View(page, rows, pageTogglesVisible = pageTogglesEnabled, attached = attached).also { view = it }
    }

    /** Source toggle callback: page changes only on TOUCH_END and only when enabled. */
    fun onPageTouch(nextPage: Int, eventType: Int): View =
        if (pageTogglesEnabled && eventType == TOUCH_END) refPage(nextPage) else view()

    /** `item` TOUCH_START/END: only END opens the source MsgBox confirmation. */
    fun onRowTouch(index: Int, eventType: Int): Boolean {
        if (eventType != TOUCH_END || view()?.rows?.any { it.index == index } != true) return false
        pendingIndex = index
        pendingPrompt = "진행도 No.${index + 1}:${view().rows.first { it.index == index }.name}저장할 수 있나요?"
        lifecycle += "msgbox:confirm:${index + 1}"
        return true
    }

    /** MsgBox callback: zero confirms (`저장`); non-zero keeps SaveLayer open. */
    fun onConfirm(result: Int): Boolean {
        val index = pendingIndex ?: return false
        pendingIndex = null
        pendingPrompt = null
        if (result != 0) return false
        repository.save(index)
        lifecycle += "dispatch:SAVE_GAME:$index"
        // `_temp` does not remove yet when tip=true: MsgBox("저장 완료.")
        // must itself finish before func/removeLayer.
        if (tip) { completionTipOpen = true; lifecycle += "msgbox:complete" } else complete()
        return true
    }

    /** Post-save MsgBox (`저장 완료.`) completion callback. */
    fun onCompletionTip(eventType: Int): Boolean {
        if (eventType != TOUCH_END || !completionTipOpen) return false
        completionTipOpen = false
        complete()
        return true
    }

    /** bg1/button TOUCH_END: invokes func then removes, no save. */
    fun onCancel(eventType: Int): Boolean {
        if (eventType != TOUCH_END || !attached) return false
        callback?.invoke()
        lifecycle += "callback"
        lifecycle += "removeFromParent"
        attached = false
        view = view()?.copy(attached = false)
        return true
    }

    /** Whether a source `저장 완료.` MsgBox would be displayed before removal. */
    fun showsCompletionTip(): Boolean = tip
    fun completionTipOpen(): Boolean = completionTipOpen
    fun pendingSlot(): Int? = pendingIndex
    fun pendingPrompt(): String? = pendingPrompt
    fun storedPage(): Int = storedPage
    fun takeLifecycle(): List<String> = lifecycle.toList().also { lifecycle.clear() }
    fun view(): View = requireNotNull(view) { "SaveLayer.onCreate must run before access" }

    private fun complete() {
        callback?.invoke()
        lifecycle += "callback"
        lifecycle += "removeLayer:SaveLayer"
        attached = false
        view = view()?.copy(attached = false)
    }

    private fun decodeRow(index: Int, page: Int): Row {
        val text = repository.load(index)
        val root = text?.takeIf { it.startsWith('{') }?.let { runCatching { JsonReader().parse(it) }.getOrNull() }
        // SaveLayer labels the physical save slot (`c + 1`), even though
        // non-zero pages begin at `20 * page + 2`.
        val number = "No.${(index + 1).toString().padStart(3, ' ')}"
        if (root == null) return Row(index, 0, number, "---", "진행 상황 저장 안 함", false)
        val model = root.get("model")?.get("game") ?: root.get("model")
        // Config.MODEL_PROPERTY_INDEX.STAGE_N is exactly 1.
        val stage = model?.get("property2")?.get(1)?.asInt() ?: model?.getInt("stage", 0) ?: 0
        return Row(
            index = index,
            time = root.getLong("time", 0),
            number = number,
            stage = "전역${1 + (stage shr 1)}",
            name = root.getString("name", ""),
            occupied = root.has("name"),
        )
    }

    companion object { const val TOUCH_END = 2 }
}
