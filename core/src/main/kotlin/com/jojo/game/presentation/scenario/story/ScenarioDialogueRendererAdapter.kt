// Scenario
package com.jojo.game.presentation.scenario.story

import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.shared.dialogue.DialogueRenderAssets
import com.jojo.game.presentation.shared.dialogue.DialogueRenderModel

/** 시나리오 자산을 공용 대화 렌더러의 자산 포트로 변환하는 어댑터다. */
internal class ScenarioDialogueRendererAssetsAdapter(
    /** 시나리오 화면이 소유한 지연 로드 자산이다. */
    private val source: ScenarioSceneAssets,
) : DialogueRenderAssets {
    /** 대화창 배경은 시나리오에서 사용하는 원본 패널을 그대로 전달한다. */
    override val dialoguePanel get() = source.dialoguePanelTexture
    /** 선택지 전체 배경을 공용 선택지 계약으로 노출한다. */
    override val choicePanel get() = source.choicePanelTexture
    /** 선택지 행 배경을 공용 선택지 계약으로 노출한다. */
    override val choiceRow get() = source.choiceRowTexture
    /** 시나리오 정보 모달의 나인 패치 자원이다. */
    override val infoPanel get() = source.infoPanelPatch
    /** 시나리오 대사 본문에 최적화된 글꼴이다. */
    override val bodyFont get() = source.streetDialogueFont
    /** 시나리오 화자 이름에 최적화된 글꼴이다. */
    override val speakerFont get() = source.streetSpeakerFont
    /** 시나리오 선택지와 모달 제목에 사용하는 글꼴이다. */
    override val titleFont get() = source.titleFont
    /** 시나리오 인물 ID를 원본 초상화 자원으로 변환한다. */
    override fun portrait(portraitId: Int) = source.portraitTexture(portraitId)
}

/** 기존 거리 대사 view를 공용 대사 표시 모델로 변환한다. */
internal fun ScenarioStreetDialogueView.toDialogueRenderModel(): DialogueRenderModel? =
    takeIf { it.hasDialogue }?.let {
        DialogueRenderModel(
            speaker = it.speaker,
            visibleText = it.visibleText,
            portraitId = it.portraitId,
            isLeft = it.isLeft,
            isAtTop = it.isAtTop,
        )
    }
