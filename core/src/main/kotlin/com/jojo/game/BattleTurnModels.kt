package com.jojo.game
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.*

/**
 * data class  `TurnTrigger`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class TurnTrigger(val round: Int, val faction: Faction)

/**
 * class  `BattleEvent`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleEvent(
    val id: String,
    val trigger: TurnTrigger,
    private val action: (Battle) -> Unit,
) {
    /**
     * 공개 메서드 `matches`
     *
     * ### 파라미터
    - `state` (`Battle`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun matches(state: Battle): Boolean = state.round >= trigger.round && state.activeFaction == trigger.faction

    /**
     * 공개 메서드 `execute`
     *
     * ### 파라미터
    - `state` (`Battle`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun execute(state: Battle) = action(state)
}

/**
 * data class  `TurnResult`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class TurnResult(val round: Int, val activeFaction: Faction, val firedEvents: List<String>)

/**
 * enum class  `CampSettlementStage`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class CampSettlementStage { START_STATE, END_RESTORE }

/**
 * data class  `BattleUnitTurnChange`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
 * data class  `CampSettlement`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
    /**
     * data class  `LocalAura`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * data class  `Growth`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Growth(
        val unitId: String,
        val grants: List<SettlementGrowthGrant>,
    ) : SettlementSubflow
}

/**
 * enum class  `SettlementGrowthKind`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class SettlementGrowthKind { UNIT_EXP, WEAPON_EXP, ARMOR_EXP }

/** The two independently max-merged equipment EXP entries in g_charinfo. */
enum class BattleEquipmentExperienceKind { WEAPON, ARMOR }

/**
 * data class  `SettlementGrowthGrant`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class SettlementGrowthGrant(
    val kind: SettlementGrowthKind,
    /** Skill value passed to Unit.unitAddExp/equipAddExp. */
    val requestedAmount: Int,
    val unitResult: CampaignExperienceResult? = null,
    val equipmentResult: CampaignEquipmentExperienceResult? = null,
) {
    val requiresLevelUpPresentation: Boolean
        get() =
            unitResult?.leveledUp == true || equipmentResult?.leveledUp == true
    val requiresItemUpgradeCallback: Boolean get() = equipmentResult?.leveledUp == true
}

sealed interface RestoreGrowthResolution<out T> {
    data object NotApplicable : RestoreGrowthResolution<Nothing>
    data object Unavailable : RestoreGrowthResolution<Nothing>

    /**
     * data class  `Applied`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Applied<T>(val value: T) : RestoreGrowthResolution<T>
}

/**
 * data class  `RoundAdvance`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class RoundAdvance(val completedRound: Int, val round: Int)

/**
 * data class  `WeatherTransition`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class WeatherTransition(val previous: BattleWeather, val current: BattleWeather) {
    val changed: Boolean get() = previous != current
}

/**
 * data class  `AiTurnResult`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
/**
 * data class  `AiPlannerTrace`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class AiPlannerTrace(
    val characterId: Int,
    val ai: Int,
    val x: Int,
    val y: Int,
    val value: Int?,
    val actionValue: Int?,
    val targetId: String?,
    val magicId: Int?,
)

/**
 * enum class  `BattleOutcome`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class BattleOutcome { PLAYER_VICTORY, ENEMY_VICTORY }
