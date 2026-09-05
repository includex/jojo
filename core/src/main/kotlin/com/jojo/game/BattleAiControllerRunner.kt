package com.jojo.game
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleAiDecisionPlanner
import com.jojo.game.domain.battle.BattleAiScorer
import com.jojo.game.domain.battle.BattleMovementPlanner
import com.jojo.game.domain.battle.BattleAttributeCalculator

internal data class BattleAiControllerEnvironment(
    val units: () -> Collection<BattleUnit>,
    val unitAt: (Int, Int) -> BattleUnit?,
    val areAllied: (BattleUnit, BattleUnit) -> Boolean,
    val movementOffsets: Set<Pair<Int, Int>>,
    val terrain: BattleTerrainGrid?,
    val terrainResumeRates: Map<Int, Int>,
    val enemyMasterUnitId: String?,
    val reachableTiles: (String) -> Map<Pair<Int, Int>, Int>,
    val findMovementPath: (unit: BattleUnit, targetX: Int, targetY: Int, avoidEnemies: Boolean, penalizeEnemyTiles: Boolean, allowEnemyOnTarget: Boolean) -> List<Pair<Int, Int>>?,
    val findReachableEmptyPosition: (unit: BattleUnit, point: Pair<Int, Int>, capturedMovePoints: Set<Pair<Int, Int>>) -> Pair<Int, Int>?,
    val movePoints: (BattleUnit, Int) -> BattleMovementPlanner.MovePoints,
    val weather: () -> BattleWeather,
    val decisionEnv: BattleAiDecisionEnvironment,
)

internal data class AiControllerResult(
    val status: Int,
    val decision: AiDecision?,
    val sourcePoints: List<Control.Point>,
    val activeAi: Int,
)

/**
 * Executes the Cocos ControlManager driver for an AI unit, integrating ControlControllerFactory,
 * target tracking, pathfinding, and decision evaluation.
 */
internal object BattleAiControllerRunner {

    private val directDestinationOffsets: List<Pair<Int, Int>> = listOf(
        0 to 1, 1 to 0, -1 to 0, 0 to -1,
        0 to 2, 1 to 1, -1 to 1, 2 to 0, -2 to 0, 1 to -1, -1 to -1, 0 to -2,
        0 to 3, 1 to 2, -1 to 2, 2 to 1, -2 to 1, 3 to 0, -3 to 0,
        2 to -1, -2 to -1, 1 to -2, -1 to -2, 0 to -3,
        0 to 4, 1 to 3, -1 to 3, 2 to 2, -2 to 2, 3 to 1, -3 to 1,
        4 to 0, -4 to 0, 3 to -1, -3 to -1, 2 to -2, -2 to -2,
        1 to -3, -1 to -3, 0 to -4,
    )

    private fun distance(a: BattleUnit, b: BattleUnit): Int =
        kotlin.math.abs(a.tileX - b.tileX) + kotlin.math.abs(a.tileY - b.tileY)

    fun run(
        unit: BattleUnit,
        opponents: List<BattleUnit>,
        targetById: BattleUnit?,
        env: BattleAiControllerEnvironment,
    ): AiControllerResult {
        var selectedByControl: AiDecision? = null
        lateinit var controlManager: ControlManager
        controlManager = ControlManager(
            state = object : ControlManager.UnitState {
                override fun isControlled() = false
                override fun ai() = unit.ai
                override fun targetIndex() = unit.aiTargetCharacterId
                override fun targetX() = unit.aiTargetX
                override fun targetY() = unit.aiTargetY
                override fun targetExists(index: Int) = env.units().any { it.visible && it.characterId == index }
            },
            factory = object : ControlManager.Factory {
                override fun create(ai: Int): ControlManager.Driver = object : ControlManager.Driver {
                    private val controllerAi = ai
                    private val controller = ControlControllerFactory.create(ai)
                    private var data = ControlData()
                    override fun setManager(manager: ControlManager) = Unit
                    override fun setWithData(targetIndex: Int, x: Int, y: Int) {
                        data = ControlData(targetIndex, Control.Point(x, y))
                    }

                    override fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int {
                        val capturedMovePoints = pointHash.mapTo(linkedSetOf()) { it.x to it.y }
                        val context = object : BattleControlContext {
                            override fun currentPoint() = Control.Point(unit.tileX, unit.tileY)
                            override fun isParalyzed() = BattleStatus.PARALYSIS in unit.statuses
                            override fun isSurrounded() = env.movementOffsets.all { (dx, dy) ->
                                env.unitAt(
                                    unit.tileX + dx,
                                    unit.tileY + dy
                                ) != null
                            }

                            override fun isMine() = unit.isPlayerSide()
                            override fun setPersistentAi(ai: Int) {
                                unit.ai = ai
                            }

                            override fun target(index: Int) =
                                env.units().firstOrNull { it.visible && it.characterId == index }?.let {
                                    ControlTarget(
                                        index,
                                        Control.Point(it.tileX, it.tileY),
                                        it.isPlayerSide(),
                                        distance(unit, it)
                                    )
                                }

                            override fun hasAttackTargets(targetIndex: Int?): Boolean {
                                val candidates =
                                    if (targetIndex == null) opponents else opponents.filter { it.characterId == targetIndex }
                                return linkedSetOf(unit.tileX to unit.tileY).apply { addAll(env.reachableTiles(unit.id).keys) }
                                    .any { (x, y) -> candidates.any { BattleAiScorer.canAttackFrom(unit, x, y, it) } }
                            }

                            override fun exhaustedRetreat(): ControlTransition? {
                                val weakThreshold = unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
                                if (unit.hitPoints >= weakThreshold) return null
                                val resume = points.asSequence()
                                    .filter { point ->
                                        (env.terrainResumeRates[env.terrain?.terrainAt(point.x, point.y)] ?: 0) > 0
                                    }
                                    .filter { point -> env.unitAt(point.x, point.y)?.let { it.id == unit.id } != false }
                                    .maxByOrNull { point ->
                                        env.terrainResumeRates[env.terrain?.terrainAt(
                                            point.x,
                                            point.y
                                        )] ?: 0
                                    }
                                if (resume != null) return ControlTransition(
                                    ControlAi.MOVE_MAGIC,
                                    ControlData(-1, resume)
                                )
                                val master =
                                    env.enemyMasterUnitId?.let { id -> env.units().firstOrNull { it.id == id } }
                                        ?.takeIf { !unit.isPlayerSide() && it.visible && it.id != unit.id }
                                val friend = env.units().asSequence()
                                    .filter { it.visible && it.id != unit.id && env.areAllied(it, unit) }
                                    .minByOrNull { distance(unit, it) }
                                return (master ?: friend)?.let { target ->
                                    ControlTransition(
                                        ControlAi.RETREAT_TO,
                                        ControlData(-1, Control.Point(target.tileX, target.tileY))
                                    )
                                }
                            }

                            override fun nearestOpponent() = opponents.mapNotNull { opponent ->
                                env.findMovementPath(unit, opponent.tileX, opponent.tileY, false, false, false)
                                    ?.let { path -> opponent to path.size }
                            }.minByOrNull { it.second }?.first?.let {
                                ControlTarget(
                                    it.characterId ?: -1,
                                    Control.Point(it.tileX, it.tileY),
                                    it.isPlayerSide(),
                                    distance(unit, it)
                                )
                            }

                            override fun winRectCentre(): Control.Point? = null
                            override fun destinationPoint(target: Control.Point): Control.Point? {
                                val targetPoint = target.x to target.y
                                if (targetPoint !in capturedMovePoints) return null
                                return sequenceOf(targetPoint)
                                    .plus(directDestinationOffsets.asSequence().map { (dx, dy) ->
                                        target.x + dx to target.y + dy
                                    })
                                    .firstOrNull { point ->
                                        point in capturedMovePoints && env.unitAt(point.first, point.second) == null
                                    }
                                    ?.let { Control.Point(it.first, it.second) }
                            }

                            override fun nearPoint(target: Control.Point): Control.Point? {
                                val route =
                                    env.findMovementPath(unit, target.x, target.y, true, false, true) ?: return null
                                val lastReachableIndex = route.indexOfFirst { it !in capturedMovePoints }
                                    .let { if (it < 0) route.lastIndex else it - 1 }
                                for (index in lastReachableIndex downTo 1) {
                                    env.findReachableEmptyPosition(unit, route[index], capturedMovePoints)
                                        ?.let { point ->
                                            return Control.Point(point.first, point.second)
                                        }
                                }
                                return Control.Point(unit.tileX, unit.tileY)
                            }

                            override fun blockingEnemy(target: Control.Point): Int? {
                                val route =
                                    env.findMovementPath(unit, target.x, target.y, true, true, false) ?: return null
                                return route.asSequence()
                                    .mapNotNull { point -> env.unitAt(point.first, point.second) }
                                    .firstOrNull { occupant -> !env.areAllied(occupant, unit) }
                                    ?.characterId
                            }

                            override fun chooseAi(mode: Int): Control.Result? {
                                val controllerPoints = when (controllerAi) {
                                    ControlAi.HOLD -> listOf(Control.Point(unit.tileX, unit.tileY))
                                    ControlAi.MOVE_ATTACK, ControlAi.MOVE_MAGIC, ControlAi.MOVE_ATTACK_UNIT -> listOf(
                                        data.target
                                    )

                                    else -> points
                                }
                                selectedByControl = BattleAiDecisionPlanner.chooseAiDecision(
                                    unit = unit,
                                    opponents = opponents,
                                    designated = targetById,
                                    aiMode = controllerAi,
                                    aiFlags = mode,
                                    candidatePoints = controllerPoints.map { it.x to it.y },
                                    env = env.decisionEnv,
                                )
                                return selectedByControl?.let { choice ->
                                    Control.Result(
                                        choice.x,
                                        choice.y,
                                        kind = if (choice.magicId == null) "attack" else "magic",
                                        value = choice.value
                                    )
                                }
                            }
                        }
                        val step = controller.step(context, data)
                        step.transition?.let { transition ->
                            controlManager.setControl(
                                transition.ai,
                                transition.data.targetIndex,
                                transition.data.target.x,
                                transition.data.target.y
                            )
                        }
                        step.result?.let(controlManager::setResult)
                        return step.status
                    }
                }
            },
        )
        val moveArea = env.movePoints(unit, BattleAttributeCalculator.finalMovement(unit, env.weather()))
        val sourcePoints = moveArea.points.keys.map { (x, y) -> Control.Point(x, y) }
        val sourceHash = sourcePoints.toCollection(linkedSetOf())
        val controlStatus = controlManager.selectMovePoint(sourcePoints, sourceHash)
        return AiControllerResult(controlStatus, selectedByControl, sourcePoints, controlManager.activeAi ?: unit.ai)
    }
}
