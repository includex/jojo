// Battle
package com.jojo.game.application.battle

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.scenario.*

/**
 * `BattleCommandFlow` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class BattleCommandFlow {
    companion object {
        /**
         * `TOUCH_END` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val TOUCH_END = 2
        /**
         * `ATTACK_BIT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ATTACK_BIT = 1 shl 0
        /**
         * `MAGICK_BIT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val MAGICK_BIT = 1 shl 1
        /**
         * `PROPERTY_BIT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val PROPERTY_BIT = 1 shl 2
        /**
         * `SWAP_BIT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val SWAP_BIT = 1 shl 3
        /**
         * `SIEGE_BIT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val SIEGE_BIT = 1 shl 4
    }


    /**
     * `Command` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    enum class Command(val tag: Int) {
        ATTACK(0), MAGICK(1), PROPERTY(2), SWAP(3), SIEGE(4), WAIT(5), CANCEL(6);

        companion object {

            /**
             * `fromTag`: 타입의 핵심 동작을 수행한다.
             * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
             */

            fun fromTag(tag: Int) = entries.firstOrNull { it.tag == tag }
        }
    }


    /**
     * `Phase` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    enum class Phase { IDLE, MOVING, COMMAND, CHILD_ACTION, COMMITTED, ROLLED_BACK }


    /**
     * `UnitPose` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class UnitPose(val x: Int, val y: Int, val direction: Int)


    /**
     * `Move` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Move(
        /**
         * `unitId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unitId: String,
        /**
         * `before` (UnitPose,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val before: UnitPose,
        /**
         * `destination` (UnitPose,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val destination: UnitPose,
        /**
         * `enabledMask` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val enabledMask: Int,
    )


    /**
     * `Button` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Button(
        /**
         * `command` (Command,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val command: Command,
        /**
         * `interactable` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val interactable: Boolean,
        /**
         * `listenerPriority` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val listenerPriority: Int = 1,
        /**
         * `grayscale` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val grayscale: Boolean = false,
    )

    /**
     * `Result` 계약 인터페이스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    sealed interface Result {
        /**
         * `Ignored` 싱글턴 객체: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data object Ignored : Result


        /**
         * `OpenChild` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class OpenChild(val command: Command) : Result


        /**
         * `Commit` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class Commit(val command: Command) : Result


        /**
         * `Rollback` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class Rollback(val unitId: String, val pose: UnitPose) : Result
    }

    /**
     * `move` (Move?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var move: Move? = null
    /**
     * `phase` (Phase): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var phase: Phase = Phase.IDLE
        private set
    /**
     * `childCommand` (Command?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var childCommand: Command? = null
        private set

    /** beginMove: 전투 단계의 시작 상태를 만들고 필요한 값을 초기화한다. */
    fun beginMove(unitId: String, before: UnitPose) {
        move = Move(unitId, before, before, 0)
        childCommand = null
        phase = Phase.MOVING
    }
    /**
     * `movementCompleted`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun movementCompleted(destination: UnitPose, enabledMask: Int) {
        val pending = checkNotNull(move) { "movementCompleted requires beginMove" }
        check(phase == Phase.MOVING) { "movementCompleted requires MOVING, was $phase" }
        move = pending.copy(destination = destination, enabledMask = enabledMask and 0x1f)
        phase = Phase.COMMAND
    }

    /** abandonMoveForScriptEnd: 입력 또는 이벤트를 반영해 전투 상태를 전환한다. */
    fun abandonMoveForScriptEnd() {
        move = null
        childCommand = null
        phase = Phase.IDLE
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun view(): List<Button> {
        val mask = move?.enabledMask ?: 0
        return Command.entries.map { command ->
            val enabled = command.tag >= 5 || mask and (1 shl command.tag) != 0
            Button(command, enabled, grayscale = command.tag < 5 && !enabled)
        }
    }

    /** touch: 입력 또는 이벤트를 반영해 전투 상태를 전환한다. */
    fun touch(tag: Int, event: Int): Result {
        if (phase != Phase.COMMAND || event != TOUCH_END) return Result.Ignored
        val command = Command.fromTag(tag) ?: return Result.Ignored
        if (!view().getValue(command).interactable) return Result.Ignored
        if (command == Command.CANCEL) {
            val pending = checkNotNull(move)
            phase = Phase.ROLLED_BACK
            return Result.Rollback(pending.unitId, pending.before)
        }
        if (command == Command.WAIT) {
            phase = Phase.COMMITTED
            return Result.Commit(command)
        }
        childCommand = command
        phase = Phase.CHILD_ACTION
        return Result.OpenChild(command)
    }
    /**
     * `childCancelled`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun childCancelled() {
        check(phase == Phase.CHILD_ACTION) { "childCancelled requires CHILD_ACTION" }
        childCommand = null
        phase = Phase.COMMAND
    }


    /**
     * `childCompleted`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun childCompleted(consumesAction: Boolean): Result {
        check(phase == Phase.CHILD_ACTION) { "childCompleted requires CHILD_ACTION" }
        val command = checkNotNull(childCommand)
        childCommand = null
        return if (consumesAction) {
            phase = Phase.COMMITTED
            Result.Commit(command)
        } else {
            phase = Phase.COMMAND
            Result.Ignored
        }
    }

    /**
     * `List`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun List<Button>.getValue(command: Command) = first { it.command == command }
}
/**
 * `BattleMoveScriptContinuation` 싱글턴 객체: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object BattleMoveScriptContinuation {

    /**
     * `shouldOpenCommand`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun shouldOpenCommand(scriptState: PlaybackState, battleEndedByScript: Boolean): Boolean =
        scriptState == PlaybackState.COMPLETE && !battleEndedByScript
}
/**
 * `BattleCommandRenderModel` 싱글턴 객체: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

object BattleCommandRenderModel {
    /**
     * `PANEL_OPACITY` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val PANEL_OPACITY = 200f / 255f
    /**
     * `DISMISS_DIM_OPACITY` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val DISMISS_DIM_OPACITY = 10f / 255f
    /**
     * `DISABLED_COMPONENT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val DISABLED_COMPONENT = 160f / 255f


    /**
     * `Node` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Node(
        /**
         * `path` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val path: String,
        /**
         * `visible` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visible: Boolean,
        /**
         * `interactable` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val interactable: Boolean,
        /**
         * `grayscaleMaterial` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val grayscaleMaterial: Boolean,
        /**
         * `listenerPriority` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val listenerPriority: Int,
    )


    /**
     * `nodes`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun nodes(buttons: List<BattleCommandFlow.Button>): List<Node> = buildList {
        add(Node("Canvas/Layer/bg", true, false, false, 0))
        buttons.forEach { button ->
            add(
                Node(
                    path = "Canvas/Layer/bg/button${button.command.tag}",
                    visible = true,
                    interactable = button.interactable,
                    grayscaleMaterial = button.grayscale,
                    listenerPriority = button.listenerPriority,
                )
            )
        }
        add(Node("Canvas/Layer/Panel_cancel", true, true, false, 2))
    }

    /**
     * `Icon` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Icon(val asset: String, val x: Float, val y: Float, val width: Float, val height: Float)


    /**
     * `ButtonVisual` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class ButtonVisual(
        /**
         * `x` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Float,
        /**
         * `y` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Float,
        /**
         * `width` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val width: Float,
        /**
         * `height` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val height: Float,
        /**
         * `labelX` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val labelX: Float,
        /**
         * `labelY` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val labelY: Float,
        /**
         * `icons` (List<Icon>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val icons: List<Icon>,
    )

    /**
     * `visuals` (List<ButtonVisual>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val visuals: List<ButtonVisual> = listOf(
        ButtonVisual(
            743.6f,
            291.175f,
            120f,
            120f,
            753.6f,
            334.175f,
            listOf(Icon("command1", 749.6f, 373.175f, 32f, 32f), Icon("command1", 825.6f, 297.175f, 32f, 32f))
        ),
        ButtonVisual(
            871.6f,
            291.175f,
            120f,
            120f,
            881.6f,
            334.175f,
            listOf(Icon("command2", 875.6f, 375.175f, 32f, 32f), Icon("command2", 953.6f, 297.175f, 32f, 32f))
        ),
        ButtonVisual(
            1000.6f,
            291.175f,
            120f,
            120f,
            1010.6f,
            334.175f,
            listOf(Icon("command3", 1004.6f, 377.175f, 30f, 30f), Icon("command3", 1084.6f, 297.175f, 30f, 30f))
        ),
        ButtonVisual(
            743.6f,
            165.42f,
            120f,
            120f,
            753.6f,
            208.42f,
            listOf(Icon("command5", 747.6f, 253.42f, 32f, 28f), Icon("command5", 825.6f, 171.42f, 32f, 28f))
        ),
        ButtonVisual(
            871.6f,
            165.42f,
            120f,
            120f,
            881.6f,
            208.42f,
            listOf(Icon("command6", 875.6f, 249.42f, 32f, 32f), Icon("command6", 953.6f, 171.42f, 32f, 32f))
        ),
        ButtonVisual(
            1000.6f,
            165.42f,
            120f,
            120f,
            1010.6f,
            208.42f,
            listOf(Icon("command4", 1004.6f, 249.42f, 32f, 32f), Icon("command4", 1082.6f, 171.42f, 32f, 32f))
        ),
        ButtonVisual(842.65f, 106.491f, 181.9f, 50f, 883.6f, 114.491f, emptyList()),
    )
}
