// Battle
package com.jojo.game.presentation.battle.overlay

/**
 * `CocosLabelBaseline`: 원본 엔진이 `cc.Label`(system font, ttf) 글자를 노드 안 어디에 놓는지를 옮긴 것이다.
 *
 * 원본 구현은 `cocos-engine-web/cocos2d/core/renderer/utils/label/ttf.js`의
 * `_calculateSize`(Overflow.NONE)와 `_calculateFillTextStartPosition`이며,
 * 비율 상수는 `cocos2d/core/utils/text-utils.js`에 있다.
 *
 * 프리팹이 저장하는 것은 라벨 **노드**의 위치와 크기뿐이다. 글자는 그 안에서
 * 여기 규칙대로 놓이므로, 포팅에서도 노드 좌표 → baseline 변환을 이 규칙으로 해야 한다.
 */
object CocosLabelBaseline {
    /** text-utils.js `_BASELINE_RATIO = 0.26`. */
    const val BASELINE_RATIO = 0.26f

    /**
     * text-utils.js `_BASELINE_OFFSET`: `CC_RUNTIME` 빌드에서만 `BASELINE_RATIO * 2 / 3`이고
     * 그 밖에는 0이다. 대조 대상인 sgccz-desktop은 web 엔진을 Electron에 얹은 빌드라 0이다.
     */
    const val BASELINE_OFFSET = 0f

    /**
     * `nodeHeight`: ttf.js `_calculateSize`의 Overflow.NONE 분기가 정하는 노드 높이다.
     *
     * `rawHeight = (줄 수 + BASELINE_RATIO) * lineHeight`이고, `cc.LabelOutline`이 붙어 있으면
     * `_contentSizeExtend.height = outlineWidth * 2`가 더 붙는다.
     * 프리팹 값으로 확인된다: 외곽선 2인 라벨은 54.4, 외곽선이 없는 라벨은 50.4다(lineHeight 40).
     */
    fun nodeHeight(lineHeight: Float, outlineWidth: Float, lines: Int = 1): Float =
        (lines + BASELINE_RATIO) * lineHeight + 2f * outlineWidth

    /**
     * `baselineFromTop`: ttf.js `_calculateFillTextStartPosition`이 돌려주는 첫 줄 baseline이다.
     * 캔버스(= 노드) 위쪽에서 아래로 잰 값이며 `VerticalTextAlignment.CENTER` 기준이다.
     */
    fun baselineFromTop(fontSize: Float, lineHeight: Float, outlineWidth: Float, lines: Int = 1): Float {
        val canvasHeight = nodeHeight(lineHeight, outlineWidth, lines)
        val drawStartY = lineHeight * (lines - 1)
        var y = fontSize * (1f - BASELINE_RATIO / 2f)
        // CENTER: 남는 세로 여백을 위아래로 반씩 나눈다.
        val blank = drawStartY + 2f * outlineWidth + fontSize - canvasHeight
        y -= blank / 2f
        y += BASELINE_OFFSET * fontSize
        // `_canvasPadding.y`: 외곽선 폭만큼 위쪽 여백이 있다.
        return y + outlineWidth
    }

    /**
     * `baselineFromBottom`: 라벨 노드 아래 모서리에서 위로 잰 첫 줄 baseline이다.
     * 프리팹 좌표가 아래에서 위로 가는 포팅 쪽 좌표계에 바로 쓸 수 있다.
     */
    fun baselineFromBottom(fontSize: Float, lineHeight: Float, outlineWidth: Float, lines: Int = 1): Float =
        nodeHeight(lineHeight, outlineWidth, lines) - baselineFromTop(fontSize, lineHeight, outlineWidth, lines)
}
