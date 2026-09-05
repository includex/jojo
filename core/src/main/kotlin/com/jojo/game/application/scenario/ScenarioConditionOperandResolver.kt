package com.jojo.game.application.scenario

import com.jojo.game.asInt

/**
 * Resolves the condition API's immediate, variable, and emulated-address
 * operands. This is deliberately state-free; the environment owns the maps
 * that model the source stage's pvars and gvars memory.
 */
internal object ScenarioConditionOperandResolver {
    const val ADDRESS_INTVAR_START = 5_251_072
    const val ADDRESS_INTVAR_END = 5_255_168

    fun value(kind: Int, value: Int, environment: ScenarioConditionEnvironment): Int = when (kind) {
        0 -> value
        1 -> read(environment.pvars[value].asInt(), environment)
        2 -> environment.pvars[value].asInt()
        4 -> environment.gvars[value].asInt()
        5 -> ADDRESS_INTVAR_START + 4 * value
        else -> 0
    }

    fun read(address: Int, environment: ScenarioConditionEnvironment): Int =
        if (address in ADDRESS_INTVAR_START until ADDRESS_INTVAR_END) {
            environment.gvars[(address - ADDRESS_INTVAR_START) / 4].asInt()
        } else 0

    fun write(address: Int, value: Int, environment: ScenarioConditionEnvironment) {
        if (address in ADDRESS_INTVAR_START until ADDRESS_INTVAR_END) {
            environment.gvars[(address - ADDRESS_INTVAR_START) / 4] = value
        }
    }
}
