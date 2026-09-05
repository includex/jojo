package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20

/**
 * Actual game route: BattleScreen id 1 owns the persistent NoticeInfoLayer id
 * 25, and all state changes enter through its authored touch/NOTICE_MSG API.
 */
/**
 * class  `BattleNoticeRoute`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleNoticeRoute private constructor(val notice: NoticeInfoLayer) {
    val attachedLayerIds = listOf(1, 25)

    companion object {
        /**
         * 공개 메서드 `initialize`
         *
         * ### 파라미터
        - `state` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `BattleNoticeRoute`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun initialize(state: String): BattleNoticeRoute {
            val route = BattleNoticeRoute(NoticeInfoLayer())
            when (state) {
                "notice-hidden" -> Unit
                "notice-shown" -> {
                    route.notice.touch(NoticeInfoLayer.TOUCH_END)
                    route.notice.advance(NoticeInfoLayer.SLIDE_SECONDS)
                }

                "notice-messages" -> {
                    route.notice.touch(NoticeInfoLayer.TOUCH_END)
                    route.notice.advance(NoticeInfoLayer.SLIDE_SECONDS)
                    repeat(52) { route.notice.noticeMessage("알림 ${String.format("%02d", it + 1)}") }
                }

                "notice-hidden-clear" -> {
                    route.notice.touch(NoticeInfoLayer.TOUCH_END)
                    route.notice.advance(NoticeInfoLayer.SLIDE_SECONDS)
                    repeat(3) { route.notice.noticeMessage("알림 ${String.format("%02d", it + 1)}") }
                    route.notice.touch(NoticeInfoLayer.TOUCH_END)
                    route.notice.advance(NoticeInfoLayer.SLIDE_SECONDS)
                }

                else -> error("Unknown NoticeInfo fixture state: $state")
            }
            return route
        }
    }
}

/** Log-only fixture; framebuffer capture remains intentionally disabled. */
class NoticeInfoFixtureScreen(private val game: JojoGame, private val state: String) : ScreenAdapter() {
    private val route = BattleNoticeRoute.initialize(state)

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.writeRenderEventLogIfRequested()
    }

    /**
     * 공개 메서드 `renderEventLog`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun renderEventLog(): String = NoticeInfoBattleRenderEvents.jsonl(state, route)
}

/**
 * object  `NoticeInfoBattleRenderEvents`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object NoticeInfoBattleRenderEvents {
    private data class UnitDraw(val x: Float, val y: Float, val atlas: String, val barWidth: Float, val bar: String)

    private val units = listOf(
        UnitDraw(160f, 768f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(448f, 768f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(832f, 768f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(544f, 672f, "9e/9eebca65-e81b-4ba4-ad61-7ac20d03661c.f1ee0", 88f, "Mark_68-1"),
        UnitDraw(640f, 672f, "9e/9eebca65-e81b-4ba4-ad61-7ac20d03661c.f1ee0", 88f, "Mark_68-1"),
        UnitDraw(160f, 672f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(352f, 672f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(832f, 576f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(544f, 384f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(640f, 384f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(544f, 288f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(640f, 288f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(832f, 288f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(352f, 192f, "3f/3f8fbf89-4dd0-4d0b-88e0-9c7927fe5693.3f9c2", 14.667f, "Mark_3-1"),
        UnitDraw(832f, 192f, "ca/ca6577ee-3ca1-4280-9d60-117070dd2d0b.6ef7f", 14.667f, "Mark_3-1"),
        UnitDraw(256f, 192f, "31/31cc3c95-4d6e-4c10-848f-ef1ca165e78f.850f3", 88f, "Mark_68-1"),
        UnitDraw(640f, 96f, "19/19ac1287-4d09-45f4-bf9a-f5eb8b21795c.89d84", 88f, "Mark_3-1"),
        UnitDraw(832f, 96f, "3f/3f8fbf89-4dd0-4d0b-88e0-9c7927fe5693.3f9c2", 14.667f, "Mark_3-1"),
        UnitDraw(544f, 0f, "19/19ac1287-4d09-45f4-bf9a-f5eb8b21795c.89d84", 88f, "Mark_3-1"),
    )

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `state` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `route` (`BattleNoticeRoute`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(state: String, route: BattleNoticeRoute): String {
        check(route.attachedLayerIds == listOf(1, 25))
        val name = state.removePrefix("notice-")
        val phase = "battle-notice-$name"
        val log = RenderEventLog()

        /**
         * 공개 메서드 `draw`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun draw(path: String, type: String, x: Float, y: Float, w: Float, h: Float, asset: String) =
            log.draw(phase, "HallLayer", path, type, x, y, w, h, asset)
        draw(
            "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>"
        )
        units.forEachIndexed { index, unit ->
            val frame = if (index == 13 && state != "notice-hidden") 151072816 else 33632304
            draw(
                "Canvas/Layer/ScrollView/view/content/map/unit/mask/node", "sprite", unit.x, unit.y, 96f, 96f,
                "assets/Game/native/${unit.atlas}.png#$frame"
            )
            draw(
                "Canvas/Layer/ScrollView/view/content/map/unit/info/bar2/sprite", "sliced-sprite",
                unit.x + 4f, unit.y - 1f, unit.barWidth, 6f, unit.bar
            )
        }
        if (state == "notice-messages" || state == "notice-hidden-clear") {
            draw("Canvas/Layer/ScrollView/view/content/map/New Node", "sprite", 424f, 264f, 48f, 48f, "Mark_10-1")
        }
        draw("Canvas/Layer/menu_button/Background", "sprite", 1353.953f, 8f, 60f, 60f, "menu")
        NoticeInfoRenderer.append(log, name, route.notice.view())
        return log.jsonl()
    }
}
