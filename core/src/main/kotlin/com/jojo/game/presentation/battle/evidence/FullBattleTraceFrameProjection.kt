package com.jojo.game.presentation.battle.evidence

import com.jojo.game.FullBattleTraceRecorder
import com.jojo.game.presentation.battle.BattleEvidenceView

/** Immutable, renderer-free hand-off for one full-battle trace row. */
internal data class FullBattleTraceFrameInput(
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
    val dialogue: FullBattleTraceDialogueInput,
    val phase: String,
    val script: String,
    val bootstrapBusy: List<String>,
    val cameraX: Float,
    val cameraY: Float,
    val mapObjectRevision: Int,
    val mapObjectsJson: String,
    val fightJson: String,
    val aiPresentation: FullBattleTraceAiPresentationInput?,
    val actions: List<String>,
    val units: List<FullBattleTraceUnitInput>,
    val driver: FullBattleTraceDriverInput,
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

internal data class FullBattleTraceDialogueInput(
    val active: Boolean,
    val revision: Long,
    val sourceText: String?,
    val speakerId: String,
    val text: String,
)

internal data class FullBattleTraceAiPresentationInput(
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

internal data class FullBattleTraceDriverInput(
    val selectedUnitId: String?,
    val commandPhase: String,
    val lastInput: String?,
    val menuTap: String?,
    val eventMessage: String,
    val autoOverlay: String,
)

internal data class FullBattleTraceUnitInput(
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
    val sprite: FullBattleTraceSpriteInput?,
    val abilities: List<Int>,
    val level: Int,
    val posts: Int,
    val armId: Int,
    val experience: Int,
    val attackOffsets: List<FullBattleTracePoint>,
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

internal data class FullBattleTraceSpriteInput(val sourceY: Int, val sourceWidth: Int, val sourceHeight: Int)
internal data class FullBattleTracePoint(val x: Int, val y: Int)

/** Maps a value-only frame into the stable recorder view and legacy JSON fragments. */
internal object FullBattleTraceFrameProjector {
    fun project(input: FullBattleTraceFrameInput): BattleEvidenceView = BattleEvidenceView(
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
        actionsJson = input.actions.joinToString(",") { "\"${FullBattleTraceRecorder.escape(it)}\"" },
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

    private fun aiJson(value: FullBattleTraceAiPresentationInput?): String = value?.let {
        "{\"stage\":\"${it.stage}\",\"actor\":${it.actorCharacterId},\"from\":[${it.fromX},${it.fromY}],\"to\":[${it.toX},${it.toY}],\"target\":${it.targetCharacterId},\"targetHpBefore\":${it.targetHpBefore},\"deferred\":${it.deferred},\"hasAction\":${it.hasAction}}"
    } ?: "null"

    private fun driverJson(value: FullBattleTraceDriverInput): String =
        "{\"selectedUnit\":${value.selectedUnitId?.let(::quoted) ?: "null"},\"commandPhase\":\"${value.commandPhase}\",\"lastInput\":${value.lastInput?.let(::quoted) ?: "null"},\"menuTap\":${value.menuTap?.let(::quoted) ?: "null"},\"eventMessage\":${quoted(value.eventMessage)},\"autoOverlay\":\"${value.autoOverlay}\"}"

    private fun unitJson(unit: FullBattleTraceUnitInput): String {
        val abilities = unit.abilities.joinToString(",")
        val attackOffsets = unit.attackOffsets.joinToString(",") { "[${it.x},${it.y}]" }
        val sprite = unit.sprite?.let { "[0,${it.sourceY},${it.sourceWidth},${it.sourceHeight}]" } ?: "null"
        val skills = SKILL_IDS.mapIndexed { index, id -> "[$id,${unit.skillValues.getOrElse(index) { 255 }}]" }.joinToString(",")
        val statuses = (0..14).joinToString(",") { status(unit, it).toString() }
        val statusRounds = (0..14).joinToString(",") { statusRound(unit, it).toString() }
        return "[${unit.internalIndex},${unit.characterId},${unit.factionOrdinal},${unit.tileX},${unit.tileY},${unit.hitPoints},${unit.magicPoints},${unit.direction},${unit.action},${if (unit.visible) 1 else 0},1,${if (unit.hasActed) 1 else 0},${unit.ai},${unit.aiValue},\"anime${unit.action}_${unit.direction}\",${number(unit.animationTime)},$sprite,{\"abilities\":[$abilities],\"level\":${unit.level},\"posts\":${unit.posts},\"arm\":${unit.armId},\"experience\":${unit.experience},\"growth\":{\"abilities\":[$abilities],\"level\":${unit.level},\"posts\":${unit.posts},\"arm\":${unit.armId},\"experience\":${unit.experience}},\"attackOffsets\":[$attackOffsets],\"terrain\":${unit.terrain},\"rates\":[${unit.rates.joinToString(",")}],\"skills\":[$skills],\"statuses\":[$statuses],\"statusRounds\":[$statusRounds],\"visual\":[${number(unit.visualX)},${number(unit.visualY)}]}]"
    }

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

    private fun quoted(value: String): String = "\"${FullBattleTraceRecorder.escape(value)}\""
    private fun number(value: Float): String = FullBattleTraceRecorder.number(value)
    private val SKILL_IDS = listOf(7, 43, 197, 262, 276)
}
