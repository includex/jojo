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
/**
 * `RuntimeBattleUnitSnapshot`: 상태나 데이터를 조회한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun RuntimeBattleUnitSnapshot.type(): Faction = faction
/**
 * `RuntimeBattleUnitSnapshot`: 상태나 데이터를 조회한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun RuntimeBattleUnitSnapshot.effectiveFaction(): Faction = effectiveFaction
/**
 * `RuntimeBattleUnitSnapshot`: 상태나 데이터를 조회한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun RuntimeBattleUnitSnapshot.tile(): Pair<Int, Int> = x to y
/**
 * `RuntimeGridPoint`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun RuntimeGridPoint.tile(): Pair<Int, Int> = x to y
/**
 * `Pair`: 타입의 핵심 동작을 수행한다.
 * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

internal fun Pair<Int, Int>.point(): RuntimeGridPoint = RuntimeGridPoint(first, second)
/** RuntimeMagicSnapshot: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
internal val RuntimeMagicSnapshot.expendMp: Int get() = cost
/** RuntimeMagicSnapshot: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
internal val RuntimeMagicSnapshot.hitArea: RuntimeHitArea
    get() = RuntimeHitArea(allScreen, offsets.mapTo(linkedSetOf()) { it.tile() })

/** RuntimeHitArea: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class RuntimeHitArea(val allScreen: Boolean, val offsets: Set<Pair<Int, Int>>)
