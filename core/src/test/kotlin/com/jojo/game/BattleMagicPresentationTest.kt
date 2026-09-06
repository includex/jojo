// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.battle.MagicTarget
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.presentation.battle.timeline.*

import kotlin.test.Test
import kotlin.test.assertEquals

/** BattleMagicPresentationTest: BattleMagicPresentation의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleMagicPresentationTest {
    private fun magic(type: Int = 0, category: Int = 0) = GameDataCatalog.MagicProfile(
        id = 1, name = "m", type = type, target = 0,
        hitArea = GameDataCatalog.HitAreaProfile(0, emptySet()), effectAreaId = 0,
        effectOffsets = emptySet(), expendMp = 0, power = 0, harmType = 0, category = category,
    )

    @Test
    fun `aggregates same target exactly as char-info key accumulation`() {
        val result = TacticalActionResult.Magic("m", 0, listOf(
            MagicTarget("target", damage = 12, hitRate = 100, hit = true, defeated = false),
            MagicTarget("target", damage = 8, hitRate = 100, hit = true, defeated = false),
        ))
        assertEquals(listOf(BattleMagicPresentation.Change("target", hpAdd = -20)), BattleMagicPresentation.changes(result, "caster", magic()))
    }

    @Test
    fun `MP drain gives target loss and caster recovery in separate original groups`() {
        val result = TacticalActionResult.Magic("m", 0, listOf(
            MagicTarget("target", damage = 0, magicDrain = 7, magicRecovery = 5, hitRate = 100, hit = true, defeated = false),
        ))
        assertEquals(listOf(BattleMagicPresentation.Change("target", mpAdd = -7), BattleMagicPresentation.Change("caster", mpAdd = 5)), BattleMagicPresentation.changes(result, "caster", magic(type = 6)))
    }

    @Test
    fun `MX recovery belongs to caster while normal MP restore belongs to target`() {
        val mx = TacticalActionResult.Magic("m", 0, listOf(MagicTarget("target", damage = 40, magicRecovery = 25, hitRate = 100, hit = true, defeated = false)))
        assertEquals(listOf(BattleMagicPresentation.Change("target", hpAdd = -40), BattleMagicPresentation.Change("caster", mpAdd = 25)), BattleMagicPresentation.changes(mx, "caster", magic(type = 20, category = 24)))
        val restore = TacticalActionResult.Magic("m", 0, listOf(MagicTarget("target", damage = 0, magicRecovery = 25, hitRate = 100, hit = true, defeated = false)))
        assertEquals(listOf(BattleMagicPresentation.Change("target", mpAdd = 25)), BattleMagicPresentation.changes(restore, "caster", magic(type = 20)))
    }
}
