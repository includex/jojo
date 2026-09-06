// Verification
package com.jojo.game.verification.title

import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** StartItemRenderEvents: 초기 Login._launch에서 StartItemLayer id5로 이어지는 운영 그리기 목록이다. */
object StartItemRenderEvents {

    /** jsonl: 검증 상태를 JSONL 한 줄로 직렬화한다. */
    fun jsonl(): String {
        val log = RenderEventLog()
        val phase = "login-main-stable"
        log.draw(
            phase, "HallLayer", "Canvas/bg", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/resources/native/4d/4debf9ca-54d9-48e2-855c-34ef06c80bc4.5e28d.jpg#Logo_1-1"
        )
        log.draw(
            phase, "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
            opacity = 0f, visible = false
        )
        val ys = listOf(538f, 412f, 285f, 159f)
        ys.forEachIndexed { index, y ->
            log.draw(
                phase, "Login", "Canvas/Layer/bg1/button$index/Background", "sliced-sprite",
                1099.372f, y, 352f, 88f, "U_select_12-1_$index"
            )
        }
        return log.jsonl()
    }
}
