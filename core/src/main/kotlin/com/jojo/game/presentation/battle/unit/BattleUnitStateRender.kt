package com.jojo.game.presentation.battle.unit

import com.jojo.game.*
/** Geometry/order adapter for BattleUnit's dynamically appended `status` child. */
object BattleUnitStateRender {
    /**
     * data class  `Command`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Command(
        val textureIndex: Int,
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val order: String = "after-unit-info-before-harm-number",
    )

    fun command(
        effect: BattleUnitStateAnimation.Effect?,
        secondsSinceCreate: Float,
        unitLeft: Float,
        unitBottom: Float,
        unitSize: Float,
    ): Command? {
        if (effect == null || !effect.active) return null
        val sample = effect.sampleAt(secondsSinceCreate)
        val scale = unitSize / 48f
        val iconSize = 16f * scale
        val centreX = unitLeft + unitSize / 2f + sample.position.first * scale
        val centreY = unitBottom + unitSize / 2f + sample.position.second * scale
        return Command(sample.textureIndex, centreX - iconSize / 2f, centreY - iconSize / 2f, iconSize, iconSize)
    }

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `command` (`Command`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Int = 0`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(command: Command, frame: Int = 0): String = RenderEventLog(frame).also { log ->
        log.draw(
            "battle-state",
            "BattleScreen",
            "Canvas/Layer/ScrollView/view/content/map/unit/status",
            "sprite",
            command.x,
            command.y,
            command.width,
            command.height,
            "maps/ui/battle-status/state_${command.textureIndex}.png",
        )
    }.jsonl()
}

