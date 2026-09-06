// Game
package com.jojo.game.presentation.battle.overlay

/** UsePropertyLayer: 원본 UsePropertyLayer의 상호작용 상태이다. 짧은 누름은 항목 선택, 1초 누름은 ItemLayer 열기로 구분하며 목록은 열린 상태를 유지한다. */

class UsePropertyLayer(
    properties: List<Property>,
    private val onSelect: (Property?) -> Unit,
    private val onInspect: (Property) -> Unit,
) {

    data class Property(
        val id: Int,
        val name: String,
        val typeName: String,
        val count: Int,
        val icon: Int,
    )

    /** 아이템 저장소의 순서를 화면 표시 순서로 사용한다. */
    val rows: List<Property> = properties.toList()

    var attached: Boolean = true
        private set

    var previewSeconds: Float = 0f
        private set

    private var pendingIndex: Int? = null


    fun touchStart(index: Int) {
        if (!attached || index !in rows.indices) return
        cancelPendingPreview()
        pendingIndex = index
    }


    fun update(delta: Float) {
        if (!attached || pendingIndex == null) return
        previewSeconds += delta.coerceAtLeast(0f)
        if (previewSeconds < LONG_PRESS_SECONDS) return
        val property = rows[pendingIndex ?: return]
        cancelPendingPreview()
        onInspect(property)
    }


    fun touchEnd(index: Int) {
        if (!attached || pendingIndex != index || index !in rows.indices) return
        val property = rows[index]
        cancelPendingPreview()
        attached = false
        onSelect(property)
    }


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

    private fun cancelPendingPreview() {
        pendingIndex = null
        previewSeconds = 0f
    }

    companion object {
        const val LONG_PRESS_SECONDS = 1f
    }
}
