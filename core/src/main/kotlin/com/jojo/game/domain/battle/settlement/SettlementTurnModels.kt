// Battle
package com.jojo.game.domain.battle.settlement

import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
import com.jojo.game.domain.campaign.CampaignExperienceResult

/** CampSettlementStage: 진영 시작 상태 적용과 종료 복원 정산의 시점을 구분한다. */
enum class CampSettlementStage { START_STATE, END_RESTORE }

/** BattleUnitTurnChange: 진영 정산 전후 유닛의 체력·기력·상태 이상·행동 완료 상태를 비교한다. */
data class BattleUnitTurnChange(
    val unitId: String,
    val hitPointsBefore: Int,
    val hitPointsAfter: Int,
    val magicPointsBefore: Int,
    val magicPointsAfter: Int,
    val statusesBefore: Map<BattleStatus, Int>,
    val statusesAfter: Map<BattleStatus, Int>,
    val attributeLiftsBefore: Map<BattleAttribute, Int>,
    val attributeLiftsAfter: Map<BattleAttribute, Int>,
    val actionCompleteBefore: Boolean = false,
    val actionCompleteAfter: Boolean = false,
    val actionStatusRoundBefore: Int = 0,
    val actionStatusRoundAfter: Int = 0,
)

/** CampSettlement: 한 진영 정산의 대상 진영·유닛 변화·표현할 하위 흐름을 묶는다. */
data class CampSettlement(
    val stage: CampSettlementStage,
    val faction: Faction,
    val changes: List<BattleUnitTurnChange>,
    /** subflows: 상태 정산 뒤 순서대로 재생할 오라와 성장 표현 흐름이다. */
    val subflows: List<SettlementSubflow> = emptyList(),
    /** subflowsCaptured: 하위 표현 흐름을 이미 계산해 중복 생성하지 않음을 나타낸다. */
    val subflowsCaptured: Boolean = false,
)

/** SettlementSubflow: 진영 정산 중 표시할 지역 오라와 성장 결과의 공통 흐름이다. */
sealed interface SettlementSubflow {
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
         * `focusDelaySeconds` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val focusDelaySeconds: Float = .3f,
        /**
         * `soundIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val soundIndex: Int = 39,
        /**
         * `infoSkillId` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val infoSkillId: Int = skillId,
        /**
         * `actionId` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val actionId: Int = 30,
        /**
         * `meffName` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val meffName: String? = null,
        /**
         * `targets` (List<String>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targets: List<String>,
        /**
         * `nestedChanges` (List<BattleUnitTurnChange>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val nestedChanges: List<BattleUnitTurnChange>,
    ) : SettlementSubflow

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
    ) : SettlementSubflow
}

/** SettlementGrowthKind: 정산으로 지급하는 유닛·무기·방어구 경험치의 종류를 구분한다. */
enum class SettlementGrowthKind { UNIT_EXP, WEAPON_EXP, ARMOR_EXP }

/** SettlementGrowthGrant: 성장 대상에 요청한 경험치와 실제 성장 결과를 함께 보관한다. */
data class SettlementGrowthGrant(
    val kind: SettlementGrowthKind,
    val requestedAmount: Int,
    val unitResult: CampaignExperienceResult? = null,
    val equipmentResult: CampaignEquipmentExperienceResult? = null,
) {
    /**
     * `requiresLevelUpPresentation` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val requiresLevelUpPresentation: Boolean
        get() = unitResult?.leveledUp == true || equipmentResult?.leveledUp == true
    /**
     * `requiresItemUpgradeCallback` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val requiresItemUpgradeCallback: Boolean get() = equipmentResult?.leveledUp == true
}

/** RestoreGrowthResolution: 복원 효과의 성장 정산이 적용 가능·불가·적용 완료 중 어디에 속하는지 나타낸다. */
sealed interface RestoreGrowthResolution<out T> {
    /**
     * `NotApplicable` 싱글턴 객체: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data object NotApplicable : RestoreGrowthResolution<Nothing>
    /**
     * `Unavailable` 싱글턴 객체: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data object Unavailable : RestoreGrowthResolution<Nothing>
    /**
     * `Applied` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Applied<T>(val value: T) : RestoreGrowthResolution<T>
}
