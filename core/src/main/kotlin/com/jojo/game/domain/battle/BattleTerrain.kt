// Battle
package com.jojo.game.domain.battle

import com.jojo.game.domain.scenario.ScenarioFire
import com.jojo.game.domain.scenario.ScenarioMapObject


/** BattleTerrainGrid: 전투 맵의 기본 지형과 동적 덮어쓰기 지형을 보관하며, 좌표별 지형을 조회한다. */
data class BattleTerrainGrid(
    val width: Int,
    val height: Int,
    val rows: List<IntArray>,
) {
    /** overlays: 화재와 객체 변화로 기본 지형을 대체한 타일의 지형 식별자이다. */
    private val overlays = mutableMapOf<Pair<Int, Int>, Int>()


    /**
     * `terrainAt`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun terrainAt(x: Int, y: Int): Int = overlays[x to y] ?: rows.getOrNull(y)?.getOrNull(x) ?: -1


    /**
     * `resetOverlays`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun resetOverlays() = overlays.clear()


    /**
     * `applyObjectOverlays`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun applyObjectOverlays(objects: Collection<ScenarioMapObject>) {
        objects.forEach { objectState ->
            val point = objectState.x to objectState.y
            // 원본 객체 지형 갱신은 문이 아닌 객체 유형(0..3)의 지형만 바꾼다. 문은 자체 지형을
            // 유지하고, 전술 상태에서는 별도의 차단 타일로 표현한다.
            if (objectState.objectId in 0..3) {
                if (objectState.enabled) overlays[point] = objectState.terrainId else overlays.remove(point)
            }
        }
    }

    /** applyFires: 활성 화재 좌표를 화염 지형으로 반영해 이동·피해 계산에 사용한다. */
    fun applyFires(fires: Collection<ScenarioFire>) {
        fires.filter { it.enabled }.forEach { fire -> overlays[fire.x to fire.y] = FIRE_TERRAIN_ID }
    }

    companion object {
        /**
         * `FIRE_TERRAIN_ID` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val FIRE_TERRAIN_ID = 26

    }
}
