// Test
package com.jojo.game

import com.jojo.game.presentation.battle.overlay.WinConditionsLayer

import kotlin.test.*
/** WinConditionsLayerTest: WinConditionsLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class WinConditionsLayerTest{@Test fun `source rich texts and panel cancel`() {var n=0;val l=WinConditionsLayer();val v=l.onCreate("적을\n물리쳐라",20){n++};assertTrue(v.first.contains("ff0000"));assertTrue(v.first.contains("적을<br/>물리쳐라"));assertTrue(v.second.contains("제한 턴 수 20"));assertTrue(l.cancel(2));assertEquals(1,n)}
    @Test fun `source replaces only first newline`() { val view=WinConditionsLayer().onCreate("첫째\n둘째\n셋째",1){}; assertTrue(view.first.contains("첫째<br/>둘째\n셋째")) }
    @Test fun `raw source listener invokes callback on repeated end`() { var count=0; val layer=WinConditionsLayer(); layer.onCreate("x",1){count++}; assertTrue(layer.cancel(2)); assertTrue(layer.cancel(2)); assertEquals(2,count) }}
