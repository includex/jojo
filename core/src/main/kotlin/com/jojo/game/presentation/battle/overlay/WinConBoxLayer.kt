// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.battle.*


/** 승리 조건 문구를 스크롤 패널 상태로 보관하고 확인 입력 뒤 닫힘을 알린다. */

class WinConBoxLayer {
    data class CreateData(val info: String, val onClose: () -> Unit)
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
        val titleScale: Float = 2f,
        val richTextNode: String = "bg0/scrollview/view/content/richtext", val listenerPriority: Int = 2,
        val panelCancelNode: String = "Panel_cancel",
        val blocksUnderlyingInput: Boolean = true,
    )


    /** 승리 조건 문구와 스크롤 위치, 입력 차단 여부를 렌더링 값으로 제공한다. */
    data class View(
        val prefab: Prefab, val label: String, val scrollAtTop: Boolean, val attached: Boolean,
        val blocksUnderlyingInput: Boolean = true,
    )

    private var callback: (() -> Unit)? = null
    private var attached = false
    private var scrollAtTop = false
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
            attached = false // removeFromParent precedes `t.func()` in source.
            callback?.invoke()
        }
        return view()
    }


    fun view() = View(prefab = PREFAB, label = label, scrollAtTop = scrollAtTop, attached = attached)

    companion object {
        /** PREFAB: 승리 조건 상자에 붙일 기본 프리팹 식별자이다. */
        const val TOUCH_END = 2
        val PREFAB = Prefab()
    }
}
