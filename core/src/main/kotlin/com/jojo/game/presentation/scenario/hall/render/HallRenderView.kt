// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.jojo.game.application.runtime.RuntimeScenarioOverlay

/** HallMenuRenderView: 거점 메뉴 렌더링 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallMenuRenderView(
    val eventName: String,
    val stageName: String,
    val ambitionFrom: Int,
    val ambitionTo: Int,
    val ambitionElapsedSeconds: Float,
    val indicatorEnabled: Boolean,
    val interactive: Boolean,
    val variant: RuntimeScenarioOverlay?,
)

internal data class HallSaveRowRenderView(val number: String, val stage: String, val name: String)
internal data class HallSaveRenderView(
    val rows: List<HallSaveRowRenderView>,
    val pendingPrompt: String?,
    val completionTipOpen: Boolean,
)

internal data class HallCommandRenderView(
    val menuTexture: com.badlogic.gdx.graphics.Texture?,
    val battleTexture: com.badlogic.gdx.graphics.Texture?,
    val equipTexture: com.badlogic.gdx.graphics.Texture?,
    val buyTexture: com.badlogic.gdx.graphics.Texture?,
    val sellTexture: com.badlogic.gdx.graphics.Texture?,
)

internal sealed interface HallManagementRenderView {
    data class Equip(val view: com.jojo.game.presentation.scenario.hall.HallEquipView) : HallManagementRenderView
    data class Sell(val catalog: com.jojo.game.presentation.scenario.hall.HallSellView) : HallManagementRenderView
}

internal data class HallBuyManagementRenderView(
    val catalog: com.jojo.game.presentation.scenario.hall.HallBuyCatalogView,
    val summary: com.jojo.game.presentation.scenario.hall.HallBuyUnitSummaryView,
    val money: String,
    val notice: String?,
)

internal sealed interface HallInfoRenderView {
    data class Forces(val view: com.jojo.game.presentation.scenario.hall.HallForcesView) : HallInfoRenderView
    data class Property(val view: com.jojo.game.presentation.scenario.hall.HallPropertyView) : HallInfoRenderView
    data class Terrain(val view: com.jojo.game.presentation.scenario.hall.HallTerrainView) : HallInfoRenderView
    data class Treasure(val view: com.jojo.game.presentation.scenario.hall.HallTreasureView) : HallInfoRenderView
    data class Helper(val view: com.jojo.game.presentation.scenario.hall.HallHelperView) : HallInfoRenderView
}

/** HallRenderGeometry: 거점 렌더링 Geometry이며, 시나리오 장면을 정확히 표시하기 위한 변환·갱신 규칙을 제공한다. */
internal object HallRenderGeometry {
    val menuButtonCenters = floatArrayOf(55.107f, 143.365f, 231.846f, 320.74f, 423.317f, 511.575f, 600.056f, 690.441f, 789.44f)
    fun saveRowY(index: Int): Float = 547.534f - index * 52f
}
