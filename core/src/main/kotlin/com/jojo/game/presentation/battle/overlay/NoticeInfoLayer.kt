// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 전투 알림을 큐에 보관하고 열림·닫힘 슬라이드와 표시 행 수를 관리한다. */

class NoticeInfoLayer {

    /** 알림 패널의 표시 위치, 슬라이드 상태, 현재 메시지 목록을 노출한다. */
    data class View(
        /**
         * `shown` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val shown: Boolean,
        /**
         * `sliding` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sliding: Boolean,
        /**
         * `bgY` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val bgY: Float,
        /**
         * `messages` (List<String>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val messages: List<String>,
        /**
         * `poolSize` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val poolSize: Int,
    )

    /**
     * `messages` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val messages = ArrayDeque<String>()
    /**
     * `poolSize` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var poolSize = 1
    /**
     * `slideElapsed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var slideElapsed = 0f
    /**
     * `slideStartY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var slideStartY = HIDDEN_Y
    /**
     * `slideTargetY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var slideTargetY = HIDDEN_Y

    /**
     * `shown` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var shown: Boolean = false
        private set
    /**
     * `sliding` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var sliding: Boolean = false
        private set
    /**
     * `bgY` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var bgY: Float = HIDDEN_Y
        private set

    /** TOUCH_END 입력으로 패널을 열거나 닫고 닫힘 시 메시지 큐를 비운다. */
    fun touch(eventType: Int): Boolean {
        if (eventType != TOUCH_END) return false
        slideStartY = bgY
        shown = !shown
        slideTargetY = if (shown) SHOWN_Y else HIDDEN_Y
        slideElapsed = 0f
        sliding = true
        if (!shown) {
            poolSize += messages.size
            messages.clear()
        }
        return true
    }
    /**
     * `noticeMessage`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun noticeMessage(text: String): Boolean {
        if (!shown) return false
        check(poolSize > 0) { "NoticeInfoLayer lost its prefab cloning seed" }
        poolSize--
        if (poolSize < 1) poolSize++ // cc.instantiate(row), then put clone
        messages += text
        if (messages.size > MAX_MESSAGES) {
            messages.removeFirst()
            poolSize++
        }
        return true
    }


    /**
     * `advance`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun advance(seconds: Float) {
        if (!sliding) return
        slideElapsed = (slideElapsed + seconds.coerceAtLeast(0f)).coerceAtMost(SLIDE_SECONDS)
        val progress = slideElapsed / SLIDE_SECONDS
        val eased = 1f - (1f - progress) * (1f - progress) * (1f - progress) * (1f - progress)
        bgY = slideStartY + (slideTargetY - slideStartY) * eased
        if (slideElapsed >= SLIDE_SECONDS) {
            bgY = slideTargetY
            sliding = false
        }
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view() = View(shown, sliding, bgY, messages.toList(), poolSize)

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
        /**
         * `SLIDE_SECONDS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SLIDE_SECONDS = .6f
        /**
         * `MAX_MESSAGES` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MAX_MESSAGES = 50
        /**
         * `HIDDEN_Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val HIDDEN_Y = -600f
        /**
         * `SHOWN_Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SHOWN_Y = -200f
    }
}
/**
 * `NoticeDrawCommand`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class NoticeDrawCommand(
    val path: String,
    val type: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val asset: String? = null,
    val text: String = "",
)

/** 알림 패널의 배경·메시지 행·슬라이드 위치를 렌더 이벤트로 직렬화한다. */

object NoticeInfoRenderer {
    /**
     * `PATH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val PATH = "Canvas/Layer/bg"


    /**
     * `commands`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun commands(view: NoticeInfoLayer.View): List<NoticeDrawCommand> = buildList {
        if (view.shown) {
            add(NoticeDrawCommand(PATH, "tiled-sprite", 0f, 0f, 491f, 400f, "bg2"))
            view.messages.takeLast(VISIBLE_ROWS).forEachIndexed { index, text ->
                val y = -7.6f + ROW_HEIGHT * index
                add(
                    NoticeDrawCommand(
                        "$PATH/scrollview/view/content/item/lab",
                        "label",
                        2f,
                        y,
                        487f,
                        32.76f,
                        text = text
                    )
                )
                if (index < VISIBLE_ROWS - 1 && index < view.messages.takeLast(VISIBLE_ROWS).lastIndex) {
                    add(
                        NoticeDrawCommand(
                            "$PATH/scrollview/view/content/item/vline2",
                            "sprite",
                            2f,
                            y + 38.76f,
                            487f,
                            2f,
                            "vline2"
                        )
                    )
                }
            }
        }
        val buttonY = if (view.shown) 400.731f else .731f
        add(NoticeDrawCommand("$PATH/button/Background", "sliced-sprite", .843f, buttonY, 68f, 68f, "bg1"))
        add(NoticeDrawCommand("$PATH/button/Background/tool11", "sprite", .043f, buttonY - .8f, 69.6f, 69.6f, "tool10"))
    }


    /**
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, route: String, view: NoticeInfoLayer.View) {
        val phase = "battle-notice-$route"
        commands(view).forEach { command ->
            log.draw(
                phase = phase,
                layer = "NoticeInfoLayer",
                nodePath = command.path,
                drawType = command.type,
                x = command.x,
                y = command.y,
                w = command.width,
                h = command.height,
                assetId = command.asset,
                blend = if (command.type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
                text = command.text,
            )
        }
    }


    /**
     * `jsonl`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun jsonl(route: String, view: NoticeInfoLayer.View): String =
        RenderEventLog().also { append(it, route, view) }.jsonl()

    /**
     * `ROW_HEIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val ROW_HEIGHT = 40.76f
    /**
     * `VISIBLE_ROWS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val VISIBLE_ROWS = 10
}
