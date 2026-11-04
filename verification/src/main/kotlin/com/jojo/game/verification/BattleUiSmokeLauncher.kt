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
  var phase=0;var frames=0;var next=0f;var dialogueCaptured=false;var stableDialogue=0;var phaseFrames=0;var retainedTerrain:BattleHudTerrainView?=null
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
    fun field(name:String)=screen.javaClass.getDeclaredField(name).also{it.isAccessible=true}
    val vp=field("viewport").get(screen) as com.badlogic.gdx.utils.viewport.Viewport
    fun pointer(worldX:Float,worldY:Float) {
     val point=vp.project(com.badlogic.gdx.math.Vector2(worldX,worldY))
     val x=point.x.toInt();val y=(Gdx.graphics.height-point.y).toInt()
     check(x in 0 until Gdx.graphics.width && y in 0 until Gdx.graphics.height) { "HUD test pointer outside viewport: $x,$y" }
     Gdx.input.setCursorPosition(x,y)
    }
    fun pointerTile(x:Int,y:Int) {
     val left=field("boardLeft").getFloat(screen);val bottom=field("boardBottom").getFloat(screen);val size=field("boardTile").getFloat(screen)
     pointer(left+(x+.5f)*size,bottom-y*size+size/2f)
    }
    fun hud()=screen.javaClass.getDeclaredMethod("battleHudView").also{it.isAccessible=true}.invoke(screen) as BattleHudContextView
    fun battle()=field("battle").get(screen) as com.jojo.game.application.battle.Battle
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
     11->{
      invoke("dispatchBattleCommand",6)
      val settings=BattleHudPanelLayout.buttons(vp.worldWidth,vp.worldHeight,true).single{it.action==BattleHudPanelController.Action.SETTINGS}.rect
      val point=vp.project(com.badlogic.gdx.math.Vector2(settings.x+settings.width/2,settings.y+settings.height/2))
      val px=point.x.toInt();val py=(Gdx.graphics.height-point.y).toInt()
      Gdx.input.inputProcessor?.let{it.touchDown(px,py,0,Input.Buttons.LEFT);it.touchUp(px,py,0,Input.Buttons.LEFT)}
      val settingsOverlay=screen.javaClass.getDeclaredMethod("getSettingsOverlay").also{it.isAccessible=true}.invoke(screen)
      check(settingsOverlay.javaClass.getMethod("view").invoke(settingsOverlay)!=null) { "Top HUD environment button did not open settings" }
      Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE)
      val lin=battle().units.values.single{it.characterId==512}
      invoke("handleTileClick",lin.tileX,lin.tileY)
      capture("hud")
      pointerTile(lin.tileX,lin.tileY)
     }
     12->{
      check(hud().unit?.name==battle().units.values.single{it.characterId==512}.displayName) { "Hovering Lin did not show Lin in HUD: ${hud()}" }
      check(hud().unit?.portrait!=null) { "Hovered protagonist portrait missing" }
      check(hud().terrain!=null)
      capture("hud-hover-unit")
      val enemy=battle().units.values.single{it.characterId==522}
      pointerTile(enemy.tileX,enemy.tileY)
     }
     13->{
      val enemy=battle().units.values.single{it.characterId==522}
      check(hud().unit?.name==enemy.displayName) { "HUD kept old unit while hovering enemy: ${hud()}" }
      check(hud().terrain?.basis?.contains(enemy.displayName)==true)
      capture("hud-hover-enemy")
      val lin=battle().units.values.single{it.characterId==512}
      val empty=listOf(0 to 1,0 to -1,1 to 0,-1 to 0).map{lin.tileX+it.first to lin.tileY+it.second}.first{(x,y)->x>=0&&y>=0&&battle().unitAt(x,y)==null}
      pointerTile(empty.first,empty.second)
     }
     14->{
      check(hud().unit==null) { "HUD retained a character on empty terrain: ${hud()}" }
      check(hud().terrain!=null) { "Empty tile terrain information missing" }
      check(hud().terrain?.basis?.contains(battle().units.values.single{it.characterId==512}.displayName)==true) { "Terrain lost selected unit basis: ${hud()}" }
      retainedTerrain=null
      capture("hud-hover-terrain")
      val controller=field("battleHudPanel").get(screen) as BattleHudPanelController
      val panel=BattleHudPanelLayout.panel(vp.worldWidth,vp.worldHeight,controller.expanded)
      pointer(panel.x+16f,panel.y+16f)
     }
     15->{
      check(hud().unit==null&&hud().terrain==retainedTerrain) { "HUD surface must clear hover context: ${hud()}" }
      check(hud().terrain?.basis==retainedTerrain?.basis) { "HUD changed terrain calculation basis" }
      capture("hud-empty-context")
      invoke("openHelperLayer")
      val enemy=battle().units.values.single{it.characterId==522}
      pointerTile(enemy.tileX,enemy.tileY)
     }
     16->{
      check(hud().unit==null&&hud().terrain==retainedTerrain) { "A modal must freeze battlefield context while cursor moves to another unit: ${hud()}" }
      check(hud().terrain?.basis==retainedTerrain?.basis) { "Modal changed terrain calculation basis" }
      capture("hud-modal-context")
      Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE)
      (field("battleHudPanel").get(screen) as BattleHudPanelController).toggle()
      println("BATTLE_HUD_HOVER_PASS Lin -> Dong -> empty terrain; portrait present; HUD clears context; modal freezes context")
     }
     17->{capture("hud-collapsed");invoke("dispatchBattleCommand",6)
      val campaign=screen.javaClass.getDeclaredField("campaign").also{it.isAccessible=true}.get(screen) as com.jojo.game.domain.campaign.CampaignState
      campaign.inventory.addUnitItem(512,216,1)
      val lin=probe.battle.snapshot.units.single{it.characterId==512};invoke("handleTileClick",lin.x,lin.y);invoke("handleTileClick",lin.x,lin.y);invoke("dispatchBattleCommand",2)}
     18->{capture("items");val layer=screen.javaClass.getDeclaredField("usePropertyLayer").also{it.isAccessible=true}.get(screen) as UsePropertyLayer;layer.touchStart(0);layer.update(1.1f)}
     19->{capture("item-detail");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);invoke("dispatchBattleCommand",1)}
     20->{capture("magic");val layer=screen.javaClass.getDeclaredField("magickListLayer").also{it.isAccessible=true}.get(screen) as com.jojo.game.presentation.shared.overlay.MagicUiList;layer.start(0)
      screen.javaClass.getDeclaredField("magickPressedRow").also{it.isAccessible=true}.set(screen,0)
      screen.javaClass.getDeclaredField("magickPressedAt").also{it.isAccessible=true}.setFloat(screen,-100f)}
     21->{capture("magic-detail");Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE);invoke("dispatchBattleCommand",6)
      val battle=screen.javaClass.getDeclaredField("battle").also{it.isAccessible=true}.get(screen) as com.jojo.game.application.battle.Battle
      val lin=battle.units.values.single{it.characterId==512}
      val dest=battle.movement.reachableTiles(lin.id).keys.first{(x,y)->kotlin.math.abs(x-lin.tileX)+kotlin.math.abs(y-lin.tileY)==1&&battle.unitAt(x,y)==null}
      invoke("handleTileClick",lin.tileX,lin.tileY);invoke("handleTileClick",dest.first,dest.second)
      val tiles=screen.javaClass.getDeclaredMethod("selectableAreaTiles").also{it.isAccessible=true}.invoke(screen) as List<*>
      check(tiles.isEmpty()){ "Ranges visible while moving" }}
     22->{val moving=screen.javaClass.getDeclaredField("movementAnimation").also{it.isAccessible=true}.get(screen)
      if(moving!=null)phase-- else{capture("move-arrived");println("BATTLE_UI_SMOKE_PASS ${width}x$height ranges=hidden-during-move");Gdx.app.exit()}}

    }
   }
   emptyList()
  }
  Lwjgl3Application(JojoGame(GameLaunchConfiguration(entryPoint=GameEntryPoint.BATTLE,initialScenario="S_90",initialScenarioExplicit=true,runtimeBattleDriver=driver,automatedRun=true)),Lwjgl3ApplicationConfiguration().apply{setTitle("임충전 UI 검증");setWindowedMode(width,height);setForegroundFPS(60)})
 }
}
