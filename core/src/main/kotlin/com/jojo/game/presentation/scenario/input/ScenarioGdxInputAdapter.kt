// Scenario
package com.jojo.game.presentation.scenario.input

import com.badlogic.gdx.InputAdapter

/** ScenarioGdxInputAdapter: 시나리오 Gdx 입력 어댑터이며, 시나리오 장면을 정확히 표시하기 위한 변환·갱신 규칙을 제공한다. */
internal class ScenarioGdxInputAdapter(
    private val controller: ScenarioInputController,
    private val worldAt: (Int, Int) -> Pair<Float, Float>,
) : InputAdapter() {
    override fun keyDown(keycode: Int): Boolean = controller.keyDown(keycode)
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val (x, y) = worldAt(screenX, screenY)
        return controller.touch(x, y)
    }
    override fun scrolled(amountX: Float, amountY: Float): Boolean = controller.scroll(amountY)
}
