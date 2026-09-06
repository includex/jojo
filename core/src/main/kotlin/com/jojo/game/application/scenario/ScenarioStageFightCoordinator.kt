// Game
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

import java.util.*

/**
 * `ScenarioStageFightCoordinator` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal class ScenarioStageFightCoordinator {
    /**
     * `fightInitialized` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var fightInitialized: Boolean = false
        private set
    /**
     * `fightCommands` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val fightCommands = ArrayDeque<ScenarioFightCommand>()
    /**
     * `nextFightId` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var nextFightId = 1L
    /**
     * `activeFightPreviousBackgroundSound` (Int?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var activeFightPreviousBackgroundSound: Int? = null
    /**
     * `activeFightId` (Long?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var activeFightId: Long? = null
        private set

    /**
     * `backgroundSound` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var backgroundSound: Int = -1
        private set

    /** 새 스크립트 효과가 시작되면 이전 효과음을 해제한다. */
    private var activeEffectSoundId: Int = -1
    /**
     * `pendingSoundEffects` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val pendingSoundEffects = mutableListOf<ScenarioSoundEffect>()


    /**
     * `initFight`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun initFight() {
        fightInitialized = true
    }


    /**
     * `startFight`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
        // 전투 시작은 fight.end()가 호출될 때까지 ENTER_DANTIAO 상태로 전환한다.
        backgroundSound = 8
        return fightId
    }


    /**
     * `enqueueFightCommand`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
     * `consumeFightCommands`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeFightCommands(): List<ScenarioFightCommand> =
        fightCommands.toList().also { fightCommands.clear() }


    /**
     * `setBackgroundSound`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setBackgroundSound(soundId: Int) {
        backgroundSound = soundId
    }


    /**
     * `effectSound`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun effectSound(soundId: Int, mode: Int = 1) {
        if (activeEffectSoundId != -1) pendingSoundEffects += ScenarioSoundEffect(activeEffectSoundId, 0)
        activeEffectSoundId = if (mode == 0) -1 else soundId
        if (mode > 0) pendingSoundEffects += ScenarioSoundEffect(soundId, mode)
    }


    /**
     * `consumeSoundEffects`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeSoundEffects(): List<ScenarioSoundEffect> =
        pendingSoundEffects.toList().also { pendingSoundEffects.clear() }
}
