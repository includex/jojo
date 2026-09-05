package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class SourceArmProfileContractTest {
    @Test
    fun `original arm profiles preserve ATTACKDELAY field`() {
        // The field is read directly from arms[6], not inferred from remote
        // attack or the avatar asset name.
        val delayed = GameDataCatalog.ArmProfile(1, "병종", 0, false, true, 100, 0, emptyMap(), emptyMap(), emptyMap())
        val ordinary = GameDataCatalog.ArmProfile(2, "병종", 0, false, false, 100, 0, emptyMap(), emptyMap(), emptyMap())
        assertEquals(true, delayed.attackDelay)
        assertEquals(false, ordinary.attackDelay)
    }
}
