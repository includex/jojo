// Verification
package com.jojo.game.verification.evidence

import com.jojo.game.DefineUnitFlow
import com.jojo.game.DefineUnitRoute
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/**
 * DefineUnitRenderEvents: 새 무장 설정 화면의 렌더 이벤트를 `DefineUnitFlow` 상태에서 계산한다.
 *
 * 예전에는 원본의 렌더 이벤트를 gzip·base64로 저장해 두었다가 그대로 내놓았다.
 * 그러면 원본과의 비교가 순환 논증이 되고, 저장본이 낡았을 때만 어긋난다.
 * 이름·점수·다섯 능력치는 흐름이 들고 있는 값에서 나오고, 글자 폭은
 * [SourceLabelWidth]로 재현한다.
 */
internal object DefineUnitRenderEvents {
    /** 라벨 혼합 방식이다. */
    private val LABEL_BLEND = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
    /** 스프라이트 혼합 방식이다. */
    private val SPRITE_BLEND = listOf(770, 771)

    /** 다섯 능력치의 이름이다. */
    private val ABILITY_NAMES = listOf("무력", "지휘", "지력", "민첩성", "운기")

    /**
     * 능력치 줄의 y다.
     *
     * 간격이 57, 57, 58, 57로 고르지 않다. 원본 프리팹이 그렇게 배치돼 있으므로
     * 공식으로 만들지 않고 잰 값을 그대로 둔다.
     */
    private val ABILITY_ROW_Y = listOf(398.993f, 341.993f, 284.993f, 226.993f, 169.993f)

    /** 기록: 배경 위에 설정 창과 확인창을 순서대로 남긴다. */
    fun record(flow: DefineUnitFlow, route: DefineUnitRoute): String {
        val phase = "hall-define-unit-${route.key}-stable"
        val log = RenderEventLog()

        /** draw: 설정 창의 한 줄을 남긴다. */
        fun draw(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") =
            log.draw(
                phase, "DefineUnitLayer", "Canvas/Layer/$path", type, x, y, w, h, asset,
                blend = if (type == "label") LABEL_BLEND else SPRITE_BLEND, text = text,
            )

        /** label: 글자 폭을 재현한 본문 라벨을 남긴다. */
        fun label(path: String, text: String, x: Float, y: Float, width: Float = SourceLabelWidth.body(text)) =
            draw(path, "label", x, y, width, 50.4f, text = text)

        log.draw(
            phase, "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>",
        )
        log.draw(
            phase, "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            // 이 화면의 반투명 막은 80/255다. 화면마다 값이 달라 공통 상수로 묶지 않는다.
            "default_sprite_splash", opacity = 80f / 255f,
        )
        draw("bg", "tiled-sprite", 241.186f, 125.5f, 1006f, 549f, "Logo_9-1")
        draw("bg/bg1", "sprite", 241.186f, 614.5f, 1006f, 60f, "bg1")
        // 제목은 본문보다 큰 글꼴이라 본문 폭 표로 재현되지 않는다. 잰 값을 쓴다.
        draw("bg/bg1/label", "label", 611.971f, 619.3f, 264.43f, 50.4f, text = "새로운 무장 설정")
        draw("bg/face", "sprite", 185.286f, 245.277f, 384f, 480f, "1")
        label("bg/label", "이름: ", 528.471f, 556.8f)
        label("bg/label2", "점수:${flow.score}", 1098.269f, 552.557f)
        draw("bg/editbox1/BACKGROUND_SPRITE", "sliced-sprite", 621.386f, 555.3f, 249.6f, 53.4f, "box1")
        draw("bg/editbox1/TEXT_LABEL", "label", 623.386f, 555.3f, 247.6f, 53.4f, text = flow.name)
        label("bg/label", "직업:", 534.031f, 495.8f)
        draw("bg/bg3", "sliced-sprite", 621.186f, 494.3f, 250f, 53.4f, "box1")
        label("bg/bg3/label", "군주", 711.586f, 495.8f)
        listOf(0 to "확인", 1 to "초기화").forEach { (index, text) ->
            val y = if (index == 0) 143.343f else 206.618f
            draw("bg/button$index/Background", "sliced-sprite", 1038.919f, y, 190f, 56.1f, "box3")
            draw("bg/button$index/Background/Label", "label", 1083.919f, y + 10.196f, 100f, 40f, text = text)
        }
        flow.abilities.forEachIndexed { index, value ->
            val y = ABILITY_ROW_Y[index]
            val row = "bg/att_bg${index + 1}"
            label("$row/label1", "${ABILITY_NAMES[index]}:$value", 515.646f, y)
            draw("$row/button1/Background", "sliced-sprite", 711.082f, y + 2.565f, 85.3f, 43.3f, "box3")
            draw("$row/button1/Background/Label", "label", 703.732f, y + 6.2f, 100f, 40f, text = "+")
            draw("$row/button2/Background", "sliced-sprite", 803.782f, y + 2.565f, 85.3f, 43.3f, "box3")
            draw("$row/button2/Background/Label", "label", 796.432f, y + 7.744f, 100f, 40f, text = "-")
            label("$row/label2", "A", 899.525f, y)
        }
        promptText(flow)?.let { SourceMsgBox.append(log, phase, "DefineUnitLayer", it) }
        return log.jsonl()
    }

    /** 확인창 문구: 흐름이 열어 둔 확인창의 본문을 돌려준다. */
    private fun promptText(flow: DefineUnitFlow): String? = when (flow.prompt) {
        DefineUnitFlow.Prompt.RESET -> "능력을 초기화하시겠습니까?"
        DefineUnitFlow.Prompt.FINISH -> "캐릭터 생성을 종료하시겠습니까?"
        DefineUnitFlow.Prompt.NONE -> null
    }
}
