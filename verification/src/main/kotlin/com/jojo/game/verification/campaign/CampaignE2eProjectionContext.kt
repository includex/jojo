// Verification
package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.BattleRuntimeScreenProbe

/** CampaignE2eProjectionContext: core의 불변 스냅샷과 읽기 전용 질의 결과를 감싸는 검증 전용 문맥이다. */
internal data class CampaignE2eProjectionContext(
    /** screen: 검증 화면 상태를 담는다. */
    val screen: BattleRuntimeScreenProbe,
    /** authoredMechanicRoute: 검증 실행 계획을 담는다. */
    val authoredMechanicRoute: AuthoredMechanicRouteTracker,
)
