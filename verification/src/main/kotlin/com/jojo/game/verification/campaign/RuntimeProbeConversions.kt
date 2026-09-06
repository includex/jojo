// Verification
package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.RuntimeBattleUnitSnapshot
import com.jojo.game.application.runtime.RuntimeGridPoint
import com.jojo.game.application.runtime.RuntimeMagicSnapshot
import com.jojo.game.domain.battle.Faction

/** RuntimeBattleUnitSnapshot: 전투 무장 상태를 담는다. */
internal val RuntimeBattleUnitSnapshot.tileX: Int get() = x
/** RuntimeBattleUnitSnapshot: 전투 무장 상태를 담는다. */
internal val RuntimeBattleUnitSnapshot.tileY: Int get() = y
internal fun RuntimeBattleUnitSnapshot.type(): Faction = faction
internal fun RuntimeBattleUnitSnapshot.effectiveFaction(): Faction = effectiveFaction
internal fun RuntimeBattleUnitSnapshot.tile(): Pair<Int, Int> = x to y
internal fun RuntimeGridPoint.tile(): Pair<Int, Int> = x to y
internal fun Pair<Int, Int>.point(): RuntimeGridPoint = RuntimeGridPoint(first, second)
/** RuntimeMagicSnapshot: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
internal val RuntimeMagicSnapshot.expendMp: Int get() = cost
/** RuntimeMagicSnapshot: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
internal val RuntimeMagicSnapshot.hitArea: RuntimeHitArea
    get() = RuntimeHitArea(allScreen, offsets.mapTo(linkedSetOf()) { it.tile() })

/** RuntimeHitArea: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class RuntimeHitArea(val allScreen: Boolean, val offsets: Set<Pair<Int, Int>>)
