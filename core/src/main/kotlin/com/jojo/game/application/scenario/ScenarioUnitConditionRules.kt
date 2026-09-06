// Scenario
package com.jojo.game.application.scenario


/** ScenarioUnitConditionRules: 스크립트 선택자와 유닛 상태를 대조해 조건식의 참·거짓을 판정한다. */
internal object ScenarioUnitConditionRules {
    /**
     * `stateMatches`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
