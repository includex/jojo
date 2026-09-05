package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog
import com.jojo.game.application.runtime.RuntimeScenarioOverlay

internal class ScenarioHallEquipEvidenceRecorder(
    private val input: ScenarioHallEquipEvidenceInput,
) {
    fun append(log: RenderEventLog, phase: String = "hall-equip-stable", layer: String = "EquipLayer") {
        val scale = .86f
        val sprites = listOf(770, 771)
        val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun event(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "", visible: Boolean = true, opacity: Float = 1f) =
            log.draw(phase, layer, path, type, x * scale, y * scale, w * scale, h * scale, asset, opacity, if (type == "label") labels else sprites, visible, text)
        fun label(path: String, value: String, x: Float, y: Float, w: Float, h: Float = 50.4f, visible: Boolean = true) =
            event(path, "label", x, y, w, h, text = value, visible = visible)
        fun button(path: String, value: String, x: Float, y: Float, w: Float, labelX: Float, labelY: Float, labelW: Float) {
            event("$path/Background", "sliced-sprite", x, y, w, 50f, "box3")
            label("$path/Background/Label", value, labelX, labelY, labelW, 40f)
        }
        fun width(value: String) = if (value == "조조" || value == "군웅") 69.2f else 103.8f
        val nameWidth = width(input.unitName)
        val postsWidth = width(input.postsName)
        event("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .392f)
        event("Canvas/Layer/bg1", "tiled-sprite", 138.186f, 33.5f, 1212f, 733f, "Logo_9-1")
        event("Canvas/Layer/bg1/box3", "sliced-sprite", 138.186f, 33.5f, 1212f, 733f, "box3")
        event("Canvas/Layer/bg1/title", "sprite", 138.186f, 716.5f, 1212f, 50f, "bg1")
        label("Canvas/Layer/bg1/title/label", "장비", 709.586f, 716.3f, 69.2f)
        button("Canvas/Layer/bg1/button5", "이전 무장", 979.686f, 44f, 177f, 988.186f, 52f, 160f)
        button("Canvas/Layer/bg1/button6", "다음 무장", 1156.686f, 44f, 177f, 1165.186f, 52f, 160f)
        button("Canvas/Layer/bg1/button7", "종료", 748.527f, 44f, 97f, 747.027f, 52f, 100f)
        button("Canvas/Layer/bg1/button8", "모두 해제", 573.685f, 44f, 173.2f, 580.285f, 52f, 160f)
        listOf("전부", "무기", "보구", "보조").forEachIndexed { index, value ->
            val x = 144.186f + index * 150f; button("Canvas/Layer/bg1/button${10 + index}", value, x, 659f, 150f, x - 5f, 667f, 160f)
        }
        button("Canvas/Layer/bg1/button14", "정보", 145.76f, 44f, 99.7f, 115.61f, 52f, 160f)
        event("Canvas/Layer/bg1/box1", "sliced-sprite", 144.486f, 99.95f, 703.4f, 560.1f, "box1")
        event("Canvas/Layer/bg1/box1/box2", "sliced-sprite", 144.486f, 99.95f, 703.4f, 560.1f, "box2")
        event("Canvas/Layer/bg1/vline", "sprite", 849.486f, 39.35f, 6f, 677.1f, "vline")
        event("Canvas/Layer/bg1/button0", "sliced-sprite", 924.186f, 658f, 360f, 56f, "box3")
        event("Canvas/Layer/bg1/button0/vline", "sprite", 1101.186f, 664.15f, 6f, 47.7f, "vline")
        label("Canvas/Layer/bg1/button0/label0", input.unitName, 1014.039f - nameWidth / 2f, 663.8f, nameWidth)
        label("Canvas/Layer/bg1/button0/label1", input.postsName, 1196.1f - postsWidth / 2f, 663.8f, postsWidth)
        val base = "Canvas/Layer/bg1/scrollview/view/content/box1"
        event("$base/face", "sprite", 894.812f, 413.337f, 192f, 240f, input.faceFrame.toString())
        if (input.variant == RuntimeScenarioOverlay.UNIT_LIST_CLOSE) event("$base/face/bg0", "sliced-sprite", 894.812f, 413.337f, 192f, 240f, "box2")
        else event("$base/face/bg0", "sliced-sprite", 870.812f, 415.337f, 240f, 236f, "box2")
        label("$base/label0", input.unitName, 1122.186f, 601.72f, nameWidth); label("$base/label1", input.postsName, 1122.186f, 551.72f, postsWidth)
        label("$base/label", "Exp", 1122.186f, 450.72f, 68.93f); event("$base/progressBar", "sliced-sprite", 1197.186f, 450.92f, 134f, 24f, "default_scrollbar_bg")
        event("$base/progressBar/bar", "sliced-sprite", 1199.186f, 452.92f, 0f, 20f, "Mark_6-1"); label("$base/progressBar/label", "0/100", 1214.136f, 452.094f, 100.1f)
        label("$base/label", "Lv", 1122.061f, 500.72f, 42.25f); label("$base/label2", input.level.toString(), 1185.061f, 500.778f, 22.25f)
        listOf("HP" to (878.401f to 359.72f), "MP" to (1126.186f to 359.72f), "공격력" to (886.286f to 300.72f), "정신력" to (1134.286f to 300.72f), "방어력" to (886.286f to 240.72f), "폭발력" to (1134.286f to 240.72f), "사기" to (883.586f to 181.72f), "이동력" to (1134.286f to 181.72f)).forEach { (name, position) ->
            label("$base/label", name, position.first, position.second, if (name == "HP") 55.57f else if (name == "MP") 60f else if (name == "사기") 69.2f else 103.8f)
        }
        val boxes = listOf(floatArrayOf(1008.186f,359.92f,1014.816f,359.72f,66.74f), floatArrayOf(1257.186f,359.92f,1274.941f,359.72f,44.49f), floatArrayOf(1008.186f,300.92f,1025.941f,300.72f,44.49f), floatArrayOf(1257.186f,300.92f,1274.941f,300.72f,44.49f), floatArrayOf(1008.186f,240.92f,1025.941f,240.72f,44.49f), floatArrayOf(1257.186f,240.92f,1274.941f,240.72f,44.49f), floatArrayOf(1008.186f,181.92f,1025.941f,181.72f,44.49f), floatArrayOf(1257.186f,181.92f,1286.061f,181.72f,22.25f))
        boxes.forEachIndexed { index, box -> val value = input.stats.getOrElse(index) { 0 }.toString(); val w = if (value == "115" || value == "112") 63.77f else box[4]; event("$base/bg$index", "sliced-sprite", box[0], box[1], 80f, 50f, "box2"); label("$base/bg$index/label", value, box[0] + (80f - w) / 2f, box[3], w) }
        input.slots.take(3).forEachIndexed { index, slot -> appendSlot(event = ::event, label = ::label, index = index, slot = slot) }
    }

    private fun appendSlot(event: (String, String, Float, Float, Float, Float, String?, String, Boolean, Float) -> Unit, label: (String, String, Float, Float, Float, Float, Boolean) -> Unit, index: Int, slot: ScenarioHallEquipEvidenceSlot) {
        val root = "Canvas/Layer/bg1/scrollview/view/content/bg$index"; val visible = index < 2; val detail = index == 0
        val rootY = 24.38f - index * 158f; val labelY = floatArrayOf(122.083f,-38.415f,-194.883f)[index]; val valueY = floatArrayOf(122.38f,-38.62f,-194.62f)[index]; val frameY = floatArrayOf(33.733f,-126.765f,-283.233f)[index]
        event(root, "sliced-sprite", 867.136f, rootY, 468.1f, 150f, "box1", "", visible, 1f)
        label("$root/label", listOf("무기:", "보구: ", "보조: ")[index], if (index == 0) 1047.737f else 1039.506f, labelY, if (index == 0) 80.31f else 91.43f, 50.4f, visible)
        label("$root/label0", slot.name, 1124.186f, valueY, 206f, 50f, visible); event("$root/box2", "sliced-sprite", 874.796f, frameY, 134.78f, 135.1f, "box2", "", visible, 1f)
        slot.icon?.let { event("$root/box2/icon", "sprite", 878.186f, 37.283f - index * 160.498f, 128f, 128f, "$it-1", "", visible, 1f) }
        if (visible) { label("$root/label_0", "Lv", 1018.186f, 76.083f - index * 160.498f, 42.25f, 50.4f, detail); label("$root/label1", slot.level.toString(), 1085.186f, 76.083f - index * 160.498f, 22.25f, 50.4f, detail); label("$root/label_1", "Exp", 1018.186f, 30.083f - index * 160.498f, 68.93f, 50.4f, detail); event("$root/progressBar", "sliced-sprite", 1104.186f, 29.283f - index * 160.498f, 204f, 24f, "default_scrollbar_bg", "", detail, 1f); event("$root/progressBar/bar", "sliced-sprite", 1106.186f, 31.283f - index * 160.498f, 0f, 20f, "Mark_6-1", "", detail, 1f); label("$root/progressBar/label", "${slot.experience}/100", 1156.136f, 30.457f - index * 160.498f, 100.1f, 50.4f, detail) }
    }
}
