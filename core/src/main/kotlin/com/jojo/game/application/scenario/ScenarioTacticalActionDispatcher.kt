// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.application.scenario.*

import com.jojo.game.domain.scenario.*

import com.jojo.game.presentation.battle.timeline.BattlePhysicalPresentationTimeline

import com.badlogic.gdx.utils.JsonValue

/**
 * `ScenarioTacticalEnvironment` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class ScenarioTacticalEnvironment(
    val stage: ScenarioStage,
    val battleContext: ScenarioBattleScriptContext,
    val externalBattlePresentation: Boolean,
    val suspendFor: (Float) -> Unit,
    val resolveStageUnitReference: (Int, Int) -> ScenarioUnitReference?,
    val unitReference: (JsonValue, Frame) -> ScenarioUnitReference?,
    val headReference: (JsonValue, Frame) -> HeadReference?,
)

/**
 * `ScenarioHandledCall` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class ScenarioHandledCall(val value: Any?)

/** ScenarioTacticalActionDispatcher: 이동·공격·지형물 등 전술 스크립트 호출을 ScenarioStage 변경으로 변환한다. */
internal object ScenarioTacticalActionDispatcher {

    /**
     * `dispatch`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
                /**
                 * `values` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val values = env.stage.setUnitStatuses(args.firstOrNull().asList())
                /**
                 * `presents` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val presents = values.any { change ->
                    /**
                     * `hp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val hp = (change["hp"] as? Number)?.toInt() ?: 0
                    /**
                     * `mp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val mp = (change["mp"] as? Number)?.toInt() ?: 0
                    /**
                     * `status` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val status = (change["status"] as? Number)?.toInt() ?: -1
                    /**
                     * `hiddenStatuses` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

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
                /**
                 * `enabled` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val enabled = args.firstOrNull().asBooleanValue()
                /**
                 * `x` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val x = args.intAt(1)
                /**
                 * `y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val y = args.intAt(2)
                env.stage.setFire(enabled, x, y)
                if (env.externalBattlePresentation && enabled && env.stage.battleDrawRequested) {
                    env.stage.requestMapPresentation(ScenarioMapPresentationRequest(x, y, 1f))
                    env.suspendFor(Float.MAX_VALUE)
                }
                return ScenarioHandledCall(null)
            }

            "stage.setFires" -> {
                /**
                 * `enabled` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val enabled = args.firstOrNull().asBooleanValue()
                /**
                 * `positions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val positions = args.getOrNull(1).asList()
                env.stage.setFires(enabled, positions)
                /**
                 * `last` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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
                /**
                 * `x` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val x = args.intAt(0)
                /**
                 * `y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val y = args.intAt(1)
                /**
                 * `raw` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val raw = args.intAt(2)
                if (env.externalBattlePresentation) {
                    /**
                     * `magicCallId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

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
                /**
                 * `flags` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val flags = args.intAt(2)
                env.stage.attackAction(args.intAt(0), args.intAt(1), flags)
                env.suspendFor(BattlePhysicalPresentationTimeline.scriptedAttackDuration(flags))
                return ScenarioHandledCall(null)
            }

            "stage.setObjects" -> {
                /**
                 * `enabled` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val enabled = args.firstOrNull().asBooleanValue()
                /**
                 * `terrain` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val terrain = args.getOrNull(1).asInt()
                /**
                 * `positions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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
                /**
                 * `enabled` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val enabled = args.firstOrNull().asBooleanValue()
                /**
                 * `terrain` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val terrain = args.getOrNull(1).asInt()
                /**
                 * `positions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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
                    /**
                     * `count` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

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
                    /**
                     * `entry` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

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
                /**
                 * `requests` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val requests = args.firstOrNull().asList().mapNotNull { values ->
                    /**
                     * `entry` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val entry = values.asList()
                    /**
                     * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

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
                /**
                 * `duration` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val duration = env.stage.moveUnits(requests)
                if (duration > 0f) env.suspendFor(duration)
                return ScenarioHandledCall(null)
            }

            "stage.unit" -> {
                /**
                 * `value` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val value = args.firstOrNull().asInt()
                return ScenarioHandledCall(env.resolveStageUnitReference(value, args.getOrNull(1)?.asInt() ?: 0))
            }

            "stage.head" -> return ScenarioHandledCall(HeadReference(args.firstOrNull().asInt()))
            else -> return ScenarioUnitActionDispatcher.dispatch(path, node, args, frame, env)
        }
    }

    /**
     * `enqueueMapObjectsPresentation`: 화면 표시 상태를 렌더링한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun enqueueMapObjectsPresentation(
        stage: ScenarioStage,
        externalBattlePresentation: Boolean,
        enabled: Boolean,
        terrainId: Int,
        positions: List<Any?>,
        soundOnFirstOnly: Boolean,
    ): Boolean {
        if (!externalBattlePresentation || !stage.battleDrawRequested) return false
        /**
         * `objects` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val objects = positions.mapNotNull { raw ->
            /**
             * `values` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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
