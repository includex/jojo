// Presentation
package com.jojo.game.presentation.title

/** TitleViewState: 타이틀 화면에 표시할 메뉴·선택 상태·오버레이 정보를 묶은 불변 화면 모델이다. */
data class TitleViewState(
    val mode: TitleMode,
    val optionalOverlayRoute: LoginOptionalOverlayRoute?,
    val loadRows: List<TitleLoadRow> = emptyList(),
    val loadConfirmationMessage: String? = null,
    val settings: TitleSettingsView? = null,
    val registrationLoading: TitleLoadingView? = null,
    val elapsedSeconds: Float = 0f,
)

enum class TitleMode { LOGIN, LOAD, SETTING }

data class TitleLoadRow(
    val number: String,
    val stage: String,
    val name: String,
    val occupied: Boolean,
)

data class TitleSettingsView(
    val flags: Int,
    val messageSpeed: Int,
    val notificationLevel: Int,
    val background: Int,
    val gameSpeed: Float,
)

data class TitleLoadingView(
    val blockerOpacity: Float,
    val imageVisible: Boolean,
)
