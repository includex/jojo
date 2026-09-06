// Test
package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.combat.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** PhysicalAttackAreaResolverTest: PhysicalAttackAreaResolver의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

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

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        attacker.attackEffectOffsets = setOf(0 to 1, 0 to -1)
        val defaultPositions = PhysicalAttackAreaResolver.physicalEffectPositions(attacker, target)
        assertEquals(setOf(3 to 3, 3 to 1), defaultPositions)

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        val area0Attacker = attacker.copy(attackEffectAreaId = 0)
        assertTrue(PhysicalAttackAreaResolver.physicalEffectPositions(area0Attacker, target).isEmpty())
        val area12Attacker = attacker.copy(attackEffectAreaId = 12)
        assertTrue(PhysicalAttackAreaResolver.physicalEffectPositions(area12Attacker, target).isEmpty())

        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        val area4Attacker = attacker.copy(attackEffectAreaId = 4)
        assertEquals(setOf(4 to 2), PhysicalAttackAreaResolver.physicalEffectPositions(area4Attacker, target))

        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        val area7Attacker = attacker.copy(attackEffectAreaId = 7)
        assertEquals(setOf(4 to 2, 5 to 2), PhysicalAttackAreaResolver.physicalEffectPositions(area7Attacker, target))

        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
        val area9Attacker = attacker.copy(attackEffectAreaId = 9)
        assertEquals(setOf(3 to 1, 3 to 3), PhysicalAttackAreaResolver.physicalEffectPositions(area9Attacker, target))

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
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
        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
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
