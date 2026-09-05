package com.jojo.game

internal data class CampaignE2eActionInputs(
    val manualAttack: CampaignE2eAttackInput?,
    val manualMagic: CampaignE2eMagicInput?,
)

internal class CampaignE2eBattleInputProjection {
    fun project(
        board: CampaignE2eBattlePlanningBoard,
        movePlan: CampaignE2eMovePlan,
    ): CampaignE2eActionInputs {
        val ctx = board.ctx
        val battle = board.battle
        val selected = board.selected
        val screenPoint = ctx.screenPoint
        val sourceScenario = board.scenario
        val visibleEnemies = board.visibleEnemies
        val s57Route = board.s57Route
        val protectS57MineMaster = board.protectS57MineMaster
        val s57FirstRoomFocusUnit = board.s57FirstRoomFocusUnit
        val occupiedTiles = board.occupiedTiles
        val s57CriticalFinisherActive = movePlan.s57CriticalFinisherActive
        val canAttack = board::canAttack
        val attackableFrom = board::attackableFrom
        val s57MagicPlanFor = board::guidedMagicPlanFor
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
                    .map { enemy ->
                        S57EscortFocusBlocker(
                            enemy.id,
                            enemy.tileX to enemy.tileY,
                            enemy.hitPoints,
                            enemy.retreatCount
                        )
                    }
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
            ?.let { source ->
                attackTarget?.let { target ->
                    s01CaoCaoSafeLeaderAttack(
                        source.hitPoints,
                        source.defense,
                        source.tileX to source.tileY,
                        target.characterId,
                        target.attack,
                        target.level,
                        target.tileX to target.tileY,
                    )
                }
            } ?: false
        val livePhysicalAttackTarget = attackTarget?.takeIf { target ->
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
        val magicRows = selected?.magic.orEmpty()
            .filter { it.expendMp != 255 }
            .sortedBy { it.id }
        val guidedMagicPlan = selected?.let { s57MagicPlanFor(it) }

        /**
         * 공개 메서드 `magickRowScreenPoint`
         *
         * ### 파라미터
        - `row` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Pair<Int, Int>`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

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
        return CampaignE2eActionInputs(manualAttack, manualMagic)
    }
}
