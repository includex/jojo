package com.jojo.port

import com.badlogic.gdx.utils.JsonReader

/**
 * Source-faithful state port of `ui/LoadGameLayer.js`.
 *
 * `Manager.loadGame(index, 2)` returns the raw save after the confirmation;
 * this layer deliberately does not treat a row tap as a load.  Its host owns
 * the actual CampaignStore transition, while this module owns source order,
 * slot paging and validation.
 */
class LoadGameLayer(private val repository: Repository) {
    /** Exact `_loadGame` scene branch: battle=2 is the post-battle Hall path. */
    enum class RestoreRoute { HALL, BATTLE, HALL_AFTER_BATTLE }
    interface Repository {
        fun load(index: Int): String?
        fun savedPage(): Int
        fun savePage(page: Int)
        fun featureEnabled(name: String): Boolean
        fun versionCode(): Int
        /** Corresponds to Manager.resetGame + Model.loadGame + replaceScene. */
        fun restore(index: Int, raw: String, route: RestoreRoute): Boolean
    }

    data class Row(
        val index: Int,
        val time: Long,
        val number: String,
        val stage: String,
        val name: String,
        val occupied: Boolean,
    )
    data class Confirmation(val index: Int, val message: String)
    data class View(
        val page: Int,
        val rows: List<Row>,
        val pageTogglesVisible: Boolean,
        val attached: Boolean,
        val confirmation: Confirmation? = null,
        val notice: String? = null,
    )

    private var page = -1
    private var attached = false
    private var pendingIndex: Int? = null
    private var view: View? = null

    /** Source onCreate: background, pooled rows, SAVE_PAGE, then _refPage. */
    fun onCreate(): View {
        attached = true
        return refPage(repository.savedPage().coerceAtLeast(0))
    }

    /** `_refPage`: page 0 contains slots 0..21; later pages begin at 20p+2. */
    fun refPage(nextPage: Int): View {
        require(nextPage >= 0) { "SAVE_PAGE must be non-negative" }
        if (page == nextPage && view != null) return view()
        page = nextPage
        repository.savePage(page)
        val start = if (page == 0) 0 else page * 20 + 2
        val count = if (page == 0) 22 else 20
        val rows = (start until start + count).map(::decodeRow).sortedByDescending(Row::time)
        return View(page, rows, repository.featureEnabled("ZDBHSW"), attached).also { view = it }
    }

    /** Source toggle listener: feature-gated and only TOUCH_END changes page. */
    fun onPageTouch(nextPage: Int, eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END || !repository.featureEnabled("ZDBHSW") || nextPage !in 0..4) return false
        refPage(nextPage)
        return true
    }

    /** Item listener: only Cocos TOUCH_END opens MsgBox. */
    fun onRowTouch(index: Int, eventType: Int): Boolean {
        if (eventType != TOUCH_END || !attached || view()?.rows?.any { it.index == index } != true) return false
        pendingIndex = index
        val row = view().rows.first { it.index == index }
        view = view().copy(confirmation = Confirmation(index, "진행도 No.${index + 1}:${row.name}불러올 수 있나요?"), notice = null)
        return true
    }

    /** MsgBox callback: result 0 is the source's '불러오기' action. */
    fun onConfirm(result: Int): Boolean {
        val index = pendingIndex ?: return false
        val raw = repository.load(index)
        pendingIndex = null
        if (result != 0) {
            view = view().copy(confirmation = null)
            return false
        }
        if (raw.isNullOrEmpty() || !raw.startsWith('{')) return fail("저장 파일이 손실되었습니다!")
        val root = runCatching { JsonReader().parse(raw) }.getOrNull() ?: return fail("저장 파일이 손실되었습니다!")
        val version = root.get("model")?.getInt("version", 0) ?: 0
        if (version > repository.versionCode()) return fail("저장이 호환되지 않아 불러오기에 실패했습니다!")
        val route = when (root.getInt("battle", 0)) {
            2 -> RestoreRoute.HALL_AFTER_BATTLE
            0 -> RestoreRoute.HALL
            else -> RestoreRoute.BATTLE
        }
        val restored = repository.restore(index, raw, route)
        // `_loadGame` replaces the scene but does not call removeFromParent.
        // Keep this layer's local attachment state unchanged for source parity.
        view = view().copy(confirmation = null, attached = attached, notice = if (restored) null else "저장 파일이 손실되었습니다!")
        return restored
    }

    /** bg1/button0 has only a TOUCH_END close handler in the original. */
    fun onCancel(eventType: Int): Boolean {
        if (eventType != TOUCH_END || !attached) return false
        attached = false
        view = view().copy(attached = false, confirmation = null)
        return true
    }

    fun view(): View = requireNotNull(view) { "LoadGameLayer.onCreate must run before access" }
    fun pendingSlot(): Int? = pendingIndex

    private fun fail(notice: String): Boolean {
        view = view().copy(confirmation = null, notice = notice)
        return false
    }

    private fun decodeRow(index: Int): Row {
        val root = repository.load(index)?.takeIf { it.startsWith('{') }?.let { runCatching { JsonReader().parse(it) }.getOrNull() }
        if (root == null) return Row(index, 0, number(index), "---", "---", false)
        val model = root.get("model")?.get("game") ?: root.get("model")
        val stage = model?.get("property2")?.get(1)?.asInt() ?: model?.getInt("stage", 0) ?: 0
        return Row(index, root.getLong("time", 0), number(index), "전역${1 + (stage shr 1)}", root.getString("name", ""), true)
    }

    private fun number(index: Int) = "No.${(index + 1).toString().padStart(3, ' ')}"
    companion object { const val TOUCH_END = 2 }
}
