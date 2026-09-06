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

/**
 * `TitleMode`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class TitleMode { LOGIN, LOAD, SETTING }

/**
 * `TitleLoadRow`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class TitleLoadRow(
    val number: String,
    val stage: String,
    val name: String,
    val occupied: Boolean,
)

/**
 * `TitleSettingsView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class TitleSettingsView(
    val flags: Int,
    val messageSpeed: Int,
    val notificationLevel: Int,
    val background: Int,
    val gameSpeed: Float,
)

/**
 * `TitleLoadingView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class TitleLoadingView(
    val blockerOpacity: Float,
    val imageVisible: Boolean,
)
