package com.jojo.game.verification.title

import com.jojo.game.application.runtime.RuntimeTitleStartupDriver
import com.jojo.game.application.runtime.TitleStartupPresentation

internal class VerificationTitleStartupDriver(private val route: String?) : RuntimeTitleStartupDriver {
    override fun presentation() = when {
        route == "login-setting" -> TitleStartupPresentation(settingsOpen = true, useInitialSettings = true)
        route == "login-load" -> TitleStartupPresentation(loadOpen = true)
        route?.startsWith("login-load-row") == true -> TitleStartupPresentation(
            loadOpen = true,
            loadRow = route.removePrefix("login-load-row").toIntOrNull(),
        )
        else -> TitleStartupPresentation()
    }
}
