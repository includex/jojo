package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch

/** Owns the complete title scene resource lifetime. */
internal class TitleSceneAssets {
    val dimPixel = Texture(Pixmap(1, 1, Pixmap.Format.RGBA8888).also {
        it.setColor(Color.WHITE)
        it.fill()
    })
    val loginBackground = Texture(Gdx.files.internal("maps/ui/title/background.jpg")).linear()
    val loginButtons = (0..3).map { Texture(Gdx.files.internal("maps/ui/title/button$it.png")).linear() }

    private val uiTextures = mutableListOf<Texture>()
    private fun uiTexture(path: String) = Texture(Gdx.files.internal(path)).also {
        it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        uiTextures += it
    }

    val loadLogo9 = uiTexture("maps/ui/title/load/logo9.png")
    private val loadButton = uiTexture("maps/ui/title/load/button.png")
    private val loadTitle = uiTexture("maps/ui/title/load/title.png")
    private val loadBox2 = uiTexture("maps/ui/title/load/box2.png")
    private val loadRow = uiTexture("maps/ui/title/load/row.png")
    private val loadVline = uiTexture("maps/ui/title/load/vline.png")
    val loadEagle = uiTexture("maps/ui/title/load/eagle.png")
    val loadOuterPatch = NinePatch(loadButton, 9, 42, 42, 7)
    val loadTitlePatch = NinePatch(loadTitle, 5, 10, 10, 5)
    val loadBoxPatch = NinePatch(loadBox2, 3, 14, 14, 3)
    val loadRowPatch = NinePatch(loadRow, 1, 18, 18, 1)
    val loadVlinePatch = NinePatch(loadVline, 0, 6, 37, 2)

    val settingLogo9 = uiTexture("maps/ui/title/setting/logo9.png")
    val settingBox1 = uiTexture("maps/ui/title/setting/box1.png")
    val settingTitle = uiTexture("maps/ui/title/setting/title.png")
    private val settingBox2 = uiTexture("maps/ui/title/setting/box2.png")
    val settingToggle = uiTexture("maps/ui/title/setting/toggle.png")
    val settingCheck = uiTexture("maps/ui/title/setting/check.png")
    val settingRadioOff = uiTexture("maps/ui/title/setting/radio-off.png")
    val settingRadioOn = uiTexture("maps/ui/title/setting/radio-on.png")
    private val settingSlider = uiTexture("maps/ui/title/setting/slider.png")
    val settingBox6 = uiTexture("maps/ui/title/setting/box6.png")
    val settingStyles = (0..3).map { uiTexture("maps/ui/title/setting/style$it.png") }
    private val settingButton = uiTexture("maps/ui/title/setting/button.png")
    val loadingSpinner = uiTexture("maps/ui/system-overlay/uiloading.png")
    val settingBox1Patch = NinePatch(settingBox1, 3, 14, 14, 3)
    val settingBox2Patch = NinePatch(settingBox2, 3, 14, 14, 3)
    val settingSliderPatch = NinePatch(settingSlider, 10, 10, 7, 4)
    val settingButtonPatch = NinePatch(settingButton, 9, 42, 42, 7)

    val uiFont: BitmapFont = KoreanFont.create(34, UI_GLYPHS, fillColor = Color.WHITE)

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
        loginBackground.dispose()
        loginButtons.forEach(Texture::dispose)
        uiTextures.forEach(Texture::dispose)
        uiFont.dispose()
        dimPixel.dispose()
    }

    private fun Texture.linear() = also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    private companion object {
        const val UI_GLYPHS =
            "진행도 불러오기읽을 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다.취소환경 설정항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.배경 음악 듣기효과음 듣기전투 시 전장 축소 이미지가 자동으로 표시됩니다.대화창 자동 닫힘체력 바가 유닛 위에 있습니다텍스트 속도느림중빠르게게임 속도정보 설명자세히보통요약대화창 색상확인No.---전역영천의 전투불러올 수 있나요?0123456789:()제턴 "
    }
}
