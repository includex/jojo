// Game
package com.jojo.game.presentation.shared.overlay

/** DialogState: 복원한 시스템 UI 생성기에서 파생한 프레임워크 독립 상태이다. */
class DialogState(private val callback: (Int) -> Unit) {
    var attached = true
    var label = ""
    var flag = 7
    val visible = MutableList(3) { true }
    var panel = false


    fun create(text: String, f: Int = 7) {
        label = text; flag = f
        var buttons = f and 3
        if (f and 4 != 0) buttons = buttons or 32
        panel = buttons and 32 != 0
        for (i in 0..2) visible[i] = buttons and (1 shl i) != 0
    }


    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            callback(tag); attached = false
        }
    }
}


/** ChoiceDialogState: 두 선택 버튼 입력을 결과 번호로 콜백에 전달하는 간단한 모달 상태다. */
class ChoiceDialogState(private val callback: (Int) -> Unit) {
    var attached = true


    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback(tag)
        }
    }
}


/** QuantityDialogState: 수량 문자열을 허용 범위로 보정하고 확인·취소 결과를 전달하는 입력 모달 상태다. */
class QuantityDialogState(private val callback: (Int) -> Unit) {
    var n = 1
    var attached = true
    val events = mutableListOf<List<Any>>()


    fun input(value: String, count: Int) {
        val next = (value.toDoubleOrNull()?.toInt() ?: 0).coerceIn(1, count)
        if (n != next) {
            n = next; events += listOf("INPUT_CHANGE", next)
        }
    }


    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback(if (tag == 0) n else 0)
        }
    }
}


/** ToggleDialogState: 확인 전에 토글 값을 바꾸고 최종 선택 결과와 함께 영속화할 값을 보관하는 모달 상태다. */
class ToggleDialogState(private val callback: (Int) -> Unit, initialToggle: Boolean = false) {
    var attached = true
    var checked = initialToggle
    var persistedToggle = if (initialToggle) 1 else 0
    val visible = MutableList(3) { false }


    fun create(flag: Int) {
        var buttons = flag and 3; if (flag and 4 != 0) buttons = buttons or 32; for (i in 0..2) visible[i] =
            buttons and (1 shl i) != 0
    }


    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            persistedToggle = if (checked) 1 else 0; callback(if (checked) tag or 2 else tag); attached = false
        }
    }
}


/** ToastQueue: 화면에 순서대로 띄울 짧은 알림 문구와 부착 상태를 보관하는 대기열이다. */
class ToastQueue {
    val items = mutableListOf<String>()
    var attached = true
    fun create(text: String) {
        if (text.isEmpty()) {
            attached = false; return
        }; if (items.size == 5) items.removeAt(0); items += text
    }

    fun expireAll() {
        items.clear(); attached = false
    }
}


/** ProgressState: 작업 진행률을 표시할 라벨과 회전 애니메이션 여부를 나타내는 UI 상태다. */
class ProgressState {
    var label = ""
    var spinning = false
    fun create() {
        spinning = true
    }

    fun set(v: Double) {
        label = "자원 로딩 중${(100 * v).toInt()}%"
    }
}


/** LoadingState: 로딩 화면의 이미지·패널 투명도·대기 시간·회전 표시를 조합하는 UI 상태다. */
class LoadingState {
    var image = true
    var panelOpacity: Int? = null
    var spinning = false
    var delay: Int? = null
    fun create(flag: Int) {
        if (flag and 3 != 0) {
            image = false; panelOpacity = 0; if (flag and 1 != 0) delay = 5
        } else spinning = true
    }

    fun time(n: Int) {
        if (delay != null && n >= delay!!) {
            image = true; panelOpacity = 100; spinning = true; delay = null
        }
    }
}
