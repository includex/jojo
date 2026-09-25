// Battle
package com.jojo.game.application.battle.combat

import com.jojo.game.presentation.i18n.GameText

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.magic.MagicEnvironment
import com.jojo.game.domain.battle.magic.MagicResolver
import com.jojo.game.domain.battle.combat.*
import com.jojo.game.domain.battle.BattlePropertyResolver

/**
 * `BattleTacticalActionEnvironment` 클래스: combat 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class BattleTacticalActionEnvironment(
    val outcome: () -> BattleOutcome?,
    val units: () -> Map<String, BattleUnit>,
    val activeFaction: () -> Faction,
    val areAllied: (BattleUnit, BattleUnit) -> Boolean,
    val movementOffsets: Set<Pair<Int, Int>>,
    val propertyItems: Map<Int, BattlePropertyItem>,
    val consumeSelectedProperty: (Int) -> Boolean,
    val notifyPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit,
    val physicalCombatEnvironment: () -> PhysicalCombatEnvironment,
    val magicEnvironment: () -> MagicEnvironment,
)
/**
 * `BattleTacticalActionExecutor` 싱글턴 객체: combat 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object BattleTacticalActionExecutor {

    /**
     * `attack`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun attack(
        attackerId: String,
        targetId: String,
        damage: Int? = null,
        env: BattleTacticalActionEnvironment,
    ): TacticalActionResult {
        if (env.outcome() != null) return TacticalActionResult.Rejected(GameText.S_14E3B0AF5D)
        /**
         * `attacker` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attacker = env.units()[attackerId] ?: return TacticalActionResult.Rejected(GameText.S_D26EB7B9F4)
        /**
         * `target` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val target = env.units()[targetId] ?: return TacticalActionResult.Rejected(GameText.S_367A0A7181)
        if (!attacker.visible || !target.visible) return TacticalActionResult.Rejected(GameText.S_A3E8A0BBCD)
        if (attacker.effectiveFaction() != env.activeFaction()) return TacticalActionResult.Rejected(GameText.S_50AFF62D3A)
        if (BattleStatus.PARALYSIS in attacker.statuses || BattleStatus.CONFUSION in attacker.statuses) return TacticalActionResult.Rejected(
            GameText.S_D1327CFCA3
        )
        if (env.areAllied(attacker, target)) return TacticalActionResult.Rejected(GameText.S_BDBDED5992)
        if (attacker.hasActed) return TacticalActionResult.Rejected(GameText.S_D7AE11A0C8)
        /**
         * `offset` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offset = target.tileX - attacker.tileX to target.tileY - attacker.tileY
        if (!attacker.attackAllScreen && offset !in attacker.attackOffsets) return TacticalActionResult.Rejected(GameText.S_657B5FEB42)
        return PhysicalCombatResolver.executeAttack(attacker, target, damage, env.physicalCombatEnvironment())
    }

    /**
     * `forcedAttack`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun forcedAttack(
        attackerId: String,
        targetId: String,
        env: BattleTacticalActionEnvironment,
    ): TacticalActionResult {
        /**
         * `attacker` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attacker = env.units()[attackerId] ?: return TacticalActionResult.Rejected(GameText.S_D26EB7B9F4)
        /**
         * `target` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val target = env.units()[targetId] ?: return TacticalActionResult.Rejected(GameText.S_367A0A7181)
        return ForcedPhysicalCombatResolver.executeForcedAttack(attacker, target, env.physicalCombatEnvironment())
    }

    /**
     * `useProperty`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun useProperty(
        userId: String,
        targetId: String,
        itemId: Int,
        env: BattleTacticalActionEnvironment,
    ): TacticalActionResult {
        if (env.outcome() != null) return TacticalActionResult.Rejected(GameText.S_14E3B0AF5D)
        /**
         * `user` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val user = env.units()[userId] ?: return TacticalActionResult.Rejected(GameText.S_B9A3425560)
        /**
         * `target` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val target = env.units()[targetId] ?: return TacticalActionResult.Rejected(GameText.S_367A0A7181)
        /**
         * `item` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val item = env.propertyItems[itemId] ?: return TacticalActionResult.Rejected(GameText.S_2D243A64E7)
        if (user.effectiveFaction() != env.activeFaction() || user.hasActed) return TacticalActionResult.Rejected(GameText.S_AD31679FA5)
        if (!env.areAllied(user, target)) return TacticalActionResult.Rejected(GameText.S_C5D202B13B)
        /**
         * `offset` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val offset = target.tileX - user.tileX to target.tileY - user.tileY
        if (target != user && offset !in env.movementOffsets) return TacticalActionResult.Rejected(GameText.S_8F3BC8881B)
        /**
         * `applied` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val applied = applyProperty(item, target, { env.consumeSelectedProperty(itemId) }, env.notifyPermanentProperty)
            ?: return TacticalActionResult.Rejected(GameText.S_CEF693F5A0)
        user.markActionComplete()
        return applied
    }

    /**
     * `applyProperty`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun applyProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        consume: () -> Boolean,
        notifyPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit,
    ): TacticalActionResult.Item? = BattlePropertyResolver.applyProperty(
        item = item,
        target = target,
        consume = consume,
        notifyPermanentProperty = notifyPermanentProperty,
    )

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
        env: BattleTacticalActionEnvironment,
    ): TacticalActionResult = MagicResolver.castMagic(
        attackerId, targetId, magicId, reaction, bypassCondition, env.magicEnvironment(),
    )

    /**
     * `castMagicAt`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun castMagicAt(
        attackerId: String,
        targetX: Int,
        targetY: Int,
        magicId: Int,
        env: BattleTacticalActionEnvironment,
    ): TacticalActionResult =
        MagicResolver.castMagicAt(attackerId, targetX, targetY, magicId, env.magicEnvironment())
}
