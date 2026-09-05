package com.jojo.game

/** Pure source rules shared by the live unitDeath/scripted-hide callbacks and focused tests. */
internal object UnitDeathPresentation {
    fun sortedDying(units: Collection<BattleUnit>): List<BattleUnit> = units
        .filter { it.visible && it.hitPoints <= 0 }
        .sortedBy { 100 * it.tileY + it.tileX }

    fun hideAction(hideType: Int, selfMaster: Boolean): Int = when {
        hideType == 0 -> 47
        hideType == 2 || selfMaster -> 24
        else -> 23
    }
}
