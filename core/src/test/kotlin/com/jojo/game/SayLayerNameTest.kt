package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `SayLayerNameTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SayLayerNameTest {
    @Test
    fun `SayLayer removes from the first numeric instance marker`() {
        assertEquals("황건군 ", GameDataCatalog.sayLayerUnitName("황건군 1"))
        assertEquals("궁병", GameDataCatalog.sayLayerUnitName("궁병12"))
        assertEquals("조조", GameDataCatalog.sayLayerUnitName("조조"))
    }
}
