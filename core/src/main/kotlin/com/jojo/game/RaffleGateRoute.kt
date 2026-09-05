package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Production SettingLayer button-8 contract.  The desktop shell returns a
 * supportAd status code before Global143 may be attached; codes below eight
 * deliberately leave the user on SettingLayer.
 */
/**
 * class  `RaffleGateRoute`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class RaffleGateRoute {
    /**
     * enum class  `Layer`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Layer { HALL, HALL_MENU, SETTING, RAFFLE }

    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(val layer: Layer, val supportAdCode: Int?, val raffleAttached: Boolean, val input: List<String>)

    private var layer = Layer.HALL
    private var supportAdCode: Int? = null
    private val input = mutableListOf<String>()

    /**
     * 공개 메서드 `openHallMenu`
     *
     * ### 파라미터
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun openHallMenu(touchEnd: Boolean) {
        if (touchEnd && layer == Layer.HALL) {
            input += "HallLayer menu TOUCH_END"
            layer = Layer.HALL_MENU
        }
    }

    /**
     * 공개 메서드 `hallMenuButton`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun hallMenuButton(tag: Int, touchEnd: Boolean) {
        if (touchEnd && layer == Layer.HALL_MENU && tag == 3) {
            input += "HallMenuLayer button3 TOUCH_END"
            layer = Layer.SETTING
        }
    }

    /**
     * 공개 메서드 `settingButton`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `touchEnd` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `helperCode` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun settingButton(tag: Int, touchEnd: Boolean, helperCode: Int) {
        if (!touchEnd || layer != Layer.SETTING || tag != 8) return
        input += "SettingLayer button13(tag8) TOUCH_END"
        supportAdCode = helperCode
        if (helperCode >= 8) layer = Layer.RAFFLE
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

    fun view() = View(layer, supportAdCode, layer == Layer.RAFFLE, input.toList())
}

/**
 * object  `RaffleGateRenderEvents`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object RaffleGateRenderEvents {
    private const val PHASE = "hall-raffle-gated-stable"
    private val alpha = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(): String = RenderEventLog().apply {
        /**
         * 공개 메서드 `d`
         *
         * ### 파라미터
        - `layer` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String?=null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String=""`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `opacity` (`Float=1f`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `blend` (`Any=listOf(770,771`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun d(
            layer: String,
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            opacity: Float = 1f,
            blend: Any = listOf(770, 771)
        ) =
            draw(PHASE, layer, path, type, x, y, w, h, asset, opacity, blend, true, text)

        /**
         * 공개 메서드 `e`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String?=null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String=""`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `opacity` (`Float=1f`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `blend` (`Any=listOf(770,771`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun e(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            opacity: Float = 1f,
            blend: Any = listOf(770, 771)
        ) =
            d("SettingLayer", path, type, x, y, w, h, asset, text, opacity, blend)

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float=50.4f`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(path: String, text: String, x: Float, y: Float, w: Float, h: Float = 50.4f) =
            e(path, "label", x, y, w, h, text = text, blend = alpha)

        d(
            "HallLayer",
            "Canvas/Layer/map",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        d(
            "HallLayer",
            "Canvas/Layer/Panel_cancel",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "default_sprite_splash",
            opacity = .118f
        )
        e("Canvas/Layer/bg", "tiled-sprite", 195.686f, 41f, 1097f, 718f, "Logo_9-1")
        e("Canvas/Layer/bg/box1", "tiled-sprite", 195.686f, 41f, 1097f, 718f, "box1")
        e("Canvas/Layer/bg/bg1", "sprite", 195.686f, 709f, 1097f, 50f, "bg1")
        label("Canvas/Layer/bg/bg1/label", "환경 설정", 200.686f, 708.8f, 149.51f)
        e("Canvas/Layer/bg/scrollview", "sliced-sprite", 203.686f, 110f, 1081f, 596f, "box2")
        label(
            "Canvas/Layer/bg/scrollview/view/content/label",
            "항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.",
            65.851f,
            650.189f,
            1078.67f
        )

        val toggles = listOf(
            Triple(floatArrayOf(218.29f, 611f, 252.29f), "배경 음악 듣기", true),
            Triple(floatArrayOf(218.29f, 546f, 252.29f), "효과음 듣기", true),
            Triple(floatArrayOf(218.29f, 482f, 252.186f), "전투 시 전장 축소 이미지가 자동으로 표시됩니다.", true),
            Triple(floatArrayOf(218.186f, 417f, 252.186f), "대화창 자동 닫힘", false),
            Triple(floatArrayOf(218.186f, 353f, 252.186f), "체력 바가 유닛 위에 있습니다", false),
        )
        toggles.forEachIndexed { i, (r, text, checked) ->
            val p = "Canvas/Layer/bg/scrollview/view/content/button$i/toggle"
            e("$p/Background", "sprite", r[0], r[1], 28f, 28f, "default_toggle_normal")
            if (checked) e(
                "$p/checkmark",
                "sprite",
                if (i == 2) 218.186f else r[0],
                r[1],
                28f,
                28f,
                "default_toggle_checkmark"
            )
            label("$p/Label", text, r[2], r[1] - 6f, 526f, 40f)
        }
        /**
         * 공개 메서드 `panel`
         *
         * ### 파라미터
        - `i` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `bx` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `by` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `bw` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `title` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun panel(i: Int, y: Float, bx: Float, by: Float, bw: Float, title: String) {
            val p = "Canvas/Layer/bg/scrollview/view/content/panel$i"; e(
                p,
                "sliced-sprite",
                793.336f,
                y,
                479.7f,
                100f,
                "box1"
            ); e(
                "$p/bg1",
                "sprite",
                if (i == 3) 833.325f else 831.428f,
                by + .2f,
                if (i == 3) 210f else 166f,
                50f,
                "bg1"
            ); label("$p/bg1/label", title, bx, by, bw)
        }

        /**
         * 공개 메서드 `radios`
         *
         * ### 파라미터
        - `i` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `selected` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `values` (`List<String>`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun radios(i: Int, y: Float, selected: Int, values: List<String>) {
            val xs = floatArrayOf(818.346f, 974.286f, 1119.419f)
            val lx = floatArrayOf(855.731f, 1024.671f, 1169.804f); values.forEachIndexed { j, v ->
                val p = "Canvas/Layer/bg/scrollview/view/content/panel$i/toggleContainer/toggle$j"; e(
                "$p/Background",
                "sprite",
                xs[j],
                y,
                32f,
                32f,
                "default_radio_button_off"
            ); if (j == selected) e(
                "$p/checkmark",
                "sprite",
                xs[j],
                y,
                32f,
                32f,
                "default_radio_button_on"
            ); label("$p/Label", v, lx[j], y - 4f, 90f, 40f)
            }
        }
        panel(0, 520.389f, 822.373f, 595.481f, 184.11f, "텍스트 속도"); radios(0, 545.638f, 1, listOf("느림", "중", "빠르게"))
        panel(1, 388.389f, 839.673f, 463.481f, 149.51f, "게임 속도")
        e(
            "Canvas/Layer/bg/scrollview/view/content/panel1/slider/Background",
            "sliced-sprite",
            816.186f,
            418.346f,
            434f,
            20f,
            "default_scrollbar"
        )
        e(
            "Canvas/Layer/bg/scrollview/view/content/panel1/slider/Handle",
            "sliced-sprite",
            800.186f,
            412.346f,
            32f,
            32f,
            "default_radio_button_off"
        )
        panel(2, 256.389f, 839.673f, 331.481f, 149.51f, "정보 설명"); radios(2, 281.638f, 1, listOf("자세히", "보통", "요약"))
        val p = "Canvas/Layer/bg/scrollview/view/content/panel3"
        e(p, "sliced-sprite", 793.336f, 81.389f, 479.7f, 142f, "box1"); e(
        "$p/bg1",
        "sprite",
        833.325f,
        198.167f,
        210f,
        50f,
        "bg1"
    ); label("$p/bg1/label", "대화창 색상", 846.27f, 197.967f, 184.11f)
        floatArrayOf(831.12f, 933.12f, 1035.12f, 1137.12f).forEachIndexed { i, x ->
            val q = "$p/item$i"; e(
            q,
            "sliced-sprite",
            x,
            92.703f,
            100f,
            100f,
            "box1"
        ); e("$q/Logo_9-1", "sprite", x + 2f, 94.703f, 96f, 96f, "Logo_${9 + i}-1"); if (i == 0) e(
            "$q/box6",
            "tiled-sprite",
            x,
            92.703f,
            100f,
            100f,
            "box6"
        )
        }
        e("Canvas/Layer/bg/button1/Background", "sliced-sprite", 1130.186f, 47f, 156f, 56f, "box3")
        label("Canvas/Layer/bg/button1/Background/Label", "확인", 1158.186f, 57.261f, 100f, 40f)
    }.jsonl()
}

/**
 * class  `RaffleGateRouteScreen`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class RaffleGateRouteScreen(private val game: JojoGame) : ScreenAdapter() {
    private val shapes = ShapeRenderer()
    private val route = RaffleGateRoute()
    private var entered = false
    override fun render(delta: Float) {
        if (!entered) {
            route.openHallMenu(true); route.hallMenuButton(3, true); route.settingButton(
                8,
                true,
                0
            ); check(route.view().layer == RaffleGateRoute.Layer.SETTING); entered = true
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color = Color(.25f, .22f, .17f, 1f); shapes.rect(
            0f,
            0f,
            1488.372f,
            800f
        ); shapes.color = Color(.72f, .67f, .55f, 1f); shapes.rect(195.686f, 41f, 1097f, 718f); shapes.end()
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

    fun renderEventLog(): String = RaffleGateRenderEvents.jsonl()
    override fun dispose() {
        shapes.dispose()
    }
}
