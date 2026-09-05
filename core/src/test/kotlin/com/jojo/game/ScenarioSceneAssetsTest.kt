package com.jojo.game

import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssetCache
import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioSceneAssetsTest {
    @Test
    fun cacheOwnsReplacedResourcesAndDisposesTheRemainingResourceOnce() {
        val released = mutableListOf<String>()
        val cache = ScenarioSceneAssetCache<String, String>(released::add)

        cache["maps/ui/panel.png"] = "panel"
        cache["maps/ui/panel.png"] = "replacement"

        assertEquals("replacement", cache["maps/ui/panel.png"])
        assertEquals(listOf("panel"), released)
        cache.dispose()
        cache.dispose()

        assertEquals(listOf("panel", "replacement"), released)
        assertEquals(null, cache["maps/ui/panel.png"])
    }
}
