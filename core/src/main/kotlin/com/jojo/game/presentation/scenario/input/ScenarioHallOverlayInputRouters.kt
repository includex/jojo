// Scenario
package com.jojo.game.presentation.scenario.input

import com.jojo.game.presentation.scenario.hall.HallLayerTapIntent

internal object ScenarioHallSaveInputRouter {
    sealed interface Command {
        data object CompletionTip : Command
        data class Confirm(val accepted: Boolean) : Command
        data object Cancel : Command
        data class SelectRow(val index: Int) : Command
        data object None : Command
    }

    fun route(x: Float, y: Float, completionTip: Boolean, pending: Boolean, rows: Int): Command {
        val sourceX = x / .86f; val sourceY = y / .86f
        if (completionTip) return if (sourceX in 654.186f..834.186f && sourceY in 271.285f..321.285f) Command.CompletionTip else Command.None
        if (pending) return when {
            sourceX in 554.186f..734.186f && sourceY in 271.285f..321.285f -> Command.Confirm(true)
            sourceX in 754.186f..934.186f && sourceY in 271.285f..321.285f -> Command.Confirm(false)
            else -> Command.None
        }
        if (sourceX in 1045.855f..1193.455f && sourceY in 100.162f..156.162f) return Command.Cancel
        if (sourceX !in 289.186f..1197.186f) return Command.None
        val row = (0 until minOf(rows, 8)).firstOrNull { sourceY in (547.534f - it * 52f)..(597.534f - it * 52f) }
        return row?.let(Command::SelectRow) ?: Command.None
    }
}

internal object ScenarioExclusiveInputRouter {
    enum class Command { SET_LIST, EXCLUSIVE_LIST, CLOSE, NONE }
    fun route(intent: HallLayerTapIntent): Command = when (intent) {
        HallLayerTapIntent.PRIMARY -> Command.SET_LIST
        HallLayerTapIntent.SECONDARY -> Command.EXCLUSIVE_LIST
        HallLayerTapIntent.CLOSE, HallLayerTapIntent.CANCEL -> Command.CLOSE
        HallLayerTapIntent.NONE -> Command.NONE
    }
}
