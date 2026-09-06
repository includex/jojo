// Verification
package com.jojo.game.verification

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration

/** VerificationHeadlessLauncher: 분리된 검증 모음을 모니터 없이 실행하는 프로세스 진입점이다. */
object VerificationHeadlessLauncher {
    /** main: 헤드리스 검증 애플리케이션을 실행한다. */
    @JvmStatic
    /**
     * `main`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
