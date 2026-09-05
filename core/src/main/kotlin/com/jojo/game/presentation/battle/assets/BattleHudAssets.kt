package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.utils.Disposable

/** Owns static battle HUD, menu, dialogue, status, and selection resources. */
internal class BattleHudAssets : Disposable {
    val dialoguePanelTexture = linearOptional("maps/ui/dialogue-panel.png")
    val fightSpeechLeftTexture = linearOptional("maps/ui/fight-speech-left.png")
    val fightIntroTileTexture = linearOptional("maps/ui/win-condition/bg0.png")
    private val fightBackgroundTextureDelegate = lazy {
        listOf(1, 6).mapNotNull { index ->
            linearOptional("maps/ui/fight-bg-$index.jpg")?.let { index to it }
        }.toMap()
    }
    val fightBackgroundTextures get() = fightBackgroundTextureDelegate.value

    val yingchuan477BodyTexture = linearOptional("maps/ui/yingchuan-477-body.png")
    val yingchuan477SpeakerTexture = linearOptional("maps/ui/yingchuan-477-speaker.png")
    val yingchuan477FaceTexture = linearOptional("maps/ui/yingchuan-477-face.png")
    val yingchuan474FaceTexture = linearOptional("maps/ui/yingchuan-474-face.png")
    val battleMenuTexture = linearOptional("maps/ui/battle-menu.png")
    private val battleButtonBackgroundTexture = linearOptional("maps/ui/battle-button-bg.png")
    val battleButtonBackgroundPatch = battleButtonBackgroundTexture?.let { NinePatch(it, 5, 5, 5, 5) }
    val battleRecordTexture = linearOptional("maps/ui/battle-record.png")
    val battleEndTurnTexture = linearOptional("maps/ui/battle-end-turn.png")
    val naturalMiniMapTexture = linearOptional("maps/ui/battle-smlmap-1.jpg")
    val naturalMiniMapMarkerTextures = mapOf(
        "img5" to linearOptional("maps/ui/battle-smlmap-img5.png"),
        "img9" to linearOptional("maps/ui/battle-smlmap-img9.png"),
    )
    val naturalWeatherTexture = linearOptional("maps/ui/battle-menu/weather_0.png")

    private val menuBackgroundTexture = menuTexture("background")
    private val menuFrameTexture = menuTexture("frame")
    private val menuBoxTexture = menuTexture("box")
    private val menuButtonTexture = menuTexture("button")
    val menuTitleBarTexture = menuTexture("title-bar")
    val menuProgressBarTexture = menuTexture("progress-bar")
    val menuBackgroundPatch = menuBackgroundTexture?.let { NinePatch(it, 5, 5, 5, 5) }
    val menuFramePatch = menuFrameTexture?.let { NinePatch(it, 3, 3, 3, 3) }
    val menuBoxPatch = menuBoxTexture?.let { NinePatch(it, 3, 3, 3, 3) }
    val menuButtonPatch = menuButtonTexture?.let { NinePatch(it, 9, 7, 9, 11) }
    val menuWeatherTextures = (1..5).associateWith { sheet ->
        (0 until 4).map { frame -> menuTexture("weather_${sheet}_$frame") }
    }
    val menuToolTextures = (1..12).map { menuTexture("tool$it") }
    val menuHelpTexture = menuTexture("help")

    val battleStateTextures = (0 until 4).map { index ->
        linearOptional("maps/ui/battle-status/state_$index.png")
    }
    val battleAttributeStatusTextures = listOf("down", "up").map { name ->
        linearOptional("maps/ui/battle-status/attribute_$name.png")
    }
    val enemyHpBarTexture = linearOptional("maps/marks/68.png")
    val famousEnemyHpBarTexture = linearOptional("maps/marks/2.png")
    val friendHpBarTexture = linearOptional("maps/marks/3.png")
    val mineHpBarTexture = linearOptional("maps/marks/5.png")
    val terrainMask19 = optional("maps/marks/19.png")
    val terrainMask21 = linearOptional("maps/marks/21.png")
    val selectAreaTextures = listOf(
        "range-red", "range-green", "range-blue", "range-red-box", "range-green-box",
    ).associateWith { optional("maps/selection/$it.png") }
    val battleCursorTexture = optional("maps/selection/cursor.png")
    val fireTexture = optional("maps/select/20.png")
    val battleSayTexture = optional("maps/ui/battle-say.png")

    private val battleCommandIconDelegate = lazy {
        (1..6).associateWith { index ->
            Texture(Gdx.files.internal("maps/ui/battle-command/command$index.png")).also {
                it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            }
        }
    }
    val battleCommandIcons get() = battleCommandIconDelegate.value
    private val autoBattleToggleDelegate = lazyTexture("maps/ui/title/setting/toggle.png")
    private val autoBattleCheckmarkDelegate = lazyTexture("maps/ui/auto-battle/checkmark.png")
    private val autoBattleBannerDelegate = lazyTexture("maps/ui/auto-battle/img2.png")
    private val autoBattlePlateDelegate = lazyTexture("maps/ui/auto-battle/img3.png")
    val autoBattleToggle get() = autoBattleToggleDelegate.value
    val autoBattleCheckmark get() = autoBattleCheckmarkDelegate.value
    val autoBattleBanner get() = autoBattleBannerDelegate.value
    val autoBattlePlate get() = autoBattlePlateDelegate.value

    private fun menuTexture(name: String) = linearOptional("maps/ui/battle-menu/$name.png")
    private fun optional(path: String): Texture? =
        Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)

    private fun linearOptional(path: String): Texture? = optional(path)?.also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    private fun lazyTexture(path: String) = lazy { Texture(Gdx.files.internal(path)) }

    override fun dispose() {
        listOf(
            dialoguePanelTexture, fightSpeechLeftTexture, fightIntroTileTexture,
            yingchuan477BodyTexture, yingchuan477SpeakerTexture, yingchuan477FaceTexture,
            yingchuan474FaceTexture, battleMenuTexture, battleButtonBackgroundTexture,
            battleRecordTexture, battleEndTurnTexture, naturalMiniMapTexture, naturalWeatherTexture,
            menuBackgroundTexture, menuFrameTexture, menuBoxTexture, menuButtonTexture,
            menuTitleBarTexture, menuProgressBarTexture, menuHelpTexture,
            enemyHpBarTexture, famousEnemyHpBarTexture, friendHpBarTexture, mineHpBarTexture,
            terrainMask19, terrainMask21, battleCursorTexture, fireTexture, battleSayTexture,
        ).filterNotNull().forEach(Texture::dispose)
        naturalMiniMapMarkerTextures.values.filterNotNull().forEach(Texture::dispose)
        menuWeatherTextures.values.flatten().filterNotNull().forEach(Texture::dispose)
        menuToolTextures.filterNotNull().forEach(Texture::dispose)
        battleStateTextures.filterNotNull().forEach(Texture::dispose)
        battleAttributeStatusTextures.filterNotNull().forEach(Texture::dispose)
        selectAreaTextures.values.filterNotNull().forEach(Texture::dispose)
        if (fightBackgroundTextureDelegate.isInitialized()) {
            fightBackgroundTextureDelegate.value.values.forEach(Texture::dispose)
        }
        if (battleCommandIconDelegate.isInitialized()) {
            battleCommandIconDelegate.value.values.forEach(Texture::dispose)
        }
        listOf(
            autoBattleToggleDelegate, autoBattleCheckmarkDelegate,
            autoBattleBannerDelegate, autoBattlePlateDelegate,
        ).filter { it.isInitialized() }.forEach { it.value.dispose() }
    }
}
