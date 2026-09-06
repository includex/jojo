// Dialogue
package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

/** Scene2D 대화창이 참조하는 화면 자원이다. 자원 수명은 화면이 소유한다. */
data class DialogueScene2dAssets(
    /** 대화·모달 창의 배경 텍스처다. */
    val dialoguePanel: Texture?,
    /** 선택지 창의 배경 텍스처다. */
    val choicePanel: Texture? = dialoguePanel,
    /** 화자 초상화를 조회한다. */
    val portrait: (Int) -> Texture? = { null },
    /** 대사 본문 글꼴이다. */
    val bodyFont: BitmapFont,
    /** 화자 이름 글꼴이다. */
    val speakerFont: BitmapFont = bodyFont,
    /** 제목·선택지 글꼴이다. */
    val titleFont: BitmapFont = bodyFont,
)

/**
 * 공용 대화 세션 표시를 Scene2D Actor 트리로 투영하는 재사용 뷰다.
 *
 * Stage와 Skin을 소유하지 않는다. 화면은 자신의 Stage와 Skin을 주입하고 dispose해야 하며,
 * Texture·BitmapFont도 이 뷰가 해제하지 않는다. 따라서 기존 SpriteBatch 자산과 이중 해제가 없다.
 */
class DialogueScene2dView(
    /** 외부 화면이 수명과 스타일을 관리하는 Skin이다. */
    private val skin: Skin,
    /** 대화창이 사용하는 텍스처·글꼴 자원이다. */
    private val assets: DialogueScene2dAssets,
    /** 선택지 버튼을 눌렀을 때 전달할 선택 인덱스다. */
    private val onChoice: (Int) -> Unit = {},
) : Table() {
    /** 최상위 Window이며 모든 대화·선택·모달 자식의 부모다. */
    private val window = Window("", windowStyle())

    /** 마지막으로 투영한 모델이며 동일 모델의 Actor 재구성을 막는다. */
    private var lastModel: DialogueOverlayModel? = null

    init {
        isVisible = false
        setFillParent(true)
        addActor(window)
        window.isVisible = false
        touchable = Touchable.childrenOnly
    }

    /** 세션 표시 모델을 Window·Table·Label·Image·TextButton 트리로 갱신한다. */
    fun present(model: DialogueOverlayModel?) {
        if (lastModel == model) return
        lastModel = model
        window.clearChildren()
        isVisible = model != null
        window.isVisible = model != null
        if (model == null) return
        configureWindow(model)
        model.modal?.let { addModal(it) }
        model.dialogue?.let { addDialogue(it) }
        model.choice?.let { addChoice(it) }
        invalidateHierarchy()
    }

    /** Actor 트리를 비우고 다음 모델을 새로 받을 수 있게 한다. */
    fun clearView() = present(null)

    /** 화면 종류에 맞춰 전체화면이 아닌 실제 패널 크기와 위치를 설정한다. */
    private fun configureWindow(model: DialogueOverlayModel) {
        val dialogue = model.dialogue
        when {
            dialogue != null -> {
                val placement = dialogue.componentPlacement
                val x = placement?.panelX ?: dialogue.panelXOverride
                    ?: if (dialogue.isLeft) 274.54054f else 316.40878f
                val y = placement?.panelY ?: dialogue.panelYOverride
                    ?: 55.47f + if (dialogue.isAtTop) 373.24f else 0f
                val battleSized = placement != null || dialogue.panelXOverride != null
                window.setSize(
                    placement?.panelWidth ?: if (battleSized) 796f else 686.28f,
                    placement?.panelHeight ?: if (battleSized) 212f else 164.26f,
                )
                window.setPosition(x, y)
            }
            model.choice != null -> {
                val confirmation = model.choice.isConfirmation
                window.setSize(if (confirmation) 351.74f else 642.42f, if (confirmation) 134.16f else 157.98f)
                window.setPosition(if (confirmation) 464.13f else 423.71f, if (confirmation) 276.92f else 265.01f)
            }
            model.modal != null -> {
                window.setSize(640f, 120f)
                window.setPosition((1280f - window.width) / 2f, (688f - window.height) / 2f)
            }
            else -> {
                isVisible = false
                window.isVisible = false
            }
        }
    }

    /** 대사 Actor를 화자·초상화·본문 Table로 구성한다. */
    private fun addDialogue(model: DialogueRenderModel) {
        model.componentPlacement?.let { placement ->
            addPositionedDialogue(model, placement)
            return
        }
        val content = Table(skin)
        model.portraitId?.let(assets.portrait)?.let { texture ->
            content.add(Image(TextureRegionDrawable(TextureRegion(texture)))).size(192f, 240f).pad(8f)
        }
        val text = Table(skin)
        text.add(Label(model.speaker, Label.LabelStyle(assets.speakerFont, Color.WHITE))).left().row()
        text.add(
            Label(
                model.visibleText,
                Label.LabelStyle(assets.bodyFont, Color.BLACK),
            ),
        ).width(728f).left().growY()
        content.add(text).expand().fill().pad(16f)
        window.add(content).expand().fill().pad(16f)
    }

    /** 원본 SayLayer처럼 패널 밖의 초상화를 포함한 모든 대사 요소를 절대 좌표로 배치한다. */
    private fun addPositionedDialogue(model: DialogueRenderModel, placement: DialogueComponentPlacement) {
        model.portraitId?.let(assets.portrait)?.let { texture ->
            Image(TextureRegionDrawable(TextureRegion(texture))).also { portrait ->
                val bounds = DialoguePortraitGeometry.fit(
                    texture,
                    placement.portraitX,
                    placement.portraitY,
                    placement.portraitWidth,
                    placement.portraitHeight,
                )
                portrait.setSize(bounds.width, bounds.height)
                portrait.setPosition(
                    bounds.x - placement.panelX,
                    bounds.y - placement.panelY,
                )
                window.addActor(portrait)
            }
        }
        Label(model.speaker, Label.LabelStyle(assets.speakerFont, Color.WHITE)).also { speaker ->
            speaker.pack()
            speaker.setPosition(
                placement.speakerX - placement.panelX,
                placement.speakerBaselineY - placement.panelY - speaker.height,
            )
            window.addActor(speaker)
        }
        Label(model.visibleText, Label.LabelStyle(assets.bodyFont, Color.BLACK)).also { body ->
            body.setWrap(true)
            body.setSize(placement.textWidth, placement.panelHeight - 42f)
            body.setPosition(
                placement.textX - placement.panelX,
                placement.textBaselineY - placement.panelY - body.prefHeight,
            )
            window.addActor(body)
        }
    }

    /** 선택지 Actor를 TextButton 목록으로 구성한다. */
    private fun addChoice(model: ChoiceRenderModel) {
        val content = Table(skin)
        content.add(Label(model.title, Label.LabelStyle(assets.titleFont, Color.WHITE))).left().row()
        model.options.forEachIndexed { index, option ->
            val label = if (index == model.selectedIndex) "▶ $option" else option
            val button = TextButton(label, textButtonStyle(index == model.selectedIndex))
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) = onChoice(index)
            })
            content.add(button).left().minWidth(420f).pad(4f).row()
        }
        window.add(content).expand().fill().pad(16f)
    }

    /** 모달 본문을 중앙 Label로 구성한다. */
    private fun addModal(model: ModalRenderModel) {
        val content = Table(skin)
        val text = sanitize(model.fixedText + model.visibleText)
        content.add(Label(text, Label.LabelStyle(assets.titleFont, Color.WHITE))).center().expand()
        window.add(content).expand().fill().pad(24f)
    }

    /** 공용 Skin에 등록된 스타일을 우선하되 최소 스타일을 자체 생성한다. */
    private fun windowStyle(): Window.WindowStyle = Window.WindowStyle(
        assets.titleFont,
        Color.WHITE,
        drawable(assets.dialoguePanel),
    )

    /** 선택된 항목과 일반 항목의 글꼴 색을 구분한다. */
    private fun textButtonStyle(selected: Boolean): TextButton.TextButtonStyle =
        TextButton.TextButtonStyle(
            null,
            null,
            null,
            assets.titleFont,
        ).also { it.fontColor = if (selected) Color.YELLOW else Color.WHITE }

    /** 텍스처를 Scene2D Drawable로 감싼다. Drawable은 원본 텍스처를 소유하지 않는다. */
    private fun drawable(texture: Texture?): Drawable? = texture?.let { TextureRegionDrawable(TextureRegion(it)) }

    /** 기존 리치 텍스트 제어 토큰을 Scene2D Label용 문자열에서 제거한다. */
    private fun sanitize(text: String): String = text.replace(Regex("<[^>]*>|\\[C[0-9A-Fa-f]+"), "").replace('☆', '★')
}
