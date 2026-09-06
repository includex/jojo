// Battle
package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.utils.Disposable
internal class BattleOverlayAssets : Disposable {
    val sectionBackgroundTexture = optionalTexture("maps/ui/section/logo5.jpg")
    val rewardItemTexture = optionalTexture("maps/marks/47.png")
    val terrainLayerBackgroundTexture = terrainTexture("background")
    private val terrainLayerPanelTexture = terrainTexture("panel")
    private val terrainLayerRowEvenTexture = terrainTexture("row-even")
    private val terrainLayerRowOddTexture = terrainTexture("row-odd")
    private val terrainLayerVlineTexture = terrainTexture("vline")
    val terrainLayerPanelPatch = terrainLayerPanelTexture?.let { NinePatch(it, 7, 8, 7, 7) }
    val terrainLayerRowEvenPatch = terrainLayerRowEvenTexture?.let { NinePatch(it, 1, 1, 1, 1) }
    val terrainLayerRowOddPatch = terrainLayerRowOddTexture?.let { NinePatch(it, 1, 1, 1, 1) }
    val terrainLayerVlinePatch = terrainLayerVlineTexture?.let { NinePatch(it, 0, 0, 2, 1) }

    val winConditionBackgroundTexture = winConditionTexture("bg0")
    private val winConditionBoxTexture = winConditionTexture("box3")
    private val winConditionScrollTexture = winConditionTexture("scroll-box2")
    val winConditionLogoTexture = winConditionTexture("logo3")
    val winConditionBoxPatch = winConditionBoxTexture?.let { NinePatch(it, 9, 7, 9, 11) }
    val winConditionScrollPatch = winConditionScrollTexture?.let { NinePatch(it, 3, 3, 3, 3) }
    val loseLogoTexture = linearOptionalTexture("maps/ui/result/logo8.jpg")

    private fun terrainTexture(name: String): Texture? =
        linearOptionalTexture("maps/ui/terrain-layer/$name.png")

    private fun winConditionTexture(name: String): Texture? =
        linearOptionalTexture("maps/ui/win-condition/$name.png")

    private fun linearOptionalTexture(path: String): Texture? =
        optionalTexture(path)?.also {
            it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }

    private fun optionalTexture(path: String): Texture? =
        Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)

    override fun dispose() {
        listOf(
            sectionBackgroundTexture,
            rewardItemTexture,
            terrainLayerBackgroundTexture,
            terrainLayerPanelTexture,
            terrainLayerRowEvenTexture,
            terrainLayerRowOddTexture,
            terrainLayerVlineTexture,
            winConditionBackgroundTexture,
            winConditionBoxTexture,
            winConditionScrollTexture,
            winConditionLogoTexture,
            loseLogoTexture,
        ).filterNotNull().forEach(Texture::dispose)
    }
}
