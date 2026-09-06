package com.jojo.game.presentation.battle

import com.jojo.game.ScenarioStage

/** 초기 전투 인계 전에 완료되어야 하는 콜백 상태입니다. */
internal data class BattleBootstrapCallbackState(
    val move: Boolean = false,
    val attackAction: Boolean = false,
    val hide: Boolean = false,
    val show: Boolean = false,
    val fight: Boolean = false,
) {
    /** 아직 완료되지 않은 초기화 차단 사유를 반환합니다. */
    fun blockingReasons(): List<String> = buildList {
        if (move) add("move")
        if (attackAction) add("attackAction")
        if (hide) add("hide")
        if (show) add("show")
        if (fight) add("fight")
    }
}

/** 초기 전투 작업이 시작되지 않았다면 시작합니다. */
internal fun completeInitialBattleOperation(stage: ScenarioStage) {
    if (!stage.battleOperationStarted) stage.startOperation()
}
