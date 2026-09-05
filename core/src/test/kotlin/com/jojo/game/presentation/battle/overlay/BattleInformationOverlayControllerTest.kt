package com.jojo.game.presentation.battle.overlay

import com.jojo.game.PropertyLayer
import com.jojo.game.TerrainLayer
import com.jojo.game.TreasureLayer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BattleInformationOverlayControllerTest {
    @Test
    fun `property tab selection delegates to PropertyLayer and close is typed`() {
        val controller = controller()
        controller.openProperty()

        controller.dispatch(BattleInformationOverlayController.Intent.SelectPropertyTab(PropertyLayer.Tab.PROPERTY))
        assertEquals(PropertyLayer.Tab.PROPERTY.ordinal, controller.propertyView()?.selectedTab)

        val effect = controller.dispatch(BattleInformationOverlayController.Intent.Close).effect
        assertEquals(BattleInformationOverlayController.Mode.PROPERTY, assertIs<BattleInformationOverlayController.Effect.Closed>(effect).mode)
    }

    @Test
    fun `terrain and discovered treasure taps retain source-layer selection rules`() {
        val controller = controller()
        controller.openTerrain()
        controller.dispatch(BattleInformationOverlayController.Intent.SelectTerrainTab(TerrainLayer.Tab.EXPEND))
        assertEquals("1", controller.terrainView()?.rows?.single()?.values?.single()?.text)

        controller.openTreasure()
        controller.dispatch(BattleInformationOverlayController.Intent.Tap(300f, 600f))
        assertTrue(controller.treasureView()?.rows?.single()?.selected == true)
    }

    private fun controller() = BattleInformationOverlayController(
        propertyLayer = PropertyLayer(
            listOf(
                PropertyLayer.Item(1, "검", itemType = 0, icon = 1),
                PropertyLayer.Item(2, "약", itemType = 26, icon = 2),
            ),
            mapOf(1 to 1, 2 to 3),
        ),
        terrainLayer = TerrainLayer(
            terrain = listOf(TerrainLayer.Terrain(1, "평지", flag = 1, magic = 2)),
            arms = listOf(TerrainLayer.Arm(1, "창병", terrainRise = mapOf(1 to 100), terrainExpend = mapOf(1 to 1))),
        ),
        treasureLayer = TreasureLayer(
            listOf(TreasureLayer.Item(3, "옥새", icon = 3, property = true, description = "보물")),
            discovered = setOf(3),
        ),
        itemIcon = { null },
        terrainIcon = { null },
    )
}
