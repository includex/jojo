// Scenario
package com.jojo.game.presentation.scenario.render

import com.jojo.game.application.runtime.RuntimeScenarioOverlay

import com.jojo.game.presentation.scenario.story.ScenarioStreetDialogueView

/** ScenarioBattlefieldRenderView: 시나리오 Battlefield 렌더링 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class ScenarioBattlefieldRenderView(
    val backgroundId: Int,
    val drawCharacters: Boolean,
    val drawUnits: Boolean,
    val units: List<ScenarioBattlefieldUnitView>,
    val heads: List<ScenarioBattlefieldHeadView>,
)

/**
 * `ScenarioBattlefieldUnitView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class ScenarioBattlefieldUnitView(
    val id: Int,
    val visualX: Float,
    val visualY: Float,
    val visible: Boolean,
    val zIndex: Float,
    val siblingOrder: Int,
    val textureAssetId: Int,
    val frameRow: Int,
    val flipX: Boolean,
    val showSpeechBubble: Boolean,
)

/**
 * `ScenarioBattlefieldHeadView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class ScenarioBattlefieldHeadView(
    val portraitId: Int,
    val visualX: Float,
    val visualY: Float,
    val opacity: Float,
    val zIndex: Float,
    val siblingOrder: Int,
)

/** ScenarioBattlefieldRenderGeometry: 시나리오 Battlefield 렌더링 Geometry이며, 시나리오 장면을 정확히 표시하기 위한 변환·갱신 규칙을 제공한다. */
internal object ScenarioBattlefieldRenderGeometry {
    /**
     * `mapX`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun mapX(x: Float, y: Float): Float = (x - y + 42f) * 16f
    /**
     * `mapY`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun mapY(x: Float, y: Float): Float = 1073.28f - (x + y) * 6.88f
    /**
     * `headCenterX`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun headCenterX(x: Float): Float = x * 2f + 55.04f
    /**
     * `headCenterY`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun headCenterY(y: Float): Float = 688f - y * 1.72f - 68.8f

    /**
     * `orderedNodeIds`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun orderedNodeIds(view: ScenarioBattlefieldRenderView): List<String> = buildList {
        if (!view.drawCharacters) return@buildList
        if (view.drawUnits) view.units.filter { it.visible }.forEach { add(Node(it.zIndex, it.siblingOrder, "unit:${it.id}")) }
        view.heads.filter { it.opacity > 0f }.forEach { add(Node(it.zIndex, it.siblingOrder, "head:${it.portraitId}")) }
    }.sortedWith(compareBy<Node> { it.zIndex }.thenBy { it.siblingOrder }).map(Node::id)

    /**
     * `Node`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    private data class Node(val zIndex: Float, val siblingOrder: Int, val id: String)
}

/**
 * `ScenarioOverlayState`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class ScenarioOverlayState { DIALOGUE, CHOICE, DELAY, MODAL }
/**
 * `ScenarioOverlayModalKind`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal enum class ScenarioOverlayModalKind { EVENT, INFO, MAP_INFO, SECTION, AMBITION, OTHER }

/**
 * `ScenarioChoiceRenderView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class ScenarioChoiceRenderView(
    val isAsk: Boolean,
    val portraitId: Int?,
    val options: List<String>,
)

/**
 * `ScenarioModalRenderView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class ScenarioModalRenderView(
    val kind: ScenarioOverlayModalKind,
    val text: String,
    val visibleText: String,
    val fixedText: String,
    val variant: RuntimeScenarioOverlay?,
)

/**
 * `ScenarioOverlayRenderView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class ScenarioOverlayRenderView(
    val state: ScenarioOverlayState,
    val dialogue: ScenarioStreetDialogueView?,
    val choice: ScenarioChoiceRenderView?,
    val modal: ScenarioModalRenderView?,
)
