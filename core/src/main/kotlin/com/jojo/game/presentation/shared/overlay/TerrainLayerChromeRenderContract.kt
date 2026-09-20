// Game
package com.jojo.game.presentation.shared.overlay

/**
 * TerrainLayerChromeRenderContract: TerrainLayer 표 본문을 둘러싼 원본 프리팹 장식 영역이다.
 * 실제 TerrainLayer-open 자료의 좌표를 보존하며 입력과 지형 자료에서 독립적으로 렌더링할 수 있다.
 *
 * 이 파일은 `battle-terrain-layer` 경로 기하의 **단일 출처**다. 증거 기록기
 * `TerrainLayerRenderEvents`와 화면 그리기 `BattleTerrainOverlayRenderer`가 모두 여기를 읽으므로,
 * 여기 숫자를 고치면 증거와 그림이 함께 움직인다. 아래 상수의 값은 모두 원본 하네스가 남긴
 * TerrainLayer-open 그리기 기록에서 옮겨 온 것이며, 상수마다 어느 노드에서 온 값인지 적어 둔다.
 * 좌표계는 원본과 같이 왼쪽 아래가 원점이고 y가 위로 증가하며, 상자는 좌하단 기준이다.
 */

object TerrainLayerChromeRenderContract {

    /**
     * `Patch`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Patch(
        /**
         * `path` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val path: String,
        /**
         * `x` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Float,
        /**
         * `y` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Float,
        /**
         * `width` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val width: Float,
        /**
         * `height` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val height: Float,
        /**
         * `capInset` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val capInset: Int,
    )

    /**
     * `Box`: 원본 노드 하나가 차지하는 사각 영역을 좌하단 좌표와 크기로 보관한다.
     * 증거 기록기는 이 값을 그대로 적고, 렌더러는 같은 값으로 그린다.
     */

    data class Box(
        /** `x` (Float): 원본 노드의 좌측 좌표다. */
        val x: Float,
        /** `y` (Float): 원본 노드의 아래쪽 좌표다. */
        val y: Float,
        /** `width` (Float): 원본 노드의 가로 크기다. */
        val width: Float,
        /** `height` (Float): 원본 노드의 세로 크기다. */
        val height: Float,
    ) {
        /** 글자 윗변: 원본 라벨 상자의 위쪽 좌표이며 비트맵 글꼴을 그릴 기준선이다. */
        val top: Float get() = y + height
    }

    /**
     * `Button`: 표 위쪽 병과 제목 칸과 표 아래 단추처럼 배경 상자와 문구 상자가 짝을 이루는 노드다.
     */

    data class Button(
        /** `node` (String): 원본 프리팹의 노드 이름이다. */
        val node: String,
        /** `box` (Box): `Background` 노드가 차지하는 상자다. */
        val box: Box,
        /** `labelBox` (Box): `Background/Label` 노드가 차지하는 상자다. */
        val labelBox: Box,
        /** `text` (String): 원본이 그 자리에 적는 문구다. */
        val text: String,
    )

    /**
     * `PANEL_X` (상태 값): 원본 `Canvas/Layer/bg` 타일 배경의 좌측 좌표다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_X = 274.236f
    /**
     * `PANEL_Y` (상태 값): 원본 `Canvas/Layer/bg` 타일 배경의 아래쪽 좌표다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_Y = 100f
    /**
     * `PANEL_WIDTH` (상태 값): 원본 `Canvas/Layer/bg` 타일 배경의 가로 크기다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_WIDTH = 1021.1f
    /**
     * `PANEL_HEIGHT` (상태 값): 원본 `Canvas/Layer/bg` 타일 배경의 세로 크기다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_HEIGHT = 600f

    /** 가림막: 원본 `Canvas/Layer/Panel_cancel`이 화면 전체를 덮는 상자다. */
    val dimmer = Box(0f, 0f, 1488.372f, 800f)
    /** 가림막 불투명도: 원본 `Panel_cancel`이 남긴 opacity 100/255 값이다. */
    const val DIMMER_OPACITY = 0.392f

    /**
     * `outerBox` (상태 값): 원본 `Canvas/Layer/bg/box1` 테두리가 타일 배경과 같은 상자를 덮는다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val outerBox = Patch("maps/ui/terrain-layer/outer-box.png", PANEL_X, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT, 3)
    /** 제목 띠 아래쪽 좌표: 원본 `Canvas/Layer/bg/bg1` 노드의 y다. */
    const val TITLE_STRIP_Y = 650f
    /** 제목 띠 세로 크기: 원본 `Canvas/Layer/bg/bg1` 노드의 높이다. */
    const val TITLE_STRIP_HEIGHT = 50f
    /**
     * `titleStrip` (상태 값): 원본 `Canvas/Layer/bg/bg1` 제목 띠다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val titleStrip = Patch("maps/ui/terrain-layer/title-strip.png", PANEL_X, TITLE_STRIP_Y, PANEL_WIDTH, TITLE_STRIP_HEIGHT, 5)
    /** 제목 문구 상자: 원본 `Canvas/Layer/bg/bg1/label` 노드의 좌표와 크기다. */
    val titleLabel = Box(282.086f, 649.8f, 229.83f, 50.4f)
    /** 제목 문구: 원본 `bg1/label`이 적는 글자다. */
    const val TITLE_TEXT = "지형 정보 일람"

    /** 안쪽 상자: 원본 `Canvas/Layer/bg/panel` 노드가 표 본문을 감싸는 상자다. */
    val panelBox = Box(285.538f, 183.098f, 1001.1f, 459.3f)

    /** 표에 그려지는 행 수: 원본은 `content` 아래 아홉 칸까지만 뷰포트에 남긴다. */
    const val ROW_COUNT = 9
    /** 아이콘과 병과 값을 함께 그리는 행 수: 아홉 번째 행은 잘려 지형 이름만 남는다. */
    const val ROW_ICON_COUNT = 8
    /** 첫 행 아래쪽 좌표: 원본 `content/item0` 노드의 y다. */
    const val ROW_TOP_Y = 527.398f
    /** 행 높이이자 행 간격: 원본 `content/item0`·`item1` 노드의 높이다. */
    const val ROW_HEIGHT = 75f
    /** 행 배경 좌측 좌표: 원본 `content/item0` 노드의 x다. */
    const val ROW_X = 289.538f
    /** 행 배경 가로 크기: 원본 `content/item0` 노드의 너비다. */
    const val ROW_WIDTH = 993.1f

    /** 행 배경: 위에서 `rowIndex` 번째 행이 차지하는 상자다. */
    fun rowBox(rowIndex: Int) = Box(ROW_X, rowY(rowIndex), ROW_WIDTH, ROW_HEIGHT)

    /** 행 아래쪽 좌표: 첫 행에서 행 높이만큼 아래로 내려간 자리다. */
    fun rowY(rowIndex: Int) = ROW_TOP_Y - rowIndex * ROW_HEIGHT

    /** 짝수 행 아이콘 좌측 좌표: 원본 `content/item0/icon` 노드의 x다. */
    const val ROW_ICON_X_EVEN = 292.488f
    /** 홀수 행 아이콘 좌측 좌표: 원본 `content/item1/icon` 노드의 x다. */
    const val ROW_ICON_X_ODD = 292.919f
    /** 아이콘이 행 바닥에서 떠 있는 높이: 원본 `icon` 노드의 y에서 행 y를 뺀 값이다. */
    const val ROW_ICON_DY = 3.9f
    /** 아이콘 한 변: 원본 `icon` 노드의 가로·세로 크기다. */
    const val ROW_ICON_SIZE = 67.2f

    /** 행 아이콘: 짝수·홀수 행이 각자 다른 좌측 좌표를 쓴다. */
    fun rowIconBox(rowIndex: Int) = Box(
        if (isEvenRow(rowIndex)) ROW_ICON_X_EVEN else ROW_ICON_X_ODD,
        rowY(rowIndex) + ROW_ICON_DY,
        ROW_ICON_SIZE,
        ROW_ICON_SIZE,
    )

    /** 짝수 행 이름 라벨 좌측 좌표: 원본 `content/item0/label` 노드의 x다. */
    const val ROW_NAME_X_EVEN = 376.088f
    /** 홀수 행 이름 라벨 좌측 좌표: 원본 `content/item1/label` 노드의 x다. */
    const val ROW_NAME_X_ODD = 375.651f
    /** 짝수 행 이름 라벨이 행 바닥에서 떠 있는 높이: 원본 `item0/label`의 y에서 행 y를 뺀 값이다. */
    const val ROW_NAME_DY_EVEN = 34.82f
    /** 홀수 행 이름 라벨이 행 바닥에서 떠 있는 높이: 원본 `item1/label`의 y에서 행 y를 뺀 값이다. */
    const val ROW_NAME_DY_ODD = 34.598f
    /** 이름 글자 한 자의 가로 크기: 원본 `label` 너비를 글자 수로 나눈 값이다. */
    const val ROW_NAME_GLYPH_WIDTH = 31.14f
    /** 이름 라벨 세로 크기: 원본 `label` 노드의 높이다. */
    const val ROW_NAME_HEIGHT = 45.36f

    /** 행 이름 라벨: 글자 수에 따라 너비가 늘어난다. */
    fun rowNameBox(rowIndex: Int, length: Int) = Box(
        if (isEvenRow(rowIndex)) ROW_NAME_X_EVEN else ROW_NAME_X_ODD,
        rowY(rowIndex) + if (isEvenRow(rowIndex)) ROW_NAME_DY_EVEN else ROW_NAME_DY_ODD,
        ROW_NAME_GLYPH_WIDTH * length,
        ROW_NAME_HEIGHT,
    )

    /** 특기 표시 첫 칸의 좌측 좌표: 원본 `item/skill/skill_0` 노드의 x다. */
    const val SKILL_X = 369.088f
    /** 특기 표시 칸 간격: 원본 `skill_1`과 `skill_0`의 x 차이다. */
    const val SKILL_STEP = 33f
    /** 특기 표시가 행 바닥에서 떠 있는 높이: 원본 `skill_0`의 y에서 행 y를 뺀 값이다. */
    const val SKILL_DY = 6.5f
    /** 특기 표시 한 변: 원본 `skill_0` 노드의 가로·세로 크기다. */
    const val SKILL_SIZE = 30f

    /** 특기 표시: 행 안에서 왼쪽부터 일정 간격으로 놓인다. */
    fun skillBox(rowIndex: Int, index: Int) = Box(
        SKILL_X + index * SKILL_STEP,
        rowY(rowIndex) + SKILL_DY,
        SKILL_SIZE,
        SKILL_SIZE,
    )

    /** 첫 병과 열의 좌측 좌표: 원본 `item/label0` 노드의 x다. */
    const val VALUE_FIRST_X = 516.463f
    /** 병과 열 간격: 원본 `label1`과 `label0`의 x 차이다. */
    const val VALUE_STEP = 60f
    /** 원본 6..12열은 단순한 60픽셀 간격보다 1픽셀 왼쪽에서 시작한다. */
    const val VALUE_TAIL_FROM = 6
    /** 6..12열 보정: 원본 `label6` 이후가 계산값에서 왼쪽으로 밀린 만큼이다. */
    const val VALUE_TAIL_CORRECTION = -1f
    /** 좁은 글자 보정: 원본이 `○` 한 글자 라벨을 열 안에서 오른쪽으로 밀어 넣은 만큼이다. */
    const val VALUE_NARROW_CORRECTION = 6.525f
    /** 좁은 글자: 원본에서 라벨 너비가 줄어드는 유일한 문구다. */
    const val VALUE_NARROW_TEXT = "○"
    /** 좁은 글자 라벨 가로 크기: 원본 `○` 라벨의 너비다. */
    const val VALUE_NARROW_WIDTH = 30.2f
    /** 보통 라벨 가로 크기: 원본 병과 값 라벨의 너비다. */
    const val VALUE_WIDTH = 43.25f
    /** 병과 값 라벨이 행 바닥에서 떠 있는 높이: 원본 `label0`의 y에서 행 y를 뺀 값이다. */
    const val VALUE_DY = 6f
    /** 병과 값 라벨 세로 크기: 원본 `label0` 노드의 높이다. */
    const val VALUE_HEIGHT = 63f
    /** 홀수 행 일부 라벨의 세로 크기: 원본이 같은 라벨을 0.001만큼 크게 적는 자리다. */
    const val VALUE_HEIGHT_ODD = 63.001f

    /** 병과 값 라벨 좌측 좌표: 열 간격에 꼬리 열 보정과 좁은 글자 보정을 더한 자리다. */
    fun valueX(index: Int, text: String): Float {
        val tailCorrection = if (index >= VALUE_TAIL_FROM) VALUE_TAIL_CORRECTION else 0f
        val glyphCorrection = if (text == VALUE_NARROW_TEXT) VALUE_NARROW_CORRECTION else 0f
        return VALUE_FIRST_X + index * VALUE_STEP + tailCorrection + glyphCorrection
    }

    /** 병과 값 라벨: 열 번호와 문구에 따라 좌표와 크기가 갈린다. */
    fun valueBox(rowIndex: Int, index: Int, text: String): Box {
        val narrow = text == VALUE_NARROW_TEXT
        val odd = !isEvenRow(rowIndex)
        val height = if (odd && (index % 3 != 0 || index == VALUE_ODD_TALL_INDEX)) VALUE_HEIGHT_ODD else VALUE_HEIGHT
        return Box(
            valueX(index, text),
            rowY(rowIndex) + VALUE_DY,
            if (narrow) VALUE_NARROW_WIDTH else VALUE_WIDTH,
            height,
        )
    }

    /** 홀수 행 마지막 열: 세 칸 간격 규칙에서 벗어나 원본이 0.001 큰 높이를 적는 열이다. */
    const val VALUE_ODD_TALL_INDEX = 12

    /** 세로 줄의 좌측 좌표들: 원본 `panel/vline` 노드가 그려진 순서 그대로다. */
    val verticalLineXs = listOf(
        505.588f, 564.788f, 624.288f, 684.788f, 745.288f, 804.788f,
        865.488f, 924.888f, 984.388f, 1044.488f, 1103.888f, 1103.888f, 1164.588f, 1223.588f,
    )
    /** 세로 줄 아래쪽 좌표: 원본 `panel/vline` 노드의 y다. */
    const val VERTICAL_LINE_Y = 189.448f
    /** 세로 줄 가로 크기: 원본 `panel/vline` 노드의 너비다. */
    const val VERTICAL_LINE_WIDTH = 6f
    /** 세로 줄 세로 크기: 원본 `panel/vline` 노드의 높이다. */
    const val VERTICAL_LINE_HEIGHT = 448.6f

    /** 세로 줄: 좌측 좌표만 다르고 나머지는 모두 같다. */
    fun verticalLineBox(x: Float) = Box(x, VERTICAL_LINE_Y, VERTICAL_LINE_WIDTH, VERTICAL_LINE_HEIGHT)

    /** 제목 칸 아래쪽 좌표: 원본 `panel/button… 노드의 Background` 노드 대부분이 쓰는 y다. */
    const val HEADER_Y = 602.183f
    /** 제목 칸 아래쪽 좌표(살짝 위): 원본이 `button`·`button0`·`button11`·`button12`에만 쓰는 y다. */
    const val HEADER_Y_RAISED = 602.358f
    /** 제목 칸 세로 크기: 원본 `button… 노드의 Background` 노드의 높이다. */
    const val HEADER_HEIGHT = 40f
    /** 이름 칸 가로 크기: 원본 `button/Background` 노드의 너비다. */
    const val HEADER_NAME_WIDTH = 223f
    /** 병과 칸 가로 크기: 원본 `button0/Background` 노드의 너비다. */
    const val HEADER_ARM_WIDTH = 60f
    /** 제목 문구 상자 가로 크기: 원본 `Background/Label` 노드의 너비다. */
    const val HEADER_LABEL_WIDTH = 100f
    /** 이름 칸 문구가 칸 안에서 오른쪽으로 들어간 거리: 원본 `button/Background/Label`의 x 차이다. */
    const val HEADER_NAME_LABEL_DX = 61.5f
    /** 병과 칸 문구가 칸 밖으로 나간 거리: 원본 `button… 노드의 Background/Label`의 x 차이다. */
    const val HEADER_ARM_LABEL_DX = -20f

    /** 제목 칸: 원본이 `button`부터 그린 순서 그대로 보관한다. */
    val headers: List<Button> = listOf(
        header("button", 285.588f, HEADER_Y_RAISED, "이름"),
        header("button0", 508.088f, HEADER_Y_RAISED, "마왕"),
        header("button1", 568.397f, HEADER_Y, "보병"),
        header("button2", 628.116f, HEADER_Y, "기병"),
        header("button3", 688.268f, HEADER_Y, "궁기"),
        header("button4", 748.137f, HEADER_Y, "포차"),
        header("button5", 808.101f, HEADER_Y, "무술"),
        header("button11", 1167.145f, HEADER_Y_RAISED, "무술"),
        header("button10", 1107.125f, HEADER_Y, "포차"),
        header("button9", 1047.297f, HEADER_Y, "궁기"),
        header("button8", 987.145f, HEADER_Y, "기병"),
        header("button7", 927.426f, HEADER_Y, "보병"),
        header("button6", 867.443f, HEADER_Y, "군주"),
        header("button12", 1227.088f, HEADER_Y_RAISED, "무술"),
    )

    /** 이름 칸: 표 맨 왼쪽 지형 이름 열의 제목이다. */
    val nameHeader: Button = headers.first { it.node == NAME_HEADER_NODE }

    /** 병과 칸: 왼쪽부터 오른쪽 순서로 다시 세운 제목 칸이다. */
    val armHeaders: List<Button> = headers.filter { it.node != NAME_HEADER_NODE }.sortedBy { it.box.x }

    /** 하단 단추: 원본이 `button0`부터 그린 순서 그대로 보관한다. */
    val footerButtons: List<Button> = listOf(
        Button("button0", Box(285.436f, 110.1f, 196.7f, 61.8f), Box(301.386f, 121f, 164.8f, 40f), "지형 효과"),
        Button("button1", Box(491.436f, 109.4f, 222.7f, 63.2f), Box(498.186f, 116.2f, 209.2f, 49.6f), "기동력 소모"),
        Button("button2", Box(1164.786f, 111f, 120f, 60f), Box(1174.786f, 121f, 100f, 40f), "확인"),
    )

    /** 이식본 비트맵 글꼴의 원본 크기: 배율 1일 때 그려지는 글자 크기다. */
    const val BASE_FONT_SIZE = 26f
    /** 제목 글자 크기: 원본 `bg1/label` 상자 높이 50.4를 글자 높이 비율 1.26으로 나눈 값이다. */
    const val TITLE_FONT_SIZE = 40f
    /** 행 이름 글자 크기: 원본 `item/label` 상자 높이 45.36을 글자 높이 비율 1.26으로 나눈 값이다. */
    const val ROW_NAME_FONT_SIZE = 36f
    /** 병과 값 글자 크기: 원본 `item/label0` 상자 높이 63을 글자 높이 비율 1.26으로 나눈 값이다. */
    const val VALUE_FONT_SIZE = 50f
    /** 제목 칸 글자 크기: 원본 기록의 `Background/Label` 상자가 글자 크기를 담지 않아 이식본 값을 유지한다. */
    const val HEADER_FONT_SIZE = 26f
    /** 하단 단추 글자 크기: 원본 기록의 `Background/Label` 상자가 글자 크기를 담지 않아 이식본 값을 유지한다. */
    const val FOOTER_FONT_SIZE = 36.4f

    /** 지형 창 배경 요소의 표시 순서를 나타낸다. */
    fun chrome(): List<Patch> = listOf(outerBox, titleStrip)

    /** 짝수 행 여부: 원본은 짝수 행에 `item0`, 홀수 행에 `item1` 노드를 쓴다. */
    private fun isEvenRow(rowIndex: Int) = rowIndex % 2 == 0

    /** 이름 칸 노드 이름: 병과 칸과 달리 번호가 붙지 않는다. */
    private const val NAME_HEADER_NODE = "button"

    /** 제목 칸 한 칸: 이름 칸과 병과 칸은 너비와 문구 위치 규칙만 다르다. */
    private fun header(node: String, x: Float, y: Float, text: String): Button {
        val name = node == NAME_HEADER_NODE
        return Button(
            node,
            Box(x, y, if (name) HEADER_NAME_WIDTH else HEADER_ARM_WIDTH, HEADER_HEIGHT),
            Box(x + if (name) HEADER_NAME_LABEL_DX else HEADER_ARM_LABEL_DX, y, HEADER_LABEL_WIDTH, HEADER_HEIGHT),
            text,
        )
    }
}
