// Battle
package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.utils.Disposable
/**
 * `BattleOverlayAssets`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class BattleOverlayAssets : Disposable {
    /**
     * `sectionBackgroundTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val sectionBackgroundTexture = optionalTexture("maps/ui/section/logo5.jpg")
    /**
     * `rewardItemTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val rewardItemTexture = optionalTexture("maps/marks/47.png")
    /**
     * `terrainLayerBackgroundTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val terrainLayerBackgroundTexture = terrainTexture("background")
    /**
     * `terrainLayerPanelTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val terrainLayerPanelTexture = terrainTexture("panel")
    /**
     * `terrainLayerRowEvenTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val terrainLayerRowEvenTexture = terrainTexture("row-even")
    /**
     * `terrainLayerRowOddTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val terrainLayerRowOddTexture = terrainTexture("row-odd")
    /**
     * `terrainLayerVlineTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val terrainLayerVlineTexture = terrainTexture("vline")
    /**
     * `terrainLayerPanelPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val terrainLayerPanelPatch = terrainLayerPanelTexture?.let { NinePatch(it, 7, 8, 7, 7) }
    /**
     * `terrainLayerRowEvenPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val terrainLayerRowEvenPatch = terrainLayerRowEvenTexture?.let { NinePatch(it, 1, 1, 1, 1) }
    /**
     * `terrainLayerRowOddPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val terrainLayerRowOddPatch = terrainLayerRowOddTexture?.let { NinePatch(it, 1, 1, 1, 1) }
    /**
     * `terrainLayerVlinePatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val terrainLayerVlinePatch = terrainLayerVlineTexture?.let { NinePatch(it, 0, 0, 2, 1) }

    /**
     * `winConditionBackgroundTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val winConditionBackgroundTexture = winConditionTexture("bg0")
    /**
     * `winConditionBoxTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val winConditionBoxTexture = winConditionTexture("box3")
    /**
     * `winConditionScrollTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val winConditionScrollTexture = winConditionTexture("scroll-box2")
    /**
     * `winConditionLogoTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val winConditionLogoTexture = winConditionTexture("logo3")
    /**
     * `winConditionBoxPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val winConditionBoxPatch = winConditionBoxTexture?.let { NinePatch(it, 9, 7, 9, 11) }
    /**
     * `winConditionScrollPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val winConditionScrollPatch = winConditionScrollTexture?.let { NinePatch(it, 3, 3, 3, 3) }
    /**
     * `loseLogoTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loseLogoTexture = linearOptionalTexture("maps/ui/result/logo8.jpg")

    /**
     * `terrainTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun terrainTexture(name: String): Texture? =
        linearOptionalTexture("maps/ui/terrain-layer/$name.png")

    /**
     * `winConditionTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun winConditionTexture(name: String): Texture? =
        linearOptionalTexture("maps/ui/win-condition/$name.png")

    /**
     * `linearOptionalTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun linearOptionalTexture(path: String): Texture? =
        optionalTexture(path)?.also {
            it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }

    /**
     * `optionalTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun optionalTexture(path: String): Texture? =
        Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)

    /**
     * `dispose`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
