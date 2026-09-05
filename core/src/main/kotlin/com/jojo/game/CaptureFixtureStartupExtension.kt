package com.jojo.game

import com.badlogic.gdx.Screen
import java.util.ServiceLoader

/** Neutral production seam for optional, externally supplied capture fixtures. */
data class CaptureFixtureStartupRequest(
    val game: JojoGame,
    val captureState: String?,
    val showScreen: (Screen) -> Unit,
)

interface CaptureFixtureStartupExtension {
    fun route(request: CaptureFixtureStartupRequest): Boolean
}

internal object CaptureFixtureStartupExtensions {
    fun route(request: CaptureFixtureStartupRequest): Boolean =
        ServiceLoader.load(CaptureFixtureStartupExtension::class.java).any { it.route(request) }
}
