package com.jojo.game

/**
 * Renderer-independent lifecycle contract for the two source scene roots
 * which immediately hand control back to Login.
 *
 * Welcome.fire briefly owns a 1280x800 Logo_1-1 sprite, whereas End.fire has
 * no drawable child. Both replace the scene from `onCreate`, so neither root
 * has a stable post-create render phase; render parity belongs to the Login
 * scene reached by the emitted request.
 */
/**
 * class  `TerminalSceneFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class TerminalSceneFlow(private val kind: Kind) {
    /**
     * enum class  `Kind`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Kind { WELCOME, END }

    /**
     * data class  `SceneRoot`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class SceneRoot(
        val scene: String,
        val canvasWidth: Int = 1280,
        val canvasHeight: Int = 800,
        val authoredDrawables: List<Drawable>,
    )

    /**
     * data class  `Drawable`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Drawable(
        val path: String,
        val width: Int,
        val height: Int,
        val assetFrame: String,
    )

    /**
     * data class  `ReplaceScene`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class ReplaceScene(val scene: String = "Login", val flag: Int? = null)

    private val emitted = mutableListOf<ReplaceScene>()

    val root: SceneRoot = when (kind) {
        Kind.WELCOME -> SceneRoot(
            scene = "Welcome",
            authoredDrawables = listOf(Drawable("Canvas/Logo_1-1", 1280, 800, "Logo_1-1")),
        )

        Kind.END -> SceneRoot(scene = "End", authoredDrawables = emptyList())
    }

    /** UIScene.onCreate; this happens during activation, before a stable draw. */
    fun onCreate() = replaceLogin()

    /** Source UI event phase values: 3=touch/click release, 5=keyboard back. */
    fun onEvent(phase: Int) {
        when (kind) {
            Kind.WELCOME -> if (phase == 3 || phase == 5) replaceLogin()
            Kind.END -> if (phase == 3) replaceLogin()
        }
    }

    /**
     * 공개 메서드 `drainRequests`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<ReplaceScene>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun drainRequests(): List<ReplaceScene> = emitted.toList().also { emitted.clear() }

    private fun replaceLogin() {
        // Welcome explicitly supplies flag=1; End calls the one-argument
        // overload and therefore preserves the absent/default flag.
        emitted += ReplaceScene(flag = if (kind == Kind.WELCOME) 1 else null)
    }
}
