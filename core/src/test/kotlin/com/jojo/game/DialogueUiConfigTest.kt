package com.jojo.game

import com.jojo.game.presentation.shared.dialogue.DialogueRenderLayout
import com.jojo.game.presentation.shared.dialogue.DialogueUiConfig
import kotlin.test.*
import org.junit.jupiter.api.Test

class DialogueUiConfigTest {
    private fun config(scenario: String = "{}") = DialogueUiConfig.parse("version: 1\nprofiles:\n  scenario: $scenario\n  battle: {}\n")

    @Test fun defaultsPreserveExistingLayoutAndRasterLabels() {
        val profile = config().profile("scenario")
        assertEquals(DialogueRenderLayout(), profile.applyLayout(DialogueRenderLayout()))
        assertTrue(profile.legacyRaster)
        assertTrue(profile.followSpeaker)
    }

    @Test fun customLayoutAndFontsApplyWithoutRasterLabels() {
        val profile = config("{layout: {panelWidth: 700, textOffsetY: 99}, bodyFont: {size: 32, color: '#123456FF', bold: true}, legacyRaster: true}").profile("scenario")
        assertEquals(700f, profile.applyLayout(DialogueRenderLayout()).panelWidth)
        assertEquals(99f, profile.applyLayout(DialogueRenderLayout()).textOffsetY)
        assertEquals(32, profile.bodyFont.size)
        assertFalse(profile.legacyRaster)
    }

    @Test fun backgroundAndRuntimeAssetsAreValidated() {
        val c = config("{background: {leftImage: ui/panel.png, tint: '#FFFFFFFF', ninePatch: {enabled: true, left: 4, right: 4, top: 8, bottom: 8}}}")
        assertEquals(8, c.profile("scenario").background.ninePatch.top)
        assertFailsWith<IllegalArgumentException> { c.validateAssets { false } }
        c.validateAssets { it == "ui/panel.png" }
    }

    @Test fun rejectsUnknownKeysAndInvalidValues() {
        listOf("{layouts: {}}", "{bodyFont: {size: null}}", "{layout: {width: 1}}", "{layout: {speakerLabelScale: 2}}", "{layout: {panelWidht: 4}}", "{bodyFont: {size: 0}}", "{bodyFont: {scaleX: -1}}",
            "{bodyFont: {color: red}}", "{background: {leftImage: ../panel.png}}", "{legacyRaster: yesPlease}",
            "{layout: {panelWidth: .nan}}", "{layout: {choiceVisibleRows: 2.5}}", "{followSpeaker: 1}").forEach {
            assertFails("Must reject $it") { config(it) }
        }
        assertFails { DialogueUiConfig.parse("version: 2\nprofiles: {scenario: {}, battle: {}}") }
        assertFails { DialogueUiConfig.parse("version: 1\nprofiles: {scenario: {}}") }
        assertFails { DialogueUiConfig.parse("version: 1\nprofiles: {scenario: &x {}, battle: *x}") }
        assertFails { config("{legacyRaster: true, legacyRaster: false}") }
    }
}
