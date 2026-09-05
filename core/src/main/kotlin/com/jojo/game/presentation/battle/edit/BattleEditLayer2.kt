package com.jojo.game.presentation.battle.edit

import com.jojo.game.*
import com.jojo.game.presentation.battle.edit.evidence.BattleEditLayer2ChildRenderEvents
import com.jojo.game.presentation.battle.edit.evidence.BattleEditLayer2RegisterRenderEvents
import com.jojo.game.presentation.battle.edit.evidence.BattleEditLayer2ScenePanelRenderEvents
import com.jojo.game.presentation.battle.edit.evidence.BattleEditLayer2WeatherRenderEvents

/** Renderer-independent contract of Battle/scene/EditLayer2 (layer id 23). */
class BattleEditLayer2(
    initialWeather: Int,
    initialRound: Int,
    private val canApplyRound: Boolean,
) {
    sealed interface Effect {
        /**
         * data class  `SetWeather`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class SetWeather(val value: Int) : Effect

        /**
         * data class  `SetRound`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class SetRound(val value: Int) : Effect

        /**
         * data class  `Toast`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Toast(val text: String) : Effect

        /** Instance.LAYER.EditLayer, id 120, prefab Global/scene/EditLayer3. */
        data object OpenGlobalEditor : Effect

        /**
         * data class  `KillAll`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class KillAll(val flag: Int) : Effect
        data object Remove : Effect
    }

    companion object {
        val weatherNames = listOf("맑음", "어두움", "바람", "비", "설")
        const val ROUND_DISABLED_TOAST = "'활성화' 미적용. 난이도가 한 단계 낮아질 때마다 최대 턴 수가 늘어남 기능"
    }

    private val original = mapOf(0 to initialWeather, 1 to initialRound)
    private val pending = linkedMapOf<Int, Int>()
    private var editChanged = false

    var weatherLabel: String = weatherNames[initialWeather]
        private set
    var roundText: String = initialRound.toString()
        private set
    var weatherPanelVisible: Boolean = false
        private set
    var removed: Boolean = false
        private set

    /**
     * 공개 메서드 `openWeatherPanel`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun openWeatherPanel() {
        weatherPanelVisible = true
    }

    /**
     * 공개 메서드 `closeWeatherPanel`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun closeWeatherPanel() {
        weatherPanelVisible = false
    }

    /**
     * 공개 메서드 `selectWeather`
     *
     * ### 파라미터
    - `value` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectWeather(value: Int) {
        require(value in weatherNames.indices)
        // Preserve the recovered source typo: selecting the original value
        // deletes `_data.key`, so an already pending weather value survives.
        if (value != original.getValue(0)) pending[0] = value
        weatherLabel = weatherNames[value]
    }

    /**
     * 공개 메서드 `textChanged`
     *
     * ### 파라미터
    - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun textChanged(value: String) {
        roundText = value
        editChanged = true
    }

    /**
     * 공개 메서드 `editingDidEnd`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun editingDidEnd() {
        if (!editChanged) return
        editChanged = false
        val value = roundText.toDoubleOrNull()?.toInt() ?: 0
        if (value == original.getValue(1)) pending.remove(1) else pending[1] = value
    }

    /**
     * 공개 메서드 `touchButton`
     *
     * ### 파라미터
    - `tag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `phase` (`Int = 2`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Effect>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touchButton(tag: Int, phase: Int = 2): List<Effect> {
        // Cocos removal detaches the node but does not invalidate a retained
        // callback reference; the recovered handler still dispatches if such
        // a callback is delivered after removal.
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
     * 공개 메서드 `pendingValues`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Map<Int, Int>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun pendingValues(): Map<Int, Int> = pending.toMap()
}

/**
 * enum class  `BattleEditLayer2Route`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class BattleEditLayer2Route(val key: String) {
    INITIAL("initial"), WEATHER("weather"), ROUND("round"), APPLY("apply"), CHILD("child"), CHILD_SCENE("child-scene"), REGISTER(
        "register"
    );

    companion object {
        /**
         * 공개 메서드 `parse`
         *
         * ### 파라미터
        - `state` (`String?`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `BattleEditLayer2Route?`
         * - 반환값: 동작 결과의 도메인 값입니다.
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

/** Visible draw submissions of the actual Menu(BJ) -> EditLayer2 route. */
object BattleEditLayer2RenderEvents {
    private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `route` (`BattleEditLayer2Route`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `model` (`BattleEditLayer2`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(route: BattleEditLayer2Route, model: BattleEditLayer2): String {
        val log = RenderEventLog()
        val phase = if (route == BattleEditLayer2Route.REGISTER) "battle-register-open" else "battle-edit2-${route.key}"
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

    private fun appendEdit2(log: RenderEventLog, phase: String, layer: String, model: BattleEditLayer2) {
        /**
         * 공개 메서드 `d`
         *
         * ### 파라미터
        - `path` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `type` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `x` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `y` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `w` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `h` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `asset` (`String?=null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `text` (`String=""`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `blend` (`Any=listOf(770,771`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
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
