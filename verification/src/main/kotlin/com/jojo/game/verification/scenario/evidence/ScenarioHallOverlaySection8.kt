// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

/** appendFixture8: 검증 이벤트와 산출물을 기록한다. */
internal fun appendFixture8(writer: ScenarioHallOverlayEventWriter) = with(writer) {
                repeat(4) { index ->
                    event(
                        "HallLayer", "Canvas/Layer/button$index/Background", "sliced-sprite",
                        1041.372f + index * 96f, 2f, 96f, 96f, index.toString()
                    )
                }
                event("HallLayer", "Canvas/Layer/button/Background", "sprite", 36.047f, 370f, 60f, 60f, "menu")
}
