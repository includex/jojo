// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.application.runtime.BattleTraceRecorder
import com.jojo.game.application.runtime.RuntimeBattleTraceView

/** RuntimeBattleTraceFrameInput: 화면 전투 상태를 추적 JSON으로 변환하기 전 필요한 원시 프레임 데이터다. */
internal data class RuntimeBattleTraceFrameInput(
    val frame: Long,
    val elapsed: Float,
    val delta: Float,
    val round: Int,
    val camp: Int,
    val maxRounds: Int,
    val playerCount: Int,
    val friendCount: Int,
    val enemyCount: Int,
    val paused: Boolean,
    val ended: Boolean,
    val collocation: Boolean,
    val dialogue: RuntimeBattleTraceDialogueInput,
    val phase: String,
    val script: String,
    val bootstrapBusy: List<String>,
    val cameraX: Float,
    val cameraY: Float,
    val mapObjectRevision: Int,
    val mapObjectsJson: String,
    val fightJson: String,
    val aiPresentation: RuntimeBattleTraceAiPresentationInput?,
    val actions: List<String>,
    val units: List<RuntimeBattleTraceUnitInput>,
    val driver: RuntimeBattleTraceDriverInput,
    val observation: String?,
    val scriptEnded: Boolean,
    val scriptedOutcome: String?,
    val resultFlow: String,
    val modalKind: String?,
    val pendingScriptPasses: Int,
    val pendingAiDeathPass: Int,
    val postActionDeaths: Boolean,
    val pendingAiResolution: Boolean,
    val activeAiCamp: String?,
    val roundLayer: Boolean,
    val turnSettlement: Boolean,
    val combatPresentation: Boolean,
)

/**
 * `RuntimeBattleTraceDialogueInput` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class RuntimeBattleTraceDialogueInput(
    val active: Boolean,
    val revision: Long,
    val sourceText: String?,
    val speakerId: String,
    val text: String,
)

/**
 * `RuntimeBattleTraceAiPresentationInput` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class RuntimeBattleTraceAiPresentationInput(
    val stage: String,
    val actorCharacterId: Int,
    val fromX: Int,
    val fromY: Int,
    val toX: Int,
    val toY: Int,
    val targetCharacterId: Int,
    val targetHpBefore: Int,
    val deferred: Boolean,
    val hasAction: Boolean,
)

/**
 * `RuntimeBattleTraceDriverInput` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class RuntimeBattleTraceDriverInput(
    val selectedUnitId: String?,
    val commandPhase: String,
    val lastInput: String?,
    val menuTap: String?,
    val eventMessage: String,
    val autoOverlay: String,
)

/**
 * `RuntimeBattleTraceUnitInput` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class RuntimeBattleTraceUnitInput(
    val internalIndex: Int,
    val characterId: Int,
    val factionOrdinal: Int,
    val tileX: Int,
    val tileY: Int,
    val hitPoints: Int,
    val magicPoints: Int,
    val direction: Int,
    val action: Int,
    val visible: Boolean,
    val hasActed: Boolean,
    val ai: Int,
    val aiValue: Int,
    val animationTime: Float,
    val sprite: RuntimeBattleTraceSpriteInput?,
    val abilities: List<Int>,
    val level: Int,
    val posts: Int,
    val armId: Int,
    val experience: Int,
    val attackOffsets: List<RuntimeBattleTracePoint>,
    val terrain: Int,
    val rates: List<Int>,
    val skillValues: List<Int>,
    val attributeLifts: List<Int>,
    val attributeLiftRounds: List<Int>,
    val paralysisActive: Boolean,
    val paralysisRound: Int,
    val silenceActive: Boolean,
    val silenceRound: Int,
    val confusionActive: Boolean,
    val confusionRound: Int,
    val poisonActive: Boolean,
    val poisonRound: Int,
    val lostActive: Boolean,
    val lostRound: Int,
    val actionStatusRound: Int,
    val visualX: Float,
    val visualY: Float,
)

/**
 * `RuntimeBattleTraceSpriteInput` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class RuntimeBattleTraceSpriteInput(val sourceY: Int, val sourceWidth: Int, val sourceHeight: Int)
/**
 * `RuntimeBattleTracePoint` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class RuntimeBattleTracePoint(val x: Int, val y: Int)

/** RuntimeBattleTraceMapObject: 원본 gate 배열 한 칸에 해당하는 지도 객체 원시값이다. */
internal data class RuntimeBattleTraceMapObject(
    val x: Int,
    val y: Int,
    val objectId: Int,
    val terrainId: Int,
    val enabled: Boolean,
)

/** RuntimeBattleTraceMapObjectSnapshot: 변화한 프레임에만 행을 싣는 지도 객체 관측 결과다. */
internal data class RuntimeBattleTraceMapObjectSnapshot(val revision: Int, val json: String)

/**
 * RuntimeBattleTraceMapObjectJournal: 원본 full-battle-trace-renderer의 mapObjectObservation과 같은 규칙으로
 * 지도 객체를 관측한다. 행은 `[TYPE, TERRAIN, X, Y]` 순서이고(BATTLE_GATE_ATTR), 꺼진 객체는 원본에서
 * gate 칸이 TYPE=65535로 비워지므로 제외하며, 서명이 바뀐 프레임에서만 행과 증가한 revision을 싣는다.
 */
internal class RuntimeBattleTraceMapObjectJournal {
    /** `revision` (상태 값): 마지막으로 부여한 지도 객체 변경 번호를 보관한다. */
    private var revision = 0

    /** `signature` (상태 값): 마지막으로 기록한 행 서명을 보관해 변화 여부만 판정한다. */
    private var signature: String? = null

    /** `observe`: 현재 지도 객체를 관측해 변화한 프레임에만 행을 싣는 스냅샷을 만든다. */
    fun observe(objects: Collection<RuntimeBattleTraceMapObject>): RuntimeBattleTraceMapObjectSnapshot {
        val rows = objects.asSequence().filter { it.enabled }
            .sortedWith(
                compareBy<RuntimeBattleTraceMapObject>({ it.x }, { it.y }, { it.objectId }, { terrainOf(it) }),
            )
            .joinToString(",") { "[${it.objectId},${terrainOf(it)},${it.x},${it.y}]" }
        val next = "[$rows]"
        if (next == signature) return RuntimeBattleTraceMapObjectSnapshot(revision, "null")
        signature = next
        revision++
        return RuntimeBattleTraceMapObjectSnapshot(revision, next)
    }

    /**
     * `terrainOf`: 원본 BattleLayer.setObject2가 TYPE<3인 객체의 지형을 [HUO, CHUAN, HUO]로 덮어쓴 뒤
     * gate 배열에 넣는 것과 같게, 대본이 넘긴 지형 대신 기록용 지형을 고른다.
     */
    private fun terrainOf(objectValue: RuntimeBattleTraceMapObject): Int =
        OBJECT_TERRAIN.getOrNull(objectValue.objectId) ?: objectValue.terrainId

    private companion object {
        /** TERRAIN.HUO=26, TERRAIN.CHUAN=27 (원본 Config.js). */
        val OBJECT_TERRAIN = intArrayOf(26, 27, 26)
    }
}

/** RuntimeBattleTraceFrameProjector: 원시 전투 프레임을 검증 파일용 RuntimeBattleTraceView로 직렬화한다. */
internal object RuntimeBattleTraceFrameProjector {
    /**
     * `project`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun project(input: RuntimeBattleTraceFrameInput): RuntimeBattleTraceView = RuntimeBattleTraceView(
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

    /**
     * `aiJson`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun aiJson(value: RuntimeBattleTraceAiPresentationInput?): String = value?.let {
        "{\"stage\":\"${it.stage}\",\"actor\":${it.actorCharacterId},\"from\":[${it.fromX},${it.fromY}],\"to\":[${it.toX},${it.toY}],\"target\":${it.targetCharacterId},\"targetHpBefore\":${it.targetHpBefore},\"deferred\":${it.deferred},\"hasAction\":${it.hasAction}}"
    } ?: "null"

    /**
     * `driverJson`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun driverJson(value: RuntimeBattleTraceDriverInput): String =
        "{\"selectedUnit\":${value.selectedUnitId?.let(::quoted) ?: "null"},\"commandPhase\":\"${value.commandPhase}\",\"lastInput\":${value.lastInput?.let(::quoted) ?: "null"},\"menuTap\":${value.menuTap?.let(::quoted) ?: "null"},\"eventMessage\":${quoted(value.eventMessage)},\"autoOverlay\":\"${value.autoOverlay}\"}"

    /**
     * `unitJson`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun unitJson(unit: RuntimeBattleTraceUnitInput): String {
        val abilities = unit.abilities.joinToString(",")
        val attackOffsets = unit.attackOffsets.joinToString(",") { "[${it.x},${it.y}]" }
        val sprite = unit.sprite?.let { "[0,${it.sourceY},${it.sourceWidth},${it.sourceHeight}]" } ?: "null"
        val skills = SKILL_IDS.mapIndexed { index, id -> "[$id,${unit.skillValues.getOrElse(index) { 255 }}]" }.joinToString(",")
        val statuses = (0..14).joinToString(",") { status(unit, it).toString() }
        val statusRounds = (0..14).joinToString(",") { statusRound(unit, it).toString() }
        return "[${unit.internalIndex},${unit.characterId},${unit.factionOrdinal},${unit.tileX},${unit.tileY},${unit.hitPoints},${unit.magicPoints},${unit.direction},${unit.action},${if (unit.visible) 1 else 0},1,${if (unit.hasActed) 1 else 0},${unit.ai},${unit.aiValue},\"anime${unit.action}_${unit.direction}\",${number(unit.animationTime)},$sprite,{\"abilities\":[$abilities],\"level\":${unit.level},\"posts\":${unit.posts},\"arm\":${unit.armId},\"experience\":${unit.experience},\"growth\":{\"abilities\":[$abilities],\"level\":${unit.level},\"posts\":${unit.posts},\"arm\":${unit.armId},\"experience\":${unit.experience}},\"attackOffsets\":[$attackOffsets],\"terrain\":${unit.terrain},\"rates\":[${unit.rates.joinToString(",")}],\"skills\":[$skills],\"statuses\":[$statuses],\"statusRounds\":[$statusRounds],\"visual\":[${number(unit.visualX)},${number(unit.visualY)}]}]"
    }

    /**
     * `status`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun status(unit: RuntimeBattleTraceUnitInput, index: Int): Int = when (index) {
        in 0..5 -> (unit.attributeLifts.getOrElse(index) { 0 } + 1).coerceIn(0, 2)
        7 -> if (unit.paralysisActive) 0 else 1
        8 -> if (unit.silenceActive) 0 else 1
        9 -> if (unit.confusionActive) 0 else 1
        10 -> if (unit.poisonActive) 0 else 1
        13 -> if (unit.lostActive) 0 else 1
        14 -> if (unit.hasActed) 0 else 1
        else -> 1
    }

    /**
     * `statusRound`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun statusRound(unit: RuntimeBattleTraceUnitInput, index: Int): Int = when (index) {
        in 0..5 -> unit.attributeLiftRounds.getOrElse(index) { 0 }
        7 -> unit.paralysisRound
        8 -> unit.silenceRound
        9 -> unit.confusionRound
        10 -> unit.poisonRound
        13 -> unit.lostRound
        14 -> unit.actionStatusRound
        else -> 0
    }

    /**
     * `quoted`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun quoted(value: String): String = "\"${BattleTraceRecorder.escape(value)}\""
    /**
     * `number`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun number(value: Float): String = BattleTraceRecorder.number(value)
    /**
     * `SKILL_IDS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val SKILL_IDS = listOf(7, 43, 197, 262, 276)
}
