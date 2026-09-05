package com.jojo.game.verification.title.evidence

internal fun writeSignInOverlayChrome(context: LoginOptionalOverlayEventContext) {
    context.draw("Canvas/Layer/Panel_cancel", "sprite", 0.000000f, 0.000000f, 1279.999920f, 688.000000f, "default_sprite_splash", 0.392f, listOf(770, 771), "")
    context.draw("Canvas/Layer/Logo_12-1", "tiled-sprite", 127.009960f, 21.070000f, 1025.980000f, 645.860000f, "Logo_12-1", 1f, listOf(770, 771), "")
    context.draw("Canvas/Layer/Logo_12-1/box4", "sliced-sprite", 127.009960f, 21.070000f, 1025.980000f, 645.860000f, "box4", 1f, listOf(770, 771), "")
    context.draw("Canvas/Layer/Logo_12-1/bg1", "sprite", 127.009960f, 615.330000f, 1025.980000f, 51.600000f, "bg1", 1f, listOf(770, 771), "")
    context.draw("Canvas/Layer/Logo_12-1/bg1/box3", "sliced-sprite", 127.009960f, 615.330000f, 1025.980000f, 51.600000f, "box3", 1f, listOf(770, 771), "")
    context.draw("Canvas/Layer/Logo_12-1/bg1/label", "label", 604.739960f, 620.318000f, 130.298600f, 45.064000f, null, 1f, listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), "출석 체크")
    context.draw("Canvas/Layer/Logo_12-1/bg1/label2", "label", 746.602120f, 617.738000f, 389.201600f, 46.784000f, null, 1f, listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), "연속으로 출석했습니다0하늘")
    context.draw("Canvas/Layer/Logo_12-1/scrollview", "tiled-sprite", 142.913080f, 85.140000f, 994.166880f, 529.760000f, "Logo_12-1", 1f, listOf(770, 771), "")
    context.draw("Canvas/Layer/Logo_12-1/scrollview/box2", "tiled-sprite", 142.913080f, 85.140000f, 994.166880f, 529.760000f, "box2", 1f, listOf(770, 771), "")
}
