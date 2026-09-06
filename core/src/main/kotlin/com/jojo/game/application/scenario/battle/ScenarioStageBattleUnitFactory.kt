// Scenario
package com.jojo.game.application.scenario.battle

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.campaign.*

/**
 * `ScenarioStageBattleUnitCreation` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class ScenarioStageBattleUnitCreation(
    val battleUnit: ScenarioBattleUnit,
    val initiallyVisible: Boolean,
)

/** ScenarioStageBattleUnitFactory: 스크립트 인수와 유닛 데이터로 전장 배치용 ScenarioBattleUnit을 생성한다. */
internal class ScenarioStageBattleUnitFactory {
    /**
     * `create`: 필요한 객체나 결과를 생성한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun create(
        faction: ScenarioUnitFaction,
        raw: Any?,
        fallbackIndex: Int,
        campaign: CampaignState,
        enemyBlockStart: Int,
    ): ScenarioStageBattleUnitCreation? {
        @Suppress("UNCHECKED_CAST")
        /**
         * `entry` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val entry = raw as? Map<String, Any?> ?: return null
        /**
         * `instanceId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val instanceId = entry["i"].asIntOr(fallbackIndex)
        /**
         * `rosterIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val rosterIndex = entry["idx"].asIntOr(instanceId)
        /**
         * `characterId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val characterId = if (faction == ScenarioUnitFaction.MINE && "id" !in entry) {
            campaign.roster.battleRoster.getOrNull(rosterIndex) ?: return null
        } else entry["id"].asIntOr(instanceId)
        /**
         * `initialAi` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val initialAi = when {
            faction == ScenarioUnitFaction.MINE -> 1
            "ai" in entry -> entry["ai"].asIntOr(0)
            else -> 2
        }
        /**
         * `battleSlot` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val battleSlot = BattleSlotLayout.slotFor(
            faction,
            if (faction == ScenarioUnitFaction.MINE) rosterIndex else instanceId,
            enemyBlockStart,
        )
        /**
         * `hidden` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hidden = entry["hide"].asIntOr(0) != 0
        return ScenarioStageBattleUnitCreation(
            ScenarioBattleUnit(
                instanceId = instanceId,
                characterId = characterId,
                faction = faction,
                x = entry["x"].asIntOr(0),
                y = entry["y"].asIntOr(0),
                authoredX = "x" in entry,
                authoredY = "y" in entry,
                direction = entry["dir"].asIntOr(2),
                level = entry["lv"].asIntOr(0),
                reinforcement = faction == ScenarioUnitFaction.ENEMY && entry["yj"].asIntOr(0) != 0,
                hidden = hidden,
                ai = initialAi,
                aiTargetId = entry["targetId"].asIntOr(-1),
                aiTargetX = entry["targetX"].asIntOr(0),
                aiTargetY = entry["targetY"].asIntOr(0),
                battleSlot = battleSlot,
            ),
            initiallyVisible = !hidden,
        )
    }

    /**
     * `Any`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun Any?.asIntOr(default: Int): Int = when (this) {
        is Number -> toInt()
        is Boolean -> if (this) 1 else 0
        is String -> toIntOrNull() ?: default
        else -> default
    }
}
