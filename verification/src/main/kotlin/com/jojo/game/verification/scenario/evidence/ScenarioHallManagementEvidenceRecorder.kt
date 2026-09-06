// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** ScenarioHallManagementEvidenceRecorder: 매입·매각 거점 관리 화면의 목록과 버튼 렌더링 이벤트를 기록한다. */
internal class ScenarioHallManagementEvidenceRecorder(
    /** input: 검증 입력 정보를 담는다. */
    private val input: ScenarioHallManagementEvidenceInput,
) {
    /** append: 검증 이벤트와 산출물을 기록한다. */
    fun append(log: RenderEventLog) {
        val kind = input.kind
        val layer = if (kind == ScenarioHallManagementEvidenceKind.BUY) "BuyLayer" else "SellLayer"
        if (kind == ScenarioHallManagementEvidenceKind.BUY || kind == ScenarioHallManagementEvidenceKind.SELL) {
            /** source: 원본 검증 데이터를 조회한다. */
            fun source(
                path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                asset: String? = null, text: String = "", label: Boolean = false
            ) =
                log.draw(
                    "management", layer, "Canvas/Layer/$path", type, x, y, w, h, asset,
                    blend = if (label) listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771), text = text
                )


            /** label: 텍스트 라벨 이벤트를 렌더 로그에 추가한다. */
            fun label(path: String, text: String, x: Float, y: Float, w: Float, h: Float = 43.344f) =
                source(path, "label", x, y, w, h, text = text, label = true)
            if (kind == ScenarioHallManagementEvidenceKind.SELL) {
                source("bg1", "tiled-sprite", 267.62f, 65.36f, 744.76f, 557.28f, "Logo_9-1")
                source("bg1/box3", "sliced-sprite", 267.62f, 65.36f, 744.76f, 557.28f, "box3")
                source("bg1/title", "sprite", 267.62f, 579.64f, 744.76f, 43f, "bg1")
                label("bg1/title/label", "판매하기", 605.60f, 579.468f, 119.024f)
                label("bg1/label", "현금", 283.444f, 80.266f, 59.512f)
                source("bg1/box2", "sliced-sprite", 350.309f, 80.438f, 142.502f, 43f, "box2")
                label("bg1/box2/label", input.money.toString(), 467.929f, 80.266f, 19.135f)
                source("bg1/button7/Background", "sliced-sprite", 870.48f, 76.138f, 129f, 51.6f, "box3")
                label("bg1/button7/Background/Label", "종료", 891.98f, 87.318f, 86f, 34.4f)
                source("bg1/box1", "sliced-sprite", 271.06f, 68.8f, 737.88f, 493.64f, "box1")
                source("bg1/box1/bg1", "sliced-sprite", 281.568f, 538.325f, 140.352f, 37.324f, "bg1")
                label("bg1/box1/bg1/label", "창고 목록", 287.454f, 535.315f, 128.579f)
                source("bg1/box1/button0/Background", "sliced-sprite", 522.18f, 76.138f, 172f, 51.6f, "box3")
                label("bg1/box1/button0/Background/Label", "무기점", 566.129f, 87.318f, 111.8f, 34.4f)
                source("bg1/box1/button0/Background/command1", "sprite", 535.16f, 89.947f, 27.52f, 27.52f, "command1")
                source("bg1/box1/button1/Background", "sliced-sprite", 694.18f, 76.138f, 172f, 51.6f, "box3")
                label("bg1/box1/button1/Background/Label", "상점", 738.129f, 87.318f, 111.8f, 34.4f)
                source("bg1/box1/button1/Background/command1", "sprite", 708.02f, 90.807f, 25.8f, 25.8f, "command3")
                source("bg1/box1/box0", "sliced-sprite", 274.50f, 137.6f, 731f, 399.04f, "box2")
                return
            }

            source("bg1", "tiled-sprite", 168.290f, 28.81f, 943.42f, 630.38f, "Logo_9-1")
            source("bg1/box3", "sliced-sprite", 168.290f, 28.81f, 943.42f, 630.38f, "box3")
            source("bg1/title", "sprite", 168.290f, 616.19f, 943.42f, 43f, "bg1")
            label("bg1/title/label", "매입", 610.244f, 616.018f, 59.512f)
            label("bg1/label", "현금", 188.844f, 39.388f, 59.512f)
            source("bg1/box2", "sliced-sprite", 258.676f, 39.56f, 141.556f, 43f, "box2")
            label("bg1/box2/label", input.money.toString(), 378.107f, 39.388f, 19.135f)
            /** sourceButton: 원본 버튼 입력을 변환한다. */
            fun sourceButton(
                path: String,
                text: String,
                x: Float,
                y: Float,
                w: Float,
                tx: Float,
                tw: Float,
                labelYOffset: Float = 8.6f,
            ) {
                source("$path/Background", "sliced-sprite", x, y, w, 48.16f, "box3")
                label("$path/Background/Label", text, tx, y + labelYOffset, tw, 34.4f)
            }
            sourceButton("bg1/button5", "이전 무장", 678.700f, 36.98f, 146.2f, 683f, 137.6f)
            sourceButton("bg1/button6", "다음 무장", 838.660f, 36.98f, 146.2f, 842.96f, 137.6f)
            sourceButton("bg1/button7", "종료", 530.780f, 36.98f, 120.4f, 547.98f, 86f)
            source("bg1/box1", "sliced-sprite", 176.030f, 89.01f, 480.74f, 503.1f, "box1")
            source("bg1/box1/bg1", "sliced-sprite", 187.478f, 574.188f, 137.772f, 36.464f, "bg1")
            label("bg1/box1/bg1/label", "상품 목록", 192.075f, 570.748f, 128.579f)
            sourceButton("bg1/box1/button0", "무기점", 183.045f, 521.159f, 154.8f, 221.874f, 105.78f, 9.002f)
            source("bg1/box1/button0/Background/command1", "sprite", 191.667f, 533.248f, 27.52f, 27.52f, "command1")
            sourceButton("bg1/box1/button1", "상점", 337.547f, 521.159f, 154.8f, 376.376f, 105.78f, 9.002f)
            source("bg1/box1/button1/Background/command1", "sprite", 347.030f, 534.108f, 25.8f, 25.8f, "command3")
            source("bg1/box1/box0", "sliced-sprite", 182.910f, 95.331f, 465.26f, 426.818f, "box2")
            input.buyRows.forEachIndexed { index, item ->
                val y = 369.069f - index * 153.08f
                val path = "bg1/box1/box0/scrollview/view/content/item"
                source(path, "sliced-sprite", 184.630f, y, 461.82f, 151.36f, "box3")
                source("$path/box2", "sliced-sprite", 190.905f, y + 57.955f, 86f, 86f, "box2")
                source("$path/box2/icon", "sprite", 195.377f, y + 62.427f, 77.056f, 77.056f, "1-1")
                label("$path/label0", item.name, 283.1f, y + 103.888f, 195.53f)
                label("$path/label", "속성: ", 283.1f, y + 59.168f, 78.63f)
                label("$path/label1", item.typeName, 371.68f, y + 59.168f, 29.756f)
                label("$path/label", "레벨:", 475.74f, y + 103.888f, 69.067f)
                label("$path/label2", "1", 562.6f, y + 103.888f, 19.135f)
                label("$path/label", "인벤토리: ", 193.66f, y + 8.428f, 138.142f)
                label("$path/label3", item.inventoryCount.toString(), 280.52f, y + 8.428f, 19.135f)
                label("$path/label", "총합: ", 338.14f, y + 8.428f, 78.63f)
                label("$path/label4", "0", 425f, y + 8.428f, 19.135f)
                label("$path/label", "가격:", 475.74f, y + 8.428f, 69.067f)
                label("$path/label5", item.purchasePrice.toString(), 562.6f, y + 8.428f, 19.135f)
            }
            val unit = input.unit
            val level = unit.level
            val stats = unit.stats
            source("bg1/vline", "sprite", 664.568f, 33.841f, 5.16f, 582.306f, "vline")
            source("bg1/button0", "sliced-sprite", 726.230f, 565.88f, 309.6f, 48.16f, "box3")
            source("bg1/button0/vline", "sprite", 878.450f, 571.169f, 5.16f, 41.022f, "vline")
            label("bg1/button0/label0", unit.name, 773.747f, 570.868f, 59.512f)
            val postsName = unit.postsName
            label("bg1/button0/label1", postsName, 930.320f, 570.868f, 59.512f)
            source("bg1/scrollview/view/content/box1/face", "sprite", 703.548f, 355.470f, 165.12f, 206.4f, "1")
            source(
                "bg1/scrollview/view/content/box1/face/bg0",
                "sliced-sprite",
                682.908f,
                355.470f,
                206.4f,
                206.4f,
                "box2"
            )
            label("bg1/scrollview/view/content/box1/label0", unit.name, 899.090f, 517.479f, 59.512f)
            label("bg1/scrollview/view/content/box1/label1", postsName, 899.090f, 464.159f, 59.512f)
            label("bg1/scrollview/view/content/box1/label", "Exp", 899.090f, 357.519f, 59.28f)
            source(
                "bg1/scrollview/view/content/box1/progressBar",
                "sliced-sprite",
                963.590f,
                368.871f,
                115.24f,
                20.64f,
                "default_scrollbar_bg"
            )
            source(
                "bg1/scrollview/view/content/box1/progressBar/bar",
                "sliced-sprite",
                965.310f,
                370.591f,
                0f,
                17.2f,
                "Mark_6-1"
            )
            label("bg1/scrollview/view/content/box1/progressBar/label", "0/100", 978.167f, 369.881f, 86.086f)
            label("bg1/scrollview/view/content/box1/label", "Lv", 898.982f, 410.839f, 36.335f)
            label("bg1/scrollview/view/content/box1/label2", level.toString(), 953.162f, 410.839f, 19.135f)
            val statNames = listOf("HP", "MP", "공격력", "정신력", "방어력", "폭발력", "사기", "이동력")
            val statLabelRects = listOf(
                689.434f to 309.359f, 902.530f to 309.359f, 696.216f to 258.619f, 909.496f to 258.619f,
                696.216f to 207.019f, 909.496f to 207.019f, 693.894f to 156.279f, 909.496f to 156.279f
            )
            val boxRects = listOf(
                801.050f to 309.531f, 1015.190f to 309.531f, 801.050f to 258.791f, 1015.190f to 258.791f,
                801.050f to 207.191f, 1015.190f to 207.191f, 801.050f to 156.451f, 1015.190f to 156.451f
            )
            val statLabelWidths = listOf(47.7902f, 51.6f, 89.268f, 89.268f, 89.268f, 89.268f, 59.512f, 89.268f)
            statNames.forEachIndexed { index, name ->
                val (lx, ly) = statLabelRects[index]
                label("bg1/scrollview/view/content/box1/label", name, lx, ly, statLabelWidths[index])
            }
            stats.forEachIndexed { index, value ->
                val (bx, by) = boxRects[index]
                source("bg1/scrollview/view/content/box1/bg$index", "sliced-sprite", bx, by, 68.8f, 43f, "box2")
                val textWidth = when (value.toString().length) {
                    1 -> 19.135f; 2 -> 38.261f; else -> 57.396f
                }
                label(
                    "bg1/scrollview/view/content/box1/bg$index/label",
                    value.toString(),
                    bx + (68.8f - textWidth) / 2f,
                    by - .172f,
                    textWidth
                )
            }
            unit.weapon?.let { equipped ->
                val item = equipped
                source("bg1/scrollview/view/content/bg0", "sliced-sprite", 679.747f, 20.967f, 402.566f, 129f, "box1")
                label("bg1/scrollview/view/content/bg0/label", "무기:", 834.760f, 101.326f, 69.067f)
                label("bg1/scrollview/view/content/bg0/label0", item.name, 897.066f, 101.025f, 59.512f)
                source(
                    "bg1/scrollview/view/content/bg0/box2",
                    "sliced-sprite",
                    685.515f,
                    28.484f,
                    115.911f,
                    116.186f,
                    "box2"
                )
                source("bg1/scrollview/view/content/bg0/box2/icon", "sprite", 688.43f, 31.537f, 110.08f, 110.08f, "1-1")
                label("bg1/scrollview/view/content/bg0/label_0", "Lv", 810.295f, 60.248f, 36.335f)
                label("bg1/scrollview/view/content/bg0/label1", equipped.level.toString(), 864.032f, 60.142f, 19.135f)
                label("bg1/scrollview/view/content/bg0/label_1", "Exp", 812.27f, 26.298f, 59.28f)
                source(
                    "bg1/scrollview/view/content/bg0/progressBar",
                    "sliced-sprite",
                    880.21f,
                    25.61f,
                    175.44f,
                    20.64f,
                    "default_scrollbar_bg"
                )
                source(
                    "bg1/scrollview/view/content/bg0/progressBar/bar",
                    "sliced-sprite",
                    881.93f,
                    27.33f,
                    0f,
                    17.2f,
                    "Mark_6-1"
                )
                label("bg1/scrollview/view/content/bg0/progressBar/label", "0/100", 924.887f, 26.62f, 86.086f)
            }
            return
        }
    }
}
