// Runtime
package com.jojo.game.application.runtime

/** RuntimeTitleStartupDriver: 제목 화면의 설정·불러오기 자동 조작 상태를 제공하는 계약이다. */
interface RuntimeTitleStartupDriver {
    fun presentation(): TitleStartupPresentation = TitleStartupPresentation()
}

/** TitleStartupPresentation: 제목 화면에서 자동으로 열 패널과 선택 행을 지정하는 표시 값이다. */
data class TitleStartupPresentation(
    val settingsOpen: Boolean = false,
    val loadOpen: Boolean = false,
    val loadRow: Int? = null,
    val useInitialSettings: Boolean = false,
)
