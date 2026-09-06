// Test
package com.jojo.game.presentation.battle.evidence

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BattleCompositionEvidenceProjectorTest {
    @Test
    fun `projects visible units terrain masks and opening dialogue from immutable snapshots`() {
        var speakerLookups = 0
        var plannerLookups = 0

        val view = BattleCompositionEvidenceProjector.project(
            input(
                openingSayRoute = true,
                sourceScenario = "S_00",
                units = listOf(
                    BattleCompositionEvidenceUnitInput(
                        id = "unit-1", visible = true, textureUuid = "atlas-1", sourceY = 101,
                        sourceWidth = 48, sourceHeight = 48, characterId = 7, tileX = 2, tileY = 3,
                        scriptedAction = 4, flipX = true,
                    ),
                    BattleCompositionEvidenceUnitInput(
                        id = "hidden", visible = false, textureUuid = null, sourceY = 1,
                        sourceWidth = 48, sourceHeight = 48, characterId = 8, tileX = 4, tileY = 5,
                        scriptedAction = null, flipX = false,
                    ),
                ),
                terrainAt = { x, _ -> if (x == 2) 10 else 1 },
                dialogue = BattleCompositionEvidenceDialogueInput("7", "원문", "원", typewriterComplete = false),
                speakerName = { speakerId ->
                    speakerLookups++
                    "장수$speakerId"
                },
                enemyPlanner = {
                    plannerLookups++
                    null
                },
            )
        )

        assertEquals("r00-opening-say", view.scenarioKey)
        assertEquals(1, view.units.size)
        assertEquals(33_632_304, view.units.single().frame)
        assertEquals("hight-light/u_value=1", view.units.single().material)
        assertEquals(-1, view.units.single().scaleX)
        assertEquals(listOf("Mark_19-1"), view.masks.map(BattleCompositionMask::frame))
        assertEquals("장수7", view.scenario.dialogue?.speakerName)
        assertEquals("문", view.scenario.dialogue?.remainingText)
        assertTrue(view.scenario.dialogue!!.opening)
        assertTrue(view.scenario.dialogue!!.typewriterActive)
        assertEquals(1, speakerLookups)
        assertEquals(0, plannerLookups)
    }

    @Test
    fun `projects action and enemy planner only for their selected scenario routes`() {
        val actionView = BattleCompositionEvidenceProjector.project(
            input(
                animationClock = 2f,
                actionCapture = BattleCompositionEvidenceActionCaptureInput(6, 1f / 24f),
                action = BattleCompositionEvidenceActionInput(6, 2, endsAt = 3f),
                enemyPlanner = { error("non-enemy routes must not query the planner") },
            )
        )
        assertEquals("battle-action-6-f0", actionView.scenarioKey)
        assertEquals(BattleCompositionAction(6, 2, active = true), actionView.scenario.action)
        assertNull(actionView.scenario.enemyPlanner)

        var plannerLookups = 0
        val enemyView = BattleCompositionEvidenceProjector.project(
            input(
                enemyTurnRoute = true,
                enemyPlanner = {
                    plannerLookups++
                    BattleCompositionEvidenceEnemyPlannerInput(474, 1, 10, 11, 12, 13, "target", 14)
                },
            )
        )
        assertEquals("enemy-turn", enemyView.scenarioKey)
        assertEquals(1, plannerLookups)
        assertEquals(474, enemyView.scenario.enemyPlanner?.characterId)
        assertFalse(enemyView.scenario.loseActive)
    }

    private fun input(
        animationClock: Float = 1f,
        sourceScenario: String = "R_00",
        openingSayRoute: Boolean = false,
        enemyTurnRoute: Boolean = false,
        units: List<BattleCompositionEvidenceUnitInput> = emptyList(),
        terrainAt: (Int, Int) -> Int = { _, _ -> 0 },
        dialogue: BattleCompositionEvidenceDialogueInput? = null,
        speakerName: (String) -> String? = { null },
        actionCapture: BattleCompositionEvidenceActionCaptureInput? = null,
        action: BattleCompositionEvidenceActionInput? = null,
        enemyPlanner: () -> BattleCompositionEvidenceEnemyPlannerInput? = { null },
    ) = BattleCompositionEvidenceProjectionInput(
        animationClock = animationClock,
        visualAnimationClock = 1.25f,
        mapOnlyCapture = false,
        sourceScenario = sourceScenario,
        returnScenario = "R_00",
        battleMenuOpen = false,
        effectCount = 0,
        openingSayRoute = openingSayRoute,
        dialogueOneRoute = false,
        actionCapture = actionCapture,
        winModalRoute = false,
        enemyTurnRoute = enemyTurnRoute,
        loseResultRoute = false,
        winResultRoute = false,
        units = units,
        terrainAt = terrainAt,
        dialogue = dialogue,
        speakerName = speakerName,
        action = action,
        winConditionOpen = false,
        winConditionModal = false,
        enemyPlanner = enemyPlanner,
        loseActive = false,
        winPromptActive = false,
    )
}
