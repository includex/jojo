// Verification
package com.jojo.game.verification
import com.jojo.game.domain.battle.command.*

import com.jojo.game.*


/** EnemyTurnTraceHarness: 실제 ControlManager 전환 프로토콜로 적 턴 의사결정을 검증한다. */
object EnemyTurnTraceHarness {
    /** q: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun q(s: String) = "\"$s\""
    /** main: 검증 실행 흐름을 시작하고 종료 상태를 반환한다. */
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

    /** run: 검증 실행에 필요한 상태를 구성한다. */
    private fun run(m: MatchResult): String {
        val id = m.groupValues[1]
        val ai = m.groupValues[2].toInt()
        val immobile = m.groupValues[3] == "true"
        val attack = m.groupValues[4] == "true"
        val desired = m.groupValues[5]
        val events = mutableListOf<String>()
        lateinit var manager: ControlManager
        val state = object : ControlManager.UnitState {
            /** isControlled: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun isControlled() = false
            /** ai: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun ai() = ai
            /** targetIndex: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun targetIndex() = -1
            /** targetX: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun targetX() = -1
            /** targetY: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun targetY() = -1
            /** targetExists: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
            override fun targetExists(index: Int) = index == 99
        }
        val factory = object : ControlManager.Factory {
            /** create: 검증 실행에 필요한 상태를 구성한다. */
            override fun create(controllerAi: Int) = object : ControlManager.Driver {
                /** controller: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                private val controller = ControlControllerFactory.create(controllerAi)
                /** data: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                private var data = ControlData()

                init {
                    events += "controller:$controllerAi"
                }

                /** setManager: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun setManager(manager: ControlManager) = Unit
                /** setWithData: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun setWithData(targetIndex: Int, x: Int, y: Int) {
                    data = ControlData(targetIndex, Control.Point(x, y))
                }

                /** selectMovePoint: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                override fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int {
                    manager.setResult(Control.Result(0, 0)) // Control.selectMovePoint's initial current-point result
                    val context = object : BattleControlContext {
                        /** currentPoint: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun currentPoint() = Control.Point(0, 0)
                        /** isParalyzed: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun isParalyzed() = immobile
                        /** isSurrounded: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun isSurrounded() = false
                        /** isMine: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun isMine() = false
                        /** setPersistentAi: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun setPersistentAi(ai: Int) = Unit
                        /** target: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun target(index: Int) =
                            if (index == 99) ControlTarget(99, Control.Point(3, 0), true, 3) else null

                        /** hasAttackTargets: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun hasAttackTargets(targetIndex: Int?) = attack
                        /** exhaustedRetreat: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun exhaustedRetreat() = null
                        /** nearestOpponent: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun nearestOpponent() =
                            if (desired == "move") ControlTarget(99, Control.Point(3, 0), true, 3) else null

                        /** winRectCentre: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun winRectCentre() = null
                        /** destinationPoint: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun destinationPoint(target: Control.Point) = Control.Point(1, 0)
                        /** nearPoint: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun nearPoint(target: Control.Point) = Control.Point(1, 0)
                        /** blockingEnemy: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                        override fun blockingEnemy(target: Control.Point) = null
                        /** chooseAi: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
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
