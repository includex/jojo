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
import com.jojo.game.domain.battle.command.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.BattleAiScorer
import com.jojo.game.domain.battle.BattleAttributeCalculator

internal data class BattleAiTurnEnvironment(
    val outcome: () -> BattleOutcome?,
    val activeFaction: () -> Faction,
    val round: () -> Int,
    val units: () -> Map<String, BattleUnit>,
    val terrain: BattleTerrainGrid?,
    val terrainResumeRates: Map<Int, Int>,
    val areAllied: (BattleUnit, BattleUnit) -> Boolean,
    val hasAttackCandidate: (BattleUnit, BattleUnit) -> Boolean,
    val moveUnit: (unitId: String, targetX: Int, targetY: Int) -> TacticalActionResult,
    val attack: (attackerId: String, targetId: String) -> TacticalActionResult,
    val castMagic: (attackerId: String, targetId: String, magicId: Int, reaction: Boolean, bypassCondition: Boolean) -> TacticalActionResult,
    val lastMovePath: (String) -> List<Pair<Int, Int>>,
    val traceActions: MutableList<String>,
    val aiTurnOrder: () -> List<String>?,
    val clearAiTurnOrder: () -> Unit,
    val setLastAiUnitResolution: (AiUnitResolution?) -> Unit,
    val lastAiUnitResolution: () -> AiUnitResolution?,
    val runtimeSnapshot: () -> BattleActionSnapshot,
    val restoreRuntime: (BattleActionSnapshot) -> Unit,
    val setPendingActionTransaction: (BattleActionTransaction?) -> Unit,
    val pendingActionTransaction: () -> BattleActionTransaction?,
    val stagedHitSideEffects: () -> MutableList<() -> Unit>?,
    val setStagedHitSideEffects: (MutableList<() -> Unit>?) -> Unit,
    val stagedCompletionSideEffects: () -> MutableList<() -> Unit>?,
    val setStagedCompletionSideEffects: (MutableList<() -> Unit>?) -> Unit,
    val createActionTransaction: (actorId: String, before: BattleActionSnapshot, after: BattleActionSnapshot, hitSideEffects: List<() -> Unit>, completionSideEffects: List<() -> Unit>) -> BattleActionTransaction,
    val controllerEnv: BattleAiControllerEnvironment,
)

/**
 * Resolves AI turn execution for a camp in tactical combat, managing unit ordering,
 * controller dispatch, side-effect staging, and deferred mutation transactions.
 */
internal object BattleAiTurnResolver {

    private fun distance(a: BattleUnit, b: BattleUnit): Int =
        kotlin.math.abs(a.tileX - b.tileX) + kotlin.math.abs(a.tileY - b.tileY)

    fun resolveAiTurn(
        maxUnits: Int = Int.MAX_VALUE,
        deferMutations: Boolean = false,
        env: BattleAiTurnEnvironment,
    ): AiTurnResult {
        require(maxUnits > 0)
        if (env.outcome() != null) return AiTurnResult(0, 0, 0)
        check(!deferMutations || maxUnits == 1) { "deferred AI playback resolves exactly one _ai2 actor" }
        check(!deferMutations || env.pendingActionTransaction() == null) { "previous deferred AI actor has not completed" }
        val beforeResolution = if (deferMutations) env.runtimeSnapshot() else null
        if (deferMutations) {
            env.setStagedHitSideEffects(mutableListOf())
            env.setStagedCompletionSideEffects(mutableListOf())
        }
        env.setLastAiUnitResolution(null)
        var moves = 0
        var attacks = 0
        var holds = 0
        var resolvedUnits = 0
        var currentActor: BattleUnit? = null
        var currentFromX = 0
        var currentFromY = 0
        var currentHealthBefore: Map<String, Int> = emptyMap()
        var currentMoveArea: List<Pair<Int, Int>> = emptyList()


        fun record(
            unit: BattleUnit,
            targetId: String? = null,
            magicId: Int? = null,
            result: TacticalActionResult? = null
        ) {
            val actionArea = when (result) {
                is TacticalActionResult.Attack -> if (unit.attackAllScreen) {
                    env.terrain?.let { grid -> (0 until grid.width).flatMap { x -> (0 until grid.height).map { y -> x to y } } }
                        .orEmpty()
                } else (unit.attackOffsets + unit.attackEffectOffsets).map { (dx, dy) -> unit.tileX + dx to unit.tileY + dy }

                is TacticalActionResult.Magic -> unit.magic.firstOrNull { it.id == magicId }
                    ?.hitArea?.offsets?.map { (dx, dy) -> unit.tileX + dx to unit.tileY + dy }.orEmpty()

                else -> emptyList()
            }
            env.setLastAiUnitResolution(
                AiUnitResolution(
                    actorId = unit.id,
                    fromX = currentFromX,
                    fromY = currentFromY,
                    toX = unit.tileX,
                    toY = unit.tileY,
                    path = env.lastMovePath(unit.id).takeIf { unit.tileX != currentFromX || unit.tileY != currentFromY }
                        .orEmpty(),
                    targetId = targetId,
                    magicId = magicId,
                    result = result,
                    healthBeforeAction = currentHealthBefore,
                    moveArea = currentMoveArea,
                    actionArea = actionArea,
                )
            )
            resolvedUnits++
        }


        fun hold(unit: BattleUnit) {
            unit.markActionComplete()
            holds++
            check(currentActor === unit)
            record(unit)
        }

        var firstPlannedId = env.aiTurnOrder()?.firstOrNull()
        env.clearAiTurnOrder()
        var tracedAiSort = false

        while (resolvedUnits < maxUnits) {
            if (env.outcome() != null) break
            val remaining = env.units().values.asSequence()
                .filter { it.visible && it.effectiveFaction() == env.activeFaction() && !it.hasActed }
                .sortedWith(compareByDescending<BattleUnit> {
                    BattleAiScorer.aiSortValue(it, env.terrain, env.terrainResumeRates)
                }.thenBy { BattleAttributeCalculator.effective(it, BattleAttribute.DEFENSE) })
                .toList()

            val round = env.round()
            val activeFaction = env.activeFaction()
            if (!tracedAiSort && round == 2 && activeFaction == Faction.ENEMY) {
                env.traceActions += "sort-r2-enemy:" + remaining.joinToString(";") {
                    "${it.characterId}=v${
                        BattleAiScorer.aiSortValue(
                            it,
                            env.terrain,
                            env.terrainResumeRates
                        )
                    },hp${it.hitPoints}/${it.maxHitPoints},arm${it.armType},remote${it.remoteAttack},mov${
                        BattleAttributeCalculator.effectiveMovement(
                            it
                        )
                    },def${
                        BattleAttributeCalculator.effective(
                            it,
                            BattleAttribute.DEFENSE
                        )
                    },terrain${
                        env.terrain?.terrainAt(
                            it.tileX,
                            it.tileY
                        )
                    },resume${
                        env.terrainResumeRates[env.terrain?.terrainAt(
                            it.tileX,
                            it.tileY
                        )] ?: 0
                    },status${it.statuses}"
                }
                tracedAiSort = true
            }
            val unit = firstPlannedId?.let(env.units()::get)
                ?.takeIf { it.visible && it.effectiveFaction() == activeFaction && !it.hasActed }
                ?: remaining.firstOrNull()
                ?: break
            firstPlannedId = null
            currentActor = unit
            currentFromX = unit.tileX
            currentFromY = unit.tileY
            currentHealthBefore = env.units().mapValues { it.value.hitPoints }
            currentMoveArea = emptyList()

            if (BattleStatus.CONFUSION in unit.statuses) {
                hold(unit)
                continue
            }
            val retainedTarget = env.units().values.firstOrNull {
                it.visible && it.characterId == unit.aiTargetCharacterId
            }
            when (unit.ai) {
                3 -> when {
                    retainedTarget == null -> unit.ai = 1
                    env.areAllied(unit, retainedTarget) && distance(unit, retainedTarget) < 3 -> unit.ai = 0
                    !env.areAllied(unit, retainedTarget) && !env.hasAttackCandidate(unit, retainedTarget) -> {
                        unit.ai = 9
                        unit.aiTargetX = retainedTarget.tileX
                        unit.aiTargetY = retainedTarget.tileY
                    }
                }

                5 -> when {
                    retainedTarget == null -> unit.ai = 1
                    distance(unit, retainedTarget) < 3 -> unit.ai = 0
                    else -> {
                        unit.ai = 7
                        unit.aiTargetX = retainedTarget.tileX
                        unit.aiTargetY = retainedTarget.tileY
                    }
                }
            }
            val opponents = env.units().values.filter { it.visible && !env.areAllied(it, unit) }
            val targetById = retainedTarget?.takeIf { !env.areAllied(it, unit) }

            val controllerResult = BattleAiControllerRunner.run(unit, opponents, targetById, env.controllerEnv)
            currentMoveArea = controllerResult.sourcePoints.map { it.x to it.y }

            if (controllerResult.status != 0 || controllerResult.decision == null) {
                hold(unit)
                continue
            }
            val decision = controllerResult.decision
            if (controllerResult.activeAi in setOf(ControlAi.ACTIVE, ControlAi.HOLD)) {
                unit.aiValue = decision.actionValue
            }
            val traceFrom = "${unit.tileX},${unit.tileY}"
            val diagnosticPoints =
                if (unit.characterId == 474 && round == 1) controllerResult.sourcePoints.joinToString(";") { "${it.x},${it.y}" } else ""
            env.traceActions += "r$round/${activeFaction.name}/${unit.characterId}:$traceFrom->${decision.x},${decision.y}:target=${
                decision.targetId?.let(
                    env.units()::get
                )?.characterId
            }:magic=${decision.magicId}:score=${decision.actionValue}:points=$diagnosticPoints"

            if (decision.x != unit.tileX || decision.y != unit.tileY) {
                if (env.moveUnit(unit.id, decision.x, decision.y) is TacticalActionResult.Success) moves++ else {
                    hold(unit)
                    continue
                }
            }
            val selected = decision.targetId?.let(env.units()::get)
            if (selected != null && decision.magicId != null) {
                val profile = unit.magic.firstOrNull { it.id == decision.magicId }
                val bypassCondition = profile?.aiUse == 13
                val magicResult = env.castMagic(unit.id, selected.id, decision.magicId, false, bypassCondition)
                if (unit.characterId == 146 && round == 2) {
                    val profileText =
                        profile?.let { "id=${it.id},type=${it.type},target=${it.target},area=${it.effectAreaId},power=${it.power},harm=${it.harmType},category=${it.category},limit=${it.hitRateLimit}" }
                    env.traceActions += "diagMagic146:profile=$profileText:targetArm=${selected.armId},magicHarm=${selected.magicHarmRate}:result=$magicResult"
                }
                if (magicResult is TacticalActionResult.Magic) {
                    attacks++
                    record(unit, selected.id, decision.magicId, magicResult)
                } else hold(unit)
            } else if (selected != null && selected.visible && BattleAiScorer.canAttack(unit, selected)) {
                val attackResult = env.attack(unit.id, selected.id)
                if ((unit.characterId in setOf(0, 32, 258, 259, 477, 479) && round == 3) ||
                    (unit.characterId == 3 && round == 4)
                ) env.traceActions += "diagAttack${unit.characterId}r$round:offsets=${unit.attackOffsets}:statuses=${unit.statuses}:result=$attackResult"
                if (attackResult is TacticalActionResult.Attack) {
                    attacks++
                    record(unit, selected.id, result = attackResult)
                } else hold(unit)
            } else hold(unit)
        }

        val lastResolution = env.lastAiUnitResolution()
        if (deferMutations && lastResolution != null) {
            val afterResolution = env.runtimeSnapshot()
            val before = requireNotNull(beforeResolution)
            val hitSideEffects = env.stagedHitSideEffects().orEmpty().toList()
            val completionSideEffects = env.stagedCompletionSideEffects().orEmpty().toList()
            env.setStagedHitSideEffects(null)
            env.setStagedCompletionSideEffects(null)
            env.restoreRuntime(before)
            env.setPendingActionTransaction(
                env.createActionTransaction(
                    lastResolution.actorId, before, afterResolution, hitSideEffects, completionSideEffects,
                )
            )
        } else if (deferMutations) {
            env.setStagedHitSideEffects(null)
            env.setStagedCompletionSideEffects(null)
        }
        return AiTurnResult(moves, attacks, holds)
    }
}
