// Scenario
package com.jojo.game.presentation.scenario.story

import com.jojo.game.presentation.scenario.overlay.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.shared.dialogue.DialoguePortraitGeometry

/** ScenarioStoryRenderer: 궁전 고정 장면과 거리 대사 연출을 전용 view에 따라 그린다. */
internal object ScenarioStoryRenderer {
    /** drawPalaceFixture: 궁전 장면의 고정 오브젝트와 장식 요소를 렌더링한다. */
    fun drawPalaceFixture(assets: ScenarioSceneAssets, batch: SpriteBatch, view: ScenarioPalaceFixtureView) {
        batch.begin()
        batch.setBlendFunctionSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA)
        batch.color = Color.WHITE
        assets.portraitTexture(view.portraitId)?.let { texture ->
            val bounds = DialoguePortraitGeometry.fit(texture, 98.628f * .86f, 496f * .86f, 192f * .86f, 240f * .86f)
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height)
        }
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

}
