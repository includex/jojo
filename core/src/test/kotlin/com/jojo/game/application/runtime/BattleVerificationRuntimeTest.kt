// Test
package com.jojo.game.application.runtime

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 전투 검증 런타임 테스트: 자동 검증 모드의 실행 조건을 확인한다. */
class BattleVerificationRuntimeTest {
    /** 검증 모드 판별: 튜토리얼과 스크립트 검증의 활성 상태를 확인한다. */
    @Test
    fun `verification modes expose their runtime policy`() {
        val tutorial = BattleVerificationRuntime(tutorial = true, scripted = false)
        val scripted = BattleVerificationRuntime(tutorial = false, scripted = true)
        val ordinary = BattleVerificationRuntime(tutorial = false, scripted = false)

        assertTrue(tutorial.active)
        assertTrue(tutorial.usesTutorialBattle)
        assertFalse(tutorial.usesScriptedBattle)
        assertTrue(scripted.active)
        assertTrue(scripted.usesScriptedBattle)
        assertFalse(ordinary.active)
        assertFalse(ordinary.isReady(0.8f))
        assertTrue(ordinary.isReady(0.81f))
    }
}
