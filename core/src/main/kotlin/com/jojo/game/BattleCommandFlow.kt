package com.jojo.game

/**
 * Production state contract for the recovered Battle CommandLayer.
 *
 * BattleScreen owns movement and action execution.  This class preserves the
 * continuation state between those operations so opening a child list is not
 * confused with the existing desktop M/B development shortcuts.
 */
class BattleCommandFlow {
    companion object {
        const val TOUCH_END = 2
        const val ATTACK_BIT = 1 shl 0
        const val MAGICK_BIT = 1 shl 1
        const val PROPERTY_BIT = 1 shl 2
        const val SWAP_BIT = 1 shl 3
        const val SIEGE_BIT = 1 shl 4
    }

    enum class Command(val tag: Int) {
        ATTACK(0), MAGICK(1), PROPERTY(2), SWAP(3), SIEGE(4), WAIT(5), CANCEL(6);

        companion object {
            fun fromTag(tag: Int) = entries.firstOrNull { it.tag == tag }
        }
    }

    enum class Phase { IDLE, MOVING, COMMAND, CHILD_ACTION, COMMITTED, ROLLED_BACK }

    data class UnitPose(val x: Int, val y: Int, val direction: Int)
    data class Move(
        val unitId: String,
        val before: UnitPose,
        val destination: UnitPose,
        val enabledMask: Int,
    )

    data class Button(
        val command: Command,
        val interactable: Boolean,
        val listenerPriority: Int = 1,
        val grayscale: Boolean = false,
    )

    sealed interface Result {
        data object Ignored : Result
        data class OpenChild(val command: Command) : Result
        data class Commit(val command: Command) : Result
        data class Rollback(val unitId: String, val pose: UnitPose) : Result
    }

    private var move: Move? = null
    var phase: Phase = Phase.IDLE
        private set
    var childCommand: Command? = null
        private set

    /** Unit selection starts `_process`; CommandLayer must not exist yet. */
    fun beginMove(unitId: String, before: UnitPose) {
        move = Move(unitId, before, before, 0)
        childCommand = null
        phase = Phase.MOVING
    }

    /** `unitMove` completion is the sole production entry to `sel_command`. */
    fun movementCompleted(destination: UnitPose, enabledMask: Int) {
        val pending = checkNotNull(move) { "movementCompleted requires beginMove" }
        check(phase == Phase.MOVING) { "movementCompleted requires MOVING, was $phase" }
        move = pending.copy(destination = destination, enabledMask = enabledMask and 0x1f)
        phase = Phase.COMMAND
    }

    /** `stage.end()` aborts the source move callback before `sel_command`. */
    fun abandonMoveForScriptEnd() {
        move = null
        childCommand = null
        phase = Phase.IDLE
    }

    fun view(): List<Button> {
        val mask = move?.enabledMask ?: 0
        return Command.entries.map { command ->
            val enabled = command.tag >= 5 || mask and (1 shl command.tag) != 0
            Button(command, enabled, grayscale = command.tag < 5 && !enabled)
        }
    }

    /** Mirrors CommandLayer's button TOUCH_END and Panel_cancel(tag 6). */
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

    /** MagickList/UseProperty cancellation re-enters the same command loop. */
    fun childCancelled() {
        check(phase == Phase.CHILD_ACTION) { "childCancelled requires CHILD_ACTION" }
        childCommand = null
        phase = Phase.COMMAND
    }

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

    private fun List<Button>.getValue(command: Command) = first { it.command == command }
}

/** Source ctrl_mine checks isEnd immediately after its move callback script. */
internal object BattleMoveScriptContinuation {
    fun shouldOpenCommand(scriptState: PlaybackState, battleEndedByScript: Boolean): Boolean =
        scriptState == PlaybackState.COMPLETE && !battleEndedByScript
}

/** Renderer-facing nodes; geometry/assets are filled from the actual source fixture. */
object BattleCommandRenderModel {
    const val PANEL_OPACITY = 200f / 255f
    const val DISMISS_DIM_OPACITY = 10f / 255f
    const val DISABLED_COMPONENT = 160f / 255f

    data class Node(
        val path: String,
        val visible: Boolean,
        val interactable: Boolean,
        val grayscaleMaterial: Boolean,
        val listenerPriority: Int,
    )

    fun nodes(buttons: List<BattleCommandFlow.Button>): List<Node> = buildList {
        add(Node("Canvas/Layer/bg", true, false, false, 0))
        buttons.forEach { button ->
            add(Node(
                path = "Canvas/Layer/bg/button${button.command.tag}",
                visible = true,
                interactable = button.interactable,
                grayscaleMaterial = button.grayscale,
                listenerPriority = button.listenerPriority,
            ))
        }
        add(Node("Canvas/Layer/Panel_cancel", true, true, false, 2))
    }

    /**
     * Literal CommandLayer prefab geometry after the 1280x800 source canvas
     * is projected into the verification canvas.  Each command owns two
     * copies of the same SpriteFrame (img0 and img1); command3 and command5
     * retain their source-trimmed 15x15 and 16x14 pixel extents.
     */
    data class Icon(val asset: String, val x: Float, val y: Float, val width: Float, val height: Float)
    data class ButtonVisual(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val labelX: Float,
        val labelY: Float,
        val icons: List<Icon>,
    )

    val visuals: List<ButtonVisual> = listOf(
        ButtonVisual(743.6f, 291.175f, 120f, 120f, 753.6f, 334.175f, listOf(Icon("command1", 749.6f, 373.175f, 32f, 32f), Icon("command1", 825.6f, 297.175f, 32f, 32f))),
        ButtonVisual(871.6f, 291.175f, 120f, 120f, 881.6f, 334.175f, listOf(Icon("command2", 875.6f, 375.175f, 32f, 32f), Icon("command2", 953.6f, 297.175f, 32f, 32f))),
        ButtonVisual(1000.6f, 291.175f, 120f, 120f, 1010.6f, 334.175f, listOf(Icon("command3", 1004.6f, 377.175f, 30f, 30f), Icon("command3", 1084.6f, 297.175f, 30f, 30f))),
        ButtonVisual(743.6f, 165.42f, 120f, 120f, 753.6f, 208.42f, listOf(Icon("command5", 747.6f, 253.42f, 32f, 28f), Icon("command5", 825.6f, 171.42f, 32f, 28f))),
        ButtonVisual(871.6f, 165.42f, 120f, 120f, 881.6f, 208.42f, listOf(Icon("command6", 875.6f, 249.42f, 32f, 32f), Icon("command6", 953.6f, 171.42f, 32f, 32f))),
        ButtonVisual(1000.6f, 165.42f, 120f, 120f, 1010.6f, 208.42f, listOf(Icon("command4", 1004.6f, 249.42f, 32f, 32f), Icon("command4", 1082.6f, 171.42f, 32f, 32f))),
        ButtonVisual(842.65f, 106.491f, 181.9f, 50f, 883.6f, 114.491f, emptyList()),
    )
}
