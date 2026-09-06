// Battle
package com.jojo.game.domain.battle

import com.jojo.game.*
import com.jojo.game.domain.campaign.*

/** BattleStateJournal: 전투 진행 중 변하는 상태를 기록하며, 초기 구성과 분리된 유닛·턴·자금 정보를 유지한다. */
internal class BattleStateJournal(
    initialWeather: BattleWeather,
    initialPlayerMoney: Int,
    initialEnemyMoney: Int,
    blockedTiles: Set<Pair<Int, Int>>,
) {
    /**
     * `blockedTiles` (MutableSet<Pair<Int, Int>>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val blockedTiles: MutableSet<Pair<Int, Int>> = blockedTiles.toMutableSet()
    /**
     * `firedEventIds` (LinkedHashSet<String>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val firedEventIds: LinkedHashSet<String> = linkedSetOf()
    /**
     * `traceActions` (MutableList<String>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val traceActions: MutableList<String> = mutableListOf()
    /**
     * `lastMovePaths` (LinkedHashMap<String, List<Pair<Int, Int>>>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val lastMovePaths: LinkedHashMap<String, List<Pair<Int, Int>>> = linkedMapOf()
    /**
     * `equipmentUpgrades` (ArrayDeque<CampaignEquipmentExperienceResult>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val equipmentUpgrades: ArrayDeque<CampaignEquipmentExperienceResult> = ArrayDeque()

    /**
     * `round` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var round: Int = 1
        private set
    /**
     * `activeFaction` (Faction): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var activeFaction: Faction = Faction.PLAYER
        private set
    /**
     * `weather` (BattleWeather): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var weather: BattleWeather = initialWeather
        private set
    /**
     * `moveLength` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var moveLength: Int = 0
        private set
    /**
     * `aiTurnOrder` (List<String>?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var aiTurnOrder: List<String>? = null
    /**
     * `lastAiUnitResolution` (AiUnitResolution?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var lastAiUnitResolution: AiUnitResolution? = null
    /**
     * `pendingActionTransaction` (BattleActionTransaction?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var pendingActionTransaction: BattleActionTransaction? = null
    /**
     * `playerMoney` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var playerMoney: Int = initialPlayerMoney
        private set
    /**
     * `enemyMoney` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var enemyMoney: Int = initialEnemyMoney
        private set
    /**
     * `hitEffects` (MutableList<() -> Unit>?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var hitEffects: MutableList<() -> Unit>? = null
    /**
     * `completionEffects` (MutableList<() -> Unit>?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var completionEffects: MutableList<() -> Unit>? = null

    /**
     * `setRound`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setRound(value: Int) {
        round = value
    }

    /**
     * `setActiveFaction`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setActiveFaction(value: Faction) {
        activeFaction = value
    }

    /**
     * `setWeather`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setWeather(value: BattleWeather) {
        weather = value
    }

    /**
     * `setMoveLength`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setMoveLength(value: Int) {
        moveLength = value
    }

    /**
     * `currentMoveLength`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun currentMoveLength(): Int = moveLength
    /**
     * `recordAiTurnOrder`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun recordAiTurnOrder(value: List<String>?) {
        aiTurnOrder = value
    }

    /**
     * `recordLastAiUnitResolution`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun recordLastAiUnitResolution(value: AiUnitResolution?) {
        lastAiUnitResolution = value
    }

    /**
     * `recordPendingActionTransaction`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun recordPendingActionTransaction(value: BattleActionTransaction?) {
        pendingActionTransaction = value
    }

    /**
     * `setPlayerMoney`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setPlayerMoney(value: Int) {
        playerMoney = value
    }

    /**
     * `setEnemyMoney`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setEnemyMoney(value: Int) {
        enemyMoney = value
    }

    /**
     * `recordStagedHitSideEffects`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun recordStagedHitSideEffects(value: MutableList<() -> Unit>?) {
        hitEffects = value
    }

    /**
     * `recordStagedCompletionSideEffects`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun recordStagedCompletionSideEffects(value: MutableList<() -> Unit>?) {
        completionEffects = value
    }

    /**
     * `recordMove`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun recordMove(id: String, path: List<Pair<Int, Int>>, nodes: Int) {
        moveLength = nodes
        lastMovePaths[id] = path.toList()
    }

    /**
     * `lastMovePath`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun lastMovePath(id: String): List<Pair<Int, Int>> = lastMovePaths[id].orEmpty()
    /**
     * `blockedTiles`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun blockedTiles(): Set<Pair<Int, Int>> = blockedTiles
    /**
     * `mutableBlockedTiles`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun mutableBlockedTiles(): MutableSet<Pair<Int, Int>> = blockedTiles
    /**
     * `mutableFiredEventIds`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun mutableFiredEventIds(): LinkedHashSet<String> = firedEventIds
    /**
     * `firedEventIdsSnapshot`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun firedEventIdsSnapshot(): Set<String> = firedEventIds.toSet()
    /**
     * `mutableTraceActions`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun mutableTraceActions(): MutableList<String> = traceActions
    /**
     * `traceActionsSnapshot`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun traceActionsSnapshot(): List<String> = traceActions.toList()
    /**
     * `mutableLastMovePaths`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun mutableLastMovePaths(): MutableMap<String, List<Pair<Int, Int>>> = lastMovePaths
    /**
     * `mutableEquipmentUpgrades`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun mutableEquipmentUpgrades(): ArrayDeque<CampaignEquipmentExperienceResult> = equipmentUpgrades
    /**
     * `stagedHitSideEffects`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun stagedHitSideEffects(): MutableList<() -> Unit>? = hitEffects
    /**
     * `stagedCompletionSideEffects`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun stagedCompletionSideEffects(): MutableList<() -> Unit>? = completionEffects
    /**
     * `hasStagedCompletionSideEffects`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hasStagedCompletionSideEffects(): Boolean = completionEffects != null
    /**
     * `stageHitSideEffect`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun stageHitSideEffect(effect: () -> Unit) {
        hitEffects?.add(effect) ?: effect()
    }

    /**
     * `stageCompletionSideEffect`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun stageCompletionSideEffect(effect: () -> Unit) {
        completionEffects?.add(effect)
    }

    /**
     * `clearBlockedTiles`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun clearBlockedTiles() = blockedTiles.clear()
    /**
     * `addBlockedTiles`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addBlockedTiles(values: Collection<Pair<Int, Int>>) = blockedTiles.addAll(values)
    /**
     * `queueEquipmentUpgrade`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun queueEquipmentUpgrade(value: CampaignEquipmentExperienceResult) {
        equipmentUpgrades += value
    }

    /**
     * `consumeEquipmentUpgrade`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeEquipmentUpgrade(): CampaignEquipmentExperienceResult? =
        if (equipmentUpgrades.isEmpty()) null else equipmentUpgrades.removeFirst()
}
