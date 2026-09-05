package com.jojo.game

/** Source-faithful typing/cancel lifecycle of recovered ui/MapInfoLayer.js. */
class MapInfoLayer(
    private val setting: Int,
    private val replace: (String) -> String,
    private val complete: () -> Unit,
    private val remove: () -> Unit
) {
    /**
     * data class  `Data`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Data(
        val txt: String,
        val changePage: Boolean = false,
        val wepon: Boolean = false,
        val wait: Boolean = false
    )

    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(val text: String, val typing: Boolean, val autoCloseDelay: Int?, val attached: Boolean)

    private var content = ""
    private var next = ""
    private var typing = false
    private var autoClose: Int? = null
    private var once = false
    private var attached = false
    private var tickRendered = false
    private var data = Data("")

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `d` (`Data`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(d: Data) {
        attached = true; setData(d)
    }

    /**
     * 공개 메서드 `setData`
     *
     * ### 파라미터
    - `d` (`Data`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setData(d: Data) {
        once = true; data = d
        val text = replace(d.txt)
        val prefix = if (d.changePage) {
            content = ""; ""
        } else if (d.wepon && content.isNotEmpty()) "<br/>" else ""; next = prefix + text; typing = true; tickRendered =
            false; autoClose = null
    }

    /**
     * 공개 메서드 `tick`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun tick() {
        if (!typing) return
        var nesting = 0; while (next.isNotEmpty()) {
            val c = next[0]; next =
                next.drop(1); content += c; if (c == '<') nesting++ else if (nesting > 0 && c == '>') nesting--; if (nesting == 0) break
        }; tickRendered = true; if (next.isEmpty()) {
            typing = false; enableAutoClose()
        }
    }

    private fun enableAutoClose() {
        if (setting and AUTO_CLOSE != 0) autoClose = if (data.wait) 5 else 1
    }

    /**
     * 공개 메서드 `elapse`
     *
     * ### 파라미터
    - `seconds` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun elapse(seconds: Int) {
        if (autoClose != null && seconds >= autoClose!!) next()
    }

    /**
     * 공개 메서드 `cancel`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun cancel(event: Int) {
        if (event != TOUCH_END) return; if (typing) {
            content += next; next = ""; typing = false; tickRendered = false; enableAutoClose()
        } else {
            autoClose = null; next()
        }
    }

    /**
     * 공개 메서드 `skip`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun skip() {
        next()
    }

    private fun next() {
        if (once) {
            once = false; complete(); remove(); attached = false
        }
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

    fun view() =
        View(if (tickRendered && content.contains("<color=")) "$content</c>" else content, typing, autoClose, attached)

    companion object {
        const val TOUCH_END = 2
        const val AUTO_CLOSE = 8
    }
}
