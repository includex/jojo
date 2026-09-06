// Presentation
package com.jojo.game.presentation.shared.overlay

/** HelperLayer: 캠페인 안내 정보를 색상 리치 텍스트로 조합해 스크롤 가능한 도움말 화면에 표시한다. */

class HelperLayer(private val model: Model, private val removeFromParent: () -> Unit = {}) {

    /**
     * `Info`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Info(val type: Int, val reserved: String = "", val text: String)


    /**
     * `Model`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    interface Model {

        /**
         * `getInfo`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun getInfo(): Iterable<Info>


        /**
         * `replaceSpeInfo`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun replaceSpeInfo(text: String, flags: Int): String
    }


    /**
     * `Prefab`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Prefab(
        /**
         * `root` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val root: String = "HelperLayer", val background: String = "Logo_12-1",
        /**
         * `canvasWidth` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val canvasWidth: Float = 1280f, val canvasHeight: Float = 800f,
        /**
         * `backgroundWidth` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val backgroundWidth: Float = 1193f, val backgroundHeight: Float = 751f,
        /**
         * `scrollPath` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scrollPath: String = "Logo_12-1/scrollview", val scrollWidth: Float = 1161f, val scrollHeight: Float = 616f,
        /**
         * `richTextPath` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val richTextPath: String = "Logo_12-1/scrollview/view/content/richtext",
        /**
         * `richTextMaxWidth` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val richTextMaxWidth: Int = 1157, val richTextLineHeight: Int = 50,
        /**
         * `buttonPath` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val buttonPath: String = "Logo_12-1/button0", val buttonWidth: Float = 147.6f, val buttonHeight: Float = 56f,
        /**
         * `buttonLocalX` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val buttonLocalX: Float = 502.065f, val buttonLocalY: Float = -339.813f,
        /**
         * `buttonText` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val buttonText: String = "확인", val listenerPriority: Int = 1,
    )


    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(
        /**
         * `prefab` (Prefab,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val prefab: Prefab,
        /**
         * `richText` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val richText: String,
        /**
         * `attached` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attached: Boolean,
        /**
         * `blocksUnderlyingInput` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val blocksUnderlyingInput: Boolean = true
    )

    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = false
    /**
     * `richText` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var richText = ""

    /** onCreate: 정보 항목을 색상 태그와 줄바꿈으로 조합하고 원본 특수 표기를 치환해 화면을 연다. */
    fun onCreate(): View {
        val rows = buildString {
            for ((type, _, text) in model.getInfo()) {
                when (type) {
                    1 -> append("<color=#000000>").append(text).append("</color>")
                    2 -> append("<color=#ff0000>").append(text).append("</color>")
                    3 -> append("<color=#0000ff>").append(text).append("</color>")
                    4 -> append("<color=#f000f0>").append(text).append("</color>")
                }
                // 원본 JS는 알 수 없는 유형에도 줄바꿈을 추가한다.
                append("<br/>")
            }
        }
        richText = model.replaceSpeInfo(rows, REPLACE_FLAGS)
        attached = true
        return view()
    }

    /** onButtonTouch: 확인 버튼의 터치 종료를 받으면 도움말 레이어를 제거하고 분리 상태를 갱신한다. */
    fun onButtonTouch(eventType: Int): View {
        if (eventType == TOUCH_END) {
            removeFromParent(); attached = false
        }
        return view()
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view() = View(PREFAB, richText, attached)

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
        /**
         * `REPLACE_FLAGS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val REPLACE_FLAGS = 15
        /**
         * `PREFAB` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val PREFAB = Prefab()
    }
}

/** SourceInfoText: 원본 안내 문구의 유닛·전역 변수·색상 표기를 화면용 리치 텍스트로 치환한다. */
object SourceInfoText {
    /**
     * `replace`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun replace(
        input: String,
        flags: Int = 15,
        unitName: (Int) -> String = { "" },
        global: (Int) -> Int = { 0 },
        colors: List<String> = emptyList(),
    ): String {
        /**
         * `text` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var text = input.trim()
        if (text.isEmpty()) return text
        if (flags and 1 != 0) {
            Regex("&\\*[.+](\\d+)\\n").findAll(text).toList()
                .forEach { text = text.replace(it.value, "&${it.groupValues[1]}\n") }
            Regex("\\*[.+](\\d+)").findAll(text).toList()
                .forEach { text = text.replace(it.value, unitName(it.groupValues[1].toInt())) }
        }
        if (flags and 2 != 0) {
            Regex("\\*/(\\d+)").findAll(text).toList()
                .forEach { text = text.replace(it.value, global(it.groupValues[1].toInt()).toString()) }
            Regex("\\*(\\d+)").findAll(text).toList()
                .forEach { text = text.replace(it.value, global(it.groupValues[1].toInt()).toString()) }
        }
        if (flags and 4 != 0) text = text.replace("\n", "<br/>")
        if (flags and 8 != 0) {
            /**
             * `pattern` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val pattern = Regex("\\[C(\\w\\w)(.*?)\\]")
            while (true) {
                /**
                 * `m` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val m = pattern.find(text) ?: break
                /**
                 * `color` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val color = colors.getOrNull(m.groupValues[1].toInt(16)) ?: ""
                text = text.replace(m.value, "<color=$color>${m.groupValues[2]}</color>")
            }
        }
        return text
    }
}
