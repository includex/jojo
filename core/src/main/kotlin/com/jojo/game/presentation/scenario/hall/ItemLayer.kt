// Game
package com.jojo.game.presentation.scenario.hall

/** ItemLayer: 공용 아이템 화면의 동작 상태를 관리한다. */
class ItemLayer(
    val itemId: Int,
    /** `itemName` (String): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val itemName: String,
    val canDrop: Boolean,
    /** `repository` (Repository): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val repository: Repository,
) {

    /**
     * `Repository`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    interface Repository {

        /**
         * `discard`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun discard(itemId: Int): Boolean
    }

    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
        private set
    /**
     * `discardConfirmationOpen` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var discardConfirmationOpen = false
        private set
    /**
     * `toast` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var toast: String? = null
        private set


    /**
     * `onButton`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
    }
}
