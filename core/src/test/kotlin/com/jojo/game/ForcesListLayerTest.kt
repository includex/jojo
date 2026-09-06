// Test
package com.jojo.game

import com.jojo.game.presentation.battle.overlay.ForcesListLayer

import kotlin.test.*
/** ForcesListLayerTest: ForcesListLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class ForcesListLayerTest {
 private fun unit(id:Int,famous:Boolean=false)=ForcesListLayer.Unit(id,"U$id","보병",1,30,40,5,8,10,11,12,13,14,famous,status=mapOf(0 to 0,1 to 1,2 to 0,3 to 1,4 to 0))
 @Test fun `source flag enables enemy tab and famous ordering`() { val l=ForcesListLayer(); val v=l.onCreate(listOf(unit(4),unit(8,true)),listOf(unit(3)),1); assertEquals(8,v.rows.first().unit.id); assertTrue(v.tabsVisible); assertEquals(3,l.changeSel(1).rows.single().unit.id) }
 @Test fun `battle rows show and color every source ability from ATT through MOR`() {
  val row=ForcesListLayer().onCreate(listOf(unit(1)),flag=1).rows.single()
  assertEquals(listOf("10","11","12","13","14"),row.labels.drop(5))
  assertEquals(listOf(ForcesListLayer.RowColor.RED,ForcesListLayer.RowColor.BLUE,ForcesListLayer.RowColor.RED,ForcesListLayer.RowColor.BLUE,ForcesListLayer.RowColor.RED),row.colors.drop(5))
 }
 @Test fun `end only selection and close`() { val l=ForcesListLayer();l.onCreate(listOf(unit(1)),flag=0); assertNull(l.onRowTouch(0,0));assertEquals(1,l.onRowTouch(0,2)?.id);assertFalse(l.onClose(0));assertTrue(l.onClose(2)) }
}
