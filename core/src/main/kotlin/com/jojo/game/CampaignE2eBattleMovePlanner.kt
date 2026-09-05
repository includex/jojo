package com.jojo.game
import com.jojo.game.domain.battle.*

internal data class CampaignE2eMovePlan(
    val manualMove: CampaignE2eMoveInput?,
    val s57CriticalFinisherActive: Boolean,
)

internal class CampaignE2eBattleMovePlanner {
    private data class PlannedMove(
        val unit: BattleUnit,
        val destination: Pair<Int, Int>,
        val actionRank: Int,
        val targetRank: Int,
        val distance: Int,
    )

    fun plan(board: CampaignE2eBattlePlanningBoard): CampaignE2eMovePlan {
        val battle = board.battle
        val selected = board.selected
        val screenPoint = board.ctx.screenPoint
        val sourceScenario = board.scenario
        val guidedAuthoredRoute = board.guidedAuthoredRoute
        val visibleEnemies = board.visibleEnemies
        val s57FirstRoomFocus = board.s57FirstRoomFocus
        val s57Route = board.s57Route
        val s57GateTarget = board.s57GateTarget
        val waitForAuthoredAttrition = board.waitForAuthoredAttrition
        val protectS57MineMaster = board.protectS57MineMaster
        val s57FirstRoomFocusUnit = board.s57FirstRoomFocusUnit
        val occupiedTiles = board.occupiedTiles
        val strategicTarget = board.strategicTarget
        val targetPriority = board::targetPriority
        val attackableFrom = board::attackableFrom
        val canAttack = board::canAttack
        val s57MagicPlanFor = board::guidedMagicPlanFor
        val candidateSequence = when {
            selected != null -> sequenceOf(selected)
            sourceScenario == "S_01" -> battle.units.values.asSequence().filter {
                it.visible && it.type() == Faction.PLAYER && !it.hasActed
            }

            s57GateTarget != null -> battle.units.values.asSequence().filter {
                it.visible && it.type() == Faction.PLAYER && it.characterId == 0 && !it.hasActed
            }

            else -> battle.units.values.asSequence()
                .filter { it.visible && it.type() == Faction.PLAYER && !it.hasActed }
        }
        val eligibleCandidates = candidateSequence.filter { productionManualUnitEligible(it.statuses) }.toList()
        val s57MineMaster = eligibleCandidates.firstOrNull { it.characterId == 0 }
        val s57CriticalFinisherMoveDestination = s57FirstRoomFocusUnit?.let { focus ->
            s57MineMaster?.let { mineMaster ->
                s57CriticalFinisherDestination(
                    current = mineMaster.tileX to mineMaster.tileY,
                    reachableLegalTiles = executableProductionMoveTiles(
                        mineMaster.tileX to mineMaster.tileY,
                        battle.movement.reachableTiles(mineMaster.id).keys,
                        occupiedTiles,
                    ),
                    focusTile = focus.tileX to focus.tileY,
                    attackAllScreen = mineMaster.attackAllScreen,
                    attackOffsets = mineMaster.attackOffsets,
                )
            }
        }
        val s57CriticalFinisherActive = s57FirstRoomCriticalFinisherActive(
            s57FirstRoomFocus?.hitPoints,
            s57MineMaster?.let { mineMaster ->
                s57FirstRoomFocusUnit?.let { focus -> battle.combat.physicalDamagePreview(mineMaster.id, focus.id) }
            } ?: 0,
            s57CriticalFinisherMoveDestination != null,
        )

        val rawCandidates = if (protectS57MineMaster) {
            val escorts = eligibleCandidates.filter { it.characterId != 0 }
            val mineMaster = eligibleCandidates.filter { it.characterId == 0 }
            when {
                s57CriticalFinisherActive -> mineMaster
                escorts.isNotEmpty() -> escorts
                else -> mineMaster
            }
        } else eligibleCandidates
        val attritionCandidates = if (waitForAuthoredAttrition) {
            rawCandidates.filter { it.characterId != 0 }
        } else rawCandidates
        val locatedCandidates = attritionCandidates.filter { it.hasAuthoredTileX && it.hasAuthoredTileY }
            .ifEmpty { attritionCandidates }
        val s57FocusHitAvailableAcrossEscorts = s57FirstRoomFocusUnit?.let { focus ->
            protectS57MineMaster && locatedCandidates.any { unit ->
                unit.characterId != 0 && executableProductionMoveTiles(
                    unit.tileX to unit.tileY,
                    battle.movement.reachableTiles(unit.id).keys,
                    occupiedTiles,
                ).any { tile -> canAttack(unit, tile, focus) }
            }
        } == true
        val move = locatedCandidates.mapNotNull { unit ->
            val current = unit.tileX to unit.tileY
            val currentMagicPlan = s57MagicPlanFor(unit)
            val reachable = executableProductionMoveTiles(
                current,
                battle.movement.reachableTiles(unit.id).keys,
                occupiedTiles,
            )
            val escortFocus = s57FirstRoomFocusUnit?.takeIf {
                protectS57MineMaster && (unit.characterId != 0 ||
                        (unit.characterId == 0 && s57CriticalFinisherActive))
            }
            val escortGuards = escortFocus?.let { focus ->
                visibleEnemies.asSequence()
                    .filter { enemy -> enemy.id != focus.id && enemy.characterId !in setOf(165, 162, 169) }
                    .map { enemy ->
                        S57EscortFocusBlocker(
                            enemy.id,
                            enemy.tileX to enemy.tileY,
                            enemy.hitPoints,
                            enemy.retreatCount
                        )
                    }
                    .toList()
            }.orEmpty()
            val openedFocusStagingByGuard = escortFocus?.let { focus ->
                val stagingTiles = unit.attackOffsets.map { (dx, dy) ->
                    focus.tileX - dx to focus.tileY - dy
                }.toSet()
                escortGuards.associate { guard ->
                    val attackFrom = s57EscortAttackFrom(
                        current, reachable, guard.tile, unit.attackAllScreen, unit.attackOffsets,
                    )
                    val opensRoute = attackFrom != null && battle.movement.canEnterTilesIgnoringEnemyWithinMoves(
                        unit.id, guard.unitId, attackFrom, stagingTiles,
                    )
                    guard.unitId to if (opensRoute) stagingTiles else emptySet()
                }
            }.orEmpty()
            val escortBlockerFallback = escortFocus?.takeIf { !s57FocusHitAvailableAcrossEscorts }?.let { focus ->
                s57EscortFocusBlockerFallback(
                    current = current,
                    reachableLegalTiles = reachable,
                    focusTile = focus.tileX to focus.tileY,
                    attackAllScreen = unit.attackAllScreen,
                    attackOffsets = unit.attackOffsets,
                    occupiedTiles = occupiedTiles,
                    guards = escortGuards,
                    openedStagingReachableByGuard = openedFocusStagingByGuard,
                )
            }
            val currentTargets = if (escortFocus != null) {
                listOfNotNull(
                    escortFocus.takeIf { canAttack(unit, current, it) }
                        ?: escortBlockerFallback?.blocker?.let { blocker ->
                            visibleEnemies.firstOrNull { it.id == blocker.unitId }
                                ?.takeIf { canAttack(unit, current, it) }
                        },
                )
            } else attackableFrom(unit, current)
            val attackTiles = if (escortFocus != null) {
                val leaderTiles = reachable.mapNotNull { tile ->
                    escortFocus.takeIf { canAttack(unit, tile, it) }
                        ?.let { target -> Triple(tile, 0, target.hitPoints) }
                }
                leaderTiles.ifEmpty {
                    escortBlockerFallback?.let { fallback ->
                        listOf(Triple(fallback.attackFrom, 1, fallback.blocker.hitPoints))
                    }.orEmpty()
                }
            } else {
                reachable.mapNotNull { tile ->
                    attackableFrom(unit, tile).firstOrNull()
                        ?.let { target -> Triple(tile, targetPriority(target), target.hitPoints) }
                }
            }
            val chosenAttack = attackTiles.minWithOrNull(
                compareBy<Triple<Pair<Int, Int>, Int, Int>> { it.second }
                    .thenBy { it.third }
                    .thenBy { candidate ->
                        strategicTarget?.let {
                            kotlin.math.abs(candidate.first.first - it.first) + kotlin.math.abs(
                                candidate.first.second - it.second
                            )
                        }
                            ?: 0
                    },
            )
            val destination = when {
                currentMagicPlan != null -> current
                s57GateTarget != null -> s57GateDestination(current, reachable)
                s57CriticalFinisherActive && unit.characterId == 0 -> s57CriticalFinisherMoveDestination
                sourceScenario == "S_01" && unit.characterId == 0 -> s01SurvivalDestination(
                    current,
                    reachable,
                    visibleEnemies.map { it.tileX to it.tileY },
                    battle.units.values.asSequence()
                        .filter { it.visible && it.hitPoints > 0 && it.type() == Faction.PLAYER }
                        .map { it.tileX to it.tileY }
                        .toList(),
                )

                protectS57MineMaster && unit.characterId == 0 && escortFocus == null -> reachable.minWithOrNull(
                    compareBy<Pair<Int, Int>> {
                        kotlin.math.abs(it.first - 21) + kotlin.math.abs(it.second - 19)
                    }.thenBy { kotlin.math.abs(it.first - current.first) + kotlin.math.abs(it.second - current.second) },
                )

                escortFocus != null && escortBlockerFallback != null -> escortBlockerFallback.attackFrom
                escortFocus != null -> s57EscortFocusDestination(
                    current, reachable, escortFocus.tileX to escortFocus.tileY,
                    unit.attackAllScreen, unit.attackOffsets,
                )

                currentTargets.isNotEmpty() -> current
                chosenAttack != null -> chosenAttack.first
                strategicTarget != null -> reachable.minWithOrNull(
                    compareBy<Pair<Int, Int>> { kotlin.math.abs(it.first - strategicTarget.first) + kotlin.math.abs(it.second - strategicTarget.second) }
                        .thenBy { kotlin.math.abs(it.first - current.first) + kotlin.math.abs(it.second - current.second) },
                )

                else -> reachable.filter { it != current }
                    .sortedWith(compareBy<Pair<Int, Int>> { it.first }.thenBy { it.second })
                    .firstOrNull()
            } ?: return@mapNotNull null
            val attacks = if (escortFocus != null) {
                listOfNotNull(
                    escortFocus.takeIf { canAttack(unit, destination, it) }
                        ?: escortBlockerFallback?.blocker?.let { blocker ->
                            visibleEnemies.firstOrNull { it.id == blocker.unitId }
                                ?.takeIf { canAttack(unit, destination, it) }
                        },
                )
            } else attackableFrom(unit, destination)
            val planningTarget = escortFocus?.let { it.tileX to it.tileY } ?: strategicTarget
            val distance = planningTarget?.let {
                kotlin.math.abs(destination.first - it.first) + kotlin.math.abs(destination.second - it.second)
            } ?: 0
            PlannedMove(
                unit,
                destination,
                if (currentMagicPlan != null) -1 else if (escortFocus != null) s57FirstRoomActionRank(
                    leaderHit = escortFocus.takeIf { canAttack(unit, destination, it) } != null,
                    focusProgress = escortBlockerFallback == null,
                    blockerHit = escortBlockerFallback?.blocker?.let { blocker ->
                        visibleEnemies.firstOrNull { it.id == blocker.unitId }
                            ?.let { canAttack(unit, destination, it) }
                    } == true,
                ) else if (attacks.isNotEmpty()) 0 else 1,
                if (escortFocus != null) 0 else currentMagicPlan?.targetId?.let { targetId ->
                    battle.units[targetId]?.let { targetPriority(it) }
                } ?: attacks.firstOrNull()?.let { targetPriority(it) } ?: 10_000,
                distance,
            )
        }.minWithOrNull(
            if (guidedAuthoredRoute) compareBy<PlannedMove> { it.actionRank }
                .thenBy { it.targetRank }.thenBy { it.distance }.thenByDescending { it.unit.hitPoints }
            else compareBy { rawCandidates.indexOf(it.unit) },
        )?.let { Triple(it.unit, it.destination, screenPoint(it.unit.tileX, it.unit.tileY)) }
        return CampaignE2eMovePlan(
            manualMove = move?.let { (_, destination, source) ->
                val target = screenPoint(destination.first, destination.second)
                CampaignE2eMoveInput(source.first, source.second, target.first, target.second)
            },
            s57CriticalFinisherActive = s57CriticalFinisherActive,
        )
    }
}
