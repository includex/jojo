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
        var attackSamples = 0
        var nextAttackSample = 0f
        val driver = RuntimeBattleDriver { frame, probe ->
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
            }
            if (frame.elapsed >= nextTap && (probe.playback == PlaybackState.DIALOGUE || probe.playback == PlaybackState.MODAL || probe.winConditionsOpen)) {
                val (x,y) = if (probe.winConditionsOpen) probe.winConditionButtonScreenX to probe.winConditionButtonScreenY
                    else probe.battleMenuButtonScreenX to probe.battleMenuButtonScreenY + 200
                Gdx.input.inputProcessor?.let { it.touchDown(x,y,0,Input.Buttons.LEFT); it.touchUp(x,y,0,Input.Buttons.LEFT) }
                nextTap = frame.elapsed + .25f
            }
            if (probe.bootstrapComplete && probe.turnPhase == "PLAYER_INPUT" && probe.playback != PlaybackState.DIALOGUE && !probe.winConditionsOpen) {
                if (++readyFrames == 8) {
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
    private fun capture(path: String) {
        val file = Gdx.files.absolute(path); file.parent().mkdirs()
        val pixels = ScreenUtils.getFrameBufferPixmap(0,0,Gdx.graphics.backBufferWidth,Gdx.graphics.backBufferHeight)
        PixmapIO.writePNG(file,pixels,-1,true); pixels.dispose()
    }
}
