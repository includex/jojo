package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `SourceAbilityPhaseContractTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SourceAbilityPhaseContractTest {
    @Test
    fun `battle profile uses source runtime ability phase thresholds`() {
        val profile = requireNotNull(GameDataCatalog.load().battleProfile(unitId = 32, scriptLevel = 6))

        // Source Unit.wuWeiPhase: raw [39,36,38,37,50], posts bonus 3,
        // phase thresholds [127,45,35,25], level 7. Equipment is merged
        // later when the scenario creates its BattleUnit.
        assertEquals(60, profile.attack)
        assertEquals(57, profile.defense)
        assertEquals(59, profile.spirit)
        assertEquals(58, profile.critical)
        assertEquals(71, profile.morale)
    }
}
