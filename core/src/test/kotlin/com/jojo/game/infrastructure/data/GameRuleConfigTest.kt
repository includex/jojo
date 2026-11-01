package com.jojo.game.infrastructure.data

import com.jojo.game.domain.rules.*
import kotlin.test.*

class GameRuleConfigTest {
    @Test fun `unknown handlers fail at engine creation`() {
        val config=GameRuleConfig(emptyList(),listOf(RuleDefinition("game.unknown",RuleEvent.TURN_START,"missing.handler")))
        assertFailsWith<IllegalArgumentException> { config.createEngine(RuleRandom { 0 }) }
    }

    @Test fun `game module registers a new handler through public contract`() {
        val handler=object: RuleHandler {
            override val id="game.specialRecovery"
            override fun validate(parameters:Map<String,String>) { require(parameters.isEmpty()) }
            override fun evaluate(context:RuleContext,parameters:Map<String,String>,random:RuleRandom)=
                listOf(RuleEffect.HpDelta(context.actorId,7))
        }
        val module=object:RuleModule { override val handlers=listOf(handler) }
        val config=GameRuleConfig(emptyList(),listOf(RuleDefinition("game.heal",RuleEvent.TURN_START,handler.id)))
        val engine=config.createEngine(RuleRandom { 0 },listOf(module))
        val context=RuleContext(RuleEvent.TURN_START,"u",mapOf("u" to RuleUnitSnapshot("u",10,20,0,0)))
        assertEquals(17,engine.execute(config.rules,context).apply(context).units.getValue("u").hitPoints)
        assertFailsWith<IllegalArgumentException> { config.createEngine(RuleRandom { 0 },listOf(module,module)) }
    }
}
