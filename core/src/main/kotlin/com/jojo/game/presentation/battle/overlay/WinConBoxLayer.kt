// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.battle.*


/** 승리 조건 문구를 스크롤 패널 상태로 보관하고 확인 입력 뒤 닫힘을 알린다. */

class WinConBoxLayer {
    /**
     * `CreateData`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class CreateData(val info: String, val onClose: () -> Unit)
    /**
     * `Prefab`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Prefab(
        /**
         * `rootName` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val rootName: String = "WinConBoxLayer", val backgroundNode: String = "bg0",
        /**
         * `canvasWidth` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val canvasWidth: Float = 1280f, val canvasHeight: Float = 800f,
        /**
         * `backgroundWidth` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val backgroundWidth: Float = 989f, val backgroundHeight: Float = 670f,
        /**
         * `backgroundCenterX` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val backgroundCenterX: Float = 640f, val backgroundCenterY: Float = 400f,
        /**
         * `scrollNode` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scrollNode: String = "bg0/scrollview", val scrollWidth: Float = 803f, val scrollHeight: Float = 543f,
        /**
         * `scrollLocalX` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scrollLocalX: Float = 64f, val scrollLocalY: Float = 42f,
        /**
         * `contentWidth` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val contentWidth: Float = 803f, val contentHeight: Float = 479.36f,
        /**
         * `buttonNode` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val buttonNode: String = "bg0/button", val buttonWidth: Float = 256.7f, val buttonHeight: Float = 60f,
        /**
         * `buttonLocalX` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val buttonLocalX: Float = 341.298f, val buttonLocalY: Float = -281.796f,
        /**
         * `buttonLabel` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val buttonLabel: String = "짐이 알겠다.", val buttonFontSize: Int = 32, val buttonLineHeight: Int = 36,
        /**
         * `titleNode` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val titleNode: String = "bg0/Logo_3-1", val titleWidth: Float = 53f, val titleHeight: Float = 62f,
        /**
         * `titleScale` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val titleScale: Float = 2f,
        /**
         * `richTextNode` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val richTextNode: String = "bg0/scrollview/view/content/richtext", val listenerPriority: Int = 2,
        /**
         * `panelCancelNode` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val panelCancelNode: String = "Panel_cancel",
        /**
         * `blocksUnderlyingInput` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val blocksUnderlyingInput: Boolean = true,
    )


    /** 승리 조건 문구와 스크롤 위치, 입력 차단 여부를 렌더링 값으로 제공한다. */
    data class View(
        /**
         * `prefab` (Prefab, val label: String, val scrollAtTop: Boolean, val attached: Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val prefab: Prefab, val label: String, val scrollAtTop: Boolean, val attached: Boolean,
        /**
         * `blocksUnderlyingInput` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val blocksUnderlyingInput: Boolean = true,
    )

    /**
     * `callback` ((() -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var callback: (() -> Unit)? = null
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = false
    /**
     * `scrollAtTop` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var scrollAtTop = false
    /**
     * `label` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var label = ""

    /** 승리 조건 문구를 연결하고 스크롤 상자를 열린 상태로 초기화한다. */
    fun onCreate(data: CreateData): View {
        callback = data.onClose
        attached = true
        label = data.info
        scrollAtTop = true
        return view()
    }

    /** 확인 버튼의 TOUCH_END에서 레이어를 닫고 등록된 종료 콜백을 호출한다. */
    fun onButtonTouch(eventType: Int): View {
        if (eventType == TOUCH_END) {
            attached = false // 원본 흐름에서도 콜백 실행 전에 부모에서 제거한다.
            callback?.invoke()
        }
        return view()
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view() = View(prefab = PREFAB, label = label, scrollAtTop = scrollAtTop, attached = attached)

    companion object {
        /** PREFAB: 승리 조건 상자에 붙일 기본 프리팹 식별자이다. */
        const val TOUCH_END = 2
        /**
         * `PREFAB` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val PREFAB = Prefab()
    }
}
