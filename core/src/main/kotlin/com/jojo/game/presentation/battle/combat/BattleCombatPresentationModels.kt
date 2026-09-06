// Battle
package com.jojo.game.presentation.battle.combat

import com.jojo.game.domain.battle.PhysicalAttackPass
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.infrastructure.data.GameDataCatalog

/** 물리 공격 재생 대기열: 공격 패스와 시각 체력·기력·반격 마법 진행 상태를 보관한다. */
internal data class PhysicalPassPresentationQueue(
    val passes: List<PhysicalAttackPass>,
    var nextPassIndex: Int,
    var startsAt: Float,
    val visualHp: MutableMap<String, Int>,
    val visualMp: MutableMap<String, Int>,
    val counterMagicId: Int?,
    val counterMagic: TacticalActionResult.Magic?,
    val counterCasterId: String,
    val counterTargetId: String,
    val presentedSpeechPasses: MutableSet<Int> = linkedSetOf(),
    var awaitingSpeechPassIndex: Int? = null,
    var counterMagicSpeechPresented: Boolean = false,
    var counterMagicAwaitingSpeech: Boolean = false,
)

/** 마법 공격 재생 대기열: 마법 결과, 원본 프로필, 대상 효과와 패스별 대사 진행 상태를 보관한다. */
internal data class MagicPassPresentationQueue(
    val result: TacticalActionResult.Magic,
    val casterId: String,
    val targetId: String?,
    val profile: GameDataCatalog.MagicProfile?,
    val effectId: Int,
    var nextPassIndex: Int,
    var startsAt: Float,
    val visualHp: MutableMap<String, Int>,
    val visualMp: MutableMap<String, Int>,
    val reaction: Boolean = false,
    val presentedSpeechPasses: MutableSet<Int> = linkedSetOf(),
    var awaitingSpeechPassIndex: Int? = null,
)

/** 치명타 대사 대기 상태: 대사를 마친 뒤 이어갈 전술 행동과 당시 생명력 정보를 보관한다. */
internal data class PendingCriticalSpeechAction(
    val result: TacticalActionResult,
    val unitName: String,
    val actorId: String?,
    val magicId: Int?,
    val targetId: String?,
    val healthBeforeAction: Map<String, Int>,
    val moveActorId: String?,
    val continueBattleScript: Boolean,
)

/** 진행 중인 반격 마법 상태: 효과가 끝날 때까지 행동을 보류할 유닛과 종료 시각을 정의한다. */
internal data class ActiveCounterMagicPresentation(val unitIds: Set<String>, val endsAt: Float)

/** 반격 재생 대기 상태: 첫 반격 피해와 이어질 추가타 정보를 시간순으로 보관한다. */
internal data class CounterPresentation(
    val attackerId: String,
    val targetId: String,
    val harm: Int,
    val targetHpBefore: Int,
    val critical: Boolean,
    val mpShieldDamage: Int,
    val followUpDamage: Int,
    val followUpMpShieldDamage: Int,
    val followUpCritical: Boolean,
    val startsAt: Float,
)

/** 반격 추가타 재생 대기 상태: 반격 후 이어지는 단일 피해·보호막 피해·치명타 정보를 정의한다. */
internal data class CounterFollowUpPresentation(
    val attackerId: String,
    val targetId: String,
    val harm: Int,
    val targetHpBefore: Int,
    val critical: Boolean,
    val mpShieldDamage: Int,
    val startsAt: Float,
)

/** 공격 추가타 재생 대기 상태: 추가 공격과 그 뒤 반격에 필요한 피해·회복·체력 기준값을 정의한다. */
internal data class FollowUpPresentation(
    val attackerId: String,
    val targetId: String,
    val harm: Int,
    val targetHpBefore: Int,
    val critical: Boolean,
    val mpShieldDamage: Int,
    val startsAt: Float,
    val counterDamage: Int,
    val counterMpShieldDamage: Int,
    val counterCritical: Boolean,
    val counterLifeStealHealing: Int,
    val counterTargetHpBefore: Int,
)
