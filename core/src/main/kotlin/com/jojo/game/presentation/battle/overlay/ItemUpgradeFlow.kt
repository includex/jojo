// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.campaign.*

/** ItemUpgradeFlow: Global113 ItemUpgradeLayer의 런타임 상태를 관리한다. 3초 자동 종료와 취소 버튼 종료가 같은 완료 경로를 사용하며, 먼저 레이어를 제거해 중복 완료를 막는다. */

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


    fun update(delta: Float) {
        if (!attached) return
        elapsed += delta.coerceAtLeast(0f)
        if (elapsed >= AUTO_CLOSE_SECONDS) complete()
    }


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
