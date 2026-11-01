package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.*
import com.jojo.game.domain.scenario.PlaybackState

/** Exercises the mod through the unmodified production screen and input contracts. */
object LinchongModSmokeLauncher {
    @JvmStatic fun main(args: Array<String>) {
        val scenario = args.firstOrNull() ?: "S_90"
        val output = args.getOrNull(1) ?: "build/linchong-smoke/$scenario"
        var nextTap = 0f
        var capturedDialogue = false
        var readyFrames = 0
        var debugAt = 3f
        var checkedRescue = false
        var checkedEscape = false
        var attackSamples = 0
        var nextAttackSample = 0f
        var previousConfirmedDialogue: String? = null
        var escapedDialogue: String? = null
        var continuousDialogueTransitions = 0
        val driver = RuntimeBattleDriver { frame, probe ->
            escapedDialogue?.let {
                check(probe.playback == PlaybackState.DIALOGUE && probe.dialogueText == it) {
                    "Escape advanced or dismissed battle dialogue"
                }
                escapedDialogue = null
                println("LINCHONG_ESCAPE_DIALOGUE_PASS")
            }
            previousConfirmedDialogue?.let { previous ->
                check(probe.playback == PlaybackState.DIALOGUE && probe.dialogueText != null) {
                    "Dialogue panel disappeared between consecutive opening lines"
                }
                check(probe.dialogueText != previous) { "Dialogue confirmation did not advance the line" }
                continuousDialogueTransitions++
                previousConfirmedDialogue = null
                println("CONTINUOUS_DIALOGUE_TRANSITION $continuousDialogueTransitions")
            }
            if (probe.activeActionSourceAction in setOf(6, 7, 25, 48) &&
                attackSamples < 8 && frame.elapsed >= nextAttackSample) {
                capture("$output-combat-${attackSamples.toString().padStart(2, '0')}.png")
                println("SPRITE_COMBAT_SAMPLE actor=${probe.activeActionActorId} action=${probe.activeActionSourceAction} time=${frame.elapsed}")
                attackSamples++
                nextAttackSample = frame.elapsed + .12f
            }
            if (frame.elapsed >= debugAt) {
                println("MOD_STATE $scenario ${probe.playback} ${probe.turnPhase} win=${probe.winConditionsOpen} boot=${probe.bootstrapComplete}")
                capture("$output-current.png")
                debugAt += 15f
            }
            check(frame.elapsed < 90f) { "Timed out: $scenario ${probe.playback} ${probe.turnPhase}" }
            if (probe.playback == PlaybackState.DIALOGUE && !capturedDialogue && probe.dialogueTextComplete) {
                capture("$output-dialogue.png")
                capturedDialogue = true
                escapedDialogue = probe.dialogueText
                Gdx.input.inputProcessor?.keyDown(Input.Keys.ESCAPE)
                return@RuntimeBattleDriver emptyList()
            }
            if (frame.elapsed >= nextTap && (probe.playback == PlaybackState.DIALOGUE || probe.playback == PlaybackState.MODAL || probe.winConditionsOpen)) {
                // S90 opens with consecutive say calls: the very next rendered
                // frame must already contain the next line, without a blank delay.
                if (scenario == "S_90" && continuousDialogueTransitions < 3 &&
                    probe.playback == PlaybackState.DIALOGUE && probe.dialogueTextComplete) {
                    previousConfirmedDialogue = probe.dialogueText
                }
                val (x,y) = if (probe.winConditionsOpen) probe.winConditionButtonScreenX to probe.winConditionButtonScreenY
                    else probe.battleMenuButtonScreenX to probe.battleMenuButtonScreenY + 200
                Gdx.input.inputProcessor?.let { it.touchDown(x,y,0,Input.Buttons.LEFT); it.touchUp(x,y,0,Input.Buttons.LEFT) }
                nextTap = frame.elapsed + .25f
            }
            if (probe.bootstrapComplete && probe.turnPhase == "PLAYER_INPUT" && probe.playback != PlaybackState.DIALOGUE && !probe.winConditionsOpen) {
                if (++readyFrames == 8) {
                    if (!checkedEscape) {
                        verifyEscapeCancellation()
                        checkedEscape = true
                    }
                    val units = probe.battle.snapshot.units
                    check(units.any { it.characterId == 512 && it.hitPoints > 0 }) { "Missing living Lin Chong" }
                    check(units.all { (it.characterId ?: 512) >= 512 }) { "Original campaign actor leaked into mod" }
                    capture("$output-battle.png")
                    if (scenario == "S_90" && !checkedRescue) {
                        checkedRescue = true
                        readyFrames = 0
                        return@RuntimeBattleDriver listOf(RuntimeBattleCommand.EndTurn)
                    }
                    if (scenario == "S_90") {
                        check(continuousDialogueTransitions == 3) { "Dialogue continuity checks were not exercised" }
                        check(units.any { it.characterId == 513 && it.visible }) { "First enemy phase did not spawn Lu Zhishen" }
                        capture("$output-rescue.png")
                    }
                    Gdx.files.absolute("$output.txt").writeString("PASS $scenario original BattleScreen ready; units=${units.size}\n" + units.joinToString("\n") { "${it.characterId} (${it.x},${it.y}) hp=${it.hitPoints} ${it.faction}" }, false)
                    println("LINCHONG_SMOKE_PASS $scenario units=${units.size}")
                    Gdx.app.exit()
                }
            }
            emptyList()
        }
        val config = GameLaunchConfiguration(entryPoint = GameEntryPoint.BATTLE, initialScenario = scenario,
            initialScenarioExplicit = true, battleReturnScenario = scenario.replace("S_", "R_"),
            runtimeBattleDriver = driver, automatedRun = true)
        Lwjgl3Application(JojoGame(config), Lwjgl3ApplicationConfiguration().apply {
            setTitle("임충전 · 기존 엔진 검증"); setWindowedMode(1280,688); setForegroundFPS(60)
        })
    }
    /** Exercise the production InputProcessor without touching campaign data. */
    private fun verifyEscapeCancellation() {
        val game = Gdx.app.applicationListener as JojoGame
        val screen = game.screen
        val input = checkNotNull(Gdx.input.inputProcessor)
        fun field(name: String) = screen.javaClass.getDeclaredField(name).also { it.isAccessible = true }
        fun flag(name: String) = field(name).getBoolean(screen)
        fun escape() {
            input.keyDown(Input.Keys.ESCAPE)
            check(game.screen === screen) { "Escape left the active battle" }
        }
        escape()
        check(flag("battleMenuOpen")) { "Escape did not open battle menu" }
        escape()
        check(!flag("battleMenuOpen")) { "Escape did not close battle menu" }
        val selectMenu = screen.javaClass.getDeclaredMethod("handleBattleMenuTap", Int::class.javaPrimitiveType)
            .also { it.isAccessible = true }
        // Return-to-title prompt, save, load, settings, forces and information overlays.
        for (index in listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 13)) {
            escape()
            check(flag("battleMenuOpen"))
            selectMenu.invoke(screen, index)
            escape()
            check(!flag("returnToTitlePromptOpen"))
            // A second Escape must reach the battlefield and open its menu.
            escape()
            check(flag("battleMenuOpen")) { "Escape failed to close menu overlay $index" }
            escape()
        }
        field("magicMode").setBoolean(screen, true)
        escape()
        check(!flag("magicMode") && !flag("battleMenuOpen")) { "Escape failed to cancel targeting" }
        println("LINCHONG_ESCAPE_PASS menu,return-prompt,save,load,settings,forces,property,terrain,treasure,target")
    }

    private fun capture(path: String) {
        val file = Gdx.files.absolute(path); file.parent().mkdirs()
        val pixels = ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.backBufferWidth,Gdx.graphics.backBufferHeight)
        PixmapIO.writePNG(file,pixels,-1,true); pixels.dispose()
    }
}
