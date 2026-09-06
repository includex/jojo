// Verification
package com.jojo.game.verification.preparation

/** writeStartBattleBackdropEvents: 검증 이벤트와 산출물을 기록한다. */
internal fun writeStartBattleBackdropEvents(context: StartBattleRenderEventContext) = with(context) {
    context.draw("HallLayer", "Canvas/Layer/map", "sprite", 0.0f * scale, 0.0f * scale, 1488.372f * scale, 800.0f * scale, "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>", 1.0f, listOf(770, 771), true, "")
    context.draw("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0.0f * scale, 0.0f * scale, 1488.372f * scale, 800.0f * scale, "default_sprite_splash", 0.118f, listOf(770, 771), true, "")
    context.draw("StartBattleScreen", "Canvas/Layer/bg", "tiled-sprite", 160.536f * scale, 50.0f * scale, 1167.3f * scale, 700.0f * scale, "Logo_9-1", 1.0f, listOf(770, 771), true, "")
    context.draw("StartBattleScreen", "Canvas/Layer/bg/scrollview/view/content", "sprite", 167.186f * scale, 3.5f * scale, 800.0f * scale, 736.0f * scale, "U_select_4-1", 1.0f, listOf(770, 771), true, "")
}
