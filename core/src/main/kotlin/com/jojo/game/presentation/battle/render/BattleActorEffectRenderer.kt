// Battle
package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.jojo.game.presentation.battle.assets.BattleHudAssets
import com.jojo.game.presentation.battle.unit.BattleUnitAttributeStatusRender
import com.jojo.game.presentation.battle.unit.BattleUnitStateRender

/** 전투 배우·효과 표시 뷰: 그리기 직전에 확정한 유닛, 효과, 발화 표식을 보관한다. */
internal data class BattleActorEffectRenderView(
    val boardLeft: Float,
    val boardBottom: Float,
    val tileSize: Float,
    val actors: List<BattleActorRenderUnit>,
    val effects: List<BattleEffectRender>,
    val sayMarker: BattleSayMarkerRender?,
)

/** 전투 배우 표시 정보: 스프라이트, 체력 바, 상태 아이콘을 그리는 데 필요한 불변 값을 정의한다. */
/**
 * 원본 `BattleUnit.showHarmBar`: 공격 예고 동안 대상 유닛의 `info` 노드에 명중률(label0, 12px 흰색·검정 테두리, 오른쪽 위),
 * 피해(label1, 14px 빨강·연노랑 테두리, 오른쪽 아래), 잃을 체력을 드러내는 bar0(현재 비율) 위에 bar2(공격 뒤 비율)를 겹친다.
 */
internal data class BattleAttackPreviewRender(
    val hitRate: Int,
    val harm: Int,
    val currentRatio: Float,
    val afterRatio: Float,
)

internal data class BattleActorRenderUnit(
    val id: String,
    val tileX: Float,
    val tileY: Float,
    val texture: Texture?,
    val sourceY: Int,
    val sourceWidth: Int,
    val sourceHeight: Int,
    val size: Float,
    val offsetX: Float,
    val offsetY: Float,
    val flipX: Boolean,
    val terrainMask: Texture?,
    val sourceHighlight: Boolean,
    /** 점등 재질의 세기다. 원본의 `u_value`와 같은 0..1 범위다. */
    val highlightValue: Float = 1f,
    /** 현재 클립이 지정한 알파값이다. 퇴각·사망 연출이 이 값으로 깜빡이고 사라진다. */
    val opacity: Float = 1f,
    /** 애니메이션 색상 키가 sprite에 곱하는 24-bit RGB 값이다. */
    val colorRgb: Int = 0xffffff,
    val hpTexture: Texture?,
    val hpRatio: Float,
    val showHpBar: Boolean,
    val attributeStatuses: List<BattleUnitAttributeStatusRender.Command>,
    val state: BattleUnitStateRender.Command?,
    val stateTexture: Texture?,
    val attackPreview: BattleAttackPreviewRender? = null,
)

/** 전투 효과 표시 정보: 현재 프레임의 텍스처 영역과 투명도를 정의한다. */
internal data class BattleEffectRender(
    val texture: Texture,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val sourceX: Int,
    val sourceY: Int,
    val sourceWidth: Int,
    val sourceHeight: Int,
    val alpha: Float,
)

/** 전투 발화 표식: 대화 중인 유닛 타일과 표식 텍스처를 정의한다. */
internal data class BattleSayMarkerRender(val texture: Texture, val tileX: Float, val tileY: Float)

/** 전투 배우·효과 렌더러: 확정된 표시 뷰를 SpriteBatch 호출 순서로 그린다. */
internal class BattleActorEffectRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `hudAssets` (BattleHudAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val hudAssets: BattleHudAssets,
    /** `highlightShader` ((() -> ShaderProgram)?): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val highlightShader: (() -> ShaderProgram)?,
    /** 원본 info/label0 "100%": fontSize 12(월드 24), LabelOutline 2px 검정. */
    private val previewRateFont: BitmapFont? = null,
    /** 원본 info/label1 "45": fontSize 14(월드 28), 글자 (255,0,0), LabelOutline 2px (255,255,201). */
    private val previewHarmFont: BitmapFont? = null,
) {
    /** 배우 그리기: 상태 아이콘, 본체, 체력 바, 상태 효과 순서로 출력한다. */
    fun drawActors(view: BattleActorEffectRenderView) {
        view.actors.forEach { actor ->
            actor.attributeStatuses.forEach { status ->
                hudAssets.battleAttributeStatusTextures.getOrNull(status.textureIndex)?.let { texture ->
                    batch.color = Color.WHITE
                    batch.draw(texture, status.x, status.y, status.size, status.size)
                }
            }
            actor.texture?.let { texture ->
                val x = view.boardLeft + actor.tileX * view.tileSize +
                    (view.tileSize - actor.size) / 2f + actor.offsetX
                val y = view.boardBottom - actor.tileY * view.tileSize +
                    (view.tileSize - actor.size) / 2f + actor.offsetY
                val shader = highlightShader?.takeIf { actor.sourceHighlight }?.invoke()
                if (shader != null) {
                    batch.flush()
                    batch.shader = shader
                    shader.setUniformf("u_value", actor.highlightValue)
                }
                batch.setColor(
                    ((actor.colorRgb ushr 16) and 0xff) / 255f,
                    ((actor.colorRgb ushr 8) and 0xff) / 255f,
                    (actor.colorRgb and 0xff) / 255f,
                    actor.opacity,
                )
                drawMasked(actor.terrainMask, x, y, actor.size, view.tileSize) {
                    batch.draw(
                        texture, x, y, actor.size, actor.size, 0,
                        if (actor.sourceY + actor.sourceHeight > texture.height) 0 else actor.sourceY,
                        minOf(actor.sourceWidth, texture.width), minOf(actor.sourceHeight, texture.height),
                        actor.flipX, false,
                    )
                }
                if (shader != null) {
                    batch.flush()
                    batch.shader = null
                }
                batch.color = Color.WHITE
            }
            if (actor.showHpBar) actor.hpTexture?.let { texture ->
                val width = 88f
                val x = view.boardLeft + actor.tileX * view.tileSize + (view.tileSize - width) / 2f
                val y = view.boardBottom - actor.tileY * view.tileSize - 1f
                batch.color = Color.WHITE
                val preview = actor.attackPreview
                if (preview == null) {
                    batch.draw(texture, x, y, width * actor.hpRatio, 6f)
                } else {
                    // 원본 showHarmBar(피해): bar0.progress = 현재/최대, 그 위에 bar2.progress = (현재-피해)/최대.
                    hudAssets.previewLostHpBarTexture?.let { batch.draw(it, x, y, width * preview.currentRatio, 6f) }
                    batch.draw(texture, x, y, width * preview.afterRatio, 6f)
                }
            }
            actor.attackPreview?.let { preview ->
                val tileLeft = view.boardLeft + actor.tileX * view.tileSize
                val tileBottom = view.boardBottom - actor.tileY * view.tileSize
                val right = tileLeft + view.tileSize
                previewRateFont?.let { font ->
                    // label0: anchor (1, 1)이 유닛 (24, 24)에 있어 오른쪽 위 모서리에 붙는다.
                    val text = "${preview.hitRate}%"
                    val layout = GlyphLayout(font, text)
                    font.color = Color.WHITE
                    font.draw(batch, text, right - layout.width, tileBottom + view.tileSize)
                }
                previewHarmFont?.let { font ->
                    // label1: anchor (1, 0)이 유닛 (24, -24)에 있어 오른쪽 아래 모서리에 붙는다. 노드 높이 19.12(월드 38.24).
                    val text = preview.harm.toString()
                    val layout = GlyphLayout(font, text)
                    font.draw(batch, text, right - layout.width, tileBottom + 38.24f)
                }
                batch.color = Color.WHITE
            }
            actor.state?.let { command -> actor.stateTexture?.let { texture ->
                batch.color = Color.WHITE
                batch.draw(texture, command.x, command.y, command.width, command.height)
            } }
        }
        batch.color = Color.WHITE
    }

    /** 효과 그리기: 애니메이션 효과의 현재 프레임과 투명도를 배치에 출력한다. */
    fun drawEffects(view: BattleActorEffectRenderView) {
        view.effects.forEach { effect ->
            batch.color = Color(1f, 1f, 1f, effect.alpha)
            batch.draw(effect.texture, effect.x, effect.y, effect.width, effect.height, effect.sourceX, effect.sourceY,
                effect.sourceWidth, effect.sourceHeight, false, false)
        }
        batch.color = Color.WHITE
    }

    /** 발화 표식 그리기: 대화 유닛 우상단에 말풍선 표식을 배치한다. */
    fun drawSayMarker(view: BattleActorEffectRenderView) {
        view.sayMarker?.let { marker ->
            batch.color = Color.WHITE
            batch.draw(marker.texture, view.boardLeft + marker.tileX * view.tileSize + view.tileSize * 0.75f,
                view.boardBottom - marker.tileY * view.tileSize + view.tileSize * 0.75f,
                view.tileSize / 2f, view.tileSize / 2f)
        }
    }

    /** 지형 마스크 적용: 스텐실 버퍼에 마스크를 기록한 뒤 배우 스프라이트만 통과시킨다. */
    private fun drawMasked(mask: Texture?, x: Float, y: Float, actorSize: Float, tileSize: Float, draw: () -> Unit) {
        if (mask == null) {
            draw()
            return
        }
        batch.flush()
        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST)
        Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT)
        Gdx.gl.glColorMask(false, false, false, false)
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xff)
        Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE)
        val maskSize = tileSize * (80f / 48f)
        batch.draw(mask, x + (actorSize - maskSize) / 2f, y + (actorSize - maskSize) / 2f, maskSize, maskSize)
        batch.flush()
        Gdx.gl.glColorMask(true, true, true, true)
        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, 0xff)
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP)
        draw()
        batch.flush()
        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST)
    }
}
