// Presentation
package com.jojo.game.presentation.shared.overlay

/** SectionLayer: 시나리오 장·막 제목을 두 단계로 표시하고 자동 진행 또는 입력 완료 콜백을 제어한다. */
class SectionLayer(private val setting: Int) {
    /** 구간 화면을 그리는 데 필요한 상태입니다. */
    data class View(
        /**
         * `label` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val label: String,
        /**
         * `count` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val count: Int,
        /**
         * `scheduled` (List<Int>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scheduled: List<Int>,
        /**
         * `callbacks` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val callbacks: Int,
        /**
         * `attached` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attached: Boolean
    )

    /**
     * `name` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var name = ""
    /**
     * `index` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var index = 0
    /**
     * `count` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var count = 0
    /**
     * `label` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var label = ""
    /**
     * `callbacks` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var callbacks = 0
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = true
    /**
     * `scheduled` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val scheduled = mutableListOf<Int>()
    /**
     * `fn` ((() -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var fn: (() -> Unit)? = null

    /** 구간 이름과 완료 콜백으로 화면을 초기화합니다. */
    fun onCreate(idx: Int, name: String, callback: () -> Unit): View {
        this.index = idx; this.name = name; fn = callback; label =
            if (idx == 0) "서막" else chapter(idx); if (setting and AUTO_CLOSE != 0) scheduled += 3; return view()
    }

    /**
     * `chapter`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun chapter(n: Int): String {
        val r = listOf("십", "일", "2", "삼", "넷", "다섯", "육", "칠", "팔", "구")
        var i = n
        var v = "장막"; while (i > 0) {
            v = r[i % 10] + v; i /= 10
        }; return "제$v"
    }

    /** 터치 종료 입력을 다음 구간 진행으로 변환합니다. */
    fun next(event: Int): View {
        if (event == TOUCH_END) next(); return view()
    }

    /** 구간 진행 횟수를 증가시키고 완료 시 콜백을 실행합니다. */
    fun next() {
        count++; if (count == 1) label = name else if (count == 2) {
            fn?.invoke(); callbacks++; attached = false
        }
    }

    /** 자동 진행 한 번을 실행하고 현재 상태를 반환합니다. */
    fun auto(): View {
        next(); return view()
    }

    /** 남은 구간을 건너뛰고 완료 콜백을 실행합니다. */
    fun skip(): View {
        fn?.invoke(); callbacks++; attached = false; return view()
    }

    /** 현재 구간 화면 상태를 반환합니다. */
    fun view() = View(label, count, scheduled.toList(), callbacks, attached)

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
        /**
         * `AUTO_CLOSE` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val AUTO_CLOSE = 8
    }
}
