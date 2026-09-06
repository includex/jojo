// Game
package com.jojo.game.presentation.shared.overlay

/** DialogState: 복원한 시스템 UI 생성기에서 파생한 프레임워크 독립 상태이다. */
class DialogState(private val callback: (Int) -> Unit) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `label` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var label = ""
    /**
     * `flag` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var flag = 7
    /**
     * `visible` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val visible = MutableList(3) { true }
    /**
     * `panel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var panel = false


    /**
     * `create`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun create(text: String, f: Int = 7) {
        label = text; flag = f
        var buttons = f and 3
        if (f and 4 != 0) buttons = buttons or 32
        panel = buttons and 32 != 0
        for (i in 0..2) visible[i] = buttons and (1 shl i) != 0
    }


    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            callback(tag); attached = false
        }
    }
}


/** ChoiceDialogState: 두 선택 버튼 입력을 결과 번호로 콜백에 전달하는 간단한 모달 상태다. */
class ChoiceDialogState(private val callback: (Int) -> Unit) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true


    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback(tag)
        }
    }
}


/** QuantityDialogState: 수량 문자열을 허용 범위로 보정하고 확인·취소 결과를 전달하는 입력 모달 상태다. */
class QuantityDialogState(private val callback: (Int) -> Unit) {
    /**
     * `n` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var n = 1
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val events = mutableListOf<List<Any>>()


    /**
     * `input`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun input(value: String, count: Int) {
        val next = (value.toDoubleOrNull()?.toInt() ?: 0).coerceIn(1, count)
        if (n != next) {
            n = next; events += listOf("INPUT_CHANGE", next)
        }
    }


    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback(if (tag == 0) n else 0)
        }
    }
}


/** ToggleDialogState: 확인 전에 토글 값을 바꾸고 최종 선택 결과와 함께 영속화할 값을 보관하는 모달 상태다. */
class ToggleDialogState(private val callback: (Int) -> Unit, initialToggle: Boolean = false) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `checked` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var checked = initialToggle
    /**
     * `persistedToggle` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var persistedToggle = if (initialToggle) 1 else 0
    /**
     * `visible` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val visible = MutableList(3) { false }


    /**
     * `create`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun create(flag: Int) {
        var buttons = flag and 3; if (flag and 4 != 0) buttons = buttons or 32; for (i in 0..2) visible[i] =
            buttons and (1 shl i) != 0
    }


    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            persistedToggle = if (checked) 1 else 0; callback(if (checked) tag or 2 else tag); attached = false
        }
    }
}


/** ToastQueue: 화면에 순서대로 띄울 짧은 알림 문구와 부착 상태를 보관하는 대기열이다. */
class ToastQueue {
    /**
     * `items` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val items = mutableListOf<String>()
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `create`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun create(text: String) {
        if (text.isEmpty()) {
            attached = false; return
        }; if (items.size == 5) items.removeAt(0); items += text
    }

    /**
     * `expireAll`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun expireAll() {
        items.clear(); attached = false
    }
}


/** ProgressState: 작업 진행률을 표시할 라벨과 회전 애니메이션 여부를 나타내는 UI 상태다. */
class ProgressState {
    /**
     * `label` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var label = ""
    /**
     * `spinning` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var spinning = false
    /**
     * `create`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun create() {
        spinning = true
    }

    /**
     * `set`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun set(v: Double) {
        label = "자원 로딩 중${(100 * v).toInt()}%"
    }
}


/** LoadingState: 로딩 화면의 이미지·패널 투명도·대기 시간·회전 표시를 조합하는 UI 상태다. */
class LoadingState {
    /**
     * `image` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var image = true
    /**
     * `panelOpacity` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var panelOpacity: Int? = null
    /**
     * `spinning` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var spinning = false
    /**
     * `delay` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var delay: Int? = null
    /**
     * `create`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun create(flag: Int) {
        if (flag and 3 != 0) {
            image = false; panelOpacity = 0; if (flag and 1 != 0) delay = 5
        } else spinning = true
    }

    /**
     * `time`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun time(n: Int) {
        if (delay != null && n >= delay!!) {
            image = true; panelOpacity = 100; spinning = true; delay = null
        }
    }
}
