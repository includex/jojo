package com.jojo.game.presentation.battle.unit

import com.jojo.game.*
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
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

    /**
     * data class  `HarmNumber`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * data class  `AttributeStatusIcon`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class AttributeStatusIcon(val active: Boolean, val down: Boolean)

    /**
     * data class  `DefaultAction`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class DefaultAction(val action: Int, val loop: Boolean)

    /**
     * data class  `HarmBarInput`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class HarmBarInput(
        val hitPoints: Int,
        val maxHitPoints: Int,
        val magicPoints: Int,
        val maxMagicPoints: Int,
    )

    /**
     * data class  `DefaultActionInput`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class DefaultActionInput(
        val visible: Boolean,
        val hitPoints: Int,
        val maxHitPoints: Int,
        val famous: Boolean,
        val hasActed: Boolean,
        val poisoned: Boolean,
        val paralyzed: Boolean,
    )

    /**
     * 공개 메서드 `refreshHpBar`
     *
     * ### 파라미터
    - `hitPoints` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `maxHitPoints` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun refreshHpBar(hitPoints: Int, maxHitPoints: Int) {
        hpBarProgress = hpRatio(hitPoints, maxHitPoints)
    }

    /**
     * 공개 메서드 `showHarmNumber`
     *
     * ### 파라미터
    - `hpAdd` (`Int? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `mpAdd` (`Int? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun showHarmNumber(hpAdd: Int? = null, mpAdd: Int? = null) {
        val isHp = mpAdd == null
        val value = mpAdd ?: hpAdd ?: return
        clearHarmNumber()
        harmNumber = HarmNumber(value = kotlin.math.abs(value), isHp = isHp, xOffset = if (isHp) -24 else 24)
    }

    /**
     * 공개 메서드 `clearHarmNumber`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun clearHarmNumber() {
        harmNumber = null
    }

    fun refreshStatus(
        statuses: Map<BattleStatus, Int>,
        attributeLifts: Map<BattleAttribute, Int>,
    ): BattleUnitStateAnimation.Effect? {
        val effect = stateAnimation.refresh(
            listOf(
                BattleStatus.PARALYSIS in statuses,
                BattleStatus.SILENCE in statuses,
                BattleStatus.CONFUSION in statuses,
                BattleStatus.POISON in statuses,
            )
        )
        refreshAttributeStatusIcons(attributeLifts)
        return effect
    }

    /**
     * 공개 메서드 `refreshAttributeStatusIcons`
     *
     * ### 파라미터
    - `attributeLifts` (`Map<BattleAttribute, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun refreshAttributeStatusIcons(attributeLifts: Map<BattleAttribute, Int>) {
        attributeStatusIcons = BattleAttribute.entries.associateWith { attribute ->
            val lift = attributeLifts[attribute] ?: 0
            AttributeStatusIcon(active = lift != 0, down = lift == -1)
        }
    }

    /**
     * 공개 메서드 `setStateAnimationVisible`
     *
     * ### 파라미터
    - `visible` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setStateAnimationVisible(visible: Boolean) = stateAnimation.setVisible(visible)

    /**
     * 공개 메서드 `showHarmBar`
     *
     * ### 파라미터
    - `input` (`HarmBarInput`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `hpAdd` (`Int? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `mpAdd` (`Int? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `hitRate` (`Number? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
