// Dialogue
package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align

/**
 * 선택창을 Scene2D Actor로 구성하는 계층이다.
 *
 * 항목 배경과 본문을 위젯으로 만들고 터치도 그 위젯이 직접 받는다. 좌표는 그리기와 같은
 * [DialogueRenderLayout]에서 가져오므로 배치와 클릭 판정이 어긋날 수 없다.
 *
 * 두 버튼 확인 상자(`isConfirmation`)는 항목 목록이 아니라 별개 위젯이라 여기서 다루지 않고
 * 공용 렌더러가 계속 그린다.
 */
class ChoiceScene2dLayer(
    /** 그리기와 공유하는 원본 배치 값이다. */
    private val layout: DialogueRenderLayout,
    /** 항목을 눌렀을 때 전달할 선택 인덱스다. */
    private val onChoice: (Int) -> Unit,
) : Group() {
    /** 마지막으로 구성한 표시 상태이며 같은 상태의 Actor 재구성을 막는다. */
    private var lastKey: Any? = null

    init {
        touchable = Touchable.childrenOnly
        isVisible = false
    }

    /**
     * `handles`: 이 계층이 맡는 선택창인지 판단한다.
     *
     * 확인 상자는 공용 렌더러가 그리므로 제외한다.
     */
    fun handles(model: ChoiceRenderModel?): Boolean = model != null && !model.isConfirmation

    /** `present`: 표시 상태를 Actor 트리에 반영한다. */
    fun present(model: ChoiceRenderModel?, assets: DialogueRenderAssets) {
        if (!handles(model)) {
            isVisible = false
            if (lastKey != null) {
                clearChildren()
                lastKey = null
            }
            return
        }
        val choice = requireNotNull(model)
        isVisible = true
        // 원본 ChooseLayer는 항목을 모두 만든 뒤 `view` 안에서만 보여 주고 나머지는 스크롤로
        // 닿게 한다. 그리기와 같은 규칙으로 보이는 창의 첫 항목을 정한다.
        val first = choice.firstVisibleIndex
            .coerceIn(0, maxOf(0, choice.options.size - layout.choiceVisibleRows))
        val key = listOf(choice.options, choice.selectedIndex, first, choice.portraitId)
        if (key == lastKey) return
        lastKey = key
        clearChildren()
        assets.choicePanel?.let {
            addActor(
                image(it, layout.choicePanelX, layout.choicePanelY, layout.choicePanelWidth, layout.choicePanelHeight),
            )
        }
        choice.portraitId?.let(assets::portrait)?.let { texture ->
            val bounds = DialoguePortraitGeometry.fit(
                texture, layout.choicePortraitX, layout.choicePortraitY, layout.portraitWidth, layout.portraitHeight,
            )
            addActor(image(texture, bounds.x, bounds.y, bounds.width, bounds.height))
        }
        choice.options.drop(first).take(layout.choiceVisibleRows).forEachIndexed { row, option ->
            addActor(rowActor(first + row, option, layout.choiceRowBottom(row), choice.selectedIndex, assets))
        }
    }

    /** `rowActor`: 항목 하나를 배경·본문·클릭 리스너를 갖춘 Actor로 만든다. */
    private fun rowActor(
        index: Int,
        option: String,
        rowY: Float,
        selectedIndex: Int,
        assets: DialogueRenderAssets,
    ): Group {
        val group = Group()
        group.setBounds(layout.choiceRowX, rowY, layout.choiceRowWidth, layout.choiceRowHeight)
        group.touchable = Touchable.enabled
        assets.choiceRow?.let { group.addActor(image(it, 0f, 0f, layout.choiceRowWidth, layout.choiceRowHeight)) }
        // 원본은 터치 전용이라 선택 표시가 없다. 키보드 조작을 위해 선택 항목만 원본 화자
        // 라벨과 같은 파란색으로 구분하고, 나머지는 원본처럼 검은색으로 둔다.
        val color =
            if (index == selectedIndex) Color(35f / 255f, 2f / 255f, 234f / 255f, 1f) else Color.BLACK
        val label = Label(option, Label.LabelStyle(assets.bodyFont, color))
        // 원본 항목의 `richtext`는 배경과 같은 높이를 차지해 글자가 세로 가운데에 놓인다.
        label.setBounds(
            layout.choiceTextX - layout.choiceRowX, 0f,
            layout.choiceRowWidth - (layout.choiceTextX - layout.choiceRowX), layout.choiceRowHeight,
        )
        label.setAlignment(Align.left)
        label.touchable = Touchable.disabled
        group.addActor(label)
        group.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) = onChoice(index)
        })
        return group
    }

    /** `image`: 텍스처를 지정한 사각형에 놓는 Image Actor를 만든다. */
    private fun image(texture: Texture, x: Float, y: Float, width: Float, height: Float): Image =
        Image(TextureRegion(texture)).apply {
            setBounds(x, y, width, height)
            touchable = Touchable.disabled
        }
}
