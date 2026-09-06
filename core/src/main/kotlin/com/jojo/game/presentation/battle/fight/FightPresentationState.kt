// Battle
package com.jojo.game.presentation.battle.fight

import com.jojo.game.domain.scenario.ScenarioFightCommand

/** 전투 연출 진영: 컷신의 아군·적군 배우와 대사 패널을 구분한다. */
enum class FightSide { MINE, ENEMY }

/** 전투 컷신 유닛 상태: 인물 생성 여부, 행동 프레임, 부모·자식 변환, 전후면 순서를 보관한다. */
data class FightUnitPresentation(
    var characterId: Int? = null,
    var created: Boolean = false,
    var action: Int? = null,
    var actionElapsedSeconds: Float = 0f,
    var parentX: Float = 0f,
    var parentScaleX: Float = 1f,
    var childX: Float = 0f,
    var childScaleX: Float = 1f,
    var zIndex: Int = 0,
    var dead: Boolean = false,
) {
    /** 자식 변환 초기화: 직전 행동의 자식 이동·반전을 부모 좌표에 반영하고 기본 상태로 되돌린다. */
    fun resetAnimatedChild() {
        parentX += 4f * childX * if (parentScaleX < 0f) -1f else 1f
        childX = 0f
        if (childScaleX < 0f) parentScaleX *= -1f
        childScaleX = 1f
    }
}

/** 전투 컷신 대사 상태: 원문, 타이핑 중인 내용, 실제 표시할 서식 문자열을 보관한다. */
data class FightSpeechPresentation(
    var active: Boolean = false,
    var sourceText: String = "",
    var content: String = "",
    var renderedText: String = "",
)

/** 전투 행동 자세: 현재 행동 프레임이 유닛 자식 노드에 적용할 위치·반전·투명도를 정의한다. */
data class FightActionPose(
    val childX: Float = 0f,
    val childY: Float = 0f,
    val childScaleX: Float = 1f,
    val opacity: Float = 255f,
)


/** 전투 컷신 이벤트: 시나리오 명령 재생 중 시작·타격·대사·소리·완료 시점을 외부에 전달한다. */
sealed class FightPresentationEvent {
    data class CommandStarted(val command: ScenarioFightCommand) : FightPresentationEvent()
    data class ActionStarted(val side: FightSide, val action: Int) : FightPresentationEvent()
    data class TextChanged(val side: FightSide, val renderedText: String) : FightPresentationEvent()
    data class Hit(val attacker: FightSide, val defender: FightSide) : FightPresentationEvent()
    data class Sound(
        val side: FightSide,
        val action: Int,
        val value: String,
        val atActionSeconds: Float,
    ) : FightPresentationEvent()
    data class CommandCompleted(val command: ScenarioFightCommand) : FightPresentationEvent()
}

/** 전투 컷신 상태기: 시나리오 Fight 명령을 시간순 배우·대사·효과음 이벤트와 화면 상태로 변환한다. */
class FightPresentationState(
    private val actionDurations: Map<Int, Float> = SOURCE_ACTION_DURATIONS,
    private val actionHitTimes: Map<Int, Float> = SOURCE_ACTION_HIT_TIMES,
    private val isMineUnit: (Int) -> Boolean? = { null },
    private val actionPoseAt: ((Int, Float) -> FightActionPose)? = null,
    private val actionSoundsCrossed: ((Int, Float, Float, Boolean) -> List<FightSpriteTimeline.SoundEvent>)? = null,
) {
    companion object {
        const val FRAME_SECONDS = 1f / 24f
        const val TYPE_SECONDS = 0.04f
        const val SPEECH_CLOSE_SECONDS = 1.6f
        const val START_SECONDS = 4f
        const val SHOW_START_SECONDS = 1f
        private const val ACTION_DEATH = 28
        private const val ACTION_ENTER = 29
        /** SOURCE_ACTION_FRAMES: 원본 일반 행동의 프레임 번호 목록이다. */
        val SOURCE_ACTION_FRAMES: Map<Int, Int> = listOf(
            4, 4, 10, 8, 8, 4, 4, 4, 4, 10,
            18, 4, 24, 8, 792, 360, 12, 8, 12, 8,
            5, 5, 6, 8, 20, 5, 5, 8, 12, 11,
        ).mapIndexed { action, frames -> action to frames }.toMap()
        val SOURCE_ACTION_DURATIONS: Map<Int, Float> =
            SOURCE_ACTION_FRAMES.mapValues { (_, frames) -> frames * FRAME_SECONDS }
        /** SOURCE_ACTION_HIT_FRAMES: 원본 타격 행동의 프레임 번호 목록이다. */
        val SOURCE_ACTION_HIT_FRAMES: Map<Int, Int> = mapOf(
            16 to 8,
            17 to 4,
            18 to 4,
            19 to 4,
            24 to 15,
        )
        val SOURCE_ACTION_HIT_TIMES: Map<Int, Float> =
            SOURCE_ACTION_HIT_FRAMES.mapValues { (_, frames) -> frames * FRAME_SECONDS }
        fun typingTickCount(text: String): Int = typingContents(text).size
        fun typingContents(text: String): List<String> = FightSpeechLifecycle.typingContents(text)
        fun renderedRichText(content: String): String = FightSpeechLifecycle.renderedRichText(content)
    }
    private data class TimedMutation(val at: Float, val order: Int, val mutate: () -> Unit)
    val mine = FightUnitPresentation()
    val enemy = FightUnitPresentation()
    private val actions = FightActionTimeline(actionDurations, actionHitTimes, actionPoseAt, actionSoundsCrossed)
    private val speeches = FightSpeechLifecycle()
    val mineSpeech get() = speeches.mine
    val enemySpeech get() = speeches.enemy
    val emittedEvents = mutableListOf<FightPresentationEvent>()
    var activeFightId: Long? = null
        private set
    var backgroundIndex: Int = 0
        private set
    var currentCommand: ScenarioFightCommand? = null
        private set
    var commandElapsedSeconds: Float = 0f
        private set
    var commandDurationSeconds: Float = 0f
        private set
    var introBackgroundActive: Boolean = false
        private set
    var duelBackgroundActive: Boolean = false
        private set
    var startRevealGroup: Int = 0
        private set
    var startCrossFade: Float = 0f
        private set
    var startLabelsActive: Boolean = false
        private set
    var lastHitAtSeconds: Float? = null
        private set
    var pendingAnimationCallbacks: Int = 0
        private set
    var mineIndex: Int = 0
        private set
    var enemyIndex: Int = 1
        private set
    val commandComplete: Boolean get() = currentCommand == null

    private val mutations = mutableListOf<TimedMutation>()
    private var nextMutation = 0
    private var mutationOrder = 0
    fun unit(side: FightSide): FightUnitPresentation = if (side == FightSide.MINE) mine else enemy
    fun speech(side: FightSide): FightSpeechPresentation = if (side == FightSide.MINE) mineSpeech else enemySpeech

    /** begin: 전투 단계의 시작 상태를 만들고 필요한 값을 초기화한다. */
    fun begin(command: ScenarioFightCommand): Float {
        check(currentCommand == null) { "FightLayer command is still active: $currentCommand" }
        if (command is ScenarioFightCommand.Start) {
            check(activeFightId == null) { "a FightLayer is already active" }
            activeFightId = command.fightId
        } else {
            check(activeFightId == command.fightId) { "fight command does not target the active FightLayer" }
        }
        currentCommand = command
        commandElapsedSeconds = 0f
        commandDurationSeconds = 0f
        lastHitAtSeconds = null
        pendingAnimationCallbacks = 0
        mutations.clear()
        actions.reset()
        nextMutation = 0
        mutationOrder = 0
        emittedEvents += FightPresentationEvent.CommandStarted(command)
        when (command) {
            is ScenarioFightCommand.Start -> beginStart(command)
            is ScenarioFightCommand.ShowUnit -> beginShowUnit(command)
            is ScenarioFightCommand.ShowStart -> beginShowStart()
            is ScenarioFightCommand.SetAction -> beginSetAction(command)
            is ScenarioFightCommand.Say -> beginSay(command)
            is ScenarioFightCommand.Attack1 -> beginAttack1(command)
            is ScenarioFightCommand.Attack2 -> beginAttack2(command)
            is ScenarioFightCommand.Death -> beginDeath(command)
            is ScenarioFightCommand.End -> {
                introBackgroundActive = false
                duelBackgroundActive = false
                activeFightId = null
                complete(command)
            }
        }
        mutations.sortWith(compareBy<TimedMutation> { it.at }.thenBy { it.order })
        return commandDurationSeconds
    }
    /** advance: 현재 전투 상태를 다음 처리 단계로 진행한다. */
    fun advance(deltaSeconds: Float): List<FightPresentationEvent> {
        require(deltaSeconds >= 0f) { "deltaSeconds must be non-negative" }
        if (currentCommand == null) return emptyList()
        val eventStart = emittedEvents.size
        val advancingCommand = currentCommand
        val target = (commandElapsedSeconds + deltaSeconds).coerceAtMost(commandDurationSeconds)
        while (nextMutation < mutations.size && mutations[nextMutation].at <= target + 0.000001f) {
            val mutation = mutations[nextMutation++]
            updateActionStates(mutation.at)
            commandElapsedSeconds = mutation.at
            mutation.mutate()
        }
        commandElapsedSeconds = target
        updateActionStates(target)
        if (advancingCommand is ScenarioFightCommand.Start) {
            startCrossFade = ((target - 2f) / 2f).coerceIn(0f, 1f)
        }
        return emittedEvents.subList(eventStart, emittedEvents.size).toList()
    }
    private fun beginStart(command: ScenarioFightCommand.Start) {
        val layout = FightStartSequence.layout(command, isMineUnit)
        backgroundIndex = layout.backgroundIndex
        mineIndex = layout.mineIndex
        enemyIndex = layout.enemyIndex
        mine.characterId = layout.mineCharacterId
        enemy.characterId = layout.enemyCharacterId
        FightStartSequence.resetSlot(mine, mineIndex)
        FightStartSequence.resetSlot(enemy, enemyIndex)
        mine.created = false
        enemy.created = false
        mine.dead = false
        enemy.dead = false
        introBackgroundActive = true
        duelBackgroundActive = false
        startRevealGroup = 0
        startCrossFade = 0f
        schedule(0.5f) { startRevealGroup = 1 }
        schedule(1f) { startRevealGroup = 2 }
        schedule(1.5f) { startRevealGroup = 3 }
        schedule(2f) {
            duelBackgroundActive = true
            startCrossFade = 0f
        }
        schedule(START_SECONDS) {
            introBackgroundActive = false
            startCrossFade = 1f
        }
        finishAt(START_SECONDS)
    }
    private fun beginShowUnit(command: ScenarioFightCommand.ShowUnit) {
        val side = command.side()
        val fighter = unit(side)
        fighter.created = true
        startAction(side, ACTION_ENTER)
        val authoredAt = duration(ACTION_ENTER)
        schedule(authoredAt) { startAction(side, command.entryAction) }
        val speechAt = authoredAt + duration(command.entryAction)
        scheduleSpeech(side, command.text, speechAt)
        finishAt(speechAt + speechDuration(command.text))
    }
    private fun beginShowStart() {
        startLabelsActive = true
        speeches.deactivateAll()
        schedule(SHOW_START_SECONDS) { startLabelsActive = false }
        finishAt(SHOW_START_SECONDS)
    }
    private fun beginSetAction(command: ScenarioFightCommand.SetAction) {
        val side = command.side()
        setForeground(side)
        startAction(side, command.action)
        pendingAnimationCallbacks = 1
        schedule(duration(command.action)) { pendingAnimationCallbacks-- }
        finishAt(duration(command.action))
    }
    private fun beginSay(command: ScenarioFightCommand.Say) {
        val side = command.side()
        scheduleSpeech(side, command.text, 0f, applyImmediately = true)
        finishAt(speechDuration(command.text))
    }
    private fun beginAttack2(command: ScenarioFightCommand.Attack2) = beginAttack(
        FightAttackPlanner.attack2(command, ::duration, ::hitTime),
    )
    private fun beginAttack1(command: ScenarioFightCommand.Attack1) = beginAttack(
        FightAttackPlanner.attack1(command, ::duration, ::hitTime),
    )
    private fun beginAttack(plan: FightAttackPlan) {
        setForeground(plan.attacker)
        startAction(plan.attacker, plan.attackerAction)
        pendingAnimationCallbacks = 1
        schedule(plan.attackerEndsAt) { pendingAnimationCallbacks-- }
        if (plan.defenderStartsImmediately) {
            startAction(plan.defender, plan.defenderAction)
            pendingAnimationCallbacks++
        }
        schedule(plan.hitAt) {
            lastHitAtSeconds = plan.hitAt
            emittedEvents += FightPresentationEvent.Hit(plan.attacker, plan.defender)
            startAction(plan.defender, plan.defenderAction)
            if (!plan.defenderStartsImmediately) pendingAnimationCallbacks++
        }
        schedule(plan.defenderEndsAt) {
            if (plan.completionClearsAllCallbacks) pendingAnimationCallbacks = 0 else pendingAnimationCallbacks--
        }
        finishAt(plan.duration)
    }
    private fun beginDeath(command: ScenarioFightCommand.Death) {
        val side = if (command.enemy) FightSide.ENEMY else FightSide.MINE
        startAction(side, ACTION_DEATH)
        pendingAnimationCallbacks = 1
        schedule(duration(ACTION_DEATH)) {
            pendingAnimationCallbacks = 0
            unit(side).dead = true
        }
        finishAt(duration(ACTION_DEATH))
    }
    private fun scheduleSpeech(side: FightSide, text: String, startsAt: Float, applyImmediately: Boolean = false) {
        val begin = {
            speeches.begin(side, text)
        }
        if (applyImmediately) begin() else schedule(startsAt, begin)
        typingContents(text).forEachIndexed { index, content ->
            schedule(startsAt + (index + 1) * TYPE_SECONDS) {
                emittedEvents += speeches.applyContent(side, content)
            }
        }
    }
    private fun speechDuration(text: String): Float = speeches.duration(text, TYPE_SECONDS, SPEECH_CLOSE_SECONDS)
    private fun setForeground(side: FightSide) {
        unit(side).zIndex = 1
        unit(side.other()).zIndex = 0
    }
    private fun startAction(side: FightSide, action: Int) {
        emittedEvents += actions.start(side, unit(side), commandElapsedSeconds, action)
    }

    private fun updateActionStates(at: Float) {
        emittedEvents += actions.advance(at, ::unit)
    }
    private fun duration(action: Int): Float = actions.duration(action)
    private fun hitTime(action: Int): Float = actions.hitTime(action)
    private fun schedule(at: Float, mutation: () -> Unit) {
        mutations += TimedMutation(at, mutationOrder++, mutation)
    }
    private fun finishAt(at: Float) {
        commandDurationSeconds = at
        val command = requireNotNull(currentCommand)
        schedule(at) { complete(command) }
    }
    private fun complete(command: ScenarioFightCommand) {
        currentCommand = null
        emittedEvents += FightPresentationEvent.CommandCompleted(command)
    }
    private fun ScenarioFightCommand.ShowUnit.side() = if (mine) FightSide.MINE else FightSide.ENEMY
    private fun ScenarioFightCommand.SetAction.side() = if (mine) FightSide.MINE else FightSide.ENEMY
    private fun ScenarioFightCommand.Say.side() = if (mine) FightSide.MINE else FightSide.ENEMY
    private fun ScenarioFightCommand.Attack1.side() = if (mine) FightSide.MINE else FightSide.ENEMY
    private fun ScenarioFightCommand.Attack2.side() = if (mine) FightSide.MINE else FightSide.ENEMY
    private fun FightSide.other() = if (this == FightSide.MINE) FightSide.ENEMY else FightSide.MINE

}
