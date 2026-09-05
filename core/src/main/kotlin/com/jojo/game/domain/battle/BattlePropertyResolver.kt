package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.label

/**
 * Pure Kotlin resolution for battle consumables and property item effects.
 */
internal object BattlePropertyResolver {

    /**
     * Applies the effect of a property/consumable item to the target unit.
     * Shared by player-initiated item usage and automatic combat consumption (ZDSY).
     */
    fun applyProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        consume: () -> Boolean,
        notifyPermanentProperty: ((BattlePropertyItem, BattleUnit) -> Unit)? = null,
    ): TacticalActionResult.Item? {
        val effect = when (item.itemType) {
            26 -> {
                if (target.hitPoints >= target.maxHitPoints || !consume()) return null
                val amount = if (item.value == 255) target.maxHitPoints else item.value
                val recovered = minOf(amount, target.maxHitPoints - target.hitPoints)
                target.addHpcur(recovered)
                "HP ${recovered} 회복"
            }

            27 -> {
                if (target.magicPoints >= target.maxMagicPoints || !consume()) return null
                val amount = if (item.value == 255) target.maxMagicPoints else item.value
                val recovered = minOf(amount, target.maxMagicPoints - target.magicPoints)
                target.addMpcur(recovered)
                "MP ${recovered} 회복"
            }

            28, 29, 30, 31 -> {
                val status = listOf(
                    BattleStatus.CONFUSION,
                    BattleStatus.POISON,
                    BattleStatus.PARALYSIS,
                    BattleStatus.SILENCE,
                )[item.itemType - 28]
                if (status !in target.statuses || !consume()) return null
                check(target.cureStatus(status))
                "${status.label()} 치료"
            }

            32 -> {
                if (target.statuses.isEmpty() || !consume()) return null
                check(target.cureAllStatuses())
                "모든 이상 상태 치료"
            }

            33, 34, 35, 36, 37 -> {
                val attribute = listOf(
                    BattleAttribute.ATTACK,
                    BattleAttribute.SPIRIT,
                    BattleAttribute.DEFENSE,
                    BattleAttribute.CRITICAL,
                    BattleAttribute.MORALE,
                )[item.itemType - 33]
                if (!consume()) return null
                target.applyAttributeLift(attribute, 1, 3)
                "${attribute.label()} 상승"
            }

            42 -> {
                if (!consume()) return null
                target.maxHitPoints += item.value
                target.addHpcur(item.value)
                notifyPermanentProperty?.invoke(item, target)
                "최대 HP ${item.value} 증가"
            }

            43 -> {
                if (!consume()) return null
                target.maxMagicPoints += item.value
                target.addMpcur(item.value)
                notifyPermanentProperty?.invoke(item, target)
                "최대 MP ${item.value} 증가"
            }

            else -> return null
        }
        return TacticalActionResult.Item(item.name, target.id, effect)
    }
}
