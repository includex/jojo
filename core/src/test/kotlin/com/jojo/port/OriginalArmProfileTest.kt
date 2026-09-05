package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class OriginalArmProfileTest {
    @Test
    fun `original arm profiles preserve ATTACKDELAY field`() {
        // The field is read directly from arms[6], not inferred from remote
        // attack or the avatar asset name.
        val delayed = OriginalGameData.ArmProfile(1, "병종", 0, false, true, 100, 0, emptyMap(), emptyMap(), emptyMap())
        val ordinary = OriginalGameData.ArmProfile(2, "병종", 0, false, false, 100, 0, emptyMap(), emptyMap(), emptyMap())
        assertEquals(true, delayed.attackDelay)
        assertEquals(false, ordinary.attackDelay)
    }
}
