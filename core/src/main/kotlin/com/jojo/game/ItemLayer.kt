package com.jojo.game

/** Behavioural state for the shared Global/scene/ItemLayer. */
class ItemLayer(
    val itemId: Int,
    private val itemName: String,
    val canDrop: Boolean,
    private val repository: Repository,
) {
    /**
     * interface  `Repository`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface Repository {
        /**
         * 공개 메서드 `discard`
         *
         * ### 파라미터
        - `itemId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Boolean`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun discard(itemId: Int): Boolean
    }

    var attached = true
        private set
    var discardConfirmationOpen = false
        private set
    var toast: String? = null
        private set

    /**
     * 공개 메서드 `onButton`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `eventType` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
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

    companion object {
        const val TOUCH_END = 2
    }
}
