package com.jojo.game.presentation.battle.unit
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.domain.battle.*

import com.jojo.game.*
/** Geometry/order adapter for BattleUnit's dynamically appended `status` child. */
object BattleUnitStateRender {

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
