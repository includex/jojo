// Presentation
package com.jojo.game.presentation.title.assets

import com.jojo.game.presentation.shared.KoreanFont

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch

/** TitleSceneAssets: 제목 장면 자원이며, 화면 표시에 필요한 텍스처와 자원 경로를 보관한다. */
internal class TitleSceneAssets {
    /**
     * `dimPixel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val dimPixel = Texture(Pixmap(1, 1, Pixmap.Format.RGBA8888).also {
        it.setColor(Color.WHITE)
        it.fill()
    })
    /**
     * `loginBackground` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loginBackground = Texture(Gdx.files.internal("maps/ui/title/background.jpg")).linear()
    /**
     * `loginButtons` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loginButtons = (0..3).map { Texture(Gdx.files.internal("maps/ui/title/button$it.png")).linear() }

    /**
     * `uiTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val uiTextures = mutableListOf<Texture>()
    /**
     * `uiTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun uiTexture(path: String) = Texture(Gdx.files.internal(path)).also {
        it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        uiTextures += it
    }

    /**
     * `loadLogo9` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loadLogo9 = uiTexture("maps/ui/title/load/logo9.png")
    /**
     * `loadButton` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val loadButton = uiTexture("maps/ui/title/load/button.png")
    /**
     * `loadTitle` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val loadTitle = uiTexture("maps/ui/title/load/title.png")
    /**
     * `loadBox2` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val loadBox2 = uiTexture("maps/ui/title/load/box2.png")
    /**
     * `loadRow` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val loadRow = uiTexture("maps/ui/title/load/row.png")
    /**
     * `loadVline` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val loadVline = uiTexture("maps/ui/title/load/vline.png")
    /**
     * `loadEagle` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loadEagle = uiTexture("maps/ui/title/load/eagle.png")
    /**
     * `loadOuterPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loadOuterPatch = NinePatch(loadButton, 9, 42, 42, 7)
    /**
     * `loadTitlePatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loadTitlePatch = NinePatch(loadTitle, 5, 10, 10, 5)
    /**
     * `loadBoxPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loadBoxPatch = NinePatch(loadBox2, 3, 14, 14, 3)
    /**
     * `loadRowPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loadRowPatch = NinePatch(loadRow, 1, 18, 18, 1)
    /**
     * `loadVlinePatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loadVlinePatch = NinePatch(loadVline, 0, 6, 37, 2)

    /**
     * `settingLogo9` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingLogo9 = uiTexture("maps/ui/title/setting/logo9.png")
    /**
     * `settingBox1` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingBox1 = uiTexture("maps/ui/title/setting/box1.png")
    /**
     * `settingTitle` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingTitle = uiTexture("maps/ui/title/setting/title.png")
    /**
     * `settingBox2` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val settingBox2 = uiTexture("maps/ui/title/setting/box2.png")
    /**
     * `settingToggle` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingToggle = uiTexture("maps/ui/title/setting/toggle.png")
    /**
     * `settingCheck` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingCheck = uiTexture("maps/ui/title/setting/check.png")
    /**
     * `settingRadioOff` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingRadioOff = uiTexture("maps/ui/title/setting/radio-off.png")
    /**
     * `settingRadioOn` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingRadioOn = uiTexture("maps/ui/title/setting/radio-on.png")
    /**
     * `settingSlider` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val settingSlider = uiTexture("maps/ui/title/setting/slider.png")
    /**
     * `settingBox6` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingBox6 = uiTexture("maps/ui/title/setting/box6.png")
    /**
     * `settingStyles` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingStyles = (0..3).map { uiTexture("maps/ui/title/setting/style$it.png") }
    /**
     * `settingButton` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val settingButton = uiTexture("maps/ui/title/setting/button.png")
    /**
     * `loadingSpinner` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val loadingSpinner = uiTexture("maps/ui/system-overlay/uiloading.png")
    /**
     * `settingBox1Patch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingBox1Patch = NinePatch(settingBox1, 3, 14, 14, 3)
    /**
     * `settingBox2Patch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingBox2Patch = NinePatch(settingBox2, 3, 14, 14, 3)
    /**
     * `settingSliderPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingSliderPatch = NinePatch(settingSlider, 10, 10, 7, 4)
    /**
     * `settingButtonPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val settingButtonPatch = NinePatch(settingButton, 9, 42, 42, 7)

    /**
     * `uiFont` (BitmapFont): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val uiFont: BitmapFont = KoreanFont.create(34, UI_GLYPHS, fillColor = Color.WHITE)


    /**
     * `dispose`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispose() {
        loginBackground.dispose()
        loginButtons.forEach(Texture::dispose)
        uiTextures.forEach(Texture::dispose)
        uiFont.dispose()
        dimPixel.dispose()
    }

    /**
     * `Texture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun Texture.linear() = also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    private companion object {
        /**
         * `UI_GLYPHS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val UI_GLYPHS =
            "진행도 불러오기읽을 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다.취소환경 설정항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.배경 음악 듣기효과음 듣기전투 시 전장 축소 이미지가 자동으로 표시됩니다.대화창 자동 닫힘체력 바가 유닛 위에 있습니다텍스트 속도느림중빠르게게임 속도정보 설명자세히보통요약대화창 색상확인No.---전역영천의 전투불러올 수 있나요?0123456789:()제턴 "
    }
}
