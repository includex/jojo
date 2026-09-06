// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.presentation.scenario.overlay.*

/** HallExclusiveView: 거점 전용 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallExclusiveView(
    val selectedTab: Tab,
) {
    /**
     * `Tab`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class Tab { SET_LIST, EXCLUSIVE_LIST }

    companion object {
        /**
         * `from`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun from(layer: ExclusiveLayer) = HallExclusiveView(
            selectedTab = when (layer.selectedTab) {
                ExclusiveLayer.Tab.SET_LIST -> Tab.SET_LIST
                ExclusiveLayer.Tab.EXCLUSIVE_LIST -> Tab.EXCLUSIVE_LIST
            },
        )
    }
}

/**
 * `HallFeatsView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallFeatsView(
    val rows: List<Row>,
    val helpOpen: Boolean,
    val helpText: String,
) {
    /**
     * `Row`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class Row(
        /**
         * `title` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val title: String,
        /**
         * `ability` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val ability: String,
        /**
         * `progressRatio` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val progressRatio: Float,
        /**
         * `progressLabel` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val progressLabel: String,
        /**
         * `phaseLabel` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val phaseLabel: String,
    )

    companion object {
        /**
         * `from`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun from(layer: FeatsLayer, helpOpen: Boolean): HallFeatsView = HallFeatsView(
            rows = layer.view().rows.map { row ->
                Row(
                    title = row.title,
                    ability = row.ability.toString(),
                    progressRatio = row.progressRatio,
                    progressLabel = row.progressLabel,
                    phaseLabel = row.phaseLabel,
                )
            },
            helpOpen = helpOpen,
            helpText = FeatsLayer.HELP_TEXT,
        )
    }
}
