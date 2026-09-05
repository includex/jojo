package com.jojo.game

/** Renderer-independent visual state derived from one tactical unit. */
class BattleUnitPresentationState(
    initialHitPoints: Int,
    initialMaxHitPoints: Int,
) {
    /** Authored status-effect animation selection for paralysis, silence, confusion, and poison. */
    val stateAnimation = BattleUnitStateAnimation()

    var hpBarProgress: Float = hpRatio(initialHitPoints, initialMaxHitPoints)
        private set

    var harmNumber: HarmNumber? = null
        private set

    var harmBarPreview: BattleHarmBar.View = BattleHarmBar.View()
        private set

    var attributeStatusIcons: Map<BattleAttribute, AttributeStatusIcon> = emptyMap()
        private set

    data class HarmNumber(
        val value: Int,
        val isHp: Boolean,
        val xOffset: Int,
        val yOffset: Int = 24,
        val zIndex: Int = 999,
        val colorRgb: Int = if (isHp) 0xFFFFFF else 0xE0E000,
        val outlineRgb: Int = 9_212_044,
        val outlineWidth: Int = 1,
    )

    data class AttributeStatusIcon(val active: Boolean, val down: Boolean)

    data class DefaultAction(val action: Int, val loop: Boolean)

    data class HarmBarInput(
        val hitPoints: Int,
        val maxHitPoints: Int,
        val magicPoints: Int,
        val maxMagicPoints: Int,
    )

    data class DefaultActionInput(
        val visible: Boolean,
        val hitPoints: Int,
        val maxHitPoints: Int,
        val famous: Boolean,
        val hasActed: Boolean,
        val poisoned: Boolean,
        val paralyzed: Boolean,
    )

    fun refreshHpBar(hitPoints: Int, maxHitPoints: Int) {
        hpBarProgress = hpRatio(hitPoints, maxHitPoints)
    }

    fun showHarmNumber(hpAdd: Int? = null, mpAdd: Int? = null) {
        val isHp = mpAdd == null
        val value = mpAdd ?: hpAdd ?: return
        clearHarmNumber()
        harmNumber = HarmNumber(value = kotlin.math.abs(value), isHp = isHp, xOffset = if (isHp) -24 else 24)
    }

    fun clearHarmNumber() {
        harmNumber = null
    }

    fun refreshStatus(
        statuses: Map<BattleStatus, Int>,
        attributeLifts: Map<BattleAttribute, Int>,
    ): BattleUnitStateAnimation.Effect? {
        val effect = stateAnimation.refresh(listOf(
            BattleStatus.PARALYSIS in statuses,
            BattleStatus.SILENCE in statuses,
            BattleStatus.CONFUSION in statuses,
            BattleStatus.POISON in statuses,
        ))
        refreshAttributeStatusIcons(attributeLifts)
        return effect
    }

    fun refreshAttributeStatusIcons(attributeLifts: Map<BattleAttribute, Int>) {
        attributeStatusIcons = BattleAttribute.entries.associateWith { attribute ->
            val lift = attributeLifts[attribute] ?: 0
            AttributeStatusIcon(active = lift != 0, down = lift == -1)
        }
    }

    fun setStateAnimationVisible(visible: Boolean) = stateAnimation.setVisible(visible)

    fun showHarmBar(input: HarmBarInput, hpAdd: Int? = null, mpAdd: Int? = null, hitRate: Number? = null) {
        harmBarPreview = BattleHarmBar.show(
            input.hitPoints,
            input.maxHitPoints,
            input.magicPoints,
            input.maxMagicPoints,
            hpAdd,
            mpAdd,
            hitRate,
        )
    }

    /** Chooses the authored idle animation from current unit state. */
    fun defaultAction(input: DefaultActionInput): DefaultAction {
        if (!input.visible) return DefaultAction(STAND, loop = true)
        val lowHp = input.hitPoints < (input.maxHitPoints * (if (input.famous) 4 else 2) / 10)
        return if (lowHp) {
            when {
                input.hasActed && input.poisoned -> DefaultAction(XU_RUO_ZD, true)
                input.hasActed -> DefaultAction(XU_RUO_ACTION, false)
                input.poisoned && input.paralyzed -> DefaultAction(CHUAN_QI_ZD_MB, true)
                input.poisoned -> DefaultAction(CHUAN_QI_ZD, true)
                input.paralyzed -> DefaultAction(CHUAN_QI_MB, true)
                else -> DefaultAction(CHUAN_QI, true)
            }
        } else {
            when {
                input.hasActed && input.poisoned -> DefaultAction(STAND_UP_ZD, true)
                input.hasActed -> DefaultAction(STAND_UP_ACTION, false)
                input.poisoned && input.paralyzed -> DefaultAction(STAND_ZD_MB, true)
                input.poisoned -> DefaultAction(STAND_ZD, true)
                input.paralyzed -> DefaultAction(STAND_MB, true)
                else -> DefaultAction(STAND, true)
            }
        }
    }

    private fun hpRatio(hitPoints: Int, maxHitPoints: Int): Float =
        hitPoints.toFloat() / maxHitPoints.coerceAtLeast(1)

    private companion object {
        const val STAND = 0
        const val CHUAN_QI = 9
        const val STAND_MB = 36
        const val STAND_ZD = 37
        const val STAND_ZD_MB = 38
        const val STAND_UP_ACTION = 39
        const val STAND_UP_ZD = 40
        const val CHUAN_QI_ZD = 41
        const val CHUAN_QI_MB = 42
        const val CHUAN_QI_ZD_MB = 43
        const val XU_RUO_ACTION = 44
        const val XU_RUO_ZD = 45
    }
}
