// Game
package com.jojo.game.presentation.battle.overlay

/** UsePropertyLayer: 원본 UsePropertyLayer의 상호작용 상태이다. 짧은 누름은 항목 선택, 1초 누름은 ItemLayer 열기로 구분하며 목록은 열린 상태를 유지한다. */

class UsePropertyLayer(
    properties: List<Property>,
    /** `onSelect` ((Property?) -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val onSelect: (Property?) -> Unit,
    /** `onInspect` ((Property) -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val onInspect: (Property) -> Unit,
) {

    /**
     * `Property`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Property(
        /**
         * `id` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String,
        /**
         * `typeName` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val typeName: String,
        /**
         * `count` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val count: Int,
        /**
         * `icon` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val icon: Int,
    )

    /** 아이템 저장소의 순서를 화면 표시 순서로 사용한다. */
    val rows: List<Property> = properties.toList()

    /**
     * `attached` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached: Boolean = true
        private set

    /**
     * `previewSeconds` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var previewSeconds: Float = 0f
        private set

    /**
     * `pendingIndex` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var pendingIndex: Int? = null


    /**
     * `touchStart`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touchStart(index: Int) {
        if (!attached || index !in rows.indices) return
        cancelPendingPreview()
        pendingIndex = index
    }


    /**
     * `update`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun update(delta: Float) {
        if (!attached || pendingIndex == null) return
        previewSeconds += delta.coerceAtLeast(0f)
        if (previewSeconds < LONG_PRESS_SECONDS) return
        val property = rows[pendingIndex ?: return]
        cancelPendingPreview()
        onInspect(property)
    }


    /**
     * `touchEnd`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touchEnd(index: Int) {
        if (!attached || pendingIndex != index || index !in rows.indices) return
        val property = rows[index]
        cancelPendingPreview()
        attached = false
        onSelect(property)
    }


    /**
     * `touchCancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touchCancel() {
        if (!attached) return
        cancelPendingPreview()
    }

    /** 배경 버튼과 전체 화면 취소 영역의 공통 입력 규칙이다. */
    fun closeTouchEnd() {
        if (!attached) return
        cancelPendingPreview()
        attached = false
        onSelect(null)
    }

    /**
     * `cancelPendingPreview`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun cancelPendingPreview() {
        pendingIndex = null
        previewSeconds = 0f
    }

    companion object {
        /**
         * `LONG_PRESS_SECONDS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val LONG_PRESS_SECONDS = 1f
    }
}
