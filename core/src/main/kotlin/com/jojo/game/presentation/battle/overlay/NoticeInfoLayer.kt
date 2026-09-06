// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 전투 알림을 큐에 보관하고 열림·닫힘 슬라이드와 표시 행 수를 관리한다. */

class NoticeInfoLayer {

    /** 알림 패널의 표시 위치, 슬라이드 상태, 현재 메시지 목록을 노출한다. */
    data class View(
        val shown: Boolean,
        val sliding: Boolean,
        val bgY: Float,
        val messages: List<String>,
        val poolSize: Int,
    )

    private val messages = ArrayDeque<String>()
    private var poolSize = 1
    private var slideElapsed = 0f
    private var slideStartY = HIDDEN_Y
    private var slideTargetY = HIDDEN_Y

    var shown: Boolean = false
        private set
    var sliding: Boolean = false
        private set
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


    fun view() = View(shown, sliding, bgY, messages.toList(), poolSize)

    companion object {
        const val TOUCH_END = 2
        const val SLIDE_SECONDS = .6f
        const val MAX_MESSAGES = 50
        const val HIDDEN_Y = -600f
        const val SHOWN_Y = -200f
    }
}
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
    private const val PATH = "Canvas/Layer/bg"


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


    fun jsonl(route: String, view: NoticeInfoLayer.View): String =
        RenderEventLog().also { append(it, route, view) }.jsonl()

    private const val ROW_HEIGHT = 40.76f
    private const val VISIBLE_ROWS = 10
}
