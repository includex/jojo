// Presentation
package com.jojo.game.presentation.title

/** LoginOptionalOverlayRoute: 타이틀에서 로그인 선택 오버레이를 열거나 닫는 화면 경로를 구분한다. */
enum class LoginOptionalOverlayRoute(val state: String) {
    SIGNIN_OPEN("login-signin-open"), VERSION_OPEN("login-version-open");

    companion object {
        fun parse(value: String?) = entries.firstOrNull { it.state == value?.removeSuffix("-fixture") }
    }
}
