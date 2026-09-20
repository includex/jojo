// Battle
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.render.UsePropertyDetailRenderContract
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 아이템 사용 증거 입력: 아이템 목록과 상세 화면을 JSONL 렌더 이벤트로 옮길 불변 표시 값이다. */
internal data class BattleUsePropertyRenderEventView(
    val route: RuntimeBattleRoute?,
    val rows: List<BattleUsePropertyRowView>?,
    val detail: BattleUsePropertyDetailView?,
    val profile: BattleUsePropertyProfileView?,
    val postNames: List<String>,
)

/** 아이템 행: 사용 목록에서 아이콘·효과·재고를 표시하는 한 항목이다. */
internal data class BattleUsePropertyRowView(val name: String, val typeName: String, val count: Int, val icon: Int)

/** 아이템 상세: 선택한 아이템의 목록 표시 정보이다. */
internal data class BattleUsePropertyDetailView(val name: String, val typeName: String, val icon: Int)

/**
 * 아이템 설명: 상세 창에서 가격과 설명을 함께 표시하는 장비 정보이다.
 *
 * [category]는 원본 `ITEM_TYPE`과 같은 번호로, "장착 가능한 부대" 표의 글자색을 가른다
 * (`UsePropertyDetailRenderContract.postsCanEquip`).
 */
internal data class BattleUsePropertyProfileView(val purchasePrice: Int, val intro: String, val category: Int)

/** 아이템 사용 증거 기록기: 목록·상세 경로의 고정 렌더 이벤트를 원본 순서 JSONL로 구성한다. */
internal object BattleUsePropertyRenderEventRecorder {
    /** 기록: 경로가 없으면 빈 결과를, 목록이 없으면 전장만, 상세가 완전하면 상세 패널까지 기록한다. */
    fun jsonl(view: BattleUsePropertyRenderEventView): String {
        val route = view.route ?: return RenderEventLog().jsonl()
        val phase = route.evidencePhase()
        val log = RenderEventLog()
        val append = BattleUsePropertyEventAppender(log, phase)
        append("HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>")
        val rows = view.rows ?: return log.jsonl()
        appendList(append, rows)
        val detail = view.detail ?: return log.jsonl()
        val profile = view.profile ?: return log.jsonl()
        appendDetail(append, detail, profile, view.postNames)
        return log.jsonl()
    }

    /** 경로 변환: 자동 검증 경로를 캡처 산출물 phase 문자열로 고정한다. */
    private fun RuntimeBattleRoute.evidencePhase(): String = when (this) {
        RuntimeBattleRoute.USE_PROPERTY_DETAIL -> "battle-use-property-detail"
        RuntimeBattleRoute.USE_PROPERTY_SELECT -> "battle-use-property-select"
        RuntimeBattleRoute.USE_PROPERTY_CANCEL -> "battle-use-property-cancel"
        else -> "battle-use-property-list"
    }

    /** 목록: 흐림막, 각 소지품 행, 취소 버튼을 원본 좌표 순서로 기록한다. */
    private fun appendList(append: BattleUsePropertyEventAppender, rows: List<BattleUsePropertyRowView>) {
        append("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", 40f / 255f)
        append("UsePropertyLayer", "Canvas/Layer/bg", "tiled-sprite", 795.536f, 390f, 491f, 410f, "Logo_9-1")
        append("UsePropertyLayer", "Canvas/Layer/bg/box3", "sliced-sprite", 795.536f, 390f, 491f, 410f, "box1")
        append("UsePropertyLayer", "Canvas/Layer/bg/box2", "sliced-sprite", 799.536f, 448f, 483f, 348f, "box2")
        rows.forEachIndexed { index, item ->
            val y = 681f - index * 112f
            val path = "Canvas/Layer/bg/box2/scrollview/view/content/item0"
            append("UsePropertyLayer", path, "sliced-sprite", 803.536f, y, 475f, 110f, "box3")
            append("UsePropertyLayer", "$path/box2", "sliced-sprite", 809.55f, y + 5f, 100f, 100f, "box2")
            append("UsePropertyLayer", "$path/box2/icon", "sprite", 814.55f, y + 10f, 90f, 90f, "Game/Item2/${item.icon}-1")
            append("UsePropertyLayer", "$path/label0", "label", 912.036f, y + 56.8f, 191.5f, text = item.name)
            append("UsePropertyLayer", "$path/label", "label", 912.036f, y + 4.8f, 91.43f, text = "효능: ")
            append("UsePropertyLayer", "$path/label1", "label", 1015.631f, y + 3.915f, 135.88f, text = item.typeName)
            append("UsePropertyLayer", "$path/label", "label", 1108.272f, y + 56.8f, 160.63f, text = "인벤토리: ")
            append("UsePropertyLayer", "$path/label2", "label", 1249.503f, y + 56.8f, 22.25f, text = item.count.toString())
        }
        append("UsePropertyLayer", "Canvas/Layer/bg/button/Background", "sliced-sprite", 1131.145f, 394.896f, 150f, 50f, "box3")
        append("UsePropertyLayer", "Canvas/Layer/bg/button/Background/Label", "label", 1156.145f, 403.896f, 100f, 40f, text = "취소")
    }

    /** 상세: 선택 아이템의 설명·효과·장착 가능 직위와 확인 버튼을 기록한다. */
    private fun appendDetail(append: BattleUsePropertyEventAppender, detail: BattleUsePropertyDetailView, profile: BattleUsePropertyProfileView, postNames: List<String>) {
        append("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", .392f)
        append("UsePropertyLayer", "Canvas/Layer/bg1", "tiled-sprite", 253.186f, 80f, 982f, 640f, "Logo_9-1")
        append("UsePropertyLayer", "Canvas/Layer/bg1/box2", "sliced-sprite", 253.186f, 80f, 982f, 640f, "box3")
        append("UsePropertyLayer", "Canvas/Layer/bg1/label0", "label", 420.186f, 658.8f, 203.1f, text = detail.name)
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg4", "sliced-sprite", 265.778f, 564.802f, 144f, 144f, "box2")
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg4/icon", "sprite", 273.778f, 572.802f, 128f, 128f, "Game/Item2/${detail.icon}-1")
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg0", "sliced-sprite", 420.536f, 498.55f, 343.5f, 100.9f, "box1")
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg0/label", "label", 432.137f, 548.543f, 80.31f, text = "속성:")
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg0/label0", "label", 522.525f, 548.543f, 103.8f, text = "아이템")
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg0/label", "label", 432.137f, 503.543f, 80.31f, text = "가격:")
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg0/label1", "label", 522.525f, 503.543f, 66.74f, text = profile.purchasePrice.toString())
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg1", "sliced-sprite", 261.686f, 92.5f, 501f, 377f, "box1")
        appendHeadband(append, UsePropertyDetailRenderContract.EFFECT_HEADBAND)
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg1/bg1/label", "label", 477.586f, 442.5f, 69.2f, text = "효과")
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg1/scrollview/view/content/label", "label", 265.686f, 389.966f, 493f, 55.44f, text = detail.typeName)
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg2", "sliced-sprite", 770.186f, 157.5f, 448f, 247f, "box2")
        appendHeadband(append, UsePropertyDetailRenderContract.INTRO_HEADBAND)
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg2/bg1/label", "label", 953.586f, 378.8f, 69.2f, text = "설명")
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg2/scrollview/view/content/label", "label", 774.186f, 191.26f, 440f, 187.44f, text = profile.intro)
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg3", "sliced-sprite", 770.186f, 427f, 448f, 260f, "box1")
        appendHeadband(append, UsePropertyDetailRenderContract.POSTS_HEADBAND)
        append("UsePropertyLayer", "Canvas/Layer/bg1/bg3/bg1/label", "label", 804.516f, 661.573f, 379.34f, text = "장착 가능한 부대입니다.")
        appendPostsTable(append, postNames, profile.category)
        append("UsePropertyLayer", "Canvas/Layer/bg1/button1/Background", "sliced-sprite", 1065.827f, 97.824f, 150f, 50f, "box3")
        append("UsePropertyLayer", "Canvas/Layer/bg1/button1/Background/Label", "label", 1090.827f, 104.824f, 100f, 40f, text = "확인")
    }

    /** 머리띠: 문구 뒤 `bg1` 스프라이트 한 장을 계약이 가진 사각형 그대로 기록한다. */
    private fun appendHeadband(append: BattleUsePropertyEventAppender, headband: UsePropertyDetailRenderContract.Headband) =
        append(
            "UsePropertyLayer", headband.nodePath, "sprite", headband.x, headband.y, headband.width, headband.height,
            UsePropertyDetailRenderContract.HEADBAND_ASSET
        )

    /**
     * 장착 가능한 부대 표: 행 배경과 직위명 칸을 계약의 기하로 기록한다.
     *
     * 행 수는 `posts` 표 길이에서 온다(13×3=39). 글자색은 원본과 같이 두 가지뿐이며
     * `UsePropertyDetailRenderContract.postsCanEquip`이 가른다.
     */
    private fun appendPostsTable(append: BattleUsePropertyEventAppender, postNames: List<String>, category: Int) {
        val contract = UsePropertyDetailRenderContract
        repeat(contract.rowCount(postNames.size)) { row ->
            val y = contract.rowY(row)
            append(
                "UsePropertyLayer", contract.ROW_NODE_PATH, "sliced-sprite", contract.ROW_X, y,
                contract.ROW_WIDTH, contract.ROW_HEIGHT, contract.rowAsset(row)
            )
            repeat(contract.COLUMNS) { column ->
                val index = contract.postsIndex(row, column)
                val value = postNames.getOrElse(index) { "" }
                append(
                    "UsePropertyLayer", "${contract.ROW_NODE_PATH}/label$column", "label",
                    contract.labelX(column, value), contract.labelY(row), contract.measuredWidth(value),
                    contract.LABEL_HEIGHT, text = value, color = contract.labelColor(category, index)
                )
            }
        }
    }
}

/** 아이템 사용 증거 추가기: 종류에 따라 원본 스프라이트·라벨 혼합 규칙을 적용한다. */
private class BattleUsePropertyEventAppender(private val log: RenderEventLog, private val phase: String) {
    /** 추가: 좌표·자산·문구를 가진 렌더 이벤트 한 건을 기록한다. */
    operator fun invoke(layer: String, path: String, type: String, x: Float, y: Float, width: Float, height: Float = 50.4f, asset: String? = null, opacity: Float = 1f, text: String = "", color: String? = null) =
        // 색조: 그리기 쪽 `drawUsePropertyLayer`·`drawUsePropertyDetail`은 `batch.begin()` 직후
        // `batch.color = Color.WHITE`와 `font.color = Color.BLACK`을 세우고 끝까지 바꾸지 않는다
        // (직위명 39칸만 호출자가 색을 직접 넘긴다). 흐림막만 `shapes.color`로 검정을 깐다.
        log.draw(
            phase, layer, path, type, x, y, width, height, asset, opacity,
            if (type == "label") labels else sprites, true, text,
            color
                ?: if (path == "Canvas/Layer/Panel_cancel") SCRIM_BLACK
                else if (type == "label") LABEL_BLACK
                else SPRITE_WHITE,
        )
}

/** 흐림막 색: 원본 `Panel_cancel` 노드 색은 검정이고 투명도만 다르다. */
private const val SCRIM_BLACK = "#000000"

/** 기본 글자색: 그리기 함수가 `font.color = Color.BLACK`을 세운다. */
private const val LABEL_BLACK = "#000000"

/** 스프라이트 색조: 이 창의 스프라이트 노드에는 `_color`가 없어 엔진 기본 흰색이다. */
private const val SPRITE_WHITE = "#ffffff"

/** 스프라이트 혼합: 원본 숫자 기반 알파 혼합 규칙을 보존한다. */
private val sprites = listOf(770, 771)
/** 라벨 혼합: 글자 가장자리 알파 혼합 규칙을 보존한다. */
private val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
