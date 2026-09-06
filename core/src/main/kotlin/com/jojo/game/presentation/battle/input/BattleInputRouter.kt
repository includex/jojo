// Battle
package com.jojo.game.presentation.battle.input

import com.jojo.game.domain.battle.*
/**
 * `BattleInputSurface`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
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
    /**
     * `pointerCapture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `keyboardCapture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `hitTest`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hitTest(x: Float, y: Float): BattleInputTarget? = hitRegions.asSequence()
        .firstOrNull { it.contains(x, y) }
        ?.target
}
/**
 * `BattleInputCapture`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class BattleInputCapture {
    DIALOGUE, SETTLEMENT_INFO, ROUND, RESULT, MODAL_INFO, LOSE, COMMAND,
    USE_PROPERTY_DETAIL, USE_PROPERTY, MAGIC_INFO, MAGIC_LIST, JIQI, REWARD,
    ITEM_UPGRADE, SCRIPT_WIN_CONDITIONS, UNIT_INFO, FORCES, HELPER, SETTING,
    SAVE, LOAD, TREASURE, PROPERTY, TERRAIN, WIN_CONDITION, AUTO_PROMPT,
    AUTO_TUOGUAN, CHOICE, SCRIPT_PAUSED, BATTLE_MENU, MINI_MAP, MENU_HUD, MAP,
    PLAYER,
}
/**
 * `BattleInputTarget`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class BattleInputTarget { MENU_HUD, MINI_MAP, BATTLE_MENU, MAP }
/**
 * `BattleInputHitRegion`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class BattleInputHitRegion(
    val target: BattleInputTarget,
    val left: Float,
    val bottom: Float,
    val right: Float,
    val top: Float,
) {
    /**
     * `contains`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun contains(x: Float, y: Float): Boolean = x in left..right && y in bottom..top
}

/** BattleInputIntent: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
sealed interface BattleInputIntent {
    /**
     * `KeyDown`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class KeyDown(val keycode: Int, val capture: BattleInputCapture) : BattleInputIntent
    /**
     * `PointerDown`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class PointerDown(
        /**
         * `x` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Float,
        /**
         * `y` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Float,
        /**
         * `capture` (BattleInputCapture,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val capture: BattleInputCapture,
        /**
         * `target` (BattleInputTarget?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val target: BattleInputTarget?,
    ) : BattleInputIntent
    /**
     * `PointerDrag`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class PointerDrag(
        /**
         * `x` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Float,
        /**
         * `y` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Float,
        /**
         * `deltaX` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val deltaX: Float,
        /**
         * `deltaY` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val deltaY: Float,
        /**
         * `moved` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val moved: Boolean,
    ) : BattleInputIntent
    /**
     * `PointerUp`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class PointerUp(
        /**
         * `x` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Float,
        /**
         * `y` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Float,
        /**
         * `pressedCapture` (BattleInputCapture?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val pressedCapture: BattleInputCapture?,
        /**
         * `pressedTarget` (BattleInputTarget?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val pressedTarget: BattleInputTarget?,
        /**
         * `releasedTarget` (BattleInputTarget?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val releasedTarget: BattleInputTarget?,
        /**
         * `moved` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val moved: Boolean,
    ) : BattleInputIntent
}
/**
 * `BattleInputRouter`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class BattleInputRouter {
    /**
     * `pressedCapture` (BattleInputCapture?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var pressedCapture: BattleInputCapture? = null
    /**
     * `pressedTarget` (BattleInputTarget?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var pressedTarget: BattleInputTarget? = null
    /**
     * `lastX` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var lastX: Float? = null
    /**
     * `lastY` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var lastY: Float? = null
    /**
     * `startX` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var startX: Float? = null
    /**
     * `startY` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var startY: Float? = null
    /**
     * `moved` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var moved = false

    /**
     * `keyDown`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun keyDown(keycode: Int, surface: BattleInputSurface): BattleInputIntent.KeyDown =
        BattleInputIntent.KeyDown(keycode, surface.keyboardCapture())

    /**
     * `pointerDown`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `pointerDragged`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `pointerUp`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `cancelPointer`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun cancelPointer() {
        clearGesture()
    }

    /**
     * `clearGesture`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
        /**
         * `DRAG_THRESHOLD` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val DRAG_THRESHOLD = 2f
    }
}
