package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.JojoGame
import com.jojo.game.application.battle.Battle
import com.jojo.game.application.runtime.*
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState

/** Runs the real S91 Lua duel with production presentation completion callbacks. */
object LuaDuelSmokeLauncher {
    @JvmStatic fun main(args:Array<String>) {
        var arranged=false
        var nextTap=0f
        var started=false
        val driver=RuntimeBattleDriver { frame,probe ->
            check(frame.elapsed < 110f) { "S91 Lua duel timed out: ${probe.playback} ${probe.turnPhase}" }
            val screen=(Gdx.app.applicationListener as JojoGame).screen
            fun field(name:String)=screen.javaClass.getDeclaredField(name).also { it.isAccessible=true }.get(screen)
            val runtime=field("scriptRuntime") as ScenarioInterpreter
            val campaign=field("campaign") as CampaignState
            check(runtime.scriptLanguage=="lua")
            if((campaign.globalVariables["linchong.duel_win"] as? Number)?.toInt()==1) {
                check((campaign.globalVariables["linchong.b02_duel_completed"] as? Number)?.toInt()==1)
                println("LUA_DUEL_SMOKE_PASS S_91 Lua start/showStart/attack1/attack1/end completed; continuation flags set")
                Gdx.app.exit()
                return@RuntimeBattleDriver emptyList()
            }
            if(runtime.stage.activeFightId != null && !started) {
                started=true
                println("LUA_DUEL_PRESENTATION_STARTED")
            }
            if(frame.elapsed>=nextTap && (probe.playback==PlaybackState.DIALOGUE || probe.playback==PlaybackState.MODAL || probe.winConditionsOpen)) {
                val (x,y)=if(probe.winConditionsOpen) probe.winConditionButtonScreenX to probe.winConditionButtonScreenY
                    else probe.battleMenuButtonScreenX to probe.battleMenuButtonScreenY+200
                Gdx.input.inputProcessor?.let { it.touchDown(x,y,0,Input.Buttons.LEFT);it.touchUp(x,y,0,Input.Buttons.LEFT) }
                nextTap=frame.elapsed+.2f
            }
            if(!arranged && probe.bootstrapComplete && probe.turnPhase=="PLAYER_INPUT" && probe.playback==PlaybackState.COMPLETE && !probe.winConditionsOpen) {
                val battle=field("battle") as Battle
                val lin=battle.units.values.single { it.characterId==512 }
                val hong=battle.units.values.single { it.characterId==517 }
                lin.tileX=hong.tileX-1;lin.tileY=hong.tileY
                arranged=true
                println("LUA_DUEL_ADJACENT_TRIGGER")
                return@RuntimeBattleDriver listOf(RuntimeBattleCommand.EndTurn)
            }
            emptyList()
        }
        Lwjgl3Application(JojoGame(GameLaunchConfiguration(entryPoint=GameEntryPoint.BATTLE,
            initialScenario="S_91",initialScenarioExplicit=true,battleReturnScenario="R_91",runtimeBattleDriver=driver,automatedRun=true)),
            Lwjgl3ApplicationConfiguration().apply { setTitle("Lua S91 duel regression");setWindowedMode(1280,688);setForegroundFPS(60) })
    }
}
