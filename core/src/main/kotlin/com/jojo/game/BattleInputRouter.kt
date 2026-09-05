package com.jojo.game

/**
 * Value-only description of the input surfaces currently mounted by BattleScreen.
 *
 * The router deliberately knows nothing about Battle, LibGDX, assets, or a
 * scenario.  BattleScreen projects its mutable presentation state into this
 * description and consumes the intents returned by [BattleInputRouter].
 */
data class BattleInputSurface(
    val dialogue: Boolean = false,
    val settlementInfo: Boolean = false,
    val roundLayer: Boolean = false,
    val resultPrompt: Boolean = false,
    val modalInfo: Boolean = false,
    val loseScene: Boolean = false,
    val command: Boolean = false,
    val usePropertyDetail: Boolean = false,
    val useProperty: Boolean = false,
    val magicInfo: Boolean = false,
    val magicList: Boolean = false,
    val jiqi: Boolean = false,
    val reward: Boolean = false,
    val itemUpgrade: Boolean = false,
    val scriptWinConditions: Boolean = false,
    val unitInfo: Boolean = false,
    val forces: Boolean = false,
    val helper: Boolean = false,
    val setting: Boolean = false,
    val save: Boolean = false,
    val load: Boolean = false,
    val treasure: Boolean = false,
    val property: Boolean = false,
    val terrain: Boolean = false,
    val winCondition: Boolean = false,
    val autoPrompt: Boolean = false,
    val autoTuoGuan: Boolean = false,
    val choice: Boolean = false,
    val battleMenu: Boolean = false,
    val miniMap: Boolean = false,
    val menuHud: Boolean = false,
    val interactiveRoute: BattleInteractiveInput.Route = BattleInteractiveInput.Route.PLAYER_INPUT,
    val hitRegions: List<BattleInputHitRegion> = emptyList(),
) {
    /** Precedence is the same top-to-bottom ownership order as BattleScreen. */
    fun pointerCapture(x: Float? = null, y: Float? = null): BattleInputCapture = when {
        dialogue -> BattleInputCapture.DIALOGUE
        settlementInfo -> BattleInputCapture.SETTLEMENT_INFO
        roundLayer -> BattleInputCapture.ROUND
        resultPrompt -> BattleInputCapture.RESULT
        modalInfo -> BattleInputCapture.MODAL_INFO
        loseScene -> BattleInputCapture.LOSE
        command -> BattleInputCapture.COMMAND
        usePropertyDetail -> BattleInputCapture.USE_PROPERTY_DETAIL
        useProperty -> BattleInputCapture.USE_PROPERTY
        magicInfo -> BattleInputCapture.MAGIC_INFO
        magicList -> BattleInputCapture.MAGIC_LIST
        jiqi -> BattleInputCapture.JIQI
        reward -> BattleInputCapture.REWARD
        itemUpgrade -> BattleInputCapture.ITEM_UPGRADE
        scriptWinConditions -> BattleInputCapture.SCRIPT_WIN_CONDITIONS
        unitInfo -> BattleInputCapture.UNIT_INFO
        forces -> BattleInputCapture.FORCES
        helper -> BattleInputCapture.HELPER
        setting -> BattleInputCapture.SETTING
        save -> BattleInputCapture.SAVE
        load -> BattleInputCapture.LOAD
        treasure -> BattleInputCapture.TREASURE
        property -> BattleInputCapture.PROPERTY
        terrain -> BattleInputCapture.TERRAIN
        winCondition -> BattleInputCapture.WIN_CONDITION
        autoPrompt -> BattleInputCapture.AUTO_PROMPT
        autoTuoGuan -> BattleInputCapture.AUTO_TUOGUAN
        choice -> BattleInputCapture.CHOICE
        interactiveRoute != BattleInteractiveInput.Route.PLAYER_INPUT -> BattleInputCapture.SCRIPT_PAUSED
        battleMenu -> BattleInputCapture.BATTLE_MENU
        miniMap && x != null && y != null && hitTest(x, y) == BattleInputTarget.MINI_MAP -> BattleInputCapture.MINI_MAP
        menuHud && x != null && y != null && hitTest(x, y) == BattleInputTarget.MENU_HUD -> BattleInputCapture.MENU_HUD
        else -> BattleInputCapture.MAP
    }

    fun keyboardCapture(): BattleInputCapture = when {
        dialogue -> BattleInputCapture.DIALOGUE
        settlementInfo -> BattleInputCapture.SETTLEMENT_INFO
        roundLayer -> BattleInputCapture.ROUND
        loseScene -> BattleInputCapture.LOSE
        resultPrompt -> BattleInputCapture.RESULT
        helper -> BattleInputCapture.HELPER
        setting -> BattleInputCapture.SETTING
        save -> BattleInputCapture.SAVE
        forces -> BattleInputCapture.FORCES
        unitInfo -> BattleInputCapture.UNIT_INFO
        load -> BattleInputCapture.LOAD
        reward -> BattleInputCapture.REWARD
        itemUpgrade -> BattleInputCapture.ITEM_UPGRADE
        treasure -> BattleInputCapture.TREASURE
        property -> BattleInputCapture.PROPERTY
        terrain -> BattleInputCapture.TERRAIN
        choice -> BattleInputCapture.CHOICE
        interactiveRoute != BattleInteractiveInput.Route.PLAYER_INPUT -> BattleInputCapture.SCRIPT_PAUSED
        else -> BattleInputCapture.PLAYER
    }

    fun hitTest(x: Float, y: Float): BattleInputTarget? = hitRegions.asSequence()
        .firstOrNull { it.contains(x, y) }
        ?.target
}

enum class BattleInputCapture {
    DIALOGUE, SETTLEMENT_INFO, ROUND, RESULT, MODAL_INFO, LOSE, COMMAND,
    USE_PROPERTY_DETAIL, USE_PROPERTY, MAGIC_INFO, MAGIC_LIST, JIQI, REWARD,
    ITEM_UPGRADE, SCRIPT_WIN_CONDITIONS, UNIT_INFO, FORCES, HELPER, SETTING,
    SAVE, LOAD, TREASURE, PROPERTY, TERRAIN, WIN_CONDITION, AUTO_PROMPT,
    AUTO_TUOGUAN, CHOICE, SCRIPT_PAUSED, BATTLE_MENU, MINI_MAP, MENU_HUD, MAP,
    PLAYER,
}

enum class BattleInputTarget { MENU_HUD, MINI_MAP, BATTLE_MENU, MAP }

data class BattleInputHitRegion(
    val target: BattleInputTarget,
    val left: Float,
    val bottom: Float,
    val right: Float,
    val top: Float,
) {
    fun contains(x: Float, y: Float): Boolean = x in left..right && y in bottom..top
}

sealed interface BattleInputIntent {
    data class KeyDown(val keycode: Int, val capture: BattleInputCapture) : BattleInputIntent
    data class PointerDown(
        val x: Float,
        val y: Float,
        val capture: BattleInputCapture,
        val target: BattleInputTarget?,
    ) : BattleInputIntent

    data class PointerDrag(
        val x: Float,
        val y: Float,
        val deltaX: Float,
        val deltaY: Float,
        val moved: Boolean,
    ) : BattleInputIntent

    data class PointerUp(
        val x: Float,
        val y: Float,
        val pressedCapture: BattleInputCapture?,
        val pressedTarget: BattleInputTarget?,
        val releasedTarget: BattleInputTarget?,
        val moved: Boolean,
    ) : BattleInputIntent
}

/** Stateful only with respect to one pointer gesture; all game state stays in the caller. */
class BattleInputRouter {
    private var pressedCapture: BattleInputCapture? = null
    private var pressedTarget: BattleInputTarget? = null
    private var lastX: Float? = null
    private var lastY: Float? = null
    private var startX: Float? = null
    private var startY: Float? = null
    private var moved = false

    fun keyDown(keycode: Int, surface: BattleInputSurface): BattleInputIntent.KeyDown =
        BattleInputIntent.KeyDown(keycode, surface.keyboardCapture())

    fun pointerDown(x: Float, y: Float, surface: BattleInputSurface): BattleInputIntent.PointerDown {
        val capture = surface.pointerCapture(x, y)
        pressedCapture = capture
        pressedTarget = surface.hitTest(x, y)
        lastX = x
        lastY = y
        startX = x
        startY = y
        moved = false
        return BattleInputIntent.PointerDown(x, y, capture, pressedTarget)
    }

    fun pointerDragged(x: Float, y: Float, surface: BattleInputSurface): BattleInputIntent.PointerDrag {
        val previousX = lastX ?: x
        val previousY = lastY ?: y
        val deltaX = x - previousX
        val deltaY = y - previousY
        if (startX != null && startY != null &&
            kotlin.math.hypot(x - startX!!, y - startY!!) > DRAG_THRESHOLD
        ) moved = true
        lastX = x
        lastY = y
        return BattleInputIntent.PointerDrag(
            x, y, deltaX, deltaY, moved &&
                    pressedCapture == BattleInputCapture.MAP &&
                    surface.pointerCapture() == BattleInputCapture.MAP
        )
    }

    fun pointerUp(x: Float, y: Float, surface: BattleInputSurface): BattleInputIntent.PointerUp {
        val intent = BattleInputIntent.PointerUp(
            x = x,
            y = y,
            pressedCapture = pressedCapture,
            pressedTarget = pressedTarget,
            releasedTarget = surface.hitTest(x, y),
            moved = moved,
        )
        clearGesture()
        return intent
    }

    fun cancelPointer() {
        clearGesture()
    }

    private fun clearGesture() {
        pressedCapture = null
        pressedTarget = null
        lastX = null
        lastY = null
        startX = null
        startY = null
        moved = false
    }

    private companion object {
        const val DRAG_THRESHOLD = 2f
    }
}
