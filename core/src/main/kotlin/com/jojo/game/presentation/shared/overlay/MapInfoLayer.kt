// Game
package com.jojo.game.presentation.shared.overlay

/** MapInfoLayer: 지도 정보창의 타이핑과 취소 생명주기를 관리한다. */
class MapInfoLayer(
    /** `setting` (Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val setting: Int,
    /** `replace` ((String) -> String): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val replace: (String) -> String,
    /** `complete` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val complete: () -> Unit,
    /** `remove` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val remove: () -> Unit
) {

    /**
     * `Data`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Data(
        /**
         * `txt` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val txt: String,
        /**
         * `changePage` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val changePage: Boolean = false,
        /**
         * `wepon` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val wepon: Boolean = false,
        /**
         * `wait` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val wait: Boolean = false
    )


    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(val text: String, val typing: Boolean, val autoCloseDelay: Int?, val attached: Boolean)

    /**
     * `content` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var content = ""
    /**
     * `next` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var next = ""
    /**
     * `typing` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var typing = false
    /**
     * `autoClose` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var autoClose: Int? = null
    /**
     * `once` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var once = false
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var attached = false
    /**
     * `tickRendered` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var tickRendered = false
    /**
     * `data` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var data = Data("")


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(d: Data) {
        attached = true; setData(d)
    }


    /**
     * `setData`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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
     * `tick`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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

    /**
     * `enableAutoClose`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun enableAutoClose() {
        if (setting and AUTO_CLOSE != 0) autoClose = if (data.wait) 5 else 1
    }


    /**
     * `elapse`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun elapse(seconds: Int) {
        if (autoClose != null && seconds >= autoClose!!) next()
    }


    /**
     * `cancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun cancel(event: Int) {
        if (event != TOUCH_END) return; if (typing) {
            content += next; next = ""; typing = false; tickRendered = false; enableAutoClose()
        } else {
            autoClose = null; next()
        }
    }


    /**
     * `skip`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun skip() {
        next()
    }

    /**
     * `next`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun next() {
        if (once) {
            once = false; complete(); remove(); attached = false
        }
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view() =
        View(if (tickRendered && content.contains("<color=")) "$content</c>" else content, typing, autoClose, attached)

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
