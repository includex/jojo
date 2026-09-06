// Scenario Dialogue
package com.jojo.game.presentation.scenario

/** 원본 DialogueLayer가 사용하는 화자 전환·말풍선 위치 계산 결과다. */
internal data class ScenarioDialoguePlacement(
    /** 현재 대화창이 사용할 좌우 순번이다. 0은 왼쪽, 1은 오른쪽이다. */
    val side: Int,
    /** 화자가 화면 위쪽에 있으면 true, 아래쪽이면 false다. */
    val atTop: Boolean,
    /** 원본 bg0/bg1 교대에 사용하는 누적 화자 순번이다. */
    val bubbleIndex: Int,
)

/**
 * 원본 DialogueLayer의 대화창 배치 규칙을 보관한다.
 *
 * 화자가 바뀔 때만 bg0/bg1 순번을 증가시키고, 같은 화자가 이어지면 현재 순번을 유지한다.
 * 화자 0은 원본과 동일하게 첫 번째 말풍선 순번으로 되돌린다. 등록되지 않은 Hall unit은
 * 위치 판정과 순번 변경에서 제외해 이전 대화창 배치를 유지한다.
 */
internal class ScenarioDialoguePlacementPolicy {
    /** 마지막으로 배치를 확정한 등록 화자 ID다. */
    private var lastSpeakerId = -1

    /** 원본 bg0/bg1 선택을 위한 화자 전환 누적 순번이다. */
    private var bubbleIndex = 0

    /**
     * Hall unit의 YPos와 화자 ID를 이용해 현재 대화창 배치를 계산한다.
     * `unitY`가 null이면 원본 DialogueLayer처럼 기존 순번과 위치를 유지한다.
     */
    fun resolve(speakerId: Int?, unitY: (Int) -> Float?): ScenarioDialoguePlacement {
        val y = speakerId?.let(unitY)
        if (speakerId != null && y != null) {
            if (speakerId != lastSpeakerId) bubbleIndex++
            if (speakerId == 0) bubbleIndex = 0
            lastSpeakerId = speakerId
        }
        return ScenarioDialoguePlacement(
            side = Math.floorMod(bubbleIndex, 2),
            atTop = y != null && y < TOP_THRESHOLD,
            bubbleIndex = bubbleIndex,
        )
    }

    /** 새 시나리오·대화 흐름에서 원본의 첫 말풍선 상태로 되돌린다. */
    fun reset() {
        lastSpeakerId = -1
        bubbleIndex = 0
    }

    private companion object {
        /** 원본 DialogueLayer가 위쪽 말풍선을 선택하는 YPos 경계다. */
        const val TOP_THRESHOLD = -50f
    }
}
