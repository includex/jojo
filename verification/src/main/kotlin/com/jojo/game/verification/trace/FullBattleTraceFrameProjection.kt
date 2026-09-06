// Verification
package com.jojo.game.verification.trace

import com.jojo.game.application.runtime.BattleTraceRecorder
import com.jojo.game.application.runtime.RuntimeBattleTraceView

/** FullBattleTraceFrameInput: 전체 전투 추적 한 행을 전달하는 불변·렌더러 비의존 값이다. */
internal data class FullBattleTraceFrameInput(
    /** frame: 프레임 번호 상태를 검증 흐름에 전달한다. */
    val frame: Long,
    /** elapsed: 경과 시간 상태를 검증 흐름에 전달한다. */
    val elapsed: Float,
    /** delta: 경과 시간 상태를 검증 흐름에 전달한다. */
    val delta: Float,
    /** round: 라운드 값을 보관한다. */
    val round: Int,
    /** camp: 진영 상태를 검증 흐름에 전달한다. */
    val camp: Int,
    /** maxRounds: 최대 라운드 상태를 검증 흐름에 전달한다. */
    val maxRounds: Int,
    /** playerCount: 플레이어 수 값을 보관한다. */
    val playerCount: Int,
    /** friendCount: 아군 수 값을 보관한다. */
    val friendCount: Int,
    /** enemyCount: 적 수 값을 보관한다. */
    val enemyCount: Int,
    /** paused: 일시 정지 여부 여부를 나타낸다. */
    val paused: Boolean,
    /** ended: 종료 여부 여부를 나타낸다. */
    val ended: Boolean,
    /** collocation: 배치 상태 상태를 검증 흐름에 전달한다. */
    val collocation: Boolean,
    /** dialogue: 대화 상태 상태를 검증 흐름에 전달한다. */
    val dialogue: FullBattleTraceDialogueInput,
    /** phase: 진행 단계 상태를 검증 흐름에 전달한다. */
    val phase: String,
    /** script: 스크립트 상태를 검증 흐름에 전달한다. */
    val script: String,
    /** bootstrapBusy: 초기화 진행 여부 값을 보관한다. */
    val bootstrapBusy: List<String>,
    /** cameraX: 카메라 X 좌표 값을 보관한다. */
    val cameraX: Float,
    /** cameraY: 카메라 Y 좌표 값을 보관한다. */
    val cameraY: Float,
    /** mapObjectRevision: 지도 객체 버전 상태를 검증 흐름에 전달한다. */
    val mapObjectRevision: Int,
    /** mapObjectsJson: 지도 객체 JSON 상태를 검증 흐름에 전달한다. */
    val mapObjectsJson: String,
    /** fightJson: 전투 JSON 상태를 검증 흐름에 전달한다. */
    val fightJson: String,
    /** aiPresentation: AI 표현 상태를 검증 흐름에 전달한다. */
    val aiPresentation: FullBattleTraceAiPresentationInput?,
    /** actions: 검증 입력 정보를 담는다. */
    val actions: List<String>,
    /** units: 검증 대상 무장 정보를 담는다. */
    val units: List<FullBattleTraceUnitInput>,
    /** driver: 실행 드라이버 상태를 검증 흐름에 전달한다. */
    val driver: FullBattleTraceDriverInput,
    /** observation: 관찰 결과 상태를 검증 흐름에 전달한다. */
    val observation: String?,
    /** scriptEnded: 스크립트 종료 여부 여부를 나타낸다. */
    val scriptEnded: Boolean,
    /** scriptedOutcome: 검증 결과를 담는다. */
    val scriptedOutcome: String?,
    /** resultFlow: 검증 결과를 담는다. */
    val resultFlow: String,
    /** modalKind: 모달 종류 상태를 검증 흐름에 전달한다. */
    val modalKind: String?,
    /** pendingScriptPasses: 대기 스크립트 횟수 상태를 검증 흐름에 전달한다. */
    val pendingScriptPasses: Int,
    /** pendingAiDeathPass: AI 사망 처리 대기 여부 상태를 검증 흐름에 전달한다. */
    val pendingAiDeathPass: Int,
    /** postActionDeaths: 검증 입력 정보를 담는다. */
    val postActionDeaths: Boolean,
    /** pendingAiResolution: AI 판정 대기 여부 상태를 검증 흐름에 전달한다. */
    val pendingAiResolution: Boolean,
    /** activeAiCamp: 활성 Ai Camp 상태를 검증 흐름에 전달한다. */
    val activeAiCamp: String?,
    /** roundLayer: 라운드 레이어 상태를 검증 흐름에 전달한다. */
    val roundLayer: Boolean,
    /** turnSettlement: 턴 정산 상태를 검증 흐름에 전달한다. */
    val turnSettlement: Boolean,
    /** combatPresentation: 전투 표현 상태를 검증 흐름에 전달한다. */
    val combatPresentation: Boolean,
)

/** FullBattleTraceDialogueInput: 현재 대화의 활성 여부·개정 번호·원문·화자·표시 문구를 기록한다. */
internal data class FullBattleTraceDialogueInput(
    /** active: 활성 여부를 나타낸다. */
    val active: Boolean,
    /** revision: 버전 상태를 검증 흐름에 전달한다. */
    val revision: Long,
    /** sourceText: 원본 문구 상태를 검증 흐름에 전달한다. */
    val sourceText: String?,
    /** speakerId: 화자 식별자 값을 보관한다. */
    val speakerId: String,
    /** text: 검증 대상 식별과 표시 정보를 담는다. */
    val text: String,
)

/** FullBattleTraceAiPresentationInput: AI 연출의 단계·행동자·이동 좌표·대상·지연 상태를 기록한다. */
internal data class FullBattleTraceAiPresentationInput(
    /** stage: 단계 상태를 검증 흐름에 전달한다. */
    val stage: String,
    /** actorCharacterId: 검증 대상 무장 정보를 담는다. */
    val actorCharacterId: Int,
    /** fromX: 시작 X 좌표 값을 보관한다. */
    val fromX: Int,
    /** fromY: 시작 Y 좌표 값을 보관한다. */
    val fromY: Int,
    /** toX: to X 값을 보관한다. */
    val toX: Int,
    /** toY: to Y 값을 보관한다. */
    val toY: Int,
    /** targetCharacterId: 검증 대상 무장 정보를 담는다. */
    val targetCharacterId: Int,
    /** targetHpBefore: 검증 대상 정보를 담는다. */
    val targetHpBefore: Int,
    /** deferred: 지연 작업 상태를 검증 흐름에 전달한다. */
    val deferred: Boolean,
    /** hasAction: 검증 입력 정보를 담는다. */
    val hasAction: Boolean,
)

/** FullBattleTraceDriverInput: 자동 전투 구동기의 선택 유닛·명령 단계·최근 입력·메뉴 탭을 기록한다. */
internal data class FullBattleTraceDriverInput(
    /** selectedUnitId: 검증 대상 무장 정보를 담는다. */
    val selectedUnitId: String?,
    /** commandPhase: 검증 입력 정보를 담는다. */
    val commandPhase: String,
    /** lastInput: 검증 입력 정보를 담는다. */
    val lastInput: String?,
    /** menuTap: 메뉴 입력 상태를 검증 흐름에 전달한다. */
    val menuTap: String?,
    /** eventMessage: 검증 이벤트 목록을 담는다. */
    val eventMessage: String,
    /** autoOverlay: 자동 전투 오버레이 값을 보관한다. */
    val autoOverlay: String,
)

/** FullBattleTraceUnitInput: 전투 유닛의 위치·체력·행동·스프라이트·능력치·상태 이상을 한 행으로 기록한다. */
internal data class FullBattleTraceUnitInput(
    /** internalIndex: 내부 인덱스 값을 보관한다. */
    val internalIndex: Int,
    /** characterId: 검증 대상 무장 정보를 담는다. */
    val characterId: Int,
    /** factionOrdinal: 검증 입력 정보를 담는다. */
    val factionOrdinal: Int,
    /** tileX: 타일 X 좌표 값을 보관한다. */
    val tileX: Int,
    /** tileY: 타일 Y 좌표 값을 보관한다. */
    val tileY: Int,
    /** hitPoints: 체력 상태를 검증 흐름에 전달한다. */
    val hitPoints: Int,
    /** magicPoints: 마법력 상태를 검증 흐름에 전달한다. */
    val magicPoints: Int,
    /** direction: 방향 상태를 검증 흐름에 전달한다. */
    val direction: Int,
    /** action: 검증 입력 정보를 담는다. */
    val action: Int,
    /** visible: 표시 여부 여부를 나타낸다. */
    val visible: Boolean,
    /** hasActed: 검증 실행 조건을 나타낸다. */
    val hasActed: Boolean,
    /** ai: AI 상태를 검증 흐름에 전달한다. */
    val ai: Int,
    /** aiValue: AI 값 값을 보관한다. */
    val aiValue: Int,
    /** animationTime: 애니메이션 시간 값을 보관한다. */
    val animationTime: Float,
    /** sprite: 스프라이트 상태를 검증 흐름에 전달한다. */
    val sprite: FullBattleTraceSpriteInput?,
    /** abilities: 능력치 목록 상태를 검증 흐름에 전달한다. */
    val abilities: List<Int>,
    /** level: 레벨 값을 보관한다. */
    val level: Int,
    /** posts: 게시물 목록 상태를 검증 흐름에 전달한다. */
    val posts: Int,
    /** armId: 무기 식별자 값을 보관한다. */
    val armId: Int,
    /** experience: 경험치 값을 보관한다. */
    val experience: Int,
    /** attackOffsets: 공격 오프셋 상태를 검증 흐름에 전달한다. */
    val attackOffsets: List<FullBattleTracePoint>,
    /** terrain: 지형 정보 상태를 검증 흐름에 전달한다. */
    val terrain: Int,
    /** rates: 비율 목록 상태를 검증 흐름에 전달한다. */
    val rates: List<Int>,
    /** skillValues: 기술 값 목록 상태를 검증 흐름에 전달한다. */
    val skillValues: List<Int>,
    /** attributeLifts: 능력 상승값 상태를 검증 흐름에 전달한다. */
    val attributeLifts: List<Int>,
    /** attributeLiftRounds: 능력 상승 라운드 상태를 검증 흐름에 전달한다. */
    val attributeLiftRounds: List<Int>,
    /** paralysisActive: 마비 진행 여부 여부를 나타낸다. */
    val paralysisActive: Boolean,
    /** paralysisRound: 마비 라운드 값을 보관한다. */
    val paralysisRound: Int,
    /** silenceActive: 침묵 진행 여부 여부를 나타낸다. */
    val silenceActive: Boolean,
    /** silenceRound: 침묵 라운드 값을 보관한다. */
    val silenceRound: Int,
    /** confusionActive: 혼란 진행 여부 여부를 나타낸다. */
    val confusionActive: Boolean,
    /** confusionRound: 혼란 라운드 값을 보관한다. */
    val confusionRound: Int,
    /** poisonActive: 중독 진행 여부 여부를 나타낸다. */
    val poisonActive: Boolean,
    /** poisonRound: 중독 라운드 값을 보관한다. */
    val poisonRound: Int,
    /** lostActive: 패배 상태 여부 여부를 나타낸다. */
    val lostActive: Boolean,
    /** lostRound: 패배 라운드 값을 보관한다. */
    val lostRound: Int,
    /** actionStatusRound: 현재 검증 상태를 담는다. */
    val actionStatusRound: Int,
    /** visualX: 시각 X 좌표 값을 보관한다. */
    val visualX: Float,
    /** visualY: 시각 Y 좌표 값을 보관한다. */
    val visualY: Float,
)

/** FullBattleTraceSpriteInput: 유닛 스프라이트 원본 영역의 Y 좌표와 크기를 기록한다. */
internal data class FullBattleTraceSpriteInput(val sourceY: Int, val sourceWidth: Int, val sourceHeight: Int)
/** FullBattleTracePoint: 추적 JSON에 포함할 정수 좌표 한 점을 나타낸다. */
internal data class FullBattleTracePoint(val x: Int, val y: Int)

/** FullBattleTraceFrameProjector: 값 전용 프레임을 안정적인 기록 뷰와 기존 JSON 조각으로 변환한다. */
internal object FullBattleTraceFrameProjector {
    /** project: 실행 상태를 검증 모델로 투영한다. */
    fun project(input: FullBattleTraceFrameInput): RuntimeBattleTraceView = RuntimeBattleTraceView(
        frame = input.frame,
        elapsed = input.elapsed,
        delta = input.delta,
        round = input.round,
        camp = input.camp,
        maxRounds = input.maxRounds,
        playerCount = input.playerCount,
        friendCount = input.friendCount,
        enemyCount = input.enemyCount,
        paused = input.paused,
        ended = input.ended,
        collocation = input.collocation,
        dialogue = input.dialogue.active,
        dialogueRevision = input.dialogue.revision,
        dialogueIdentity = input.dialogue.sourceText?.let { "${input.dialogue.revision}:${Integer.toHexString(it.hashCode())}" }.orEmpty(),
        dialogueSpeakerId = input.dialogue.speakerId,
        dialogueText = input.dialogue.text,
        phase = input.phase,
        script = input.script,
        bootstrapBusy = input.bootstrapBusy,
        cameraX = input.cameraX,
        cameraY = input.cameraY,
        mapObjectRevision = input.mapObjectRevision,
        mapObjectsJson = input.mapObjectsJson,
        fightJson = input.fightJson,
        aiPresentationJson = aiJson(input.aiPresentation),
        actionsJson = input.actions.joinToString(",") { "\"${BattleTraceRecorder.escape(it)}\"" },
        unitsJson = input.units.joinToString(",", transform = ::unitJson),
        driverJson = driverJson(input.driver),
        observation = input.observation,
        scriptEnded = input.scriptEnded,
        scriptedOutcome = input.scriptedOutcome,
        resultFlow = input.resultFlow,
        modalKind = input.modalKind,
        pendingScriptPasses = input.pendingScriptPasses,
        pendingAiDeathPass = input.pendingAiDeathPass,
        postActionDeaths = input.postActionDeaths,
        pendingAiResolution = input.pendingAiResolution,
        activeAiCamp = input.activeAiCamp,
        roundLayer = input.roundLayer,
        turnSettlement = input.turnSettlement,
        combatPresentation = input.combatPresentation,
    )

    /** aiJson: AI 상태를 JSON 조각으로 직렬화한다. */
    private fun aiJson(value: FullBattleTraceAiPresentationInput?): String = value?.let {
        "{\"stage\":\"${it.stage}\",\"actor\":${it.actorCharacterId},\"from\":[${it.fromX},${it.fromY}],\"to\":[${it.toX},${it.toY}],\"target\":${it.targetCharacterId},\"targetHpBefore\":${it.targetHpBefore},\"deferred\":${it.deferred},\"hasAction\":${it.hasAction}}"
    } ?: "null"

    /** driverJson: 드라이버 상태를 JSON으로 직렬화한다. */
    private fun driverJson(value: FullBattleTraceDriverInput): String =
        "{\"selectedUnit\":${value.selectedUnitId?.let(::quoted) ?: "null"},\"commandPhase\":\"${value.commandPhase}\",\"lastInput\":${value.lastInput?.let(::quoted) ?: "null"},\"menuTap\":${value.menuTap?.let(::quoted) ?: "null"},\"eventMessage\":${quoted(value.eventMessage)},\"autoOverlay\":\"${value.autoOverlay}\"}"

    /** unitJson: 무장 상태를 JSON으로 직렬화한다. */
    private fun unitJson(unit: FullBattleTraceUnitInput): String {
        val abilities = unit.abilities.joinToString(",")
        val attackOffsets = unit.attackOffsets.joinToString(",") { "[${it.x},${it.y}]" }
        val sprite = unit.sprite?.let { "[0,${it.sourceY},${it.sourceWidth},${it.sourceHeight}]" } ?: "null"
        val skills = SKILL_IDS.mapIndexed { index, id -> "[$id,${unit.skillValues.getOrElse(index) { 255 }}]" }.joinToString(",")
        val statuses = (0..14).joinToString(",") { status(unit, it).toString() }
        val statusRounds = (0..14).joinToString(",") { statusRound(unit, it).toString() }
        return "[${unit.internalIndex},${unit.characterId},${unit.factionOrdinal},${unit.tileX},${unit.tileY},${unit.hitPoints},${unit.magicPoints},${unit.direction},${unit.action},${if (unit.visible) 1 else 0},1,${if (unit.hasActed) 1 else 0},${unit.ai},${unit.aiValue},\"anime${unit.action}_${unit.direction}\",${number(unit.animationTime)},$sprite,{\"abilities\":[$abilities],\"level\":${unit.level},\"posts\":${unit.posts},\"arm\":${unit.armId},\"experience\":${unit.experience},\"growth\":{\"abilities\":[$abilities],\"level\":${unit.level},\"posts\":${unit.posts},\"arm\":${unit.armId},\"experience\":${unit.experience}},\"attackOffsets\":[$attackOffsets],\"terrain\":${unit.terrain},\"rates\":[${unit.rates.joinToString(",")}],\"skills\":[$skills],\"statuses\":[$statuses],\"statusRounds\":[$statusRounds],\"visual\":[${number(unit.visualX)},${number(unit.visualY)}]}]"
    }

    /** status: 현재 상태 요약을 반환한다. */
    private fun status(unit: FullBattleTraceUnitInput, index: Int): Int = when (index) {
        in 0..5 -> (unit.attributeLifts.getOrElse(index) { 0 } + 1).coerceIn(0, 2)
        7 -> if (unit.paralysisActive) 0 else 1
        8 -> if (unit.silenceActive) 0 else 1
        9 -> if (unit.confusionActive) 0 else 1
        10 -> if (unit.poisonActive) 0 else 1
        13 -> if (unit.lostActive) 0 else 1
        14 -> if (unit.hasActed) 0 else 1
        else -> 1
    }

    /** statusRound: 상태 라운드를 계산한다. */
    private fun statusRound(unit: FullBattleTraceUnitInput, index: Int): Int = when (index) {
        in 0..5 -> unit.attributeLiftRounds.getOrElse(index) { 0 }
        7 -> unit.paralysisRound
        8 -> unit.silenceRound
        9 -> unit.confusionRound
        10 -> unit.poisonRound
        13 -> unit.lostRound
        14 -> unit.actionStatusRound
        else -> 0
    }

    /** quoted: 문자열을 JSON 인용 형식으로 만든다. */
    private fun quoted(value: String): String = "\"${BattleTraceRecorder.escape(value)}\""
    /** number: 문자열에서 수치 값을 읽는다. */
    private fun number(value: Float): String = BattleTraceRecorder.number(value)
    /** SKILL_IDS: 기술 식별자 목록 상태를 검증 흐름에 전달한다. */
    private val SKILL_IDS = listOf(7, 43, 197, 262, 276)
}
