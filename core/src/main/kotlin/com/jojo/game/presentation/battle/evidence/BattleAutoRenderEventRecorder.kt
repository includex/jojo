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
        draw("MsgBox4", "Canvas/Layer/bg0/label", "label", 573.686f, 335f, 463f, 190f, text = "모든 부대의 명령을 종료하시겠습니까?")
        draw("MsgBox4", "Canvas/Layer/bg0/btns/tuoguan/Background", "sprite", 518.416f, 281.197f, 28f, 28f, "default_toggle_normal")
        if (checked) {
            draw(
                "MsgBox4", "Canvas/Layer/bg0/btns/tuoguan/checkmark", "sprite", 518.416f, 281.197f, 28f, 28f,
                "assets/resources/native/73/73a0903d-d80e-4e3c-aa67-f999543c08f5.7661e.png#default_toggle_checkmark",
            )
        }
        draw("MsgBox4", "Canvas/Layer/bg0/btns/tuoguan/label", "label", 567.257f, 267.997f, 73.2f, 54.4f, text = "위임")
        draw("MsgBox4", "Canvas/Layer/bg0/btns/button1/Background", "sliced-sprite", 674.536f, 270.197f, 150f, 50f, "box3")
        draw("MsgBox4", "Canvas/Layer/bg0/btns/button1/Background/Label", "label", 699.536f, 278.042f, 100f, 40f, text = "비")
        draw("MsgBox4", "Canvas/Layer/bg0/btns/button0/Background", "sliced-sprite", 844.536f, 270.197f, 150f, 50f, "box3")
        draw("MsgBox4", "Canvas/Layer/bg0/btns/button0/Background/Label", "label", 869.536f, 278.042f, 100f, 40f, text = "예")
    }

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
    )
}
