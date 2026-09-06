// Dialogue
package com.jojo.game.presentation.shared.dialogue

/** 대화 한 줄: 화자 식별자, 본문, 초상화·배치에 필요한 화면 힌트를 함께 전달한다. */
data class DialogueMessage(
    /** 같은 본문도 새 대사로 구별하기 위한 화면 갱신 번호이다. */
    val revision: Long,
    /** 화자 데이터 또는 화면 유닛을 찾는 식별자이다. */
    val speakerId: String?,
    /** 리치 텍스트 태그를 포함할 수 있는 원문이다. */
    val text: String,
    /** 화면별 초상화 선택에 쓰는 선택 식별자이다. */
    val portraitId: String? = speakerId,
    /** 말풍선·패널의 배치 방향을 나타낸다. */
    val placement: DialoguePlacement = DialoguePlacement.DEFAULT,
    /** 원본 대화창 교대 정책이 계산한 좌우 순번이다. 0은 왼쪽이다. */
    val side: Int = 0,
    /** 원본 화자 YPos 판정 결과로 대화창을 위쪽에 표시할지 여부다. */
    val atTop: Boolean = false,
)

/** 대사 배치: 공용 세션이 화면별 배치 규칙을 침범하지 않도록 힌트만 보관한다. */
enum class DialoguePlacement {
    DEFAULT,
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
}

/** 선택지 표시 상태: 옵션과 현재 선택 위치, 확인형 여부를 묶는다. */
data class DialogueChoice(
    /** 같은 옵션 목록을 새 요청으로 구별하는 화면 갱신 번호이다. */
    val revision: Long,
    /** 화면에 표시할 선택지 문구 목록이다. */
    val options: List<String>,
    /** 현재 커서가 가리키는 옵션 번호이다. */
    val selectedIndex: Int = 0,
    /** 예·아니오처럼 확인 흐름으로 표시할지 여부이다. */
    val isConfirmation: Boolean = false,
    /** 선택 화면에 함께 표시할 초상화 식별자이다. */
    val portraitId: String? = null,
)

/** 모달 종류: 대화 패널과 구별되는 화면 차단형 안내의 표시 성격이다. */
enum class DialogueModalKind { EVENT, INFO, MAP_INFO, SECTION, AMBITION, OTHER }

/** 모달 표시 상태: 본문과 고정 본문, 자동 진행 정책을 화면으로 전달한다. */
data class DialogueModal(
    /** 같은 본문을 새 모달로 구별하는 화면 갱신 번호이다. */
    val revision: Long,
    /** 모달의 화면 표현 종류이다. */
    val kind: DialogueModalKind,
    /** 글자 표시 효과를 적용할 가변 본문이다. */
    val text: String,
    /** 글자 표시 없이 항상 함께 보일 고정 본문이다. */
    val fixedText: String = "",
    /** 자동 진행을 허용할지 여부이다. */
    val autoAdvance: Boolean = false,
    /** 글자 표시 완료 뒤 자동 진행까지 기다릴 시간이다. */
    val autoAdvanceDelaySeconds: Float = 1f,
)

/** 세션 화면 모드: 현재 입력을 대사·선택지·모달 중 어느 계약으로 해석할지 나타낸다. */
enum class DialogueSessionMode { IDLE, DIALOGUE, CHOICE, MODAL }

/** 공용 대화 화면 스냅샷: 렌더러가 필요한 상태만 불변 값으로 제공한다. */
data class DialogueSessionView(
    /** 현재 화면 모드이다. */
    val mode: DialogueSessionMode,
    /** 표시 중인 대사이며 대사 모드가 아니면 null이다. */
    val dialogue: DialogueMessage?,
    /** 공개된 대사 본문이며 리치 텍스트 태그는 제거된다. */
    val dialogueVisibleText: String,
    /** 표시 중인 선택지이며 선택지 모드가 아니면 null이다. */
    val choice: DialogueChoice?,
    /** 표시 중인 모달이며 모달 모드가 아니면 null이다. */
    val modal: DialogueModal?,
    /** 공개된 모달 본문이며 리치 텍스트 태그는 제거된다. */
    val modalVisibleText: String,
    /** 현재 글자 표시가 끝났는지 여부이다. */
    val textComplete: Boolean,
)

/** 공용 대화 입력: 키보드·마우스·터치 입력을 화면 독립적인 의도로 변환한다. */
sealed interface DialogueSessionInput {
    /** 현재 글자 표시를 완료하거나 다음 대사·모달·선택 확정을 요청한다. */
    data object Confirm : DialogueSessionInput

    /** 선택지 커서를 이전 옵션으로 이동한다. */
    data object SelectPrevious : DialogueSessionInput

    /** 선택지 커서를 다음 옵션으로 이동한다. */
    data object SelectNext : DialogueSessionInput

    /** 선택지 커서를 지정한 옵션으로 이동한다. */
    data class Select(val index: Int) : DialogueSessionInput

    /** 현재 화면의 글자 표시를 건너뛴다. */
    data object RevealAll : DialogueSessionInput

    /** 표시를 즉시 닫고 외부 흐름으로 복귀한다. */
    data object Dismiss : DialogueSessionInput
}

/** 공용 대화 전이: 세션 입력 처리 뒤 시나리오·전투 어댑터가 수행할 후속 동작이다. */
sealed interface DialogueSessionTransition {
    /** 현재 상태에서 처리할 입력이 없었다. */
    data object Ignored : DialogueSessionTransition

    /** 현재 문장이 즉시 모두 공개되었다. */
    data object TextRevealed : DialogueSessionTransition

    /** 외부 실행기가 다음 대사를 공급하거나 실행을 재개해야 한다. */
    data object AdvanceDialogue : DialogueSessionTransition

    /** 선택 커서가 이동했다. */
    data class ChoiceSelectionChanged(val selectedIndex: Int) : DialogueSessionTransition

    /** 선택이 확정되어 외부 실행기가 결과를 반영해야 한다. */
    data class ChoiceConfirmed(val selectedIndex: Int) : DialogueSessionTransition

    /** 외부 실행기가 다음 모달 페이지를 공급하거나 실행을 재개해야 한다. */
    data object AdvanceModal : DialogueSessionTransition

    /** 자동 진행 시간이 끝나 외부 실행기를 진행해야 한다. */
    data object AutoAdvance : DialogueSessionTransition

    /** 현재 대화 UI가 즉시 닫혔다. */
    data object Dismissed : DialogueSessionTransition
}

/**
 * 공용 대화 세션: 대사·선택지·모달의 표시 상태, 글자 공개, 입력 전이를 한곳에서 관리한다.
 *
 * 실제 스크립트 실행과 전투 연출 대기열은 소유하지 않는다. 각 화면 어댑터가 전이 결과를 받아
 * 기존 실행기에 전달하므로, 시나리오와 전투가 동일한 입력 규칙을 쓰면서도 각자 흐름을 유지한다.
 */
class DialogueSession(
    /** 글자 한 단위를 공개하는 기본 간격이다. */
    private val characterIntervalSeconds: Float = DEFAULT_CHARACTER_INTERVAL_SECONDS,
) {
    init {
        require(characterIntervalSeconds > 0f) { "글자 공개 간격은 0보다 커야 합니다." }
    }

    /** 현재 화면 모드이다. */
    private var mode = DialogueSessionMode.IDLE

    /** 화면에 표시할 대사이다. */
    private var dialogue: DialogueMessage? = null

    /** 화면에 표시할 선택지이다. */
    private var choice: DialogueChoice? = null

    /** 화면에 표시할 모달이다. */
    private var modal: DialogueModal? = null

    /** 대사 본문의 글자 공개 진행 상태이다. */
    private val dialogueReveal = DialogueTextReveal(characterIntervalSeconds)

    /** 모달 본문의 글자 공개 진행 상태이다. */
    private val modalReveal = DialogueTextReveal(characterIntervalSeconds)

    /** 자동 진행까지 남은 시간이며 null이면 자동 진행을 기다리지 않는다. */
    private var autoAdvanceRemainingSeconds: Float? = null

    /** 현재 화면을 렌더링하기 위한 불변 스냅샷이다. */
    val view: DialogueSessionView
        get() = DialogueSessionView(
            mode = mode,
            dialogue = dialogue,
            dialogueVisibleText = dialogueReveal.visibleText,
            choice = choice,
            modal = modal,
            modalVisibleText = modalReveal.visibleText,
            textComplete = activeReveal().isComplete,
        )

    /** 대사를 표시하고 같은 갱신 번호가 아니면 글자 공개를 처음부터 시작한다. */
    fun presentDialogue(message: DialogueMessage) {
        if (mode == DialogueSessionMode.DIALOGUE && dialogue?.revision == message.revision) return
        clearExcept(DialogueSessionMode.DIALOGUE)
        mode = DialogueSessionMode.DIALOGUE
        dialogue = message
        dialogueReveal.setSource(message.text)
    }

    /** 선택지를 표시하고 입력 커서를 전달한 위치로 맞춘다. */
    fun presentChoice(request: DialogueChoice) {
        val normalized = request.copy(selectedIndex = request.coerceInOptionRange())
        if (mode == DialogueSessionMode.CHOICE && choice?.revision == normalized.revision) {
            choice = normalized
            return
        }
        clearExcept(DialogueSessionMode.CHOICE)
        mode = DialogueSessionMode.CHOICE
        choice = normalized
    }

    /** 모달을 표시하고 같은 갱신 번호가 아니면 글자 공개를 처음부터 시작한다. */
    fun presentModal(request: DialogueModal) {
        if (mode == DialogueSessionMode.MODAL && modal?.revision == request.revision) return
        clearExcept(DialogueSessionMode.MODAL)
        mode = DialogueSessionMode.MODAL
        modal = request
        modalReveal.setSource(request.text)
    }

    /** 모든 표시 상태와 진행 중인 글자 공개를 초기화한다. */
    fun clear() {
        mode = DialogueSessionMode.IDLE
        dialogue = null
        choice = null
        modal = null
        dialogueReveal.reset()
        modalReveal.reset()
        autoAdvanceRemainingSeconds = null
    }

    /** 프레임 시간을 반영해 글자를 공개하고 자동 진행 시점을 판정한다. */
    fun update(deltaSeconds: Float, autoAdvanceEnabled: Boolean): DialogueSessionTransition {
        val delta = deltaSeconds.coerceAtLeast(0f)
        when (mode) {
            DialogueSessionMode.DIALOGUE -> dialogueReveal.update(delta)
            DialogueSessionMode.MODAL -> modalReveal.update(delta)
            DialogueSessionMode.IDLE, DialogueSessionMode.CHOICE -> return DialogueSessionTransition.Ignored
        }
        val request = modal ?: return updateDialogueAutoAdvance(delta, autoAdvanceEnabled)
        if (!request.autoAdvance || !autoAdvanceEnabled || !modalReveal.isComplete) {
            autoAdvanceRemainingSeconds = null
            return DialogueSessionTransition.Ignored
        }
        return tickAutoAdvance(request.autoAdvanceDelaySeconds, delta)
    }

    /** 입력 의도를 현재 모드의 공개·이동·확정 전이로 변환한다. */
    fun dispatch(input: DialogueSessionInput): DialogueSessionTransition = when (mode) {
        DialogueSessionMode.IDLE -> DialogueSessionTransition.Ignored
        DialogueSessionMode.DIALOGUE -> dispatchTextInput(dialogueReveal, DialogueSessionTransition.AdvanceDialogue, input)
        DialogueSessionMode.MODAL -> dispatchTextInput(modalReveal, DialogueSessionTransition.AdvanceModal, input)
        DialogueSessionMode.CHOICE -> dispatchChoiceInput(input)
    }

    /** 대사 자동 진행을 글자 공개 완료 뒤의 지연 시간으로 처리한다. */
    private fun updateDialogueAutoAdvance(delta: Float, enabled: Boolean): DialogueSessionTransition {
        if (!enabled || !dialogueReveal.isComplete) {
            autoAdvanceRemainingSeconds = null
            return DialogueSessionTransition.Ignored
        }
        return tickAutoAdvance(DEFAULT_AUTO_ADVANCE_DELAY_SECONDS, delta)
    }

    /** 모달·대사 공통 글자 공개 입력을 처리한다. */
    private fun dispatchTextInput(
        reveal: DialogueTextReveal,
        completedTransition: DialogueSessionTransition,
        input: DialogueSessionInput,
    ): DialogueSessionTransition = when (input) {
        DialogueSessionInput.Confirm -> {
            if (reveal.revealAllIfPending()) {
                autoAdvanceRemainingSeconds = null
                DialogueSessionTransition.TextRevealed
            } else completedTransition
        }

        DialogueSessionInput.RevealAll -> if (reveal.revealAllIfPending()) {
            autoAdvanceRemainingSeconds = null
            DialogueSessionTransition.TextRevealed
        } else DialogueSessionTransition.Ignored

        DialogueSessionInput.Dismiss -> {
            clear()
            DialogueSessionTransition.Dismissed
        }

        DialogueSessionInput.SelectPrevious,
        DialogueSessionInput.SelectNext,
        is DialogueSessionInput.Select -> DialogueSessionTransition.Ignored
    }

    /** 선택지 이동과 확정 입력을 처리한다. */
    private fun dispatchChoiceInput(input: DialogueSessionInput): DialogueSessionTransition {
        val current = choice ?: return DialogueSessionTransition.Ignored
        return when (input) {
            DialogueSessionInput.Confirm -> DialogueSessionTransition.ChoiceConfirmed(current.selectedIndex)
            DialogueSessionInput.SelectPrevious -> updateChoiceSelection(current, current.selectedIndex - 1)
            DialogueSessionInput.SelectNext -> updateChoiceSelection(current, current.selectedIndex + 1)
            is DialogueSessionInput.Select -> updateChoiceSelection(current, input.index)
            DialogueSessionInput.Dismiss -> {
                clear()
                DialogueSessionTransition.Dismissed
            }

            DialogueSessionInput.RevealAll -> DialogueSessionTransition.Ignored
        }
    }

    /** 선택지 목록 안에서 순환하는 커서 위치를 저장하고 변경 전이를 반환한다. */
    private fun updateChoiceSelection(current: DialogueChoice, requestedIndex: Int): DialogueSessionTransition {
        if (current.options.isEmpty()) return DialogueSessionTransition.Ignored
        val selected = Math.floorMod(requestedIndex, current.options.size)
        choice = current.copy(selectedIndex = selected)
        return DialogueSessionTransition.ChoiceSelectionChanged(selected)
    }

    /** 자동 진행 타이머를 시작하거나 만료 전이를 반환한다. */
    private fun tickAutoAdvance(delay: Float, delta: Float): DialogueSessionTransition {
        val remaining = autoAdvanceRemainingSeconds
        if (remaining == null) {
            autoAdvanceRemainingSeconds = delay.coerceAtLeast(0f)
            return DialogueSessionTransition.Ignored
        }
        val next = remaining - delta
        if (next > 0f) {
            autoAdvanceRemainingSeconds = next
            return DialogueSessionTransition.Ignored
        }
        autoAdvanceRemainingSeconds = null
        return DialogueSessionTransition.AutoAdvance
    }

    /** 활성 상태의 글자 공개기를 선택한다. */
    private fun activeReveal(): DialogueTextReveal = when (mode) {
        DialogueSessionMode.DIALOGUE -> dialogueReveal
        DialogueSessionMode.MODAL -> modalReveal
        DialogueSessionMode.IDLE, DialogueSessionMode.CHOICE -> EMPTY_REVEAL
    }

    /** 새 모드와 충돌하는 화면 상태만 비운다. */
    private fun clearExcept(nextMode: DialogueSessionMode) {
        if (nextMode != DialogueSessionMode.DIALOGUE) {
            dialogue = null
            dialogueReveal.reset()
        }
        if (nextMode != DialogueSessionMode.CHOICE) choice = null
        if (nextMode != DialogueSessionMode.MODAL) {
            modal = null
            modalReveal.reset()
        }
        autoAdvanceRemainingSeconds = null
    }

    /** 선택지 옵션 수에 맞춰 최초 커서를 보정한다. */
    private fun DialogueChoice.coerceInOptionRange(): Int =
        if (options.isEmpty()) 0 else selectedIndex.coerceIn(0, options.lastIndex)

    private companion object {
        /** 기본 글자 공개 간격이다. */
        const val DEFAULT_CHARACTER_INTERVAL_SECONDS = 0.04f

        /** 일반 대사의 기본 자동 진행 지연 시간이다. */
        const val DEFAULT_AUTO_ADVANCE_DELAY_SECONDS = 1f

        /** 유휴 상태에서 완료된 것으로 취급할 빈 글자 공개기이다. */
        val EMPTY_REVEAL = DialogueTextReveal(DEFAULT_CHARACTER_INTERVAL_SECONDS)
    }
}

/** 리치 텍스트 원문을 태그 단위로 공개해 렌더러가 바로 쓸 수 있는 본문을 제공한다. */
class DialogueTextReveal(
    /** 글자 또는 태그 한 단위를 공개하는 간격이다. */
    private val characterIntervalSeconds: Float = 0.04f,
) {
    init {
        require(characterIntervalSeconds > 0f) { "글자 공개 간격은 0보다 커야 합니다." }
    }

    /** 현재 공개 중인 원문이다. */
    private var source = ""

    /** 원문에서 다음에 공개할 문자 위치이다. */
    private var cursor = 0

    /** 글자 공개 간격을 누적한 시간이다. */
    private var accumulatorSeconds = 0f

    /** 원문 전체가 공개되었는지 여부이다. */
    val isComplete: Boolean get() = cursor >= source.length

    /** 렌더러에 전달할 공개 본문이며 리치 텍스트 태그는 제외한다. */
    val visibleText: String get() = source.substring(0, cursor).replace(RICH_TEXT_TAG, "")

    /** 새 원문을 설정하고 이전 원문과 다를 때만 공개 진행을 초기화한다. */
    fun setSource(text: String) {
        if (source == text) return
        source = text
        cursor = 0
        accumulatorSeconds = 0f
    }

    /** 프레임 시간을 누적해 공개할 글자 또는 태그 단위를 진행한다. */
    fun update(deltaSeconds: Float) {
        accumulatorSeconds += deltaSeconds.coerceAtLeast(0f)
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
        return true
    }

    /** 원문과 공개 진행을 초기 상태로 되돌린다. */
    fun reset() {
        source = ""
        cursor = 0
        accumulatorSeconds = 0f
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
        /** 표시용 본문에서 제거할 리치 텍스트 태그 패턴이다. */
        val RICH_TEXT_TAG = Regex("<[^>]*>")
    }
}
