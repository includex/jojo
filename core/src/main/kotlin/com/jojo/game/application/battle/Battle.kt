// Battle
package com.jojo.game.application.battle
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*

import com.jojo.game.*
import com.jojo.game.domain.battle.BattleSkillTemp
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.settlement.*
import com.jojo.game.domain.battle.BattleAiScorer
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.application.runtime.BattleTraceRandomStreams

import java.util.*


/** Battle: 렌더링과 분리된 전술 전투 조정기로, 유닛 상태와 전투 규칙을 결정론적으로 처리한다. */
class Battle(
    units: List<BattleUnit>,
    events: List<BattleEvent>,
    blockedTiles: Set<Pair<Int, Int>> = emptySet(),
    terrain: BattleTerrainGrid? = null,
    enemyMasterUnitId: String? = null,
    initialWeather: BattleWeather = BattleWeather.CLEAR,
    weatherSchedule: List<BattleWeather> = emptyList(),
    weatherOffset: Int = 0,
    terrainMagicFlags: Map<Int, Int> = emptyMap(),
    /** terrainResumeRates: 지형마다 전투 유닛의 체력을 회복하는 비율이다. */
    terrainResumeRates: Map<Int, Int> = emptyMap(),
    /** terrainResumeMp: 지형마다 전투 유닛의 기력을 회복하는 양이다. */
    terrainResumeMp: Map<Int, Int> = emptyMap(),
    /** enabledFeatures: 시나리오가 활성화한 전투 기능을 비트 마스크로 나타낸다. */
    enabledFeatures: Int = 0,
    /** skillTempResetTypes: 스킬별 임시값을 초기화하는 라운드 규칙이다. */
    skillTempResetTypes: Map<Int, BattleSkillTemp.ResetType> = emptyMap(),
    /** statusRoundFor: 상태 이상별 기본 지속 라운드를 반환하는 규칙이다. */
    statusRoundFor: (BattleStatus) -> Int = { 3 },
    /** attributeStatusRoundFor: 능력치 증감별 기본 지속 라운드를 반환하는 규칙이다. */
    attributeStatusRoundFor: (BattleAttribute) -> Int = { 3 },
    movementOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    /** directDestinationOffsets: 지정 이동이 허용하는 상대 목적지 좌표 목록이다. */
    directDestinationOffsets: List<Pair<Int, Int>> = emptyList(),
    /** infantryOffsets: 보병 이동에 사용하는 인접 좌표 규칙이다. */
    infantryOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    propertyItems: Map<Int, BattlePropertyItem> = emptyMap(),
    consumeProperty: (Int) -> Boolean = { false },
    /** zdsyGlobalValue: 전투 규칙에서 참조하는 전역 시나리오 값이다. */
    zdsyGlobalValue: Int = 0,
    /** consumeAutomaticProperty: 자동 발동한 속성 아이템을 저장소에서 차감하는 콜백이다. */
    consumeAutomaticProperty: (Int) -> Unit = {},
    onPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit = { _, _ -> },
    onUnitDefeated: (BattleUnit, BattleUnit) -> Unit = { _, _ -> },
    /** onBattleExperience: 전투 경험치 획득을 캠페인 성장 결과로 정산하는 콜백이다. */
    onBattleExperience: (BattleUnit, Int) -> CampaignExperienceResult? = { _, _ -> null },
    experienceLimit: (Int) -> Int = { 100 },
    levelLimit: Int = 50,
    /** onBattleLevelUp: 전투 중 레벨 상승 뒤 파생 능력치를 갱신하는 콜백이다. */
    onBattleLevelUp: (BattleUnit) -> Unit = {},
    onPhysicalDamage: (BattleUnit, BattleUnit, Int) -> Unit = { _, _, _ -> },
    /** onEquipmentExperienceAward: 정산된 장비 경험치를 장비별 성장 결과로 변환하는 콜백이다. */
    onEquipmentExperienceAward: ((BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> List<CampaignEquipmentExperienceResult>)? = null,
    /** onEquipmentExperience: 물리 공격마다 장비 경험치 결과를 계산하는 호환 콜백이다. */
    onEquipmentExperience: (BattleUnit, BattleUnit, Int) -> List<CampaignEquipmentExperienceResult> = { _, _, _ -> emptyList() },
    /** onRestoreUnitExperience: 회복 효과로 얻는 유닛 경험치를 정산하는 콜백이다. */
    onRestoreUnitExperience: (BattleUnit, Int) -> RestoreGrowthResolution<CampaignExperienceResult> = { _, _ -> RestoreGrowthResolution.Unavailable },
    onRestoreEquipmentExperience: (BattleUnit, Int, CampaignEquipmentSlot) -> RestoreGrowthResolution<CampaignEquipmentExperienceResult> = { _, _, _ -> RestoreGrowthResolution.Unavailable },
    random: Random = Random(0),
    /** sourceRandomStreams: 원본 난수 흐름을 재현해 전투 기록을 동일하게 만드는 선택 입력이다. */
    sourceRandomStreams: BattleTraceRandomStreams? = null,
    /** initialPlayerMoney: 전투 시작 시 플레이어가 보유한 금액이다. */
    initialPlayerMoney: Int = 0,
    /** initialEnemyMoney: 전투 시작 시 적 진영이 보유한 금액이다. */
    initialEnemyMoney: Int = 0,
    onUnitRetreat: (BattleUnit) -> Unit = {},
) {
    internal val configuration = buildBattleConfiguration(
        events, terrain, enemyMasterUnitId, weatherSchedule, weatherOffset, terrainMagicFlags,
        terrainResumeRates, terrainResumeMp, enabledFeatures, skillTempResetTypes,
        statusRoundFor, attributeStatusRoundFor, movementOffsets, infantryOffsets, propertyItems,
        consumeProperty, zdsyGlobalValue, consumeAutomaticProperty, onPermanentProperty, onUnitDefeated,
        onBattleExperience, experienceLimit, levelLimit, onBattleLevelUp, onPhysicalDamage,
        onEquipmentExperienceAward, onEquipmentExperience, onRestoreUnitExperience,
        onRestoreEquipmentExperience, random, sourceRandomStreams, onUnitRetreat,
    )
    internal val journal = BattleStateJournal(initialWeather, initialPlayerMoney, initialEnemyMoney, blockedTiles)
    internal val skillTemps =
        BattleSkillTemp { configuration.skillTempResetTypes[it] ?: BattleSkillTemp.ResetType.RESET }
    internal val probabilityResolver = BattleProbabilityResolver(random, sourceRandomStreams)

    /** lastMovePath: 유닛이 마지막으로 확정한 이동 경로를 반환해 이동 표현에 제공한다. */
    fun lastMovePath(id: String): List<Pair<Int, Int>> = journal.lastMovePath(id)
    internal val battlefield = Battlefield(units)

    /** units: 전장에 남아 있는 전투 유닛을 식별자별로 조회하는 읽기 전용 맵이다. */
    val units: Map<String, BattleUnit> = battlefield.activeMap
    val experience by lazy { BattleExperienceFacade(configuration, journal) { this.units } }
    val presentation by lazy {
        BattlePresentationTransactionFacade(
            battlefield = battlefield,
            units = { this.units },
            skillTemps = skillTemps,
            journal = journal,
            moveUnitOperation = { id, x, y -> movement.moveUnit(id, x, y, null) },
            lastMovePath = ::lastMovePath,
            attackOperation = { attackerId, targetId -> combat.attack(attackerId, targetId, null) },
            castMagicOperation = { attackerId, targetId, magicId ->
                combat.castMagic(attackerId, targetId, magicId, false, false)
            },
            usePropertyOperation = { userId, targetId, itemId ->
                combat.useProperty(userId, targetId, itemId)
            },
            isBattleEnded = { outcome() != null },
            activeFaction = { activeFaction },
            onUnitRetreat = configuration.onUnitRetreat,
        )
    }
    val combat by lazy { BattleCombatFacade(this) }
    val ai by lazy { BattleAiFacade(this) }
    val movement by lazy {
        BattleMovementQueryFacade(
            configuration = configuration,
            journal = journal,
            battlefield = battlefield,
            units = { this.units },
            activeFaction = { activeFaction },
            weather = { weather },
            isBattleEnded = { outcome() != null },
            areAllied = ::areAllied,
        )
    }
    val roundLifecycle by lazy {
        BattleRoundLifecycleFacade(
            configuration,
            journal,
            battlefield,
            { this.units.values },
            skillTemps,
            this,
            { unit -> BattleAiScorer.aiSortValue(unit, configuration.terrain, configuration.terrainResumeRates) }
        )
    }

    /** traceActions: 전투 재현과 검증에 사용하는 수행 행동 기록이다. */
    val traceActions: MutableList<String> get() = journal.mutableTraceActions()

    /** lastAiUnitResolution: 가장 최근 AI 유닛이 선택한 이동과 전술 행동 결과이다. */
    var lastAiUnitResolution: AiUnitResolution?
        get() = journal.lastAiUnitResolution
        private set(value) {
            journal.recordLastAiUnitResolution(value)
        }

    /** pendingActionTransaction: 표현 완료 전까지 보류하는 현재 전투 행동 트랜잭션이다. */
    var pendingActionTransaction: BattleActionTransaction?
        get() = journal.pendingActionTransaction
        private set(value) {
            journal.recordPendingActionTransaction(value)
        }

    /** playerMoney: 전투 중 플레이어 진영이 보유한 금액으로, 보호막과 보상에 사용한다. */
    var playerMoney: Int
        get() = journal.playerMoney
        private set(value) {
            journal.setPlayerMoney(value)
        }
    var enemyMoney: Int
        get() = journal.enemyMoney
        private set(value) {
            journal.setEnemyMoney(value)
        }

    val firedEventIds: LinkedHashSet<String> get() = journal.mutableFiredEventIds()
    var round: Int
        get() = journal.round
        private set(value) {
            journal.setRound(value)
        }
    var activeFaction: Faction
        get() = journal.activeFaction
        private set(value) {
            journal.setActiveFaction(value)
        }

    /** selectVerificationFaction: 검증 경로에서 아군 진영만 현재 조작 진영으로 선택한다. */
    internal fun selectVerificationFaction(faction: Faction) {
        require(faction.isPlayerSide()) { "Verification routes may only select an allied camp." }
        activeFaction = faction
    }

    private val outcomeCoordinator = BattleOutcomeCoordinator(
        units = { this.units.values },
        getRound = { round },
        enabledFeatures = { configuration.enabledFeatures },
        initialMaxRounds = 99,
    )
    val maxRounds: Int get() = outcomeCoordinator.maxRounds
    val scriptedOutcome: BattleOutcome? get() = outcomeCoordinator.scriptedOutcome
    var weather: BattleWeather
        get() = journal.weather
        private set(value) {
            journal.setWeather(value)
        }

    internal fun setWeatherFromCombat(value: BattleWeather) {
        weather = value
    }

    internal fun setPlayerMoneyFromEnvironment(value: Int) {
        playerMoney = value
    }

    internal fun setEnemyMoneyFromEnvironment(value: Int) {
        enemyMoney = value
    }

    fun unitAt(tileX: Int, tileY: Int): BattleUnit? = battlefield.unitAt(tileX, tileY)

    fun outcome(): BattleOutcome? = outcomeCoordinator.outcome()

    /** setMaxRounds: 전투의 라운드 제한을 설정해 시간 종료 판정에 사용한다. */
    fun setMaxRounds(value: Int) = outcomeCoordinator.setMaxRounds(value)

    /** setResolvedMaxRounds: 시나리오에서 해석한 라운드 제한을 결과 조정기에 반영한다. */
    fun setResolvedMaxRounds(value: Int) = outcomeCoordinator.setResolvedMaxRounds(value)

    fun enabledFeatureMask(): Int = configuration.enabledFeatures

    /** applyEditedWeather: 편집 화면에서 전달한 날씨 번호를 안전한 전장 날씨로 적용한다. */
    fun applyEditedWeather(value: Int) {
        weather = BattleWeather.entries[value.coerceIn(BattleWeather.entries.indices)]
    }

    fun applyEditedRound(value: Int) {
        round = value.coerceAtLeast(1)
    }

    /** skillTemp: 유닛 스킬의 현재 임시값을 조회하고, 값이 없으면 기본값을 반환한다. */
    fun skillTemp(unitId: String, skillId: Int, default: Int = 0): Int = skillTemps.value(unitId, skillId, default)
    fun setSkillTemp(unitId: String, skillId: Int, amount: Int, recordedRound: Int = round) =
        skillTemps.set(unitId, skillId, amount, recordedRound)

    fun incSkillTemp(unitId: String, skillId: Int): Int = skillTemps.increment(unitId, skillId, round)
    fun setBlockedTiles(values: Collection<Pair<Int, Int>>) {
        journal.clearBlockedTiles()
        journal.addBlockedTiles(values)
    }

    /** setScriptedOutcome: 시나리오 스크립트가 확정한 전투 승패를 저장한다. */
    fun setScriptedOutcome(value: BattleOutcome) = outcomeCoordinator.setScriptedOutcome(value)

    /** syncScriptedOutcome: 외부 스크립트 결과와 현재 전투 승패 상태를 동기화한다. */
    fun syncScriptedOutcome(value: BattleOutcome?) = outcomeCoordinator.syncScriptedOutcome(value)

    fun addUnit(unit: BattleUnit) {
        battlefield.add(unit)
        initializeRateGauges(unit)
    }

    /** initializeRateGauges: 새 유닛의 확률 판정 게이지를 초기화한다. */
    fun initializeRateGauges(unit: BattleUnit) = probabilityResolver.initializeRateGauges(unit)

    /** initializeAllRateGauges: 현재 전장의 모든 유닛 확률 게이지를 초기화한다. */
    fun initializeAllRateGauges() = units.values.forEach(::initializeRateGauges)

    /** rollStatusDuration: 상태 이상 적용 시 사용할 지속 라운드를 난수 규칙으로 결정한다. */
    fun rollStatusDuration(): Int = probabilityResolver.rollStatusDuration()

    internal fun canAttack(attacker: BattleUnit, target: BattleUnit): Boolean =
        BattleAiScorer.canAttack(attacker, target)

    internal fun areAllied(left: Faction, right: Faction): Boolean =
        left.isPlayerSide() == right.isPlayerSide()

    internal fun areAllied(left: BattleUnit, right: BattleUnit): Boolean =
        areAllied(left.effectiveFaction(), right.effectiveFaction())


}
