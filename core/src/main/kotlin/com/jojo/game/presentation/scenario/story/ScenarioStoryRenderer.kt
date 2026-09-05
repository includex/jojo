package com.jojo.game.presentation.scenario.story

import com.jojo.game.presentation.scenario.overlay.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** Stateless rendering of story-only visual layers. */
internal object ScenarioStoryRenderer {
    /** Draws the source Palace fixture's upper DialogueLayer while owning the batch lifecycle. */
    fun drawPalaceFixture(assets: ScenarioSceneAssets, batch: SpriteBatch, view: ScenarioPalaceFixtureView) {
        batch.begin()
        batch.setBlendFunctionSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA)
        batch.color = Color.WHITE
        assets.portraitTexture(view.portraitId)?.let { batch.draw(it, 98.628f * .86f, 496f * .86f, 192f * .86f, 240f * .86f) }
        assets.dialoguePanelTexture?.let { texture ->
            batch.draw(texture, 319.233f * .86f, 498.5f * .86f, 798f * .86f, 191f * .86f, 0, 0, texture.width, texture.height, true, false)
        }
        assets.streetDialogueFont.color = Color.BLACK
        assets.streetDialogueFont.draw(batch, view.dialogueText, 382.487f * .86f, (587.814f + 52.92f) * .86f)
        assets.streetSpeakerFont.color = Color.WHITE
        assets.streetSpeakerFont.draw(batch, view.speaker, 403.896f * .86f, (633.52f + 54.4f) * .86f)
        batch.end()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    }

    /** Draws the cumulative source DialogueLayer contents while [batch] is active. */
    fun drawStreetDialogue(
        assets: ScenarioSceneAssets,
        batch: SpriteBatch,
        view: ScenarioStreetDialogueView,
        index: Int,
    ) {
        batch.setBlendFunctionSeparate(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA,
            GL20.GL_ONE,
            GL20.GL_ONE_MINUS_SRC_ALPHA,
        )
        batch.color = Color.WHITE
        val dialogueY = if (view.isAtTop) 373.24f else 0f
        val panelX = if (view.isLeft) 274.54054f else 316.40878f
        assets.dialoguePanelTexture?.let { texture ->
            batch.draw(
                texture,
                panelX,
                55.47f + dialogueY,
                686.28f,
                164.26f,
                0,
                0,
                texture.width,
                texture.height,
                view.isLeft,
                false
            )
        }
        if (view.hasDialogue && view.portraitId != null && index >= 1) {
            assets.portraitTexture(view.portraitId)?.let { texture ->
                batch.draw(texture, if (view.isLeft) 84.8199f else 1030.2742f, 53.32f + dialogueY, 165.12f, 206.4f)
            }
        }
        if (view.hasDialogue && index >= 3) {
            assets.streetDialogueFont.color = Color.BLACK
            assets.streetDialogueFont.draw(
                batch,
                view.visibleText,
                if (view.isLeft) 328.93882f else 370.80706f,
                163.5f + dialogueY,
                626.08f,
                Align.left,
                true,
            )
        }
        if (view.hasDialogue && index >= 2) {
            assets.streetSpeakerFont.color = Color.WHITE
            assets.streetSpeakerFont.draw(
                batch,
                view.speaker,
                if (view.isLeft) 323.44676f else 365.315f,
                202.5f + dialogueY
            )
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    }
}
