package com.jojo.game.presentation.title

/** Routes for the optional sign-in and version overlays shown above the title screen. */
enum class LoginOptionalOverlayRoute(val state: String) {
    SIGNIN_OPEN("login-signin-open"), VERSION_OPEN("login-version-open");

    companion object {
        fun parse(value: String?) = entries.firstOrNull { it.state == value?.removeSuffix("-fixture") }
    }
}
