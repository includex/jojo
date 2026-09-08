// Verification
package com.jojo.game.verification.evidence

/**
 * SourceLabelWidth: 원본 라벨의 글자 폭을 계산한다.
 *
 * 원본 Cocos Label 노드는 글자에 맞춰 크기가 정해지므로, 렌더 이벤트에 남는 폭이
 * 문자열마다 다르다. 이식본이 그 폭을 상수로 적어 두면 값이 바뀌는 라벨(능력치처럼
 * 상태에서 오는 숫자)마다 어긋난다.
 *
 * 아래 값은 본문 크기(높이 50.4) 라벨을 원본 프레임에서 잰 것이다.
 * `이름: `=91.43, `직업:`=80.31, `점수:25`=124.81, `민첩성:40`=159.41, `A`=26.68이
 * 모두 이 표로 재현된다.
 */
internal object SourceLabelWidth {
    /** 한글 한 글자의 폭이다. */
    private const val HANGUL = 34.6f
    /** 숫자 한 글자의 폭이다. */
    private const val DIGIT = 22.25f
    /** 쌍점의 폭이다. */
    private const val COLON = 11.11f
    /** 공백의 폭이다. */
    private const val SPACE = 11.12f
    /** 라틴 대문자 한 글자의 폭이다. */
    private const val LATIN_UPPER = 26.68f

    /**
     * 작은 글꼴 숫자 한 글자의 폭이다.
     *
     * `1024. 공백`(90.21)과 `255. 공백`(79.08)의 차이가 11.13이다. 본문 22.25의 정확한
     * 절반(11.125)이 아니므로 잰 값을 그대로 쓴다.
     */
    private const val SMALL_DIGIT = 11.13f

    /**
     * 판 단추 폭: 본문의 절반 크기 글꼴로 재는다.
     *
     * 유닛 특성 편집 화면의 판 단추 라벨은 본문보다 한 단계 작은 글꼴이고, 잰 값이
     * 정확히 본문의 절반이다(`1024. 공백`=90.21, `255. 공백`=79.08 — 숫자 한 글자
     * 차이가 11.13으로 본문 22.25의 절반이다).
     *
     * 라틴 소문자와 붙임표는 표본이 `FF-End` 하나뿐이라 그 한 값에 맞춘 어림이다.
     * 다른 라틴 문자열이 이 화면에 나오면 다시 재야 한다.
     */
    fun panelButton(text: String): Float = text.sumOf { character ->
        when {
            character == ':' -> (COLON / 2).toDouble()
            character == '.' -> 5.53
            character == ' ' -> (SPACE / 2).toDouble()
            character == '-' -> 6.68
            character.isDigit() -> SMALL_DIGIT.toDouble()
            character in 'A'..'Z' -> (LATIN_UPPER / 2).toDouble()
            character in 'a'..'z' -> 9.99
            else -> (HANGUL / 2).toDouble()
        }
    }.toFloat()

    /** 본문 폭: 문자열을 이루는 글자들의 폭을 더한다. */
    fun body(text: String): Float = text.sumOf { character ->
        when {
            character == ':' -> COLON.toDouble()
            character == ' ' -> SPACE.toDouble()
            character.isDigit() -> DIGIT.toDouble()
            character in 'A'..'Z' -> LATIN_UPPER.toDouble()
            else -> HANGUL.toDouble()
        }
    }.toFloat()
}
