// Verification
package com.jojo.game.verification

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import java.util.concurrent.CountDownLatch

/** ScenarioBatchVerificationApplication: LibGDX 수명 주기와 검증 완료 신호만 담당한다. */
class ScenarioBatchVerificationApplication : ApplicationAdapter() {
    /** completed: 검증 완료 신호를 전달해 대기 중인 호출을 깨운다. */
    private val completed = CountDownLatch(1)
    /** failure: 검증 중 발생한 예외를 보관한다. */
    @Volatile private var failure: Throwable? = null
    /** ran: 검증 렌더링을 한 번만 실행했는지 나타낸다. */
    private var ran = false

    /** render: 검증 입력을 처리하고 관련 상태를 갱신한다. */
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

    /** awaitCompletion: 검증 렌더링이 끝날 때까지 호출 스레드를 대기시킨다. */
    fun awaitCompletion() {
        completed.await()
        failure?.let { throw it }
    }
}
