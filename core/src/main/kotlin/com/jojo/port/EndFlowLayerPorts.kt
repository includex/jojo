package com.jojo.port

/** Direct lifecycle ports for battle/Lose.js, ui/End.js and ui/SkipLayer.js. */
class LoseLayerPort(private val sink: Sink) {
    interface Sink { fun sound(id: Int); fun schedule(seconds: Int, block: () -> Unit); fun helper(cmd: String); fun msgBox(text: String, reply: (Int) -> Unit); fun login(); fun endGame() }
    fun onCreate() { sink.sound(0); sink.schedule(3) { sink.msgBox("다시 플레이하시겠습니까?") { if (it == 0) sink.login() else sink.endGame() } }; sink.helper("showInterstitial") }
}
class EndLayerPort(private val login: () -> Unit) { fun onCreate() = login(); fun onEvent(event: Int) { if (event == 3) login() } }
class SkipLayerPort(private val sink: Sink) {
    interface Sink { fun msgBox(text: String, reply: (Int) -> Unit); fun dispatch(name: String) }
    var panel = false; var button = true; var zIndex = 0
    fun onCreate() { panel=false; button=true; zIndex=999 }
    fun touch(event: Int) { if(event==2) sink.msgBox("스토리를 건너뛸까요?") { if(it==0) { button=false; panel=true; sink.dispatch("SKIP") } } }
    fun swap() { panel=!panel; button=!button }
}
