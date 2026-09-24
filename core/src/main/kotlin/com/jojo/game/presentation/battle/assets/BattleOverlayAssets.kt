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
    private val terrainLayerOuterBoxTexture = terrainTexture("outer-box")
    private val terrainLayerTitleStripTexture = terrainTexture("title-strip")
    private val terrainLayerFooterTexture = linearOptionalTexture("maps/ui/unit-info/box3.png")
    val terrainLayerOuterBoxPatch = terrainLayerOuterBoxTexture?.let { NinePatch(it, 3, 3, 3, 3) }
    val terrainLayerTitleStripPatch = terrainLayerTitleStripTexture?.let { NinePatch(it, 5, 5, 5, 5) }
    val terrainLayerFooterPatch = terrainLayerFooterTexture?.let { NinePatch(it, 8, 8, 8, 8) }
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

    /**
     * `terrainLayerSkillTextures`: 지형 행의 특기 아이콘 네 개다.
     *
     * 원본 `battle/TerrainLayer.js:109`는 `skill/skill_0..3` **스프라이트**에 회색 material을
     * 씌워 비활성을 표현한다(`0 == (1 << _ & l) ? grayLight : defMater`). 포트는 그 자리에
     * `●`/`○` **글자**를 그리고 있었다. 회색판은 이미 자산으로 뽑혀 있어 전당 쪽
     * `HallTerrainRenderPlan`이 같은 규칙으로 쓴다.
     */
    val terrainLayerSkillTextures = (1..4).map { terrainTexture("skill$it") }

    /** 비활성 특기 아이콘: 원본의 `grayLight` material에 해당하는 회색판이다. */
    val terrainLayerSkillDisabledTextures = (1..4).map { terrainTexture("skill$it-disabled") }

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
     * `winConditionBoxPatch` (NinePatch?): 승리 조건 상자가 쓰는 `box3`의 9분할이다.
     * `box3`는 원본 전체에서 하나뿐인 SpriteFrame이고
     * (resources/import/ee/ee381589-5374-4719-9fda-fd162a2fd65b.0fe56.json)
     * `capInsets`는 Cocos 순서 (left, top, right, bottom)로 [9, 7, 9, 11]이다.
     * LibGDX `NinePatch`는 (left, right, top, bottom) 순서라 9, 9, 7, 11로 옮긴다.
     */

    val winConditionBoxPatch = winConditionBoxTexture?.let { NinePatch(it, 9, 9, 7, 11) }
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
            terrainLayerOuterBoxTexture,
            terrainLayerTitleStripTexture,
            terrainLayerFooterTexture,
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
