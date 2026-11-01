package com.jojo.game.infrastructure.rules

import com.jojo.game.domain.rules.*
import kotlin.test.*

class LuaRuleHandlerTest {
    private val context = RuleContext(RuleEvent.TURN_START, "a", mapOf("a" to RuleUnitSnapshot("a", 10, 20, 4, 8)))
    private val random = RuleRandom { 2 }
    private fun handler(body: String) = LuaRuleHandler("test.lua", body, "mods/recovery.lua")

    @Test fun evaluatesParametersAndCopiesSnapshots() {
        val rule = handler("""return {evaluate=function(ctx,p)
            local amount=tonumber(p.amount)+ctx.actor.maxHitPoints-ctx.actor.hitPoints
            ctx.actor.hitPoints=0
            return {{type='HpDelta',unitId=ctx.owner.id,amount=amount}}
        end}""")
        assertEquals(listOf(RuleEffect.HpDelta("a", 13)), rule.evaluate(context, mapOf("amount" to "3"), random))
        assertEquals(10, context.units.getValue("a").hitPoints)
    }

    @Test fun resetsModuleAndGlobalsForEveryEvaluation() {
        val rule = handler("""local n=0; return {evaluate=function(ctx,p)
            n=n+1; globalCount=(globalCount or 0)+1
            return {{type='HpDelta',unitId=ctx.actorId,amount=n+globalCount}}
        end}""")
        repeat(3) { assertEquals(listOf(RuleEffect.HpDelta("a", 2)), rule.evaluate(context, emptyMap(), random)) }
    }

    @Test fun capabilityWhitelistCannotReachHostOrBypassBudget() {
        handler("""assert(os==nil and io==nil and package==nil and require==nil and luajava==nil)
            assert(debug==nil and coroutine==nil and pcall==nil and xpcall==nil)
            assert(load==nil and loadfile==nil and dofile==nil and setmetatable==nil)
            assert(math.random==nil and string==nil)
            return {evaluate=function() return {} end}""")
    }

    @Test fun initializationAndCallbackHaveInstructionBudgets() {
        val initialization = assertFailsWith<IllegalArgumentException> { handler("while true do end") }
        assertTrue(initialization.message!!.contains("mods/recovery.lua [test.lua]"))
        assertTrue(initialization.message!!.contains("instruction budget"))
        val rule = handler("return {evaluate=function() while true do end end}")
        assertFailsWith<IllegalArgumentException> { rule.evaluate(context, emptyMap(), random) }
    }

    @Test fun sharesInstructionBudgetBetweenInitializationAndEvaluation() {
        // Each loop fits by itself; their combined bytecodes exceed the invocation budget.
        val rule = handler("local n=0; for i=1,14000 do n=n+1 end; return {evaluate=function() for i=1,14000 do n=n+1 end; return {} end}")
        assertTrue(assertFailsWith<IllegalArgumentException> { rule.evaluate(context, emptyMap(), random) }.message!!.contains("instruction budget"))
    }

    @Test fun exposesCombatOwnerEquipmentAndPhase() {
        val rule = handler("""return {evaluate=function(ctx)
            assert(ctx.phase=='POWER' and ctx.damageKind=='physical')
            assert(ctx.actor.id=='a' and ctx.target.id=='b' and ctx.owner.id=='b')
            assert(ctx.sourceId=='equipment:12' and ctx.owner.characterId==2)
            assert(ctx.owner.equipmentIds[1]==12 and ctx.owner.defense==5)
            return {{type='AttackPowerModifier',amount=math.floor(ctx.attackPower*0.5)}}
        end}""")
        val combat = context.copy(event=RuleEvent.HIT, targetId="b", ownerId="b", sourceId="equipment:12",
            combatPhase=RuleCombatPhase.POWER, attackPower=11, damageKind="physical",
            units=context.units + ("b" to RuleUnitSnapshot("b", 10, 20, 4, 8, characterId=2, equipmentIds=listOf(12), defense=5)))
        assertEquals(listOf(RuleEffect.AttackPowerModifier(5)), rule.evaluate(combat, emptyMap(), random))
    }

    @Test fun rejectsRecursionAndExponentialStringGrowth() {
        val recursive = handler("local function f() return 1+f() end; return {evaluate=f}")
        assertFailsWith<IllegalArgumentException> { recursive.evaluate(context, emptyMap(), random) }
        val strings = handler("return {evaluate=function() local x='a'; for i=1,100 do x=x..x end; return {} end}")
        assertTrue(assertFailsWith<IllegalArgumentException> { strings.evaluate(context, emptyMap(), random) }.message!!.contains("string budget"))
    }

    @Test fun rejectsInvalidEffectsAndModuleContracts() {
        assertFailsWith<IllegalArgumentException> { handler("return {id='wrong',evaluate=function() end}") }
        for (result in listOf("{{type='wat'}}", "{{type='HpDelta',unitId='a',amount=1.5}}", "{[2]={type='HpDelta',unitId='a',amount=1}}", "{{type='DamageModifier',amount=0/0}}")) {
            val rule = handler("return {evaluate=function() return $result end}")
            assertFailsWith<IllegalArgumentException> { rule.evaluate(context, emptyMap(), random) }
        }
        val rule = handler("return {evaluate=function() local r={} for i=1,129 do r[i]={type='DamageModifier',amount=1} end return r end}")
        assertFailsWith<IllegalArgumentException> { rule.evaluate(context, emptyMap(), random) }
    }

    @Test fun injectedRandomUsesEnginePolicy() {
        val rule = handler("return {evaluate=function(ctx) return {{type='HpDelta',unitId=ctx.actorId,amount=ctx.random(5)}} end}")
        assertEquals(listOf(RuleEffect.HpDelta("a", 2)), rule.evaluate(context, emptyMap(), random))
        assertFailsWith<IllegalArgumentException> { rule.evaluate(context, emptyMap(), RuleRandom { error("forbidden") }) }
    }
}
