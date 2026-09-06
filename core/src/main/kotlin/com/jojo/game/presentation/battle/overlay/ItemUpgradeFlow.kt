// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.campaign.*

/** ItemUpgradeFlow: Global113 ItemUpgradeLayer의 런타임 상태를 관리한다. 3초 자동 종료와 취소 버튼 종료가 같은 완료 경로를 사용하며, 먼저 레이어를 제거해 중복 완료를 막는다. */

class ItemUpgradeFlow(
    val request: CampaignEquipmentExperienceResult,
    val ownerName: String,
    val itemName: String,
    val attributeName: String,
    /** `onComplete` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val onComplete: () -> Unit,
) {
    /**
     * `elapsed` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var elapsed: Float = 0f
        private set
    /**
     * `attached` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached: Boolean = true
        private set
    /**
     * `completionCount` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var completionCount: Int = 0
        private set


    /**
     * `update`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun update(delta: Float) {
        if (!attached) return
        elapsed += delta.coerceAtLeast(0f)
        if (elapsed >= AUTO_CLOSE_SECONDS) complete()
    }


    /**
     * `panelCancelTouchEnd`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun panelCancelTouchEnd() = complete()

    /**
     * `complete`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun complete() {
        if (!attached) return
        attached = false
        completionCount++
        onComplete()
    }

    companion object {
        /**
         * `AUTO_CLOSE_SECONDS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val AUTO_CLOSE_SECONDS = 3f
    }
}
