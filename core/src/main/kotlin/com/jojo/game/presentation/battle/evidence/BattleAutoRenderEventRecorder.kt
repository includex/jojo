// Battle
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.overlay.AutoBattleFlow
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 자동 전투 증거 입력: 캡처 경로와 확정된 자동 전투 표시 상태를 함께 전달한다. */
internal data class BattleAutoRenderEventInput(
    val route: RuntimeBattleRoute,
    val view: AutoBattleFlow.View,
)

/** 자동 전투 증거 기록기: 확인 창과 위임 배너의 고정 렌더 이벤트를 원본 순서 JSONL로 기록한다. */
internal object BattleAutoRenderEventRecorder {
    /** 기록: 자동 전투 경로와 불변 표시 상태를 캡처용 렌더 이벤트 JSONL로 변환한다. */
    fun jsonl(input: BattleAutoRenderEventInput): String {
        val log = RenderEventLog()
        val draw = BattleAutoEventAppender(log, input.route.evidencePhase())
        when (input.view.overlay) {
            AutoBattleFlow.Overlay.PROMPT -> appendPrompt(draw, input.view.checked)
            AutoBattleFlow.Overlay.TUOGUAN -> appendTuoGuan(draw)
            AutoBattleFlow.Overlay.NONE -> Unit
        }
        return log.jsonl()
    }

    /** 경로 변환: 자동 전투 fixture 경로를 산출물 phase 문자열로 고정한다. */
    private fun RuntimeBattleRoute.evidencePhase(): String = when (this) {
        RuntimeBattleRoute.AUTO_PROMPT_OFF -> "battle-auto-battle-prompt-off"
        RuntimeBattleRoute.AUTO_PROMPT_ON -> "battle-auto-battle-prompt-on"
        RuntimeBattleRoute.AUTO_ACTIVE -> "battle-auto-battle-active"
        else -> "battle-auto-battle"
    }

    /** 확인 창: 전장 배경, 위임 토글, 확인·취소 버튼을 기존 화면 그리기 순서로 기록한다. */
    private fun appendPrompt(draw: BattleAutoEventAppender, checked: Boolean) {
        draw(
            "HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>",
        )
        draw("MsgBox4", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
        draw("MsgBox4", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
        draw("MsgBox4", "Canvas/Layer/bg0/Logo_3-1", "sprite", 453.005f, 373.951f, 106f, 124f, "Logo_3-1")
        draw(
            "MsgBox4", "Canvas/Layer/bg0/label", "label", 573.686f, 335f, 463f, 190f,
            text = "모든 부대의 명령을 종료하시겠습니까?", color = MESSAGE_BROWN, outline = MESSAGE_OUTLINE_GOLD,
        )
        draw("MsgBox4", "Canvas/Layer/bg0/btns/tuoguan/Background", "sprite", 518.416f, 281.197f, 28f, 28f, "default_toggle_normal")
        if (checked) {
            draw(
                "MsgBox4", "Canvas/Layer/bg0/btns/tuoguan/checkmark", "sprite", 518.416f, 281.197f, 28f, 28f,
                "assets/resources/native/73/73a0903d-d80e-4e3c-aa67-f999543c08f5.7661e.png#default_toggle_checkmark",
            )
        }
        draw(
            "MsgBox4", "Canvas/Layer/bg0/btns/tuoguan/label", "label", 567.257f, 267.997f, 73.2f, 54.4f,
            text = "위임", color = TOGGLE_BLUE, outline = TOGGLE_OUTLINE_SKY,
        )
        draw("MsgBox4", "Canvas/Layer/bg0/btns/button1/Background", "sliced-sprite", 674.536f, 270.197f, 150f, 50f, "box3")
        draw(
            "MsgBox4", "Canvas/Layer/bg0/btns/button1/Background/Label", "label", 699.536f, 278.042f, 100f, 40f,
            text = "비", color = NO_RED, outline = NO_OUTLINE_PINK,
        )
        draw("MsgBox4", "Canvas/Layer/bg0/btns/button0/Background", "sliced-sprite", 844.536f, 270.197f, 150f, 50f, "box3")
        draw(
            "MsgBox4", "Canvas/Layer/bg0/btns/button0/Background/Label", "label", 869.536f, 278.042f, 100f, 40f,
            text = "예", color = YES_GREEN, outline = YES_OUTLINE_MINT,
        )
    }

    /**
     * 본문 문구 색: 원본 `MsgBox4` 프리팹 `Layer/bg0/label` 노드의 `_color` 4278215059 = (147,97,0).
     * 포트는 같은 값을 `BattleScreen.kt`의 `msgBoxMessageFont` `fillColor`에 구워 넣는다.
     * (프리팹 `assets/resources/import/9b/9bdd4d86-fa7e-40b4-9b40-889f799473d3.e1ded.json`)
     */
    internal const val MESSAGE_BROWN = "#936100"

    /** 위임 문구 색: 같은 프리팹 `btns/tuoguan/label` `_color` 4294903040 = (0,5,255). */
    internal const val TOGGLE_BLUE = "#0005ff"

    /** 「비」 문구 색: 같은 프리팹 `btns/button1/Background/Label` `_color` 4278190332 = (252,0,0). */
    internal const val NO_RED = "#fc0000"

    /** 「예」 문구 색: 같은 프리팹 `btns/button0/Background/Label` `_color` 4278218242 = (2,110,0). */
    internal const val YES_GREEN = "#026e00"

    /**
     * 테두리 색은 노드 색이 아니라 라벨마다 붙은 `cc.LabelOutline` 부품의 `_color`다(모두 `_width` 2).
     * 같은 프리팹에서 읽었고, 포트는 같은 값을 `BattleScreen.kt`의 `msgBox*Font` `borderColor`에 굽는다.
     * `Layer/bg0/label` LabelOutline `_color` 4285457151 = (255,226,110).
     */
    internal const val MESSAGE_OUTLINE_GOLD = "#ffe26e"

    /** 위임 문구 테두리: `btns/tuoguan/label` LabelOutline `_color` 4294962803 = (115,238,255). */
    internal const val TOGGLE_OUTLINE_SKY = "#73eeff"

    /** 「비」 문구 테두리: `btns/button1/Background/Label` LabelOutline `_color` 4292138239 = (255,212,212). */
    internal const val NO_OUTLINE_PINK = "#ffd4d4"

    /** 「예」 문구 테두리: `btns/button0/Background/Label` LabelOutline `_color` 4288279420 = (124,243,153). */
    internal const val YES_OUTLINE_MINT = "#7cf399"

    /**
     * 기본 노드 색: 위 프리팹과 `TuoGuanLayer`(4d576540-8ba3-4316-81bd-31f59cefc477)의
     * 스프라이트 노드에는 `_color`가 없어 엔진 기본 흰색이고, 포트도 `batch.color = Color.WHITE`로
     * 그대로 그린다(`BattleAutoOverlayRenderer.draw`). 투명도만 다른 노드는 `opacity`가 따로 나른다.
     */
    internal const val NODE_WHITE = "#ffffff"

    /** 위임 배너: 자동 진행 중인 전장의 배경과 상단 상태 배너를 기록한다. */
    private fun appendTuoGuan(draw: BattleAutoEventAppender) {
        draw(
            "HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>",
        )
        draw(
            "HallLayer", "Canvas/Layer/img2", "sprite", 0f, 0f, 1488.372f, 264f,
            "assets/resources/native/21/2110e4bf-3344-42aa-b4ff-8183c4cb93f6.52abe.png#img2",
        )
        draw("HallLayer", "Canvas/Layer/img2/img3", "sprite", 613.686f, 25.894f, 261f, 83f, "img3")
    }
}

/** 자동 전투 증거 추가기: phase와 라벨·스프라이트 혼합 규칙을 고정해 한 행씩 기록한다. */
private class BattleAutoEventAppender(private val log: RenderEventLog, private val phase: String) {
    /** 추가: 계층·경로·좌표·자산·문구를 가진 렌더 이벤트 한 건을 기록한다. */
    operator fun invoke(
        layer: String,
        path: String,
        type: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        asset: String? = null,
        text: String = "",
        // 색은 노드 색만 적는다. 원본 하네스는 `node.color`의 RGB 세 채널만 내보내므로
        // 투명도를 섞으면 무조건 어긋난다. 투명도는 `opacity`가 따로 나른다.
        color: String = BattleAutoRenderEventRecorder.NODE_WHITE,
        // 테두리 색은 `cc.LabelOutline`이 있는 라벨에만 적는다. 없는 그리기는 null로 남고
        // 비교기가 한쪽만 적힌 항목을 건너뛰므로 스프라이트에 가짜 값이 생기지 않는다.
        outline: String? = null,
    ) = log.draw(
        phase,
        layer,
        path,
        type,
        x,
        y,
        width,
        height,
        asset,
        blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
        text = text,
        color = color,
        outline = outline,
    )
}
