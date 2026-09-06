// Game
package com.jojo.game.presentation.shared.overlay

/** MapInfoLayer: 지도 정보창의 타이핑과 취소 생명주기를 관리한다. */
class MapInfoLayer(
    private val setting: Int,
    private val replace: (String) -> String,
    private val complete: () -> Unit,
    private val remove: () -> Unit
) {

    data class Data(
        val txt: String,
        val changePage: Boolean = false,
        val wepon: Boolean = false,
        val wait: Boolean = false
    )


    data class View(val text: String, val typing: Boolean, val autoCloseDelay: Int?, val attached: Boolean)

    private var content = ""
    private var next = ""
    private var typing = false
    private var autoClose: Int? = null
    private var once = false
    private var attached = false
    private var tickRendered = false
    private var data = Data("")


    fun onCreate(d: Data) {
        attached = true; setData(d)
    }


    fun setData(d: Data) {
        once = true; data = d
        val text = replace(d.txt)
        val prefix = if (d.changePage) {
            content = ""; ""
        } else if (d.wepon && content.isNotEmpty()) "<br/>" else ""; next = prefix + text; typing = true; tickRendered =
            false; autoClose = null
    }


    fun tick() {
        if (!typing) return
        var nesting = 0; while (next.isNotEmpty()) {
            val c = next[0]; next =
                next.drop(1); content += c; if (c == '<') nesting++ else if (nesting > 0 && c == '>') nesting--; if (nesting == 0) break
        }; tickRendered = true; if (next.isEmpty()) {
            typing = false; enableAutoClose()
        }
    }

    private fun enableAutoClose() {
        if (setting and AUTO_CLOSE != 0) autoClose = if (data.wait) 5 else 1
    }


    fun elapse(seconds: Int) {
        if (autoClose != null && seconds >= autoClose!!) next()
    }


    fun cancel(event: Int) {
        if (event != TOUCH_END) return; if (typing) {
            content += next; next = ""; typing = false; tickRendered = false; enableAutoClose()
        } else {
            autoClose = null; next()
        }
    }


    fun skip() {
        next()
    }

    private fun next() {
        if (once) {
            once = false; complete(); remove(); attached = false
        }
    }


    fun view() =
        View(if (tickRendered && content.contains("<color=")) "$content</c>" else content, typing, autoClose, attached)

    companion object {
        const val TOUCH_END = 2
        const val AUTO_CLOSE = 8
    }
}
