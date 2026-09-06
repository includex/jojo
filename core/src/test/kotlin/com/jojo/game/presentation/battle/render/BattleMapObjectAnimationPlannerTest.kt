// Battle Render Test
package com.jojo.game.presentation.battle.render

import kotlin.test.Test
import kotlin.test.assertEquals

/** 맵 오브젝트 계획기가 게이트 배치와 화염·선택 오브젝트 atlas 행을 원본 시간 규칙으로 계산하는지 검증한다. */
class BattleMapObjectAnimationPlannerTest {
    /** 게이트 영역: 중앙 타일 좌표에서 좌하단 한 타일을 기준으로 3×3 타일 크기를 계산한다. */
    @Test
    fun `게이트의 3x3 타일 영역을 계산한다`() {
        assertEquals(
            BattleMapObjectBounds(left = 192f, bottom = 288f, size = 288f),
            BattleMapObjectAnimationPlanner.gateBounds(0f, 480f, 96f, 3, 1),
        )
    }

    /** atlas 행: 화염과 object ID별 반복 행 시작 위치를 원본 timeline 행으로 변환한다. */
    @Test
    fun `화염과 선택 오브젝트 atlas 행을 계산한다`() {
        assertEquals(0, BattleMapObjectAnimationPlanner.fireSourceY(0f))
        assertEquals(192, BattleMapObjectAnimationPlanner.objectSourceY(1, 0f))
        assertEquals(288, BattleMapObjectAnimationPlanner.objectSourceY(2, 0f))
    }
}
