package com.jojo.game.verification.title.evidence

internal fun writeSignInOverlayActions(context: LoginOptionalOverlayEventContext) {
    context.draw("Canvas/Layer/Logo_12-1/label", "label", 144.419800f, 28.793660f, 170.271400f, 46.784000f, null, 1f, listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), "행운 코인: 0")
    context.draw("Canvas/Layer/Logo_12-1/button2/Background", "sliced-sprite", 534.348960f, 27.692000f, 256.538000f, 48.160000f, "box3", 1f, listOf(770, 771), "")
    context.draw("Canvas/Layer/Logo_12-1/button2/Background/Label", "label", 619.617960f, 35.432000f, 86.000000f, 34.400000f, null, 1f, listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), "출석 체크")
}
