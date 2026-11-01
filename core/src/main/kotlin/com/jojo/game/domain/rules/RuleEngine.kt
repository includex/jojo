package com.jojo.game.domain.rules

import java.util.Collections

/** The first version of the rule contract. Unknown versions fail during registration. */
const val RULE_CONTRACT_VERSION = 1

enum class RuleEvent { TURN_START, TURN_END, ATTACK, HIT, DAMAGE_CALCULATION, ACTION_COMPLETED }
enum class RuleCombatPhase { POWER, DAMAGE }
enum class RuleBindingScope { GLOBAL, CHARACTER, EQUIPMENT }

/** Optional owner selector. Equipment bindings use equipped item IDs, never inventory contents. */
data class RuleBinding(val scope: RuleBindingScope = RuleBindingScope.GLOBAL, val targetId: Int? = null) {
    init {
        require(if (scope == RuleBindingScope.GLOBAL) targetId == null else targetId != null && targetId >= 0) {
            "A character/equipment binding needs a nonnegative id; global bindings have no id"
        }
    }
}


data class RuleDefinition(
    val id: String,
    val event: RuleEvent,
    val handlerId: String,
    val priority: Int = 0,
    val parameters: Map<String, String> = emptyMap(),
    val version: Int = RULE_CONTRACT_VERSION,
    val binding: RuleBinding = RuleBinding(),
)

/** Values are copied at the engine boundary before a handler receives them. */
data class RuleUnitSnapshot(
    val id: String,
    val hitPoints: Int,
    val maxHitPoints: Int,
    val magicPoints: Int,
    val maxMagicPoints: Int,
    val resources: Map<String, Int> = emptyMap(),
    val statuses: Set<String> = emptySet(),
    val characterId: Int? = null,
    val equipmentIds: List<Int> = emptyList(),
    val attack: Int = 0,
    val defense: Int = 0,
    val spirit: Int = 0,
    val level: Int = 1,
)

data class RuleContext(
    val event: RuleEvent,
    val actorId: String,
    val units: Map<String, RuleUnitSnapshot>,
    val targetId: String? = null,
    val baseDamage: Int? = null,
    val resourceBounds: Map<String, IntRange> = emptyMap(),
    val combatPhase: RuleCombatPhase? = null,
    val attackPower: Int? = null,
    val damageKind: String? = null,
    val ownerId: String? = null,
    val sourceId: String? = null,
)

sealed interface RuleEffect {
    data class HpDelta(val unitId: String, val amount: Int) : RuleEffect
    data class MpDelta(val unitId: String, val amount: Int) : RuleEffect
    data class ResourceDelta(val unitId: String, val resourceId: String, val amount: Int) : RuleEffect
    data class StatusAdd(val unitId: String, val statusId: String) : RuleEffect
    data class StatusRemove(val unitId: String, val statusId: String) : RuleEffect
    /** Additive damage adjustment; only legal during DAMAGE_CALCULATION. */
    data class DamageModifier(val amount: Int) : RuleEffect
    data class AttackPowerModifier(val amount: Int) : RuleEffect
}

fun interface RuleRandom { fun nextInt(bound: Int): Int }

interface RuleHandler {
    val id: String
    val supportedEvents: Set<RuleEvent> get() = RuleEvent.entries.toSet()
    /** Validation runs before any handler for this execution is called. */
    fun validate(parameters: Map<String, String>)
    fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom): List<RuleEffect>
}

/** Code extension point. The composition root supplies modules explicitly for each battle. */
interface RuleModule { val handlers: List<RuleHandler> }

data class RuleExecution(val effects: List<RuleEffect>) {
    /** Build a new state only after every effect has passed validation. No input state is mutated. */
    fun apply(
        context: RuleContext,
        resourceBounds: Map<String, IntRange> = context.resourceBounds,
        resourceDefaults: Map<String, Int> = emptyMap(),
    ): RuleContext {
        val result = context.units.mapValues { (_, unit) ->
            unit.copy(resources = unit.resources.toMap(), statuses = unit.statuses.toSet())
        }.toMutableMap()
        for (effect in effects) {
            if (effect is RuleEffect.DamageModifier || effect is RuleEffect.AttackPowerModifier) {
                require(context.event in setOf(RuleEvent.ATTACK, RuleEvent.HIT, RuleEvent.DAMAGE_CALCULATION))
                continue
            }
            val id = when (effect) {
                is RuleEffect.HpDelta -> effect.unitId
                is RuleEffect.MpDelta -> effect.unitId
                is RuleEffect.ResourceDelta -> effect.unitId
                is RuleEffect.StatusAdd -> effect.unitId
                is RuleEffect.StatusRemove -> effect.unitId
                is RuleEffect.DamageModifier, is RuleEffect.AttackPowerModifier -> error("Handled above")
            }
            val unit = requireNotNull(result[id]) { "Unknown effect target: $id" }
            result[id] = when (effect) {
                is RuleEffect.HpDelta -> unit.copy(hitPoints = (unit.hitPoints.toLong() + effect.amount).coerceIn(0L, unit.maxHitPoints.toLong()).toInt())
                is RuleEffect.MpDelta -> unit.copy(magicPoints = (unit.magicPoints.toLong() + effect.amount).coerceIn(0L, unit.maxMagicPoints.toLong()).toInt())
                is RuleEffect.ResourceDelta -> {
                    require(namespacedId(effect.resourceId))
                    val bounds = requireNotNull(resourceBounds[effect.resourceId]) { "Undeclared resource: ${effect.resourceId}" }
                    val initial = unit.resources[effect.resourceId] ?: resourceDefaults[effect.resourceId]
                        ?: error("Missing resource value: ${effect.resourceId}")
                    require(initial in bounds) { "Resource out of range: ${effect.resourceId}" }
                    val value = initial.toLong() + effect.amount
                    require(value in bounds.first.toLong()..bounds.last.toLong()) { "Resource out of range: ${effect.resourceId}" }
                    unit.copy(resources = unit.resources + (effect.resourceId to value.toInt()))
                }
                is RuleEffect.StatusAdd -> {
                    require(namespacedId(effect.statusId))
                    unit.copy(statuses = unit.statuses + effect.statusId)
                }
                is RuleEffect.StatusRemove -> {
                    require(namespacedId(effect.statusId))
                    unit.copy(statuses = unit.statuses - effect.statusId)
                }
                is RuleEffect.DamageModifier, is RuleEffect.AttackPowerModifier -> error("Handled above")
            }
        }
        modifiedDamage(context.baseDamage ?: 0)
        return context.copy(units = Collections.unmodifiableMap(result.mapValues { (_, unit) ->
            unit.copy(
                resources = Collections.unmodifiableMap(unit.resources.toMap()),
                statuses = Collections.unmodifiableSet(unit.statuses.toSet()),
                        equipmentIds = Collections.unmodifiableList(unit.equipmentIds.toList()),
            )
        }))
    }

    fun modifiedAttackPower(basePower: Int): Int {
        val result = effects.filterIsInstance<RuleEffect.AttackPowerModifier>()
            .fold(basePower.toLong()) { sum, effect -> Math.addExact(sum, effect.amount.toLong()) }
        require(result in 0..Int.MAX_VALUE.toLong()) { "Modified attack power is out of range" }
        return result.toInt()
    }

    fun modifiedDamage(baseDamage: Int): Int {
        val result = effects.filterIsInstance<RuleEffect.DamageModifier>()
            .fold(baseDamage.toLong()) { sum, effect -> Math.addExact(sum, effect.amount.toLong()) }
        require(result in 0..Int.MAX_VALUE.toLong()) { "Modified damage is out of range" }
        return result.toInt()
    }
}

/**
 * Evaluates one event without touching live battle state. Validation of the complete effect plan
 * happens before the plan is returned to an adapter for application.
 */
class RuleEngine(
    handlers: List<RuleHandler>,
    private val random: RuleRandom,
    private val maxRulesPerEvent: Int = 128,
    private val maxEffectsPerEvent: Int = 512,
) {
    private val handlersById: Map<String, RuleHandler>
    private val evaluating = ThreadLocal.withInitial { false }

    init {
        require(maxRulesPerEvent > 0 && maxEffectsPerEvent > 0)
        require(handlers.none { it.id.isBlank() })
        require(handlers.map { it.id }.toSet().size == handlers.size) { "Duplicate rule handler" }
        handlersById = handlers.associateBy { it.id }
    }

    /** Validate a complete config when it is loaded, before the first battle event. */
    fun validate(definitions: List<RuleDefinition>) {
        require(definitions.map { it.id }.toSet().size == definitions.size) { "Duplicate rule id" }
        definitions.forEach { definition ->
            require(definition.version == RULE_CONTRACT_VERSION) { "Unsupported rule version: ${definition.version}" }
            require(definition.id.isNotBlank())
            val handler = requireNotNull(handlersById[definition.handlerId]) { "Unknown rule handler: ${definition.handlerId}" }
            require(definition.event in handler.supportedEvents) {
                "Handler ${definition.handlerId} does not support ${definition.event}"
            }
            handler.validate(definition.parameters)
        }
    }

    fun execute(definitions: List<RuleDefinition>, context: RuleContext): RuleExecution {
        check(!evaluating.get()) { "Recursive rule evaluation" }
        evaluating.set(true)
        try {
            validateContext(context)
            validate(definitions)
            val applicable = definitions.filter { it.event == context.event }
                .sortedWith(compareBy<RuleDefinition> { it.priority }.thenBy { it.id })
            require(applicable.size <= maxRulesPerEvent) { "Rule budget exceeded" }
            val frozen = context.copy(
                units = Collections.unmodifiableMap(context.units.mapValues { (_, unit) ->
                    unit.copy(
                        resources = Collections.unmodifiableMap(unit.resources.toMap()),
                        statuses = Collections.unmodifiableSet(unit.statuses.toSet()),
                        equipmentIds = Collections.unmodifiableList(unit.equipmentIds.toList()),
                    )
                }),
                resourceBounds = Collections.unmodifiableMap(context.resourceBounds.toMap()),
            )
            val effects = ArrayList<RuleEffect>()
            val eventRandom = if (context.event in setOf(RuleEvent.DAMAGE_CALCULATION, RuleEvent.ATTACK, RuleEvent.HIT)) {
                RuleRandom { error("Random draws are forbidden during damage calculation") }
            } else random
            for (definition in applicable) {
                val ownerId = if (context.event == RuleEvent.HIT) requireNotNull(context.targetId) else context.actorId
                val owner = frozen.units.getValue(ownerId)
                val binding = definition.binding
                val matches = when (binding.scope) {
                    RuleBindingScope.GLOBAL -> true
                    RuleBindingScope.CHARACTER -> owner.characterId == binding.targetId
                    RuleBindingScope.EQUIPMENT -> binding.targetId in owner.equipmentIds
                }
                if (!matches) continue
                val scoped = frozen.copy(ownerId = ownerId, sourceId = when (binding.scope) {
                    RuleBindingScope.GLOBAL -> "global"
                    RuleBindingScope.CHARACTER -> "character:${binding.targetId}"
                    RuleBindingScope.EQUIPMENT -> "equipment:${binding.targetId}"
                })
                val produced = handlersById.getValue(definition.handlerId)
                    .evaluate(scoped, Collections.unmodifiableMap(definition.parameters.toMap()), eventRandom)
                require(produced.size <= maxEffectsPerEvent - effects.size) { "Effect budget exceeded" }
                effects.addAll(produced)
            }
            effects.forEach { validateEffect(it, frozen) }
            return RuleExecution(Collections.unmodifiableList(effects.toList()))
        } finally {
            evaluating.set(false)
        }
    }

    private fun validateContext(context: RuleContext) {
        require(context.units.containsKey(context.actorId)) { "Unknown actor" }
        require(context.targetId == null || context.units.containsKey(context.targetId)) { "Unknown target" }
        require(context.baseDamage == null || context.baseDamage >= 0)
        require(context.attackPower == null || context.attackPower >= 0)
        if (context.event == RuleEvent.ATTACK || context.event == RuleEvent.HIT) {
            require(context.targetId != null && context.combatPhase != null) { "Combat rules require a target and phase" }
            if (context.combatPhase == RuleCombatPhase.POWER) require(context.attackPower != null)
            else require(context.baseDamage != null)
        }
        context.resourceBounds.forEach { (id, bounds) ->
            require(namespacedId(id) && !bounds.isEmpty()) { "Invalid resource bounds: $id" }
        }
        context.units.forEach { (id, unit) ->
            require(id == unit.id && id.isNotBlank())
            require(unit.maxHitPoints >= 0 && unit.hitPoints in 0..unit.maxHitPoints)
            require(unit.maxMagicPoints >= 0 && unit.magicPoints in 0..unit.maxMagicPoints)
            unit.resources.forEach { (key, value) ->
                require(namespacedId(key)) { "Invalid resource: $key" }
                context.resourceBounds[key]?.let { bounds -> require(value in bounds) { "Resource out of range: $key" } }
            }
            require(unit.statuses.all(::namespacedId))
        }
    }

    private fun validateEffect(effect: RuleEffect, context: RuleContext) {
        if (context.event == RuleEvent.DAMAGE_CALCULATION) require(effect is RuleEffect.DamageModifier) {
            "Damage calculation may only modify damage"
        }
        if (context.event == RuleEvent.ATTACK || context.event == RuleEvent.HIT) {
            require(if (context.combatPhase == RuleCombatPhase.POWER) effect is RuleEffect.AttackPowerModifier
                else effect is RuleEffect.DamageModifier) { "Combat rules may only modify the current phase's value" }
        }
        when (effect) {
            is RuleEffect.HpDelta -> require(context.units.containsKey(effect.unitId))
            is RuleEffect.MpDelta -> require(context.units.containsKey(effect.unitId))
            is RuleEffect.ResourceDelta -> {
                require(context.units.containsKey(effect.unitId))
                require(namespacedId(effect.resourceId))
            }
            is RuleEffect.StatusAdd -> {
                require(context.units.containsKey(effect.unitId))
                require(namespacedId(effect.statusId))
            }
            is RuleEffect.StatusRemove -> {
                require(context.units.containsKey(effect.unitId))
                require(namespacedId(effect.statusId))
            }
            is RuleEffect.DamageModifier -> require(context.event == RuleEvent.DAMAGE_CALCULATION ||
                (context.event in setOf(RuleEvent.ATTACK, RuleEvent.HIT) && context.combatPhase == RuleCombatPhase.DAMAGE))
            is RuleEffect.AttackPowerModifier -> require(context.event in setOf(RuleEvent.ATTACK, RuleEvent.HIT) &&
                context.combatPhase == RuleCombatPhase.POWER)
        }
    }

}

private fun namespacedId(value: String): Boolean =
    value.matches(Regex("[A-Za-z][A-Za-z0-9_-]*\\.[A-Za-z][A-Za-z0-9_.-]*"))

/** Generic recovery accepts a precomputed legacy amount, preserving its existing formula. */
object RecoveryRuleHandler : RuleHandler {
    override val id = "core.recovery"
    override val supportedEvents = setOf(RuleEvent.TURN_START, RuleEvent.TURN_END, RuleEvent.ACTION_COMPLETED)

    override fun validate(parameters: Map<String, String>) {
        require(parameters.keys.all { it == "hpAmount" || it == "mpAmount" }) { "Unknown recovery parameter" }
        require("hpAmount" in parameters || "mpAmount" in parameters) { "Recovery amount missing" }
        require(parameters.values.all { it.toIntOrNull()?.let { amount -> amount >= 0 } == true }) { "Invalid recovery amount" }
    }

    override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom): List<RuleEffect> {
        val actor = context.units.getValue(context.actorId)
        return buildList {
            parameters["hpAmount"]?.let { amount ->
                add(RuleEffect.HpDelta(actor.id, minOf(amount.toInt(), actor.maxHitPoints - actor.hitPoints)))
            }
            parameters["mpAmount"]?.let { amount ->
                add(RuleEffect.MpDelta(actor.id, minOf(amount.toInt(), actor.maxMagicPoints - actor.magicPoints)))
            }
        }
    }
}

/** Generic resource regeneration; the resource must be declared in extension state. */
object ResourceRegenerationRuleHandler : RuleHandler {
    override val id = "core.resourceRegeneration"
    override val supportedEvents = setOf(RuleEvent.TURN_START, RuleEvent.TURN_END, RuleEvent.ACTION_COMPLETED)

    override fun validate(parameters: Map<String, String>) {
        require(parameters.keys == setOf("resource", "amount")) { "Expected resource and amount" }
        require(namespacedId(parameters.getValue("resource"))) { "Invalid resource name" }
        require(parameters.getValue("amount").toIntOrNull()?.let { it >= 0 } == true) { "Invalid regeneration amount" }
    }

    override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom): List<RuleEffect> {
        val resourceId = parameters.getValue("resource")
        val bounds = requireNotNull(context.resourceBounds[resourceId]) { "Undeclared resource: $resourceId" }
        // A declared per-character resource may not belong to this actor (for example an enemy).
        val current = context.units.getValue(context.actorId).resources[resourceId] ?: return emptyList()
        require(current in bounds) { "Resource out of range: $resourceId" }
        val amount = minOf(parameters.getValue("amount").toInt().toLong(), bounds.last.toLong() - current).toInt()
        return listOf(RuleEffect.ResourceDelta(context.actorId, resourceId, amount))
    }
}

/** Simple additive damage adjustment for data-defined rules. */
object DamageModifierRuleHandler : RuleHandler {
    override val id = "core.damageModifier"
    override val supportedEvents = setOf(RuleEvent.DAMAGE_CALCULATION)

    override fun validate(parameters: Map<String, String>) {
        require(parameters.keys == setOf("amount")) { "Expected amount" }
        require(parameters.getValue("amount").toIntOrNull() != null) { "Invalid damage modifier" }
    }

    override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom): List<RuleEffect> =
        listOf(RuleEffect.DamageModifier(parameters.getValue("amount").toInt()))
}
