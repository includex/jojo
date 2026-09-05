package com.jojo.game.application.runtime

import com.jojo.game.*

/** Neutral optional evidence contract implemented by external runtime screens. */
interface RuntimeRenderEventLogProvider {
    fun runtimeRenderEventLog(): String
}

interface RuntimeCompositionTraceProvider {
    fun runtimeCompositionTrace(): String
}
