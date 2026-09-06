// Verification
package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.RuntimeScreenProbe

/** CampaignE2eRuntimeObserver: core가 verification에 의존하지 않도록 캠페인 E2E 구동을 검증 모듈에서 설치한다. */
class CampaignE2eRuntimeObserver(config: CampaignE2eTraceConfig) : RuntimeScreenObserver {
    /** driver: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private val driver = CampaignE2eDriver(config)

    /** update: 검증 상태를 입력에 맞게 갱신한다. */
    override fun update(delta: Float, screen: RuntimeScreenProbe) = driver.update(delta, screen)

    /** scenarioStarted: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    override fun scenarioStarted(module: String, index: Int) = driver.scenarioStarted(module, index)
}
