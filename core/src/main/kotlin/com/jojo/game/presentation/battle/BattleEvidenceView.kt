package com.jojo.game.presentation.battle

import com.jojo.game.*

/**
 * Immutable screen snapshot consumed by the full-battle evidence recorder.
 *
 * The view deliberately contains only value data and already projected JSON
 * fragments.  It has no renderer, game, Battle, or mutable model dependency.
 */
data class BattleEvidenceView(
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
    val dialogue: Boolean,
    val dialogueRevision: Long,
    val dialogueIdentity: String,
    val dialogueSpeakerId: String,
    val dialogueText: String,
    val phase: String,
    val script: String,
    val bootstrapBusy: List<String>,
    val cameraX: Float,
    val cameraY: Float,
    val mapObjectRevision: Int,
    val mapObjectsJson: String,
    val fightJson: String,
    val aiPresentationJson: String,
    val actionsJson: String,
    val unitsJson: String,
    val driverJson: String,
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

/** Pure JSONL row recorder for [BattleEvidenceView] snapshots. */
object BattleEvidenceRecorder {
    fun frame(view: BattleEvidenceView): String {
        val observationJson = view.observation?.let {
            ",\"observation\":\"${FullBattleTraceRecorder.escape(it)}\""
        }.orEmpty()
        val resultLifecycleJson =
            ",\"scriptEnded\":${view.scriptEnded},\"scriptedOutcome\":${view.scriptedOutcome?.let { "\"$it\"" } ?: "null"},\"resultFlow\":\"${view.resultFlow}\",\"modalKind\":${view.modalKind?.let { "\"$it\"" } ?: "null"}" +
                    ",\"resultCallbacks\":{\"pendingScriptPasses\":${view.pendingScriptPasses},\"pendingAiDeathPass\":${view.pendingAiDeathPass},\"postActionDeaths\":${view.postActionDeaths},\"pendingAiResolution\":${view.pendingAiResolution},\"activeAiCamp\":${view.activeAiCamp?.let { "\"$it\"" } ?: "null"},\"roundLayer\":${view.roundLayer},\"turnSettlement\":${view.turnSettlement},\"combatPresentation\":${view.combatPresentation}}"
        return "{\"f\":${view.frame},\"t\":${FullBattleTraceRecorder.number(view.elapsed)},\"dt\":${
            FullBattleTraceRecorder.number(
                view.delta
            )
        },\"round\":${view.round},\"camp\":${view.camp},\"maxRounds\":${view.maxRounds},\"playerCount\":${view.playerCount},\"friendCount\":${view.friendCount},\"enemyCount\":${view.enemyCount},\"paused\":${view.paused},\"end\":${view.ended},\"collocation\":${view.collocation},\"dialogue\":${if (view.dialogue) 1 else 0},\"dialogueRevision\":${view.dialogueRevision},\"dialogueIdentity\":\"${
            FullBattleTraceRecorder.escape(
                view.dialogueIdentity
            )
        }\",\"dialogueSpeakerId\":\"${FullBattleTraceRecorder.escape(view.dialogueSpeakerId)}\",\"dialogueText\":\"${
            FullBattleTraceRecorder.escape(
                view.dialogueText
            )
        }\",\"phase\":\"${view.phase}\",\"script\":\"${view.script}\",\"bootstrapBusy\":[${
            view.bootstrapBusy.joinToString(
                ","
            ) { "\"$it\"" }
        }],\"camera\":[${FullBattleTraceRecorder.number(view.cameraX)},${FullBattleTraceRecorder.number(view.cameraY)}],\"mapObjectRevision\":${view.mapObjectRevision},\"mapObjects\":${view.mapObjectsJson},\"fight\":${view.fightJson},\"aiPresentation\":${view.aiPresentationJson},\"actions\":[${view.actionsJson}],\"units\":[${view.unitsJson}],\"driver\":${view.driverJson}$observationJson$resultLifecycleJson}"
    }
}

