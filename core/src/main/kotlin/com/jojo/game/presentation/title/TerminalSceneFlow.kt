// Title
package com.jojo.game.presentation.title

/** TerminalSceneFlow: Login으로 즉시 제어를 돌려주는 두 원본 장면 루트의 렌더러 독립 수명 주기 계약이다. Welcome은 잠시 Logo_1-1을 보유하고 End는 그리지 않으며, 두 장면 모두 생성 시 Login으로 교체한다. */

class TerminalSceneFlow(private val kind: Kind) {

    /**
     * `Kind`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class Kind { WELCOME, END }


    /**
     * `SceneRoot`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class SceneRoot(
        /**
         * `scene` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scene: String,
        /**
         * `canvasWidth` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val canvasWidth: Int = 1280,
        /**
         * `canvasHeight` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val canvasHeight: Int = 800,
        /**
         * `authoredDrawables` (List<Drawable>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val authoredDrawables: List<Drawable>,
    )


    /**
     * `Drawable`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Drawable(
        /**
         * `path` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val path: String,
        /**
         * `width` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val width: Int,
        /**
         * `height` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val height: Int,
        /**
         * `assetFrame` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val assetFrame: String,
    )


    /**
     * `ReplaceScene`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class ReplaceScene(val scene: String = "Login", val flag: Int? = null)

    /**
     * `emitted` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val emitted = mutableListOf<ReplaceScene>()

    /**
     * `root` (SceneRoot): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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


    /**
     * `drainRequests`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun drainRequests(): List<ReplaceScene> = emitted.toList().also { emitted.clear() }

    /**
     * `replaceLogin`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun replaceLogin() {
        // 환영 화면은 flag=1을 명시하고 종료 화면은 인자 하나짜리 호출로
        // 생략된 기본 플래그를 유지한다.
        emitted += ReplaceScene(flag = if (kind == Kind.WELCOME) 1 else null)
    }
}
