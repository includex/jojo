package com.jojo.game.application.scenario

import com.jojo.game.intAt

/** Source-compatible unit-attribute comparison rules used by stage.unitStateTest. */
internal object ScenarioUnitConditionRules {
    fun stateMatches(args: List<Any?>, environment: ScenarioConditionEnvironment): Boolean {
        val unitId = args.intAt(0)
        val attribute = args.intAt(1)
        val compared = args.intAt(2)
        val mode = args.intAt(3)
        val value = environment.battleContext.attributes[unitId]?.get(attribute)
            ?: environment.stageUnitAttribute(unitId, attribute)
        return when (mode) {
            0 -> value >= compared
            1 -> value < compared
            2 -> value == compared
            3 -> value != compared
            else -> false
        }
    }
}
