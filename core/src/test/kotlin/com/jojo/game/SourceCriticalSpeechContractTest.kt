package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class SourceCriticalSpeechContractTest {
    private val data by lazy(GameDataCatalog::load)

    @Test
    fun `named critical lines match source criIds lookup`() {
        assertEquals(listOf("내 이 기술을 받아라! 이것이 바로 황천지검이다!"), data.unitProfile(0)?.criticalSpeech?.texts)
        assertEquals(listOf("이것은 만민의 분노입니다!"), data.unitProfile(32)?.criticalSpeech?.texts)
        assertEquals(false, data.unitProfile(32)?.criticalSpeech?.randomized)
    }

    @Test
    fun `generic units retain source three-line CRIMSG group`() {
        assertEquals(listOf("응응응...!", "으윽...!", "후우후……!"), data.unitProfile(146)?.criticalSpeech?.texts)
        assertEquals(listOf("오호호...!", "하아……!", "아악……!"), data.unitProfile(210)?.criticalSpeech?.texts)
        assertEquals(true, data.unitProfile(146)?.criticalSpeech?.randomized)
    }
}
