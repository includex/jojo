// Presentation
package com.jojo.game.presentation.shared.overlay

import com.jojo.game.domain.battle.*


import com.badlogic.gdx.utils.JsonReader

/** SaveLayer: 저장 슬롯을 페이지별로 표시하고 사용자의 확인 뒤 선택 슬롯에 현재 진행 상태를 기록한다. */

class SaveLayer(private val repository: Repository, private val pageTogglesEnabled: Boolean = true) {

    /**
     * `Repository`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    interface Repository {
        /** load: 지정 저장 슬롯의 원본 데이터를 읽어 비어 있으면 null을 반환한다. */
        fun load(index: Int): String?

        /** save: 지정 저장 슬롯에 현재 진행 상태를 기록한다. */
        fun save(index: Int)
    }


    /**
     * `Row`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Row(
        /**
         * `index` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val index: Int,
        /**
         * `time` (Long,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val time: Long,
        /**
         * `number` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val number: String,
        /**
         * `stage` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val stage: String,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String,
        /**
         * `occupied` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val occupied: Boolean,
    )


    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(val page: Int, val rows: List<Row>, val pageTogglesVisible: Boolean, val attached: Boolean)

    /**
     * `page` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var page = -1
    /**
     * `tip` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var tip = true
    /**
     * `callback` ((() -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var callback: (() -> Unit)? = null
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = false
    /**
     * `pendingIndex` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var pendingIndex: Int? = null

    /** pendingPrompt: 사용자가 선택한 슬롯을 저장할지 확인할 때 표시할 안내 문구다. */
    private var pendingPrompt: String? = null
    /**
     * `completionTipOpen` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var completionTipOpen = false
    /**
     * `view` (View?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var view: View? = null
    /**
     * `storedPage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var storedPage = 0
    /**
     * `lifecycle` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val lifecycle = mutableListOf<String>()

    /** onCreate: 저장 화면을 부착하고 완료 콜백·안내 표시 정책·마지막 페이지를 초기화한다. */
    fun onCreate(onComplete: (() -> Unit)? = null, showCompleteTip: Boolean = true, savedPage: Int = 0): View {
        callback = onComplete
        tip = showCompleteTip
        attached = true
        lifecycle.clear()
        return refPage(savedPage)
    }

    /** refPage: 지정 페이지의 슬롯 데이터를 해석해 시간순 저장 목록 화면 모델을 갱신한다. */
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

    /** onPageTouch: 페이지 버튼의 터치 종료를 확인해 허용된 경우 표시 페이지를 전환한다. */
    fun onPageTouch(nextPage: Int, eventType: Int): View =
        if (pageTogglesEnabled && eventType == TOUCH_END) refPage(nextPage) else view()

    /** onRowTouch: 저장할 슬롯을 선택하고 확인 모달에 표시할 슬롯 안내 문구를 준비한다. */
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
        // tip=true이면 `_temp`는 즉시 제거하지 않고, MsgBox("저장 완료.")가 끝난 뒤 func/removeLayer를 실행한다.
        if (tip) {
            completionTipOpen = true; lifecycle += "msgbox:complete"
        } else complete()
        return true
    }

    /** onCompletionTip: 저장 완료 안내창의 종료 입력을 받아 저장 화면 마무리를 이어간다. */
    fun onCompletionTip(eventType: Int): Boolean {
        if (eventType != TOUCH_END || !completionTipOpen) return false
        completionTipOpen = false
        complete()
        return true
    }

    /** onCancel: 취소 버튼 입력으로 저장 화면을 닫고 호출자에게 완료를 알린다. */
    fun onCancel(eventType: Int): Boolean {
        if (eventType != TOUCH_END || !attached) return false
        callback?.invoke()
        lifecycle += "callback"
        lifecycle += "removeFromParent"
        attached = false
        view = view()?.copy(attached = false)
        return true
    }

    /** showsCompletionTip: 원본처럼 제거 전에 `저장 완료.` 안내창을 표시하는지 반환한다. */
    fun showsCompletionTip(): Boolean = tip


    /**
     * `completionTipOpen`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun completionTipOpen(): Boolean = completionTipOpen


    /**
     * `pendingSlot`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun pendingSlot(): Int? = pendingIndex


    /**
     * `pendingPrompt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun pendingPrompt(): String? = pendingPrompt


    /**
     * `storedPage`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun storedPage(): Int = storedPage


    /**
     * `takeLifecycle`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun takeLifecycle(): List<String> = lifecycle.toList().also { lifecycle.clear() }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(): View = requireNotNull(view) { "SaveLayer.onCreate must run before access" }

    /**
     * `complete`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun complete() {
        callback?.invoke()
        lifecycle += "callback"
        lifecycle += "removeLayer:SaveLayer"
        attached = false
        view = view()?.copy(attached = false)
    }

    /**
     * `decodeRow`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun decodeRow(index: Int, page: Int): Row {
        val text = repository.load(index)
        val root = text?.takeIf { it.startsWith('{') }?.let { runCatching { JsonReader().parse(it) }.getOrNull() }
        // SaveLayer는 0이 아닌 페이지가 `20 * page + 2`에서 시작해도 실제 저장 슬롯 번호(`c + 1`)를 표시한다.
        val number = "No.${(index + 1).toString().padStart(3, ' ')}"
        if (root == null) return Row(index, 0, number, "---", "진행 상황 저장 안 함", false)
        val model = root.get("model")?.get("game") ?: root.get("model")
        // Config.MODEL_PROPERTY_INDEX.STAGE_N의 인덱스는 정확히 1이다.
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

    companion object {
        /** `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
        const val TOUCH_END = 2
    }
}
