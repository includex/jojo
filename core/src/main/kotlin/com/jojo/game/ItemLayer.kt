package com.jojo.game

/** 공용 아이템 화면의 동작 상태를 관리한다. */
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
            0 -> {
                attached = false; true
            }

            1 -> if (canDrop) {
                discardConfirmationOpen = true; true
            } else false

            else -> false
        }
    }

    /** 확인 대화상자의 0번 결과를 수락으로 처리한다. */
    fun onDiscardAnswer(result: Int): Boolean {
        if (!discardConfirmationOpen) return false
        discardConfirmationOpen = false
        if (result != 0) return false
        if (!repository.discard(itemId)) return false
        toast = "$itemName 이미 버렸습니다..."
        attached = false
        return true
    }

    companion object {
        const val TOUCH_END = 2
    }
}
