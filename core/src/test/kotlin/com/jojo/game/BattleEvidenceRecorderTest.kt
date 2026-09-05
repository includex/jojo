package com.jojo.game
import com.jojo.game.presentation.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleEvidenceRecorderTest {
    @Test
    fun `full trace row remains byte stable for immutable snapshot`() {
        val view = BattleEvidenceView(
            frame = 1,
            elapsed = 1.5f,
            delta = .25f,
            round = 2,
            camp = 0,
            maxRounds = 99,
            playerCount = 1,
            friendCount = 2,
            enemyCount = 3,
            paused = true,
            ended = false,
            collocation = true,
            dialogue = true,
            dialogueRevision = 7,
            dialogueIdentity = "2:abc",
            dialogueSpeakerId = "1",
            dialogueText = "A\nB",
            phase = "PLAYER",
            script = "DIALOGUE",
            bootstrapBusy = listOf("x"),
            cameraX = 1f,
            cameraY = -2.5f,
            mapObjectRevision = 2,
            mapObjectsJson = "null",
            fightJson = "null",
            aiPresentationJson = "null",
            actionsJson = "\"move\"",
            unitsJson = "\"u\"",
            driverJson = "{}",
            observation = "obs\n",
            scriptEnded = true,
            scriptedOutcome = "WIN",
            resultFlow = "WIN_SAVE_PROMPT",
            modalKind = "reward",
            pendingScriptPasses = 2,
            pendingAiDeathPass = 1,
            postActionDeaths = false,
            pendingAiResolution = true,
            activeAiCamp = "ENEMY",
            roundLayer = true,
            turnSettlement = false,
            combatPresentation = true,
        )

        assertEquals(
            "{\"f\":1,\"t\":1.5,\"dt\":0.25,\"round\":2,\"camp\":0,\"maxRounds\":99,\"playerCount\":1,\"friendCount\":2,\"enemyCount\":3,\"paused\":true,\"end\":false,\"collocation\":true,\"dialogue\":1,\"dialogueRevision\":7,\"dialogueIdentity\":\"2:abc\",\"dialogueSpeakerId\":\"1\",\"dialogueText\":\"A\\nB\",\"phase\":\"PLAYER\",\"script\":\"DIALOGUE\",\"bootstrapBusy\":[\"x\"],\"camera\":[1,-2.5],\"mapObjectRevision\":2,\"mapObjects\":null,\"fight\":null,\"aiPresentation\":null,\"actions\":[\"move\"],\"units\":[\"u\"],\"driver\":{},\"observation\":\"obs\\n\",\"scriptEnded\":true,\"scriptedOutcome\":\"WIN\",\"resultFlow\":\"WIN_SAVE_PROMPT\",\"modalKind\":\"reward\",\"resultCallbacks\":{\"pendingScriptPasses\":2,\"pendingAiDeathPass\":1,\"postActionDeaths\":false,\"pendingAiResolution\":true,\"activeAiCamp\":\"ENEMY\",\"roundLayer\":true,\"turnSettlement\":false,\"combatPresentation\":true}}",
            BattleEvidenceRecorder.frame(view),
        )
    }
}
