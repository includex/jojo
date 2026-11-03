package com.jojo.game.application.scenario

import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.*

class LuaScenarioRunnerTest {
    private fun runner(source:String)=ScenarioInterpreter("LUA_TEST",emptyMap(),CampaignState(),source)
    @Test fun `Lua locals survive dialogue choice and timed waits`() {
        val r=runner("""
            function scene0()
                local n = 40
                stage.say('&512\nFirst')
                n = n + stage.choice('One\nTwo', -1)
                stage.delay(1)
                stage.say('&512\nResult ' .. n)
            end
        """.trimIndent())
        r.start("scene0")
        assertEquals("lua",r.scriptLanguage)
        assertEquals("First",r.currentDialogue?.text)
        r.advanceDialogue()
        assertEquals(PlaybackState.CHOICE,r.state)
        r.selectChoice(1);r.confirmChoice()
        assertEquals(PlaybackState.DELAY,r.state)
        r.skipDelay()
        assertEquals("Result 42",r.currentDialogue?.text)
        r.advanceDialogue();assertEquals(PlaybackState.COMPLETE,r.state)
    }
    @Test fun `Lua rejects unknown host calls and bounds infinite loops`() {
        assertFails { runner("function scene0() stage.missingApi() end").start("scene0") }
        val failure=assertFails { runner("function scene0() while true do end end").start("scene0") }
        assertTrue(failure.message.orEmpty().contains("budget"))
    }
    @Test fun `Lua has no filesystem process or package capabilities`() {
        val r=runner("""
            function scene0()
                if io ~= nil or os ~= nil or luajava ~= nil or package ~= nil or require ~= nil or debug ~= nil then
                    error('sandbox leak')
                end
                stage.say('&512\nSafe')
            end
        """.trimIndent())
        r.start("scene0");assertEquals("Safe",r.currentDialogue?.text)
    }
}
