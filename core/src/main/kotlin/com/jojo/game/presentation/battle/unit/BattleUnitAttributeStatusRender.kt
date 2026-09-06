package com.jojo.game.presentation.battle.unit

import com.jojo.game.domain.battle.*

import com.jojo.game.*
/** Geometry adapter for BattleUnit prefab status/unit_status_0..5. */
object BattleUnitAttributeStatusRender {

    data class Command(
        val attribute: BattleAttribute,
        val textureIndex: Int,
        val x: Float,
        val y: Float,
        val size: Float,
    )

    private val positions = listOf(-18 to 16, -18 to 1, -18 to -15, 18 to 16, 18 to 1, 18 to -15)

    fun commands(
        statuses: Map<BattleAttribute, BattleUnitPresentationState.AttributeStatusIcon>,
        otherNodesVisible: Boolean,
        unitLeft: Float,
        unitBottom: Float,
        unitSize: Float,
    ): List<Command> {
        if (!otherNodesVisible) return emptyList()
        val scale = unitSize / 48f
        val size = 12f * scale
        return BattleAttribute.entries.mapIndexedNotNull { index, attribute ->
            val status = statuses[attribute]?.takeIf { it.active } ?: return@mapIndexedNotNull null
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
