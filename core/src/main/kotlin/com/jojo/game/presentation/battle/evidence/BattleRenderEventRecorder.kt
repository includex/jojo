// Battle
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.presentation.battle.overlay.WinConditionsLayer
import com.jojo.game.presentation.battle.render.WinConditionRenderContract
import com.jojo.game.presentation.battle.render.ItemUpgradeRenderContract
import com.jojo.game.presentation.battle.render.BattleRewardRenderContract
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 전투 화면 공통 외형을 기록하기 위한 값 전용 모델입니다. */
internal data class BattleRenderEventView(
    val phase: String,
    val route: BattleRenderEventRoute,
    val mapBottom: Float,
    val units: List<BattleRenderEventUnitView>,
    val dialogueMarker: BattleRenderEventMarkerView? = null,
    val dialogue: BattleRenderEventDialogueView? = null,
    val winConditions: BattleRenderEventWinConditionsView? = null,
    val itemUpgrade: BattleRenderEventItemUpgradeView? = null,
    val reward: BattleRenderEventRewardView? = null,
)

/** BattleRenderEventRoute: 전투 화면 흐름에서 현재 처리 종류를 구분한다. */
internal enum class BattleRenderEventRoute { INIT, DIALOGUE_BLEND, WIN_COMPACT, WIN_FULL, ITEM_UPGRADE, REWARD }

/** BattleRenderEventUnitView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class BattleRenderEventUnitView(
    val x: Float,
    val y: Float,
    val size: Float,
    val spriteAsset: String?,
    val healthBar: BattleRenderEventHealthBarView?,
)

/** BattleRenderEventHealthBarView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class BattleRenderEventHealthBarView(val x: Float, val y: Float, val width: Float, val asset: String)
/** BattleRenderEventMarkerView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class BattleRenderEventMarkerView(val x: Float, val y: Float)
/** BattleRenderEventDialogueView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class BattleRenderEventDialogueView(val headAsset: String?, val text: String, val speakerName: String)
/** BattleRenderEventWinConditionsView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class BattleRenderEventWinConditionsView(
    val first: String,
    val second: String,
    val childLabels: List<String> = emptyList(),
)

/** BattleRenderEventItemUpgradeView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class BattleRenderEventItemUpgradeView(
    val iconAsset: String,
    val itemName: String,
    val newLevel: Int,
    val ownerName: String,
    val attributeName: String,
    val oldValue: Int,
    val newValue: Int,
)

/** BattleRenderEventRewardView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class BattleRenderEventRewardView(
    val phase: BattleRenderEventRewardPhase,
    val money: Int = 0,
    val flag: Int = 0,
    val items: List<BattleRenderEventRewardItemView> = emptyList(),
)

/** BattleRenderEventRewardPhase: 전투 화면 흐름에서 현재 처리 종류를 구분한다. */
internal enum class BattleRenderEventRewardPhase { MONEY, ITEMS, NONE }
/** BattleRenderEventRewardItemView: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
internal data class BattleRenderEventRewardItemView(val iconAsset: String, val name: String)

/** 불변 화면 모델을 원본 그리기 순서의 JSONL로 기록합니다. */
internal object BattleRenderEventRecorder {
    /** 렌더 이벤트 모델을 JSONL 문자열로 직렬화합니다. */
    fun jsonl(view: BattleRenderEventView): String = RenderEventLog().also { log ->
        BattleBattlefieldRenderEvents.append(log, view)
        when (view.route) {
            BattleRenderEventRoute.INIT -> BattleInitRenderEvents.append(log, view.phase)
            BattleRenderEventRoute.DIALOGUE_BLEND -> BattleDialogueBlendRenderEvents.append(log, view)
            BattleRenderEventRoute.WIN_COMPACT -> BattleWinConditionRenderEvents.appendCompact(log, view.phase, requireNotNull(view.winConditions).first)
            BattleRenderEventRoute.WIN_FULL -> BattleWinConditionRenderEvents.appendFull(log, view.phase, requireNotNull(view.winConditions))
            BattleRenderEventRoute.ITEM_UPGRADE -> {
                BattleChromeRenderEvents.append(log, view.phase, "ItemUpgradeLayer")
                BattleItemUpgradeRenderEvents.append(log, view.phase, requireNotNull(view.itemUpgrade))
                BattleTitleRenderEvents.append(log, view.phase, "ItemUpgradeLayer")
            }
            BattleRenderEventRoute.REWARD -> {
                BattleChromeRenderEvents.append(log, view.phase, "BattleScreen")
                BattleTitleRenderEvents.appendOverlay(log, view.phase)
                BattleRewardRenderEvents.append(log, view.phase, view.reward)
                BattleTitleRenderEvents.append(log, view.phase, "BattleScreen")
            }
        }
    }.jsonl()
}
/**
 * `BattleBattlefieldRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

private object BattleBattlefieldRenderEvents {
    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, view: BattleRenderEventView) {
        draw(log, view.phase, "HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, view.mapBottom, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>")
        view.units.forEach { unit ->
            unit.spriteAsset?.let { draw(log, view.phase, "HallLayer", "Canvas/Layer/ScrollView/view/content/map/unit/mask/node", "sprite", unit.x, unit.y, unit.size, unit.size, it) }
            unit.healthBar?.let { bar -> draw(log, view.phase, "HallLayer", "Canvas/Layer/ScrollView/view/content/map/unit/info/bar2/sprite", "sliced-sprite", bar.x, bar.y, bar.width, 6f, bar.asset) }
        }
        view.dialogueMarker?.let { draw(log, view.phase, "HallLayer", "Canvas/Layer/ScrollView/view/content/map/New Node", "sprite", it.x, it.y, 48f, 48f, "Mark_10-1") }
        draw(log, view.phase, "HallLayer", "Canvas/Layer/menu_button/Background", "sprite", 1353.9535f, 8f, 60f, 60f, "menu")
    }
}
/**
 * `BattleInitRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

private object BattleInitRenderEvents {
    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, phase: String) {
        val layer = "BattleInitLayer"
        draw(log, phase, layer, "Canvas/Layer/bg/button/Background", "sliced-sprite", .843f, .731f, 68f, 68f, "bg1")
        draw(log, phase, layer, "Canvas/Layer/bg/button/Background/tool11", "sprite", .043f, -.069f, 69.6f, 69.6f, "tool10")
        draw(log, phase, layer, "Canvas/Layer/bg/btn/Background", "sliced-sprite", 1418.372f, 730f, 70f, 70f, "bg1")
        draw(log, phase, layer, "Canvas/Layer/bg/btn/Background/tool11", "sprite", 1418.572f, 730.2f, 69.6f, 69.6f, "tool11")
        draw(log, phase, layer, "Canvas/Layer/bg", "sprite", 0f, 0f, 1488.372f, 800f, "assets/resources/native/59/5961a224-35cd-4838-b67a-a072b0b31ca4.14b27.jpg#Logo_5-1")
        // 원본 `BattleInitLayer` 프리팹: fontSize 140 굵게, `label0`(검정 그림자) 위에 `label1`(흰색)이
        // 겹친다. 그리기 쪽 `BattleRewardOverlayRenderer.drawSection`도 같은 두 색으로 두 번 그린다.
        draw(log, phase, layer, "Canvas/Layer/bg/label0", "label", 431.986f, 301.8f, 644.4f, 176.4f, text = "영천의 전투", blend = labels, color = "#000000")
        draw(log, phase, layer, "Canvas/Layer/bg/label1", "label", 421.986f, 311.8f, 644.4f, 176.4f, text = "영천의 전투", blend = labels, color = "#ffffff")
    }
}
/**
 * `BattleDialogueBlendRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

private object BattleDialogueBlendRenderEvents {
    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, view: BattleRenderEventView) {
        val dialogue = requireNotNull(view.dialogue)
        dialogue.headAsset?.let { draw(log, view.phase, "SayLayer", "Canvas/Layer/bg0/face", "sprite", 1064.618f, 330f, 192f, 240f, it) }
        draw(log, view.phase, "SayLayer", "Canvas/Layer/bg0/bg2", "sprite", 245.65f, 332f, 796f, 212f, "U_select_11-1")
        draw(log, view.phase, "SayLayer", "Canvas/Layer/bg0/bg2/richtext", "rich-text", 272.705f, 431.814f, 728f, 52.92f, text = dialogue.text, blend = labels)
        draw(log, view.phase, "SayLayer", "Canvas/Layer/bg0/bg2/richtext/RICHTEXT_CHILD", "label", 272.705f, 431.814f, 72.28f, 52.92f, text = dialogue.text, blend = labels)
        draw(log, view.phase, "SayLayer", "Canvas/Layer/bg0/label", "label", 304.804f, 485.639f, 97.42f, 49.36f, text = dialogue.speakerName, blend = labels)
    }
}
/**
 * `BattleWinConditionRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

private object BattleWinConditionRenderEvents {
    /**
     * `appendCompact`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun appendCompact(log: RenderEventLog, phase: String, text: String) {
        draw(log, phase, "WinConBoxLayer", "Canvas/Layer/bg0", "tiled-sprite", 249.686f, 65f, 989f, 670f, "Logo_9-1")
        draw(log, phase, "WinConBoxLayer", "Canvas/Layer/bg0/box2", "tiled-sprite", 249.686f, 65f, 989f, 670f, "box3")
        draw(log, phase, "WinConBoxLayer", "Canvas/Layer/bg0/Logo_3-1", "sprite", 280.574f, 588.927f, 106f, 124f, "Logo_3-1")
        draw(log, phase, "WinConBoxLayer", "Canvas/Layer/bg0/scrollview/box3", "sliced-sprite", 406.686f, 170.5f, 803f, 543f, "box2")
        // 이 줄의 y는 원본 ScrollView가 배치를 마친 뒤의 값(522.206)이다. 붙자마자 재면
        // 한 프레임 이른 520.887이 잡혀, 예전에는 그 값에 맞춰져 있었다.
        draw(
            log, phase, "WinConBoxLayer", "Canvas/Layer/bg0/scrollview/view/content/item", "label",
            409.359f, 522.206f, 803f, 191.36f, text = text, blend = labels,
            // 그리기 쪽 `drawWinConditionBox`가 글꼴에 넣는 것과 같은 계약이다.
            color = WinConditionRenderContract.BODY_HEX,
        )
        draw(log, phase, "WinConBoxLayer", "Canvas/Layer/bg0/button/Background", "sliced-sprite", 957.134f, 88.204f, 256.7f, 60f, "box3")
        draw(
            log, phase, "WinConBoxLayer", "Canvas/Layer/bg0/button/Background/Label", "label",
            985.869f, 93.461f, 199.23f, 54.4f, text = "짐이 알겠다.", blend = labels,
            color = WinConditionRenderContract.BUTTON_HEX,
        )
    }

    /**
     * `appendFull`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun appendFull(log: RenderEventLog, phase: String, conditions: BattleRenderEventWinConditionsView) {
        val texts = conditions.childLabels
        val widths = listOf(367.43f, 537.49f, 537.49f, 531.39f)
        draw(log, phase, "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", 80f / 255f)
        // 색은 서식 문자열 안에 들어 있다. 그림자 막과 위 막이 서로 다른 태그를 쓰므로
        // 막마다 자기 문자열에서 뽑는다 — 어느 쪽도 따로 적어 둘 값이 아니다.
        rich(log, phase, "richtext1", 39.467f, 260.08f, conditions.first, texts, widths, 620.08f)
        rich(log, phase, "richtext2", 27.323f, 267.913f, conditions.second, texts, widths, 627.913f)
    }

    /**
     * `rich`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun rich(log: RenderEventLog, phase: String, name: String, x: Float, y: Float, content: String, texts: List<String>, widths: List<Float>, labelY: Float) {
        // `cc.RichText` 노드 자체에는 색이 없어 엔진 기본 흰색이다.
        draw(
            log, phase, "HallLayer", "Canvas/Layer/$name", "rich-text", x, y, 537.49f, 511.2f,
            text = content, blend = labels, color = RICH_TEXT_WHITE,
        )
        val colors = WinConditionsLayer.childColors(content)
        texts.forEachIndexed { index, text ->
            draw(
                log, phase, "HallLayer", "Canvas/Layer/$name/RICHTEXT_CHILD", "label",
                x, labelY - index * 120f, widths[index], 151.2f, text = text, blend = labels,
                color = colors.getOrElse(index) { RICH_TEXT_WHITE },
            )
        }
    }

    /** `cc.RichText` 노드 색: 프리팹이 값을 두지 않아 엔진 기본 흰색이다. */
    private const val RICH_TEXT_WHITE = "#ffffff"
}
/**
 * `BattleChromeRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

private object BattleChromeRenderEvents {
    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, phase: String, layer: String) {
        draw(log, phase, layer, "Canvas/Layer/bg/button/Background", "sliced-sprite", .843f, .731f, 68f, 68f, "bg1")
        draw(log, phase, layer, "Canvas/Layer/bg/button/Background/tool11", "sprite", .043f, -.069f, 69.6f, 69.6f, "tool10")
        draw(log, phase, layer, "Canvas/Layer/bg/btn/Background", "sliced-sprite", 1418.3721f, 730f, 70f, 70f, "bg1")
        draw(log, phase, layer, "Canvas/Layer/bg/btn/Background/tool11", "sprite", 1418.5721f, 730.2f, 69.6f, 69.6f, "tool11")
    }
}
/**
 * `BattleTitleRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

private object BattleTitleRenderEvents {
    /**
     * `asset` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val asset = "assets/resources/native/59/5961a224-35cd-4838-b67a-a072b0b31ca4.14b27.jpg#Logo_5-1"
    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, phase: String, layer: String) {
        draw(log, phase, layer, "Canvas/Layer/bg", "sprite", 0f, 0f, 1488.3721f, 800f, asset)
        draw(log, phase, layer, "Canvas/Layer/bg/label0", "label", 431.986f, 301.8f, 644.4f, 176.4f, text = "영천의 전투", blend = labels, color = BattleRewardRenderContract.SECTION_SHADOW_HEX)
        draw(log, phase, layer, "Canvas/Layer/bg/label1", "label", 421.986f, 311.8f, 644.4f, 176.4f, text = "영천의 전투", blend = labels, color = BattleRewardRenderContract.WHITE_HEX)
    }
    /**
     * `appendOverlay`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun appendOverlay(log: RenderEventLog, phase: String) {
        draw(log, phase, "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.3721f, 800f, "default_sprite_splash", 50f / 255f)
    }
}
/**
 * `BattleItemUpgradeRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

private object BattleItemUpgradeRenderEvents {
    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, phase: String, view: BattleRenderEventItemUpgradeView) {
        /**
         * `event`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun event(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") =
            draw(log, phase, "ItemUpgradeLayer", path, type, x, y, w, h, asset, text = text,
                blend = if (type == "label") labels else sprites,
                color = when {
                    path == "Canvas/Layer/bg/label2" -> ItemUpgradeRenderContract.OWNER_HEX
                    type == "label" -> ItemUpgradeRenderContract.LABEL_HEX
                    else -> null
                })
        event("Canvas/Layer/bg", "tiled-sprite", 544.186f, 270.5f, 400f, 259f, "Logo_9-1")
        event("Canvas/Layer/bg/box3", "sliced-sprite", 544.186f, 270.5f, 400f, 259f, "box3")
        event("Canvas/Layer/bg/box2", "sliced-sprite", 551.222f, 451.34f, 70f, 70f, "box2")
        event("Canvas/Layer/bg/box2/item", "sprite", 554.222f, 454.34f, 64f, 64f, view.iconAsset)
        event("Canvas/Layer/bg/label0", "label", 628.186f, 470.8f, 69.2f, 50.4f, text = view.itemName)
        event("Canvas/Layer/bg/label1", "label", 908.186f, 470.8f, 22.25f, 50.4f, text = view.newLevel.toString())
        event("Canvas/Layer/bg/label", "label", 861.186f, 470.8f, 42.25f, 50.4f, text = "Lv")
        event("Canvas/Layer/bg/label2", "label", 624.386f, 415.9f, 189.3f, 50.4f, text = view.ownerName)
        event("Canvas/Layer/bg/label", "label", 815.347f, 415.934f, 69.2f, 50.4f, text = "장비")
        event("Canvas/Layer/bg/scrollview", "sliced-sprite", 553.136f, 281.5f, 379.5f, 130.4f, "box2")
        event("Canvas/Layer/bg/scrollview/view/content/label", "label", 554.836f, 361.5f, 379.5f, 50.4f, text = "${view.attributeName} ${view.oldValue} -> ${view.newValue}")
    }
}
/**
 * `BattleRewardRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

private object BattleRewardRenderEvents {
    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, phase: String, reward: BattleRenderEventRewardView?) = when (reward?.phase ?: BattleRenderEventRewardPhase.NONE) {
        BattleRenderEventRewardPhase.MONEY -> {
            val value = requireNotNull(reward)
            /**
             * `label`: 타입의 핵심 동작을 수행한다.
             * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
             */

            fun label(path: String, x: Float, y: Float, w: Float, h: Float, text: String, color: String) = draw(log, phase, "BattleScreen", path, "label", x, y, w, h, text = text, blend = labels, color = color)
            label("Canvas/Layer/bg0/label", 527.747f, 464.417f, 448.54f, 151.2f, "전투 종료", BattleRewardRenderContract.END_SHADOW_HEX)
            label("Canvas/Layer/bg0/label", 519.916f, 476.394f, 448.54f, 151.2f, "전투 종료", BattleRewardRenderContract.WHITE_HEX)
            label("Canvas/Layer/bg0/label", 282.777f, 248.492f, 311.4f, 151.2f, "보상금", BattleRewardRenderContract.MONEY_SHADOW_HEX)
            label("Canvas/Layer/bg0/label", 274.533f, 254.4f, 311.4f, 151.2f, "보상금", BattleRewardRenderContract.MONEY_TEXT_HEX)
            val money = value.money.toString()
            label("Canvas/Layer/bg0/label02", 967.617f, 247.807f, 200.21f, 151.2f, money, BattleRewardRenderContract.MONEY_SHADOW_HEX)
            label("Canvas/Layer/bg0/label01", 958.035f, 254.4f, 200.21f, 151.2f, money, BattleRewardRenderContract.MONEY_TEXT_HEX)
            val stars = (0 until 3).joinToString("  ") { if (value.flag and (1 shl it) != 0) "★" else "☆" }
            label("Canvas/Layer/bg0/label12", 531.389f, 52.817f, 444.76f, 151.2f, stars, BattleRewardRenderContract.MONEY_SHADOW_HEX)
            label("Canvas/Layer/bg0/label11", 521.806f, 56.113f, 444.76f, 151.2f, stars, BattleRewardRenderContract.MONEY_TEXT_HEX)
        }
        BattleRenderEventRewardPhase.ITEMS -> {
            draw(log, phase, "BattleScreen", "Canvas/Layer/bg1/label", "label", 596.73f, 574.944f, 311.4f, 151.2f, text = "전리품", blend = labels, color = BattleRewardRenderContract.LOOT_SHADOW_HEX)
            draw(log, phase, "BattleScreen", "Canvas/Layer/bg1/label", "label", 588.486f, 587.942f, 311.4f, 151.2f, text = "전리품", blend = labels, color = BattleRewardRenderContract.WHITE_HEX)
            requireNotNull(reward).items.take(3).forEachIndexed { index, item ->
                val y = 433.5f - index * 157f; val root = "Canvas/Layer/bg1/item$index"
                draw(log, phase, "BattleScreen", root, "tiled-sprite", 499.686f, y, 489f, 101f, "Mark_47-1")
                draw(log, phase, "BattleScreen", "$root/box3", "sliced-sprite", 499.686f, y, 489f, 101f, "box3")
                draw(log, phase, "BattleScreen", "$root/icon", "sprite", 534.974f, y + 18.5f, 64f, 64f, item.iconAsset)
                draw(log, phase, "BattleScreen", "$root/label", "label", 648.999f, y + 22.78f, 164.46f, 55.44f, text = item.name, blend = labels, color = BattleRewardRenderContract.ITEM_NAME_HEX)
            }
        }
        BattleRenderEventRewardPhase.NONE -> Unit
    }
}

/**
 * `sprites` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
 */

private val sprites = listOf(770, 771)

/** 흐림막 색: 원본 `Panel_cancel` 노드 색은 검정이고 투명도만 다르다. */
private const val SCRIM_BLACK = "#000000"

/** 스프라이트 색조: 전장 노드에는 `_color`가 없어 엔진 기본 흰색이고 포트도 흰색으로 그린다. */
private const val SPRITE_WHITE = "#ffffff"
/**
 * `labels` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
 */

private val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

/**
 * `draw`: 화면 표시 상태를 렌더링한다.
 * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

private fun draw(log: RenderEventLog, phase: String, layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, opacity: Float = 1f, text: String = "", blend: Any = sprites, color: String? = null) {
    if (opacity <= 0f || x + w <= 0f || x >= 1488.3721f || y + h <= 0f || y >= 800f) return
    // 스프라이트 색조: 전장 지도와 유닛을 그리는 `BattleMapRenderer`·`BattleMapObjectRenderer`·
    // `BattleGridMapSurfaceRenderer`는 모두 `batch.color`를 흰색으로 두고 그린다(투명도만 다른
    // 자리는 `opacity` 항목이 따로 나른다). 흐림막만 `shapes.color`로 검정을 깐다. 글자색은
    // 화면마다 달라 호출자가 넘길 때만 적는다.
    val resolved = color ?: when {
        type == "label" -> null
        path == "Canvas/Layer/Panel_cancel" -> SCRIM_BLACK
        else -> SPRITE_WHITE
    }
    log.draw(phase, layer, path, type, x, y, w, h, asset, opacity, blend, true, text, resolved)
}
