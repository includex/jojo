package com.jojo.game

/**
 * Testable implementation of `battle/WinConBoxLayer.js`.
 *
 * The Cocos prefab owns `bg0/scrollview`, `bg0/button`, and `lab`; rendering
 * code may bind this state verbatim without giving the layer game authority.
 */
class WinConBoxLayer {
    data class CreateData(val info: String, val onClose: () -> Unit)
    /** Serialized `Battle/scene/WinConBoxLayer` prefab geometry (1280×800 canvas). */
    data class Prefab(
        val rootName: String = "WinConBoxLayer", val backgroundNode: String = "bg0",
        val canvasWidth: Float = 1280f, val canvasHeight: Float = 800f,
        val backgroundWidth: Float = 989f, val backgroundHeight: Float = 670f,
        val backgroundCenterX: Float = 640f, val backgroundCenterY: Float = 400f,
        val scrollNode: String = "bg0/scrollview", val scrollWidth: Float = 803f, val scrollHeight: Float = 543f,
        val scrollLocalX: Float = 64f, val scrollLocalY: Float = 42f,
        val contentWidth: Float = 803f, val contentHeight: Float = 479.36f,
        val buttonNode: String = "bg0/button", val buttonWidth: Float = 256.7f, val buttonHeight: Float = 60f,
        val buttonLocalX: Float = 341.298f, val buttonLocalY: Float = -281.796f,
        val buttonLabel: String = "짐이 알겠다.", val buttonFontSize: Int = 32, val buttonLineHeight: Int = 36,
        val titleNode: String = "bg0/Logo_3-1", val titleWidth: Float = 53f, val titleHeight: Float = 62f,
        /** Fixture transform: Logo_3-1 scale=(2,2), not its raw atlas size. */
        val titleScale: Float = 2f,
        val richTextNode: String = "bg0/scrollview/view/content/richtext", val listenerPriority: Int = 2,
        /** Full-screen BlockInputEvents node, invisible but part of the fixture stack. */
        val panelCancelNode: String = "Panel_cancel", val blocksUnderlyingInput: Boolean = true,
    )
    data class View(
        val prefab: Prefab, val label: String, val scrollAtTop: Boolean, val attached: Boolean,
        val blocksUnderlyingInput: Boolean = true,
    )

    private var callback: (() -> Unit)? = null
    private var attached = false
    private var scrollAtTop = false
    private var label = ""

    /** Source onCreate: `_setBg("bg0")`, assign lab, then scrollToTop(). */
    fun onCreate(data: CreateData): View {
        callback = data.onClose
        attached = true
        label = data.info
        scrollAtTop = true
        return view()
    }

    /** Source listener invokes only for Cocos TOUCH_END (event type 2). */
    fun onButtonTouch(eventType: Int): View {
        // The recovered listener has no attachment guard.  A direct listener
        // invocation after removeFromParent therefore repeats both effects;
        // preserve that observable contract in the isolated game as well.
        if (eventType == TOUCH_END) {
            attached = false // removeFromParent precedes `t.func()` in source.
            callback?.invoke()
        }
        return view()
    }

    fun view() = View(prefab = PREFAB, label = label, scrollAtTop = scrollAtTop, attached = attached)

    companion object {
        /** Cocos.Event.EventTouch.TOUCH_END. */
        const val TOUCH_END = 2
        val PREFAB = Prefab()
    }
}
