package com.jojo.game.presentation.scenario.overlay

/** Direct state implementations for the recovered choice/command Cocos layers. */
class ChoiceLayer(private val plainNewline: Boolean) {
    /**
     * data class  `Row`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Row(val tag: Int, val text: String, val listenerPriority: Int = 1)

    private var attached = true
    private var callback: ((Int) -> Unit)? = null
    private val rows = mutableListOf<Row>()
    var zIndex = 0; private set

    /**
     * Source ChooseLayer only loads the portrait when face != -1.  The image
     * itself remains an engine concern; retaining this request in the game is
     * important because it changes the observable resource/lifecycle contract.
     */
    var requestedFace: Int? = null; private set

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `info` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `face` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `fn` (`(Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(info: String, face: Int, fn: (Int) -> Unit) {
        attached = true
        rows.clear()
        zIndex = 100 // Config.Z_INDEX.CHOICE_LAYER.
        requestedFace = face.takeUnless { it == -1 }
        callback = fn
        val values = if (plainNewline) info.replace("\\n", "\n").split("\n") else info.split("<br/>")
        values.forEachIndexed { index, text -> rows += Row(index + 1, text) }
    }
    // The original listener does not guard against a detached layer.  A
    // direct repeated TOUCH_END therefore removes/calls again as well.
    /**
     * 공개 메서드 `onRowTouch`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onRowTouch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback?.invoke(tag)
        }
    }

    /**
     * 공개 메서드 `rows`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun rows() = rows.toList()
    fun attached() = attached
}

/**
 * class  `CommandLayer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class CommandLayer {
    /**
     * data class  `Button`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Button(val tag: Int, val interactable: Boolean, val priority: Int)

    private var attached = true
    private var callback: ((Int) -> Unit)? = null
    private val buttons = mutableListOf<Button>()

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `enabledMask` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `fn` (`(Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
    // Same direct-listener semantics as CommandLayer.js: no attached check.
    /**
     * 공개 메서드 `onButtonTouch`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onButtonTouch(tag: Int, event: Int) {
        if (event == 2) {
            attached = false; callback?.invoke(tag)
        }
    }

    /**
     * 공개 메서드 `onCancelTouch`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCancelTouch(event: Int) {
        if (event == 2) {
            attached = false; callback?.invoke(6)
        }
    }

    /**
     * 공개 메서드 `buttons`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun buttons() = buttons.toList()
    fun attached() = attached
}
