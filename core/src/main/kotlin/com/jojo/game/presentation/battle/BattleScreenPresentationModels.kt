package com.jojo.game.presentation.battle

import com.jojo.game.domain.battle.*
import com.jojo.game.presentation.battle.render.*
import com.jojo.game.domain.battle.BattleUnitMoveTimeline

import com.jojo.game.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.settlement.*
import com.jojo.game.domain.scenario.*
import com.jojo.game.presentation.battle.timeline.*

/** Headless companion to BattleScreen's recovered showWinCondition/action methods. */
data class BattleScreenIsolatedUnit(val control: Boolean, val exist: Boolean, val acted: Boolean)


data class BattleScreenIsolatedView(
    val paused: Boolean,
    val modal: Boolean,
    val action: Boolean,
    val events: List<String>
)


class BattleScreenIsolatedContract(
    private val units: List<BattleScreenIsolatedUnit>,
    private val collocation: Boolean,
    private val round: Int
) {
    private var paused = false
    private var modal = false
    private val pending = mutableListOf<String>()


    fun showWinCondition(text: String) {
        paused = true
        modal = true
        pending += "pause"
        pending += "layer:WinConditionsLayer:$text:$round"
    }


    fun cancel(event: Int) {
        if (event == WinConditionsLayer.TOUCH_END && modal) {
            modal = false
            paused = false
            pending += "resume"
        }
    }


    fun nextNotOperUnit(camp: Int) = !collocation && camp == 0 && units.any { it.control && it.exist && !it.acted }


    fun view() = BattleScreenIsolatedView(paused, modal, nextNotOperUnit(0), pending.toList().also { pending.clear() })
}

internal enum class UnitAnimationKind { ATTACK, SPECIAL, HIT, DEATH }


enum class UnitSpriteSource { MOVEMENT, ATTACK, SPECIAL }

internal data class UnitSpriteFrame(
    val source: UnitSpriteSource,
    val sourceY: Int,
    val sourceWidth: Int = 48,
    val sourceHeight: Int = 48,
    val flipX: Boolean = false,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
)

internal data class ScriptedUnitVisual(val action: Int, val startedAt: Float)

internal data class CaptureActionSample(val action: Int, val sample: Float)

internal data class UnitActionAnimation(
    val unitId: String,
    val kind: UnitAnimationKind,
    val direction: Int,
    val startedAt: Float,
    val endsAt: Float,
    /** Source BRAnime action number. */
    val sourceAction: Int = 6,
)

internal data class UnitMoveAnimation(
    val unitId: String,
    val path: List<Pair<Int, Int>>,
    val timeline: BattleUnitMoveTimeline.Timeline,
    val startedAt: Float,
    val cameraTickCursor: MovementCameraTickCursor = MovementCameraTickCursor(),
) {
    val endsAt: Float get() = startedAt + timeline.idleAt
}

internal data class MagicEffectAnimation(
    val effectId: Int,
    val targetIds: List<String>,
    val startedAt: Float,
    val endsAt: Float,
    var soundPlayed: Boolean = false,
)

internal data class BackMoveAnimation(
    val unitId: String,
    val move: PhysicalBackMove,
    val startedAt: Float,
    val endsAt: Float,
)

internal data class HarmNumberAnimation(val amount: Int, val isHp: Boolean, val startedAt: Float, val endsAt: Float)

internal data class TimedBattleMutation(val at: Float, val mutation: () -> Unit)

/**
 * The two facing-direction callbacks that BattleScreen adds around a victim's
 * hit reaction.  Kept separate so the source-action distinction can be
 * exercised without constructing LibGDX's full battle scene.
 */
internal object BattleScreenLoseCondition {

    fun defeated(units: Collection<BattleUnit>, mineMasterBattleId: String?): Boolean {
        mineMasterBattleId?.let { masterId ->
            units.firstOrNull { it.id == masterId }?.let { return it.hitPoints < 1 }
        }
        return units.none { it.faction == Faction.PLAYER && it.hitPoints > 0 }
    }
}

internal object BattleScreenHitReactionDirectionScheduler {
    fun schedule(
        sourceAction: Int,
        reactionDirection: Int,
        previousDirection: Int?,
        startsAt: Float,
        endsAt: Float,
        schedule: (Float, () -> Unit) -> Unit,
        isCurrentReaction: () -> Boolean,
        setDirection: (Int) -> Unit,
    ) {
        schedule(startsAt) { setDirection(reactionDirection) }
        // `_attack3`'s normal-hit callback restores the saved h after
        // anime32.  The blocked anime26 branch runs defaultAction(-1), so it
        // deliberately leaves its countDir(target) facing in place.
        if (sourceAction != 26 && previousDirection != null) {
            schedule(endsAt) {
                if (isCurrentReaction()) setDirection(previousDirection)
            }
        }
    }
}

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

internal data class ActiveCounterMagicPresentation(val unitIds: Set<String>, val endsAt: Float)

internal data class BattleCharacterRouteSample(
    val unit: BattleUnit,
    val state: BattleCharacterPresentation,
    val unitLeft: Float,
    val unitBottom: Float,
    val frameTime: Float,
    val assetFrameId: String,
    val avatarWidth: Float,
    val avatarHeight: Float,
    val avatarOffsetX: Float,
    val avatarOffsetY: Float,
    val harmRect: FloatArray?,
    /** Direction of the actually playing Cocos clip, which can lag setDirFast. */
    val frameDirection: Int,
)

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

internal data class CounterFollowUpPresentation(
    val attackerId: String,
    val targetId: String,
    val harm: Int,
    val targetHpBefore: Int,
    val critical: Boolean,
    val mpShieldDamage: Int,
    val startsAt: Float,
)

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

internal data class PendingDeathAnimation(
    val unitId: String,
    val direction: Int,
    val sourceAction: Int,
    val duration: Float,
    val showRetireMessage: Boolean,
)

internal enum class TurnDeathStage { NONE, PRE_SCRIPT, HIDING, POST_SCRIPT }

internal sealed interface TurnSettlementOp {

    data class Focus(val unitId: String, val seconds: Float, val forceCenter: Boolean) : TurnSettlementOp


    data class Sound(val soundIndex: Int) : TurnSettlementOp


    data class Info2(val text: String) : TurnSettlementOp


    data class Actions(val unitId: String, val actionIds: List<Int>) : TurnSettlementOp


    data class UnitInfo(val plan: SettlementUnitPlan) : TurnSettlementOp


    data class GrowthInfo(val unitId: String, val grants: List<SettlementGrowthGrant>) : TurnSettlementOp


    data class Meff(val effectId: Int, val targetIds: List<String>) : TurnSettlementOp


    data class ItemUpgrade(val unitId: String, val result: CampaignEquipmentExperienceResult) : TurnSettlementOp


    data class HideState(val unitIds: List<String>) : TurnSettlementOp


    data class Refresh(val unitIds: List<String>) : TurnSettlementOp


    data class Default(val unitId: String) : TurnSettlementOp
}

internal data class ActiveUnitDeath(
    val pending: PendingDeathAnimation,
    val endsAt: Float,
    val originalHp: Int,
)
