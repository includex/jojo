// Verification
package com.jojo.game.verification.title.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog
import com.jojo.game.presentation.title.LoginOptionalOverlayRoute

/** LoginOptionalOverlayRenderEvents: 타이틀 선택 오버레이의 원본 이벤트 순서를 기준 형태로 재현한다. */
internal object LoginOptionalOverlayRenderEvents {
    /** append: 검증 이벤트와 산출물을 기록한다. */
    fun append(log: RenderEventLog, route: LoginOptionalOverlayRoute) {
        val context = LoginOptionalOverlayEventContext(
            log = log,
            phase = route.state,
            layer = if (route == LoginOptionalOverlayRoute.SIGNIN_OPEN) "SignInLayer" else "VersionInfoLayer",
        )
        when (route) {
            LoginOptionalOverlayRoute.SIGNIN_OPEN -> {
                writeSignInOverlayChrome(context)
                writeSignInOverlayFirstAttendanceRows(context)
                writeSignInOverlayRemainingAttendanceRows(context)
                writeSignInOverlayActions(context)
            }
            LoginOptionalOverlayRoute.VERSION_OPEN -> writeVersionOverlayEvents(context)
        }
    }
}
