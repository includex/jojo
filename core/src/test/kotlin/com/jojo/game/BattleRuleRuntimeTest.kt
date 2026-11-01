package com.jojo.game

import com.jojo.game.application.battle.Battle
import com.jojo.game.application.battle.BattleRuleRuntime
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.SkillEffects
import com.jojo.game.domain.battle.combat.FlatPhysicalDamageContext
import com.jojo.game.domain.battle.combat.PhysicalCriticalRateContext
import com.jojo.game.domain.battle.combat.PhysicalDamageCalculator
import com.jojo.game.domain.battle.combat.PhysicalDamageRateContext
import com.jojo.game.domain.rules.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BattleRuleRuntimeTest {
    @Test
    fun turnStartRegeneratesDeclaredCampaignResourceThroughRealSettlement() {
        val state = ExtensionState(listOf(ExtensionResourceDeclaration("yitian", "innerEnergy", 0, 0, 100, true)))
        val rules = listOf(RuleDefinition("innerEnergy", RuleEvent.TURN_START,
            ResourceRegenerationRuleHandler.id, parameters = mapOf("resource" to "yitian.innerEnergy", "amount" to "5")))
        val runtime = BattleRuleRuntime(rules, RuleEngine(listOf(ResourceRegenerationRuleHandler), RuleRandom { 0 }), state)
        val unit = BattleUnit("hero", "Hero", Faction.PLAYER, 0, 0, characterId = 7)
        val battle = Battle(listOf(unit), emptyList(), ruleRuntime = runtime)

        battle.roundLifecycle.settleActiveCampStart()

        assertEquals(5, state.read("yitian.innerEnergy", 7))
    }

    @Test
    fun legacyNearbyHealingKeepsTruncationAndHealthCap() {
        val runtime = BattleRuleRuntime()
        val target = BattleUnit("ally", "Ally", Faction.PLAYER, 0, 0,
            hitPoints = 96, maxHitPoints = 101)

        runtime.recoverHp(target, target.maxHitPoints * 7 / 100)

        assertEquals(101, target.hitPoints)
    }

    @Test
    fun nearbyHealthSkillUsesRuleRecoveryAtCampStart() {
        val caster = BattleUnit("healer", "Healer", Faction.PLAYER, 0, 0,
            skills = mapOf(SkillEffects.id("nearbyHealthRecovery") to 7))
        val target = BattleUnit("ally", "Ally", Faction.PLAYER, 1, 0,
            hitPoints = 60, maxHitPoints = 101)
        val battle = Battle(listOf(caster, target), emptyList())

        battle.roundLifecycle.settleActiveCampStart()

        assertEquals(67, target.hitPoints)
    }

    @Test
    fun actionRuleRunsOnceWhenActionIsCompleted() {
        val state = ExtensionState(listOf(ExtensionResourceDeclaration("test", "energy", 0, 0, 10, true)))
        val rules = listOf(RuleDefinition("actionEnergy", RuleEvent.ACTION_COMPLETED,
            ResourceRegenerationRuleHandler.id, parameters = mapOf("resource" to "test.energy", "amount" to "1")))
        val runtime = BattleRuleRuntime(rules, RuleEngine(listOf(ResourceRegenerationRuleHandler), RuleRandom { 0 }), state)
        val unit = BattleUnit("hero", "Hero", Faction.PLAYER, 0, 0, characterId = 7)
        Battle(listOf(unit), emptyList(), ruleRuntime = runtime)

        unit.markActionComplete()
        unit.markActionComplete()

        assertEquals(1, state.read("test.energy", 7))
    }

    @Test
    fun deferredActionDoesNotWriteCampaignResourceUntilCommit() {
        val state = ExtensionState(listOf(ExtensionResourceDeclaration("test", "energy", 0, 0, 10, true)))
        val rules = listOf(RuleDefinition("actionEnergy", RuleEvent.ACTION_COMPLETED,
            ResourceRegenerationRuleHandler.id, parameters = mapOf("resource" to "test.energy", "amount" to "1")))
        val runtime = BattleRuleRuntime(rules, RuleEngine(listOf(ResourceRegenerationRuleHandler), RuleRandom { 0 }), state)
        val unit = BattleUnit("hero", "Hero", Faction.PLAYER, 0, 0, characterId = 7)
        val battle = Battle(listOf(unit), emptyList(), ruleRuntime = runtime)
        val staged = mutableListOf<() -> Unit>()
        battle.journal.recordStagedCompletionSideEffects(staged)

        unit.markActionComplete()
        assertEquals(0, state.read("test.energy", 7))
        assertEquals(1, staged.size)
        battle.journal.recordStagedCompletionSideEffects(null)
        staged.single().invoke()

        assertEquals(1, state.read("test.energy", 7))
    }

    @Test
    fun normalPhysicalCalculationAppliesConfiguredDamageModifier() {
        val attacker = BattleUnit("attacker", "Attacker", Faction.PLAYER, 0, 0)
        val target = BattleUnit("target", "Target", Faction.ENEMY, 1, 0)
        fun damage() = PhysicalDamageCalculator.calculatePhysicalDamage(
            attacker, target, 40,
            PhysicalDamageRateContext(false, 5, false, false, 0),
            FlatPhysicalDamageContext(),
            PhysicalCriticalRateContext(incomingDirection = 0),
            0,
        )
        val ordinary = damage()
        val rules = listOf(RuleDefinition("bonusDamage", RuleEvent.DAMAGE_CALCULATION,
            DamageModifierRuleHandler.id, parameters = mapOf("amount" to "9")))
        val runtime = BattleRuleRuntime(rules, RuleEngine(listOf(DamageModifierRuleHandler), RuleRandom { 0 }))
        Battle(listOf(attacker, target), emptyList(), ruleRuntime = runtime)

        assertEquals(ordinary + 9, damage())
    }
    @Test
    fun attackPowerAndBothSidesDamageRulesArePureAcrossPreviews() {
        val handler = object : RuleHandler {
            override val id = "test.combat"
            override fun validate(parameters: Map<String, String>) = Unit
            override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom): List<RuleEffect> =
                when (context.combatPhase) {
                    RuleCombatPhase.POWER -> listOf(RuleEffect.AttackPowerModifier(if (context.event == RuleEvent.ATTACK) 20 else -4))
                    RuleCombatPhase.DAMAGE -> listOf(RuleEffect.DamageModifier(if (context.event == RuleEvent.ATTACK) 9 else -3))
                    null -> emptyList()
                }
        }
        val attacker = BattleUnit("attacker", "Attacker", Faction.PLAYER, 0, 0, attack = 80)
        val target = BattleUnit("target", "Target", Faction.ENEMY, 1, 0)
        val runtime = BattleRuleRuntime(listOf(
            RuleDefinition("attack", RuleEvent.ATTACK, handler.id),
            RuleDefinition("hit", RuleEvent.HIT, handler.id),
        ), RuleEngine(listOf(handler), RuleRandom { error("Preview consumed random") }))
        val baseline = PhysicalDamageCalculator.basePhysicalDamage(attacker, target,
            com.jojo.game.domain.battle.combat.BasePhysicalDamageContext())
        Battle(listOf(attacker, target), emptyList(), ruleRuntime = runtime)

        repeat(3) {
            assertEquals(baseline + 8, PhysicalDamageCalculator.basePhysicalDamage(attacker, target,
                com.jojo.game.domain.battle.combat.BasePhysicalDamageContext()))
            assertEquals(46, runtime.modifyPhysicalDamage(attacker, target, 40))
        }
        // Forced and special physical attacks can supply a fixed base override; final hooks still run.
        assertEquals(46, PhysicalDamageCalculator.calculatePhysicalDamage(
            attacker, target, 999,
            PhysicalDamageRateContext(false, 5, false, false, 0),
            FlatPhysicalDamageContext(), PhysicalCriticalRateContext(incomingDirection = 0),
            0, overrideDamage = 40,
        ))
        assertEquals(80, attacker.attack)
        assertEquals(100, attacker.hitPoints)
        assertEquals(100, target.hitPoints)
    }

    @Test
    fun turnRulesUseActiveCampInsteadOfGroupedPlayerSide() {
        val handler = object : RuleHandler {
            override val id = "test.turn"
            override fun validate(parameters: Map<String, String>) = Unit
            override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom) =
                listOf(RuleEffect.HpDelta(context.actorId, 1))
        }
        val player = BattleUnit("player", "Player", Faction.PLAYER, 0, 0, hitPoints = 50, maxHitPoints = 100)
        val friend = BattleUnit("friend", "Friend", Faction.FRIEND, 1, 0, hitPoints = 50, maxHitPoints = 100)
        val battle = Battle(listOf(player, friend), emptyList(), ruleRuntime = BattleRuleRuntime(listOf(
            RuleDefinition("start", RuleEvent.TURN_START, handler.id),
            RuleDefinition("end", RuleEvent.TURN_END, handler.id),
        ), RuleEngine(listOf(handler), RuleRandom { 0 })))

        battle.roundLifecycle.settleActiveCampStart()
        battle.roundLifecycle.settleActiveCampEnd()
        assertEquals(52, player.hitPoints)
        assertEquals(50, friend.hitPoints)
    }

    @Test
    fun projectedEquipmentUsesRealItemIdsAndSkipsEmptySlots() {
        val projector = com.jojo.game.application.battle.BattleUnitProjector(null, null,
            mapOf(7 to listOf(2, 1, 3, 1, 1), 8 to listOf(3, 1, 1, 1, 1)))
        fun project(id: Int) = projector.project(com.jojo.game.domain.scenario.ScenarioBattleUnit(
            id, id, com.jojo.game.domain.scenario.ScenarioUnitFaction.ENEMY, 0, 0))

        assertEquals(listOf(0, 71), project(7).equipmentIds)
        assertEquals(listOf(1), project(8).equipmentIds)
        assertEquals(emptyList(), project(9).equipmentIds)
    }

    @Test
    fun equipmentBindingsFollowLevelAndPostsReprojection() {
        val handler = object : RuleHandler {
            override val id = "test.gear"
            override fun validate(parameters: Map<String, String>) = Unit
            override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom) =
                listOf(RuleEffect.DamageModifier(7))
        }
        val attacker = BattleUnit("hero", "Hero", Faction.PLAYER, 0, 0, equipmentIds = listOf(0))
        val target = BattleUnit("enemy", "Enemy", Faction.ENEMY, 1, 0)
        val runtime = BattleRuleRuntime(listOf(RuleDefinition("gear", RuleEvent.DAMAGE_CALCULATION,
            handler.id, binding = RuleBinding(RuleBindingScope.EQUIPMENT, 1))),
            RuleEngine(listOf(handler), RuleRandom { 0 }))
        assertEquals(40, runtime.modifyPhysicalDamage(attacker, target, 40))

        attacker.refreshLevelDerivedState(attacker.copy(equipmentIds = listOf(1)))
        assertEquals(47, runtime.modifyPhysicalDamage(attacker, target, 40))
        attacker.refreshPostsDerivedState(attacker.copy(equipmentIds = listOf(0)), refreshAbilityPhase = false)
        assertEquals(40, runtime.modifyPhysicalDamage(attacker, target, 40))
    }

}
