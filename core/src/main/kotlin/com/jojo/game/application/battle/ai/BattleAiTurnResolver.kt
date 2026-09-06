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
import com.jojo.game.domain.battle.command.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.BattleAiScorer
import com.jojo.game.domain.battle.BattleAttributeCalculator

/**
 * `BattleAiTurnEnvironment` 클래스: ai 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

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

/** BattleAiTurnResolver: 전투 Ai 턴 판별기이며, 입력 조건과 전투 규칙을 적용해 판정 결과를 계산한다. */
internal object BattleAiTurnResolver {

    /**
     * `distance`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun distance(a: BattleUnit, b: BattleUnit): Int =
        kotlin.math.abs(a.tileX - b.tileX) + kotlin.math.abs(a.tileY - b.tileY)

    /**
     * `resolveAiTurn`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun resolveAiTurn(
        maxUnits: Int = Int.MAX_VALUE,
        deferMutations: Boolean = false,
        env: BattleAiTurnEnvironment,
    ): AiTurnResult {
        require(maxUnits > 0)
        if (env.outcome() != null) return AiTurnResult(0, 0, 0)
        check(!deferMutations || maxUnits == 1) { "deferred AI playback resolves exactly one _ai2 actor" }
        check(!deferMutations || env.pendingActionTransaction() == null) { "previous deferred AI actor has not completed" }
        /**
         * `beforeResolution` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val beforeResolution = if (deferMutations) env.runtimeSnapshot() else null
        if (deferMutations) {
            env.setStagedHitSideEffects(mutableListOf())
            env.setStagedCompletionSideEffects(mutableListOf())
        }
        env.setLastAiUnitResolution(null)
        /**
         * `moves` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var moves = 0
        /**
         * `attacks` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var attacks = 0
        /**
         * `holds` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var holds = 0
        /**
         * `resolvedUnits` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var resolvedUnits = 0
        /**
         * `currentActor` (BattleUnit?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var currentActor: BattleUnit? = null
        /**
         * `currentFromX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var currentFromX = 0
        /**
         * `currentFromY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var currentFromY = 0
        /**
         * `currentHealthBefore` (Map<String, Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var currentHealthBefore: Map<String, Int> = emptyMap()
        /**
         * `currentMoveArea` (List<Pair<Int, Int>>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var currentMoveArea: List<Pair<Int, Int>> = emptyList()


        /**
         * `record`: 타입의 핵심 동작을 수행한다.
         * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun record(
            unit: BattleUnit,
            targetId: String? = null,
            magicId: Int? = null,
            result: TacticalActionResult? = null
        ) {
            /**
             * `actionArea` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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


        /**
         * `hold`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun hold(unit: BattleUnit) {
            unit.markActionComplete()
            holds++
            check(currentActor === unit)
            record(unit)
        }

        /**
         * `firstPlannedId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var firstPlannedId = env.aiTurnOrder()?.firstOrNull()
        env.clearAiTurnOrder()
        /**
         * `tracedAiSort` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var tracedAiSort = false

        while (resolvedUnits < maxUnits) {
            if (env.outcome() != null) break
            /**
             * `remaining` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val remaining = env.units().values.asSequence()
                .filter { it.visible && it.effectiveFaction() == env.activeFaction() && !it.hasActed }
                .sortedWith(compareByDescending<BattleUnit> {
                    BattleAiScorer.aiSortValue(it, env.terrain, env.terrainResumeRates)
                }.thenBy { BattleAttributeCalculator.effective(it, BattleAttribute.DEFENSE) })
                .toList()

            /**
             * `round` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val round = env.round()
            /**
             * `activeFaction` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
            /**
             * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
            /**
             * `retainedTarget` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
            /**
             * `opponents` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val opponents = env.units().values.filter { it.visible && !env.areAllied(it, unit) }
            /**
             * `targetById` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val targetById = retainedTarget?.takeIf { !env.areAllied(it, unit) }

            /**
             * `controllerResult` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val controllerResult = BattleAiControllerRunner.run(unit, opponents, targetById, env.controllerEnv)
            currentMoveArea = controllerResult.sourcePoints.map { it.x to it.y }

            if (controllerResult.status != 0 || controllerResult.decision == null) {
                hold(unit)
                continue
            }
            /**
             * `decision` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val decision = controllerResult.decision
            if (controllerResult.activeAi in setOf(ControlAi.ACTIVE, ControlAi.HOLD)) {
                unit.aiValue = decision.actionValue
            }
            /**
             * `traceFrom` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val traceFrom = "${unit.tileX},${unit.tileY}"
            /**
             * `diagnosticPoints` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
            /**
             * `selected` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val selected = decision.targetId?.let(env.units()::get)
            if (selected != null && decision.magicId != null) {
                /**
                 * `profile` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val profile = unit.magic.firstOrNull { it.id == decision.magicId }
                /**
                 * `bypassCondition` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val bypassCondition = profile?.aiUse == 13
                /**
                 * `magicResult` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val magicResult = env.castMagic(unit.id, selected.id, decision.magicId, false, bypassCondition)
                if (unit.characterId == 146 && round == 2) {
                    /**
                     * `profileText` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val profileText =
                        profile?.let { "id=${it.id},type=${it.type},target=${it.target},area=${it.effectAreaId},power=${it.power},harm=${it.harmType},category=${it.category},limit=${it.hitRateLimit}" }
                    env.traceActions += "diagMagic146:profile=$profileText:targetArm=${selected.armId},magicHarm=${selected.magicHarmRate}:result=$magicResult"
                }
                if (magicResult is TacticalActionResult.Magic) {
                    attacks++
                    record(unit, selected.id, decision.magicId, magicResult)
                } else hold(unit)
            } else if (selected != null && selected.visible && BattleAiScorer.canAttack(unit, selected)) {
                /**
                 * `attackResult` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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

        /**
         * `lastResolution` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val lastResolution = env.lastAiUnitResolution()
        if (deferMutations && lastResolution != null) {
            /**
             * `afterResolution` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val afterResolution = env.runtimeSnapshot()
            /**
             * `before` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val before = requireNotNull(beforeResolution)
            /**
             * `hitSideEffects` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val hitSideEffects = env.stagedHitSideEffects().orEmpty().toList()
            /**
             * `completionSideEffects` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
