// Test
package com.jojo.game

import com.jojo.game.presentation.battle.assets.*

import kotlin.test.Test
import kotlin.test.assertEquals

/** BattleDynamicTexturePathsTest: BattleDynamicTexturePaths의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleDynamicTexturePathsTest {
    @Test
    fun `movement candidates preserve three-stage fallback order`() {
        assertEquals(
            listOf(
                "maps/units/mov2/93.png",
                "maps/units/mov/93.png",
                "maps/units/93.png",
            ),
            BattleDynamicTexturePaths.movement(93),
        )
    }

    @Test
    fun `fight action candidates omit flat movement fallback`() {
        assertEquals(
            listOf("maps/units/mov2/20.png", "maps/units/mov/20.png"),
            BattleDynamicTexturePaths.action("mov", 20),
        )
        assertEquals(
            listOf("maps/units/atk2/20.png", "maps/units/atk/20.png"),
            BattleDynamicTexturePaths.action("atk", 20),
        )
    }
}
