package com.jojo.game

/** Direct, Cocos-free UI state derived from the recovered System UI factories. */
class DialogState(private val callback: (Int) -> Unit) {
    var attached = true; var label = ""; var flag = 7
    val visible = MutableList(3) { true }; var panel = false
    fun create(text: String, f: Int = 7) {
        label = text; flag = f
        var buttons = f and 3
        if (f and 4 != 0) buttons = buttons or 32
        panel = buttons and 32 != 0
        for (i in 0..2) visible[i] = buttons and (1 shl i) != 0
    }
    fun touch(tag: Int, event: Int) { if (event == 2) { callback(tag); attached = false } }
}
class ChoiceDialogState(private val callback: (Int) -> Unit) {
    var attached = true
    fun touch(tag: Int, event: Int) { if (event == 2) { attached = false; callback(tag) } }
}
class QuantityDialogState(private val callback: (Int) -> Unit) {
    var n = 1; var attached = true; val events = mutableListOf<List<Any>>()
    fun input(value: String, count: Int) {
        val next = (value.toDoubleOrNull()?.toInt() ?: 0).coerceIn(1, count)
        if (n != next) { n = next; events += listOf("INPUT_CHANGE", next) }
    }
    fun touch(tag: Int, event: Int) { if (event == 2) { attached = false; callback(if (tag == 0) n else 0) } }
}
class ToggleDialogState(private val callback: (Int) -> Unit, initialToggle: Boolean = false) {
    var attached = true; var checked = initialToggle; var persistedToggle = if (initialToggle) 1 else 0
    val visible = MutableList(3) { false }
    fun create(flag: Int) { var buttons = flag and 3; if (flag and 4 != 0) buttons = buttons or 32; for (i in 0..2) visible[i] = buttons and (1 shl i) != 0 }
    fun touch(tag: Int, event: Int) { if (event == 2) { persistedToggle = if (checked) 1 else 0; callback(if (checked) tag or 2 else tag); attached = false } }
}
class ToastQueue { val items = mutableListOf<String>(); var attached = true; fun create(text: String) { if (text.isEmpty()) { attached = false; return }; if (items.size == 5) items.removeAt(0); items += text }; fun expireAll() { items.clear(); attached = false } }
class ProgressState { var label = ""; var spinning = false; fun create() { spinning = true }; fun set(v: Double) { label = "자원 로딩 중${(100 * v).toInt()}%" } }
class LoadingState { var image = true; var panelOpacity: Int? = null; var spinning = false; var delay: Int? = null; fun create(flag: Int) { if (flag and 3 != 0) { image = false; panelOpacity = 0; if (flag and 1 != 0) delay = 5 } else spinning = true }; fun time(n: Int) { if (delay != null && n >= delay!!) { image = true; panelOpacity = 100; spinning = true; delay = null } } }
