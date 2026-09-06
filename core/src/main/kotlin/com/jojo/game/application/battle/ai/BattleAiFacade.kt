// Battle
package com.jojo.game.application.battle.ai

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*

/** BattleAiFacade: 전투 Ai 진입점이며, 관련 전투 기능을 묶어 안정적인 호출 경로를 제공한다. */
class BattleAiFacade internal constructor(private val battle: Battle) {
    /**
     * `environment` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val environment by lazy { BattleAiEnvironmentAssembler.build(battle) }

    /**
     * `resolveTurn`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun resolveTurn(maxUnits: Int = Int.MAX_VALUE, deferMutations: Boolean = false): AiTurnResult =
        BattleAiCoordinator.resolveAiTurn(maxUnits, deferMutations, environment)

    /**
     * `tracePlanner`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun tracePlanner(characterId: Int, aiFlags: Int = 1): AiPlannerTrace? =
        BattleAiCoordinator.traceAiPlannerAtCurrentPoint(characterId, aiFlags, environment)

    /**
     * `previewAttackValue`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun previewAttackValue(attackerId: String, targetId: String): Int =
        BattleAiCoordinator.previewAiAttackValue(attackerId, targetId, environment)
}
