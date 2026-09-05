package com.jojo.game.verification.campaign

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

/**
 * Sends campaign-navigation input to the installed production processor and
 * retains the trace evidence for every attempt.
 */
internal class CampaignE2eInputReporter(
    private val screenObservation: () -> String,
) {
    private val acceptedInputs = mutableListOf<String>()
    private val inputRecords = mutableListOf<CampaignE2eInputRecord>()

    val inputs: List<String> get() = acceptedInputs
    val records: List<CampaignE2eInputRecord> get() = inputRecords
    var transitionEnterCount = 0
        private set

    fun key(code: Int, context: String) {
        if (context.endsWith(":transition")) transitionEnterCount++
        val before = screenObservation()
        val accepted =
            checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }.keyDown(code)
        recordInput(context, accepted, before, screenObservation())
    }

    fun pointer(x: Int, y: Int, context: String) {
        val input = checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }
        val before = screenObservation()
        val accepted = input.touchDown(x, y, 0, Input.Buttons.LEFT)
        input.touchUp(x, y, 0, Input.Buttons.LEFT)
        recordInput(context, accepted, before, screenObservation())
    }

    fun recordAcceptedInput(event: String) {
        acceptedInputs += event
    }

    fun recordInputAttempt(record: CampaignE2eInputRecord) {
        inputRecords += record
    }

    private fun recordInput(event: String, accepted: Boolean, before: String, after: String) {
        inputRecords += CampaignE2eInputRecord(event, accepted, before, after)
        if (accepted) acceptedInputs += event
    }
}
