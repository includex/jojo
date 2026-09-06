// Battle
package com.jojo.game.application.battle.movement

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleMovementPlanner
/**
 * `BattleMovementEnvironmentAssembler` 싱글턴 객체: movement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object BattleMovementEnvironmentAssembler {
    /**
     * `build`: 필요한 객체나 결과를 생성한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun build(
        units: () -> Map<String, BattleUnit>,
        unitAt: (Int, Int) -> BattleUnit?,
        activeFaction: () -> Faction,
        weather: () -> BattleWeather,
        terrain: BattleTerrainGrid?,
        blockedTiles: MutableSet<Pair<Int, Int>>,
        movementPlanner: BattleMovementPlanner<BattleUnit>,
        allPresentationUnits: () -> List<BattleUnit>,
        isBattleEnded: () -> Boolean,
        recordMove: (String, List<Pair<Int, Int>>, Int) -> Unit,
    ): BattleMovementEnvironment = BattleMovementEnvironment(
        units = units,
        unitAt = unitAt,
        activeFaction = activeFaction,
        weather = weather,
        terrain = terrain,
        blockedTiles = blockedTiles,
        movementPlanner = movementPlanner,
        allPresentationUnits = allPresentationUnits,
        isBattleEnded = isBattleEnded,
        onMoveExecuted = recordMove,
    )
}
