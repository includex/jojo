package com.jojo.game

internal data class BattleAiDecisionEnvironment(
    val scoringEnv: BattleAiScoringEnvironment,
    val reachableTiles: (String) -> Map<Pair<Int, Int>, Int>,
    val terrainResumeRates: Map<Int, Int>,
    val weather: () -> BattleWeather,
    val round: () -> Int,
    val onRecordDiagnostic: (String) -> Unit = {},
    val hasDiagnosticEntry: (prefix: String) -> Boolean = { false },
)

internal data class AiDecision(
    val x: Int,
    val y: Int,
    val targetId: String?,
    val magicId: Int?,
    val value: Int,
    /** Control._AIProcess stores info.value, not terrain-inclusive value. */
    val actionValue: Int = 0,
)

/**
 * Pure Kotlin decision planner for AI movement and action selection.
 * Evaluates reachable tiles, cover pressure, physical attack scoring, and strategy/magic candidate scoring.
 */
internal object BattleAiDecisionPlanner {

    fun chooseAiDecision(
        unit: BattleUnit,
        opponents: List<BattleUnit>,
        designated: BattleUnit?,
        aiMode: Int = unit.ai,
        aiFlags: Int = 0,
        candidatePoints: Collection<Pair<Int, Int>>? = null,
        env: BattleAiDecisionEnvironment,
    ): AiDecision? {
        val reachable = env.reachableTiles(unit.id).keys
        val points = candidatePoints?.toCollection(linkedSetOf()) ?: when (aiMode) {
            0 -> linkedSetOf(unit.tileX to unit.tileY).apply {
                val allPoints = linkedSetOf(unit.tileX to unit.tileY).apply { addAll(reachable) }
                val hasAttackTarget = allPoints.any { (x, y) ->
                    opponents.any { target -> BattleAiScorer.canAttackFrom(unit, x, y, target) }
                }
                if (hasAttackTarget) addAll(reachable)
            }
            2 -> linkedSetOf(unit.tileX to unit.tileY)
            4, 6 -> {
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
        var best: AiDecision? = null
        val diagnosticScores = mutableListOf<String>()
        val originalX = unit.tileX
        val originalY = unit.tileY
        val effectiveAiFlags = if (aiFlags and 2 != 0 && unit.skills[226]?.and(255)?.let { it != 255 } == true) aiFlags and 2.inv() else aiFlags
        points.forEach { (x, y) ->
            env.scoringEnv.unitAt(x, y)?.takeIf { it !== unit }?.let { return@forEach }
            unit.tileX = x
            unit.tileY = y
            var value = (unit.terrainImpacts[env.scoringEnv.terrain?.terrainAt(x, y)] ?: 100) / 5
            val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
            if (unit.armType == 1 || unit.remoteAttack || wounded) {
                env.scoringEnv.units().filter { it.visible && it != unit }.forEach { other ->
                    val d = ControlScoring.coverDistance(unit.tileX, unit.tileY, other.tileX, other.tileY)
                    if (d in 1..4) {
                        value += ControlScoring.coverPressure(d, env.scoringEnv.areAllied(unit, other))
                    }
                }
            }
            if (wounded) value += env.terrainResumeRates[env.scoringEnv.terrain?.terrainAt(x, y)] ?: 0
            val physicalTargets = if (unit.attackAllScreen) opponents else unit.attackOffsets
                .mapNotNull { (dx, dy) -> env.scoringEnv.unitAt(unit.tileX + dx, unit.tileY + dy) }
                .distinct()
                .filter { candidate -> candidate in opponents }
            val round = env.round()
            if ((unit.characterId == 474 && round == 1 && (x to y) in setOf(8 to 17, 9 to 17)) ||
                (unit.characterId in setOf(258, 259) && round in 2..3 && physicalTargets.isNotEmpty())) {
                diagnosticScores += "$x,$y=" + physicalTargets.joinToString("|") {
                    "${it.characterId}:${BattleAiScorer.estimatedAttackValue(unit, it, env.scoringEnv)}:hp=${it.hitPoints}/${it.maxHitPoints}:harm=${PhysicalDamageCalculator.basePhysicalDamage(unit, it, env.scoringEnv.basePhysicalDamageContext(unit, it, false))}:rate=${env.scoringEnv.probabilityResolver.physicalHitRate(unit, it)}"
                }
            }
            val scoredPhysicalTargets = physicalTargets.filter { candidate ->
                BattleAiScorer.canAttack(unit, candidate) &&
                    (effectiveAiFlags and 2 == 0 || !BattleAiScorer.canAttack(candidate, unit))
            }.mapNotNull { target ->
                val rawValue = BattleAiScorer.estimatedAttackValue(unit, target, env.scoringEnv)
                if (rawValue < 1) null else target to rawValue
            }
            val scoredTarget = scoredPhysicalTargets.maxByOrNull { (target, rawValue) ->
                rawValue + if (aiMode in setOf(ControlAi.ATTACK_UNIT, ControlAi.MOVE_ATTACK_UNIT) && target === designated) 110 else 0
            }
            val target = scoredTarget?.first
            val physicalValue = scoredTarget?.second ?: Int.MIN_VALUE
            val designatedBonus = if (aiMode in setOf(ControlAi.ATTACK_UNIT, ControlAi.MOVE_ATTACK_UNIT) && target === designated) 110 else 0
            val scoredPhysicalValue = if (physicalValue == Int.MIN_VALUE) physicalValue else physicalValue + designatedBonus
            val magic = bestAiMagic(unit, opponents, designated, aiMode, env)
            val useMagic = magic != null && magic.third > scoredPhysicalValue
            val actionValue = if (useMagic) magic!!.third else scoredPhysicalValue
            if (actionValue != Int.MIN_VALUE) value += actionValue + 30
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
            val friend234 = env.scoringEnv.units().firstOrNull { it.characterId == 234 }
            env.onRecordDiagnostic("diag${unit.characterId}:u234=${friend234?.tileX},${friend234?.tileY},v=${friend234?.visible},acted=${friend234?.hasActed}:arm=${unit.armType},remote=${unit.remoteAttack}:offsets=${unit.attackOffsets.joinToString("|") { "${it.first},${it.second}" }}:skills=${unit.skills.keys.joinToString("|")}:${diagnosticScores.joinToString(";")}")
        }
        return best
    }

    fun bestAiMagic(
        attacker: BattleUnit,
        opponents: List<BattleUnit>,
        designated: BattleUnit?,
        aiMode: Int = attacker.ai,
        env: BattleAiDecisionEnvironment,
    ): Triple<BattleUnit, GameDataCatalog.MagicProfile, Int>? {
        val scoreCache = linkedMapOf<String, Int>()
        val candidates = attacker.magic.asSequence()
            .filter { it.aiUse != 13 && MagicDamageCalculator.magicConditionReason(attacker, it, env.weather()) == null }
            .filter { attacker.magicPoints >= it.expendMp }
            .flatMap { magic ->
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
                        var score = BattleAiScorer.estimatedMagicValue(attacker, target, magic, scoreCache, env.scoringEnv)
                        if (score >= 1) {
                            if (!env.scoringEnv.areAllied(attacker, target)) {
                                score += kotlin.math.abs(attacker.tileX - target.tileX) + kotlin.math.abs(attacker.tileY - target.tileY)
                            }
                            score -= magic.expendMp * 100 / attacker.maxMagicPoints.coerceAtLeast(1)
                            if (aiMode in setOf(ControlAi.ATTACK_UNIT, ControlAi.MOVE_ATTACK_UNIT) && target === designated) score += 110
                            magic.effectOffsets.mapNotNull { (dx, dy) -> env.scoringEnv.unitAt(target.tileX + dx, target.tileY + dy) }
                                .filter { affected ->
                                    affected !== target && affected.visible && when (magic.target) {
                                        0 -> !env.scoringEnv.areAllied(affected, attacker)
                                        1 -> env.scoringEnv.areAllied(affected, attacker)
                                        else -> true
                                    }
                                }
                                .forEach { affected ->
                                    score += BattleAiScorer.estimatedMagicValue(attacker, affected, magic, scoreCache, env.scoringEnv)
                                }
                        }
                        Triple(target, magic, score)
                    }
            }
            .filter { it.third > 0 }
            .toList()
        val round = env.round()
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
