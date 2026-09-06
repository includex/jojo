// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.presentation.battle.timeline.BattleCharacterStrictState
import com.jojo.game.presentation.battle.unit.UnitSpriteFrame
import com.jojo.game.presentation.battle.unit.UnitSpriteSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 전투 캐릭터 fixture 조정자가 화면 포트의 유닛·프레임을 명령과 증거 JSONL로 일관되게 조립하는지 검증한다. */
class BattleCharacterRouteFixtureCoordinatorTest {
    /** 피격 경로: 선택 프레임의 atlas crop과 fixture 피해 숫자 영역을 draw 명령 및 JSONL에 유지한다. */
    @Test
    fun `피격 fixture의 프레임 명령과 JSONL을 조립한다`() {
        val coordinator = BattleCharacterRouteFixtureCoordinator()
        val port = object : BattleCharacterRouteFixturePort {
            private val unit = BattleUnit("unit-210", "테스트", Faction.ENEMY, 0, 0, characterId = 210)
            override fun unit(characterId: Int) = unit.takeIf { it.characterId == characterId }
            override fun spriteFrame(action: Int, direction: Int, elapsed: Float, loop: Boolean) =
                UnitSpriteFrame(UnitSpriteSource.ATTACK, sourceY = 123, sourceWidth = 64, sourceHeight = 48, flipX = true)
            override fun idleSpriteFrame(unit: BattleUnit) = UnitSpriteFrame(UnitSpriteSource.MOVEMENT, sourceY = 0)
        }

        coordinator.install(BattleCharacterStrictState.HIT_IMPACT, port)
        val sample = coordinator.drawSamples(port).single()

        assertEquals(listOf(0, 123, 64, 48), sample.commands.first().sourceRect)
        assertEquals(true, sample.commands.first().flipX)
        assertEquals(611.3f, sample.commands.last().x)
        assertEquals(64.48f, sample.commands.last().height)
        assertTrue(coordinator.jsonl(BattleCharacterStrictState.HIT_IMPACT, port).contains("\"sourceRect\":[0, 123, 64, 48]"))
    }
}
