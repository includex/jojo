package com.jojo.game.verification.fixture

import com.badlogic.gdx.Screen
import com.jojo.game.CaptureFixtureStartupExtension
import com.jojo.game.CaptureFixtureStartupRequest
import com.jojo.game.domain.battle.Faction
import com.jojo.game.presentation.battle.unit.BattleSpriteFixtureScreen

/** Verification-owned `sprite:` fixture route installed through the core seam. */
class SpriteCaptureFixtureStartupExtension : CaptureFixtureStartupExtension {
    override fun route(request: CaptureFixtureStartupRequest): Boolean {
        val fixture = parseSpriteFixtureRequest(request.captureState) ?: return false
        request.showScreen(
            BattleSpriteFixtureScreen(
                request.game, fixture.characterId, fixture.action, fixture.direction, fixture.frameTick, fixture.faction,
            ),
        )
        return true
    }
}

internal data class SpriteFixtureRequest(
    val characterId: Int,
    val action: Int,
    val direction: Int,
    val frameTick: Int,
    val faction: Faction,
)

internal fun parseSpriteFixtureRequest(captureState: String?): SpriteFixtureRequest? {
    val parts = captureState?.takeIf { it.startsWith("sprite:") }?.split(':') ?: return null
    require(parts.size == 6) { "sprite fixture requires character:action:dir:tick:camp" }
    val faction = when (parts[5].toInt()) {
        0 -> Faction.PLAYER; 1 -> Faction.FRIEND; 2 -> Faction.ENEMY; 3 -> Faction.REINFORCEMENTS
        else -> error("sprite fixture camp must be 0, 1, 2, or 3")
    }
    return SpriteFixtureRequest(parts[1].toInt(), parts[2].toInt(), parts[3].toInt(), parts[4].toInt(), faction)
}
