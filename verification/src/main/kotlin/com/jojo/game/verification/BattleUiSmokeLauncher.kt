package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.*
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.battle.overlay.*

object BattleUiSmokeLauncher {
 @JvmStatic fun main(args:Array<String>) {
  val out=args.firstOrNull() ?: "verification/build/battle-ui"
  val width=args.getOrNull(1)?.toInt() ?:1280
  val height=args.getOrNull(2)?.toInt() ?:688
  var phase=0;var frames=0;var next=0f;var dialogueCaptured=false;var stableDialogue=0;var phaseFrames=0
  fun capture(name:String) { val p=ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.backBufferWidth,Gdx.graphics.backBufferHeight);val f=Gdx.files.absolute("$out/$name.png");f.parent().mkdirs();PixmapIO.writePNG(f,p,-1,true);p.dispose() }
  val driver=RuntimeBattleDriver { frame,probe ->
   check(frame.elapsed<100f) { "UI smoke timeout phase=$phase" }
   stableDialogue=if(probe.dialogueTextComplete)stableDialogue+1 else 0
   if(stableDialogue>=2 && probe.dialogueTextComplete && probe.dialogueSpeakerId=="512" && !dialogueCaptured) { capture("dialogue-battle");dialogueCaptured=true }
   if(probe.playback==PlaybackState.DIALOGUE ||probe.winConditionsOpen||probe.playback==PlaybackState.MODAL) {
    if(frame.elapsed>=next) {
     val x=if(probe.winConditionsOpen)probe.winConditionButtonScreenX else probe.battleMenuButtonScreenX
     val y=if(probe.winConditionsOpen)probe.winConditionButtonScreenY else probe.battleMenuButtonScreenY+200
     Gdx.input.inputProcessor?.let{it.touchDown(x,y,0,Input.Buttons.LEFT);it.touchUp(x,y,0,Input.Buttons.LEFT)};next=frame.elapsed+.15f
    }
   } else if(probe.bootstrapComplete&&probe.turnPhase=="PLAYER_INPUT"&&++frames>8) {
    val screen=(Gdx.app.applicationListener as JojoGame).screen
    fun invoke(name:String,vararg values:Int) { screen.javaClass.getDeclaredMethod(name,*Array(values.size){Int::class.javaPrimitiveType!!}).also{it.isAccessible=true}.invoke(screen,*values.toTypedArray()) }
    fun overlay()=screen.javaClass.getDeclaredField("unitInfoOverlay").also{it.isAccessible=true}.get(screen) as Any
    if (++phaseFrames % 3 != 0) return@RuntimeBattleDriver emptyList()
    when(phase++) {
     0->{ val lin=probe.battle.snapshot.units.single{it.characterId==512};invoke("handleTileClick",lin.x,lin.y);invoke("handleTileClick",lin.x,lin.y) }
     1->{capture("command");invoke("dispatchBattleCommand",7)}
     2->{check((overlay().javaClass.getMethod("view").invoke(overlay()) as BattleUnitInfoOverlayView?)?.unit?.characterId==512);capture("info-base");val r=BattleUnitInfoLayout.tabs[2]; val vp=screen.javaClass.getDeclaredField("viewport").also{it.isAccessible=true}.get(screen) as com.badlogic.gdx.utils.viewport.Viewport;val point=vp.project(com.badlogic.gdx.math.Vector2(r.x+r.width/2,r.y+r.height/2));val x=point.x.toInt();val y=(Gdx.graphics.height-point.y).toInt();Gdx.input.inputProcessor?.let{it.touchDown(x,y,0,Input.Buttons.LEFT);it.touchUp(x,y,0,Input.Buttons.LEFT)}}
     3->{check((overlay().javaClass.getMethod("view").invoke(overlay()) as BattleUnitInfoOverlayView?)?.tab==2) { "Equipment tab input failed" };capture("info-equipment");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE)}
     4->{check(!(overlay().javaClass.getMethod("isVisible").invoke(overlay()) as Boolean));capture("command-return");invoke("openUnitInfoLayer",522)}
     5->{check((overlay().javaClass.getMethod("view").invoke(overlay()) as BattleUnitInfoOverlayView?)?.unit?.characterId==522);capture("info-enemy");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);invoke("openBattleMenu");invoke("handleBattleMenuTap",1)}
     6->{capture("save");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);invoke("openBattleMenu");invoke("handleBattleMenuTap",2)}
     7->{capture("load");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);invoke("openBattleMenu");invoke("handleBattleMenuTap",3)}
     8->{capture("settings");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);invoke("openForcesListLayer")}
     9->{capture("forces");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);invoke("openHelperLayer")}
     10->{capture("help");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE)}
     11->{capture("hud");val f=screen.javaClass.getDeclaredField("battleHudPanel").also{it.isAccessible=true};(f.get(screen) as BattleHudPanelController).toggle()}
     12->{capture("hud-collapsed");invoke("dispatchBattleCommand",6)
      val campaign=screen.javaClass.getDeclaredField("campaign").also{it.isAccessible=true}.get(screen) as com.jojo.game.domain.campaign.CampaignState
      campaign.inventory.addUnitItem(512,216,1)
      val lin=probe.battle.snapshot.units.single{it.characterId==512};invoke("handleTileClick",lin.x,lin.y);invoke("handleTileClick",lin.x,lin.y);invoke("dispatchBattleCommand",2)}
     13->{capture("items");val layer=screen.javaClass.getDeclaredField("usePropertyLayer").also{it.isAccessible=true}.get(screen) as UsePropertyLayer;layer.touchStart(0);layer.update(1.1f)}
     14->{capture("item-detail");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);invoke("dispatchBattleCommand",1)}
     15->{capture("magic");val layer=screen.javaClass.getDeclaredField("magickListLayer").also{it.isAccessible=true}.get(screen) as com.jojo.game.presentation.shared.overlay.MagicUiList;layer.start(0)
      screen.javaClass.getDeclaredField("magickPressedRow").also{it.isAccessible=true}.set(screen,0)
      screen.javaClass.getDeclaredField("magickPressedAt").also{it.isAccessible=true}.setFloat(screen,-100f)}
     16->{capture("magic-detail");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);invoke("dispatchBattleCommand",6)
      val battle=screen.javaClass.getDeclaredField("battle").also{it.isAccessible=true}.get(screen) as com.jojo.game.application.battle.Battle
      val lin=battle.units.values.single{it.characterId==512}
      val dest=battle.movement.reachableTiles(lin.id).keys.first{(x,y)->kotlin.math.abs(x-lin.tileX)+kotlin.math.abs(y-lin.tileY)==1&&battle.unitAt(x,y)==null}
      invoke("handleTileClick",lin.tileX,lin.tileY);invoke("handleTileClick",dest.first,dest.second)
      val tiles=screen.javaClass.getDeclaredMethod("selectableAreaTiles").also{it.isAccessible=true}.invoke(screen) as List<*>
      check(tiles.isEmpty()){ "Ranges visible while moving" }}
     17->{val moving=screen.javaClass.getDeclaredField("movementAnimation").also{it.isAccessible=true}.get(screen)
      if(moving!=null)phase-- else{capture("move-arrived");println("BATTLE_UI_SMOKE_PASS ${width}x$height ranges=hidden-during-move");Gdx.app.exit()}}

    }
   }
   emptyList()
  }
  Lwjgl3Application(JojoGame(GameLaunchConfiguration(entryPoint=GameEntryPoint.BATTLE,initialScenario="S_90",initialScenarioExplicit=true,runtimeBattleDriver=driver,automatedRun=true)),Lwjgl3ApplicationConfiguration().apply{setTitle("임충전 UI 검증");setWindowedMode(width,height);setForegroundFPS(60)})
 }
}
