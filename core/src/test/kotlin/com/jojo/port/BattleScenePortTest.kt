package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleScenePortTest {
    private class Layer : BattleScenePort.BattleLayer {
        val filterCalls = mutableListOf<Int>()
        override fun save(out: MutableMap<String, Any?>) { out["battle"] = 7 }
        override fun filterUnits(flag: Int): List<Any?> {
            filterCalls += flag
            return listOf(flag)
        }
    }

    @Test
    fun `Battle scene creates only BattleLayer and saves nested model before callback`() {
        val layer = Layer()
        val calls = mutableListOf<String>()
        val scene = BattleScenePort(
            factory = object : BattleScenePort.Factory {
                override fun addBattleLayer(data: Any?): BattleScenePort.BattleLayer { calls += "layer:$data"; return layer }
                override fun addForcesList(mine: List<Any?>, enemy: List<Any?>, flag: Int) = Unit
                override fun stringify(value: Map<String, Any?>): String {
                    assertEquals(mapOf("battle" to 7, "model" to mapOf("unit" to 2)), value)
                    return "json"
                }
            },
            model = BattleScenePort.Model { it["unit"] = 2 },
            manager = BattleScenePort.Manager { index, json -> calls += "save:$index:$json" },
            battleLayerResource = "battle-prefab",
            battleInitLayer = "init",
            miniMapLayer = "mini",
            noticeInfoLayer = "notice",
        )

        scene.onCreate("stage")
        scene.saveGame(BattleScenePort.SaveRequest(3) { calls += "callback" })

        assertEquals(listOf("layer:stage", "save:3:json", "callback"), calls)
        assertEquals("battle-prefab", scene.getResource(BattleScenePort.Layer.BATTLE_LAYER))
        assertEquals("init", scene.getResource(BattleScenePort.Layer.BATTLE_INIT_LAYER))
        assertEquals("mini", scene.getResource(BattleScenePort.Layer.MINI_MAP_LAYER))
        assertEquals("notice", scene.getResource(BattleScenePort.Layer.NOTICE_INFO_LAYER))
        assertEquals(null, scene.getResource(BattleScenePort.Layer.ROUND_LAYER))
    }

    @Test
    fun `SHOW_CHARACTER_LIST uses source flags and ForcesList flag one`() {
        val layer = Layer()
        var added: Triple<List<Any?>, List<Any?>, Int>? = null
        val scene = BattleScenePort(
            factory = object : BattleScenePort.Factory {
                override fun addBattleLayer(data: Any?) = layer
                override fun addForcesList(mine: List<Any?>, enemy: List<Any?>, flag: Int) { added = Triple(mine, enemy, flag) }
                override fun stringify(value: Map<String, Any?>) = "{}"
            },
            model = BattleScenePort.Model {}, manager = BattleScenePort.Manager { _, _ -> },
        )

        scene.onCreate(null)
        scene.showCharacterList()

        assertEquals(listOf(1187, 1196), layer.filterCalls)
        assertEquals(Triple(listOf(1187), listOf(1196), 1), added)
    }
}
