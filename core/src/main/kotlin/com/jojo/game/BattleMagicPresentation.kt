package com.jojo.game

/**
 * Pure projection of BattleScreen._magicProcess's `l` (target) and `o`
 * (caster recovery) char-info groups.  Battle has already committed the
 * result; this planner retains the old visual value until `playMeff` ends.
 */
object BattleMagicPresentation {
    data class Change(val unitId: String, val hpAdd: Int = 0, val mpAdd: Int = 0)

    fun changes(
        result: TacticalActionResult.Magic,
        casterId: String,
        magic: GameDataCatalog.MagicProfile?,
    ): List<Change> = changes(result.targets, casterId, magic)

    fun changes(
        targets: List<MagicTarget>,
        casterId: String,
        magic: GameDataCatalog.MagicProfile?,
    ): List<Change> {
        val values = linkedMapOf<String, Change>()
        fun add(id: String, hp: Int = 0, mp: Int = 0) {
            val old = values[id] ?: Change(id)
            values[id] = old.copy(hpAdd = old.hpAdd + hp, mpAdd = old.mpAdd + mp)
        }
        targets.forEach { target ->
            add(target.targetId, hp = target.healing - target.damage)
            when {
                target.magicDrain > 0 -> {
                    add(target.targetId, mp = -target.magicDrain)
                    add(casterId, mp = target.magicRecovery)
                }
                // MX (HP -> caster MP) stores target HP loss in damage and
                // recovery in the caster's `o` char-info group.
                magic?.type == 20 && magic.category == 24 && target.magicRecovery > 0 ->
                    add(casterId, mp = target.magicRecovery)
                target.magicRecovery > 0 -> add(target.targetId, mp = target.magicRecovery)
            }
            if (target.casterHealing > 0) add(casterId, hp = target.casterHealing)
        }
        return values.values.filter { it.hpAdd != 0 || it.mpAdd != 0 }
    }
}
