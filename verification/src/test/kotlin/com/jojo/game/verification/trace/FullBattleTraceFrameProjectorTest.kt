package com.jojo.game.verification.trace

import kotlin.test.Test
import kotlin.test.assertEquals

class FullBattleTraceFrameProjectorTest {
    @Test
    fun `unit driver and dialogue fragments retain byte stable legacy order`() {
        val view = FullBattleTraceFrameProjector.project(frame(unit()))

        assertEquals(
            "[3,11,2,4,5,90,30,6,20,1,1,1,7,8,\"anime20_6\",0.25,[0,10,20,30],{\"abilities\":[1,2,3,4,5],\"level\":6,\"posts\":7,\"arm\":8,\"experience\":9,\"growth\":{\"abilities\":[1,2,3,4,5],\"level\":6,\"posts\":7,\"arm\":8,\"experience\":9},\"attackOffsets\":[[1,2]],\"terrain\":110,\"rates\":[0,1,2,3,4,5,6,7],\"skills\":[[7,1],[43,2],[197,3],[262,4],[276,5]],\"statuses\":[0,1,2,2,1,1,1,0,1,0,1,1,1,0,0],\"statusRounds\":[4,5,6,7,8,9,0,10,0,11,0,0,0,12,13],\"visual\":[1.5,2.5]}]",
            view.unitsJson,
        )
        assertEquals(
            "{\"selectedUnit\":\"u-1\",\"commandPhase\":\"COMMAND\",\"lastInput\":\"tap\\\\n\",\"menuTap\":null,\"eventMessage\":\"hello\",\"autoOverlay\":\"NONE\"}",
            view.driverJson,
        )
        assertEquals("", view.dialogueIdentity)
        assertEquals("\"attack\"", view.actionsJson)
    }

    private fun frame(unit: FullBattleTraceUnitInput) = FullBattleTraceFrameInput(
        frame = 1, elapsed = 1f, delta = .1f, round = 2, camp = 0, maxRounds = 9,
        playerCount = 1, friendCount = 0, enemyCount = 1, paused = false, ended = false, collocation = false,
        dialogue = FullBattleTraceDialogueInput(false, 3, null, "", ""), phase = "PLAYER", script = "COMPLETE",
        bootstrapBusy = emptyList(), cameraX = 0f, cameraY = 0f, mapObjectRevision = 0, mapObjectsJson = "null",
        fightJson = "null", aiPresentation = null, actions = listOf("attack"), units = listOf(unit),
        driver = FullBattleTraceDriverInput("u-1", "COMMAND", "tap\\n", null, "hello", "NONE"),
        observation = null, scriptEnded = false, scriptedOutcome = null, resultFlow = "NONE", modalKind = null,
        pendingScriptPasses = 0, pendingAiDeathPass = 0, postActionDeaths = false, pendingAiResolution = false,
        activeAiCamp = null, roundLayer = false, turnSettlement = false, combatPresentation = false,
    )

    private fun unit() = FullBattleTraceUnitInput(
        internalIndex = 3, characterId = 11, factionOrdinal = 2, tileX = 4, tileY = 5,
        hitPoints = 90, magicPoints = 30, direction = 6, action = 20, visible = true, hasActed = true,
        ai = 7, aiValue = 8, animationTime = .25f, sprite = FullBattleTraceSpriteInput(10, 20, 30),
        abilities = listOf(1, 2, 3, 4, 5), level = 6, posts = 7, armId = 8, experience = 9,
        attackOffsets = listOf(FullBattleTracePoint(1, 2)), terrain = 110, rates = (0..7).toList(),
        skillValues = listOf(1, 2, 3, 4, 5), attributeLifts = listOf(-1, 0, 1, 2, 0, 0),
        attributeLiftRounds = listOf(4, 5, 6, 7, 8, 9), paralysisActive = true, paralysisRound = 10,
        silenceActive = false, silenceRound = 0, confusionActive = true, confusionRound = 11,
        poisonActive = false, poisonRound = 0, lostActive = true, lostRound = 12, actionStatusRound = 13,
        visualX = 1.5f, visualY = 2.5f,
    )
}
