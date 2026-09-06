// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals

/** SourceAbilityPhaseContractTest: SourceAbilityPhaseContract의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class SourceAbilityPhaseContractTest {
    @Test
    fun `battle profile uses source runtime ability phase thresholds`() {
        val profile = requireNotNull(GameDataCatalog.load().battleProfile(unitId = 32, scriptLevel = 6))

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(60, profile.attack)
        assertEquals(57, profile.defense)
        assertEquals(59, profile.spirit)
        assertEquals(58, profile.critical)
        assertEquals(71, profile.morale)
    }
}
