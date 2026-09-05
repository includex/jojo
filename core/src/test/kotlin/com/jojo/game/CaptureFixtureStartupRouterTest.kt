package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * class  `CaptureFixtureStartupRouterTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class CaptureFixtureStartupRouterTest {
    @Test fun `sprite request parses all authored fields and camp`() {
        assertEquals(
            SpriteFixtureRequest(11, 2, 3, 40, Faction.ENEMY),
            parseSpriteFixtureRequest("sprite:11:2:3:40:2"),
        )
        assertEquals(Faction.PLAYER, parseSpriteFixtureRequest("sprite:1:2:3:4:0")?.faction)
        assertEquals(Faction.FRIEND, parseSpriteFixtureRequest("sprite:1:2:3:4:1")?.faction)
        assertEquals(Faction.REINFORCEMENTS, parseSpriteFixtureRequest("sprite:1:2:3:4:3")?.faction)
        assertNull(parseSpriteFixtureRequest("battle-view-fixture"))
    }

    @Test fun `sprite request rejects malformed field count and camp`() {
        assertFailsWith<IllegalArgumentException> { parseSpriteFixtureRequest("sprite:1:2") }
        assertFailsWith<IllegalStateException> { parseSpriteFixtureRequest("sprite:1:2:3:4:9") }
    }
}
