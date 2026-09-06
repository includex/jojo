// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.presentation.shared.overlay.TerrainLayer

/** HallTerrainView: 거점 지형 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallTerrainView(
    val riseTab: Boolean,
    val rows: List<HallTerrainRowView>,
) {
    companion object {
        /**
         * `sourceNames` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private val sourceNames = listOf("평원", "초원", "숲", "황지", "산지", "암산")

        /**
         * `from`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun from(tab: TerrainLayer.Tab, cells: List<TerrainLayer.Cell>): HallTerrainView = HallTerrainView(
            riseTab = tab == TerrainLayer.Tab.RISE,
            rows = cells.take(6).mapIndexed { index, cell ->
                HallTerrainRowView(
                    name = sourceNames.getOrElse(index) { cell.terrainName.trim() },
                    iconIndex = cell.iconIndex,
                    enabledSkills = cell.enabledSkills.toList(),
                    values = cell.values.take(13).map(TerrainLayer.Value::text),
                )
            },
        )
    }
}

/**
 * `HallTerrainRowView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallTerrainRowView(
    val name: String,
    val iconIndex: Int,
    val enabledSkills: List<Boolean>,
    val values: List<String>,
)
