package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FightPresentationStateTest {
    private val fightId = 7L

    private fun started(): FightPresentationState = FightPresentationState().also {
        assertClose(4f, it.begin(ScenarioFightCommand.Start(fightId, 3, 134, 5, 2)))
        it.advance(4f)
        assertTrue(it.commandComplete)
    }

    @Test
    fun `shipped FightUnit action timings retain exact S01 frame totals`() {
        val expected = mapOf(0 to 4, 1 to 4, 2 to 10, 6 to 4, 11 to 4, 17 to 8, 21 to 5, 28 to 12, 29 to 11)
        expected.forEach { (action, frames) ->
            assertClose(frames / 24f, requireNotNull(FightPresentationState.SOURCE_ACTION_DURATIONS[action]))
        }
        assertClose(4f / 24f, requireNotNull(FightPresentationState.SOURCE_ACTION_HIT_TIMES[17]))
    }

    @Test
    fun `Start reproduces three half-second reveals and two-second crossfade`() {
        val state = FightPresentationState()
        val command = ScenarioFightCommand.Start(fightId, 3, 134, 5, 2)

        assertClose(4f, state.begin(command))
        assertTrue(state.introBackgroundActive)
        assertFalse(state.duelBackgroundActive)
        state.advance(0.5f)
        assertEquals(1, state.startRevealGroup)
        state.advance(0.5f)
        assertEquals(2, state.startRevealGroup)
        state.advance(0.5f)
        assertEquals(3, state.startRevealGroup)
        state.advance(0.5f)
        assertTrue(state.duelBackgroundActive)
        assertFalse(state.commandComplete)
        state.advance(1f)
        assertClose(0.5f, state.startCrossFade)
        state.advance(1f)

        assertTrue(state.commandComplete)
        assertFalse(state.introBackgroundActive)
        assertClose(1f, state.startCrossFade)
        assertEquals(fightId, state.activeFightId)
    }

    @Test
    fun `ShowUnit runs ENTER authored action and UTF-16 typing in sequence`() {
        val state = started()
        val text = "가😀"
        val command = ScenarioFightCommand.ShowUnit(fightId, true, text, 9)
        val expected = 11f / 24f + 10f / 24f + 3 * 0.04f + 1.6f

        assertClose(expected, state.begin(command))
        assertEquals(29, state.mine.action)
        state.mine.childX = 7f
        state.mine.childScaleX = -1f
        state.advance(11f / 24f)
        assertEquals(9, state.mine.action)
        assertClose(-228f, state.mine.parentX)
        assertClose(4f, state.mine.parentScaleX)
        assertClose(0f, state.mine.childX)
        assertClose(1f, state.mine.childScaleX)

        state.advance(10f / 24f)
        assertTrue(state.mineSpeech.active)
        assertEquals("", state.mineSpeech.renderedText)
        state.advance(0.04f)
        assertEquals("가", state.mineSpeech.renderedText)
        state.advance(0.04f)
        // JS substring(0, 1) exposes the high surrogate for one scheduler tick.
        assertEquals(2, state.mineSpeech.content.length)
        state.advance(0.04f)
        assertEquals(text, state.mineSpeech.renderedText)
        state.advance(1.6f)
        assertTrue(state.commandComplete)
    }

    @Test
    fun `action callback1 is emitted once even when advance lands on the same boundary twice`() {
        val state = FightPresentationState(actionSoundsCrossed = { action, from, to, includeStart ->
            if (action == 6 && includeStart && from == 0f && to >= 0f) {
                listOf(FightSpriteTimeline.SoundEvent(0f, "8"))
            } else emptyList()
        })
        state.begin(ScenarioFightCommand.Start(fightId, 3, 134, 5, 2))
        state.advance(4f)
        state.begin(ScenarioFightCommand.SetAction(fightId, true, 6))

        val first = state.advance(0f).filterIsInstance<FightPresentationEvent.Sound>()
        val second = state.advance(0f).filterIsInstance<FightPresentationEvent.Sound>()
        state.advance(4f / 24f)

        assertEquals(listOf("8"), first.map { it.value })
        assertTrue(second.isEmpty())
        assertEquals(1, state.emittedEvents.filterIsInstance<FightPresentationEvent.Sound>().size)
    }

    @Test
    fun `ShowStart hides speech and acknowledges after exactly one second`() {
        val state = started()
        state.mineSpeech.active = true
        state.enemySpeech.active = true

        assertClose(1f, state.begin(ScenarioFightCommand.ShowStart(fightId)))
        assertTrue(state.startLabelsActive)
        assertFalse(state.mineSpeech.active)
        assertFalse(state.enemySpeech.active)
        state.advance(0.999f)
        assertFalse(state.commandComplete)
        state.advance(0.001f)
        assertFalse(state.startLabelsActive)
        assertTrue(state.commandComplete)
    }

    @Test
    fun `SetAction applies FightUnit anchor-child reset before playing`() {
        val state = started()
        state.enemy.parentX = 100f
        state.enemy.parentScaleX = -1f
        state.enemy.childX = 5f
        state.enemy.childScaleX = -1f

        assertClose(4f / 24f, state.begin(ScenarioFightCommand.SetAction(fightId, false, 6)))
        assertEquals(6, state.enemy.action)
        assertClose(80f, state.enemy.parentX)
        assertClose(1f, state.enemy.parentScaleX)
        assertEquals(1, state.enemy.zIndex)
        assertEquals(0, state.mine.zIndex)
        state.advance(4f / 24f)
        assertTrue(state.commandComplete)
    }

    @Test
    fun `production pose sampler carries authored movement into next action anchor`() {
        val state = FightPresentationState(actionPoseAt = { action, elapsed ->
            if (action == 2 && elapsed >= 10f / 24f) FightActionPose(childX = -50f)
            else FightActionPose()
        })
        state.begin(ScenarioFightCommand.Start(fightId, 3, 134, 5, 2))
        state.advance(4f)
        val duration = state.begin(ScenarioFightCommand.ShowUnit(fightId, true, "", 2))
        state.advance(duration)

        assertClose(-50f, state.mine.childX)
        state.begin(ScenarioFightCommand.SetAction(fightId, true, 0))
        // Source slot 0 starts at -200 with scaleX=-4; _reset adds
        // 4 * -50 * -1, placing the mounted attacker at x=0.
        assertClose(0f, state.mine.parentX)
        assertClose(-4f, state.mine.parentScaleX)
    }

    @Test
    fun `Say groups rich-text tags and counts emoji as two UTF-16 ticks`() {
        assertEquals(listOf("<color=#fff>", "<color=#fff>가", "<color=#fff>가</color>"),
            FightPresentationState.typingContents("<color=#fff>가</color>"))
        assertEquals(3, FightPresentationState.typingTickCount("가😀"))

        val state = started()
        val command = ScenarioFightCommand.Say(fightId, false, "<color=#fff>가</color>", false)
        assertClose(3 * 0.04f + 1.6f, state.begin(command))
        state.advance(0.04f)
        assertEquals("<color=#fff></c>", state.enemySpeech.renderedText)
        state.advance(0.08f)
        assertEquals("<color=#fff>가</color></c>", state.enemySpeech.renderedText)
        state.advance(1.6f)
        assertTrue(state.commandComplete)
    }

    @Test
    fun `Attack2 waits for hit-started victim action instead of attacker finish`() {
        val state = started()
        val command = ScenarioFightCommand.Attack2(fightId, true, 1, false)

        assertClose(9f / 24f, state.begin(command))
        assertEquals(17, state.mine.action)
        assertEquals(1, state.mine.zIndex)
        state.advance(4f / 24f)
        assertClose(4f / 24f, requireNotNull(state.lastHitAtSeconds))
        assertEquals(21, state.enemy.action)
        assertEquals(2, state.pendingAnimationCallbacks)
        state.advance(4f / 24f)
        assertFalse(state.commandComplete, "attacker finished, but hit reaction still owns the join")
        assertEquals(1, state.pendingAnimationCallbacks)
        state.advance(1f / 24f)
        assertTrue(state.commandComplete)
        assertEquals(0, state.pendingAnimationCallbacks)
    }

    @Test
    fun `Attack1 is a first-class joined command with recovered actions`() {
        val state = started()
        val command = ScenarioFightCommand.Attack1(fightId, false, 3, false)

        // anime19 hits at 4/24 and ends at 8/24; anime27 reaction lasts 8/24.
        assertClose(12f / 24f, state.begin(command))
        assertEquals(19, state.enemy.action)
        state.advance(4f / 24f)
        assertEquals(27, state.mine.action)
        assertFalse(state.commandComplete)
        state.advance(8f / 24f)
        assertTrue(state.commandComplete)
    }

    @Test
    fun `Death targets source enemy flag and End completes synchronously`() {
        val state = started()
        val death = ScenarioFightCommand.Death(fightId, enemy = true)

        assertClose(0.5f, state.begin(death))
        assertEquals(28, state.enemy.action)
        state.advance(0.5f)
        assertTrue(state.enemy.dead)
        assertTrue(state.commandComplete)

        val end = ScenarioFightCommand.End(fightId)
        assertClose(0f, state.begin(end))
        assertTrue(state.commandComplete)
        assertNull(state.activeFightId)
        assertIs<FightPresentationEvent.CommandCompleted>(state.emittedEvents.last())
    }

    @Test
    fun `S18 AST preserves attack1 instead of treating it as an unhandled instant call`() {
        val runtime = PythonAstRuntime.load("S_18")
        runtime.enableExternalFightPresentation()
        runtime.setScriptVariables((0..100).associateWith { if (it == 47) 0 else 1 })
        runtime.setBattleContext(
            PythonAstRuntime.BattleScriptContext(
                round = 1,
                camp = 0,
                positions = mapOf(15 to (10 to 10), 7 to (11 to 10)),
                activeCharacterIds = setOf(15, 7),
            ),
        )

        runtime.start("scene1")
        assertEquals("15", runtime.currentDialogue?.speakerId)
        runtime.advanceDialogue()
        assertEquals("7", runtime.currentDialogue?.speakerId)
        runtime.advanceDialogue()

        var attack: ScenarioFightCommand.Attack1? = null
        repeat(20) {
            val commands = runtime.stage.consumeFightCommands()
            commands.filterIsInstance<ScenarioFightCommand.Attack1>().firstOrNull()?.let { attack = it }
            if (attack != null) return@repeat
            // An authored stage.delay has no presentation command; external
            // FightLayer waits always have exactly one queued command.
            if (commands.isEmpty()) runtime.skipDelay() else runtime.resumeExternalDelay()
        }

        assertEquals(ScenarioFightCommand.Attack1(requireNotNull(attack).fightId, false, 3, true), attack)
        assertFalse("fight.attack1" in runtime.unhandledCalls)
    }

    @Test
    fun `S01 actual AST duel stays FIFO through delay20 and continues past End automatically`() {
        fun ScenarioFightCommand.kind(): String = when (this) {
            is ScenarioFightCommand.Start -> "Start"
            is ScenarioFightCommand.ShowUnit -> "ShowUnit"
            is ScenarioFightCommand.ShowStart -> "ShowStart"
            is ScenarioFightCommand.SetAction -> "SetAction"
            is ScenarioFightCommand.Say -> "Say"
            is ScenarioFightCommand.Attack1 -> "Attack1"
            is ScenarioFightCommand.Attack2 -> "Attack2"
            is ScenarioFightCommand.Death -> "Death"
            is ScenarioFightCommand.End -> "End"
        }
        val runtime = PythonAstRuntime.load("S_01")
        runtime.enableExternalFightPresentation()
        runtime.setScriptVariables(
            mapOf(11 to 1, 12 to 1, 20 to 1, 21 to 1, 30 to 1, 31 to 1, 40 to 0, 41 to 1),
        )
        runtime.setBattleContext(
            PythonAstRuntime.BattleScriptContext(
                round = 1,
                camp = 0,
                positions = mapOf(3 to (5 to 5), 134 to (6 to 5)),
                activeCharacterIds = setOf(3, 134),
            ),
        )
        runtime.start("scene1")
        runtime.advanceDialogue()
        runtime.advanceDialogue()

        val state = FightPresentationState()
        val pending = ArrayDeque<ScenarioFightCommand>()
        pending.addAll(runtime.stage.consumeFightCommands())
        val trace = mutableListOf<String>()
        val authoredDelayTrace = mutableListOf<String>()
        var sequence = 0
        var postEndAstObservedBeforeEndAck = false
        while (trace.none { it.endsWith(":End") }) {
            if (pending.isEmpty()) {
                // S_01 has one authored stage.delay between Attack2 and Say;
                // it is not a FightLayer callback and produces no command.
                assertEquals(PlaybackState.DELAY, runtime.state)
                assertEquals("Attack2", trace.last { it.startsWith("resume:") }.substringAfterLast(':'))
                authoredDelayTrace += "delay20:start"
                runtime.update(1.999f, autoCloseUi = false)
                assertEquals(PlaybackState.DELAY, runtime.state)
                assertTrue(runtime.stage.consumeFightCommands().isEmpty())
                // Cross the 2.0 s boundary with a small margin so the test
                // does not depend on Float representation of 1.999 + .001.
                runtime.update(0.002f, autoCloseUi = false)
                authoredDelayTrace += "delay20:complete"
                pending.addAll(runtime.stage.consumeFightCommands())
            }
            val command = pending.removeFirst()
            if (command is ScenarioFightCommand.End) {
                // fight.end is synchronous. The resume owned by the final
                // fight.say has already evaluated hide/getItem/setAction/say
                // before the renderer acknowledges the queued End command.
                assertFalse(runtime.stage.unit(134).visible)
                assertTrue(127 in runtime.stage.acquiredItems)
                assertEquals(PlaybackState.DIALOGUE, runtime.state)
                assertEquals(Dialogue("135", "아, 화웅 장군이 패배했다!"), runtime.currentDialogue)
                postEndAstObservedBeforeEndAck = true
            }
            sequence++
            trace += "begin:$sequence:${command.kind()}"
            val duration = state.begin(command)
            state.advance(duration)
            assertTrue(state.commandComplete)
            trace += "complete:$sequence:${command.kind()}"
            if (command !is ScenarioFightCommand.End) {
                assertEquals(PlaybackState.DELAY, runtime.state)
                runtime.resumeExternalDelay()
                trace += "resume:$sequence:${command.kind()}"
                pending.addAll(runtime.stage.consumeFightCommands())
            }
        }

        val begun = trace.filter { it.startsWith("begin:") }.map { it.substringAfterLast(':') }
        val completed = trace.filter { it.startsWith("complete:") }.map { it.substringAfterLast(':') }
        val resumed = trace.filter { it.startsWith("resume:") }.map { it.substringAfterLast(':') }
        val expected = listOf(
            "Start", "ShowUnit", "ShowUnit", "ShowStart", "SetAction", "Say", "SetAction", "Say",
            "SetAction", "Attack2", "Say", "SetAction", "Death", "SetAction", "SetAction", "Say", "End",
        )
        assertEquals(expected, begun)
        assertEquals(expected, completed)
        assertEquals(expected.dropLast(1), resumed)
        assertEquals(listOf("delay20:start", "delay20:complete"), authoredDelayTrace)
        assertTrue(postEndAstObservedBeforeEndAck)
        assertEquals(PlaybackState.DIALOGUE, runtime.state)
        assertEquals(Dialogue("135", "아, 화웅 장군이 패배했다!"), runtime.currentDialogue)
    }

    @Test
    fun `S01 ordinary dialogue close clears stale external delay before fight transition`() {
        val runtime = PythonAstRuntime.load("S_01")
        runtime.enableExternalFightPresentation()
        runtime.setScriptVariables(
            mapOf(11 to 1, 12 to 1, 20 to 1, 21 to 1, 30 to 1, 31 to 1, 40 to 0, 41 to 1),
        )
        runtime.setBattleContext(
            PythonAstRuntime.BattleScriptContext(
                round = 1,
                camp = 0,
                positions = mapOf(3 to (5 to 5), 134 to (6 to 5)),
                activeCharacterIds = setOf(3, 134),
            ),
        )

        runtime.start("scene1")
        runtime.advanceDialogue() // first speaker page
        assertEquals(Dialogue("134", "흥, 아무도 아니야!"), runtime.currentDialogue)

        // Reproduce the stale Float.MAX_VALUE left by an earlier external
        // FightLayer pause.  This close is an ordinary stage.say, so it has
        // no externalDialogueReturnState and must reset that timer before
        // the deferred SayLayer callback frame resumes the AST.
        val delay = PythonAstRuntime::class.java
            .getDeclaredField("delayRemainingSeconds")
            .also { it.isAccessible = true }
        delay.setFloat(runtime, Float.MAX_VALUE)

        runtime.advanceDialogue(deferCloseCallbackFrame = true)
        runtime.update(1f / 60f)
        runtime.update(1f / 60f)

        assertEquals(PlaybackState.DELAY, runtime.state)
        assertIs<ScenarioFightCommand.Start>(runtime.stage.consumeFightCommands().single())
    }

    private fun assertClose(expected: Float, actual: Float) {
        assertTrue(kotlin.math.abs(expected - actual) < 0.00001f, "expected=$expected actual=$actual")
    }
}
