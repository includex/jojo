package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `SourceArmProfileContractTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SourceArmProfileContractTest {
    @Test
    fun `original arm profiles preserve ATTACKDELAY field`() {
        // The field is read directly from arms[6], not inferred from remote
        // attack or the avatar asset name.
        val delayed = GameDataCatalog.ArmProfile(1, "병종", 0, false, true, 100, 0, emptyMap(), emptyMap(), emptyMap())
        val ordinary = GameDataCatalog.ArmProfile(2, "병종", 0, false, false, 100, 0, emptyMap(), emptyMap(), emptyMap())
        assertEquals(true, delayed.attackDelay)
        assertEquals(false, ordinary.attackDelay)
    }
}
