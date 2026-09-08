// Verification
package com.jojo.game.verification.evidence

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/**
 * LearnUnitSkillScreenEvents: 유닛 특성 편집 화면의 렌더 이벤트를 게임 자료에서 계산한다.
 *
 * 예전에는 원본의 렌더 이벤트를 gzip·base64로 저장해 두었다가 그대로 내놓았다.
 * 그러면 원본과의 비교가 순환 논증이 되고, 저장본이 낡았을 때만 어긋난다.
 *
 * 목록은 원본 `LearnUnitSkillLayer`와 같이 `unitPostsSkill` 표를 훑어
 * `"<번호>.<이름>"`으로 적는다. 오른쪽 세 판의 칸 값도 같은 표(재능)와 저장 자료
 * (전용·세트)에서 온다. 저장 자료가 비어 있는 새 프로필에서는 원본이 쓰는 기본값
 * 1024·255가 그대로 나오고, 이름은 각 표의 조회 결과가 비면 "공백"이 된다.
 */
internal object LearnUnitSkillScreenEvents {
    /** 라벨 혼합 방식이다. */
    private val LABEL_BLEND = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
    /** 스프라이트 혼합 방식이다. */
    private val SPRITE_BLEND = listOf(770, 771)

    /** 비어 있는 칸의 이름이다. 원본이 각 조회에 넘기는 기본값과 같다. */
    private const val EMPTY_SLOT = "공백"

    /** 목록 첫 줄의 y와 줄 간격이다. */
    private const val FIRST_ROW_Y = 650.5f
    private const val ROW_PITCH = 64f

    /** 판 단추의 폭이다. 라벨은 이 안에서 가운데로 온다. */
    private const val BUTTON_WIDTH = 180.8f

    /** 판 하나의 배치다. */
    private data class Panel(
        val y: Float,
        val height: Float,
        val title: String,
        val titleY: Float,
        val buttons: List<PanelButton>,
        val editBoxes: List<Pair<Float, Float>>,
        val extraTitles: List<Pair<String, Float>> = emptyList(),
    )

    /** 판 단추 하나의 배치다. `index`는 노드 이름의 번호이고 목록 순서는 그리기 순서다. */
    private data class PanelButton(val index: Int, val x: Float, val y: Float)

    /**
     * 세 판의 배치다.
     *
     * 단추 x가 508.786과 509.786처럼 1씩 다르고 그리기 순서도 0,1,3,2로 어긋난다.
     * 원본 프리팹이 그렇게 저장돼 있으므로 공식으로 만들지 않고 잰 값을 그대로 둔다.
     */
    private val PANELS = listOf(
        Panel(
            y = 508.5f, height = 193f, title = "재능", titleY = 646.8f,
            buttons = listOf(
                PanelButton(0, 509.786f, 587.4f),
                PanelButton(1, 725.786f, 587.4f),
                PanelButton(2, 940.786f, 587.4f),
                PanelButton(3, 508.786f, 520.4f),
            ),
            editBoxes = listOf(733.186f to 525f),
        ),
        Panel(
            y = 302f, height = 190f, title = "전용", titleY = 437.8f,
            buttons = listOf(
                PanelButton(0, 509.786f, 376.55f),
                PanelButton(1, 720.786f, 376.55f),
                PanelButton(3, 719.786f, 314.4f),
                PanelButton(2, 508.786f, 314.4f),
            ),
            editBoxes = listOf(931.186f to 381.15f, 931.186f to 319f),
        ),
        Panel(
            y = 89f, height = 194f, title = "세트", titleY = 220.71f,
            buttons = listOf(
                PanelButton(0, 509.786f, 163.4f),
                PanelButton(1, 696.786f, 163.4f),
                PanelButton(2, 883.786f, 163.4f),
            ),
            editBoxes = listOf(1071.186f to 168f),
            extraTitles = listOf("무기" to 565.586f, "보구" to 752.586f, "보조" to 939.586f),
        ),
    )

    /** 세트 판의 둘째 줄이다. 첫 줄과 같은 x를 쓰고 y만 내려간다. */
    private val SECOND_SET_ROW = listOf(
        PanelButton(3, 508.786f, 101.4f),
        PanelButton(4, 696.786f, 101.4f),
        PanelButton(5, 883.786f, 101.4f),
    )

    /** 기록: 배경 위에 편집 창을 남긴다. */
    fun record(
        catalog: GameDataCatalog,
        selectedSkillId: Int,
        routeKey: String = "default",
        /** 저장된 재능 첫 칸의 무장 번호다. 편집을 적용한 화면이 이 자리를 쓴다. */
        unit0Override: Int? = null,
    ): String {
        val phase = "hall-learn-$routeKey-stable"
        val log = RenderEventLog()

        /** draw: 편집 창의 한 줄을 남긴다. */
        fun draw(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String? = null, text: String = "") =
            log.draw(
                phase, "HallLayer", "Canvas/Layer/$path", type, x, y, w, h, asset,
                blend = if (type == "label") LABEL_BLEND else SPRITE_BLEND, text = text,
            )

        log.draw(
            phase, "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>",
        )
        log.draw(
            phase, "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            "default_sprite_splash", opacity = 100f / 255f,
        )
        val root = "Logo_12-1"
        draw(root, "tiled-sprite", 132.186f, 17f, 1232f, 760f, "Logo_9-1")
        draw("$root/box4", "sliced-sprite", 132.186f, 17f, 1232f, 760f, "box4")
        draw("$root/bg1", "sprite", 132.186f, 717f, 1232f, 60f, "bg1")
        draw("$root/bg1/box3", "sliced-sprite", 132.186f, 717f, 1232f, 60f, "box3")
        // 제목은 본문보다 큰 글꼴이라 본문 폭 표로 재현되지 않는다. 잰 값을 쓴다.
        draw("$root/bg1/label", "label", 139.639f, 722.8f, 231.83f, 52.4f, text = "유닛 특성 편집")
        draw("$root/scrollview", "tiled-sprite", 147.186f, 89.5f, 234f, 623f, "Logo_12-1")
        draw("$root/scrollview/box2", "tiled-sprite", 147.186f, 89.5f, 234f, 623f, "box2")
        // 원본 스크롤뷰는 표의 모든 줄을 만들어 둔다. 화면 밖으로 밀린 줄은 비교기가
        // 양쪽에서 똑같이 걸러 내므로 여기서 줄 수를 미리 자르지 않는다.
        val item = "$root/scrollview/view/content/item0"
        catalog.postSkillNames().forEachIndexed { index, name ->
            val y = FIRST_ROW_Y - index * ROW_PITCH
            draw(item, "sprite", 150.036f, y, 229.1f, 60f, "bg1")
            draw("$item/box3", "sliced-sprite", 150.036f, y, 229.1f, 60f, "box3")
            if (index == selectedSkillId) draw("$item/box6", "sprite", 151.036f, y + 2f, 226.1f, 57f, "box6")
            draw("$item/label", "label", 155.986f, y + 6f, 211.9f, 54f, text = "$index.$name")
        }
        draw("$root/button0/Background", "sliced-sprite", 1195.772f, 29.187f, 147.6f, 56f, "box3")
        draw("$root/button0/Background/Label", "label", 1219.572f, 38.187f, 100f, 40f, text = "수정")
        draw("$root/button1/Background", "sliced-sprite", 1040.095f, 29.209f, 147.6f, 56f, "box3")
        draw("$root/button1/Background/Label", "label", 1080.755f, 28.489f, 66.28f, 59.44f, text = "폐쇄")
        appendPanels(catalog, selectedSkillId, unit0Override, ::draw)
        return log.jsonl()
    }

    /** 판 그리기: 세 판의 칸 이름과 값을 자료에서 만들어 남긴다. */
    private fun appendPanels(
        catalog: GameDataCatalog,
        skillId: Int,
        unit0Override: Int?,
        draw: (String, String, Float, Float, Float, Float, String?, String) -> Unit,
    ) {
        val slots = panelSlotLabels(catalog, skillId, unit0Override)
        val values = panelSlotValues(catalog, skillId)
        // 칸은 단추 번호 순서로 정해지고 그리기 순서는 프리팹의 zIndex를 따른다.
        // 둘을 같은 것으로 보면 원본이 0,1,3,2로 내는 판에서 라벨이 서로 바뀐다.
        val panelSlotBase = listOf(0, 4, 8)
        PANELS.forEachIndexed { panelIndex, panel ->
            val root = "Logo_12-1/panel$panelIndex"
            draw(root, "sliced-sprite", 388.686f - if (panelIndex == 0) 1f else 0f, panel.y, 959f - if (panelIndex == 0) 0f else 2f, panel.height, "default_panel", "")
            draw("$root/label", "label", 401.586f, panel.titleY, 69.2f, 50.4f, null, panel.title)
            panel.buttons.forEach { button ->
                appendButton(draw, root, button, slots[panelSlotBase[panelIndex] + button.index])
            }
            panel.editBoxes.forEachIndexed { boxIndex, (x, y) ->
                draw("$root/editbox$boxIndex/BACKGROUND_SPRITE", "sliced-sprite", x, y, 160f, 40f, "default_editbox_bg", "")
                draw("$root/editbox$boxIndex/TEXT_LABEL", "label", x + 2f, y, 158f, 40f, null, values[panelIndex * 2 + boxIndex].toString())
            }
            if (panelIndex == 2) {
                SECOND_SET_ROW.forEach { button ->
                    appendButton(draw, root, button, slots[panelSlotBase[panelIndex] + button.index])
                }
                draw("$root/editbox1/BACKGROUND_SPRITE", "sliced-sprite", 1071.186f, 106f, 160f, 40f, "default_editbox_bg", "")
                draw("$root/editbox1/TEXT_LABEL", "label", 1073.186f, 106f, 158f, 40f, null, values[5].toString())
                panel.extraTitles.forEach { (title, x) ->
                    draw("$root/label", "label", x, panel.titleY, 69.2f, 50.4f, null, title)
                }
            }
        }
    }

    /** 단추 하나: 배경과 가운데 정렬된 라벨을 남기고 소비한 칸 수를 돌려준다. */
    private fun appendButton(
        draw: (String, String, Float, Float, Float, Float, String?, String) -> Unit,
        root: String,
        button: PanelButton,
        text: String,
    ) {
        draw("$root/button${button.index}/Background", "sliced-sprite", button.x, button.y, BUTTON_WIDTH, 49.2f, "default_btn_normal", "")
        val width = SourceLabelWidth.panelButton(text)
        draw(
            "$root/button${button.index}/Background/Label", "label",
            button.x + (BUTTON_WIDTH - width) / 2f, button.y - .6f, width, 50.4f, null, text,
        )
    }

    /** 칸 이름: 원본이 각 칸에 넣는 `"<번호>. <이름>"` 문자열을 순서대로 만든다. */
    private fun panelSlotLabels(catalog: GameDataCatalog, skillId: Int, unit0Override: Int?): List<String> = buildList {
        (0 until 3).forEach { slot ->
            val stored = catalog.postSkillAttribute(skillId, UNIT0 + slot, EMPTY_UNIT)
            val id = if (slot == 0) unit0Override ?: stored else stored
            add("$id. " + unitSlotName(catalog, id))
        }
        val posts = catalog.postSkillAttribute(skillId, POSTS, EMPTY_ITEM)
        add("$posts. " + catalog.postsName(posts).orEmptySlot())
        // 전용·세트 칸은 저장 자료에서 오는데 새 프로필에서는 비어 있어 기본값이 나온다.
        repeat(2) {
            add("$EMPTY_UNIT. " + unitSlotName(catalog, EMPTY_UNIT))
            add("$EMPTY_ITEM. " + (catalog.equipmentProfile(EMPTY_ITEM)?.name.orEmptySlot()))
        }
        repeat(6) { add("$EMPTY_ITEM. " + (catalog.equipmentProfile(EMPTY_ITEM)?.name.orEmptySlot())) }
    }

    /** 칸 값: 각 판의 입력칸에 들어가는 효과 값이다. */
    private fun panelSlotValues(catalog: GameDataCatalog, skillId: Int): List<Int> =
        listOf(catalog.postSkillAttribute(skillId, EFF_VALUE, 0), 0, 0, 0, 0, 0)

    /** 비어 있는 이름은 원본과 같이 "공백"으로 읽는다. */
    private fun String?.orEmptySlot(): String = this?.takeIf { it.isNotEmpty() } ?: EMPTY_SLOT

    /**
     * 무장 칸 이름: 원본 `unitAttr2(id, NAME, "공백")`과 같은 계약이다.
     *
     * 이 조회는 정적 무장 표가 아니라 저장 자료의 속성 배열을 읽는다. 번호가
     * `UNIT_LIMIT`(1024) 안이면 새 프로필의 빈 칸 값 `0`이 그대로 이름 자리에 들어가고,
     * 밖이면 기본값 "공백"이 된다. 그래서 1001번은 "1001. 0", 1024번은 "1024. 공백"이다.
     */
    private fun unitSlotName(catalog: GameDataCatalog, id: Int): String = when {
        id < 0 || id >= UNIT_LIMIT -> EMPTY_SLOT
        else -> catalog.unitProfile(id)?.name.orEmptySlot().takeIf { it != EMPTY_SLOT } ?: UNSET_SAVE_SLOT
    }

    /** `UNIT_POSTS_SKILL_ATTR.POSTS`다. */
    private const val POSTS = 2
    /** `UNIT_POSTS_SKILL_ATTR.EFF_VALUE`다. */
    private const val EFF_VALUE = 5
    /** `UNIT_POSTS_SKILL_ATTR.UNIT0`다. */
    private const val UNIT0 = 6
    /** 원본이 무장 칸 조회에 넘기는 기본값이다. */
    private const val EMPTY_UNIT = 1024
    /** 원본이 장비·직업 칸 조회에 넘기는 기본값이다. */
    private const val EMPTY_ITEM = 255
    /** 저장 자료의 무장 속성 배열 크기다(`Config.UNIT_LIMIT`). */
    private const val UNIT_LIMIT = 1024
    /** 새 프로필의 빈 저장 칸이 이름 자리에 내놓는 값이다. */
    private const val UNSET_SAVE_SLOT = "0"
}
