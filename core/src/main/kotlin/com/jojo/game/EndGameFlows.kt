package com.jojo.game

/** Lifecycle flows for battle/Lose.js, ui/End.js and ui/SkipLayer.js. */
class LossFlow(private val sink: Sink) {
    /**
     * interface  `Sink`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface Sink {
        fun sound(id: Int)
        fun schedule(seconds: Int, block: () -> Unit)
        fun helper(cmd: String)
        fun msgBox(text: String, reply: (Int) -> Unit)
        fun login()
        fun endGame()
    }

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate() {
        sink.sound(0); sink.schedule(3) { sink.msgBox("다시 플레이하시겠습니까?") { if (it == 0) sink.login() else sink.endGame() } }; sink.helper(
            "showInterstitial"
        )
    }
}

/**
 * class  `TerminalFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class TerminalFlow(private val login: () -> Unit) {
    fun onCreate() = login()
    fun onEvent(event: Int) {
        if (event == 3) login()
    }
}

/**
 * class  `StorySkipFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class StorySkipFlow(private val sink: Sink) {
    /**
     * interface  `Sink`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    interface Sink {
        fun msgBox(text: String, reply: (Int) -> Unit)
        fun dispatch(name: String)
    }

    var panel = false
    var button = true
    var zIndex = 0

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate() {
        panel = false; button = true; zIndex = 999
    }

    /**
     * 공개 메서드 `touch`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touch(event: Int) {
        if (event == 2) sink.msgBox("스토리를 건너뛸까요?") {
            if (it == 0) {
                button = false; panel = true; sink.dispatch("SKIP")
            }
        }
    }

    /**
     * 공개 메서드 `swap`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun swap() {
        panel = !panel; button = !button
    }
}
