// Battle
package com.jojo.game.application.battle.presentation

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.isEnemySide
import com.jojo.game.domain.battle.isPlayerSide
/**
 * `BattleOutcomeCoordinator` 클래스: presentation 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal class BattleOutcomeCoordinator(
    /**
     * `units` (() -> Collection<BattleUnit>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val units: () -> Collection<BattleUnit>,
    /**
     * `getRound` (() -> Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val getRound: () -> Int,
    /**
     * `enabledFeatures` (() -> Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val enabledFeatures: () -> Int,
    initialMaxRounds: Int = 99,
) {
    /**
     * `maxRounds` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var maxRounds: Int = initialMaxRounds
        private set

    /**
     * `scriptedOutcome` (BattleOutcome?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var scriptedOutcome: BattleOutcome? = null
        private set


    /**
     * `outcome`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun outcome(): BattleOutcome? {
        scriptedOutcome?.let { return it }
        val playerRemaining = units().any { it.effectiveFaction().isPlayerSide() }
        val enemyRemaining = units().any { it.effectiveFaction().isEnemySide() }
        return when {
            getRound() >= maxRounds -> BattleOutcome.ENEMY_VICTORY
            !enemyRemaining && playerRemaining -> BattleOutcome.PLAYER_VICTORY
            !playerRemaining && enemyRemaining -> BattleOutcome.ENEMY_VICTORY
            else -> null
        }
    }
    /**
     * `setMaxRounds`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setMaxRounds(value: Int) {
        val extra = if (enabledFeatures() and ENABLED_FEATURE_ZJHH != 0) 4 else 0
        maxRounds = (value + extra).coerceAtLeast(1)
    }
    /**
     * `setResolvedMaxRounds`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setResolvedMaxRounds(value: Int) {
        maxRounds = value.coerceAtLeast(1)
    }
    /**
     * `setScriptedOutcome`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setScriptedOutcome(value: BattleOutcome) {
        scriptedOutcome = value
    }

    /**
     * `syncScriptedOutcome`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun syncScriptedOutcome(value: BattleOutcome?) {
        value?.let { scriptedOutcome = it }
    }

    companion object {
        /**
         * `ENABLED_FEATURE_ZJHH` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ENABLED_FEATURE_ZJHH = 8
    }
}
