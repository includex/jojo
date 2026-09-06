// Verification
package com.jojo.game.presentation.battle.unit

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.jojo.game.domain.battle.BattleAvatarResolver
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.JojoGame
import com.jojo.game.domain.battle.Faction
import com.jojo.game.presentation.battle.unit.UnitSpriteSource

/** BattleSpriteFixtureScreen: 전투 스프라이트 리소스를 검증하기 위한 전용 화면이다. */
class BattleSpriteFixtureScreen(
    /** game: 검증 흐름에서 사용하는 값을 담는다. */
    private val game: JojoGame,
    /** characterId: character id 값을 보관해 검증 흐름에서 사용한다. */
    private val characterId: Int,
    /** action: 검증 입력 또는 동작 정보를 담는다. */
    private val action: Int,
    /** direction: 검증 흐름에서 사용하는 값을 담는다. */
    private val direction: Int,
    /** frameTick: 검증 실행의 시간 또는 프레임 값을 담는다. */
    private val frameTick: Int,
    /** faction: 검증 입력 또는 동작 정보를 담는다. */
    private val faction: Faction,
) : ScreenAdapter() {
    /** viewport: 검증 화면의 좌표계와 카메라 상태를 담는다. */
    private val viewport = FitViewport(1280f, 688f)
    /** batch: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val batch = SpriteBatch()
    /** catalog: 검증 흐름에서 사용하는 값을 담는다. */
    private val catalog = GameDataCatalog.load()
    /** profile: 검증 결과를 저장할 경로를 담는다. */
    private val profile = requireNotNull(catalog.unitProfile(characterId)) { "Unknown fixture character: $characterId" }
    /** arm: 검증 흐름에서 사용하는 값을 담는다. */
    private val arm = requireNotNull(catalog.armProfile(profile.armId))
    /** avatar: 검증 흐름에서 사용하는 값을 담는다. */
    private val avatar = requireNotNull(BattleAvatarResolver.resolve(catalog, characterId, profile.posts, arm.id, faction))
    /** frame: 검증 실행의 시간 또는 프레임 값을 담는다. */
    private val frame = requireNotNull(BattleSpriteTimeline.load().frame(action, direction, frameTick / 24f)) {
        "Missing fixture animation: character=$characterId action=$action direction=$direction"
    }
    /** texture: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val texture: Texture = run {
        /**
         * `family` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val family = when (frame.source) {
            UnitSpriteSource.ATTACK -> "atk"; UnitSpriteSource.MOVEMENT -> "mov"; UnitSpriteSource.SPECIAL -> "spc"
        }
        /**
         * `file` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val file = Gdx.files.internal("maps/units/${family}2/$avatar.png").takeIf { it.exists() }
            ?: Gdx.files.internal("maps/units/$family/$avatar.png")
        require(file.exists()) { "Missing fixture atlas: $family avatar=$avatar" }
        Texture(file).also { it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest) }
    }

    /** render: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply(); batch.projectionMatrix = viewport.camera.combined
        val size = 384f; val sourceY = if (frame.sourceY + frame.sourceHeight > texture.height) 0 else frame.sourceY
        batch.begin()
        batch.draw(texture, (1280f - size) / 2 + frame.offsetX, (688f - size) / 2 + frame.offsetY, size, size, 0, sourceY, frame.sourceWidth, frame.sourceHeight, frame.flipX, false)
        batch.end(); game.captureFrameIfRequested()
    }

    /** resize: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() { texture.dispose(); batch.dispose() }
}
