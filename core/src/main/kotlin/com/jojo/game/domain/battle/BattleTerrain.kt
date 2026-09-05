package com.jojo.game.domain.battle

import com.jojo.game.ScenarioFire
import com.jojo.game.ScenarioMapObject


/** Original Hexzmap terrain grid used by BattleScreen.getBattleTerrain(x, y). */
data class BattleTerrainGrid(
    val width: Int,
    val height: Int,
    val rows: List<IntArray>,
) {
    /** Runtime terrain replacements made through BattleScreen.setObject2. */
    private val overlays = mutableMapOf<Pair<Int, Int>, Int>()

    /**
     * 공개 메서드 `terrainAt`
     *
     * ### 파라미터
    - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun terrainAt(x: Int, y: Int): Int = overlays[x to y] ?: rows.getOrNull(y)?.getOrNull(x) ?: -1

    /**
     * 공개 메서드 `resetOverlays`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun resetOverlays() = overlays.clear()

    /**
     * 공개 메서드 `applyObjectOverlays`
     *
     * ### 파라미터
    - `objects` (`Collection<ScenarioMapObject>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun applyObjectOverlays(objects: Collection<ScenarioMapObject>) {
        objects.forEach { objectState ->
            val point = objectState.x to objectState.y
            // setObject2 only replaces terrain for its non-gate (0..3)
            // object types. Gates retain their own terrain data but are
            // represented separately as blocked tiles in the tactical state.
            if (objectState.objectId in 0..3) {
                if (objectState.enabled) overlays[point] = objectState.terrainId else overlays.remove(point)
            }
        }
    }

    /** BattleScreen.setFire/setFires calls setObject2 with TERRAIN.HUO (26). */
    fun applyFires(fires: Collection<ScenarioFire>) {
        fires.filter { it.enabled }.forEach { fire -> overlays[fire.x to fire.y] = FIRE_TERRAIN_ID }
    }

    companion object {
        const val FIRE_TERRAIN_ID = 26

    }
}
