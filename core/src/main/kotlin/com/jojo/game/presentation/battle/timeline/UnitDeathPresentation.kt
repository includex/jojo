package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.*

import com.jojo.game.*
/** Pure source rules shared by the live unitDeath/scripted-hide callbacks and focused tests. */
internal object UnitDeathPresentation {
    /**
     * 공개 메서드 `sortedDying`
     *
     * ### 파라미터
    - `units` (`Collection<BattleUnit>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<BattleUnit>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun sortedDying(units: Collection<BattleUnit>): List<BattleUnit> = units
        .filter { it.visible && it.hitPoints <= 0 }
        .sortedBy { 100 * it.tileY + it.tileX }

    /**
     * 공개 메서드 `hideAction`
     *
     * ### 파라미터
    - `hideType` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `selfMaster` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun hideAction(hideType: Int, selfMaster: Boolean): Int = when {
        hideType == 0 -> 47
        hideType == 2 || selfMaster -> 24
        else -> 23
    }
}

