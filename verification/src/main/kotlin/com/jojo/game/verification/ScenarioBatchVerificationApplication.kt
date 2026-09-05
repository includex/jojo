package com.jojo.game.verification

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import java.util.concurrent.CountDownLatch

/** Owns only the LibGDX lifecycle, marker reporting, and process completion. */
class ScenarioBatchVerificationApplication : ApplicationAdapter() {
    private val completed = CountDownLatch(1)
    @Volatile private var failure: Throwable? = null
    private var ran = false

    override fun render() {
        if (ran) return
        ran = true
        try {
            ScenarioBatchVerificationSuite().verify().forEach { message ->
                Gdx.app.log("JojoGame", message)
            }
        } catch (error: Throwable) {
            failure = error
        } finally {
            completed.countDown()
            Gdx.app.exit()
        }
    }

    fun awaitCompletion() {
        completed.await()
        failure?.let { throw it }
    }
}
