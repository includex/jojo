package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.magic.BattleMagicProfile

/**
 * data class  `BattleUnit`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
    /** Unit.EXP. Enemy/Friend actors keep this battle-local; Mine is persisted by the factory. */
    var experience: Int = 0,
    /** Current Unit.posts(), which can differ from the base table after promotion. */
    var posts: Int = 0,
    var attack: Int = 45,
    var defense: Int = 25,
    var spirit: Int = 35,
    var critical: Int = 35,
    var morale: Int = 35,
    /** Unit.wuwei(UNIT_ATTR_NAME2.WL), independent of final attack ability. */
    val martial: Int = attack,
    var armId: Int = 0,
    /** Original ARM_TYPE: 0 all-rounder, 1 civil officer, 2 martial officer. */
    var armType: Int = 0,
    var remoteAttack: Boolean = false,
    /** ARM_ATTR_NAME.MOVESOUND, read by countOtherHarm's KZQB branch. */
    var armMoveSound: Int = 0,
    /** BattleUnit.moveSpeed(): ARM_ATTR_NAME.MOVESPEED == 0. */
    var fastMove: Boolean = true,
    /** Original armAttr2(arm, ATTACKDELAY). */
    var attackDelay: Boolean = false,
    var armRestraints: Map<Int, Int> = emptyMap(),
    var terrainImpacts: Map<Int, Int> = emptyMap(),
    /** Original arms[n].terrain.expend values; 255 marks an impassable terrain. */
    var terrainMovementCosts: Map<Int, Int> = emptyMap(),
    var magicHarmRate: Int = 100,
    var attackOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    /** Unit.effarea(): ZHUORE(0), overridden by SKILL_TYPE.CTGJ's value. */
    var attackEffectOffsets: Set<Pair<Int, Int>> = emptySet(),
    /** Null is an injected custom shape; non-null is Config.EFFAREA ID. */
    var attackEffectAreaId: Int? = null,
    var attackAllScreen: Boolean = false,
    var magic: List<BattleMagicProfile> = emptyList(),
    /** Original Unit.skills() map: SKILL_TYPE id to its resolved effect value. */
    var skills: Map<Int, Int> = emptyMap(),
    val statuses: MutableMap<BattleStatus, Int> = linkedMapOf(),
    val attributeLifts: MutableMap<BattleAttribute, Int> = linkedMapOf(),
    val attributeLiftRounds: MutableMap<BattleAttribute, Int> = linkedMapOf(),
    var movement: Int = 3,
    var ai: Int = 0,
    var aiTargetCharacterId: Int = -1,
    var aiTargetX: Int = 0,
    var aiTargetY: Int = 0,
    /** BattleUnit.AIValue(), set from Control._AIProcess's chosen action score. */
    var aiValue: Int = 0,
    /** BattleUnit.rate/setRate: JQ_BDMZL..JQ_BBJL's eight source gauges. */
    val rateAccumulators: MutableMap<Int, Int> = linkedMapOf(),
    var hasActed: Boolean = false,
    /** Original movement is independent from the action-complete (XD) state. */
    var hasMoved: Boolean = false,
    var visible: Boolean = true,
    /** BattleUnit.setOhterNodeVisible(false) during BattleScreen.unitHide. */
    var otherNodesVisible: Boolean = true,
    /** BattleUnitFlag.RETREAT and Unit.incRetreat() state. */
    var retreatFlag: Boolean = false,
    var retreatCount: Int = 0,
    /** Unit.retireMessage(), used only by BAI_TUI unitHide. */
    val retireMessage: String? = null,
    /** Unit.getCritTxt data; BattleUnit.checkCrit exposes it on alternating criticals. */
    val criticalSpeech: GameDataCatalog.CriticalSpeechProfile = GameDataCatalog.CriticalSpeechProfile(
        emptyList(),
        false
    ),
    var criticalSpeechChecks: Int = 0,
    /** BATTLE_UNIT_FALG.DEATH_MSG, independently mutable through retreatTxt(). */
    var deathMessageEnabled: Boolean = faction == Faction.PLAYER,
    /** Facing direction: 0=up, 1=right, 2=down, 3=left. */
    var direction: Int = 2,
    /** Stable character identity used by game data and scenario commands. */
    val characterId: Int? = null,
    /** Unit.isFamous(), which selects BattleScreen.hpbars[4] for enemy units. */
    val famous: Boolean = false,
    /** Whether the authored battle record supplied each coordinate field. */
    var hasAuthoredTileX: Boolean = true,
    var hasAuthoredTileY: Boolean = true,
    /**
     * Stable battle-instance slot. This differs from [characterId]:
     * repeated createEnemy calls can create several actors from one character
     * and place them in the 60/140/220 slot blocks.
     */
    val battleSlot: Int? = null,
) {
    /** Packed STATUS_ROUND slot for BATTLE_UNIT_STATUS2.XD (source index 14). */
    var actionStatusRound: Int = if (hasActed) 1 else 0

    /** `setStateRound(XD)` writes its round before publishing the status. */
    fun markActionComplete() {
        actionStatusRound = 1
        hasActed = true
    }

    /**
     * Moves an attribute's DOWN/NORMAL/UP value only one step toward the
     * requested lift, so an opposite request first neutralizes the current lift.
     */
    /**
     * 공개 메서드 `applyAttributeLift`
     *
     * ### 파라미터
    - `attribute` (`BattleAttribute`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `requested` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `rounds` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /** Apply Unit.setLevel's model/cache refresh without disturbing tactical state. */
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

    /** Exact Unit.refAbilityPhase surface: ATT..MOR and HP/MP only. */
    fun refreshAbilityPhase(source: BattleUnit) {
        maxHitPoints = source.maxHitPoints
        maxMagicPoints = source.maxMagicPoints
        attack = source.attack
        defense = source.defense
        spirit = source.spirit
        critical = source.critical
        morale = source.morale
    }

    /**
     * Rebuild the live projection after Unit.setPosts.  Unlike level refresh,
     * promotion changes its arm/range tables as well as derived ability,
     * posts-skill and magic caches.  Deliberately retain tactical location,
     * current HP/MP, statuses, turn state and the currently loaded avatar.
     */
    /**
     * 공개 메서드 `refreshPostsDerivedState`
     *
     * ### 파라미터
    - `source` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `refreshAbilityPhase` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
        // `Unit.setPosts` invokes refAbilityPhase only through its second
        // flags/Mine gate.  Enemy/Friend callers still refresh posts skills,
        // magics, arm and movement, but retain their stored ATT..MP values.
        if (refreshAbilityPhase) refreshLevelDerivedState(source)
        else {
            movement = source.movement
            skills = source.skills
            magic = source.magic
        }
    }

    /** Authored `BattleUnit.camp()`; LOST never overwrites this value. */
    val baseFaction: Faction get() = faction

    /** Source `BattleUnit.type(false)` including MS/迷失 camp inversion. */
    fun effectiveFaction(ignoreLost: Boolean = false): Faction {
        if (ignoreLost || BattleStatus.LOST !in statuses) return baseFaction
        return if (baseFaction.isPlayerSide()) Faction.REINFORCEMENTS else Faction.FRIEND
    }

    /** Source-compatible spelling: `type(true)` requests the base camp. */
    fun type(baseCamp: Boolean = false): Faction = effectiveFaction(ignoreLost = baseCamp)

    /** Source `BattleUnit.isMine(baseCamp)` without losing authored camp. */
    fun isPlayerSide(useBaseFaction: Boolean = false): Boolean = effectiveFaction(useBaseFaction).isPlayerSide()

    /** Removes one abnormal state from this battle-domain unit. */
    fun cureStatus(status: BattleStatus): Boolean {
        if (statuses.remove(status) == null) return false
        return true
    }

    /** Clears every abnormal state from this battle-domain unit. */
    fun cureAllStatuses(): Boolean {
        if (statuses.isEmpty()) return false
        statuses.clear()
        return true
    }

    /** Restores the mutable combat state required when a retained unit rejoins battle. */
    fun resetAfterRetreat() {
        setHpcur(maxHitPoints)
        setMpcur(maxMagicPoints)
        statuses.clear()
        attributeLifts.clear()
        attributeLiftRounds.clear()
        hasMoved = false
    }

    /** Replaces status and attribute-lift state as one memento restoration command. */
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

    /** Direct Kotlin implementation of BattleUnit.addHpcur(t, e=0). */
    fun addHpcur(value: Int, keepAlive: Boolean = false) =
        setHpcur((hitPoints + value).let { if (keepAlive) maxOf(1, it) else it })

    /** Direct Kotlin implementation of BattleUnit.addMpcur(t). */
    fun addMpcur(value: Int) = setMpcur(magicPoints + value)

    /** Direct Kotlin implementation of BattleUnit.setHpcur(t) → setCurHp(t). */
    fun setHpcur(value: Int) = setCurHp(value)

    /** Direct Kotlin implementation of BattleUnit.setMpcur(t) → setCurMp(t). */
    fun setMpcur(value: Int) = setCurMp(value)

    /** BattleUnit.setCurHp clamps to [0, hp]. */
    fun setCurHp(value: Int) {
        hitPoints = value.coerceIn(0, maxHitPoints)
    }

    /** BattleUnit.setCurMp clamps to [0, mp]. */
    fun setCurMp(value: Int) {
        magicPoints = value.coerceIn(0, maxMagicPoints)
    }
}
