package com.jojo.game.verification.fixture

import com.jojo.game.domain.battle.Faction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class SpriteCaptureFixtureStartupExtensionTest {
    @Test fun `sprite request parses authored fields and factions`() {
        assertEquals(SpriteFixtureRequest(11, 2, 3, 40, Faction.ENEMY), parseSpriteFixtureRequest("sprite:11:2:3:40:2"))
        assertEquals(Faction.PLAYER, parseSpriteFixtureRequest("sprite:1:2:3:4:0")?.faction)
        assertEquals(Faction.REINFORCEMENTS, parseSpriteFixtureRequest("sprite:1:2:3:4:3")?.faction)
        assertNull(parseSpriteFixtureRequest("battle-view-fixture"))
    }

    @Test fun `sprite request rejects invalid forms`() {
        assertFailsWith<IllegalArgumentException> { parseSpriteFixtureRequest("sprite:1:2") }
        assertFailsWith<IllegalStateException> { parseSpriteFixtureRequest("sprite:1:2:3:4:9") }
    }
}
