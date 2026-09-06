// Battle
package com.jojo.game.presentation.battle.timeline
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.battle.*

import com.jojo.game.*

/**
 * `BattleMagicPresentation`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object BattleMagicPresentation {
    /**
     * `Change`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Change(val unitId: String, val hpAdd: Int = 0, val mpAdd: Int = 0)

    /**
     * `changes`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun changes(
        result: TacticalActionResult.Magic,
        casterId: String,
        magic: GameDataCatalog.MagicProfile?,
    ): List<Change> = changes(result.targets, casterId, magic)

    /**
     * `changes`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun changes(
        targets: List<MagicTarget>,
        casterId: String,
        magic: GameDataCatalog.MagicProfile?,
    ): List<Change> {
        /**
         * `values` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val values = linkedMapOf<String, Change>()


        /**
         * `add`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun add(id: String, hp: Int = 0, mp: Int = 0) {
            val old = values[id] ?: Change(id)
            values[id] = old.copy(hpAdd = old.hpAdd + hp, mpAdd = old.mpAdd + mp)
        }
        targets.forEach { target ->
            add(target.targetId, hp = target.healing - target.damage)
            when {
                target.magicDrain > 0 -> {
                    add(target.targetId, mp = -target.magicDrain)
                    add(casterId, mp = target.magicRecovery)
                }
                magic?.type == 20 && magic.category == 24 && target.magicRecovery > 0 ->
                    add(casterId, mp = target.magicRecovery)

                target.magicRecovery > 0 -> add(target.targetId, mp = target.magicRecovery)
            }
            if (target.casterHealing > 0) add(casterId, hp = target.casterHealing)
        }
        return values.values.filter { it.hpAdd != 0 || it.mpAdd != 0 }
    }
}
