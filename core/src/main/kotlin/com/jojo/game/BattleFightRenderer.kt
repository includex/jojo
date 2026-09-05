package com.jojo.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.Viewport
import kotlin.math.abs

/** Draws one immutable FightLayer snapshot using resources owned by BattleScreen. */
internal class BattleFightRenderer(
    private val batch: SpriteBatch,
    private val font: BitmapFont,
    private val dialogueFont: BitmapFont,
    private val viewport: Viewport,
    private val hudAssets: BattleHudAssets,
    private val dynamicTextures: BattleDynamicTextureRepository,
    private val timeline: FightSpriteTimeline,
    private val highlightShader: () -> ShaderProgram,
    private val grayShader: () -> ShaderProgram,
) {
    /**
     * 공개 메서드 `draw`
     *
     * ### 파라미터
    - `view` (`FightPresentationView`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun draw(view: FightPresentationView) {
        val centerX = viewport.worldWidth / 2f
        val centerY = 400f
        val backgroundX = centerX - 624f
        val backgroundY = 16f
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        if (view.introBackgroundActive) {
            drawIntro(view, centerX, centerY, backgroundX, backgroundY, 1f - view.startCrossFade)
        }
        if (view.duelBackgroundActive) {
            hudAssets.fightBackgroundTextures[view.backgroundIndex + 1]?.let {
                batch.color = Color(1f, 1f, 1f, view.startCrossFade.coerceIn(0f, 1f))
                batch.draw(it, backgroundX, backgroundY, 1248f, 768f)
            }
            batch.color = Color.WHITE
            val slot0 = view.fighterAt(0)
            val slot1 = view.fighterAt(1)
            drawName(slot0, centerX - 434.932f, centerY + 266f, 260f, Align.left, Color(36f / 255f, 0f, 1f, 1f))
            drawName(
                slot1,
                centerX + 44.9904f,
                centerY - 262f,
                390f,
                Align.right,
                Color(227f / 255f, 3f / 255f, 3f / 255f, 1f)
            )
            font.data.setScale(130f / 26f)
            font.color = Color.WHITE
            font.draw(batch, "VS", centerX - 86.71f, centerY + 70f, 173.42f, Align.center, false)

            listOf(view.mine, view.enemy)
                .filter(FightFighterView::created)
                .sortedBy(FightFighterView::zIndex)
                .forEach { drawUnit(it, centerX, centerY) }
            drawSpeech(slot0, centerX, centerY)
            drawSpeech(slot1, centerX, centerY)
            if (view.startLabelsActive) {
                font.data.setScale(200f / 26f)
                font.color = Color.WHITE
                font.draw(batch, "승리", centerX - 320.46f, centerY + 90f, 200f, Align.center, false)
                font.draw(batch, "감소", centerX + 120.5216f, centerY + 90f, 200f, Align.center, false)
            }
        }
        font.data.setScale(1f)
        font.color = Color.WHITE
        batch.color = Color.WHITE
        batch.end()
    }

    private fun drawIntro(
        view: FightPresentationView,
        centerX: Float,
        centerY: Float,
        backgroundX: Float,
        backgroundY: Float,
        alpha: Float,
    ) {
        hudAssets.fightIntroTileTexture?.let { texture ->
            batch.color = Color(1f, 1f, 1f, alpha)
            for (row in 0 until 8) for (column in 0 until 13) {
                batch.draw(texture, backgroundX + column * 96f, backgroundY + row * 96f, 96f, 96f)
            }
        }
        if (view.startRevealGroup >= 1) {
            drawPortrait(view.fighterAt(0), centerX, centerY, -519f, 264f, alpha)
            drawIntroName(view.fighterAt(0), centerX, centerY, -519f, 153f, Color(0f, 41f / 255f, 1f, 1f), alpha)
        }
        if (view.startRevealGroup >= 2) {
            font.data.setScale(130f / 26f)
            font.color = Color(1f, 1f, 1f, alpha)
            font.draw(batch, "VS", centerX - 100f, centerY + 70f, 200f, Align.center, false)
        }
        if (view.startRevealGroup >= 3) {
            drawPortrait(view.fighterAt(1), centerX, centerY, 519f, -264f, alpha)
            drawIntroName(view.fighterAt(1), centerX, centerY, 519f, -153f, Color(1f, 151f / 255f, 0f, 1f), alpha)
        }
    }

    private fun drawPortrait(
        fighter: FightFighterView,
        centerX: Float,
        centerY: Float,
        localX: Float,
        localY: Float,
        alpha: Float,
    ) {
        val faceId = fighter.portraitFaceId ?: return
        val texture = dynamicTextures.head(faceId) ?: return
        val scale = 120f / maxOf(texture.width, texture.height) * 1.4f
        val width = texture.width * scale
        val height = texture.height * scale
        batch.color = Color(1f, 1f, 1f, alpha)
        val mask = Rectangle(centerX + localX - 64f, centerY + localY - 80f, 128f, 160f)
        val scissors = Rectangle()
        ScissorStack.calculateScissors(viewport.camera, batch.transformMatrix, mask, scissors)
        batch.flush()
        if (ScissorStack.pushScissors(scissors)) {
            batch.draw(texture, centerX + localX - width / 2f, centerY + localY - height / 2f, width, height)
            batch.flush()
            ScissorStack.popScissors()
        }
    }

    private fun drawIntroName(
        fighter: FightFighterView,
        centerX: Float,
        centerY: Float,
        localX: Float,
        localY: Float,
        color: Color,
        alpha: Float,
    ) {
        val name = fighter.introName ?: return
        font.data.setScale(54f / 26f)
        font.color = Color(color.r, color.g, color.b, alpha)
        font.draw(batch, name, centerX + localX - 70f, centerY + localY + 27f, 140f, Align.center, false)
    }

    private fun drawName(fighter: FightFighterView, x: Float, y: Float, width: Float, align: Int, color: Color) {
        val name = fighter.name ?: return
        font.data.setScale(130f / 26f)
        font.color = Color(0.5f, 0.5f, 0.5f, 1f)
        font.draw(batch, name, x + 10f, y - 10f + 65f, width, align, false)
        font.color = color
        font.draw(batch, name, x, y + 65f, width, align, false)
    }

    private fun drawUnit(fighter: FightFighterView, centerX: Float, centerY: Float) {
        val action = fighter.action ?: return
        val frame = timeline.frame(action, fighter.actionElapsedSeconds) ?: return
        val avatar = fighter.avatarId ?: return
        val texture = when (frame.source) {
            UnitSpriteSource.ATTACK -> dynamicTextures.action("atk", avatar)
            UnitSpriteSource.MOVEMENT -> dynamicTextures.action("mov", avatar)
            UnitSpriteSource.SPECIAL -> dynamicTextures.action("spc", avatar)
        } ?: return
        val scaleX = fighter.parentScaleX * frame.pose.childScaleX
        val width = frame.sourceWidth * abs(scaleX)
        val height = frame.sourceHeight * 4f
        val worldCenterX = centerX + fighter.parentX + fighter.parentScaleX * frame.pose.childX
        val worldCenterY = centerY + 4f * frame.pose.childY
        batch.color = Color(1f, 1f, 1f, frame.pose.opacity / 255f)
        when (frame.material) {
            BattleCharacterMaterial.HIGHLIGHT -> {
                batch.flush()
                val shader = highlightShader()
                batch.shader = shader
                shader.setUniformf("u_value", frame.materialValue)
            }

            BattleCharacterMaterial.GRAY -> {
                batch.flush()
                batch.shader = grayShader()
            }

            else -> {}
        }
        batch.draw(
            texture, worldCenterX - width / 2f, worldCenterY - height / 2f, width, height,
            0, if (frame.sourceY + frame.sourceHeight > texture.height) 0 else frame.sourceY,
            minOf(frame.sourceWidth, texture.width), minOf(frame.sourceHeight, texture.height),
            scaleX < 0f, false,
        )
        if (frame.material == BattleCharacterMaterial.HIGHLIGHT || frame.material == BattleCharacterMaterial.GRAY) {
            batch.flush()
            batch.shader = null
        }
        batch.color = Color.WHITE
    }

    private fun drawSpeech(fighter: FightFighterView, centerX: Float, centerY: Float) {
        if (!fighter.speech.active) return
        val slot = fighter.slot
        val panelCenterX = centerX + if (slot == 0) -15.2f else 17.1f
        val panelCenterY = centerY + if (slot == 0) 249f else -264f
        val panelWidth = if (slot == 0) 830.5f else 834.1f
        val panel = if (slot == 0) hudAssets.fightSpeechLeftTexture else hudAssets.dialoguePanelTexture
        panel?.let { batch.draw(it, panelCenterX - panelWidth / 2f, panelCenterY - 80f, panelWidth, 160f) }
        dialogueFont.data.setScale(1f)
        dialogueFont.color = Color.BLACK
        val textX = panelCenterX + if (slot == 0) -350.556f else -385.057f
        val textY = panelCenterY + if (slot == 0) 68.211f else 67.634f
        dialogueFont.draw(batch, fighter.speech.renderedText, textX, textY, 728f, Align.left, true)
    }
}
