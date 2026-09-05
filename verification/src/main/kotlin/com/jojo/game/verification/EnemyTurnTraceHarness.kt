package com.jojo.game.verification
import com.jojo.game.domain.battle.command.*

import com.jojo.game.*

/** Game half of tools/enemy_turn_source_trace_harness.js.  It deliberately
 * drives the real ControlManager + ControlControllers transition protocol. */
/**
 * object  `EnemyTurnTraceHarness`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object EnemyTurnTraceHarness {
    private fun q(s: String) = "\"$s\""
    @JvmStatic
    fun main(args: Array<String>) {
        val text = java.nio.file.Files.readString(java.nio.file.Path.of(args[0]))
        val cases =
            Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"ai\\\":(\\d+),\\\"immobile\\\":(true|false),\\\"attack\\\":(true|false),\\\"action\\\":\\\"([^\\\"]+)\\\"}").findAll(
                text
            )
        val output = cases.joinToString(prefix = "[", postfix = "]") { run(it) }
        java.nio.file.Files.writeString(java.nio.file.Path.of(args[1]), output); println(output)
    }

    private fun run(m: MatchResult): String {
        val id = m.groupValues[1]
        val ai = m.groupValues[2].toInt()
        val immobile = m.groupValues[3] == "true"
        val attack = m.groupValues[4] == "true"
        val desired = m.groupValues[5]
        val events = mutableListOf<String>()
        lateinit var manager: ControlManager
        val state = object : ControlManager.UnitState {
            override fun isControlled() = false
            override fun ai() = ai
            override fun targetIndex() = -1
            override fun targetX() = -1
            override fun targetY() = -1
            override fun targetExists(index: Int) = index == 99
        }
        val factory = object : ControlManager.Factory {
            override fun create(controllerAi: Int) = object : ControlManager.Driver {
                private val controller = ControlControllerFactory.create(controllerAi)
                private var data = ControlData()

                init {
                    events += "controller:$controllerAi"
                }

                override fun setManager(manager: ControlManager) = Unit
                override fun setWithData(targetIndex: Int, x: Int, y: Int) {
                    data = ControlData(targetIndex, Control.Point(x, y))
                }

                override fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int {
                    manager.setResult(Control.Result(0, 0)) // Control.selectMovePoint's initial current-point result
                    val context = object : BattleControlContext {
                        override fun currentPoint() = Control.Point(0, 0)
                        override fun isParalyzed() = immobile
                        override fun isSurrounded() = false
                        override fun isMine() = false
                        override fun setPersistentAi(ai: Int) = Unit
                        override fun target(index: Int) =
                            if (index == 99) ControlTarget(99, Control.Point(3, 0), true, 3) else null

                        override fun hasAttackTargets(targetIndex: Int?) = attack
                        override fun exhaustedRetreat() = null
                        override fun nearestOpponent() =
                            if (desired == "move") ControlTarget(99, Control.Point(3, 0), true, 3) else null

                        override fun winRectCentre() = null
                        override fun destinationPoint(target: Control.Point) = Control.Point(1, 0)
                        override fun nearPoint(target: Control.Point) = Control.Point(1, 0)
                        override fun blockingEnemy(target: Control.Point) = null
                        override fun chooseAi(mode: Int) = when (desired) {
                            "move" -> Control.Result(1, 0); "attack" -> Control.Result(
                                0,
                                0,
                                targetIndex = 99,
                                kind = "attack"
                            ); else -> null
                        }
                    }
                    val step = controller.step(context, data); step.transition?.let {
                        manager.setControl(
                            it.ai,
                            it.data.targetIndex,
                            it.data.target.x,
                            it.data.target.y
                        )
                    }; step.result?.let(manager::setResult); return step.status
                }
            }
        }
        manager = ControlManager(state, factory)
        val status = manager.selectMovePoint(
            listOf(Control.Point(0, 0), Control.Point(1, 0)),
            setOf(Control.Point(0, 0), Control.Point(1, 0))
        )
        val result = manager.result ?: Control.Result(0, 0)
        val action = when {
            desired == "attack" -> "attack:99"; desired == "move" && result.x == 1 -> "move:1,0"; else -> "hold"
        }
        return "{\"case\":${q(id)},\"status\":$status,\"result\":{\"x\":${result.x},\"y\":${result.y},\"kind\":${
            result.kind?.let(
                ::q
            ) ?: "null"
        }},\"events\":[${
            listOf("turn:start:player", "round:complete", "turn:enemy").plus(events)
                .plus(listOf(action, "end:enemy", "handoff:player")).joinToString { q(it) }
        }]}"
    }
}
