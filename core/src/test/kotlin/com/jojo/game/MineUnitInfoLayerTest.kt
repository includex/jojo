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

 /**
  * 원본 `MineUnitInfoLayer` 프리팹(dd2699f7…528ab)에서 `_color`를 적은 노드는 투명한
  * `Panel_cancel` 하나뿐이라 라벨은 모두 `cc.Label` 기본 흰색이다. 그리기 쪽
  * `drawSettlementOverlays`와 이 증거 로그가 같은 상수를 읽는지 확인한다.
  */
 @Test fun `mine panel labels record the drawing font colour and sprites record none`(){
  assertEquals("#ffffffff",SettlementInfoRenderContract.LABEL_COLOR)
  val u=BattleUnit("43","보병 ",Faction.FRIEND,10,17,119,119,11,11,level=1)
  val rows=MineUnitInfoRenderEvents.jsonl(MineUnitInfoLayer().onCreate(u,"경보병"))
   .lineSequence().filter{it.isNotBlank()}.toList()
  val labels=rows.filter{it.contains("\"drawType\":\"label\"")}
  assertEquals(15,labels.size)
  assertTrue(labels.all{it.contains("\"color\":\"${SettlementInfoRenderContract.LABEL_COLOR}\"")})
  // 판때기·막대도 색을 적는다. 그리기 쪽 `drawSettlementOverlays`가 `batch.color = Color.WHITE`를
  // 세우고 패널이 끝날 때까지 바꾸지 않으므로 흰 색조는 포트가 실제로 아는 값이다. 비워 두면
  // 비교기가 그 행을 건너뛰어 색조가 들어가도 아무 게이트가 떨어지지 않는다.
  assertTrue(rows.filterNot{it.contains("\"drawType\":\"label\"")}
   .all{it.contains("\"color\":\"${SettlementInfoRenderContract.SPRITE_WHITE}\"")})
  assertTrue(rows.none{it.contains("\"color\":null")})
 }

 /**
  * 원본 `recovered-js/modules/ui/MineUnitInfoLayer.js:173-176`:
  * `T >= 3 && A[0] == A[1]`인 줄(무기 경험치 label3, 방어구 경험치 label4)만
  * 글자가 "MAX"가 되고 노드 색이 `cc.color(17, 17, 251)`이 된다. 나머지는 숫자에 기본 흰색이다.
  */
 @Test fun `equipment exp rows read MAX in blue only at the limit`(){
  assertEquals("#1111fbff",SettlementInfoRenderContract.MAX_LABEL_COLOR)
  val u=BattleUnit("43","보병 ",Faction.FRIEND,10,17,119,119,11,11,level=1)
  val base=MineUnitInfoLayer().onCreate(u,"경보병")

  /** label3/label4 두 줄의 (글자, 색)을 증거 로그에서 뽑는다. */
  fun equipRows(v:MineUnitInfoLayer.View)=MineUnitInfoRenderEvents.jsonl(v).lineSequence()
   .filter{it.contains("Canvas/Layer/bg/label3\"")||it.contains("Canvas/Layer/bg/label4\"")}
   .map{row->Regex("\"text\":\"([^\"]*)\"").find(row)!!.groupValues[1] to Regex("\"color\":\"([^\"]*)\"").find(row)!!.groupValues[1]}
   .toList()

  // 상한에 닿지 않은 줄: 현재 값을 그대로, 다른 라벨과 같은 흰색으로 적는다.
  assertEquals(
   listOf("17" to SettlementInfoRenderContract.LABEL_COLOR,"199" to SettlementInfoRenderContract.LABEL_COLOR),
   equipRows(base.copy(weaponExp=17,maxWeaponExp=200,armorExp=199,maxArmorExp=200)),
  )
  // 값이 상한과 같은 줄만 "MAX"에 파란색이다.
  assertEquals(
   listOf("MAX" to SettlementInfoRenderContract.MAX_LABEL_COLOR,"0" to SettlementInfoRenderContract.LABEL_COLOR),
   equipRows(base.copy(weaponExp=200,maxWeaponExp=200,armorExp=0,maxArmorExp=100)),
  )
  assertEquals(
   listOf("MAX" to SettlementInfoRenderContract.MAX_LABEL_COLOR,"MAX" to SettlementInfoRenderContract.MAX_LABEL_COLOR),
   equipRows(base.copy(weaponExp=250,maxWeaponExp=250,armorExp=100,maxArmorExp=100)),
  )
  // 체력·내공·경험치 줄(T < 3)은 값이 상한과 같아도 숫자와 흰색을 지킨다.
  val full=MineUnitInfoRenderEvents.jsonl(base).lineSequence()
   .filter{it.contains("Canvas/Layer/bg/p0/label0\"")}.single()
  assertTrue(full.contains("\"text\":\"119\"")&&full.contains("\"color\":\"${SettlementInfoRenderContract.LABEL_COLOR}\""))
 }
}
