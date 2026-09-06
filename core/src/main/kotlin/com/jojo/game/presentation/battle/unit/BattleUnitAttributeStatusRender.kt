// Battle
package com.jojo.game.presentation.battle.unit

import com.jojo.game.domain.battle.*

import com.jojo.game.*
/**
 * `BattleUnitAttributeStatusRender`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object BattleUnitAttributeStatusRender {

    /** Command: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
    data class Command(
        /**
         * `attribute` (BattleAttribute,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attribute: BattleAttribute,
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
         * `size` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val size: Float,
    )

    /**
     * `positions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val positions = listOf(-18 to 16, -18 to 1, -18 to -15, 18 to 16, 18 to 1, 18 to -15)

    /**
     * `commands`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun commands(
        statuses: Map<BattleAttribute, BattleUnitPresentationState.AttributeStatusIcon>,
        otherNodesVisible: Boolean,
        unitLeft: Float,
        unitBottom: Float,
        unitSize: Float,
    ): List<Command> {
        if (!otherNodesVisible) return emptyList()
        /**
         * `scale` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val scale = unitSize / 48f
        /**
         * `size` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val size = 12f * scale
        return BattleAttribute.entries.mapIndexedNotNull { index, attribute ->
            /**
             * `status` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val status = statuses[attribute]?.takeIf { it.active } ?: return@mapIndexedNotNull null
            /**
             * `position` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val position = positions[index]
            Command(
                attribute = attribute,
                textureIndex = if (status.down) 0 else 1,
                x = unitLeft + unitSize / 2f + position.first * scale - size / 2f,
                y = unitBottom + unitSize / 2f + position.second * scale - size / 2f,
                size = size,
            )
        }
    }
}
