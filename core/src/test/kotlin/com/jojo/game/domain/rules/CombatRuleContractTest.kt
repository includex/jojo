package com.jojo.game.domain.rules

import kotlin.test.*

class CombatRuleContractTest {
    private val units = mapOf(
        "attacker" to RuleUnitSnapshot("attacker", 20, 30, 4, 8, characterId = 1, equipmentIds = listOf(101)),
        "defender" to RuleUnitSnapshot("defender", 20, 30, 4, 8, characterId = 2, equipmentIds = listOf(202)),
    )

    private fun context(event: RuleEvent, phase: RuleCombatPhase) = RuleContext(
        event, "attacker", units, targetId = "defender", baseDamage = 10,
        attackPower = 20, combatPhase = phase, damageKind = "physical",
    )

    private fun handler(body: (RuleContext, RuleRandom) -> List<RuleEffect>) = object : RuleHandler {
        override val id = "test.combat"
        override fun validate(parameters: Map<String, String>) = Unit
        override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom) = body(context, random)
    }

    private fun definition(event: RuleEvent, binding: RuleBinding = RuleBinding(), id: String = "test.rule") =
        RuleDefinition(id, event, "test.combat", binding = binding)

    @Test fun bindingsSelectAttackerOnAttackAndDefenderOnHitUsingEquippedIdsOnly() {
        for (event in listOf(RuleEvent.ATTACK, RuleEvent.HIT)) {
            val calls = mutableListOf<Pair<String?, String?>>()
            val engine = RuleEngine(listOf(handler { ctx, _ ->
                calls += ctx.ownerId to ctx.sourceId
                assertEquals("attacker", ctx.actorId)
                assertEquals("defender", ctx.targetId)
                emptyList()
            }), RuleRandom { error("No random draw expected") })
            val definitions = listOf(
                definition(event, id = "a.global"),
                definition(event, RuleBinding(RuleBindingScope.CHARACTER, 1), "b.attacker"),
                definition(event, RuleBinding(RuleBindingScope.CHARACTER, 2), "c.defender"),
                definition(event, RuleBinding(RuleBindingScope.EQUIPMENT, 101), "d.attackerGear"),
                definition(event, RuleBinding(RuleBindingScope.EQUIPMENT, 202), "e.defenderGear"),
                // An item absent from equipment cannot activate, even if campaign inventory owns it.
                definition(event, RuleBinding(RuleBindingScope.EQUIPMENT, 303), "f.unequipped"),
            )
            engine.execute(definitions, context(event, RuleCombatPhase.POWER).copy(ownerId = "spoofed", sourceId = "spoofed"))
            val owner = if (event == RuleEvent.ATTACK) "attacker" else "defender"
            val character = if (event == RuleEvent.ATTACK) 1 else 2
            val gear = if (event == RuleEvent.ATTACK) 101 else 202
            assertEquals(listOf<Pair<String?, String?>>(owner to "global", owner to "character:$character", owner to "equipment:$gear"), calls)
        }
    }

    @Test fun pureCombatPhasesRejectRandomWithoutConsumingCampaignGenerator() {
        var draws = 0
        val engine = RuleEngine(listOf(handler { _, random -> random.nextInt(10); emptyList() }), RuleRandom { draws++; 0 })
        for (event in listOf(RuleEvent.ATTACK, RuleEvent.HIT)) for (phase in RuleCombatPhase.entries) {
            assertFailsWith<IllegalStateException> { engine.execute(listOf(definition(event)), context(event, phase)) }
        }
        assertEquals(0, draws)
    }

    @Test fun pureCombatPhasesRejectEveryStateEffectAndWrongModifierType() {
        val stateEffects = listOf(
            RuleEffect.HpDelta("attacker", 1), RuleEffect.MpDelta("attacker", 1),
            RuleEffect.ResourceDelta("attacker", "test.rage", 1),
            RuleEffect.StatusAdd("attacker", "test.buff"), RuleEffect.StatusRemove("attacker", "test.buff"),
        )
        for (event in listOf(RuleEvent.ATTACK, RuleEvent.HIT)) for (phase in RuleCombatPhase.entries) {
            val wrongModifier = if (phase == RuleCombatPhase.POWER) RuleEffect.DamageModifier(1) else RuleEffect.AttackPowerModifier(1)
            for (effect in stateEffects + wrongModifier) {
                val engine = RuleEngine(listOf(handler { _, _ -> listOf(effect) }), RuleRandom { 0 })
                assertFailsWith<IllegalArgumentException> { engine.execute(listOf(definition(event)), context(event, phase)) }
            }
            assertEquals(20, units.getValue("attacker").hitPoints)
        }
    }

    @Test fun validModifiersApplyOnlyToTheirPhaseAndOverflowIsRejected() {
        for (event in listOf(RuleEvent.ATTACK, RuleEvent.HIT)) for (phase in RuleCombatPhase.entries) {
            fun execution(amount: Int): RuleExecution {
                val effect = if (phase == RuleCombatPhase.POWER) RuleEffect.AttackPowerModifier(amount) else RuleEffect.DamageModifier(amount)
                return RuleEngine(listOf(handler { _, _ -> listOf(effect) }), RuleRandom { 0 })
                    .execute(listOf(definition(event)), context(event, phase))
            }
            fun modified(execution: RuleExecution, base: Int) = if (phase == RuleCombatPhase.POWER) execution.modifiedAttackPower(base) else execution.modifiedDamage(base)
            assertEquals(15, modified(execution(5), 10))
            assertFailsWith<IllegalArgumentException> { modified(execution(Int.MAX_VALUE), 1) }
            assertFailsWith<IllegalArgumentException> { modified(execution(-11), 10) }
        }
    }

    @Test fun missingCombatPhaseTargetOrPhaseValueFailsBeforeHandler() {
        var called = false
        val engine = RuleEngine(listOf(handler { _, _ -> called = true; emptyList() }), RuleRandom { 0 })
        for (event in listOf(RuleEvent.ATTACK, RuleEvent.HIT)) {
            val base = context(event, RuleCombatPhase.POWER)
            for (invalid in listOf(base.copy(combatPhase = null), base.copy(targetId = null), base.copy(attackPower = null),
                base.copy(combatPhase = RuleCombatPhase.DAMAGE, baseDamage = null))) {
                assertFailsWith<IllegalArgumentException> { engine.execute(listOf(definition(event)), invalid) }
            }
        }
        assertFalse(called)
    }
}
