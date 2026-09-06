// Battle
package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.combat.*
import com.jojo.game.domain.battle.command.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.magic.BattleMagicProfile
import com.jojo.game.domain.battle.magic.MagicDamageCalculator
import com.jojo.game.domain.battle.BattleAiScorer

/**
 * `BattleAiDecisionEnvironment` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class BattleAiDecisionEnvironment(
    val scoringEnv: BattleAiScoringEnvironment,
    val reachableTiles: (String) -> Map<Pair<Int, Int>, Int>,
    val terrainResumeRates: Map<Int, Int>,
    val weather: () -> BattleWeather,
    val round: () -> Int,
    val onRecordDiagnostic: (String) -> Unit = {},
    val hasDiagnosticEntry: (prefix: String) -> Boolean = { false },
)

/**
 * `AiDecision` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class AiDecision(
    val x: Int,
    val y: Int,
    val targetId: String?,
    val magicId: Int?,
    val value: Int,
    val actionValue: Int = 0,
)

/** BattleAiDecisionPlanner: AI 유닛의 이동·공격 후보를 계산하고, 현재 전장에 가장 알맞은 행동을 선택한다. */
internal object BattleAiDecisionPlanner {

    /**
     * `chooseAiDecision`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun chooseAiDecision(
        unit: BattleUnit,
        opponents: List<BattleUnit>,
        designated: BattleUnit?,
        aiMode: Int = unit.ai,
        aiFlags: Int = 0,
        candidatePoints: Collection<Pair<Int, Int>>? = null,
        env: BattleAiDecisionEnvironment,
    ): AiDecision? {
        /**
         * `reachable` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val reachable = env.reachableTiles(unit.id).keys
        /**
         * `points` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val points = candidatePoints?.toCollection(linkedSetOf()) ?: when (aiMode) {
            0 -> linkedSetOf(unit.tileX to unit.tileY).apply {
                /**
                 * `allPoints` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val allPoints = linkedSetOf(unit.tileX to unit.tileY).apply { addAll(reachable) }
                /**
                 * `hasAttackTarget` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val hasAttackTarget = allPoints.any { (x, y) ->
                    opponents.any { target -> BattleAiScorer.canAttackFrom(unit, x, y, target) }
                }
                if (hasAttackTarget) addAll(reachable)
            }

            2 -> linkedSetOf(unit.tileX to unit.tileY)
            4, 6 -> {
                /**
                 * `destination` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val destination = reachable.minByOrNull { (x, y) ->
                    kotlin.math.abs(x - unit.aiTargetX) + kotlin.math.abs(y - unit.aiTargetY)
                }
                destination?.let { linkedSetOf(it) } ?: linkedSetOf(unit.tileX to unit.tileY)
            }

            7, 8, 9 -> reachable.minByOrNull { (x, y) ->
                kotlin.math.abs(x - unit.aiTargetX) + kotlin.math.abs(y - unit.aiTargetY)
            }?.let { destination -> linkedSetOf(destination) } ?: linkedSetOf(unit.tileX to unit.tileY)

            else -> linkedSetOf(unit.tileX to unit.tileY).apply { addAll(reachable) }
        }
        /**
         * `best` (AiDecision?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var best: AiDecision? = null
        /**
         * `diagnosticScores` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val diagnosticScores = mutableListOf<String>()
        /**
         * `originalX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val originalX = unit.tileX
        /**
         * `originalY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val originalY = unit.tileY
        /**
         * `effectiveAiFlags` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val effectiveAiFlags = if (aiFlags and 2 != 0 && unit.skills[226]?.and(255)
                ?.let { it != 255 } == true
        ) aiFlags and 2.inv() else aiFlags
        points.forEach { (x, y) ->
            env.scoringEnv.unitAt(x, y)?.takeIf { it !== unit }?.let { return@forEach }
            unit.tileX = x
            unit.tileY = y
            /**
             * `value` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            var value = (unit.terrainImpacts[env.scoringEnv.terrain?.terrainAt(x, y)] ?: 100) / 5
            /**
             * `wounded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
            if (unit.armType == 1 || unit.remoteAttack || wounded) {
                env.scoringEnv.units().filter { it.visible && it != unit }.forEach { other ->
                    /**
                     * `d` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val d = ControlScoring.coverDistance(unit.tileX, unit.tileY, other.tileX, other.tileY)
                    if (d in 1..4) {
                        value += ControlScoring.coverPressure(d, env.scoringEnv.areAllied(unit, other))
                    }
                }
            }
            if (wounded) value += env.terrainResumeRates[env.scoringEnv.terrain?.terrainAt(x, y)] ?: 0
            /**
             * `physicalTargets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val physicalTargets = if (unit.attackAllScreen) opponents else unit.attackOffsets
                .mapNotNull { (dx, dy) -> env.scoringEnv.unitAt(unit.tileX + dx, unit.tileY + dy) }
                .distinct()
                .filter { candidate -> candidate in opponents }
            /**
             * `round` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val round = env.round()
            if ((unit.characterId == 474 && round == 1 && (x to y) in setOf(8 to 17, 9 to 17)) ||
                (unit.characterId in setOf(258, 259) && round in 2..3 && physicalTargets.isNotEmpty())
            ) {
                diagnosticScores += "$x,$y=" + physicalTargets.joinToString("|") {
                    "${it.characterId}:${
                        BattleAiScorer.estimatedAttackValue(
                            unit,
                            it,
                            env.scoringEnv
                        )
                    }:hp=${it.hitPoints}/${it.maxHitPoints}:harm=${
                        PhysicalDamageCalculator.basePhysicalDamage(
                            unit,
                            it,
                            env.scoringEnv.basePhysicalDamageContext(unit, it, false)
                        )
                    }:rate=${env.scoringEnv.probabilityResolver.physicalHitRate(unit, it)}"
                }
            }
            /**
             * `scoredPhysicalTargets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val scoredPhysicalTargets = physicalTargets.filter { candidate ->
                BattleAiScorer.canAttack(unit, candidate) &&
                        (effectiveAiFlags and 2 == 0 || !BattleAiScorer.canAttack(candidate, unit))
            }.mapNotNull { target ->
                /**
                 * `rawValue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val rawValue = BattleAiScorer.estimatedAttackValue(unit, target, env.scoringEnv)
                if (rawValue < 1) null else target to rawValue
            }
            /**
             * `scoredTarget` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val scoredTarget = scoredPhysicalTargets.maxByOrNull { (target, rawValue) ->
                rawValue + if (aiMode in setOf(
                        ControlAi.ATTACK_UNIT,
                        ControlAi.MOVE_ATTACK_UNIT
                    ) && target === designated
                ) 110 else 0
            }
            /**
             * `target` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val target = scoredTarget?.first
            /**
             * `physicalValue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val physicalValue = scoredTarget?.second ?: Int.MIN_VALUE
            /**
             * `designatedBonus` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val designatedBonus = if (aiMode in setOf(
                    ControlAi.ATTACK_UNIT,
                    ControlAi.MOVE_ATTACK_UNIT
                ) && target === designated
            ) 110 else 0
            /**
             * `scoredPhysicalValue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val scoredPhysicalValue =
                if (physicalValue == Int.MIN_VALUE) physicalValue else physicalValue + designatedBonus
            /**
             * `magic` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val magic = bestAiMagic(unit, opponents, designated, aiMode, env)
            /**
             * `useMagic` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val useMagic = magic != null && magic.third > scoredPhysicalValue
            /**
             * `actionValue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val actionValue = if (useMagic) magic!!.third else scoredPhysicalValue
            if (actionValue != Int.MIN_VALUE) value += actionValue + 30
            /**
             * `candidate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val candidate = AiDecision(
                x, y,
                if (useMagic) magic!!.first.id else target?.id,
                if (useMagic) magic!!.second.id else null,
                value,
                actionValue.takeIf { it != Int.MIN_VALUE } ?: 0,
            )
            if (best == null || candidate.value > best!!.value) best = candidate
        }
        unit.tileX = originalX
        unit.tileY = originalY
        if (diagnosticScores.isNotEmpty()) {
            /**
             * `friend234` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val friend234 = env.scoringEnv.units().firstOrNull { it.characterId == 234 }
            env.onRecordDiagnostic(
                "diag${unit.characterId}:u234=${friend234?.tileX},${friend234?.tileY},v=${friend234?.visible},acted=${friend234?.hasActed}:arm=${unit.armType},remote=${unit.remoteAttack}:offsets=${
                    unit.attackOffsets.joinToString(
                        "|"
                    ) { "${it.first},${it.second}" }
                }:skills=${unit.skills.keys.joinToString("|")}:${diagnosticScores.joinToString(";")}"
            )
        }
        return best
    }

    /**
     * `bestAiMagic`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun bestAiMagic(
        attacker: BattleUnit,
        opponents: List<BattleUnit>,
        designated: BattleUnit?,
        aiMode: Int = attacker.ai,
        env: BattleAiDecisionEnvironment,
    ): Triple<BattleUnit, BattleMagicProfile, Int>? {
        /**
         * `scoreCache` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scoreCache = linkedMapOf<String, Int>()
        /**
         * `candidates` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val candidates = attacker.magic.asSequence()
            .filter {
                it.aiUse != 13 && MagicDamageCalculator.magicConditionReason(
                    attacker,
                    it,
                    env.weather()
                ) == null
            }
            .filter { attacker.magicPoints >= it.expendMp }
            .flatMap { magic ->
                /**
                 * `targets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val targets = when (magic.target) {
                    1 -> env.scoringEnv.units().filter { it.visible && env.scoringEnv.areAllied(it, attacker) }
                    2 -> listOf(attacker)
                    3 -> env.scoringEnv.units().filter { it.visible }
                    else -> opponents
                }
                targets.asSequence()
                    .filter { target ->
                        magic.category in setOf(1, 29) || magic.hitArea.allScreen ||
                                (target.tileX - attacker.tileX to target.tileY - attacker.tileY) in magic.hitArea.offsets
                    }
                    .map { target ->
                        /**
                         * `score` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                         */

                        var score =
                            BattleAiScorer.estimatedMagicValue(attacker, target, magic, scoreCache, env.scoringEnv)
                        if (score >= 1) {
                            if (!env.scoringEnv.areAllied(attacker, target)) {
                                score += kotlin.math.abs(attacker.tileX - target.tileX) + kotlin.math.abs(attacker.tileY - target.tileY)
                            }
                            score -= magic.expendMp * 100 / attacker.maxMagicPoints.coerceAtLeast(1)
                            if (aiMode in setOf(
                                    ControlAi.ATTACK_UNIT,
                                    ControlAi.MOVE_ATTACK_UNIT
                                ) && target === designated
                            ) score += 110
                            magic.effectOffsets.mapNotNull { (dx, dy) ->
                                env.scoringEnv.unitAt(
                                    target.tileX + dx,
                                    target.tileY + dy
                                )
                            }
                                .filter { affected ->
                                    affected !== target && affected.visible && when (magic.target) {
                                        0 -> !env.scoringEnv.areAllied(affected, attacker)
                                        1 -> env.scoringEnv.areAllied(affected, attacker)
                                        else -> true
                                    }
                                }
                                .forEach { affected ->
                                    score += BattleAiScorer.estimatedMagicValue(
                                        attacker,
                                        affected,
                                        magic,
                                        scoreCache,
                                        env.scoringEnv
                                    )
                                }
                        }
                        Triple(target, magic, score)
                    }
            }
            .filter { it.third > 0 }
            .toList()
        /**
         * `round` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val round = env.round()
        /**
         * `diagnosticMagicActor` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val diagnosticMagicActor = when {
            attacker.characterId == 147 && round == 6 -> "147r6"
            attacker.characterId == 22 && round == 4 -> "22r4"
            else -> null
        }
        if (diagnosticMagicActor != null && !env.hasDiagnosticEntry("diagMagicScores$diagnosticMagicActor:")) {
            env.onRecordDiagnostic("diagMagicScores$diagnosticMagicActor:" + candidates.joinToString(";") { (target, magic, score) ->
                "m${magic.id}/t${target.characterId}/s$score/c${magic.category}/h${magic.harmType}/p${magic.power}/mp${magic.expendMp}/ai${magic.aiUse}"
            })
        }
        return candidates.maxByOrNull { it.third }
    }
}
