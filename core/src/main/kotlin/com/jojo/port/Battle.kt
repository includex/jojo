package com.jojo.port

import java.util.Random

/** Original BATTLE_CAMP: Mine, Friend, Enemy, Reinforcements. */
enum class Faction { PLAYER, FRIEND, ENEMY, REINFORCEMENTS }

fun Faction.isEnemySide(): Boolean = this == Faction.ENEMY || this == Faction.REINFORCEMENTS
fun Faction.isPlayerSide(): Boolean = !isEnemySide()

/** Original BATTLE_UNIT_STATUS2 persistent abnormal states. */
enum class BattleStatus {
    PARALYSIS, SILENCE, CONFUSION, POISON, LOST;

    companion object {
        fun fromSourceIndex(index: Int): BattleStatus? = when (index) {
            7 -> PARALYSIS
            8 -> SILENCE
            9 -> CONFUSION
            10 -> POISON
            13 -> LOST
            else -> null
        }
    }
}
/** Original BATTLE_UNIT_STATUS2 0..5: temporary ability lift/down states. */
enum class BattleAttribute { ATTACK, DEFENSE, SPIRIT, CRITICAL, MORALE, MOVEMENT }
enum class BattleWeather { CLEAR, CLOUDY, WINDY, HEAVY_RAIN, SNOW }

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
    var magic: List<OriginalGameData.MagicProfile> = emptyList(),
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
    /** BattleUnit.setOhterNodeVisible(false) during BattleLayer.unitHide. */
    var otherNodesVisible: Boolean = true,
    /** BattleUnitFlag.RETREAT and Unit.incRetreat() state. */
    var retreatFlag: Boolean = false,
    var retreatCount: Int = 0,
    /** Unit.retireMessage(), used only by BAI_TUI unitHide. */
    val retireMessage: String? = null,
    /** Unit.getCritTxt data; BattleUnit.checkCrit exposes it on alternating criticals. */
    val criticalSpeech: OriginalGameData.CriticalSpeechProfile = OriginalGameData.CriticalSpeechProfile(emptyList(), false),
    var criticalSpeechChecks: Int = 0,
    /** BATTLE_UNIT_FALG.DEATH_MSG, independently mutable through retreatTxt(). */
    var deathMessageEnabled: Boolean = faction == Faction.PLAYER,
    /** Original BattleUnit.dir (0=up, 1=right, 2=down, 3=left). */
    var direction: Int = 2,
    val sourceCharacterId: Int? = null,
    /** Unit.isFamous(), which selects BattleLayer.hpbars[4] for enemy units. */
    val famous: Boolean = false,
    /** Whether the authored battle record supplied each coordinate field. */
    var sourceTileXAuthored: Boolean = true,
    var sourceTileYAuthored: Boolean = true,
    /**
     * Config.js `_unitSet` index.  This differs from [sourceCharacterId]:
     * repeated createEnemy calls can create several actors from one character
     * and place them in the 60/140/220 slot blocks.
     */
    val sourceBattleSlot: Int? = null,
) {
    /** Packed STATUS_ROUND slot for BATTLE_UNIT_STATUS2.XD (source index 14). */
    var actionStatusRound: Int = if (hasActed) 1 else 0

    /** `setStateRound(XD)` writes its round before publishing the status. */
    fun markActionComplete() {
        actionStatusRound = 1
        hasActed = true
    }

    /**
     * BattleUnit._setStatus for ATT..MOV. The source moves the packed
     * DOWN/NORMAL/UP value only one step toward the requested lift, so an
     * opposite request first neutralizes the existing lift.
     */
    fun applySourceAttributeLift(attribute: BattleAttribute, requested: Int, rounds: Int): Int {
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

    /** Source `_state_meff` selection for MB/JZ/HL/ZD; renderer-independent. */
    val stateAnimation = BattleUnitStateAnimation()

    /** BattleUnit._refHpBar's ProgressBar value. */
    var hpBarProgress: Float = hpRatio()
        private set

    /** BattleUnit.showHarmNum's single `harmNum` child node. */
    var harmNumber: HarmNumber? = null
        private set

    /** BattleUnit.showHarmBar's target-preview state (bar0/bar1/bar2). */
    var harmBarPreview: BattleHarmBar.View = BattleHarmBar.View()
        private set

    /**
     * BattleUnit._refStatus's six authored `status/unit_status_<n>` nodes.
     * `down=true` selects statusImgs[0]; every other non-normal lift selects
     * statusImgs[1], exactly like the source ternary.
     */
    data class AttributeStatusIcon(val active: Boolean, val down: Boolean)
    var attributeStatusIcons: Map<BattleAttribute, AttributeStatusIcon> = emptyMap()
        private set

    init { refStateAnime() }

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

    /** Direct port of BattleUnit.addHpcur(t, e=0). */
    fun addHpcur(value: Int, keepAlive: Boolean = false) = setHpcur((hitPoints + value).let { if (keepAlive) maxOf(1, it) else it })

    /** Direct port of BattleUnit.addMpcur(t). */
    fun addMpcur(value: Int) = setMpcur(magicPoints + value)

    /** Direct port of BattleUnit._refHpBar(). */
    fun refHpBar() { hpBarProgress = hpRatio() }

    /** Direct port of BattleUnit.setHpcur(t) → setCurHp(t). */
    fun setHpcur(value: Int) = setCurHp(value)

    /** Direct port of BattleUnit.setMpcur(t) → setCurMp(t). */
    fun setMpcur(value: Int) = setCurMp(value)

    /** BattleUnit.setCurHp clamps to [0, hp] and immediately refreshes bar2. */
    fun setCurHp(value: Int) {
        hitPoints = value.coerceIn(0, maxHitPoints)
        refHpBar()
    }

    /** BattleUnit.setCurMp clamps to [0, mp]. */
    fun setCurMp(value: Int) { magicPoints = value.coerceIn(0, maxMagicPoints) }

    /** Direct port of BattleUnit.showHarmNum(info). */
    fun showHarmNum(hpAdd: Int? = null, mpAdd: Int? = null) {
        val isHp = mpAdd == null
        val value = mpAdd ?: hpAdd ?: return
        clsHarmNum()
        harmNumber = HarmNumber(value = kotlin.math.abs(value), isHp = isHp, xOffset = if (isHp) -24 else 24)
    }

    /** Direct port of BattleUnit.clsHarmNum(). */
    fun clsHarmNum() { harmNumber = null }

    /** Direct port of BattleUnit.refStateAnime's status-source input. */
    fun refStateAnime(): BattleUnitStateAnimation.Effect? {
        val effect = stateAnimation.refresh(listOf(
            BattleStatus.PARALYSIS in statuses, // MB
            BattleStatus.SILENCE in statuses,   // JZ
            BattleStatus.CONFUSION in statuses, // HL
            BattleStatus.POISON in statuses,    // ZD
        ))
        refAttributeStatusIcons()
        return effect
    }

    /** Direct port of BattleUnit._refStatus. */
    fun refAttributeStatusIcons() {
        attributeStatusIcons = BattleAttribute.entries.associateWith { attribute ->
            val lift = attributeLifts[attribute] ?: 0
            AttributeStatusIcon(active = lift != 0, down = lift == -1)
        }
    }

    /** Direct port of BattleUnit.setStateAnimeVisible. */
    fun setStateAnimeVisible(visible: Boolean) = stateAnimation.setVisible(visible)

    /** Direct port of BattleUnit.showHarmBar(info). */
    fun showHarmBar(hpAdd: Int? = null, mpAdd: Int? = null, hitRate: Number? = null) {
        harmBarPreview = BattleHarmBar.show(hitPoints, maxHitPoints, magicPoints, maxMagicPoints, hpAdd, mpAdd, hitRate)
    }

    data class DefaultAction(val action: Int, val loop: Boolean)

    /**
     * Direct port of BattleUnit.defaultAction(t): choose the authored idle
     * BRAnime from low HP, action-complete, poison, and paralysis state.
     */
    fun defaultAction(): DefaultAction {
        if (!visible) return DefaultAction(STAND, loop = true)
        val poisoned = BattleStatus.POISON in statuses
        val paralyzed = BattleStatus.PARALYSIS in statuses
        val lowHp = hitPoints < (maxHitPoints * (if (famous) 4 else 2) / 10)
        return if (lowHp) {
            when {
                hasActed && poisoned -> DefaultAction(XU_RUO_ZD, true)
                hasActed -> DefaultAction(XU_RUO_ACTION, false)
                poisoned && paralyzed -> DefaultAction(CHUAN_QI_ZD_MB, true)
                poisoned -> DefaultAction(CHUAN_QI_ZD, true)
                paralyzed -> DefaultAction(CHUAN_QI_MB, true)
                else -> DefaultAction(CHUAN_QI, true)
            }
        } else {
            when {
                hasActed && poisoned -> DefaultAction(STAND_UP_ZD, true)
                hasActed -> DefaultAction(STAND_UP_ACTION, false)
                poisoned && paralyzed -> DefaultAction(STAND_ZD_MB, true)
                poisoned -> DefaultAction(STAND_ZD, true)
                paralyzed -> DefaultAction(STAND_MB, true)
                else -> DefaultAction(STAND, true)
            }
        }
    }

    private fun hpRatio(): Float = hitPoints.toFloat() / maxHitPoints.coerceAtLeast(1)

    companion object {
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

data class TurnTrigger(val round: Int, val faction: Faction)

class BattleEvent(
    val id: String,
    val trigger: TurnTrigger,
    private val action: (BattleState) -> Unit,
) {
    fun matches(state: Battle): Boolean = state.round >= trigger.round && state.activeFaction == trigger.faction
    fun execute(state: BattleState) = action(state)
}

data class TurnResult(val round: Int, val activeFaction: Faction, val firedEvents: List<String>)
enum class CampSettlementStage { START_STATE, END_RESTORE }
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

/**
 * The small, callback-owned `h` settlement assembled by `_magicProcess`.
 * Entries deliberately remain present when [hasStatesPayload] is true but
 * the state maps are equal: `setCharInfoBykey(h, unit, STATES, {})` still
 * inserts the unit into `h.index`, and the following `_jiesuan` focuses it.
 */
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

data class MagicLocalSettlement(val entries: List<MagicLocalSettlementEntry>)

data class CampSettlement(
    val stage: CampSettlementStage,
    val faction: Faction,
    val changes: List<BattleUnitTurnChange>,
    /** Authored callback subflows executed inside `_stateProcess`/`restore`. */
    val subflows: List<SettlementSubflow> = emptyList(),
    /** Distinguishes an authored empty result from a legacy caller that omitted subflow capture. */
    val subflowsCaptured: Boolean = false,
)

sealed interface SettlementSubflow {
    data class LocalAura(
        val casterId: String,
        val skillId: Int,
        val skillValue: Int,
        val focusDelaySeconds: Float = .3f,
        val soundIndex: Int = 39,
        val infoSkillId: Int = skillId,
        val actionId: Int = 30,
        /** `resume_hp`/`resume_mp`; null for cleanse and ability aura. */
        val meffName: String? = null,
        val targets: List<String>,
        /** Exact mutations consumed by the nested `_jiesuan`. */
        val nestedChanges: List<BattleUnitTurnChange>,
    ) : SettlementSubflow

    data class Growth(
        val unitId: String,
        val grants: List<SettlementGrowthGrant>,
    ) : SettlementSubflow
}

enum class SettlementGrowthKind { UNIT_EXP, WEAPON_EXP, ARMOR_EXP }
/** The two independently max-merged equipment EXP entries in g_charinfo. */
enum class BattleEquipmentExperienceKind { WEAPON, ARMOR }
data class SettlementGrowthGrant(
    val kind: SettlementGrowthKind,
    /** Skill value passed to Unit.unitAddExp/equipAddExp. */
    val requestedAmount: Int,
    val unitResult: CampaignExperienceResult? = null,
    val equipmentResult: CampaignEquipmentExperienceResult? = null,
) {
    val requiresLevelUpPresentation: Boolean get() =
        unitResult?.leveledUp == true || equipmentResult?.leveledUp == true
    val requiresItemUpgradeCallback: Boolean get() = equipmentResult?.leveledUp == true
}
sealed interface RestoreGrowthResolution<out T> {
    data object NotApplicable : RestoreGrowthResolution<Nothing>
    data object Unavailable : RestoreGrowthResolution<Nothing>
    data class Applied<T>(val value: T) : RestoreGrowthResolution<T>
}
data class RoundAdvance(val completedRound: Int, val round: Int)
data class WeatherTransition(val previous: BattleWeather, val current: BattleWeather) {
    val changed: Boolean get() = previous != current
}
data class AiTurnResult(val moves: Int, val attacks: Int, val holds: Int)

/** One source `_ai2` actor pass, retained so the renderer can await it. */
data class AiUnitResolution(
    val actorId: String,
    val fromX: Int,
    val fromY: Int,
    val toX: Int,
    val toY: Int,
    val path: List<Pair<Int, Int>>,
    val targetId: String? = null,
    val magicId: Int? = null,
    val result: TacticalActionResult? = null,
    val healthBeforeAction: Map<String, Int> = emptyMap(),
    val moveArea: List<Pair<Int, Int>> = emptyList(),
    val actionArea: List<Pair<Int, Int>> = emptyList(),
)

/**
 * Read-only observation of the same candidate planner that [runEnemyTurn]
 * delegates to through ControlManager.  This deliberately exposes the raw
 * score: it is an evidence record, not a compatibility-normalized result.
 */
data class AiPlannerTrace(
    val sourceCharacterId: Int,
    val ai: Int,
    val x: Int,
    val y: Int,
    val value: Int?,
    val actionValue: Int?,
    val targetId: String?,
    val magicId: Int?,
)

enum class BattleOutcome { PLAYER_VICTORY, ENEMY_VICTORY }

sealed interface TacticalActionResult {
    data object Success : TacticalActionResult
    data class Rejected(val reason: String) : TacticalActionResult
    data class Attack(
        /** HP loss only; MPFY harm is reported exclusively by mpShieldDamage. */
        val damage: Int,
        val defeated: Boolean,
        val hitRate: Int = 100,
        val hit: Boolean = true,
        val critical: Boolean = false,
        /** BattleLayer._attack6 default physical counterattack. */
        val counterDamage: Int = 0,
        val attackerDefeated: Boolean = false,
        /** Original XXGJ: HP restored from the inflicted physical damage. */
        val lifeStealHealing: Int = 0,
        /** Second hit from BattleLayer._attack2's continuous-attack loop. */
        val followUpDamage: Int = 0,
        /** MPFY's MP_ADD for the second `_attack3` invocation. */
        val followUpMpShieldDamage: Int = 0,
        /** Counterattack's FJBDSJ/SQGJ follow-up, after the 25% penalty. */
        val counterFollowUpDamage: Int = 0,
        /** MPFY's MP_ADD from `_attack6`'s first physical counter. */
        val counterMpShieldDamage: Int = 0,
        /** MPFY's MP_ADD from the counterattack's second `_attack2` pass. */
        val counterFollowUpMpShieldDamage: Int = 0,
        /** Original XXGJ applied by the counterattacker in _attack6. */
        val counterLifeStealHealing: Int = 0,
        /** Source action selection for the second `_attack2` pass. */
        val followUpCritical: Boolean = false,
        /** Source action selection for `_attack6`'s first counter. */
        val counterCritical: Boolean = false,
        /** Source action selection for `_attack6`'s continuous counter. */
        val counterFollowUpCritical: Boolean = false,
        /** BattleUnit.countAtkHarm's CTGJ effect-area records, after primary hit. */
        val splashTargets: List<PhysicalTarget> = emptyList(),
        /** BattleLayer._attack3 MPFY: this MP loss replaces, rather than accompanies, HP loss. */
        val mpShieldDamage: Int = 0,
        /** BattleLayer._attack3 QXL: direct recovery from its final `n` harm. */
        val qxlHealing: Int = 0,
        /** BattleLayer._attack3 FTSH post-reaction, non-lethal recoil. */
        val recoilDamage: Int = 0,
        /** Block-only MENG_JI/NI_FAN retaliation before ordinary counterattack. */
        val blockRetaliationDamage: Int = 0,
        /** JQFY's actual money expenditure while changing the hit to one HP. */
        val moneyShieldSpent: Int = 0,
        /** XSJQ delta applied to Game.money() for this primary hit. */
        val playerMoneyDelta: Int = 0,
        /** XSJQ delta applied to BattleLayer.ENEMY_MONEY for this primary hit. */
        val enemyMoneyDelta: Int = 0,
        /** BattleLayer._attack6 CLFJ strategy counter, when it supersedes physical counterattack. */
        val counterMagic: Magic? = null,
        /** Exact CLFJ skill value used to resolve [counterMagic]. */
        val counterMagicId: Int? = null,
        /** BattleLayer._attack3 ZDSY's self-targeted automatic property use. */
        val automaticProperty: Item? = null,
        /** Exact `_attack2` pass/target order for callback-faithful presentation. */
        val physicalPasses: List<PhysicalAttackPass> = emptyList(),
    ) : TacticalActionResult
    data class Magic(
        val name: String,
        val cost: Int,
        val targets: List<MagicTarget>,
        /** Each `_magicProcess` invocation, in source execution order. */
        val passes: List<List<MagicTarget>> = listOf(targets),
        /** One morale-critical decision shared by every CLLJ pass. */
        val critical: Boolean = false,
        /** Unit.checkCrit/getCritTxt result immediately before each pass preparation. */
        val criticalSpeeches: List<String?> = List(passes.size) { null },
        /** One callback-local `_magicProcess` -> `_jiesuan(h)` payload per pass. */
        val localSettlements: List<MagicLocalSettlement> = List(passes.size) { MagicLocalSettlement(emptyList()) },
    ) : TacticalActionResult
    data class Item(val name: String, val target: String, val effect: String) : TacticalActionResult
}

/** Original property item data used by BattleLayer._usePro2. */
data class BattlePropertyItem(val id: Int, val name: String, val itemType: Int, val value: Int)

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
    /** A strategy such as 패기/쇠기 changes several original status lifts at once. */
    val attributes: Map<BattleAttribute, Int> = emptyMap(),
    val magicRecovery: Int = 0,
    val magicDrain: Int = 0,
    val casterHealing: Int = 0,
)

/** One BattleUnit.countAtkHarm target record, including physical splash hits. */
data class PhysicalTarget(val targetId: String, val damage: Int, val hitRate: Int = 100)

/** `_attack2` only. Surround/siege attacks use their separate source coroutine and are not physical passes. */
enum class PhysicalAttackPassKind { ACTIVE, ACTIVE_FOLLOW_UP, COUNTER, COUNTER_FOLLOW_UP }

/** One completed `_attack3` invocation, retaining effects that aggregate fields cannot order. */
data class PhysicalAttackTargetResult(
    val targetId: String,
    /** Final source `n`, used by the harm number and FTSH. */
    val sourceHarm: Int,
    /** Actual HP loss; zero when MPFY took the hit. */
    val damage: Int,
    val mpShieldDamage: Int = 0,
    val moneyShieldSpent: Int = 0,
    val lifeStealHealing: Int = 0,
    val qxlHealing: Int = 0,
    val recoilDamage: Int = 0,
    val blockRetaliations: List<BattlePhysicalCallbackPlan.BlockRetaliation> = emptyList(),
    val playerMoneyDelta: Int = 0,
    val enemyMoneyDelta: Int = 0,
    val automaticPropertyId: Int? = null,
    val automaticProperty: TacticalActionResult.Item? = null,
    val automaticPropertyHpDelta: Int = 0,
    val automaticPropertyMpDelta: Int = 0,
    val automaticPropertyCallbackCount: Int = 0,
    /** Source TPGJ `backMove`: starts with anime32 and commits its tile after .08s. */
    val backMove: PhysicalBackMove? = null,
    /** Exact target-local `_jiesuan(t, o)` payload produced by this `_attack3`. */
    val localStatusSettlement: MagicLocalSettlement = MagicLocalSettlement(emptyList()),
    val hasLocalStatusSettlement: Boolean = false,
    val defeated: Boolean = false,
)

data class PhysicalBackMove(
    val fromX: Int,
    val fromY: Int,
    val toX: Int,
    val toY: Int,
    /** `BattleUnit.backMove` uses one fixed Cocos moveTo duration. */
    val durationSeconds: Float = .08f,
)

/** One attack animation and its sequential primary/CTGJ `_attack3` callbacks. */
data class PhysicalAttackPass(
    val kind: PhysicalAttackPassKind,
    val attackerId: String,
    val critical: Boolean,
    val targets: List<PhysicalAttackTargetResult>,
    /** Retained even when countAtkHarm returned no records for a miss. */
    val primaryTargetId: String? = targets.firstOrNull()?.targetId,
    /** Source say4(Unit.getCritTxt()) shown before this pass's attack action. */
    val criticalSpeech: String? = null,
)

/**
 * Deterministic tactical state with one-shot event consumption. The state is
 * intentionally independent of rendering so battle scripts can be tested
 * without a LibGDX window.
 */
/** Port of the original `battle/Battle.js` tactical-state owner. */
class Battle(
    units: List<BattleUnit>,
    private val events: List<BattleEvent>,
    blockedTiles: Set<Pair<Int, Int>> = emptySet(),
    private val terrain: BattleTerrainGrid? = null,
    private val enemyMasterUnitId: String? = null,
    initialWeather: BattleWeather = BattleWeather.CLEAR,
    private val weatherSchedule: List<BattleWeather> = emptyList(),
    private val weatherOffset: Int = 0,
    private val terrainMagicFlags: Map<Int, Int> = emptyMap(),
    /** GAME_CFG.terrain[n].resumeHP, used by Control._cxpl. */
    private val terrainResumeRates: Map<Int, Int> = emptyMap(),
    /** GAME_CFG.terrain[n].resumeMP, applied during BattleLayer._stateProcess. */
    private val terrainResumeMp: Map<Int, Int> = emptyMap(),
    /** BattleLayer.eFlag(), injected from the scenario/game feature mask. */
    private val enabledFeatures: Int = 0,
    /** defineSkillAttr(skill, RESET_TYPE, RESET), supplied by original data. */
    private val skillTempResetTypes: Map<Int, BattleSkillTemp.ResetType> = emptyMap(),
    /** Model.stateExInfoByIdx(status, ROUND, 3), injected from GAME_CFG.status. */
    private val statusRoundFor: (BattleStatus) -> Int = { 3 },
    /** Same packed status-table round for ATT..MOV slots 0..5. */
    private val attributeStatusRoundFor: (BattleAttribute) -> Int = { 3 },
    private val movementOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    /** Config.HITAREA.MO_YU_JIAN3, in authored order for Control._zdmdd. */
    private val directDestinationOffsets: List<Pair<Int, Int>> = listOf(
        0 to 1, 1 to 0, -1 to 0, 0 to -1,
        0 to 2, 1 to 1, -1 to 1, 2 to 0, -2 to 0, 1 to -1, -1 to -1, 0 to -2,
        0 to 3, 1 to 2, -1 to 2, 2 to 1, -2 to 1, 3 to 0, -3 to 0,
        2 to -1, -2 to -1, 1 to -2, -1 to -2, 0 to -3,
        0 to 4, 1 to 3, -1 to 3, 2 to 2, -2 to 2, 3 to 1, -3 to 1,
        4 to 0, -4 to 0, 3 to -1, -3 to -1, 2 to -2, -2 to -2,
        1 to -3, -1 to -3, 0 to -4,
    ),
    /** Config.HITAREA.BU_BING, used by BattleUnit.count_attackHarm JDGJ. */
    private val infantryOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    private val propertyItems: Map<Int, BattlePropertyItem> = emptyMap(),
    private val consumeProperty: (Int) -> Boolean = { false },
    /** Game.getGVars(GLOBAL_VAR.ZDSY, 0), injected from the source save state. */
    private val zdsyGlobalValue: Int = 0,
    /** ItemStore.pushProperty(id, -1), deliberately separate from player input consumption. */
    private val consumeAutomaticProperty: (Int) -> Unit = {},
    private val onPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit = { _, _ -> },
    private val onUnitDefeated: (BattleUnit, BattleUnit) -> Unit = { _, _ -> },
    /** Mine-only persistence hook. Enemy/Friend EXP remains in [BattleUnit.experience]. */
    private val onBattleExperience: (BattleUnit, Int) -> CampaignExperienceResult? = { _, _ -> null },
    private val experienceLimit: (Int) -> Int = { 100 },
    private val levelLimit: Int = 50,
    /** Rebuild Unit.setLevel's derived battle projection after EXP raises LV. */
    private val onBattleLevelUp: (BattleUnit) -> Unit = {},
    private val onPhysicalDamage: (BattleUnit, BattleUnit, Int) -> Unit = { _, _, _ -> },
    /**
     * Exact BattleLayer g_charinfo equipment settlement.  Unlike the legacy
     * physical callback below, this receives one already max-merged slot
     * award after the complete outer action has resolved.
     */
    private val onEquipmentExperienceAward: ((BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> List<CampaignEquipmentExperienceResult>)? = null,
    /** Compatibility hook for presentation/tests that observe each _attack3 hit. */
    private val onEquipmentExperience: (BattleUnit, BattleUnit, Int) -> List<CampaignEquipmentExperienceResult> = { _, _, _ -> emptyList() },
    /** restore() skills 149..151, kept separate from attack-earned EXP. */
    private val onRestoreUnitExperience: (BattleUnit, Int) -> RestoreGrowthResolution<CampaignExperienceResult> = { _, _ -> RestoreGrowthResolution.Unavailable },
    private val onRestoreEquipmentExperience: (BattleUnit, Int, CampaignState.EquipmentSlot) -> RestoreGrowthResolution<CampaignEquipmentExperienceResult> = { _, _, _ -> RestoreGrowthResolution.Unavailable },
    private val random: Random = Random(0),
    /** Opt-in exact Tool.random/Math.random streams for full source replay. */
    private val sourceRandomStreams: SourceRandomStreams? = null,
    /** Game.money() at battle entry, injected because BattleLayer owns it. */
    initialPlayerMoney: Int = 0,
    /** Battle attribute ENEMY_MONEY at battle entry. */
    initialEnemyMoney: Int = 0,
    private val onUnitRetreat: (BattleUnit) -> Unit = {},
) {
    /** Scripted gates can open/close after scene0, so this cannot stay immutable. */
    private val blockedTiles = blockedTiles.toMutableSet()
    /** hitarea[QUN_XIONG].ps is an authored array; Set iteration must not alter A* FIFO ties. */
    private val orderedMovementOffsets = buildList {
        val sourceOrder = listOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
        sourceOrder.filterTo(this) { it in movementOffsets }
        movementOffsets.filterTo(this) { it !in sourceOrder }
    }
    private val skillTemps = BattleSkillTemp { skillTempResetTypes[it] ?: BattleSkillTemp.ResetType.RESET }
    /** BattleLayer._move_len, assigned from unitMove's path-node array length. */
    private var moveLength: Int = 0
    /** The start-inclusive route passed from BattleLayer.unitMove to BattleUnit.move2. */
    private val lastMovePaths = linkedMapOf<String, List<Pair<Int, Int>>>()
    fun lastMovePath(id: String): List<Pair<Int, Int>> = lastMovePaths[id].orEmpty()
    val units = units.associateByTo(linkedMapOf()) { it.id }
    /** Ordered AI decisions retained for deterministic full-battle diagnostics. */
    val traceActions = mutableListOf<String>()
    /** First s_AISortUnit result captured before asynchronous state settlement. */
    private var aiTurnOrder: List<String>? = null
    /** Most recently resolved `_ai2` actor; consumed by BattleLayer presentation. */
    var lastAiUnitResolution: AiUnitResolution? = null
        private set
    /**
     * One `_ai2` result calculated ahead of rendering but kept out of the
     * live model until BattleLayer reaches the matching source callbacks.
     */
    var deferredAiMutation: DeferredAiMutation? = null
        private set
    /** BattleLayer's two money stores, exposed for injected source-parity tests. */
    var playerMoney: Int = initialPlayerMoney
        private set
    var enemyMoney: Int = initialEnemyMoney
        private set
    /**
     * _jiesuan/unitDeath keeps the Cocos BattleUnit node alive through its
     * current hit/death clip, although it no longer participates in combat.
     */
    private val presentationUnits = linkedMapOf<String, BattleUnit>()
    /** Ordered Global113 requests produced by the real physical-damage path. */
    private val equipmentUpgrades = ArrayDeque<CampaignEquipmentExperienceResult>()
    /** Non-model callbacks produced while precomputing one visible AI actor. */
    private var stagedAiHitSideEffects: MutableList<() -> Unit>? = null
    private var stagedAiCompletionSideEffects: MutableList<() -> Unit>? = null
    fun consumeEquipmentUpgrade(): CampaignEquipmentExperienceResult? = equipmentUpgrades.removeFirstOrNull()
    /** BattleLayer._addWeaponExp entry used by settlement and deterministic route tests. */
    fun addEquipmentExperience(attackerId: String, targetId: String, damage: Int) {
        val attacker = units[attackerId] ?: return
        val target = units[targetId] ?: return
        val apply = {
            val results = onEquipmentExperienceAward?.let { award ->
                buildList {
                    addAll(award(attacker, target, equipmentExperienceAmount(attacker, target, damage, BattleEquipmentExperienceKind.WEAPON), BattleEquipmentExperienceKind.WEAPON))
                    addAll(award(target, attacker, equipmentExperienceAmount(target, attacker, damage, BattleEquipmentExperienceKind.ARMOR), BattleEquipmentExperienceKind.ARMOR))
                }
            } ?: onEquipmentExperience(attacker, target, damage)
            results.filterTo(equipmentUpgrades) { it.leveledUp }
            Unit
        }
        stagedAiHitSideEffects?.add(apply) ?: apply()
    }
    private fun notifyPhysicalDamage(attacker: BattleUnit, target: BattleUnit, damage: Int) {
        val apply = {
            onPhysicalDamage(attacker, target, damage)
            // Older callers use this as a per-hit presentation callback.
            // Production uses onEquipmentExperienceAward, settled below by
            // max slot reward, so it must not mutate campaign EXP here.
            if (onEquipmentExperienceAward == null) {
                onEquipmentExperience(attacker, target, damage).filterTo(equipmentUpgrades) { it.leveledUp }
            }
            Unit
        }
        stagedAiHitSideEffects?.add(apply) ?: apply()
    }
    private fun notifyEquipmentExperienceAward(
        recipient: BattleUnit,
        opponent: BattleUnit,
        amount: Int,
        kind: BattleEquipmentExperienceKind,
    ) {
        val award = onEquipmentExperienceAward ?: return
        val apply = { award(recipient, opponent, amount, kind).filterTo(equipmentUpgrades) { it.leveledUp }; Unit }
        stagedAiCompletionSideEffects?.add(apply) ?: apply()
    }
    /** BattleLayer.count_wqExp/count_hjExp, before setCharInfoBykey max-merges it. */
    private fun equipmentExperienceAmount(
        recipient: BattleUnit,
        opponent: BattleUnit,
        sourceHarm: Int,
        kind: BattleEquipmentExperienceKind,
    ): Int = when (kind) {
        BattleEquipmentExperienceKind.WEAPON -> if (sourceHarm == 0) 1 else if (recipient.level <= opponent.level) 3 else 2
        BattleEquipmentExperienceKind.ARMOR -> if (sourceHarm == 0) 1 else if (recipient.level <= opponent.level) 4 else 3
    }
    private fun notifyUnitDefeated(winner: BattleUnit, defeated: BattleUnit) {
        val apply = { onUnitDefeated(winner, defeated) }
        stagedAiCompletionSideEffects?.add(apply) ?: apply()
    }
    private fun notifyBattleExperience(unit: BattleUnit, amount: Int) {
        if (amount <= 0) return
        val apply = {
            val oldLevel = unit.level
            val persistent = onBattleExperience(unit, amount)
            if (persistent != null) {
                unit.level = persistent.level
                unit.experience = persistent.experience
            } else {
                var remaining = amount
                while (remaining > 0) {
                    val limit = experienceLimit(unit.level).coerceAtLeast(1)
                    val gained = minOf(remaining, (limit - unit.experience).coerceAtLeast(0))
                    unit.experience += gained
                    remaining -= gained
                    if (unit.experience >= limit && unit.level < levelLimit) {
                        unit.level++
                        unit.experience = 0
                    } else break
                }
            }
            if (unit.level != oldLevel) onBattleLevelUp(unit)
            Unit
        }
        stagedAiCompletionSideEffects?.add(apply) ?: apply()
    }

    /** BattleLayer.count_exp, before g_charinfo's per-attacker EXP_ADD max merge. */
    private fun battleExperience(attacker: BattleUnit, target: BattleUnit, defeated: Boolean): Int {
        val difference = kotlin.math.abs(target.level - attacker.level)
        var result = if (target.level >= attacker.level) 8 + maxOf(1, 2 * difference)
        else maxOf(1, 8 - difference)
        if (defeated) {
            result *= 4
            if (target.id == enemyMasterUnitId) result *= 2
        }
        attacker.skills[67]?.and(255)?.takeIf { it != 255 }?.let { result += it }
        return result
    }
    private fun notifyConsumeAutomaticProperty(itemId: Int) {
        val apply = { consumeAutomaticProperty(itemId) }
        stagedAiHitSideEffects?.add(apply) ?: apply()
    }
    private fun notifyPermanentProperty(item: BattlePropertyItem, target: BattleUnit) {
        val apply = { onPermanentProperty(item, target) }
        stagedAiHitSideEffects?.add(apply) ?: apply()
    }
    private fun consumeSelectedProperty(itemId: Int): Boolean {
        val completion = stagedAiCompletionSideEffects
        if (completion != null) {
            // The production UI only offers positive-count rows. Preserve
            // that selection during calculation and consume on _usePro2's
            // post-animation callback instead of leaking inventory early.
            completion += { consumeProperty(itemId); Unit }
            return true
        }
        return consumeProperty(itemId)
    }
    fun presentationUnit(id: String): BattleUnit? = units[id] ?: presentationUnits[id]
    fun pendingPresentationUnits(): Collection<BattleUnit> = presentationUnits.values
    /**
     * Source traces keep a defeated BattleUnit node through anime23/24 and
     * its final hidden callback. Tactical queries must still use [units],
     * while render/trace observers need both collections in stable order.
     */
    fun presentationUnits(): List<BattleUnit> =
        (units.values + presentationUnits.values).distinctBy { it.id }
    fun clearPresentationUnit(id: String) { presentationUnits.remove(id) }
    fun completeScriptedUnitHide(id: String) {
        presentationUnit(id)?.visible = false
    }
    /** BattleUnit.show makes a retained defeated unit participate in combat again. */
    fun restorePresentationUnit(id: String): BattleUnit? {
        val unit = units[id] ?: presentationUnits.remove(id)?.also { units[id] = it } ?: return null
        unit.retreatFlag = false
        if (unit.hitPoints < 1) {
            unit.setHpcur(unit.maxHitPoints)
            unit.setMpcur(unit.maxMagicPoints)
            unit.statuses.clear()
            unit.attributeLifts.clear()
            unit.attributeLiftRounds.clear()
            // clearAllState(1) preserves XD/action-complete but removes MS.
            unit.hasMoved = false
            unit.refStateAnime()
            unit.refAttributeStatusIcons()
        }
        unit.visible = true
        return unit
    }
    fun incrementUnitRetreat(unit: BattleUnit) {
        unit.retreatCount++
        onUnitRetreat(unit)
    }
    private fun removeUnit(id: String) { units.remove(id)?.let { presentationUnits[id] = it } }

    internal data class RuntimeUnitState(
        val unit: BattleUnit,
        val tileX: Int,
        val tileY: Int,
        val hitPoints: Int,
        val maxHitPoints: Int,
        val magicPoints: Int,
        val maxMagicPoints: Int,
        val level: Int,
        val direction: Int,
        val hasActed: Boolean,
        val actionStatusRound: Int,
        val hasMoved: Boolean,
        val visible: Boolean,
        val otherNodesVisible: Boolean,
        val retreatFlag: Boolean,
        val retreatCount: Int,
        val ai: Int,
        val aiTargetCharacterId: Int,
        val aiTargetX: Int,
        val aiTargetY: Int,
        val aiValue: Int,
        val criticalSpeechChecks: Int,
        val statuses: Map<BattleStatus, Int>,
        val attributeLifts: Map<BattleAttribute, Int>,
        val attributeLiftRounds: Map<BattleAttribute, Int>,
        val rates: Map<Int, Int>,
    )

    internal data class RuntimeSnapshot(
        val activeIds: List<String>,
        val presentationIds: List<String>,
        val states: Map<String, RuntimeUnitState>,
        val playerMoney: Int,
        val enemyMoney: Int,
        val skillTemps: Map<String, Map<Int, Pair<Int, Int>>>,
        val moveLength: Int,
        val lastMovePaths: Map<String, List<Pair<Int, Int>>>,
        val traceActions: List<String>,
    )

    private fun runtimeSnapshot(): RuntimeSnapshot {
        val all = linkedMapOf<String, BattleUnit>().apply {
            putAll(units)
            putAll(presentationUnits)
        }
        return RuntimeSnapshot(
            activeIds = units.keys.toList(),
            presentationIds = presentationUnits.keys.toList(),
            states = all.mapValues { (_, unit) ->
                RuntimeUnitState(
                    unit, unit.tileX, unit.tileY, unit.hitPoints, unit.maxHitPoints,
                    unit.magicPoints, unit.maxMagicPoints, unit.level, unit.direction,
                    unit.hasActed, unit.actionStatusRound, unit.hasMoved, unit.visible, unit.otherNodesVisible,
                    unit.retreatFlag, unit.retreatCount, unit.ai,
                    unit.aiTargetCharacterId, unit.aiTargetX, unit.aiTargetY, unit.aiValue,
                    unit.criticalSpeechChecks,
                    unit.statuses.toMap(), unit.attributeLifts.toMap(),
                    unit.attributeLiftRounds.toMap(), unit.rateAccumulators.toMap(),
                )
            },
            playerMoney = playerMoney,
            enemyMoney = enemyMoney,
            skillTemps = skillTemps.snapshot(),
            moveLength = moveLength,
            lastMovePaths = lastMovePaths.mapValues { it.value.toList() },
            traceActions = traceActions.toList(),
        )
    }

    private fun restoreRuntime(snapshot: RuntimeSnapshot) {
        snapshot.states.values.forEach { state ->
            state.unit.tileX = state.tileX
            state.unit.tileY = state.tileY
            state.unit.maxHitPoints = state.maxHitPoints
            state.unit.setHpcur(state.hitPoints)
            state.unit.maxMagicPoints = state.maxMagicPoints
            state.unit.setMpcur(state.magicPoints)
            state.unit.level = state.level
            state.unit.direction = state.direction
            state.unit.hasActed = state.hasActed
            state.unit.actionStatusRound = state.actionStatusRound
            state.unit.hasMoved = state.hasMoved
            state.unit.visible = state.visible
            state.unit.otherNodesVisible = state.otherNodesVisible
            state.unit.retreatFlag = state.retreatFlag
            state.unit.retreatCount = state.retreatCount
            state.unit.ai = state.ai
            state.unit.aiTargetCharacterId = state.aiTargetCharacterId
            state.unit.aiTargetX = state.aiTargetX
            state.unit.aiTargetY = state.aiTargetY
            state.unit.aiValue = state.aiValue
            state.unit.criticalSpeechChecks = state.criticalSpeechChecks
            state.unit.statuses.clear(); state.unit.statuses.putAll(state.statuses)
            state.unit.attributeLifts.clear(); state.unit.attributeLifts.putAll(state.attributeLifts)
            state.unit.attributeLiftRounds.clear(); state.unit.attributeLiftRounds.putAll(state.attributeLiftRounds)
            state.unit.rateAccumulators.clear(); state.unit.rateAccumulators.putAll(state.rates)
            state.unit.refStateAnime()
        }
        units.clear()
        snapshot.activeIds.forEach { id -> snapshot.states[id]?.unit?.let { units[id] = it } }
        presentationUnits.clear()
        snapshot.presentationIds.forEach { id -> snapshot.states[id]?.unit?.let { presentationUnits[id] = it } }
        playerMoney = snapshot.playerMoney
        enemyMoney = snapshot.enemyMoney
        skillTemps.restore(snapshot.skillTemps)
        moveLength = snapshot.moveLength
        lastMovePaths.clear(); lastMovePaths.putAll(snapshot.lastMovePaths)
        traceActions.clear(); traceActions.addAll(snapshot.traceActions)
    }

    inner class DeferredAiMutation internal constructor(
        val actorId: String,
        private val before: RuntimeSnapshot,
        private val after: RuntimeSnapshot,
        private val hitSideEffects: List<() -> Unit>,
        private val completionSideEffects: List<() -> Unit>,
    ) {
        private var complete = false
        private var hitEffectsCommitted = 0

        fun initialHp(id: String): Int? = before.states[id]?.hitPoints
        fun initialMp(id: String): Int? = before.states[id]?.magicPoints

        /** BattleUnit.move2's final callback commits its logical destination. */
        fun commitMovement(commitActionState: Boolean = false) {
            if (complete) return
            val source = after.states[actorId] ?: return
            val target = before.states[actorId]?.unit ?: return
            val moved = target.tileX != source.tileX || target.tileY != source.tileY
            target.tileX = source.tileX
            target.tileY = source.tileY
            // A source move2 completion calls setPos(), which replaces even
            // an initially omitted prefab axis with _countPos(tileX,tileY).
            // Subsequent centerUnit(unit) must therefore use both authored
            // tile axes rather than the original create-record omissions.
            if (moved) {
                target.sourceTileXAuthored = true
                target.sourceTileYAuthored = true
            }
            target.direction = source.direction
            target.hasMoved = source.hasMoved
            // A move/hold with no following action completes `_ai2` at this
            // callback. Source g_charinfo applies XD round then status in the
            // same observable bucket as move2's final setPos.
            if (commitActionState) {
                target.actionStatusRound = source.actionStatusRound
                target.hasActed = source.hasActed
            }
        }

        /** `BattleUnit.backMove` publishes the victim tile in moveTo's completion callback. */
        fun commitPosition(id: String, x: Int, y: Int) {
            if (complete) return
            val unit = before.states[id]?.unit ?: return
            unit.tileX = x
            unit.tileY = y
            unit.sourceTileXAuthored = true
            unit.sourceTileYAuthored = true
        }

        /** `_attack3`/`playMeff` callback commits only the values visible at that hit. */
        fun commitVitals(id: String, hp: Int? = null, mp: Int? = null) {
            if (complete) return
            val unit = before.states[id]?.unit ?: return
            hp?.let(unit::setHpcur)
            mp?.let(unit::setMpcur)
        }

        /**
         * `_attack3` applies JQFY and XSJQ synchronously in the hit callback,
         * before it starts the target's hurt animation. Publish only that
         * callback-local delta here; [commitAll] later restores the absolute
         * resolved snapshot, so an already-published delta is not added twice.
         */
        fun commitEconomy(playerDelta: Int = 0, enemyDelta: Int = 0) {
            if (complete) return
            playerMoney += playerDelta
            enemyMoney += enemyDelta
        }

        /** `_jiesuan(target, localInfo)` publishes this target's staged states before the next `_attack3`. */
        fun commitStatuses(id: String) {
            if (complete) return
            val source = after.states[id] ?: return
            val unit = before.states[id]?.unit ?: return
            unit.statuses.clear(); unit.statuses.putAll(source.statuses)
            unit.attributeLifts.clear(); unit.attributeLifts.putAll(source.attributeLifts)
            unit.attributeLiftRounds.clear(); unit.attributeLiftRounds.putAll(source.attributeLiftRounds)
            unit.refStateAnime()
            unit.refAttributeStatusIcons()
        }

        /** Publish one callback-local `_jiesuan` snapshot, not the action's later final state. */
        fun commitStatuses(entry: MagicLocalSettlementEntry) {
            if (complete) return
            val unit = before.states[entry.targetId]?.unit ?: return
            unit.statuses.clear(); unit.statuses.putAll(entry.statusesAfter)
            unit.attributeLifts.clear(); unit.attributeLifts.putAll(entry.attributeLiftsAfter)
            unit.attributeLiftRounds.clear(); unit.attributeLiftRounds.putAll(entry.attributeLiftRoundsAfter)
            unit.refStateAnime()
            unit.refAttributeStatusIcons()
        }

        fun commitNextHitSideEffect() {
            if (complete) return
            hitSideEffects.getOrNull(hitEffectsCommitted)?.invoke()
            if (hitEffectsCommitted < hitSideEffects.size) hitEffectsCommitted++
        }

        /** Hurt/death/follow-up callbacks are complete; expose the resolved state atomically. */
        fun commitAll() {
            if (complete) return
            // Deferred resolution snapshots are computed before movement and
            // attack clips play. Those clips update the live actor/targets to
            // the source's last movement segment and countDir facing. Keep
            // those presentation-authored directions across the atomic state
            // restore instead of reviving the snapshot's first-edge facing.
            val liveDirections = before.states.mapNotNull { (id, state) ->
                val direction = state.unit.direction
                if (id == actorId || direction != state.direction) id to direction else null
            }.toMap()
            val moved = before.states[actorId]?.unit?.let { beforeUnit ->
                after.states[actorId]?.unit?.let { afterUnit ->
                    beforeUnit.tileX != afterUnit.tileX || beforeUnit.tileY != afterUnit.tileY
                }
            } == true
            restoreRuntime(after)
            liveDirections.forEach { (id, direction) ->
                (units[id] ?: presentationUnits[id])?.direction = direction
            }
            if (moved) {
                // restoreRuntime replaces the live object with the staged
                // snapshot, so re-apply the node-position transition here.
                // The source move2 completion has already called setPos,
                // including when this deferred action is player-driven and
                // never called commitMovement separately.
                units[actorId]?.sourceTileXAuthored = true
                units[actorId]?.sourceTileYAuthored = true
            }
            while (hitEffectsCommitted < hitSideEffects.size) commitNextHitSideEffect()
            completionSideEffects.forEach { it() }
            complete = true
            if (deferredAiMutation === this) deferredAiMutation = null
        }
    }

    data class DeferredMoveResult(
        val result: TacticalActionResult,
        val path: List<Pair<Int, Int>>,
    )

    /**
     * Player commands use the same calculate-then-callback-commit boundary as
     * `_ai2`. The calculation consumes RNG once, while all mutable battle
     * state and external callbacks stay hidden until the authored animation
     * edge commits [deferredAiMutation].
     */
    private fun <T : TacticalActionResult> resolveDeferredAction(
        actorId: String,
        resolve: () -> T,
    ): T {
        check(deferredAiMutation == null) { "previous deferred battle action has not completed" }
        val before = runtimeSnapshot()
        stagedAiHitSideEffects = mutableListOf()
        stagedAiCompletionSideEffects = mutableListOf()
        val result = try {
            resolve()
        } catch (failure: Throwable) {
            stagedAiHitSideEffects = null
            stagedAiCompletionSideEffects = null
            restoreRuntime(before)
            throw failure
        }
        val hitSideEffects = stagedAiHitSideEffects.orEmpty().toList()
        val completionSideEffects = stagedAiCompletionSideEffects.orEmpty().toList()
        stagedAiHitSideEffects = null
        stagedAiCompletionSideEffects = null
        if (result is TacticalActionResult.Rejected) {
            restoreRuntime(before)
            return result
        }
        val after = runtimeSnapshot()
        restoreRuntime(before)
        deferredAiMutation = DeferredAiMutation(actorId, before, after, hitSideEffects, completionSideEffects)
        return result
    }

    fun moveUnitForPresentation(id: String, targetX: Int, targetY: Int): DeferredMoveResult {
        var path = emptyList<Pair<Int, Int>>()
        val result = resolveDeferredAction(id) {
            moveUnit(id, targetX, targetY).also {
                if (it !is TacticalActionResult.Rejected) path = lastMovePath(id).toList()
            }
        }
        return DeferredMoveResult(result, path)
    }

    fun attackForPresentation(attackerId: String, targetId: String): TacticalActionResult =
        resolveDeferredAction(attackerId) { attack(attackerId, targetId) }

    fun castMagicForPresentation(attackerId: String, targetId: String, magicId: Int): TacticalActionResult =
        resolveDeferredAction(attackerId) { castMagic(attackerId, targetId, magicId) }

    fun usePropertyForPresentation(userId: String, targetId: String, itemId: Int): TacticalActionResult =
        resolveDeferredAction(userId) { useProperty(userId, targetId, itemId) }

    fun hasPendingAiUnits(): Boolean = outcome() == null && units.values.any {
        it.visible && it.effectiveFaction() == activeFaction && !it.hasActed
    }
    val firedEventIds = linkedSetOf<String>()
    var round: Int = 1
        private set
    var activeFaction: Faction = Faction.PLAYER
        private set

    /** Selects the controllable allied camp used by deterministic actual-route verification. */
    internal fun selectVerificationFaction(faction: Faction) {
        require(faction.isPlayerSide()) { "Verification routes may only select an allied camp." }
        activeFaction = faction
    }
    var maxRounds: Int = 99
        private set
    var weather: BattleWeather = initialWeather
        private set
    private var scriptedOutcome: BattleOutcome? = null

    /**
     * Compatibility wrapper for model-only callers.  Production turn flow
     * uses the individual lifecycle methods below so every source coroutine
     * barrier can be presented before the following mutation is applied.
     */
    fun endTurn(): TurnResult {
        settleActiveCampEnd()
        if (activeFaction == Faction.REINFORCEMENTS) {
            val advance = advanceRound()
            resetCompletedRoundSkillTemps(advance.completedRound)
            applyScheduledWeather()
        }
        var result: TurnResult
        val fired = mutableListOf<String>()
        do {
            result = advanceToNextCamp()
            settleActiveCampStart()
            fired += runActiveCampEvents()
            prepareActiveCampOperation()
            // Legacy/model callers have no visual callback to observe an
            // empty FRIEND or REINFORCEMENTS pass. Consume that no-op pass
            // atomically, while the production BattleTurnController still
            // exposes all four source coroutine phases.
            if (activeFaction == Faction.PLAYER || units.values.any {
                    it.visible && it.effectiveFaction() == activeFaction
                }
            ) break
            settleActiveCampEnd()
            if (activeFaction == Faction.REINFORCEMENTS) {
                val advance = advanceRound()
                resetCompletedRoundSkillTemps(advance.completedRound)
                applyScheduledWeather()
            }
        } while (true)
        return result.copy(round = round, activeFaction = activeFaction, firedEvents = fired)
    }

    /** BattleLayer.restore, before its nested unitDeath callback. */
    fun settleActiveCampEnd(): CampSettlement = captureSettlement(
        stage = CampSettlementStage.END_RESTORE,
        faction = activeFaction,
    ) { subflows ->
        // ctrl_mine clears XD for the entire allied side after FRIEND's AI,
        // and for the entire enemy side after REINFORCEMENTS, through an
        // authored _jiesuan before restore. It is not a _setOper mutation.
        if (activeFaction == Faction.FRIEND || activeFaction == Faction.REINFORCEMENTS) {
            val side = activeFaction.isPlayerSide()
            units.values.filter { it.effectiveFaction().isPlayerSide() == side }.forEach {
                it.hasActed = false
                it.refStateAnime()
            }
        }
        processEndOfTurn(activeFaction, subflows)
    }

    /**
     * `_setOper` changes curCamp before RoundLayer and before `_stateProcess`.
     * This method deliberately does not apply state, reset actors, or weather.
     */
    fun advanceToNextCamp(): TurnResult {
        activeFaction = when (activeFaction) {
            Faction.PLAYER -> Faction.FRIEND
            Faction.FRIEND -> Faction.ENEMY
            Faction.ENEMY -> Faction.REINFORCEMENTS
            Faction.REINFORCEMENTS -> Faction.PLAYER
        }
        return TurnResult(round, activeFaction, emptyList())
    }

    /** First run_script inside unitDeath, after `_stateProcess` presentation. */
    fun runActiveCampEvents(): List<String> = events
        .asSequence()
        .filter { it.id !in firedEventIds && it.matches(this) }
        .onEach {
            firedEventIds += it.id
            it.execute(this)
        }
        .map { it.id }
        .toList()

    /** BattleLayer._stateProcess; mutations occur only after RoundLayer closes. */
    fun settleActiveCampStart(): CampSettlement = captureSettlement(
        stage = CampSettlementStage.START_STATE,
        faction = activeFaction,
    ) { subflows -> processStartOfTurn(activeFaction, subflows) }

    /**
     * Source `_ai2` captures its actor order after state settlement/death.
     * Resetting and sorting here prevents a future camp from being observable
     * while the preceding card or state animation is still on screen.
     */
    fun prepareActiveCampOperation() {
        units.values.filter { it.effectiveFaction() == activeFaction }.forEach {
            it.hasActed = false
            it.hasMoved = false
            it.aiValue = 0
        }
        // _ai2 captures this list only after _stateProcess, battle script, and
        // the first unitDeath pass have all completed.
        aiTurnOrder = units.values.asSequence()
            .filter { it.visible && it.effectiveFaction() == activeFaction && !it.hasActed }
            .sortedWith(compareByDescending<BattleUnit>(::aiSortValue).thenBy { effective(it, BattleAttribute.DEFENSE) })
            .map { it.id }
            .toList()
    }

    /** `addRound`, before the new-round battle script. */
    fun advanceRound(): RoundAdvance {
        check(activeFaction == Faction.REINFORCEMENTS) { "round may advance only after the reinforcements camp" }
        val completedRound = round
        round++
        return RoundAdvance(completedRound, round)
    }

    /** `resetSkillTemp(T)`, after new-round script/unitDeath and before weather. */
    fun resetCompletedRoundSkillTemps(completedRound: Int) {
        check(completedRound == round - 1) { "only the just-completed round may be reset" }
        skillTemps.reset(completedRound)
    }

    /** `_countCurrentWeather`/`_switchWeather`, after new-round script/death. */
    fun applyScheduledWeather(): WeatherTransition {
        val previous = weather
        if (weatherSchedule.isNotEmpty()) {
            weather = weatherSchedule[Math.floorMod(round + weatherOffset, weatherSchedule.size)]
        }
        return WeatherTransition(previous, weather)
    }

    private data class UnitTurnSnapshot(
        val hp: Int,
        val mp: Int,
        val statuses: Map<BattleStatus, Int>,
        val lifts: Map<BattleAttribute, Int>,
        val actionComplete: Boolean,
        val actionStatusRound: Int,
    )

    private fun captureSettlement(
        stage: CampSettlementStage,
        faction: Faction,
        settle: (MutableList<SettlementSubflow>) -> List<BattleUnitTurnChange>?,
    ): CampSettlement {
        val before = turnSnapshot()
        val subflows = mutableListOf<SettlementSubflow>()
        val primaryChanges = settle(subflows)
        val changes = primaryChanges ?: turnChanges(before)
        return CampSettlement(stage, faction, changes, subflows, subflowsCaptured = true)
    }

    private fun turnSnapshot(): Map<String, UnitTurnSnapshot> = units.mapValues { (_, unit) ->
        UnitTurnSnapshot(
            hp = unit.hitPoints,
            mp = unit.magicPoints,
            statuses = unit.statuses.toMap(),
            lifts = unit.attributeLifts.toMap(),
            actionComplete = unit.hasActed,
            actionStatusRound = unit.actionStatusRound,
        )
    }

    private fun turnChanges(before: Map<String, UnitTurnSnapshot>): List<BattleUnitTurnChange> =
        before.mapNotNull { (id, old) ->
            val unit = units[id] ?: presentationUnits[id] ?: return@mapNotNull null
            val changed = old.hp != unit.hitPoints || old.mp != unit.magicPoints ||
                old.statuses != unit.statuses || old.lifts != unit.attributeLifts ||
                old.actionComplete != unit.hasActed || old.actionStatusRound != unit.actionStatusRound
            if (!changed) return@mapNotNull null
            BattleUnitTurnChange(
                unitId = id,
                hitPointsBefore = old.hp,
                hitPointsAfter = unit.hitPoints,
                magicPointsBefore = old.mp,
                magicPointsAfter = unit.magicPoints,
                statusesBefore = old.statuses,
                statusesAfter = unit.statuses.toMap(),
                attributeLiftsBefore = old.lifts,
                attributeLiftsAfter = unit.attributeLifts.toMap(),
                actionCompleteBefore = old.actionComplete,
                actionCompleteAfter = unit.hasActed,
                actionStatusRoundBefore = old.actionStatusRound,
                actionStatusRoundAfter = unit.actionStatusRound,
            )
        }

    fun unitAt(tileX: Int, tileY: Int): BattleUnit? = units.values.firstOrNull { it.visible && it.tileX == tileX && it.tileY == tileY }

    fun outcome(): BattleOutcome? {
        scriptedOutcome?.let { return it }
        // BATTLE_UNIT_FALG.HIDE changes rendering/targeting, not isExist().
        // Yingchuan's only Mine actor is hidden until round two and must still
        // prevent the opening cut-scene from being adjudicated as a loss.
        val playerRemaining = units.values.any { it.effectiveFaction().isPlayerSide() }
        val enemyRemaining = units.values.any { it.effectiveFaction().isEnemySide() }
        return when {
            round >= maxRounds -> BattleOutcome.ENEMY_VICTORY
            !enemyRemaining && playerRemaining -> BattleOutcome.PLAYER_VICTORY
            !playerRemaining && enemyRemaining -> BattleOutcome.ENEMY_VICTORY
            else -> null
        }
    }

    /** BattleLayer.setMaxRound: ZJHH contributes exactly four turns. */
    fun setMaxRounds(value: Int) {
        maxRounds = (value + if (enabledFeatures and ENABLED_FEATURE_ZJHH != 0) 4 else 0).coerceAtLeast(1)
    }

    /** A ScenarioStage setMaxRound value has already applied BattleLayer.eFlag(). */
    fun setResolvedMaxRounds(value: Int) { maxRounds = value.coerceAtLeast(1) }

    fun enabledFeatureMask(): Int = enabledFeatures

    /** Recovered BattleLayer.setWeather/setRound entry points used by EditLayer2. */
    fun applyEditedWeather(value: Int) { weather = BattleWeather.entries[value.coerceIn(BattleWeather.entries.indices)] }
    fun applyEditedRound(value: Int) { round = value.coerceAtLeast(1) }

    /** BattleLayer.skillTemp/setSkillTemp/incSkillTemp, exposed for scripts. */
    fun skillTemp(unitId: String, skillId: Int, default: Int = 0): Int = skillTemps.value(unitId, skillId, default)
    fun setSkillTemp(unitId: String, skillId: Int, amount: Int, recordedRound: Int = round) =
        skillTemps.set(unitId, skillId, amount, recordedRound)
    fun incSkillTemp(unitId: String, skillId: Int): Int = skillTemps.increment(unitId, skillId, round)
    fun setBlockedTiles(values: Collection<Pair<Int, Int>>) {
        blockedTiles.clear()
        blockedTiles.addAll(values)
    }

    /**
     * The same weighted flood-fill used by BattleLayer._showMoveArea.  This
     * is exposed to the renderer so the desktop client can show the original
     * selectable movement area instead of accepting invisible movement.
     */
    fun reachableTiles(id: String): Map<Pair<Int, Int>, Int> {
        val unit = units[id] ?: return emptyMap()
        // BattleLayer.canMovePoints exits before seeding psAry for MaBi.
        if (!unit.visible || BattleStatus.PARALYSIS in unit.statuses || unit.hasMoved || unit.hasActed) return emptyMap()
        // BattleLayer.canMovePoints seeds its walk with BattleUnit.mov_final,
        // which includes the active weather penalty.  The selectable overlay
        // must use that value too; using only the attribute lift shortened
        // S_00 unit 210's source range by five tiles.
        val movement = finalMovement(unit)
        // The source returns every psHash entry to `_showMoveArea`, including
        // the actor's current tile and same-camp occupied tiles.  Move
        // execution separately rejects occupied destinations, but omitting
        // them here made the rendered range differ from the original.
        return movePoints(unit, movement).points
            .mapValuesTo(linkedMapOf()) { (_, point) -> movement - point.remaining }
    }

    /**
     * Read-only S57 route probe after an attackable guard is removed. The
     * guard attack itself consumes the current action; source0/escort policy
     * may therefore use the following or the next real movement turn to enter
     * a leader's physical attack-staging tile. This deliberately projects no
     * more than those two turns and never mutates a BattleUnit.
     */
    fun canEnterTilesIgnoringEnemyWithinMoves(
        id: String,
        ignoredEnemyId: String,
        start: Pair<Int, Int>,
        targetTiles: Set<Pair<Int, Int>>,
        moves: Int = 2,
    ): Boolean {
        val unit = units[id] ?: return false
        if (!unit.visible || targetTiles.isEmpty() || moves < 1) return false
        val movement = finalMovement(unit)
        var frontier = linkedSetOf(start)
        repeat(moves) {
            val next = linkedSetOf<Pair<Int, Int>>()
            frontier.forEach { origin ->
                movePoints(unit, movement, ignoredEnemyId, origin).points.keys.forEach { tile ->
                    // `movePoints` retains same-camp occupants for the source
                    // overlay. A real next command cannot end on one; nor can
                    // it end on a still-live enemy other than the removed guard.
                    val occupant = unitAt(tile.first, tile.second)
                    if (tile == origin || occupant == null || occupant.id == ignoredEnemyId) next += tile
                }
            }
            if (next.any { it in targetTiles }) return true
            frontier = next
            if (frontier.isEmpty()) return false
        }
        return false
    }
    /** Scenario scripts can end a battle through reward()/lose() without eliminating every enemy. */
    fun setScriptedOutcome(value: BattleOutcome) { scriptedOutcome = value }

    /**
     * Mirrors a ScenarioStage result without clearing an outcome on ordinary
     * scene1 passes which have not called reward/lose.  Script callbacks can
     * publish this after the initial BattleLayer script invocation.
     */
    fun syncScriptedOutcome(value: BattleOutcome?) {
        value?.let { scriptedOutcome = it }
    }

    fun moveUnit(id: String, targetX: Int, targetY: Int, maxDistance: Int? = null): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val unit = units[id] ?: return TacticalActionResult.Rejected("유닛이 없습니다.")
        if (!unit.visible) return TacticalActionResult.Rejected("아직 등장하지 않은 유닛입니다.")
        if (BattleStatus.PARALYSIS in unit.statuses || BattleStatus.CONFUSION in unit.statuses) return TacticalActionResult.Rejected("행동할 수 없는 상태입니다.")
        if (unit.effectiveFaction() != activeFaction) return TacticalActionResult.Rejected("현재 진영의 유닛만 조작할 수 있습니다.")
        if (unit.hasActed) return TacticalActionResult.Rejected("이미 행동한 유닛입니다.")
        if (unit.hasMoved) return TacticalActionResult.Rejected("이미 이동한 유닛입니다.")
        if (targetX < 0 || targetY < 0 || terrain?.let { targetX >= it.width || targetY >= it.height } == true) {
            return TacticalActionResult.Rejected("맵 밖으로 이동할 수 없습니다.")
        }
        if (targetX to targetY in blockedTiles) return TacticalActionResult.Rejected("장애물이 있는 칸입니다.")
        if (unitAt(targetX, targetY) != null) return TacticalActionResult.Rejected("다른 유닛이 있는 칸입니다.")
        val route = movePoints(unit, maxDistance ?: finalMovement(unit))
        val destination = targetX to targetY
        if (destination !in route.points) return TacticalActionResult.Rejected("이동 범위를 벗어났습니다.")
        val path = route.pathTo(destination)
        // BattleLayer.unitMove returns `s.length`, and sends that exact same
        // start-inclusive `s` array into BattleUnit.move2.
        val nodes = path.size
        // move2 synchronously runs its first segment's setAction2 callback;
        // it does not face toward the dominant destination axis up front.
        path.getOrNull(1)?.let { first ->
            unit.direction = facingDirection(unit.tileX, unit.tileY, first.first, first.second)
        }
        unit.tileX = targetX
        unit.tileY = targetY
        moveLength = nodes
        lastMovePaths[id] = path
        unit.hasMoved = true
        return TacticalActionResult.Success
    }

    fun attack(attackerId: String, targetId: String, damage: Int? = null): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val attacker = units[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val target = units[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        if (!attacker.visible || !target.visible) return TacticalActionResult.Rejected("아직 등장하지 않은 유닛입니다.")
        if (attacker.effectiveFaction() != activeFaction) return TacticalActionResult.Rejected("현재 진영의 유닛만 조작할 수 있습니다.")
        if (BattleStatus.PARALYSIS in attacker.statuses || BattleStatus.CONFUSION in attacker.statuses) return TacticalActionResult.Rejected("행동할 수 없는 상태입니다.")
        if (areAllied(attacker, target)) return TacticalActionResult.Rejected("아군을 공격할 수 없습니다.")
        if (attacker.hasActed) return TacticalActionResult.Rejected("이미 행동한 유닛입니다.")
        val offset = target.tileX - attacker.tileX to target.tileY - attacker.tileY
        if (!attacker.attackAllScreen && offset !in attacker.attackOffsets) return TacticalActionResult.Rejected("공격 범위를 벗어난 적입니다.")
        // _attack2 first determines its one/two-hit loop, then creates
        // `o = e.getAtkStatus()` before entering the per-target hit loop.
        // Keep that random order and retain the result for the second pass.
        val plannedContinuousAttack = continuousAttack(attacker, target)
        // BattleLayer._attack2 creates `o = e.getAtkStatus()` before the
        // per-target hit loop.  The same records are then passed to every
        // _attack3 call in this attack sequence.
        val attackStatusBatch = rollAttackStatusBatch(attacker)
        // `_attack2` advances the opposed critical gauges before
        // countAtkHarm performs its hit check. A miss still changes BJL and
        // BBJL, which is observable on the following attack.
        val criticalRoll = damage == null && criticalHit(attacker, target)
        // BattleUnit.countBaseHarm/count_hitRate in the original client.
        // Skill, state and terrain modifiers are added separately; the base
        // arm restraint is already supplied by the original arms table.
        val hitRate = physicalHitRate(attacker, target)
        val hit = physicalHit(attacker, target, hitRate)
        val attackTerrain = attacker.terrainImpacts[terrain?.terrainAt(attacker.tileX, attacker.tileY)] ?: 100
        val defenseTerrain = target.terrainImpacts[terrain?.terrainAt(target.tileX, target.tileY)] ?: 100
        val adjustedAttack = effective(attacker, BattleAttribute.ATTACK) * attackTerrain / 100
        val adjustedDefense = privateDefense(attacker, target, BattleAttribute.DEFENSE) * defenseTerrain / 100
        val baseDamage = maxOf(1, (adjustedAttack - adjustedDefense) / 2 + 25 + attacker.level)
        val critical = hit && criticalRoll &&
            !(target.skills[49]?.and(255)?.let { it != 255 } == true && attacker.skills[227]?.and(255)?.let { it != 255 } != true)
        // count_attackHarm consumes XU_SHI while constructing this active
        // attack's target records, before hit animation/result resolution.
        val xuShiDamage = if (damage == null) consumeXuShiDamage(attacker) else 0
        val specialDamage = if (hit && damage == null) mrspDamage(attacker, target) else null
        val resolvedDamage = if (hit) {
            specialDamage ?: run {
                // The optional value is the source attack's starting harm,
                // not an already-settled result.  `_attack3` still applies
                // the ordinary percentage resistance and flat skill chain
                // to scripted/test supplied harm before HP is changed.
                if (damage == 0) return@run 0
                var normalDamage = damage?.coerceAtLeast(0)
                    ?: maxOf(1, baseDamage * physicalArmRestraint(attacker, target) / 100)
                normalDamage = normalDamage * physicalDamageRate(attacker, target) / 100
                normalDamage = physicalDamageAfterResistance(normalDamage, attacker, target)
                normalDamage += physicalFlatSkillDamage(attacker, target, activeAttack = true) + xuShiDamage
                normalDamage = maxOf(1, normalDamage)
                normalDamage = armorPiercingMinimumDamage(attacker, target, normalDamage)
                normalDamage = cappedPhysicalDamage(target, normalDamage)
                maxOf(physicalMinimumDamage(attacker), normalDamage * physicalCriticalRate(attacker, target, critical) / 100)
            }
        } else 0
        if (hit && specialDamage == null) consumeMpAttackSkill(attacker)
        // `countAtkHarm` materializes the complete target array before the
        // attack animation reaches its hit callback.  In particular, TPGJ
        // movement or a death callback from primary `_attack3` must not
        // change this pass's already-selected CTGJ targets.
        val primarySplashHarms = if (damage == null) {
            // countAtkHarm receives the raw CRIT flag. A primary miss does
            // not clear it before CTGJ harm is calculated.
            computePhysicalSplashHarms(attacker, target, criticalRoll)
        } else {
            emptyList()
        }
        val physicalPasses = mutableListOf<PhysicalAttackPass>()
        // g_charinfo stores one EXP_ADD entry per attacker and merges repeated
        // target/pass writes with max(), not sum(). Apply only after the full
        // active/counter action has settled so a level-up cannot affect a
        // follow-up that was already part of the same source `_attack`.
        // BattleUnit is a mutable data class, so it must never be a hash key.
        val experienceByAttacker = linkedMapOf<String, Pair<BattleUnit, Int>>()
        fun recordExperience(source: BattleUnit, victim: BattleUnit, victimDefeated: Boolean) {
            val reward = battleExperience(source, victim, victimDefeated)
            experienceByAttacker[source.id] = source to maxOf(experienceByAttacker[source.id]?.second ?: 0, reward)
        }
        data class EquipmentExperienceRecord(
            val recipient: BattleUnit,
            val opponent: BattleUnit,
            val kind: BattleEquipmentExperienceKind,
            val amount: Int,
        )
        val equipmentByRecipient = linkedMapOf<Pair<String, BattleEquipmentExperienceKind>, EquipmentExperienceRecord>()
        fun recordEquipment(recipient: BattleUnit, opponent: BattleUnit, sourceHarm: Int, kind: BattleEquipmentExperienceKind) {
            val amount = equipmentExperienceAmount(recipient, opponent, sourceHarm, kind)
            val key = recipient.id to kind
            if (amount > (equipmentByRecipient[key]?.amount ?: 0)) {
                equipmentByRecipient[key] = EquipmentExperienceRecord(recipient, opponent, kind, amount)
            }
        }
        fun recordPhysicalEquipment(source: BattleUnit, victim: BattleUnit, sourceHarm: Int) {
            // _attack3 always writes the defender's HJ_EXP_ADD. WQ_EXP_ADD
            // is suppressed only when the attacking unit wears civil armor.
            recordEquipment(victim, source, sourceHarm, BattleEquipmentExperienceKind.ARMOR)
            if (source.armType != 1) recordEquipment(source, victim, sourceHarm, BattleEquipmentExperienceKind.WEAPON)
        }
        val splashTargets = mutableListOf<PhysicalTarget>()
        var moneyShieldSpent = 0
        var blockRetaliationDamage = 0
        var lifeStealHealing = 0
        var qxlHealing = 0
        var recoilDamage = 0
        var playerMoneyDelta = 0
        var enemyMoneyDelta = 0
        var automaticProperty: TacticalActionResult.Item? = null
        var counterLifeStealHealing = 0
        fun recordResolution(result: PhysicalAttackTargetResult, counter: Boolean = false) {
            moneyShieldSpent += result.moneyShieldSpent
            blockRetaliationDamage += result.blockRetaliations.sumOf { it.damage }
            if (counter) counterLifeStealHealing += result.lifeStealHealing else lifeStealHealing += result.lifeStealHealing
            qxlHealing += result.qxlHealing
            recoilDamage += result.recoilDamage
            playerMoneyDelta += result.playerMoneyDelta
            enemyMoneyDelta += result.enemyMoneyDelta
            if (automaticProperty == null) automaticProperty = result.automaticProperty
        }
        val primaryPassTargets = mutableListOf<PhysicalAttackTargetResult>()
        val primaryTransfer = if (hit) physicalDamageTransfer(attacker, target, resolvedDamage) else null
        val primarySourceHarm = resolvedDamage - (primaryTransfer?.second ?: 0)
        val primaryResolution = resolvePhysicalTarget(
            attacker, target, primarySourceHarm, attackStatusBatch, activeAttack = damage == null,
        ).also(::recordResolution)
        recordExperience(attacker, target, primaryResolution.defeated)
        recordPhysicalEquipment(attacker, target, primaryResolution.sourceHarm)
        primaryPassTargets += primaryResolution
        primaryTransfer?.let { (affected, harm) ->
            val result = resolvePhysicalTarget(attacker, affected, harm, attackStatusBatch, activeAttack = damage == null)
            recordResolution(result)
            primaryPassTargets += result
            recordExperience(attacker, affected, result.defeated)
            recordPhysicalEquipment(attacker, affected, result.sourceHarm)
        }
        primarySplashHarms.forEach { (affected, harm) ->
            val result = resolvePhysicalTarget(attacker, affected, harm, attackStatusBatch, activeAttack = true)
            recordResolution(result)
            primaryPassTargets += result
            // countAtkHarm's CTGJ record keeps the calculated `o` payload;
            // _attack3 clamps only the later HP mutation.  Preserve that
            // legacy payload here while physicalPasses exposes settled damage.
            splashTargets += PhysicalTarget(result.targetId, harm)
            recordExperience(attacker, affected, result.defeated)
            recordPhysicalEquipment(attacker, affected, result.sourceHarm)
        }
        // countAtkHarm returns its incoming CRIT flag even when the hit is
        // guarded/missed; source checkCrit/say4 is gated by that raw flag.
        val primaryCriticalSpeech = resolveCriticalSpeech(attacker, criticalRoll)
        physicalPasses += PhysicalAttackPass(
            kind = PhysicalAttackPassKind.ACTIVE,
            attackerId = attacker.id,
            // `_attack2` chooses anime21 from its CRIT flag before
            // countAtkHarm can turn the hit into FYZMGJ guard/miss.
            critical = criticalRoll,
            targets = primaryPassTargets,
            primaryTargetId = target.id,
            criticalSpeech = primaryCriticalSpeech,
        )
        // Keep Attack.damage's established count_attackHarm payload: normal
        // HP harm is the calculated value even when the victim has less HP.
        // `_attack3`-local shields are the exceptions and expose 0/1.  The
        // per-target pass result separately retains the actual clamped HP
        // delta required by the renderer.
        val primaryHpDamage = primaryResolution.let { resolution ->
            when {
                resolution.mpShieldDamage > 0 -> 0
                resolution.moneyShieldSpent > 0 -> resolution.damage
                else -> primarySourceHarm
            }
        }
        val mpShieldDamage = primaryResolution.mpShieldDamage
        attacker.markActionComplete()
        var defeated = target.hitPoints <= 0
        var followUpDamage = 0
        var followUpMpShieldDamage = 0
        var followUpCritical = false
        // BattleLayer._attack2 starts with the count_sjl decision, but a
        // landed critical with BJBLJ changes its loop limit from one to two
        // before the next iteration.  This is not a separate proc: it is the
        // same two-pass loop, so it must also work when the ordinary SJL roll
        // did not grant a follow-up.
        val criticalFollowUp = criticalRoll && attacker.skills[7]?.and(255)?.let { it != 255 } == true // BJBLJ
        if (attacker.hitPoints > 0 && !defeated && (plannedContinuousAttack || criticalFollowUp)) {
            val followUpCriticalRoll = criticalHit(attacker, target)
            val followUpHit = target.skills[47]?.and(255)?.let { it != 255 } != true && physicalHit(attacker, target, hitRate)
            val followUpIsCritical = followUpHit && followUpCriticalRoll
            val followUpPassTargets = mutableListOf<PhysicalAttackTargetResult>()
            followUpCritical = followUpIsCritical
            val followUpSpecialDamage = if (followUpHit) mrspDamage(attacker, target) else null
            val followUpSourceHarm = if (followUpHit) {
                followUpSpecialDamage ?: run {
                    var raw = maxOf(1, baseDamage * physicalArmRestraint(attacker, target) / 100)
                    raw = raw * physicalDamageRate(attacker, target) / 100
                    raw = physicalDamageAfterResistance(raw, attacker, target) + physicalFlatSkillDamage(attacker, target)
                    raw = maxOf(1, raw)
                    raw = armorPiercingMinimumDamage(attacker, target, raw)
                    maxOf(physicalMinimumDamage(attacker), cappedPhysicalDamage(target, raw) * physicalCriticalRate(attacker, target, followUpIsCritical, continuous = true) / 100)
                }
            } else 0
            if (followUpHit && followUpSpecialDamage == null) consumeMpAttackSkill(attacker)
            val followUpSplashHarms = if (damage == null) {
                computePhysicalSplashHarms(attacker, target, followUpCriticalRoll, continuous = true)
            } else {
                emptyList()
            }
            val transfer = if (followUpHit) physicalDamageTransfer(attacker, target, followUpSourceHarm) else null
            val primaryHarm = followUpSourceHarm - (transfer?.second ?: 0)
            val followUpPrimary = resolvePhysicalTarget(
                attacker,
                target,
                primaryHarm,
                attackStatusBatch,
                activeAttack = damage == null,
            )
            recordExperience(attacker, target, followUpPrimary.defeated)
            recordPhysicalEquipment(attacker, target, followUpPrimary.sourceHarm)
            recordResolution(followUpPrimary)
            followUpPassTargets += followUpPrimary
            followUpDamage = followUpPrimary.damage
            followUpMpShieldDamage = followUpPrimary.mpShieldDamage
            transfer?.let { (affected, harm) ->
                val result = resolvePhysicalTarget(attacker, affected, harm, attackStatusBatch, activeAttack = damage == null)
                recordResolution(result)
                followUpPassTargets += result
                recordExperience(attacker, affected, result.defeated)
                recordPhysicalEquipment(attacker, affected, result.sourceHarm)
            }
            followUpSplashHarms.forEach { (affected, harm) ->
                val result = resolvePhysicalTarget(attacker, affected, harm, attackStatusBatch, activeAttack = true)
                recordResolution(result)
                followUpPassTargets += result
                splashTargets += PhysicalTarget(result.targetId, harm)
                recordExperience(attacker, affected, result.defeated)
                recordPhysicalEquipment(attacker, affected, result.sourceHarm)
            }
            val followUpCriticalSpeech = resolveCriticalSpeech(attacker, followUpCriticalRoll)
            physicalPasses += PhysicalAttackPass(
                kind = PhysicalAttackPassKind.ACTIVE_FOLLOW_UP,
                attackerId = attacker.id,
                critical = followUpCriticalRoll,
                targets = followUpPassTargets,
                primaryTargetId = target.id,
                criticalSpeech = followUpCriticalSpeech,
            )
            defeated = target.hitPoints <= 0
        }
        // BattleLayer._attack6 gives CLFJ its configured magic counter first.
        // A legal `_magic` result suppresses physical retaliation entirely.
        val counterMagic = target.skills[13]?.and(255)?.takeIf { it != 255 }
            ?.let { magicId -> castMagic(target.id, attacker.id, magicId, reaction = true) as? TacticalActionResult.Magic }
        val canCounter = counterMagic == null && attacker.hitPoints > 0 && !defeated && target.visible && attacker.skills[226]?.and(255)?.let { it == 255 } != false && canAttack(target, attacker) &&
            BattleStatus.PARALYSIS !in target.statuses && BattleStatus.CONFUSION !in target.statuses
        var counterDamage = 0
        var counterFollowUpDamage = 0
        var counterMpShieldDamage = 0
        var counterFollowUpMpShieldDamage = 0
        var counterCriticalResult = false
        var counterFollowUpCritical = false
        if (canCounter) {
            val counterStatusBatch = rollAttackStatusBatch(target)
            val counterHitRate = physicalHitRate(target, attacker)
            val counterCriticalRoll = criticalHit(target, attacker)
            val counterHit = physicalHit(target, attacker, counterHitRate)
            val counterAttackTerrain = target.terrainImpacts[terrain?.terrainAt(target.tileX, target.tileY)] ?: 100
            val counterDefenseTerrain = attacker.terrainImpacts[terrain?.terrainAt(attacker.tileX, attacker.tileY)] ?: 100
            val counterBase = maxOf(1, ((effective(target, BattleAttribute.ATTACK) * counterAttackTerrain / 100 - privateDefense(target, attacker, BattleAttribute.DEFENSE) * counterDefenseTerrain / 100) / 2) + 25 + target.level)
            val counterCritical = counterHit && counterCriticalRoll
            val counterPassTargets = mutableListOf<PhysicalAttackTargetResult>()
            counterCriticalResult = counterCritical
            val counterSourceHarm = if (counterHit) {
                var counterRaw = maxOf(1, counterBase * physicalArmRestraint(target, attacker) / 100)
                counterRaw = counterRaw * physicalDamageRate(target, attacker) / 100
                counterRaw = physicalDamageAfterResistance(counterRaw, target, attacker) + physicalFlatSkillDamage(target, attacker)
                counterRaw = maxOf(1, counterRaw)
                counterRaw = armorPiercingMinimumDamage(target, attacker, counterRaw)
                maxOf(physicalMinimumDamage(target), cappedPhysicalDamage(attacker, counterRaw) * physicalCriticalRate(target, attacker, counterCritical, counter = true) / 100)
            } else 0
            if (counterHit) {
                consumeMpAttackSkill(target)
            }
            val counterSplashHarms = computePhysicalSplashHarms(
                attacker = target,
                primaryTarget = attacker,
                critical = counterCriticalRoll,
                activeAttack = false,
                counter = true,
            )
            val transfer = if (counterHit) physicalDamageTransfer(target, attacker, counterSourceHarm) else null
            val primaryHarm = counterSourceHarm - (transfer?.second ?: 0)
            val counterPrimary = resolvePhysicalTarget(target, attacker, primaryHarm, counterStatusBatch, activeAttack = false)
            recordExperience(target, attacker, counterPrimary.defeated)
            recordPhysicalEquipment(target, attacker, counterPrimary.sourceHarm)
            recordResolution(counterPrimary, counter = true)
            counterPassTargets += counterPrimary
            counterDamage = counterPrimary.damage
            counterMpShieldDamage = counterPrimary.mpShieldDamage
            transfer?.let { (affected, harm) ->
                val result = resolvePhysicalTarget(target, affected, harm, counterStatusBatch, activeAttack = false)
                recordResolution(result, counter = true)
                counterPassTargets += result
                recordExperience(target, affected, result.defeated)
                recordPhysicalEquipment(target, affected, result.sourceHarm)
            }
            counterSplashHarms.forEach { (affected, harm) ->
                val result = resolvePhysicalTarget(target, affected, harm, counterStatusBatch, activeAttack = false)
                recordResolution(result, counter = true)
                counterPassTargets += result
                recordExperience(target, affected, result.defeated)
                recordPhysicalEquipment(target, affected, result.sourceHarm)
            }
            val counterCriticalSpeech = resolveCriticalSpeech(target, counterCriticalRoll)
            physicalPasses += PhysicalAttackPass(
                kind = PhysicalAttackPassKind.COUNTER,
                attackerId = target.id,
                critical = counterCriticalRoll,
                targets = counterPassTargets,
                primaryTargetId = attacker.id,
                criticalSpeech = counterCriticalSpeech,
            )
            // _attack2 applies BJBLJ from the raw critical roll even when
            // countAtkHarm settles that pass as a zero-harm guard/miss.
            val forcedCounterFollowUp =
                listOf(197, 43).any { target.skills[it]?.and(255)?.let { value -> value != 255 } == true } ||
                    (counterCriticalRoll && target.skills[7]?.and(255)?.let { value -> value != 255 } == true)
            if (attacker.hitPoints > 0 && forcedCounterFollowUp) {
                val secondCriticalRoll = criticalHit(target, attacker)
                val secondHit = physicalHit(target, attacker, counterHitRate)
                val counterFollowUpTargets = mutableListOf<PhysicalAttackTargetResult>()
                counterFollowUpCritical = secondHit && secondCriticalRoll
                val counterFollowUpSourceHarm = if (secondHit) {
                    var raw = maxOf(1, counterBase * physicalArmRestraint(target, attacker) / 100)
                    raw = raw * physicalDamageRate(target, attacker) / 100
                    raw = physicalDamageAfterResistance(raw, target, attacker) + physicalFlatSkillDamage(target, attacker)
                    raw = maxOf(1, raw)
                    raw = armorPiercingMinimumDamage(target, attacker, raw)
                    maxOf(physicalMinimumDamage(target), cappedPhysicalDamage(attacker, raw) * physicalCriticalRate(target, attacker, secondCriticalRoll, counter = true, continuous = true) / 100)
                } else 0
                if (secondHit) consumeMpAttackSkill(target)
                val counterFollowUpSplashHarms = computePhysicalSplashHarms(
                    attacker = target,
                    primaryTarget = attacker,
                    critical = secondCriticalRoll,
                    activeAttack = false,
                    counter = true,
                    continuous = true,
                )
                val transfer = if (secondHit) physicalDamageTransfer(target, attacker, counterFollowUpSourceHarm) else null
                val primaryHarm = counterFollowUpSourceHarm - (transfer?.second ?: 0)
                val secondPrimary = resolvePhysicalTarget(target, attacker, primaryHarm, counterStatusBatch, activeAttack = false)
                recordExperience(target, attacker, secondPrimary.defeated)
                recordPhysicalEquipment(target, attacker, secondPrimary.sourceHarm)
                recordResolution(secondPrimary, counter = true)
                counterFollowUpTargets += secondPrimary
                counterFollowUpDamage = secondPrimary.damage
                counterFollowUpMpShieldDamage = secondPrimary.mpShieldDamage
                transfer?.let { (affected, harm) ->
                    val result = resolvePhysicalTarget(target, affected, harm, counterStatusBatch, activeAttack = false)
                    recordResolution(result, counter = true)
                    counterFollowUpTargets += result
                    recordExperience(target, affected, result.defeated)
                    recordPhysicalEquipment(target, affected, result.sourceHarm)
                }
                counterFollowUpSplashHarms.forEach { (affected, harm) ->
                    val result = resolvePhysicalTarget(target, affected, harm, counterStatusBatch, activeAttack = false)
                    recordResolution(result, counter = true)
                    counterFollowUpTargets += result
                    recordExperience(target, affected, result.defeated)
                    recordPhysicalEquipment(target, affected, result.sourceHarm)
                }
                val counterFollowUpCriticalSpeech = resolveCriticalSpeech(target, secondCriticalRoll)
                physicalPasses += PhysicalAttackPass(
                    kind = PhysicalAttackPassKind.COUNTER_FOLLOW_UP,
                    attackerId = target.id,
                    critical = secondCriticalRoll,
                    targets = counterFollowUpTargets,
                    primaryTargetId = attacker.id,
                    criticalSpeech = counterFollowUpCriticalSpeech,
                )
            }
        }
        val attackerDefeated = attacker.hitPoints <= 0
        if (attackerDefeated) removeUnit(attacker.id)
        experienceByAttacker.values.forEach { (unit, reward) -> notifyBattleExperience(unit, reward) }
        equipmentByRecipient.values.forEach { record ->
            notifyEquipmentExperienceAward(record.recipient, record.opponent, record.amount, record.kind)
        }
        return TacticalActionResult.Attack(
            damage = primaryHpDamage,
            defeated = defeated,
            hitRate = hitRate,
            hit = hit,
            critical = critical,
            counterDamage = counterDamage,
            attackerDefeated = attackerDefeated,
            lifeStealHealing = lifeStealHealing,
            followUpDamage = followUpDamage,
            followUpMpShieldDamage = followUpMpShieldDamage,
            counterFollowUpDamage = counterFollowUpDamage,
            counterMpShieldDamage = counterMpShieldDamage,
            counterFollowUpMpShieldDamage = counterFollowUpMpShieldDamage,
            counterLifeStealHealing = counterLifeStealHealing,
            followUpCritical = followUpCritical,
            counterCritical = counterCriticalResult,
            counterFollowUpCritical = counterFollowUpCritical,
            splashTargets = splashTargets,
            mpShieldDamage = mpShieldDamage,
            qxlHealing = qxlHealing,
            recoilDamage = recoilDamage,
            blockRetaliationDamage = blockRetaliationDamage,
            moneyShieldSpent = moneyShieldSpent,
            playerMoneyDelta = playerMoneyDelta,
            enemyMoneyDelta = enemyMoneyDelta,
            counterMagic = counterMagic,
            counterMagicId = counterMagic?.let { target.skills[13]?.and(255) },
            automaticProperty = automaticProperty,
            physicalPasses = physicalPasses,
        )
    }

    /**
     * Original BattleUnit.count_attackHarm: PJGJ (174) raises damage to the
     * configured percentage of the defender's maximum HP.  defineSkill.bin
     * marks this skill with ARG=1, meaning the resolved effect is a percent.
     */
    private fun armorPiercingMinimumDamage(attacker: BattleUnit, target: BattleUnit, currentDamage: Int): Int {
        val percent = attacker.skills[174]?.and(255)?.takeIf { it != 255 } ?: return currentDamage
        return maxOf(currentDamage, percent * target.maxHitPoints / 100)
    }

    /** BattleUnit.count_attackHarm: XZSH (242) caps pre-critical damage. */
    private fun cappedPhysicalDamage(target: BattleUnit, currentDamage: Int): Int =
        target.skills[242]?.and(255)?.takeIf { it != 255 }?.let { minOf(currentDamage, it) } ?: currentDamage

    /** BattleUnit.count_attackHarm applies this floor after every modifier. */
    private fun physicalMinimumDamage(attacker: BattleUnit): Int {
        if (attacker.isPlayerSide() || attacker.armType == 1) return 1
        val famousMineCount = units.values.count { it.visible && it.isPlayerSide() && it.famous }
        return maxOf(1, attacker.maxHitPoints * minOf(7, famousMineCount) / 100)
    }

    /**
     * BattleUnit._countAttackHarmAdd's stateless damage additions.  Temporary
     * per-turn effects (charge, MP consumption, moved-tile counters) remain
     * in the turn runtime; these source skills are directly derivable here.
     */
    private fun physicalFlatSkillDamage(attacker: BattleUnit, target: BattleUnit, activeAttack: Boolean = false): Int {
        fun effect(unit: BattleUnit, skill: Int): Int? = unit.skills[skill]?.and(255)?.takeIf { it != 255 }
        var addition = 0
        effect(attacker, 9)?.let { addition += attacker.hitPoints * it / 100 } // BIAO_HAN
        effect(attacker, 141)?.let { addition += effective(attacker, BattleAttribute.SPIRIT) * it / 100 } // LRHY
        effect(attacker, 33)?.let { addition += target.magicPoints * it / 100 } // DI_FA
        effect(attacker, 183)?.let { addition += attacker.martial * it * 10 / 100 } // QXJD
        if (activeAttack) effect(attacker, 26)?.let { addition += skillTemp(attacker.id, 26) * it } // CHGJ
        if (activeAttack && moveLength >= 2) effect(attacker, 25)?.let { addition += (moveLength - 1) * it } // CFGJ
        // JDGJ counts every existing unit in the attacker's BU_BING area;
        // filterHitAreaUnit(..., 8) deliberately has no camp filter.
        effect(attacker, 109)?.let { addition += it * infantryOffsets.count { (dx, dy) ->
            unitAt(attacker.tileX + dx, attacker.tileY + dy)?.visible == true
        } }
        val fixed = attacker.level / 2 + 15
        if (effect(attacker, 95) != null) addition += fixed // GDZS
        if (effect(target, 95) != null) addition -= target.level / 2 + 15
        listOf(
            80 to BattleAttribute.ATTACK,
            79 to BattleAttribute.DEFENSE,
            81 to BattleAttribute.SPIRIT,
            78 to BattleAttribute.CRITICAL,
            83 to BattleAttribute.MORALE,
        ).forEach { (skill, attribute) ->
            effect(attacker, skill)?.let { addition += effective(attacker, attribute) * it / 100 }
        }
        return addition
    }

    /** BattleUnit._countAttackHarmAdd: MPGJ consumes one MP for a normal hit. */
    private fun consumeMpAttackSkill(attacker: BattleUnit) {
        if (attacker.skills[4]?.and(255)?.let { it != 255 } == true) attacker.addMpcur(-1)
    }

    /** BattleUnit.count_attackHarm's one-shot XU_SHI(243) addition. */
    private fun consumeXuShiDamage(attacker: BattleUnit): Int {
        val effect = attacker.skills[243]?.and(255)?.takeIf { it != 255 } ?: return 0
        val stored = skillTemp(attacker.id, 243)
        if (stored < 1) return 0
        setSkillTemp(attacker.id, 243, 0)
        return stored * effect
    }

    /** BattleLayer._jiesuan's CHGJ increment for a ZHUDONG/CTGJ defender. */
    private fun accumulateChargeWhenHit(defender: BattleUnit, activeAttack: Boolean) {
        if (activeAttack && defender.skills[26]?.and(255)?.let { it != 255 } == true) {
            incSkillTemp(defender.id, 26)
        }
    }

    /** BattleUnit.count_attackHarm's MRSP: a five-step max-HP damage roll. */
    private fun mrspDamage(attacker: BattleUnit, target: BattleUnit): Int? {
        if (attacker.skills[156]?.and(255)?.let { it != 255 } != true) return null
        return target.maxHitPoints * BattleMrspDamage.percent(sourceRandom100()) / 100
    }

    /** Direct port of BattleUnit._countAttackHarmRate. */
    private fun physicalDamageRate(attacker: BattleUnit, target: BattleUnit): Int {
        fun effect(unit: BattleUnit, skill: Int): Int? = unit.skills[skill]?.and(255)?.takeIf { it != 255 }
        var rate = 100
        if (BattleStatus.CONFUSION in target.statuses) rate += 10
        // filterHitAreaUnit(target, BU_BING, 13): existing same-isMine unit,
        // stopping after the first one.  QI_LING applies only when none is
        // found; the source does not include SELF for this filter.
        val targetIsMine = target.isPlayerSide()
        val targetHasNearbyCampUnit = infantryOffsets.any { (dx, dy) ->
            unitAt(target.tileX + dx, target.tileY + dy)?.let { it.isPlayerSide() == targetIsMine } == true
        }
        if (!targetHasNearbyCampUnit) effect(attacker, 176)?.let { rate += it } // QI_LING
        // ARM_ATTR_NAME2.MOVESOUND selects mounted/wheeled/foot attack bonuses.
        when (target.armMoveSound) {
            0 -> effect(attacker, 129)?.let { rate += it } // JMGJ
            1 -> effect(attacker, 164)?.let { rate += it } // PCGJ
            2 -> effect(attacker, 11)?.let { rate += it } // BBGJ
        }
        // HU_XI checks whether the attacker has any abnormal (MB..ZD) state.
        if (attacker.statuses.isNotEmpty()) effect(attacker, 99)?.let { rate += it }
        effect(attacker, 110)?.let { rate += (14 - finalMovement(target)) * it } // JFGJ
        effect(attacker, 312)?.let { effect -> // JFGJ2
            rate += 5 * (effect - if (BattleStatus.PARALYSIS in target.statuses) 0 else finalMovement(target))
        }
        effect(attacker, 104)?.let { value -> // HMGJ
            val direction = facingDirection(attacker.tileX, attacker.tileY, target.tileX, target.tileY)
            rate += when {
                direction == attacker.direction -> value / 3
                direction % 2 != attacker.direction % 2 -> value / 2
                else -> value
            }
        }
        if (!hasPhysicalEffectTargets(attacker, target)) effect(attacker, 126)?.let { rate += it } // TJGJ
        val sameLine = attacker.tileX == target.tileX || attacker.tileY == target.tileY
        val dx = kotlin.math.abs(attacker.tileX - target.tileX)
        val dy = kotlin.math.abs(attacker.tileY - target.tileY)
        if (sameLine && dx + dy < 3) effect(attacker, 234)?.let { rate += it } // WU_BIAN
        effect(attacker, 184)?.let { rate += 5 * (14 - target.movement) } // QJTJ
        if (backPosition(target, attacker) == null) effect(attacker, 221)?.let { rate += it } // TPGJ
        effect(attacker, 114)?.let { rate += it } // JQGJ
        effect(attacker, 292)?.let { rate += 10 + sourceFlagRandom(0, 5) } // MRSP2
        if (sameLine) effect(target, 6)?.let { rate -= it } // BA_HAI
        if (!sameLine) effect(target, 121)?.let { rate -= it } // JSXXSH
        if (dx == 1 && dy == 1) effect(target, 132)?.let { rate -= it } // JUAN_WU
        effect(target, 118)?.let { rate -= it } // JQWLSH
        effect(target, 245)?.let { rate -= (target.maxHitPoints - target.hitPoints) * 100 / target.maxHitPoints.coerceAtLeast(1) } // XZDD
        effect(target, 247)?.let { rate += target.movement * it } // XLGJ
        if (attacker.armMoveSound == 0) effect(target, 139)?.let { rate -= it } // KZQB
        effect(target, 250)?.let { rate -= if (backPosition(target, attacker) == null) it / 2 else it } // YI_BU
        effect(target, 275)?.let { value -> // ZHONG_ZHUANG
            val incoming = facingDirection(attacker.tileX, attacker.tileY, target.tileX, target.tileY)
            rate -= when {
                incoming == target.direction -> value / 3
                incoming % 2 == target.direction % 2 -> 0
                else -> 2 * (value / 3)
            }
        }
        return rate
    }

    /** BattleUnit._count_atk_crit, excluding only the unrepresented YDGJ flag. */
    private fun physicalCriticalRate(
        attacker: BattleUnit,
        target: BattleUnit,
        critical: Boolean,
        counter: Boolean = false,
        continuous: Boolean = false,
        splash: Boolean = false,
    ): Int {
        fun effect(unit: BattleUnit, skill: Int): Int? = unit.skills[skill]?.and(255)?.takeIf { it != 255 }
        var rate = 100
        if (critical) {
            rate += 50
            // Source checks `if (this.skill(ZMYJZS))` rather than comparing
            // with 255.  Missing skills return 255 (truthy in JavaScript), so
            // ordinary criticals receive this extra 30%; only an explicit
            // zero disables it.
            if ((attacker.skills[271]?.and(255) ?: 255) != 0) rate += 30 // ZMYJZS
        }
        if (counter) {
            effect(attacker, 46)?.let { bonus -> // FAN_SHI consumes one stored counter bonus.
                if (skillTemp(attacker.id, 46) != 0) {
                    setSkillTemp(attacker.id, 46, 0)
                    rate += bonus
                }
            }
            if (effect(attacker, 181) == null) rate -= 25 // QHFJ
        }
        if (continuous && effect(attacker, 291) == null) rate -= 25 // QHLJ
        if (splash) rate -= 20 // CTGJ
        effect(attacker, 217)?.let { bonus -> // TXGJ
            val incoming = facingDirection(attacker.tileX, attacker.tileY, target.tileX, target.tileY)
            rate += when {
                incoming == target.direction -> bonus
                incoming % 2 == target.direction % 2 -> bonus - 20
                else -> bonus - 10
            }
        }
        return rate
    }

    /** BattleUnit.count_sjl + BattleLayer._attack2 continuous-attack gate. */
    private fun continuousAttack(attacker: BattleUnit, target: BattleUnit): Boolean {
        val forced = listOf(197, 276).any { attacker.skills[it]?.and(255)?.let { value -> value != 255 } == true }
        val own = effective(attacker, BattleAttribute.CRITICAL).toDouble()
        val opponent = privateDefense(attacker, target, BattleAttribute.CRITICAL).coerceAtLeast(1).toDouble()
        val rate = when {
            own >= 3 * opponent -> 100
            own >= 2 * opponent -> ((own / opponent * .8 - 1.4) * 100).toInt()
            own >= opponent -> ((own / opponent * .18 - .16) * 100).toInt()
            else -> 0
        }.coerceIn(0, 100)
        // BattleLayer._attack2 always calls countRate first, then SQGJ/ZDLJ
        // overrides only `h`.  Preserve that otherwise invisible gauge
        // mutation for the next non-forced attack.
        val rolled = countRate(attacker, target, Rate.SJL, Rate.BSJL, rate)
        return forced || rolled
    }

    /**
     * BattleUnit._count_atk_bzsx.  JDKZ2 (316) fixes the matchup to 130% or
     * 70% before the normal arms-table rate; JDKZ (133) then shifts that
     * normal table rate for the attacker or defender.
     */
    private fun physicalArmRestraint(attacker: BattleUnit, target: BattleUnit): Int {
        if (attacker.skills[316]?.and(255)?.let { it != 255 } == true) return 130
        if (target.skills[316]?.and(255)?.let { it != 255 } == true) return 70
        return (attacker.armRestraints[target.armId] ?: 100) +
            (attacker.skills[133]?.and(255)?.takeIf { it != 255 } ?: 0) -
            (target.skills[133]?.and(255)?.takeIf { it != 255 } ?: 0)
    }

    /**
     * BattleLayer.showUseProperty + _usePro2 for the portable combat
     * consumables.  The original permits selecting an allied target in the
     * infantry hit area; this tactical port uses the same adjacent area.
     */
    fun useProperty(userId: String, targetId: String, itemId: Int): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val user = units[userId] ?: return TacticalActionResult.Rejected("사용 유닛이 없습니다.")
        val target = units[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        val item = propertyItems[itemId] ?: return TacticalActionResult.Rejected("사용할 수 없는 아이템입니다.")
        if (user.effectiveFaction() != activeFaction || user.hasActed) return TacticalActionResult.Rejected("현재 행동할 수 없는 유닛입니다.")
        if (!areAllied(user, target)) return TacticalActionResult.Rejected("아군에게만 사용할 수 있습니다.")
        val offset = target.tileX - user.tileX to target.tileY - user.tileY
        if (target != user && offset !in movementOffsets) return TacticalActionResult.Rejected("아이템 사용 범위를 벗어났습니다.")
        val applied = applyProperty(item, target) { consumeSelectedProperty(itemId) }
            ?: return TacticalActionResult.Rejected("아이템을 사용할 수 없습니다.")
        user.markActionComplete()
        return applied
    }

    /**
     * BattleLayer._usePro2's state mutation, shared by the player-selected
     * path and `_attack3` ZDSY.  The caller owns inventory mutation because
     * ZDSY uses ItemStore.pushProperty directly before entering _usePro2.
     */
    private fun applyProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        consume: () -> Boolean,
    ): TacticalActionResult.Item? {
        val effect = when (item.itemType) {
            26 -> {
                if (target.hitPoints >= target.maxHitPoints || !consume()) return null
                val amount = if (item.value == 255) target.maxHitPoints else item.value
                val recovered = minOf(amount, target.maxHitPoints - target.hitPoints)
                target.addHpcur(recovered)
                "HP ${recovered} 회복"
            }
            27 -> {
                if (target.magicPoints >= target.maxMagicPoints || !consume()) return null
                val amount = if (item.value == 255) target.maxMagicPoints else item.value
                val recovered = minOf(amount, target.maxMagicPoints - target.magicPoints)
                target.addMpcur(recovered)
                "MP ${recovered} 회복"
            }
            28, 29, 30, 31 -> {
                val status = listOf(BattleStatus.CONFUSION, BattleStatus.POISON, BattleStatus.PARALYSIS, BattleStatus.SILENCE)[item.itemType - 28]
                if (status !in target.statuses || !consume()) return null
                target.statuses.remove(status)
                target.refStateAnime()
                "${status.label()} 치료"
            }
            32 -> {
                if (target.statuses.isEmpty() || !consume()) return null
                target.statuses.clear()
                target.refStateAnime()
                "모든 이상 상태 치료"
            }
            33, 34, 35, 36, 37 -> {
                // _usePro2: WL, ZL, TS, MJ, YQ -> ATT, SPR, DEF, CRI, MOR.
                val attribute = listOf(BattleAttribute.ATTACK, BattleAttribute.SPIRIT, BattleAttribute.DEFENSE, BattleAttribute.CRITICAL, BattleAttribute.MORALE)[item.itemType - 33]
                if (!consume()) return null
                target.applySourceAttributeLift(attribute, 1, 3)
                "${attribute.label()} 상승"
            }
            42 -> {
                if (!consume()) return null
                target.maxHitPoints += item.value
                target.addHpcur(item.value)
                notifyPermanentProperty(item, target)
                "최대 HP ${item.value} 증가"
            }
            43 -> {
                if (!consume()) return null
                target.maxMagicPoints += item.value
                target.addMpcur(item.value)
                notifyPermanentProperty(item, target)
                "최대 MP ${item.value} 증가"
            }
            else -> return null
        }
        return TacticalActionResult.Item(item.name, target.id, effect)
    }

    /** BattleLayer.attackAction: scripted/cinematic attack outside normal turn input. */
    fun forcedAttack(attackerId: String, targetId: String): TacticalActionResult {
        val attacker = units[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val target = units[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        if (!attacker.visible || !target.visible || areAllied(attacker, target)) return TacticalActionResult.Rejected("강제 공격 대상을 찾을 수 없습니다.")
        val hitRate = physicalHitRate(attacker, target)
        val criticalRoll = criticalHit(attacker, target)
        val hit = physicalHit(attacker, target, hitRate)
        val attackTerrain = attacker.terrainImpacts[terrain?.terrainAt(attacker.tileX, attacker.tileY)] ?: 100
        val defenseTerrain = target.terrainImpacts[terrain?.terrainAt(target.tileX, target.tileY)] ?: 100
        val base = maxOf(1, ((effective(attacker, BattleAttribute.ATTACK) * attackTerrain / 100 - privateDefense(attacker, target, BattleAttribute.DEFENSE) * defenseTerrain / 100) / 2) + 25 + attacker.level)
        val critical = hit && criticalRoll &&
            !(target.skills[49]?.and(255)?.let { it != 255 } == true && attacker.skills[227]?.and(255)?.let { it != 255 } != true)
        val specialDamage = if (hit) mrspDamage(attacker, target) else null
        val damage = if (hit) {
            specialDamage ?: run {
                var raw = maxOf(1, base * physicalArmRestraint(attacker, target) / 100)
                raw = raw * physicalDamageRate(attacker, target) / 100
                raw = physicalDamageAfterResistance(raw, attacker, target)
                raw += physicalFlatSkillDamage(attacker, target)
                raw = maxOf(1, raw)
                raw = armorPiercingMinimumDamage(attacker, target, raw)
                maxOf(physicalMinimumDamage(attacker), cappedPhysicalDamage(target, raw) * physicalCriticalRate(attacker, target, critical) / 100)
            }
        } else 0
        if (hit && specialDamage == null) consumeMpAttackSkill(attacker)
        target.addHpcur(-damage)
        val lifeStealHealing = attacker.skills[238]?.and(255)?.takeIf { it != 255 && damage > 0 }
            ?.let { minOf(attacker.maxHitPoints - attacker.hitPoints, it * damage / 100) } ?: 0
        attacker.addHpcur(lifeStealHealing)
        val defeated = target.hitPoints <= 0
        if (hit) notifyPhysicalDamage(attacker, target, damage)
        if (defeated) {
            notifyUnitDefeated(attacker, target)
            removeUnit(target.id)
        }
        return TacticalActionResult.Attack(damage, defeated, hitRate, hit, critical, lifeStealHealing = lifeStealHealing)
    }

    /**
     * Original offensive-strategy baseline: range/MP/area, magic hit rate,
     * spirit formula and defender arm magic resistance. Status/weather/skill
     * modifiers are resolved by the higher-level script layer.
     */
    fun castMagic(
        attackerId: String,
        targetId: String,
        magicId: Int,
        reaction: Boolean = false,
        bypassCondition: Boolean = false,
    ): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val attacker = units[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val target = units[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        val magic = attacker.magic.firstOrNull { it.id == magicId } ?: return TacticalActionResult.Rejected("사용할 수 없는 전략입니다.")
        if (!attacker.visible || !target.visible || (!reaction && (attacker.effectiveFaction() != activeFaction || attacker.hasActed))) return TacticalActionResult.Rejected("현재 유닛은 전략을 사용할 수 없습니다.")
        if (BattleStatus.PARALYSIS in attacker.statuses || BattleStatus.CONFUSION in attacker.statuses || BattleStatus.SILENCE in attacker.statuses) return TacticalActionResult.Rejected("현재 상태에서는 전략을 사용할 수 없습니다.")
        if (magic.target == 2) {
            if (attacker.magicPoints < magic.expendMp) return TacticalActionResult.Rejected("MP가 부족합니다.")
            attacker.addMpcur(-magic.expendMp)
            if (!reaction) attacker.markActionComplete()
            weather = when (magic.id) {
                58 -> BattleWeather.HEAVY_RAIN // HAOYU
                59 -> BattleWeather.CLEAR // QINGMING
                60 -> BattleWeather.CLOUDY // YINGTIAN
                else -> weather
            }
            return TacticalActionResult.Magic(magic.name, magic.expendMp, emptyList())
        }
        val targetsAllies = magic.target == 1
        val targetsAny = magic.target == 3
        if (!targetsAny && ((targetsAllies && !areAllied(attacker, target)) || (!targetsAllies && areAllied(attacker, target)))) {
            return TacticalActionResult.Rejected(if (targetsAllies) "아군만 대상으로 할 수 있는 전략입니다." else "적군만 대상으로 할 수 있는 전략입니다.")
        }
        val offset = target.tileX - attacker.tileX to target.tileY - attacker.tileY
        if (magic.category !in setOf(1, 29) && !magic.hitArea.allScreen && offset !in magic.hitArea.offsets) return TacticalActionResult.Rejected("전략 범위를 벗어났습니다.")
        if (!magicTerrainAllowed(magic, target)) return TacticalActionResult.Rejected("이 지형에서는 사용할 수 없는 전략입니다.")
        if (!bypassCondition) magicConditionReason(attacker, magic)?.let { return TacticalActionResult.Rejected(it) }
        if (attacker.magicPoints < magic.expendMp) return TacticalActionResult.Rejected("MP가 부족합니다.")
        attacker.addMpcur(-magic.expendMp)
        if (!reaction) attacker.markActionComplete()
        // BattleLayer._magic performs one morale critical check against the
        // selected primary target before processing any effect targets.  The
        // resulting flag is shared by both CLLJ passes and advances BJL/BBJL
        // even when the critical check fails.
        val magicCritical = magic.harmType != 4 && if (attacker.skills[269]?.and(255)?.let { it != 255 } == true) {
            true // ZMYJCL bypasses countRate entirely.
        } else {
            criticalHit(attacker, target)
        }
        val offsets = magic.effectOffsets + (0 to 0)
        // Keep object references even when a lethal magic result moves the
        // victim out of the active unit map before global settlement.
        val experienceTargets = (units.values + presentationUnits.values).associateBy { it.id }
        data class MagicEquipmentExperienceRecord(
            val recipient: BattleUnit,
            val opponent: BattleUnit,
            val kind: BattleEquipmentExperienceKind,
            val amount: Int,
        )
        val magicEquipmentByRecipient = linkedMapOf<Pair<String, BattleEquipmentExperienceKind>, MagicEquipmentExperienceRecord>()
        fun recordMagicEquipment(recipient: BattleUnit, opponent: BattleUnit, sourceHarm: Int, kind: BattleEquipmentExperienceKind) {
            val amount = equipmentExperienceAmount(recipient, opponent, sourceHarm, kind)
            val key = recipient.id to kind
            if (amount > (magicEquipmentByRecipient[key]?.amount ?: 0)) {
                magicEquipmentByRecipient[key] = MagicEquipmentExperienceRecord(recipient, opponent, kind, amount)
            }
        }
        val effectCandidates = units.values.filter { unit ->
            unit.visible && magicTerrainAllowed(magic, unit) &&
                (targetsAny || areAllied(unit, attacker) == targetsAllies) &&
                (unit.tileX - target.tileX to (unit.tileY - target.tileY)) in offsets
        }.toList()
        // filterMagicHitareaUnit promotes SB/BH to the original all-screen
        // target area.  QL (청룡) then chooses five targets from its effect
        // area with replacement, exactly as BattleLayer._magicAttack does.
        // `_magic` invokes `_magicProcess` twice for CLLJ.  Keep passes
        // separate: each gets its own playMeff group and only pass two has
        // ATTACK_FLAG.LIANJI's 90% count_magicHarm modifier.
        val repeatCount = if (attacker.skills[16]?.and(255)?.let { it != 255 } == true) 2 else 1
        val criticalSpeeches = mutableListOf<String?>()
        val localSettlements = mutableListOf<MagicLocalSettlement>()
        val resultPasses = buildList {
            repeat(repeatCount) { pass ->
                // Source runs checkCrit/getCritTxt before the preparation
                // action and before _magicAttack selects random QL targets.
                criticalSpeeches += resolveCriticalSpeech(attacker, magicCritical)
                val affectedUnits = when (magic.category) {
                    1, 29 -> units.values.filter { unit ->
                        unit.visible && (targetsAny || areAllied(unit, attacker) == targetsAllies)
                    }.toList()
                    26 -> if (effectCandidates.isEmpty()) emptyList() else List(5) {
                        effectCandidates[sourceDefaultRandom(0, effectCandidates.lastIndex)]
                    }
                    else -> effectCandidates
                }
                val localEntries = mutableListOf<MagicLocalSettlementEntry>()
                add(affectedUnits.map { victim ->
                val statusesBefore = victim.statuses.toMap()
                val liftsBefore = victim.attributeLifts.toMap()
                val liftRoundsBefore = victim.attributeLiftRounds.toMap()
            fun local(result: MagicTarget): MagicTarget {
                // Source only calls setCharInfoBykey(h, ..., STATES, P) on
                // the non-miss branch.  P may nevertheless be empty.
                if (result.hit) localEntries += MagicLocalSettlementEntry(
                    victim.id,
                    statusesBefore,
                    victim.statuses.toMap(),
                    liftsBefore,
                    victim.attributeLifts.toMap(),
                    hasStatesPayload = true,
                    attributeLiftRoundsBefore = liftRoundsBefore,
                    attributeLiftRoundsAfter = victim.attributeLiftRounds.toMap(),
                )
                return result
            }
            fun magicHarm(value: Int): Int {
                var result = if (pass > 0) kotlin.math.floor(value * .9).toInt() else value
                if (magicCritical) result += kotlin.math.floor(result * .5).toInt()
                return result
            }
            // BattleLayer._magicProcess handles these exceptional strategy
            // types before the generic damage/status calculation.
            if (magic.type == 22) { // HUIGUI: restore an already-acted unit
                victim.hasActed = false
                attacker.ai = 0
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = true, defeated = false))
            }
            if (magic.type == 25 && magic.category == 29) { // SISHEN / BH
                val healing = victim.maxHitPoints - victim.hitPoints
                victim.setCurHp(victim.maxHitPoints)
                victim.statuses.clear()
                victim.refStateAnime()
                return@map local(MagicTarget(victim.id, damage = 0, healing = healing, hitRate = 100, hit = true, defeated = false))
            }
            if (magic.type == 26 || magic.type == 28) { // BAQI / SHUAIQI
                val lift = if (magic.type == 26) 1 else -1
                val attributes = listOf(BattleAttribute.ATTACK, BattleAttribute.DEFENSE, BattleAttribute.SPIRIT, BattleAttribute.CRITICAL, BattleAttribute.MORALE)
                    .associateWith { attribute -> victim.applySourceAttributeLift(attribute, lift, 3) }
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = true, defeated = false, attributes = attributes))
            }
            if (magic.type == 27) { // QIANGXING
                val applied = victim.applySourceAttributeLift(BattleAttribute.MOVEMENT, 1, 3)
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = true, defeated = false, attribute = BattleAttribute.MOVEMENT, lift = applied))
            }
            if (magic.type == 6) { // XISHOU_MP
                val hitRate = magicHitRate(attacker, victim, magic)
                val hit = magicHit(attacker, victim, magic, hitRate)
                val base = maxOf(1, (effective(attacker, BattleAttribute.SPIRIT) - effective(victim, BattleAttribute.SPIRIT)) / 3 + 25 + attacker.level)
                val drained = if (hit) minOf(victim.magicPoints, maxOf(1, magicHarm(base * magic.power / 100))) else 0
                victim.addMpcur(-drained)
                val recovered = minOf(attacker.maxMagicPoints - attacker.magicPoints, drained)
                attacker.addMpcur(recovered)
                return@map local(MagicTarget(victim.id, damage = 0, magicRecovery = recovered, magicDrain = drained, hitRate = hitRate, hit = hit, defeated = false))
            }
            val status = magic.statusEffect()
            var appliedStatus: BattleStatus? = null
            if (status != null) {
                val hitRate = magicHitRate(attacker, victim, magic)
                val hit = magicHit(attacker, victim, magic, hitRate)
                if (hit) {
                    victim.statuses[status] = statusDuration(status, victim)
                    victim.refStateAnime()
                    appliedStatus = status
                }
                // Source _magicProcess does not stop after applying an
                // abnormal state.  A spell with harmType != NO performs a
                // second, independent accumulated hit check and then deals
                // damage as well (for example magic 33, 독연).
                if (magic.harmType == 4) {
                    return@map local(MagicTarget(victim.id, damage = 0, status = appliedStatus, hitRate = hitRate, hit = hit, defeated = false))
                }
            }
            val attributeChange = magic.attributeChange()
            if (magic.type == 21) { // JUEXING: remove only abnormal states, not stat lifts.
                val hadStatus = victim.statuses.isNotEmpty()
                victim.statuses.clear()
                victim.refStateAnime()
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = hadStatus, defeated = false))
            }
            if (magic.type == 7 || magic.type == 11) { // NLXJ / TSNL: martial ATT, civil SPR, all-rounder both.
                val lift = if (magic.type == 7) -1 else 1
                val attributes = when (victim.armType) {
                    1 -> mapOf(BattleAttribute.SPIRIT to lift)
                    2 -> mapOf(BattleAttribute.ATTACK to lift)
                    else -> mapOf(BattleAttribute.ATTACK to lift, BattleAttribute.SPIRIT to lift)
                }.mapValues { (attribute, value) -> victim.applySourceAttributeLift(attribute, value, 3) }
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = true, defeated = false, attributes = attributes))
            }
            if (attributeChange != null) {
                val (attribute, lift) = attributeChange
                val hitRate = magicHitRate(attacker, victim, magic)
                val hit = magicHit(attacker, victim, magic, hitRate)
                var appliedLift = 0
                if (hit) {
                    appliedLift = victim.applySourceAttributeLift(attribute, lift, 3)
                }
                return@map local(MagicTarget(
                    targetId = victim.id, damage = 0, hitRate = hitRate, hit = hit, defeated = false,
                    attribute = attribute.takeIf { hit }, lift = appliedLift,
                ))
            }
            if (magic.type == 19) {
                // BattleUnit.count_magicHarm(JHP): percentage of caster HP plus
                // strategist bonus (the original special-cases ids 39/41).
                // The percentage is the caster's current HP, not the target's.
                val base = attacker.hitPoints * magic.power / 100 + if (magic.id == 39 || magic.id == 41) attacker.spirit / 10 else attacker.spirit / 2
                val healingRate = healingTerrainRate(attacker, magic)
                val healing = minOf(victim.maxHitPoints - victim.hitPoints, maxOf(0, magicHarm(base * healingRate / 100)))
                victim.addHpcur(healing)
                return@map local(MagicTarget(victim.id, damage = 0, healing = healing, hitRate = 100, hit = true, defeated = false))
            }
            if (magic.type == 20 && magic.category == 24) { // MX: target HP → caster MP
                val transferred = minOf(40, maxOf(0, victim.hitPoints - 1))
                if (transferred > 0 && attacker.magicPoints < attacker.maxMagicPoints) {
                    victim.addHpcur(-transferred, keepAlive = true)
                    val recovered = minOf(attacker.maxMagicPoints - attacker.magicPoints, transferred * 5 / 8)
                    attacker.addMpcur(recovered)
                    return@map local(MagicTarget(victim.id, damage = transferred, magicRecovery = recovered, hitRate = 100, hit = true, defeated = false))
                }
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = false, defeated = false))
            }
            if (magic.type == 20) {
                // BattleUnit.count_magicHarm(JMP) returns the original spell's MP value.
                val healing = minOf(victim.maxMagicPoints - victim.magicPoints, magicHarm(magic.expendMp))
                victim.addMpcur(healing)
                return@map local(MagicTarget(victim.id, damage = 0, magicRecovery = healing, hitRate = 100, hit = true, defeated = false))
            }
            val hitRate = magicHitRate(attacker, victim, magic)
            val hit = magicHit(attacker, victim, magic, hitRate)
            val assassination = magic.type == 4 && magic.category == 2
            val base = if (assassination) {
                // AN_SHA under the YH category uses a percentage of target HP.
                victim.maxHitPoints * magic.power / 100
            } else {
                maxOf(1, (effective(attacker, BattleAttribute.SPIRIT) - effective(victim, BattleAttribute.SPIRIT)) / 3 + 25 + attacker.level)
            }
            val damage = if (hit) {
                if (assassination) maxOf(1, magicHarm(base))
                else {
                    var value = maxOf(1, base * magic.power / 100 * victim.magicHarmRate / 100)
                    value += magicFlatSkillDamage(attacker, magic)
                    value = maxOf(1, value * magicSkillDamageRate(attacker, victim, magic) / 100)
                    value = value * magicWeatherRate(magic) / 100
            value = value * offensiveMagicTerrainRate(victim, magic) / 100
                    // BattleUnit.isMine() is true for both MINE and FRIEND;
                    // count_magicHarm's minimum applies to ENEMY only.
                    val enemyMinimum = if (!attacker.isPlayerSide()) {
                        maxOf(1, (minOf(7, units.values.count { it.visible && it.isPlayerSide() }) * attacker.maxMagicPoints) / 100)
                    } else 1
                    magicHarm(maxOf(enemyMinimum, value))
                }
            } else 0
            victim.addHpcur(-damage)
            val casterHealing = if (magic.type == 5 && damage > 0) {
                minOf(attacker.maxHitPoints - attacker.hitPoints, damage).also { attacker.addHpcur(it) }
            } else 0
            val defeated = victim.hitPoints <= 0
            if (defeated) { removeUnit(victim.id); notifyUnitDefeated(attacker, victim) }
            local(MagicTarget(
                targetId = victim.id,
                damage = damage,
                healing = 0,
                hitRate = hitRate,
                hit = hit,
                defeated = defeated,
                casterHealing = casterHealing,
                status = appliedStatus,
            ))
                })
                localSettlements += MagicLocalSettlement(localEntries)
            }
        }
        val results = resultPasses.flatten()
        // `_magicProcess` writes EXP_ADD for every resolved target after its
        // harm/no-harm branch.  Support/status magic is therefore not exempt.
        // The write occurs before `_jiesuan`, so a magic kill still uses the
        // ordinary (non-defeat) reward and repeated targets max-merge.
        val reward = results.mapNotNull { result ->
            experienceTargets[result.targetId]?.let { victim ->
                battleExperience(attacker, victim, defeated = false)
            }
        }.maxOrNull()
        if (reward != null) notifyBattleExperience(attacker, reward)
        results.forEach { result ->
            val victim = experienceTargets[result.targetId] ?: return@forEach
            // `_magicProcess`'s U is the MP drain for XISHOU_MP and harm
            // otherwise. HJ_EXP_ADD is inside the harmType != NO branch,
            // while the caster's WQ_EXP_ADD is deliberately outside it.
            val sourceHarm = result.magicDrain.takeIf { it > 0 } ?: result.damage
            if (magic.harmType != 4) {
                recordMagicEquipment(victim, attacker, sourceHarm, BattleEquipmentExperienceKind.ARMOR)
            }
            if (attacker.armType != 2) {
                recordMagicEquipment(attacker, victim, sourceHarm, BattleEquipmentExperienceKind.WEAPON)
            }
        }
        magicEquipmentByRecipient.values.forEach { record ->
            notifyEquipmentExperienceAward(record.recipient, record.opponent, record.amount, record.kind)
        }
        return TacticalActionResult.Magic(
            magic.name, magic.expendMp, results, resultPasses,
            critical = magicCritical, criticalSpeeches = criticalSpeeches,
            localSettlements = localSettlements,
        )
    }

    /** Coordinate-target special magic.  SHUN_YI moves its caster to a vacant tile. */
    fun castMagicAt(attackerId: String, targetX: Int, targetY: Int, magicId: Int): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val attacker = units[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val magic = attacker.magic.firstOrNull { it.id == magicId } ?: return TacticalActionResult.Rejected("사용할 수 없는 전략입니다.")
        if (magic.type != 37) return TacticalActionResult.Rejected("좌표를 대상으로 할 수 없는 전략입니다.")
        if (!attacker.visible || attacker.effectiveFaction() != activeFaction || attacker.hasActed) return TacticalActionResult.Rejected("현재 유닛은 전략을 사용할 수 없습니다.")
        if (attacker.magicPoints < magic.expendMp) return TacticalActionResult.Rejected("MP가 부족합니다.")
        if (unitAt(targetX, targetY) != null || targetX < 0 || targetY < 0 || terrain?.let { targetX >= it.width || targetY >= it.height } == true) return TacticalActionResult.Rejected("이동할 수 없는 칸입니다.")
        val offset = targetX - attacker.tileX to targetY - attacker.tileY
        if (!magic.hitArea.allScreen && offset !in magic.hitArea.offsets) return TacticalActionResult.Rejected("전략 범위를 벗어났습니다.")
        attacker.addMpcur(-magic.expendMp)
        attacker.tileX = targetX
        attacker.tileY = targetY
        attacker.markActionComplete()
        return TacticalActionResult.Magic(magic.name, magic.expendMp, emptyList())
    }

    fun addUnit(unit: BattleUnit) {
        check(unit.id !in units) { "이미 존재하는 유닛: ${unit.id}" }
        initializeSourceRates(unit)
        units[unit.id] = unit
    }

    /** BattleLayer._truncUnitData seeds JQ_BDMZL through JQ_BBJL inclusively. */
    fun initializeSourceRates(unit: BattleUnit) {
        if (unit.rateAccumulators.isNotEmpty()) return
        (Rate.BDMZL..Rate.BBJL).forEach { index ->
            unit.rateAccumulators[index] = sourceRandom100()
        }
    }

    /** Initial scripted units pass through the same _truncUnitData seeding. */
    fun initializeAllSourceRates() = units.values.forEach(::initializeSourceRates)

    /** BattleUnit.setStateRound when an event explicitly supplies a status. */
    fun rollScriptedStatusRound(): Int = if (sourceRandomStreams != null) sourceDefaultRandom(1, 3) else 3

    /** Exact numeric key built by BattleLayer.s_AISortUnit. */
    private fun aiSortValue(unit: BattleUnit): Double {
        val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
        val resumeHp = terrainResumeRates[terrain?.terrainAt(unit.tileX, unit.tileY)] ?: 0
        var value = when {
            resumeHp > 0 && !wounded -> 110.0
            wounded -> 30.0
            else -> 0.0
        }
        if (BattleStatus.CONFUSION in unit.statuses) value -= 20.0
        if (BattleStatus.PARALYSIS in unit.statuses) value -= 10.0
        value += when (unit.armType) {
            2 -> (if (unit.remoteAttack) 25 else 10) + 100.0 * unit.hitPoints / unit.maxHitPoints.coerceAtLeast(1)
            0 -> 20 + 100.0 * unit.hitPoints / unit.maxHitPoints.coerceAtLeast(1)
            else -> 30 + 100.0 * (unit.maxHitPoints - unit.hitPoints) / unit.maxHitPoints.coerceAtLeast(1)
        }
        return value + 15 - effectiveMovement(unit)
    }

    /**
     * Cocos BattleConfg.AI 0..9 dispatch, without presentation delays.  The
     * scripted target id/coordinates are retained from BattleUnit.setAI.
     */
    @JvmOverloads
    fun resolveAiTurn(maxUnits: Int = Int.MAX_VALUE, deferMutations: Boolean = false): AiTurnResult {
        require(maxUnits > 0)
        if (outcome() != null) return AiTurnResult(0, 0, 0)
        check(!deferMutations || maxUnits == 1) { "deferred AI playback resolves exactly one _ai2 actor" }
        check(!deferMutations || deferredAiMutation == null) { "previous deferred AI actor has not completed" }
        val beforeResolution = if (deferMutations) runtimeSnapshot() else null
        if (deferMutations) {
            stagedAiHitSideEffects = mutableListOf()
            stagedAiCompletionSideEffects = mutableListOf()
        }
        lastAiUnitResolution = null
        var moves = 0
        var attacks = 0
        var holds = 0
        var resolvedUnits = 0
        var currentActor: BattleUnit? = null
        var currentFromX = 0
        var currentFromY = 0
        var currentHealthBefore: Map<String, Int> = emptyMap()
        var currentMoveArea: List<Pair<Int, Int>> = emptyList()
        fun record(unit: BattleUnit, targetId: String? = null, magicId: Int? = null, result: TacticalActionResult? = null) {
            val actionArea = when (result) {
                is TacticalActionResult.Attack -> if (unit.attackAllScreen) {
                    terrain?.let { grid -> (0 until grid.width).flatMap { x -> (0 until grid.height).map { y -> x to y } } }.orEmpty()
                } else (unit.attackOffsets + unit.attackEffectOffsets).map { (dx, dy) -> unit.tileX + dx to unit.tileY + dy }
                is TacticalActionResult.Magic -> unit.magic.firstOrNull { it.id == magicId }
                    ?.hitArea?.offsets?.map { (dx, dy) -> unit.tileX + dx to unit.tileY + dy }.orEmpty()
                else -> emptyList()
            }
            lastAiUnitResolution = AiUnitResolution(
                actorId = unit.id,
                fromX = currentFromX,
                fromY = currentFromY,
                toX = unit.tileX,
                toY = unit.tileY,
                path = lastMovePath(unit.id).takeIf { unit.tileX != currentFromX || unit.tileY != currentFromY }.orEmpty(),
                targetId = targetId,
                magicId = magicId,
                result = result,
                healthBeforeAction = currentHealthBefore,
                moveArea = currentMoveArea,
                actionArea = actionArea,
            )
            resolvedUnits++
        }
        fun hold(unit: BattleUnit) {
            // BattleLayer._ai2 puts UNIT_STATUS2.XD in g_charinfo before
            // ControlManager selects a point.  A controller that only moves
            // or finds no action therefore still consumes this unit's turn.
            unit.markActionComplete()
            holds++
            check(currentActor === unit)
            record(unit)
        }
        // The first sort is already in flight when state settlement begins.
        // Every later `_ai2` iteration recomputes s_AISortUnit from live HP,
        // terrain and status values after the previous actor finishes.
        var firstPlannedId = aiTurnOrder?.firstOrNull()
        aiTurnOrder = null
        var tracedAiSort = false
        while (resolvedUnits < maxUnits) {
            // `ctrl_mine` tests isEnd after every completed unit callback.
            // A batch resolver must not let the rest of the camp move after
            // the preceding actor has produced a terminal roster.
            if (outcome() != null) break
            val remaining = units.values.asSequence()
                .filter { it.visible && it.effectiveFaction() == activeFaction && !it.hasActed }
                .sortedWith(compareByDescending<BattleUnit>(::aiSortValue).thenBy { effective(it, BattleAttribute.DEFENSE) })
                .toList()
            if (!tracedAiSort && round == 2 && activeFaction == Faction.ENEMY) {
                traceActions += "sort-r2-enemy:" + remaining.joinToString(";") {
                    "${it.sourceCharacterId}=v${aiSortValue(it)},hp${it.hitPoints}/${it.maxHitPoints},arm${it.armType},remote${it.remoteAttack},mov${effectiveMovement(it)},def${effective(it, BattleAttribute.DEFENSE)},terrain${terrain?.terrainAt(it.tileX, it.tileY)},resume${terrainResumeRates[terrain?.terrainAt(it.tileX, it.tileY)] ?: 0},status${it.statuses}"
                }
                tracedAiSort = true
            }
            val unit = firstPlannedId?.let(units::get)
                ?.takeIf { it.visible && it.effectiveFaction() == activeFaction && !it.hasActed }
                ?: remaining.firstOrNull()
                ?: break
            firstPlannedId = null
            currentActor = unit
            currentFromX = unit.tileX
            currentFromY = unit.tileY
            currentHealthBefore = units.mapValues { it.value.hitPoints }
            currentMoveArea = emptyList()
            // CtrlJSYD (AI 2) still constructs ControlManager and evaluates
            // attacks/magic from the current tile; it only suppresses movement.
            // BattleLayer._ai2 exits before `_process` for HUN_LUAN
            // (CONFUSION) only.  Paralysis deliberately enters Control,
            // whose _process1 then changes its temporary controller to
            // JIAN_SHOU_YUAN_DI.  Keeping those two source stages distinct
            // matters because _ai2 has already written XD at this point.
            if (BattleStatus.CONFUSION in unit.statuses) {
                hold(unit)
                continue
            }
            // CtrlGJWJ/CtrlGSWJ resolve their retained target through
            // Battle.unit(index, 1), not through the enemy-only list.  A
            // missing target re-enters active AI; a close friendly follow
            // target re-enters passive AI.  The old port silently treated
            // both as an arbitrary enemy target.
            val retainedTarget = units.values.firstOrNull {
                it.visible && it.sourceCharacterId == unit.aiTargetCharacterId
            }
            when (unit.ai) {
                3 -> when {
                    retainedTarget == null -> unit.ai = 1 // CtrlGJWJ
                    areAllied(unit, retainedTarget) && distance(unit, retainedTarget) < 3 -> unit.ai = 0
                    !areAllied(unit, retainedTarget) && !hasAttackCandidate(unit, retainedTarget) -> {
                        // CtrlGJWJ._ganlu(2) enters CtrlYDDZDDGJ, whose
                        // _psAryIter yields only the selected route point.
                        unit.ai = 9
                        unit.aiTargetX = retainedTarget.tileX
                        unit.aiTargetY = retainedTarget.tileY
                    }
                }
                5 -> when {
                    retainedTarget == null -> unit.ai = 1 // CtrlGSWJ
                    distance(unit, retainedTarget) < 3 -> unit.ai = 0
                    else -> {
                        // CtrlGSWJ._ganlu(0) enters CtrlYDDZDDJS.
                        unit.ai = 7
                        unit.aiTargetX = retainedTarget.tileX
                        unit.aiTargetY = retainedTarget.tileY
                    }
                }
            }
            val opponents = units.values.filter { it.visible && !areAllied(it, unit) }
            val targetById = retainedTarget?.takeIf { !areAllied(it, unit) }
            val nearestOpponent = opponents.minByOrNull { distance(unit, it) }
            // BattleLayer._ai scores every point in _process(...).psAry,
            // including the current tile, then keeps the best action nested
            // under that point.  This replaces the former nearest-enemy
            // shortcut so terrain, designated targets and reachable attacks
            // participate in the same decision.
            // Control.selectMovePoint is the source entry point.  Its
            // _process1 short-circuits paralysis/complete surrounding before
            // Control._AIProcess scores a single reachable point.
            var selectedByControl: AiDecision? = null
            lateinit var controlManager: ControlManager
            controlManager = ControlManager(
                state = object : ControlManager.UnitState {
                    override fun isControlled() = false
                    override fun ai() = unit.ai
                    override fun targetIndex() = unit.aiTargetCharacterId
                    override fun targetX() = unit.aiTargetX
                    override fun targetY() = unit.aiTargetY
                    // ControlManager.js checks battle.unit(targetId) only;
                    // CtrlGSWJ deliberately follows same-camp targets too.
                    override fun targetExists(index: Int) = units.values.any { it.visible && it.sourceCharacterId == index }
                },
                factory = object : ControlManager.Factory {
                    override fun create(ai: Int): ControlManager.Driver = object : ControlManager.Driver {
                        // `ai` is the manager's current (possibly temporary)
                        // controller.  It is intentionally distinct from
                        // unit.ai, which ControlManager.js does not rewrite.
                        private val controllerAi = ai
                        private val controller = ControlControllerFactory.create(ai)
                        private var data = ControlData()
                        override fun setManager(manager: ControlManager) = Unit
                        override fun setWithData(targetIndex: Int, x: Int, y: Int) {
                            data = ControlData(targetIndex, Control.Point(x, y))
                        }
                        override fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int {
                            // Control's _AStar/_zdmdd/findEmptyPos consume the
                            // psHash captured for this exact _process call.
                            // Rebuilding it can observe a later unit state and
                            // make a just-revealed AI6 actor fall through to
                            // an otherwise spurious hold.
                            val capturedMovePoints = pointHash.mapTo(linkedSetOf()) { it.x to it.y }
                            val port = object : ControlControllerPort {
                                override fun currentPoint() = Control.Point(unit.tileX, unit.tileY)
                                override fun isParalyzed() = BattleStatus.PARALYSIS in unit.statuses
                                override fun isSurrounded() = movementOffsets.all { (dx, dy) -> unitAt(unit.tileX + dx, unit.tileY + dy) != null }
                                override fun isMine() = unit.isPlayerSide()
                                override fun setPersistentAi(ai: Int) { unit.ai = ai }
                                override fun target(index: Int) = units.values.firstOrNull { it.visible && it.sourceCharacterId == index }?.let {
                                    ControlTarget(index, Control.Point(it.tileX, it.tileY), it.isPlayerSide(), distance(unit, it))
                                }
                                override fun hasAttackTargets(targetIndex: Int?): Boolean {
                                    val candidates = if (targetIndex == null) opponents else opponents.filter { it.sourceCharacterId == targetIndex }
                                    return linkedSetOf(unit.tileX to unit.tileY).apply { addAll(reachableTiles(unit.id).keys) }
                                        .any { (x, y) -> candidates.any { canAttackFrom(unit, x, y, it) } }
                                }
                                // Direct `_cxpl` adapter.  Like the source,
                                // this returns a temporary ControlManager
                                // transition; it never writes unit.ai.
                                override fun exhaustedRetreat(): ControlTransition? {
                                    val weakThreshold = unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
                                    if (unit.hitPoints >= weakThreshold) return null
                                    val resume = points.asSequence()
                                        .filter { point -> terrainResumeRates[terrain?.terrainAt(point.x, point.y)] ?: 0 > 0 }
                                        .filter { point -> unitAt(point.x, point.y)?.let { it.id == unit.id } != false }
                                        .maxByOrNull { point -> terrainResumeRates[terrain?.terrainAt(point.x, point.y)] ?: 0 }
                                    if (resume != null) return ControlTransition(ControlAi.MOVE_MAGIC, ControlData(-1, resume))
                                    val master = enemyMasterUnitId?.let(units::get)
                                        ?.takeIf { !unit.isPlayerSide() && it.visible && it.id != unit.id }
                                    val friend = units.values.asSequence()
                                        .filter { it.visible && it.id != unit.id && areAllied(it, unit) }
                                        .minByOrNull { distance(unit, it) }
                                    return (master ?: friend)?.let { target ->
                                        ControlTransition(ControlAi.RETREAT_TO, ControlData(-1, Control.Point(target.tileX, target.tileY)))
                                    }
                                }
                                override fun nearestOpponent() = opponents.mapNotNull { opponent ->
                                    sourceAStar(unit, opponent.tileX, opponent.tileY, 0)?.let { path -> opponent to path.size }
                                }.minByOrNull { it.second }?.first?.let {
                                    ControlTarget(it.sourceCharacterId ?: -1, Control.Point(it.tileX, it.tileY), it.isPlayerSide(), distance(unit, it))
                                }
                                override fun winRectCentre(): Control.Point? = null
                                // Control._zdmdd does not choose the reachable
                                // point nearest a remote target. It is entered
                                // only when the authored target itself exists in
                                // psHash; otherwise CtrlDZDD/CtrlTZZDD must fall
                                // through to _ganlu's AStar(flags=9) route. If
                                // the target is occupied, source probes
                                // MO_YU_JIAN3 in its authored order.
                                override fun destinationPoint(target: Control.Point): Control.Point? {
                                    val targetPoint = target.x to target.y
                                    if (targetPoint !in capturedMovePoints) return null
                                    return sequenceOf(targetPoint)
                                        .plus(directDestinationOffsets.asSequence().map { (dx, dy) ->
                                            target.x + dx to target.y + dy
                                        })
                                        .firstOrNull { point ->
                                            point in capturedMovePoints && unitAt(point.first, point.second) == null
                                        }
                                        ?.let { Control.Point(it.first, it.second) }
                                }
                                override fun nearPoint(target: Control.Point): Control.Point? {
                                    val route = sourceAStar(unit, target.x, target.y, 9) ?: return null
                                    val lastReachableIndex = route.indexOfFirst { it !in capturedMovePoints }
                                        .let { if (it < 0) route.lastIndex else it - 1 }
                                    for (index in lastReachableIndex downTo 1) {
                                        sourceFindEmptyPosition(unit, route[index], capturedMovePoints)?.let { point ->
                                            return Control.Point(point.first, point.second)
                                        }
                                    }
                                    return Control.Point(unit.tileX, unit.tileY)
                                }
                                override fun blockingEnemy(target: Control.Point): Int? {
                                    // Control._ganlu falls back from AStar(9)
                                    // to AStar(5). Bit 4 keeps opposing units
                                    // in the route with a +255 cost, then the
                                    // first such unit becomes CtrlGJWJ's
                                    // designated target.
                                    val route = sourceAStar(unit, target.x, target.y, 5) ?: return null
                                    return route.asSequence()
                                        .mapNotNull { point -> unitAt(point.first, point.second) }
                                        .firstOrNull { occupant -> !areAllied(occupant, unit) }
                                        ?.sourceCharacterId
                                }
                                override fun chooseAi(mode: Int): Control.Result? {
                                    val controllerPoints = when (controllerAi) {
                                        ControlAi.HOLD -> listOf(Control.Point(unit.tileX, unit.tileY))
                                        ControlAi.MOVE_ATTACK, ControlAi.MOVE_MAGIC, ControlAi.MOVE_ATTACK_UNIT -> listOf(data.target)
                                        else -> points
                                    }
                                    selectedByControl = chooseAiDecision(
                                        unit = unit,
                                        opponents = opponents,
                                        designated = targetById,
                                        aiMode = controllerAi,
                                        aiFlags = mode,
                                        forcedTarget = data.target,
                                        candidatePoints = controllerPoints.map { it.x to it.y },
                                    )
                                    return selectedByControl?.let { choice -> Control.Result(choice.x, choice.y, kind = if (choice.magicId == null) "attack" else "magic", value = choice.value) }
                                }
                            }
                            val step = controller.step(port, data)
                            step.transition?.let { transition ->
                                // ControlManager.setControl replaces only its
                                // live controller/data.  It must not rewrite
                                // the unit's persistent AI fields: the source
                                // writes those only through Ctrl*.setAI().
                                controlManager.setControl(transition.ai, transition.data.targetIndex, transition.data.target.x, transition.data.target.y)
                            }
                            step.result?.let(controlManager::setResult)
                            return step.status
                        }
                    }
                },
            )
            // `_process(unit)` passes canMovePoints' ordered psAry and psHash
            // to ControlManager.  Supplying an empty synthetic set here made
            // every controller score a reconstructed approximation instead.
            // BattleLayer.canMovePoints starts from BattleUnit.mov_final(),
            // including the active windy/heavy-rain penalty. Using the raw
            // lifted MOV here admitted an extra zero-remaining ring into AI
            // scoring (for example S_00 unit 258 could incorrectly use
            // (11,11) in round 3).
            val moveArea = movePoints(unit, finalMovement(unit))
            val sourcePoints = moveArea.points.keys.map { (x, y) -> Control.Point(x, y) }
            currentMoveArea = sourcePoints.map { it.x to it.y }
            val sourceHash = sourcePoints.toCollection(linkedSetOf())
            val controlStatus = controlManager.selectMovePoint(sourcePoints, sourceHash)
            if (controlStatus != 0) {
                hold(unit)
                continue
            }
            val decision = selectedByControl
            if (decision == null) {
                hold(unit)
                continue
            }
            // Base Control._AIProcess4 is empty. Only CtrlZDCJ (AI 1) and
            // CtrlJSYD (AI 2) override it to persist `info.value`; passive
            // and temporary movement controllers must leave AIValue at the
            // camp-start value of zero. Use ControlManager's final controller
            // rather than unit.ai because a retry may have replaced it.
            if (controlManager.activeAi in setOf(ControlAi.ACTIVE, ControlAi.HOLD)) {
                unit.aiValue = decision.actionValue
            }
            val traceFrom = "${unit.tileX},${unit.tileY}"
            val diagnosticPoints = if (unit.sourceCharacterId == 474 && round == 1) sourcePoints.joinToString(";") { "${it.x},${it.y}" } else ""
            traceActions += "r$round/${activeFaction.name}/${unit.sourceCharacterId}:$traceFrom->${decision.x},${decision.y}:target=${decision.targetId?.let(units::get)?.sourceCharacterId}:magic=${decision.magicId}:score=${decision.actionValue}:points=$diagnosticPoints"
            if (decision.x != unit.tileX || decision.y != unit.tileY) {
                if (moveUnit(unit.id, decision.x, decision.y) is TacticalActionResult.Success) moves++ else {
                    hold(unit)
                    continue
                }
            }
            // CtrlDZDD/CtrlTZZDD keep the authored persistent AI after a
            // movement reaches its destination.  The source writes passive
            // only when that controller is entered again on a later turn and
            // observes that the actor already starts on the target tile.
            val selected = decision.targetId?.let(units::get)
            if (selected != null && decision.magicId != null) {
                val profile = unit.magic.firstOrNull { it.id == decision.magicId }
                val bypassCondition = profile?.aiUse == 13
                val magicResult = castMagic(unit.id, selected.id, decision.magicId, bypassCondition = bypassCondition)
                if (unit.sourceCharacterId == 146 && round == 2) {
                    val profileText = profile?.let { "id=${it.id},type=${it.type},target=${it.target},area=${it.effectAreaId},power=${it.power},harm=${it.harmType},category=${it.category},limit=${it.hitRateLimit}" }
                    traceActions += "diagMagic146:profile=$profileText:targetArm=${selected.armId},magicHarm=${selected.magicHarmRate}:result=$magicResult"
                }
                if (magicResult is TacticalActionResult.Magic) {
                    attacks++
                    record(unit, selected.id, decision.magicId, magicResult)
                } else hold(unit)
            } else if (selected != null && selected.visible && canAttack(unit, selected)) {
                val attackResult = attack(unit.id, selected.id)
                if ((unit.sourceCharacterId in setOf(0, 32, 258, 259, 477, 479) && round == 3) ||
                    (unit.sourceCharacterId == 3 && round == 4)
                ) traceActions += "diagAttack${unit.sourceCharacterId}r$round:offsets=${unit.attackOffsets}:statuses=${unit.statuses}:result=$attackResult"
                if (attackResult is TacticalActionResult.Attack) {
                    attacks++
                    record(unit, selected.id, result = attackResult)
                } else hold(unit)
            } else hold(unit)
        }
        if (deferMutations && lastAiUnitResolution != null) {
            val afterResolution = runtimeSnapshot()
            val before = requireNotNull(beforeResolution)
            val hitSideEffects = stagedAiHitSideEffects.orEmpty().toList()
            val completionSideEffects = stagedAiCompletionSideEffects.orEmpty().toList()
            stagedAiHitSideEffects = null
            stagedAiCompletionSideEffects = null
            restoreRuntime(before)
            deferredAiMutation = DeferredAiMutation(
                lastAiUnitResolution!!.actorId, before, afterResolution, hitSideEffects, completionSideEffects,
            )
        } else if (deferMutations) {
            stagedAiHitSideEffects = null
            stagedAiCompletionSideEffects = null
        }
        return AiTurnResult(moves, attacks, holds)
    }

    private data class AiDecision(
        val x: Int,
        val y: Int,
        val targetId: String?,
        val magicId: Int?,
        val value: Int,
        /** Control._AIProcess stores info.value, not terrain-inclusive value. */
        val actionValue: Int = 0,
    )

    private companion object {
        /** Config.ENABLED_FEATURE.ZJHH. */
        const val ENABLED_FEATURE_ZJHH = 8
        /** Config.ENABLED_FEATURE.ZDBHSW. */
        const val ENABLED_FEATURE_ZDBHSW = 32
    }

    /** Source BattleLayer._ai's point/action maximization for physical actions. */
    private fun chooseAiDecision(
        unit: BattleUnit,
        opponents: List<BattleUnit>,
        designated: BattleUnit?,
        aiMode: Int = unit.ai,
        /** Control._AIProcess(t), notably CtrlYDDZDDBM's t=2. */
        aiFlags: Int = 0,
        forcedTarget: Control.Point = Control.Point(unit.aiTargetX, unit.aiTargetY),
        candidatePoints: Collection<Pair<Int, Int>>? = null,
    ): AiDecision? {
        // BattleLayer._process supplies controller-specific psAry. Passive
        // and hold controllers receive only their current point; destination
        // controllers receive the reachable point closest to the script's
        // target. Active controllers retain the complete reachable set.
        val reachable = reachableTiles(unit.id).keys
        val points = candidatePoints?.toCollection(linkedSetOf()) ?: when (aiMode) {
            // CtrlBDCJ._selectMovePoint2 first calls _aiHaveAttackTargets(),
            // which scans every psAry movement point.  It stops the turn
            // only when no one of those points can attack an opponent.
            0 -> linkedSetOf(unit.tileX to unit.tileY).apply {
                val allPoints = linkedSetOf(unit.tileX to unit.tileY).apply { addAll(reachable) }
                val hasAttackTarget = allPoints.any { (x, y) ->
                    opponents.any { target -> canAttackFrom(unit, x, y, target) }
                }
                if (hasAttackTarget) addAll(reachable)
            }
            // CtrlJSYD._psAryIter yields its current point only.
            2 -> linkedSetOf(unit.tileX to unit.tileY)
            4, 6 -> {
                val destination = reachable.minByOrNull { (x, y) ->
                    kotlin.math.abs(x - unit.aiTargetX) + kotlin.math.abs(y - unit.aiTargetY)
                }
                // CtrlDZDD/CtrlTZZDD call _zdmdd then _ganlu.  Both methods
                // replace the controller with a forced-destination control;
                // they do not submit the current tile to BattleLayer._ai for
                // a terrain-score comparison.
                destination?.let { linkedSetOf(it) } ?: linkedSetOf(unit.tileX to unit.tileY)
            }
            // CtrlYDDZDDJS/CtrlYDDZDDBM/CtrlYDDZDDGJ override
            // _psAryIter and submit only their forced destination point.
            7, 8, 9 -> reachable.minByOrNull { (x, y) ->
                kotlin.math.abs(x - unit.aiTargetX) + kotlin.math.abs(y - unit.aiTargetY)
            }?.let { destination -> linkedSetOf(destination) } ?: linkedSetOf(unit.tileX to unit.tileY)
            else -> linkedSetOf(unit.tileX to unit.tileY).apply { addAll(reachable) }
        }
        var best: AiDecision? = null
        val diagnosticScores = mutableListOf<String>()
        val originalX = unit.tileX
        val originalY = unit.tileY
        // Control._AIProcess clears flag 2 when the actor owns WFJGJ.
        val effectiveAiFlags = if (aiFlags and 2 != 0 && unit.skills[226]?.and(255)?.let { it != 255 } == true) aiFlags and 2.inv() else aiFlags
        points.forEach { (x, y) ->
            // Control._AIProcess calls searchUnitByPos(s,l,0).  psAry may
            // contain a friendly-occupied routing node, but only the acting
            // unit itself is evaluated from such a position.
            unitAt(x, y)?.takeIf { it !== unit }?.let { return@forEach }
            unit.tileX = x
            unit.tileY = y
            // Control._AIProcess: floor(i.terrainImpact() / 5), rather than
            // the raw 100-based terrain percentage.
            var value = (unit.terrainImpacts[terrain?.terrainAt(x, y)] ?: 100) / 5
            // `_AIProcess` adds cover pressure only for a civil officer,
            // ranged arm, or wounded actor, then adds the terrain's
            // RESUMEHP value only for the wounded case.  Controller modes
            // do not receive an implicit destination/target bonus here.
            val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
            if (unit.armType == 1 || unit.remoteAttack || wounded) {
                units.values.filter { it.visible && it != unit }.forEach { other ->
                    // Control calls BattleUnit.distance(other, 1) here. In
                    // the source, flag bit 1 retains only diagonal adjacency.
                    val d = ControlScoring.coverDistance(
                        unit.tileX, unit.tileY, other.tileX, other.tileY,
                    )
                    if (d in 1..4) {
                        value += ControlScoring.coverPressure(d, areAllied(unit, other))
                    }
                }
            }
            if (wounded) value += terrainResumeRates[terrain?.terrainAt(x, y)] ?: 0
            // BattleLayer.filterHitAreaUnit walks the authored hit-area `ps`
            // array, not BattleState's unit insertion order.  Equal scores
            // retain the first target in that offset order (straight tiles
            // precede diagonals for infantry).
            val physicalTargets = if (unit.attackAllScreen) opponents else unit.attackOffsets
                .mapNotNull { (dx, dy) -> unitAt(unit.tileX + dx, unit.tileY + dy) }
                .distinct()
                .filter { candidate -> candidate in opponents }
            if ((unit.sourceCharacterId == 474 && round == 1 && (x to y) in setOf(8 to 17, 9 to 17)) ||
                (unit.sourceCharacterId in setOf(258, 259) && round in 2..3 && physicalTargets.isNotEmpty())) {
                diagnosticScores += "$x,$y=" + physicalTargets.joinToString("|") {
                    "${it.sourceCharacterId}:${estimatedAttackValue(unit, it)}:hp=${it.hitPoints}/${it.maxHitPoints}:harm=${sourceAttackHarm(unit, it, false)}:rate=${physicalHitRate(unit, it)}"
                }
            }
            val scoredPhysicalTargets = physicalTargets.filter { candidate ->
                canAttack(unit, candidate) &&
                    // `_AIProcess(2)` skips a physical target that can
                    // already attack the actor; CtrlYDDZDDBM uses this when
                    // travelling to a magic destination.
                    (effectiveAiFlags and 2 == 0 || !canAttack(candidate, unit))
            }.mapNotNull { target ->
                val rawValue = estimatedAttackValue(unit, target)
                // Control._AIProcess rejects `_countAttackValue` below one
                // before `_AIProcess2` adds the controller's GJZDWJ bonus.
                // Applying the bonus first incorrectly revives attacks whose
                // counterattack makes their original score non-positive.
                if (rawValue < 1) null else target to rawValue
            }
            val scoredTarget = scoredPhysicalTargets.maxByOrNull { (target, rawValue) ->
                // Control._AIProcess overwrites its temporary FZGJ/distance
                // value with _countAttackValue before comparison.
                rawValue + if (
                    aiMode in setOf(ControlAi.ATTACK_UNIT, ControlAi.MOVE_ATTACK_UNIT) && target === designated
                ) 110 else 0
            }
            val target = scoredTarget?.first
            val physicalValue = scoredTarget?.second ?: Int.MIN_VALUE
            // CtrlGJWJ and CtrlYDDZDDGJ override `_AIProcess2`: the retained
            // target receives Config.AI_VALUE.GJZDWJ (110) after its normal
            // attack score.  No other controller receives this bonus.
            val designatedBonus = if (aiMode in setOf(ControlAi.ATTACK_UNIT, ControlAi.MOVE_ATTACK_UNIT) && target === designated) 110 else 0
            val scoredPhysicalValue = if (physicalValue == Int.MIN_VALUE) physicalValue else physicalValue + designatedBonus
            val magic = bestAiMagic(unit, opponents, designated, aiMode)
            val useMagic = magic != null && magic.third > scoredPhysicalValue
            val actionValue = if (useMagic) magic!!.third else scoredPhysicalValue
            if (actionValue != Int.MIN_VALUE) value += actionValue + 30
            val candidate = AiDecision(
                x, y,
                if (useMagic) magic!!.first.id else target?.id,
                if (useMagic) magic!!.second.id else null,
                value,
                actionValue.takeIf { it != Int.MIN_VALUE } ?: 0,
            )
            if (best == null || candidate.value > best!!.value) best = candidate
        }
        unit.tileX = originalX
        unit.tileY = originalY
        if (diagnosticScores.isNotEmpty()) {
            val friend234 = units.values.firstOrNull { it.sourceCharacterId == 234 }
            traceActions += "diag${unit.sourceCharacterId}:u234=${friend234?.tileX},${friend234?.tileY},v=${friend234?.visible},acted=${friend234?.hasActed}:arm=${unit.armType},remote=${unit.remoteAttack}:offsets=${unit.attackOffsets.joinToString("|") { "${it.first},${it.second}" }}:skills=${unit.skills.keys.joinToString("|")}:${diagnosticScores.joinToString(";")}"
        }
        return best
    }

    /**
     * Captures the actual AI scorer for one source character without running
     * a turn, moving a unit, or injecting an expected choice.  Cocos
     * BattleLayer._ai accepts an explicit point array; the source evidence
     * for R_00 unit 474 supplied only its current point, so callers pass the
     * same constrained candidate set here.
     */
    fun traceAiPlannerAtCurrentPoint(sourceCharacterId: Int, aiFlags: Int = 1): AiPlannerTrace? {
        val unit = units.values.firstOrNull { it.visible && it.sourceCharacterId == sourceCharacterId } ?: return null
        // This is the direct BattleLayer._ai path, not Control._AIProcess:
        // direct _ai adds raw terrainImpact (100-based) and flag bit 1 skips
        // all physical/magic action scoring.  The ordinary turn runner still
        // calls chooseAiDecision through ControlManager for _AIProcess.
        val value = cocosAiBaseValueAt(unit, unit.tileX, unit.tileY)
        return AiPlannerTrace(
            sourceCharacterId = sourceCharacterId,
            ai = unit.ai,
            x = unit.tileX,
            y = unit.tileY,
            value = value,
            actionValue = null,
            targetId = null,
            magicId = null,
        )
    }

    /** Direct Cocos BattleLayer._ai's `C += t.terrainImpact()` base score. */
    private fun cocosAiBaseValueAt(unit: BattleUnit, x: Int, y: Int): Int {
        var value = unit.terrainImpacts[terrain?.terrainAt(x, y)] ?: 100
        val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
        if (unit.armType == 1 || unit.remoteAttack || wounded) {
            val originalX = unit.tileX
            val originalY = unit.tileY
            unit.tileX = x
            unit.tileY = y
            units.values.filter { it.visible && it !== unit }.forEach { other ->
                val d = ControlScoring.coverDistance(
                    unit.tileX, unit.tileY, other.tileX, other.tileY,
                )
                value += ControlScoring.coverPressure(d, areAllied(unit, other))
            }
            unit.tileX = originalX
            unit.tileY = originalY
        }
        if (wounded) value += terrainResumeRates[terrain?.terrainAt(x, y)] ?: 0
        return value
    }

    /** Source Control._AIProcess magic branch, evaluated after moving the actor to each candidate tile. */
    private fun bestAiMagic(
        attacker: BattleUnit,
        opponents: List<BattleUnit>,
        designated: BattleUnit?,
        aiMode: Int = attacker.ai,
    ): Triple<BattleUnit, OriginalGameData.MagicProfile, Int>? {
        // `_AIProcess` owns one cache for all candidates at this point.
        val scoreCache = linkedMapOf<String, Int>()
        val candidates = attacker.magic.asSequence()
        // Control._AIProcess enters the strategy scorer only when
        // AIIsUse()!=13 and magicConditionTest returns zero.  Value 13 marks
        // a player-only strategy; it does not bypass the condition gate.
        .filter { it.aiUse != 13 && magicConditionReason(attacker, it) == null }
        .filter { attacker.magicPoints >= it.expendMp }
        .flatMap { magic ->
            val targets = when (magic.target) {
                1 -> units.values.filter { it.visible && areAllied(it, attacker) }
                2 -> listOf(attacker)
                3 -> units.values.filter { it.visible }
                else -> opponents
            }
            targets.asSequence()
                .filter { target ->
                    magic.category in setOf(1, 29) || magic.hitArea.allScreen ||
                        (target.tileX - attacker.tileX to target.tileY - attacker.tileY) in magic.hitArea.offsets
                }
                .map { target ->
                    var score = estimatedMagicValue(attacker, target, magic, scoreCache)
                    // Control skips the entire candidate, including effect
                    // tiles, when the selected primary has no positive value.
                    if (score >= 1) {
                        if (!areAllied(attacker, target)) score += distance(attacker, target)
                        // Control._AIProcess subtracts `et` for every candidate
                        // before effect-area additions: floor(expendMp *
                        // AI_VALUE.HP_MP_RATE / unit.mp()).
                        score -= magic.expendMp * 100 / attacker.maxMagicPoints.coerceAtLeast(1)
                        if (aiMode in setOf(ControlAi.ATTACK_UNIT, ControlAi.MOVE_ATTACK_UNIT) && target === designated) score += 110
                        magic.effectOffsets.mapNotNull { (dx, dy) -> unitAt(target.tileX + dx, target.tileY + dy) }
                            .filter { affected ->
                                affected !== target && affected.visible && when (magic.target) {
                                    0 -> !areAllied(affected, attacker) // MAGIC_TARGET.ENEMY
                                    1 -> areAllied(affected, attacker)  // MAGIC_TARGET.MINE
                                    else -> true
                                }
                            }
                            .forEach { affected -> score += estimatedMagicValue(attacker, affected, magic, scoreCache) }
                    }
                    Triple(target, magic, score)
                }
        }
        .filter { it.third > 0 }
        .toList()
        val diagnosticMagicActor = when {
            attacker.sourceCharacterId == 147 && round == 6 -> "147r6"
            attacker.sourceCharacterId == 22 && round == 4 -> "22r4"
            else -> null
        }
        if (diagnosticMagicActor != null &&
            traceActions.none { it.startsWith("diagMagicScores$diagnosticMagicActor:") }) {
            traceActions += "diagMagicScores$diagnosticMagicActor:" + candidates.joinToString(";") { (target, magic, score) ->
                "m${magic.id}/t${target.sourceCharacterId}/s$score/c${magic.category}/h${magic.harmType}/p${magic.power}/mp${magic.expendMp}/ai${magic.aiUse}"
            }
        }
        return candidates.maxByOrNull { it.third }
    }

    /** Injectable `Control._countAttackValue` preview for one primary target. */
    fun previewAiAttackValue(attackerId: String, targetId: String): Int {
        val attacker = units[attackerId] ?: return 0
        val target = units[targetId] ?: return 0
        return estimatedAttackValue(attacker, target)
    }

    /**
     * Read-only ordinary physical-harm preview for input planning.  This is
     * the source `countBaseHarm` value before hit, critical, and the
     * move-dependent attack effects are rolled, so asking for it cannot
     * consume a skill temp or advance either unit's combat state.
     */
    fun previewPhysicalDamage(attackerId: String, targetId: String): Int {
        val attacker = units[attackerId] ?: return 0
        val target = units[targetId] ?: return 0
        return sourceAttackHarm(attacker, target, splash = false)
    }

    /**
     * Live adapter for Control._countAttackValue.  Keeping the score engine
     * separate is useful for exhaustive injection tests, but production AI
     * must feed it the same countAtkHarm2-shaped records as the source.
     */
    private fun estimatedAttackValue(attacker: BattleUnit, target: BattleUnit): Int {
        return ControlScoring.attackValue(
            AiScoringUnit(attacker),
            AiScoringUnit(target),
            counter = true,
        )
    }

    /**
     * One countAtkHarm2 preview record.  This is intentionally only
     * BattleUnit.countBaseHarm: the AI preview does not apply arm restraint,
     * attack bonuses, critical multipliers, resistance, or current-HP caps
     * used by the subsequently played attack.
     */
    private fun sourceAttackHarm(attacker: BattleUnit, target: BattleUnit, splash: Boolean): Int {
        val attackTerrain = attacker.terrainImpacts[terrain?.terrainAt(attacker.tileX, attacker.tileY)] ?: 100
        val defenseTerrain = target.terrainImpacts[terrain?.terrainAt(target.tileX, target.tileY)] ?: 100
        val attack = effective(attacker, BattleAttribute.ATTACK) * attackTerrain / 100
        val defense = privateDefense(attacker, target, BattleAttribute.DEFENSE) * defenseTerrain / 100
        var damage = maxOf(1, (attack - defense) / 2 + 25 + attacker.level)
        // countAtkHarm2 deducts floor(25% * baseHarm) for every non-primary
        // filterEffAreaUnit entry.
        if (splash) damage -= damage / 4
        val minimum = if (!attacker.isPlayerSide() && attacker.armType != 1) {
            maxOf(1, attacker.maxHitPoints * minOf(7, units.values.count { it.visible && it.isPlayerSide() }) / 100)
        } else 1
        return maxOf(minimum, damage)
    }

    /** Direct BattleUnit wrapper used by the Control.js scoring port. */
    private inner class AiScoringUnit(val source: BattleUnit) : ControlScoring.Unit {
        override val index: Int get() = source.sourceCharacterId ?: source.id.hashCode()
        override val hp: Int get() = source.maxHitPoints
        override val hpCur: Int get() = source.hitPoints
        override val mp: Int get() = source.maxMagicPoints
        override val mpCur: Int get() = source.magicPoints
        override val armType: Int get() = source.armType
        override val isRemote: Boolean get() = source.remoteAttack
        override val famous: Boolean get() = source.famous
        override val mine: Boolean get() = source.isPlayerSide()
        override val ai: Int get() = source.ai
        override val aiValue: Int get() = source.aiValue
        override fun skill(id: Int): Int = source.skills[id]?.and(255) ?: 255
        override fun status(index: Int): Int = when (index) {
            0, 1, 2, 3, 4, 5 -> when {
                (source.attributeLifts[BattleAttribute.entries[index]] ?: 0) < 0 -> ControlScoring.Lift.DOWN
                (source.attributeLifts[BattleAttribute.entries[index]] ?: 0) > 0 -> ControlScoring.Lift.UP
                else -> ControlScoring.Lift.NORMAL
            }
            7 -> if (BattleStatus.PARALYSIS in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            8 -> if (BattleStatus.SILENCE in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            9 -> if (BattleStatus.CONFUSION in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            10 -> if (BattleStatus.POISON in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            13 -> if (BattleStatus.LOST in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            14 -> if (source.hasActed) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            else -> ControlScoring.Lift.NORMAL
        }
        override fun isCanXue(): Boolean = source.hitPoints < source.maxHitPoints * (if (source.famous) 4 else 2) / 10
        override fun isCanLan(): Boolean = source.magicPoints < source.maxMagicPoints * (if (source.famous) 4 else 2) / 10
        override fun attackHarms(target: ControlScoring.Unit): List<ControlScoring.AttackHarm> {
            val primary = (target as? AiScoringUnit)?.source ?: return emptyList()
            // BattleUnit.countAtkHarm2 begins with BattleLayer.testUnit.
            // This guard is also reached recursively while estimating a
            // counterattack, so a confused or out-of-range defender must
            // contribute no retaliation score.
            if (!source.visible || BattleStatus.CONFUSION in source.statuses ||
                !primary.visible || !canAttack(source, primary)) return emptyList()
            // BattleUnit.countAtkHarm2: filterEffAreaUnit(..., 42), then
            // unshift(target).  The filter contains opposing live units only.
            val affected = buildList {
                add(primary to false)
                physicalEffectPositions(source, primary).asSequence()
                    .mapNotNull { (x, y) -> unitAt(x, y) }
                    .filter { it !== primary && it.visible && !areAllied(source, it) }
                    .forEach { add(it to true) }
            }
            var flag = when {
                primary.skills.keys.any { it in intArrayOf(226, 44, 251, 50) && primary.skills[it]?.and(255) != 255 } -> 0
                canAttack(primary, source) -> 1
                else -> 0
            }
            flag = flag or when (source.armType) { 0 -> 8; 1 -> 16; else -> 0 }
            return affected.map { (victim, splash) ->
                val harm = sourceAttackHarm(source, victim, splash)
                if (victim.famous) flag = flag or 2
                if (harm >= victim.hitPoints) flag = flag or 4
                val hitRate = if (BattleStatus.CONFUSION in victim.statuses) 100 else physicalHitRate(source, victim)
                ControlScoring.AttackHarm(harm, AiScoringUnit(victim), flag, hitRate)
            }
        }
        override fun magicHarm(magic: ControlScoring.Magic, target: ControlScoring.Unit): Int {
            val profile = (magic as? AiMagic)?.source ?: return 0
            val victim = (target as? AiScoringUnit)?.source ?: return 0
            val base = maxOf(1, (effective(source, BattleAttribute.SPIRIT) - effective(victim, BattleAttribute.SPIRIT)) / 3 + 25 + source.level)
            return when (profile.type) {
                // count_magicHarm's dedicated restoration formulas.
                19 -> source.hitPoints * profile.power / 100 + if (profile.id == 39 || profile.id == 41) source.spirit / 10 else source.spirit / 2
                20 -> profile.expendMp
                4 -> if (profile.category == 2) victim.maxHitPoints * profile.power / 100 else offensiveMagicHarm(base, profile, victim)
                else -> offensiveMagicHarm(base, profile, victim)
            }
        }

        private fun offensiveMagicHarm(base: Int, magic: OriginalGameData.MagicProfile, victim: BattleUnit): Int {
            var value = maxOf(1, base * magic.power / 100 * victim.magicHarmRate / 100)
            value += magicFlatSkillDamage(source, magic)
            value = maxOf(1, value * magicSkillDamageRate(source, victim, magic) / 100)
            value = value * magicWeatherRate(magic) / 100
            value = value * offensiveMagicTerrainRate(source, magic) / 100
            val minimum = if (!source.isPlayerSide()) {
                maxOf(1, minOf(7, units.values.count { it.visible && it.isPlayerSide() }) * source.maxMagicPoints / 100)
            } else 1
            return maxOf(minimum, value)
        }
    }

    private data class AiMagic(val source: OriginalGameData.MagicProfile) : ControlScoring.Magic {
        override val id get() = source.id
        override val category get() = source.category
        override val type get() = source.type
        override val harmType get() = source.harmType
        override val expendMp get() = source.expendMp
    }

    /** Live adapter for Control._countMagicValue's status/HP/MP scoring. */
    private fun estimatedMagicValue(
        attacker: BattleUnit,
        target: BattleUnit,
        magic: OriginalGameData.MagicProfile,
        cache: MutableMap<String, Int>,
    ): Int = ControlScoring.magicValue(
        AiMagic(magic), AiScoringUnit(attacker), AiScoringUnit(target), cache,
        hitRate = { _, _, _ -> magicHitRate(attacker, target, magic) },
    )

    private fun canAttack(attacker: BattleUnit, target: BattleUnit): Boolean =
        attacker.attackAllScreen || ((target.tileX - attacker.tileX) to (target.tileY - attacker.tileY)) in attacker.attackOffsets

    /** Control._aiHaveAttackTargets evaluated for a candidate `psAry` tile. */
    private fun canAttackFrom(attacker: BattleUnit, x: Int, y: Int, target: BattleUnit): Boolean =
        attacker.attackAllScreen || ((target.tileX - x) to (target.tileY - y)) in attacker.attackOffsets

    /** Exact position half of BattleLayer.filterEffAreaUnit(attacker, target, effarea, 42). */
    private fun physicalEffectPositions(attacker: BattleUnit, target: BattleUnit): Set<Pair<Int, Int>> {
        val effectArea = attacker.attackEffectAreaId ?: return attacker.attackEffectOffsets.mapTo(linkedSetOf()) { (dx, dy) ->
            target.tileX + dx to target.tileY + dy
        }
        // BattleLayer.filterEffAreaUnit explicitly assigns `f = []` for
        // ZHUORE and leaves YUANZHEN in that same empty default branch.
        if (effectArea == 0 || effectArea == 12) return emptySet()
        fun sign(value: Int) = value.compareTo(0)
        val dx = sign(target.tileX - attacker.tileX)
        val dy = sign(target.tileY - attacker.tileY)
        val dynamic = when (effectArea) {
            4, 5, 7 -> List(if (effectArea == 4) 1 else if (effectArea == 5) 5 else 2) { index ->
                target.tileX + dx * (index + 1) to target.tileY + dy * (index + 1)
            }
            9 -> when {
                dx == 0 && dy == 0 -> emptyList()
                dx == 0 -> listOf(target.tileX - 1 to target.tileY, target.tileX + 1 to target.tileY)
                dy == 0 -> listOf(target.tileX to target.tileY - 1, target.tileX to target.tileY + 1)
                else -> listOf(target.tileX + dx to target.tileY, target.tileX to target.tileY + dy)
            }
            11 -> {
                val side = when {
                    dx == 0 && dy == 0 -> emptyList()
                    dx == 0 -> listOf(target.tileX - 1 to target.tileY, target.tileX + 1 to target.tileY)
                    dy == 0 -> listOf(target.tileX to target.tileY - 1, target.tileX to target.tileY + 1)
                    else -> listOf(target.tileX + dx to target.tileY, target.tileX to target.tileY + dy)
                }
                side + List(2) { index -> target.tileX + dx * (index + 1) to target.tileY + dy * (index + 1) }
            }
            else -> emptyList()
        }
        if (dynamic.isNotEmpty()) return dynamic.toCollection(linkedSetOf())
        // KUANGWU (10) anchors the table pattern at attacker; all ordinary
        // static patterns anchor at target. ZHUORE (0) intentionally empty.
        val anchor = if (effectArea == 10) attacker else target
        return attacker.attackEffectOffsets.mapTo(linkedSetOf()) { (x, y) -> anchor.tileX + x to anchor.tileY + y }
    }

    /**
     * BattleUnit.countAtkHarm ZYSH (277).  Once an attack has really hit,
     * the defender may redirect a percentage of the pending harm to the
     * lowest-HP unit on the opposing side inside its own attack pattern.
     * The original attacker is removed from that candidate list.
     */
    private fun physicalDamageTransfer(
        attacker: BattleUnit,
        defender: BattleUnit,
        sourceHarm: Int,
    ): Pair<BattleUnit, Int>? {
        val percent = defender.skills[277]?.and(255)?.takeIf { it != 255 } ?: return null
        if (sourceHarm < defender.level || BattleStatus.CONFUSION in defender.statuses) return null
        val candidates = (if (defender.attackAllScreen) {
            units.values.asSequence()
        } else {
            defender.attackOffsets.asSequence().mapNotNull { (dx, dy) ->
                unitAt(defender.tileX + dx, defender.tileY + dy)
            }
        }).distinct()
            .filter { it !== attacker && !areAllied(defender, it) }
            .toList()
            .let { found -> if (found.size > 1) found.sortedBy { it.hitPoints } else found }
        val recipient = candidates.firstOrNull() ?: return null
        return recipient to (sourceHarm * percent / 100)
    }

    /** `countAtkHarm` constructs all CTGJ records before the attack hit callback. */
    private fun computePhysicalSplashHarms(
        attacker: BattleUnit,
        primaryTarget: BattleUnit,
        critical: Boolean,
        activeAttack: Boolean = true,
        counter: Boolean = false,
        continuous: Boolean = false,
    ): List<Pair<BattleUnit, Int>> = physicalEffectPositions(attacker, primaryTarget).asSequence()
        .mapNotNull { (x, y) -> unitAt(x, y) }
        .filter { it !== primaryTarget && it.visible && !areAllied(attacker, it) }
        .map { affected ->
            val special = mrspDamage(attacker, affected)
            val harm = special ?: run {
                val attackTerrain = attacker.terrainImpacts[terrain?.terrainAt(attacker.tileX, attacker.tileY)] ?: 100
                val defenseTerrain = affected.terrainImpacts[terrain?.terrainAt(affected.tileX, affected.tileY)] ?: 100
                val base = maxOf(1, (
                    effective(attacker, BattleAttribute.ATTACK) * attackTerrain / 100 -
                        effective(affected, BattleAttribute.DEFENSE) * defenseTerrain / 100
                    ) / 2 + 25 + attacker.level)
                var value = maxOf(1, base * physicalArmRestraint(attacker, affected) / 100)
                value = value * physicalDamageRate(attacker, affected) / 100
                value = physicalDamageAfterResistance(value, attacker, affected)
                value += physicalFlatSkillDamage(attacker, affected, activeAttack = activeAttack)
                value = maxOf(1, value)
                value = armorPiercingMinimumDamage(attacker, affected, value)
                value = cappedPhysicalDamage(affected, value)
                maxOf(
                    physicalMinimumDamage(attacker),
                    value * physicalCriticalRate(
                        attacker,
                        affected,
                        critical,
                        counter = counter,
                        continuous = continuous,
                        splash = true,
                    ) / 100,
                )
            }
            if (special == null) consumeMpAttackSkill(attacker)
            affected to harm
        }
        .toList()

    /** BattleUnit.countAtkHarm's `a.length > 0` HAVE_CT predicate. */
    private fun hasPhysicalEffectTargets(attacker: BattleUnit, target: BattleUnit): Boolean =
        physicalEffectPositions(attacker, target).asSequence()
            .mapNotNull { (x, y) -> unitAt(x, y) }
            .any { it !== target && it.visible && !areAllied(attacker, it) }

    /** CtrlGJWJ._aiHaveAttackTargets(targetIndex) across every psAry point. */
    private fun hasAttackCandidate(attacker: BattleUnit, target: BattleUnit): Boolean =
        linkedSetOf(attacker.tileX to attacker.tileY).apply { addAll(reachableTiles(attacker.id).keys) }
            .any { (x, y) -> canAttackFrom(attacker, x, y, target) }

    private fun areAllied(left: Faction, right: Faction): Boolean =
        left.isPlayerSide() == right.isPlayerSide()

    private fun areAllied(left: BattleUnit, right: BattleUnit): Boolean =
        areAllied(left.effectiveFaction(), right.effectiveFaction())

    /** Config.RATES, stored from BATTLE_UNIT_ATTR_NAME.JQ_BDMZL onward. */
    private object Rate {
        const val BDMZL = 0; const val GDL = 1; const val SJL = 2; const val BSJL = 3
        const val FSMZL = 4; const val FSGDL = 5; const val BJL = 6; const val BBJL = 7
    }

    /**
     * Direct BattleUnit.countRate port.  Source combat probabilities are
     * deterministic opposed gauges, not a fresh random roll: the attacker
     * receives `n` (doubled by JQJB), the defender receives `100-n`, and the
     * side that crosses first wins before both values wrap by 100.
     */
    private fun countRate(attacker: BattleUnit, defender: BattleUnit, attackerRate: Int, defenderRate: Int, rate: Int): Boolean {
        var incoming = rate
        if (attacker.skills[111]?.and(255)?.let { it != 255 } == true) incoming = incoming shl 1 // JQJB
        var own = (attacker.rateAccumulators[attackerRate] ?: 0) + incoming
        var other = (defender.rateAccumulators[defenderRate] ?: 0) + 100 - rate
        val success = other < own
        if (success) own -= 100 else other -= 100
        attacker.rateAccumulators[attackerRate] = own.coerceIn(0, 255)
        defender.rateAccumulators[defenderRate] = other.coerceIn(0, 255)
        return success
    }

    /** Model.random() delegates to Tool.random(0, 100), whose bounds are inclusive. */
    private fun sourceRandom100(): Int = sourceDefaultRandom(0, 100)
    private fun sourceDefaultRandom(min: Int, max: Int): Int = sourceRandomStreams?.random(min, max, 0) ?: (random.nextInt(max - min + 1) + min)
    private fun sourceFlagRandom(min: Int, max: Int): Int = sourceRandomStreams?.random(min, max, 1) ?: (random.nextInt(max - min + 1) + min)

    /** BattleUnit.checkCrit increments for countAtkHarm's retained CRIT flag, then shows every other one. */
    private fun resolveCriticalSpeech(unit: BattleUnit, criticalFlag: Boolean): String? {
        if (!criticalFlag) return null
        val show = unit.criticalSpeechChecks % 2 == 0
        unit.criticalSpeechChecks++
        if (!show) return null
        val speech = unit.criticalSpeech
        if (speech.texts.isEmpty()) return null
        val index = when {
            !speech.randomized || speech.texts.size == 1 -> 0
            speech.flagRandom -> sourceFlagRandom(0, speech.texts.lastIndex)
            else -> sourceDefaultRandom(0, speech.texts.lastIndex)
        }
        return speech.texts[index]
    }

    private fun distance(a: BattleUnit, b: BattleUnit): Int = kotlin.math.abs(a.tileX - b.tileX) + kotlin.math.abs(a.tileY - b.tileY)

    /** BattleUnit.canBack/backMove: one tile directly away from the attacker. */
    private fun backPosition(defender: BattleUnit, attacker: BattleUnit): Pair<Int, Int>? {
        val dx = when {
            defender.tileX < attacker.tileX -> -1
            defender.tileX > attacker.tileX -> 1
            else -> 0
        }
        val dy = when {
            defender.tileY < attacker.tileY -> -1
            defender.tileY > attacker.tileY -> 1
            else -> 0
        }
        val point = defender.tileX + dx to defender.tileY + dy
        if (point.first < 0 || point.second < 0) return null
        if (terrain?.let { point.first >= it.width || point.second >= it.height } == true) return null
        if (point in blockedTiles || unitAt(point.first, point.second) != null) return null
        val terrainId = terrain?.terrainAt(point.first, point.second)
        if (terrainId?.let { defender.terrainMovementCosts[it] ?: 255 } ?: 1 >= 255) return null
        return point
    }

    private fun moveToward(unit: BattleUnit, goalX: Int, goalY: Int): Boolean {
        val candidates = movePoints(unit, finalMovement(unit)).points.keys
            .asSequence()
            .filter { it != unit.tileX to unit.tileY }
            .sortedBy { kotlin.math.abs(goalX - it.first) + kotlin.math.abs(goalY - it.second) }
        val target = candidates.firstOrNull { (x, y) -> x to y !in blockedTiles && unitAt(x, y) == null } ?: return false
        return moveUnit(unit.id, target.first, target.second) is TacticalActionResult.Success
    }

    /** BattleUnit.countDir: 0 up, 1 right, 2 down, 3 left. */
    private fun facingDirection(fromX: Int, fromY: Int, toX: Int, toY: Int): Int {
        val dx = kotlin.math.abs(toX - fromX)
        val dy = kotlin.math.abs(toY - fromY)
        return if (dy > dx) {
            if (fromY > toY) 0 else 2
        } else if (fromX > toX) 3 else 1
    }

    /** Direct port of BattleLayer.canMovePoints' FIFO remaining-movement walk. */
    private fun movePoints(
        unit: BattleUnit,
        movement: Int,
        ignoredEnemyId: String? = null,
        startOverride: Pair<Int, Int>? = null,
    ): MovePoints {
        // `canMovePoints` queue entries are [x, y, remaining, parent,
        // blockedByEnemyNear].  Keep the final flag: a unit without TJYD may
        // enter, but may not expand from, a tile adjacent to an enemy.
        data class Queued(
            val point: Pair<Int, Int>,
            val remaining: Int,
            val parent: Pair<Int, Int>?,
            val blockedByEnemyNear: Boolean = false,
        )
        val start = startOverride ?: (unit.tileX to unit.tileY)
        val remainingByPoint = linkedMapOf(start to movement)
        val queue = ArrayDeque<Queued>()
        queue += Queued(start, movement, null)
        val processed = linkedMapOf<Pair<Int, Int>, MovePoint>()
        // Config.SKILL_TYPE: CYYD(29), ELYD(35), TJEL(219), TJYD(220).
        val ignoresTerrain = unit.skills[29]?.and(255)?.let { it != 255 } == true
        val allTerrainAndEnemyNear = unit.skills[219]?.and(255)?.let { it != 255 } == true
        val oneTerrainCost = !allTerrainAndEnemyNear && unit.skills[35]?.and(255)?.let { it != 255 } == true
        val canLeaveEnemyNear = allTerrainAndEnemyNear || unit.skills[220]?.and(255)?.let { it != 255 } == true
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            // Source `y[key]` is assigned when a FIFO entry is popped; a
            // later, higher-remaining revisit deliberately replaces parent.
            // Source overwrites y[key].idx with the current pop counter and
            // later sorts by that last idx. LinkedHashMap assignment alone
            // retains the first insertion position, so explicitly reinsert
            // revisited points to preserve the source's final psAry order.
            processed.remove(current.point)
            processed[current.point] = MovePoint(current.remaining, current.parent)
            if (current.blockedByEnemyNear) continue
            orderedMovementOffsets.forEach { (dx, dy) ->
                val next = current.point.first + dx to current.point.second + dy
                if (next.first < 0 || next.second < 0 || next in blockedTiles) return@forEach
                if (terrain?.let { next.first >= it.width || next.second >= it.height } == true) return@forEach
                val occupant = unitAt(next.first, next.second)
                // checkRoundArm rejects only a direct enemy.  A same-camp
                // unit remains in psHash (and is discarded later by
                // Control._AIProcess if it is not the acting unit).
                if (occupant != null && occupant.id != ignoredEnemyId && occupant !== unit && !areAllied(occupant, unit)) return@forEach
                val terrainId = terrain?.terrainAt(next.first, next.second)
                val cost = when {
                    ignoresTerrain || allTerrainAndEnemyNear || oneTerrainCost -> 1
                    else -> terrainId?.let { unit.terrainMovementCosts[it] ?: 255 } ?: 1
                }
                if (cost >= 255 || current.remaining < cost) return@forEach
                val remaining = current.remaining - cost
                if (remainingByPoint[next]?.let { remaining <= it } == true) return@forEach
                val enemyNear = !allTerrainAndEnemyNear && !canLeaveEnemyNear && movementOffsets.any { (nearX, nearY) ->
                    unitAt(next.first + nearX, next.second + nearY)?.let {
                        it.id != ignoredEnemyId && !areAllied(it, unit)
                    } == true
                }
                remainingByPoint[next] = remaining
                queue += Queued(next, remaining, current.point, enemyNear)
            }
        }
        return MovePoints(processed, start)
    }

    /** BattleLayer.AStar, including its stable weighted queue and flag bits. */
    private fun sourceAStar(unit: BattleUnit, targetX: Int, targetY: Int, flags: Int): List<Pair<Int, Int>>? {
        data class Node(val point: Pair<Int, Int>, val cost: Int, val parent: Pair<Int, Int>?, val order: Long)
        val start = unit.tileX to unit.tileY
        val queue = mutableListOf(Node(start, 0, null, 0))
        val visited = linkedSetOf(start)
        val parents = linkedMapOf<Pair<Int, Int>, Pair<Int, Int>?>()
        var sequence = 1L
        val avoidEnemies = flags and 1 != 0
        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            parents[current.point] = current.parent
            if (current.point == targetX to targetY) {
                val path = mutableListOf<Pair<Int, Int>>()
                var cursor: Pair<Int, Int>? = current.point
                while (cursor != null) {
                    path += cursor
                    cursor = parents[cursor]
                }
                return path.asReversed()
            }
            orderedMovementOffsets.forEach { (dx, dy) ->
                val next = current.point.first + dx to current.point.second + dy
                if (!visited.add(next)) return@forEach
                if (next.first < 0 || next.second < 0 || terrain?.let { next.first >= it.width || next.second >= it.height } == true) return@forEach
                // `blockedTiles` is the tactical representation of the
                // source's live gate nodes. BattleLayer.getBattleTerrain
                // resolves such a node to BATTLE_GATE_ATTR.TERRAIN before
                // AStar calls getArmTerrain; closed gates therefore fail the
                // same `k >= 255` branch as an impassable map tile. Keep the
                // rejection after `visited.add`, matching the source's `f`
                // insertion before its bounds/terrain checks.
                if (next in blockedTiles) return@forEach
                var cost = terrain?.terrainAt(next.first, next.second)?.let { unit.terrainMovementCosts[it] ?: 255 } ?: 1
                if (cost >= 255) return@forEach
                if (unit.skills.keys.any { it in setOf(35, 219) && unit.skills[it]?.and(255) != 255 }) cost = 1
                if (avoidEnemies && !(flags and 8 != 0 && next == targetX to targetY)) {
                    val occupant = unitAt(next.first, next.second)
                    if (occupant != null && !areAllied(occupant, unit)) {
                        if (flags and 4 != 0) cost += 255 else return@forEach
                    }
                }
                queue += Node(next, current.cost + cost, current.point, sequence++)
            }
            queue.sortWith(compareBy<Node>({ it.cost }, { it.order }))
        }
        return null
    }

    /**
     * Read-only path used by authored `stage.unit(...).move(...)` commands.
     * Mirrors BattleUnit.move: findEmptyPos first, then BattleLayer.AStar with
     * flags=0. Coordinate mutation remains at the presentation callback.
     */
    fun scriptedMovePath(sourceCharacterId: Int, targetX: Int, targetY: Int): List<Pair<Int, Int>>? {
        val unit = (units.values + presentationUnits.values)
            .firstOrNull { it.sourceCharacterId == sourceCharacterId } ?: return null
        val clamped = targetX.coerceIn(0, (terrain?.width ?: 100) - 1) to
            targetY.coerceIn(0, (terrain?.height ?: 100) - 1)
        val queue = ArrayDeque<Pair<Int, Int>>()
        val visited = linkedSetOf(clamped)
        queue += clamped
        var destination: Pair<Int, Int>? = null
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            val terrainCost = terrain?.terrainAt(point.first, point.second)
                ?.let { unit.terrainMovementCosts[it] ?: 255 } ?: 1
            if ((unitAt(point.first, point.second)?.let { it === unit } != false) && terrainCost < 255) {
                destination = point
                break
            }
            orderedMovementOffsets.forEach { (dx, dy) ->
                val next = point.first + dx to point.second + dy
                if (next.first >= 0 && next.second >= 0 &&
                    next.first < (terrain?.width ?: 100) && next.second < (terrain?.height ?: 100) &&
                    visited.add(next)
                ) queue += next
            }
        }
        return destination?.let { sourceAStar(unit, it.first, it.second, 0) }
    }

    /** BattleLayer.findEmptyPos constrained to ControlManager.psHash. */
    private fun sourceFindEmptyPosition(
        unit: BattleUnit,
        seed: Pair<Int, Int>,
        reachable: Set<Pair<Int, Int>>,
    ): Pair<Int, Int>? {
        // BattleLayer.findEmptyPos is not a breadth-first search.  It keeps
        // the QUN_XIONG frontier sorted by accumulated arm-terrain cost,
        // then returns the first unoccupied passable position.  This matters
        // when `_AStar` reaches an occupied psHash point: the source may
        // choose a lower-cost detour rather than the geometrically nearest
        // empty tile.
        data class Node(val point: Pair<Int, Int>, val totalExpend: Int, val order: Long)
        val queue = mutableListOf(Node(seed, 0, 0))
        val visited = linkedSetOf(seed)
        var sequence = 1L
        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            val point = current.point
            val terrainCost = terrain?.terrainAt(point.first, point.second)?.let { unit.terrainMovementCosts[it] ?: 255 } ?: 1
            if ((unitAt(point.first, point.second)?.let { it === unit } != false) && terrainCost < 255) return point
            orderedMovementOffsets.forEach { (dx, dy) ->
                val next = point.first + dx to point.second + dy
                // The original marks a neighbor visited before its bounds
                // check and still queues impassable terrain.  Its cost order,
                // rather than a BFS ring, determines the first empty psHash
                // tile returned to Control._AStar.
                if (next !in reachable || !visited.add(next)) return@forEach
                if (next.first < 0 || next.second < 0 ||
                    next.first >= (terrain?.width ?: 100) || next.second >= (terrain?.height ?: 100)
                ) return@forEach
                val cost = terrain?.terrainAt(next.first, next.second)
                    ?.let { unit.terrainMovementCosts[it] ?: 255 } ?: 1
                queue += Node(next, current.totalExpend + cost, sequence++)
            }
            // V8's stable Array.sort preserves QUN_XIONG insertion order on
            // equal expenditure.  Retain that order explicitly here.
            queue.sortWith(compareBy<Node>({ it.totalExpend }, { it.order }))
        }
        return null
    }

    private data class MovePoint(val remaining: Int, val parent: Pair<Int, Int>?)
    private data class MovePoints(val points: Map<Pair<Int, Int>, MovePoint>, val start: Pair<Int, Int>) {
        fun pathTo(destination: Pair<Int, Int>): List<Pair<Int, Int>> {
            val path = mutableListOf(destination)
            var point = requireNotNull(points[destination]).parent
            while (point != null) {
                path += point
                if (point == start) break
                point = requireNotNull(points[point]).parent
            }
            return path.asReversed()
        }
    }

    private fun physicalHitRate(attackerCritical: Int, defenderCritical: Int): Int {
        val attacker = attackerCritical.toDouble()
        val defender = defenderCritical.coerceAtLeast(1).toDouble()
        val rate = when {
            attacker >= 2 * defender -> 100.0
            attacker >= defender -> 10 * (attacker - defender) / defender + 90
            attacker >= defender / 2 -> 30 * (attacker - defender / 2) / (defender / 2) + 60
            else -> 30 * (attacker - defender / 3) / (defender / 3) + 30
        }
        // Source performs every division as JavaScript Number arithmetic and
        // truncates only once at the end. Integer intermediate divisions
        // made several opposed gauges drift by one point.
        return rate.toInt().coerceIn(25, 100)
    }

    /** BattleUnit.countAtkHarm hit modifiers and immunity skills. */
    private fun physicalHitRate(attacker: BattleUnit, target: BattleUnit): Int {
        fun effect(unit: BattleUnit, skill: Int) = unit.skills[skill]?.and(255)?.takeIf { it != 255 }
        val baseline = physicalHitRate(
            effective(attacker, BattleAttribute.CRITICAL),
            privateDefense(attacker, target, BattleAttribute.CRITICAL),
        )
        return (baseline - (effect(target, 64) ?: 0) - (effect(target, 71) ?: 0) + (effect(attacker, 66) ?: 0)).coerceIn(25, 100)
    }

    /** BattleUnit.countAtkHarm: update gauge, then apply its source overrides. */
    private fun physicalHit(attacker: BattleUnit, target: BattleUnit, hitRate: Int): Boolean {
        val rolled = countRate(attacker, target, Rate.BDMZL, Rate.GDL, hitRate)
        // FYYJGJ is checked before GJJDMZ in the original post-countRate
        // chain, so remote immunity remains absolute.
        if (attacker.remoteAttack && target.skills[48]?.and(255)?.let { it != 255 } == true) return false
        if (attacker.skills[92]?.and(255)?.let { it != 255 } == true) return true // GJJDMZ
        if (BattleStatus.CONFUSION in target.statuses) return true
        return rolled
    }

    /** BattleUnit.count_crit_rate: morale-based critical chance. */
    private fun criticalRate(attackerMorale: Int, defenderMorale: Int): Int {
        val attacker = attackerMorale.coerceAtLeast(1)
        val defender = defenderMorale.coerceAtLeast(1)
        val rate = when {
            attacker >= 3 * defender -> 100
            attacker >= 2 * defender -> ((attacker.toDouble() / defender * .8 - 1.4) * 100).toInt()
            attacker >= defender -> ((attacker.toDouble() / defender * .18 - .16) * 100).toInt()
            else -> 0
        }
        return rate.coerceIn(0, 100)
    }

    /** BattleUnit.count_crit_rate followed by BattleUnit.countRate. */
    private fun criticalHit(attacker: BattleUnit, target: BattleUnit): Boolean {
        val rate = if (attacker.skills[270]?.and(255)?.let { it != 255 } == true) {
            100 // ZMYJGJ: count_crit_rate returns 100, it does not skip countRate.
        } else {
            criticalRate(
                effective(attacker, BattleAttribute.MORALE),
                privateDefense(attacker, target, BattleAttribute.MORALE),
            )
        }
        return countRate(attacker, target, Rate.BJL, Rate.BBJL, rate)
    }

    private fun magicHitRate(attackerSpirit: Int, attackerMorale: Int, defenderSpirit: Int, defenderMorale: Int): Int {
        val attacker = (attackerSpirit + attackerMorale).toDouble()
        val defender = (defenderSpirit + defenderMorale).coerceAtLeast(1).toDouble()
        val rate = when {
            attacker >= 2 * defender -> 100.0
            attacker >= defender -> 10 * (attacker - defender) / defender + 90
            attacker >= defender / 2 -> 30 * (attacker - defender / 2) / (defender / 2) + 60
            else -> 30 * (attacker - defender / 3) / (defender / 3) + 30
        }
        return rate.toInt().coerceIn(25, 100)
    }

    /** BattleLayer._countMagicHitRateMilit + count_magic_hitRate skill modifiers. */
    private fun magicHitRate(attacker: BattleUnit, target: BattleUnit, magic: OriginalGameData.MagicProfile): Int {
        fun effect(unit: BattleUnit, skill: Int) = unit.skills[skill]?.and(255)?.takeIf { it != 255 }
        val base = magicHitRate(
            effective(attacker, BattleAttribute.SPIRIT), effective(attacker, BattleAttribute.MORALE),
            effective(target, BattleAttribute.SPIRIT), effective(target, BattleAttribute.MORALE),
        )
        val limit = magic.hitRateLimit
        if (limit == 0 || limit == 64) return 100
        // Control._countMagicHitRateMilit: hitLimit 3/4 sets the cap to zero
        // for a famous target and, crucially, does *not* set its modifier
        // flag.  The common final range() still exposes 25 as the displayed
        // hit rate; only CLMY below decides the actual automatic miss.
        val famousCap = target.famous && limit in 3..4
        var rate = when (limit) {
            1 -> base * 90 / 100
            2 -> base
            3 -> minOf(base, if (target.famous) 0 else 50)
            4 -> minOf(base, if (target.famous) 0 else 34)
            else -> base * limit
        }
        if (!famousCap) {
            if (effect(attacker, 15) != null) rate = 100 // CLJDMZ
            rate += effect(attacker, 56) ?: 0 // FZCLMZ
            rate -= effect(target, 55) ?: 0 // FZCLFY
            rate -= effect(target, 71) ?: 0 // FZQFY
        }
        return rate.coerceIn(25, 100)
    }

    /** BattleLayer.count_magic_hitRate: CLMY overrides the random result. */
    private fun magicHit(
        attacker: BattleUnit,
        target: BattleUnit,
        magic: OriginalGameData.MagicProfile,
        hitRate: Int,
    ): Boolean {
        // Source invokes countRate before testing CLMY, so retain the random
        // draw even for the guaranteed miss branch.
        val rolled = countRate(attacker, target, Rate.FSMZL, Rate.FSGDL, hitRate)
        return rolled && target.skills[17]?.and(255)?.let { it == 255 } != false
    }

    private fun OriginalGameData.MagicProfile.statusEffect(): BattleStatus? = when (category) {
        8 -> BattleStatus.CONFUSION
        9 -> BattleStatus.POISON
        10 -> BattleStatus.PARALYSIS
        11 -> BattleStatus.SILENCE
        else -> null
    }

    /**
     * getMagicTerrainRate affects efficiency rather than target legality.
     * Offensive count_magicHarm floors this rate to 85%; only JHP healing
     * retains a zero rate without CLWSDX (19).
     */
    private fun magicTerrainAllowed(magic: OriginalGameData.MagicProfile, target: BattleUnit): Boolean = true
    /** BattleUnit.getMagicWeatherRate; a bypassed weather restriction is 85% efficient. */
    private fun magicWeatherRate(magic: OriginalGameData.MagicProfile): Int {
        val allowed = when (magic.condition) {
            0 -> weather in setOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY)
            2 -> weather in setOf(BattleWeather.HEAVY_RAIN, BattleWeather.SNOW)
            3 -> weather == BattleWeather.CLEAR
            4 -> weather == BattleWeather.CLOUDY
            else -> true
        }
        return if (allowed) 100 else 85
    }

    /** Offensive count_magicHarm floors unsuitable elemental terrain at 85%. */
    private fun offensiveMagicTerrainRate(target: BattleUnit, magic: OriginalGameData.MagicProfile): Int {
        if (magic.type !in 0..3) return 100
        val terrainId = terrain?.terrainAt(target.tileX, target.tileY) ?: return 100
        val flag = terrainMagicFlags[terrainId] ?: 0
        return if (flag and (1 shl magic.type) != 0) 100 else 85
    }
    private fun healingTerrainRate(attacker: BattleUnit, magic: OriginalGameData.MagicProfile): Int {
        if (magic.type !in 0..3 || terrainMagicFlags.isEmpty()) return 100
        val terrainId = terrain?.terrainAt(attacker.tileX, attacker.tileY) ?: return 100
        val flag = terrainMagicFlags[terrainId] ?: return 100
        if (flag and (1 shl magic.type) != 0) return 100
        return if (attacker.skills[19]?.and(255)?.let { it != 255 } == true) 85 else 0
    }

    /** BattleUnit._count_magic_add's direct, non-aura additions. */
    private fun magicFlatSkillDamage(attacker: BattleUnit, magic: OriginalGameData.MagicProfile): Int {
        fun effect(skill: Int) = attacker.skills[skill]?.and(255)?.takeIf { it != 255 }
        var addition = effect(141)?.let { effective(attacker, BattleAttribute.ATTACK) * it / 100 } ?: 0 // LRHY
        if (magic.type == 0) addition += effect(107) ?: 0 // HXCLZS, Gong Huo
        return addition
    }

    /** BattleUnit._count_magic_rate. */
    private fun magicSkillDamageRate(attacker: BattleUnit, target: BattleUnit, magic: OriginalGameData.MagicProfile): Int {
        fun effect(unit: BattleUnit, skill: Int) = unit.skills[skill]?.and(255)?.takeIf { it != 255 }
        var rate = 100
        // MRSP2 is the first operation in source _count_magic_rate and uses
        // the flag-random stream. Omitting the draw changed both damage and
        // every later combat decision in long battles.
        effect(attacker, 292)?.let { rate += 10 + sourceFlagRandom(0, 5) }
        if (magic.type in 0..3) rate += effect(attacker, 75) ?: 0 // FZSLCL
        if (magic.type == 0 && magic.effectAreaId == 0) rate += effect(attacker, 128) ?: 0 // JING_CE
        if (magic.type in 4..18) rate += effect(attacker, 62) ?: 0 // FZFACL
        effect(attacker, 145)?.takeIf { attacker.hitPoints >= attacker.magicPoints / 2 }?.let { rate += it } // MAI_DONG
        rate -= effect(target, 115) ?: 0 // JQCLSH
        effect(target, 245)?.let { rate -= target.hitPoints.coerceAtMost(target.maxHitPoints).let { hp -> (target.maxHitPoints - hp) * 100 / target.maxHitPoints.coerceAtLeast(1) } }
        return maxOf(1, rate)
    }

    /** BattleLayer.magicConditionTest, including CLWSTQ (20) and KYJZ (136). */
    private fun magicConditionReason(attacker: BattleUnit, magic: OriginalGameData.MagicProfile): String? {
        fun active(skill: Int) = attacker.skills[skill]?.and(255)?.let { it != 255 } == true
        if (magic.condition in 2..5 && active(136)) return null // KYJZ: condition restriction bypass
        if (magic.condition == 1 && attacker.hitPoints < 40) return "HP가 40 미만이면 사용할 수 없는 전략입니다."
        val weatherAllowed = when (magic.condition) {
            0 -> weather in setOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY)
            2 -> weather in setOf(BattleWeather.HEAVY_RAIN, BattleWeather.SNOW)
            3 -> weather == BattleWeather.CLEAR
            4 -> weather == BattleWeather.CLOUDY
            else -> true
        }
        if (!weatherAllowed && !active(20)) return "현재 날씨에서는 사용할 수 없는 전략입니다."
        // Source condition 5 sets the special-condition bit only; KYJZ is
        // therefore required even though no weather bit is checked.
        return if (magic.condition == 5) "이 전략의 특수 사용 조건을 충족하지 못했습니다." else null
    }

    /** Control._magicValue: non-damage strategy categories map to status lifts. */
    private fun OriginalGameData.MagicProfile.attributeChange(): Pair<BattleAttribute, Int>? = when (category) {
        4 -> BattleAttribute.CRITICAL to -1 // JDMJ
        5 -> BattleAttribute.MORALE to -1 // JDSQ
        6 -> BattleAttribute.ATTACK to -1 // JDNL (martial) / spirit for civil officers
        7 -> BattleAttribute.DEFENSE to -1 // JDFY
        16 -> BattleAttribute.MOVEMENT to 1 // ZJYDL
        17 -> BattleAttribute.CRITICAL to 1 // ZJMJ
        18 -> BattleAttribute.MORALE to 1 // ZJSQ
        19 -> BattleAttribute.ATTACK to 1 // ZJNL (martial) / spirit for civil officers
        20 -> BattleAttribute.DEFENSE to 1 // ZJFY
        else -> null
    }

    /** BattleUnit.setStateRound: enemy HL/MB override GAME_CFG.status.round. */
    private fun statusDuration(status: BattleStatus, unit: BattleUnit): Int = when {
        !unit.isPlayerSide() && status == BattleStatus.CONFUSION -> 1
        !unit.isPlayerSide() && status == BattleStatus.PARALYSIS -> 2
        else -> statusRoundFor(status)
    }.coerceIn(0, 3) // source stores each round in a two-bit field

    /** BattleLayer._stateProcess: decrement states and apply terrain recovery. */
    private fun processStartOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
    ): List<BattleUnitTurnChange> {
        // Source runs _stateProcess only for MINE and ENEMY.  MINE's
        // isMine() predicate includes FRIEND, so allied durations decrement
        // together before the player camp; the later FRIEND camp does not
        // decrement them a second time.
        val processedSide = when (faction) {
            Faction.PLAYER -> true
            Faction.ENEMY -> false
            Faction.FRIEND, Faction.REINFORCEMENTS -> null
        }
        val orderedUnits = units.values.filter {
            processedSide != null && it.effectiveFaction().isPlayerSide() == processedSide
        }.sortedWith(compareBy<BattleUnit> { it.tileY }.thenBy { it.tileX })
        if (processedSide == null) return emptyList()
        val primaryChanges = mutableListOf<BattleUnitTurnChange>()
        orderedUnits.forEach { unit ->
            val ordinaryBefore = turnSnapshot()
            unit.statuses.entries.toList().forEach { (status, rounds) ->
                if (rounds <= 1) unit.statuses.remove(status) else unit.statuses[status] = rounds - 1
            }
            unit.refStateAnime()
            // subStateRound skips NORMAL attribute slots. When an active
            // lift expires, source setStateRound(remove) first writes the
            // status table's default round and then changes the state to
            // NORMAL, leaving that packed counter intact indefinitely.
            unit.attributeLifts.keys.toList().forEach { attribute ->
                val rounds = unit.attributeLiftRounds[attribute] ?: 0
                if (rounds <= 1) {
                    unit.attributeLifts.remove(attribute)
                    unit.attributeLiftRounds[attribute] = attributeStatusRoundFor(attribute)
                } else unit.attributeLiftRounds[attribute] = rounds - 1
            }
            unit.refAttributeStatusIcons()
            val terrainId = terrain?.terrainAt(unit.tileX, unit.tileY)
            if (unit.hitPoints < unit.maxHitPoints) {
                val resumeHp = terrainResumeRates[terrainId] ?: 0
                if (resumeHp != 0) unit.addHpcur(unit.maxHitPoints * resumeHp / 100)
            }
            if (unit.magicPoints < unit.maxMagicPoints) {
                val resumeMp = terrainResumeMp[terrainId] ?: 0
                if (resumeMp != 0) unit.addMpcur(resumeMp)
            }
            if (unit.hitPoints <= 0) removeUnit(unit.id)
            primaryChanges += turnChanges(ordinaryBefore)
            if (unit.hitPoints <= 0) return@forEach

            // The four authored local branches run immediately after this
            // caster's ordinary state work, before the iterator advances.
            val caster = unit
            fun effect(skillId: Int) = caster.skills[skillId]?.and(255)?.takeIf { it != 255 }
            fun nearby(): List<BattleUnit> = infantryOffsets.mapNotNull { (dx, dy) ->
                units.values.firstOrNull { target ->
                    target.tileX == caster.tileX + dx && target.tileY == caster.tileY + dy
                }
            }.filter { it.isPlayerSide() == processedSide }.distinctBy { it.id }
            fun record(
                skillId: Int,
                value: Int,
                meffName: String? = null,
                targetOrder: List<BattleUnit>,
                mutate: () -> Unit,
            ) {
                val before = turnSnapshot()
                mutate()
                val order = targetOrder.mapIndexed { index, target -> target.id to index }.toMap()
                val nested = turnChanges(before).sortedBy { order[it.unitId] ?: Int.MAX_VALUE }
                if (targetOrder.isNotEmpty()) subflows += SettlementSubflow.LocalAura(
                    casterId = caster.id,
                    skillId = skillId,
                    skillValue = value,
                    meffName = meffName,
                    targets = targetOrder.map { it.id },
                    nestedChanges = nested,
                )
            }

            effect(103)?.let { value ->
                val targets = nearby().filter { target ->
                    listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                        .any(target.statuses::containsKey)
                }
                record(103, value, targetOrder = targets) {
                    targets.forEach { target ->
                        listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                            .forEach(target.statuses::remove)
                    }
                }
            }
            effect(208)?.let { value ->
                val targets = nearby().filter { it.hitPoints < it.maxHitPoints }
                record(208, value, "resume_hp", targets) {
                    targets.forEach { target ->
                        target.addHpcur(target.maxHitPoints * value / 100)
                    }
                }
            }
            effect(209)?.let { value ->
                val targets = nearby().filter { it.magicPoints < it.maxMagicPoints }
                record(209, value, "resume_mp", targets) {
                    targets.forEach { target ->
                        val addition = if (value == 0) (caster.level + 10) / 10
                        else target.maxMagicPoints * value / 100
                        target.addMpcur(addition)
                    }
                }
            }
            effect(210)?.takeIf { it and 31 != 0 }?.let { mask ->
                val targets = nearby()
                record(210, mask, targetOrder = targets) {
                    targets.forEach { target ->
                        BattleAttribute.entries.take(5).forEachIndexed { index, attribute ->
                            if (mask and (1 shl index) != 0) {
                                target.applySourceAttributeLift(attribute, 1, 3)
                            }
                        }
                    }
                }
            }
        }
        return primaryChanges
    }

    /** BattleLayer.restore: poison is settled after that exact camp acts. */
    private fun processEndOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
    ): List<BattleUnitTurnChange> {
        units.values.filter { it.effectiveFaction() == faction }.forEach { unit ->
            val grants = buildList {
                unit.skills[149]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = onRestoreUnitExperience(unit, amount)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.UNIT_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            unit.level = resolution.value.level
                            if (resolution.value.gained > 0) add(SettlementGrowthGrant(SettlementGrowthKind.UNIT_EXP, amount, unitResult = resolution.value))
                        }
                    }
                }
                unit.skills[150]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = onRestoreEquipmentExperience(unit, amount, CampaignState.EquipmentSlot.WEAPON)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.WEAPON_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            val result = resolution.value
                            if (result.gained > 0) add(SettlementGrowthGrant(SettlementGrowthKind.WEAPON_EXP, amount, equipmentResult = result))
                            if (result.leveledUp) equipmentUpgrades += result
                        }
                    }
                }
                unit.skills[151]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = onRestoreEquipmentExperience(unit, amount, CampaignState.EquipmentSlot.ARMOR)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.ARMOR_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            val result = resolution.value
                            if (result.gained > 0) add(SettlementGrowthGrant(SettlementGrowthKind.ARMOR_EXP, amount, equipmentResult = result))
                            if (result.leveledUp) equipmentUpgrades += result
                        }
                    }
                }
            }
            if (grants.isNotEmpty()) subflows += SettlementSubflow.Growth(unit.id, grants)
        }
        val poisonBefore = turnSnapshot()
        val lethalPoison = enabledFeatures and ENABLED_FEATURE_ZDBHSW != 0
        units.values.filter { it.effectiveFaction() == faction && BattleStatus.POISON in it.statuses }
            .toList()
            .forEach { unit ->
                // With ZDBHSW enabled the source deliberately permits poison
                // death. Only the legacy-disabled branch preserves one HP.
                if (!lethalPoison && unit.hitPoints < 2) return@forEach
                val rate = if (weather == BattleWeather.CLOUDY) 15 else 10
                var damage = unit.maxHitPoints * rate / 100
                if (!lethalPoison) damage = minOf(unit.hitPoints - 1, damage)
                unit.addHpcur(-damage)
                if (unit.hitPoints <= 0) removeUnit(unit.id)
            }
        return turnChanges(poisonBefore)
    }

    /** BattleUnit._stateBuffEff uses Model.BUFF_ATT's original 20% default. */
    private fun effective(unit: BattleUnit, attribute: BattleAttribute): Int {
        fun baseOf(value: BattleAttribute): Int = when (value) {
            BattleAttribute.ATTACK -> unit.attack
            BattleAttribute.DEFENSE -> unit.defense
            BattleAttribute.SPIRIT -> unit.spirit
            BattleAttribute.CRITICAL -> unit.critical
            BattleAttribute.MORALE -> unit.morale
            BattleAttribute.MOVEMENT -> unit.movement
        }
        var base = baseOf(attribute)
        // Unit.ability(): NLFZ turns selected attributes into a 20% bonus
        // for another attribute.  The source queries _baseBility(..., 1),
        // so these contributions deliberately precede temporary status buffs.
        val abilitySupport = unit.skills[157]?.and(255)?.takeIf { it != 255 } ?: 0
        when (attribute) {
            BattleAttribute.ATTACK -> {
                if (abilitySupport and 1 != 0) base += baseOf(BattleAttribute.SPIRIT) / 5
                if (abilitySupport and 8 != 0) base += baseOf(BattleAttribute.MORALE) / 5
                if (abilitySupport and 16 != 0) base += unit.maxHitPoints / 5
            }
            BattleAttribute.DEFENSE -> if (abilitySupport and 2 != 0) base += baseOf(BattleAttribute.SPIRIT) / 5
            BattleAttribute.SPIRIT -> if (abilitySupport and 4 != 0) base += baseOf(BattleAttribute.ATTACK) / 5
            else -> Unit
        }
        return when (unit.attributeLifts[attribute]) {
            -1 -> base * 4 / 5
            1 -> base + base / 5
            else -> base
        }
    }

    /**
     * BattleUnit._pkdx(attacker, defender, attribute).  PKDX (165) does not
     * alter the attacker: it replaces every physical defensive comparison
     * with the defender's lowest final ATT..MOR ability.
     */
    private fun privateDefense(attacker: BattleUnit, defender: BattleUnit, attribute: BattleAttribute): Int {
        val hasPrivateDefense = attacker.skills[165]?.and(255)?.let { it != 255 } == true
        if (!hasPrivateDefense) return effective(defender, attribute)
        return listOf(
            BattleAttribute.ATTACK,
            BattleAttribute.DEFENSE,
            BattleAttribute.SPIRIT,
            BattleAttribute.CRITICAL,
            BattleAttribute.MORALE,
        ).minOf { effective(defender, it) }
    }

    private fun effectiveMovement(unit: BattleUnit): Int = when (unit.attributeLifts[BattleAttribute.MOVEMENT]) {
        -1 -> maxOf(0, unit.movement - 2)
        1 -> unit.movement + 2
        else -> unit.movement
    }

    /** BattleUnit.mov_final: MOV state followed by windy/heavy-rain penalty. */
    private fun finalMovement(unit: BattleUnit): Int {
        var result = effectiveMovement(unit)
        if (weather == BattleWeather.WINDY ||
            (weather == BattleWeather.HEAVY_RAIN && unit.skills[268]?.and(255)?.let { it != 255 } != true)
        ) result -= 1
        return result
    }

    /** BattleUnit.countOtherHarm: physical mitigation values are percentages. */
    private fun physicalDamageAfterResistance(rawDamage: Int, attacker: BattleUnit, defender: BattleUnit): Int {
        var rate = 100
        // WSYJFY (230) disables the defender's ranged-damage resistance.
        if (attacker.remoteAttack && attacker.skills[230]?.and(255)?.let { it != 255 } != true) {
            defender.skills[119]?.and(255)?.takeIf { it != 255 }?.let { rate -= it }
        }
        return maxOf(1, rawDamage * maxOf(0, rate) / 100)
    }

    private data class AttackStatusBatch(
        val statuses: Set<BattleStatus>,
        val downAttributes: Set<BattleAttribute>,
    )

    /** BattleUnit.getAtkStatus plus _attack2's two random supplementary lists. */
    private fun rollAttackStatusBatch(attacker: BattleUnit): AttackStatusBatch {
        val statuses = linkedSetOf<BattleStatus>()
        fun chance(skillId: Int, status: BattleStatus) {
            attacker.skills[skillId]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                if (sourceRandom100() < rate) statuses += status
            }
        }
        chance(105, BattleStatus.CONFUSION)
        chance(144, BattleStatus.PARALYSIS)
        chance(127, BattleStatus.SILENCE)
        attacker.skills[272]?.and(255)?.takeIf { it != 255 }?.let { rate ->
            if (sourceRandom100() <= rate) statuses += BattleStatus.POISON
        }
        if (attacker.skills[204]?.and(255)?.let { it != 255 } == true) {
            listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                .filterNot(statuses::contains)
                .forEach { status -> if (sourceRandom100() > 70) statuses += status }
        }
        val staticAttributes = linkedSetOf<BattleAttribute>()
        mapOf(170 to BattleAttribute.ATTACK, 169 to BattleAttribute.DEFENSE, 171 to BattleAttribute.SPIRIT,
            168 to BattleAttribute.CRITICAL, 172 to BattleAttribute.MORALE, 173 to BattleAttribute.MOVEMENT)
            .forEach { (skill, attribute) -> if (attacker.skills[skill]?.and(255)?.let { it != 255 } == true) staticAttributes += attribute }
        val down = staticAttributes.toMutableSet()
        if (attacker.skills[203]?.and(255)?.let { it != 255 } == true) {
            var threshold = 60
            BattleAttribute.entries.forEach { attribute ->
                if (attribute !in staticAttributes && sourceRandom100() > threshold) down += attribute
                threshold += 5
            }
        }
        return AttackStatusBatch(statuses, down)
    }

    /** `_attack3` setCharInfoBykey application of a precomputed attack batch. */
    private fun applyIncomingAttackStatuses(batch: AttackStatusBatch, target: BattleUnit) {
        val newlyApplied = batch.statuses.filterTo(linkedSetOf()) { it !in target.statuses }
        batch.statuses.forEach { status -> target.statuses[status] = statusDuration(status, target) }
        if (target.skills[42]?.and(255)?.let { it != 255 } == true) newlyApplied.forEach(target.statuses::remove)
        if (target.skills[122]?.and(255)?.let { it != 255 } != true) {
            batch.downAttributes.forEach { attribute ->
                target.applySourceAttributeLift(attribute, -1, 3)
            }
        }
        target.refStateAnime()
        target.refAttributeStatusIcons()
    }

    /** One source `_attack3`, including every target-local secondary effect. */
    private fun resolvePhysicalTarget(
        attacker: BattleUnit,
        target: BattleUnit,
        sourceHarm: Int,
        statuses: AttackStatusBatch,
        activeAttack: Boolean,
    ): PhysicalAttackTargetResult {
        val targetXBefore = target.tileX
        val targetYBefore = target.tileY
        val statusesBefore = target.statuses.toMap()
        val liftsBefore = target.attributeLifts.toMap()
        val liftRoundsBefore = target.attributeLiftRounds.toMap()
        var n = sourceHarm.coerceAtLeast(0)
        val blockRetaliations = mutableListOf<BattlePhysicalCallbackPlan.BlockRetaliation>()
        var mpShieldDamage = 0
        var moneyShieldSpent = 0
        var hpDamage = 0
        var lifeStealHealing = 0
        var qxlHealing = 0
        var playerMoneyDelta = 0
        var enemyMoneyDelta = 0

        if (n == 0) {
            target.skills[153]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                val harm = attacker.maxHitPoints * rate / 100
                attacker.addHpcur(-harm)
                attacker.statuses[BattleStatus.CONFUSION] = statusDuration(BattleStatus.CONFUSION, attacker)
                blockRetaliations += BattlePhysicalCallbackPlan.BlockRetaliation(
                    BattlePhysicalCallbackPlan.BlockRetaliationKind.MENG_JI_CONFUSION,
                    harm,
                )
            }
            target.skills[161]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                val harm = attacker.maxHitPoints * rate / 100
                attacker.addHpcur(-harm)
                attacker.statuses[BattleStatus.PARALYSIS] = statusDuration(BattleStatus.PARALYSIS, attacker)
                blockRetaliations += BattlePhysicalCallbackPlan.BlockRetaliation(
                    BattlePhysicalCallbackPlan.BlockRetaliationKind.NI_FAN_PARALYSIS,
                    harm,
                )
            }
            attacker.refStateAnime()
        } else {
            // `_attack3` records incoming statuses before its MPFY/HP branch.
            applyIncomingAttackStatuses(statuses, target)
            if (target.skills[2]?.and(255)?.let { it != 255 } == true && target.magicPoints > 0) {
                n = n.coerceIn(0, target.magicPoints)
                mpShieldDamage = n
                target.addMpcur(-n)
                // MPFY's break skips JQFY, HP, XXGJ, QXL and XSJQ.
            } else {
                target.skills[125]?.and(255)?.takeIf { it != 255 }?.let { costPerDamage ->
                    if (target.hitPoints >= costPerDamage) {
                        val price = kotlin.math.abs(n) * costPerDamage
                        val available = if (target.isPlayerSide()) playerMoney else enemyMoney
                        if (available >= price) {
                            if (target.isPlayerSide()) playerMoney -= price else enemyMoney -= price
                            moneyShieldSpent = price
                            n = 1
                        }
                    }
                }
                n = n.coerceIn(0, target.hitPoints)
                hpDamage = n
                target.addHpcur(-n)

                attacker.skills[238]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                    var resolvedRate = rate
                    if (!canAttack(attacker, target)) resolvedRate /= 2
                    var healing = resolvedRate * n / 100
                    val attackerIsMine = attacker.isPlayerSide()
                    val currentCampIsMine = activeFaction.isPlayerSide()
                    if (attackerIsMine != currentCampIsMine) healing = minOf(rate, healing)
                    lifeStealHealing = minOf(attacker.maxHitPoints - attacker.hitPoints, healing)
                    attacker.addHpcur(lifeStealHealing)
                }
                attacker.skills[298]?.and(255)?.takeIf { it != 255 }?.let {
                    qxlHealing = minOf(attacker.maxHitPoints - attacker.hitPoints, n)
                    attacker.addHpcur(qxlHealing)
                }
                attacker.skills[237]?.and(255)?.takeIf { it != 255 }?.let { effect ->
                    val amount = n * effect
                    if (amount >= 1) {
                        if (attacker.isPlayerSide()) {
                            playerMoney += amount
                            enemyMoney -= amount
                            playerMoneyDelta = amount
                            enemyMoneyDelta = -amount
                        } else {
                            playerMoney -= amount
                            enemyMoney += amount
                            playerMoneyDelta = -amount
                            enemyMoneyDelta = amount
                        }
                    }
                }
            }

            if (attacker.skills[221]?.and(255)?.let { it != 255 } == true) {
                backPosition(target, attacker)?.let { (x, y) ->
                    target.tileX = x
                    target.tileY = y
                }
            }
            accumulateChargeWhenHit(target, activeAttack)
        }

        // The hurt/guard callback is the first externally committed effect
        // for this target. ZDSY inventory/property callbacks must remain
        // behind it in DeferredAiMutation's staged list.
        notifyPhysicalDamage(attacker, target, n)

        val recoilDamage = target.skills[40]?.and(255)?.takeIf { it != 255 && n > 0 }
            ?.let { n * it / 100 }
            ?.takeIf { it >= 1 }
            ?: 0
        if (recoilDamage > 0) attacker.addHpcur(-recoilDamage, keepAlive = true)

        // Guard case 2 jumps directly to case 8 in `_attack3`; it never
        // enters the ZDSY cases 5..6 even if the defender was already hurt.
        var automaticPropertyId: Int? = null
        var automaticPropertyHpDelta = 0
        var automaticPropertyMpDelta = 0
        var automaticPropertyCallbackCount = 0
        val automaticProperty = if (n > 0) {
            target.skills[284]?.and(255)?.takeIf { itemId ->
                itemId != 255 && target.hitPoints > 0 && target.hitPoints < target.maxHitPoints
            }?.let { itemId ->
                automaticPropertyId = itemId
                val hpBeforeProperty = target.hitPoints
                val mpBeforeProperty = target.magicPoints
                if (target.faction == Faction.PLAYER && zdsyGlobalValue == 0) {
                    notifyConsumeAutomaticProperty(itemId)
                    automaticPropertyCallbackCount++
                }
                propertyItems[itemId]?.let { item -> applyProperty(item, target) { true } }.also {
                    automaticPropertyHpDelta = target.hitPoints - hpBeforeProperty
                    automaticPropertyMpDelta = target.magicPoints - mpBeforeProperty
                    if (it != null && propertyItems[itemId]?.itemType in setOf(42, 43)) automaticPropertyCallbackCount++
                }
            }
        } else {
            null
        }

        val defeated = target.hitPoints <= 0
        if (defeated) {
            removeUnit(target.id)
            notifyUnitDefeated(attacker, target)
        }
        val backMove = if (target.tileX != targetXBefore || target.tileY != targetYBefore) {
            PhysicalBackMove(targetXBefore, targetYBefore, target.tileX, target.tileY)
        } else null
        val localStatusSettlement = if (n > 0 &&
            (statuses.statuses.isNotEmpty() || statuses.downAttributes.isNotEmpty())
        ) {
            MagicLocalSettlement(listOf(MagicLocalSettlementEntry(
                targetId = target.id,
                statusesBefore = statusesBefore,
                statusesAfter = target.statuses.toMap(),
                attributeLiftsBefore = liftsBefore,
                attributeLiftsAfter = target.attributeLifts.toMap(),
                hasStatesPayload = true,
                attributeLiftRoundsBefore = liftRoundsBefore,
                attributeLiftRoundsAfter = target.attributeLiftRounds.toMap(),
            )))
        } else MagicLocalSettlement(emptyList())
        return PhysicalAttackTargetResult(
            targetId = target.id,
            sourceHarm = n,
            damage = hpDamage,
            mpShieldDamage = mpShieldDamage,
            moneyShieldSpent = moneyShieldSpent,
            lifeStealHealing = lifeStealHealing,
            qxlHealing = qxlHealing,
            recoilDamage = recoilDamage,
            blockRetaliations = blockRetaliations,
            playerMoneyDelta = playerMoneyDelta,
            enemyMoneyDelta = enemyMoneyDelta,
            automaticPropertyId = automaticPropertyId,
            automaticProperty = automaticProperty,
            automaticPropertyHpDelta = automaticPropertyHpDelta,
            automaticPropertyMpDelta = automaticPropertyMpDelta,
            automaticPropertyCallbackCount = automaticPropertyCallbackCount,
            backMove = backMove,
            localStatusSettlement = localStatusSettlement,
            hasLocalStatusSettlement = localStatusSettlement.entries.isNotEmpty(),
            defeated = defeated,
        )
    }

}

object BattleScenarioFactory {
    fun tutorialBattle(): BattleState = BattleState(
        units = listOf(
            BattleUnit("cao-cao", "조조", Faction.PLAYER, 3, 3),
            BattleUnit("guard", "병사", Faction.PLAYER, 2, 2),
            BattleUnit("yellow-turban", "황건적", Faction.ENEMY, 10, 5),
        ),
        events = listOf(
            BattleEvent("reinforcement-arrival", TurnTrigger(round = 2, faction = Faction.PLAYER)) { state ->
                state.addUnit(BattleUnit("reinforcement", "증원군", Faction.PLAYER, 1, 6))
            }
        )
    )

    fun fromScriptedUnits(
        units: Collection<ScenarioBattleUnit>,
        blockedTiles: Set<Pair<Int, Int>> = emptySet(),
        originalData: OriginalGameData? = null,
        terrain: BattleTerrainGrid? = null,
        enemyMasterInstanceId: Int = -1,
        initialWeather: BattleWeather = BattleWeather.CLEAR,
        weatherSchedule: List<BattleWeather> = emptyList(),
        weatherOffset: Int = 0,
        enemyEquipment: Map<Int, List<Int>> = emptyMap(),
        campaign: CampaignState? = null,
        sourceRandomStreams: SourceRandomStreams? = null,
        /** Production Config enables ZDBHSW; focused callers may override. */
        enabledFeatures: Int = 32,
    ): BattleState {
        val scriptedByBattleId = units.associateBy { it.battleId }
        fun projectUnit(unit: ScenarioBattleUnit, forcedLevel: Int? = null, forcedPosts: Int? = null): BattleUnit {
            val persistentAttributes = campaign?.unitAttributes?.get(unit.characterId).orEmpty()
            // Scenario unit levels are zero-based script values, whereas the
            // persisted Unit.LV attribute is the displayed one-based level.
            val requestedLevel = forcedLevel?.minus(1)?.coerceAtLeast(0)
                ?: persistentAttributes[18]?.minus(1)?.coerceAtLeast(0)
                ?: unit.level
            val battleProfile = originalData?.battleProfile(
                unit.characterId,
                requestedLevel,
                forcedPosts ?: persistentAttributes[17],
            )
            val equippedValues = if (unit.faction == ScenarioUnitFaction.MINE) {
                // setEnemyEquip is a legacy Stage API name; source scripts
                // also use it for named friendly/player actors. Campaign hall
                // equipment overrides that scenario fallback when present.
                campaign?.equipment?.get(unit.characterId)?.asScriptValues()
                    ?: enemyEquipment[unit.characterId].orEmpty()
            } else enemyEquipment[unit.characterId].orEmpty()
            val defaultEquipmentValues = originalData
                ?.defaultEquipment(battleProfile?.posts ?: 0, battleProfile?.level ?: 1)
                ?.asScriptValues().orEmpty()
            val effectiveEquipmentValues = listOf(
                if (equippedValues.getOrElse(0) { 0 } > 1) equippedValues[0] else defaultEquipmentValues.getOrElse(0) { 1 },
                if (equippedValues.getOrElse(0) { 0 } > 1) equippedValues.getOrElse(1) { 0 } else defaultEquipmentValues.getOrElse(1) { 1 },
                if (equippedValues.getOrElse(2) { 0 } > 1) equippedValues[2] else defaultEquipmentValues.getOrElse(2) { 1 },
                if (equippedValues.getOrElse(2) { 0 } > 1) equippedValues.getOrElse(3) { 0 } else defaultEquipmentValues.getOrElse(3) { 1 },
                if (equippedValues.getOrElse(4) { 0 } > 1) equippedValues[4] else defaultEquipmentValues.getOrElse(4) { 1 },
            )
            // Battle units equip their post's default weapon and armor first;
            // setEnemyEquip then replaces only explicitly supplied slots.
            // A five-value call containing only an accessory (R_00 unit 146)
            // must therefore retain both default stat bonuses.
            val equipment = originalData?.equipmentBonus(effectiveEquipmentValues, battleProfile?.level ?: 1)
            val profile = battleProfile?.unit
            val arm = battleProfile?.arm
            val resolvedSkills = originalData?.mergeSkills(
                originalData.skillsForUnit(unit.characterId, battleProfile?.posts ?: 0, campaign),
                originalData.equipmentSkills(effectiveEquipmentValues, battleProfile?.level ?: 1),
            ).orEmpty()
            fun passive(base: Int, skillId: Int): Int = originalData?.passiveAbility(base, skillId, resolvedSkills) ?: base
            fun divineFloor(base: Int, sourceBase: Int): Int {
                val growth = resolvedSkills[190]?.and(255)?.takeIf { it != 255 } ?: return base
                // Unit._baseBility: original raw ability + SMFT × level is a
                // lower bound before the ordinary passive ability modifiers.
                return maxOf(base, sourceBase + growth * (battleProfile?.level ?: 1))
            }
            fun ability(base: Int, sourceBase: Int, passiveSkill: Int): Int = passive(divineFloor(base, sourceBase), passiveSkill)
            val baseMaxHitPoints = persistentAttributes[9] ?: battleProfile?.maxHitPoints ?: 100
            val baseMaxMagicPoints = persistentAttributes[10] ?: battleProfile?.maxMagicPoints ?: 0
            // Unit.hitarea(): YJGJ replaces the post's normal hit-area ID.
            // The source can subsequently upgrade that pattern through
            // YJGJ_GJ; direct table patterns already cover the base override.
            val rangeSkill = resolvedSkills[258]?.and(255) ?: 255
            val attackHitArea = rangeSkill.takeIf { it != 255 }
                ?.let { originalData?.hitAreaProfile(it) }
                ?: battleProfile?.hitArea
            val upgradedAttackHitArea = if ((resolvedSkills[260]?.and(255) ?: 255) != rangeSkill) {
                attackHitArea?.upgradeId?.let { originalData?.hitAreaProfile(it) } ?: attackHitArea
            } else attackHitArea
            val learnedMagic = buildList {
                addAll(battleProfile?.magic.orEmpty())
                campaign?.extraMagic?.values
                    ?.filter { it.unitId == unit.characterId && it.learnLevel <= (battleProfile?.level ?: 1) }
                    ?.mapNotNull { learned -> originalData?.magicProfile(learned.magicId) }
                    ?.forEach { magic -> if (none { it.id == magic.id }) add(magic) }
                // Unit.magics(): XHCL grants original strategy families by
                // bit flag, in addition to post/character learned tactics.
                val xhcl = resolvedSkills[244]?.and(255) ?: 255
                if (xhcl != 255) originalData?.allMagicProfiles()
                    ?.filter { magic ->
                        (xhcl and 1 != 0 && magic.type in 0..3) ||
                            (xhcl and 2 != 0 && magic.type in 7..10) ||
                            (xhcl and 4 != 0 && (magic.type in 7..10 || magic.type in 15..18)) ||
                            (xhcl and 8 != 0 && magic.type == 19) ||
                            (xhcl and 16 != 0 && (magic.type in 11..14 || magic.type == 27)) ||
                            (xhcl and 32 != 0 && magic.type == 23) ||
                            (xhcl and 64 != 0 && magic.type == 24) ||
                            (xhcl and 128 != 0 && magic.type == 25)
                    }
                    ?.forEach { magic -> if (none { it.id == magic.id }) add(magic) }
            }.map { magic ->
                // BattleUnit.magicHitArea(): YJGJ_CL upgrades each strategy's
                // cast range through the same original hit-area table.
                if ((resolvedSkills[259]?.and(255) ?: 255) != 255) {
                    val upgraded = magic.hitArea.upgradeId.let { originalData?.hitAreaProfile(it) }
                    if (upgraded != null) magic.copy(hitArea = upgraded) else magic
                } else magic
            }.map { magic ->
                // BattleUnit.magicEffarea(): this is independent from the
                // cast-range upgrade above and expands affected neighbours.
                if ((resolvedSkills[264]?.and(255) ?: 255) != 255) {
                    originalData?.upgradedEffectArea(magic.effectAreaId)?.let { (id, offsets) ->
                        magic.copy(effectAreaId = id, effectOffsets = offsets)
                    } ?: magic
                } else magic
            }
            return BattleUnit(
                id = unit.battleId,
                name = campaign?.unitNames?.get(unit.characterId) ?: profile?.name ?: "유닛 ${unit.characterId}",
                faction = when (unit.faction) {
                    ScenarioUnitFaction.MINE -> Faction.PLAYER
                    ScenarioUnitFaction.FRIEND -> Faction.FRIEND
                    ScenarioUnitFaction.ENEMY -> if (unit.reinforcement) Faction.REINFORCEMENTS else Faction.ENEMY
                },
                tileX = unit.x,
                tileY = unit.y,
                visible = !unit.hidden,
                direction = unit.direction,
                sourceCharacterId = unit.characterId,
                sourceBattleSlot = unit.sourceBattleSlot,
                famous = profile?.famous == true,
                sourceTileXAuthored = unit.authoredX,
                sourceTileYAuthored = unit.authoredY,
                hitPoints = passive(baseMaxHitPoints, 52),
                maxHitPoints = passive(baseMaxHitPoints, 52),
                magicPoints = passive(baseMaxMagicPoints, 53),
                maxMagicPoints = passive(baseMaxMagicPoints, 53),
                level = battleProfile?.level ?: 1,
                experience = if (unit.faction == ScenarioUnitFaction.MINE) persistentAttributes[19] ?: 0 else 0,
                posts = battleProfile?.posts ?: profile?.posts ?: 0,
                // Unit._baseBility adds equipped item values after reading
                // the persisted base ability.  Keep the addition outside the
                // Elvis expression; otherwise every campaign-backed unit
                // silently loses its weapon/armor bonus.
                attack = ability((persistentAttributes[2] ?: (battleProfile?.attack ?: 45)) + (equipment?.attack ?: 0), profile?.attack ?: 45, 65),
                defense = ability((persistentAttributes[3] ?: (battleProfile?.defense ?: 25)) + (equipment?.defense ?: 0), profile?.defense ?: 25, 61),
                spirit = ability((persistentAttributes[4] ?: (battleProfile?.spirit ?: 35)) + (equipment?.spirit ?: 0), profile?.spirit ?: 35, 68),
                critical = ability(persistentAttributes[5] ?: battleProfile?.critical ?: 35, profile?.critical ?: 35, 54),
                morale = ability(persistentAttributes[6] ?: battleProfile?.morale ?: 35, profile?.morale ?: 35, 73),
                martial = profile?.attack ?: battleProfile?.attack ?: 45,
                armId = arm?.id ?: 0,
                armType = arm?.type ?: 0,
                remoteAttack = arm?.remote ?: false,
                armMoveSound = arm?.moveSound ?: 0,
                fastMove = arm?.fastMove ?: true,
                attackDelay = arm?.attackDelay ?: false,
                armRestraints = buildMap { (0 until 40).forEach { targetArm -> put(targetArm, arm?.restraintAgainst(targetArm) ?: 100) } },
                terrainImpacts = buildMap { (0 until 30).forEach { terrainId -> put(terrainId, arm?.terrainImpact(terrainId) ?: 100) } },
                terrainMovementCosts = buildMap { (0 until 30).forEach { terrainId -> put(terrainId, arm?.terrainMoveCost(terrainId) ?: 1) } },
                magicHarmRate = arm?.magicHarmRate ?: 100,
                attackOffsets = upgradedAttackHitArea?.offsets ?: setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
                // Unit.effarea(): ZHUORE(0), or the CTGJ skill's effect
                // area ID.  This is deliberately separate from hit-area.
                attackEffectOffsets = originalData?.effectAreaOffsets(resolvedSkills[32]?.and(255)?.takeIf { it != 255 } ?: 0).orEmpty(),
                attackEffectAreaId = resolvedSkills[32]?.and(255)?.takeIf { it != 255 } ?: 0,
                attackAllScreen = upgradedAttackHitArea?.allScreen ?: false,
                magic = learnedMagic,
                skills = resolvedSkills,
                movement = (battleProfile?.movement ?: 3) + (resolvedSkills[77]?.and(255)?.takeIf { it != 255 } ?: 0),
                ai = unit.ai,
                aiTargetCharacterId = unit.aiTargetId,
                aiTargetX = unit.aiTargetX,
                aiTargetY = unit.aiTargetY,
                retireMessage = unit.characterId.let { originalData?.retreatText(it) },
                criticalSpeech = profile?.criticalSpeech
                    ?: OriginalGameData.CriticalSpeechProfile(emptyList(), randomized = false),
                deathMessageEnabled = unit.deathMessageEnabled,
                retreatCount = persistentAttributes[15] ?: 0,
            )
        }
        return BattleState(
        units = units.map { projectUnit(it) },
        events = emptyList(),
        blockedTiles = blockedTiles,
        terrain = terrain,
        // BattleLayer.enemyMasterId() first resolves this authored value via
        // `_unitIds[characterId]`; it is not an enemy `i`/slot index.
        enemyMasterUnitId = units.firstOrNull {
            it.faction == ScenarioUnitFaction.ENEMY && it.characterId == enemyMasterInstanceId
        }?.battleId,
        initialWeather = initialWeather,
        weatherSchedule = weatherSchedule,
        weatherOffset = weatherOffset,
        terrainMagicFlags = originalData?.let { data -> buildMap {
            (0..64).forEach { terrainId -> data.terrainMagicFlag(terrainId).takeIf { it != 0 }?.let { put(terrainId, it) } }
        } }.orEmpty(),
        terrainResumeRates = originalData?.let { data -> buildMap {
            (0..64).forEach { terrainId -> data.terrainResumeHp(terrainId).takeIf { it != 0 }?.let { put(terrainId, it) } }
        } }.orEmpty(),
        terrainResumeMp = originalData?.let { data -> buildMap {
            (0..64).forEach { terrainId -> data.terrainResumeMp(terrainId).takeIf { it != 0 }?.let { put(terrainId, it) } }
        } }.orEmpty(),
        enabledFeatures = enabledFeatures,
        statusRoundFor = { status -> originalData?.statusRound(status) ?: 3 },
        attributeStatusRoundFor = { attribute -> originalData?.attributeStatusRound(attribute) ?: 3 },
        movementOffsets = originalData?.hitAreaProfile(0)?.offsets ?: setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
        directDestinationOffsets = originalData?.hitAreaProfile(13)?.offsets?.toList().orEmpty(),
        infantryOffsets = originalData?.hitAreaProfile(1)?.offsets ?: setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
        // _attack3 ZDSY passes an item ID directly to _usePro2.  Its enemy
        // branch has no ItemStore lookup, so all usable original items—not
        // merely the player's current inventory—must be available here.
        propertyItems = originalData?.battlePropertyItems().orEmpty()
            .map { BattlePropertyItem(it.id, it.name, it.itemType, it.value) }
            .associateBy { it.id },
        consumeProperty = campaign?.let { state -> { itemId: Int -> state.consumeItem(itemId) } } ?: { false },
        zdsyGlobalValue = (campaign?.globalVariables?.get(4035) as? Number)?.toInt() ?: 0,
        consumeAutomaticProperty = campaign?.let { state -> { itemId: Int -> state.consumeItem(itemId); Unit } } ?: {},
        onPermanentProperty = campaign?.let { state -> { item: BattlePropertyItem, target: BattleUnit ->
            target.sourceCharacterId?.let { characterId -> when (item.itemType) {
                42 -> state.setUnitAttribute(characterId, 9, target.maxHitPoints)
                43 -> state.setUnitAttribute(characterId, 10, target.maxMagicPoints)
            }
            }
        } } ?: { _, _ -> },
        // Unit EXP is settled for every resolved physical/magic target,
        // including non-lethal and zero-harm guard records (but not true
        // misses, which never enter source `_attack3`). Keep defeat notification independent
        // so a kill cannot grant the same reward twice.
        onUnitDefeated = { _, _ -> },
        onBattleExperience = if (campaign != null && originalData != null) experience@{ winner, amount ->
            if (winner.baseFaction != Faction.PLAYER) return@experience null
            val characterId = winner.sourceCharacterId ?: return@experience null
            val oldLevel = winner.level
            val result = campaign.grantExperience(characterId, oldLevel, amount, originalData)
            if (result.leveledUp) {
                // Unit.setLevel's normal path incrementally persists ATT..MP
                // before rebuilding BattleUnit's derived caches.
                val growth = originalData.unitLevelGrowth(characterId, winner.posts, campaign)
                val defaults = originalData.unitLevelDerivedAttributes(
                    characterId, winner.posts, oldLevel, mine = true, campaign = campaign,
                )
                growth.forEach { (attribute, perLevel) ->
                    val current = campaign.unitAttribute(characterId, attribute, defaults.getValue(attribute))
                    campaign.setUnitAttribute(characterId, attribute, current + perLevel * (result.level - oldLevel))
                }
            }
            result
        } else { _, _ -> null },
        experienceLimit = { level -> originalData?.unitExperienceLimit(level) ?: 100 },
        levelLimit = originalData?.unitLevelLimit() ?: 50,
        onBattleLevelUp = refresh@{ live ->
            val scripted = scriptedByBattleId[live.id] ?: return@refresh
            live.refreshLevelDerivedState(projectUnit(scripted, forcedLevel = live.level, forcedPosts = live.posts))
        },
        onUnitRetreat = campaign?.let { state -> { unit: BattleUnit ->
            unit.sourceCharacterId?.let { state.setUnitAttribute(it, 15, unit.retreatCount) }
        } } ?: {},
        onEquipmentExperienceAward = if (campaign != null && originalData != null) { recipient, _, amount, kind ->
            recipient.sourceCharacterId
                ?.takeIf { recipient.baseFaction.isPlayerSide() }
                ?.let { id ->
                    campaign.grantEquipmentExperienceAmount(
                        id,
                        amount,
                        if (kind == BattleEquipmentExperienceKind.WEAPON) CampaignState.EquipmentSlot.WEAPON else CampaignState.EquipmentSlot.ARMOR,
                        originalData,
                    )
                }
                ?.let(::listOf)
                ?: emptyList()
        } else null,
        onRestoreUnitExperience = if (campaign != null && originalData != null) { unit, amount ->
            unit.sourceCharacterId?.let { id ->
                val beforeMagic = originalData.learnedMagicIds(unit.posts, unit.level).toSet()
                val result = campaign.grantExperience(id, unit.level, amount, originalData)
                RestoreGrowthResolution.Applied(result.copy(
                    learnedMagicIds = originalData.learnedMagicIds(unit.posts, result.level).filterNot(beforeMagic::contains),
                ))
            } ?: RestoreGrowthResolution.Unavailable
        } else { _, _ -> RestoreGrowthResolution.Unavailable },
        onRestoreEquipmentExperience = if (campaign != null && originalData != null) { unit, amount, slot ->
            unit.sourceCharacterId?.let { id ->
                campaign.grantEquipmentExperienceAmount(id, amount, slot, originalData)
                    ?.let { RestoreGrowthResolution.Applied(it) }
                    ?: RestoreGrowthResolution.NotApplicable
            } ?: RestoreGrowthResolution.Unavailable
        } else { _, _, _ -> RestoreGrowthResolution.Unavailable },
        sourceRandomStreams = sourceRandomStreams,
    ).also { it.initializeAllSourceRates() }
    }
}

private fun BattleStatus.label(): String = when (this) {
    BattleStatus.PARALYSIS -> "마비"
    BattleStatus.SILENCE -> "금주"
    BattleStatus.CONFUSION -> "혼란"
    BattleStatus.POISON -> "중독"
    BattleStatus.LOST -> "길 잃음"
}

private fun BattleAttribute.label(): String = when (this) {
    BattleAttribute.ATTACK -> "공격력"
    BattleAttribute.DEFENSE -> "방어력"
    BattleAttribute.SPIRIT -> "정신력"
    BattleAttribute.CRITICAL -> "폭발력"
    BattleAttribute.MORALE -> "사기"
    BattleAttribute.MOVEMENT -> "이동력"
}

/** Transitional source-name migration for callers not yet moved to `Battle`. */
typealias BattleState = Battle
