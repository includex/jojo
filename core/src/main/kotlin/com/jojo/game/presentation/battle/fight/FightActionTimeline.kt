// Battle
package com.jojo.game.presentation.battle.fight


/** FightActionTimeline: 전투 동작 시간 흐름이며, 시간 경과에 따른 전투 상태와 표현 단계를 진행한다. */
internal class FightActionTimeline(
    private val durations: Map<Int, Float>,
    private val hitTimes: Map<Int, Float>,
    private val poseAt: ((Int, Float) -> FightActionPose)?,
    private val soundsCrossed: ((Int, Float, Float, Boolean) -> List<FightSpriteTimeline.SoundEvent>)?,
) {
    private val startedAt = mutableMapOf<FightSide, Float>()
    private val soundStartPending = mutableSetOf<FightSide>()

    fun reset() {
        startedAt.clear()
        soundStartPending.clear()
    }

    fun start(side: FightSide, fighter: FightUnitPresentation, at: Float, action: Int): FightPresentationEvent.ActionStarted {
        fighter.resetAnimatedChild()
        fighter.action = action
        fighter.actionElapsedSeconds = 0f
        startedAt[side] = at
        soundStartPending += side
        return FightPresentationEvent.ActionStarted(side, action)
    }

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

    fun duration(action: Int): Float = requireNotNull(durations[action]) {
        "missing recovered FightUnit duration for anime$action"
    }

    fun hitTime(action: Int): Float = requireNotNull(hitTimes[action]) {
        "missing recovered FightUnit hit event for anime$action"
    }
}
