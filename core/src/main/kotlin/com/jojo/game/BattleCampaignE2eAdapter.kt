package com.jojo.game

/**
 * SRPG Battle Campaign E2E State Adapter.
 *
 * Decouples the E2E verification/observation layer from the LibGDX presentation layer (BattleScreen).
 * Projects live tactical state into CampaignE2eBattleState using provided projection delegates.
 */
internal object BattleCampaignE2eAdapter {

    data class ProjectionContext(
        val scenario: String,
        val battle: Battle,
        val selectedUnitId: String?,
        val authoredMechanicRoute: AuthoredMechanicRouteTracker,
        val scriptState: PlaybackState,
        val selectedChoice: Int,
        val bootstrapPhase: BattleBootstrapPhase,
        val initialPlayerCampScriptStarted: Boolean,
        val resultScene1Observed: Boolean,
        val naturalOutcomeScriptStarted: Boolean,
        val postBattleSceneStarted: Boolean,
        val rewardOpen: Boolean,
        val winConditionsOpen: Boolean,
        val savePromptOpen: Boolean,
        val losePromptOpen: Boolean,
        val loseTitleScreenPoint: Pair<Int, Int>,
        val playerMoveCommitted: Boolean,
        val campaignStage: Int,
        val turnPhase: BattleTurnController.Phase,
        val battleMenuOpen: Boolean,
        val battleCommandOpen: Boolean,
        val battleTargetSelectionOpen: Boolean,
        val magickListOpen: Boolean,
        val magicMode: Boolean,
        val waitCommandScreenPoint: Pair<Int, Int>,
        val endRoundCommandScreenPoint: Pair<Int, Int>,
        val battleMenuButtonScreenPoint: Pair<Int, Int>,
        val autoBattleToggleScreenPoint: Pair<Int, Int>,
        val autoBattleConfirmScreenPoint: Pair<Int, Int>,
        val autoBattleOverlay: AutoBattleFlow.Overlay,
        val autoBattleChecked: Boolean,
        val collocation: Boolean,
        val committedPlayerMove: String?,
        val screenPoint: (x: Int, y: Int) -> Pair<Int, Int>,
        val projectWorldPoint: (worldX: Float, worldY: Float) -> Pair<Int, Int>,
    )

    private data class PlannedMove(
        val unit: BattleUnit,
        val destination: Pair<Int, Int>,
        val actionRank: Int,
        val targetRank: Int,
        val distance: Int,
    )

    fun computeState(ctx: ProjectionContext): CampaignE2eBattleState {
        val battle = ctx.battle
        val selected = ctx.selectedUnitId?.let(battle.units::get)
        val screenPoint = ctx.screenPoint
        val sourceScenario = ctx.scenario
        val guidedAuthoredRoute = sourceScenario == "S_52" || sourceScenario == "S_57"
        val visibleEnemies = battle.units.values.filter {
            it.visible && it.hitPoints > 0 && it.type().isEnemySide()
        }
        // S57 first room is escort-led: recompute one leader from the live
        // HP projection every driver observation, never from stale plan state.
        val s57FirstRoomFocus = s57FirstRoomEscortFocus(visibleEnemies.mapNotNull { enemy ->
            enemy.characterId?.takeIf { it in setOf(165, 162, 169) }?.let { characterId ->
                S57FirstRoomLeader(enemy.id, characterId, enemy.hitPoints, enemy.tileX to enemy.tileY)
            }
        })
        fun targetPriority(unit: BattleUnit): Int {
            val characterId = unit.characterId
            val order = when (sourceScenario) {
                "S_52" -> listOf(170, 171, 172, 173)
                // Each trio opens the next authored room; Zhuge Liang is last.
                "S_57" -> listOf(165, 162, 169, 166, 167, 168, 163, 164, 35)
                else -> emptyList()
            }
            return order.indexOf(characterId).takeIf { it >= 0 } ?: 10_000
        }
        val playerTiles = battle.units.values.asSequence()
            .filter { it.visible && it.type() == Faction.PLAYER }
            .map { it.tileX to it.tileY }
            .toList()
        val s57Route = s57AuthoredRouteSignal(
            sourceScenario,
            visibleEnemies.mapNotNull { it.characterId },
            battle.units.values.any { unit ->
                unit.visible && unit.type() == Faction.PLAYER && unit.characterId == 0 &&
                    unit.tileX in 2..16 && unit.tileY in 11..23
            },
            playerTiles.size,
        )
        // Keep the nullable route target in a stable local for the move
        // planner below (and avoid re-reading an optional route signal from
        // inside its comparison lambda).
        val s57GateTarget = s57Route.gateTarget
        val waitForAuthoredAttrition = s57Route.waitForAttrition
        val protectS57MineMaster = sourceScenario == "S_57" && s57FirstRoomFocus != null
        val s57FirstRoomFocusUnit = s57FirstRoomFocus?.unitId?.let(battle.units::get)
        val strategicVisibleEnemies = s57Route.combatTargetIds.takeIf { it.isNotEmpty() }
            ?.let { ids -> visibleEnemies.filter { it.characterId in ids } }
            ?: visibleEnemies
        // Room leaders determine where the party advances, but source maps
        // place summoned guards in the intervening corridor.  Those blockers
        // remain legal tactical targets outside the S57 first-room escort
        // focus; filtering them out globally made every Mine unit walk up to
        // the obstruction and WAIT until Cao Cao died.
        val routedVisibleEnemies = visibleEnemies
        val occupiedTiles = battle.units.values.asSequence()
            .filter { it.visible && it.hitPoints > 0 }
            .map { it.tileX to it.tileY }
            .toSet()
        val waypoint = s57GateTarget ?: ctx.authoredMechanicRoute.target(playerTiles)
        fun distanceFromParty(enemy: BattleUnit): Int = playerTiles.minOfOrNull { (x, y) ->
            kotlin.math.abs(enemy.tileX - x) + kotlin.math.abs(enemy.tileY - y)
        } ?: Int.MAX_VALUE
        val strategicComparator = if (sourceScenario == "S_57") {
            // S57 authors no order for its three room leaders. Walking toward
            // the nearest one avoids exposing Cao Cao while crossing the room.
            compareBy<BattleUnit>(::distanceFromParty).thenBy(::targetPriority)
        } else {
            compareBy<BattleUnit>(::targetPriority).thenBy(::distanceFromParty)
        }
        val strategicTarget = if (sourceScenario == "S_01") {
            val byId = visibleEnemies.associateBy { it.id }
            s01PreferredAttackTargets(
                visibleEnemies.map { S01EnemyTarget(it.id, it.characterId, it.hitPoints) },
                visibleEnemies.size,
            ).firstOrNull()?.let { byId[it.unitId] }?.let { it.tileX to it.tileY } ?: waypoint
        } else strategicVisibleEnemies.minWithOrNull(strategicComparator)
            ?.let { it.tileX to it.tileY } ?: waypoint

        fun attackableFrom(unit: BattleUnit, tile: Pair<Int, Int>): List<BattleUnit> {
            val attackable = routedVisibleEnemies.filter { target ->
                unit.attackAllScreen || (target.tileX - tile.first to target.tileY - tile.second) in unit.attackOffsets
            }
            if (sourceScenario == "S_01") {
                val byId = attackable.associateBy { it.id }
                return s01PreferredAttackTargets(
                    attackable.map { S01EnemyTarget(it.id, it.characterId, it.hitPoints) },
                    visibleEnemies.size,
                ).mapNotNull { byId[it.unitId] }
            }
            return attackable.sortedWith(compareBy<BattleUnit>(::targetPriority).thenBy { it.hitPoints })
        }
        fun canAttack(unit: BattleUnit, tile: Pair<Int, Int>, target: BattleUnit): Boolean =
            unit.attackAllScreen || (target.tileX - tile.first to target.tileY - tile.second) in unit.attackOffsets

        val candidateSequence = when {
            selected != null -> sequenceOf(selected)
            // S01's Mine party acts through the ordinary UI. Cao Cao remains
            // rear-safe by the destination/attack gates below; FRIEND still
            // takes its untouched engine-controlled AI turn.
            sourceScenario == "S_01" -> battle.units.values.asSequence().filter {
                it.visible && it.type() == Faction.PLAYER && !it.hasActed
            }
            s57GateTarget != null -> battle.units.values.asSequence().filter {
                it.visible && it.type() == Faction.PLAYER && it.characterId == 0 && !it.hasActed
            }
            else -> battle.units.values.asSequence().filter { it.visible && it.type() == Faction.PLAYER && !it.hasActed }
        }
        // Confused/paralysed units reject every manual attack in Battle.attack.
        // Planning them merely opens a child target layer that can never
        // complete (S_52's round-four confusion is the production case).
        val eligibleCandidates = candidateSequence.filter { productionManualUnitEligible(it.statuses) }.toList()
        val s57MineMaster = eligibleCandidates.firstOrNull { it.characterId == 0 }
        val s57CriticalFinisherMoveDestination = s57FirstRoomFocusUnit?.let { focus ->
            s57MineMaster?.let { mineMaster ->
                s57CriticalFinisherDestination(
                    current = mineMaster.tileX to mineMaster.tileY,
                    reachableLegalTiles = executableProductionMoveTiles(
                        mineMaster.tileX to mineMaster.tileY,
                        battle.reachableTiles(mineMaster.id).keys,
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
                s57FirstRoomFocusUnit?.let { focus -> battle.previewPhysicalDamage(mineMaster.id, focus.id) }
            } ?: 0,
            s57CriticalFinisherMoveDestination != null,
        )
        fun s57MagicPlanFor(caster: BattleUnit): CampaignE2eGuidedMagicPlan? {
            // Silence leaves ordinary physical movement/attack legal, but
            // Battle.castMagic rejects it before checking MP or range.
            if (BattleStatus.SILENCE in caster.statuses) return null
            return s57GuidedOffensiveMagicPlan(
                scenario = sourceScenario,
                guidedAuthoredRoute = guidedAuthoredRoute,
                holdFire = s57Route.holdFire,
                firstRoomLeaderVisible = protectS57MineMaster,
                casterCharacterId = caster.characterId,
                casterX = caster.tileX,
                casterY = caster.tileY,
                magicPoints = caster.magicPoints,
                options = caster.magic.map { magic ->
                    CampaignE2eMagicOption(
                        id = magic.id,
                        target = magic.target,
                        cost = magic.expendMp,
                        power = magic.power,
                        category = magic.category,
                        allScreen = magic.hitArea.allScreen,
                        offsets = magic.hitArea.offsets,
                    )
                },
                visibleEnemies = routedVisibleEnemies.map { enemy ->
                    CampaignE2eMagicTarget(enemy.id, enemy.tileX, enemy.tileY)
                },
            )
        }
        // S_57 loses immediately when Cao Cao (source 0) dies. Keep him
        // behind the party while the first-room trio is present; the authored
        // second-room branch later selects him explicitly for its gate tile.
        // Whirlwind is deliberately disabled while any first-room leader is
        // alive. Escorts engage first; Cao Cao keeps retreating even after
        // they are gone until the original room-clear callback opens the
        // second-room gate route.
        val rawCandidates = if (protectS57MineMaster) {
            // Escorts engage first. Cao Cao acts only after them, retreating
            // toward the entrance instead of advancing into focused AI fire.
            // A confirmed one-hit finisher is the sole exception: it selects
            // source 0 before every escort and permits only that leader hit.
            val escorts = eligibleCandidates.filter { it.characterId != 0 }
            val mineMaster = eligibleCandidates.filter { it.characterId == 0 }
            when {
                s57CriticalFinisherActive -> mineMaster
                escorts.isNotEmpty() -> escorts
                else -> mineMaster
            }
        } else eligibleCandidates
        // S_57 loses immediately if Cao Cao dies. During the authored
        // attrition branch, expose the remaining party through real moves and
        // enemy attacks but leave character 0 uncommitted. If only he remains
        // unacted, manualMove becomes null and the real end-round UI is used.
        val attritionCandidates = if (waitForAuthoredAttrition) {
            rawCandidates.filter { it.characterId != 0 }
        } else rawCandidates
        // Omitted x/y entries retain their prefab origin until a script move;
        // probing those synthetic (0,0) tiles caused S_52's 24 failed pans.
        val locatedCandidates = attritionCandidates.filter { it.hasAuthoredTileX && it.hasAuthoredTileY }
            .ifEmpty { attritionCandidates }
        // The old fallback was leader-first only per escort. At global sort
        // time a blocker strike tied a different escort's leader hit, so the
        // party consumed turns on reviving guards. Compute this once from the
        // same live reachable/occupancy projection used by every candidate.
        val s57FocusHitAvailableAcrossEscorts = s57FirstRoomFocusUnit?.let { focus ->
            protectS57MineMaster && locatedCandidates.any { unit ->
                unit.characterId != 0 && executableProductionMoveTiles(
                    unit.tileX to unit.tileY,
                    battle.reachableTiles(unit.id).keys,
                    occupiedTiles,
                ).any { tile -> canAttack(unit, tile, focus) }
            }
        } == true
        val move = locatedCandidates.mapNotNull { unit ->
            val current = unit.tileX to unit.tileY
            val currentMagicPlan = s57MagicPlanFor(unit)
            val reachable = executableProductionMoveTiles(
                current,
                battle.reachableTiles(unit.id).keys,
                occupiedTiles,
            )
            // During the first room escorts neither score nor attack guards:
            // a focus hit may only name the selected leader. Source 0 takes
            // the independent rear-move branch below.
            val escortFocus = s57FirstRoomFocusUnit?.takeIf {
                protectS57MineMaster && (unit.characterId != 0 ||
                    (unit.characterId == 0 && s57CriticalFinisherActive))
            }
            val escortGuards = escortFocus?.let { focus ->
                visibleEnemies.asSequence()
                    .filter { enemy -> enemy.id != focus.id && enemy.characterId !in setOf(165, 162, 169) }
                    .map { enemy -> S57EscortFocusBlocker(enemy.id, enemy.tileX to enemy.tileY, enemy.hitPoints, enemy.retreatCount) }
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
                    val opensRoute = attackFrom != null && battle.canEnterTilesIgnoringEnemyWithinMoves(
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
                        strategicTarget?.let { kotlin.math.abs(candidate.first.first - it.first) + kotlin.math.abs(candidate.first.second - it.second) }
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
                // Only when no focus staging tile is reachable can the narrow
                // blocker exception displace the usual leader-first movement.
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
                    battle.units[targetId]?.let(::targetPriority)
                } ?: attacks.firstOrNull()?.let(::targetPriority) ?: 10_000,
                distance,
            )
        }.minWithOrNull(
            if (guidedAuthoredRoute) compareBy<PlannedMove> { it.actionRank }
                .thenBy { it.targetRank }.thenBy { it.distance }.thenByDescending { it.unit.hitPoints }
            else compareBy { rawCandidates.indexOf(it.unit) },
        )?.let { Triple(it.unit, it.destination, screenPoint(it.unit.tileX, it.unit.tileY)) }

        val manualMove = move?.let { (_, destination, source) ->
            val target = screenPoint(destination.first, destination.second)
            CampaignE2eMoveInput(source.first, source.second, target.first, target.second)
        }
        val attackTarget = selected?.let { unit ->
            val escortFocus = s57FirstRoomFocusUnit?.takeIf {
                protectS57MineMaster && (unit.characterId != 0 ||
                    (unit.characterId == 0 && s57CriticalFinisherActive))
            }
            if (escortFocus != null) {
                val current = unit.tileX to unit.tileY
                val reachable = executableProductionMoveTiles(
                    current,
                    battle.reachableTiles(unit.id).keys,
                    occupiedTiles,
                )
                val escortGuards = visibleEnemies.asSequence()
                    .filter { enemy -> enemy.id != escortFocus.id && enemy.characterId !in setOf(165, 162, 169) }
                    .map { enemy -> S57EscortFocusBlocker(enemy.id, enemy.tileX to enemy.tileY, enemy.hitPoints, enemy.retreatCount) }
                    .toList()
                val stagingTiles = unit.attackOffsets.map { (dx, dy) ->
                    escortFocus.tileX - dx to escortFocus.tileY - dy
                }.toSet()
                val openedFocusStagingByGuard = escortGuards.associate { guard ->
                    val attackFrom = s57EscortAttackFrom(
                        current, reachable, guard.tile, unit.attackAllScreen, unit.attackOffsets,
                    )
                    val opensRoute = attackFrom != null && battle.canEnterTilesIgnoringEnemyWithinMoves(
                        unit.id, guard.unitId, attackFrom, stagingTiles,
                    )
                    guard.unitId to if (opensRoute) stagingTiles else emptySet()
                }
                escortFocus.takeIf { canAttack(unit, current, it) }
                    ?: (if (s57CriticalFinisherActive && unit.characterId == 0) null else s57EscortFocusBlockerFallback(
                        current = current,
                        reachableLegalTiles = reachable,
                        focusTile = escortFocus.tileX to escortFocus.tileY,
                        attackAllScreen = unit.attackAllScreen,
                        attackOffsets = unit.attackOffsets,
                        occupiedTiles = occupiedTiles,
                        guards = escortGuards,
                        openedStagingReachableByGuard = openedFocusStagingByGuard,
                    )?.blocker?.let { blocker ->
                        visibleEnemies.firstOrNull { it.id == blocker.unitId }
                            ?.takeIf { canAttack(unit, current, it) }
                    })
            } else attackableFrom(unit, unit.tileX to unit.tileY).firstOrNull()
        }
        val s01CaoCaoAttackSafe = selected?.takeIf { sourceScenario == "S_01" && it.characterId == 0 }
            ?.let { source -> attackTarget?.let { target ->
                s01CaoCaoSafeLeaderAttack(
                    source.hitPoints,
                    source.defense,
                    source.tileX to source.tileY,
                    target.characterId,
                    target.attack,
                    target.level,
                    target.tileX to target.tileY,
                )
            } } ?: false
        val livePhysicalAttackTarget = attackTarget?.takeIf { target ->
            // `CampaignE2eAttackInput` is an input projection, not a target
            // authority. Match Battle.attack's current physical hit-area
            // preconditions before publishing a map coordinate to the driver.
            val attacker = selected ?: return@takeIf false
            battle.outcome() == null && target.visible && attacker.visible && !attacker.hasActed &&
                attacker.effectiveFaction() == battle.activeFaction &&
                BattleStatus.PARALYSIS !in attacker.statuses && BattleStatus.CONFUSION !in attacker.statuses &&
                attacker.isPlayerSide() != target.isPlayerSide() &&
                (attacker.attackAllScreen ||
                    (target.tileX - attacker.tileX to target.tileY - attacker.tileY) in attacker.attackOffsets)
        }
        val manualAttack = livePhysicalAttackTarget?.takeUnless {
                (sourceScenario == "S_01" && selected?.characterId == 0 && !s01CaoCaoAttackSafe) ||
                s57Route.holdFire || (protectS57MineMaster && selected?.characterId == 0 &&
                    !s57CriticalFinisherActive)
        }?.let { target ->
            val command = ctx.projectWorldPoint(803.6f, 351.175f)
            val targetPoint = screenPoint(target.tileX, target.tileY)
            CampaignE2eAttackInput(
                command.first, command.second, targetPoint.first, targetPoint.second,
                target.id,
            )
        }
        // S57 alone receives this guided MagickList route. Keep it a
        // read-only projection over the live board: the driver will still
        // dispatch CommandLayer -> MagickListLayer -> map touches normally.
        val magicRows = selected?.magic.orEmpty()
            .filter { it.expendMp != 255 }
            .sortedBy { it.id }
        val guidedMagicPlan = selected?.let(::s57MagicPlanFor)
        fun magickRowScreenPoint(row: Int): Pair<Int, Int> {
            val worldX = if (row % 2 == 0) 611.686f else 874.686f
            val worldY = 574.5f - (row / 2) * 142f
            return ctx.projectWorldPoint(worldX, worldY)
        }
        val manualMagic = guidedMagicPlan?.let { plan ->
            val target = battle.units[plan.targetId] ?: return@let null
            val row = magicRows.indexOfFirst { it.id == plan.magicId }
            if (row < 0) return@let null
            val command = ctx.projectWorldPoint(931.6f, 351.175f)
            val rowPoint = magickRowScreenPoint(row.coerceAtLeast(0))
            val targetPoint = screenPoint(target.tileX, target.tileY)
            CampaignE2eMagicInput(
                command.first, command.second,
                rowPoint.first, rowPoint.second, targetPoint.first, targetPoint.second,
            )
        }

        val waitCommand = ctx.waitCommandScreenPoint
        val endRoundCommand = ctx.endRoundCommandScreenPoint
        val battleMenuButton = ctx.battleMenuButtonScreenPoint
        val autoBattleToggleButton = ctx.autoBattleToggleScreenPoint
        val autoBattleConfirmButton = ctx.autoBattleConfirmScreenPoint
        val loseTitleButton = ctx.loseTitleScreenPoint
        val s01EligibleMineActionRemaining = sourceScenario == "S_01" && battle.units.values.any { unit ->
            unit.visible && unit.type() == Faction.PLAYER && !unit.hasActed && productionManualUnitEligible(unit.statuses)
        }
        return CampaignE2eBattleState(
            scenario = sourceScenario,
            playback = ctx.scriptState,
            outcome = battle.outcome().takeIf { ctx.bootstrapPhase == BattleBootstrapPhase.COMPLETE },
            initialScene1Started = ctx.initialPlayerCampScriptStarted,
            resultScene1Started = ctx.resultScene1Observed || ctx.naturalOutcomeScriptStarted,
            scene2Started = ctx.postBattleSceneStarted,
            rewardOpen = ctx.rewardOpen,
            winConditionsOpen = ctx.winConditionsOpen,
            savePromptOpen = ctx.savePromptOpen,
            losePromptOpen = ctx.losePromptOpen,
            loseTitleScreenX = loseTitleButton.first,
            loseTitleScreenY = loseTitleButton.second,
            playerMoveCommitted = ctx.playerMoveCommitted,
            campaignStage = ctx.campaignStage,
            round = battle.round,
            activeFaction = battle.activeFaction,
            turnPhase = ctx.turnPhase,
            battleMenuOpen = ctx.battleMenuOpen,
            battleCommandOpen = ctx.battleCommandOpen,
            battleTargetSelectionOpen = ctx.battleTargetSelectionOpen,
            selectedUnit = ctx.selectedUnitId != null,
            manualMoveInput = manualMove,
            manualAttackInput = manualAttack,
            magickListOpen = ctx.magickListOpen,
            magicTargetSelection = ctx.magicMode,
            manualMagicInput = manualMagic,
            commandWaitScreenX = waitCommand.first,
            commandWaitScreenY = waitCommand.second,
            menuEndRoundScreenX = endRoundCommand.first,
            menuEndRoundScreenY = endRoundCommand.second,
            battleMenuButtonScreenX = battleMenuButton.first,
            battleMenuButtonScreenY = battleMenuButton.second,
            autoBattleToggleScreenX = autoBattleToggleButton.first,
            autoBattleToggleScreenY = autoBattleToggleButton.second,
            autoBattleConfirmScreenX = autoBattleConfirmButton.first,
            autoBattleConfirmScreenY = autoBattleConfirmButton.second,
            manualMoveDebug = battle.units.values.joinToString(";") {
                "${it.id}/${it.faction}/v=${it.visible}/a=${it.hasActed}/${it.tileX},${it.tileY}/r=${battle.reachableTiles(it.id).size}"
            },
            autoBattleOverlay = ctx.autoBattleOverlay,
            autoBattleChecked = ctx.autoBattleChecked,
            collocation = ctx.collocation,
            committedPlayerMove = ctx.committedPlayerMove,
            selectedChoice = ctx.selectedChoice,
            guidedAuthoredRoute = guidedAuthoredRoute,
            authoredRouteHoldFire = s57Route.holdFire,
            s01EligibleMineActionRemaining = s01EligibleMineActionRemaining,
        )
    }
}
