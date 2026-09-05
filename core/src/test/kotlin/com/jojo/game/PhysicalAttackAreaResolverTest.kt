package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `PhysicalAttackAreaResolverTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class PhysicalAttackAreaResolverTest {

    private fun unit(
        id: String,
        x: Int,
        y: Int,
        faction: Faction = Faction.ENEMY,
        hp: Int = 100,
        areaId: Int? = null,
        skills: Map<Int, Int> = emptyMap(),
    ) = BattleUnit(
        id = id,
        name = id,
        faction = faction,
        tileX = x,
        tileY = y,
        attack = 100,
        defense = 50,
        hitPoints = hp,
        maxHitPoints = hp,
        attackEffectAreaId = areaId,
        skills = skills,
    )

    @Test
    fun `physicalEffectPositions handles standard and dynamic geometry`() {
        val attacker = unit("atk", 2, 2, Faction.PLAYER)
        val target = unit("tgt", 3, 2, Faction.ENEMY)

        // Default: attacker's attackEffectOffsets relative to target
        attacker.attackEffectOffsets = setOf(0 to 1, 0 to -1)
        val defaultPositions = PhysicalAttackAreaResolver.physicalEffectPositions(attacker, target)
        assertEquals(setOf(3 to 3, 3 to 1), defaultPositions)

        // Area 0 and 12: empty
        val area0Attacker = attacker.copy(attackEffectAreaId = 0)
        assertTrue(PhysicalAttackAreaResolver.physicalEffectPositions(area0Attacker, target).isEmpty())
        val area12Attacker = attacker.copy(attackEffectAreaId = 12)
        assertTrue(PhysicalAttackAreaResolver.physicalEffectPositions(area12Attacker, target).isEmpty())

        // Area 4: 1 tile beyond target in direction of attack (dx=1, dy=0)
        val area4Attacker = attacker.copy(attackEffectAreaId = 4)
        assertEquals(setOf(4 to 2), PhysicalAttackAreaResolver.physicalEffectPositions(area4Attacker, target))

        // Area 7: 2 tiles beyond target (dx=1, dy=0)
        val area7Attacker = attacker.copy(attackEffectAreaId = 7)
        assertEquals(setOf(4 to 2, 5 to 2), PhysicalAttackAreaResolver.physicalEffectPositions(area7Attacker, target))

        // Area 9: side tiles perpendicular to attack direction (dx=1, dy=0 -> sides are dy)
        val area9Attacker = attacker.copy(attackEffectAreaId = 9)
        assertEquals(setOf(3 to 1, 3 to 3), PhysicalAttackAreaResolver.physicalEffectPositions(area9Attacker, target))

        // Area 10: anchors pattern at attacker instead of target
        val area10Attacker = attacker.copy(attackEffectAreaId = 10).apply {
            attackEffectOffsets = setOf(1 to 0)
        }
        assertEquals(setOf(3 to 2), PhysicalAttackAreaResolver.physicalEffectPositions(area10Attacker, target))
    }

    @Test
    fun `hasPhysicalEffectTargets checks opposing units in splash area`() {
        val attacker = unit("atk", 2, 2, Faction.PLAYER, areaId = 4)
        val target = unit("tgt", 3, 2, Faction.ENEMY)
        val splashVictim = unit("splash", 4, 2, Faction.ENEMY)
        val units = mapOf((3 to 2) to target, (4 to 2) to splashVictim)

        val hasTargets = PhysicalAttackAreaResolver.hasPhysicalEffectTargets(
            attacker = attacker,
            target = target,
            unitAt = { x, y -> units[x to y] },
            areAllied = { a, b -> a.faction == b.faction },
        )
        assertTrue(hasTargets)
    }

    @Test
    fun `damage transfer redirects harm to lowest-HP adjacent opposing unit with skill 277`() {
        val attacker = unit("atk", 2, 2, Faction.PLAYER)
        val defender = unit("def", 3, 2, Faction.ENEMY, skills = mapOf(277 to 50)).apply {
            attackOffsets = setOf(0 to 1, 0 to -1)
        }
        val target1 = unit("t1", 3, 3, Faction.PLAYER, hp = 80)
        val target2 = unit("t2", 3, 1, Faction.PLAYER, hp = 40)
        val units = listOf(attacker, defender, target1, target2)

        val transfer = PhysicalAttackAreaResolver.physicalDamageTransfer(
            attacker = attacker,
            defender = defender,
            resolvedHarm = 100,
            units = { units },
            unitAt = { x, y -> units.firstOrNull { it.tileX == x && it.tileY == y } },
            areAllied = { a, b -> a.faction == b.faction },
        )
        // Redirects 50% harm (50) to target2 (lowest HP among defender's opposing units)
        assertEquals(target2, transfer?.first)
        assertEquals(50, transfer?.second)
    }

    @Test
    fun `damage transfer returns null if harm is less than defender level`() {
        val attacker = unit("atk", 2, 2, Faction.PLAYER)
        val defender = unit("def", 3, 2, Faction.ENEMY, skills = mapOf(277 to 50)).apply {
            level = 50
        }
        val friend = unit("f1", 3, 3, Faction.ENEMY, hp = 80)
        val units = listOf(attacker, defender, friend)

        val transfer = PhysicalAttackAreaResolver.physicalDamageTransfer(
            attacker = attacker,
            defender = defender,
            resolvedHarm = 20, // less than level 50
            units = { units },
            unitAt = { x, y -> units.firstOrNull { it.tileX == x && it.tileY == y } },
            areAllied = { a, b -> a.faction == b.faction },
        )
        assertNull(transfer)
    }
}
