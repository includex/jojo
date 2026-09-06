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
import com.jojo.game.domain.battle.magic.MagicEnvironment
import com.jojo.game.domain.battle.magic.MagicResolver
import com.jojo.game.domain.battle.combat.*
import com.jojo.game.domain.battle.BattlePropertyResolver

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
internal object BattleTacticalActionExecutor {

    fun attack(
        attackerId: String,
        targetId: String,
        damage: Int? = null,
        env: BattleTacticalActionEnvironment,
    ): TacticalActionResult {
        if (env.outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val attacker = env.units()[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val target = env.units()[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        if (!attacker.visible || !target.visible) return TacticalActionResult.Rejected("아직 등장하지 않은 유닛입니다.")
        if (attacker.effectiveFaction() != env.activeFaction()) return TacticalActionResult.Rejected("현재 진영의 유닛만 조작할 수 있습니다.")
        if (BattleStatus.PARALYSIS in attacker.statuses || BattleStatus.CONFUSION in attacker.statuses) return TacticalActionResult.Rejected(
            "행동할 수 없는 상태입니다."
        )
        if (env.areAllied(attacker, target)) return TacticalActionResult.Rejected("아군을 공격할 수 없습니다.")
        if (attacker.hasActed) return TacticalActionResult.Rejected("이미 행동한 유닛입니다.")
        val offset = target.tileX - attacker.tileX to target.tileY - attacker.tileY
        if (!attacker.attackAllScreen && offset !in attacker.attackOffsets) return TacticalActionResult.Rejected("공격 범위를 벗어난 적입니다.")
        return PhysicalCombatResolver.executeAttack(attacker, target, damage, env.physicalCombatEnvironment())
    }

    fun forcedAttack(
        attackerId: String,
        targetId: String,
        env: BattleTacticalActionEnvironment,
    ): TacticalActionResult {
        val attacker = env.units()[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val target = env.units()[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        return ForcedPhysicalCombatResolver.executeForcedAttack(attacker, target, env.physicalCombatEnvironment())
    }

    fun useProperty(
        userId: String,
        targetId: String,
        itemId: Int,
        env: BattleTacticalActionEnvironment,
    ): TacticalActionResult {
        if (env.outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val user = env.units()[userId] ?: return TacticalActionResult.Rejected("사용 유닛이 없습니다.")
        val target = env.units()[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        val item = env.propertyItems[itemId] ?: return TacticalActionResult.Rejected("사용할 수 없는 아이템입니다.")
        if (user.effectiveFaction() != env.activeFaction() || user.hasActed) return TacticalActionResult.Rejected("현재 행동할 수 없는 유닛입니다.")
        if (!env.areAllied(user, target)) return TacticalActionResult.Rejected("아군에게만 사용할 수 있습니다.")
        val offset = target.tileX - user.tileX to target.tileY - user.tileY
        if (target != user && offset !in env.movementOffsets) return TacticalActionResult.Rejected("아이템 사용 범위를 벗어났습니다.")
        val applied = applyProperty(item, target, { env.consumeSelectedProperty(itemId) }, env.notifyPermanentProperty)
            ?: return TacticalActionResult.Rejected("아이템을 사용할 수 없습니다.")
        user.markActionComplete()
        return applied
    }

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

    fun castMagicAt(
        attackerId: String,
        targetX: Int,
        targetY: Int,
        magicId: Int,
        env: BattleTacticalActionEnvironment,
    ): TacticalActionResult =
        MagicResolver.castMagicAt(attackerId, targetX, targetY, magicId, env.magicEnvironment())
}
