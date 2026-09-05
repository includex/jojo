package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.BattleRuntimeScreenProbe

/** Verification-owned context over the core's immutable snapshot and read-only query probe. */
internal data class CampaignE2eProjectionContext(
    val screen: BattleRuntimeScreenProbe,
    val authoredMechanicRoute: AuthoredMechanicRouteTracker,
)
