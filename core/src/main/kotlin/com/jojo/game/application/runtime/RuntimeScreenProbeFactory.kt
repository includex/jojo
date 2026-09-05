package com.jojo.game.application.runtime

import com.badlogic.gdx.Screen
import com.jojo.game.presentation.battle.BattleScreen
import com.jojo.game.presentation.battle.preparation.BattlePreparationScreen
import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.presentation.title.TitleScreen

/** Core-local bridge over presentation internals; only neutral probes cross module boundaries. */
internal fun Screen?.runtimeProbe(): RuntimeScreenProbe = when (this) {
    is TitleScreen -> TitleRuntimeProbe
    is ScenarioScreen -> runtimeProbe()

    is BattlePreparationScreen -> runtimeProbe()

    is BattleScreen -> runtimeProbe()
    null -> OtherRuntimeProbe("null")
    else -> OtherRuntimeProbe(javaClass.simpleName)
}
