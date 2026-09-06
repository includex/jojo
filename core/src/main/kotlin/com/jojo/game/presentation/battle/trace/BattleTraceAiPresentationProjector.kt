// Battle
package com.jojo.game.presentation.battle.trace

import com.jojo.game.application.runtime.RuntimeBattleTraceAiPresentationInput
import com.jojo.game.domain.battle.AiUnitResolution

/** AI 추적 투영기: 화면의 AI 연출 상태를 런타임 추적 입력으로 변환한다. */
internal object BattleTraceAiPresentationProjector {
    /** 투영: AI 해상도와 화면 유닛 정보를 추적 가능한 불변 입력으로 만든다. */
    fun project(
        stage: String,
        resolution: AiUnitResolution?,
        actorCharacterId: Int,
        targetCharacterId: Int,
        targetHealthBeforeAction: Int,
        hasPendingAction: Boolean,
    ): RuntimeBattleTraceAiPresentationInput? = resolution?.let {
        RuntimeBattleTraceAiPresentationInput(
            stage, actorCharacterId, it.fromX, it.fromY, it.toX, it.toY,
            targetCharacterId, targetHealthBeforeAction, hasPendingAction, it.result != null,
        )
    }
}
