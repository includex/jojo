// Dialogue
package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport

/** 공용 대화 Scene2D 뷰를 Stage 수명주기와 연결하는 호스트다. */
class DialogueScene2dHost(
    /** 대화 위젯을 그릴 Stage다. */
    private val stage: Stage,
    /** 대화 위젯과 선택지·모달 상태를 보관하는 뷰다. */
    private val view: DialogueScene2dView,
) : Disposable {
    /** 화면이 기존 FitViewport를 그대로 사용할 수 있는 편의 생성자다. */
    constructor(viewport: Viewport, skin: Skin, assets: DialogueScene2dAssets) : this(
        Stage(viewport),
        DialogueScene2dView(skin, assets),
    )
    /** 마지막 프레임 간격을 보관해 분리된 present·render 호출을 지원한다. */
    private var lastDelta = 0f
    init {
        stage.addActor(view)
    }

    /** 화면 모델을 반영하고 Stage를 갱신·렌더링한다. */
    fun present(model: DialogueOverlayModel?) = view.present(model)

    /** 이미 모델을 반영한 위젯을 한 프레임 그린다. */
    fun render() {
        stage.act(lastDelta.coerceAtLeast(0f))
        stage.draw()
    }

    /** 모델 반영과 한 프레임 렌더링을 한 번에 수행한다. */
    fun render(model: DialogueOverlayModel?, delta: Float) {
        lastDelta = delta
        view.present(model)
        render()
    }

    /** 화면 크기 변경을 Stage 뷰포트에 전달한다. */
    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    /** Stage 입력 처리를 외부 InputMultiplexer에 연결할 수 있도록 반환한다. */
    fun inputProcessor() = stage

    /** 화면 전환 시 Scene2D 자원을 해제한다. */
    override fun dispose() {
        view.remove()
        stage.dispose()
    }
}
