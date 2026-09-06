// Verification
package com.jojo.game.verification.campaign

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

/** CampaignE2eInputReporter: 캠페인 이동 입력을 설치된 운영 처리기로 보내고 모든 시도의 추적 증거를 보존한다. */
internal class CampaignE2eInputReporter(
    /** screenObservation: 화면 관찰 상태를 담는다. */
    private val screenObservation: () -> String,
) {
    /** acceptedInputs: 검증 대상 목록을 담는다. */
    private val acceptedInputs = mutableListOf<String>()
    /** inputRecords: 검증 대상 목록을 담는다. */
    private val inputRecords = mutableListOf<CampaignE2eInputRecord>()

    /**
     * `inputs` (List<String> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val inputs: List<String> get() = acceptedInputs
    /**
     * `records` (List<CampaignE2eInputRecord> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val records: List<CampaignE2eInputRecord> get() = inputRecords
    /**
     * `transitionEnterCount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var transitionEnterCount = 0
        private set

    /** key: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun key(code: Int, context: String) {
        if (context.endsWith(":transition")) transitionEnterCount++
        val before = screenObservation()
        val accepted =
            checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }.keyDown(code)
        recordInput(context, accepted, before, screenObservation())
    }

    /** pointer: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun pointer(x: Int, y: Int, context: String) {
        val input = checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }
        val before = screenObservation()
        val accepted = input.touchDown(x, y, 0, Input.Buttons.LEFT)
        input.touchUp(x, y, 0, Input.Buttons.LEFT)
        recordInput(context, accepted, before, screenObservation())
    }

    /** recordAcceptedInput: 검증 이벤트와 산출물을 기록한다. */
    fun recordAcceptedInput(event: String) {
        acceptedInputs += event
    }

    /** recordInputAttempt: 검증 이벤트와 산출물을 기록한다. */
    fun recordInputAttempt(record: CampaignE2eInputRecord) {
        inputRecords += record
    }

    /** recordInput: 검증 이벤트와 산출물을 기록한다. */
    private fun recordInput(event: String, accepted: Boolean, before: String, after: String) {
        inputRecords += CampaignE2eInputRecord(event, accepted, before, after)
        if (accepted) acceptedInputs += event
    }
}
