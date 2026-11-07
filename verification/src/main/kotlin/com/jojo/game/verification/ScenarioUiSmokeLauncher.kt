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

object ScenarioUiSmokeLauncher {
 @JvmStatic fun main(args:Array<String>) {
  val out=args.firstOrNull() ?: "verification/build/scenario-ui"
  val capturedBackgrounds=mutableSetOf<Int>()
  var phase=0;var ticks=0;var deadline=0;var stable=0;var nextTap=0f
  var background=-1;var backgroundFrames=0
  fun capture(name:String) {val p=ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.backBufferWidth,Gdx.graphics.backBufferHeight);val f=Gdx.files.absolute("$out/$name.png");f.parent().mkdirs();PixmapIO.writePNG(f,p,-1,true);p.dispose()}
  val driver=RuntimeScenarioDriver { frame ->
   check(frame.elapsedSeconds<180)
   ticks++
   val screen=(Gdx.app.applicationListener as JojoGame).screen
   val r=screen.javaClass.getDeclaredField("playback").also{it.isAccessible=true}.get(screen) as ScenarioInterpreter
   stable=if(frame.dialogueTextComplete)stable+1 else 0
   backgroundFrames=if(background==r.stage.backgroundId)backgroundFrames+1 else 0
   background=r.stage.backgroundId
   // Commands run before rendering: hold the same actual dialogue for several rendered frames.
   if(frame.playback==PlaybackState.DIALOGUE && stable>=4 && backgroundFrames>=4 && phase !in 1..3 && capturedBackgrounds.add(background)) capture("background-$background")
   if(phase==0&&stable>=4&&backgroundFrames>=4&&r.currentDialogue?.speakerId=="512") {capture("dialogue-scenario");phase=1;deadline=ticks+3;emptyList()}
   else if(phase==1&&ticks>=deadline) {r.stage.setMenuVisible(true);screen.javaClass.getDeclaredMethod("openHallUnitInfo",Int::class.javaPrimitiveType!!).also{it.isAccessible=true}.invoke(screen,512);phase=2;deadline=ticks+3;emptyList()}
   else if(phase==2&&ticks>=deadline) {val layer=screen.javaClass.getDeclaredMethod("getHallUnitInfoLayer").also{it.isAccessible=true}.invoke(screen) as com.jojo.game.presentation.shared.overlay.UnitInfoLayer;check(layer.ref().attached);capture("info-hall");
    val close=com.jojo.game.presentation.battle.overlay.BattleUnitInfoLayout.close
    val x=((close.x+close.width/2f)*Gdx.graphics.width/1488.3721f).toInt()
    val y=(Gdx.graphics.height-(close.y+close.height/2f)*Gdx.graphics.height/800f).toInt()
    Gdx.input.inputProcessor?.let{it.touchDown(x,y,0,Input.Buttons.LEFT);it.touchUp(x,y,0,Input.Buttons.LEFT)}
    check(!layer.ref().attached){"Hall close button did not dismiss unit info"}
    r.stage.setMenuVisible(false);phase=3;deadline=ticks+4;emptyList()}
   else if(phase==3&&ticks>=deadline) {phase=4;emptyList()}
   else if(phase in 1..3) emptyList()
   else if(frame.elapsedSeconds>=nextTap) {nextTap=frame.elapsedSeconds+.15f;when(frame.playback){PlaybackState.DIALOGUE->if(frame.dialogueTextComplete&&stable>=5)listOf(RuntimeScenarioCommand.AdvanceDialogue)else emptyList();PlaybackState.MODAL->if(frame.modalTextComplete)listOf(RuntimeScenarioCommand.ResumeModal)else emptyList();PlaybackState.CHOICE->listOf(RuntimeScenarioCommand.ConfirmChoice);else->emptyList()}}
   else emptyList()
  }
  val battleDriver=RuntimeBattleDriver {frame,probe->
   check(frame.elapsed<100)
   if(probe.playback==PlaybackState.DIALOGUE||probe.winConditionsOpen||probe.playback==PlaybackState.MODAL){
    val x=if(probe.winConditionsOpen)probe.winConditionButtonScreenX else probe.battleMenuButtonScreenX
    val y=if(probe.winConditionsOpen)probe.winConditionButtonScreenY else probe.battleMenuButtonScreenY+200
    Gdx.input.inputProcessor?.let{it.touchDown(x,y,0,Input.Buttons.LEFT);it.touchUp(x,y,0,Input.Buttons.LEFT)}
   } else if(probe.bootstrapComplete&&probe.turnPhase=="PLAYER_INPUT") {check(phase==4);check(capturedBackgrounds.containsAll(listOf(991,992,993,994)));println("SCENARIO_UI_SMOKE_PASS dialogue+hall+S90");Gdx.app.exit()}
   emptyList()
  }
  Lwjgl3Application(JojoGame(GameLaunchConfiguration(entryPoint=GameEntryPoint.SCENARIO,initialScenario="R_90",initialScenarioExplicit=true,runtimeScenarioDriver=driver,runtimeBattleDriver=battleDriver,automatedRun=true)),Lwjgl3ApplicationConfiguration().apply{setTitle("임충전 대화·인물 UI 검증");setWindowedMode(1280,688);setForegroundFPS(60)})
 }
}
