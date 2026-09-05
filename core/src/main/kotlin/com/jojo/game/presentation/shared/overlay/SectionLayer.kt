package com.jojo.game.presentation.shared.overlay

/** Direct state implementation of recovered ui/SectionLayer.js. */
class SectionLayer(private val setting: Int) {
    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(
        val label: String,
        val count: Int,
        val scheduled: List<Int>,
        val callbacks: Int,
        val attached: Boolean
    )

    private var name = ""
    private var index = 0
    private var count = 0
    private var label = ""
    private var callbacks = 0
    private var attached = true
    private val scheduled = mutableListOf<Int>()
    private var fn: (() -> Unit)? = null

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `idx` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `callback` (`(`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(idx: Int, name: String, callback: () -> Unit): View {
        this.index = idx; this.name = name; fn = callback; label =
            if (idx == 0) "서막" else chapter(idx); if (setting and AUTO_CLOSE != 0) scheduled += 3; return view()
    }

    private fun chapter(n: Int): String {
        val r = listOf("십", "일", "2", "삼", "넷", "다섯", "육", "칠", "팔", "구")
        var i = n
        var v = "장막"; while (i > 0) {
            v = r[i % 10] + v; i /= 10
        }; return "제$v"
    }

    /**
     * 공개 메서드 `next`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun next(event: Int): View {
        if (event == TOUCH_END) next(); return view()
    }

    /**
     * 공개 메서드 `next`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun next() {
        count++; if (count == 1) label = name else if (count == 2) {
            fn?.invoke(); callbacks++; attached = false
        }
    }

    /**
     * 공개 메서드 `auto`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun auto(): View {
        next(); return view()
    }

    /**
     * 공개 메서드 `skip`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun skip(): View {
        fn?.invoke(); callbacks++; attached = false; return view()
    }

    /**
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view() = View(label, count, scheduled.toList(), callbacks, attached)

    companion object {
        const val TOUCH_END = 2
        const val AUTO_CLOSE = 8
    }
}
