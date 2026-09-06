// Test
package com.jojo.game

import com.jojo.game.presentation.battle.render.*

import kotlin.test.Test
import kotlin.test.assertEquals

/** BattleHealthPresentationTest: BattleHealthPresentation의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleHealthPresentationTest {
    @Test
    fun `HP display remains previous value until hit then uses only scheduled stage value`() {
        val presentation = BattleHealthPresentation()
        presentation.schedule("target", fromHp = 100, toHp = 70, revealAt = 1.25f)

        assertEquals(100, presentation.shownHp("target", 1.24f, fallbackHp = 40))
        assertEquals(70, presentation.shownHp("target", 1.25f, fallbackHp = 40))
        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        presentation.schedule("target", fromHp = 70, toHp = 40, revealAt = 2f)
        assertEquals(70, presentation.shownHp("target", 1.9f, fallbackHp = 40))
        assertEquals(40, presentation.shownHp("target", 2f, fallbackHp = 40))
    }
}
