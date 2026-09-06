// Battle
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 기기 목록 증거 입력: 화면 계층에서 확정한 기기 확률을 렌더 이벤트 기록기에 전달한다. */
internal data class BattleJiqiRenderEventView(
    val rates: List<Int>,
)

/** 기기 목록 증거 기록기: 고정된 배경·라벨·확률 순서로 캡처용 JSONL을 구성한다. */
internal object BattleJiqiRenderEventRecorder {
    /** 기록: 기기 목록 화면의 원본 렌더 이벤트 순서를 보존한 JSONL을 반환한다. */
    fun jsonl(view: BattleJiqiRenderEventView): String {
        require(view.rates.size == 8) { "기기 목록 확률은 8개여야 한다." }
        val log = RenderEventLog()
        val sprites = listOf(770, 771)
        val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", opacity: Float = 1f, owner: String = "JiQiLayer"
        ) = log.draw(
            "battle-jiqi-stable", owner, path, type, x, y, w, h, asset, opacity,
            if (type == "label") labels else sprites, true, text
        )

        draw(
            "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>", owner = "HallLayer"
        )
        draw(
            "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            "default_sprite_splash", opacity = .157f, owner = "HallLayer"
        )
        draw("Canvas/Layer/bg", "tiled-sprite", 405.686f, 234.5f, 677f, 331f, "Logo_9-1")
        draw("Canvas/Layer/bg/box3", "sliced-sprite", 405.686f, 234.5f, 677f, 331f, "box3")
        listOf(
            Triple("명중률: ", floatArrayOf(479.171f, 487.8f, 126.03f), "label"),
            Triple("방어율:", floatArrayOf(485.057f, 424.839f, 114.91f), "label"),
            Triple("쌍타율:", floatArrayOf(484.731f, 360.8f, 114.91f), "label"),
            Triple("이중 타격률:", floatArrayOf(424.571f, 297.8f, 195.23f), "label"),
        ).forEach { (text, p, node) -> draw("Canvas/Layer/bg/$node", "label", p[0], p[1], p[2], 50.4f, text = text) }
        listOf(487.8f, 424.8f, 360.8f, 297.8f).forEachIndexed { index, y ->
            draw("Canvas/Layer/bg/label$index", "label", 625.186f, y, 44.49f, 50.4f, text = view.rates[index].toString())
        }
        listOf(7 to 306.8f, 6 to 366.8f, 5 to 427.8f).forEach { (index, y) ->
            draw("Canvas/Layer/bg/label$index", "label", 978.186f, y, 44.49f, 50.4f, text = view.rates[index].toString())
        }
        draw("Canvas/Layer/bg/label", "label", 753.016f, 487.8f, 206.34f, 50.4f, text = "마법 명중률: ")
        draw("Canvas/Layer/bg/label4", "label", 978.186f, 487.8f, 44.49f, 50.4f, text = view.rates[4].toString())
        draw("Canvas/Layer/bg/label", "label", 738.416f, 306.8f, 275.54f, 50.4f, text = "피격 시 치명타율:")
        draw("Canvas/Layer/bg/label", "label", 821.431f, 370.8f, 149.51f, 50.4f, text = "치명타율:")
        draw("Canvas/Layer/bg/label", "label", 753.016f, 433.8f, 206.34f, 50.4f, text = "마법 방어율: ")
        return log.jsonl()
    }
}
