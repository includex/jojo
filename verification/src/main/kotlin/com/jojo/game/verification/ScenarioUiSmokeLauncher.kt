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
  var phase=0;var ticks=0;var deadline=0;var stable=0;var nextTap=0f
  fun capture(name:String) {val p=ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.backBufferWidth,Gdx.graphics.backBufferHeight);val f=Gdx.files.absolute("$out/$name.png");f.parent().mkdirs();PixmapIO.writePNG(f,p,-1,true);p.dispose()}
  val driver=RuntimeScenarioDriver { frame ->
   check(frame.elapsedSeconds<180)
   ticks++
   val screen=(Gdx.app.applicationListener as JojoGame).screen
   val r=screen.javaClass.getDeclaredField("playback").also{it.isAccessible=true}.get(screen) as ScenarioInterpreter
   stable=if(frame.dialogueTextComplete)stable+1 else 0
   if(phase==0&&stable>=2&&r.currentDialogue?.speakerId=="512") {capture("dialogue-scenario");phase=1;deadline=ticks+3;emptyList()}
   else if(phase==1&&ticks>=deadline) {r.stage.setMenuVisible(true);screen.javaClass.getDeclaredMethod("openHallUnitInfo",Int::class.javaPrimitiveType!!).also{it.isAccessible=true}.invoke(screen,512);phase=2;deadline=ticks+3;emptyList()}
   else if(phase==2&&ticks>=deadline) {val layer=screen.javaClass.getDeclaredMethod("getHallUnitInfoLayer").also{it.isAccessible=true}.invoke(screen) as com.jojo.game.presentation.shared.overlay.UnitInfoLayer;check(layer.ref().attached);capture("info-hall");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);phase=3;emptyList()}
   else if(phase in 1..2) emptyList()
   else if(frame.elapsedSeconds>=nextTap) {nextTap=frame.elapsedSeconds+.15f;when(frame.playback){PlaybackState.DIALOGUE->if(frame.dialogueTextComplete)listOf(RuntimeScenarioCommand.AdvanceDialogue)else emptyList();PlaybackState.MODAL->if(frame.modalTextComplete)listOf(RuntimeScenarioCommand.ResumeModal)else emptyList();PlaybackState.CHOICE->listOf(RuntimeScenarioCommand.ConfirmChoice);else->emptyList()}}
   else emptyList()
  }
  val battleDriver=RuntimeBattleDriver {frame,probe->
   check(frame.elapsed<100)
   if(probe.playback==PlaybackState.DIALOGUE||probe.winConditionsOpen||probe.playback==PlaybackState.MODAL){
    val x=if(probe.winConditionsOpen)probe.winConditionButtonScreenX else probe.battleMenuButtonScreenX
    val y=if(probe.winConditionsOpen)probe.winConditionButtonScreenY else probe.battleMenuButtonScreenY+200
    Gdx.input.inputProcessor?.let{it.touchDown(x,y,0,Input.Buttons.LEFT);it.touchUp(x,y,0,Input.Buttons.LEFT)}
   } else if(probe.bootstrapComplete&&probe.turnPhase=="PLAYER_INPUT") {check(phase==3);println("SCENARIO_UI_SMOKE_PASS dialogue+hall+S90");Gdx.app.exit()}
   emptyList()
  }
  Lwjgl3Application(JojoGame(GameLaunchConfiguration(entryPoint=GameEntryPoint.SCENARIO,initialScenario="R_90",initialScenarioExplicit=true,runtimeScenarioDriver=driver,runtimeBattleDriver=battleDriver,automatedRun=true)),Lwjgl3ApplicationConfiguration().apply{setTitle("임충전 대화·인물 UI 검증");setWindowedMode(1280,688);setForegroundFPS(60)})
 }
}
