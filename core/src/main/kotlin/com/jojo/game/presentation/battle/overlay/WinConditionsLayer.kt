// Game
package com.jojo.game.presentation.battle.overlay

/** WinConditionsLayer: 승리 조건 화면의 서식 텍스트와 취소 동작을 관리한다. */
class WinConditionsLayer {

    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(val first: String, val second: String, val attached: Boolean)

    /**
     * `done` ((() -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var done: (() -> Unit)? = null
    /**
     * `v` (View?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var v: View? = null


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(text: String, round: Int, onClose: () -> Unit): View {
        done = onClose
        val t = text.replaceFirst(
            "\n",
            "<br/>"
        ); return View(
            "<b><color=#ff0000>승리 조건</c><br/><color=#777777>$t<br/>제한 턴 수 $round</c></b>",
            "<b><color=#FFFFFF>승리 조건</c><br/><color=#FFFFFF>$t<br/>제한 턴 수 $round</c></b>",
            true
        ).also { v = it }
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(): View = v ?: View("", "", false)

    /**
     * `childLabels`: 원본 `cc.RichText`가 만드는 `RICHTEXT_CHILD` 라벨의 문구를 순서대로 준다.
     *
     * 서식 태그를 걷어내고 `<br/>`로 끊으면 원본이 자식 라벨 하나씩에 넣는 문구와 같다.
     * 그리기 쪽 `BattleScreen.drawScriptWinConditions`와 증거 쪽이 **같은 함수**를 읽는다.
     * 앞서는 증거가 `listOf("승리 조건", "장보와 장량을", "격퇴하십시오.", …)`로 영천 전투의
     * 문구를 박아 두어, 다른 전투에서 이 화면을 찍어도 영천의 문구를 적었을 것이다.
     */
    fun childLabels(): List<String> = childSegments(view().second).map { it.second }

    // 원본 콜백에는 중복 호출 방지가 없다. 일반적으로 화면 제거 뒤 입력 전달이
    // 멈춰도 직접 두 번째 종료 입력을 주면 fn()을 호출하는 계약을 유지한다.

    fun cancel(event: Int): Boolean {
        if (event != TOUCH_END) return false; done?.invoke(); v = v?.copy(attached = false); return true
    }

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2

        /** `cc.RichText` 서식 태그. 원본은 `<b>`·`<color=..>`·`</c>`를 쓴다. */
        private val TAG = Regex("<[^>]+>")

        /** `<color=#rrggbb>` 태그. 원본은 대문자와 소문자를 섞어 쓴다. */
        private val COLOR_TAG = Regex("<color=#([0-9a-fA-F]{6})>")

        /** 색 태그가 없는 조각은 `cc.Label` 기본 흰색이다. */
        private const val DEFAULT_COLOR = "#ffffff"

        /**
         * `childColors`: 같은 자식 라벨들의 글자색을 순서대로 준다.
         *
         * 원본 `cc.RichText`는 `<color=#rrggbb>` 태그를 만난 자리부터 그 색을 쓰고, 닫힐 때까지
         * 이어진다. 그림자 막(`first`)은 제목이 빨강이고 본문이 `#777777`, 위 막(`second`)은
         * 전부 흰색이다 — 색은 `onCreate`가 만든 서식 문자열 안에 이미 들어 있으므로 따로
         * 적어 둘 값이 아니다.
         */
        fun childColors(text: String): List<String> = childSegments(text).map { it.first }

        /** 서식 문자열을 `<br/>` 단위로 끊고 각 조각의 색과 문구를 뽑는다. */
        fun childSegments(text: String): List<Pair<String, String>> {
            var color = DEFAULT_COLOR
            return text.split("<br/>").map { segment ->
                COLOR_TAG.find(segment)?.let { color = "#" + it.groupValues[1].lowercase() }
                color to segment.replace(TAG, "")
            }
        }
    }
}
