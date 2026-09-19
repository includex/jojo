// Test
package com.jojo.game

import com.jojo.game.presentation.shared.dialogue.DialogueChoice
import com.jojo.game.presentation.shared.dialogue.DialogueMessage
import com.jojo.game.presentation.shared.dialogue.DialogueModal
import com.jojo.game.presentation.shared.dialogue.DialogueModalKind
import com.jojo.game.presentation.shared.dialogue.DialogueRevealTiming
import com.jojo.game.presentation.shared.dialogue.DialogueSession
import com.jojo.game.presentation.shared.dialogue.DialogueSessionInput
import com.jojo.game.presentation.shared.dialogue.DialogueSessionMode
import com.jojo.game.presentation.shared.dialogue.DialogueSessionTransition
import com.jojo.game.presentation.shared.dialogue.DialogueTextReveal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 공용 대화 세션 테스트: 시나리오·전투가 공유하는 대사·선택지·모달 입력 전이를 검증한다. */
class DialogueSessionTest {
    /** 대사 입력은 미공개 본문을 먼저 공개하고, 다음 입력에서 외부 실행기 진행을 요청한다. */
    @Test
    fun confirmRevealsDialogueBeforeAdvancingExternalFlow() {
        val session = DialogueSession()
        session.presentDialogue(DialogueMessage(revision = 1, speakerId = "1", text = "<b>안녕</b>"))

        assertFalse(session.view.textComplete)
        assertEquals(DialogueSessionTransition.TextRevealed, session.dispatch(DialogueSessionInput.Confirm))
        assertEquals("안녕", session.view.dialogueVisibleText)
        assertEquals(DialogueSessionTransition.AdvanceDialogue, session.dispatch(DialogueSessionInput.Confirm))
    }

    /** 선택지 커서는 순환 이동하고 확정 전이는 현재 선택 번호를 외부 실행기에 전달한다. */
    @Test
    fun choiceSelectionWrapsAndConfirmsSelectedIndex() {
        val session = DialogueSession()
        session.presentChoice(DialogueChoice(revision = 1, options = listOf("첫째", "둘째")))

        assertEquals(DialogueSessionTransition.ChoiceSelectionChanged(1), session.dispatch(DialogueSessionInput.SelectPrevious))
        assertEquals(1, session.view.choice?.selectedIndex)
        assertEquals(DialogueSessionTransition.ChoiceConfirmed(1), session.dispatch(DialogueSessionInput.Confirm))
    }

    /** 모달도 대사와 동일하게 글자 공개 후에만 외부 페이지·재개 전이를 요청한다. */
    @Test
    fun modalUsesSharedRevealAndAdvanceContract() {
        val session = DialogueSession()
        session.presentModal(DialogueModal(1, DialogueModalKind.INFO, "정보"))

        assertEquals(DialogueSessionMode.MODAL, session.view.mode)
        assertEquals(DialogueSessionTransition.TextRevealed, session.dispatch(DialogueSessionInput.RevealAll))
        assertEquals("정보", session.view.modalVisibleText)
        assertEquals(DialogueSessionTransition.AdvanceModal, session.dispatch(DialogueSessionInput.Confirm))
    }

    @Test
    fun externallyOwnedModalRevealUpdatesAtTheSameRevisionWithoutInternalTyping() {
        val session = DialogueSession()
        session.presentModal(DialogueModal(1, DialogueModalKind.EVENT, "ABC", externalVisibleText = "", externalTextComplete = false))

        session.update(10f, autoAdvanceEnabled = true)
        assertEquals("", session.view.modalVisibleText)
        assertFalse(session.view.textComplete)

        session.presentModal(DialogueModal(1, DialogueModalKind.EVENT, "ABC", externalVisibleText = "AB", externalTextComplete = false))
        assertEquals("AB", session.view.modalVisibleText)
        session.presentModal(DialogueModal(1, DialogueModalKind.EVENT, "ABC", externalVisibleText = "ABC", externalTextComplete = true))
        assertEquals("ABC", session.view.modalVisibleText)
        assertTrue(session.view.textComplete)
    }

    /** 자동 진행은 글자 공개 완료와 지연 시간이 모두 지난 뒤 한 번만 발생한다. */
    @Test
    fun autoAdvanceWaitsForRevealAndDelay() {
        val session = DialogueSession()
        session.presentDialogue(DialogueMessage(1, null, "가"))

        assertEquals(DialogueSessionTransition.Ignored, session.update(.04f, autoAdvanceEnabled = true))
        assertTrue(session.view.textComplete)
        assertEquals(DialogueSessionTransition.Ignored, session.update(.5f, autoAdvanceEnabled = true))
        assertEquals(DialogueSessionTransition.AutoAdvance, session.update(.5f, autoAdvanceEnabled = true))
    }

    @Test
    fun cocosCallbackTimerPrimesThenRevealsAtMostOneUnitAndDropsOvershoot() {
        val reveal = DialogueTextReveal(timing = DialogueRevealTiming.COCOS_CALLBACK_TIMER)
        reveal.setSource("ABC")

        reveal.update(10f)
        assertEquals("", reveal.visibleText, "first update only primes the source timer")

        reveal.update(.1f)
        assertEquals("A", reveal.visibleText, "a large delta triggers only one callback")

        reveal.update(.039f)
        assertEquals("A", reveal.visibleText, "overshoot from the previous callback is discarded")
        reveal.update(.001f)
        assertEquals("AB", reveal.visibleText)
    }

    @Test
    fun cocosCallbackTimerUsesJavaScriptPointZeroFourThreshold() {
        val reveal = DialogueTextReveal(timing = DialogueRevealTiming.COCOS_CALLBACK_TIMER)
        reveal.setSource("A")
        reveal.update(0f)

        reveal.update(.04f)
        assertEquals("", reveal.visibleText, "Float .04 widened to Double remains below JavaScript Number .04")
        reveal.update(.000000001f)
        assertEquals("A", reveal.visibleText)
    }

    @Test
    fun aNewRevisionRestartsTypingEvenWhenDialogueTextIsUnchanged() {
        val session = DialogueSession(dialogueRevealTiming = DialogueRevealTiming.COCOS_CALLBACK_TIMER)
        val message = DialogueMessage(1, null, "같음")
        session.presentDialogue(message)
        session.update(0f, autoAdvanceEnabled = false)
        session.update(.1f, autoAdvanceEnabled = false)
        assertEquals("같", session.view.dialogueVisibleText)

        session.presentDialogue(message.copy(revision = 2))
        assertEquals("", session.view.dialogueVisibleText)
        session.update(10f, autoAdvanceEnabled = false)
        assertEquals("", session.view.dialogueVisibleText, "the restarted timer primes before revealing")
    }

    @Test
    fun sourceDialogueNaturalCompletionStartsPrimedOnePointSixSecondTimer() {
        val session = DialogueSession(dialogueRevealTiming = DialogueRevealTiming.COCOS_CALLBACK_TIMER)
        session.presentDialogue(DialogueMessage(1, null, "가"))

        session.update(10f, autoAdvanceEnabled = true)
        assertEquals(DialogueSessionTransition.Ignored, session.update(.1f, autoAdvanceEnabled = true))
        assertTrue(session.view.textComplete)
        assertEquals(DialogueSessionTransition.Ignored, session.update(1.599f, autoAdvanceEnabled = true))
        assertEquals(DialogueSessionTransition.AutoAdvance, session.update(.002f, autoAdvanceEnabled = true))
        assertEquals(DialogueSessionTransition.Ignored, session.update(10f, autoAdvanceEnabled = true), "one-shot must not fire twice")
    }

    @Test
    fun sourceDialogueManualRevealPrimesAutoCloseOnTheNextUpdate() {
        val session = DialogueSession(dialogueRevealTiming = DialogueRevealTiming.COCOS_CALLBACK_TIMER)
        session.presentDialogue(DialogueMessage(1, null, "가나"))
        session.update(0f, autoAdvanceEnabled = true)

        assertEquals(DialogueSessionTransition.TextRevealed, session.dispatch(DialogueSessionInput.Confirm))
        assertEquals(DialogueSessionTransition.Ignored, session.update(10f, autoAdvanceEnabled = true), "first timer update only primes")
        assertEquals(DialogueSessionTransition.AutoAdvance, session.update(1.6f, autoAdvanceEnabled = true))
    }

    @Test
    fun sourceDialogueAutoCloseUsesFirstUpdateSettingSnapshotAndResetsPerRevision() {
        val session = DialogueSession(dialogueRevealTiming = DialogueRevealTiming.COCOS_CALLBACK_TIMER)
        session.presentDialogue(DialogueMessage(1, null, "가"))
        session.update(0f, autoAdvanceEnabled = false)
        session.update(.1f, autoAdvanceEnabled = true)
        assertEquals(DialogueSessionTransition.Ignored, session.update(10f, autoAdvanceEnabled = true))

        session.presentDialogue(DialogueMessage(2, null, "나"))
        session.update(0f, autoAdvanceEnabled = true)
        session.update(.1f, autoAdvanceEnabled = false)
        assertEquals(DialogueSessionTransition.AutoAdvance, session.update(1.6f, autoAdvanceEnabled = false))
    }
}
