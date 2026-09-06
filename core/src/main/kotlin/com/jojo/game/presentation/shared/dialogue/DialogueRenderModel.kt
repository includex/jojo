// Dialogue
package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch

/** 대화·선택지·모달을 한 화면에서 조합하기 위한 공용 표시 모델이다. */
data class DialogueOverlayModel(
    /** 현재 표시할 대사 정보이며, 없으면 대사 창을 그리지 않는다. */
    val dialogue: DialogueRenderModel? = null,
    /** 현재 표시할 선택지 정보이며, 없으면 선택지 창을 그리지 않는다. */
    val choice: ChoiceRenderModel? = null,
    /** 현재 표시할 모달 정보이며, 없으면 모달 배경과 본문을 그리지 않는다. */
    val modal: ModalRenderModel? = null,
)

/** 화자·본문·초상화·대사창 위치를 화면 중립적으로 표현하는 모델이다. */
data class DialogueRenderModel(
    /** 대사창에 표시할 화자 이름이다. */
    val speaker: String,
    /** 글자 공개 효과가 반영된 현재 대사 본문이다. */
    val visibleText: String,
    /** 화자 초상화 식별자이며, 없으면 초상화를 생략한다. */
    val portraitId: Int? = null,
    /** 화자와 대사창이 화면 왼쪽에 배치되는지 여부다. */
    val isLeft: Boolean = false,
    /** 대사창이 화면 위쪽에 배치되는지 여부다. */
    val isAtTop: Boolean = false,
    /** 캡처용 부분 렌더링 단계이며, 일반 화면에서는 null이다. */
    val componentStage: DialogueRenderStage? = null,
    /** 화면별 카메라 계산으로 결정된 패널 X 좌표이며, 없으면 레이아웃 기본값을 사용한다. */
    val panelXOverride: Float? = null,
    /** 화면별 화자 유닛 위치로 결정된 패널 Y 좌표이며, 없으면 레이아웃 기본값을 사용한다. */
    val panelYOverride: Float? = null,
    /** 패널과 초상화·화자·본문을 같은 화자 기준점으로 옮기는 상세 배치값이다. */
    val componentPlacement: DialogueComponentPlacement? = null,
)

/** 대화창 구성 요소의 절대 좌표: 화자 추적 대화가 패널과 모든 자식을 함께 이동시키는 계약이다. */
data class DialogueComponentPlacement(
    /** 대화 패널의 왼쪽 아래 좌표다. */
    val panelX: Float,
    /** 대화 패널의 왼쪽 아래 좌표다. */
    val panelY: Float,
    /** 대화 패널 폭이다. */
    val panelWidth: Float,
    /** 대화 패널 높이다. */
    val panelHeight: Float,
    /** 초상화의 왼쪽 아래 X 좌표다. */
    val portraitX: Float,
    /** 초상화의 왼쪽 아래 Y 좌표다. */
    val portraitY: Float,
    /** 초상화 폭이다. */
    val portraitWidth: Float,
    /** 초상화 높이다. */
    val portraitHeight: Float,
    /** 화자명 왼쪽 X 좌표다. */
    val speakerX: Float,
    /** 화자명 기준선 Y 좌표다. */
    val speakerBaselineY: Float,
    /** 본문 왼쪽 X 좌표다. */
    val textX: Float,
    /** 본문 기준선 Y 좌표다. */
    val textBaselineY: Float,
    /** 본문 줄바꿈 폭이다. */
    val textWidth: Float,
)

/** 원본 화면의 부분 캡처 단계와 공용 렌더링 단계를 연결한다. */
enum class DialogueRenderStage {
    PANEL,
    PORTRAIT,
    SPEAKER,
    TEXT,
    BACKGROUND,
    CHARACTERS,
}

/** 선택지 제목·항목·현재 선택 위치를 공용 표시 모델로 전달한다. */
data class ChoiceRenderModel(
    /** 선택지 창의 제목이다. */
    val title: String = "전술 선택",
    /** 사용자에게 표시할 선택지 항목이다. */
    val options: List<String>,
    /** 강조할 항목의 인덱스이며, 범위를 벗어나면 강조하지 않는다. */
    val selectedIndex: Int = -1,
    /** 선택지에 함께 표시할 초상화 식별자다. */
    val portraitId: Int? = null,
    /** 선택지 대신 단순 확인 상자를 표시하는지 여부다. */
    val isConfirmation: Boolean = false,
)

/** 종류별 배경·본문 표시 규칙을 유지하는 공용 모달 모델이다. */
data class ModalRenderModel(
    /** 모달의 화면 의미를 결정하는 종류다. */
    val kind: DialogueModalKind,
    /** 모달에 표시할 원문이다. */
    val text: String,
    /** 타이핑 효과가 적용된 현재 표시 문자열이다. */
    val visibleText: String = text,
    /** 지도 정보처럼 본문 앞에 고정되는 문자열이다. */
    val fixedText: String = "",
)

/** 대화 렌더러가 요구하는 자원 포트다. 화면별 자산 보관 객체는 이 계약으로 어댑트한다. */
interface DialogueRenderAssets {
    /** 대사창 배경 텍스처다. */
    val dialoguePanel: Texture?

    /** 선택지 전체 배경 텍스처다. */
    val choicePanel: Texture?

    /** 선택지 한 행의 배경 텍스처다. */
    val choiceRow: Texture?

    /** 정보 모달을 나인 패치로 그릴 자원이다. */
    val infoPanel: NinePatch?

    /** 대사 본문에 사용하는 글꼴이다. */
    val bodyFont: BitmapFont

    /** 화자 이름에 사용하는 글꼴이다. */
    val speakerFont: BitmapFont

    /** 선택지·모달 제목에 사용하는 글꼴이다. */
    val titleFont: BitmapFont

    /** 식별자에 대응하는 초상화를 반환한다. */
    fun portrait(portraitId: Int): Texture?
}

/** 원본 좌표를 화면별 자산 어댑터와 분리하기 위한 공용 배치 설정이다. */
data class DialogueRenderLayout(
    /** 렌더링 논리 화면의 폭이다. */
    val width: Float = 1280f,
    /** 렌더링 논리 화면의 높이다. */
    val height: Float = 688f,
    /** 대사창의 왼쪽 기준 X 좌표다. */
    val panelLeftX: Float = 274.54054f,
    /** 대사창의 오른쪽 기준 X 좌표다. */
    val panelRightX: Float = 316.40878f,
    /** 대사창의 기본 Y 좌표다. */
    val panelY: Float = 55.47f,
    /** 위쪽 대사창에 더하는 Y 오프셋이다. */
    val topOffsetY: Float = 373.24f,
    /** 대사창 출력 폭이다. */
    val panelWidth: Float = 686.28f,
    /** 대사창 출력 높이다. */
    val panelHeight: Float = 164.26f,
    /** 초상화 출력 폭이다. */
    val portraitWidth: Float = 165.12f,
    /** 초상화 출력 높이다. */
    val portraitHeight: Float = 206.4f,
    /** 왼쪽 초상화 X 좌표다. */
    val portraitLeftX: Float = 84.8199f,
    /** 오른쪽 초상화 X 좌표다. */
    val portraitRightX: Float = 1030.2742f,
    /** 왼쪽 화자 이름 X 좌표다. */
    val speakerLeftX: Float = 323.44676f,
    /** 오른쪽 화자 이름 X 좌표다. */
    val speakerRightX: Float = 365.315f,
    /** 화자 이름 기준선의 패널 Y 오프셋이다. */
    val speakerOffsetY: Float = 147.03f,
    /** 왼쪽 본문 X 좌표다. */
    val textLeftX: Float = 328.93882f,
    /** 오른쪽 본문 X 좌표다. */
    val textRightX: Float = 370.80706f,
    /** 본문 기준선의 패널 Y 오프셋이다. */
    val textOffsetY: Float = 108.03f,
    /** 본문 줄바꿈 폭이다. */
    val textWidth: Float = 626.08f,
)
