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
    fun mapX(x: Float, y: Float): Float = (x - y + 42f) * 16f
    fun mapY(x: Float, y: Float): Float = 1073.28f - (x + y) * 6.88f
    fun headCenterX(x: Float): Float = x * 2f + 55.04f
    fun headCenterY(y: Float): Float = 688f - y * 1.72f - 68.8f

    fun orderedNodeIds(view: ScenarioBattlefieldRenderView): List<String> = buildList {
        if (!view.drawCharacters) return@buildList
        if (view.drawUnits) view.units.filter { it.visible }.forEach { add(Node(it.zIndex, it.siblingOrder, "unit:${it.id}")) }
        view.heads.filter { it.opacity > 0f }.forEach { add(Node(it.zIndex, it.siblingOrder, "head:${it.portraitId}")) }
    }.sortedWith(compareBy<Node> { it.zIndex }.thenBy { it.siblingOrder }).map(Node::id)

    private data class Node(val zIndex: Float, val siblingOrder: Int, val id: String)
}

internal enum class ScenarioOverlayState { DIALOGUE, CHOICE, DELAY, MODAL }
internal enum class ScenarioOverlayModalKind { EVENT, INFO, MAP_INFO, SECTION, AMBITION, OTHER }

internal data class ScenarioChoiceRenderView(
    val isAsk: Boolean,
    val portraitId: Int?,
    val options: List<String>,
)

internal data class ScenarioModalRenderView(
    val kind: ScenarioOverlayModalKind,
    val text: String,
    val visibleText: String,
    val fixedText: String,
    val variant: RuntimeScenarioOverlay?,
)

internal data class ScenarioOverlayRenderView(
    val state: ScenarioOverlayState,
    val dialogue: ScenarioStreetDialogueView?,
    val choice: ScenarioChoiceRenderView?,
    val modal: ScenarioModalRenderView?,
)
