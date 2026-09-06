// Presentation
package com.jojo.game.presentation.shared.overlay

import com.badlogic.gdx.utils.JsonReader

/** LoadGameLayer: 저장 슬롯의 호환성·전투 복원 경로를 검사한 뒤 캠페인 불러오기를 요청하는 화면 상태다. */

class LoadGameLayer(private val repository: Repository) {
    /** RestoreRoute: 저장 시점이 홀·전투·전투 직후 중 어디였는지에 따라 복원할 화면 경로를 구분한다. */
    enum class RestoreRoute { HALL, BATTLE, HALL_AFTER_BATTLE }


    /**
     * `Repository`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    interface Repository {

        /**
         * `load`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun load(index: Int): String?


        /**
         * `savedPage`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun savedPage(): Int


        /**
         * `savePage`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun savePage(page: Int)


        /**
         * `featureEnabled`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun featureEnabled(name: String): Boolean


        /**
         * `versionCode`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun versionCode(): Int

        /** restore: 검증한 저장 원문을 지정 복원 경로로 적용하고 성공 여부를 반환한다. */
        fun restore(index: Int, raw: String, route: RestoreRoute): Boolean
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
     * `Confirmation`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Confirmation(val index: Int, val message: String)


    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(
        /**
         * `page` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val page: Int,
        /**
         * `rows` (List<Row>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val rows: List<Row>,
        /**
         * `pageTogglesVisible` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val pageTogglesVisible: Boolean,
        /**
         * `attached` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attached: Boolean,
        /**
         * `confirmation` (Confirmation?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val confirmation: Confirmation? = null,
        /**
         * `notice` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val notice: String? = null,
    )

    /**
     * `page` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var page = -1
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
    /**
     * `view` (View?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var view: View? = null

    /** onCreate: 마지막으로 열었던 저장 페이지를 읽어 불러오기 화면을 초기화한다. */
    fun onCreate(): View {
        attached = true
        return refPage(repository.savedPage().coerceAtLeast(0))
    }

    /** refPage: 저장 페이지를 바꾸고 슬롯 데이터를 최근 저장 순서로 재구성한다. */
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

    /** onPageTouch: 기능 해금 여부와 터치 종료를 확인해 저장 페이지 전환을 처리한다. */
    fun onPageTouch(nextPage: Int, eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END || !repository.featureEnabled("ZDBHSW") || nextPage !in 0..4) return false
        refPage(nextPage)
        return true
    }

    /** onRowTouch: 선택한 슬롯을 보관하고 불러오기 확인 문구가 포함된 화면 모델을 갱신한다. */
    fun onRowTouch(index: Int, eventType: Int): Boolean {
        if (eventType != TOUCH_END || !attached || view()?.rows?.any { it.index == index } != true) return false
        pendingIndex = index
        val row = view().rows.first { it.index == index }
        view =
            view().copy(confirmation = Confirmation(index, "진행도 No.${index + 1}:${row.name}불러올 수 있나요?"), notice = null)
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
        // `_loadGame`은 장면만 교체하고 removeFromParent를 호출하지 않으므로, 원본과 같게 이 레이어의 부착 상태를 유지한다.
        view = view().copy(confirmation = null, attached = attached, notice = if (restored) null else "저장 파일이 손실되었습니다!")
        return restored
    }

    /** onCancel: 취소 입력으로 화면을 분리하고 보류된 확인 정보를 제거한다. */
    fun onCancel(eventType: Int): Boolean {
        if (eventType != TOUCH_END || !attached) return false
        attached = false
        view = view().copy(attached = false, confirmation = null)
        return true
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(): View = requireNotNull(view) { "LoadGameLayer.onCreate must run before access" }


    /**
     * `pendingSlot`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun pendingSlot(): Int? = pendingIndex

    /**
     * `fail`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun fail(notice: String): Boolean {
        view = view().copy(confirmation = null, notice = notice)
        return false
    }

    /**
     * `decodeRow`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun decodeRow(index: Int): Row {
        val root = repository.load(index)?.takeIf { it.startsWith('{') }
            ?.let { runCatching { JsonReader().parse(it) }.getOrNull() }
        if (root == null) return Row(index, 0, number(index), "---", "---", false)
        val model = root.get("model")?.get("game") ?: root.get("model")
        val stage = model?.get("property2")?.get(1)?.asInt() ?: model?.getInt("stage", 0) ?: 0
        return Row(
            index,
            root.getLong("time", 0),
            number(index),
            "전역${1 + (stage shr 1)}",
            root.getString("name", ""),
            true
        )
    }

    /**
     * `number`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun number(index: Int) = "No.${(index + 1).toString().padStart(3, ' ')}"

    companion object {
        /** `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
        const val TOUCH_END = 2
    }
}
