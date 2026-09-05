package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.RuntimeBattleUnitSnapshot
import com.jojo.game.application.runtime.RuntimeGridPoint
import com.jojo.game.application.runtime.RuntimeMagicSnapshot
import com.jojo.game.domain.battle.Faction

internal val RuntimeBattleUnitSnapshot.tileX: Int get() = x
internal val RuntimeBattleUnitSnapshot.tileY: Int get() = y
internal fun RuntimeBattleUnitSnapshot.type(): Faction = faction
internal fun RuntimeBattleUnitSnapshot.effectiveFaction(): Faction = effectiveFaction
internal fun RuntimeBattleUnitSnapshot.tile(): Pair<Int, Int> = x to y
internal fun RuntimeGridPoint.tile(): Pair<Int, Int> = x to y
internal fun Pair<Int, Int>.point(): RuntimeGridPoint = RuntimeGridPoint(first, second)
internal val RuntimeMagicSnapshot.expendMp: Int get() = cost
internal val RuntimeMagicSnapshot.hitArea: RuntimeHitArea
    get() = RuntimeHitArea(allScreen, offsets.mapTo(linkedSetOf()) { it.tile() })

internal data class RuntimeHitArea(val allScreen: Boolean, val offsets: Set<Pair<Int, Int>>)
