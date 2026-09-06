// Test
package com.jojo.game

import com.jojo.game.domain.battle.*
import com.jojo.game.application.battle.bootstrap.BattleSceneCoordinator


import kotlin.test.Test
import kotlin.test.assertEquals

/** BattleSceneCoordinatorTest: BattleSceneCoordinator의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleSceneCoordinatorTest {
    private class Layer : BattleSceneCoordinator.BattleScreen {
        val filterCalls = mutableListOf<Int>()
        override fun save(out: MutableMap<String, Any?>) { out["battle"] = 7 }
        override fun filterUnits(flag: Int): List<Any?> {
            filterCalls += flag
            return listOf(flag)
        }
    }

    @Test
    fun `Battle scene creates only BattleScreen and saves nested model before callback`() {
        val layer = Layer()
        val calls = mutableListOf<String>()
        val scene = BattleSceneCoordinator(
            factory = object : BattleSceneCoordinator.Factory {
                override fun addBattleScreen(data: Any?): BattleSceneCoordinator.BattleScreen { calls += "layer:$data"; return layer }
                override fun addForcesList(mine: List<Any?>, enemy: List<Any?>, flag: Int) = Unit
                override fun stringify(value: Map<String, Any?>): String {
                    assertEquals(mapOf("battle" to 7, "model" to mapOf("unit" to 2)), value)
                    return "json"
                }
            },
            model = BattleSceneCoordinator.Model { it["unit"] = 2 },
            manager = BattleSceneCoordinator.Manager { index, json -> calls += "save:$index:$json" },
            battleLayerResource = "battle-prefab",
            battleInitLayer = "init",
            miniMapLayer = "mini",
            noticeInfoLayer = "notice",
        )

        scene.onCreate("stage")
        scene.saveGame(BattleSceneCoordinator.SaveRequest(3) { calls += "callback" })

        assertEquals(listOf("layer:stage", "save:3:json", "callback"), calls)
        assertEquals("battle-prefab", scene.getResource(BattleSceneCoordinator.Layer.BATTLE_LAYER))
        assertEquals("init", scene.getResource(BattleSceneCoordinator.Layer.BATTLE_INIT_LAYER))
        assertEquals("mini", scene.getResource(BattleSceneCoordinator.Layer.MINI_MAP_LAYER))
        assertEquals("notice", scene.getResource(BattleSceneCoordinator.Layer.NOTICE_INFO_LAYER))
        assertEquals(null, scene.getResource(BattleSceneCoordinator.Layer.ROUND_LAYER))
    }

    @Test
    fun `SHOW_CHARACTER_LIST uses source flags and ForcesList flag one`() {
        val layer = Layer()
        var added: Triple<List<Any?>, List<Any?>, Int>? = null
        val scene = BattleSceneCoordinator(
            factory = object : BattleSceneCoordinator.Factory {
                override fun addBattleScreen(data: Any?) = layer
                override fun addForcesList(mine: List<Any?>, enemy: List<Any?>, flag: Int) { added = Triple(mine, enemy, flag) }
                override fun stringify(value: Map<String, Any?>) = "{}"
            },
            model = BattleSceneCoordinator.Model {}, manager = BattleSceneCoordinator.Manager { _, _ -> },
        )

        scene.onCreate(null)
        scene.showCharacterList()

        assertEquals(listOf(1187, 1196), layer.filterCalls)
        assertEquals(Triple(listOf(1187), listOf(1196), 1), added)
    }
}
