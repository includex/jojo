package com.jojo.port.desktop

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import com.jojo.port.JojoGame

/** CI/monitor-independent entry point for the production scenario verifier. */
object HeadlessVerifier {
    @JvmStatic
    fun main(args: Array<String>) {
        HeadlessApplication(
            JojoGame(verifyMode = false, allScenariosVerifyMode = true),
            HeadlessApplicationConfiguration().apply { updatesPerSecond = 60 },
        )
    }
}
