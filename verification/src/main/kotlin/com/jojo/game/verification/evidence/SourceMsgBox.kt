// Verification
package com.jojo.game.verification.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/**
 * SourceMsgBox: 원본 MsgBox 프리팹의 렌더 이벤트를 남긴다.
 *
 * 원본은 확인창을 어느 화면에서 띄우든 같은 프리팹을 같은 자리에 붙인다. 화면마다
 * 같은 여덟(또는 여섯) 줄을 따로 적어 두면 한 곳만 고쳐질 때 조용히 갈라진다.
 */
internal object SourceMsgBox {
    /** 라벨 혼합 방식이다. */
    private val LABEL_BLEND = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
    /** 스프라이트 혼합 방식이다. */
    private val SPRITE_BLEND = listOf(770, 771)

    /**
     * 기록: 확인창 한 장을 남긴다.
     *
     * `singleButton`이면 원본 `flag: 1`과 같이 단추가 하나이고 가운데로 온다.
     */
    fun append(
        log: RenderEventLog,
        phase: String,
        layer: String,
        text: String,
        singleButton: Boolean = false,
        confirmText: String = "예",
        cancelText: String = "비",
    ) {
        /** draw: 확인창 한 줄을 남긴다. */
        fun draw(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String?, body: String = "") =
            log.draw(
                phase, layer, path, type, x, y, w, h, asset,
                blend = if (type == "label") LABEL_BLEND else SPRITE_BLEND, text = body,
            )
        draw("Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
        draw("Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
        draw("Canvas/Layer/bg0/Logo_3-1", "sprite", 453.005f, 373.951f, 106f, 124f, "Logo_3-1")
        draw("Canvas/Layer/bg0/label", "label", 573.686f, 335f, 463f, 190f, null, text)
        if (!singleButton) {
            draw("Canvas/Layer/bg0/btns/button1/Background", "sliced-sprite", 554.186f, 271.285f, 180f, 50f, "box3")
            draw(
                "Canvas/Layer/bg0/btns/button1/Background/Label", "label",
                557.336f, 279.085f, 168.1f, 40f, null, cancelText,
            )
        }
        val confirmX = if (singleButton) 654.186f else 754.186f
        draw("Canvas/Layer/bg0/btns/button0/Background", "sliced-sprite", confirmX, 271.285f, 180f, 50f, "box3")
        draw(
            "Canvas/Layer/bg0/btns/button0/Background/Label", "label",
            confirmX + 3.4f, 279.085f, 169.4f, 40f, null, confirmText,
        )
    }
}
