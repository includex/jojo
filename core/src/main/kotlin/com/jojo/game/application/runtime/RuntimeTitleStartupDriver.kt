package com.jojo.game.application.runtime

/** Optional external request for title startup presentation. */
interface RuntimeTitleStartupDriver {
    fun presentation(): TitleStartupPresentation = TitleStartupPresentation()
}

data class TitleStartupPresentation(
    val settingsOpen: Boolean = false,
    val loadOpen: Boolean = false,
    val loadRow: Int? = null,
    val useInitialSettings: Boolean = false,
)
