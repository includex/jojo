package com.jojo.game.verification.fixtures.framework

/** Reference models for legacy SDK traces; these are not game runtime services. */
internal class ServiceFlow(private val sink: (String) -> Unit, private val done: () -> Unit) {
    var attached = true

    fun touch(button: Int, event: Int) {
        if (event != 2) return
        when (button) {
            0 -> { attached = false; done() }
            1 -> sink("layer:skm:4")
        }
    }
}

internal class ServiceMenuState(flag: Int) {
    var attached = true
    val visible = List(5) { i -> (if (flag and 4 != 0) flag or 16 else flag) and (1 shl i) != 0 }

    fun touch(event: Int) {
        if (event != 2) attached = false
    }
}
