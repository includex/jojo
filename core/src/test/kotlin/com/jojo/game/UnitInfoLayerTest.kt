package com.jojo.game
import kotlin.test.*
/**
 * class  `UnitInfoLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class UnitInfoLayerTest { private fun u(i:Int)=UnitInfoLayer.Unit(i,"U$i","보병",i,10,20,3,5,1,2,3,4,5,listOf("화계"))
 @Test fun `tabs and source wrapping previous next`() { val l=UnitInfoLayer(listOf(u(1),u(2)));assertEquals(1,l.onCreate().unit.id);l.onButton(5,2);assertEquals(2,l.ref().unit.id);l.onButton(3,2);assertEquals(3,l.ref().tab);assertFalse(l.onButton(7,0));assertTrue(l.onButton(7,2));assertFalse(l.ref().attached) }
 @Test fun `source listeners are touch-end only and cancel is independent`() { val l=UnitInfoLayer(listOf(u(1)));l.onCreate();assertFalse(l.onButton(4,1));assertEquals(0,l.ref().tab);assertFalse(l.onCancel(1));assertTrue(l.onCancel(2));assertFalse(l.ref().attached) }
 @Test fun `source refUnit wraps and retains selected tab through persisted user default`() { val l=UnitInfoLayer(listOf(u(1),u(2)),defaultTab=2);assertEquals(2,l.onCreate(-1).unit.id);assertEquals(2,l.ref().tab);l.onButton(4,2);l.onButton(6,2);assertEquals(1,l.ref().unit.id);assertEquals(4,l.ref().tab) }
 @Test fun `source panel zero transform and routes retain item magic row`() { val x=u(1).copy(magic=listOf("화계","낙뢰"),equipment=listOf(UnitInfoLayer.Equipment("검"),null,null));val l=UnitInfoLayer(listOf(x),singleValueMode=true,featsEnabled=true);assertEquals(listOf(2,4,6,8,10),l.onCreate().values);assertTrue(l.onButton(8,2));assertEquals(UnitInfoLayer.Route.FEATS,l.takeRoutes().single().route);assertTrue(l.onEquipment(0,2));assertTrue(l.onEquipment(1,2));assertEquals("검",l.takeRoutes().single().value);assertTrue(l.onMagic(1,2));assertEquals("낙뢰",l.takeRoutes().single().value) }
 @Test fun `trace equals recovered source lifecycle fixture`() {
     val layer = UnitInfoLayer(listOf(u(1), u(2)))
/**
 * 공개 메서드 `snapshot`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `List<Any>`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

     fun snapshot(): List<Any> = listOf(layer.ref().index, layer.ref().tab, layer.ref().attached, layer.ref().panels, layer.ref().interactable)
     layer.onCreate()
     assertEquals(listOf<Any>(0, 0, true, listOf(true, false, false, false, false), listOf(false, true, true, true, true)), snapshot())
     layer.onButton(3, 1); assertEquals(0, layer.ref().tab)
     layer.onButton(3, 2); assertEquals(3, layer.ref().tab)
     layer.onButton(5, 2)
     assertEquals(listOf<Any>(1, 3, true, listOf(false, false, false, true, false), listOf(true, true, true, false, true)), snapshot())
     layer.onButton(6, 2); layer.onButton(7, 1); assertTrue(layer.ref().attached)
     layer.onButton(7, 2); assertFalse(layer.ref().attached)
 }
}
