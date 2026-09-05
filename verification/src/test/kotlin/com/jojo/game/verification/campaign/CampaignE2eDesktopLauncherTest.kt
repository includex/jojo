package com.jojo.game.verification.campaign

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CampaignE2eDesktopLauncherTest {
    @Test
    fun `default task options are bounded headless-window smoke settings`() {
        val options = CampaignE2eLaunchOptions.parse(emptyArray())

        assertEquals("R_00", options.traceConfig.stopAt.module)
        assertEquals(1, options.traceConfig.stopAt.sceneIndex)
        assertEquals(60f, options.traceConfig.maxSeconds)
        assertFalse(options.traceConfig.requireYingchuanBootstrapContract)
        assertFalse(options.visible)
    }

    @Test
    fun `full route arguments wire the observer configuration without desktop dependencies`() {
        val options = CampaignE2eLaunchOptions.parse(
            arrayOf("--output=build/full.json", "--stop=R_01:1", "--assert-bootstrap", "--visible"),
        )

        assertEquals("build/full.json", options.traceConfig.outputPath)
        assertEquals("R_01", options.traceConfig.stopAt.module)
        assertTrue(options.traceConfig.requireYingchuanBootstrapContract)
        assertTrue(options.visible)
        CampaignE2eRuntimeObserver(options.traceConfig).scenarioStarted("R_00", 0)
    }
}
