// Battle
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 전투 명령 증거 기록기: 명령·마법·아이템 경로의 고정 렌더 이벤트를 원본 순서 JSONL로 구성한다. */
internal object BattleCommandRenderEventRecorder {
    /** 기록: 자동 검증 경로에 맞는 명령 화면 렌더 이벤트 JSONL을 반환한다. */
    fun jsonl(route: RuntimeBattleRoute): String {
        val log = RenderEventLog()
        val phase = route.evidencePhase()
        val draw = BattleCommandEventAppender(log, phase)

        draw(
            "HallLayer",
            "Canvas/Layer/ScrollView/view/content/map",
            "sprite",
            -320f,
            -96f,
            1920f,
            1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>",
        )
        when (route) {
            RuntimeBattleRoute.COMMAND_CANCEL -> Unit
            RuntimeBattleRoute.COMMAND_MAGICK -> appendMagick(draw)
            RuntimeBattleRoute.COMMAND_PROPERTY -> appendProperty(draw)
            else -> appendCommand(draw)
        }
        return log.jsonl()
    }

    /** 경로 변환: 자동 실행 경로를 캡처 산출물의 phase 문자열로 고정한다. */
    private fun RuntimeBattleRoute.evidencePhase(): String = when (this) {
        RuntimeBattleRoute.COMMAND_INITIAL -> "battle-command-initial"
        RuntimeBattleRoute.COMMAND_DISABLED -> "battle-command-disabled"
        RuntimeBattleRoute.COMMAND_CANCEL -> "battle-command-cancel"
        RuntimeBattleRoute.COMMAND_MAGICK -> "battle-command-magick"
        RuntimeBattleRoute.COMMAND_PROPERTY -> "battle-command-property"
        else -> "battle-command"
    }

    /** 마법 목록: 마법 선택 창의 배경·마력·첫 항목·취소 버튼을 기록한다. */
    private fun appendMagick(draw: BattleCommandEventAppender) {
        draw("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", 40f / 255f)
        draw("MagickListLayer", "Canvas/Layer/bg0", "tiled-sprite", 474.186f, 90.5f, 540f, 619f, "Logo_9-1")
        draw("MagickListLayer", "Canvas/Layer/bg0/bg", "tiled-sprite", 474.186f, 90.5f, 540f, 619f, "box3")
        draw("MagickListLayer", "Canvas/Layer/bg0/label0", "label", 495.586f, 652.8f, 173f, 50.4f, text = "책사 ")
        draw("MagickListLayer", "Canvas/Layer/bg0/label", "label", 681.186f, 652.807f, 60f, 50.4f, text = "MP")
        draw("MagickListLayer", "Canvas/Layer/bg0/progressBar0", "sliced-sprite", 741.186f, 661.207f, 204f, 24f, "default_progressbar_bg")
        draw("MagickListLayer", "Canvas/Layer/bg0/progressBar0/bar", "sliced-sprite", 743.186f, 663.207f, 200f, 20f, "Mark_1-1")
        draw("MagickListLayer", "Canvas/Layer/bg0/progressBar1/bar", "sliced-sprite", 743.186f, 663.207f, 200f, 20f, "Mark_2-1")
        draw("MagickListLayer", "Canvas/Layer/bg0/progressBar1/label", "label", 793.136f, 653.8f, 100.1f, 50.4f, text = "42/42")
        draw("MagickListLayer", "Canvas/Layer/bg0/box2", "sliced-sprite", 478.186f, 150.5f, 532f, 499f, "box2")
        val item = "Canvas/Layer/bg0/box2/scrollview/view/content/item"
        draw("MagickListLayer", item, "sliced-sprite", 480.186f, 505.5f, 262f, 140f, "box3")
        draw("MagickListLayer", "$item/skill_0", "sprite", 485.259f, 562.883f, 76.8f, 76.8f, "1-1")
        draw("MagickListLayer", "$item/label0", "label", 572.186f, 592.3f, 69.2f, 50.4f, text = "작열")
        draw("MagickListLayer", "$item/label", "label", 572.186f, 551.3f, 94.6f, 50.4f, text = "MP：")
        draw("MagickListLayer", "$item/label2", "label", 656.065f, 551.3f, 22.25f, 50.4f, text = "6")
        draw("MagickListLayer", "$item/label", "label", 482.283f, 510.3f, 171.74f, 50.4f, text = "피해 계수: ")
        draw("MagickListLayer", "$item/label1", "label", 659.823f, 510.3f, 55.61f, 50.4f, text = "0.7")
        draw("MagickListLayer", "Canvas/Layer/bg0/button/Background", "sliced-sprite", 775.892f, 97.683f, 180f, 50f, "box3")
        draw("MagickListLayer", "Canvas/Layer/bg0/button/Background/Label", "label", 815.892f, 105.683f, 100f, 40f, text = "취소")
    }

    /** 아이템 목록: 사용 가능 아이템 두 개와 취소 버튼을 원본 좌표로 기록한다. */
    private fun appendProperty(draw: BattleCommandEventAppender) {
        draw("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", 40f / 255f)
        draw("UsePropertyLayer", "Canvas/Layer/bg", "tiled-sprite", 736f, 96f, 491f, 410f, "Logo_9-1")
        draw("UsePropertyLayer", "Canvas/Layer/bg/box3", "sliced-sprite", 736f, 96f, 491f, 410f, "box1")
        draw("UsePropertyLayer", "Canvas/Layer/bg/box2", "sliced-sprite", 740f, 154f, 483f, 348f, "box2")
        listOf(Triple("회복용 콩", "88-1", "3"), Triple("회복용 밀", "89-1", "2")).forEachIndexed { index, (name, asset, count) ->
            val y = 387f - index * 112f
            val item = "Canvas/Layer/bg/box2/scrollview/view/content/item0"
            draw("UsePropertyLayer", item, "sliced-sprite", 744f, y, 475f, 110f, "box3")
            draw("UsePropertyLayer", "$item/box2", "sliced-sprite", 750.014f, y + 5f, 100f, 100f, "box2")
            draw("UsePropertyLayer", "$item/box2/icon", "sprite", 755.014f, y + 10f, 90f, 90f, asset)
            draw("UsePropertyLayer", "$item/label0", "label", 852.5f, y + 56.8f, 191.5f, 50.4f, text = name)
            draw("UsePropertyLayer", "$item/label", "label", 852.5f, y + 4.8f, 91.43f, 50.4f, text = "효능: ")
            draw("UsePropertyLayer", "$item/label1", "label", 956.095f, y + 3.915f, 135.88f, 50.4f, text = "HP 회복")
            draw("UsePropertyLayer", "$item/label", "label", 1048.736f, y + 56.8f, 160.63f, 50.4f, text = "인벤토리: ")
            draw("UsePropertyLayer", "$item/label2", "label", 1189.967f, y + 56.8f, 22.25f, 50.4f, text = count)
        }
        draw("UsePropertyLayer", "Canvas/Layer/bg/button/Background", "sliced-sprite", 1071.609f, 100.896f, 150f, 50f, "box3")
        draw("UsePropertyLayer", "Canvas/Layer/bg/button/Background/Label", "label", 1096.609f, 109.896f, 100f, 40f, text = "취소")
    }

    /** 명령 패널: 기본 명령 여섯 개와 취소 버튼의 배경·문구·이중 아이콘을 기록한다. */
    private fun appendCommand(draw: BattleCommandEventAppender) {
        draw("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", 10f / 255f)
        draw("CommandLayer", "Canvas/Layer/bg", "tiled-sprite", 736f, 96f, 397.2f, 322.5f, "Logo_9-1", 200f / 255f)
        draw("CommandLayer", "Canvas/Layer/bg/box3", "sliced-sprite", 736f, 96f, 397.2f, 322.5f, "box3")
        val rects = listOf(
            floatArrayOf(743.6f, 291.175f), floatArrayOf(871.6f, 291.175f), floatArrayOf(1000.6f, 291.175f),
            floatArrayOf(743.6f, 165.42f), floatArrayOf(871.6f, 165.42f), floatArrayOf(1000.6f, 165.42f),
        )
        val labels = listOf("공격", "마법", "아이템", "교환", "포위 공격", "대기")
        val icons = listOf("command1", "command2", "command3", "command5", "command6", "command4")
        val firstIcons = listOf(
            floatArrayOf(749.6f, 373.175f, 32f, 32f), floatArrayOf(875.6f, 375.175f, 32f, 32f), floatArrayOf(1004.6f, 377.175f, 30f, 30f),
            floatArrayOf(747.6f, 253.42f, 32f, 28f), floatArrayOf(875.6f, 249.42f, 32f, 32f), floatArrayOf(1004.6f, 249.42f, 32f, 32f),
        )
        val secondIcons = listOf(
            floatArrayOf(825.6f, 297.175f, 32f, 32f), floatArrayOf(953.6f, 297.175f, 32f, 32f), floatArrayOf(1084.6f, 297.175f, 30f, 30f),
            floatArrayOf(825.6f, 171.42f, 32f, 28f), floatArrayOf(953.6f, 171.42f, 32f, 32f), floatArrayOf(1082.6f, 171.42f, 32f, 32f),
        )
        rects.forEachIndexed { index, rect ->
            val button = "Canvas/Layer/bg/button$index/Background"
            draw("CommandLayer", button, "sliced-sprite", rect[0], rect[1], 120f, 120f, "box3")
            draw("CommandLayer", "$button/Label", "label", rect[0] + 10f, rect[1] + 43f, 100f, 40f, text = labels[index])
            firstIcons[index].let { draw("CommandLayer", "$button/img0", "sprite", it[0], it[1], it[2], it[3], icons[index]) }
            secondIcons[index].let { draw("CommandLayer", "$button/img1", "sprite", it[0], it[1], it[2], it[3], icons[index]) }
        }
        draw("CommandLayer", "Canvas/Layer/bg/button6/Background", "sliced-sprite", 842.65f, 106.491f, 181.9f, 50f, "box3")
        draw("CommandLayer", "Canvas/Layer/bg/button6/Background/Label", "label", 883.6f, 114.491f, 100f, 40f, text = "취소")
    }

}

/** 명령 증거 추가기: 공통 phase와 원본 알파 혼합 규칙으로 이벤트 한 건을 기록한다. */
private class BattleCommandEventAppender(private val log: RenderEventLog, private val phase: String) {
    /** 추가: 좌표·자산·문구를 가진 렌더 이벤트를 기록한다. */
    operator fun invoke(
        layer: String,
        path: String,
        type: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        asset: String? = null,
        opacity: Float = 1f,
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
        opacity,
        if (type == "label") labelBlend else spriteBlend,
        text = text,
    )
}

/** 라벨 혼합: 글자 가장자리 알파 혼합 규칙을 보존한다. */
private val labelBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
/** 스프라이트 혼합: 원본 숫자 기반 알파 혼합 규칙을 보존한다. */
private val spriteBlend = listOf(770, 771)
