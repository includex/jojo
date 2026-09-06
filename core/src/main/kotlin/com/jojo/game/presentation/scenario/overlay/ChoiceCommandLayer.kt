// Scenario
package com.jojo.game.presentation.scenario.overlay

/** ChoiceLayer: 시나리오 선택지를 행으로 구성하고, 터치 완료 시 선택한 태그를 콜백으로 전달한다. */
class ChoiceLayer(private val plainNewline: Boolean) {
    /** 선택지 한 줄의 태그와 표시 정보를 담습니다. */
    data class Row(val tag: Int, val text: String, val listenerPriority: Int = 1)

    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = true
    /**
     * `callback` (((Int) -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var callback: ((Int) -> Unit)? = null
    /**
     * `rows` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val rows = mutableListOf<Row>()
    /**
     * `zIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var zIndex = 0; private set

    /** 얼굴 번호가 -1이 아닐 때 요청한 초상화 번호입니다. */
    var requestedFace: Int? = null; private set

    /** 선택지 문장을 분해하고 선택 콜백을 등록합니다. */
    fun onCreate(info: String, face: Int, fn: (Int) -> Unit) {
        attached = true
        rows.clear()
        zIndex = 100 // Config.Z_INDEX.CHOICE_LAYER.
        requestedFace = face.takeUnless { it == -1 }
        callback = fn
        val values = if (plainNewline) info.replace("\\n", "\n").split("\n") else info.split("<br/>")
        values.forEachIndexed { index, text -> rows += Row(index + 1, text) }
    }
    /** 터치 종료 이벤트에서 선택지를 확정합니다. */
    fun onRowTouch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback?.invoke(tag)
        }
    }

    /** 현재 선택지 목록의 복사본을 반환합니다. */
    fun rows() = rows.toList()
    /**
     * `attached`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun attached() = attached
}

/** CommandLayer: 활성화 비트 마스크에 맞는 시나리오 명령 버튼을 만들고, 선택 결과를 콜백으로 전달한다. */
class CommandLayer {
    /** 명령 버튼의 태그와 활성화 상태입니다. */
    data class Button(val tag: Int, val interactable: Boolean, val priority: Int)

    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = true
    /**
     * `callback` (((Int) -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var callback: ((Int) -> Unit)? = null
    /**
     * `buttons` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val buttons = mutableListOf<Button>()

    /** 활성화 비트 마스크로 명령 버튼을 구성합니다.
    - `enabledMask`: 활성화할 명령을 나타내는 비트 마스크입니다.
    - `fn`: 버튼 태그를 전달하는 선택 콜백입니다.
     *
     * 입력 콜백은 선택된 버튼 태그를 받습니다.
     */

    fun onCreate(enabledMask: Int, fn: (Int) -> Unit) {
        attached = true; callback = fn; buttons.clear(); repeat(7) { i ->
            buttons += Button(
                i,
                i >= 5 || (enabledMask and (1 shl i)) != 0,
                1
            )
        }
    }
    /** 터치 종료 이벤트에서 명령 버튼을 선택합니다. */
    fun onButtonTouch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback?.invoke(tag)
        }
    }

    /** 취소 버튼 터치 종료를 명령 태그 6으로 전달합니다. */
    fun onCancelTouch(event: Int) {
        if (event == 2) {
            attached = false; callback?.invoke(6)
        }
    }

    /** 현재 명령 버튼 목록의 복사본을 반환합니다. */
    fun buttons() = buttons.toList()
    /**
     * `attached`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun attached() = attached
}
