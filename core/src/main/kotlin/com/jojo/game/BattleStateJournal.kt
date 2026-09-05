package com.jojo.game
import com.jojo.game.domain.campaign.*

/**
 * Mutable progress owned by a battle aggregate.
 *
 * Besides turn state this records the deterministic trace and the small
 * amount of deferred presentation state needed to bridge AI calculation and
 * animation callbacks.  Commands below make each mutation explicit without
 * changing the legacy Battle surface used by screens and verification code.
 */
internal class BattleStateJournal(
    initialWeather: BattleWeather,
    initialPlayerMoney: Int,
    initialEnemyMoney: Int,
    blockedTiles: Set<Pair<Int, Int>>,
) {
    private val blockedTiles: MutableSet<Pair<Int, Int>> = blockedTiles.toMutableSet()
    private val firedEventIds: LinkedHashSet<String> = linkedSetOf()
    private val traceActions: MutableList<String> = mutableListOf()
    private val lastMovePaths: LinkedHashMap<String, List<Pair<Int, Int>>> = linkedMapOf()
    private val equipmentUpgrades: ArrayDeque<CampaignEquipmentExperienceResult> = ArrayDeque()

    var round: Int = 1
        private set
    var activeFaction: Faction = Faction.PLAYER
        private set
    var weather: BattleWeather = initialWeather
        private set
    var moveLength: Int = 0
        private set
    var aiTurnOrder: List<String>? = null
    var lastAiUnitResolution: AiUnitResolution? = null
    var pendingActionTransaction: BattleActionTransaction? = null
    var playerMoney: Int = initialPlayerMoney
        private set
    var enemyMoney: Int = initialEnemyMoney
        private set
    private var hitEffects: MutableList<() -> Unit>? = null
    private var completionEffects: MutableList<() -> Unit>? = null

    fun setRound(value: Int) {
        round = value
    }

    fun setActiveFaction(value: Faction) {
        activeFaction = value
    }

    fun setWeather(value: BattleWeather) {
        weather = value
    }

    fun setMoveLength(value: Int) {
        moveLength = value
    }

    fun currentMoveLength(): Int = moveLength
    fun recordAiTurnOrder(value: List<String>?) {
        aiTurnOrder = value
    }

    fun recordLastAiUnitResolution(value: AiUnitResolution?) {
        lastAiUnitResolution = value
    }

    fun recordPendingActionTransaction(value: BattleActionTransaction?) {
        pendingActionTransaction = value
    }

    fun setPlayerMoney(value: Int) {
        playerMoney = value
    }

    fun setEnemyMoney(value: Int) {
        enemyMoney = value
    }

    fun recordStagedHitSideEffects(value: MutableList<() -> Unit>?) {
        hitEffects = value
    }

    fun recordStagedCompletionSideEffects(value: MutableList<() -> Unit>?) {
        completionEffects = value
    }

    fun recordMove(id: String, path: List<Pair<Int, Int>>, nodes: Int) {
        moveLength = nodes
        lastMovePaths[id] = path.toList()
    }

    fun lastMovePath(id: String): List<Pair<Int, Int>> = lastMovePaths[id].orEmpty()
    fun blockedTiles(): Set<Pair<Int, Int>> = blockedTiles
    internal fun mutableBlockedTiles(): MutableSet<Pair<Int, Int>> = blockedTiles
    internal fun mutableFiredEventIds(): LinkedHashSet<String> = firedEventIds
    fun firedEventIdsSnapshot(): Set<String> = firedEventIds.toSet()
    internal fun mutableTraceActions(): MutableList<String> = traceActions
    fun traceActionsSnapshot(): List<String> = traceActions.toList()
    internal fun mutableLastMovePaths(): MutableMap<String, List<Pair<Int, Int>>> = lastMovePaths
    internal fun mutableEquipmentUpgrades(): ArrayDeque<CampaignEquipmentExperienceResult> = equipmentUpgrades
    fun stagedHitSideEffects(): MutableList<() -> Unit>? = hitEffects
    fun stagedCompletionSideEffects(): MutableList<() -> Unit>? = completionEffects
    fun hasStagedCompletionSideEffects(): Boolean = completionEffects != null
    fun stageHitSideEffect(effect: () -> Unit) {
        hitEffects?.add(effect) ?: effect()
    }

    fun stageCompletionSideEffect(effect: () -> Unit) {
        completionEffects?.add(effect)
    }

    fun clearBlockedTiles() = blockedTiles.clear()
    fun addBlockedTiles(values: Collection<Pair<Int, Int>>) = blockedTiles.addAll(values)
    fun queueEquipmentUpgrade(value: CampaignEquipmentExperienceResult) {
        equipmentUpgrades += value
    }

    fun consumeEquipmentUpgrade(): CampaignEquipmentExperienceResult? =
        if (equipmentUpgrades.isEmpty()) null else equipmentUpgrades.removeFirst()
}
