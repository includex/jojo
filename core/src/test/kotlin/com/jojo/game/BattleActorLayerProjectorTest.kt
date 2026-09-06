// Test
package com.jojo.game

import com.jojo.game.presentation.battle.render.BattleActorLayerCandidate
import com.jojo.game.presentation.battle.render.BattleActorLayerProjector
import kotlin.test.Test
import kotlin.test.assertEquals

/** BattleActorLayerProjectorTest: 전투 배우 레이어의 가시성 및 장면별 정렬 규칙을 검증한다. */
class BattleActorLayerProjectorTest {
    /** 일반 전투 정렬 검증: 숨은 유닛을 제외하고 타일 Y축 오름차순으로 표시한다. */
    @Test
    fun `normal battle orders only visible actors by tile y`() {
        val candidates = listOf(
            BattleActorLayerCandidate(0, 101, 5, visible = true),
            BattleActorLayerCandidate(1, 102, 1, visible = false),
            BattleActorLayerCandidate(2, 103, 2, visible = true),
        )

        assertEquals(
            listOf(2, 0),
            BattleActorLayerProjector.visibleSourceIndexes(candidates, dialogueBlendRoute = false),
        )
    }

    /** 대화 혼합 정렬 검증: 원본 장면에 지정된 인물은 미지정 인물보다 먼저 그린다. */
    @Test
    fun `dialogue blend uses fixed character layering order`() {
        val candidates = listOf(
            BattleActorLayerCandidate(0, 999, 0, visible = true),
            BattleActorLayerCandidate(1, 481, 9, visible = true),
            BattleActorLayerCandidate(2, 480, 7, visible = true),
            BattleActorLayerCandidate(3, 483, 3, visible = false),
        )

        assertEquals(
            listOf(2, 1, 0),
            BattleActorLayerProjector.visibleSourceIndexes(candidates, dialogueBlendRoute = true),
        )
    }
}
