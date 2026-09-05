package com.jojo.game.presentation.title

import com.jojo.game.LoginOptionalOverlayRoute

/** Immutable scene snapshot shared by title drawing and render-event verification. */
internal data class TitleViewState(
    val mode: TitleMode,
    val optionalOverlayRoute: LoginOptionalOverlayRoute?,
    val loadRows: List<TitleLoadRow> = emptyList(),
    val loadConfirmationMessage: String? = null,
    val settings: TitleSettingsView? = null,
    val registrationLoading: TitleLoadingView? = null,
    val elapsedSeconds: Float = 0f,
)

internal enum class TitleMode { LOGIN, LOAD, SETTING }

internal data class TitleLoadRow(
    val number: String,
    val stage: String,
    val name: String,
    val occupied: Boolean,
)

internal data class TitleSettingsView(
    val flags: Int,
    val messageSpeed: Int,
    val notificationLevel: Int,
    val background: Int,
    val gameSpeed: Float,
)

internal data class TitleLoadingView(
    val blockerOpacity: Float,
    val imageVisible: Boolean,
)
