// Verification
package com.jojo.game.verification.cmd

import com.jojo.game.presentation.shared.evidence.RenderEventLog
import com.jojo.game.presentation.shared.overlay.CmdLayer

/**
 * CmdRenderEvents: 내부 테스트 도구 패널의 렌더 이벤트를 `CmdLayer` 상태에서 계산한다.
 *
 * 예전에는 원본의 렌더 이벤트를 gzip·base64로 저장해 두었다가 그대로 내놓았다.
 * 그러면 원본과의 비교가 순환 논증이 되고, 저장본이 낡았을 때만 어긋난다.
 * 여기서는 항목 목록·선택 상태·안내창을 `CmdLayer`가 들고 있는 값에서 만들어 낸다.
 * 좌표는 원본 프레임에서 잰 고정값이고, 화면 배율 0.86은 마지막에 한 번만 적용한다.
 */
internal object CmdRenderEvents {
    /** 화면 배율: 설계 좌표계 1488.372x800을 1280x688로 옮긴다. */
    private const val SCALE = .86f
    /** 라벨 혼합 방식이다. */
    private val LABEL_BLEND = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
    /** 스프라이트 혼합 방식이다. */
    private val SPRITE_BLEND = listOf(770, 771)

    /** 첫 줄의 y와 줄 간격이다. */
    private const val FIRST_ROW_Y = 642.5f
    private const val ROW_PITCH = 74f

    /** 패널 단추의 그리기 순서다. 원본은 zIndex 때문에 2번을 마지막에 낸다. */
    private val BUTTON_ORDER = listOf(0, 1, 3, 4, 5, 2)

    /** 패널 단추의 y·문구·문구 좌표다. */
    private val BUTTONS = mapOf(
        0 to Button(54f, "확인", 1043.829f, 70f, 100f, 40f),
        1 to Button(275f, "복사 활성화 코드", 972.839f, 281.28f, 241.98f, 59.44f),
        2 to Button(164f, "도움말 보기", 1008.979f, 170.28f, 169.7f, 59.44f),
        3 to Button(386f, "후원", 1055.229f, 395.8f, 71.2f, 52.4f),
        4 to Button(496f, "전체 선택", 1015.074f, 505.8f, 151.51f, 52.4f),
        5 to Button(607f, "활성화 확인", 996.774f, 615.8f, 188.11f, 54.4f),
    )

    /** 패널 단추 하나의 배치다. */
    private data class Button(
        val y: Float,
        val text: String,
        val labelX: Float,
        val labelY: Float,
        val labelWidth: Float,
        val labelHeight: Float,
    )

    /** 기록: 로그인 배경 위에 패널과 안내창을 순서대로 남긴다. */
    fun record(layer: CmdLayer): String {
        val log = RenderEventLog()
        appendLoginBase(log)
        appendPanel(log, layer)
        appendPrompt(log, layer)
        return log.jsonl()
    }

    /** 그리기: 설계 좌표를 화면 배율로 옮겨 한 건을 남긴다. */
    private fun draw(
        log: RenderEventLog,
        path: String,
        type: String,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        asset: String? = null,
        text: String = "",
        opacity: Float = 1f,
        layerName: String = "CmdLayer",
    ) = log.draw(
        "login-cmd", layerName, path, type, x * SCALE, y * SCALE, w * SCALE, h * SCALE, asset,
        opacity = opacity, blend = if (type == "label") LABEL_BLEND else SPRITE_BLEND, text = text,
    )

    /** 로그인 배경: 패널 뒤에 남아 있는 타이틀 화면과 반투명 막이다. */
    private fun appendLoginBase(log: RenderEventLog) {
        log.draw(
            "login-cmd", "HallLayer", "Canvas/bg", "sprite", 0f, 0f, 1280f, 688f,
            "assets/resources/native/4d/4debf9ca-54d9-48e2-855c-34ef06c80bc4.5e28d.jpg#Logo_1-1",
        )
        floatArrayOf(582f, 456f, 329f, 203f).forEachIndexed { index, y ->
            log.draw(
                "login-cmd", "CmdLayer", "Canvas/Layer/bg1/button$index/Background", "sliced-sprite",
                945.46f, y * SCALE - 37.84f, 302.72f, 75.68f, "U_select_12-1_$index",
            )
        }
        log.draw(
            "login-cmd", "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1280f, 688f,
            "default_sprite_splash", opacity = 100f / 255f,
        )
    }

    /** 패널: 제목줄, 스크롤 영역, 항목 줄, 오른쪽 단추 묶음이다. */
    private fun appendPanel(log: RenderEventLog, layer: CmdLayer) {
        val root = "Canvas/Layer/Logo_12-1"
        draw(log, root, "tiled-sprite", 147.686f, 24.5f, 1193f, 751f, "Logo_9-1")
        draw(log, "$root/box4", "sliced-sprite", 147.686f, 24.5f, 1193f, 751f, "box4")
        draw(log, "$root/bg1", "sprite", 147.686f, 715.5f, 1193f, 60f, "bg1")
        draw(log, "$root/bg1/box3", "sliced-sprite", 147.686f, 715.5f, 1193f, 60f, "box3")
        draw(log, "$root/bg1/label", "label", 155.139f, 721.3f, 266.43f, 52.4f, text = "내부 테스트 도구")
        // 아무것도 고르지 않았으면 이 라벨은 빈 문자열이고, Cocos는 빈 라벨을 그리지 않는다.
        if (layer.label.isNotEmpty()) {
            draw(log, "$root/bg1/label1", "label", 794.066f, 720.3f, 382.12f, 54.4f, text = layer.label)
        }
        draw(log, "$root/scrollview", "tiled-sprite", 164.186f, 45.5f, 690f, 669f, "Logo_12-1")
        draw(
            log, "$root/scrollview/scrollBar", "sliced-sprite", 842.186f, 46.5f, 12f, 661f,
            "default_scrollbar_vertical_bg", opacity = 128f / 255f,
        )
        draw(
            log, "$root/scrollview/scrollBar/bar", "sliced-sprite", 843.186f, 377f, 10f, 30f,
            "default_scrollbar_vertical",
        )
        draw(log, "$root/scrollview/box2", "tiled-sprite", 164.186f, 45.5f, 690f, 669f, "box2")
        // 원본 스크롤뷰는 열네 줄을 모두 만들어 둔다. 화면 밖으로 밀린 줄은 비교기가
        // 양쪽에서 똑같이 걸러 내므로 여기서 줄 수를 미리 자르지 않는다.
        val item = "$root/scrollview/view/content/item0"
        layer.names.forEachIndexed { index, name ->
            val y = FIRST_ROW_Y - index * ROW_PITCH
            draw(log, item, "sprite", 167.836f, y, 672.5f, 70f, "bg1")
            draw(log, "$item/box3", "sliced-sprite", 167.836f, y, 672.5f, 70f, "box3")
            draw(log, "$item/label", "label", 231.038f, y + 8f, 341f, 54f, text = "(${index + 1}) $name")
            draw(log, "$item/toggle/Background", "sprite", 187.757f, y + 21f, 28f, 28f, "default_toggle_normal")
            if (layer.selected[index]) {
                draw(log, "$item/toggle1/Background", "sprite", 780.166f, y + 21f, 28f, 28f, "default_toggle_normal")
                draw(log, "$item/toggle1/checkmark", "sprite", 780.166f, y + 21f, 28f, 28f, "default_toggle_checkmark")
            }
            draw(log, "$item/button/Background", "sliced-sprite", 585.535f, y + 10f, 169f, 50f, "box3")
            draw(
                log, "$item/button/Background/Label", "label", 620.035f, y + 17.589f, 100f, 40f,
                text = "상세 정보",
            )
        }
        BUTTON_ORDER.forEach { index ->
            val button = requireNotNull(BUTTONS[index])
            draw(log, "$root/button$index/Background", "sliced-sprite", 893.829f, button.y, 400f, 70f, "box3")
            draw(
                log, "$root/button$index/Background/Label", "label",
                button.labelX, button.labelY, button.labelWidth, button.labelHeight, text = button.text,
            )
        }
    }

    /** 안내창: `CmdLayer`가 마지막으로 붙인 MsgBox를 그린다. flag 1은 단추 한 개다. */
    private fun appendPrompt(log: RenderEventLog, layer: CmdLayer) {
        val prompt = layer.modal?.takeIf { it.layer == "MsgBox" && it.txt != null } ?: return
        val body = requireNotNull(prompt.txt)
        draw(log, "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
        draw(log, "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
        draw(log, "Canvas/Layer/bg0/Logo_3-1", "sprite", 453.005f, 373.951f, 106f, 124f, "Logo_3-1")
        draw(log, "Canvas/Layer/bg0/label", "label", 573.686f, 335f, 463f, 190f, text = body)
        val single = prompt.flag == 1
        if (!single) {
            draw(log, "Canvas/Layer/bg0/btns/button1/Background", "sliced-sprite", 554.186f, 271.285f, 180f, 50f, "box3")
            draw(
                log, "Canvas/Layer/bg0/btns/button1/Background/Label", "label",
                557.336f, 279.085f, 168.1f, 40f, text = "비",
            )
        }
        val okX = if (single) 654.186f else 754.186f
        draw(log, "Canvas/Layer/bg0/btns/button0/Background", "sliced-sprite", okX, 271.285f, 180f, 50f, "box3")
        draw(
            log, "Canvas/Layer/bg0/btns/button0/Background/Label", "label",
            okX + 3.4f, 279.085f, 169.4f, 40f, text = "예",
        )
    }
}
