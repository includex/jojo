// Battle
package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit

/** MagicLocalSettlementEntry: 마법이 한 대상에게 적용되기 전후의 상태 이상과 능력치 증감을 보관한다. */

data class MagicLocalSettlementEntry(
    val targetId: String,
    val statusesBefore: Map<BattleStatus, Int>,
    val statusesAfter: Map<BattleStatus, Int>,
    val attributeLiftsBefore: Map<BattleAttribute, Int>,
    val attributeLiftsAfter: Map<BattleAttribute, Int>,
    val hasStatesPayload: Boolean,
    val attributeLiftRoundsBefore: Map<BattleAttribute, Int> = emptyMap(),
    val attributeLiftRoundsAfter: Map<BattleAttribute, Int> = emptyMap(),
)


/** MagicLocalSettlement: 한 마법 패스에서 대상별로 적용한 지역 상태 정산 목록이다. */
data class MagicLocalSettlement(val entries: List<MagicLocalSettlementEntry>)

/** TacticalActionResult: 물리·마법·아이템 전술 행동의 성공 여부와 계산 결과를 표현하는 결과 계층이다. */
sealed interface TacticalActionResult {
    /** Success: 추가 데이터 없이 전술 행동이 성공했음을 나타낸다. */
    data object Success : TacticalActionResult


    /** Rejected: 전술 행동을 실행할 수 없는 이유를 전달한다. */
    data class Rejected(val reason: String) : TacticalActionResult


    /** Attack: 물리 공격의 대상 피해·반격·회복·추가 효과를 한 결과로 보관한다. */
    data class Attack(
        val damage: Int,
        val defeated: Boolean,
        val hitRate: Int = 100,
        val hit: Boolean = true,
        val critical: Boolean = false,
        val counterDamage: Int = 0,
        val attackerDefeated: Boolean = false,
        val lifeStealHealing: Int = 0,
        val followUpDamage: Int = 0,
        val followUpMpShieldDamage: Int = 0,
        val counterFollowUpDamage: Int = 0,
        val counterMpShieldDamage: Int = 0,
        val counterFollowUpMpShieldDamage: Int = 0,
        val counterLifeStealHealing: Int = 0,
        val followUpCritical: Boolean = false,
        val counterCritical: Boolean = false,
        val counterFollowUpCritical: Boolean = false,
        val splashTargets: List<PhysicalTarget> = emptyList(),
        val mpShieldDamage: Int = 0,
        val qxlHealing: Int = 0,
        val recoilDamage: Int = 0,
        val blockRetaliationDamage: Int = 0,
        val moneyShieldSpent: Int = 0,
        val playerMoneyDelta: Int = 0,
        val enemyMoneyDelta: Int = 0,
        val counterMagic: Magic? = null,
        val counterMagicId: Int? = null,
        val automaticProperty: Item? = null,
        val physicalPasses: List<PhysicalAttackPass> = emptyList(),
    ) : TacticalActionResult


    /** Magic: 마법 사용에 따른 비용·대상·피해·지역 정산 결과를 보관한다. */
    data class Magic(
        val name: String,
        val cost: Int,
        val targets: List<MagicTarget>,
        val passes: List<List<MagicTarget>> = listOf(targets),
        val critical: Boolean = false,
        val criticalSpeeches: List<String?> = List(passes.size) { null },
        val localSettlements: List<MagicLocalSettlement> = List(passes.size) { MagicLocalSettlement(emptyList()) },
    ) : TacticalActionResult


    /** Item: 아이템 사용의 대상과 적용 효과를 전달한다. */
    data class Item(val name: String, val target: String, val effect: String) : TacticalActionResult
}

/** BattlePropertyItem: 공격 뒤 자동으로 발동할 수 있는 전투 속성 아이템의 정의이다. */
data class BattlePropertyItem(val id: Int, val name: String, val itemType: Int, val value: Int)


/** MagicTarget: 마법이 대상에게 남긴 피해·회복·상태 이상·능력치 증감 결과를 보관한다. */
data class MagicTarget(
    val targetId: String,
    val damage: Int,
    val healing: Int = 0,
    val status: BattleStatus? = null,
    val hitRate: Int,
    val hit: Boolean,
    val defeated: Boolean,
    val attribute: BattleAttribute? = null,
    val lift: Int = 0,
    /** attributes: 패기·쇠기처럼 여러 능력치 증감을 한 번에 적용하는 값이다. */
    val attributes: Map<BattleAttribute, Int> = emptyMap(),
    val magicRecovery: Int = 0,
    val magicDrain: Int = 0,
    val casterHealing: Int = 0,
)

/** PhysicalTarget: 물리 공격 대상의 식별자·피해량·명중률을 간단히 표현한다. */
data class PhysicalTarget(val targetId: String, val damage: Int, val hitRate: Int = 100)

/** PhysicalAttackPassKind: 주공격·추가 공격·반격·반격 추가 공격의 순서를 구분한다. */
enum class PhysicalAttackPassKind { ACTIVE, ACTIVE_FOLLOW_UP, COUNTER, COUNTER_FOLLOW_UP }

/** PhysicalBlockRetaliationKind: 방어 반격으로 발생하는 혼란과 마비의 종류를 구분한다. */
enum class PhysicalBlockRetaliationKind { MENG_JI_CONFUSION, NI_FAN_PARALYSIS }

/** PhysicalBlockRetaliation: 방어 반격이 대상에게 주는 유형과 피해량을 보관한다. */
data class PhysicalBlockRetaliation(
    val kind: PhysicalBlockRetaliationKind,
    val damage: Int,
)

/** PhysicalAttackTargetResult: 물리 공격 한 대상의 실제 피해·보호막·회복·반격·상태 정산 결과이다. */
data class PhysicalAttackTargetResult(
    val targetId: String,
    val resolvedHarm: Int,
    val damage: Int,
    val mpShieldDamage: Int = 0,
    val moneyShieldSpent: Int = 0,
    val lifeStealHealing: Int = 0,
    val qxlHealing: Int = 0,
    val recoilDamage: Int = 0,
    val blockRetaliations: List<PhysicalBlockRetaliation> = emptyList(),
    val playerMoneyDelta: Int = 0,
    val enemyMoneyDelta: Int = 0,
    val automaticPropertyId: Int? = null,
    val automaticProperty: TacticalActionResult.Item? = null,
    val automaticPropertyHpDelta: Int = 0,
    val automaticPropertyMpDelta: Int = 0,
    val automaticPropertyCallbackCount: Int = 0,
    val backMove: PhysicalBackMove? = null,
    val localStatusSettlement: MagicLocalSettlement = MagicLocalSettlement(emptyList()),
    val hasLocalStatusSettlement: Boolean = false,
    val defeated: Boolean = false,
)


/** PhysicalBackMove: 피격 뒤 밀려나는 유닛의 시작·도착 좌표와 이동 시간을 보관한다. */
data class PhysicalBackMove(
    val fromX: Int,
    val fromY: Int,
    val toX: Int,
    val toY: Int,
    val durationSeconds: Float = .08f,
)

/** PhysicalAttackPass: 한 공격 패스의 공격자·대상 결과·필살 대사 정보를 묶어 표현 순서를 유지한다. */
data class PhysicalAttackPass(
    val kind: PhysicalAttackPassKind,
    val attackerId: String,
    val critical: Boolean,
    val targets: List<PhysicalAttackTargetResult>,
    val primaryTargetId: String? = targets.firstOrNull()?.targetId,
    val criticalSpeech: String? = null,
)
