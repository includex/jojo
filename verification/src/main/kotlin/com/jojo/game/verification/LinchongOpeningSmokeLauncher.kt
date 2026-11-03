package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.*
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.scenario.PlaybackState

/** Replays the authored R90 opening, captures every background, and enters the real S90 battle. */
object LinchongOpeningSmokeLauncher {
    @JvmStatic fun main(args: Array<String>) {
        val output=args.firstOrNull() ?: "verification/build/linchong-opening"
        val backgrounds=linkedSetOf<Int>()
        val dialogue=linkedSetOf<String>()
        var nextTap=0f
        val scenarioDriver=RuntimeScenarioDriver { frame ->
            check(frame.elapsedSeconds<150f) { "Opening stalled at ${frame.playback}" }
            val screen=(Gdx.app.applicationListener as JojoGame).screen
            val runtime=screen.javaClass.getDeclaredField("playback").also { it.isAccessible=true }.get(screen) as ScenarioInterpreter
            check(runtime.scriptLanguage=="lua")
            val variant=runtime.stage.backgroundVariant
            if(frame.playback==PlaybackState.DIALOGUE && frame.dialogueTextComplete) {
                dialogue+=runtime.currentDialogueSourceText.orEmpty()
                if(variant in 991..994 && backgrounds.add(variant)) {
                    capture("$output/scene-$variant.png")
                    println("OPENING_SCENE_CAPTURE $variant")
                }
            }
            if(frame.elapsedSeconds<nextTap) emptyList() else {
                nextTap=frame.elapsedSeconds+.15f
                when(frame.playback) {
                    PlaybackState.DIALOGUE -> if(frame.dialogueTextComplete) listOf(RuntimeScenarioCommand.AdvanceDialogue) else emptyList()
                    PlaybackState.MODAL -> if(frame.modalTextComplete) listOf(RuntimeScenarioCommand.ResumeModal) else emptyList()
                    PlaybackState.CHOICE -> listOf(RuntimeScenarioCommand.ConfirmChoice)
                    else -> emptyList()
                }
            }
        }
        var battleCaptured=false
        var nextBattleTap=0f
        val battleDriver=RuntimeBattleDriver { frame,probe ->
            check(frame.elapsed<90f)
            if(!battleCaptured && probe.playback==PlaybackState.DIALOGUE) {
                check(backgrounds.toList()==listOf(991,992,993,994)) { "Opening backgrounds/order: $backgrounds" }
                check(dialogue.size>=15) { "Opening did not play enough authored dialogue: ${dialogue.size}" }
                capture("$output/battle-S90.png")
                Gdx.files.absolute("$output/dialogue.txt").writeString(dialogue.joinToString("\n\n"),false,"UTF-8")
                battleCaptured=true
            }
            if(frame.elapsed>=nextBattleTap && (probe.playback==PlaybackState.DIALOGUE || probe.playback==PlaybackState.MODAL || probe.winConditionsOpen)) {
                val (x,y)=if(probe.winConditionsOpen) probe.winConditionButtonScreenX to probe.winConditionButtonScreenY
                    else probe.battleMenuButtonScreenX to probe.battleMenuButtonScreenY+200
                Gdx.input.inputProcessor?.let { it.touchDown(x,y,0,Input.Buttons.LEFT);it.touchUp(x,y,0,Input.Buttons.LEFT) }
                nextBattleTap=frame.elapsed+.2f
            }
            if(battleCaptured && probe.bootstrapComplete && probe.turnPhase=="PLAYER_INPUT" && probe.playback==PlaybackState.COMPLETE && !probe.winConditionsOpen) {
                capture("$output/battle-ready.png")
                println("LINCHONG_OPENING_SMOKE_PASS backgrounds=$backgrounds dialogue=${dialogue.size} S90=ready")
                Gdx.app.exit()
            }
            emptyList()
        }
        Lwjgl3Application(JojoGame(GameLaunchConfiguration(entryPoint=GameEntryPoint.SCENARIO,
            initialScenario="R_90",initialScenarioExplicit=true,runtimeScenarioDriver=scenarioDriver,
            runtimeBattleDriver=battleDriver,automatedRun=true)), Lwjgl3ApplicationConfiguration().apply {
                setTitle("임충전 · 분리된 도입부 검증");setWindowedMode(1280,688);setForegroundFPS(60)
            })
    }
    private fun capture(path:String) {
        val file=Gdx.files.absolute(path);file.parent().mkdirs()
        val pixels=ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.backBufferWidth,Gdx.graphics.backBufferHeight)
        try { PixmapIO.writePNG(file,pixels,-1,true) } finally { pixels.dispose() }
    }
}
