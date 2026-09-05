package com.jojo.game.verification.title.evidence

import com.jojo.game.presentation.shared.evidence.RenderEventLog
import com.jojo.game.verification.title.StartItemRenderEvents
import com.jojo.game.presentation.title.TitleMode
import com.jojo.game.presentation.title.TitleViewState

/** Builds title verification events from the same immutable snapshot used for drawing. */
internal class TitleRenderEventRecorder {
    /**
     * 공개 메서드 `record`
     *
     * ### 파라미터
    - `state` (`TitleViewState`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `startItemFixture` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun record(state: TitleViewState, startItemFixture: Boolean = false): String {
        if (startItemFixture) return StartItemRenderEvents.jsonl()
        val log = RenderEventLog()
        log.draw(
            "login-main-stable", "HallLayer", "Canvas/bg", "sprite", 0f, 0f, 1280f, 688f,
            "assets/resources/native/4d/4debf9ca-54d9-48e2-855c-34ef06c80bc4.5e28d.jpg#Logo_1-1"
        )
        if (state.optionalOverlayRoute == null) log.draw(
            "login-main-stable", "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1280f, 688f,
            "default_sprite_splash", opacity = 0f, visible = false,
        )
        floatArrayOf(582f, 456f, 329f, 203f).forEachIndexed { index, y ->
            log.draw(
                "login-main-stable", "Login", "Canvas/Layer/bg1/button$index/Background", "sliced-sprite",
                945.46f, y * SCALE - 37.84f, 302.72f, 75.68f, "U_select_12-1_$index"
            )
        }
        if (state.mode != TitleMode.LOGIN) log.draw(
            "overlay", state.mode.name, "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1280f, 688f,
            "default_sprite_splash", opacity = if (state.mode == TitleMode.LOAD) .392f else .118f,
        )
        when (state.mode) {
            TitleMode.LOGIN -> Unit
            TitleMode.LOAD -> appendLoad(log, state)
            TitleMode.SETTING -> appendSettings(log, state)
        }
        state.optionalOverlayRoute?.let { LoginOptionalOverlayRenderEvents.append(log, it) }
        return log.jsonl()
    }

    private fun appendLoad(log: RenderEventLog, state: TitleViewState) {
        fun event(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", visible: Boolean = true,
            opacity: Float = 1f, layer: String = "LoadGameLayer"
        ) =
            log.draw(
                "login-load-stable", layer, path, type, x * SCALE, y * SCALE, w * SCALE, h * SCALE,
                asset, opacity = opacity, blend = if (type == "label") LABEL_BLEND else SPRITE_BLEND,
                visible = visible, text = text
            )

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float = 50.4f`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `visible` (`Boolean = true`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(path: String, text: String, x: Float, y: Float, w: Float, h: Float = 50.4f, visible: Boolean = true) =
            event(path, "label", x, y, w, h, text = text, visible = visible)
        event("Canvas/Layer/bg1", "tiled-sprite", 278.186f, 97.5f, 932f, 605f, "Logo_9-1")
        event("Canvas/Layer/bg1/box2", "sliced-sprite", 278.186f, 97.5f, 932f, 605f, "box3")
        event("Canvas/Layer/bg1/bg1", "sprite", 278.186f, 652.5f, 932f, 50f, "bg1")
        label("Canvas/Layer/bg1/bg1/label", "진행도 불러오기", 283.186f, 652.3f, 253.31f)
        label("Canvas/Layer/bg1/label", "읽을 진행 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다.", 286.785f, 603.763f, 1102.16f)
        event("Canvas/Layer/bg1/box2", "sliced-sprite", 287.186f, 174f, 912f, 428f, "box2")
        state.loadRows.take(22).forEachIndexed { index, row ->
            val y = 549f - index * 52f
            val visible = index < 12
            val path = "Canvas/Layer/bg1/box2/scrollview/view/content/item"
            event(path, "sprite", 289.186f, y, 908f, 50f, "885a69b4-08ed-4c78-8896-ffb04eb2bd20", visible = visible)
            label("$path/label0", row.number, 295.933f, y - .2f, 117.85f, visible = visible)
            label("$path/label1", row.stage, 426.886f, y - .2f, 137.6f, visible = visible)
            label("$path/label2", row.name, 578.186f, y, 615f, 50f, visible)
        }
        event("Canvas/Layer/bg1/box2/vline", "sliced-sprite", 418.75f, 176.1f, 6f, 423.8f, "vline")
        event("Canvas/Layer/bg1/box2/vline", "sliced-sprite", 564.515f, 176.1f, 6f, 423.8f, "vline")
        event("Canvas/Layer/bg1/button0/Background", "sliced-sprite", 1051.386f, 107f, 147.6f, 60f, "box3")
        label("Canvas/Layer/bg1/button0/Background/Label", "취소", 1075.186f, 119.764f, 100f, 40f)
        state.loadConfirmationMessage?.let { message ->
            event(
                "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash",
                visible = false, opacity = 0f, layer = "HallLayer"
            )
            event("Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
            event("Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
            event("Canvas/Layer/bg0/Logo_3-1", "sprite", 453.005f, 373.951f, 106f, 124f, "Logo_3-1")
            label("Canvas/Layer/bg0/label", message, 573.686f, 335f, 463f, 190f)
            event("Canvas/Layer/bg0/btns/button1/Background", "sliced-sprite", 554.186f, 271.285f, 180f, 50f, "box3")
            label("Canvas/Layer/bg0/btns/button1/Background/Label", "취소", 557.336f, 279.085f, 168.1f, 40f)
            event("Canvas/Layer/bg0/btns/button0/Background", "sliced-sprite", 754.186f, 271.285f, 180f, 50f, "box3")
            label("Canvas/Layer/bg0/btns/button0/Background/Label", "불러오기", 757.586f, 279.085f, 169.4f, 40f)
        }
    }

    private fun appendSettings(log: RenderEventLog, state: TitleViewState) {
        val view = requireNotNull(state.settings)
        fun event(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", opacity: Float = 1f
        ) =
            log.draw(
                "login-setting-stable", "SettingLayer", path, type, x * SCALE, y * SCALE, w * SCALE, h * SCALE,
                asset, opacity = opacity, blend = if (type == "label") LABEL_BLEND else SPRITE_BLEND, text = text
            )

        /**
         * 공개 메서드 `label`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float = 50.4f`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun label(path: String, text: String, x: Float, y: Float, w: Float, h: Float = 50.4f) =
            event(path, "label", x, y, w, h, text = text)
        event("Canvas/Layer/bg", "tiled-sprite", 195.686f, 41f, 1097f, 718f, "Logo_9-1")
        event("Canvas/Layer/bg/box1", "tiled-sprite", 195.686f, 41f, 1097f, 718f, "box1")
        event("Canvas/Layer/bg/bg1", "sprite", 195.686f, 709f, 1097f, 50f, "bg1")
        label("Canvas/Layer/bg/bg1/label", "환경 설정", 200.686f, 708.8f, 149.51f)
        event("Canvas/Layer/bg/scrollview", "sliced-sprite", 203.686f, 110f, 1081f, 596f, "box2")
        label(
            "Canvas/Layer/bg/scrollview/view/content/label",
            "항목을 클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.",
            65.851f,
            650.189f,
            1078.67f
        )
        val rects = listOf(218.29f to 611f, 218.29f to 546f, 218.186f to 482f, 218.186f to 417f, 218.186f to 353f)
        val labels = listOf("배경 음악 듣기", "효과음 듣기", "전투 시 전장 축소 이미지가 자동으로 표시됩니다.", "대화창 자동 닫힘", "체력 바가 유닛 위에 있습니다")
        rects.forEachIndexed { index, (x, y) ->
            val path = "Canvas/Layer/bg/scrollview/view/content/button$index/toggle"
            event("$path/Background", "sprite", x, y, 28f, 28f, "default_toggle_normal")
            if (view.flags and (1 shl index) != 0) event(
                "$path/checkmark",
                "sprite",
                if (index == 2) 218.186f else x,
                y,
                28f,
                28f,
                "default_toggle_checkmark"
            )
            label("$path/Label", labels[index], if (index < 2) 252.29f else 252.186f, y - 6f, 526f, 40f)
        }
        /**
         * 공개 메서드 `panel`
         *
         * ### 파라미터
        - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `titleX` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `titleY` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `titleW` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `title` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun panel(index: Int, y: Float, titleX: Float, titleY: Float, titleW: Float, title: String) {
            val path = "Canvas/Layer/bg/scrollview/view/content/panel$index"
            event(path, "sliced-sprite", 793.336f, y, 479.7f, 100f, "box1")
            event("$path/bg1", "sprite", 831.428f, titleY + .2f, 166f, 50f, "bg1")
            label("$path/bg1/label", title, titleX, titleY, titleW)
        }

        /**
         * 공개 메서드 `radios`
         *
         * ### 파라미터
        - `panel` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `selected` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `values` (`List<String>`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun radios(panel: Int, y: Float, selected: Int, values: List<String>) {
            val xs = floatArrayOf(818.346f, 974.286f, 1119.419f)
            val labelXs = floatArrayOf(855.731f, 1024.671f, 1169.804f)
            values.forEachIndexed { index, text ->
                val path = "Canvas/Layer/bg/scrollview/view/content/panel$panel/toggleContainer/toggle$index"
                event("$path/Background", "sprite", xs[index], y, 32f, 32f, "default_radio_button_off")
                if (index == selected) event(
                    "$path/checkmark",
                    "sprite",
                    xs[index],
                    y,
                    32f,
                    32f,
                    "default_radio_button_on"
                )
                label("$path/Label", text, labelXs[index], y - 4f, 90f, 40f)
            }
        }
        panel(0, 520.389f, 822.373f, 595.481f, 184.11f, "텍스트 속도")
        radios(0, 545.638f, view.messageSpeed, listOf("느림", "중", "빠르게"))
        panel(1, 388.389f, 839.673f, 463.481f, 149.51f, "게임 속도")
        event(
            "Canvas/Layer/bg/scrollview/view/content/panel1/slider/Background",
            "sliced-sprite",
            816.186f,
            418.346f,
            434f,
            20f,
            "default_scrollbar"
        )
        event(
            "Canvas/Layer/bg/scrollview/view/content/panel1/slider/Handle",
            "sliced-sprite",
            800.186f + 434f * view.gameSpeed,
            412.346f,
            32f,
            32f,
            "default_radio_button_off"
        )
        panel(2, 256.389f, 839.673f, 331.481f, 149.51f, "정보 설명")
        radios(2, 281.638f, view.notificationLevel, listOf("자세히", "보통", "요약"))
        val panel3 = "Canvas/Layer/bg/scrollview/view/content/panel3"
        event(panel3, "sliced-sprite", 793.336f, 81.389f, 479.7f, 142f, "box1")
        event("$panel3/bg1", "sprite", 833.325f, 198.167f, 210f, 50f, "bg1")
        label("$panel3/bg1/label", "대화창 색상", 846.27f, 197.967f, 184.11f)
        floatArrayOf(831.12f, 933.12f, 1035.12f, 1137.12f).forEachIndexed { index, x ->
            val path = "$panel3/item$index"
            event(path, "sliced-sprite", x, 92.703f, 100f, 100f, "box1")
            event("$path/Logo_9-1", "sprite", x + 2f, 94.703f, 96f, 96f, "Logo_${9 + index}-1")
            if (view.background == index) event(
                "$path/box6", "tiled-sprite", x, 92.703f, 100f, 100f, "box6",
                opacity = if (state.optionalOverlayRoute != null) 1f else .333f
            )
        }
        event("Canvas/Layer/bg/button1/Background", "sliced-sprite", 1130.186f, 47f, 156f, 56f, "box3")
        label("Canvas/Layer/bg/button1/Background/Label", "확인", 1158.186f, 57.261f, 100f, 40f)
    }

    private companion object {
        const val SCALE = .86f
        val SPRITE_BLEND = listOf(770, 771)
        val LABEL_BLEND = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
    }
}
