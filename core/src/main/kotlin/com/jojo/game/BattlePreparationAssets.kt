package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch

/** Owns preparation textures, fonts, patches, and lazy unit image caches. */
internal class BattlePreparationAssets(backgroundId: Int, unitGlyphs: String) {
    private val textures = mutableListOf<Texture>()
    private val avatarTextures = mutableMapOf<Int, Texture>()
    private val faceTextures = mutableMapOf<Int, Texture>()

    val background = texture("maps/$backgroundId.jpg") ?: texture("maps/71.jpg")
    val logo9 = texture("maps/ui/start-battle/logo9.png")
    val roster = texture("maps/ui/start-battle/roster.png")
    val selected = texture("maps/ui/start-battle/selected.png")
    val slotOpen = texture("maps/ui/start-battle/slot-open.png")
    val slotRequired = texture("maps/ui/start-battle/slot-required.png")
    val slotMinimum = texture("maps/ui/start-battle/slot-minimum.png")
    private val button = texture("maps/ui/start-battle/button.png")
    private val box1 = texture("maps/ui/start-battle/box1.png")
    private val title = texture("maps/ui/start-battle/title.png")
    val unitInfoBg1 = texture("maps/ui/unit-info/bg1.png")
    val battleViewMap = texture("maps/battle-maps/1.png")
    val dim = Pixmap(1, 1, Pixmap.Format.RGBA8888).let { pixmap ->
        pixmap.setColor(Color.BLACK)
        pixmap.fill()
        Texture(pixmap).also { textures += it }.also { pixmap.dispose() }
    }
    val outerPatch = button?.let { NinePatch(it, 9, 9, 7, 11) }
    val box1Patch = box1?.let { NinePatch(it, 3, 3, 3, 3) }
    val titlePatch = title?.let { NinePatch(it, 5, 5, 5, 5) }
    val unitInfoBoxPatch = texture("maps/ui/unit-info/box1.png")?.let { NinePatch(it, 3, 3, 3, 3) }
    val unitInfoButtonPatch = texture("maps/ui/unit-info/box3.png")?.let { NinePatch(it, 3, 3, 3, 3) }

    private val glyphs = BASE_GLYPHS + unitGlyphs
    val font: BitmapFont = KoreanFont.create(31, glyphs, fillColor = Color.BLACK)
    val rosterFont: BitmapFont = KoreanFont.create(32, glyphs)
    val rosterNameFont: BitmapFont = KoreanFont.create(31, glyphs, 1.6f, Color.RED, Color.WHITE)

    /**
     * 공개 메서드 `avatar`
     *
     * ### 파라미터
    - `id` (`Int?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun avatar(id: Int?): Texture? {
        id ?: return null
        avatarTextures[id]?.let { return it }
        val handle = Gdx.files.internal("maps/units/mov2/$id.png").takeIf { it.exists() }
            ?: Gdx.files.internal("maps/units/mov/$id.png")
        return handle.takeIf { it.exists() }?.let(::Texture)?.linear()?.also { avatarTextures[id] = it }
    }

    /**
     * 공개 메서드 `face`
     *
     * ### 파라미터
    - `headId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Texture?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun face(headId: Int): Texture? {
        faceTextures[headId]?.let { return it }
        return Gdx.files.internal("maps/heads/$headId.png").takeIf { it.exists() }
            ?.let(::Texture)?.linear()?.also { faceTextures[headId] = it }
    }

    /**
     * 공개 메서드 `dispose`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun dispose() {
        font.dispose()
        rosterFont.dispose()
        rosterNameFont.dispose()
        (textures + avatarTextures.values + faceTextures.values).distinct().forEach(Texture::dispose)
    }

    private fun texture(path: String): Texture? = Gdx.files.internal(path).takeIf { it.exists() }
        ?.let(::Texture)?.linear()?.also { textures += it }

    private fun Texture.linear() = also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    private companion object {
        const val BASE_GLYPHS = "Lv.EXPHPMP무력민첩성지력운기지휘공격방어정신폭발사기이동무장정보출진부대속성결정취소필수최소최대없음군웅조조병사허자장0123456789-/: " +
                "열전특성능력장비마법상태현금인물정상입니다모든특기보기기본소개출진횟수퇴각이전다음"
    }
}
