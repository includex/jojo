// Dialogue
package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.graphics.Color
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
    /** 식별자 조회 대신 직접 지정한 초상화다. 화면이 특수 자원을 고를 때 사용한다. */
    val portraitTexture: Texture? = null,
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
    /** 화자명 색과 외곽선 표현이다. 화면별 글꼴이 외곽선을 굽지 않을 때 렌더러가 대신 그린다. */
    val speakerStyle: DialogueSpeakerStyle = DialogueSpeakerStyle(),
    /** 본문 글꼴의 세로 배율이다. 원본 줄 간격에 맞추는 화면별 보정이다. */
    val bodyScaleY: Float = 1f,
    /** 화자명 대신 그릴 원본 래스터다. 있으면 글꼴 대신 이 텍스처를 그린다. */
    val speakerOverlay: DialogueTextureOverlay? = null,
    /** 본문 대신 그릴 원본 래스터다. 있으면 글꼴 대신 이 텍스처를 그린다. */
    val bodyOverlay: DialogueTextureOverlay? = null,
)

/**
 * 화자명 표시 방식이다.
 *
 * 원본 라벨은 채움색과 외곽선을 함께 가진다. 시나리오처럼 글꼴 자체가 외곽선을 구워 두면
 * `outlineColor`를 비워 두고, 전투처럼 외곽선 없는 글꼴을 쓰면 렌더러가 여덟 방향 오프셋으로
 * 같은 두께의 외곽선을 그린다.
 */
data class DialogueSpeakerStyle(
    /** 글자 채움색이다. */
    val fillColor: Color = Color.WHITE,
    /** 외곽선 색이다. null이면 글꼴이 이미 외곽선을 가진 것으로 보고 그리지 않는다. */
    val outlineColor: Color? = null,
    /** 외곽선 두께다. `outlineColor`가 있을 때만 쓴다. */
    val outlineWidth: Float = 0f,
    /** 화자 글꼴의 가로 배율이다. */
    val scaleX: Float = 1f,
    /** 화자 글꼴의 세로 배율이다. */
    val scaleY: Float = 1f,
)

/** 글꼴 대신 그리는 원본 래스터 조각의 위치와 크기다. */
data class DialogueTextureOverlay(
    /** 그릴 텍스처다. 수명은 이 모델을 만든 화면이 소유한다. */
    val texture: Texture,
    /** 왼쪽 아래 X 좌표다. */
    val x: Float,
    /** 왼쪽 아래 Y 좌표다. */
    val y: Float,
    /** 출력 폭이다. */
    val width: Float,
    /** 출력 높이다. */
    val height: Float,
    /** 텍스처에 곱할 색이다. 원본 글리프를 검게 찍을 때 사용한다. */
    val tint: Color = Color.WHITE,
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
    /** 화자명을 그릴 Y 좌표다. `BitmapFont.draw`에 그대로 넘기는 첫 줄 상단 값이다. */
    val speakerDrawY: Float,
    /** 본문 왼쪽 X 좌표다. */
    val textX: Float,
    /** 본문을 그릴 Y 좌표다. `BitmapFont.draw`에 그대로 넘기는 첫 줄 상단 값이다. */
    val textDrawY: Float,
    /** 본문 줄바꿈 폭이다. */
    val textWidth: Float,
    /** 패널 텍스처를 좌우 반전해 그릴지 여부다. 원본은 왼쪽 말풍선에서만 반전한다. */
    val mirrorPanel: Boolean = false,
)

/**
 * 원본 화면의 부분 캡처 단계와 공용 렌더링 단계를 연결한다.
 *
 * 원본 캡처는 노드 가시성을 하나씩 켜며 누적해 찍는다. 따라서 이 단계도 누적이며
 * `PORTRAIT`는 패널과 초상화를, `TEXT`는 네 요소를 모두 그린다. `BACKGROUND`와
 * `CHARACTERS`는 장면까지 포함한 전체 단계이므로 대화창은 전부 그린다.
 */
enum class DialogueRenderStage {
    PANEL,
    PORTRAIT,
    SPEAKER,
    TEXT,
    BACKGROUND,
    CHARACTERS;

    /** 이 단계에서 `target` 구성요소를 그려야 하는지 판단한다. */
    internal fun includes(target: DialogueRenderStage): Boolean {
        val level = CUMULATIVE_ORDER.indexOf(this).takeIf { it >= 0 } ?: CUMULATIVE_ORDER.lastIndex
        return CUMULATIVE_ORDER.indexOf(target) <= level
    }

    internal companion object {
        /** 누적 순서다. 목록에 없는 BACKGROUND/CHARACTERS는 마지막 단계로 취급한다. */
        val CUMULATIVE_ORDER = listOf(PANEL, PORTRAIT, SPEAKER, TEXT)
    }
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
    /**
     * 원본 `ChooseLayer`의 ScrollView가 보여 주고 있는 첫 항목의 인덱스다.
     *
     * 원본 프리팹은 `view`(694×169)에 항목(45 + 간격 4)을 쌓으므로 한 번에 세 개만 보이고
     * 나머지는 스크롤로 닿는다. 포트에는 드래그 스크롤이 없어 이 값으로 창을 옮긴다.
     */
    val firstVisibleIndex: Int = 0,
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
    /** 초상화 상자의 패널 기준 Y 오프셋이다. */
    val portraitOffsetY: Float = -2.15f,
    /** 초상화 출력 폭이다. */
    val portraitWidth: Float = 165.12f,
    /** 초상화 출력 높이다. */
    val portraitHeight: Float = 206.4f,
    /** 왼쪽 초상화 X 좌표다. */
    val portraitLeftX: Float = 84.8199f,
    /** 오른쪽 초상화 X 좌표다. */
    val portraitRightX: Float = 1030.2742f,
    /** 왼쪽 화자 이름 X 좌표다. */
    val speakerLeftX: Float = 349.35056f,
    /** 오른쪽 화자 이름 X 좌표다. */
    val speakerRightX: Float = 365.315f,
    /** 화자 이름 기준선의 패널 Y 오프셋이다. */
    val speakerOffsetY: Float = 147.03f,
    /** 왼쪽 본문 X 좌표다. */
    val textLeftX: Float = 328.93882f,
    /** 오른쪽 본문 X 좌표다. */
    val textRightX: Float = 341.1233f,
    /** 본문 기준선의 패널 Y 오프셋이다. */
    val textOffsetY: Float = 108.03f,
    /** 본문 줄바꿈 폭이다. */
    val textWidth: Float = 626.08f,
    /**
     * 선택지 배치. 원본 `ChooseLayer`의 노드 좌표를 화면 배율에 맞춰 옮긴 값이다.
     * 원본은 대사 말풍선과 같은 `U_select_10-1` 패널을 쓰고, 왼쪽 바깥에 얼굴을 둔다.
     */
    val choicePanelX: Float = 423.70996f,
    /** 선택지 패널의 왼쪽 아래 Y 좌표다. */
    val choicePanelY: Float = 265.009f,
    /** 선택지 패널 폭이다. */
    val choicePanelWidth: Float = 642.42f,
    /** 선택지 패널 높이다. */
    val choicePanelHeight: Float = 157.982f,
    /** 선택지 얼굴의 왼쪽 아래 X 좌표다. */
    val choicePortraitX: Float = 231.07598f,
    /** 선택지 얼굴의 왼쪽 아래 Y 좌표다. */
    val choicePortraitY: Float = 240.21004f,
    /** 선택지 항목 배경의 왼쪽 X 좌표다. */
    val choiceRowX: Float = 463.44196f,
    /** 선택지 항목 배경 폭이다. */
    val choiceRowWidth: Float = 593.916f,
    /** 선택지 항목 배경 높이다. */
    val choiceRowHeight: Float = 38.7f,
    /** 패널 위쪽에서 첫 항목 아래쪽까지의 간격이다. */
    val choiceRowTopInset: Float = 45.881f,
    /** 항목 사이 간격이다. */
    val choiceRowSpacing: Float = 42.14f,
    /** 선택지 본문의 왼쪽 X 좌표다. */
    val choiceTextX: Float = 482.87796f,
    /** 항목 아래쪽에서 본문 글꼴 기준선까지의 간격이다. */
    val choiceTextOffsetY: Float = 38.313f,
    /** 원본 `ChooseLayer`의 `view`(높이 169)에 한 번에 들어가는 항목 수다. */
    val choiceVisibleRows: Int = 3,
)
