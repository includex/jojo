package com.jojo.game

import java.util.*

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

    /**
     * 공개 메서드 `initFight`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun initFight() {
        fightInitialized = true
    }

    /**
     * 공개 메서드 `startFight`
     *
     * ### 파라미터
    - `firstUnitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `secondUnitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `backgroundIndex` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Long`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `enqueueFightCommand`
     *
     * ### 파라미터
    - `command` (`ScenarioFightCommand`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun enqueueFightCommand(command: ScenarioFightCommand) {
        check(activeFightId == command.fightId) { "fight command does not target the active fight" }
        fightCommands.addLast(command)
        if (command is ScenarioFightCommand.End) {
            activeFightPreviousBackgroundSound?.let { backgroundSound = it }
            activeFightPreviousBackgroundSound = null
            activeFightId = null
        }
    }

    /**
     * 공개 메서드 `consumeFightCommands`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<ScenarioFightCommand>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun consumeFightCommands(): List<ScenarioFightCommand> =
        fightCommands.toList().also { fightCommands.clear() }

    /**
     * 공개 메서드 `setBackgroundSound`
     *
     * ### 파라미터
    - `soundId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setBackgroundSound(soundId: Int) {
        backgroundSound = soundId
    }

    /**
     * 공개 메서드 `effectSound`
     *
     * ### 파라미터
    - `soundId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `mode` (`Int = 1`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun effectSound(soundId: Int, mode: Int = 1) {
        if (activeEffectSoundId != -1) pendingSoundEffects += ScenarioSoundEffect(activeEffectSoundId, 0)
        activeEffectSoundId = if (mode == 0) -1 else soundId
        if (mode > 0) pendingSoundEffects += ScenarioSoundEffect(soundId, mode)
    }

    /**
     * 공개 메서드 `consumeSoundEffects`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<ScenarioSoundEffect>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun consumeSoundEffects(): List<ScenarioSoundEffect> =
        pendingSoundEffects.toList().also { pendingSoundEffects.clear() }
}
