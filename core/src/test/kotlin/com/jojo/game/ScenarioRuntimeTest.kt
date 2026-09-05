package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ScenarioRuntimeTest {
    @Test
    fun `countDirection matches BattleUnit axes tie and self contracts`() {
        val stage = ScenarioStage()
        stage.seedBattleUnitPosition(1, 10, 10)
        stage.setUnitDirection(1, 2)

        fun target(id: Int, x: Int, y: Int): Int {
            stage.seedBattleUnitPosition(id, x, y)
            return stage.countDirection(1, id)
        }

        assertEquals(1, target(2, 12, 10))
        assertEquals(3, target(3, 8, 10))
        assertEquals(2, target(4, 10, 12))
        assertEquals(0, target(5, 10, 8))
        assertEquals(1, target(6, 12, 12), "equal deltas use the horizontal axis")
        assertEquals(2, stage.countDirection(1, 1), "self keeps the live direction")
    }

    @Test
    fun `S22 opening countDir queues the original defender direction`() {
        val campaign = CampaignState().also {
            it.roster.restoreBattleRoster(listOf(0, 22, 8, 1, 2, 3, 4, 5, 6, 7, 9))
        }
        val runtime = ScenarioInterpreter.load("S_22", campaign)
        runtime.enableExternalBattlePresentation()
        runtime.start("scene0")
        // loadBg/draw owns the first native callback; resuming it reaches
        // setDir(countDir) and then the action-5 presentation barrier.
        runtime.completeBattleBackgroundLoad()

        assertEquals(2, runtime.stage.unit(118).direction)
        val scripted = requireNotNull(runtime.stage.battleUnitForCharacterId(118))
        assertEquals(2, scripted.direction)
        assertEquals(2, BattleScenarioFactory.fromScriptedUnits(listOf(scripted)).units.values.single().direction)
        assertTrue(runtime.stage.consumeScriptedUnitDirections().contains(118 to 2))
    }

    @Test
    fun `initial battle rows preserve authored Control AI target fields`() {
        val stage = ScenarioStage()

        stage.createBattleUnits(
            ScenarioUnitFaction.ENEMY,
            listOf(mapOf(
                "id" to 700,
                "i" to 3,
                "x" to 12,
                "y" to 4,
                "ai" to ControlAi.RETREAT_TO,
                "targetId" to 9,
                "targetX" to 2,
                "targetY" to 8,
            )),
        )

        val battleUnit = requireNotNull(stage.battleUnitForCharacterId(700))
        assertEquals(9, battleUnit.aiTargetId)
        assertEquals(2 to 8, battleUnit.aiTargetX to battleUnit.aiTargetY)
        assertEquals(9, stage.unit(700).aiTargetId)
        assertEquals(2 to 8, stage.unit(700).aiTargetX to stage.unit(700).aiTargetY)

        val liveUnit = BattleScenarioFactory.fromScriptedUnits(stage.battleUnits.values).units.values.single()
        assertEquals(9, liveUnit.aiTargetCharacterId)
        assertEquals(2 to 8, liveUnit.aiTargetX to liveUnit.aiTargetY)
    }

    @Test
    fun `explicit zero battle coordinate remains authored for source camera anchors`() {
        val stage = ScenarioStage()

        stage.createBattleUnits(
            ScenarioUnitFaction.FRIEND,
            listOf(mapOf("id" to 258, "i" to 6, "x" to 6, "y" to 0)),
        )

        val unit = requireNotNull(stage.battleUnitForCharacterId(258))
        assertTrue(unit.authoredX)
        assertTrue(unit.authoredY)
        assertEquals(0, unit.y)
    }

    @Test
    fun `stage name mutates eagerly and waits for InfoLayer only after draw`() {
        val runtime = ScenarioInterpreter.load("R_01")
        runtime.start("scene1")

        assertEquals(PlaybackState.MODAL, runtime.state)
        assertEquals("조조가 군대를 일으키다", runtime.currentModalText)
        assertEquals("", runtime.stage.stageName)

        runtime.resumeModal()
        assertEquals("낙양 @ 총리관", runtime.stage.stageName)
        assertEquals(PlaybackState.MODAL, runtime.state)
        assertEquals("낙양 @ 총리관", runtime.currentModalText)
    }

    @Test
    fun `stage name skip branch keeps eager mutation and does not open InfoLayer`() {
        val runtime = ScenarioInterpreter.load("R_01")
        runtime.start("scene1")
        assertEquals(PlaybackState.MODAL, runtime.state)

        runtime.setStagePresentationSkipped(true)
        runtime.resumeModal()

        assertEquals("낙양 @ 총리관", runtime.stage.stageName)
        assertEquals(PlaybackState.DELAY, runtime.state)
        assertNull(runtime.currentModalText)
    }

    @Test
    fun `production dialogue close exposes one callback frame before next say`() {
        val runtime = ScenarioInterpreter.load("S_00")
        runtime.start("scene0")
        runtime.skipDelay()
        assertEquals("474", runtime.currentDialogue?.speakerId)

        runtime.advanceDialogue(deferCloseCallbackFrame = true)
        assertEquals(PlaybackState.DELAY, runtime.state)
        assertNull(runtime.currentDialogue)

        runtime.update(1f / 60f)
        assertEquals(PlaybackState.DELAY, runtime.state)
        assertNull(runtime.currentDialogue)

        runtime.update(1f / 60f)
        assertEquals(PlaybackState.DELAY, runtime.state)
        // S_00 continues with attackAction(...) and then an authored
        // stage.delay(10); both callbacks precede speaker 235.
        runtime.skipDelay()
        assertEquals(PlaybackState.DELAY, runtime.state)
        runtime.skipDelay()
        assertEquals("235", runtime.currentDialogue?.speakerId)
    }

    @Test
    fun `R01 hall battle command runs source scene8 departure before routing`() {
        val runtime = ScenarioInterpreter.load("R_01")
        runtime.stage.setMenuVisible(true)
        runtime.stage.setBackgroundSound(15)

        runtime.selectHallBattleCommand()
        runtime.start("scene8")

        assertEquals(PlaybackState.DIALOGUE, runtime.state)
        assertFalse(runtime.stage.menuVisible)
        assertEquals(Dialogue("0", "출발."), runtime.currentDialogue)
        // bgSound(-1) is after stage.say in the recovered source and therefore
        // must not execute until the SayLayer completion callback resumes it.
        assertEquals(15, runtime.stage.backgroundSound)

        runtime.advanceDialogue()

        assertEquals(PlaybackState.COMPLETE, runtime.state)
        assertNull(runtime.currentDialogue)
        assertEquals(-1, runtime.stage.backgroundSound)
    }

    @Test
    fun `R01 scene six joins its party before scene seven commits the seven slot battle entry`() {
        val campaign = CampaignState().also { it.joinedUnits += 0 }
        val runtime = ScenarioInterpreter.load("R_01", campaign)

        runtime.start("scene6")
        assertEquals(listOf(0, 1, 6, 10, 11, 5, 12), campaign.joinedUnits.toList())

        runtime.setStagePresentationSkipped(true)
        runtime.start("scene7")
        assertEquals(PlaybackState.DIALOGUE, runtime.state)
        runtime.advanceDialogue()

        assertEquals(ScenarioJoinBattleLimit(1, 7, listOf(1), emptyList()), runtime.stage.joinBattleLimit)
        assertEquals(listOf(0, 1, 6, 10, 11, 5, 12), campaign.roster.battleRoster)
    }

    @Test
    fun `native battle say4 borrows the production dialogue state until input dismisses it`() {
        val runtime = ScenarioInterpreter.load("S_00")
        val initialRevision = runtime.dialogueRevision
        val initialLifecycleRevision = runtime.dialogueLifecycleRevision

        runtime.presentExternalBattleDialogue(Dialogue("0", "퇴각 대사"))

        assertEquals(PlaybackState.DIALOGUE, runtime.state)
        assertEquals(Dialogue("0", "퇴각 대사"), runtime.currentDialogue)
        assertEquals(initialRevision + 1, runtime.dialogueRevision)
        assertEquals(initialLifecycleRevision + 1, runtime.dialogueLifecycleRevision)
        assertEquals("&0\n퇴각 대사", runtime.currentDialogueSourceText)
        runtime.advanceDialogue()
        assertEquals(PlaybackState.COMPLETE, runtime.state)
        assertNull(runtime.currentDialogue)
        runtime.presentExternalBattleDialogue(Dialogue("0", "퇴각 대사"))
        assertEquals(initialRevision + 2, runtime.dialogueRevision, "동일한 화자와 문장도 새 SayLayer 표시다")
        assertEquals(initialLifecycleRevision + 2, runtime.dialogueLifecycleRevision)
    }

    @Test
    fun `battle show request retains revival arguments for one production callback`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(ScenarioUnitFaction.MINE, listOf(mapOf("id" to 32, "i" to 0)))
        stage.setBattleUnitVisibility(32, false)
        stage.requestUnitShow(ScenarioUnitShowRequest(32, 7, 9, 3, 1))

        assertEquals(ScenarioUnitShowRequest(32, 7, 9, 3, 1), stage.consumeUnitShowRequest())
        assertNull(stage.consumeUnitShowRequest())
        assertFalse(stage.battleUnits.getValue("MINE:0").hidden)
    }

    @Test
    fun `stage center requests preserve FIFO coordinates and repeated dispatches without a barrier`() {
        val stage = ScenarioStage()
        stage.requestCameraCenter(11, 20)
        stage.requestCameraCenter(13, 20)
        stage.requestCameraCenter(13, 20)

        assertEquals(
            listOf(
                ScenarioCameraCenterRequest(11, 20),
                ScenarioCameraCenterRequest(13, 20),
                ScenarioCameraCenterRequest(13, 20),
            ),
            stage.consumeCameraCenterRequests(),
        )
        assertEquals(emptyList(), stage.consumeCameraCenterRequests())
    }

    @Test
    fun `script presentation requests retain authored FIFO and callback metadata`() {
        val stage = ScenarioStage()
        val unit = ScenarioScriptPresentationRequest.UnitHighlight(146)
        val rectangle = ScenarioScriptPresentationRequest.RectangleHighlight(10, 8, 12, 9)
        val objects = ScenarioScriptPresentationRequest.MapObjects(
            enabled = true,
            terrainId = 12,
            objects = listOf(ScenarioScriptPresentationRequest.MapObjects.Object(22, 11, 18)),
            soundOnFirstObjectOnly = true,
        )

        stage.requestScriptPresentation(unit)
        stage.requestScriptPresentation(rectangle)
        stage.requestScriptPresentation(objects)

        assertEquals(listOf(unit, rectangle, objects), stage.consumeScriptPresentationRequests())
        assertNull(stage.consumeScriptPresentationRequest())
        assertEquals(2.4f, unit.durationSeconds, 0.001f)
        assertEquals(2.4f, rectangle.durationSeconds, 0.001f)
    }

    @Test
    fun `scripted setAction retains source direction loop and FINISHED barrier contract`() {
        val stage = ScenarioStage()

        stage.setScriptedUnitAction(32, action = 6, direction = 3)
        stage.setScriptedUnitAction(32, action = 5, direction = -1, loop = true)
        stage.setScriptedUnitAction(32, action = 0)

        assertEquals(
            listOf(
                ScriptedUnitAction(32, 6, 3, loop = false, awaitsFinishedCallback = true),
                ScriptedUnitAction(32, 5, -1, loop = true, awaitsFinishedCallback = false),
                ScriptedUnitAction(32, 0, -1, loop = false, awaitsFinishedCallback = false),
            ),
            stage.consumeScriptedUnitActions(),
        )
        assertEquals(3, stage.unit(32).direction)
    }

    @Test
    fun `S09 AST keeps consecutive unit and rectangle highlights as distinct episodes`() {
        val runtime = ScenarioInterpreter.load("S_09")
        runtime.enableExternalBattlePresentation()

        runtime.start("scene0")
        assertTrue(runtime.hasPendingBattleBackgroundLoad)
        assertEquals(0, runtime.stage.battleMapIndex)
        runtime.completeBattleBackgroundLoad()
        assertEquals(PlaybackState.DELAY, runtime.state)
        assertEquals(5, runtime.stage.consumeScriptedUnitActions().single().action)
        runtime.resumeExternalDelay()
        var pages = 0
        while (runtime.state == PlaybackState.DIALOGUE && pages++ < 20) runtime.advanceDialogue()
        assertEquals(PlaybackState.MODAL, runtime.state)
        runtime.resumeModal()

        assertEquals(
            ScenarioScriptPresentationRequest.UnitHighlight(149),
            runtime.stage.consumeScriptPresentationRequest(),
        )
        assertEquals(PlaybackState.DELAY, runtime.state)
        runtime.resumeExternalDelay()
        assertEquals(
            ScenarioScriptPresentationRequest.RectangleHighlight(16, 0, 19, 0),
            runtime.stage.consumeScriptPresentationRequest(),
        )
        runtime.resumeExternalDelay()
        assertFalse("stage.unit().heightLight" in runtime.unhandledCalls)
        assertFalse("stage.heightLight" in runtime.unhandledCalls)
    }

    @Test
    fun `S00 battle getItem preserves selector action and source InfoLayer message`() {
        val runtime = ScenarioInterpreter.load("S_00")
        runtime.enableExternalBattlePresentation()
        runtime.setScriptVariables((0..100).associateWith { 1 } + (20 to 0))
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(
                round = 2,
                camp = 0,
                positionsByCamp = mapOf(0 to listOf(13 to 12)),
            ),
        )

        runtime.start("scene1")

        val request = assertIs<ScenarioScriptPresentationRequest.GetItem>(
            runtime.stage.consumeScriptPresentationRequest(),
        )
        assertEquals(150, request.itemId)
        assertEquals(1025, request.unitSelector)
        assertEquals(5, request.action)
        assertTrue(request.addToInventory)
        assertTrue(request.completionMessage.startsWith("얻었다"))
        assertTrue(request.completionMessage.endsWith("!"))
        assertEquals(PlaybackState.DELAY, runtime.state)
        runtime.presentExternalBattleInfo(request.completionMessage)
        assertEquals(PlaybackState.MODAL, runtime.state)
        runtime.resumeModal()
        assertEquals(PlaybackState.COMPLETE, runtime.state)
        assertFalse("stage.getItem" in runtime.unhandledCalls)
    }

    @Test
    fun `battle getItem false store flag presents without mutating inventory`() {
        val campaign = CampaignState()
        val stage = ScenarioStage(campaign)

        stage.getItem(150, 3, addToInventory = false)

        assertEquals(emptyMap(), campaign.inventory.items)
        assertEquals(emptyList(), stage.acquiredItems)
    }

    @Test
    fun `battle loadBg keeps map and following mutations behind its resource callback`() {
        val campaign = CampaignState().apply { globalVariables[4051] = 1 }
        val runtime = ScenarioInterpreter.load("S_00", campaign)
        runtime.enableExternalBattlePresentation()

        runtime.start("scene0")
        assertTrue(runtime.hasPendingBattleBackgroundLoad)
        assertEquals(100, runtime.requestedBattleBackgroundMapIndex)
        assertEquals(0, runtime.stage.battleMapIndex)
        assertEquals(0, campaign.globalVariables[4051])
        // A generic delay skip must not leap over a failed/pending image load.
        runtime.skipDelay()
        assertTrue(runtime.hasPendingBattleBackgroundLoad)
        assertNull(runtime.stage.consumeScriptPresentationRequest())

        runtime.completeBattleBackgroundLoad()
        assertFalse(runtime.hasPendingBattleBackgroundLoad)
        assertEquals(100, runtime.stage.battleMapIndex)

        val request = assertIs<ScenarioScriptPresentationRequest.UnitStatusSettlement>(
            runtime.stage.consumeScriptPresentationRequest(),
        )
        assertEquals(listOf(234, 235, 334), request.values.map {
            (it["unit"] as ScenarioInterpreter.UnitReference).id
        })
        assertEquals(PlaybackState.DELAY, runtime.state)
        runtime.resumeExternalDelay()
        // scene0's next statement is stage.delay(5), so the callback cannot
        // skip or merge that independent half-second source barrier.
        assertEquals(PlaybackState.DELAY, runtime.state)
        assertNull(runtime.stage.consumeScriptPresentationRequest())
    }

    @Test
    fun `S26 setObject emits the visible gate callback contract after draw`() {
        val runtime = ScenarioInterpreter.load("S_26")
        runtime.enableExternalBattlePresentation()
        runtime.stage.drawBattle()
        runtime.setScriptVariables(
            (0..100).associateWith { 1 } + mapOf(35 to 0, 56 to 0),
        )
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(
                round = 1,
                camp = 0,
                attributes = mapOf(33 to mapOf(7 to 0)),
            ),
        )

        runtime.start("scene1")
        runtime.advanceDialogue()
        runtime.advanceDialogue()
        assertEquals(PlaybackState.DELAY, runtime.state)
        assertEquals(33, runtime.stage.consumeUnitHideRequest()?.unitId)
        runtime.resumeExternalDelay()

        assertEquals(
            ScenarioScriptPresentationRequest.MapObjects(
                enabled = true,
                terrainId = 12,
                objects = listOf(ScenarioScriptPresentationRequest.MapObjects.Object(22, 11, 18)),
                soundOnFirstObjectOnly = true,
            ),
            runtime.stage.consumeScriptPresentationRequest(),
        )
        assertEquals(PlaybackState.DELAY, runtime.state)
        runtime.resumeExternalDelay()
        assertEquals(PlaybackState.DIALOGUE, runtime.state)
        assertFalse("stage.setObject" in runtime.unhandledCalls)
    }

    @Test
    fun `S00 stage info pauses before battle outcome and resumes only on Info close`() {
        val runtime = ScenarioInterpreter.load("S_00")
        runtime.setScriptVariables((0..100).associateWith { 1 } + (1 to 0))
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(round = 2, camp = 0, playerDefeated = true),
        )

        runtime.start("scene1")

        assertEquals(PlaybackState.MODAL, runtime.state)
        assertEquals(ScenarioInterpreter.ModalKind.INFO, runtime.currentModalKind)
        assertEquals("조조 황번군에게 패배했다.", runtime.currentModalText)
        assertNull(runtime.stage.scriptedBattleOutcome)
        assertFalse(runtime.stage.battleEndedByScript)

        runtime.resumeModal()

        assertEquals(PlaybackState.COMPLETE, runtime.state)
        assertEquals(BattleOutcome.ENEMY_VICTORY, runtime.stage.scriptedBattleOutcome)
        assertTrue(runtime.stage.battleEndedByScript)
        assertFalse("stage.info" in runtime.unhandledCalls)
    }

    @Test
    fun `stage info INFO_CTRL branch is synchronous and consumes its one-shot global`() {
        val campaign = CampaignState().also { it.globalVariables[4071] = 9 }
        val runtime = ScenarioInterpreter.load("S_00", campaign)
        runtime.setScriptVariables((0..100).associateWith { 1 } + (1 to 0))
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(round = 2, camp = 0, playerDefeated = true),
        )

        runtime.start("scene1")

        assertEquals(PlaybackState.COMPLETE, runtime.state)
        assertEquals(listOf(9 to "조조 황번군에게 패배했다."), runtime.stage.controlledInfos)
        assertFalse(4071 in campaign.globalVariables)
        assertEquals(BattleOutcome.ENEMY_VICTORY, runtime.stage.scriptedBattleOutcome)
    }

    @Test
    fun `battle info keeps over-100-character pages in one modal FIFO`() {
        val runtime = ScenarioInterpreter.load("S_00")
        runtime.start("scene0")
        assertEquals(PlaybackState.DELAY, runtime.state)
        val first = "가".repeat(101)
        runtime.presentExternalBattleInfo("$first\n다음 페이지", postTypingDelaySeconds = 3f)
        assertEquals(PlaybackState.MODAL, runtime.state)
        assertEquals(first, runtime.currentModalText)
        runtime.resumeModal()
        assertEquals(PlaybackState.MODAL, runtime.state)
        assertEquals("다음 페이지", runtime.currentModalText)
        runtime.completeModalTyping()
        runtime.update(2.9f)
        assertEquals(PlaybackState.MODAL, runtime.state, "작성된 delay=3이 후속 페이지에도 유지된다")
    }

    @Test
    fun `live modal timers honor source auto close setting`() {
        assertEquals(false, ScenarioInterpreter.modalMayAutoClose(ScenarioInterpreter.ModalKind.SECTION, "서막", false))
        assertEquals(false, ScenarioInterpreter.modalMayAutoClose(ScenarioInterpreter.ModalKind.MAP_INFO, "긴 지도 정보", false))
        assertEquals(false, ScenarioInterpreter.modalMayAutoClose(ScenarioInterpreter.ModalKind.INFO, "열 글자가 넘는 안내 문장", false))
        assertEquals(true, ScenarioInterpreter.modalMayAutoClose(ScenarioInterpreter.ModalKind.INFO, "영천", false))
        assertEquals(true, ScenarioInterpreter.modalMayAutoClose(ScenarioInterpreter.ModalKind.SECTION, "서막", true))
        assertEquals(true, ScenarioInterpreter.modalMayAutoClose(ScenarioInterpreter.ModalKind.AMBITION, null, false))
    }

    @Test
    fun `hall walking direction follows coordinate delta and restarts clip on a turn`() {
        val stage = ScenarioStage()
        stage.apply(ScenarioCommand.ShowUnit(0, 10, 10, 0))
        stage.apply(ScenarioCommand.MoveUnit(0, 12, 12, 2))
        val unit = stage.unit(0)

        // Source _move2 keeps _x/_y at the origin until its final callFunc;
        // only the rendered node traverses the path in the meantime.
        assertEquals(10 to 10, unit.x to unit.y)
        assertEquals(1, unit.direction)
        // The move call itself is observable before updateAnimations: the
        // command is emitted by runUntilInput after that render's animation
        // update.  The next render must advance both its script delay and its
        // move action by the same delta.
        assertEquals(10f, unit.visualX)
        assertEquals(10f, unit.visualY)
        stage.updateAnimations(0.02f)
        assertTrue(unit.visualX > 10f)
        assertEquals(10 to 10, unit.x to unit.y)
        assertEquals(1, unit.direction)
        assertEquals(0.02f, unit.animationElapsed, 0.001f)

        // The stable A* neighbor order reaches x first and then y. Source
        // setAction starts anime20_2 again when this corner is crossed.
        stage.updateAnimations(0.07f)
        assertEquals(2, unit.direction)
        assertEquals(0f, unit.animationElapsed, 0.001f)
        stage.updateAnimations(0.01f)
        assertEquals(0.01f, unit.animationElapsed, 0.001f)
        assertEquals(10 to 10, unit.x to unit.y)
        stage.updateAnimations(1f)
        assertEquals(12 to 12, unit.x to unit.y)
    }

    @Test
    fun `battle scripted movement retains source move2 duration and final callback hold`() {
        val stage = ScenarioStage()
        stage.enableBattleMovementTimeline()
        stage.apply(ScenarioCommand.ShowUnit(0, 7, 0, 1))

        // Three orthogonal route edges: 3 * .08 plus move2's final .1 delay.
        assertEquals(0.34f, stage.moveDuration(0, 8, 2), 0.001f)
        stage.apply(ScenarioCommand.MoveUnit(0, 8, 2, 2))
        stage.updateAnimations(0.3f)
        assertEquals(7 to 0, stage.unit(0).let { it.x to it.y })
        stage.updateAnimations(0.04f)
        assertEquals(8 to 2, stage.unit(0).let { it.x to it.y })
    }

    @Test
    fun `battle scripted movement consumes resolved path endpoint and commits only after callback hold`() {
        val stage = ScenarioStage()
        stage.enableBattleMovementTimeline()
        stage.apply(ScenarioCommand.ShowUnit(0, 7, 0, 1))
        stage.setBattleMovePathResolver { id, x, y ->
            assertEquals(0, id)
            assertEquals(10 to 6, x to y)
            listOf(7 to 0, 7 to 1, 8 to 1, 8 to 2)
        }

        assertEquals(0.34f, stage.moveDuration(0, 10, 6), 0.001f)
        stage.apply(ScenarioCommand.MoveUnit(0, 10, 6, 2))
        stage.updateAnimations(0.3f)
        assertEquals(7 to 0, stage.unit(0).let { it.x to it.y })
        stage.updateAnimations(0.04f)
        assertEquals(8 to 2, stage.unit(0).let { it.x to it.y })
    }

    @Test
    fun `script delay duration cannot complete before scripted move logical tile commit`() {
        val stage = ScenarioStage()
        stage.enableBattleMovementTimeline()
        stage.apply(ScenarioCommand.ShowUnit(33, 13, 1, 2))
        stage.setBattleMovePathResolver { _, _, _ ->
            listOf(13 to 1, 13 to 2, 13 to 3, 13 to 4, 12 to 4, 12 to 5)
        }

        val callbackDelay = stage.moveDuration(33, 12, 5)
        stage.apply(ScenarioCommand.MoveUnit(33, 12, 5, 2))

        // runUntilInput emits the move after the current render's animation
        // update, so its initial action remains observable without consuming
        // an extra frame from the callback delay.
        assertEquals(20, stage.unit(33).action)
        assertEquals(13 to 1, stage.unit(33).let { it.x to it.y })
        stage.updateAnimations(callbackDelay)
        assertEquals(12 to 5, stage.unit(33).let { it.x to it.y })
        assertEquals(0, stage.unit(33).action)
    }

    @Test
    fun `hidden battle unit move is source isExist no-op`() {
        val stage = ScenarioStage()
        stage.enableBattleMovementTimeline()
        stage.apply(ScenarioCommand.ShowUnit(0, 7, 0, 1))
        stage.setBattleUnitVisibility(0, false)
        var resolverCalls = 0
        stage.setBattleMovePathResolver { _, _, _ ->
            resolverCalls += 1
            listOf(7 to 0, 8 to 0)
        }

        assertEquals(0f, stage.moveDuration(0, 10, 6))
        stage.apply(ScenarioCommand.MoveUnit(0, 10, 6, 2))

        assertEquals(0, resolverCalls)
        assertEquals(7 to 0, stage.unit(0).let { it.x to it.y })
        assertEquals(1, stage.unit(0).direction)
        assertEquals(0, stage.unit(0).action)
        assertEquals(0f, stage.unit(0).moveDuration)
    }

    @Test
    fun `choice retains source face unit argument`() {
        val playback = ScenarioPlayback(
            ScenarioTimeline(
                "test",
                listOf(ScenarioCommand.Choose(Choice(listOf("첫째", "둘째"), 0))),
            ),
        )
        assertEquals(0, playback.currentChoice?.faceId)
    }

    @Test
    fun `recovered Tool random LCG preserves inclusive model random range`() {
        val (seedOne, valueOne) = ScenarioInterpreter.toolRandomFromSeed(0.0)
        val (seedTwo, valueTwo) = ScenarioInterpreter.toolRandomFromSeed(seedOne)

        assertEquals(49297.0, seedOne)
        assertEquals(42, valueOne)
        assertEquals(41, valueTwo)
        assertTrue(valueOne in 0..100)
        assertTrue(valueTwo in 0..100)
        assertTrue(seedTwo in 0.0..<233280.0)
    }

    @Test
    fun `new campaign reset clears every persistent model collection`() {
        val campaign = CampaignState()
        campaign.globalVariables[8] = 1
        campaign.setUnitAttribute(0, 17, 12)
        campaign.joinedUnits += 0
        campaign.inventory.addItem(12)
        campaign.roster.restoreBattleRoster(listOf(0))
        campaign.inventory.setEquipment(0, 2, 1, 2, 1, 2)
        campaign.applyInfoTransfer(22, "3")

        campaign.reset()

        assertEquals(emptyMap(), campaign.globalVariables)
        assertEquals(emptySet(), campaign.joinedUnits)
        assertEquals(emptyMap(), campaign.inventory.items)
        assertEquals(emptyList(), campaign.roster.battleRoster)
        assertEquals(emptyMap(), campaign.inventory.equipment)
        assertNull(campaign.endingId)
    }

    @Test
    fun `equip layer all unload returns every joined unit slot to inventory`() {
        val campaign = CampaignState()
        campaign.joinedUnits += listOf(0, 157)
        campaign.inventory.setEquipment(0, 2, 3, 2, 4, 2)
        campaign.inventory.setEquipment(157, 3, 5, 3, 6, 3)

        assertEquals(6, campaign.inventory.unequipAllEquipment())
        assertEquals(CampaignEquipment(1, 1, 1, 1, 1), campaign.inventory.equipment[0])
        assertEquals(CampaignEquipment(1, 1, 1, 1, 1), campaign.inventory.equipment[157])
        assertEquals(mapOf(0 to 1, 1 to 1, 70 to 1, 71 to 1, 109 to 1, 110 to 1), campaign.inventory.items)
        assertEquals(listOf(3), campaign.inventory.itemLevels(0))
        assertEquals(listOf(4), campaign.inventory.itemLevels(70))
        assertEquals(0, campaign.inventory.unequipAllEquipment())
    }

    @Test
    fun `source dialogue reveal consumes rich text tags without displaying markup`() {
        val reveal = SourceTextReveal()
        reveal.update("<color=#ff0000>가</c>나", 0.04f)
        assertEquals("", reveal.visibleText)

        reveal.update("<color=#ff0000>가</c>나", 0.04f)
        assertEquals("가", reveal.visibleText)
        assertEquals(false, reveal.isComplete)

        assertEquals(true, reveal.revealAllIfPending())
        assertEquals("가나", reveal.visibleText)
        assertEquals(true, reveal.isComplete)
        assertEquals(false, reveal.revealAllIfPending())
    }

    @Test
    fun `stage preserves scripted portrait and fire state`() {
        val stage = ScenarioStage()

        stage.showHead(119, 408, 76)
        stage.hideHead(119)
        stage.setFires(true, listOf(listOf(9, 7), listOf(10, 7)))
        stage.setFire(false, 10, 7)
        stage.setSection(4, "마왕이 부활하다")

        assertEquals(408, stage.heads.getValue(119).x)
        assertEquals(false, stage.heads.getValue(119).visible)
        assertEquals(true, stage.fires.getValue(9 to 7).enabled)
        assertEquals(false, stage.fires.getValue(10 to 7).enabled)
        assertEquals(4 to "마왕이 부활하다", stage.section)
    }

    @Test
    fun `map presentation request preserves exact center and callback barrier metadata`() {
        val stage = ScenarioStage()
        val fire = ScenarioMapPresentationRequest(13, 5, 1f)
        stage.requestMapPresentation(fire)

        assertEquals(fire, stage.consumeMapPresentationRequest())
        assertEquals(null, stage.consumeMapPresentationRequest())

        val magic = ScenarioMapPresentationRequest(10, 8, 1.25f, magicCallId = 5)
        stage.requestMapPresentation(magic)
        assertEquals(magic, stage.consumeMapPresentationRequest())
    }

    @Test
    fun `head presentation preserves source fade and euclidean move timing`() {
        val stage = ScenarioStage()

        assertEquals(1f, stage.showHead(119, 100, 100))
        assertEquals(0f, stage.heads.getValue(119).opacity)
        stage.updateAnimations(0.5f)
        assertEquals(0.5f, stage.heads.getValue(119).opacity, 0.001f)
        stage.updateAnimations(0.5f)

        // 3-4-5 distance maps to Head.move's 0.01 * distance seconds.
        assertEquals(0.05f, stage.moveHead(119, 103, 104), 0.001f)
        stage.updateAnimations(0.025f)
        assertEquals(101.5f, stage.heads.getValue(119).visualX, 0.001f)
        assertEquals(102f, stage.heads.getValue(119).visualY, 0.001f)

        assertEquals(1f, stage.hideHead(119))
        stage.updateAnimations(0.5f)
        assertEquals(0.5f, stage.heads.getValue(119).opacity, 0.001f)
        assertEquals(false, stage.heads.getValue(119).visible)
    }

    @Test
    fun `stage effect sound preserves original replacement lifecycle`() {
        val stage = ScenarioStage()

        stage.effectSound(105)
        stage.effectSound(127)
        stage.effectSound(127, 0)

        assertEquals(
            listOf(
                ScenarioSoundEffect(105, 1),
                ScenarioSoundEffect(105, 0),
                ScenarioSoundEffect(127, 1),
                ScenarioSoundEffect(127, 0),
            ),
            stage.consumeSoundEffects(),
        )
    }

    @Test
    fun `scripted reward and loss retain non annihilation battle outcomes`() {
        val stage = ScenarioStage()
        stage.reward()
        stage.endBattle()
        assertEquals(BattleOutcome.PLAYER_VICTORY, stage.scriptedBattleOutcome)
        assertEquals(true, stage.battleEndedByScript)

        stage.lose()
        assertEquals(BattleOutcome.ENEMY_VICTORY, stage.scriptedBattleOutcome)
    }

    @Test
    fun `stage item rewards persist in the shared campaign inventory`() {
        val campaign = CampaignState()
        val stage = ScenarioStage(campaign)

        stage.getItem(150)
        stage.getItem(150)
        stage.getItem(81)

        assertEquals(listOf(150, 150, 81), stage.acquiredItems)
        assertEquals(2, campaign.inventory.items[150])
        assertEquals(1, campaign.inventory.items[81])
    }

    @Test
    fun `battle property consumes original inventory and heals an adjacent ally`() {
        val inventory = linkedMapOf(150 to 1)
        val state = Battle(
            units = listOf(
                BattleUnit("user", "사용자", Faction.PLAYER, 0, 0),
                BattleUnit("target", "대상", Faction.PLAYER, 1, 0, hitPoints = 40, maxHitPoints = 100),
                BattleUnit("enemy", "적", Faction.ENEMY, 4, 4),
            ),
            events = emptyList(),
            propertyItems = mapOf(150 to BattlePropertyItem(150, "회복약", 26, 35)),
            consumeProperty = { id -> (inventory[id] ?: 0).let { count ->
                if (count < 1) false else { if (count == 1) inventory.remove(id) else inventory[id] = count - 1; true }
            } },
        )

        assertEquals(TacticalActionResult.Item("회복약", "target", "HP 35 회복"), state.useProperty("user", "target", 150))
        assertEquals(75, state.units.getValue("target").hitPoints)
        // BattleUnit.setCurHp is the only mutation route: its bar must be
        // synchronized after a BattleScreen._usePro2-style recovery too.
        assertEquals(.75f, state.units.getValue("target").presentation.hpBarProgress)
        assertEquals(null, inventory[150])
    }

    @Test
    fun `movement overlay uses the same weighted reachable tiles as move validation`() {
        val state = Battle(
            units = listOf(
                BattleUnit("unit", "이동자", Faction.PLAYER, 2, 2, movement = 2),
                BattleUnit("occupied", "점유", Faction.PLAYER, 3, 2),
                BattleUnit("enemy", "적", Faction.ENEMY, 8, 8),
            ),
            events = emptyList(),
            blockedTiles = setOf(2 to 3),
        )

        val area = state.reachableTiles("unit")

        assertEquals(1, area[2 to 1])
        assertEquals(2, area[1 to 1])
        // BattleScreen's psHash/overlay retains same-camp occupied routing
        // nodes.  `moveUnit` is the separate destination gate and rejects
        // the occupied point.
        assertEquals(true, 3 to 2 in area)
        assertEquals(false, 2 to 3 in area)
        assertIs<TacticalActionResult.Success>(state.moveUnit("unit", 1, 1))
    }

    @Test
    fun `guaranteed critical skill applies to physical attacks`() {
        val state = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, attack = 80, skills = mapOf(92 to 0, 270 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500, defense = 1),
            ),
            events = emptyList(),
        )

        assertEquals(true, (state.attack("attacker", "target") as TacticalActionResult.Attack).critical)
    }

    @Test
    fun `physical and remote damage resistance use original percentage rates`() {
        val physical = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500, skills = mapOf(118 to 50)),
            ), events = emptyList(),
        )
        assertEquals(50, (physical.attack("attacker", "target", damage = 100) as TacticalActionResult.Attack).damage)
    }

    @Test
    fun `physical damage cap is applied before an original forced critical`() {
        val state = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, attack = 1_000, skills = mapOf(92 to 0, 270 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, defense = 1, hitPoints = 500, maxHitPoints = 500, skills = mapOf(242 to 30)),
            ), events = emptyList(),
        )
        val result = state.attack("attacker", "target") as TacticalActionResult.Attack
        assertEquals(54, result.damage)
        assertEquals(true, result.critical)
    }

    @Test
    fun `original flat physical skill additions are resolved before damage floor`() {
        val state = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, hitPoints = 100, maxHitPoints = 100, spirit = 50, attack = 40, skills = mapOf(92 to 0, 9 to 10, 141 to 20, 80 to 25, 95 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, magicPoints = 50, level = 1, skills = mapOf(95 to 0)),
            ), events = emptyList(),
        )
        // 100 + BIAO_HAN 10 + LRHY 10 + FZZS_ATT 10 + GDZS(+15/-15).
        assertEquals(130, (state.attack("attacker", "target", damage = 100) as TacticalActionResult.Attack).damage)
    }

    @Test
    fun `scripted forced attacks use the same original physical skill order`() {
        val state = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, attack = 1, hitPoints = 50, maxHitPoints = 100, skills = mapOf(92 to 0, 174 to 30, 238 to 40)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, defense = 100, hitPoints = 500, maxHitPoints = 500, skills = mapOf(118 to 50)),
            ), events = emptyList(),
        )
        val result = state.forcedAttack("attacker", "target") as TacticalActionResult.Attack
        assertEquals(150, result.damage)
        assertEquals(50, result.lifeStealHealing)
    }

    @Test
    fun `scripted forced attacks notify physical and defeat rewards`() {
        var physicalDamage = 0
        var defeats = 0
        val state = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, attack = 100, skills = mapOf(92 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, defense = 1, hitPoints = 1, maxHitPoints = 1),
            ),
            events = emptyList(),
            onPhysicalDamage = { _, _, amount -> physicalDamage += amount },
            onUnitDefeated = { _, _ -> defeats++ },
        )

        state.forcedAttack("attacker", "target")

        assertEquals(true, physicalDamage > 0)
        assertEquals(1, defeats)
    }

    @Test
    fun `original continuous attack makes a second reduced damage hit`() {
        val state = Battle(
            units = listOf(
                BattleUnit("attacker", "연격", Faction.PLAYER, 0, 0, attack = 100, skills = mapOf(92 to 0, 226 to 0, 276 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, defense = 1, hitPoints = 500, maxHitPoints = 500),
            ), events = emptyList(),
        )
        val result = state.attack("attacker", "target") as TacticalActionResult.Attack
        assertEquals(75, result.damage)
        assertEquals(56, result.followUpDamage)
        assertEquals(369, state.units.getValue("target").hitPoints)
    }

    @Test
    fun `original counter continuous attack makes its own reduced second hit`() {
        val state = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, attack = 45, defense = 1, critical = 1, morale = 100, hitPoints = 500, maxHitPoints = 500, skills = mapOf(92 to 0)),
                BattleUnit("target", "반격자", Faction.ENEMY, 1, 0, attack = 100, defense = 25, critical = 100, morale = 1, hitPoints = 500, maxHitPoints = 500, skills = mapOf(43 to 0)),
            ), events = emptyList(),
        )
        val result = state.attack("attacker", "target") as TacticalActionResult.Attack
        // BattleScreen._attack6 starts with FAN_JI (75%); its FJBDSJ
        // follow-up adds LIANJI as well, for another -25% (50%).
        assertEquals(56, result.counterDamage)
        assertEquals(37, result.counterFollowUpDamage)
        assertEquals(407, state.units.getValue("attacker").hitPoints)
    }

    @Test
    fun `original strategy condition skills bypass weather and special gates`() {
        val clearOnly = GameDataCatalog.MagicProfile(1, "청명", 0, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), 0, emptySet(), 0, 100, 0, 0, condition = 3)
        val special = clearOnly.copy(id = 2, condition = 5)
        val rain = clearOnly.copy(id = 58, target = 2)
        fun battle(skills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magic = listOf(rain, clearOnly, special), skills = skills),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0),
            ), events = emptyList(),
        ).also { it.castMagic("caster", "target", 58); it.units.getValue("caster").hasActed = false }
        assertIs<TacticalActionResult.Rejected>(battle(emptyMap()).castMagic("caster", "target", 1))
        assertIs<TacticalActionResult.Magic>(battle(mapOf(20 to 0)).castMagic("caster", "target", 1))
        assertIs<TacticalActionResult.Rejected>(battle(emptyMap()).castMagic("caster", "target", 2))
        assertIs<TacticalActionResult.Magic>(battle(mapOf(136 to 0)).castMagic("caster", "target", 2))
    }

    @Test
    fun `original unit restraint skills override and adjust arm matchup`() {
        fun damage(attackerSkills: Map<Int, Int>, targetSkills: Map<Int, Int>): Int {
            val state = Battle(
                units = listOf(
                    BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0) + attackerSkills, armRestraints = mapOf(9 to 100)),
                    BattleUnit("target", "대상", Faction.ENEMY, 1, 0, armId = 9, hitPoints = 500, maxHitPoints = 500, defense = 1, skills = targetSkills),
                ), events = emptyList(),
            )
            return (state.attack("attacker", "target") as TacticalActionResult.Attack).damage
        }

        assertEquals(1.3, damage(mapOf(316 to 0), emptyMap()).toDouble() / damage(emptyMap(), emptyMap()), 0.02)
        assertEquals(0.7, damage(emptyMap(), mapOf(316 to 0)).toDouble() / damage(emptyMap(), emptyMap()), 0.02)
        assertEquals(1.2, damage(mapOf(133 to 20), emptyMap()).toDouble() / damage(emptyMap(), emptyMap()), 0.02)
    }

    @Test
    fun `create mine resolves source slot indices through the Hall resolved campaign roster`() {
        val campaign = CampaignState()
        campaign.joinedUnits += listOf(0, 6, 10)
        val stage = ScenarioStage(campaign)
        stage.setJoinBattle(1, 3, listOf(6), emptyList())

        stage.createBattleUnits(ScenarioUnitFaction.MINE, listOf(mapOf("idx" to 0, "x" to 4, "y" to 5, "i" to 0)))

        assertEquals(0, stage.battleUnits.getValue("MINE:0").characterId)
        assertEquals(listOf(0, 6, 10), campaign.roster.battleRoster)
    }

    @Test
    fun `Hall and StartBattle resolve R01 to four through seven with Cao Cao and Xiahou Dun mandatory`() {
        val campaign = CampaignState().also {
            it.joinedUnits += listOf(0, 1, 6, 10, 11, 5, 12)
        }

        val plan = campaign.roster.configureBattleRoster(ScenarioJoinBattleLimit(1, 7, listOf(1), emptyList()))

        assertEquals(4, plan.selectionLimit.minimum)
        assertEquals(7, plan.selectionLimit.maximum)
        assertEquals(listOf(0, 1), plan.selectionLimit.requiredUnitIds)
        assertEquals(null, plan.directBattleRoster)
        assertEquals(listOf(0, 1, 6, 10, 11, 5, 12), campaign.roster.battleRoster)
    }

    @Test
    fun `battle entry removes excluded mandatory conflicts and caps UI roster at twenty`() {
        val campaign = CampaignState().also { state ->
            state.joinedUnits += (0..24)
        }

        val plan = campaign.roster.resolveBattleEntry(
            ScenarioJoinBattleLimit(1, 30, listOf(0, 1, 24, 99), listOf(0, 1)),
        )

        assertEquals(12, plan.selectionLimit.minimum)
        assertEquals(20, plan.selectionLimit.maximum)
        assertEquals(listOf(24), plan.selectionLimit.requiredUnitIds)
        assertEquals(listOf(0, 1), plan.selectionLimit.excludedUnitIds)
        assertEquals(null, plan.directBattleRoster)
    }

    @Test
    fun `availability clamp does not trigger the raw mandatory fast path`() {
        val campaign = CampaignState().also { it.joinedUnits += listOf(0, 1) }

        val plan = campaign.roster.configureBattleRoster(ScenarioJoinBattleLimit(1, 7, listOf(1), emptyList()))

        assertEquals(1, plan.selectionLimit.minimum)
        assertEquals(2, plan.selectionLimit.maximum)
        assertEquals(null, plan.directBattleRoster)
        assertEquals(listOf(0, 1), campaign.roster.battleRoster)
    }

    @Test
    fun `Hall bypasses preparation when mandatory units fill the raw authored maximum`() {
        val campaign = CampaignState().also { it.joinedUnits += listOf(0, 1, 6) }

        val plan = campaign.roster.configureBattleRoster(ScenarioJoinBattleLimit(1, 2, listOf(1), emptyList()))

        assertEquals(listOf(0, 1), plan.directBattleRoster)
        assertEquals(listOf(0, 1), campaign.roster.battleRoster)
    }

    @Test
    fun `create mine never fabricates units for absent source roster slots`() {
        val emptyStage = ScenarioStage(CampaignState())
        emptyStage.createBattleUnits(ScenarioUnitFaction.MINE, listOf(mapOf("idx" to 0, "x" to 7, "dir" to 1, "hide" to 1)))
        assertEquals(emptyMap(), emptyStage.battleUnits)

        val campaign = CampaignState().also { it.roster.restoreBattleRoster(listOf(157)) }
        val stage = ScenarioStage(campaign)
        // The source accepts x-only hidden Mine slots during scene0 and
        // supplies the missing y as its runtime default (zero).
        stage.createBattleUnits(ScenarioUnitFaction.MINE, listOf(mapOf("idx" to 0, "x" to 7, "dir" to 1, "hide" to 1)))

        assertEquals(157, stage.battleUnits.getValue("MINE:0").characterId)
        assertEquals(7, stage.battleUnits.getValue("MINE:0").x)
        assertEquals(0, stage.battleUnits.getValue("MINE:0").y)
        assertEquals(true, stage.battleUnits.getValue("MINE:0").hidden)
        assertEquals(7 to 0, stage.unit(157).let { it.x to it.y })
        assertEquals(7f to 0f, stage.unit(157).let { it.visualX to it.visualY })
    }

    @Test
    fun `battle rectangle AI retains original aggregate camps and targets`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(ScenarioUnitFaction.MINE, listOf(mapOf("id" to 0, "i" to 0, "x" to 1, "y" to 2)))
        stage.createBattleUnits(ScenarioUnitFaction.FRIEND, listOf(mapOf("id" to 3, "i" to 0, "x" to 2, "y" to 2)))
        stage.createBattleUnits(ScenarioUnitFaction.ENEMY, listOf(mapOf("id" to 4, "i" to 0, "x" to 3, "y" to 2)))
        stage.setBattleAi(camp = 4, x1 = 0, y1 = 0, x2 = 4, y2 = 4, ai = 4, targetId = 99, targetX = 8, targetY = 9)

        listOf("MINE:0", "FRIEND:0").forEach { key ->
            stage.battleUnits.getValue(key).also {
                assertEquals(4, it.ai)
                assertEquals(99, it.aiTargetId)
                assertEquals(8, it.aiTargetX)
                assertEquals(9, it.aiTargetY)
            }
        }
        // Omitted source AI defaults to BattleScreen's hold-position value;
        // most importantly it was not overwritten by camp 4.
        assertEquals(2, stage.battleUnits.getValue("ENEMY:0").ai)
    }

    @Test
    fun `battle object state keeps source type separate from terrain overlay`() {
        val stage = ScenarioStage()
        // stage.setObjects(True, 27, [[1, 4, 8]]) in the source: 1 is the
        // object type while 27 is its terrain replacement.
        stage.setMapObjects(true, 27, listOf(listOf(1, 4, 8)))
        val objectState = stage.mapObjects.getValue(4 to 8)

        assertEquals(1, objectState.objectId)
        assertEquals(27, objectState.terrainId)
        assertEquals(true, objectState.enabled)

        stage.setMapObjects(false, 13, listOf(listOf(1, 4, 8)))
        assertEquals(false, stage.mapObjects.getValue(4 to 8).enabled)
        assertEquals(13, stage.mapObjects.getValue(4 to 8).terrainId)
        assertEquals(
            listOf(
                ScenarioMapObjectsCall(true, 27, listOf(ScenarioMapObjectsCall.Object(1, 4, 8))),
                ScenarioMapObjectsCall(false, 13, listOf(ScenarioMapObjectsCall.Object(1, 4, 8))),
            ),
            stage.mapObjectsCalls,
        )
    }

    @Test
    fun `S52 pre-draw setObjects calls retain their exact authored prefix`() {
        val runtime = ScenarioInterpreter.load("S_52")
        runtime.enableExternalBattlePresentation()
        runtime.start("scene0")
        assertTrue(runtime.hasPendingBattleBackgroundLoad)

        runtime.completeBattleBackgroundLoad()

        assertEquals(
            listOf(
                ScenarioMapObjectsCall(true, 17, listOf(ScenarioMapObjectsCall.Object(61, 7, 15), ScenarioMapObjectsCall.Object(62, 7, 16))),
                ScenarioMapObjectsCall(true, 17, listOf(ScenarioMapObjectsCall.Object(69, 4, 8), ScenarioMapObjectsCall.Object(70, 5, 8))),
                ScenarioMapObjectsCall(true, 17, listOf(ScenarioMapObjectsCall.Object(67, 7, 10), ScenarioMapObjectsCall.Object(68, 7, 11))),
                ScenarioMapObjectsCall(true, 17, listOf(ScenarioMapObjectsCall.Object(65, 12, 15), ScenarioMapObjectsCall.Object(66, 12, 16))),
                ScenarioMapObjectsCall(true, 17, listOf(ScenarioMapObjectsCall.Object(71, 12, 5), ScenarioMapObjectsCall.Object(72, 12, 6))),
                ScenarioMapObjectsCall(true, 17, listOf(ScenarioMapObjectsCall.Object(73, 14, 3), ScenarioMapObjectsCall.Object(74, 15, 3))),
            ),
            runtime.stage.mapObjectsCalls.take(6),
        )
    }

    @Test
    fun `terrain objects dynamically replace and restore map terrain`() {
        val grid = BattleTerrainGrid(width = 2, height = 1, rows = listOf(intArrayOf(6, 7)))
        grid.applyObjectOverlays(listOf(ScenarioMapObject(0, 0, objectId = 1, terrainId = 27, enabled = true)))
        assertEquals(27, grid.terrainAt(0, 0))

        grid.applyObjectOverlays(listOf(ScenarioMapObject(0, 0, objectId = 1, terrainId = 27, enabled = false)))
        assertEquals(6, grid.terrainAt(0, 0))
    }

    @Test
    fun `scripted fire overlays terrain after ordinary object overlays`() {
        val grid = BattleTerrainGrid(width = 1, height = 1, rows = listOf(intArrayOf(6)))
        grid.applyObjectOverlays(listOf(ScenarioMapObject(0, 0, objectId = 1, terrainId = 27, enabled = true)))
        grid.applyFires(listOf(ScenarioFire(0, 0, enabled = true)))
        assertEquals(BattleTerrainGrid.FIRE_TERRAIN_ID, grid.terrainAt(0, 0))

        grid.resetOverlays()
        grid.applyObjectOverlays(listOf(ScenarioMapObject(0, 0, objectId = 1, terrainId = 27, enabled = true)))
        grid.applyFires(listOf(ScenarioFire(0, 0, enabled = false)))
        assertEquals(27, grid.terrainAt(0, 0))
    }

    @Test
    fun `scripted gates can change tactical passability after battle start`() {
        val state = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 3, 0),
            ),
            events = emptyList(),
            blockedTiles = setOf(1 to 0),
        )
        assertIs<TacticalActionResult.Rejected>(state.moveUnit("player", 1, 0))

        state.setBlockedTiles(emptySet())
        assertEquals(TacticalActionResult.Success, state.moveUnit("player", 1, 0))
    }

    @Test
    fun `battle roster requires mandatory units and honors source roster limits`() {
        val campaign = CampaignState()
        campaign.joinedUnits += listOf(0, 6, 10)
        val limit = ScenarioJoinBattleLimit(2, 2, listOf(6), emptyList())

        assertEquals(false, campaign.roster.setBattleRoster(listOf(0, 10), limit))
        assertEquals(false, campaign.roster.setBattleRoster(listOf(0, 6, 10), limit))
        assertEquals(true, campaign.roster.setBattleRoster(listOf(6, 10), limit))
        assertEquals(listOf(6, 10), campaign.roster.battleRoster)
    }

    @Test
    fun `joined equipment is retained by the shared campaign state`() {
        val campaign = CampaignState()
        val stage = ScenarioStage(campaign)

        stage.setJoinEquip(3, 36, 2, 5, 1, 16)

        assertEquals(CampaignEquipment(36, 2, 5, 1, 16), campaign.inventory.equipment[3])
    }

    @Test
    fun `hall transition APIs retain original battle selection placement equipment and ending state`() {
        val stage = ScenarioStage()

        stage.setJoinBattle(1, 7, listOf(1), listOf(9))
        stage.setBattlePositions(listOf(listOf(17, 9), listOf(16, 8)))
        stage.setJoinEquip(3, 36, 1, 0, 0, 16)
        stage.infoTransfer(26, "157")
        stage.ending(2)
        stage.jumpScene(59)

        assertEquals(7, stage.joinBattleLimit?.maximum)
        assertEquals(listOf(17 to 9, 16 to 8), stage.battlePositions)
        assertEquals(16, stage.joinedEquipment.getValue(3).auxiliary)
        assertEquals(26 to "157", stage.infoTransfers.single())
        assertEquals(2, stage.endingId)
        assertEquals(59, stage.sceneJumpTarget)
        assertEquals(60, stage.sceneJumpStage)
    }

    @Test
    fun `jump scene applies and clears source global jump offset`() {
        val campaign = CampaignState().apply { globalVariables[4051] = 2 }
        val stage = ScenarioStage(campaign)

        stage.jumpScene(91)

        assertEquals(91, stage.sceneJumpTarget)
        assertEquals(492, stage.sceneJumpStage)
        assertEquals(0, campaign.globalVariables[4051])
    }

    @Test
    fun `info transfer applies source model campaign mutations across stages`() {
        val campaign = CampaignState { upperExclusive -> upperExclusive - 1 }
        val first = ScenarioStage(campaign)
        first.joinUnit(1)
        first.joinUnit(2)
        first.setUnitAttribute(1, 18, 12)
        first.setUnitAttribute(2, 18, 3)
        first.infoTransfer(18, "")
        first.infoTransfer(0, "새 이름", 2)
        first.infoTransfer(4, "20\n8\n2\n유혹")
        first.infoTransfer(5, "2\n3\n63\n마비 공격")
        first.infoTransfer(26, "157")
        first.ending(4)

        val second = ScenarioStage(campaign)
        assertEquals(7, second.unitAttribute(2, 18))
        assertEquals("새 이름", campaign.unitNames[2])
        assertEquals(CampaignMagic(2, 20, 8, "유혹"), campaign.extraMagic[2 to 20])
        assertEquals(CampaignTalent(2, 3, 63, "마비 공격"), campaign.talents[2 to 3])
        assertEquals(156, campaign.globalVariables[4025])
        assertEquals(4, campaign.endingId)
    }

    @Test
    fun `stage unit attributes retain original set add and subtract semantics`() {
        val stage = ScenarioStage()

        stage.setUnitAttribute(35, 7, 100)
        stage.changeUnitAttribute(35, 7, 2, 35)
        stage.changeUnitAttribute(35, 8, 0, 80)
        stage.changeUnitAttribute(35, 8, 1, 5)

        assertEquals(65, stage.unitAttribute(35, 7))
        assertEquals(85, stage.unitAttribute(35, 8))
    }

    @Test
    fun `original UserDefault save envelope round trips`() {
        val json = """{"stage":3,"money":1200,"vars":{"1":1}}"""
        val encoded = CampaignSaveCodec.encode(json)

        assertEquals(json, CampaignSaveCodec.decode(encoded))
        assertNull(CampaignSaveCodec.decode(encoded.dropLast(1) + "x"))
    }

    @Test
    fun `original Global binary table envelope round trips`() {
        val table = """[{"name":"조조","hp":100},{"name":"병사","hp":80}]"""
        val encoded = EncryptedGameDataCodec.encode(table)

        assertEquals(table, EncryptedGameDataCodec.decode(encoded))
        encoded[40] = (encoded[40].toInt() xor 1).toByte()
        assertNull(EncryptedGameDataCodec.decode(encoded))
    }

    @Test
    fun `stage commands run until dialogue then resume to choice`() {
        val timeline = ScenarioTimeline(
            "test",
            listOf(
                ScenarioCommand.LoadBackground(2, 30),
                ScenarioCommand.ShowUnit(181, 40, 5, 2),
                ScenarioCommand.DialogueLine(Dialogue("181", "대장님, 서둘러야 해요!")),
                ScenarioCommand.MoveUnit(181, 40, 15, 2),
                ScenarioCommand.Choose(Choice(listOf("계속", "중단")))
            )
        )

        val playback = ScenarioPlayback(timeline)
        assertEquals(PlaybackState.DIALOGUE, playback.state)
        assertEquals(71, playback.stage.backgroundId)
        assertEquals(5, playback.stage.unit(181).y)
        assertEquals("대장님, 서둘러야 해요!", playback.currentDialogue?.text)

        playback.advanceDialogue()
        assertEquals(PlaybackState.CHOICE, playback.state)
        // Source move2 keeps the logical tile at the origin until its final
        // callback; the choice can already be queued while the node moves.
        assertEquals(5, playback.stage.unit(181).y)
        assertEquals(20, playback.stage.unit(181).action)
        playback.stage.finishAnimations()
        assertEquals(15, playback.stage.unit(181).y)
        assertEquals(listOf("계속", "중단"), playback.currentChoice?.options)

        playback.selectNext()
        playback.confirmChoice()
        assertEquals(PlaybackState.COMPLETE, playback.state)
        assertEquals("중단", playback.chosenOption)
        assertNull(playback.currentChoice)
    }

    @Test
    fun `battle context seeds lazy stage unit at its live tactical tile`() {
        val runtime = ScenarioInterpreter.load("S_00", CampaignState())
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(
                round = 1,
                camp = 1,
                positions = mapOf(210 to (10 to 16)),
                stagePositions = mapOf(210 to (10 to 16), 211 to (9 to 16)),
            )
        )

        assertEquals(10, runtime.stage.unit(210).x)
        assertEquals(16, runtime.stage.unit(210).y)
        assertEquals(9f, runtime.stage.unit(211).visualX)
        assertEquals(16f, runtime.stage.unit(211).visualY)
    }

    @Test
    fun `source unit type selectors preserve exact camps and both-side aggregates`() {
        assertTrue(sourceUnitTypeMatches(0, 0))
        assertTrue(sourceUnitTypeMatches(1, 4))
        assertTrue(sourceUnitTypeMatches(3, 5))
        assertTrue(sourceUnitTypeMatches(2, 6))
        assertFalse(sourceUnitTypeMatches(1, 2))
        assertFalse(sourceUnitTypeMatches(2, 4))
    }

    @Test
    fun `stage unit flag one resolves physical source slot instead of character id`() {
        val campaign = CampaignState().also { it.roster.restoreBattleRoster(listOf(57, 139)) }
        val runtime = ScenarioInterpreter.load("S_31", campaign)
        runtime.stage.createBattleUnits(
            ScenarioUnitFaction.MINE,
            listOf(mapOf("idx" to 0, "i" to 0), mapOf("idx" to 1, "i" to 1)),
        )

        assertEquals(ScenarioInterpreter.UnitReference(57), runtime.resolveStageUnitReference(0, 1))
        assertEquals(ScenarioInterpreter.UnitReference(139), runtime.resolveStageUnitReference(1, 1))
        assertEquals(ScenarioInterpreter.UnitReference(1), runtime.resolveStageUnitReference(1, 0))
        assertNull(runtime.resolveStageUnitReference(14, 1), "missing _unitSet slots are source undefined")
    }

    @Test
    fun `S35 enemy-side position selector 1026 fires its authored capture event`() {
        val runtime = ScenarioInterpreter.load("S_35")
        runtime.setScriptVariables((0..100).associateWith { 1 } + mapOf(21 to 0, 54 to 0))
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(
                round = 1,
                camp = 0,
                positions = mapOf(62 to (6 to 4)),
                positionsByCamp = mapOf(2 to listOf(6 to 4)),
                campByCharacterId = mapOf(62 to 2),
                activeCharacterIds = setOf(62),
            ),
        )

        runtime.start("scene1")

        assertEquals(PlaybackState.DIALOGUE, runtime.state)
        assertEquals("57", runtime.currentDialogue?.speakerId)
        assertTrue(runtime.currentDialogue?.text.orEmpty().contains("양평관을 점령"))
    }

    @Test
    fun `S52 all-camp rectangle selector 6 keeps an occupied gate open`() {
        fun runtimeAtGate(occupied: Boolean): ScenarioInterpreter = ScenarioInterpreter.load("S_52").also { runtime ->
            runtime.setScriptVariables((0..100).associateWith { 1 } + (20 to 0))
            runtime.setBattleContext(
                ScenarioInterpreter.BattleScriptContext(
                    round = 1,
                    camp = 0,
                    positions = if (occupied) mapOf(438 to (7 to 15)) else emptyMap(),
                    positionsByCamp = if (occupied) mapOf(2 to listOf(7 to 15)) else emptyMap(),
                    campByCharacterId = if (occupied) mapOf(438 to 2) else emptyMap(),
                    attributes = if (occupied) mapOf(438 to mapOf(7 to 10)) else emptyMap(),
                ),
            )
            runtime.start("scene1")
        }

        val occupied = runtimeAtGate(occupied = true)
        assertFalse(occupied.stage.mapObjects.containsKey(7 to 15))

        val empty = runtimeAtGate(occupied = false)
        assertEquals(true, empty.stage.mapObjects[7 to 15]?.enabled)
        assertEquals(17, empty.stage.mapObjects[7 to 15]?.terrainId)
    }

    @Test
    fun `turn event fires once and advances past repeated end turns`() {
        val battle = BattleScenarioFactory.tutorialBattle()

        assertEquals(TurnResult(1, Faction.ENEMY, emptyList()), battle.endTurn())
        val arrival = battle.endTurn()
        assertEquals(2, arrival.round)
        assertEquals(Faction.PLAYER, arrival.activeFaction)
        assertEquals(listOf("reinforcement-arrival"), arrival.firedEvents)
        assertEquals("증원군", battle.units["reinforcement"]?.name)

        battle.endTurn()
        val nextPlayerTurn = battle.endTurn()
        assertEquals(3, nextPlayerTurn.round)
        assertEquals(emptyList(), nextPlayerTurn.firedEvents)
        assertEquals(setOf("reinforcement-arrival"), battle.firedEventIds)
    }

    @Test
    fun `original battle camps advance mine friend enemy before the next round`() {
        val battle = Battle(
            listOf(
                BattleUnit("mine", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("friend", "우군", Faction.FRIEND, 1, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 3, 0),
            ),
            emptyList(),
        )

        assertEquals(Faction.FRIEND, battle.endTurn().activeFaction)
        assertEquals(Faction.ENEMY, battle.endTurn().activeFaction)
        assertEquals(TurnResult(2, Faction.PLAYER, emptyList()), battle.endTurn())
        assertEquals("아군을 공격할 수 없습니다.", (battle.attack("mine", "friend") as TacticalActionResult.Rejected).reason)
    }

    @Test
    fun `scripted attack action resolves even outside the current unit turn`() {
        val battle = Battle(
            listOf(
                BattleUnit("mine", "아군", Faction.PLAYER, 0, 0, attack = 100, critical = 100),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, defense = 1, critical = 1, hitPoints = 100),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.forcedAttack("mine", "enemy"))

        assertEquals(true, result.hit)
        assertEquals(true, battle.units.getValue("enemy").hitPoints < 100)
    }

    @Test
    fun `battle reaches original maximum-round defeat`() {
        val battle = Battle(
            listOf(BattleUnit("mine", "아군", Faction.PLAYER, 0, 0), BattleUnit("enemy", "적군", Faction.ENEMY, 2, 0)),
            emptyList(),
        )
        battle.setMaxRounds(2)

        battle.endTurn()
        battle.endTurn()

        assertEquals(BattleOutcome.ENEMY_VICTORY, battle.outcome())
    }

    @Test
    fun `python choice variable selects matching conditional branch`() {
        val program = ScenarioScript(
            "test",
            listOf(
                ScriptStep.Command(ScenarioCommand.LoadBackground(2, 30)),
                ScriptStep.PromptChoice("sel", Choice(listOf("첫 번째", "두 번째"))),
                ScriptStep.Conditional(
                    "sel", 2,
                    whenTrue = listOf(ScriptStep.Command(ScenarioCommand.DialogueLine(Dialogue("0", "두 번째 분기")))),
                    whenFalse = listOf(ScriptStep.Command(ScenarioCommand.DialogueLine(Dialogue("0", "첫 번째 분기"))))
                )
            ),
            emptyList()
        )

        val playback = ProgramPlayback(program)
        assertEquals(PlaybackState.CHOICE, playback.state)
        playback.selectNext()
        playback.confirmChoice()

        assertEquals(PlaybackState.DIALOGUE, playback.state)
        assertEquals("두 번째 분기", playback.currentDialogue?.text)
        assertEquals("두 번째", playback.chosenOption)
    }

    @Test
    fun `player can move then attack adjacent enemy under turn rules`() {
        val battle = Battle(
            listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 2, 0, hitPoints = 35),
            ),
            emptyList(),
        )

        assertIs<TacticalActionResult.Success>(battle.moveUnit("player", 1, 0))
        assertIs<TacticalActionResult.Rejected>(battle.moveUnit("player", 0, 0))
        assertIs<TacticalActionResult.Attack>(battle.attack("player", "enemy"))
        assertEquals(null, battle.units["enemy"])
        assertEquals(BattleOutcome.PLAYER_VICTORY, battle.outcome())
    }

    @Test
    fun `original post skill status attacks affect a successful physical attack`() {
        val battle = Battle(
            listOf(
                BattleUnit("attacker", "마비 병종", Faction.PLAYER, 0, 0, attack = 90, critical = 100, skills = mapOf(144 to 100, 170 to 1)),
                BattleUnit("target", "적", Faction.ENEMY, 1, 0, defense = 1, critical = 1, hitPoints = 500),
            ),
            emptyList(),
        )

        assertIs<TacticalActionResult.Attack>(battle.attack("attacker", "target"))
        val target = battle.units.getValue("target")
        assertEquals(2, target.statuses[BattleStatus.PARALYSIS]) // Source enemy duration is two turns.
        assertEquals(-1, target.attributeLifts[BattleAttribute.ATTACK])
    }

    @Test
    fun `original physical skill floors reductions life steal and counter seal are resolved`() {
        val battle = Battle(
            listOf(
                BattleUnit("attacker", "흡혈", Faction.PLAYER, 0, 0, hitPoints = 40, maxHitPoints = 100, attack = 1, critical = 100, skills = mapOf(92 to 1, 174 to 30, 238 to 40, 226 to 1)),
                BattleUnit("target", "방어", Faction.ENEMY, 1, 0, defense = 100, critical = 1, hitPoints = 500, skills = mapOf(118 to 5)),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.attack("attacker", "target"))
        assertEquals(150, result.damage) // JQWLSH 후 PJGJ: target max HP 500의 30%를 최저 피해로 보정.
        assertEquals(60, result.lifeStealHealing)
        assertEquals(0, result.counterDamage)
        assertEquals(100, battle.units.getValue("attacker").hitPoints)
    }

    @Test
    fun `blocked tile rejects movement`() {
        val battle = Battle(
            listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 3, 0),
            ),
            emptyList(),
            blockedTiles = setOf(1 to 0),
        )

        val result = battle.moveUnit("player", 1, 0)
        assertEquals("장애물이 있는 칸입니다.", (result as TacticalActionResult.Rejected).reason)
    }

    @Test
    fun `movement range follows an unblocked path rather than direct distance`() {
        val battle = Battle(
            listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0, movement = 2),
                BattleUnit("enemy", "적군", Faction.ENEMY, 4, 0),
            ),
            emptyList(),
            blockedTiles = setOf(1 to 0),
        )

        val result = battle.moveUnit("player", 2, 0)

        assertEquals("이동 범위를 벗어났습니다.", (result as TacticalActionResult.Rejected).reason)
    }

    @Test
    fun `movement pathfinding uses the original movement offset table rather than hardcoded cardinal steps`() {
        val battle = Battle(
            listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0, movement = 1),
                BattleUnit("enemy", "적군", Faction.ENEMY, 3, 3),
            ),
            emptyList(),
            movementOffsets = setOf(1 to 1, -1 to -1),
        )

        assertIs<TacticalActionResult.Success>(battle.moveUnit("player", 1, 1))
    }

    @Test
    fun `movement consumes original arm terrain expenditure rather than tile count`() {
        val terrain = BattleTerrainGrid(3, 1, listOf(intArrayOf(0, 9, 0)))
        val battle = Battle(
            units = listOf(BattleUnit("player", "아군", Faction.PLAYER, 0, 0, movement = 2, terrainMovementCosts = mapOf(0 to 1, 9 to 2))),
            events = emptyList(),
            terrain = terrain,
        )

        assertIs<TacticalActionResult.Rejected>(battle.moveUnit("player", 2, 0))
    }

    @Test
    fun `an in-range surviving target performs the original physical counterattack`() {
        val battle = Battle(
            listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0, hitPoints = 100, maxHitPoints = 100, attack = 50, defense = 20, critical = 100),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, hitPoints = 100, maxHitPoints = 100, attack = 60, defense = 20, critical = 100),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Attack>(battle.attack("player", "enemy"))

        assertEquals(true, result.counterDamage > 0)
        assertEquals(100 - result.counterDamage, battle.units.getValue("player").hitPoints)
    }

    @Test
    fun `magic spends original MP and resolves its hit and effect area`() {
        val fire = GameDataCatalog.MagicProfile(
            id = 0,
            name = "작열",
            type = 0,
            target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            effectAreaId = 0,
            effectOffsets = emptySet(),
            expendMp = 6,
            power = 70,
            harmType = 0,
            category = 0,
        )
        val battle = Battle(
            listOf(
                BattleUnit("strategist", "책사", Faction.PLAYER, 0, 0, magicPoints = 10, maxMagicPoints = 10, spirit = 60, morale = 50, magic = listOf(fire)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, hitPoints = 100, maxHitPoints = 100, spirit = 30, morale = 30),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "enemy", 0))

        assertEquals("작열", result.name)
        assertEquals(4, battle.units.getValue("strategist").magicPoints)
        assertEquals(1, result.targets.size)
        assertEquals(true, battle.units.getValue("strategist").hasActed)
    }

    @Test
    fun `any-target strategy can resolve effect area across allied and enemy units`() {
        val any = GameDataCatalog.MagicProfile(
            id = 7, name = "전장 책략", type = 0, target = 3,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = setOf(0 to 0, 1 to 0), expendMp = 1, power = 10, harmType = 0, category = 0,
        )
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magicPoints = 3, maxMagicPoints = 3, spirit = 100, morale = 100, magic = listOf(any)),
                BattleUnit("ally", "아군", Faction.PLAYER, 1, 0, spirit = 1, morale = 1),
                BattleUnit("enemy", "적군", Faction.ENEMY, 2, 0, spirit = 1, morale = 1),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "ally", 7))

        assertEquals(setOf("ally", "enemy"), result.targets.map { it.targetId }.toSet())
    }

    @Test
    fun `original weather strategy changes battlefield weather without a unit effect target`() {
        val rain = GameDataCatalog.MagicProfile(
            id = 58, name = "호우", type = 0, target = 2,
            hitArea = GameDataCatalog.HitAreaProfile(0, emptySet()), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 2, power = 0, harmType = 4, category = 22,
        )
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magicPoints = 3, maxMagicPoints = 3, magic = listOf(rain)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 2, 0),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "enemy", 58))

        assertEquals("호우", result.name)
        assertEquals(BattleWeather.HEAVY_RAIN, battle.weather)
        assertEquals(1, battle.units.getValue("caster").magicPoints)
    }

    @Test
    fun `original configured weather advances when a new player round begins`() {
        val battle = Battle(
            listOf(BattleUnit("mine", "아군", Faction.PLAYER, 0, 0), BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0)), emptyList(),
            initialWeather = BattleWeather.CLEAR,
            weatherSchedule = listOf(BattleWeather.CLEAR, BattleWeather.CLOUDY),
            weatherOffset = 0,
        )

        battle.endTurn()
        battle.endTurn()

        assertEquals(2, battle.round)
        assertEquals(BattleWeather.CLEAR, battle.weather)
    }

    @Test
    fun `elemental strategy uses original unsuitable terrain eighty five percent floor`() {
        val fire = GameDataCatalog.MagicProfile(1, "화계", 0, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), 0, emptySet(), 1, 100, 0, 0)
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magicPoints = 3, maxMagicPoints = 3, spirit = 100, morale = 100, magic = listOf(fire)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, spirit = 1, morale = 1, hitPoints = 100, maxHitPoints = 100),
            ), emptyList(),
            terrain = BattleTerrainGrid(2, 1, listOf(intArrayOf(0, 0))), terrainMagicFlags = mapOf(0 to 0),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "enemy", 1))

        assertEquals(75, result.targets.single().damage)
    }

    @Test
    fun `offensive strategy applies target tile terrain rather than caster tile terrain`() {
        val wind = GameDataCatalog.MagicProfile(
            10, "회오리", 0, 0,
            GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 0, 50, 0, 0,
        )
        val battle = Battle(
            listOf(
                BattleUnit(
                    "caster", "조조", Faction.PLAYER, 0, 0,
                    level = 5, spirit = 61, critical = 1, morale = 1, magic = listOf(wind),
                ),
                BattleUnit(
                    "target", "적군", Faction.ENEMY, 1, 0,
                    spirit = 32, critical = 1, morale = 1, hitPoints = 96, maxHitPoints = 96,
                ),
            ),
            emptyList(),
            terrain = BattleTerrainGrid(2, 1, listOf(intArrayOf(0, 7))),
            terrainMagicFlags = mapOf(0 to 0, 7 to 1),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "target", wind.id))

        // trunc((61 - 32) / 3 + 25 + 5) * 50% = 19. The caster tile
        // is unsuitable (85%), so reading it incorrectly would return 16.
        assertEquals(19, result.targets.single().damage)
    }

    @Test
    fun `original magic weather conditions reject a clear-sky strategy in heavy rain`() {
        val rain = GameDataCatalog.MagicProfile(
            id = 58, name = "호우", type = 23, target = 2,
            hitArea = GameDataCatalog.HitAreaProfile(0, emptySet()), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 0, harmType = 4, category = 22,
        )
        val clearOnly = GameDataCatalog.MagicProfile(
            id = 1, name = "화계", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 100, harmType = 0, category = 0, condition = 0,
        )
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magicPoints = 5, maxMagicPoints = 5, magic = listOf(rain, clearOnly)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0),
            ),
            emptyList(),
        )

        assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "enemy", 58))
        battle.endTurn()
        battle.endTurn()
        assertIs<TacticalActionResult.Rejected>(battle.castMagic("caster", "enemy", 1))
    }

    @Test
    fun `original fire magic remains usable on an unsuitable target terrain`() {
        val fire = GameDataCatalog.MagicProfile(
            id = 0, name = "화계", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 100, harmType = 0, category = 0,
        )
        fun state(flag: Int) = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magicPoints = 2, maxMagicPoints = 2, magic = listOf(fire)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0),
            ),
            emptyList(),
            terrain = BattleTerrainGrid(2, 1, listOf(intArrayOf(0, 7))),
            terrainMagicFlags = mapOf(7 to flag),
        )

        assertIs<TacticalActionResult.Magic>(state(0).castMagic("caster", "enemy", 0))
        assertIs<TacticalActionResult.Magic>(state(1).castMagic("caster", "enemy", 0))
    }

    @Test
    fun `original ZJNL and ZJFY categories raise attack and defense respectively`() {
        val attackUp = GameDataCatalog.MagicProfile(
            id = 19, name = "용력", type = 11, target = 1,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 0, harmType = 4, category = 19,
        )
        val defenseUp = attackUp.copy(id = 20, name = "철벽", type = 12, category = 20)
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magicPoints = 3, maxMagicPoints = 3, spirit = 100, morale = 100, magic = listOf(attackUp, defenseUp)),
                BattleUnit("ally", "아군", Faction.PLAYER, 1, 0, spirit = 1, morale = 1),
                BattleUnit("enemy", "적군", Faction.ENEMY, 5, 0),
            ),
            emptyList(),
        )

        assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "ally", 19))
        assertEquals(1, battle.units.getValue("ally").attributeLifts.getValue(BattleAttribute.ATTACK))
        battle.endTurn()
        battle.endTurn()
        assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "ally", 20))
        assertEquals(1, battle.units.getValue("ally").attributeLifts.getValue(BattleAttribute.DEFENSE))
    }

    @Test
    fun `BAQI and SHUAIQI change every original combat ability lift`() {
        val baqi = GameDataCatalog.MagicProfile(
            id = 26, name = "패기", type = 26, target = 1,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0), allScreen = true), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 0, harmType = 4, category = 25,
        )
        val shuaiqi = baqi.copy(id = 28, name = "쇠기", type = 28, target = 0, category = 27)
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magicPoints = 4, maxMagicPoints = 4, magic = listOf(baqi, shuaiqi)),
                BattleUnit("ally", "아군", Faction.PLAYER, 1, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 2, 0),
            ),
            emptyList(),
        )

        val raise = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "ally", 26)).targets.single()
        assertEquals(5, raise.attributes.size)
        assertEquals(1, battle.units.getValue("ally").attributeLifts.getValue(BattleAttribute.ATTACK))
        battle.endTurn()
        battle.endTurn()
        val lower = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "enemy", 28)).targets.single()
        assertEquals(-1, lower.attributes.getValue(BattleAttribute.DEFENSE))
        assertEquals(-1, battle.units.getValue("enemy").attributeLifts.getValue(BattleAttribute.MORALE))
    }

    @Test
    fun `original MP absorption transfers MP and HP absorption heals its caster`() {
        val drainMp = GameDataCatalog.MagicProfile(
            id = 6, name = "흡마", type = 6, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 100, harmType = 1, category = 3,
        )
        val drainHp = drainMp.copy(id = 5, name = "흡혈", type = 5)
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, hitPoints = 40, maxHitPoints = 100, magicPoints = 1, maxMagicPoints = 20, spirit = 100, morale = 100, magic = listOf(drainMp, drainHp)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, hitPoints = 100, maxHitPoints = 100, magicPoints = 15, maxMagicPoints = 15, spirit = 1, morale = 1),
            ),
            emptyList(),
        )

        val mpResult = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "enemy", 6)).targets.single()
        assertEquals(true, mpResult.magicDrain > 0)
        assertEquals(mpResult.magicDrain, mpResult.magicRecovery)
        assertEquals(15 - mpResult.magicDrain, battle.units.getValue("enemy").magicPoints)
        battle.endTurn()
        battle.endTurn()
        val hpResult = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "enemy", 5)).targets.single()
        assertEquals(true, hpResult.damage > 0)
        assertEquals(60, hpResult.casterHealing)
        assertEquals(100, battle.units.getValue("caster").hitPoints)
    }

    @Test
    fun `original QL category repeats its effect on five randomly selected targets`() {
        val qinglong = GameDataCatalog.MagicProfile(
            id = 64, name = "청룡", type = 25, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 100, harmType = 0, category = 26,
        )
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magicPoints = 2, maxMagicPoints = 2, spirit = 100, morale = 100, magic = listOf(qinglong)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, hitPoints = 1_000, maxHitPoints = 1_000, spirit = 1, morale = 1),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "enemy", 64))

        assertEquals(5, result.targets.size)
        assertEquals(setOf("enemy"), result.targets.map { it.targetId }.toSet())
        assertEquals(true, battle.units.getValue("enemy").hitPoints < 800)
    }

    @Test
    fun `original SB category targets every enemy despite its normal hit-area table`() {
        val fullScreen = GameDataCatalog.MagicProfile(
            id = 2, name = "전장 책략", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, emptySet()), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 100, harmType = 0, category = 1,
        )
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, magicPoints = 2, maxMagicPoints = 2, spirit = 100, morale = 100, magic = listOf(fullScreen)),
                BattleUnit("enemy-one", "적군1", Faction.ENEMY, 5, 0, spirit = 1, morale = 1),
                BattleUnit("enemy-two", "적군2", Faction.ENEMY, 9, 0, spirit = 1, morale = 1),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "enemy-one", 2))

        assertEquals(setOf("enemy-one", "enemy-two"), result.targets.map { it.targetId }.toSet())
    }

    @Test
    fun `HP recovery strategy targets ally and caps at original maximum HP`() {
        val supply = GameDataCatalog.MagicProfile(
            id = 39,
            name = "소량의 보급품",
            type = 19,
            target = 1,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            effectAreaId = 0,
            effectOffsets = emptySet(),
            expendMp = 6,
            power = 28,
            harmType = 4,
            category = 13,
        )
        val battle = Battle(
            listOf(
                BattleUnit("strategist", "책사", Faction.PLAYER, 0, 0, magicPoints = 10, maxMagicPoints = 10, spirit = 60, magic = listOf(supply)),
                BattleUnit("ally", "아군", Faction.PLAYER, 1, 0, hitPoints = 40, maxHitPoints = 100),
                BattleUnit("enemy", "적군", Faction.ENEMY, 5, 5),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "ally", 39))

        assertEquals(1, result.targets.size)
        assertEquals(34, result.targets.single().healing)
        assertEquals(74, battle.units.getValue("ally").hitPoints)
    }

    @Test
    fun `original HP recovery uses caster current HP rather than target maximum`() {
        val supply = GameDataCatalog.MagicProfile(39, "보급", 19, 1, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), 0, emptySet(), 0, 28, 4, 13)
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, hitPoints = 50, maxHitPoints = 100, magicPoints = 1, maxMagicPoints = 1, spirit = 60, magic = listOf(supply)),
                BattleUnit("ally", "아군", Faction.PLAYER, 1, 0, hitPoints = 1, maxHitPoints = 100),
                BattleUnit("enemy", "적", Faction.ENEMY, 5, 5),
            ), emptyList(),
        )
        // floor(50 * 28 / 100) + floor(60 / 10)
        val result = battle.castMagic("caster", "ally", 39)
        assertIs<TacticalActionResult.Magic>(result, result.toString())
        assertEquals(20, result.targets.single().healing)
    }

    @Test
    fun `original direct magic skill additions and rates are applied`() {
        val fire = GameDataCatalog.MagicProfile(0, "화계", 0, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), 0, emptySet(), 0, 100, 0, 0)
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, attack = 100, spirit = 100, morale = 100, magic = listOf(fire), skills = mapOf(141 to 20, 107 to 5, 75 to 10, 128 to 5)),
                BattleUnit("enemy", "적", Faction.ENEMY, 1, 0, spirit = 1, morale = 1, hitPoints = 500, maxHitPoints = 500, skills = mapOf(115 to 10)),
            ), emptyList(),
        )
        // base 59 + LRHY 20 + HXCLZS 5, then (100 + 10 + 5 - 10)% = 95%.
        assertEquals(132, (battle.castMagic("caster", "enemy", 0) as TacticalActionResult.Magic).targets.single().damage)
    }

    @Test
    fun `CLLJ repeats magic process and applies 90 percent only to second pass`() {
        val fire = GameDataCatalog.MagicProfile(0, "화계", 0, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), 0, emptySet(), 0, 100, 0, 0)
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, spirit = 100, morale = 100, magic = listOf(fire),
                    skills = mapOf(16 to 0),
                    criticalSpeech = GameDataCatalog.CriticalSpeechProfile(listOf("책략 필살"), randomized = false)),
                BattleUnit("enemy", "적", Faction.ENEMY, 1, 0, spirit = 1, morale = 1, hitPoints = 500, maxHitPoints = 500),
            ), emptyList(),
        )
        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "enemy", 0))
        assertEquals(listOf(88), result.passes[0].map(MagicTarget::damage))
        assertEquals(listOf(79), result.passes[1].map(MagicTarget::damage))
        assertEquals(true, result.critical)
        assertEquals(listOf("책략 필살", null), result.criticalSpeeches)
        assertEquals(listOf(listOf("enemy"), listOf("enemy")), result.localSettlements.map { local ->
            local.entries.map(MagicLocalSettlementEntry::targetId)
        })
        assertTrue(result.localSettlements.all { it.entries.single().hasStatesPayload })
        assertEquals(333, battle.units.getValue("enemy").hitPoints)
    }

    @Test
    fun `magic local settlement retains every successful area target in source order`() {
        val fire = GameDataCatalog.MagicProfile(
            0, "화계", 0, 0,
            GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, linkedSetOf(0 to 0, 1 to 0), 0, 100, 0, 0,
        )
        val battle = Battle(listOf(
            BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, spirit = 100, magic = listOf(fire)),
            BattleUnit("first", "첫", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500),
            BattleUnit("second", "둘", Faction.ENEMY, 2, 0, hitPoints = 500, maxHitPoints = 500),
        ), emptyList())

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("caster", "first", 0))
        assertEquals(listOf("first", "second"), result.localSettlements.single().entries.map(MagicLocalSettlementEntry::targetId))
        assertTrue(result.localSettlements.single().entries.all { it.hasStatesPayload })
    }

    @Test
    fun `original physical hit and critical immunity skills are resolved`() {
        val state = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, critical = 100, morale = 100, skills = mapOf(92 to 0, 270 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500, skills = mapOf(49 to 0)),
            ), emptyList(),
        )
        val result = state.attack("attacker", "target") as TacticalActionResult.Attack
        assertEquals(100, result.hitRate)
        assertEquals(false, result.critical)
        // The critical gauge succeeded, so source `_attack2` still selects
        // HIT_ATTACK/anime21 even though FYZMGJ cancels the damaging critical.
        assertEquals(true, result.physicalPasses.first().critical)
    }

    @Test
    fun `original strategy hit limit and magic evasion are resolved`() {
        val tactic = GameDataCatalog.MagicProfile(0, "책략", 5, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), 0, emptySet(), 0, 100, 0, 0, hitRateLimit = 3)
        fun battle(targetSkills: Map<Int, Int>) = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, spirit = 100, morale = 100, magic = listOf(tactic)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, spirit = 1, morale = 1, hitPoints = 500, maxHitPoints = 500, skills = targetSkills),
            ), emptyList(),
        )
        assertEquals(50, (battle(emptyMap()).castMagic("caster", "target", 0) as TacticalActionResult.Magic).targets.single().hitRate)
        val evaded = (battle(mapOf(17 to 0)).castMagic("caster", "target", 0) as TacticalActionResult.Magic).targets.single()
        // CLMY is checked after count_magic_hitRate's displayed limit.
        assertEquals(50, evaded.hitRate)
        assertEquals(false, evaded.hit)
    }

    @Test
    fun `source magic hit rate truncates combined spirit morale ratio to 94`() {
        val magic = GameDataCatalog.MagicProfile(
            1, "책략", 0, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 0, 0, 0, 0, hitRateLimit = 2,
        )
        val battle = Battle(
            listOf(
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, spirit = 40, morale = 30, magic = listOf(magic)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, spirit = 30, morale = 20),
            ),
            emptyList(),
        )

        val result = battle.castMagic("caster", "target", 1) as TacticalActionResult.Magic

        assertEquals(94, result.targets.single().hitRate)
    }

    @Test
    fun `Control magic hit limit three and four retain source floor but ignore modifiers against famous targets`() {
        fun tactic(limit: Int) = GameDataCatalog.MagicProfile(
            0, "책략", 5, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            0, emptySet(), 0, 100, 0, 0, hitRateLimit = limit,
        )
        fun battle(limit: Int) = Battle(
            listOf(
                // CLJDMZ would normally force 100%, proving that the source
                // does not enter its modifier branch for a famous target.
                BattleUnit("caster", "책사", Faction.PLAYER, 0, 0, spirit = 100, morale = 100, magic = listOf(tactic(limit)), skills = mapOf(15 to 0)),
                BattleUnit("target", "명장", Faction.ENEMY, 1, 0, spirit = 1, morale = 1, famous = true),
            ),
            emptyList(),
        )

        for (limit in 3..4) {
            val target = assertIs<TacticalActionResult.Magic>(battle(limit).castMagic("caster", "target", 0)).targets.single()
            // The source cap is 0, then its common range(i, 25, 100)
            // exposes 25. CLJDMZ is deliberately skipped for this branch.
            assertEquals(25, target.hitRate)
        }
    }

    @Test
    fun `paralysis strategy keeps the original enemy duration and blocks its action`() {
        val paralysis = GameDataCatalog.MagicProfile(
            id = 1,
            name = "마비 책략",
            type = 0,
            target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            effectAreaId = 0,
            effectOffsets = emptySet(),
            expendMp = 1,
            power = 0,
            harmType = 4,
            category = 10,
        )
        val battle = Battle(
            listOf(
                BattleUnit("strategist", "책사", Faction.PLAYER, 0, 0, magicPoints = 5, maxMagicPoints = 5, spirit = 100, morale = 100, magic = listOf(paralysis)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, spirit = 1, morale = 1),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "enemy", 1))
        assertEquals(BattleStatus.PARALYSIS, result.targets.single().status)
        assertEquals(2, battle.units.getValue("enemy").statuses.getValue(BattleStatus.PARALYSIS))

        battle.endTurn()
        assertEquals(1, battle.units.getValue("enemy").statuses.getValue(BattleStatus.PARALYSIS))
        assertEquals("행동할 수 없는 상태입니다.", (battle.attack("enemy", "strategist") as TacticalActionResult.Rejected).reason)
    }

    @Test
    fun `poison strategy applies original ten percent maximum HP loss after target camp`() {
        val poison = GameDataCatalog.MagicProfile(
            id = 2,
            name = "독연",
            type = 0,
            target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)),
            effectAreaId = 0,
            effectOffsets = emptySet(),
            expendMp = 1,
            power = 0,
            harmType = 4,
            category = 9,
        )
        val battle = Battle(
            listOf(
                BattleUnit("strategist", "책사", Faction.PLAYER, 0, 0, magicPoints = 5, maxMagicPoints = 5, spirit = 100, morale = 100, magic = listOf(poison)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, hitPoints = 100, maxHitPoints = 100, spirit = 1, morale = 1),
            ),
            emptyList(),
        )

        assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "enemy", 2))
        battle.endTurn()
        battle.endTurn()

        assertEquals(90, battle.units.getValue("enemy").hitPoints)
        assertEquals(2, battle.units.getValue("enemy").statuses.getValue(BattleStatus.POISON))
    }

    @Test
    fun `poison uses the original cloudy weather fifteen percent rate`() {
        val poison = GameDataCatalog.MagicProfile(2, "독연", 0, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), 0, emptySet(), 1, 0, 4, 9)
        val cloudy = GameDataCatalog.MagicProfile(60, "담천", 23, 2, GameDataCatalog.HitAreaProfile(0, emptySet()), 0, emptySet(), 1, 0, 4, 22)
        val battle = Battle(
            listOf(
                BattleUnit("strategist", "책사", Faction.PLAYER, 0, 0, magicPoints = 5, maxMagicPoints = 5, spirit = 100, morale = 100, magic = listOf(poison, cloudy)),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 0, hitPoints = 100, maxHitPoints = 100, spirit = 1, morale = 1),
            ), emptyList(),
        )

        assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "strategist", 60))
        battle.units.getValue("strategist").hasActed = false
        assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "enemy", 2))
        battle.endTurn()
        battle.endTurn()

        assertEquals(85, battle.units.getValue("enemy").hitPoints)
    }

    @Test
    fun `awakening removes abnormal states without removing original stat lifts`() {
        val awakening = GameDataCatalog.MagicProfile(46, "각성", 21, 1, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), 0, emptySet(), 1, 0, 4, 15)
        val ally = BattleUnit("ally", "아군", Faction.PLAYER, 1, 0, statuses = linkedMapOf(BattleStatus.POISON to 2), attributeLifts = linkedMapOf(BattleAttribute.ATTACK to 1), attributeLiftRounds = linkedMapOf(BattleAttribute.ATTACK to 2))
        val battle = Battle(
            listOf(
                BattleUnit("strategist", "책사", Faction.PLAYER, 0, 0, magicPoints = 5, maxMagicPoints = 5, magic = listOf(awakening)),
                ally,
                BattleUnit("enemy", "적군", Faction.ENEMY, 5, 0),
            ), emptyList(),
        )

        assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "ally", 46))

        assertEquals(emptyMap(), ally.statuses)
        assertEquals(1, ally.attributeLifts[BattleAttribute.ATTACK])
        assertEquals(2, ally.attributeLiftRounds[BattleAttribute.ATTACK])
    }

    @Test
    fun `spirit weakening follows the original target arm type`() {
        val weaken = GameDataCatalog.MagicProfile(7, "정신 약화", 7, 0, GameDataCatalog.HitAreaProfile(0, setOf(1 to 0, 0 to 1)), 0, emptySet(), 1, 0, 4, 6)
        val civil = BattleUnit("civil", "문관", Faction.ENEMY, 1, 0, armType = 1)
        val martial = BattleUnit("martial", "무장", Faction.ENEMY, 0, 1, armType = 2)
        val battle = Battle(
            listOf(BattleUnit("strategist", "책사", Faction.PLAYER, 0, 0, magicPoints = 5, maxMagicPoints = 5, magic = listOf(weaken)), civil, martial), emptyList(),
        )

        assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "civil", 7))
        battle.units.getValue("strategist").hasActed = false
        assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "martial", 7))

        assertEquals(-1, civil.attributeLifts[BattleAttribute.SPIRIT])
        assertEquals(null, civil.attributeLifts[BattleAttribute.ATTACK])
        assertEquals(-1, martial.attributeLifts[BattleAttribute.ATTACK])
        assertEquals(null, martial.attributeLifts[BattleAttribute.SPIRIT])
    }

    @Test
    fun `defense-reduction strategy changes battle ability then expires after three target turns`() {
        val weakenDefense = GameDataCatalog.MagicProfile(
            id = 3, name = "선동", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 0, harmType = 4, category = 7,
        )
        val battle = Battle(
            listOf(
                BattleUnit("strategist", "책사", Faction.PLAYER, 0, 1, magicPoints = 5, maxMagicPoints = 5, spirit = 100, morale = 100, magic = listOf(weakenDefense)),
                BattleUnit("fighter", "무장", Faction.PLAYER, 1, 0, attack = 100, critical = 100),
                BattleUnit("enemy", "적군", Faction.ENEMY, 1, 1, defense = 40, critical = 1, spirit = 1, morale = 1, hitPoints = 500, maxHitPoints = 500),
            ),
            emptyList(),
        )

        val result = assertIs<TacticalActionResult.Magic>(battle.castMagic("strategist", "enemy", 3))
        assertEquals(BattleAttribute.DEFENSE, result.targets.single().attribute)
        assertEquals(-1, battle.units.getValue("enemy").attributeLifts.getValue(BattleAttribute.DEFENSE))

        battle.endTurn()
        battle.endTurn()
        val attack = battle.attack("fighter", "enemy") as TacticalActionResult.Attack
        assertEquals(true, attack.critical)
        assertEquals(108, attack.damage)
        battle.endTurn()
        battle.endTurn()
        battle.endTurn()
        assertEquals(null, battle.units.getValue("enemy").attributeLifts[BattleAttribute.DEFENSE])
    }

    @Test
    fun `original AI types distinguish passive active and fixed-destination movement`() {
        val passive = Battle(
            listOf(BattleUnit("enemy", "적", Faction.ENEMY, 0, 0, ai = 0), BattleUnit("player", "아군", Faction.PLAYER, 8, 0)),
            emptyList(),
        )
        passive.endTurn()
        assertEquals(AiTurnResult(moves = 0, attacks = 0, holds = 1), passive.resolveAiTurn())
        assertEquals(0, passive.units.getValue("enemy").tileX)

        val active = Battle(
            listOf(BattleUnit("enemy", "적", Faction.ENEMY, 0, 0, ai = 1), BattleUnit("player", "아군", Faction.PLAYER, 8, 0)),
            emptyList(),
        )
        active.endTurn()
        assertEquals(1, active.resolveAiTurn().moves)
        assertEquals(3, active.units.getValue("enemy").tileX)

        val destination = Battle(
            listOf(BattleUnit("enemy", "적", Faction.ENEMY, 0, 0, ai = 4, aiTargetX = 2, aiTargetY = 0), BattleUnit("player", "아군", Faction.PLAYER, 8, 0)),
            emptyList(),
        )
        destination.endTurn()
        assertEquals(1, destination.resolveAiTurn().moves)
        assertEquals(2, destination.units.getValue("enemy").tileX)
        // CtrlDZDD only persists BEI_DONG_CHU_JI when the controller is
        // entered while already standing on its destination.  Completing
        // the move this turn therefore retains AI 4 until the next camp.
        assertEquals(4, destination.units.getValue("enemy").ai)
        destination.endTurn()
        destination.endTurn()
        assertEquals(AiTurnResult(moves = 0, attacks = 0, holds = 1), destination.resolveAiTurn())
        assertEquals(0, destination.units.getValue("enemy").ai)
    }

    @Test
    fun `passive AI uses source psAry scan then attacks from a reachable tile`() {
        // CtrlBDCJ._aiHaveAttackTargets scans all movement candidates.  The
        // target is not attackable at x=0, but it is attackable from x=3.
        val battle = Battle(
            listOf(
                BattleUnit("enemy", "적", Faction.ENEMY, 0, 0, ai = 0, movement = 3, critical = 100),
                BattleUnit("player", "아군", Faction.PLAYER, 4, 0, critical = 1),
            ),
            emptyList(),
        )

        battle.endTurn()
        assertEquals(AiTurnResult(moves = 1, attacks = 1, holds = 0), battle.resolveAiTurn())
        assertEquals(3, battle.units.getValue("enemy").tileX)
        assertEquals(true, battle.units.getValue("player").hitPoints < 100)
    }

    @Test
    fun `active AI can attack after moving into its original attack range`() {
        val battle = Battle(
            listOf(
                BattleUnit("enemy", "적", Faction.ENEMY, 0, 0, ai = 1, movement = 3, critical = 100),
                BattleUnit("player", "아군", Faction.PLAYER, 4, 0, hitPoints = 100, maxHitPoints = 100, critical = 1),
            ),
            emptyList(),
        )

        battle.endTurn()
        val result = battle.resolveAiTurn()

        assertEquals(1, result.moves)
        assertEquals(1, result.attacks)
        assertEquals(true, battle.units.getValue("player").hitPoints < 100)
    }

    @Test
    fun `active AI ignores a no-harm strategy and uses a physical attack`() {
        val strategy = GameDataCatalog.MagicProfile(
            id = 71, name = "화계", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 40, harmType = 4, category = 0,
        )
        val battle = Battle(
            listOf(
                BattleUnit("enemy", "적 책사", Faction.ENEMY, 0, 0, ai = 1, magicPoints = 3, maxMagicPoints = 3, spirit = 100, morale = 100, magic = listOf(strategy)),
                BattleUnit("player", "아군", Faction.PLAYER, 1, 0, hitPoints = 100, maxHitPoints = 100, spirit = 1, morale = 1),
            ),
            emptyList(),
        )

        battle.endTurn()
        val result = battle.resolveAiTurn()

        assertEquals(1, result.attacks)
        // harmType=4 is MAGIC_HARM_TYPE.NO. Original _countMagicValue
        // gives it no offensive score, so Control._AIProcess selects attack.
        assertEquals(3, battle.units.getValue("enemy").magicPoints)
        assertEquals(true, battle.units.getValue("enemy").hasActed)
        assertEquals(true, battle.units.getValue("player").hitPoints < 100)
    }

    @Test
    fun `active AI evaluates a strategy from its moved candidate tile`() {
        val strategy = GameDataCatalog.MagicProfile(
            id = 71, name = "화계", type = 0, target = 0,
            hitArea = GameDataCatalog.HitAreaProfile(0, setOf(1 to 0)), effectAreaId = 0,
            effectOffsets = emptySet(), expendMp = 1, power = 255, harmType = 1, category = 0,
        )
        val battle = Battle(
            listOf(
                BattleUnit("enemy", "적 책사", Faction.ENEMY, 0, 0, ai = 1, movement = 1, magicPoints = 9, maxMagicPoints = 9, spirit = 100, magic = listOf(strategy)),
                BattleUnit("player", "아군", Faction.PLAYER, 2, 0, hitPoints = 300, maxHitPoints = 300, spirit = 1),
            ),
            emptyList(),
        )

        battle.endTurn()
        val result = battle.resolveAiTurn()

        assertEquals(1, result.moves)
        assertEquals(1, result.attacks)
        assertEquals(1, battle.units.getValue("enemy").tileX)
        assertEquals(8, battle.units.getValue("enemy").magicPoints)
    }

    @Test
    fun `scripted setAI retains target id and destination arguments`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(ScenarioUnitFaction.ENEMY, listOf(mapOf("i" to 1, "id" to 99, "x" to 2, "y" to 3)))

        stage.setUnitAi(99, ai = 4, targetId = -1, targetX = 19, targetY = 23)

        val unit = stage.battleUnits.getValue("ENEMY:1")
        assertEquals(4, unit.ai)
        assertEquals(19, unit.aiTargetX)
        assertEquals(23, unit.aiTargetY)
    }

    @Test
    fun `scripted battle units default omitted direction to source down`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(ScenarioUnitFaction.ENEMY, listOf(mapOf("i" to 1, "id" to 99, "x" to 2, "y" to 3)))

        assertEquals(2, stage.battleUnits.getValue("ENEMY:1").direction)
    }

    @Test
    fun `only explicit event direction changes are emitted to battle presentation`() {
        val stage = ScenarioStage()

        // A lazily-created proxy is bookkeeping only and must not replace a
        // BattleScreen actor's authored spawn direction.
        stage.unit(99)
        assertEquals(emptyList(), stage.consumeScriptedUnitDirections())

        stage.setUnitDirection(99, 3)
        assertEquals(listOf(99 to 3), stage.consumeScriptedUnitDirections())

        stage.apply(ScenarioCommand.MoveUnit(99, 7, 8, 1))
        assertEquals(listOf(99 to 1), stage.consumeScriptedUnitDirections())
    }

    @Test
    fun `original omitted AI defaults depend on battle camp`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(ScenarioUnitFaction.MINE, listOf(mapOf("i" to 1, "id" to 1)))
        stage.createBattleUnits(ScenarioUnitFaction.FRIEND, listOf(mapOf("i" to 2, "id" to 2)))
        stage.createBattleUnits(ScenarioUnitFaction.ENEMY, listOf(mapOf("i" to 3, "id" to 3, "ai" to 0)))

        assertEquals(1, stage.battleUnits.getValue("MINE:1").ai)
        assertEquals(2, stage.battleUnits.getValue("FRIEND:2").ai)
        assertEquals(0, stage.battleUnits.getValue("ENEMY:3").ai)
    }

    @Test
    fun `battle rectangle commands preserve original mine friend enemy camp indices`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(ScenarioUnitFaction.MINE, listOf(mapOf("i" to 1, "id" to 1, "x" to 2, "y" to 2)))
        stage.createBattleUnits(ScenarioUnitFaction.FRIEND, listOf(mapOf("i" to 2, "id" to 2, "x" to 2, "y" to 2)))
        stage.createBattleUnits(ScenarioUnitFaction.ENEMY, listOf(mapOf("i" to 3, "id" to 3, "x" to 2, "y" to 2)))

        stage.setBattleAi(camp = 1, x1 = 2, y1 = 2, x2 = 2, y2 = 2, ai = 4)
        stage.hideBattleRect(2, 2, 2, 2, camp = 2)

        assertEquals(1, stage.battleUnits.getValue("MINE:1").ai)
        assertEquals(4, stage.battleUnits.getValue("FRIEND:2").ai)
        assertEquals(true, stage.battleUnits.getValue("ENEMY:3").hidden)
    }

    @Test
    fun `scripted unit status payload is retained once for BattleScreen application`() {
        val stage = ScenarioStage()
        stage.setUnitStatuses(listOf(mapOf("unit" to ScenarioInterpreter.UnitReference(234), "hp" to -80, "hStatus" to listOf(9))))

        val statuses = stage.consumeUnitStatuses()
        assertEquals(-80, statuses.single().getValue("hp"))
        assertEquals(ScenarioInterpreter.UnitReference(234), statuses.single().getValue("unit"))
        assertEquals(emptyList(), stage.consumeUnitStatuses())
    }

    @Test
    fun `multi speaker source dialogue is split at original speaker tags`() {
        val blocks = ScenarioInterpreter.parseDialogueBlocks("&181\n대장님!\n&0\n알았다.")

        assertEquals(listOf("181", "0"), blocks.map { it.speakerId })
        assertEquals(listOf("대장님!", "알았다."), blocks.map { it.text })
    }

    @Test
    fun `multi speaker pages retain one source SayLayer lifecycle revision`() {
        val runtime = ScenarioInterpreter.load("S_00")
        runtime.setScriptVariables(mapOf(14 to 1))
        runtime.setBattleContext(ScenarioInterpreter.BattleScriptContext(round = 1, camp = 2))

        runtime.start("scene1")
        assertEquals("32", runtime.currentDialogue?.speakerId)
        val firstSayRevision = runtime.dialogueLifecycleRevision

        runtime.advanceDialogue()
        assertEquals("3", runtime.currentDialogue?.speakerId)
        val multiSpeakerRevision = runtime.dialogueLifecycleRevision
        assertEquals(firstSayRevision + 1, multiSpeakerRevision)
        val firstPageRevision = runtime.dialogueRevision

        runtime.advanceDialogue()
        assertEquals("33", runtime.currentDialogue?.speakerId)
        assertEquals(multiSpeakerRevision, runtime.dialogueLifecycleRevision)
        assertEquals(firstPageRevision + 1, runtime.dialogueRevision)

        runtime.advanceDialogue()
        assertEquals("32", runtime.currentDialogue?.speakerId)
        assertEquals(multiSpeakerRevision, runtime.dialogueLifecycleRevision)

        runtime.advanceDialogue()
        // Closing this multi-speaker SayLayer resumes the authored scripted
        // movement, whose move2 callback is represented by DELAY.
        assertEquals(PlaybackState.DELAY, runtime.state)
        assertNull(runtime.currentDialogueSourceText)
    }

    @Test
    fun `S00 battle talk opens centered fallback-aware dialogue instead of being skipped`() {
        val runtime = ScenarioInterpreter.load("S_00")
        runtime.setScriptVariables(mapOf(11 to 1, 14 to 1))
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(
                round = 2,
                camp = 3,
                activeCharacterIds = setOf(3, 32, 33, 157),
            ),
        )

        runtime.start("scene1")
        repeat(4) { runtime.advanceDialogue() }
        runtime.skipDelay()
        runtime.skipDelay()
        // The second long fire delay resumes into unit(3).move(8, 5), which
        // owns a separate move2 callback barrier before the next SayLayer.
        runtime.skipDelay()
        assertEquals("3", runtime.currentDialogue?.speakerId)
        runtime.advanceDialogue()
        runtime.skipDelay()
        runtime.skipDelay()
        assertEquals("33", runtime.currentDialogue?.speakerId)

        runtime.advanceDialogue()

        assertEquals(Dialogue("157", "헤헤, 하!\n그럼, 이건 어때?"), runtime.currentDialogue)
        assertEquals("&157\n헤헤, 하!\n그럼, 이건 어때?", runtime.currentDialogueSourceText)
        assertFalse("stage.talk" in runtime.unhandledCalls)
    }

    @Test
    fun `S00 isNear true uses source infantry diagonals for Zhang Liang dialogue`() {
        val runtime = ScenarioInterpreter.load("S_00")
        runtime.setScriptVariables((13..40).associateWith { 1 })
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(
                round = 7,
                camp = 0,
                positions = mapOf(0 to (9 to 12), 147 to (10 to 11)),
                activeCharacterIds = setOf(0, 147),
            ),
        )

        runtime.start("scene1")

        assertEquals(PlaybackState.DIALOGUE, runtime.state)
        assertEquals("0", runtime.currentDialogue?.speakerId)
        assertTrue(runtime.currentDialogueSourceText.orEmpty().contains("당신이 장량 맞죠?"))
    }

    @Test
    fun `battle reward request preserves money item levels and ending flag until callback`() {
        val stage = ScenarioStage()
        stage.reward(75, listOf(88, -1, 89, 2), true)

        assertEquals(BattleOutcome.PLAYER_VICTORY, stage.scriptedBattleOutcome)
        assertEquals(ScenarioRewardRequest(75, listOf(88, -1, 89, 2), true), stage.consumeRewardRequest())
        assertNull(stage.consumeRewardRequest())
    }

    @Test
    fun `battle unit hide request retains type until one production callback consumes it`() {
        val stage = ScenarioStage()
        stage.requestUnitHide(146, 2)

        assertEquals(ScenarioUnitHideRequest(146, 2), stage.consumeUnitHideRequest())
        assertNull(stage.consumeUnitHideRequest())
    }

    @Test
    fun `rect unit hide queues source tile order and resumes only after final callback`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(
            ScenarioUnitFaction.ENEMY,
            listOf(
                mapOf("i" to 4, "id" to 484, "x" to 8, "y" to 6),
                mapOf("i" to 2, "id" to 482, "x" to 9, "y" to 5),
                mapOf("i" to 3, "id" to 483, "x" to 7, "y" to 5),
            ),
        )

        assertEquals(3, stage.requestRectUnitHide(6, 4, 10, 7, 5, 1))
        val requests = List(3) { assertIs<ScenarioUnitHideRequest>(stage.consumeUnitHideRequest()) }
        assertEquals(listOf(483, 482, 484), requests.map { it.unitId })
        assertEquals(listOf("enemy-3", "enemy-2", "enemy-4"), requests.map { it.battleUnitId })
        assertEquals(listOf(false, false, true), requests.map { it.resumesScript })
        assertNull(stage.consumeUnitHideRequest())
    }

    @Test
    fun `rect hide is synchronous when selection empty and master mutates later hide actions`() {
        val empty = ScenarioStage()
        assertEquals(0, empty.requestRectUnitHide(0, 0, 9, 9, 6, 1))
        assertNull(empty.consumeUnitHideRequest())

        val stage = ScenarioStage()
        stage.createBattleUnits(
            ScenarioUnitFaction.MINE,
            listOf(
                mapOf("i" to 1, "id" to 33, "x" to 1, "y" to 1),
                mapOf("i" to 0, "id" to 0, "x" to 2, "y" to 1),
                mapOf("i" to 2, "id" to 32, "x" to 3, "y" to 1),
            ),
        )
        assertEquals(3, stage.requestRectUnitHide(0, 0, 4, 2, 0, 1))
        val beforeMaster = assertIs<ScenarioUnitHideRequest>(stage.consumeUnitHideRequest())
        val master = assertIs<ScenarioUnitHideRequest>(stage.consumeUnitHideRequest())
        val afterMaster = assertIs<ScenarioUnitHideRequest>(stage.consumeUnitHideRequest())
        assertEquals(1, beforeMaster.hideType)
        assertTrue(beforeMaster.showsRetireMessage)
        assertEquals(2, master.hideType)
        assertTrue(master.showsRetireMessage, "master retirement line precedes the shared type mutation")
        assertEquals(2, afterMaster.hideType)
        assertFalse(afterMaster.showsRetireMessage)
    }

    @Test
    fun `S00 rect hide suspends and emits its selected batch in production mode`() {
        val runtime = ScenarioInterpreter.load("S_00")
        runtime.enableExternalBattlePresentation()
        runtime.setScriptVariables((0..110).associateWith { 1 } + (100 to 0))
        runtime.stage.createBattleUnits(
            ScenarioUnitFaction.ENEMY,
            listOf(
                mapOf("i" to 2, "id" to 146, "x" to 9, "y" to 11),
                mapOf("i" to 3, "id" to 147, "x" to 10, "y" to 11),
            ),
        )

        runtime.start("scene1")

        assertEquals(PlaybackState.DELAY, runtime.state)
        val first = assertIs<ScenarioUnitHideRequest>(runtime.stage.consumeUnitHideRequest())
        val second = assertIs<ScenarioUnitHideRequest>(runtime.stage.consumeUnitHideRequest())
        assertEquals(listOf(146, 147), listOf(first.unitId, second.unitId))
        assertFalse(first.resumesScript)
        assertTrue(second.resumesScript)
        assertEquals(listOf(1, 1), listOf(first.hideType, second.hideType))
    }

    @Test
    fun `fight command stream preserves S01 side flags order and battle music`() {
        val stage = ScenarioStage()
        stage.setBackgroundSound(2)

        val fightId = stage.startFight(3, 134, 5)
        stage.enqueueFightCommand(ScenarioFightCommand.ShowUnit(fightId, true, "관우", 9))
        stage.enqueueFightCommand(ScenarioFightCommand.ShowUnit(fightId, false, "화웅", 8))
        stage.enqueueFightCommand(ScenarioFightCommand.ShowStart(fightId))
        stage.enqueueFightCommand(ScenarioFightCommand.Attack2(fightId, true, 1, false))
        stage.enqueueFightCommand(ScenarioFightCommand.Death(fightId, enemy = true))

        assertEquals(8, stage.backgroundSound)
        assertEquals(
            listOf(
                ScenarioFightCommand.Start(fightId, 3, 134, 5, 2),
                ScenarioFightCommand.ShowUnit(fightId, true, "관우", 9),
                ScenarioFightCommand.ShowUnit(fightId, false, "화웅", 8),
                ScenarioFightCommand.ShowStart(fightId),
                ScenarioFightCommand.Attack2(fightId, true, 1, false),
                ScenarioFightCommand.Death(fightId, enemy = true),
            ),
            stage.consumeFightCommands(),
        )

        // FightLayer.end restores the sound captured by startFight even when
        // the renderer consumed Start and the other commands incrementally.
        stage.enqueueFightCommand(ScenarioFightCommand.End(fightId))
        assertEquals(2, stage.backgroundSound)
        assertNull(stage.activeFightId)
        assertEquals(listOf(ScenarioFightCommand.End(fightId)), stage.consumeFightCommands())
    }

    @Test
    fun `S01 duel AST emits the recovered FightLayer command sequence`() {
        fun ScenarioFightCommand.kind(): String = when (this) {
            is ScenarioFightCommand.Start -> "Start"
            is ScenarioFightCommand.ShowUnit -> "ShowUnit"
            is ScenarioFightCommand.ShowStart -> "ShowStart"
            is ScenarioFightCommand.SetAction -> "SetAction"
            is ScenarioFightCommand.Say -> "Say"
            is ScenarioFightCommand.Attack2 -> "Attack2"
            is ScenarioFightCommand.Attack1 -> "Attack1"
            is ScenarioFightCommand.Death -> "Death"
            is ScenarioFightCommand.End -> "End"
        }
        val runtime = ScenarioInterpreter.load("S_01")
        runtime.setScriptVariables(
            mapOf(11 to 1, 12 to 1, 20 to 1, 21 to 1, 30 to 1, 31 to 1, 40 to 0, 41 to 1),
        )
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(
                round = 1,
                camp = 0,
                positions = mapOf(3 to (5 to 5), 134 to (6 to 5)),
                activeCharacterIds = setOf(3, 134),
            ),
        )

        runtime.start("scene1")
        assertEquals(Dialogue("3", "나는 둘째 동생을 유비관우관운장,\n만나게 해줘."), runtime.currentDialogue)
        runtime.advanceDialogue()
        assertEquals(Dialogue("134", "흥, 아무도 아니야!"), runtime.currentDialogue)
        runtime.advanceDialogue()

        // The authored two-second stage.delay follows the first strike.
        assertEquals(PlaybackState.DELAY, runtime.state)
        val beforeDelay = runtime.stage.consumeFightCommands()
        assertIs<ScenarioFightCommand.Start>(beforeDelay[0])
        assertEquals(
            listOf("Start", "ShowUnit", "ShowUnit", "ShowStart", "SetAction", "Say", "SetAction", "Say", "SetAction", "Attack2"),
            beforeDelay.map { it.kind() },
        )
        assertEquals(ScenarioFightCommand.Attack2(beforeDelay[0].fightId, true, 1, false), beforeDelay.last())

        runtime.skipDelay()
        val afterDelay = runtime.stage.consumeFightCommands()
        assertEquals(
            listOf("Say", "SetAction", "Death", "SetAction", "SetAction", "Say", "End"),
            afterDelay.map { it.kind() },
        )
        assertEquals(ScenarioFightCommand.Death(beforeDelay[0].fightId, enemy = true), afterDelay[2])
        assertIs<ScenarioFightCommand.End>(afterDelay.last())
        assertNull(runtime.stage.activeFightId)
        assertFalse("stage.startFight" in runtime.unhandledCalls)
        assertFalse(runtime.unhandledCalls.keys.any { it.startsWith("fight.") })
    }

    @Test
    fun `external FightLayer presentation acknowledges one S01 command at a time`() {
        val runtime = ScenarioInterpreter.load("S_01")
        runtime.enableExternalFightPresentation()
        runtime.setScriptVariables(
            mapOf(11 to 1, 12 to 1, 20 to 1, 21 to 1, 30 to 1, 31 to 1, 40 to 0, 41 to 1),
        )
        runtime.setBattleContext(
            ScenarioInterpreter.BattleScriptContext(
                round = 1,
                camp = 0,
                positions = mapOf(3 to (5 to 5), 134 to (6 to 5)),
            ),
        )

        runtime.start("scene1")
        runtime.advanceDialogue()
        runtime.advanceDialogue()

        assertEquals(PlaybackState.DELAY, runtime.state)
        val start = assertIs<ScenarioFightCommand.Start>(runtime.stage.consumeFightCommands().single())

        runtime.resumeExternalDelay()
        assertEquals(PlaybackState.DELAY, runtime.state)
        assertEquals(
            ScenarioFightCommand.ShowUnit(start.fightId, true, "관우관운장,\n화웅, 네 목을 가져가러 왔어.", 9),
            runtime.stage.consumeFightCommands().single(),
        )
    }

    @Test
    fun `startOper retains the source battle operation gate independently of initFight`() {
        val stage = ScenarioStage()

        stage.initFight()
        assertTrue(stage.fightInitialized)
        assertFalse(stage.battleOperationStarted)
        stage.startOperation()
        assertTrue(stage.battleOperationStarted)
    }

    @Test
    fun `setMaxRound updates the live authored turn cap`() {
        val stage = ScenarioStage()
        stage.setMaxRound(12)
        assertEquals(12, stage.battleMaxRounds)
    }

    @Test
    fun `S01 round-one cutscene reaches authored startOper after its last dialogue`() {
        val runtime = ScenarioInterpreter.load("S_01")
        runtime.setScriptVariables(
            (listOf(12, 20, 21, 30, 31, 40, 41, 50, 51, 52, 53, 54, 55, 56, 57, 58, 0, 1))
                .associateWith { 1 },
        )
        runtime.setBattleContext(ScenarioInterpreter.BattleScriptContext(round = 1, camp = 0))

        runtime.start("scene1")
        var dialogueCount = 0
        while (runtime.state == PlaybackState.DIALOGUE && dialogueCount++ < 20) runtime.advanceDialogue()

        assertTrue(runtime.stage.battleOperationStarted)
        assertEquals(PlaybackState.COMPLETE, runtime.state)
        assertFalse("stage.startOper" in runtime.unhandledCalls)
    }

    @Test
    fun `unit death ordering and hide actions follow recovered source rules`() {
        val units = listOf(
            BattleUnit("b", "b", Faction.ENEMY, 4, 3, hitPoints = 0),
            BattleUnit("a", "a", Faction.ENEMY, 7, 1, hitPoints = 0),
            BattleUnit("alive", "alive", Faction.ENEMY, 0, 0, hitPoints = 1),
        )

        assertEquals(listOf("a", "b"), UnitDeathPresentation.sortedDying(units).map { it.id })
        assertEquals(47, UnitDeathPresentation.hideAction(0, selfMaster = false))
        assertEquals(23, UnitDeathPresentation.hideAction(1, selfMaster = false))
        assertEquals(24, UnitDeathPresentation.hideAction(1, selfMaster = true))
        assertEquals(24, UnitDeathPresentation.hideAction(2, selfMaster = false))
    }

    @Test
    fun `repeated createEnemy claims distinct 80-slot blocks and keeps character lookup first`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(
            ScenarioUnitFaction.ENEMY,
            listOf(
                mapOf("i" to 0, "id" to 99, "x" to 1, "y" to 2),
                mapOf("i" to 1, "id" to 100, "x" to 2, "y" to 2),
            ),
        )
        stage.createBattleUnits(
            ScenarioUnitFaction.ENEMY,
            listOf(
                mapOf("i" to 0, "id" to 99, "x" to 8, "y" to 9),
                mapOf("i" to 2, "id" to 102, "x" to 9, "y" to 9, "yj" to 1),
            ),
        )

        assertEquals(listOf("ENEMY:0", "ENEMY:1", "ENEMY:80", "ENEMY:82"), stage.battleUnits.keys.toList())
        assertEquals(listOf(60, 61, 140, 142), stage.battleUnits.values.map { it.battleSlot })
        assertEquals(listOf("enemy-0", "enemy-1", "enemy-80", "enemy-82"), stage.battleUnits.values.map { it.battleId })
        assertEquals(1 to 2, stage.unit(99).let { it.x to it.y }, "_unitIds keeps the first character actor")

        stage.setUnitAi(99, 4, targetX = 7, targetY = 8)
        assertEquals(4, stage.battleUnits.getValue("ENEMY:0").ai)
        assertEquals(2, stage.battleUnits.getValue("ENEMY:80").ai)

        val battle = BattleScenarioFactory.fromScriptedUnits(stage.battleUnits.values, enemyMasterInstanceId = 99)
        assertEquals(setOf("enemy-0", "enemy-1", "enemy-80", "enemy-82"), battle.units.keys)
        assertEquals(140, battle.units.getValue("enemy-80").battleSlot)
        assertEquals(Faction.REINFORCEMENTS, battle.units.getValue("enemy-82").faction)
    }
}
