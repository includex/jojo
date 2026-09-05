package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.evidence.RenderEventLog

/** Cocos-independent, source-faithful core of ui/MiniMapLayer.js. */
class MiniMapLayer(private val setting: Int, private val callback: () -> Unit = {}) {
    /**
     * data class  `Marker`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Marker(var x: Int = 0, var y: Int = 0, var frame: String? = null)

    var shown = false; private set
    var size: Pair<Int, Int>? = null; private set
    var bgX = 0; private set
    var bgY = 0; private set
    var boxX = 0; private set
    var boxY = 0; private set
    var weatherFrame: String? = null; private set
    var pool = 0; private set
    var baseDestroyed = false; private set
    var sliding = false; private set
    val map = linkedMapOf<Int, Marker>()
    val highlights = mutableListOf<List<Int>>()
    private var width = 0
    private var height = 0
    private var slideElapsed = 0f

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `weather` (`Int?`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `initialPoolNodes` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(weather: Int?, initialPoolNodes: Int) {
        pool = 0; shown = setting and 16 != 0; if (weather != null) setWeather(weather)
    }

    /**
     * 공개 메서드 `load`
     *
     * ### 파라미터
    - `w` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `h` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun load(w: Int, h: Int) {
        size = 2 * w to 2 * h; width = 2 * w + 4; height = 2 * h + 4; bgX =
            if (shown) (800 - width) / 2 else (800 + width) / 2; bgY = (600 - height) / 2; callback()
    }

    /**
     * 공개 메서드 `scroll`
     *
     * ### 파라미터
    - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun scroll(x: Int, y: Int) {
        boxX = -x / 8; boxY = -y / 8
    }

    /**
     * 공개 메서드 `setWeather`
     *
     * ### 파라미터
    - `value` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setWeather(value: Int) {
        weatherFrame = "weather$value"
    }

    /**
     * 공개 메서드 `highlight`
     *
     * ### 파라미터
    - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `w` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `h` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun highlight(x: Int, y: Int, w: Int, h: Int) {
        highlights += listOf(x / 8, y / 8, w / 8, h / 8, 17)
    }

    /**
     * 공개 메서드 `visible`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `camp` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `action` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `status` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `famous` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun visible(id: Int, camp: String, action: String, status: String, famous: Boolean, x: Int, y: Int) {
        if (id !in map) {
            if (pool > 0) pool--; if (pool < 1) pool++; map[id] = Marker()
        }; ref(id, camp, action, status, famous); move(id, x, y)
    }

    /**
     * 공개 메서드 `hide`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun hide(id: Int) {
        if (map.remove(id) != null) pool++
    }

    /**
     * 공개 메서드 `move`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `x` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `y` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun move(id: Int, x: Int, y: Int) {
        map[id]?.apply { this.x = 6 * x - (size!!.first shr 2); this.y = (size!!.second shr 2) - 6 * y }
    }

    /**
     * 공개 메서드 `ref`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `camp` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `action` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `status` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `famous` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun ref(id: Int, camp: String, action: String, status: String, famous: Boolean) {
        val base = when (camp) {
            "mine" -> 1; "friend" -> 5; else -> if (famous) 13 else 9
        }
        val offset = when {
            action == "action" -> 1; status == "ms" -> 3; status == "control" -> 2; else -> 0
        }; map[id]?.frame = "sf${base + offset - 1}"
    }
    // click starts a Cocos move action; the node position is unchanged until that action advances.
    /**
     * 공개 메서드 `touch`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touch(event: Int) {
        if (event == 2) {
            shown = !shown; slideElapsed = 0f; sliding = size != null
        }
    }

    /** Advances the authored 0.6-second quartic slide to its stable endpoint. */
    fun advance(seconds: Float) {
        if (!sliding || size == null) return
        slideElapsed += seconds.coerceAtLeast(0f)
        if (slideElapsed >= SLIDE_SECONDS) {
            bgX = if (shown) (800 - width) / 2 else (800 + width) / 2
            sliding = false
        }
    }

    /**
     * 공개 메서드 `destroy`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun destroy() {
        baseDestroyed = true; pool = 0
    }

    companion object {
        const val TOUCH_END = 2
        const val SLIDE_SECONDS = .6f
    }
}

/**
 * Source Cocos MiniMapLayer submissions after its real 0.6-second slide has
 * reached an endpoint.  The marker ordering is BattleScreen insertion order,
 * not a coordinate sort, and therefore intentionally remains explicit.
 */
/**
 * object  `MiniMapRenderEvents`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object MiniMapRenderEvents {
    /**
     * data class  `MarkerDraw`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class MarkerDraw(val asset: String, val x: Float, val y: Float)

    val yingchuanMarkers = listOf(
        MarkerDraw("img5", 1366.372f, 578f), MarkerDraw("img5", 1354.372f, 566f),
        MarkerDraw("img5", 1390.372f, 578f), MarkerDraw("img5", 1330.372f, 590f),
        MarkerDraw("img5", 1390.372f, 590f), MarkerDraw("img9", 1354.372f, 650f),
        MarkerDraw("img9", 1366.372f, 650f), MarkerDraw("img9", 1318.372f, 590f),
        MarkerDraw("img9", 1354.372f, 602f), MarkerDraw("img9", 1366.372f, 602f),
        MarkerDraw("img9", 1390.372f, 602f), MarkerDraw("img9", 1354.372f, 614f),
        MarkerDraw("img9", 1366.372f, 614f), MarkerDraw("img9", 1306.372f, 662f),
        MarkerDraw("img9", 1306.372f, 650f), MarkerDraw("img9", 1330.372f, 650f),
        MarkerDraw("img9", 1342.372f, 662f), MarkerDraw("img9", 1390.372f, 662f),
        MarkerDraw("img9", 1390.372f, 638f),
    )

    /**
     * 공개 메서드 `append`
     *
     * ### 파라미터
    - `log` (`RenderEventLog`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `shown` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun append(log: RenderEventLog, shown: Boolean) {
        val phase = "battle-mini-map-${if (shown) "shown" else "hidden"}"
        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String, opacity: Float = 1f
        ) =
            log.draw(phase, "MiniMapLayer", path, type, x, y, w, h, asset, opacity)
        if (shown) {
            draw("Canvas/Layer/bg", "sliced-sprite", 1244.372f, 556f, 244f, 244f, "box5")
            draw("Canvas/Layer/bg/map", "sprite", 1246.372f, 558f, 240f, 240f, "Smlmap_1-1", 168f / 255f)
            yingchuanMarkers.forEach { marker ->
                draw("Canvas/Layer/bg/map/tiled", "sprite", marker.x, marker.y, 16f, 16f, marker.asset)
            }
            draw("Canvas/Layer/bg/weather", "sprite", 1248.372f, 560f, 57.6f, 57.6f, "weather_0", 127f / 255f)
            draw("Canvas/Layer/bg/box", "sliced-sprite", 1286.372f, 570f, 186.047f, 100f, "box6")
        }
        // At the hidden endpoint only the button remains inside the widened
        // viewport; the bg quad starts exactly at its right edge.
        val buttonX = if (shown) 1174.372f else 1418.372f
        draw("Canvas/Layer/bg/btn/Background", "sliced-sprite", buttonX, 730f, 70f, 70f, "bg1")
        draw("Canvas/Layer/bg/btn/Background/tool11", "sprite", buttonX + .2f, 730.2f, 69.6f, 69.6f, "tool11")
    }

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `shown` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(shown: Boolean): String = RenderEventLog().also { append(it, shown) }.jsonl()
}
