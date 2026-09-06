// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.presentation.shared.overlay.TerrainLayer

/** HallTerrainView: 거점 지형 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallTerrainView(
    val riseTab: Boolean,
    val rows: List<HallTerrainRowView>,
) {
    companion object {
        private val sourceNames = listOf("평원", "초원", "숲", "황지", "산지", "암산")

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

internal data class HallTerrainRowView(
    val name: String,
    val iconIndex: Int,
    val enabledSkills: List<Boolean>,
    val values: List<String>,
)
