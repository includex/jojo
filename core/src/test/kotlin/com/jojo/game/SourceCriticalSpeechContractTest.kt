// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import kotlin.test.Test
import kotlin.test.assertEquals

/** SourceCriticalSpeechContractTest: SourceCriticalSpeechContract의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class SourceCriticalSpeechContractTest {
    private val data by lazy(GameDataCatalog::load)

    @Test
    fun `named critical lines match source criIds lookup`() {
        assertEquals(listOf("받아라! 이것이 황천의 검이다!"), data.unitProfile(0)?.criticalSpeech?.texts)
        assertEquals(listOf("이것이 만백성의 분노다!"), data.unitProfile(32)?.criticalSpeech?.texts)
        assertEquals(false, data.unitProfile(32)?.criticalSpeech?.randomized)
    }

    @Test
    fun `generic units retain source three-line CRIMSG group`() {
        assertEquals(listOf("으으음……!", "으으으……!", "후후후……!"), data.unitProfile(146)?.criticalSpeech?.texts)
        assertEquals(listOf("으오오……!", "하아아……!", "이야아……!"), data.unitProfile(210)?.criticalSpeech?.texts)
        assertEquals(true, data.unitProfile(146)?.criticalSpeech?.randomized)
    }
}
