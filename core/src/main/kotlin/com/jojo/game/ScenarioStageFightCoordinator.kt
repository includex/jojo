package com.jojo.game

import java.util.ArrayDeque

internal class ScenarioStageFightCoordinator {
    var fightInitialized: Boolean = false
        private set
    private val fightCommands = ArrayDeque<ScenarioFightCommand>()
    private var nextFightId = 1L
    private var activeFightPreviousBackgroundSound: Int? = null
    var activeFightId: Long? = null
        private set

    var backgroundSound: Int = -1
        private set
    /** Mirrors StageLayer._effSoundIdx: every new scripted effect releases its prior active instance. */
    private var activeEffectSoundId: Int = -1
    private val pendingSoundEffects = mutableListOf<ScenarioSoundEffect>()

    fun initFight() {
        fightInitialized = true
    }

    fun startFight(firstUnitId: Int, secondUnitId: Int, backgroundIndex: Int): Long {
        check(activeFightId == null) { "a scripted fight is already active" }
        val fightId = nextFightId++
        activeFightId = fightId
        activeFightPreviousBackgroundSound = backgroundSound
        fightCommands.addLast(
            ScenarioFightCommand.Start(
                fightId = fightId,
                firstUnitId = firstUnitId,
                secondUnitId = secondUnitId,
                backgroundIndex = backgroundIndex,
                previousBackgroundSound = backgroundSound,
            )
        )
        // BattleScreen.startFight switches to ENTER_DANTIAO until fight.end().
        backgroundSound = 8
        return fightId
    }

    fun enqueueFightCommand(command: ScenarioFightCommand) {
        check(activeFightId == command.fightId) { "fight command does not target the active fight" }
        fightCommands.addLast(command)
        if (command is ScenarioFightCommand.End) {
            activeFightPreviousBackgroundSound?.let { backgroundSound = it }
            activeFightPreviousBackgroundSound = null
            activeFightId = null
        }
    }

    fun consumeFightCommands(): List<ScenarioFightCommand> =
        fightCommands.toList().also { fightCommands.clear() }

    fun setBackgroundSound(soundId: Int) {
        backgroundSound = soundId
    }

    fun effectSound(soundId: Int, mode: Int = 1) {
        if (activeEffectSoundId != -1) pendingSoundEffects += ScenarioSoundEffect(activeEffectSoundId, 0)
        activeEffectSoundId = if (mode == 0) -1 else soundId
        if (mode > 0) pendingSoundEffects += ScenarioSoundEffect(soundId, mode)
    }

    fun consumeSoundEffects(): List<ScenarioSoundEffect> =
        pendingSoundEffects.toList().also { pendingSoundEffects.clear() }
}
