package com.jojo.game.presentation.shared.overlay

/** 장면 구간 표시와 자동 진행 상태를 관리합니다. */
class SectionLayer(private val setting: Int) {
    /** 구간 화면을 그리는 데 필요한 상태입니다. */
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

    /** 구간 이름과 완료 콜백으로 화면을 초기화합니다. */
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
        const val TOUCH_END = 2
        const val AUTO_CLOSE = 8
    }
}
