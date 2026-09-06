// Battle
package com.jojo.game.application.battle.experience

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.campaign.*

/** BattleExperienceFacade: 전투 경험치 진입점이며, 관련 전투 기능을 묶어 안정적인 호출 경로를 제공한다. */
class BattleExperienceFacade internal constructor(
    configuration: BattleConfiguration,
    /**
     * `journal` (BattleStateJournal,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val journal: BattleStateJournal,
    units: () -> Map<String, BattleUnit>,
) {
    /**
     * `environment` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val environment = BattleExperienceEnvironmentAssembler.build(configuration, journal, units)

    /**
     * `consumeEquipmentUpgrade`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeEquipmentUpgrade(): CampaignEquipmentExperienceResult? =
        journal.consumeEquipmentUpgrade()

    /**
     * `addEquipmentExperience`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addEquipmentExperience(attackerId: String, targetId: String, damage: Int) =
        BattleExperienceCoordinator.addEquipmentExperience(attackerId, targetId, damage, environment)

    /**
     * `notifyPhysicalDamage`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun notifyPhysicalDamage(attacker: BattleUnit, target: BattleUnit, damage: Int) =
        BattleExperienceCoordinator.notifyPhysicalDamage(attacker, target, damage, environment)

    /**
     * `notifyEquipmentExperienceAward`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun notifyEquipmentExperienceAward(
        recipient: BattleUnit,
        opponent: BattleUnit,
        amount: Int,
        kind: BattleEquipmentExperienceKind,
    ) = BattleExperienceCoordinator.notifyEquipmentExperienceAward(recipient, opponent, amount, kind, environment)

    /**
     * `equipmentExperienceAmount`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun equipmentExperienceAmount(
        recipient: BattleUnit,
        opponent: BattleUnit,
        resolvedHarm: Int,
        kind: BattleEquipmentExperienceKind,
    ): Int = BattleExperienceCoordinator.equipmentExperienceAmount(recipient, opponent, resolvedHarm, kind)

    /**
     * `notifyUnitDefeated`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun notifyUnitDefeated(winner: BattleUnit, defeated: BattleUnit) =
        BattleExperienceCoordinator.notifyUnitDefeated(winner, defeated, environment)

    /**
     * `notifyBattleExperience`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun notifyBattleExperience(unit: BattleUnit, amount: Int) =
        BattleExperienceCoordinator.notifyBattleExperience(unit, amount, environment)

    /**
     * `battleExperience`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleExperience(attacker: BattleUnit, target: BattleUnit, defeated: Boolean): Int =
        BattleExperienceCoordinator.battleExperience(attacker, target, defeated, environment.enemyMasterUnitId)

    /**
     * `notifyConsumeAutomaticProperty`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun notifyConsumeAutomaticProperty(itemId: Int, consume: (Int) -> Unit) {
        journal.stageHitSideEffect { consume(itemId) }
    }

    /**
     * `notifyPermanentProperty`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun notifyPermanentProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        apply: (BattlePropertyItem, BattleUnit) -> Unit
    ) {
        journal.stageHitSideEffect { apply(item, target) }
    }

    /**
     * `consumeSelectedProperty`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeSelectedProperty(itemId: Int, consume: (Int) -> Boolean): Boolean {
        if (journal.hasStagedCompletionSideEffects()) {
            journal.stageCompletionSideEffect { consume(itemId); Unit }
            return true
        }
        return consume(itemId)
    }
}
