package com.jojo.game.verification.campaign

import com.jojo.game.application.scenario.ScenarioBattleScriptContext
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.domain.scenario.ScenarioScriptPresentationRequest
import kotlin.test.*

/** Branch fixtures exercise production Python-AST scripts; they do not claim combat playability. */
class LinchongNativeScenarioTest {
    private data class Fixture(val campaign: CampaignState, val script: ScenarioInterpreter, val opening: List<ScenarioScriptPresentationRequest>)
    private fun settle(r:ScenarioInterpreter,choice:Int=0): List<ScenarioScriptPresentationRequest> {
        val presentations = mutableListOf<ScenarioScriptPresentationRequest>()
        var steps=0
        while(r.state!=PlaybackState.COMPLETE) {
            check(steps++<4000){"Native mod script did not settle: ${r.state}"}
            when(r.state) {
                PlaybackState.DIALOGUE->r.advanceDialogue()
                PlaybackState.CHOICE->{r.selectChoice(choice);r.confirmChoice()}
                PlaybackState.DELAY->{presentations += r.stage.consumeScriptPresentationRequests();r.skipDelay()}
                PlaybackState.MODAL->r.resumeModal()
                PlaybackState.COMPLETE->Unit
            }
        }
        return presentations
    }
    private fun chapter(index:Int, external:Boolean=false):Fixture {
        val campaign=CampaignState().also{it.reset()}
        val route=ScenarioInterpreter.load("R_90",campaign);route.start("scene0");settle(route)
        val r=ScenarioInterpreter.load("S_${90+index}",campaign)
        if (external) r.enableExternalBattlePresentation()
        r.start("scene0");if (external) r.completeBattleBackgroundLoad();val opening=settle(r)
        return Fixture(campaign,r,opening)
    }
    private fun tick(f:Fixture,round:Int=1,camp:Int=0,hp:Map<Int,Int> = emptyMap(),positions:Map<Int,Pair<Int,Int>> = emptyMap(),choice:Int=0): List<ScenarioScriptPresentationRequest> {
        val units=f.script.stage.battleUnits.values.toList()
        val points=units.associate{it.characterId to (it.x to it.y)}+positions
        val attrs=(512..574).associateWith{mapOf(7 to (hp[it]?:100))}
        f.script.setBattleContext(ScenarioBattleScriptContext(round,camp,positions=points,activeCharacterIds=points.keys,attributes=attrs))
        f.script.start("scene1");return settle(f.script,choice)
    }
    private fun won(f:Fixture) {
        assertTrue(f.script.stage.battleEndedByScript)
        assertNotEquals(BattleOutcome.ENEMY_VICTORY,f.script.stage.scriptedBattleOutcome)
        assertNotNull(f.script.stage.rewardRequest)
    }
    @Test fun `dock marker appears with opening objective and does not repeat on turns`() {
        val f=chapter(5,external=true)
        assertTrue(ScenarioScriptPresentationRequest.RectangleHighlight(18,7,18,8) in f.opening)
        for (round in 1..3) for (camp in listOf(0,2)) {
            val requests=tick(f,round=round,camp=camp)
            assertTrue(requests.filterIsInstance<ScenarioScriptPresentationRequest.RectangleHighlight>().isEmpty())
        }
    }
    @Test fun `rescue announces exit once without repeating on later player turns`() {
        val f=chapter(0,external=true)
        val exit=ScenarioScriptPresentationRequest.RectangleHighlight(14,5,14,6)
        assertTrue(exit in f.opening)
        assertTrue(exit in tick(f,camp=2))
        for (round in 2..3) {
            val requests=tick(f,round=round)
            assertTrue(requests.filterIsInstance<ScenarioScriptPresentationRequest.RectangleHighlight>().isEmpty())
        }
    }
    @Test fun `displayed final round remains playable until next round and permits last turn victory`() {
        val f=chapter(0)
        val limit=f.script.stage.battleMaxRounds
        assertEquals(9,limit)
        tick(f,camp=2)
        for (camp in listOf(0,1,2)) {
            tick(f,round=limit,camp=camp)
            assertFalse(f.script.stage.battleEndedByScript)
        }
        tick(f,round=limit+1)
        assertEquals(BattleOutcome.ENEMY_VICTORY,f.script.stage.scriptedBattleOutcome)
        val lastTurnWin=chapter(0)
        tick(lastTurnWin,camp=2)
        tick(lastTurnWin,round=limit,positions=mapOf(512 to (14 to 5)))
        won(lastTurnWin)
    }
    @Test fun `R90 grants compact native starting gear once and preserves later equipment`() {
        val c=CampaignState().also{it.reset()}
        val first=ScenarioInterpreter.load("R_90",c);first.start("scene0");settle(first)
        assertTrue(512 in c.joinedUnits)
        assertEquals(setOf(60,102),c.inventory.equippedItems().filter{it.unitId==512}.map{it.itemId}.toSet())
        // Apply a later loadout through the same native stage equipment API used by content.
        first.stage.setJoinEquip(512,66,2,34,3,30)
        val upgraded=c.inventory.equipmentFor(512)
        val reentered=ScenarioInterpreter.load("R_90",c);reentered.start("scene0");settle(reentered)
        assertEquals(upgraded,c.inventory.equipmentFor(512))
        assertEquals(setOf(64,102,137),c.inventory.equippedItems().filter{it.unitId==512}.map{it.itemId}.toSet())
    }
    @Test fun `all eight native battle starts place protagonist and select their own map`() {
        repeat(8){i->val f=chapter(i);assertEquals(58+i,f.script.stage.battleMapIndex);assertTrue(f.script.stage.battleUnits.values.any{it.characterId==512});assertTrue(f.script.stage.battleOperationStarted);f.script.stage.battleUnits.values.filter{it.characterId in 566..570}.forEach{assertEquals(2,it.ai)};assertFalse(f.script.stage.battleEndedByScript)}
    }
    @Test fun `first enemy phase rescues Lin and reaching east exit clears B01`() {
        val f=chapter(0);tick(f,camp=2)
        assertTrue(f.script.stage.battleUnits.values.any{it.characterId==513})
        assertEquals(1,(f.campaign.globalVariables["linchong.b01_rescued"] as Number).toInt())
        tick(f,round=2,positions=mapOf(512 to (14 to 5)));won(f)
        assertEquals(2,f.campaign.inventory.items[200]);assertTrue(137 in f.campaign.inventory.items)
    }
    @Test fun `B02 HP surrender grants native rewards and optional skip routes to R93`() {
        for(pick in 0..1){val f=chapter(1);tick(f,hp=mapOf(517 to 42),choice=pick);won(f);assertTrue(202 in f.campaign.inventory.items);assertTrue(512 to 101 in f.campaign.extraMagic);assertEquals(if(pick==1)186 else null,f.script.stage.sceneJumpStage)}
    }
    @Test fun `EX01 boss defeat requires warehouse alive`() {
        val good=chapter(2);tick(good,hp=mapOf(533 to 0,568 to 1));won(good)
        val lost=chapter(2);tick(lost,hp=mapOf(533 to 0,568 to 0));assertEquals(BattleOutcome.ENEMY_VICTORY,lost.script.stage.scriptedBattleOutcome)
    }
    @Test fun `B03 rescue choice accepts or declines permanent recruitment`() {
        for(pick in 0..1) {
            val f=chapter(3)
            tick(f,round=3,positions=mapOf(512 to (9 to 9),514 to (10 to 9)))
            assertEquals(1,(f.campaign.globalVariables["linchong.b03_hu_rescued"] as Number).toInt())
            tick(f,round=3,hp=mapOf(540 to 0,566 to 1,567 to 0),choice=pick);won(f)
            assertEquals(pick==0,514 in f.campaign.joinedUnits)
        }
    }
    @Test fun `B04 reveal and rescue flags lead to Jin recruitment on victory`() {
        val f=chapter(4);tick(f,round=3,positions=mapOf(512 to (7 to 8),515 to (8 to 8)))
        assertEquals(1,(f.campaign.globalVariables["linchong.b04_fire_revealed"] as Number).toInt())
        assertEquals(1,(f.campaign.globalVariables["linchong.b04_jin_rescued"] as Number).toInt())
        tick(f,round=4,hp=mapOf(524 to 0));won(f);assertTrue(515 in f.campaign.joinedUnits)
    }
    @Test fun `B05 needs two distinct allies at dock and hidden waterway controls optional route`() {
        val f=chapter(5);tick(f,positions=mapOf(512 to (9 to 11)))
        assertEquals(1,(f.campaign.globalVariables["linchong.b05_hidden_waterway"] as Number).toInt())
        tick(f,round=2,positions=mapOf(512 to (18 to 7),516 to (17 to 7)));assertFalse(f.script.stage.battleEndedByScript)
        tick(f,round=2,positions=mapOf(512 to (18 to 7),516 to (18 to 8)),choice=1);won(f);assertEquals(194,f.script.stage.sceneJumpStage)
    }
    @Test fun `EX02 cargo capture and final nonlethal threshold clear without permanent Yang recruitment`() {
        val boat=chapter(6);tick(boat,hp=mapOf(559 to 0),positions=mapOf(512 to (11 to 7)));won(boat)
        val last=chapter(7);tick(last,hp=mapOf(518 to 63));won(last);assertFalse(518 in last.campaign.joinedUnits)
        assertEquals(1,(last.campaign.globalVariables["linchong.campaign_complete"] as Number).toInt())
    }
    @Test fun `completed final route stays in native completion menu instead of restarting battle`() {
        val f=chapter(7);tick(f,hp=mapOf(518 to 63));won(f)
        val terminal=ScenarioInterpreter.load("R_97",f.campaign);terminal.start("scene0");settle(terminal)
        assertTrue(terminal.stage.menuVisible);assertFalse(terminal.stage.battleEndedByScript);assertEquals(1,terminal.stage.endingId)
    }
    @Test fun `protagonist defeat wins priority over simultaneous objective completion`() {
        val f=chapter(7);tick(f,hp=mapOf(512 to 0,518 to 1));assertEquals(BattleOutcome.ENEMY_VICTORY,f.script.stage.scriptedBattleOutcome);assertNull(f.script.stage.rewardRequest)
    }
}
