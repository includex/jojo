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

        fun sprite(path: String, x: Float, y: Float, width: Float, height: Float, asset: String, opacity: Float = 1f) {
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
            )
        }

        fun label(path: String, text: String, x: Float, y: Float, width: Float, height: Float) {
            log.draw(phase, "HallLayer", path, "label", x, y, width, height, null, 1f, labels, true, text)
        }

        sprite(
            "Canvas/Layer/ScrollView/view/content/map",
            -320f,
            -96f,
            1920f,
            1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>",
        )
        sprite("Canvas/Layer/Panel_cancel", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", 80f / 255f)
        if (view.roundLabelsVisible) {
            label("Canvas/Layer/label02", "아군 단계", 526.713f, 380.09f, 448.54f, 151.2f)
            label("Canvas/Layer/label01", "아군 단계", 519.916f, 385.09f, 448.54f, 151.2f)
            val width = if (view.roundText == "최종 턴") 344.74f else 274.34f
            val shadowX = if (view.roundText == "최종 턴") 578.613f else 613.813f
            label("Canvas/Layer/label12", view.roundText, shadowX, 247.7f, width, 151.2f)
            label("Canvas/Layer/label11", view.roundText, shadowX - 6.797f, 252.7f, width, 151.2f)
        } else if (view.campLabelsVisible) {
            label("Canvas/Layer/label22", "적군 단계", 526.713f, 319.4f, 448.54f, 151.2f)
            label("Canvas/Layer/label21", "적군 단계", 519.916f, 324.4f, 448.54f, 151.2f)
        }
        return log.jsonl()
    }

    /** 경로 변환: 라운드 화면의 검증 상태를 산출물 phase 이름으로 고정한다. */
    private fun RuntimeBattleRoute?.evidencePhase(): String = when (this) {
        RuntimeBattleRoute.ROUND_FINAL -> "battle-round-final"
        RuntimeBattleRoute.ROUND_ENEMY -> "battle-round-enemy"
        else -> "battle-round-normal"
    }
}
