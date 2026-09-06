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

/**
 * `HallSaveRowRenderView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallSaveRowRenderView(val number: String, val stage: String, val name: String)
/**
 * `HallSaveRenderView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallSaveRenderView(
    val rows: List<HallSaveRowRenderView>,
    val pendingPrompt: String?,
    val completionTipOpen: Boolean,
)

/**
 * `HallCommandRenderView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallCommandRenderView(
    val menuTexture: com.badlogic.gdx.graphics.Texture?,
    val battleTexture: com.badlogic.gdx.graphics.Texture?,
    val equipTexture: com.badlogic.gdx.graphics.Texture?,
    val buyTexture: com.badlogic.gdx.graphics.Texture?,
    val sellTexture: com.badlogic.gdx.graphics.Texture?,
)

/**
 * `HallManagementRenderView`: 관련 상태와 동작을 묶는 interface다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal sealed interface HallManagementRenderView {
    /**
     * `Equip`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Equip(val view: com.jojo.game.presentation.scenario.hall.HallEquipView) : HallManagementRenderView
    /**
     * `Sell`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Sell(val catalog: com.jojo.game.presentation.scenario.hall.HallSellView) : HallManagementRenderView
}

/**
 * `HallBuyManagementRenderView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallBuyManagementRenderView(
    val catalog: com.jojo.game.presentation.scenario.hall.HallBuyCatalogView,
    val summary: com.jojo.game.presentation.scenario.hall.HallBuyUnitSummaryView,
    val money: String,
    val notice: String?,
)

/**
 * `HallInfoRenderView`: 관련 상태와 동작을 묶는 interface다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal sealed interface HallInfoRenderView {
    /**
     * `Forces`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Forces(val view: com.jojo.game.presentation.scenario.hall.HallForcesView) : HallInfoRenderView
    /**
     * `Property`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Property(val view: com.jojo.game.presentation.scenario.hall.HallPropertyView) : HallInfoRenderView
    /**
     * `Terrain`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Terrain(val view: com.jojo.game.presentation.scenario.hall.HallTerrainView) : HallInfoRenderView
    /**
     * `Treasure`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Treasure(val view: com.jojo.game.presentation.scenario.hall.HallTreasureView) : HallInfoRenderView
    /**
     * `Helper`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Helper(val view: com.jojo.game.presentation.scenario.hall.HallHelperView) : HallInfoRenderView
}

/** HallRenderGeometry: 거점 렌더링 Geometry이며, 시나리오 장면을 정확히 표시하기 위한 변환·갱신 규칙을 제공한다. */
internal object HallRenderGeometry {
    /**
     * `menuButtonCenters` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuButtonCenters = floatArrayOf(55.107f, 143.365f, 231.846f, 320.74f, 423.317f, 511.575f, 600.056f, 690.441f, 789.44f)
    /**
     * `saveRowY`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun saveRowY(index: Int): Float = 547.534f - index * 52f
}
