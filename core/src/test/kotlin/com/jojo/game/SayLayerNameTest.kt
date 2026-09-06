// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import kotlin.test.Test
import kotlin.test.assertEquals

/** SayLayerNameTest: SayLayerName의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class SayLayerNameTest {
    @Test
    fun `SayLayer removes from the first numeric instance marker`() {
        assertEquals("황건군 ", GameDataCatalog.sayLayerUnitName("황건군 1"))
        assertEquals("궁병", GameDataCatalog.sayLayerUnitName("궁병12"))
        assertEquals("조조", GameDataCatalog.sayLayerUnitName("조조"))
    }
}
