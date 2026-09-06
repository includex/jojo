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
    private val environment by lazy { BattleAiEnvironmentAssembler.build(battle) }

    fun resolveTurn(maxUnits: Int = Int.MAX_VALUE, deferMutations: Boolean = false): AiTurnResult =
        BattleAiCoordinator.resolveAiTurn(maxUnits, deferMutations, environment)

    fun tracePlanner(characterId: Int, aiFlags: Int = 1): AiPlannerTrace? =
        BattleAiCoordinator.traceAiPlannerAtCurrentPoint(characterId, aiFlags, environment)

    fun previewAttackValue(attackerId: String, targetId: String): Int =
        BattleAiCoordinator.previewAiAttackValue(attackerId, targetId, environment)
}
