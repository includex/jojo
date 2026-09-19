package com.jojo.game

import com.jojo.game.application.scenario.ScenarioDelayCoordinator
import com.jojo.game.application.scenario.ScenarioDialogueCoordinator
import com.jojo.game.application.scenario.ScenarioModalController
import com.jojo.game.application.scenario.ScenarioStage
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.domain.scenario.ScenarioCommand
import kotlin.test.Test
import kotlin.test.assertEquals

class HallRenderSelectionPhaseTest {
    @Test fun `stage timer action change renders on the next AnimationManager phase`() {
        val stage = ScenarioStage()
        stage.apply(ScenarioCommand.ShowUnit(181, 40, 45, 2))
        var state = PlaybackState.COMPLETE
        var remaining = 0f
        val delay = coordinator(stage, { state }, { state = it }, { remaining }, { remaining = it }) {
            stage.setScriptedUnitAction(181, action = 0, direction = 0)
            state = PlaybackState.DIALOGUE
        }

        delay.suspendForStageDelay(1)
        delay.update(0f)
        delay.update(0.1f)

        val unit = stage.unit(181)
        assertEquals(0, unit.direction, "timer continuation mutates the logical Hall unit immediately")
        assertEquals(2, unit.hallRenderDirection, "the completed AnimationManager phase retains the prior sprite")

        delay.update(0f)
        assertEquals(0, unit.hallRenderDirection, "the next AnimationManager phase samples the new direction")
    }

    @Test fun `Hall move completion continuation is sampled in the same render phase`() {
        val stage = ScenarioStage()
        stage.apply(ScenarioCommand.ShowUnit(181, 40, 44, 2))
        stage.moveUnits(listOf(ScenarioCommand.MoveUnit(181, 40, 45, 2)))
        var state = PlaybackState.DELAY
        var remaining = 0f
        val delay = coordinator(stage, { state }, { state = it }, { remaining }, { remaining = it }) {
            stage.setScriptedUnitAction(181, action = 6, direction = 3)
            state = PlaybackState.COMPLETE
        }
        delay.suspendForHallMoves(setOf(181))

        delay.update(0f)
        delay.update(1f)

        val unit = stage.unit(181)
        assertEquals(0f, unit.moveDuration)
        assertEquals(6, unit.hallRenderAction)
        assertEquals(3, unit.hallRenderDirection)
    }

    private fun coordinator(
        stage: ScenarioStage,
        getState: () -> PlaybackState,
        setState: (PlaybackState) -> Unit,
        getRemaining: () -> Float,
        setRemaining: (Float) -> Unit,
        resume: () -> Unit,
    ): ScenarioDelayCoordinator {
        val dialogue = ScenarioDialogueCoordinator(stage, setState, {}, setRemaining)
        val modal = ScenarioModalController(stage, setState, {})
        return ScenarioDelayCoordinator(
            stage, dialogue, modal,
            getState = getState,
            onSetState = setState,
            onResumeExecution = resume,
            getDelayRemainingSeconds = getRemaining,
            onSetDelayRemainingSeconds = setRemaining,
        )
    }
}
