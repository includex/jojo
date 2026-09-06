// Battle
package com.jojo.game.presentation.battle.fight


/** FightActionTimeline: 전투 동작 시간 흐름이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */
internal class FightActionTimeline(
    /** `durations` (Map<Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val durations: Map<Int, Float>,
    /** `hitTimes` (Map<Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val hitTimes: Map<Int, Float>,
    /** `poseAt` (((Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val poseAt: ((Int, Float) -> FightActionPose)?,
    /** `soundsCrossed` (((Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val soundsCrossed: ((Int, Float, Float, Boolean) -> List<FightSpriteTimeline.SoundEvent>)?,
) {
    /**
     * `startedAt` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val startedAt = mutableMapOf<FightSide, Float>()
    /**
     * `soundStartPending` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val soundStartPending = mutableSetOf<FightSide>()

    /**
     * `reset`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun reset() {
        startedAt.clear()
        soundStartPending.clear()
    }

    /**
     * `start`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun start(side: FightSide, fighter: FightUnitPresentation, at: Float, action: Int): FightPresentationEvent.ActionStarted {
        fighter.resetAnimatedChild()
        fighter.action = action
        fighter.actionElapsedSeconds = 0f
        startedAt[side] = at
        soundStartPending += side
        return FightPresentationEvent.ActionStarted(side, action)
    }

    /**
     * `advance`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun advance(at: Float, fighterFor: (FightSide) -> FightUnitPresentation): List<FightPresentationEvent.Sound> = buildList {
        startedAt.forEach { (side, beganAt) ->
            val fighter = fighterFor(side)
            val action = fighter.action ?: return@forEach
            val elapsed = (at - beganAt).coerceAtLeast(0f).coerceAtMost(duration(action))
            soundsCrossed?.invoke(action, fighter.actionElapsedSeconds, elapsed, soundStartPending.remove(side))
                ?.forEach { sound -> add(FightPresentationEvent.Sound(side, action, sound.value, sound.atSeconds)) }
            fighter.actionElapsedSeconds = elapsed
            poseAt?.invoke(action, elapsed)?.let { pose ->
                fighter.childX = pose.childX
                fighter.childScaleX = pose.childScaleX
            }
        }
    }

    /**
     * `duration`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun duration(action: Int): Float = requireNotNull(durations[action]) {
        "missing recovered FightUnit duration for anime$action"
    }

    /**
     * `hitTime`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hitTime(action: Int): Float = requireNotNull(hitTimes[action]) {
        "missing recovered FightUnit hit event for anime$action"
    }
}
