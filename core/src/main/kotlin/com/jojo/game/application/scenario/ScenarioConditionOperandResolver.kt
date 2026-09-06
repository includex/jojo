// Scenario
package com.jojo.game.application.scenario


/** ScenarioConditionOperandResolver: 조건식에 인코딩된 값 종류를 실제 변수·상수·전장 수치로 해석한다. */
internal object ScenarioConditionOperandResolver {
    /**
     * `ADDRESS_INTVAR_START` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val ADDRESS_INTVAR_START = 5_251_072
    /**
     * `ADDRESS_INTVAR_END` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val ADDRESS_INTVAR_END = 5_255_168

    /**
     * `value`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun value(kind: Int, value: Int, environment: ScenarioConditionEnvironment): Int = when (kind) {
        0 -> value
        1 -> read(environment.pvars[value].asInt(), environment)
        2 -> environment.pvars[value].asInt()
        4 -> environment.gvars[value].asInt()
        5 -> ADDRESS_INTVAR_START + 4 * value
        else -> 0
    }

    /**
     * `read`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun read(address: Int, environment: ScenarioConditionEnvironment): Int =
        if (address in ADDRESS_INTVAR_START until ADDRESS_INTVAR_END) {
            environment.gvars[(address - ADDRESS_INTVAR_START) / 4].asInt()
        } else 0

    /**
     * `write`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun write(address: Int, value: Int, environment: ScenarioConditionEnvironment) {
        if (address in ADDRESS_INTVAR_START until ADDRESS_INTVAR_END) {
            environment.gvars[(address - ADDRESS_INTVAR_START) / 4] = value
        }
    }
}
