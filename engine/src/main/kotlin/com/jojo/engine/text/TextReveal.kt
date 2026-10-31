package com.jojo.engine.text

/** 대사 글자 공개가 프레임 시간을 소비하는 방식이다. */
enum class RevealTiming {
    /** 누적된 시간이 간격을 여러 번 넘으면 한 update에서 여러 단위를 공개한다. */
    ACCUMULATING,

    /** Cocos CallbackTimer처럼 첫 update를 초기화에 쓰고 한 update에서 한 단위만 공개한다. */
    COCOS_CALLBACK_TIMER,
}

/** 리치 텍스트 원문을 태그 단위로 공개해 렌더러가 바로 쓸 수 있는 본문을 제공한다. */
class TextReveal(
    /** 글자 또는 태그 한 단위를 공개하는 간격이다. */
    private val characterIntervalSeconds: Float = 0.04f,
    /** 프레임 시간 누적과 callback 실행 규칙이다. */
    private val timing: RevealTiming = RevealTiming.ACCUMULATING,
) {
    init {
        require(characterIntervalSeconds > 0f) { "characterIntervalSeconds must be positive" }
    }

    /** 현재 공개 중인 원문이다. */
    private var source = ""

    /** 원문에서 다음에 공개할 문자 위치이다. */
    private var cursor = 0

    /** 글자 공개 간격을 누적한 시간이다. */
    private var accumulatorSeconds = 0f

    /** Cocos CallbackTimer가 마지막 callback 이후 누적한 Double 시간이다. */
    private var callbackTimerElapsedSeconds = 0.0

    /** Cocos CallbackTimer가 첫 update에서 elapsed를 0으로 초기화했는지 여부다. */
    private var callbackTimerPrimed = false

    /** 원문 전체가 공개되었는지 여부이다. */
    val isComplete: Boolean get() = cursor >= source.length

    /** 렌더러에 전달할 공개 본문이며 리치 텍스트 태그는 제외한다. */
    val visibleText: String get() = source.substring(0, cursor).replace(RICH_TEXT_TAG, "")

    /** 새 표시 요청의 원문을 설정하고 공개 진행을 처음부터 시작한다. */
    fun setSource(text: String) {
        source = text
        cursor = 0
        accumulatorSeconds = 0f
        callbackTimerElapsedSeconds = 0.0
        callbackTimerPrimed = false
    }

    /** 프레임 시간을 누적해 공개할 글자 또는 태그 단위를 진행한다. */
    fun update(deltaSeconds: Float) {
        if (timing == RevealTiming.COCOS_CALLBACK_TIMER) {
            updateCocosCallbackTimer(deltaSeconds)
            return
        }
        advanceAccumulating(deltaSeconds.coerceAtLeast(0f))
    }

    /** Retains the cursor for the same text; a changed source begins a new run. */
    fun update(text: String, deltaSeconds: Float) {
        if (text != source) setSource(text)
        if (timing == RevealTiming.ACCUMULATING) advanceAccumulating(deltaSeconds)
        else update(deltaSeconds)
    }

    private fun advanceAccumulating(deltaSeconds: Float) {
        accumulatorSeconds += deltaSeconds
        while (accumulatorSeconds >= characterIntervalSeconds && !isComplete) {
            accumulatorSeconds -= characterIntervalSeconds
            revealNextSourceUnit()
        }
    }

    /** 아직 공개하지 않은 원문이 있으면 전부 공개하고 true를 반환한다. */
    fun revealAllIfPending(): Boolean {
        if (isComplete) return false
        cursor = source.length
        accumulatorSeconds = 0f
        callbackTimerElapsedSeconds = 0.0
        return true
    }

    /** 원문과 공개 진행을 초기 상태로 되돌린다. */
    fun reset() {
        source = ""
        cursor = 0
        accumulatorSeconds = 0f
        callbackTimerElapsedSeconds = 0.0
        callbackTimerPrimed = false
    }

    /** 원본 Cocos CallbackTimer의 prime, 한 callback, remainder 폐기 규칙을 적용한다. */
    private fun updateCocosCallbackTimer(deltaSeconds: Float) {
        if (!callbackTimerPrimed) {
            callbackTimerPrimed = true
            callbackTimerElapsedSeconds = 0.0
            return
        }
        callbackTimerElapsedSeconds += deltaSeconds.coerceAtLeast(0f).toDouble()
        if (callbackTimerElapsedSeconds >= COCOS_CHARACTER_INTERVAL_SECONDS && !isComplete) {
            callbackTimerElapsedSeconds = 0.0
            revealNextSourceUnit()
        }
    }

    /** 일반 문자는 한 글자, 리치 텍스트는 태그 전체를 한 단위로 공개한다. */
    private fun revealNextSourceUnit() {
        if (source[cursor] != '<') {
            cursor++
            return
        }
        val close = source.indexOf('>', cursor)
        cursor = if (close == -1) cursor + 1 else close + 1
    }

    private companion object {
        /** DialogueLayer가 JavaScript Number로 schedule에 전달하는 정확한 interval이다. */
        const val COCOS_CHARACTER_INTERVAL_SECONDS = 0.04

        /** 표시용 본문에서 제거할 리치 텍스트 태그 패턴이다. */
        val RICH_TEXT_TAG = Regex("<[^>]*>")
    }
}
