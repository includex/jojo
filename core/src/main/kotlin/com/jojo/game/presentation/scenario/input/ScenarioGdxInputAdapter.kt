// Scenario
package com.jojo.game.presentation.scenario.input

import com.badlogic.gdx.InputAdapter

/** ScenarioGdxInputAdapter: 시나리오 Gdx 입력 어댑터이며, 시나리오 장면을 정확히 표시하기 위한 변환·갱신 규칙을 제공한다. */
internal class ScenarioGdxInputAdapter(
    /** `controller` (ScenarioInputController): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val controller: ScenarioInputController,
    /** `worldAt` ((Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val worldAt: (Int, Int) -> Pair<Float, Float>,
) : InputAdapter() {
    /**
     * `keyDown`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun keyDown(keycode: Int): Boolean = controller.keyDown(keycode)
    /**
     * `touchDown`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val (x, y) = worldAt(screenX, screenY)
        return controller.touch(x, y)
    }
    /**
     * `scrolled`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun scrolled(amountX: Float, amountY: Float): Boolean = controller.scroll(amountY)
}
