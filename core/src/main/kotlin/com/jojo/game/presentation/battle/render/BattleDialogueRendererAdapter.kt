// Battle
package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.jojo.game.presentation.shared.dialogue.DialogueRenderAssets
import com.jojo.game.presentation.shared.dialogue.DialogueRenderModel
import com.jojo.game.presentation.shared.dialogue.DialogueOverlayModel
import com.jojo.game.presentation.shared.dialogue.DialogueRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4

/** 전투가 소유한 HUD 자원을 공용 대화 렌더러의 자산 포트로 변환한다. */
class BattleDialogueRendererAssetsAdapter(
    /** 전투 대화창 배경이다. */
    override val dialoguePanel: Texture?,
    /** 전투 선택지 전체 배경이다. */
    override val choicePanel: Texture? = null,
    /** 전투 선택지 행 배경이다. */
    override val choiceRow: Texture? = null,
    /** 전투 정보 모달 패널이다. */
    override val infoPanel: NinePatch? = null,
    /** 전투 대사 본문 글꼴이다. */
    override val bodyFont: BitmapFont,
    /** 전투 화자 이름 글꼴이다. */
    override val speakerFont: BitmapFont = bodyFont,
    /** 전투 선택지·모달 제목 글꼴이다. */
    override val titleFont: BitmapFont = bodyFont,
    /** 전투 유닛 ID를 초상화 자원으로 바꾸는 함수다. */
    private val portraitProvider: (Int) -> Texture? = { null },
) : DialogueRenderAssets {
    /** 전투 초상화 자원을 공용 계약으로 반환한다. */
    override fun portrait(portraitId: Int): Texture? = portraitProvider(portraitId)
}

/** 전투 화면의 기존 호출부가 공용 렌더러를 단계적으로 사용할 수 있게 하는 어댑터다. */
class BattleDialogueRendererAdapter(
    /** 전투 좌표와 공용 표시 순서를 가진 렌더러다. */
    private val renderer: DialogueRenderer,
    /** 전투 HUD 자원을 공용 자산 포트로 제공한다. */
    private val assets: DialogueRenderAssets,
) {
    /** 대사·선택·모달을 전투 화면의 SpriteBatch와 ShapeRenderer에 연결한다. */
    fun draw(
        batch: SpriteBatch,
        shapes: ShapeRenderer,
        projection: Matrix4,
        model: DialogueOverlayModel,
    ) = renderer.draw(batch, shapes, projection, model, assets)
}

/** 전투 런타임 대화 값을 공용 표시 모델로 변환한다. */
fun battleDialogueRenderModel(
    speaker: String,
    visibleText: String,
    portraitId: Int? = null,
    isLeft: Boolean = false,
    isAtTop: Boolean = false,
): DialogueRenderModel = DialogueRenderModel(
    speaker = speaker,
    visibleText = visibleText,
    portraitId = portraitId,
    isLeft = isLeft,
    isAtTop = isAtTop,
)
