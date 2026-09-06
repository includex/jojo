package com.jojo.game

/**
 * Interaction state of the source `UsePropertyLayer`.
 *
 * The source deliberately distinguishes a short press from a one-second
 * press: releasing while the timer is pending selects the item, while the
 * timer completing opens ItemLayer and leaves this list attached.
 */

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
