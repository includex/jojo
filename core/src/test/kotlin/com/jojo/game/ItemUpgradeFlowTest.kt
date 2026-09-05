package com.jojo.game
import com.jojo.game.domain.campaign.*

import com.jojo.game.domain.campaign.CampaignEquipmentSlot

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * class  `ItemUpgradeFlowTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ItemUpgradeFlowTest {
    private fun request() = CampaignEquipmentExperienceResult(
        unitId = 32,
        slot = CampaignEquipmentSlot.WEAPON,
        itemId = 0,
        gained = 1,
        oldLevel = 2,
        newLevel = 3,
        oldExperience = 99,
        newExperience = 100,
        oldValue = 20,
        newValue = 30,
    )

    @Test fun `actual campaign equipment mutation retains max-level exp`() {
        val data = GameDataCatalog.load()
        val campaign = CampaignState()
        campaign.inventory.setEquipment(32, CampaignEquipment(2, 2, 72, 1, 111, weaponExperience = 99))

        val result = assertNotNull(campaign.equipmentProgression.grantBattleExperience(32, 1, 1, 1, true, data))

        assertTrue(result.leveledUp)
        assertEquals(2, result.oldLevel)
        assertEquals(3, result.newLevel)
        assertEquals(20, result.oldValue)
        assertEquals(30, result.newValue)
        assertEquals(100, campaign.inventory.equipment.getValue(32).weaponExperience)
    }

    @Test fun `panel cancel completes callback exactly once`() {
        var callbacks = 0
        val flow = ItemUpgradeFlow(request(), "유비", "단검", "공격력") { callbacks++ }
        flow.panelCancelTouchEnd()
        flow.panelCancelTouchEnd()
        flow.update(3f)
        assertFalse(flow.attached)
        assertEquals(1, flow.completionCount)
        assertEquals(1, callbacks)
    }

    @Test fun `timer stays attached before three seconds and then completes`() {
        var callbacks = 0
        val flow = ItemUpgradeFlow(request(), "유비", "단검", "공격력") { callbacks++ }
        flow.update(2.999f)
        assertTrue(flow.attached)
        flow.update(.001f)
        assertFalse(flow.attached)
        assertEquals(1, callbacks)
    }
}
