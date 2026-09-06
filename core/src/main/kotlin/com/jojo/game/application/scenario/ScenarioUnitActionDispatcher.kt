// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.application.scenario.*

import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

/** ScenarioUnitActionDispatcher: 스크립트의 유닛 생성·삭제·상태 변경 호출을 무대 유닛 상태에 반영한다. */
internal object ScenarioUnitActionDispatcher {

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
            "stage.unit().move" -> {
                env.unitReference(node, frame)?.let { unit ->
                    /**
                     * `duration` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val duration = env.stage.moveDuration(unit.id, args.intAt(0), args.intAt(1))
                    env.stage.apply(ScenarioCommand.MoveUnit(unit.id, args.intAt(0), args.intAt(1), args.intAt(2)))
                    if (duration > 0f) env.suspendFor(duration)
                }
                return ScenarioHandledCall(null)
            }

            "stage.unit().setAction" -> {
                env.unitReference(node, frame)?.let { unit ->
                    env.stage.setScriptedUnitAction(
                        unitId = unit.id,
                        action = args.intAt(0),
                        direction = args.getOrNull(1)?.asInt() ?: -1,
                        loop = args.getOrNull(2)?.asBooleanValue() ?: false,
                    )
                    if (env.externalBattlePresentation && args.intAt(0) > 0 && !(args.getOrNull(2)?.asBooleanValue()
                            ?: false)
                    ) {
                        env.suspendFor(Float.MAX_VALUE)
                    }
                }
                return ScenarioHandledCall(null)
            }

            "stage.unit().show" -> {
                env.unitReference(node, frame)?.let { reference ->
                    /**
                     * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val unit = env.stage.unit(reference.id)
                    if (!unit.visible) {
                        if (env.externalBattlePresentation) {
                            /**
                             * `request` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                             */

                            val request = ScenarioUnitShowRequest(
                                unitId = reference.id,
                                x = args.getOrNull(0)?.asInt() ?: -1,
                                y = args.getOrNull(1)?.asInt() ?: -1,
                                direction = args.getOrNull(2)?.asInt() ?: -1,
                                flags = args.getOrNull(3)?.asInt() ?: 0,
                            )
                            env.stage.requestUnitShow(request)
                            env.suspendFor(Float.MAX_VALUE)
                        } else unit.visible = true
                    }
                }
                return ScenarioHandledCall(null)
            }

            "stage.unit().hide" -> {
                env.unitReference(node, frame)?.let {
                    if (!env.stage.unit(it.id).visible) Unit
                    else if (env.externalBattlePresentation) {
                        env.stage.requestUnitHide(it.id, args.firstOrNull()?.asInt() ?: 0)
                        env.suspendFor(Float.MAX_VALUE)
                    } else env.stage.unit(it.id).visible = false
                }
                return ScenarioHandledCall(null)
            }

            "stage.unit().setDir" -> {
                env.unitReference(node, frame)?.let { env.stage.setUnitDirection(it.id, args.firstOrNull().asInt()) }
                return ScenarioHandledCall(null)
            }

            "stage.unit().setPosts" -> {
                env.unitReference(node, frame)?.let { unit ->
                    /**
                     * `flags` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

                    val flags = args.getOrNull(1)?.asInt() ?: 19
                    env.stage.setBattleUnitPosts(
                        unit.id,
                        args.firstOrNull().asInt(),
                        flags,
                        enabledFeatures = env.battleContext.enabledFeatures
                    )
                    if (env.externalBattlePresentation && env.stage.lastBattleUnitPostsRequiresPause) env.suspendFor(
                        Float.MAX_VALUE
                    )
                }
                return ScenarioHandledCall(null)
            }

            "model.unit().setPosts" -> {
                env.unitReference(node, frame)?.let { unit ->
                    env.stage.setModelUnitPosts(
                        unit.id, args.firstOrNull().asInt(), args.getOrNull(1)?.asInt() ?: 3,
                        enabledFeatures = env.battleContext.enabledFeatures,
                    )
                }
                return ScenarioHandledCall(null)
            }

            "stage.unit().addLv" -> {
                env.unitReference(node, frame)?.let {
                    env.stage.addUnitLevels(it.id, args.firstOrNull().asInt(), env.battleContext.enabledFeatures)
                }
                return ScenarioHandledCall(null)
            }

            "stage.unit().setAI" -> {
                env.unitReference(node, frame)?.let {
                    env.stage.setUnitAi(
                        unitId = it.id,
                        ai = args.intAt(0),
                        targetId = args.getOrNull(1)?.asInt() ?: -1,
                        targetX = args.getOrNull(2)?.asInt() ?: 0,
                        targetY = args.getOrNull(3)?.asInt() ?: 0,
                    )
                }
                return ScenarioHandledCall(null)
            }

            "stage.unit().retreatTxt" -> {
                env.unitReference(node, frame)?.let {
                    env.stage.setUnitRetreatTextEnabled(it.id, args.firstOrNull().asBooleanValue())
                }
                return ScenarioHandledCall(null)
            }

            "stage.unit().heightLight" -> {
                env.unitReference(node, frame)?.let {
                    if (env.externalBattlePresentation) {
                        env.stage.requestScriptPresentation(ScenarioScriptPresentationRequest.UnitHighlight(it.id))
                        env.suspendFor(Float.MAX_VALUE)
                    }
                }
                return ScenarioHandledCall(null)
            }

            "stage.head().move" -> {
                env.headReference(node, frame)?.let {
                    env.stage.moveHead(it.id, args.intAt(0), args.intAt(1)).let { duration ->
                        if (duration > 0f) env.suspendFor(duration)
                    }
                }
                return ScenarioHandledCall(null)
            }

            "stage.head().hide" -> {
                env.headReference(node, frame)?.let {
                    env.stage.hideHead(it.id).let { duration ->
                        if (duration > 0f) env.suspendFor(duration)
                    }
                }
                return ScenarioHandledCall(null)
            }

            else -> return null
        }
    }
}
