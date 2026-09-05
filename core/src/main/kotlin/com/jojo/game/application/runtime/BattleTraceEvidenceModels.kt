package com.jojo.game.application.runtime

/**
 * Immutable screen snapshot consumed by the full-battle evidence recorder.
 *
 * The view deliberately contains only value data and already projected JSON
 * fragments.  It has no renderer, game, Battle, or mutable model dependency.
 */
data class RuntimeBattleTraceView(
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
