package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleInteractiveInputTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleInteractiveInputTest {
    @Test fun `S01 survival move maximizes enemy separation then keeps the right rear ally line`() {
        assertEquals(
            7 to 5,
            s01SurvivalDestination(
                current = 5 to 5,
                reachableLegalTiles = listOf(5 to 5, 3 to 5, 7 to 5, 6 to 6),
                visibleEnemyTiles = listOf(4 to 5),
                alliedTiles = listOf(5 to 5, 8 to 5),
            ),
        )
    }

    @Test fun `S01 withdrawal choice returns to and confirms first authored row`() {
        assertEquals(S01WithdrawalChoiceAction.PREVIOUS, s01WithdrawalChoiceAction(2))
        assertEquals(S01WithdrawalChoiceAction.CONFIRM, s01WithdrawalChoiceAction(0))
    }

    @Test fun `dialogue and choice keep ownership of battle input`() {
        assertEquals(
            BattleInteractiveInput.Route.DIALOGUE,
            BattleInteractiveInput.route(PlaybackState.DIALOGUE, BattleTurnController.Phase.PLAYER_INPUT),
        )
        assertEquals(
            BattleInteractiveInput.Route.CHOICE,
            BattleInteractiveInput.route(PlaybackState.CHOICE, BattleTurnController.Phase.PLAYER_INPUT),
        )
        assertEquals(
            BattleInteractiveInput.Route.DIALOGUE,
            BattleInteractiveInput.route(PlaybackState.DIALOGUE, BattleTurnController.Phase.FINISHED),
        )
    }

    @Test fun `delay modal and non-player phases cannot leak into tactical input`() {
        assertEquals(BattleInteractiveInput.Route.SCRIPT_PAUSED, BattleInteractiveInput.route(PlaybackState.DELAY, BattleTurnController.Phase.PLAYER_INPUT))
        assertEquals(BattleInteractiveInput.Route.SCRIPT_PAUSED, BattleInteractiveInput.route(PlaybackState.MODAL, BattleTurnController.Phase.PLAYER_INPUT))
        assertEquals(BattleInteractiveInput.Route.TURN_PAUSED, BattleInteractiveInput.route(PlaybackState.COMPLETE, BattleTurnController.Phase.BOOTSTRAP))
        assertEquals(BattleInteractiveInput.Route.TURN_PAUSED, BattleInteractiveInput.route(PlaybackState.COMPLETE, BattleTurnController.Phase.CAMP_SCRIPT))
        assertEquals(BattleInteractiveInput.Route.TURN_PAUSED, BattleInteractiveInput.route(PlaybackState.COMPLETE, BattleTurnController.Phase.AI))
    }

    @Test fun `completed script at player phase reaches tactical input`() {
        assertEquals(BattleInteractiveInput.Route.PLAYER_INPUT, BattleInteractiveInput.route(PlaybackState.COMPLETE, BattleTurnController.Phase.PLAYER_INPUT))
        val trace = BattleInteractiveInput.trace()
        println(trace)
        assertTrue(trace.contains("\"script\":\"DIALOGUE\""))
        assertTrue(trace.contains("\"route\":\"PLAYER_INPUT\""))
        assertTrue(trace.lineSequence().first { it.contains("\"script\":\"DIALOGUE\"") }.contains("\"paused\":true"))
        assertFalse(trace.lineSequence().filter { it.contains("PLAYER_INPUT") && it.contains("\"script\":\"COMPLETE\"") }.any { it.contains("\"paused\":true") })
    }

    @Test fun `production planner skips units whose manual actions are rejected by status`() {
        assertTrue(productionManualUnitEligible(emptyMap()))
        assertFalse(productionManualUnitEligible(mapOf(BattleStatus.PARALYSIS to 1)))
        assertFalse(productionManualUnitEligible(mapOf(BattleStatus.CONFUSION to 2)))
        assertTrue(productionManualUnitEligible(mapOf(BattleStatus.POISON to 3)))
    }

    @Test fun `S01 leader focus uses lowest HP and recovered tie order before guard fallback`() {
        val targets = listOf(
            S01EnemyTarget("guard", 200, 1),
            S01EnemyTarget("leader134", 134, 40),
            S01EnemyTarget("leader129", 129, 25),
            S01EnemyTarget("leader131", 131, 25),
        )
        assertEquals("leader131", s01PreferredAttackTargets(targets, visibleEnemyCount = 12).first().unitId)
        assertEquals(emptyList(), s01PreferredAttackTargets(listOf(S01EnemyTarget("guard", 200, 1)), visibleEnemyCount = 8))
        assertEquals("guard", s01PreferredAttackTargets(listOf(S01EnemyTarget("guard", 200, 1)), visibleEnemyCount = 9).first().unitId)
    }

    @Test fun `S01 Cao Cao only attacks a cardinal leader when conservative counter is survivable`() {
        assertTrue(s01CaoCaoSafeLeaderAttack(100, 30, 5 to 5, 131, 60, 5, 5 to 6))
        assertFalse(s01CaoCaoSafeLeaderAttack(80, 30, 5 to 5, 131, 120, 7, 5 to 6))
        assertFalse(s01CaoCaoSafeLeaderAttack(100, 30, 5 to 5, 200, 20, 1, 5 to 6))
        assertFalse(s01CaoCaoSafeLeaderAttack(100, 30, 5 to 5, 131, 20, 1, 6 to 6))
    }

    @Test fun `S01 cannot end round while any eligible Mine action remains`() {
        assertFalse(productionEndRoundAllowed("S_01", s01EligibleMineActionRemaining = true))
        assertTrue(productionEndRoundAllowed("S_01", s01EligibleMineActionRemaining = false))
        assertTrue(productionEndRoundAllowed("S_02", s01EligibleMineActionRemaining = true))
    }

    @Test fun `S01 manual Mine route bypasses standalone zero-move quota`() {
        assertTrue(
            productionManualMoveAllowed(
                scenario = "S_01", guidedAuthoredRoute = false, playerMoveCommitted = true,
                manualMoveAttempts = 0, manualMoveAttemptLimit = 0,
            ),
        )
        assertFalse(
            productionManualMoveAllowed(
                scenario = "S_02", guidedAuthoredRoute = false, playerMoveCommitted = true,
                manualMoveAttempts = 0, manualMoveAttemptLimit = 0,
            ),
        )
        assertTrue(
            productionManualMoveAllowed(
                scenario = "S_52", guidedAuthoredRoute = true, playerMoveCommitted = true,
                manualMoveAttempts = 10, manualMoveAttemptLimit = 0,
            ),
        )
    }

    @Test fun `production trace driver waits for initial startOper completion`() {
        assertFalse(productionTacticalInputReady(false, PlaybackState.COMPLETE, BattleTurnController.Phase.PLAYER_INPUT))
        assertFalse(productionTacticalInputReady(true, PlaybackState.DIALOGUE, BattleTurnController.Phase.BOOTSTRAP))
        assertFalse(productionTacticalInputReady(true, PlaybackState.COMPLETE, BattleTurnController.Phase.BOOTSTRAP))
        assertTrue(productionTacticalInputReady(true, PlaybackState.COMPLETE, BattleTurnController.Phase.PLAYER_INPUT))
    }

    @Test fun `guided authored route confirms end round prompt without entrusted control`() {
        assertEquals(
            ProductionAutoBattlePromptAction.CONFIRM,
            productionAutoBattlePromptAction(guidedAuthoredRoute = true, checked = false),
        )
        assertEquals(
            ProductionAutoBattlePromptAction.TOGGLE,
            productionAutoBattlePromptAction(guidedAuthoredRoute = true, checked = true),
        )
    }

    @Test fun `generic production route checks entrusted toggle before confirming`() {
        assertEquals(
            ProductionAutoBattlePromptAction.TOGGLE,
            productionAutoBattlePromptAction(guidedAuthoredRoute = false, checked = false),
        )
        assertEquals(
            ProductionAutoBattlePromptAction.CONFIRM,
            productionAutoBattlePromptAction(guidedAuthoredRoute = false, checked = true),
        )
    }

    @Test fun `S01 end round confirms unchecked prompt without entering entrusted control`() {
        assertEquals(
            ProductionAutoBattlePromptAction.CONFIRM,
            productionAutoBattlePromptActionForScenario("S_01", guidedAuthoredRoute = false, checked = false),
        )
        assertEquals(
            ProductionAutoBattlePromptAction.TOGGLE,
            productionAutoBattlePromptActionForScenario("S_01", guidedAuthoredRoute = false, checked = true),
        )
        assertEquals(
            ProductionAutoBattlePromptAction.TOGGLE,
            productionAutoBattlePromptActionForScenario("S_00", guidedAuthoredRoute = false, checked = false),
        )
        assertEquals(
            ProductionAutoBattlePromptAction.CONFIRM,
            productionAutoBattlePromptActionForScenario("S_00", guidedAuthoredRoute = false, checked = true),
        )
    }

    @Test fun `R01 to S01 preparation fills all seven authored slots before normal confirmation`() {
        assertEquals(
            CampaignBattlePreparationAction.NEXT_UNIT,
            campaignBattlePreparationAction("R_01", "S_01", selectedCount = 4, maximum = 7, cursorSelected = true, canStart = true),
        )
        assertEquals(
            CampaignBattlePreparationAction.TOGGLE_UNIT,
            campaignBattlePreparationAction("R_01", "S_01", selectedCount = 6, maximum = 7, cursorSelected = false, canStart = true),
        )
        assertEquals(
            CampaignBattlePreparationAction.START,
            campaignBattlePreparationAction("R_01", "S_01", selectedCount = 7, maximum = 7, cursorSelected = true, canStart = true),
        )
        assertEquals(
            CampaignBattlePreparationAction.START,
            campaignBattlePreparationAction("R_02", "S_02", selectedCount = 4, maximum = 7, cursorSelected = true, canStart = true),
        )
    }

    @Test fun `S52 production route advances only after entering each authored trigger rectangle`() {
        val route = AuthoredMechanicRouteTracker("S_52")
        assertEquals(4 to 10, route.target(listOf(4 to 20)))
        assertEquals(9 to 15, route.target(listOf(3 to 9)))
        assertEquals(9 to 5, route.target(listOf(11 to 17)))
        assertEquals(14 to 10, route.target(listOf(8 to 4)))
        assertEquals(14 to 15, route.target(listOf(16 to 12)))
        assertEquals(14 to 1, route.target(listOf(13 to 14)))
        assertEquals(14 to 1, route.target(listOf(12 to 2)))
        assertEquals(6, route.completedWaypoints())
    }

    @Test fun `non puzzle scenarios do not receive synthetic route targets`() {
        assertEquals(null, AuthoredMechanicRouteTracker("S_57").target(listOf(20 to 20)))
    }

    @Test fun `production destination excludes occupied movement overlay cells`() {
        // Source canMovePoints exposes allied occupied cells for highlighting,
        // while unitMove rejects them. The driver must plan from the latter.
        assertEquals(
            listOf(3 to 20, 3 to 19),
            executableProductionMoveTiles(
                current = 3 to 20,
                reachable = listOf(4 to 15, 3 to 19, 4 to 16),
                occupied = setOf(3 to 20, 4 to 15, 4 to 16),
            ),
        )
    }

    @Test fun `S57 gate uses the source predicate rectangle rather than blocked point 16 19`() {
        // v15: (16,19) and (16,20) are map objects; a legal inside tile
        // still wins over the old point-distance local minimum at (17,19).
        assertEquals(
            16 to 18,
            s57GateDestination(
                current = 17 to 19,
                reachableLegalTiles = listOf(17 to 19, 17 to 18, 16 to 18, 17 to 20),
            ),
        )
        // When no inside tile is reachable, exclude current so the route
        // makes deterministic progress toward the rectangle.
        assertEquals(
            17 to 18,
            s57GateDestination(
                current = 17 to 19,
                reachableLegalTiles = listOf(17 to 19, 17 to 20, 17 to 18),
            ),
        )
        assertEquals(17 to 19, s57GateDestination(17 to 19, listOf(17 to 19)))
    }

    @Test fun `S57 first-room escorts focus lowest HP leader with recovered tie order`() {
        assertEquals(
            "leader169",
            s57FirstRoomEscortFocus(listOf(
                S57FirstRoomLeader("leader165", 165, 28, 4 to 4),
                S57FirstRoomLeader("leader162", 162, 21, 5 to 4),
                S57FirstRoomLeader("leader169", 169, 12, 6 to 4),
            ))?.unitId,
        )
        assertEquals(
            "leader162",
            s57FirstRoomEscortFocus(listOf(
                S57FirstRoomLeader("leader165", 165, 12, 4 to 4),
                S57FirstRoomLeader("leader169", 169, 12, 6 to 4),
                S57FirstRoomLeader("leader162", 162, 12, 5 to 4),
            ))?.unitId,
        )
        assertEquals(
            "leader169",
            s57FirstRoomEscortFocus(listOf(
                S57FirstRoomLeader("leader165", 165, 12, 4 to 4),
                S57FirstRoomLeader("leader169", 169, 12, 6 to 4),
            ))?.unitId,
        )
    }

    @Test fun `S57 escort moves or attacks only its focused leader`() {
        // Destination (2,0) is the only legal tile with the focused leader
        // in the unit's attack offsets; guard positions never enter this policy.
        assertEquals(
            2 to 0,
            s57EscortFocusDestination(
                current = 0 to 0,
                reachableLegalTiles = listOf(0 to 0, 1 to 0, 2 to 0),
                focusTile = 3 to 0,
                attackAllScreen = false,
                attackOffsets = setOf(1 to 0),
            ),
        )
        assertEquals(
            1 to 0,
            s57EscortFocusDestination(
                current = 0 to 0,
                reachableLegalTiles = listOf(0 to 0, 1 to 0),
                focusTile = 4 to 0,
                attackAllScreen = false,
                attackOffsets = setOf(1 to 0),
            ),
        )
        assertEquals(
            0 to 0,
            s57EscortFocusDestination(0 to 0, listOf(0 to 0), 4 to 0, false, setOf(1 to 0)),
        )
        assertEquals(
            0 to 0,
            s57EscortFocusDestination(0 to 0, listOf(0 to 0, 1 to 0), 4 to 0, true, emptySet()),
        )
    }

    @Test fun `S57 blocker fallback preserves leader priority and excludes unrelated guards`() {
        val guard = S57EscortFocusBlocker("guard", 2 to 0, 40)
        val leaderPriorityGuard = S57EscortFocusBlocker("leader-priority-guard", 1 to 0, 20)
        val unrelated = S57EscortFocusBlocker("unrelated", 1 to 1, 1)
        // A legal leader-staging tile exists, so even a closer attackable guard
        // cannot displace the focused attack.
        assertEquals(
            null,
            s57EscortFocusBlockerFallback(
                current = 0 to 0, reachableLegalTiles = listOf(0 to 0, 2 to 0), focusTile = 3 to 0,
                attackAllScreen = false, attackOffsets = setOf(1 to 0),
                occupiedTiles = setOf(1 to 0), guards = listOf(leaderPriorityGuard),
                openedStagingReachableByGuard = emptyMap(),
            ),
        )
        // Guard (2,0) occupies the only next cardinal progress step from the
        // legal frontier (1,0) to leader staging (3,0).
        assertEquals(
            S57EscortFocusBlockerFallback(guard, 1 to 0),
            s57EscortFocusBlockerFallback(
                current = 0 to 0, reachableLegalTiles = listOf(0 to 0, 1 to 0), focusTile = 4 to 0,
                attackAllScreen = false, attackOffsets = setOf(1 to 0),
                occupiedTiles = setOf(2 to 0, 1 to 1), guards = listOf(guard, unrelated),
                openedStagingReachableByGuard = mapOf("guard" to listOf(3 to 0)),
            ),
        )
        // This guard can be attacked but does not reduce the path distance to
        // any focus staging tile, so it remains excluded from the S57 route.
        assertEquals(
            null,
            s57EscortFocusBlockerFallback(
                current = 0 to 0, reachableLegalTiles = listOf(0 to 0), focusTile = 4 to 0,
                attackAllScreen = false, attackOffsets = setOf(1 to 1),
                occupiedTiles = setOf(1 to 1), guards = listOf(unrelated),
                openedStagingReachableByGuard = mapOf("unrelated" to listOf(3 to 3)),
            ),
        )
    }

    @Test fun `S57 first-room ranks leader hit then progress then immediate blocker`() {
        assertEquals(0, s57FirstRoomActionRank(leaderHit = true, focusProgress = true, blockerHit = true))
        assertEquals(1, s57FirstRoomActionRank(leaderHit = false, focusProgress = true, blockerHit = true))
        assertEquals(2, s57FirstRoomActionRank(leaderHit = false, focusProgress = false, blockerHit = true))
        assertEquals(3, s57FirstRoomActionRank(leaderHit = false, focusProgress = false, blockerHit = false))
    }

    @Test fun `S57 fallback chooses a new immediate frontier guard before a shown former casualty`() {
        val revived = S57EscortFocusBlocker("revived", 2 to 0, 10, retreatCount = 1)
        val fresh = S57EscortFocusBlocker("fresh", 2 to 1, 90)
        assertEquals(
            fresh,
            s57EscortFocusBlockerFallback(
                current = 0 to 0, reachableLegalTiles = listOf(0 to 0, 1 to 0, 1 to 1), focusTile = 4 to 0,
                attackAllScreen = false, attackOffsets = setOf(1 to 0, 1 to -1),
                occupiedTiles = setOf(2 to 0, 2 to 1), guards = listOf(revived, fresh),
                openedStagingReachableByGuard = mapOf("revived" to listOf(3 to 0), "fresh" to listOf(3 to 1)),
            )?.blocker,
        )
    }

    @Test fun `S57 blocking guard fallback yields an Attack UI tile instead of WAIT`() {
        val guard = S57EscortFocusBlocker("guard", 2 to 0, 40)
        val fallback = s57EscortFocusBlockerFallback(
            // This is the post-move observation: the selected escort is now
            // at the returned attackFrom tile and can issue CommandLayer Attack.
            current = 1 to 0, reachableLegalTiles = listOf(1 to 0), focusTile = 4 to 0,
            attackAllScreen = false, attackOffsets = setOf(1 to 0),
            occupiedTiles = setOf(1 to 0, 2 to 0), guards = listOf(guard),
            openedStagingReachableByGuard = mapOf("guard" to listOf(3 to 0)),
        )
        assertEquals(guard, fallback?.blocker)
        assertEquals(1 to 0, fallback?.attackFrom)
    }

    @Test fun `S57 blocker is excluded unless its removal opens a real focus staging tile`() {
        val guard = S57EscortFocusBlocker("guard", 2 to 0, 40)
        // It is an attackable Manhattan-frontier guard, but the live
        // counterfactual flood-fill still cannot enter (3,0), the only
        // physical staging tile for leader (4,0).
        assertEquals(
            null,
            s57EscortFocusBlockerFallback(
                current = 0 to 0, reachableLegalTiles = listOf(0 to 0, 1 to 0), focusTile = 4 to 0,
                attackAllScreen = false, attackOffsets = setOf(1 to 0),
                occupiedTiles = setOf(2 to 0), guards = listOf(guard),
                openedStagingReachableByGuard = mapOf("guard" to listOf(1 to 1)),
            ),
        )
    }

    @Test fun `S57 blocker keeps its actual attack tile while route evidence may span next move`() {
        val guard = S57EscortFocusBlocker("guard", 2 to 0, 40)
        assertEquals(
            1 to 0,
            s57EscortAttackFrom(
                current = 0 to 0,
                reachableLegalTiles = listOf(0 to 0, 1 to 0, 1 to 1),
                guardTile = guard.tile,
                attackAllScreen = false,
                attackOffsets = setOf(1 to 0),
            ),
        )
        // The map value denotes the bounded post-kill route probe. It is
        // deliberately sufficient to enable this immediate real Attack UI
        // action even though staging is reached on the following move.
        assertEquals(
            guard,
            s57EscortFocusBlockerFallback(
                current = 0 to 0, reachableLegalTiles = listOf(0 to 0, 1 to 0), focusTile = 4 to 0,
                attackAllScreen = false, attackOffsets = setOf(1 to 0),
                occupiedTiles = setOf(2 to 0), guards = listOf(guard),
                openedStagingReachableByGuard = mapOf("guard" to setOf(3 to 0)),
            )?.blocker,
        )
    }

    @Test fun `S57 critical finisher releases Cao Cao only for a reachable one-hit leader attack`() {
        assertTrue(
            s57FirstRoomCriticalFinisherActive(
                focusedLeaderHitPoints = 9,
                expectedSourcePhysicalDamage = 28,
                sourceCanReachLeaderAttackTile = true,
            ),
        )
        assertFalse(
            s57FirstRoomCriticalFinisherActive(
                focusedLeaderHitPoints = 29,
                expectedSourcePhysicalDamage = 28,
                sourceCanReachLeaderAttackTile = true,
            ),
        )
        assertFalse(
            s57FirstRoomCriticalFinisherActive(
                focusedLeaderHitPoints = 9,
                expectedSourcePhysicalDamage = 28,
                sourceCanReachLeaderAttackTile = false,
            ),
        )
        assertFalse(
            s57FirstRoomCriticalFinisherActive(
                focusedLeaderHitPoints = null,
                expectedSourcePhysicalDamage = 28,
                sourceCanReachLeaderAttackTile = true,
            ),
        )
    }

    @Test fun `S57 critical finisher chooses only a legal leader attack tile`() {
        assertEquals(
            3 to 2,
            s57CriticalFinisherDestination(
                current = 0 to 0,
                reachableLegalTiles = listOf(0 to 0, 1 to 0, 3 to 2, 2 to 2),
                focusTile = 4 to 2,
                attackAllScreen = false,
                attackOffsets = setOf(1 to 0),
            ),
        )
        assertEquals(
            null,
            s57CriticalFinisherDestination(
                current = 0 to 0,
                reachableLegalTiles = listOf(0 to 0, 1 to 0),
                focusTile = 4 to 2,
                attackAllScreen = false,
                attackOffsets = setOf(1 to 0),
            ),
        )
    }

    @Test fun `open attack selector only clicks a live identified hit-area target`() {
        val stale = CampaignE2eAttackInput(1, 2, 100, 200, "old-target")
        val live = CampaignE2eAttackInput(1, 2, 300, 400, "live-target")
        assertEquals(live, productionLiveAttackInput(stale, live))
        assertEquals(null, productionLiveAttackInput(stale, null))
        assertEquals(null, productionLiveAttackInput(stale, CampaignE2eAttackInput(1, 2, 300, 400)))
    }

    @Test fun `S57 guided route waits for real attrition only after second room is revealed`() {
        assertFalse(waitForS57AuthoredAttrition("S_57", 15, listOf(165, 162, 169)))
        assertTrue(waitForS57AuthoredAttrition("S_57", 15, listOf(166, 438, 439)))
        assertFalse(waitForS57AuthoredAttrition("S_57", 1, listOf(166, 167, 168)))
        assertFalse(waitForS57AuthoredAttrition("S_52", 12, listOf(166, 167, 168)))
    }

    @Test fun `S57 driver targets room leaders then protects mine master in trap through real waits`() {
        val firstRoomWithEscorts = s57AuthoredRouteSignal(
            "S_57", listOf(165, 162, 414), mineMasterInSecondRoom = false, visiblePlayerCount = 15,
        )
        assertEquals(
            setOf(165, 162),
            firstRoomWithEscorts.combatTargetIds,
        )
        assertEquals(null, firstRoomWithEscorts.gateTarget, "escorts present keeps source 0 on the protected retreat route")
        val lastMineAtFirstRoom = s57AuthoredRouteSignal(
            "S_57", listOf(165), mineMasterInSecondRoom = false, visiblePlayerCount = 1,
        )
        assertEquals(null, lastMineAtFirstRoom.gateTarget)
        assertFalse(lastMineAtFirstRoom.holdFire, "first-room leaders still own the route even when source 0 is alone")

        val gate = s57AuthoredRouteSignal("S_57", listOf(166, 167, 168), mineMasterInSecondRoom = false, visiblePlayerCount = 8)
        assertEquals(16 to 19, gate.gateTarget)
        assertTrue(gate.holdFire, "driver must issue CommandLayer WAIT while Cao Cao walks to the source rectangle")

        val attrition = s57AuthoredRouteSignal("S_57", listOf(166, 167, 168), mineMasterInSecondRoom = true, visiblePlayerCount = 8)
        assertTrue(attrition.waitForAttrition)
        assertTrue(attrition.holdFire)
        assertFalse(s57AuthoredRouteSignal("S_57", listOf(166), mineMasterInSecondRoom = true, visiblePlayerCount = 1).waitForAttrition)
    }

    @Test fun `S57 guided driver only considers Whirlwind after first room leaders clear`() {
        val whirlwind = CampaignE2eMagicOption(
            id = 10, target = 0, cost = 6, power = 50, category = 0,
            allScreen = false, offsets = setOf(1 to 0),
        )
        assertEquals(
            CampaignE2eGuidedMagicPlan(10, "adjacent"),
            s57GuidedOffensiveMagicPlan(
                scenario = "S_57", guidedAuthoredRoute = true, holdFire = false, firstRoomLeaderVisible = false,
                casterCharacterId = 0, casterX = 4, casterY = 4, magicPoints = 36,
                options = listOf(whirlwind),
                visibleEnemies = listOf(CampaignE2eMagicTarget("adjacent", 5, 4)),
            ),
        )
        assertEquals(null, s57GuidedOffensiveMagicPlan(
            scenario = "S_57", guidedAuthoredRoute = true, holdFire = false, firstRoomLeaderVisible = false,
            casterCharacterId = 0, casterX = 4, casterY = 4, magicPoints = 36,
            options = listOf(whirlwind),
            visibleEnemies = listOf(CampaignE2eMagicTarget("out-of-range", 7, 4)),
        ))
        assertEquals(null, s57GuidedOffensiveMagicPlan(
            scenario = "S_57", guidedAuthoredRoute = true, holdFire = false, firstRoomLeaderVisible = true,
            casterCharacterId = 0, casterX = 4, casterY = 4, magicPoints = 36,
            options = listOf(whirlwind),
            visibleEnemies = listOf(CampaignE2eMagicTarget("leader-present", 5, 4)),
        ))
    }

    @Test fun `guided Whirlwind remains limited to S57 Cao Cao and combat windows`() {
        val whirlwind = CampaignE2eMagicOption(10, 0, 6, 50, 0, true, emptySet())
        val otherMagic = CampaignE2eMagicOption(11, 0, 6, 50, 0, true, emptySet())
        val enemy = CampaignE2eMagicTarget("enemy", 9, 9)
        assertEquals(null, s57GuidedOffensiveMagicPlan(
            "S_57", guidedAuthoredRoute = true, holdFire = true, firstRoomLeaderVisible = false,
            casterCharacterId = 0, casterX = 0, casterY = 0, magicPoints = 36,
            options = listOf(whirlwind), visibleEnemies = listOf(enemy),
        ))
        assertEquals(null, s57GuidedOffensiveMagicPlan(
            "S_57", guidedAuthoredRoute = true, holdFire = false, firstRoomLeaderVisible = false,
            casterCharacterId = 1, casterX = 0, casterY = 0, magicPoints = 36,
            options = listOf(whirlwind), visibleEnemies = listOf(enemy),
        ))
        assertEquals(null, s57GuidedOffensiveMagicPlan(
            "S_52", guidedAuthoredRoute = true, holdFire = false, firstRoomLeaderVisible = false,
            casterCharacterId = 0, casterX = 0, casterY = 0, magicPoints = 36,
            options = listOf(whirlwind), visibleEnemies = listOf(enemy),
        ))
        assertEquals(null, s57GuidedOffensiveMagicPlan(
            "S_57", guidedAuthoredRoute = true, holdFire = false, firstRoomLeaderVisible = false,
            casterCharacterId = 0, casterX = 0, casterY = 0, magicPoints = 5,
            options = listOf(whirlwind), visibleEnemies = listOf(enemy),
        ))
        assertEquals(null, s57GuidedOffensiveMagicPlan(
            "S_57", guidedAuthoredRoute = true, holdFire = false, firstRoomLeaderVisible = false,
            casterCharacterId = 0, casterX = 0, casterY = 0, magicPoints = 36,
            options = listOf(otherMagic), visibleEnemies = listOf(enemy),
        ))
    }
}
