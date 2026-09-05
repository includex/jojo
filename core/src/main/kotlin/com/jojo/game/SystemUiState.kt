package com.jojo.game

/** Direct, Cocos-free UI state derived from the recovered System UI factories. */
class DialogState(private val callback: (Int) -> Unit) {
    var attached = true
    var label = ""
    var flag = 7
    val visible = MutableList(3) { true }
    var panel = false

    /**
     * 공개 메서드 `create`
     *
     * ### 파라미터
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `f` (`Int = 7`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun create(text: String, f: Int = 7) {
        label = text; flag = f
        var buttons = f and 3
        if (f and 4 != 0) buttons = buttons or 32
        panel = buttons and 32 != 0
        for (i in 0..2) visible[i] = buttons and (1 shl i) != 0
    }

    /**
     * 공개 메서드 `touch`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            callback(tag); attached = false
        }
    }
}

/**
 * class  `ChoiceDialogState`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ChoiceDialogState(private val callback: (Int) -> Unit) {
    var attached = true

    /**
     * 공개 메서드 `touch`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback(tag)
        }
    }
}

/**
 * class  `QuantityDialogState`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class QuantityDialogState(private val callback: (Int) -> Unit) {
    var n = 1
    var attached = true
    val events = mutableListOf<List<Any>>()

    /**
     * 공개 메서드 `input`
     *
     * ### 파라미터
    - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `count` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun input(value: String, count: Int) {
        val next = (value.toDoubleOrNull()?.toInt() ?: 0).coerceIn(1, count)
        if (n != next) {
            n = next; events += listOf("INPUT_CHANGE", next)
        }
    }

    /**
     * 공개 메서드 `touch`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback(if (tag == 0) n else 0)
        }
    }
}

/**
 * class  `ToggleDialogState`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ToggleDialogState(private val callback: (Int) -> Unit, initialToggle: Boolean = false) {
    var attached = true
    var checked = initialToggle
    var persistedToggle = if (initialToggle) 1 else 0
    val visible = MutableList(3) { false }

    /**
     * 공개 메서드 `create`
     *
     * ### 파라미터
    - `flag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun create(flag: Int) {
        var buttons = flag and 3; if (flag and 4 != 0) buttons = buttons or 32; for (i in 0..2) visible[i] =
            buttons and (1 shl i) != 0
    }

    /**
     * 공개 메서드 `touch`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touch(tag: Int, event: Int) {
        if (event == 2) {
            persistedToggle = if (checked) 1 else 0; callback(if (checked) tag or 2 else tag); attached = false
        }
    }
}

/**
 * class  `ToastQueue`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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

/**
 * class  `ProgressState`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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

/**
 * class  `LoadingState`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
