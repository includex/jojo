package com.jojo.game

/** Owns the AI environment and keeps AI preview/turn operations on one boundary. */
internal class BattleAiFacade(private val battle: Battle) {
    private val environment by lazy { BattleAiEnvironmentAssembler.build(battle) }

    fun resolveTurn(maxUnits: Int, deferMutations: Boolean): AiTurnResult =
        BattleAiCoordinator.resolveAiTurn(maxUnits, deferMutations, environment)

    fun tracePlanner(characterId: Int, aiFlags: Int): AiPlannerTrace? =
        BattleAiCoordinator.traceAiPlannerAtCurrentPoint(characterId, aiFlags, environment)

    fun previewAttackValue(attackerId: String, targetId: String): Int =
        BattleAiCoordinator.previewAiAttackValue(attackerId, targetId, environment)
}
