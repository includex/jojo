// Battle
package com.jojo.game.domain.battle
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.*
import com.jojo.game.domain.battle.magic.BattleMagicProfile


/** BattleUnit: 전장에 배치된 유닛의 기본 정보·능력치·행동 상태를 함께 보관하는 전술 모델이다. */
data class BattleUnit(
    val id: String,
    val name: String,
    val faction: Faction,
    var tileX: Int,
    var tileY: Int,
    var hitPoints: Int = 100,
    var maxHitPoints: Int = hitPoints,
    var magicPoints: Int = 0,
    var maxMagicPoints: Int = magicPoints,
    var level: Int = 1,
    /** experience: 전투 중 획득해 레벨과 성장 정산에 반영할 경험치이다. */
    var experience: Int = 0,
    /** posts: 유닛의 직위 식별자로, 파생 능력치와 스킬 구성을 결정한다. */
    var posts: Int = 0,
    var attack: Int = 45,
    var defense: Int = 25,
    var spirit: Int = 35,
    var critical: Int = 35,
    var morale: Int = 35,
    /** martial: 기본 공격력을 보존하는 무력 값으로, 직위 갱신 전후 비교에 사용한다. */
    val martial: Int = attack,
    var armId: Int = 0,
    /** armType: 장비한 무기의 전투 유형으로, 상성 및 공격 규칙을 결정한다. */
    var armType: Int = 0,
    var remoteAttack: Boolean = false,
    /** armMoveSound: 무기 유형에 따라 재생할 이동 효과음 식별자이다. */
    var armMoveSound: Int = 0,
    /** fastMove: 이동 애니메이션을 빠른 속도로 재생할지 나타낸다. */
    var fastMove: Boolean = true,
    /** attackDelay: 공격 표현 전에 지연 시간을 적용할지 나타낸다. */
    var attackDelay: Boolean = false,
    var armRestraints: Map<Int, Int> = emptyMap(),
    var terrainImpacts: Map<Int, Int> = emptyMap(),
    /** terrainMovementCosts: 지형별 이동 비용으로, 도달 가능한 타일 계산에 사용한다. */
    var terrainMovementCosts: Map<Int, Int> = emptyMap(),
    var magicHarmRate: Int = 100,
    var attackOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    /** attackEffectOffsets: 공격 효과가 퍼지는 상대 좌표 목록이다. */
    var attackEffectOffsets: Set<Pair<Int, Int>> = emptySet(),
    /** attackEffectAreaId: 공격 효과 범위를 식별하는 원본 영역 번호이다. */
    var attackEffectAreaId: Int? = null,
    var attackAllScreen: Boolean = false,
    var magic: List<BattleMagicProfile> = emptyList(),
    /** skills: 유닛이 보유한 스킬과 해당 스킬의 레벨 또는 값 목록이다. */
    var skills: Map<Int, Int> = emptyMap(),
    val statuses: MutableMap<BattleStatus, Int> = linkedMapOf(),
    val attributeLifts: MutableMap<BattleAttribute, Int> = linkedMapOf(),
    val attributeLiftRounds: MutableMap<BattleAttribute, Int> = linkedMapOf(),
    var movement: Int = 3,
    var ai: Int = 0,
    var aiTargetCharacterId: Int = -1,
    var aiTargetX: Int = 0,
    var aiTargetY: Int = 0,
    /** aiValue: AI가 행동 대상을 평가할 때 사용하는 현재 점수이다. */
    var aiValue: Int = 0,
    /** rateAccumulators: 확률 판정마다 누적하는 게이지 값으로, 반복 실패 보정에 사용한다. */
    val rateAccumulators: MutableMap<Int, Int> = linkedMapOf(),
    var hasActed: Boolean = false,
    /** hasMoved: 현재 턴에 이미 이동했는지 나타내며, 재이동을 제한한다. */
    var hasMoved: Boolean = false,
    var visible: Boolean = true,
    /** otherNodesVisible: 유닛 외의 전장 노드를 함께 표시할지 나타낸다. */
    var otherNodesVisible: Boolean = true,
    /** retreatFlag: 퇴각 처리 중인 유닛임을 표시해 일반 사망 처리와 구분한다. */
    var retreatFlag: Boolean = false,
    var retreatCount: Int = 0,
    /** retireMessage: 퇴각 시 표시할 대사 또는 알림 문자열이다. */
    val retireMessage: String? = null,
    /** criticalSpeech: 필살 공격 시 사용할 대사 후보와 재생 규칙이다. */
    val criticalSpeech: GameDataCatalog.CriticalSpeechProfile = GameDataCatalog.CriticalSpeechProfile(
        emptyList(),
        false
    ),
    var criticalSpeechChecks: Int = 0,
    /** deathMessageEnabled: 유닛 사망 대사를 화면에 표시할지 나타낸다. */
    var deathMessageEnabled: Boolean = faction == Faction.PLAYER,
    /** direction: 전장 스프라이트가 바라보는 방향으로, 이동과 공격 표현에 사용한다. */
    var direction: Int = 2,
    /** characterId: 원본 캐릭터 데이터와 연결하는 인물 식별자이다. */
    val characterId: Int? = null,
    /** famous: 이름 있는 주요 인물인지 나타내며, 표현과 시나리오 규칙에 사용한다. */
    val famous: Boolean = false,
    /** hasAuthoredTileX: 원본 시나리오가 X 좌표를 명시했는지 나타낸다. */
    var hasAuthoredTileX: Boolean = true,
    var hasAuthoredTileY: Boolean = true,
    /** battleSlot: 편성 화면에서 유닛을 식별하는 전투 배치 슬롯 번호이다. */
    val battleSlot: Int? = null,
) {
    /** actionStatusRound: 행동 상태가 기록된 라운드 번호로, 턴별 초기화 판단에 사용한다. */
    var actionStatusRound: Int = if (hasActed) 1 else 0

    /** markActionComplete: 유닛이 현재 턴의 행동을 마쳤음을 기록한다. */
    fun markActionComplete() {
        actionStatusRound = 1
        hasActed = true
    }

    /** applyAttributeLift: 요청한 능력치 증감을 한 단계 적용하고, 남은 지속 라운드를 기록한다. */

    fun applyAttributeLift(attribute: BattleAttribute, requested: Int, rounds: Int): Int {
        val current = (attributeLifts[attribute] ?: 0).coerceIn(-1, 1)
        val target = requested.coerceIn(-1, 1)
        val next = when {
            current < target -> current + 1
            current > target -> current - 1
            else -> current
        }
        if (next == 0) attributeLifts.remove(attribute) else attributeLifts[attribute] = next
        attributeLiftRounds[attribute] = rounds.coerceIn(0, 3)
        return next
    }

    /** refreshLevelDerivedState: 성장한 원본 유닛의 레벨 기반 능력치와 전투 구성을 복사한다. */
    fun refreshLevelDerivedState(source: BattleUnit) {
        level = source.level
        maxHitPoints = source.maxHitPoints
        maxMagicPoints = source.maxMagicPoints
        attack = source.attack
        defense = source.defense
        spirit = source.spirit
        critical = source.critical
        morale = source.morale
        movement = source.movement
        skills = source.skills
        magic = source.magic
        attackOffsets = source.attackOffsets
        attackEffectOffsets = source.attackEffectOffsets
        attackEffectAreaId = source.attackEffectAreaId
        attackAllScreen = source.attackAllScreen
    }

    /** refreshAbilityPhase: 원본 유닛의 체력·정신력·공방 능력치만 전투 유닛에 갱신한다. */
    fun refreshAbilityPhase(source: BattleUnit) {
        maxHitPoints = source.maxHitPoints
        maxMagicPoints = source.maxMagicPoints
        attack = source.attack
        defense = source.defense
        spirit = source.spirit
        critical = source.critical
        morale = source.morale
    }

    /** refreshPostsDerivedState: 직위 변경으로 달라진 장비·스킬·마법·이동 관련 파생값을 갱신한다. */

    fun refreshPostsDerivedState(source: BattleUnit, refreshAbilityPhase: Boolean) {
        posts = source.posts
        armId = source.armId
        armType = source.armType
        remoteAttack = source.remoteAttack
        armMoveSound = source.armMoveSound
        fastMove = source.fastMove
        attackDelay = source.attackDelay
        armRestraints = source.armRestraints
        terrainImpacts = source.terrainImpacts
        terrainMovementCosts = source.terrainMovementCosts
        magicHarmRate = source.magicHarmRate
        attackOffsets = source.attackOffsets
        attackEffectOffsets = source.attackEffectOffsets
        attackEffectAreaId = source.attackEffectAreaId
        attackAllScreen = source.attackAllScreen
        // 직위 갱신은 두 번째 플래그와 아군 진영 조건에서만 능력치 단계를 다시 계산한다.
        // 적군·우군도 직위 스킬·마법·무기·이동은 갱신하지만 기존 능력치 값은 유지한다.
        if (refreshAbilityPhase) refreshLevelDerivedState(source)
        else {
            movement = source.movement
            skills = source.skills
            magic = source.magic
        }
    }

    /** baseFaction: 상태 이상과 무관한 유닛의 원래 소속 진영이다. */
    val baseFaction: Faction get() = faction

    /** effectiveFaction: 이탈 상태를 반영해 현재 전투에서 적용할 소속 진영을 반환한다. */
    fun effectiveFaction(ignoreLost: Boolean = false): Faction {
        if (ignoreLost || BattleStatus.LOST !in statuses) return baseFaction
        return if (baseFaction.isPlayerSide()) Faction.REINFORCEMENTS else Faction.FRIEND
    }

    /** type: 기본 진영 사용 여부에 따라 전술 처리용 진영을 반환한다. */
    fun type(baseCamp: Boolean = false): Faction = effectiveFaction(ignoreLost = baseCamp)

    /** isPlayerSide: 유닛이 플레이어 편으로 취급되는지 판정한다. */
    fun isPlayerSide(useBaseFaction: Boolean = false): Boolean = effectiveFaction(useBaseFaction).isPlayerSide()

    /** cureStatus: 지정한 상태 이상을 제거하고 실제 제거 여부를 반환한다. */
    fun cureStatus(status: BattleStatus): Boolean {
        if (statuses.remove(status) == null) return false
        return true
    }

    /** cureAllStatuses: 적용 중인 모든 상태 이상을 제거하고 변경 여부를 반환한다. */
    fun cureAllStatuses(): Boolean {
        if (statuses.isEmpty()) return false
        statuses.clear()
        return true
    }

    /** resetAfterRetreat: 퇴각 뒤 체력·기력·상태 이상·이동 상태를 초기값으로 되돌린다. */
    fun resetAfterRetreat() {
        setHpcur(maxHitPoints)
        setMpcur(maxMagicPoints)
        statuses.clear()
        attributeLifts.clear()
        attributeLiftRounds.clear()
        hasMoved = false
    }

    /** restoreStatusState: 기록된 상태 이상과 능력치 증감 정보를 유닛에 복원한다. */
    fun restoreStatusState(
        restoredStatuses: Map<BattleStatus, Int>,
        restoredAttributeLifts: Map<BattleAttribute, Int>,
        restoredAttributeLiftRounds: Map<BattleAttribute, Int>,
    ) {
        statuses.clear()
        statuses.putAll(restoredStatuses)
        attributeLifts.clear()
        attributeLifts.putAll(restoredAttributeLifts)
        attributeLiftRounds.clear()
        attributeLiftRounds.putAll(restoredAttributeLiftRounds)
    }

    /** addHpcur: 현재 체력에 변화를 더하고, 필요하면 생존 체력 하한을 유지한다. */
    fun addHpcur(value: Int, keepAlive: Boolean = false) =
        setHpcur((hitPoints + value).let { if (keepAlive) maxOf(1, it) else it })

    /** addMpcur: 현재 기력에 변화를 더한 뒤 허용 범위로 제한한다. */
    fun addMpcur(value: Int) = setMpcur(magicPoints + value)

    /** setHpcur: 현재 체력을 최대 체력 범위 안의 값으로 설정한다. */
    fun setHpcur(value: Int) = setCurHp(value)

    /** setMpcur: 현재 기력을 최대 기력 범위 안의 값으로 설정한다. */
    fun setMpcur(value: Int) = setCurMp(value)

    /** setCurHp: 체력 값을 0과 최대 체력 사이로 제한해 저장한다. */
    fun setCurHp(value: Int) {
        hitPoints = value.coerceIn(0, maxHitPoints)
    }

    /** setCurMp: 기력 값을 0과 최대 기력 사이로 제한해 저장한다. */
    fun setCurMp(value: Int) {
        magicPoints = value.coerceIn(0, maxMagicPoints)
    }
}
