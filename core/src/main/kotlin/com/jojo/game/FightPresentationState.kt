package com.jojo.game

import kotlin.math.max

/** Logical side used by FightLayer after its `_mineIdx`/`_enemyIdx` lookup. */
enum class FightSide { MINE, ENEMY }

/**
 * Mutable transform owned by a FightUnit node and its animated sprite child.
 *
 * The animation data moves/flips the child. Before *every* subsequent action,
 * FightUnit._reset folds that child transform into the anchor parent. Keeping
 * both transforms is essential: setting only the next action reproduces the
 * frames but makes a fighter jump or face the wrong way between actions.
 */
/**
 * data class  `FightUnitPresentation`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
    /** Exact recovered FightUnit._reset contract. */
    fun resetAnimatedChild() {
        parentX += 4f * childX * if (parentScaleX < 0f) -1f else 1f
        childX = 0f
        if (childScaleX < 0f) parentScaleX *= -1f
        childScaleX = 1f
    }
}

/**
 * data class  `FightSpeechPresentation`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class FightSpeechPresentation(
    var active: Boolean = false,
    var sourceText: String = "",
    var content: String = "",
    var renderedText: String = "",
)

/** Child-node values sampled from the shipped animeFR curves. */
data class FightActionPose(
    val childX: Float = 0f,
    val childY: Float = 0f,
    val childScaleX: Float = 1f,
    val opacity: Float = 255f,
)

/**
 * sealed class  `FightPresentationEvent`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

sealed class FightPresentationEvent {
    /**
     * data class  `CommandStarted`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class CommandStarted(val command: ScenarioFightCommand) : FightPresentationEvent()

    /**
     * data class  `ActionStarted`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class ActionStarted(val side: FightSide, val action: Int) : FightPresentationEvent()

    /**
     * data class  `TextChanged`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class TextChanged(val side: FightSide, val renderedText: String) : FightPresentationEvent()

    /**
     * data class  `Hit`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Hit(val attacker: FightSide, val defender: FightSide) : FightPresentationEvent()

    /**
     * data class  `Sound`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Sound(
        val side: FightSide,
        val action: Int,
        val value: String,
        val atActionSeconds: Float,
    ) : FightPresentationEvent()

    /**
     * data class  `CommandCompleted`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class CommandCompleted(val command: ScenarioFightCommand) : FightPresentationEvent()
}

/**
 * Renderer-independent state machine for the recovered Cocos FightLayer.
 *
 * [begin] accepts exactly one ScenarioFightCommand. The owner renders the
 * public state, calls [advance], and acknowledges the AST only after the
 * matching [FightPresentationEvent.CommandCompleted]. No command is silently
 * fast-forwarded, so attack hit reactions and parallel animation callbacks
 * remain visible.
 */
/**
 * class  `FightPresentationState`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class FightPresentationState(
    private val actionDurations: Map<Int, Float> = SOURCE_ACTION_DURATIONS,
    private val actionHitTimes: Map<Int, Float> = SOURCE_ACTION_HIT_TIMES,
    /** Mirrors FightLayer's `this._cs[0].isMine()` slot selection. */
    private val isMineUnit: (Int) -> Boolean? = { null },
    /** Production supplies the animeFR sampler; timing-only tests may omit it. */
    private val actionPoseAt: ((Int, Float) -> FightActionPose)? = null,
    /** Production supplies callback1 crossings from the same animeFR asset. */
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

        /** Exact frame totals from the shipped `animeFR` FightUnit asset. */
        val SOURCE_ACTION_FRAMES: Map<Int, Int> = listOf(
            4, 4, 10, 8, 8, 4, 4, 4, 4, 10,
            18, 4, 24, 8, 792, 360, 12, 8, 12, 8,
            5, 5, 6, 8, 20, 5, 5, 8, 12, 11,
        ).mapIndexed { action, frames -> action to frames }.toMap()

        val SOURCE_ACTION_DURATIONS: Map<Int, Float> =
            SOURCE_ACTION_FRAMES.mapValues { (_, frames) -> frames * FRAME_SECONDS }

        /** `hit` event offsets in the same recovered animation asset. */
        val SOURCE_ACTION_HIT_FRAMES: Map<Int, Int> = mapOf(
            16 to 8,
            17 to 4,
            18 to 4,
            19 to 4,
            24 to 15,
        )
        val SOURCE_ACTION_HIT_TIMES: Map<Int, Float> =
            SOURCE_ACTION_HIT_FRAMES.mapValues { (_, frames) -> frames * FRAME_SECONDS }

        /** Number of Cocos scheduler callbacks used by say2 for this UTF-16 string. */
        fun typingTickCount(text: String): Int = typingContents(text).size

        /**
         * Raw RichText contents after each 0.04-second say2 callback.
         * Markup is consumed as a group, while surrogate pairs remain two JS
         * UTF-16 substring steps, exactly as in the recovered implementation.
         */
        /**
         * 공개 메서드 `typingContents`
         *
         * ### 파라미터
        - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `List<String>`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun typingContents(text: String): List<String> {
            var remaining = text
            var content = ""
            val result = mutableListOf<String>()
            do {
                var tagDepth = 0
                while (remaining.isNotEmpty()) {
                    val next = remaining.substring(0, 1)
                    remaining = remaining.substring(1)
                    content += next
                    if (next == "<") tagDepth++
                    else if (tagDepth > 0 && next == ">") tagDepth--
                    if (tagDepth == 0) break
                }
                result += content
            } while (remaining.isNotEmpty())
            return result
        }

        /**
         * 공개 메서드 `renderedRichText`
         *
         * ### 파라미터
        - `content` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun renderedRichText(content: String): String =
            if ("<color=" in content) "$content</c>" else content
    }

    private data class TimedMutation(val at: Float, val order: Int, val mutate: () -> Unit)

    val mine = FightUnitPresentation()
    val enemy = FightUnitPresentation()
    val mineSpeech = FightSpeechPresentation()
    val enemySpeech = FightSpeechPresentation()
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
    private val actionStartedAt = mutableMapOf<FightSide, Float>()
    private val actionSoundStartPending = mutableSetOf<FightSide>()
    private var nextMutation = 0
    private var mutationOrder = 0

    /**
     * 공개 메서드 `unit`
     *
     * ### 파라미터
    - `side` (`FightSide`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `FightUnitPresentation`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun unit(side: FightSide): FightUnitPresentation = if (side == FightSide.MINE) mine else enemy

    /**
     * 공개 메서드 `speech`
     *
     * ### 파라미터
    - `side` (`FightSide`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `FightSpeechPresentation`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun speech(side: FightSide): FightSpeechPresentation = if (side == FightSide.MINE) mineSpeech else enemySpeech

    /** Begin one command and return its source-faithful callback duration. */
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
        actionStartedAt.clear()
        actionSoundStartPending.clear()
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

    /**
     * Advance only the current command. Any excess time is intentionally not
     * applied to a future command; the AST owns the next-command boundary.
     */
    /**
     * 공개 메서드 `advance`
     *
     * ### 파라미터
    - `deltaSeconds` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<FightPresentationEvent>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun advance(deltaSeconds: Float): List<FightPresentationEvent> {
        require(deltaSeconds >= 0f) { "deltaSeconds must be non-negative" }
        if (currentCommand == null) return emptyList()
        val eventStart = emittedEvents.size
        val advancingCommand = currentCommand
        val target = (commandElapsedSeconds + deltaSeconds).coerceAtMost(commandDurationSeconds)
        while (nextMutation < mutations.size && mutations[nextMutation].at <= target + 0.000001f) {
            val mutation = mutations[nextMutation++]
            // Sample the outgoing action at the callback boundary before a
            // following startAction folds its child transform into parent.
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
        backgroundIndex = command.backgroundIndex
        mineIndex = if (isMineUnit(command.firstUnitId) ?: true) 0 else 1
        enemyIndex = if (mineIndex == 0) 1 else 0
        mine.characterId = if (mineIndex == 0) command.firstUnitId else command.secondUnitId
        enemy.characterId = if (enemyIndex == 0) command.firstUnitId else command.secondUnitId
        /**
         * 공개 메서드 `resetSlot`
         *
         * ### 파라미터
        - `fighter` (`FightUnitPresentation`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `slot` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun resetSlot(fighter: FightUnitPresentation, slot: Int) {
            fighter.parentX = if (slot == 0) -200f else 200f
            fighter.parentScaleX = if (slot == 0) -4f else 4f
            fighter.childX = 0f
            fighter.childScaleX = 1f
            fighter.action = null
            fighter.actionElapsedSeconds = 0f
            fighter.zIndex = 0
        }
        resetSlot(mine, mineIndex)
        resetSlot(enemy, enemyIndex)
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
        mineSpeech.active = false
        enemySpeech.active = false
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

    private fun beginAttack2(command: ScenarioFightCommand.Attack2) {
        require(command.style in 0..2) { "FightLayer.attack2 style must be 0..2" }
        val attacker = command.side()
        val defender = attacker.other()
        val attackerAction = when (command.style) {
            0 -> 16
            1 -> 17
            else -> 18
        }
        val defenderAction = when (command.style) {
            0, 1 -> if (command.defended) 20 else 21
            else -> 18
        }
        setForeground(attacker)
        startAction(attacker, attackerAction)
        pendingAnimationCallbacks = 1
        val attackerEnds = duration(attackerAction)
        schedule(attackerEnds) { pendingAnimationCallbacks-- }

        if (command.style == 2) {
            startAction(defender, defenderAction)
            pendingAnimationCallbacks++
        }
        val hitAt = hitTime(attackerAction)
        schedule(hitAt) {
            lastHitAtSeconds = hitAt
            emittedEvents += FightPresentationEvent.Hit(attacker, defender)
            // play() restarts the defender clip. Existing `once(finished)`
            // callbacks survive that restart, so all defender joins complete
            // at the new clip's finish.
            startAction(defender, defenderAction)
            if (command.style != 2) pendingAnimationCallbacks++
        }
        val defenderEnds = hitAt + duration(defenderAction)
        schedule(defenderEnds) {
            pendingAnimationCallbacks = 0
        }
        finishAt(max(attackerEnds, defenderEnds))
    }

    private fun beginAttack1(command: ScenarioFightCommand.Attack1) {
        require(command.style in 0..4) { "FightLayer.attack1 style must be 0..4" }
        val attacker = command.side()
        val defender = attacker.other()
        val attackerAction = if (command.critical) 24 else 19
        val defenderAction = when (command.style) {
            0 -> 21
            1 -> 20
            2 -> 22
            3 -> 27
            else -> 23
        }
        setForeground(attacker)
        startAction(attacker, attackerAction)
        pendingAnimationCallbacks = 1
        val attackerEnds = duration(attackerAction)
        schedule(attackerEnds) { pendingAnimationCallbacks-- }
        val hitAt = hitTime(attackerAction)
        schedule(hitAt) {
            lastHitAtSeconds = hitAt
            emittedEvents += FightPresentationEvent.Hit(attacker, defender)
            startAction(defender, defenderAction)
            pendingAnimationCallbacks++
        }
        val defenderEnds = hitAt + duration(defenderAction)
        schedule(defenderEnds) { pendingAnimationCallbacks-- }
        finishAt(max(attackerEnds, defenderEnds))
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
            val panel = speech(side)
            panel.active = true
            panel.sourceText = text
            panel.content = ""
            panel.renderedText = ""
        }
        if (applyImmediately) begin() else schedule(startsAt, begin)
        typingContents(text).forEachIndexed { index, content ->
            schedule(startsAt + (index + 1) * TYPE_SECONDS) {
                val rendered = renderedRichText(content)
                speech(side).content = content
                speech(side).renderedText = rendered
                emittedEvents += FightPresentationEvent.TextChanged(side, rendered)
            }
        }
    }

    private fun speechDuration(text: String): Float =
        typingTickCount(text) * TYPE_SECONDS + SPEECH_CLOSE_SECONDS

    private fun setForeground(side: FightSide) {
        unit(side).zIndex = 1
        unit(side.other()).zIndex = 0
    }

    private fun startAction(side: FightSide, action: Int) {
        val fighter = unit(side)
        fighter.resetAnimatedChild()
        fighter.action = action
        fighter.actionElapsedSeconds = 0f
        actionStartedAt[side] = commandElapsedSeconds
        actionSoundStartPending += side
        emittedEvents += FightPresentationEvent.ActionStarted(side, action)
    }

    private fun updateActionStates(at: Float) {
        actionStartedAt.forEach { (side, startedAt) ->
            val fighter = unit(side)
            val action = fighter.action ?: return@forEach
            val actionElapsed = (at - startedAt).coerceAtLeast(0f).coerceAtMost(duration(action))
            val includeStart = actionSoundStartPending.remove(side)
            actionSoundsCrossed?.invoke(
                action,
                fighter.actionElapsedSeconds,
                actionElapsed,
                includeStart,
            )?.forEach { sound ->
                emittedEvents += FightPresentationEvent.Sound(side, action, sound.value, sound.atSeconds)
            }
            fighter.actionElapsedSeconds = actionElapsed
            actionPoseAt?.invoke(action, actionElapsed)?.let { pose ->
                fighter.childX = pose.childX
                fighter.childScaleX = pose.childScaleX
            }
        }
    }

    private fun duration(action: Int): Float = requireNotNull(actionDurations[action]) {
        "missing recovered FightUnit duration for anime$action"
    }

    private fun hitTime(action: Int): Float = requireNotNull(actionHitTimes[action]) {
        "missing recovered FightUnit hit event for anime$action"
    }

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
