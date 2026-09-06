// Battle
package com.jojo.game.presentation.battle.render

/** 전투 체력 표현: 실제 체력 반영 시각 전후로 화면에 보여 줄 체력값을 유닛별로 보관한다. */
class BattleHealthPresentation {
    /** 체력 전이: 표시 전 체력, 표시 후 체력, 전환 시각을 하나의 예약값으로 보관한다. */
    data class Transition(val fromHp: Int, val toHp: Int, val revealAt: Float)

    /** 유닛별 전이: 아직 화면에 표시해야 하는 체력 변화 예약 목록이다. */
    private val transitions = linkedMapOf<String, Transition>()

    /** 전이 예약: 지정 시각까지 이전 체력을 보이고 이후 새 체력으로 전환하도록 기록한다. */
    fun schedule(unitId: String, fromHp: Int, toHp: Int, revealAt: Float) {
        transitions[unitId] = Transition(fromHp, toHp, revealAt)
    }

    /** 표시 체력: 현재 시각이 예약 전환을 지났는지에 따라 이전·이후 체력 또는 기본 체력을 반환한다. */
    fun shownHp(unitId: String, now: Float, fallbackHp: Int): Int = transitions[unitId]?.let {
        if (now < it.revealAt) it.fromHp else it.toHp
    } ?: fallbackHp
    /** 전이 제거: 유닛 표현이 정리됐을 때 남은 체력 표시 예약을 삭제한다. */
    fun clear(unitId: String) {
        transitions.remove(unitId)
    }
}
