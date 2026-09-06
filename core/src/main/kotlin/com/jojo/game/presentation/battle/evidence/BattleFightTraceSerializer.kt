// Battle
package com.jojo.game.presentation.battle.evidence

import com.jojo.game.presentation.battle.fight.FightActionPose
import com.jojo.game.presentation.battle.fight.FightPresentationState
import com.jojo.game.presentation.battle.fight.FightSide
import com.jojo.game.presentation.battle.fight.FightUnitPresentation
import com.jojo.game.presentation.battle.fight.FightSpriteTimeline
import com.jojo.game.application.runtime.BattleTraceRecorder

/** 전투 연출 추적 직렬화기: 전투 장면의 배경·유닛·대사를 프레임 증거 JSON으로 변환한다. */
internal object BattleFightTraceSerializer {
    /** 직렬화: 활성 전투 연출 상태를 추적 JSON 문자열로 반환한다. */
    fun serialize(active: Boolean, state: FightPresentationState, sprites: FightSpriteTimeline): String {
        if (!active) return "null"
        fun slotUnit(slot: Int): FightUnitPresentation =
            if (state.mineIndex == slot) state.mine else state.enemy
        fun fighter(unit: FightUnitPresentation): String {
            val action = unit.action
            val pose = action?.let { sprites.pose(it, unit.actionElapsedSeconds) } ?: FightActionPose()
            return "[${unit.characterId ?: "null"},${unit.created},${action ?: "null"}," +
                "${number(unit.actionElapsedSeconds)},${number(unit.parentX)},${number(unit.parentScaleX)}," +
                "${number(pose.childX)},${number(pose.childY)},${number(pose.childScaleX)},${number(pose.opacity)}," +
                "${unit.zIndex},${unit.dead}]"
        }
        fun speech(unit: FightUnitPresentation): String {
            val side = if (unit === state.mine) FightSide.MINE else FightSide.ENEMY
            return state.speech(side).let { "[${it.active},\"${escape(it.renderedText)}\"]" }
        }
        val first = slotUnit(0)
        val second = slotUnit(1)
        val introOpacity = if (state.introBackgroundActive) 1f - state.startCrossFade else 0f
        val duelOpacity = if (state.duelBackgroundActive) state.startCrossFade else 0f
        return "{\"mineIndex\":${state.mineIndex},\"enemyIndex\":${state.enemyIndex}," +
            "\"backgrounds\":[[${state.introBackgroundActive},${number(introOpacity)}]," +
            "[${state.duelBackgroundActive},${number(duelOpacity)}]],\"units\":[${fighter(first)},${fighter(second)}]," +
            "\"speeches\":[${speech(first)},${speech(second)}]}"
    }

    private fun number(value: Float): String = BattleTraceRecorder.number(value)
    private fun escape(value: String): String = BattleTraceRecorder.escape(value)
}
