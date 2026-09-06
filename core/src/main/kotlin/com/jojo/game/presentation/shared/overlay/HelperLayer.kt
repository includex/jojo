package com.jojo.game.presentation.shared.overlay

/**
 * Injectable implementation of `ui/HelperLayer.js` and its `Global/scene/HelperLayer`
 * prefab.  The string remains Cocos RichText markup until a renderer consumes
 * it; no colour information is discarded in this layer.
 */

class HelperLayer(private val model: Model, private val removeFromParent: () -> Unit = {}) {

    data class Info(val type: Int, val reserved: String = "", val text: String)


    interface Model {

        fun getInfo(): Iterable<Info>


        fun replaceSpeInfo(text: String, flags: Int): String
    }


    data class Prefab(
        val root: String = "HelperLayer", val background: String = "Logo_12-1",
        val canvasWidth: Float = 1280f, val canvasHeight: Float = 800f,
        val backgroundWidth: Float = 1193f, val backgroundHeight: Float = 751f,
        val scrollPath: String = "Logo_12-1/scrollview", val scrollWidth: Float = 1161f, val scrollHeight: Float = 616f,
        val richTextPath: String = "Logo_12-1/scrollview/view/content/richtext",
        val richTextMaxWidth: Int = 1157, val richTextLineHeight: Int = 50,
        val buttonPath: String = "Logo_12-1/button0", val buttonWidth: Float = 147.6f, val buttonHeight: Float = 56f,
        val buttonLocalX: Float = 502.065f, val buttonLocalY: Float = -339.813f,
        val buttonText: String = "확인", val listenerPriority: Int = 1,
    )


    data class View(
        val prefab: Prefab,
        val richText: String,
        val attached: Boolean,
        val blocksUnderlyingInput: Boolean = true
    )

    private var attached = false
    private var richText = ""

    /** Exact onCreate: set bg, build four coloured rows, then replace with flags 15. */
    fun onCreate(): View {
        val rows = buildString {
            for ((type, _, text) in model.getInfo()) {
                when (type) {
                    1 -> append("<color=#000000>").append(text).append("</color>")
                    2 -> append("<color=#ff0000>").append(text).append("</color>")
                    3 -> append("<color=#0000ff>").append(text).append("</color>")
                    4 -> append("<color=#f000f0>").append(text).append("</color>")
                }
                // JS appends a break even for an unknown type.
                append("<br/>")
            }
        }
        richText = model.replaceSpeInfo(rows, REPLACE_FLAGS)
        attached = true
        return view()
    }

    /** `addTouchEventListener(button0, ..., 1)`: only TOUCH_END removes it. */
    fun onButtonTouch(eventType: Int): View {
        if (eventType == TOUCH_END) {
            removeFromParent(); attached = false
        }
        return view()
    }


    fun view() = View(PREFAB, richText, attached)

    companion object {
        const val TOUCH_END = 2
        const val REPLACE_FLAGS = 15
        val PREFAB = Prefab()
    }
}

/** Exact `Model.replaceSpeInfo(text, flags)` behavior needed by HelperLayer. */
object SourceInfoText {
    fun replace(
        input: String,
        flags: Int = 15,
        unitName: (Int) -> String = { "" },
        global: (Int) -> Int = { 0 },
        colors: List<String> = emptyList(),
    ): String {
        var text = input.trim()
        if (text.isEmpty()) return text
        if (flags and 1 != 0) {
            Regex("&\\*[.+](\\d+)\\n").findAll(text).toList()
                .forEach { text = text.replace(it.value, "&${it.groupValues[1]}\n") }
            Regex("\\*[.+](\\d+)").findAll(text).toList()
                .forEach { text = text.replace(it.value, unitName(it.groupValues[1].toInt())) }
        }
        if (flags and 2 != 0) {
            Regex("\\*/(\\d+)").findAll(text).toList()
                .forEach { text = text.replace(it.value, global(it.groupValues[1].toInt()).toString()) }
            Regex("\\*(\\d+)").findAll(text).toList()
                .forEach { text = text.replace(it.value, global(it.groupValues[1].toInt()).toString()) }
        }
        if (flags and 4 != 0) text = text.replace("\n", "<br/>")
        if (flags and 8 != 0) {
            val pattern = Regex("\\[C(\\w\\w)(.*?)\\]")
            while (true) {
                val m = pattern.find(text) ?: break
                val color = colors.getOrNull(m.groupValues[1].toInt(16)) ?: ""
                text = text.replace(m.value, "<color=$color>${m.groupValues[2]}</color>")
            }
        }
        return text
    }
}
