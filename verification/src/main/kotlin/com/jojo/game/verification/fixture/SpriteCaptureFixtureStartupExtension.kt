// Verification
package com.jojo.game.verification.fixture

import com.jojo.game.application.runtime.RuntimeStartupRequest

import com.badlogic.gdx.Screen
import com.jojo.game.application.runtime.RuntimeStartupExtension
import com.jojo.game.domain.battle.Faction
import com.jojo.game.presentation.battle.unit.BattleSpriteFixtureScreen

/** SpriteCaptureFixtureStartupExtension: core 연결부를 통해 설치되는 검증 전용 sprite: 픽스처 경로이다. */
class SpriteCaptureFixtureStartupExtension : RuntimeStartupExtension {
    /** route: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    override fun route(request: RuntimeStartupRequest): Boolean {
        val fixture = parseSpriteFixtureRequest(request.state) ?: return false
        request.showScreen(
            BattleSpriteFixtureScreen(
                request.game, fixture.characterId, fixture.action, fixture.direction, fixture.frameTick, fixture.faction,
            ),
        )
        return true
    }
}

/** SpriteFixtureRequest: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class SpriteFixtureRequest(
    /** characterId: 전투 무장 상태를 담는다. */
    val characterId: Int,
    /** action: 검증 입력 정보를 담는다. */
    val action: Int,
    /** direction: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val direction: Int,
    /** frameTick: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val frameTick: Int,
    /** faction: 전투 무장 상태를 담는다. */
    val faction: Faction,
)

/** parseSpriteFixtureRequest: 외부 입력을 검증 모델로 해석한다. */
internal fun parseSpriteFixtureRequest(captureState: String?): SpriteFixtureRequest? {
    val parts = captureState?.takeIf { it.startsWith("sprite:") }?.split(':') ?: return null
    require(parts.size == 6) { "sprite fixture requires character:action:dir:tick:camp" }
    val faction = when (parts[5].toInt()) {
        0 -> Faction.PLAYER; 1 -> Faction.FRIEND; 2 -> Faction.ENEMY; 3 -> Faction.REINFORCEMENTS
        else -> error("sprite fixture camp must be 0, 1, 2, or 3")
    }
    return SpriteFixtureRequest(parts[1].toInt(), parts[2].toInt(), parts[3].toInt(), parts[4].toInt(), faction)
}
