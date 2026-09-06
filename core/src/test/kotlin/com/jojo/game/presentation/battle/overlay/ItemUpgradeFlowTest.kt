// Test
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.campaign.*

import com.jojo.game.domain.campaign.CampaignEquipmentSlot

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** ItemUpgradeFlowTest: ItemUpgradeFlow의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

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
