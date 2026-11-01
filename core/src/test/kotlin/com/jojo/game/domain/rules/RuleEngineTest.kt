package com.jojo.game.domain.rules

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RuleEngineTest {
    private val random = RuleRandom { 0 }
    private val actor = RuleUnitSnapshot("actor", 8, 10, 2, 5)
    private val context = RuleContext(RuleEvent.TURN_START, "actor", mapOf("actor" to actor))

    @Test
    fun `rules run by priority then id regardless of input order`() {
        val handler = object : RuleHandler {
            override val id = "test.order"
            override fun validate(parameters: Map<String, String>) { require(parameters.keys == setOf("label")) }
            override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom) =
                listOf(RuleEffect.StatusAdd(context.actorId, "test.${parameters.getValue("label")}"))
        }
        val rules = listOf(
            RuleDefinition("z", RuleEvent.TURN_START, handler.id, 1, mapOf("label" to "z")),
            RuleDefinition("b", RuleEvent.TURN_START, handler.id, 0, mapOf("label" to "b")),
            RuleDefinition("a", RuleEvent.TURN_START, handler.id, 0, mapOf("label" to "a")),
        )
        val effects = RuleEngine(listOf(handler), random).execute(rules, context).effects
        assertEquals(listOf("test.a", "test.b", "test.z"), effects.map { (it as RuleEffect.StatusAdd).statusId })
    }

    @Test
    fun `invalid definition fails before any handler evaluates`() {
        var called = false
        val handler = object : RuleHandler {
            override val id = "test.handler"
            override fun validate(parameters: Map<String, String>) { require(parameters.isEmpty()) }
            override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom): List<RuleEffect> {
                called = true
                return emptyList()
            }
        }
        val engine = RuleEngine(listOf(handler), random)
        assertFailsWith<IllegalArgumentException> {
            engine.execute(listOf(
                RuleDefinition("first", RuleEvent.TURN_START, handler.id),
                RuleDefinition("second", RuleEvent.TURN_START, "missing"),
            ), context)
        }
        assertEquals(false, called)
        assertFailsWith<IllegalArgumentException> {
            engine.execute(listOf(
                RuleDefinition("duplicate", RuleEvent.TURN_START, handler.id),
                RuleDefinition("duplicate", RuleEvent.TURN_START, handler.id),
            ), context)
        }
        assertFailsWith<IllegalArgumentException> {
            engine.execute(listOf(RuleDefinition("version", RuleEvent.TURN_START, handler.id, version = 2)), context)
        }
    }

    @Test
    fun `recovery clamps to maximum and resource application is atomic`() {
        val engine = RuleEngine(listOf(RecoveryRuleHandler, ResourceRegenerationRuleHandler), random)
        val recovery = engine.execute(listOf(RuleDefinition(
            "recover", RuleEvent.TURN_START, RecoveryRuleHandler.id,
            parameters = mapOf("hpAmount" to "8", "mpAmount" to "4"),
        )), context)
        val recovered = recovery.apply(context).units.getValue("actor")
        assertEquals(10, recovered.hitPoints)
        assertEquals(5, recovered.magicPoints)
        assertEquals(8, actor.hitPoints)

        val resourceContext = context.copy(
            units = mapOf("actor" to actor.copy(resources = mapOf("yitian.innerEnergy" to 0))),
            resourceBounds = mapOf("yitian.innerEnergy" to 0..10),
        )
        val resource = engine.execute(listOf(RuleDefinition(
            "regen", RuleEvent.TURN_START, ResourceRegenerationRuleHandler.id,
            parameters = mapOf("resource" to "yitian.innerEnergy", "amount" to "5"),
        )), resourceContext)
        assertFailsWith<IllegalArgumentException> {
            resource.apply(resourceContext, mapOf("yitian.innerEnergy" to 0..4))
        }
        assertEquals(emptyMap(), context.units.getValue("actor").resources)
        assertEquals(5, resource.apply(resourceContext)
            .units.getValue("actor").resources.getValue("yitian.innerEnergy"))
    }

    @Test
    fun `regeneration saturates at declared cap across repeated turns`() {
        val engine = RuleEngine(listOf(ResourceRegenerationRuleHandler), random)
        val rule = RuleDefinition(
            "regen", RuleEvent.TURN_START, ResourceRegenerationRuleHandler.id,
            parameters = mapOf("resource" to "yitian.innerEnergy", "amount" to "5"),
        )
        var state = context.copy(
            units = mapOf("actor" to actor.copy(resources = mapOf("yitian.innerEnergy" to 0))),
            resourceBounds = mapOf("yitian.innerEnergy" to 0..100),
        )
        repeat(25) { state = engine.execute(listOf(rule), state).apply(state) }
        assertEquals(100, state.units.getValue("actor").resources.getValue("yitian.innerEnergy"))
    }

    @Test
    fun `declared per character resource skips actor without a value`() {
        val engine = RuleEngine(listOf(ResourceRegenerationRuleHandler), random)
        val rule = RuleDefinition(
            "regen", RuleEvent.TURN_START, ResourceRegenerationRuleHandler.id,
            parameters = mapOf("resource" to "yitian.innerEnergy", "amount" to "5"),
        )
        val enemy = context.copy(resourceBounds = mapOf("yitian.innerEnergy" to 0..100))
        assertEquals(emptyList(), engine.execute(listOf(rule), enemy).effects)
    }

    @Test
    fun `damage modifier only applies to damage event and respects bounds`() {
        val handler = object : RuleHandler {
            override val id = "test.damage"
            override fun validate(parameters: Map<String, String>) { require(parameters.isEmpty()) }
            override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom) =
                listOf(RuleEffect.DamageModifier(3))
        }
        val engine = RuleEngine(listOf(handler), random)
        assertFailsWith<IllegalArgumentException> {
            engine.execute(listOf(RuleDefinition("invalid", RuleEvent.TURN_START, handler.id)), context)
        }
        val damageContext = context.copy(event = RuleEvent.DAMAGE_CALCULATION, baseDamage = 5)
        val result = engine.execute(listOf(RuleDefinition("valid", RuleEvent.DAMAGE_CALCULATION, handler.id)), damageContext)
        assertEquals(8, result.modifiedDamage(5))
    }

    @Test
    fun `effect budget and recursive calls fail safely`() {
        lateinit var engine: RuleEngine
        val handler = object : RuleHandler {
            override val id = "test.recursive"
            override fun validate(parameters: Map<String, String>) { require(parameters.isEmpty()) }
            override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom): List<RuleEffect> {
                assertFailsWith<IllegalStateException> {
                    engine.execute(listOf(RuleDefinition("nested", context.event, id)), context)
                }
                return listOf(RuleEffect.HpDelta(context.actorId, 1), RuleEffect.HpDelta(context.actorId, 1))
            }
        }
        engine = RuleEngine(listOf(handler), random, maxEffectsPerEvent = 1)
        assertFailsWith<IllegalArgumentException> {
            engine.execute(listOf(RuleDefinition("top", RuleEvent.TURN_START, handler.id)), context)
        }
    }

    @Test
    fun `duplicate handlers fail registration and random source is injected`() {
        val handler = object : RuleHandler {
            override val id = "test.random"
            override fun validate(parameters: Map<String, String>) { require(parameters.isEmpty()) }
            override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom) =
                listOf(RuleEffect.HpDelta(context.actorId, random.nextInt(10)))
        }
        assertFailsWith<IllegalArgumentException> { RuleEngine(listOf(handler, handler), random) }
        val engine = RuleEngine(listOf(handler), RuleRandom { bound -> bound - 1 })
        assertEquals(
            listOf(RuleEffect.HpDelta("actor", 9)),
            engine.execute(listOf(RuleDefinition("random", RuleEvent.TURN_START, handler.id)), context).effects,
        )
    }

    @Test
    fun `damage calculation cannot consume random and built in events validate at load time`() {
        var draws = 0
        val random = RuleRandom { draws++; 0 }
        val handler = object : RuleHandler {
            override val id = "test.randomDamage"
            override fun validate(parameters: Map<String, String>) { require(parameters.isEmpty()) }
            override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom): List<RuleEffect> {
                random.nextInt(2)
                return listOf(RuleEffect.DamageModifier(1))
            }
        }
        val engine = RuleEngine(listOf(handler, RecoveryRuleHandler, DamageModifierRuleHandler), random)
        assertFailsWith<IllegalStateException> {
            engine.execute(
                listOf(RuleDefinition("randomDamage", RuleEvent.DAMAGE_CALCULATION, handler.id)),
                context.copy(event = RuleEvent.DAMAGE_CALCULATION, baseDamage = 5),
            )
        }
        assertEquals(0, draws)
        assertFailsWith<IllegalArgumentException> {
            engine.validate(listOf(RuleDefinition(
                "invalidRecovery", RuleEvent.DAMAGE_CALCULATION, RecoveryRuleHandler.id,
                parameters = mapOf("hpAmount" to "1"),
            )))
        }
        assertFailsWith<IllegalArgumentException> {
            engine.validate(listOf(RuleDefinition(
                "invalidDamage", RuleEvent.TURN_START, DamageModifierRuleHandler.id,
                parameters = mapOf("amount" to "1"),
            )))
        }
    }
}
