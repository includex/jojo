package com.jojo.game
import com.jojo.game.domain.campaign.*

/**
 * Runtime state of Global113 ItemUpgradeLayer. The source schedules the same
 * callback at three seconds and also invokes it from Panel_cancel TOUCH_END.
 * Completion is idempotent because either path removes the layer first.
 */
/**
 * class  `ItemUpgradeFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ItemUpgradeFlow(
    val request: CampaignEquipmentExperienceResult,
    val ownerName: String,
    val itemName: String,
    val attributeName: String,
    private val onComplete: () -> Unit,
) {
    var elapsed: Float = 0f
        private set
    var attached: Boolean = true
        private set
    var completionCount: Int = 0
        private set

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
        if (!attached) return
        elapsed += delta.coerceAtLeast(0f)
        if (elapsed >= AUTO_CLOSE_SECONDS) complete()
    }

    /**
     * 공개 메서드 `panelCancelTouchEnd`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun panelCancelTouchEnd() = complete()

    private fun complete() {
        if (!attached) return
        attached = false
        completionCount++
        onComplete()
    }

    companion object {
        const val AUTO_CLOSE_SECONDS = 3f
    }
}
