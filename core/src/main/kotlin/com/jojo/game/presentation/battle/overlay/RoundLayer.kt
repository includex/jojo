package com.jojo.game.presentation.battle.overlay

/**
 * Direct state implementation of recovered-js/modules/battle/RoundLayer.js.
 *
 * Cocos owns the labels and `scheduleOnce`; this model exposes their exact
 * resulting state and lets the desktop renderer inject the two-second timer.
 */

class RoundLayer(
    private val remove: () -> Unit,
    private val complete: () -> Unit,
) {
    /**
     * The recovered JavaScript distinguishes an absent `round` property from
     * a present value (`"round" in t`).  Keep that distinction in the game
     * instead of using null as a proxy for both cases.
     */

    data class CreateArgs(
        val roundPresent: Boolean,
        val round: Int = 0,
        val max: Int? = null,
        /** `max: null` is distinct from an absent JS property (`undefined`). */
        val maxPresent: Boolean = max != null,
    )


    data class View(
        val roundLabelsVisible: Boolean,
        val campLabelsVisible: Boolean,
        val roundText: String,
    )

    private var finished = false
    var view: View = View(roundLabelsVisible = false, campLabelsVisible = true, roundText = "")
        private set

    /** Equivalent to RoundLayer.onCreate({ fn, round?, max? }). */
    fun onCreate(args: CreateArgs) {
        // In JavaScript `round > undefined` is false.  Therefore a supplied
        // round with no max is still rendered as a numbered round, not an
        // exception as the earlier Kotlin approximation did.
        // JavaScript numeric coercion: `round > null` compares with zero,
        // whereas `round > undefined` is false.
        val isFinal = args.maxPresent && args.round > (args.max ?: 0)
        val text = if (!args.roundPresent) "" else if (isFinal) "최종 턴" else "제${args.round}턴"
        view = View(
            roundLabelsVisible = args.roundPresent,
            campLabelsVisible = !args.roundPresent,
            roundText = text,
        )
    }

    /** Compatibility adapter for callers that use null to mean an absent property. */
    fun onCreate(round: Int?, max: Int?) = onCreate(
        CreateArgs(roundPresent = round != null, round = round ?: 0, max = max),
    )

    /** Equivalent to its single `scheduleOnce(..., 2)` callback. */
    fun elapsed(seconds: Float) {
        if (!finished && seconds >= DISPLAY_SECONDS) {
            finished = true
            remove()
            complete()
        }
    }

    companion object {
        const val DISPLAY_SECONDS = 2f
    }
}
