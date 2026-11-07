package com.jojo.game.domain.battle

/** Resolved only at the start of the caster's own faction turn. */
internal object BattleMeditation {
    fun recover(unit: BattleUnit) {
        if (unit.statuses.remove(BattleStatus.MEDITATION) == null) return
        if (!unit.visible || unit.hitPoints <= 0) return
        unit.addHpcur(unit.maxHitPoints / 10)
        unit.addMpcur(unit.maxMagicPoints / 5)
    }
}
