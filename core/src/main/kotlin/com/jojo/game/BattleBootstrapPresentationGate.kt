package com.jojo.game

/** Callback-owning stage work which may delay the UNKNOWN -> Mine hand-off. */
internal data class BattleBootstrapCallbackState(
    val move: Boolean = false,
    val attackAction: Boolean = false,
    val hide: Boolean = false,
    val show: Boolean = false,
    val fight: Boolean = false,
) {
    /**
     * 공개 메서드 `blockingReasons`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<String>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun blockingReasons(): List<String> = buildList {
        if (move) add("move")
        if (attackAction) add("attackAction")
        if (hide) add("hide")
        if (show) add("show")
        if (fight) add("fight")
    }
}

/** BattleScreen._execControlScript(true)'s completion callback. */
internal fun completeInitialBattleOperation(stage: ScenarioStage) {
    if (!stage.battleOperationStarted) stage.startOperation()
}
