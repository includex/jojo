// Battle
package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.utils.Disposable
/**
 * `BattleHudAssets`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class BattleHudAssets : Disposable {
    /**
     * `dialoguePanelTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val dialoguePanelTexture = linearOptional("maps/ui/dialogue-panel.png")
    /**
     * `fightSpeechLeftTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val fightSpeechLeftTexture = linearOptional("maps/ui/fight-speech-left.png")
    /**
     * `fightIntroTileTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val fightIntroTileTexture = linearOptional("maps/ui/win-condition/bg0.png")
    /**
     * `fightBackgroundTextureDelegate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val fightBackgroundTextureDelegate = lazy {
        listOf(1, 6).mapNotNull { index ->
            linearOptional("maps/ui/fight-bg-$index.jpg")?.let { index to it }
        }.toMap()
    }
    /**
     * `fightBackgroundTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val fightBackgroundTextures get() = fightBackgroundTextureDelegate.value

    /**
     * `yingchuan477BodyTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val yingchuan477BodyTexture = linearOptional("maps/ui/yingchuan-477-body.png")
    /**
     * `yingchuan477SpeakerTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val yingchuan477SpeakerTexture = linearOptional("maps/ui/yingchuan-477-speaker.png")
    /**
     * `yingchuan477FaceTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val yingchuan477FaceTexture = linearOptional("maps/ui/yingchuan-477-face.png")
    /**
     * `yingchuan474FaceTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val yingchuan474FaceTexture = linearOptional("maps/ui/yingchuan-474-face.png")
    /**
     * `battleMenuTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleMenuTexture = linearOptional("maps/ui/battle-menu.png")
    /**
     * `battleButtonBackgroundTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val battleButtonBackgroundTexture = linearOptional("maps/ui/battle-button-bg.png")
    /**
     * `battleButtonBackgroundPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleButtonBackgroundPatch = battleButtonBackgroundTexture?.let { NinePatch(it, 5, 5, 5, 5) }
    /**
     * `battleRecordTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleRecordTexture = linearOptional("maps/ui/battle-record.png")
    /**
     * `battleEndTurnTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleEndTurnTexture = linearOptional("maps/ui/battle-end-turn.png")
    /**
     * `naturalMiniMapTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val naturalMiniMapTexture = linearOptional("maps/ui/battle-smlmap-1.jpg")
    /**
     * `naturalMiniMapMarkerTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val naturalMiniMapMarkerTextures = mapOf(
        "img5" to linearOptional("maps/ui/battle-smlmap-img5.png"),
        "img9" to linearOptional("maps/ui/battle-smlmap-img9.png"),
    )
    /**
     * `naturalWeatherTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val naturalWeatherTexture = linearOptional("maps/ui/battle-menu/weather_0.png")

    /**
     * `menuBackgroundTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val menuBackgroundTexture = menuTexture("background")
    /**
     * `menuFrameTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val menuFrameTexture = menuTexture("frame")
    /**
     * `menuBoxTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val menuBoxTexture = menuTexture("box")
    /**
     * `menuButtonTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val menuButtonTexture = menuTexture("button")
    /**
     * `menuTitleBarTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuTitleBarTexture = menuTexture("title-bar")
    /**
     * `menuProgressBarTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuProgressBarTexture = menuTexture("progress-bar")
    /**
     * `menuBackgroundPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuBackgroundPatch = menuBackgroundTexture?.let { NinePatch(it, 5, 5, 5, 5) }
    /**
     * `menuFramePatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuFramePatch = menuFrameTexture?.let { NinePatch(it, 3, 3, 3, 3) }
    /**
     * `menuBoxPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuBoxPatch = menuBoxTexture?.let { NinePatch(it, 3, 3, 3, 3) }
    /**
     * `menuButtonPatch` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuButtonPatch = menuButtonTexture?.let { NinePatch(it, 9, 7, 9, 11) }
    /**
     * `menuWeatherTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuWeatherTextures = (1..5).associateWith { sheet ->
        (0 until 4).map { frame -> menuTexture("weather_${sheet}_$frame") }
    }
    /**
     * `menuToolTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuToolTextures = (1..12).map { menuTexture("tool$it") }
    /**
     * `menuHelpTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val menuHelpTexture = menuTexture("help")

    /**
     * `battleStateTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleStateTextures = (0 until 4).map { index ->
        linearOptional("maps/ui/battle-status/state_$index.png")
    }
    /**
     * `battleAttributeStatusTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleAttributeStatusTextures = listOf("down", "up").map { name ->
        linearOptional("maps/ui/battle-status/attribute_$name.png")
    }
    /**
     * `enemyHpBarTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val enemyHpBarTexture = linearOptional("maps/marks/68.png")
    /**
     * `famousEnemyHpBarTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val famousEnemyHpBarTexture = linearOptional("maps/marks/2.png")
    /**
     * `friendHpBarTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val friendHpBarTexture = linearOptional("maps/marks/3.png")
    /**
     * `mineHpBarTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val mineHpBarTexture = linearOptional("maps/marks/5.png")
    /**
     * `terrainMask19` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val terrainMask19 = optional("maps/marks/19.png")
    /**
     * `terrainMask21` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val terrainMask21 = linearOptional("maps/marks/21.png")
    /**
     * `selectAreaTextures` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val selectAreaTextures = listOf(
        "range-red", "range-green", "range-blue", "range-red-box", "range-green-box",
    ).associateWith { optional("maps/selection/$it.png") }
    /**
     * `battleCursorTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleCursorTexture = optional("maps/selection/cursor.png")
    /**
     * `fireTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val fireTexture = optional("maps/select/20.png")
    /**
     * `battleSayTexture` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleSayTexture = optional("maps/ui/battle-say.png")

    /**
     * `battleCommandIconDelegate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val battleCommandIconDelegate = lazy {
        (1..6).associateWith { index ->
            Texture(Gdx.files.internal("maps/ui/battle-command/command$index.png")).also {
                it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            }
        }
    }
    /**
     * `battleCommandIcons` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleCommandIcons get() = battleCommandIconDelegate.value
    /**
     * `autoBattleToggleDelegate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val autoBattleToggleDelegate = lazyTexture("maps/ui/title/setting/toggle.png")
    /**
     * `autoBattleCheckmarkDelegate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val autoBattleCheckmarkDelegate = lazyTexture("maps/ui/auto-battle/checkmark.png")
    /**
     * `autoBattleBannerDelegate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val autoBattleBannerDelegate = lazyTexture("maps/ui/auto-battle/img2.png")
    /**
     * `autoBattlePlateDelegate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val autoBattlePlateDelegate = lazyTexture("maps/ui/auto-battle/img3.png")
    /**
     * `autoBattleToggle` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val autoBattleToggle get() = autoBattleToggleDelegate.value
    /**
     * `autoBattleCheckmark` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val autoBattleCheckmark get() = autoBattleCheckmarkDelegate.value
    /**
     * `autoBattleBanner` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val autoBattleBanner get() = autoBattleBannerDelegate.value
    /**
     * `autoBattlePlate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val autoBattlePlate get() = autoBattlePlateDelegate.value

    /**
     * `menuTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun menuTexture(name: String) = linearOptional("maps/ui/battle-menu/$name.png")
    /**
     * `optional`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun optional(path: String): Texture? =
        Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)

    /**
     * `linearOptional`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun linearOptional(path: String): Texture? = optional(path)?.also {
        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    /**
     * `lazyTexture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun lazyTexture(path: String) = lazy { Texture(Gdx.files.internal(path)) }

    /**
     * `dispose`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
