// Battle
package com.jojo.game.domain.battle.settlement

import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
/**
 * `BattleSettlementPlan` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class BattleSettlementPlan(
    val stage: CampSettlementStage,
    val camp: Faction,
    val units: List<SettlementUnitPlan>,
    val meffBuckets: List<SettlementMeffBucket>,
    val pendingIntegrations: List<SettlementPendingIntegration> = emptyList(),
    val authoredSubflows: List<SettlementAuthoredSubflowPlan> = emptyList(),
) {
    /**
     * `sourceDataComplete` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val sourceDataComplete: Boolean get() = pendingIntegrations.isEmpty()
    /**
     * `fullyRepresented` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val fullyRepresented: Boolean get() = sourceDataComplete && authoredSubflows.isEmpty()
}

/**
 * `SettlementAuthoredSubflowPlan` 계약 인터페이스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

sealed interface SettlementAuthoredSubflowPlan {
    /**
     * `LocalAura` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class LocalAura(
        /**
         * `casterId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val casterId: String,
        /**
         * `skillId` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val skillId: Int,
        /**
         * `skillValue` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val skillValue: Int,
        /**
         * `steps` (List<SettlementAuraStep>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val steps: List<SettlementAuraStep>,
        /**
         * `nestedSettlement` (BattleSettlementPlan,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val nestedSettlement: BattleSettlementPlan,
    ) : SettlementAuthoredSubflowPlan

    /**
     * `Growth` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Growth(
        /**
         * `unitId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unitId: String,
        /**
         * `grants` (List<SettlementGrowthGrant>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val grants: List<SettlementGrowthGrant>,
        /**
         * `steps` (List<SettlementGrowthStep>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val steps: List<SettlementGrowthStep>,
    ) : SettlementAuthoredSubflowPlan
}

/**
 * `SettlementAuraStep` 계약 인터페이스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

sealed interface SettlementAuraStep {
    /**
     * `Focus` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Focus(val seconds: Float) : SettlementAuraStep
    /**
     * `Sound` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Sound(val soundIndex: Int) : SettlementAuraStep
    /**
     * `Info2` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Info2(val skillId: Int) : SettlementAuraStep
    /**
     * `ActionFinished` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class ActionFinished(val actionId: Int) : SettlementAuraStep
    /**
     * `PlayMeff` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class PlayMeff(val semanticName: String, val targetIds: List<String>) : SettlementAuraStep
    /**
     * `NestedSettlement` 싱글턴 객체: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data object NestedSettlement : SettlementAuraStep
    /**
     * `DefaultAction` 싱글턴 객체: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data object DefaultAction : SettlementAuraStep
}

/**
 * `SettlementGrowthStep` 계약 인터페이스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

sealed interface SettlementGrowthStep {
    /**
     * `InfoValues` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class InfoValues(val grants: List<SettlementGrowthGrant>) : SettlementGrowthStep
    /**
     * `AbilityLevelUp` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class AbilityLevelUp(val attribute: BattleAttribute) : SettlementGrowthStep
    /**
     * `UnitLevelUpActionFinished` 싱글턴 객체: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data object UnitLevelUpActionFinished : SettlementGrowthStep
    /**
     * `UnitLevelUpInfo` 싱글턴 객체: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data object UnitLevelUpInfo : SettlementGrowthStep
    /**
     * `LearnedMagicInfo` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class LearnedMagicInfo(val magicId: Int) : SettlementGrowthStep
    /**
     * `EquipmentLevelUpAction` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class EquipmentLevelUpAction(val result: CampaignEquipmentExperienceResult) : SettlementGrowthStep
    /**
     * `EquipmentLevelUpInfo` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class EquipmentLevelUpInfo(val result: CampaignEquipmentExperienceResult) : SettlementGrowthStep
    /**
     * `ItemUpgradeCallback` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class ItemUpgradeCallback(val result: CampaignEquipmentExperienceResult) : SettlementGrowthStep
    /**
     * `DefaultAction` 싱글턴 객체: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data object DefaultAction : SettlementGrowthStep
}

/**
 * `SettlementPendingKind` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

enum class SettlementPendingKind { LOCAL_AURA, EXPERIENCE_AND_LEVEL_UP }
/**
 * `SettlementPendingIntegration` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class SettlementPendingIntegration(val kind: SettlementPendingKind, val unitIds: List<String>)

/**
 * `SettlementInfoKind` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

enum class SettlementInfoKind { HP, MP }
/**
 * `SettlementInfoPanel` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

enum class SettlementInfoPanel { MINE, OTHER }
/**
 * `SettlementStateChangeKind` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

enum class SettlementStateChangeKind { ADD, REMOVE, ROUND_UPDATE, LIFT }

/**
 * `SettlementInfoDelta` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class SettlementInfoDelta(
    val kind: SettlementInfoKind,
    val before: Int,
    val after: Int,
) {
    /**
     * `tickCount` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val tickCount: Int get() = minOf(kotlin.math.abs(after - before), 5)
    /**
     * `tickSeconds` (Float get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val tickSeconds: Float get() = tickCount * .2f
}

/**
 * `SettlementStateChange` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class SettlementStateChange(
    val sourceStatusIndex: Int,
    val kind: SettlementStateChangeKind,
    val roundBefore: Int? = null,
    val roundAfter: Int? = null,
    val liftBefore: Int? = null,
    val liftAfter: Int? = null,
) {
    /**
     * `meffSlot` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val meffSlot: Int get() = when {
        kind == SettlementStateChangeKind.REMOVE -> 0
        kind == SettlementStateChangeKind.LIFT && (liftAfter ?: 0) < 0 -> 0
        kind == SettlementStateChangeKind.LIFT && (liftAfter ?: 0) > 0 -> 2
        else -> 1
    }
}

/**
 * `SettlementUnitPlan` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class SettlementUnitPlan(
    val unitId: String,
    val baseFaction: Faction,
    val effectiveFactionBefore: Faction,
    val effectiveFactionAfter: Faction,
    val infoPanel: SettlementInfoPanel?,
    val infoDeltas: List<SettlementInfoDelta>,
    val stateChanges: List<SettlementStateChange>,
    val hasStatesPayload: Boolean = stateChanges.isNotEmpty(),
    val preInfoDelaySeconds: Float = if (infoDeltas.isEmpty()) 0f else .1f,
    val infoCloseSeconds: Float = if (infoDeltas.isEmpty()) 0f else .3f,
) {
    /**
     * `infoBarrierSeconds` (Float get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val infoBarrierSeconds: Float get() = (
        kotlin.math.round((preInfoDelaySeconds.toDouble() + infoDeltas.sumOf { it.tickSeconds.toDouble() } + infoCloseSeconds) * 1_000) /
            1_000.0
        ).toFloat()
}

/**
 * `SettlementMeffKey` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class SettlementMeffKey(
    val sourceStatusIndex: Int,
    val meffSlot: Int,
    val actualMeffId: Int? = null,
)

/**
 * `SettlementMeffTarget` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class SettlementMeffTarget(val unitId: String, val state: SettlementStateChange)
/**
 * `SettlementMeffBucket` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class SettlementMeffBucket(
    val key: SettlementMeffKey,
    val targets: List<SettlementMeffTarget>,
    val simultaneousTargets: Boolean = true,
    val callbackTargetUnitId: String = targets.last().unitId,
)
