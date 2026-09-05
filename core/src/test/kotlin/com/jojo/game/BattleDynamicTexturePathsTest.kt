package com.jojo.game

import com.jojo.game.presentation.battle.assets.*

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `BattleDynamicTexturePathsTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
