// Battle
package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram

/** BattleGridRenderView: 전투 격자 렌더링 표시 정보이며, 화면에 필요한 전투 정보를 만들고 표시한다. */
data class BattleGridRenderView(
    val map: BattleGridMapSurface?,
    val miniMap: BattleGridMiniMapView?,
)
/**
 * `BattleGridMapSurface`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

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
/**
 * `BattleGridMiniMapMarker`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class BattleGridMiniMapMarker(
    val texture: Texture,
    val x: Float,
    val y: Float,
)

/**
 * `BattleGridMiniMapBox`: 미니맵 위에 현재 화면 범위를 나타내는 사각 표시다.
 *
 * 원본 `MiniMapLayer`는 `box` 노드를 캔버스의 1/8 크기로 잡아 두고 `MAP_SCROLLING`
 * 이벤트마다 `_box.position = (-content.x / 8, -content.y / 8)`으로 옮긴다. 즉 상자는
 * 맵을 축소한 비율 그대로 현재 보이는 영역을 따라다닌다.
 */
data class BattleGridMiniMapBox(val x: Float, val y: Float, val width: Float, val height: Float)

/** BattleGridMiniMapView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
data class BattleGridMiniMapView(
    val shown: Boolean,
    val framePatch: NinePatch?,
    val boxPatch: NinePatch?,
    val mapTexture: Texture?,
    val weatherTexture: Texture?,
    val markers: List<BattleGridMiniMapMarker>,
    val box: BattleGridMiniMapBox? = null,
)

/** BattleGridMapSurfaceRenderer: 전투 격자 지도 Surface 렌더러이며, 화면에 필요한 전투 정보를 만들고 표시한다. */
class BattleGridMapSurfaceRenderer(private val batch: SpriteBatch) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattleGridRenderView) {
        view.map?.let(::drawMap)
    }

    /**
     * `drawOverlay`: 미니맵만 따로 그린다.
     *
     * 원본 `MiniMapLayer`는 전장 위에 얹힌 별도 레이어라 유닛·연출에 가려지지 않는다.
     * 보드와 같은 패스에서 그리면 뒤이어 그리는 유닛과 효과가 미니맵을 덮는다.
     */
    fun drawOverlay(view: BattleGridRenderView) {
        view.miniMap?.let(::drawMiniMap)
    }

    /**
     * `drawMap`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
            batch.flush()
            batch.shader = null
            priorFilter?.let { (min, mag) -> map.texture.setFilter(min, mag) }
        }
    }

    /**
     * `drawMiniMap`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
        if (view.shown) view.box?.let { box -> view.boxPatch?.draw(batch, box.x, box.y, box.width, box.height) }
    }
}
