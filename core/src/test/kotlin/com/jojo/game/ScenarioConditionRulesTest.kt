package com.jojo.game

import com.jojo.game.application.scenario.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScenarioConditionRulesTest {
    private fun environment(
        attributes: Map<Int, Map<Int, Int>> = emptyMap(),
        stageValue: Int = 0,
    ) = ScenarioConditionEnvironment(
        gvars = mutableMapOf(2 to 31),
        pvars = mutableMapOf(1 to ScenarioConditionOperandResolver.ADDRESS_INTVAR_START + 8, 2 to 7),
        battleContext = ScenarioBattleScriptContext(round = 1, camp = 0, attributes = attributes),
        stageUnitAttribute = { _, _ -> stageValue },
    )

    @Test
    fun `operand resolver preserves stage variable and integer-address rules`() {
        val environment = environment()
        val address = ScenarioConditionOperandResolver.ADDRESS_INTVAR_START + 8

        assertEquals(9, ScenarioConditionOperandResolver.value(0, 9, environment))
        assertEquals(31, ScenarioConditionOperandResolver.value(1, 1, environment))
        assertEquals(7, ScenarioConditionOperandResolver.value(2, 2, environment))
        assertEquals(31, ScenarioConditionOperandResolver.value(4, 2, environment))
        assertEquals(address, ScenarioConditionOperandResolver.value(5, 2, environment))

        ScenarioConditionOperandResolver.write(address, 44, environment)
        ScenarioConditionOperandResolver.write(123, 99, environment)
        assertEquals(44, ScenarioConditionOperandResolver.read(address, environment))
        assertEquals(0, ScenarioConditionOperandResolver.read(123, environment))
    }

    @Test
    fun `evaluator mutates through resolved address operands`() {
        val environment = environment()

        ScenarioConditionEvaluator.applyStageVarOperation(listOf(0, 1, 0, 2, 2), environment)

        assertEquals(38, environment.gvars[2])
        assertTrue(ScenarioConditionEvaluator.testStageVariables(listOf(1, 1, 0, 0, 38), environment))
    }

    @Test
    fun `unit condition rules prefer battle attributes and fall back to stage values`() {
        val battleValue = environment(attributes = mapOf(7 to mapOf(9 to 15)), stageValue = 4)
        val stageValue = environment(stageValue = 4)

        assertTrue(ScenarioUnitConditionRules.stateMatches(listOf(7, 9, 15, 2), battleValue))
        assertFalse(ScenarioUnitConditionRules.stateMatches(listOf(7, 9, 16, 0), battleValue))
        assertTrue(ScenarioConditionEvaluator.unitStateTest(listOf(8, 9, 4, 0), stageValue))
        assertTrue(ScenarioConditionEvaluator.unitStateTest(listOf(8, 9, 5, 1), stageValue))
    }
}
