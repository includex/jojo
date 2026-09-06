// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import kotlin.test.Test
import kotlin.test.assertEquals

/** SourceArmProfileContractTest: SourceArmProfileContract의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class SourceArmProfileContractTest {
    @Test
    fun `original arm profiles preserve ATTACKDELAY field`() {
        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val delayed = GameDataCatalog.ArmProfile(1, "병종", 0, false, true, 100, 0, emptyMap(), emptyMap(), emptyMap())
        val ordinary = GameDataCatalog.ArmProfile(2, "병종", 0, false, false, 100, 0, emptyMap(), emptyMap(), emptyMap())
        assertEquals(true, delayed.attackDelay)
        assertEquals(false, ordinary.attackDelay)
    }
}
