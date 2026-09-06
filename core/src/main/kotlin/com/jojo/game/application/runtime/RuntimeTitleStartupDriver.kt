// Runtime
package com.jojo.game.application.runtime

/** RuntimeTitleStartupDriver: 제목 화면의 설정·불러오기 자동 조작 상태를 제공하는 계약이다. */
interface RuntimeTitleStartupDriver {
    /**
     * `presentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun presentation(): TitleStartupPresentation = TitleStartupPresentation()
}

/** TitleStartupPresentation: 제목 화면에서 자동으로 열 패널과 선택 행을 지정하는 표시 값이다. */
data class TitleStartupPresentation(
    val settingsOpen: Boolean = false,
    val loadOpen: Boolean = false,
    val loadRow: Int? = null,
    val useInitialSettings: Boolean = false,
)
