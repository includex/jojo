// Battle
package com.jojo.game.presentation.battle.bootstrap

import com.jojo.game.application.scenario.ScenarioStage

/** 초기 전투 인계 전에 끝나야 하는 유한 콜백의 진행 상태다. */
internal data class BattleBootstrapCallbackState(
    val move: Boolean = false,
    val attackAction: Boolean = false,
    val hide: Boolean = false,
    val show: Boolean = false,
    val fight: Boolean = false,
) {
    /** 아직 완료되지 않아 bootstrap 진행을 막는 콜백 이름을 원본 순서로 반환한다. */
    fun blockingReasons(): List<String> = buildList {
        if (move) add("move")
        if (attackAction) add("attackAction")
        if (hide) add("hide")
        if (show) add("show")
        if (fight) add("fight")
    }
}

/** 전투 작업이 아직 시작되지 않았을 때 한 번만 시작한다. */
internal fun completeInitialBattleOperation(stage: ScenarioStage) {
    if (!stage.battleOperationStarted) stage.startOperation()
}
