package com.jojo.game.presentation.title


/** Immutable title presentation snapshot exposed through the runtime observer port. */
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
