package com.jojo.game

import com.jojo.game.application.battle.Battle
import com.jojo.game.domain.battle.BattlePropertyItem
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.infrastructure.data.CampaignStore
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.infrastructure.preferences.InMemoryPreferences
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BattleCarriedItemTest {
    @Test fun `carried medicine transfers saves and heals by its catalog value`() {
        val profile = requireNotNull(GameDataCatalog.load().equipmentProfile(216))
        assertEquals(26, profile.itemType)
        assertEquals(100, profile.value)

        val preferences = InMemoryPreferences()
        val store = CampaignStore(preferences)
        val inventory = store.state.inventory
        inventory.addUnitItem(513, 216, 4)
        assertEquals(4, inventory.items[216])
        assertEquals(0, inventory.sharedItemCount(216))
        assertFalse(inventory.consumeItem(216))
        assertFalse(inventory.consumeBattleItem(999, 216))
        assertFalse(inventory.transferUnitItem(512, 513, 216))
        assertFalse(inventory.transferUnitItem(513, 512, 216, 5))
        assertEquals(4, inventory.unitItemCount(513, 216))
        assertTrue(inventory.transferUnitItem(513, 512, 216))
        assertEquals(3, inventory.unitItemCount(513, 216))
        assertEquals(1, inventory.unitItemCount(512, 216))
        store.persist()

        val restored = CampaignStore(preferences).state.inventory
        assertEquals(4, restored.items[216])
        assertEquals(3, restored.unitItemCount(513, 216))
        assertEquals(1, restored.unitItemCount(512, 216))
        assertEquals(0, restored.sharedItemCount(216))

        val battle = Battle(
            units = listOf(
                BattleUnit("lin", "Lin", Faction.PLAYER, 5, 5, hitPoints = 25, maxHitPoints = 150, characterId = 512),
                BattleUnit("lu", "Lu", Faction.PLAYER, 6, 5, characterId = 513),
                BattleUnit("enemy", "Enemy", Faction.ENEMY, 9, 9),
            ),
            events = emptyList(),
            propertyItems = mapOf(216 to BattlePropertyItem(216, profile.name, profile.itemType, profile.value)),
            consumePropertyForUnit = { unit, itemId -> restored.consumeBattleItem(unit.characterId, itemId) },
        )
        val result = battle.combat.useProperty("lin", "lin", 216)
        assertIs<TacticalActionResult.Item>(result)
        assertEquals(125, battle.units.getValue("lin").hitPoints)
        assertEquals(0, restored.unitItemCount(512, 216))
        assertEquals(3, restored.unitItemCount(513, 216))
        assertEquals(3, restored.items[216])
        assertEquals(0, restored.sharedItemCount(216))
    }
}
