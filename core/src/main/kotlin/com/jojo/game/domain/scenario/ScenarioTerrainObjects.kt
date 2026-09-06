// Scenario
package com.jojo.game.domain.scenario

/** ScenarioMapObject: 시나리오 지도에 배치되는 오브젝트의 좌표·지형·표시 상태를 나타낸다. */
data class ScenarioMapObject(
    val x: Int,
    val y: Int,
    val objectId: Int,
    val terrainId: Int,
    val enabled: Boolean,
)

/** ScenarioFire: 지도 위 불꽃 효과의 좌표와 표시 여부를 나타내는 시나리오 상태다. */
data class ScenarioFire(
    val x: Int,
    val y: Int,
    val enabled: Boolean,
)
