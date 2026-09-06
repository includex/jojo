// Test
package com.jojo.game

import com.jojo.game.domain.battle.*


import com.jojo.game.presentation.battle.evidence.BattleCompositionAction
import com.jojo.game.presentation.battle.evidence.BattleCompositionEvidenceRecorder
import com.jojo.game.presentation.battle.evidence.BattleCompositionEvidenceView
import com.jojo.game.presentation.battle.evidence.BattleCompositionScenario
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BattleCompositionEvidenceRecorderTest {
    @Test
    fun `composition schema keeps ordered static records and terminal fields`() {
        val json = BattleCompositionEvidenceRecorder.record(
            BattleCompositionEvidenceView(
                scenarioKey = "natural-r00",
                animationClock = 0f,
                visualAnimationClock = 1.25f,
                tracedMapBottom = -96,
                units = emptyList(),
                masks = emptyList(),
                scenario = BattleCompositionScenario(),
                naturalSay = true,
                modal = false,
                effectCount = 3,
            )
        )

        assertTrue(json.startsWith("{\"state\":\"R_00/natural-battle/t=stable\",\"scenarioKey\":\"natural-r00\",\"oracle\":\"isolated-libgdx-runtime\",\"animationClock\":0.0,\"visualAnimationClock\":1.25,\"records\":["))
        assertTrue(json.indexOf("Battle/Canvas/Layer/ScrollView/view/content/map\"") < json.indexOf("Battle/Canvas/Layer/bg/map\""))
        assertTrue(json.indexOf("bg/map/tiled#0") < json.indexOf("bg/map/tiled#18"))
        assertTrue(json.contains("maps/ui/battle-say.png"))
        assertTrue(json.endsWith(",\"modal\":false,\"effectCount\":3}"))
    }

    @Test
    fun `scenario records preserve branch order and values`() {
        val json = BattleCompositionEvidenceRecorder.record(
            BattleCompositionEvidenceView(
                scenarioKey = "battle-action-6-f0",
                animationClock = 2f,
                visualAnimationClock = 2f,
                tracedMapBottom = -560,
                units = emptyList(),
                masks = emptyList(),
                scenario = BattleCompositionScenario(
                    action = BattleCompositionAction(sourceAction = 6, direction = 2, active = true)
                ),
                naturalSay = false,
                modal = true,
                effectCount = 0,
            )
        )

        assertEquals(1, Regex("\\\"kind\\\":\\\"BRAnime\\\"").findAll(json).count())
        assertTrue(json.contains("\"draw\":[-320,-560,1920,1920]"))
        assertTrue(json.contains("\"action\":6,\"direction\":2,\"active\":true,\"timeline\":\"attack6-f0\""))
        assertTrue(json.endsWith(",\"modal\":true,\"effectCount\":0}"))
    }
}
