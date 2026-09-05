package com.jojo.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align

/** Stateless rendering of story-only visual layers. */
internal object ScenarioStoryRenderer {
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
