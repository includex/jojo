package com.jojo.game.presentation.title.evidence

import com.jojo.game.RenderEventLog
import com.jojo.game.presentation.title.LoginOptionalOverlayRoute

/** Canonical authored event traversal for title-screen optional overlays. */
internal object LoginOptionalOverlayRenderEvents {
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
