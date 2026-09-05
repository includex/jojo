package com.jojo.game.verification

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration

/** Monitor-independent process entry point for the isolated verification suite. */
object VerificationHeadlessLauncher {
    @JvmStatic
/**
 * 공개 메서드 `main`
 *
 * ### 파라미터
- `args` (`Array<String>`): 구현 기준으로 역할 및 허용 값 정의 필요
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

    fun main(args: Array<String>) {
        val application = ScenarioBatchVerificationApplication()
        HeadlessApplication(
            application,
            HeadlessApplicationConfiguration().apply { updatesPerSecond = 60 },
        )
        application.awaitCompletion()
    }
}
