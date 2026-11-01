package com.jojo.game.infrastructure.data

import kotlin.test.Test
import kotlin.test.assertEquals

class GamePackageTest {
    @Test
    fun `compiled package chooses routes and save namespace`() {
        val content = GamePackage.parse("""
            {"id":"custom","title":"Custom","startModule":"R_10","startScene":"opening",
             "resumeScene":"return","protagonistId":42,"saveNamespace":"custom-save",
             "defaultBattleModule":"S_00","battleModules":{"R_10":"S_10"}}
        """)
        assertEquals("S_10", content.battleModule("R_10"))
        assertEquals("S_00", content.battleModule("R_11"))
        assertEquals("R_10", content.returnModule("S_10"))
        assertEquals("custom-save", content.saveNamespace)
        assertEquals(42, content.protagonistId)
    }
}
