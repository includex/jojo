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

    /** drawStreetDialogue: 거리 장면의 대화 상자와 화자 초상화를 렌더링한다. */
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
        /**
         * `dialogueY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val dialogueY = if (view.isAtTop) 373.24f else 0f
        /**
         * `panelX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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
                val bounds = DialoguePortraitGeometry.fit(
                    texture,
                    if (view.isLeft) 84.8199f else 1030.2742f,
                    53.32f + dialogueY,
                    165.12f,
                    206.4f,
                )
                batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height)
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
