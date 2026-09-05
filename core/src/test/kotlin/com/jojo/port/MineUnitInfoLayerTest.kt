package com.jojo.port
import kotlin.test.*
class MineUnitInfoLayerTest {
 @Test fun `mine result panel carries all five progress values and completes once`(){var n=0;val u=BattleUnit("43","보병 ",Faction.FRIEND,10,17,119,119,11,11,level=1);val l=MineUnitInfoLayer();val v=l.onCreate(u,"경보병"){n++};assertEquals(listOf(119,119,11,11,0,100,0,0),listOf(v.hp,v.maxHp,v.mp,v.maxMp,v.exp,v.maxExp,v.weaponExp,v.armorExp));assertEquals(30,MineUnitInfoRenderEvents.jsonl(v).lineSequence().count{it.isNotBlank()});l.complete();l.complete();assertFalse(l.view().attached);assertEquals(1,n)}
 @Test fun `mine prefab contract retains three full source bars and equipment icons`(){
  val sprites=SettlementInfoRenderContract.sprites(SettlementInfoRenderContract.Panel.MINE)
  assertEquals(listOf("maps/ui/settlement-info/bg2.png","maps/ui/settlement-info/box1.png"),sprites.take(2).map{it.path})
  assertEquals(3,sprites.count{it.path=="maps/ui/settlement-info/progress-bg.png"})
  assertEquals(listOf("maps/ui/settlement-info/mark61.png","maps/ui/settlement-info/mark62.png"),sprites.takeLast(2).map{it.path})
  assertEquals(SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark6.png",807.5f,149f,370f,20f),sprites[10])
 }
}
