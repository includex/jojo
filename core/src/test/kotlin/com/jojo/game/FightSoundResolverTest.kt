package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FightSoundResolverTest {
    @Test
    fun `yidong maps source arm move types and preserves silent values`() {
        assertEquals(24, FightSoundResolver.resolve("yidong", 0).effectId)
        assertEquals(25, FightSoundResolver.resolve("yidong", 1).effectId)
        assertEquals(23, FightSoundResolver.resolve("yidong", 2).effectId)
        assertNull(FightSoundResolver.resolve("yidong", 3).effectId)
        assertNull(FightSoundResolver.resolve("yidong", -1).effectId)
        assertEquals(24, FightSoundResolver.resolve("yidong", 4).effectId)
    }

    @Test
    fun `numeric callback routes effects and greater-than-300 background IDs`() {
        assertEquals(8, FightSoundResolver.resolve("8").effectId)
        assertNull(FightSoundResolver.resolve("8").backgroundId)
        assertEquals(19, FightSoundResolver.resolve("319").backgroundId)
        assertNull(FightSoundResolver.resolve("319").effectId)
    }
}
