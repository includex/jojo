package com.jojo.game
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/**
 * Source-faithful state owned by Global117 NoticeInfoLayer.
 *
 * The real layer is persistent for the lifetime of BattleScreen. NOTICE_MSG is
 * deliberately ignored while hidden, and hiding returns every live row to
 * the node pool. The first pooled prefab is retained as a cloning seed, which
 * explains the source pool counts represented here.
 */
/**
 * class  `NoticeInfoLayer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class NoticeInfoLayer {
    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /** Authored button listener reacts only to UILayer TOUCH_END (event 2). */
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

    /** Queued Manager event delivery calls this listener on a later update. */
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
     * 공개 메서드 `advance`
     *
     * ### 파라미터
    - `seconds` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun advance(seconds: Float) {
        if (!sliding) return
        slideElapsed = (slideElapsed + seconds.coerceAtLeast(0f)).coerceAtMost(SLIDE_SECONDS)
        val progress = slideElapsed / SLIDE_SECONDS
        // cc.easeQuarticActionOut(): 1 - (1-t)^4
        val eased = 1f - (1f - progress) * (1f - progress) * (1f - progress) * (1f - progress)
        bgY = slideStartY + (slideTargetY - slideStartY) * eased
        if (slideElapsed >= SLIDE_SECONDS) {
            bgY = slideTargetY
            sliding = false
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

    fun view() = View(shown, sliding, bgY, messages.toList(), poolSize)

    companion object {
        const val TOUCH_END = 2
        const val SLIDE_SECONDS = .6f
        const val MAX_MESSAGES = 50
        const val HIDDEN_Y = -600f
        const val SHOWN_Y = -200f
    }
}

/** A single actual draw submission produced by [NoticeInfoRenderer]. */
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

/**
 * Stable-endpoint NoticeInfo composition shared by BattleScreen drawing and
 * its render-event logger. Commands remain renderer-independent so the live
 * SpriteBatch path and JSONL path consume the same geometry and ordering.
 */
/**
 * object  `NoticeInfoRenderer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object NoticeInfoRenderer {
    private const val PATH = "Canvas/Layer/bg"

    /**
     * 공개 메서드 `commands`
     *
     * ### 파라미터
    - `view` (`NoticeInfoLayer.View`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<NoticeDrawCommand>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun commands(view: NoticeInfoLayer.View): List<NoticeDrawCommand> = buildList {
        if (view.shown) {
            add(NoticeDrawCommand(PATH, "tiled-sprite", 0f, 0f, 491f, 400f, "bg2"))
            // Cocos ScrollView positions the newest ten 40.76px rows inside
            // its 392px viewport. The final separator lies outside the clip.
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
     * 공개 메서드 `append`
     *
     * ### 파라미터
    - `log` (`RenderEventLog`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `route` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `view` (`NoticeInfoLayer.View`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `route` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `view` (`NoticeInfoLayer.View`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(route: String, view: NoticeInfoLayer.View): String =
        RenderEventLog().also { append(it, route, view) }.jsonl()

    private const val ROW_HEIGHT = 40.76f
    private const val VISIBLE_ROWS = 10
}
