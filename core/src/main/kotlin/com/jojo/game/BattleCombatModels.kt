package com.jojo.game

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
        /** BattleScreen._attack6 default physical counterattack. */
        val counterDamage: Int = 0,
        val attackerDefeated: Boolean = false,
        /** Original XXGJ: HP restored from the inflicted physical damage. */
        val lifeStealHealing: Int = 0,
        /** Second hit from BattleScreen._attack2's continuous-attack loop. */
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
        /** BattleScreen._attack3 MPFY: this MP loss replaces, rather than accompanies, HP loss. */
        val mpShieldDamage: Int = 0,
        /** BattleScreen._attack3 QXL: direct recovery from its final `n` harm. */
        val qxlHealing: Int = 0,
        /** BattleScreen._attack3 FTSH post-reaction, non-lethal recoil. */
        val recoilDamage: Int = 0,
        /** Block-only MENG_JI/NI_FAN retaliation before ordinary counterattack. */
        val blockRetaliationDamage: Int = 0,
        /** JQFY's actual money expenditure while changing the hit to one HP. */
        val moneyShieldSpent: Int = 0,
        /** XSJQ delta applied to Game.money() for this primary hit. */
        val playerMoneyDelta: Int = 0,
        /** XSJQ delta applied to BattleScreen.ENEMY_MONEY for this primary hit. */
        val enemyMoneyDelta: Int = 0,
        /** BattleScreen._attack6 CLFJ strategy counter, when it supersedes physical counterattack. */
        val counterMagic: Magic? = null,
        /** Exact CLFJ skill value used to resolve [counterMagic]. */
        val counterMagicId: Int? = null,
        /** BattleScreen._attack3 ZDSY's self-targeted automatic property use. */
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

/** Original property item data used by BattleScreen._usePro2. */
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

/** Direct physical attack passes; surround and siege actions use separate resolution flows. */
enum class PhysicalAttackPassKind { ACTIVE, ACTIVE_FOLLOW_UP, COUNTER, COUNTER_FOLLOW_UP }

/** One resolved physical target, retaining effects that aggregate fields cannot order. */
data class PhysicalAttackTargetResult(
    val targetId: String,
    /** Final resolved harm used by the harm number, reflection, and equipment experience. */
    val resolvedHarm: Int,
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
