package com.jojo.game

/**
 * Interaction state of the source `UsePropertyLayer`.
 *
 * The source deliberately distinguishes a short press from a one-second
 * press: releasing while the timer is pending selects the item, while the
 * timer completing opens ItemLayer and leaves this list attached.
 */
/**
 * class  `UsePropertyLayer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class UsePropertyLayer(
    properties: List<Property>,
    private val onSelect: (Property?) -> Unit,
    private val onInspect: (Property) -> Unit,
) {
    /**
     * data class  `Property`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Property(
        val id: Int,
        val name: String,
        val typeName: String,
        val count: Int,
        val icon: Int,
    )

    /** ItemStore.allProperty() order is presentation order in the source. */
    val rows: List<Property> = properties.toList()

    var attached: Boolean = true
        private set

    var previewSeconds: Float = 0f
        private set

    private var pendingIndex: Int? = null

    /**
     * 공개 메서드 `touchStart`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touchStart(index: Int) {
        if (!attached || index !in rows.indices) return
        cancelPendingPreview()
        pendingIndex = index
    }

    /**
     * 공개 메서드 `update`
     *
     * ### 파라미터
    - `delta` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `touchEnd`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touchEnd(index: Int) {
        if (!attached || pendingIndex != index || index !in rows.indices) return
        val property = rows[index]
        cancelPendingPreview()
        attached = false
        onSelect(property)
    }

    /**
     * 공개 메서드 `touchCancel`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touchCancel() {
        if (!attached) return
        cancelPendingPreview()
    }

    /** Both `bg/button` and full-screen `Panel_cancel` have this contract. */
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
