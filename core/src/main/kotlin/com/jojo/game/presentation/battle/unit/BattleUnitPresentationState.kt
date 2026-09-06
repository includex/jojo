// Battle
package com.jojo.game.presentation.battle.unit

import com.jojo.game.*
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus

/** 전투 유닛 표시 상태: 체력 바·피해 숫자·상태 아이콘·기본 대기 동작을 실제 유닛 수치와 분리해 계산한다. */
class BattleUnitPresentationState(
    initialHitPoints: Int,
    initialMaxHitPoints: Int,
) {
    /** 상태 애니메이션: 상태 이상 아이콘의 현재 프레임과 활성 여부를 보관한다. */
    val stateAnimation = BattleUnitStateAnimation()

    /** 체력 바 비율: 현재 표시할 HP를 최대 HP로 나눈 0~1 비율이다. */
    var hpBarProgress: Float = hpRatio(initialHitPoints, initialMaxHitPoints)
        private set

    /** 피해 숫자: 마지막 HP·MP 변화에서 화면에 띄울 숫자와 색상 정보다. */
    var harmNumber: HarmNumber? = null
        private set

    /** 피해 바 미리보기: 공격 선택 중 HP·MP 변화와 명중률을 비교할 표시값이다. */
    var harmBarPreview: BattleHarmBar.View = BattleHarmBar.View()
        private set

    /** 능력치 상태 아이콘: 각 능력치 상승·하락 여부를 화면 아이콘 정보로 변환한 결과다. */
    var attributeStatusIcons: Map<BattleAttribute, AttributeStatusIcon> = emptyMap()
        private set
    /** 피해 숫자: 표시값·색상·출력 위치와 원본 테두리 스타일을 정의한다. */
    data class HarmNumber(
        /**
         * `value` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Int,
        /**
         * `isHp` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val isHp: Boolean,
        /**
         * `xOffset` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val xOffset: Int,
        /**
         * `yOffset` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val yOffset: Int = 24,
        /**
         * `zIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zIndex: Int = 999,
        /**
         * `colorRgb` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val colorRgb: Int = if (isHp) 0xFFFFFF else 0xE0E000,
        /**
         * `outlineRgb` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val outlineRgb: Int = 9_212_044,
        /**
         * `outlineWidth` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val outlineWidth: Int = 1,
    )
    /** 능력치 상태 아이콘: 해당 능력치 효과의 활성 여부와 하락 방향을 정의한다. */
    data class AttributeStatusIcon(val active: Boolean, val down: Boolean)
    /** 기본 동작: 현재 유닛 상태가 요구하는 atlas action과 반복 재생 여부를 정의한다. */
    data class DefaultAction(val action: Int, val loop: Boolean)
    /** 피해 바 입력: HP·MP 변화 미리보기에 필요한 현재·최대 수치를 묶는다. */
    data class HarmBarInput(
        /**
         * `hitPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitPoints: Int,
        /**
         * `maxHitPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxHitPoints: Int,
        /**
         * `magicPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magicPoints: Int,
        /**
         * `maxMagicPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxMagicPoints: Int,
    )
    /** 기본 동작 입력: 생존·행동 완료·상태 이상에 따른 대기 action 선택 조건이다. */
    data class DefaultActionInput(
        /**
         * `visible` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visible: Boolean,
        /**
         * `hitPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitPoints: Int,
        /**
         * `maxHitPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxHitPoints: Int,
        /**
         * `famous` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val famous: Boolean,
        /**
         * `hasActed` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hasActed: Boolean,
        /**
         * `poisoned` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val poisoned: Boolean,
        /**
         * `paralyzed` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val paralyzed: Boolean,
    )


    /** 체력 바 갱신: 최신 HP·최대 HP 비율을 표시 상태에 반영한다. */
    fun refreshHpBar(hitPoints: Int, maxHitPoints: Int) {
        hpBarProgress = hpRatio(hitPoints, maxHitPoints)
    }


    /** 피해 숫자 표시: MP 변화가 있으면 MP 색상을 우선하고, 없으면 HP 변화 숫자를 기록한다. */
    fun showHarmNumber(hpAdd: Int? = null, mpAdd: Int? = null) {
        val isHp = mpAdd == null
        val value = mpAdd ?: hpAdd ?: return
        clearHarmNumber()
        harmNumber = HarmNumber(value = kotlin.math.abs(value), isHp = isHp, xOffset = if (isHp) -24 else 24)
    }


    /** 피해 숫자 제거: 이전 타격에서 남은 숫자 표시 상태를 비운다. */
    fun clearHarmNumber() {
        harmNumber = null
    }

    /** 상태 갱신: 상태 이상 애니메이션과 능력치 상승·하락 아이콘을 최신 전투 상태로 동기화한다. */
    fun refreshStatus(
        statuses: Map<BattleStatus, Int>,
        attributeLifts: Map<BattleAttribute, Int>,
    ): BattleUnitStateAnimation.Effect? {
        /**
         * `effect` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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


    /** 능력치 아이콘 갱신: 각 능력치의 상승·하락 값을 아이콘 활성·방향 정보로 변환한다. */
    fun refreshAttributeStatusIcons(attributeLifts: Map<BattleAttribute, Int>) {
        attributeStatusIcons = BattleAttribute.entries.associateWith { attribute ->
            val lift = attributeLifts[attribute] ?: 0
            AttributeStatusIcon(active = lift != 0, down = lift == -1)
        }
    }


    /** 상태 애니메이션 표시: 유닛 노드 표시 여부에 맞춰 상태 아이콘 애니메이션도 함께 켜거나 끈다. */
    fun setStateAnimationVisible(visible: Boolean) = stateAnimation.setVisible(visible)


    /** 피해 바 미리보기: 예상 HP·MP 변화와 명중률을 UI가 바로 그릴 수 있는 값으로 계산한다. */
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
    /** 기본 동작 선택: 체력 비율·행동 완료·독·마비 상태에 맞는 원본 대기 action을 반환한다. */
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

    /** 체력 비율 계산: 최대 HP가 0이어도 안전하게 1 이상으로 나눠 화면 비율을 계산한다. */
    private fun hpRatio(hitPoints: Int, maxHitPoints: Int): Float =
        hitPoints.toFloat() / maxHitPoints.coerceAtLeast(1)

    private companion object {
        /**
         * `STAND` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val STAND = 0
        /**
         * `CHUAN_QI` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHUAN_QI = 9
        /**
         * `STAND_MB` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val STAND_MB = 36
        /**
         * `STAND_ZD` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val STAND_ZD = 37
        /**
         * `STAND_ZD_MB` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val STAND_ZD_MB = 38
        /**
         * `STAND_UP_ACTION` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val STAND_UP_ACTION = 39
        /**
         * `STAND_UP_ZD` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val STAND_UP_ZD = 40
        /**
         * `CHUAN_QI_ZD` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHUAN_QI_ZD = 41
        /**
         * `CHUAN_QI_MB` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHUAN_QI_MB = 42
        /**
         * `CHUAN_QI_ZD_MB` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHUAN_QI_ZD_MB = 43
        /**
         * `XU_RUO_ACTION` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val XU_RUO_ACTION = 44
        /**
         * `XU_RUO_ZD` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val XU_RUO_ZD = 45
    }
}
