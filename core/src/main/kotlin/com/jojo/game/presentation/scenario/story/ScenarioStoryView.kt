// Scenario
package com.jojo.game.presentation.scenario.story

import com.jojo.game.presentation.scenario.overlay.*

/** ScenarioStreetDialogueView: 거리 대사 장면의 화자·본문·말풍선 표시 위치를 전달하는 불변 입력이다. */
internal data class ScenarioStreetDialogueView(
    val hasDialogue: Boolean,
    val portraitId: Int?,
    val speaker: String,
    val visibleText: String,
    val isLeft: Boolean,
    val isAtTop: Boolean,
)

/** ScenarioPalaceFixtureView: 궁전 fixture 장면에서 유지할 장식·대사 상태를 전달하는 불변 입력이다. */
internal data class ScenarioPalaceFixtureView(
    val dialogueText: String,
    val portraitId: Int,
    val speaker: String,
)

/**
 * `ScenarioStreetDialogueStages`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal object ScenarioStreetDialogueStages {
    /**
     * `order` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val order = listOf("panel", "portrait", "speaker", "text", "background", "characters")

    /**
     * `indexOf`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun indexOf(stage: String): Int = order.indexOf(stage)

    /**
     * `nameAt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun nameAt(index: Int): String? = order.getOrNull(index)

    /**
     * `backgroundIndex`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun backgroundIndex(): Int = order.indexOf("background")

    /**
     * `charactersIndex`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun charactersIndex(): Int = order.indexOf("characters")
}
