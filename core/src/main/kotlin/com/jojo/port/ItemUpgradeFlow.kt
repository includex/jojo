package com.jojo.port

/**
 * Runtime state of Global113 ItemUpgradeLayer. The source schedules the same
 * callback at three seconds and also invokes it from Panel_cancel TOUCH_END.
 * Completion is idempotent because either path removes the layer first.
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
