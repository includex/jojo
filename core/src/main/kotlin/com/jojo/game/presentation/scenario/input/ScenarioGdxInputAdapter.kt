package com.jojo.game.presentation.scenario.input

import com.badlogic.gdx.InputAdapter

/** LibGDX coordinate adapter; routing remains testable without a viewport. */
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
