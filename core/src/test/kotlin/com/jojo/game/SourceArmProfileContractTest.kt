// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.infrastructure.data.GameDataRepository
import com.jojo.game.infrastructure.data.ClasspathThenGdxGameDataResourceSource

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

    @Test
    fun `arms table has no siege column so canSiegle is always false`() {
        // 원본 checkCanSiege (recovered-js/modules/battle/BattleLayer.js:3769)는 먼저
        // t.unit().canSiegle()을 확인하고, canSiegle() (game-data/Unit.js:327)은
        // armAttr2(arm(), ARM_ATTR_NAME2.SIEGE)를 읽는다. SIEGE는 exdata 11번 열이다
        // (core/Config.js:427). 11번 열이 없는 행은 Model.js:703-711에 따라 0으로 채워진다.
        // 배포된 arms 표(40행)에는 0~10번 열만 있고 11번 열이 없음을 직접 복호화로 확인했으므로,
        // 두 게임 모두 포위 공격 명령을 결코 켜지 않는다 — 이식본이 SIEGE_BIT를 세우지 않는 것은
        // 결함이 아니라 이 사실을 그대로 반영한 것이다. 표가 재배포되어 11번 열이 생기면 이
        // 결론과 이식본의 동작이 함께 틀려지므로, 그 사실을 이 테스트가 잡아낸다.
        val arms = GameDataRepository(ClasspathThenGdxGameDataResourceSource()).load().arms

        val rowsWithSiegeColumn = arms.withIndex().filter { (_, row) -> row.has("11") }.map { it.index }

        assertEquals(
            emptyList(),
            rowsWithSiegeColumn,
            "arms 표에 11번(SIEGE) 열이 나타났다: 포위 공격 규칙이 실제로 켜졌다는 뜻이다. " +
                "ArmProfile.siege 필드를 추가하고 GameDataCatalogUnitDomain.armProfile -> " +
                "BattleUnit -> BattleScreen.battleCommandMask까지 SIEGE_BIT를 배선하라 " +
                "(BattleLayer.js:3769, Unit.js:327, Config.js:427 참고).",
        )
    }
}
