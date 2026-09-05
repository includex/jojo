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

/**
 * 공개 메서드 `awaitCompletion`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

    fun awaitCompletion() {
        completed.await()
        failure?.let { throw it }
    }
}
