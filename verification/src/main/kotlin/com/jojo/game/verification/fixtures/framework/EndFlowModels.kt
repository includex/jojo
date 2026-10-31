package com.jojo.game.verification.fixtures.framework

import com.jojo.game.presentation.i18n.SystemMessage

/** Legacy end-screen reference models used only by EndFlowTraceHarness. */
internal class LossFlow(private val sink: Sink) {
    interface Sink {
        fun sound(id: Int)
        fun schedule(seconds: Int, block: () -> Unit)
        fun helper(cmd: String)
        fun msgBox(text: String, reply: (Int) -> Unit)
        fun login()
        fun endGame()
    }

    fun onCreate() {
        sink.sound(0)
        sink.schedule(3) {
            sink.msgBox(SystemMessage.S_B8C06C1007) {
                if (it == 0) sink.login() else sink.endGame()
            }
        }
        sink.helper("showInterstitial")
    }
}

internal class TerminalFlow(private val login: () -> Unit) {
    fun onCreate() = login()

    fun onEvent(event: Int) {
        if (event == 3) login()
    }
}
