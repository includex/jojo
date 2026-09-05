package com.jojo.game

class LoadLayer {
    data class View(val labelActive: Boolean, val label: String, val anime: String)
    fun onCreate(text: String?) = View(text != null, text ?: "", "rotateBy:2:360:repeatForever")
}

class ModalLoadProductionRoute {
    var attached = false; private set
    var text: String? = null; private set
    fun getSystemTimeStarted() { attached = true; text = "검증 중……" }
    fun requestCompleted() { attached = false }
}
