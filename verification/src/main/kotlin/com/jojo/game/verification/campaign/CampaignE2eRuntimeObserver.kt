package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.RuntimeScreenProbe

/** Installs campaign E2E driving from verification without creating a core-to-verification dependency. */
class CampaignE2eRuntimeObserver(config: CampaignE2eTraceConfig) : RuntimeScreenObserver {
    private val driver = CampaignE2eDriver(config)

    override fun update(delta: Float, screen: RuntimeScreenProbe) = driver.update(delta, screen)

    override fun scenarioStarted(module: String, index: Int) = driver.scenarioStarted(module, index)
}
