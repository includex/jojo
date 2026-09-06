// Verification
package com.jojo.game.verification.title

import com.jojo.game.application.runtime.RuntimeTitleStartupDriver
import com.jojo.game.application.runtime.TitleStartupPresentation

/** VerificationTitleStartupDriver: 검증 실행의 시작 경로와 화면 전환을 구동한다. */
internal class VerificationTitleStartupDriver(private val route: String?) : RuntimeTitleStartupDriver {
    /** presentation: 전투 표현 상태를 검증 출력으로 변환한다. */
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
