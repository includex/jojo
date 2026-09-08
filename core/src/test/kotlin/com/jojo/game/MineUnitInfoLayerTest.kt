// Test
package com.jojo.game

import com.jojo.game.presentation.battle.overlay.*

import com.jojo.game.domain.battle.*

import kotlin.test.*
/** MineUnitInfoLayerTest: MineUnitInfoLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class MineUnitInfoLayerTest {
 @Test fun `mine result panel carries all five progress values and completes once`(){var n=0;val u=BattleUnit("43","보병 ",Faction.FRIEND,10,17,119,119,11,11,level=1);val l=MineUnitInfoLayer();val v=l.onCreate(u,"경보병"){n++};assertEquals(listOf(119,119,11,11,0,100,0,0),listOf(v.hp,v.maxHp,v.mp,v.maxMp,v.exp,v.maxExp,v.weaponExp,v.armorExp));assertEquals(30,MineUnitInfoRenderEvents.jsonl(v).lineSequence().count{it.isNotBlank()});l.complete();l.complete();assertFalse(l.view().attached);assertEquals(1,n)}
 @Test fun `mine prefab contract retains three full source bars and equipment icons`(){
  val sprites=SettlementInfoRenderContract.sprites(SettlementInfoRenderContract.Panel.MINE)
  assertEquals(listOf("maps/ui/settlement-info/bg2.png","maps/ui/settlement-info/box1.png"),sprites.take(2).map{it.path})
  assertEquals(3,sprites.count{it.path=="maps/ui/settlement-info/progress-bg.png"})
  assertEquals(listOf("maps/ui/settlement-info/mark61.png","maps/ui/settlement-info/mark62.png"),sprites.takeLast(2).map{it.path})
  assertEquals(SettlementInfoRenderContract.Sprite("maps/ui/settlement-info/mark6.png",807.5f,149f,370f,20f),sprites[10])
 }
 /**
  * 원본 `MineUnitInfoLayer` 프리팹에서 테두리 `box3`와 막대 바탕 `p0~p2`만
  * `cc.Sprite._type == 1`(SLICED)이다. 나머지는 SIMPLE이므로 cap inset이 0이어야
  * 20x20 / 60x15 프레임을 471x257.5 / 374x24로 늘일 때 테두리가 뭉개지지 않는다.
  */
 @Test fun `mine prefab contract slices only the frame and the bar background`(){
  val sprites=SettlementInfoRenderContract.sprites(SettlementInfoRenderContract.Panel.MINE)
  assertEquals(
   mapOf("maps/ui/settlement-info/box1.png" to 2,"maps/ui/settlement-info/progress-bg.png" to 3),
   sprites.filter{it.capInset>0}.associate{it.path to it.capInset},
  )
  assertEquals(4,sprites.count{it.capInset>0})
 }
}
