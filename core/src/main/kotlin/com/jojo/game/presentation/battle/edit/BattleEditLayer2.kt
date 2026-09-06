// Battle
package com.jojo.game.presentation.battle.edit
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.domain.battle.*

import com.jojo.game.*
import com.jojo.game.presentation.battle.edit.evidence.BattleEditLayer2ChildRenderEvents
import com.jojo.game.presentation.battle.edit.evidence.BattleEditLayer2RegisterRenderEvents
import com.jojo.game.presentation.battle.edit.evidence.BattleEditLayer2ScenePanelRenderEvents
import com.jojo.game.presentation.battle.edit.evidence.BattleEditLayer2WeatherRenderEvents
/**
 * `BattleEditLayer2`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class BattleEditLayer2(
    initialWeather: Int,
    initialRound: Int,
    /** `canApplyRound` (Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val canApplyRound: Boolean,
) {
    /** Effect: 전투 화면의 입력 또는 처리 결과를 전달하는 메시지이다. */
    sealed interface Effect {
        /**
         * `SetWeather`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class SetWeather(val value: Int) : Effect
        /**
         * `SetRound`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class SetRound(val value: Int) : Effect
        /**
         * `Toast`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class Toast(val text: String) : Effect
        /**
         * `OpenGlobalEditor`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object OpenGlobalEditor : Effect
        /**
         * `KillAll`: 관련 상태와 동작을 묶는 class다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data class KillAll(val flag: Int) : Effect
        /**
         * `Remove`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Remove : Effect
    }

    companion object {
        /**
         * `weatherNames` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val weatherNames = listOf("맑음", "어두움", "바람", "비", "설")
        /**
         * `ROUND_DISABLED_TOAST` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ROUND_DISABLED_TOAST = "'활성화' 미적용. 난이도가 한 단계 낮아질 때마다 최대 턴 수가 늘어남 기능"
    }

    /**
     * `original` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val original = mapOf(0 to initialWeather, 1 to initialRound)
    /**
     * `pending` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val pending = linkedMapOf<Int, Int>()
    /**
     * `editChanged` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var editChanged = false

    /**
     * `weatherLabel` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var weatherLabel: String = weatherNames[initialWeather]
        private set
    /**
     * `roundText` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var roundText: String = initialRound.toString()
        private set
    /**
     * `weatherPanelVisible` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var weatherPanelVisible: Boolean = false
        private set
    /**
     * `removed` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var removed: Boolean = false
        private set


    /**
     * `openWeatherPanel`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openWeatherPanel() {
        weatherPanelVisible = true
    }


    /**
     * `closeWeatherPanel`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun closeWeatherPanel() {
        weatherPanelVisible = false
    }


    /**
     * `selectWeather`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun selectWeather(value: Int) {
        require(value in weatherNames.indices)
        if (value != original.getValue(0)) pending[0] = value
        weatherLabel = weatherNames[value]
    }


    /**
     * `textChanged`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun textChanged(value: String) {
        roundText = value
        editChanged = true
    }


    /**
     * `editingDidEnd`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun editingDidEnd() {
        if (!editChanged) return
        editChanged = false
        val value = roundText.toDoubleOrNull()?.toInt() ?: 0
        if (value == original.getValue(1)) pending.remove(1) else pending[1] = value
    }


    /**
     * `touchButton`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touchButton(tag: Int, phase: Int = 2): List<Effect> {
        if (phase != 2) return emptyList()
        return when (tag) {
            0 -> buildList {
                pending.forEach { (key, value) ->
                    when (key) {
                        0 -> add(Effect.SetWeather(value))
                        1 -> if (canApplyRound) add(Effect.SetRound(value)) else add(Effect.Toast(ROUND_DISABLED_TOAST))
                    }
                }
                removed = true
                add(Effect.Remove)
            }

            2 -> listOf(Effect.OpenGlobalEditor)
            3 -> listOf(Effect.KillAll(3))
            4 -> listOf(Effect.KillAll(1))
            5 -> listOf(Effect.KillAll(0))
            else -> emptyList()
        }
    }


    /**
     * `pendingValues`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun pendingValues(): Map<Int, Int> = pending.toMap()
}


/** BattleEditLayer2Route: 전투 화면 흐름에서 현재 처리 종류를 구분한다. */
enum class BattleEditLayer2Route(val key: String) {
    INITIAL("initial"), WEATHER("weather"), ROUND("round"), APPLY("apply"), CHILD("child"), CHILD_SCENE("child-scene"), REGISTER(
        "register"
    );

    companion object {

        /**
         * `parse`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun parse(state: String?): BattleEditLayer2Route? {
            val normalized = state?.removeSuffix("-fixture") ?: return null
            if (normalized == "battle-register-open") return REGISTER
            val key = normalized.removePrefix("battle-edit2-")
            if (!normalized.startsWith("battle-edit2-")) return null
            return entries.firstOrNull { it.key == key }
        }
    }
}
/**
 * `BattleEditLayer2RenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object BattleEditLayer2RenderEvents {
    /**
     * `alphaBlend` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")


    /**
     * `jsonl`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun jsonl(route: BattleEditLayer2Route, model: BattleEditLayer2): String {
        val log = RenderEventLog()
        val phase = if (route == BattleEditLayer2Route.REGISTER) "battle-register-open" else "battle-edit2-${route.key}"
        /**
         * `draw`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun draw(
            layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", opacity: Float = 1f, blend: Any = listOf(770, 771)
        ) =
            log.draw(phase, layer, path, type, x, y, w, h, asset, opacity, blend, true, text)
        draw(
            "HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>"
        )
        if (route == BattleEditLayer2Route.APPLY) return log.jsonl()
        draw(
            "HallLayer",
            "Canvas/Layer/Panel_cancel",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "default_sprite_splash",
            opacity = .314f
        )
        val childRoute =
            route == BattleEditLayer2Route.CHILD || route == BattleEditLayer2Route.CHILD_SCENE || route == BattleEditLayer2Route.REGISTER
        val overlayLayer = when (route) {
            BattleEditLayer2Route.REGISTER -> "RegisterLayer"
            BattleEditLayer2Route.CHILD, BattleEditLayer2Route.CHILD_SCENE -> "EditLayer3"
            else -> "EditLayer2"
        }
        appendEdit2(log, phase, overlayLayer, model)
        if (route == BattleEditLayer2Route.WEATHER) BattleEditLayer2WeatherRenderEvents.append(log, phase)
        if (childRoute) BattleEditLayer2ChildRenderEvents.append(log, phase, overlayLayer)
        if (route == BattleEditLayer2Route.CHILD_SCENE) BattleEditLayer2ScenePanelRenderEvents.append(log, phase)
        if (route == BattleEditLayer2Route.REGISTER) BattleEditLayer2RegisterRenderEvents.append(log, phase)
        return log.jsonl()
    }

    /**
     * `appendEdit2`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun appendEdit2(log: RenderEventLog, phase: String, layer: String, model: BattleEditLayer2) {

        /**
         * `d`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun d(
            path: String,
            type: String,
            x: Float,
            y: Float,
            w: Float,
            h: Float,
            asset: String? = null,
            text: String = "",
            blend: Any = listOf(770, 771)
        ) =
            log.draw(phase, layer, path, type, x, y, w, h, asset, blend = blend, text = text)
        d("Canvas/Layer/bg", "tiled-sprite", 453.686f, 195f, 581f, 410f, "Logo_9-1")
        d("Canvas/Layer/bg/bg1", "sprite", 453.686f, 546.55f, 581f, 58.5f, "bg1")
        d("Canvas/Layer/bg/bg1/label", "label", 669.431f, 550.6f, 149.51f, 50.4f, text = "전장 편집", blend = alphaBlend)
        d("Canvas/Layer/bg/label", "label", 675.735f, 488.8f, 91.43f, 50.4f, text = "날씨: ", blend = alphaBlend)
        d("Canvas/Layer/bg/bg2", "sliced-sprite", 767.301f, 487.229f, 169.8f, 50f, "box1")
        val weatherWidth =
            if (model.weatherLabel.length == 1) 34.6f else if (model.weatherLabel.length == 2) 69.2f else 103.8f
        d(
            "Canvas/Layer/bg/bg2/label",
            "label",
            852.201f - weatherWidth / 2f,
            487.029f,
            weatherWidth,
            50.4f,
            text = model.weatherLabel,
            blend = alphaBlend
        )
        d("Canvas/Layer/bg/label", "label", 618.435f, 432.8f, 126.03f, 50.4f, text = "현재 턴:", blend = alphaBlend)
        d("Canvas/Layer/bg/editbox0/BACKGROUND_SPRITE", "sliced-sprite", 768.224f, 430.411f, 160f, 50f, "box1")
        d(
            "Canvas/Layer/bg/editbox0/TEXT_LABEL",
            "label",
            770.224f,
            430.411f,
            158f,
            50f,
            text = model.roundText,
            blend = alphaBlend
        )
        val buttons = listOf(
            floatArrayOf(495.886f, 207.8f, 580.686f, 210.9f) to "수정",
            floatArrayOf(772.686f, 207.8f, 857.486f, 210.9f) to "취소",
            floatArrayOf(495.886f, 354.9f, 580.686f, 358f) to "전역",
            floatArrayOf(495.886f, 277.1f, 500.371f, 280.2f) to "적군 체력 감소",
            floatArrayOf(772.686f, 354.9f, 817.331f, 358f) to "적군 전멸",
            floatArrayOf(772.686f, 277.1f, 817.331f, 280.2f) to "아군 만피"
        )
        buttons.forEachIndexed { index, (r, text) ->
            d("Canvas/Layer/bg/button$index/Background", "sliced-sprite", r[0], r[1], 238.8f, 56.6f, "box3")
            val w = when (index) {
                3 -> 229.83f; 4, 5 -> 149.51f; else -> 69.2f
            }
            d(
                "Canvas/Layer/bg/button$index/Background/Label",
                "label",
                r[2],
                r[3],
                w,
                50.4f,
                text = text,
                blend = alphaBlend
            )
        }
    }

}
