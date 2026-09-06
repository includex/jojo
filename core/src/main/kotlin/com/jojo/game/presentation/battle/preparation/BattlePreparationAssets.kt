// Battle
package com.jojo.game.presentation.battle.preparation

import com.jojo.game.presentation.shared.KoreanFont

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
/**
 * `BattlePreparationAssets`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class BattlePreparationAssets(backgroundId: Int, unitGlyphs: String) {
    /**
     * `textures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val textures = mutableListOf<Texture>()
    /**
     * `avatarTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val avatarTextures = mutableMapOf<Int, Texture>()
    /**
     * `faceTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val faceTextures = mutableMapOf<Int, Texture>()

    /**
     * `background` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val background = texture("maps/$backgroundId.jpg") ?: texture("maps/71.jpg")
    /**
     * `logo9` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val logo9 = texture("maps/ui/start-battle/logo9.png")
    /**
     * `roster` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val roster = texture("maps/ui/start-battle/roster.png")
    /**
     * `selected` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val selected = texture("maps/ui/start-battle/selected.png")
    /**
     * `slotOpen` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val slotOpen = texture("maps/ui/start-battle/slot-open.png")
    /**
     * `slotRequired` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val slotRequired = texture("maps/ui/start-battle/slot-required.png")
    /**
     * `slotMinimum` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val slotMinimum = texture("maps/ui/start-battle/slot-minimum.png")
    /**
     * `button` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val button = texture("maps/ui/start-battle/button.png")
    /**
     * `box1` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val box1 = texture("maps/ui/start-battle/box1.png")
    /**
     * `title` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val title = texture("maps/ui/start-battle/title.png")
    /**
     * `unitInfoBg1` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val unitInfoBg1 = texture("maps/ui/unit-info/bg1.png")
    /**
     * `battleViewMap` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleViewMap = texture("maps/battle-maps/1.png")
    /**
     * `dim` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val dim = Pixmap(1, 1, Pixmap.Format.RGBA8888).let { pixmap ->
        pixmap.setColor(Color.BLACK)
        pixmap.fill()
        Texture(pixmap).also { textures += it }.also { pixmap.dispose() }
    }
    /**
     * `outerPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val outerPatch = button?.let { NinePatch(it, 9, 9, 7, 11) }
    /**
     * `box1Patch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val box1Patch = box1?.let { NinePatch(it, 3, 3, 3, 3) }
    /**
     * `titlePatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val titlePatch = title?.let { NinePatch(it, 5, 5, 5, 5) }
    /**
     * `unitInfoBoxPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val unitInfoBoxPatch = texture("maps/ui/unit-info/box1.png")?.let { NinePatch(it, 3, 3, 3, 3) }
    /**
     * `unitInfoButtonPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val unitInfoButtonPatch = texture("maps/ui/unit-info/box3.png")?.let { NinePatch(it, 3, 3, 3, 3) }

    /**
     * `glyphs` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val glyphs = BASE_GLYPHS + unitGlyphs
    /**
     * `font` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val font: BitmapFont = KoreanFont.create(31, glyphs, fillColor = Color.BLACK)
    /**
     * `rosterFont` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val rosterFont: BitmapFont = KoreanFont.create(32, glyphs)
    /**
     * `rosterNameFont` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val rosterNameFont: BitmapFont = KoreanFont.create(31, glyphs, 1.6f, Color.RED, Color.WHITE)


    /**
     * `avatar`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun avatar(id: Int?): Texture? {
        id ?: return null
        avatarTextures[id]?.let { return it }
        val handle = Gdx.files.internal("maps/units/mov2/$id.png").takeIf { it.exists() }
            ?: Gdx.files.internal("maps/units/mov/$id.png")
        return handle.takeIf { it.exists() }?.let(::Texture)?.linear()?.also { avatarTextures[id] = it }
    }


    /**
     * `face`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun face(headId: Int): Texture? {
        faceTextures[headId]?.let { return it }
        return Gdx.files.internal("maps/heads/$headId.png").takeIf { it.exists() }
            ?.let(::Texture)?.linear()?.also { faceTextures[headId] = it }
    }


    /**
     * `dispose`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispose() {
        font.dispose()
        rosterFont.dispose()
        rosterNameFont.dispose()
        (textures + avatarTextures.values + faceTextures.values).distinct().forEach(Texture::dispose)
    }

    /**
     * `texture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun texture(path: String): Texture? = Gdx.files.internal(path).takeIf { it.exists() }
        ?.let(::Texture)?.linear()?.also { textures += it }

    /**
     * `Texture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun Texture.linear() = also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    private companion object {
        /**
         * `BASE_GLYPHS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val BASE_GLYPHS = "Lv.EXPHPMP무력민첩성지력운기지휘공격방어정신폭발사기이동무장정보출진부대속성결정취소필수최소최대없음군웅조조병사허자장0123456789-/: " +
                "열전특성능력장비마법상태현금인물정상입니다모든특기보기기본소개출진횟수퇴각이전다음"
    }
}
