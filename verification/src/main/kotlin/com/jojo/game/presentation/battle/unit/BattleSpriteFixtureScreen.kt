package com.jojo.game.presentation.battle.unit

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.jojo.game.presentation.battle.BattleAvatarResolver
import com.jojo.game.GameDataCatalog
import com.jojo.game.JojoGame
import com.jojo.game.domain.battle.Faction
import com.jojo.game.presentation.battle.UnitSpriteSource

/** 전투 스프라이트 리소스를 검증하기 위한 전용 화면이다. */
class BattleSpriteFixtureScreen(
    private val game: JojoGame,
    private val characterId: Int,
    private val action: Int,
    private val direction: Int,
    private val frameTick: Int,
    private val faction: Faction,
) : ScreenAdapter() {
    private val viewport = FitViewport(1280f, 688f)
    private val batch = SpriteBatch()
    private val catalog = GameDataCatalog.load()
    private val profile = requireNotNull(catalog.unitProfile(characterId)) { "Unknown fixture character: $characterId" }
    private val arm = requireNotNull(catalog.armProfile(profile.armId))
    private val avatar = requireNotNull(BattleAvatarResolver.resolve(catalog, characterId, profile.posts, arm.id, faction))
    private val frame = requireNotNull(BattleSpriteTimeline.load().frame(action, direction, frameTick / 24f)) {
        "Missing fixture animation: character=$characterId action=$action direction=$direction"
    }
    private val texture: Texture = run {
        val family = when (frame.source) {
            UnitSpriteSource.ATTACK -> "atk"; UnitSpriteSource.MOVEMENT -> "mov"; UnitSpriteSource.SPECIAL -> "spc"
        }
        val file = Gdx.files.internal("maps/units/${family}2/$avatar.png").takeIf { it.exists() }
            ?: Gdx.files.internal("maps/units/$family/$avatar.png")
        require(file.exists()) { "Missing fixture atlas: $family avatar=$avatar" }
        Texture(file).also { it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest) }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply(); batch.projectionMatrix = viewport.camera.combined
        val size = 384f; val sourceY = if (frame.sourceY + frame.sourceHeight > texture.height) 0 else frame.sourceY
        batch.begin()
        batch.draw(texture, (1280f - size) / 2 + frame.offsetX, (688f - size) / 2 + frame.offsetY, size, size, 0, sourceY, frame.sourceWidth, frame.sourceHeight, frame.flipX, false)
        batch.end(); game.captureFrameIfRequested()
    }

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    override fun dispose() { texture.dispose(); batch.dispose() }
}
