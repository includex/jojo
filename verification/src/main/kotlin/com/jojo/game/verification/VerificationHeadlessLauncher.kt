package com.jojo.game.verification

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration

/** Monitor-independent process entry point for the isolated verification suite. */
object VerificationHeadlessLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val application = ScenarioBatchVerificationApplication()
        HeadlessApplication(
            application,
            HeadlessApplicationConfiguration().apply { updatesPerSecond = 60 },
        )
        application.awaitCompletion()
    }
}
