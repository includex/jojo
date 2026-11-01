package com.jojo.game.application.battle

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleAttributeCalculator
import com.jojo.game.domain.rules.*

/** Bridges the pure rule plan to one battle's live units and campaign extension resources. */
class BattleRuleRuntime(
    private val definitions: List<RuleDefinition> = emptyList(),
    private val engine: RuleEngine? = null,
    private val extensionState: ExtensionState? = null,
) {
    private val legacyRecovery = RuleEngine(listOf(RecoveryRuleHandler), RuleRandom { 0 })

    fun recoverHp(unit: BattleUnit, amount: Int) {
        val context = context(RuleEvent.TURN_START, unit)
        val definition = RuleDefinition("core.legacyNearbyHealthRecovery", RuleEvent.TURN_START,
            RecoveryRuleHandler.id, parameters = mapOf("hpAmount" to amount.toString()))
        apply(legacyRecovery.execute(listOf(definition), context), context, mapOf(unit.id to unit))
    }

    fun onTurnStart(unit: BattleUnit) = execute(RuleEvent.TURN_START, unit)

    fun onTurnEnd(unit: BattleUnit) = execute(RuleEvent.TURN_END, unit)

    fun onActionCompleted(unit: BattleUnit) = execute(RuleEvent.ACTION_COMPLETED, unit)

    fun modifyAttackPower(attacker: BattleUnit, target: BattleUnit, power: Int, kind: String = "physical"): Int {
        var result = power
        for (event in listOf(RuleEvent.ATTACK, RuleEvent.HIT)) {
            if (engine == null || definitions.none { it.event == event }) continue
            val context = context(event, attacker, target).copy(
                combatPhase = RuleCombatPhase.POWER, attackPower = result, damageKind = kind,
                ownerId = if (event == RuleEvent.HIT) target.id else attacker.id,
            )
            result = engine.execute(definitions, context).modifiedAttackPower(result)
        }
        return result
    }

    fun modifyPhysicalDamage(attacker: BattleUnit, target: BattleUnit, damage: Int): Int =
        modifyDamage(attacker, target, damage, "physical")

    fun modifyDamage(attacker: BattleUnit, target: BattleUnit, damage: Int, kind: String): Int {
        if (engine == null || definitions.none { it.event in setOf(RuleEvent.ATTACK, RuleEvent.HIT, RuleEvent.DAMAGE_CALCULATION) }) return damage
        val intrinsicPower = BattleAttributeCalculator.effective(attacker,
            if (kind == "magic") BattleAttribute.SPIRIT else BattleAttribute.ATTACK)
        val power = modifyAttackPower(attacker, target, intrinsicPower, kind)
        var result = damage
        for (event in listOf(RuleEvent.ATTACK, RuleEvent.HIT, RuleEvent.DAMAGE_CALCULATION)) {
            if (engine == null || definitions.none { it.event == event }) continue
            val context = context(event, attacker, target, result).copy(
                combatPhase = if (event == RuleEvent.DAMAGE_CALCULATION) null else RuleCombatPhase.DAMAGE,
                attackPower = power, damageKind = kind, ownerId = if (event == RuleEvent.HIT) target.id else attacker.id,
            )
            result = engine.execute(definitions, context).modifiedDamage(result)
        }
        return result
    }

    private fun execute(event: RuleEvent, actor: BattleUnit) {
        if (engine == null || definitions.none { it.event == event }) return
        val context = context(event, actor)
        val before = extensionState?.snapshot()
        try {
            apply(engine.execute(definitions, context), context, mapOf(actor.id to actor))
        } catch (failure: Throwable) {
            if (before != null) extensionState?.restore(before)
            throw failure
        }
    }

    private fun context(event: RuleEvent, actor: BattleUnit, target: BattleUnit? = null, damage: Int? = null): RuleContext {
        val units = listOfNotNull(actor, target).associate { unit ->
            val resources = extensionState?.declarations()?.mapNotNull { declaration ->
                val characterId = unit.characterId.takeIf { declaration.perCharacter }
                if (declaration.perCharacter && characterId == null) null
                else declaration.resourceKey to extensionState.read(declaration.resourceKey, characterId)
            }?.toMap().orEmpty()
            unit.id to RuleUnitSnapshot(unit.id, unit.hitPoints, unit.maxHitPoints,
                unit.magicPoints, unit.maxMagicPoints, resources,
                unit.statuses.keys.map { "core.${it.name.lowercase()}" }.toSet(),
                characterId = unit.characterId, equipmentIds = unit.equipmentIds.toList(),
                attack = unit.attack, defense = unit.defense, spirit = unit.spirit, level = unit.level)
        }
        val bounds = extensionState?.declarations()?.associate { it.resourceKey to (it.minValue..it.maxValue) }.orEmpty()
        return RuleContext(event, actor.id, units, target?.id, damage, bounds)
    }

    private fun apply(execution: RuleExecution, context: RuleContext, units: Map<String, BattleUnit>) {
        val declarations = extensionState?.declarations().orEmpty()
        val bounds = declarations.associate { it.resourceKey to it.minValue..it.maxValue }
        execution.effects.forEach { effect ->
            val status = when (effect) {
                is RuleEffect.StatusAdd -> effect.statusId
                is RuleEffect.StatusRemove -> effect.statusId
                else -> null
            }
            require(status == null || (status.startsWith("core.") && BattleStatus.entries.any {
                it.name.equals(status.removePrefix("core."), true)
            })) { "Unsupported battle status $status" }
        }
        val after = execution.apply(context, bounds)
        // Check every adapter mapping before any live value changes.
        after.units.forEach { (id, result) ->
            val unit = units.getValue(id)
            result.statuses.forEach { status ->
                require(status.startsWith("core.") && BattleStatus.entries.any { it.name.equals(status.removePrefix("core."), true) }) {
                    "Unsupported battle status $status"
                }
            }
            result.resources.forEach { (resource, _) ->
                val declaration = declarations.firstOrNull { it.resourceKey == resource }
                    ?: error("Unknown extension resource $resource")
                require(!declaration.perCharacter || unit.characterId != null) { "Character resource without character ID" }
            }
        }
        after.units.forEach { (id, result) ->
            val unit = units.getValue(id)
            unit.hitPoints = result.hitPoints
            unit.magicPoints = result.magicPoints
            val nextStatuses = result.statuses.associate { status ->
                BattleStatus.entries.first { it.name.equals(status.removePrefix("core."), true) } to
                    (unit.statuses[BattleStatus.entries.first { it.name.equals(status.removePrefix("core."), true) }] ?: 1)
            }
            unit.statuses.keys.retainAll(nextStatuses.keys)
            nextStatuses.forEach { (status, rounds) -> unit.statuses.putIfAbsent(status, rounds) }
            result.resources.forEach { (resource, value) ->
                val declaration = declarations.first { it.resourceKey == resource }
                extensionState!!.write(declaration.resourceKey, value, unit.characterId.takeIf { declaration.perCharacter })
            }
        }
    }
}
