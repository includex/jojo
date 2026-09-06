// Scenario
package com.jojo.game.presentation.scenario.input

/** ScenarioInputRouter: 시나리오 입력 Router이며, 사용자 입력과 런타임 상태를 해석해 화면 전환과 오버레이 처리를 조정한다. */
object ScenarioInputRouter {
    /**
     * `HallLayer`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class HallLayer { FEATS, UNIT_INFO, MAGIC, ITEM, SAVE, INFO, EXCLUSIVE, MANAGEMENT, MAIN }
    /**
     * `Touch`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    sealed interface Touch {
        /**
         * `Hall`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Hall(val layer: HallLayer, val closesManagement: Boolean = false) : Touch
        /**
         * `SelectAndConfirm`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class SelectAndConfirm(val index: Int) : Touch
        /**
         * `Advance`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Advance : Touch
        /**
         * `None`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object None : Touch
    }

    /**
     * `HallState`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class HallState(
        /**
         * `completeMenu` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val completeMenu: Boolean,
        /**
         * `feats` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val feats: Boolean,
        /**
         * `unitInfo` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unitInfo: Boolean,
        /**
         * `magic` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magic: Boolean,
        /**
         * `item` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val item: Boolean,
        /**
         * `save` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val save: Boolean,
        /**
         * `info` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val info: Boolean,
        /**
         * `exclusive` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val exclusive: Boolean,
        /**
         * `management` (Management?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val management: Management?,
        /**
         * `unitListOpen` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unitListOpen: Boolean,
    )

    /**
     * `Management`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class Management { EQUIP, BUY, SELL }

    /**
     * `hallTouch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hallTouch(state: HallState, x: Float, y: Float): Touch {
        if (!state.completeMenu) return Touch.None
        when {
            state.feats -> return Touch.Hall(HallLayer.FEATS)
            state.unitInfo -> return Touch.Hall(HallLayer.UNIT_INFO)
            state.magic -> return Touch.Hall(HallLayer.MAGIC)
            state.item -> return Touch.Hall(HallLayer.ITEM)
            state.save -> return Touch.Hall(HallLayer.SAVE)
            state.info -> return Touch.Hall(HallLayer.INFO)
            state.exclusive -> return Touch.Hall(HallLayer.EXCLUSIVE)
        }
        val management = state.management ?: return Touch.Hall(HallLayer.MAIN)
        if (management == Management.EQUIP && state.unitListOpen) return Touch.Hall(HallLayer.MANAGEMENT)
        val closes = when (management) {
            Management.EQUIP -> x in 642f..730f && y in 35f..84f
            Management.BUY -> x in 529f..653f && y in 35f..86f
            Management.SELL -> x in 869f..1000f && y in 75f..130f
        }
        return Touch.Hall(HallLayer.MANAGEMENT, closes)
    }

    /**
     * `choiceTouch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun choiceTouch(ask: Boolean, optionCount: Int, x: Float, y: Float): Touch {
        if (x !in 463f..1059f) return Touch.Advance
        if (ask) {
            val index = when {
                x in 482.84f..628.18f && y in 306.16f..349.16f -> 0
                x in 646.27f..791.61f && y in 306.16f..349.16f -> 1
                else -> -1
            }
            return if (index >= 0) Touch.SelectAndConfirm(index) else Touch.None
        }
        val row = ((401f - y) / 44f).toInt()
        return if (row in 0 until minOf(optionCount, 3)) Touch.SelectAndConfirm(row) else Touch.None
    }
}
