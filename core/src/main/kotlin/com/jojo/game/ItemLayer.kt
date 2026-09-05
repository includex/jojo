package com.jojo.game

/** Behavioural state for the shared Global/scene/ItemLayer. */
class ItemLayer(
    val itemId: Int,
    private val itemName: String,
    val canDrop: Boolean,
    private val repository: Repository,
) {
    interface Repository {
        fun discard(itemId: Int): Boolean
    }

    var attached = true
        private set
    var discardConfirmationOpen = false
        private set
    var toast: String? = null
        private set

    fun onButton(index: Int, eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END) return false
        return when (index) {
            0 -> { attached = false; true }
            1 -> if (canDrop) { discardConfirmationOpen = true; true } else false
            else -> false
        }
    }

    /** MsgBox result zero is the source `예` action. */
    fun onDiscardAnswer(result: Int): Boolean {
        if (!discardConfirmationOpen) return false
        discardConfirmationOpen = false
        if (result != 0) return false
        if (!repository.discard(itemId)) return false
        toast = "$itemName 이미 버렸습니다..."
        attached = false
        return true
    }

    companion object { const val TOUCH_END = 2 }
}
