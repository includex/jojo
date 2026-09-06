// Battle
package com.jojo.game.application.battle.combat

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.combat.*

/** BattleCombatFacade: 전투 전투 처리 진입점이며, 관련 전투 기능을 묶어 안정적인 호출 경로를 제공한다. */
class BattleCombatFacade internal constructor(private val battle: Battle) {
    /**
     * `tacticalEnvironment` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val tacticalEnvironment by lazy { BattleCombatEnvironmentAssembler.tactical(battle) }
    /**
     * `physicalContext` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val physicalContext by lazy { BattleCombatEnvironmentAssembler.physicalContext(battle) }

    /**
     * `attack`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun attack(attackerId: String, targetId: String, damage: Int? = null): TacticalActionResult =
        BattleTacticalActionExecutor.attack(attackerId, targetId, damage, tacticalEnvironment)

    /**
     * `useProperty`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun useProperty(userId: String, targetId: String, itemId: Int): TacticalActionResult =
        BattleTacticalActionExecutor.useProperty(userId, targetId, itemId, tacticalEnvironment)

    /**
     * `applyProperty`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun applyProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        consume: () -> Boolean,
        notifyPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit,
    ): TacticalActionResult.Item? =
        BattleTacticalActionExecutor.applyProperty(item, target, consume, notifyPermanentProperty)

    /**
     * `forcedAttack`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun forcedAttack(attackerId: String, targetId: String): TacticalActionResult =
        BattleTacticalActionExecutor.forcedAttack(attackerId, targetId, tacticalEnvironment)

    /**
     * `castMagic`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun castMagic(
        attackerId: String,
        targetId: String,
        magicId: Int,
        reaction: Boolean = false,
        bypassCondition: Boolean = false,
    ): TacticalActionResult = BattleTacticalActionExecutor.castMagic(
        attackerId, targetId, magicId, reaction, bypassCondition, tacticalEnvironment,
    )

    /**
     * `castMagicAt`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun castMagicAt(attackerId: String, targetX: Int, targetY: Int, magicId: Int): TacticalActionResult =
        BattleTacticalActionExecutor.castMagicAt(attackerId, targetX, targetY, magicId, tacticalEnvironment)

    /**
     * `physicalDamagePreview`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun physicalDamagePreview(attackerId: String, targetId: String): Int {
        val attacker = battle.units[attackerId] ?: return 0
        val target = battle.units[targetId] ?: return 0
        return PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            BattlePhysicalContextBuilder.basePhysicalDamageContext(
                attacker, target, splash = false, env = physicalContext,
            ),
        )
    }

    /**
     * `physicalContext`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun physicalContext(): BattlePhysicalContextEnvironment = physicalContext
}
