package com.jojo.game.presentation.battle

import com.jojo.game.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.settlement.*
import com.jojo.game.domain.scenario.*
import com.jojo.game.presentation.battle.timeline.*

/** Headless companion to BattleScreen's recovered showWinCondition/action methods. */
data class BattleScreenIsolatedUnit(val control: Boolean, val exist: Boolean, val acted: Boolean)

/**
 * data class  `BattleScreenIsolatedView`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class BattleScreenIsolatedView(
    val paused: Boolean,
    val modal: Boolean,
    val action: Boolean,
    val events: List<String>
)

/**
 * class  `BattleScreenIsolatedContract`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleScreenIsolatedContract(
    private val units: List<BattleScreenIsolatedUnit>,
    private val collocation: Boolean,
    private val round: Int
) {
    private var paused = false
    private var modal = false
    private val pending = mutableListOf<String>()

    /**
     * 공개 메서드 `showWinCondition`
     *
     * ### 파라미터
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun showWinCondition(text: String) {
        paused = true
        modal = true
        pending += "pause"
        pending += "layer:WinConditionsLayer:$text:$round"
    }

    /**
     * 공개 메서드 `cancel`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun cancel(event: Int) {
        if (event == WinConditionsLayer.TOUCH_END && modal) {
            modal = false
            paused = false
            pending += "resume"
        }
    }

    /**
     * 공개 메서드 `nextNotOperUnit`
     *
     * ### 파라미터
    - `camp` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun nextNotOperUnit(camp: Int) = !collocation && camp == 0 && units.any { it.control && it.exist && !it.acted }

    /**
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view() = BattleScreenIsolatedView(paused, modal, nextNotOperUnit(0), pending.toList().also { pending.clear() })
}

internal enum class UnitAnimationKind { ATTACK, SPECIAL, HIT, DEATH }

/**
 * enum class  `UnitSpriteSource`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
    /**
     * 공개 메서드 `defeated`
     *
     * ### 파라미터
    - `units` (`Collection<BattleUnit>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `mineMasterBattleId` (`String?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
    /**
     * data class  `Focus`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Focus(val unitId: String, val seconds: Float, val forceCenter: Boolean) : TurnSettlementOp

    /**
     * data class  `Sound`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Sound(val soundIndex: Int) : TurnSettlementOp

    /**
     * data class  `Info2`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Info2(val text: String) : TurnSettlementOp

    /**
     * data class  `Actions`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Actions(val unitId: String, val actionIds: List<Int>) : TurnSettlementOp

    /**
     * data class  `UnitInfo`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class UnitInfo(val plan: SettlementUnitPlan) : TurnSettlementOp

    /**
     * data class  `GrowthInfo`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class GrowthInfo(val unitId: String, val grants: List<SettlementGrowthGrant>) : TurnSettlementOp

    /**
     * data class  `Meff`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Meff(val effectId: Int, val targetIds: List<String>) : TurnSettlementOp

    /**
     * data class  `ItemUpgrade`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class ItemUpgrade(val unitId: String, val result: CampaignEquipmentExperienceResult) : TurnSettlementOp

    /**
     * data class  `HideState`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class HideState(val unitIds: List<String>) : TurnSettlementOp

    /**
     * data class  `Refresh`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Refresh(val unitIds: List<String>) : TurnSettlementOp

    /**
     * data class  `Default`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Default(val unitId: String) : TurnSettlementOp
}

internal data class ActiveUnitDeath(
    val pending: PendingDeathAnimation,
    val endsAt: Float,
    val originalHp: Int,
)

internal data class ActiveScriptedHide(
    val request: ScenarioUnitHideRequest,
    val battleUnitId: String,
    val endsAt: Float,
    val originalHp: Int,
)

internal data class PendingScriptedHide(
    val request: ScenarioUnitHideRequest,
    val battleUnitId: String,
)

internal data class ActiveScriptedShow(
    val request: ScenarioUnitShowRequest,
    val battleUnitId: String,
    val endsAt: Float,
)

internal data class ActiveScriptedUnitPosts(
    val request: ScenarioUnitPostsRequest,
    val battleUnitId: String,
)

internal data class ActiveMapPresentation(val request: ScenarioMapPresentationRequest, val endsAt: Float)

internal data class ActiveScriptedUnitAction(
    val request: ScriptedUnitAction,
    val battleUnitId: String,
    val endsAt: Float,
)
