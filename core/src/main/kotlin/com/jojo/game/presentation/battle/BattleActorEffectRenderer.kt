package com.jojo.game.presentation.battle

import com.jojo.game.domain.battle.*
import com.jojo.game.presentation.battle.assets.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.jojo.game.presentation.battle.unit.BattleUnitAttributeStatusRender
import com.jojo.game.presentation.battle.unit.BattleUnitStateRender

/** 그리기 직전에 전투 화면이 투영한 배우·효과 불변 상태입니다. */
internal data class BattleActorEffectRenderView(
    val boardLeft: Float,
    val boardBottom: Float,
    val tileSize: Float,
    val actors: List<BattleActorRenderUnit>,
    val effects: List<BattleEffectRender>,
    val sayMarker: BattleSayMarkerRender?,
)

/** 전투 유닛과 하위 프리팹을 그리는 데 필요한 상태입니다. */
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
    val hpTexture: Texture?,
    val hpRatio: Float,
    val showHpBar: Boolean,
    val attributeStatuses: List<BattleUnitAttributeStatusRender.Command>,
    val state: BattleUnitStateRender.Command?,
    val stateTexture: Texture?,
)

/** 현재 애니메이션 시점의 맵 부착 마법 효과 샘플입니다. */
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

/** 현재 발화 배우에 부착된 말풍선 표식입니다. */
internal data class BattleSayMarkerRender(val texture: Texture, val tileX: Float, val tileY: Float)

/** 배우와 효과의 그리기 순서만 담당하는 렌더러입니다. */
internal class BattleActorEffectRenderer(
    private val batch: SpriteBatch,
    private val hudAssets: BattleHudAssets,
    private val highlightShader: (() -> ShaderProgram)?,
) {
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
                    shader.setUniformf("u_value", 1f)
                }
                drawMasked(actor.terrainMask, x, y, actor.size, view.tileSize) {
                    batch.draw(
                        texture,
                        x,
                        y,
                        actor.size,
                        actor.size,
                        0,
                        if (actor.sourceY + actor.sourceHeight > texture.height) 0 else actor.sourceY,
                        minOf(actor.sourceWidth, texture.width),
                        minOf(actor.sourceHeight, texture.height),
                        actor.flipX,
                        false,
                    )
                }
                if (shader != null) {
                    batch.flush()
                    batch.shader = null
                }
            }
            if (actor.showHpBar) {
                actor.hpTexture?.let { texture ->
                    val width = 88f
                    val x = view.boardLeft + actor.tileX * view.tileSize + (view.tileSize - width) / 2f
                    val y = view.boardBottom - actor.tileY * view.tileSize - 1f
                    batch.color = Color.WHITE
                    batch.draw(texture, x, y, width * actor.hpRatio, 6f)
                }
            }
            actor.state?.let { command ->
                actor.stateTexture?.let { texture ->
                    batch.color = Color.WHITE
                    batch.draw(texture, command.x, command.y, command.width, command.height)
                }
            }
        }
        batch.color = Color.WHITE
    }

    fun drawEffects(view: BattleActorEffectRenderView) {
        view.effects.forEach { effect ->
            batch.color = Color(1f, 1f, 1f, effect.alpha)
            batch.draw(
                effect.texture,
                effect.x,
                effect.y,
                effect.width,
                effect.height,
                effect.sourceX,
                effect.sourceY,
                effect.sourceWidth,
                effect.sourceHeight,
                false,
                false,
            )
        }
        batch.color = Color.WHITE
    }

    fun drawSayMarker(view: BattleActorEffectRenderView) {
        view.sayMarker?.let { marker ->
            batch.color = Color.WHITE
            batch.draw(
                marker.texture,
                view.boardLeft + marker.tileX * view.tileSize + view.tileSize * 0.75f,
                view.boardBottom - marker.tileY * view.tileSize + view.tileSize * 0.75f,
                view.tileSize / 2f,
                view.tileSize / 2f,
            )
        }
    }

    private fun drawMasked(
        mask: Texture?,
        x: Float,
        y: Float,
        actorSize: Float,
        tileSize: Float,
        draw: () -> Unit,
    ) {
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
