// Test
package com.jojo.game

import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.infrastructure.data.GameDataCatalog
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * FeatsProgressContractTest: 공훈 화면 다섯 줄이 원본 `Unit`의 계산을 따르는지 검증한다.
 *
 * 갓 시작한 조조의 값은 원본 하네스가 잰 공훈 창과 같다(적성 41·49·46·40·42, 모은 공훈 0,
 * 다음 목표 100, 다음 단계 127). 예전에는 이 넷을 코드에 적어 두었기 때문에 다른 무장이나
 * 공훈이 쌓인 뒤에는 어긋났다.
 */
class FeatsProgressContractTest {
    @Test
    fun `a fresh Cao Cao matches the source feats window`() {
        val rows = GameDataCatalog.load().featsProgress(0, null)

        assertEquals(listOf(41, 49, 46, 40, 42), rows.map { it.aptitude })
        assertEquals(List(5) { 0 }, rows.map { it.progress })
        assertEquals(List(5) { 100 }, rows.map { it.nextProgress })
        assertEquals(List(5) { 127 }, rows.map { it.nextPhase })
    }

    @Test
    fun `accumulated feats and a raised aptitude move the row off its defaults`() {
        // 저장 자료가 값을 들고 있으면 그 값을 쓴다. 무력 적성 90은 능력 단계를 올리고,
        // 그만큼 다음 목표가 100을 넘어선다.
        val campaign = CampaignState()
        campaign.setUnitAttribute(0, 9, 90)
        campaign.setUnitAttribute(0, 28, 37)

        val row = GameDataCatalog.load().featsProgress(0, campaign).first()

        assertEquals(90, row.aptitude)
        assertEquals(37, row.progress)
        assertEquals(162, row.nextProgress)
    }
}
