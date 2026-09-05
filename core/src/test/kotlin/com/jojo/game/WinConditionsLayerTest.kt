package com.jojo.game
import kotlin.test.*
/**
 * class  `WinConditionsLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class WinConditionsLayerTest{@Test fun `source rich texts and panel cancel`() {var n=0;val l=WinConditionsLayer();val v=l.onCreate("적을\n물리쳐라",20){n++};assertTrue(v.first.contains("ff0000"));assertTrue(v.first.contains("적을<br/>물리쳐라"));assertTrue(v.second.contains("제한 턴 수 20"));assertTrue(l.cancel(2));assertEquals(1,n)}
    @Test fun `source replaces only first newline`() { val view=WinConditionsLayer().onCreate("첫째\n둘째\n셋째",1){}; assertTrue(view.first.contains("첫째<br/>둘째\n셋째")) }
    @Test fun `raw source listener invokes callback on repeated end`() { var count=0; val layer=WinConditionsLayer(); layer.onCreate("x",1){count++}; assertTrue(layer.cancel(2)); assertTrue(layer.cancel(2)); assertEquals(2,count) }}
