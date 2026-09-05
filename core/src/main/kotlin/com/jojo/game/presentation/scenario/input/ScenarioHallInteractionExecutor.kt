package com.jojo.game.presentation.scenario.input

import com.jojo.game.presentation.scenario.hall.HallInteractionIntent

interface ScenarioHallInteractionPort {
    fun startBattle()
    fun openManagement(kindName: String)
    fun selectHallMenu(index: Int)
}

internal object ScenarioHallInteractionExecutor {
    fun execute(intent: HallInteractionIntent, port: ScenarioHallInteractionPort) = when (intent) {
        HallInteractionIntent.None, HallInteractionIntent.MenuClosed, HallInteractionIntent.OpenMenu -> Unit
        HallInteractionIntent.StartBattle -> port.startBattle()
        is HallInteractionIntent.OpenManagement -> port.openManagement(intent.kind.name)
        is HallInteractionIntent.MenuSelection -> port.selectHallMenu(intent.index)
    }
}
