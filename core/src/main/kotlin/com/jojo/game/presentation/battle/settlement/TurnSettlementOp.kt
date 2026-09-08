// Battle Settlement
package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.battle.settlement.SettlementGrowthGrant
import com.jojo.game.domain.battle.settlement.SettlementUnitPlan
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult

/** 전투 정산 화면이 실행할 순서와 각 단계의 입력을 표현한다. */
internal sealed interface TurnSettlementOp {
    /** 특정 유닛에 카메라 초점을 맞춘다. */
    data class Focus(val unitId: String, val seconds: Float, val forceCenter: Boolean) : TurnSettlementOp

    /** 정산 효과음을 재생한다. */
    data class Sound(val soundIndex: Int) : TurnSettlementOp

    /** 정산 안내 문구를 표시한다. */
    data class Info2(val text: String) : TurnSettlementOp

    /** 유닛의 행동 연출 목록을 실행한다. */
    data class Actions(val unitId: String, val actionIds: List<Int>) : TurnSettlementOp

    /**
     * 유닛 정보 정산 패널을 표시한다.
     *
     * 원본 `_jiesuan`은 유닛 하나마다 체력·기력과 경험치를 한 객체(`O`)에 모아 `MineUnitInfoLayer`
     * 한 장으로 보여 준다. [grants]가 비어 있지 않으면 그 한 장짜리 표시에 해당한다.
     */
    data class UnitInfo(
        val plan: SettlementUnitPlan,
        val grants: List<SettlementGrowthGrant> = emptyList(),
    ) : TurnSettlementOp

    /** 성장 보상과 능력치 상승 정보를 표시한다. */
    data class GrowthInfo(val unitId: String, val grants: List<SettlementGrowthGrant>) : TurnSettlementOp

    /** 정산 중 마법 효과를 표시한다. */
    data class Meff(val effectId: Int, val targetIds: List<String>) : TurnSettlementOp

    /** 장비 경험치 상승 결과를 표시한다. */
    data class ItemUpgrade(val unitId: String, val result: CampaignEquipmentExperienceResult) : TurnSettlementOp

    /** 유닛 상태 아이콘을 숨긴다. */
    data class HideState(val unitIds: List<String>) : TurnSettlementOp

    /** 정산이 끝난 유닛 화면을 갱신한다. */
    data class Refresh(val unitIds: List<String>) : TurnSettlementOp

    /** 유닛의 기본 대기 행동을 실행한다. */
    data class Default(val unitId: String) : TurnSettlementOp
}
