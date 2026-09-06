package com.jojo.game.verification

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import java.util.concurrent.CountDownLatch

/** LibGDX 수명 주기와 검증 완료 신호만 담당한다. */
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

/** 검증 렌더링이 끝날 때까지 호출 스레드를 대기시킨다. */
    fun awaitCompletion() {
        completed.await()
        failure?.let { throw it }
    }
}
