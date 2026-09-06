// Test
package com.jojo.game

import com.jojo.game.infrastructure.preferences.InMemoryPreferences
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.infrastructure.data.CampaignStore
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.campaign.CampaignInventory
import com.jojo.game.domain.campaign.CampaignRoster
import com.jojo.game.domain.campaign.CampaignEquipmentProgression
import com.jojo.game.domain.campaign.CampaignEquipmentRepository
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** CampaignDomainCollaboratorsTest: CampaignDomainCollaborators의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class CampaignDomainCollaboratorsTest {
    private val data = GameDataCatalog.load()

    @Test fun `published collection views cannot bypass collaborator commands`() {
        val inventory = CampaignInventory().apply {
            addItem(3)
            restoreDiscoveredTreasures(listOf(9))
            setEquipment(0, 1, 1, 1, 1, 1)
        }
        val roster = CampaignRoster().apply { restoreBattleRoster(listOf(0)) }

        assertFailsWith<UnsupportedOperationException> {
            (inventory.items as MutableMap<Int, Int>)[3] = 99
        }
        assertFailsWith<UnsupportedOperationException> {
            (inventory.discoveredTreasures as MutableSet<Int>).add(10)
        }
        assertFailsWith<UnsupportedOperationException> {
            (inventory.equipment as MutableMap<Int, CampaignEquipment>).clear()
        }
        assertFailsWith<UnsupportedOperationException> {
            (roster.battleRoster as MutableList<Int>).add(7)
        }
    }

    @Test fun `inventory preserves insertion order and equips the newest instance first`() {
        val inventory = CampaignInventory()
        inventory.setEquipment(0, 1, 1, 1, 1, 1)
        inventory.addItem(3, level = 2, experience = 7)
        inventory.addItem(3, level = 5, experience = 9)
        inventory.addItem(6, level = 4, experience = 1)

        assertEquals(listOf(3, 6), inventory.items.keys.toList())
        assertNotNull(inventory.equipInventoryItem(0, 3, data))
        assertEquals(5, inventory.equipment.getValue(0).weaponLevel)
        assertEquals(9, inventory.equipment.getValue(0).weaponExperience)
        assertEquals(listOf(2), inventory.itemLevels(3))
        assertEquals(listOf(7), inventory.itemExperiences(3))
    }

    @Test fun `equipment progression mutates only its narrow repository`() {
        val stored = linkedMapOf(
            7 to CampaignEquipment(2, 1, 1, 1, 1, weaponExperience = 0),
        )
        val repository = object : CampaignEquipmentRepository {
            override fun equipmentFor(unitId: Int) = stored[unitId]
            override fun storeEquipment(unitId: Int, equipment: CampaignEquipment) {
                stored[unitId] = equipment
            }
        }
        val progression = CampaignEquipmentProgression(repository)

        val result = assertNotNull(
            progression.grantExperienceAmount(7, 1, CampaignEquipmentSlot.WEAPON, data),
        )

        assertEquals(1, result.gained)
        assertEquals(result.newExperience, stored.getValue(7).weaponExperience)
    }

    @Test fun `roster keeps authored direct fast path before UI caps`() {
        val joined = linkedSetOf(0, 8, 22)
        val roster = CampaignRoster { joined }

        val direct = roster.configureBattleRoster(
            ScenarioJoinBattleLimit(1, 2, requiredUnitIds = listOf(8), excludedUnitIds = emptyList()),
        )

        assertEquals(listOf(0, 8), direct.directBattleRoster)
        assertEquals(listOf(0, 8), roster.battleRoster)
        assertFalse(roster.setBattleRoster(listOf(22), direct.selectionLimit))
        assertTrue(roster.setBattleRoster(listOf(8, 0), direct.selectionLimit))
        assertEquals(listOf(8, 0), roster.battleRoster)
    }

    @Test fun `aggregate reset clears every collaborator`() {
        val state = CampaignState().apply {
            joinedUnits += 0
            inventory.addItem(3)
            inventory.restoreDiscoveredTreasures(listOf(9))
            inventory.setEquipment(0, 2, 1, 1, 1, 1)
            roster.restoreBattleRoster(listOf(0))
        }

        state.reset()

        assertTrue(state.inventory.items.isEmpty())
        assertTrue(state.inventory.discoveredTreasures.isEmpty())
        assertTrue(state.inventory.equipment.isEmpty())
        assertTrue(state.roster.battleRoster.isEmpty())
    }

    @Test fun `save restore retains extracted state order and equipment instance stack`() {
        val preferences = InMemoryPreferences()
        CampaignStore(preferences).apply {
            state.joinedUnits += 0
            state.inventory.addItem(3, level = 2, experience = 7)
            state.inventory.addItem(3, level = 5, experience = 9)
            state.inventory.addItem(6, level = 4, experience = 1)
            state.inventory.restoreDiscoveredTreasures(listOf(9, 4))
            state.inventory.setEquipment(0, 1, 1, 1, 1, 1)
            state.roster.restoreBattleRoster(listOf(0, 8))
            persist()
        }

        val restored = CampaignStore(preferences).state

        assertEquals(listOf(3, 6), restored.inventory.items.keys.toList())
        assertEquals(listOf(9, 4), restored.inventory.discoveredTreasures.toList())
        assertEquals(listOf(0, 8), restored.roster.battleRoster)
        assertNotNull(restored.inventory.equipInventoryItem(0, 3, data))
        assertEquals(5, restored.inventory.equipment.getValue(0).weaponLevel)
        assertEquals(9, restored.inventory.equipment.getValue(0).weaponExperience)
        assertEquals(listOf(2), restored.inventory.itemLevels(3))
    }
}
