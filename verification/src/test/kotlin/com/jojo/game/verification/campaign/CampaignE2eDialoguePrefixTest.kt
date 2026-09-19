package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CampaignE2eDialoguePrefixTest {
    private val stop = CampaignE2eStopPoint("R_00", 1)
    private fun probe(revision: Long, text: String = "같은 대사") = ScenarioRuntimeProbe(
        module = "R_00", playback = PlaybackState.DIALOGUE, options = emptyList(), selectedChoice = 0,
        sceneIndex = 1, startedScenes = listOf(0, 1), backgroundId = 30, unitIds = setOf(181, 0),
        campaignStage = 0, menuVisible = false, dialogueText = text, dialogueSpeakerId = "181",
        dialogueRevision = revision, hallBattleScenePending = false, battleButtonScreenX = 0, battleButtonScreenY = 0,
    )

    @Test
    fun `repeated frames count once but identical consecutive authored pages count separately`() {
        val prefix = CampaignE2eDialoguePrefix(stop)
        prefix.observe(probe(1))
        prefix.observe(probe(1))
        prefix.observe(probe(2))
        assertEquals(listOf(1L, 2L), prefix.pages.map { it.revision })
        assertEquals(listOf("같은 대사", "같은 대사"), prefix.pages.map { it.text })
        assertEquals("181", prefix.pages[0].speakerId)
        assertEquals(listOf(0, 181), prefix.pages[0].unitIds)
        assertEquals(30, prefix.pages[0].backgroundId)
    }

    @Test
    fun `only requested scene dialogue is recorded`() {
        val prefix = CampaignE2eDialoguePrefix(stop)
        prefix.observe(probe(1).copy(sceneIndex = 0))
        prefix.observe(probe(2).copy(module = "R_01"))
        prefix.observe(probe(3).copy(playback = PlaybackState.DELAY))
        prefix.observe(probe(4).copy(dialogueText = null))
        assertEquals(emptyList(), prefix.pages)
    }

    @Test
    fun `dialogue stop waits for count and rejects scene overshoot`() {
        val evaluator = CampaignE2eStopEvaluator(stop, 3)
        assertEquals(CampaignE2eStopEvaluator.Decision.CONTINUE, evaluator.evaluate("R_00", 0, 0, 0))
        assertEquals(CampaignE2eStopEvaluator.Decision.CONTINUE, evaluator.evaluate("R_00", 1, 0, 2))
        assertEquals(CampaignE2eStopEvaluator.Decision.REACHED, evaluator.evaluate("R_00", 1, 0, 3))
        assertFailsWith<IllegalStateException> { evaluator.evaluate("R_00", 2, 0, 2) }
        assertFailsWith<IllegalStateException> { evaluator.evaluate("R_01", 0, 2, 2) }
    }

    @Test
    fun `launcher exposes optional positive dialogue count and preserves default`() {
        assertEquals(null, CampaignE2eLaunchOptions.parse(emptyArray()).traceConfig.stopDialoguePages)
        assertEquals(3, CampaignE2eLaunchOptions.parse(arrayOf("--stop-dialogue-pages=3")).traceConfig.stopDialoguePages)
        for (value in listOf("0", "-1", "invalid")) {
            assertFailsWith<IllegalArgumentException> { CampaignE2eLaunchOptions.parse(arrayOf("--stop-dialogue-pages=$value")) }
        }
    }
}
