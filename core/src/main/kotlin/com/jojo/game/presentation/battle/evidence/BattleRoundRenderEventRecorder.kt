// Battle
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.presentation.battle.overlay.RoundLayer
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** 라운드 화면 증거 입력: 라운드 경로와 현재 오버레이 문구를 함께 보관한다. */
internal data class BattleRoundRenderEventInput(
    val route: RuntimeBattleRoute?,
    val view: RoundLayer.View?,
)

/** 라운드 화면 증거 기록기: 라운드 오버레이의 고정 렌더 이벤트를 JSONL로 구성한다. */
internal object BattleRoundRenderEventRecorder {
    /** 기록: 라운드 경로와 표시 상태를 원본 렌더 순서의 JSONL로 변환한다. */
    fun jsonl(input: BattleRoundRenderEventInput): String {
        val view = input.view ?: return RenderEventLog().jsonl()
        val phase = input.route.evidencePhase()
        val log = RenderEventLog()
        val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

        /**
         * `sprite`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun sprite(
            path: String, x: Float, y: Float, width: Float, height: Float, asset: String,
            opacity: Float = 1f, color: String = NODE_WHITE,
        ) {
            log.draw(
                phase,
                "HallLayer",
                path,
                "sprite",
                x,
                y,
                width,
                height,
                asset,
                opacity,
                listOf(770, 771),
                true,
                "",
                color,
            )
        }

        /**
         * `label`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun label(path: String, text: String, x: Float, y: Float, width: Float, height: Float, color: String) {
            log.draw(phase, "HallLayer", path, "label", x, y, width, height, null, 1f, labels, true, text, color)
        }

        sprite(
            "Canvas/Layer/ScrollView/view/content/map",
            -320f,
            -96f,
            1920f,
            1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>",
        )
        sprite(
            "Canvas/Layer/Panel_cancel", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", 80f / 255f,
            color = SCRIM_BLACK,
        )
        if (view.roundLabelsVisible) {
            label("Canvas/Layer/label02", "아군 단계", 526.713f, 380.09f, 448.54f, 151.2f, SHADOW_RED)
            label("Canvas/Layer/label01", "아군 단계", 519.916f, 385.09f, 448.54f, 151.2f, LABEL_WHITE)
            /**
             * `width` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val width = if (view.roundText == "최종 턴") 344.74f else 274.34f
            /**
             * `shadowX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val shadowX = if (view.roundText == "최종 턴") 578.613f else 613.813f
            label("Canvas/Layer/label12", view.roundText, shadowX, 247.7f, width, 151.2f, ROUND_SHADOW_GREY)
            label("Canvas/Layer/label11", view.roundText, shadowX - 6.797f, 252.7f, width, 151.2f, LABEL_WHITE)
        } else if (view.campLabelsVisible) {
            label("Canvas/Layer/label22", "적군 단계", 526.713f, 319.4f, 448.54f, 151.2f, SHADOW_RED)
            label("Canvas/Layer/label21", "적군 단계", 519.916f, 324.4f, 448.54f, 151.2f, LABEL_WHITE)
        }
        return log.jsonl()
    }

    /**
     * 전장 흐림막: 원본 `RoundLayer` 프리팹
     * (`assets/resources/import/5d/5ddb08c6-7f1b-4d23-8ff3-6066ab4ce3a4.44132.json`)의
     * `Layer/Panel_cancel` `_color` 4278190080 = (0,0,0), `_opacity` 80. 포트도
     * `BattleScreen.drawRoundLayer`가 `Color(0,0,0,80/255)`로 덮는다.
     */
    private const val SCRIM_BLACK = "#000000"

    /**
     * 앞 글자 색: 같은 프리팹의 `label01`·`label11`·`label21`은 `_color`를 갖지 않아
     * 엔진 기본 흰색이다. 포트도 `Color.WHITE`로 그린다.
     */
    private const val LABEL_WHITE = "#ffffff"

    /**
     * 단계 그림자 색: 같은 프리팹의 `label02`·`label22` `_color` 4278190335 = (255,0,0).
     * 포트도 `Color.RED`로 그린다.
     */
    private const val SHADOW_RED = "#ff0000"

    /**
     * 턴 수 그림자 색: 원본 프리팹 `label12`의 `_color` 4286545795 = (131,127,127)이다.
     * 단계 그림자(`label02`/`label22`)만 빨강이고 턴 수 그림자는 따뜻한 회색이다.
     * 포트는 (255,128,128)을 쓰다가 이 값으로 고쳤다. `BattleScreen.ROUND_TURN_SHADOW`와 같다.
     */
    private const val ROUND_SHADOW_GREY = "#837f7f"

    /**
     * 기본 노드 색: 배경 지도 노드에는 색조가 없다.
     * 같은 `nodePath`를 흰색으로 기록하는 `BattleCommandRenderEventRecorder`의 경로가
     * 이미 색 비교를 통과하고 있어 하네스도 흰색을 낸다는 것이 확인돼 있다.
     */
    private const val NODE_WHITE = "#ffffff"

    /** 경로 변환: 라운드 화면의 검증 상태를 산출물 phase 이름으로 고정한다. */
    private fun RuntimeBattleRoute?.evidencePhase(): String = when (this) {
        RuntimeBattleRoute.ROUND_FINAL -> "battle-round-final"
        RuntimeBattleRoute.ROUND_ENEMY -> "battle-round-enemy"
        else -> "battle-round-normal"
    }
}
