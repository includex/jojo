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

class TerminalSceneFlow(private val kind: Kind) {

    enum class Kind { WELCOME, END }


    data class SceneRoot(
        val scene: String,
        val canvasWidth: Int = 1280,
        val canvasHeight: Int = 800,
        val authoredDrawables: List<Drawable>,
    )


    data class Drawable(
        val path: String,
        val width: Int,
        val height: Int,
        val assetFrame: String,
    )


    data class ReplaceScene(val scene: String = "Login", val flag: Int? = null)

    private val emitted = mutableListOf<ReplaceScene>()

    val root: SceneRoot = when (kind) {
        Kind.WELCOME -> SceneRoot(
            scene = "Welcome",
            authoredDrawables = listOf(Drawable("Canvas/Logo_1-1", 1280, 800, "Logo_1-1")),
        )

        Kind.END -> SceneRoot(scene = "End", authoredDrawables = emptyList())
    }

    /** 장면 활성화 시 안정적인 첫 화면 전에 생성 처리를 실행한다. */
    fun onCreate() = replaceLogin()

    /** UI 이벤트 단계 값 중 3은 입력 해제, 5는 뒤로가기를 뜻한다. */
    fun onEvent(phase: Int) {
        when (kind) {
            Kind.WELCOME -> if (phase == 3 || phase == 5) replaceLogin()
            Kind.END -> if (phase == 3) replaceLogin()
        }
    }


    fun drainRequests(): List<ReplaceScene> = emitted.toList().also { emitted.clear() }

    private fun replaceLogin() {
        // 환영 화면은 flag=1을 명시하고 종료 화면은 인자 하나짜리 호출로
        // 생략된 기본 플래그를 유지한다.
        emitted += ReplaceScene(flag = if (kind == Kind.WELCOME) 1 else null)
    }
}
