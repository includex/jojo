// Battle Dialogue
package com.jojo.game.presentation.battle.dialogue

import com.jojo.game.presentation.shared.dialogue.DialogueComponentPlacement

/**
 * 전투 SayLayer 배치 정책: 원본 `_resetPos`와 같이 화자 중심 좌표로 대화창 전체를 이동한다.
 *
 * 원본은 화자에게 카메라를 맞춘 다음 `convertToWorldSpaceAR`의 Y 부호로 패널을 위·아래에
 * 두고, 패널 반높이와 유닛·말풍선 간격(48 + 32)을 더한다. 이 정책은 그 결과를 LibGDX
 * 왼쪽 아래 좌표로 변환하므로 패널·초상화·화자명·본문이 같은 기준점에서 함께 이동한다.
 */
internal object BattleDialoguePlacementPolicy {
    /** 원본 SayLayer 대화 패널의 왼쪽 아래 X 좌표다. */
    private const val PANEL_X = 245.65f
    /** 화자를 찾지 못했을 때 사용하는 기존 SayLayer 패널의 왼쪽 아래 Y 좌표다. */
    private const val DEFAULT_PANEL_Y = 282f
    /** 원본 SayLayer 대화 패널 폭이다. */
    private const val PANEL_WIDTH = 796f
    /** 원본 SayLayer 대화 패널 높이다. */
    private const val PANEL_HEIGHT = 212f
    /** 원본 SayLayer 초상화의 패널 기준 X 오프셋이다. */
    private const val PORTRAIT_OFFSET_X = 818.97f
    /** 원본 SayLayer 초상화의 패널 기준 Y 오프셋이다. */
    private const val PORTRAIT_OFFSET_Y = -2f
    /** 원본 SayLayer 초상화 폭이다. */
    private const val PORTRAIT_WIDTH = 192f
    /** 원본 SayLayer 초상화 높이다. */
    private const val PORTRAIT_HEIGHT = 240f
    /** 원본 SayLayer 화자명의 패널 기준 X 오프셋이다. */
    private const val SPEAKER_OFFSET_X = 61.58f
    /** 원본 SayLayer 화자명의 패널 기준 기준선 Y 오프셋이다. */
    private const val SPEAKER_OFFSET_Y = 189.40f
    /** 원본 SayLayer 본문의 패널 기준 X 오프셋이다. */
    private const val TEXT_OFFSET_X = 33.055f
    /** 원본 SayLayer 본문의 패널 기준 기준선 Y 오프셋이다. */
    private const val TEXT_OFFSET_Y = 99.814f
    /** 원본 SayLayer 본문 줄바꿈 폭이다. */
    private const val TEXT_WIDTH = 728f
    /** 원본 유닛 노드의 반높이다. */
    private const val UNIT_HALF_HEIGHT = 48f
    /** 원본 유닛과 대화창 사이의 추가 간격이다. */
    private const val DIALOGUE_GAP = 32f

    /**
     * 변환된 화자 화면 중심으로 SayLayer 전체 배치를 계산한다.
     *
     * @param speakerScreenCenterY `convertToWorldSpaceAR` 결과를 화면 좌표로 되돌린 화자 중심 Y다.
     * @param viewportHeight 현재 논리 화면 높이다.
     */
    fun place(speakerScreenCenterY: Float?, viewportHeight: Float): DialogueComponentPlacement {
        if (speakerScreenCenterY == null) return componentPlacement(DEFAULT_PANEL_Y)
        val safeViewportHeight = viewportHeight.coerceAtLeast(PANEL_HEIGHT)
        val centeredWorldY = speakerScreenCenterY - safeViewportHeight / 2f
        val direction = if (centeredWorldY < 0f) 1f else -1f
        val panelCenterWorldY = centeredWorldY + direction * (PANEL_HEIGHT / 2f + UNIT_HALF_HEIGHT + DIALOGUE_GAP)
        val panelY = panelCenterWorldY + safeViewportHeight / 2f - PANEL_HEIGHT / 2f
        return componentPlacement(panelY)
    }

    /** 패널 아래 좌표가 정해진 뒤 모든 자식의 동일한 상대 오프셋을 적용한다. */
    private fun componentPlacement(panelY: Float): DialogueComponentPlacement =
        DialogueComponentPlacement(
            panelX = PANEL_X,
            panelY = panelY,
            panelWidth = PANEL_WIDTH,
            panelHeight = PANEL_HEIGHT,
            portraitX = PANEL_X + PORTRAIT_OFFSET_X,
            portraitY = panelY + PORTRAIT_OFFSET_Y,
            portraitWidth = PORTRAIT_WIDTH,
            portraitHeight = PORTRAIT_HEIGHT,
            speakerX = PANEL_X + SPEAKER_OFFSET_X,
            speakerBaselineY = panelY + SPEAKER_OFFSET_Y,
            textX = PANEL_X + TEXT_OFFSET_X,
            textBaselineY = panelY + TEXT_OFFSET_Y,
            textWidth = TEXT_WIDTH,
        )
    }
