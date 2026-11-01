package com.jojo.game

import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.jojo.game.infrastructure.data.GameDataCatalogCombatDomain
import com.jojo.game.infrastructure.data.GameDataCatalogEquipmentDomain
import com.jojo.game.infrastructure.data.GameDataCatalogUnitDomain
import com.jojo.game.infrastructure.data.GameDataTableBundle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SparseGameDataCatalogTest {
    @Test fun `absent indexed slots stay absent in catalog lookups and lists`() {
        val reader = JsonReader()
        fun rows(json: String): List<JsonValue> =
            generateSequence(reader.parse(json).child) { it.next }.toList()
        val absent = rows("[null]")
        val tables = GameDataTableBundle(
            units = rows("""[null,null,{"0":"임충"}]"""),
            arms = absent, posts = absent, hitAreas = absent, effectAreas = absent,
            magics = absent, items = rows("""[null,{"0":"철창"}]"""),
            itemSkills = reader.parse("{}"), unitPostSkills = absent, defineSkills = absent,
            shops = absent, config = reader.parse("{}"), gameConfig = reader.parse("{}"),
        )
        val combat = GameDataCatalogCombatDomain(tables)
        val units = GameDataCatalogUnitDomain(tables, combat)
        val equipment = GameDataCatalogEquipmentDomain(tables)
        assertNull(units.unitProfile(0))
        assertEquals(listOf(2), units.allUnitIds())
        assertEquals(listOf("임충"), units.allUnitNames())
        assertNull(equipment.equipmentProfile(0))
        assertEquals(listOf(1), equipment.allEquipmentProfiles().map { it.id })
        assertNull(combat.magicProfile(0))
        assertEquals(emptyList(), combat.allMagicProfiles())
        assertNull(units.armProfile(0))
        assertEquals(emptyMap(), units.skillsForUnit(0, 0, null))
    }
}
