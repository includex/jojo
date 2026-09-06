// Battle
package com.jojo.game.presentation.battle.unit
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.domain.battle.*

import com.jojo.game.*
/**
 * `BattleUnitStateRender`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object BattleUnitStateRender {

    /** Command: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
    data class Command(
        /**
         * `textureIndex` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val textureIndex: Int,
        /**
         * `x` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Float,
        /**
         * `y` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Float,
        /**
         * `width` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val width: Float,
        /**
         * `height` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val height: Float,
        /**
         * `order` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val order: String = "after-unit-info-before-harm-number",
    )

    /**
     * `command`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun command(
        effect: BattleUnitStateAnimation.Effect?,
        secondsSinceCreate: Float,
        unitLeft: Float,
        unitBottom: Float,
        unitSize: Float,
    ): Command? {
        if (effect == null || !effect.active) return null
        /**
         * `sample` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sample = effect.sampleAt(secondsSinceCreate)
        /**
         * `scale` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scale = unitSize / 48f
        /**
         * `iconSize` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val iconSize = 16f * scale
        /**
         * `centreX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val centreX = unitLeft + unitSize / 2f + sample.position.first * scale
        /**
         * `centreY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val centreY = unitBottom + unitSize / 2f + sample.position.second * scale
        return Command(sample.textureIndex, centreX - iconSize / 2f, centreY - iconSize / 2f, iconSize, iconSize)
    }


    /**
     * `jsonl`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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
