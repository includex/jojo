// Runtime
package com.jojo.game.application.runtime

import com.badlogic.gdx.Screen
import com.jojo.game.presentation.battle.BattleScreen
import com.jojo.game.presentation.battle.preparation.BattlePreparationScreen
import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.presentation.title.TitleScreen

/** RuntimeScreenProbeFactory: libGDX 화면 객체를 자동 구동에 필요한 유형별 RuntimeScreenProbe로 변환한다. */
internal fun Screen?.runtimeProbe(): RuntimeScreenProbe = when (this) {
    is TitleScreen -> runtimeProbe()
    is ScenarioScreen -> runtimeProbe()

    is BattlePreparationScreen -> runtimeProbe()

    is BattleScreen -> runtimeProbe()
    null -> OtherRuntimeProbe("null")
    else -> OtherRuntimeProbe(javaClass.simpleName)
}
