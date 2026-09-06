// Verification
package com.jojo.game.verification.preparation

import com.jojo.game.presentation.shared.evidence.RenderEventLog
import com.jojo.game.presentation.battle.preparation.BattlePreparationViewState
import java.util.*

/** BattlePreparationTraceRecorder: 준비·정렬·전투 화면 픽스처의 검증 출력 경계이다. */
internal class BattlePreparationTraceRecorder {

    /** renderEvents: 전투 준비 상태를 렌더 이벤트 JSONL로 변환한다. */
    fun renderEvents(state: BattlePreparationViewState, route: String?): String = when {
        route == "battle-view-fixture" -> battleViewEvents()
        route?.removeSuffix("-fixture")?.startsWith("start-battle-sort-") == true ->
            battleSortEvents(route.removeSuffix("-fixture"))
        else -> RenderEventLog().also {
            appendStartBattleRenderEvents(it, route == "start-battle-unit-info-fixture")
        }.jsonl()
    }


    /** composition: 전투 준비 배치 상태를 JSON 구성으로 반환한다. */
    fun composition(state: BattlePreparationViewState): String {
        val roster = state.availableIds.mapIndexed { index, id ->
            val cx = (233.686f + index % 6 * 133f) * SCALE
            val cy = (682.202f - index / 6 * 144f) * SCALE
            "{\"id\":$id,\"selected\":${id in state.selectedIds}," +
                    "\"avatarRect\":${rect(cx - 49.536f, cy - 49.536f, 99.072f, 99.072f)}}"
        }.joinToString(",")
        val slots = (0 until state.maximum).joinToString(",") { index ->
            val centerX = (217.336f + index * 100f) * SCALE
            val kind = when {
                index < state.requiredSlotCount -> "required"
                index < state.minimum -> "minimum"
                else -> "open"
            }
            val frameHeight = if (index < state.requiredSlotCount) 51.6f else 55.04f
            "{\"index\":$index,\"kind\":\"$kind\",\"id\":${state.selectedIds.getOrNull(index) ?: -1}," +
                    "\"frameRect\":${rect(centerX - 43f, 194.915f - frameHeight / 2f, 86f, frameHeight)}}"
        }
        return "{\"state\":\"start-battle\",\"viewport\":[1280,688],\"backgroundId\":${state.backgroundId}," +
                "\"outerRect\":${rect(138.061f, 43f, 1003.878f, 602f)}," +
                "\"rosterClipRect\":${rect(143.78f, 323.79f, 688f, 312.18f)}," +
                "\"selectedPanelRect\":${rect(143.91f, 52.57f, 688f, 220.16f)}," +
                "\"infoPanelRect\":${rect(834.575f, 96.793f, 298.85f, 479.966f)}," +
                "\"infoTitleRect\":${rect(857.565f, 557.487f, 139.062f, 34.658f)}," +
                "\"selectedUnitId\":${state.cursorId ?: -1},\"faceRect\":${rect(848.51f, 372.86f, 140.35f, 175.44f)}," +
                "\"confirmRect\":${rect(954.76f, 49.88f, 86f, 43f)},\"cancelRect\":${
                    rect(
                        1049.36f,
                        49.88f,
                        86f,
                        43f
                    )
                }," +
                "\"roster\":[$roster],\"slots\":[$slots]}"
    }

    /** battleSortEvents: 부대 정렬 화면의 렌더 이벤트를 반환한다. */
    private fun battleSortEvents(route: String): String {
        val phase = "hall-$route-stable"
        val open = route.endsWith("open")
        val base = RenderEventLog().also {
            appendStartBattleRenderEvents(it, false, phase, 1f, spiritSorted = route.endsWith("select"))
        }.jsonl()
            .let { if (open) it.replace("\"layer\":\"StartBattleScreen\"", "\"layer\":\"BattleSortLayer\"") else it }
        if (!open) return base
        val extra = RenderEventLog(sequenceOffset = 97)
        extra.draw(
            phase,
            "HallLayer",
            "Canvas/Layer/Panel_cancel",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "default_sprite_splash",
            60f / 255f
        )
        extra.draw(phase, "HallLayer", "Canvas/Layer/menu", "sliced-sprite", 765.186f, 37.5f, 200f, 283.5f, "bg1")
        extra.draw(phase, "HallLayer", "Canvas/Layer/menu/bg1", "tiled-sprite", 765.186f, 37.5f, 200f, 283.5f, "box3")
        val labels = listOf("부대 속성", "공격력", "정신력", "방어력", "레벨")
        val labelX = listOf(782.448f, 799.448f, 799.448f, 799.448f, 814.448f)
        val labelW = listOf(164f, 130f, 130f, 130f, 100f)
        labels.forEachIndexed { index, value ->
            val y = 263.001f - index * 54f
            extra.draw(
                phase,
                "HallLayer",
                "Canvas/Layer/menu/button1_$index/Background",
                "sliced-sprite",
                773.998f,
                y,
                180.9f,
                50f,
                "box3"
            )
            extra.draw(
                phase, "HallLayer", "Canvas/Layer/menu/button1_$index/Background/Label", "label",
                labelX[index], y + 8f, labelW[index], 40f, opacity = 1f,
                blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), text = value
            )
        }
        return base + extra.jsonl()
    }

    /** battleViewEvents: 전투 보기 화면의 렌더 이벤트를 반환한다. */
    private fun battleViewEvents(): String {
        val log = RenderEventLog()
        val phase = "hall-battle-view-stable"
        /** draw: 검증 렌더 이벤트를 구성하고 반환한다. */
        fun draw(
            layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, opacity: Float = 1f, text: String = "",
            blend: Any = listOf(770, 771), visible: Boolean = true
        ) =
            log.draw(phase, layer, path, type, x, y, w, h, asset, opacity, blend, visible, text)
        draw(
            "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        draw(
            "BattleViewLayer",
            "Canvas/Layer/bg",
            "sprite",
            1008.372f,
            320f,
            480f,
            480f,
            "default_sprite_splash",
            .667f
        )
        draw(
            "BattleViewLayer", "Canvas/Layer/bg/map/view/content/map1", "sprite", 1008.372f, 320f, 480f, 480f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#HM_1-1"
        )
        repeat(4) { index ->
            val x = 1104.372f + index * 24f
            draw(
                "BattleViewLayer", "Canvas/Layer/bg/map/view/content/map1/box6", "sliced-sprite", x, 680f, 24f, 24f,
                "Mark_47-1", if (index == 1) 1f else .502f
            )
            draw(
                "BattleViewLayer",
                "Canvas/Layer/bg/map/view/content/map1/box6/box3",
                "sprite",
                x,
                680f,
                24f,
                24f,
                "box3"
            )
            draw(
                "BattleViewLayer", "Canvas/Layer/bg/map/view/content/map1/box6/label", "label", x + 6.272f, 681.13f,
                10.01f, 22.68f, text = (index + 1).toString(), blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
            )
        }
        draw("BattleViewLayer", "Canvas/Layer/bg/box3", "sliced-sprite", 1008.372f, 320f, 480f, 480f, "box5")
        return log.jsonl()
    }

    /** rect: 좌표와 크기를 직사각형 JSON으로 변환한다. */
    private fun rect(x: Float, y: Float, width: Float, height: Float) =
        "[${format(x)},${format(y)},${format(width)},${format(height)}]"

    /** format: 검증 출력 문자열의 형식을 구성한다. */
    private fun format(value: Float) = "%.3f".format(Locale.US, value)

    private companion object {
        /** SCALE: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
        const val SCALE = .86f
    }
}
