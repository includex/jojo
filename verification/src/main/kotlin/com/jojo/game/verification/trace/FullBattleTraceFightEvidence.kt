package com.jojo.game.verification.trace

import com.jojo.game.application.runtime.BattleTraceRecorder

/** Immutable FightLayer slot projection in original prefab order. */
internal data class FullBattleTraceFighter(
    val characterId: Int?,
    val created: Boolean,
    val action: Int?,
    val actionElapsedSeconds: Float,
    val parentX: Float,
    val parentScaleX: Float,
    val childX: Float,
    val childY: Float,
    val childScaleX: Float,
    val opacity: Float,
    val zIndex: Int,
    val dead: Boolean,
)

internal data class FullBattleTraceSpeech(val active: Boolean, val renderedText: String)

internal data class FullBattleTraceFightSnapshot(
    val mineIndex: Int,
    val enemyIndex: Int,
    val introBackgroundActive: Boolean,
    val duelBackgroundActive: Boolean,
    val startCrossFade: Float,
    val slot0: FullBattleTraceFighter,
    val slot1: FullBattleTraceFighter,
    val slot0Speech: FullBattleTraceSpeech,
    val slot1Speech: FullBattleTraceSpeech,
)

/** Serializer kept outside BattleScreen so trace JSON has no mutable renderer dependency. */
internal object FullBattleTraceFightEvidence {
    fun json(snapshot: FullBattleTraceFightSnapshot?): String {
        if (snapshot == null) return "null"
        val introOpacity = if (snapshot.introBackgroundActive) 1f - snapshot.startCrossFade else 0f
        val duelOpacity = if (snapshot.duelBackgroundActive) snapshot.startCrossFade else 0f
        return "{\"mineIndex\":${snapshot.mineIndex},\"enemyIndex\":${snapshot.enemyIndex}," +
                "\"backgrounds\":[[${snapshot.introBackgroundActive},${number(introOpacity)}]," +
                "[${snapshot.duelBackgroundActive},${number(duelOpacity)}]]," +
                "\"units\":[${fighter(snapshot.slot0)},${fighter(snapshot.slot1)}]," +
                "\"speeches\":[${speech(snapshot.slot0Speech)},${speech(snapshot.slot1Speech)}]}"
    }

    private fun fighter(value: FullBattleTraceFighter): String =
        "[${value.characterId ?: "null"},${value.created},${value.action ?: "null"},${number(value.actionElapsedSeconds)}," +
                "${number(value.parentX)},${number(value.parentScaleX)},${number(value.childX)},${number(value.childY)}," +
                "${number(value.childScaleX)},${number(value.opacity)},${value.zIndex},${value.dead}]"

    private fun speech(value: FullBattleTraceSpeech): String =
        "[${value.active},\"${BattleTraceRecorder.escape(value.renderedText)}\"]"

    private fun number(value: Float): String = BattleTraceRecorder.number(value)
}
