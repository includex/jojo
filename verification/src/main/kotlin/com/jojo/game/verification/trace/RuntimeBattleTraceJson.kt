// Verification
package com.jojo.game.verification.trace

import com.jojo.game.application.runtime.RuntimeBattleTraceView
import java.util.Locale

/** RuntimeBattleTraceJson: core의 직렬화 없는 추적 뷰를 JSONL로 인코딩하는 검증 전용 구성요소이다. */
internal object RuntimeBattleTraceJson {
    /** frame: 프레임 상태를 검증 기록으로 변환한다. */
    fun frame(view: RuntimeBattleTraceView): String {
        val observationJson = view.observation?.let { ",\"observation\":\"${escape(it)}\"" }.orEmpty()
        val resultLifecycleJson =
            ",\"scriptEnded\":${view.scriptEnded},\"scriptedOutcome\":${view.scriptedOutcome?.let { "\"${escape(it)}\"" } ?: "null"},\"resultFlow\":\"${escape(view.resultFlow)}\",\"modalKind\":${view.modalKind?.let { "\"${escape(it)}\"" } ?: "null"}" +
                    ",\"resultCallbacks\":{\"pendingScriptPasses\":${view.pendingScriptPasses},\"pendingAiDeathPass\":${view.pendingAiDeathPass},\"postActionDeaths\":${view.postActionDeaths},\"pendingAiResolution\":${view.pendingAiResolution},\"activeAiCamp\":${view.activeAiCamp?.let { "\"${escape(it)}\"" } ?: "null"},\"roundLayer\":${view.roundLayer},\"turnSettlement\":${view.turnSettlement},\"combatPresentation\":${view.combatPresentation}}"
        return "{\"f\":${view.frame},\"t\":${number(view.elapsed)},\"dt\":${number(view.delta)},\"round\":${view.round},\"camp\":${view.camp},\"maxRounds\":${view.maxRounds},\"playerCount\":${view.playerCount},\"friendCount\":${view.friendCount},\"enemyCount\":${view.enemyCount},\"paused\":${view.paused},\"end\":${view.ended},\"collocation\":${view.collocation},\"dialogue\":${if (view.dialogue) 1 else 0},\"dialogueRevision\":${view.dialogueRevision},\"dialogueIdentity\":\"${escape(view.dialogueIdentity)}\",\"dialogueSpeakerId\":\"${escape(view.dialogueSpeakerId)}\",\"dialogueText\":\"${escape(view.dialogueText)}\",\"phase\":\"${escape(view.phase)}\",\"script\":\"${escape(view.script)}\",\"bootstrapBusy\":[${view.bootstrapBusy.joinToString(",") { "\"${escape(it)}\"" }}],\"camera\":[${number(view.cameraX)},${number(view.cameraY)}],\"mapObjectRevision\":${view.mapObjectRevision},\"mapObjects\":${view.mapObjectsJson},\"fight\":${view.fightJson},\"aiPresentation\":${view.aiPresentationJson},\"actions\":[${view.actionsJson}],\"units\":[${view.unitsJson}],\"driver\":${view.driverJson}$observationJson$resultLifecycleJson}"
    }

    /** escape: JSON 특수 문자를 이스케이프한다. */
    fun escape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")

    /** number: 문자열에서 수치 값을 읽는다. */
    fun number(value: Float): String =
        if (value.isFinite()) "%.6f".format(Locale.ROOT, value).trimEnd('0').trimEnd('.') else "0"
}
