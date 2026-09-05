package com.jojo.game.verification.trace

import com.jojo.game.application.runtime.RuntimeBattleTraceView
import java.util.Locale

/** Verification-owned JSONL encoding for the core's serialization-free trace view. */
internal object RuntimeBattleTraceJson {
    fun frame(view: RuntimeBattleTraceView): String {
        val observationJson = view.observation?.let { ",\"observation\":\"${escape(it)}\"" }.orEmpty()
        val resultLifecycleJson =
            ",\"scriptEnded\":${view.scriptEnded},\"scriptedOutcome\":${view.scriptedOutcome?.let { "\"${escape(it)}\"" } ?: "null"},\"resultFlow\":\"${escape(view.resultFlow)}\",\"modalKind\":${view.modalKind?.let { "\"${escape(it)}\"" } ?: "null"}" +
                    ",\"resultCallbacks\":{\"pendingScriptPasses\":${view.pendingScriptPasses},\"pendingAiDeathPass\":${view.pendingAiDeathPass},\"postActionDeaths\":${view.postActionDeaths},\"pendingAiResolution\":${view.pendingAiResolution},\"activeAiCamp\":${view.activeAiCamp?.let { "\"${escape(it)}\"" } ?: "null"},\"roundLayer\":${view.roundLayer},\"turnSettlement\":${view.turnSettlement},\"combatPresentation\":${view.combatPresentation}}"
        return "{\"f\":${view.frame},\"t\":${number(view.elapsed)},\"dt\":${number(view.delta)},\"round\":${view.round},\"camp\":${view.camp},\"maxRounds\":${view.maxRounds},\"playerCount\":${view.playerCount},\"friendCount\":${view.friendCount},\"enemyCount\":${view.enemyCount},\"paused\":${view.paused},\"end\":${view.ended},\"collocation\":${view.collocation},\"dialogue\":${if (view.dialogue) 1 else 0},\"dialogueRevision\":${view.dialogueRevision},\"dialogueIdentity\":\"${escape(view.dialogueIdentity)}\",\"dialogueSpeakerId\":\"${escape(view.dialogueSpeakerId)}\",\"dialogueText\":\"${escape(view.dialogueText)}\",\"phase\":\"${escape(view.phase)}\",\"script\":\"${escape(view.script)}\",\"bootstrapBusy\":[${view.bootstrapBusy.joinToString(",") { "\"${escape(it)}\"" }}],\"camera\":[${number(view.cameraX)},${number(view.cameraY)}],\"mapObjectRevision\":${view.mapObjectRevision},\"mapObjects\":${view.mapObjectsJson},\"fight\":${view.fightJson},\"aiPresentation\":${view.aiPresentationJson},\"actions\":[${view.actionsJson}],\"units\":[${view.unitsJson}],\"driver\":${view.driverJson}$observationJson$resultLifecycleJson}"
    }

    fun escape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")

    fun number(value: Float): String =
        if (value.isFinite()) "%.6f".format(Locale.ROOT, value).trimEnd('0').trimEnd('.') else "0"
}
