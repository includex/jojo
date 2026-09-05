package com.jojo.game.verification

import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.application.scenario.ScenarioChoiceTrace
import com.jojo.game.application.scenario.ScenarioRandomTrace
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.io.path.createTempDirectory
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioTraceJsonTest {
    @Test fun `writes immutable scenario evidence from the runtime probe`() {
        val probe = ScenarioRuntimeProbe(
            module = "R_00", playback = PlaybackState.CHOICE, options = listOf("one"), selectedChoice = 0,
            sceneIndex = 1, startedScenes = listOf(1), campaignStage = 0, menuVisible = false,
            dialogueText = null, hallBattleScenePending = false, battleButtonScreenX = 0, battleButtonScreenY = 0,
            choiceTrace = listOf(ScenarioChoiceTrace("R_00", "scene1", 12, 0, 2)),
            randomTrace = listOf(ScenarioRandomTrace("R_00", "scene1", 13, 42)),
        )
        val directory = createTempDirectory("scenario-trace")
        val choices = directory.resolve("choices.json")
        val random = directory.resolve("random.json")

        ScenarioTraceJson.writeChoices(choices.toString(), probe)
        ScenarioTraceJson.writeRandom(random.toString(), probe)

        assertEquals("{\"choices\":[{\"module\":\"R_00\",\"function\":\"scene1\",\"line\":12,\"option\":0,\"optionCount\":2}]}\n", choices.readText())
        assertEquals("{\"random\":[{\"module\":\"R_00\",\"function\":\"scene1\",\"line\":13,\"value\":42}]}\n", random.readText())
    }
}
