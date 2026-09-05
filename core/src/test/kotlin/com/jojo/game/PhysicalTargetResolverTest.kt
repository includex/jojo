package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * class  `PhysicalTargetResolverTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class PhysicalTargetResolverTest {

    private fun unit(
        id: String = "unit",
        faction: Faction = Faction.PLAYER,
        attack: Int = 100,
        defense: Int = 50,
        spirit: Int = 50,
        critical: Int = 50,
        morale: Int = 50,
        martial: Int = 50,
        level: Int = 10,
        maxHitPoints: Int = 200,
        hitPoints: Int = 200,
        maxMagicPoints: Int = 100,
        magicPoints: Int = 50,
        movement: Int = 5,
        tileX: Int = 5,
        tileY: Int = 5,
        direction: Int = 0,
        skills: Map<Int, Int> = emptyMap(),
        statuses: Map<BattleStatus, Int> = emptyMap(),
    ) = BattleUnit(
        id = id,
        name = id,
        faction = faction,
        tileX = tileX,
        tileY = tileY,
        direction = direction,
        attack = attack,
        defense = defense,
        spirit = spirit,
        critical = critical,
        morale = morale,
        martial = martial,
        level = level,
        maxHitPoints = maxHitPoints,
        hitPoints = hitPoints,
        maxMagicPoints = maxMagicPoints,
        magicPoints = magicPoints,
        movement = movement,
        skills = skills,
        statuses = statuses.toMutableMap(),
    )

    private fun mockEnvironment(
        activeFaction: Faction = Faction.PLAYER,
        playerMoney: Int = 1000,
        enemyMoney: Int = 1000,
        onDefeat: (String) -> Unit = {},
    ): Pair<PhysicalTargetEnvironment, () -> Pair<Int, Int>> {
        var pMoney = playerMoney
        var eMoney = enemyMoney
        val env = PhysicalTargetEnvironment(
            random100 = { 50 },
            statusDuration = { _, _ -> 2 },
            canAttack = { _, _ -> true },
            backPosition = { target, _ -> target.tileX to target.tileY + 1 },
            activeFaction = activeFaction,
            getPlayerMoney = { pMoney },
            setPlayerMoney = { pMoney = it },
            getEnemyMoney = { eMoney },
            setEnemyMoney = { eMoney = it },
            propertyItem = { null },
            zdsyGlobalValue = 0,
            notifyPhysicalDamage = { _, _, _ -> },
            notifyConsumeAutomaticProperty = {},
            notifyUnitDefeated = { _, _ -> },
            onDefeat = onDefeat,
            incSkillTemp = { _, _ -> 0 },
            applyProperty = { _, _, _ -> null },
        )
        return env to { pMoney to eMoney }
    }

    @Test
    fun `zero harm triggers defender block retaliations when equipped`() {
        val attacker = unit(id = "attacker", maxHitPoints = 100, hitPoints = 100)
        val target = unit(id = "target", skills = mapOf(153 to 20)) // MENG_JI_CONFUSION: 20% harm
        val (env, _) = mockEnvironment()

        val result = PhysicalTargetResolver.resolve(
            attacker = attacker,
            target = target,
            resolvedHarm = 0,
            statuses = AttackStatusBatch(emptySet(), emptySet()),
            activeAttack = true,
            env = env,
        )

        assertEquals(0, result.damage)
        assertEquals(80, attacker.hitPoints)
        assertEquals(2, attacker.statuses[BattleStatus.CONFUSION])
        assertEquals(1, result.blockRetaliations.size)
        assertEquals(20, result.blockRetaliations.first().damage)
    }

    @Test
    fun `mp shield absorbs damage before hp damage`() {
        val attacker = unit(id = "attacker")
        val target = unit(id = "target", hitPoints = 100, magicPoints = 40, skills = mapOf(2 to 0)) // MP shield
        val (env, _) = mockEnvironment()

        val result = PhysicalTargetResolver.resolve(
            attacker = attacker,
            target = target,
            resolvedHarm = 30,
            statuses = AttackStatusBatch(emptySet(), emptySet()),
            activeAttack = true,
            env = env,
        )

        assertEquals(0, result.damage)
        assertEquals(30, result.mpShieldDamage)
        assertEquals(10, target.magicPoints)
        assertEquals(100, target.hitPoints)
    }

    @Test
    fun `money shield mitigates damage to 1 by spending money`() {
        val attacker = unit(id = "attacker")
        // Skill 125: 10 money per damage
        val target = unit(id = "target", faction = Faction.PLAYER, hitPoints = 50, skills = mapOf(125 to 10))
        val (env, getMoney) = mockEnvironment(playerMoney = 500)

        val result = PhysicalTargetResolver.resolve(
            attacker = attacker,
            target = target,
            resolvedHarm = 30,
            statuses = AttackStatusBatch(emptySet(), emptySet()),
            activeAttack = true,
            env = env,
        )

        // Price = 30 * 10 = 300 <= 500
        assertEquals(1, result.damage)
        assertEquals(300, result.moneyShieldSpent)
        assertEquals(200, getMoney().first)
        assertEquals(49, target.hitPoints)
    }

    @Test
    fun `life steal and recoil damage are applied`() {
        val attacker = unit(id = "attacker", hitPoints = 50, maxHitPoints = 100, skills = mapOf(238 to 50)) // 50% life steal
        val target = unit(id = "target", hitPoints = 100, skills = mapOf(40 to 20)) // 20% recoil
        val (env, _) = mockEnvironment()

        val result = PhysicalTargetResolver.resolve(
            attacker = attacker,
            target = target,
            resolvedHarm = 40,
            statuses = AttackStatusBatch(emptySet(), emptySet()),
            activeAttack = true,
            env = env,
        )

        // Damage = 40
        assertEquals(40, result.damage)
        // Life steal = 50% * 40 = 20 -> attacker HP becomes 50 + 20 = 70
        assertEquals(20, result.lifeStealHealing)
        // Recoil = 20% * 40 = 8 -> attacker HP becomes 70 - 8 = 62
        assertEquals(8, result.recoilDamage)
        assertEquals(62, attacker.hitPoints)
    }

    @Test
    fun `knockback skill moves target to back position`() {
        val attacker = unit(id = "attacker", skills = mapOf(221 to 0)) // knockback
        val target = unit(id = "target", tileX = 3, tileY = 4)
        val (env, _) = mockEnvironment()

        val result = PhysicalTargetResolver.resolve(
            attacker = attacker,
            target = target,
            resolvedHarm = 20,
            statuses = AttackStatusBatch(emptySet(), emptySet()),
            activeAttack = true,
            env = env,
        )

        assertNotNull(result.backMove)
        assertEquals(3, target.tileX)
        assertEquals(5, target.tileY)
    }

    @Test
    fun `defeat callback is invoked when target hp reaches 0`() {
        val attacker = unit(id = "attacker")
        val target = unit(id = "target", hitPoints = 25)
        var defeatedId: String? = null
        val (env, _) = mockEnvironment(onDefeat = { defeatedId = it })

        val result = PhysicalTargetResolver.resolve(
            attacker = attacker,
            target = target,
            resolvedHarm = 30,
            statuses = AttackStatusBatch(emptySet(), emptySet()),
            activeAttack = true,
            env = env,
        )

        assertTrue(result.defeated)
        assertEquals(0, target.hitPoints)
        assertEquals("target", defeatedId)
    }
}
