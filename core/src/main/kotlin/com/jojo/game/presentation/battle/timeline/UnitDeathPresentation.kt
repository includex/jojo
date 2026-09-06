// Battle
package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.*

import com.jojo.game.*
/**
 * `UnitDeathPresentation`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal object UnitDeathPresentation {

    /**
     * `sortedDying`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun sortedDying(units: Collection<BattleUnit>): List<BattleUnit> = units
        .filter { it.visible && it.hitPoints <= 0 }
        .sortedBy { 100 * it.tileY + it.tileX }


    /**
     * `hideAction`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hideAction(hideType: Int, selfMaster: Boolean): Int = when {
        hideType == 0 -> 47
        hideType == 2 || selfMaster -> 24
        else -> 23
    }
}
