package com.jojo.game

/**
 * Captures the mutable unit state needed to calculate an action transaction
 * and later publish or discard that calculation without replacing the unit.
 */
internal data class BattleUnitMemento(
    val unit: BattleUnit,
    val tileX: Int,
    val tileY: Int,
    val hitPoints: Int,
    val maxHitPoints: Int,
    val magicPoints: Int,
    val maxMagicPoints: Int,
    val level: Int,
    val direction: Int,
    val hasActed: Boolean,
    val actionStatusRound: Int,
    val hasMoved: Boolean,
    val visible: Boolean,
    val otherNodesVisible: Boolean,
    val retreatFlag: Boolean,
    val retreatCount: Int,
    val ai: Int,
    val aiTargetCharacterId: Int,
    val aiTargetX: Int,
    val aiTargetY: Int,
    val aiValue: Int,
    val criticalSpeechChecks: Int,
    val statuses: Map<BattleStatus, Int>,
    val attributeLifts: Map<BattleAttribute, Int>,
    val attributeLiftRounds: Map<BattleAttribute, Int>,
    val rateAccumulators: Map<Int, Int>,
) {
    /** Restores the captured values into the same unit identity. */
    fun restore(): BattleUnit = unit.apply {
        tileX = this@BattleUnitMemento.tileX
        tileY = this@BattleUnitMemento.tileY
        maxHitPoints = this@BattleUnitMemento.maxHitPoints
        setHpcur(this@BattleUnitMemento.hitPoints)
        maxMagicPoints = this@BattleUnitMemento.maxMagicPoints
        setMpcur(this@BattleUnitMemento.magicPoints)
        level = this@BattleUnitMemento.level
        direction = this@BattleUnitMemento.direction
        hasActed = this@BattleUnitMemento.hasActed
        actionStatusRound = this@BattleUnitMemento.actionStatusRound
        hasMoved = this@BattleUnitMemento.hasMoved
        visible = this@BattleUnitMemento.visible
        otherNodesVisible = this@BattleUnitMemento.otherNodesVisible
        retreatFlag = this@BattleUnitMemento.retreatFlag
        retreatCount = this@BattleUnitMemento.retreatCount
        ai = this@BattleUnitMemento.ai
        aiTargetCharacterId = this@BattleUnitMemento.aiTargetCharacterId
        aiTargetX = this@BattleUnitMemento.aiTargetX
        aiTargetY = this@BattleUnitMemento.aiTargetY
        aiValue = this@BattleUnitMemento.aiValue
        criticalSpeechChecks = this@BattleUnitMemento.criticalSpeechChecks
        statuses.clear()
        statuses.putAll(this@BattleUnitMemento.statuses)
        attributeLifts.clear()
        attributeLifts.putAll(this@BattleUnitMemento.attributeLifts)
        attributeLiftRounds.clear()
        attributeLiftRounds.putAll(this@BattleUnitMemento.attributeLiftRounds)
        rateAccumulators.clear()
        rateAccumulators.putAll(this@BattleUnitMemento.rateAccumulators)
        presentation.refreshStatus(statuses, attributeLifts)
    }

    companion object {
        fun capture(unit: BattleUnit): BattleUnitMemento = BattleUnitMemento(
            unit = unit,
            tileX = unit.tileX,
            tileY = unit.tileY,
            hitPoints = unit.hitPoints,
            maxHitPoints = unit.maxHitPoints,
            magicPoints = unit.magicPoints,
            maxMagicPoints = unit.maxMagicPoints,
            level = unit.level,
            direction = unit.direction,
            hasActed = unit.hasActed,
            actionStatusRound = unit.actionStatusRound,
            hasMoved = unit.hasMoved,
            visible = unit.visible,
            otherNodesVisible = unit.otherNodesVisible,
            retreatFlag = unit.retreatFlag,
            retreatCount = unit.retreatCount,
            ai = unit.ai,
            aiTargetCharacterId = unit.aiTargetCharacterId,
            aiTargetX = unit.aiTargetX,
            aiTargetY = unit.aiTargetY,
            aiValue = unit.aiValue,
            criticalSpeechChecks = unit.criticalSpeechChecks,
            statuses = unit.statuses.toMap(),
            attributeLifts = unit.attributeLifts.toMap(),
            attributeLiftRounds = unit.attributeLiftRounds.toMap(),
            rateAccumulators = unit.rateAccumulators.toMap(),
        )
    }
}
