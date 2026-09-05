package com.jojo.game.presentation.scenario.input

import com.badlogic.gdx.Input
import com.jojo.game.domain.scenario.PlaybackState

/** Narrow state/command boundary between ScenarioScreen and input routing. */
interface ScenarioInputPort {
    fun hallState(): ScenarioInputRouter.HallState
    fun playbackState(): PlaybackState
    fun isAskChoice(): Boolean
    fun choiceCount(): Int
    fun dismissHallOverlay(): Boolean
    fun selectPrevious()
    fun selectNext()
    fun advance()
    fun selectAndConfirm(index: Int)
    fun routeHallTouch(route: ScenarioInputRouter.Touch.Hall, x: Float, y: Float)
}

class ScenarioInputController(private val port: ScenarioInputPort) {
    fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE && port.dismissHallOverlay()) return true
        when (keycode) {
            Input.Keys.UP -> port.selectPrevious()
            Input.Keys.DOWN -> port.selectNext()
            Input.Keys.ENTER, Input.Keys.SPACE -> port.advance()
        }
        return true
    }

    fun touch(x: Float, y: Float): Boolean {
        val hall = ScenarioInputRouter.hallTouch(port.hallState(), x, y)
        if (hall is ScenarioInputRouter.Touch.Hall) {
            port.routeHallTouch(hall, x, y)
            return true
        }
        if (port.playbackState() == PlaybackState.CHOICE) {
            when (val choice = ScenarioInputRouter.choiceTouch(port.isAskChoice(), port.choiceCount(), x, y)) {
                is ScenarioInputRouter.Touch.SelectAndConfirm -> port.selectAndConfirm(choice.index)
                ScenarioInputRouter.Touch.Advance -> port.advance()
                ScenarioInputRouter.Touch.None, is ScenarioInputRouter.Touch.Hall -> Unit
            }
        } else port.advance()
        return true
    }

    fun scroll(amountY: Float): Boolean {
        if (port.playbackState() != PlaybackState.CHOICE) return false
        if (amountY > 0f) port.selectNext() else if (amountY < 0f) port.selectPrevious()
        return true
    }
}
