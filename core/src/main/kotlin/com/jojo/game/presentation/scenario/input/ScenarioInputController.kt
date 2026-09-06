// Scenario
package com.jojo.game.presentation.scenario.input

import com.badlogic.gdx.Input
import com.jojo.game.domain.scenario.PlaybackState

/** ScenarioInputPort: 시나리오 입력 Port로, 시나리오 표현 계층 사이에서 필요한 동작과 데이터를 약속한다. */
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

/** ScenarioInputController: 키보드와 터치 입력을 현재 재생 상태에 맞는 선택·진행·거점 동작으로 전달한다. */
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
