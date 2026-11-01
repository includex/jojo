package com.jojo.game.infrastructure.data

import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.GlobalVariableDeclaration
import com.jojo.game.domain.scenario.GlobalVariableStore
import java.util.Base64
import kotlin.test.*

class GlobalVariablePersistenceTest {
    private val declarations = listOf(
        GlobalVariableDeclaration("campaign.chapter", "int", 0, "Chapter", 4057),
        GlobalVariableDeclaration("story.met_emperor", "bool", false, "Audience"),
        GlobalVariableDeclaration("story.legacy_flag", "bool", false, "Legacy flag", 100),
        GlobalVariableDeclaration("story.counter", "int", 3, "Counter"),
    )
    private fun state() = CampaignState(extensionResources = emptyList(), globalVariableDeclarations = declarations)
    private fun encoded(json: String) = Base64.getEncoder().encodeToString(json.toByteArray())

    @Test fun `names and numeric aliases share values and reset to defaults`() {
        val state = state()
        val globals = state.globalVariables
        globals[4057] = 23
        assertEquals(23, globals["campaign.chapter"])
        globals["campaign.chapter"] = 24
        assertEquals(24, globals[4057])
        globals["story.legacy_flag"] = true
        assertEquals(1, globals[100])
        globals[100] = 0
        assertEquals(false, globals["story.legacy_flag"])
        assertFailsWith<IllegalArgumentException> { globals[100] = 2 }
        assertFailsWith<IllegalArgumentException> { globals.entries.first { it.key == 100 }.setValue(2) }
        assertFailsWith<IllegalArgumentException> { globals["story.met_emperor"] = 1 }
        assertFailsWith<IllegalArgumentException> { globals["campaign.chapter"] = true }
        assertFailsWith<IllegalArgumentException> { globals["campaign.chaptr"] }
        assertFailsWith<IllegalArgumentException> { globals["campaign.chaptr"] = 3 }
        globals[77] = 9 // Unknown legacy scratch addresses retain compatibility.
        globals["story.counter"] = 12
        state.reset()
        assertEquals(0, globals["campaign.chapter"])
        assertEquals(3, globals["story.counter"])
        assertNull(globals[77])
    }

    @Test fun `named ints bools and unnamed legacy values survive saves`() {
        val original = state()
        original.globalVariables["campaign.chapter"] = 23
        original.globalVariables["story.met_emperor"] = true
        original.globalVariables[88] = 12
        val payload = CampaignRuntimeStateCodec(original).encode()
        val json = String(Base64.getDecoder().decode(payload))
        assertTrue(json.contains("\"campaign.chapter\":23"))
        assertFalse(json.contains("\"4057\""))
        val restored = state()
        assertTrue(CampaignRuntimeStateCodec(restored).restore(payload))
        assertEquals(23, restored.globalVariables[4057])
        assertEquals(true, restored.globalVariables["story.met_emperor"])
        assertEquals(12, restored.globalVariables[88])
    }

    @Test fun `old numeric saves migrate and missing variables use defaults`() {
        val state = state()
        state.globalVariables["story.met_emperor"] = true
        assertTrue(CampaignRuntimeStateCodec(state).restore(encoded("""{"globals":{"4057":8,"100":1,"77":6}}""")))
        assertEquals(8, state.globalVariables["campaign.chapter"])
        assertEquals(true, state.globalVariables["story.legacy_flag"])
        assertEquals(false, state.globalVariables["story.met_emperor"])
        assertEquals(3, state.globalVariables["story.counter"])
        assertEquals(6, state.globalVariables[77])
    }

    @Test fun `invalid named save or alias conflict fails without mutating campaign`() {
        val state = state()
        state.addMoney(7)
        state.globalVariables["campaign.chapter"] = 4
        val codec = CampaignRuntimeStateCodec(state)
        val sections = listOf(
            """"namedGlobals":{"version":2,"values":{}}""",
            """"namedGlobals":{"version":1,"values":{"story.unknown":1}}""",
            """"namedGlobals":{"version":1,"values":{"story.met_emperor":1}}""",
            """"namedGlobals":{"version":1,"values":{"campaign.chapter":2147483648}}""",
            """"namedGlobals":{"version":1,"values":{"campaign.chapter":1.0}}""",
            """"namedGlobals":{"version":1,"values":{"campaign.chapter":1,"campaign.chapter":2}}""",
            """"namedGlobals":{"version":1,"values":{"campaign.chapter":8}},"globals":{"4057":7}""",
            """"globals":{"100":2}""",
            """"globals":{"4057":7,"4057":8}""",
        )
        sections.forEach { section ->
            val payload = encoded("{\"money\":99,$section}")
            assertFalse(codec.canRestore(payload), section)
            assertFalse(codec.restore(payload), section)
            assertEquals(7, state.money)
            assertEquals(4, state.globalVariables["campaign.chapter"])
        }
    }

    @Test fun `registry rejects duplicate names aliases and bad defaults`() {
        assertFailsWith<IllegalArgumentException> { GlobalVariableStore(declarations + declarations.first()) }
        assertFailsWith<IllegalArgumentException> {
            GlobalVariableStore(declarations + GlobalVariableDeclaration("story.other", "int", 0, "Other", 4057))
        }
        assertFailsWith<IllegalArgumentException> { GlobalVariableDeclaration("story.flag", "bool", 0, "Flag") }
        assertTrue(GlobalVariableConfig.load().any { it.name == "campaign.current_chapter" && it.legacyId == 4057 })
    }
}
