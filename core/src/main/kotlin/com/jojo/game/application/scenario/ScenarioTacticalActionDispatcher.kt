// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.application.scenario.*

import com.jojo.game.domain.scenario.*

import com.jojo.game.presentation.battle.timeline.BattlePhysicalPresentationTimeline

import com.badlogic.gdx.utils.JsonValue

internal data class ScenarioTacticalEnvironment(
    val stage: ScenarioStage,
    val battleContext: ScenarioBattleScriptContext,
    val externalBattlePresentation: Boolean,
    val suspendFor: (Float) -> Unit,
    val resolveStageUnitReference: (Int, Int) -> ScenarioUnitReference?,
    val unitReference: (JsonValue, Frame) -> ScenarioUnitReference?,
    val headReference: (JsonValue, Frame) -> HeadReference?,
)

internal data class ScenarioHandledCall(val value: Any?)

/** ScenarioTacticalActionDispatcher: 이동·공격·지형물 등 전술 스크립트 호출을 ScenarioStage 변경으로 변환한다. */
internal object ScenarioTacticalActionDispatcher {

    fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioTacticalEnvironment,
    ): ScenarioHandledCall? {
        when (path) {
            "stage.nearEvent" -> {
                env.stage.addNearEvent(args.firstOrNull().asList(), args.getOrNull(1).asInt())
                return ScenarioHandledCall(null)
            }

            "stage.center" -> {
                env.stage.requestCameraCenter(args.intAt(0), args.intAt(1))
                return ScenarioHandledCall(null)
            }

            "stage.setEnemyEquip" -> {
                env.stage.setEnemyEquipment(args.firstOrNull().asInt(), args.drop(1))
                return ScenarioHandledCall(null)
            }

            "stage.unitAttr" -> return ScenarioHandledCall(env.stage.unitAttribute(args.intAt(0), args.intAt(1)))
            "stage.setUnitAttr" -> {
                env.stage.setUnitAttribute(args.intAt(0), args.intAt(1), args.intAt(2))
                return ScenarioHandledCall(null)
            }

            "stage.setUnitAbility" -> {
                env.stage.changeUnitAttribute(args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3))
                return ScenarioHandledCall(null)
            }

            "stage.setFAvatar" -> {
                env.stage.setUnitAttribute(args.intAt(0), 27, args.intAt(1))
                return ScenarioHandledCall(null)
            }

            "stage.setUnitStatus" -> {
                val values = env.stage.setUnitStatuses(args.firstOrNull().asList())
                val presents = values.any { change ->
                    val hp = (change["hp"] as? Number)?.toInt() ?: 0
                    val mp = (change["mp"] as? Number)?.toInt() ?: 0
                    val status = (change["status"] as? Number)?.toInt() ?: -1
                    val hiddenStatuses = change["hStatus"].asList()
                    (hp != 0 && kotlin.math.abs(hp) != 255) ||
                            (mp != 0 && kotlin.math.abs(mp) != 255) ||
                            status != -1 || hiddenStatuses.isNotEmpty()
                }
                if (env.externalBattlePresentation && presents) {
                    env.stage.requestScriptPresentation(ScenarioScriptPresentationRequest.UnitStatusSettlement(values))
                    env.suspendFor(Float.MAX_VALUE)
                }
                return ScenarioHandledCall(null)
            }

            "stage.setFire" -> {
                val enabled = args.firstOrNull().asBooleanValue()
                val x = args.intAt(1)
                val y = args.intAt(2)
                env.stage.setFire(enabled, x, y)
                if (env.externalBattlePresentation && enabled && env.stage.battleDrawRequested) {
                    env.stage.requestMapPresentation(ScenarioMapPresentationRequest(x, y, 1f))
                    env.suspendFor(Float.MAX_VALUE)
                }
                return ScenarioHandledCall(null)
            }

            "stage.setFires" -> {
                val enabled = args.firstOrNull().asBooleanValue()
                val positions = args.getOrNull(1).asList()
                env.stage.setFires(enabled, positions)
                val last = positions.lastOrNull().asList()
                if (env.externalBattlePresentation && enabled && env.stage.battleDrawRequested && last.size >= 2) {
                    env.stage.requestMapPresentation(
                        ScenarioMapPresentationRequest(
                            last[0].asInt(),
                            last[1].asInt(),
                            1f
                        )
                    )
                    env.suspendFor(Float.MAX_VALUE)
                }
                return ScenarioHandledCall(null)
            }

            "stage.playMagicMeff" -> {
                val x = args.intAt(0)
                val y = args.intAt(1)
                val raw = args.intAt(2)
                if (env.externalBattlePresentation) {
                    val magicCallId = raw.takeIf { it >= 100 && it != 255 }?.minus(100)
                    env.stage.requestMapPresentation(
                        ScenarioMapPresentationRequest(
                            x,
                            y,
                            if (magicCallId != null) 1.25f else 1f,
                            magicCallId
                        )
                    )
                    env.suspendFor(Float.MAX_VALUE)
                }
                return ScenarioHandledCall(null)
            }

            "stage.attackAction" -> {
                val flags = args.intAt(2)
                env.stage.attackAction(args.intAt(0), args.intAt(1), flags)
                env.suspendFor(BattlePhysicalPresentationTimeline.scriptedAttackDuration(flags))
                return ScenarioHandledCall(null)
            }

            "stage.setObjects" -> {
                val enabled = args.firstOrNull().asBooleanValue()
                val terrain = args.getOrNull(1).asInt()
                val positions = args.getOrNull(2).asList()
                env.stage.setMapObjects(enabled, terrain, positions)
                if (enqueueMapObjectsPresentation(
                        env.stage,
                        env.externalBattlePresentation,
                        enabled,
                        terrain,
                        positions,
                        true
                    )
                ) {
                    env.suspendFor(Float.MAX_VALUE)
                }
                return ScenarioHandledCall(null)
            }

            "stage.setObject" -> {
                val enabled = args.firstOrNull().asBooleanValue()
                val terrain = args.getOrNull(1).asInt()
                val positions =
                    listOf(listOf(args.getOrNull(2).asInt(), args.getOrNull(3).asInt(), args.getOrNull(4).asInt()))
                env.stage.setMapObjects(enabled = enabled, terrainId = terrain, positions = positions)
                if (enqueueMapObjectsPresentation(
                        env.stage,
                        env.externalBattlePresentation,
                        enabled,
                        terrain,
                        positions,
                        true
                    )
                ) {
                    env.suspendFor(Float.MAX_VALUE)
                }
                return ScenarioHandledCall(null)
            }

            "stage.heightLight" -> {
                if (env.externalBattlePresentation) {
                    env.stage.requestScriptPresentation(
                        ScenarioScriptPresentationRequest.RectangleHighlight(
                            args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3),
                        ),
                    )
                    env.suspendFor(Float.MAX_VALUE)
                }
                return ScenarioHandledCall(null)
            }

            "stage.countDir" -> return ScenarioHandledCall(env.stage.countDirection(args.intAt(0), args.intAt(1)))
            "stage.setAI" -> {
                env.stage.setBattleAi(
                    camp = args.intAt(4),
                    x1 = args.intAt(0),
                    y1 = args.intAt(1),
                    x2 = args.intAt(2),
                    y2 = args.intAt(3),
                    ai = args.intAt(5),
                    targetId = args.getOrNull(6)?.asInt() ?: -1,
                    targetX = args.getOrNull(7)?.asInt() ?: 0,
                    targetY = args.getOrNull(8)?.asInt() ?: 0,
                )
                return ScenarioHandledCall(null)
            }

            "stage.resumeCtrl" -> return ScenarioHandledCall(Unit)
            "stage.setRectUnitHide" -> {
                if (env.externalBattlePresentation) {
                    val count = env.stage.requestRectUnitHide(
                        args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3), args.intAt(4),
                        args.getOrNull(5)?.asInt() ?: 0,
                    )
                    if (count > 0) env.suspendFor(Float.MAX_VALUE)
                } else {
                    env.stage.hideBattleRect(args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3), args.intAt(4))
                }
                return ScenarioHandledCall(null)
            }

            "stage.showUnit" -> {
                env.stage.apply(ScenarioCommand.ShowUnit(args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3)))
                return ScenarioHandledCall(null)
            }

            "stage.createFriend" -> {
                env.stage.createBattleUnits(ScenarioUnitFaction.FRIEND, args.firstOrNull().asList())
                return ScenarioHandledCall(null)
            }

            "stage.createEnemy", "stage.createEnemy2" -> {
                env.stage.createBattleUnits(ScenarioUnitFaction.ENEMY, args.firstOrNull().asList())
                return ScenarioHandledCall(null)
            }

            "stage.createMine" -> {
                env.stage.createBattleUnits(ScenarioUnitFaction.MINE, args.firstOrNull().asList())
                return ScenarioHandledCall(null)
            }

            "stage.showUnits" -> {
                args.firstOrNull().asList().forEach { values ->
                    val entry = values.asList()
                    if (entry.size >= 3) env.stage.apply(
                        ScenarioCommand.ShowUnit(
                            entry[0].asInt(),
                            entry[1].asInt(),
                            entry[2].asInt(),
                            entry.getOrNull(3).asInt()
                        )
                    )
                }
                return ScenarioHandledCall(null)
            }

            "stage.unitsMove" -> {
                val requests = args.firstOrNull().asList().mapNotNull { values ->
                    val entry = values.asList()
                    val unit = entry.firstOrNull() as? ScenarioUnitReference ?: return@mapNotNull null
                    if (entry.size >= 3) {
                        ScenarioCommand.MoveUnit(
                            unit.id,
                            entry[1].asInt(),
                            entry[2].asInt(),
                            entry.getOrNull(3).asInt()
                        )
                    } else null
                }
                val duration = env.stage.moveUnits(requests)
                if (duration > 0f) env.suspendFor(duration)
                return ScenarioHandledCall(null)
            }

            "stage.unit" -> {
                val value = args.firstOrNull().asInt()
                return ScenarioHandledCall(env.resolveStageUnitReference(value, args.getOrNull(1)?.asInt() ?: 0))
            }

            "stage.head" -> return ScenarioHandledCall(HeadReference(args.firstOrNull().asInt()))
            else -> return ScenarioUnitActionDispatcher.dispatch(path, node, args, frame, env)
        }
    }

    private fun enqueueMapObjectsPresentation(
        stage: ScenarioStage,
        externalBattlePresentation: Boolean,
        enabled: Boolean,
        terrainId: Int,
        positions: List<Any?>,
        soundOnFirstOnly: Boolean,
    ): Boolean {
        if (!externalBattlePresentation || !stage.battleDrawRequested) return false
        val objects = positions.mapNotNull { raw ->
            val values = raw.asList()
            if (values.size < 3) null else ScenarioScriptPresentationRequest.MapObjects.Object(
                objectId = values[0].asInt(),
                x = values[1].asInt(),
                y = values[2].asInt(),
            )
        }.filter { enabled || it.objectId >= 4 }
        if (objects.isEmpty()) return false
        stage.requestScriptPresentation(
            ScenarioScriptPresentationRequest.MapObjects(
                enabled = enabled,
                terrainId = terrainId,
                objects = objects,
                soundOnFirstObjectOnly = soundOnFirstOnly,
            ),
        )
        return true
    }
}
