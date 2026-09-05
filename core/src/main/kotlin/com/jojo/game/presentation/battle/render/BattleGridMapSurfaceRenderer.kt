package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram

/** Immutable projection for BattleScreen's map-texture and persistent minimap pass. */
data class BattleGridRenderView(
    val map: BattleGridMapSurface?,
    val miniMap: BattleGridMiniMapView?,
)

data class BattleGridMapSurface(
    val texture: Texture,
    val left: Float,
    val bottom: Float,
    val width: Float,
    val height: Float,
    val sampleOffsetX: Float,
    val sampleOffsetY: Float,
    val cocos8Sampler: ShaderProgram?,
    val fragmentCoordinates: Boolean,
    val framebufferWorldWidth: Float,
    val framebufferWorldHeight: Float,
)

data class BattleGridMiniMapMarker(
    val texture: Texture,
    val x: Float,
    val y: Float,
)

data class BattleGridMiniMapView(
    val shown: Boolean,
    val framePatch: NinePatch?,
    val boxPatch: NinePatch?,
    val mapTexture: Texture?,
    val weatherTexture: Texture?,
    val markers: List<BattleGridMiniMapMarker>,
)

/**
 * Draws the part of a tactical grid that is independent of battle actors.
 * It deliberately owns the temporary map shader/filter changes so those
 * state changes cannot leak into the actor pass.
 */
class BattleGridMapSurfaceRenderer(private val batch: SpriteBatch) {
    fun draw(view: BattleGridRenderView) {
        view.map?.let(::drawMap)
        view.miniMap?.let(::drawMiniMap)
    }

    private fun drawMap(map: BattleGridMapSurface) {
        val sampler = map.cocos8Sampler
        val priorFilter = sampler?.let { map.texture.minFilter to map.texture.magFilter }
        if (sampler != null) {
            map.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            batch.shader = sampler
            sampler.setUniformf("u_texSize", map.texture.width.toFloat(), map.texture.height.toFloat())
            sampler.setUniformi("u_fragmentCoordinates", if (map.fragmentCoordinates) 1 else 0)
            if (map.fragmentCoordinates) {
                sampler.setUniformf(
                    "u_framebufferSize",
                    Gdx.graphics.backBufferWidth.toFloat(),
                    Gdx.graphics.backBufferHeight.toFloat(),
                )
                sampler.setUniformf("u_worldOrigin", 0f, 0f)
                sampler.setUniformf("u_worldSize", map.framebufferWorldWidth, map.framebufferWorldHeight)
                sampler.setUniformf("u_mapOrigin", map.left, map.bottom)
                sampler.setUniformf("u_mapSize", map.width, map.height)
            }
        }
        batch.color = Color.WHITE
        batch.draw(
            map.texture,
            map.left + map.sampleOffsetX,
            map.bottom + map.sampleOffsetY,
            map.width,
            map.height,
        )
        if (sampler != null) {
            // Flush before restoring so subsequent actor submissions never use
            // the Cocos8 map shader or nearest texture filtering.
            batch.flush()
            batch.shader = null
            priorFilter?.let { (min, mag) -> map.texture.setFilter(min, mag) }
        }
    }

    private fun drawMiniMap(view: BattleGridMiniMapView) {
        val offset = if (view.shown) 0f else 244f
        if (view.shown) view.framePatch?.draw(batch, 1244.3721f, 556f, 244f, 244f)
        view.mapTexture?.let { texture ->
            batch.color = Color(1f, 1f, 1f, 168f / 255f)
            batch.draw(texture, 1246.3721f + offset, 558f, 240f, 240f)
            batch.color = Color.WHITE
            view.markers.forEach { marker ->
                batch.draw(marker.texture, marker.x + offset, marker.y, 16f, 16f, 1, 1, 10, 10, false, false)
            }
        }
        view.weatherTexture?.let { texture ->
            batch.color = Color(1f, 1f, 1f, 127f / 255f)
            batch.draw(texture, 1248.3721f + offset, 560f, 57.6f, 57.6f)
        }
        batch.color = Color.WHITE
        if (view.shown) view.boxPatch?.draw(batch, 1286.3721f, 570f, 186.047f, 100f)
    }
}
