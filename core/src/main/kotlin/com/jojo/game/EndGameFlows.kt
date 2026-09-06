package com.jojo.game

/** 전투 패배와 종료·건너뛰기 화면의 생명주기를 관리한다. */
class LossFlow(private val sink: Sink) {

    interface Sink {
        fun sound(id: Int)
        fun schedule(seconds: Int, block: () -> Unit)
        fun helper(cmd: String)
        fun msgBox(text: String, reply: (Int) -> Unit)
        fun login()
        fun endGame()
    }


    fun onCreate() {
        sink.sound(0); sink.schedule(3) { sink.msgBox("다시 플레이하시겠습니까?") { if (it == 0) sink.login() else sink.endGame() } }; sink.helper(
            "showInterstitial"
        )
    }
}


class TerminalFlow(private val login: () -> Unit) {
    fun onCreate() = login()
    fun onEvent(event: Int) {
        if (event == 3) login()
    }
}


class StorySkipFlow(private val sink: Sink) {

    interface Sink {
        fun msgBox(text: String, reply: (Int) -> Unit)
        fun dispatch(name: String)
    }

    var panel = false
    var button = true
    var zIndex = 0


    fun onCreate() {
        panel = false; button = true; zIndex = 999
    }


    fun touch(event: Int) {
        if (event == 2) sink.msgBox("스토리를 건너뛸까요?") {
            if (it == 0) {
                button = false; panel = true; sink.dispatch("SKIP")
            }
        }
    }


    fun swap() {
        panel = !panel; button = !button
    }
}
