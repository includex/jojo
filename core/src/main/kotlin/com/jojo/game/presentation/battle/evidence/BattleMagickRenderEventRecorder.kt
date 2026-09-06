// Battle
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 마법 화면 증거 입력: 경로와 목록·상세 패널을 JSONL로 기록하기 위한 불변 표시 정보이다. */
internal data class BattleMagickRenderEventView(
    val route: RuntimeBattleRoute?,
    val list: BattleMagickListView?,
    val detail: BattleMagickDetailView?,
)

/** 마법 목록: 선택 가능한 마법 행을 화면 표시 순서로 보관한다. */
internal data class BattleMagickListView(val rows: List<BattleMagickRowView>)

/** 마법 행: 목록 카드에서 이름·MP 소모·위력·아이콘을 표시하는 값이다. */
internal data class BattleMagickRowView(val name: String, val cost: Int, val power: Int?, val icon: Int)

/** 마법 상세: 상세 패널의 범위·설명까지 표시하는 선택 마법 정보이다. */
internal data class BattleMagickDetailView(
    val name: String,
    val cost: Int,
    val power: Int?,
    val icon: Int,
    val hit: Int,
    val effect: Int,
    val intro: String,
)

/** 마법 증거 기록기: 마법 목록과 상세 패널의 고정 렌더 이벤트를 원본 순서 JSONL로 구성한다. */
internal object BattleMagickRenderEventRecorder {
    /** 기록: 목록이 없으면 빈 결과를, 선택 마법이 있으면 상세 패널까지 이어서 기록한다. */
    fun jsonl(view: BattleMagickRenderEventView): String {
        val list = view.list ?: return RenderEventLog().jsonl()
        val append = BattleMagickEventAppender(RenderEventLog(), view.route.evidencePhase())
        appendMapAndListChrome(append)
        appendRows(append, list.rows)
        append("MagickListLayer", "Canvas/Layer/bg0/button/Background", "sliced-sprite", 775.892f, 97.683f, 180f, 50f, "box3")
        append("MagickListLayer", "Canvas/Layer/bg0/button/Background/Label", "label", 815.892f, 105.683f, 100f, 40f, text = "취소")
        view.detail?.let { appendDetail(append, it) }
        return append.jsonl()
    }

    /** 경로 변환: 상세 캡처와 목록 캡처의 산출물 phase 이름을 고정한다. */
    private fun RuntimeBattleRoute?.evidencePhase(): String =
        if (this == RuntimeBattleRoute.MAGICK_DETAIL) "battle-magick-list-detail" else "battle-magick-list-list"

    /** 기본 패널: 전장·흐림막·MP 영역·마법 목록 상자를 원본 좌표로 기록한다. */
    private fun appendMapAndListChrome(append: BattleMagickEventAppender) {
        append("HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f, mapAsset)
        append("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", .157f)
        append("MagickListLayer", "Canvas/Layer/bg0", "tiled-sprite", 474.186f, 90.5f, 540f, 619f, "Logo_9-1")
        append("MagickListLayer", "Canvas/Layer/bg0/bg", "tiled-sprite", 474.186f, 90.5f, 540f, 619f, "box3")
        append("MagickListLayer", "Canvas/Layer/bg0/label0", "label", 495.586f, 652.8f, 173f, text = "허자장")
        append("MagickListLayer", "Canvas/Layer/bg0/label", "label", 681.186f, 652.807f, 60f, text = "MP")
        append("MagickListLayer", "Canvas/Layer/bg0/progressBar0", "sliced-sprite", 741.186f, 661.207f, 204f, 24f, "default_progressbar_bg")
        append("MagickListLayer", "Canvas/Layer/bg0/progressBar0/bar", "sliced-sprite", 743.186f, 663.207f, 82.759f, 20f, "Mark_1-1")
        append("MagickListLayer", "Canvas/Layer/bg0/progressBar1/bar", "sliced-sprite", 743.186f, 663.207f, 82.759f, 20f, "Mark_2-1")
        append("MagickListLayer", "Canvas/Layer/bg0/progressBar1/label", "label", 793.136f, 653.8f, 100.1f, text = "24/58")
        append("MagickListLayer", "Canvas/Layer/bg0/box2", "sliced-sprite", 478.186f, 150.5f, 532f, 499f, "box2")
    }

    /** 마법 행: 최대 열 개 카드의 아이콘·비용·위력 문구를 행과 열 순서대로 기록한다. */
    private fun appendRows(append: BattleMagickEventAppender, rows: List<BattleMagickRowView>) {
        rows.take(10).forEachIndexed { index, magic ->
            val x = 480.186f + 264f * (index % 2)
            val y = 505.5f - 142f * (index / 2)
            val root = "Canvas/Layer/bg0/box2/scrollview/view/content/item"
            append("MagickListLayer", root, "sliced-sprite", x, y, 262f, 140f, "box3")
            append("MagickListLayer", "$root/skill_0", "sprite", x + 5.073f, y + 57.383f, 76.8f, 76.8f, "Game/Magic/${magic.icon + 1}-1")
            append("MagickListLayer", "$root/label0", "label", x + 92f, y + 86.8f, rowWidths[index], text = magic.name)
            append("MagickListLayer", "$root/label", "label", x + 92f, y + 45.8f, 94.6f, text = "MP：")
            append("MagickListLayer", "$root/label2", "label", x + 175.879f, y + 45.8f, if (magic.cost < 10) 22.25f else 44.49f, text = magic.cost.toString())
            if (index < 8) {
                append("MagickListLayer", "$root/label", "label", x + 2.097f, y + 4.8f, 171.74f, text = "피해 계수: ")
                append("MagickListLayer", "$root/label1", "label", x + 179.637f, y + 4.8f, 77.85f, text = magic.power?.let { (it / 100f).toString() } ?: "없음")
            }
        }
    }

    /** 상세 패널: 선택 마법의 설명·명중 범위·영향 범위·확인 버튼을 목록 뒤에 기록한다. */
    private fun appendDetail(append: BattleMagickEventAppender, magic: BattleMagickDetailView) {
        append("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", .392f)
        append("MagickListLayer", "Canvas/Layer/bg1", "tiled-sprite", 452.686f, 130f, 583f, 540f, "Logo_9-1")
        append("MagickListLayer", "Canvas/Layer/bg1/box2", "sliced-sprite", 452.686f, 130f, 583f, 540f, "box3")
        append("MagickListLayer", "Canvas/Layer/bg1/label", "label", 577.509f, 604.008f, 218.71f, text = magic.name)
        append("MagickListLayer", "Canvas/Layer/bg1/skill_0", "sprite", 478.186f, 562f, 80f, 80f, "Game/Magic/${magic.icon + 1}-1")
        append("MagickListLayer", "Canvas/Layer/bg1/bg0", "sliced-sprite", 465.636f, 434f, 340.3f, 100f, "box1")
        append("MagickListLayer", "Canvas/Layer/bg1/bg0/label", "label", 476.336f, 479.826f, 80.31f, text = "위력:")
        append("MagickListLayer", "Canvas/Layer/bg1/bg0/label0", "label", 566.719f, 480.13f, 80.06f, text = "${magic.power ?: 0}%")
        append("MagickListLayer", "Canvas/Layer/bg1/bg0/label", "label", 470.776f, 436.826f, 151.43f, text = "MP 소모:")
        append("MagickListLayer", "Canvas/Layer/bg1/bg0/label1", "label", 627.053f, 436.675f, 22.25f, text = magic.cost.toString())
        append("MagickListLayer", "Canvas/Layer/bg1/bg1", "sliced-sprite", 465.636f, 147f, 340.3f, 274f, "box2")
        append("MagickListLayer", "Canvas/Layer/bg1/bg1/scrollview/view/content/label", "label", 470.786f, 187.114f, 330f, 231.44f, text = magic.intro)
        append("MagickListLayer", "Canvas/Layer/bg1/bg2", "sliced-sprite", 814.213f, 436.061f, 200f, 200f, "box1")
        append("MagickListLayer", "Canvas/Layer/bg1/bg2/bg", "sliced-sprite", 830.713f, 614.117f, 167f, 40f, "bg1")
        append("MagickListLayer", "Canvas/Layer/bg1/bg2/bg/label", "label", 839.654f, 611.005f, 149.51f, text = "가능 범위")
        append("MagickListLayer", "Canvas/Layer/bg1/bg2/img", "sprite", 834.213f, 450.755f, 160f, 160f, "Game/Hitarea/${magic.hit + 1}-1")
        append("MagickListLayer", "Canvas/Layer/bg1/bg3", "sliced-sprite", 814.213f, 204.673f, 200f, 200f, "box1")
        append("MagickListLayer", "Canvas/Layer/bg1/bg3/bg", "sliced-sprite", 831.713f, 384.673f, 165f, 40f, "bg1")
        append("MagickListLayer", "Canvas/Layer/bg1/bg3/bg/label", "label", 839.654f, 381.561f, 149.51f, text = "영향 범위")
        append("MagickListLayer", "Canvas/Layer/bg1/bg3/img", "sprite", 834.213f, 219.367f, 160f, 160f, "Game/Effarea/${magic.effect + 1}-1")
        append("MagickListLayer", "Canvas/Layer/bg1/button/Background", "sliced-sprite", 874.764f, 144.022f, 147.6f, 50f, "box3")
        append("MagickListLayer", "Canvas/Layer/bg1/button/Background/Label", "label", 898.564f, 152.022f, 100f, 40f, text = "확인")
    }

    /**
     * `rowWidths` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val rowWidths = listOf(218.71f, 138.4f, 103.8f, 69.2f, 69.2f, 149.51f, 69.2f, 103.8f, 69.2f, 149.51f)
    /**
     * `mapAsset` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val mapAsset = "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>"
}

/** 마법 증거 추가기: 라벨과 스프라이트의 원본 알파 혼합 규칙을 이벤트마다 적용한다. */
private class BattleMagickEventAppender(private val log: RenderEventLog, private val phase: String) {
    /** 추가: 경로·레이어·좌표·자산·문구를 가진 렌더 이벤트 한 건을 기록한다. */
    operator fun invoke(layer: String, path: String, type: String, x: Float, y: Float, width: Float, height: Float = 50.4f, asset: String? = null, opacity: Float = 1f, text: String = "") {
        log.draw(phase, layer, path, type, x, y, width, height, asset, opacity, if (type == "label") labels else sprites, true, text)
    }

    /** JSONL: 누적한 이벤트를 검증 캡처 파일 형식으로 내보낸다. */
    fun jsonl(): String = log.jsonl()
}

/** 스프라이트 혼합: 원본 숫자 기반 알파 혼합 규칙을 보존한다. */
private val sprites = listOf(770, 771)
/** 라벨 혼합: 글자 가장자리 알파 혼합 규칙을 보존한다. */
private val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
