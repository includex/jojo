// Verification
package com.jojo.game.verification.scenario.evidence

import com.jojo.game.presentation.scenario.*

/** appendHelperPart6: 검증 이벤트와 산출물을 기록한다. */
internal fun appendHelperPart6(writer: ScenarioHelperEventWriter) = with(writer) {
        draw(
            "Canvas/Layer/Logo_12-1/button0/Background",
            "sliced-sprite",
            1172.451f,
            32.187f,
            147.6f,
            56.0f,
            "box3",
            "",
            true
        )
        draw(
            "Canvas/Layer/Logo_12-1/button0/Background/Label",
            "label",
            1196.251f,
            41.187f,
            100.0f,
            40.0f,
            null,
            "확인",
            true
        )
}
