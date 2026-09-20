// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.evidence.RenderEventLog
import com.jojo.game.presentation.battle.render.BattleGridMiniMapBox

/** 전투 지도의 크기와 날씨를 초기화하고 유닛·기지 표식을 좌표로 갱신한다. */
class MiniMapLayer(private val setting: Int, private val callback: () -> Unit = {}) {
    /**
     * `Marker`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Marker(var x: Int = 0, var y: Int = 0, var frame: String? = null)

    /**
     * `shown` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var shown = false; private set
    /**
     * `size` (Pair<Int, Int>?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var size: Pair<Int, Int>? = null; private set
    /**
     * `bgX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var bgX = 0; private set
    /**
     * `bgY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var bgY = 0; private set
    /**
     * `boxX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var boxX = 0; private set
    /**
     * `boxY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var boxY = 0; private set
    /**
     * `weatherFrame` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var weatherFrame: String? = null; private set
    /**
     * `pool` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var pool = 0; private set
    /**
     * `baseDestroyed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var baseDestroyed = false; private set
    /**
     * `sliding` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var sliding = false; private set
    /**
     * `map` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val map = linkedMapOf<Int, Marker>()
    /**
     * `highlights` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val highlights = mutableListOf<List<Int>>()
    /**
     * `width` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var width = 0
    /**
     * `height` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var height = 0
    /**
     * `slideElapsed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var slideElapsed = 0f


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(weather: Int?, initialPoolNodes: Int) {
        pool = 0; shown = setting and 16 != 0; if (weather != null) setWeather(weather)
    }


    /**
     * `load`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun load(w: Int, h: Int) {
        size = 2 * w to 2 * h; width = 2 * w + 4; height = 2 * h + 4; bgX =
            if (shown) (800 - width) / 2 else (800 + width) / 2; bgY = (600 - height) / 2; callback()
    }


    /**
     * `scroll`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun scroll(x: Int, y: Int) {
        boxX = -x / 8; boxY = -y / 8
    }


    /**
     * `setWeather`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setWeather(value: Int) {
        weatherFrame = "weather$value"
    }


    /**
     * `highlight`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun highlight(x: Int, y: Int, w: Int, h: Int) {
        highlights += listOf(x / 8, y / 8, w / 8, h / 8, 17)
    }


    /**
     * `visible`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun visible(id: Int, camp: String, action: String, status: String, famous: Boolean, x: Int, y: Int) {
        if (id !in map) {
            if (pool > 0) pool--; if (pool < 1) pool++; map[id] = Marker()
        }; ref(id, camp, action, status, famous); move(id, x, y)
    }


    /**
     * `hide`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun hide(id: Int) {
        if (map.remove(id) != null) pool++
    }


    /**
     * `move`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun move(id: Int, x: Int, y: Int) {
        map[id]?.apply { this.x = 6 * x - (size!!.first shr 2); this.y = (size!!.second shr 2) - 6 * y }
    }


    /**
     * `ref`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun ref(id: Int, camp: String, action: String, status: String, famous: Boolean) {
        val base = when (camp) {
            "mine" -> 1; "friend" -> 5; else -> if (famous) 13 else 9
        }
        val offset = when {
            action == "action" -> 1; status == "ms" -> 3; status == "control" -> 2; else -> 0
        }; map[id]?.frame = "sf${base + offset - 1}"
    }

    /**
     * `touch`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun touch(event: Int) {
        if (event == 2) {
            shown = !shown; slideElapsed = 0f; sliding = size != null
        }
    }

    /** advance: 현재 전투 상태를 다음 처리 단계로 진행한다. */
    fun advance(seconds: Float) {
        if (!sliding || size == null) return
        slideElapsed += seconds.coerceAtLeast(0f)
        if (slideElapsed >= SLIDE_SECONDS) {
            bgX = if (shown) (800 - width) / 2 else (800 + width) / 2
            sliding = false
        }
    }


    /**
     * `destroy`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun destroy() {
        baseDestroyed = true; pool = 0
    }

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
        /**
         * `SLIDE_SECONDS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val SLIDE_SECONDS = .6f
    }
}

/**
 * `MiniMapRenderEvents`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object MiniMapRenderEvents {
    /** 원본 MiniMapLayer가 참조하는 전투 캔버스의 논리 크기. */
    const val CANVAS_WIDTH = 1488.372f
    const val CANVAS_HEIGHT = 800f
    /**
     * 미니맵 노드 색: 원본 `MiniMapLayer` 프리팹
     * (`assets/resources/import/1e/1e1e9ef6-9835-40c9-8076-26af80ae38a6.6c573.json`)의
     * `bg`·`bg/map`·`bg/map/tiled`·`bg/weather`·`bg/box`·`bg/btn/Background`·`.../tool11`
     * 어느 노드에도 `_color`가 없어 모두 엔진 기본 흰색이다. `bg/map` `_opacity` 168,
     * `bg/weather` `_opacity` 127만 다르고 이는 `opacity` 항목이 따로 나른다.
     * `bg/btn`의 `cc.Button`은 `_N$normalColor` (214,214,214)를 갖지만 `_N$transition`이 없어
     * (=NONE) 노드 색에 반영되지 않는다. 포트도 `BattleGridMapSurfaceRenderer.drawMiniMap`에서
     * RGB는 흰색으로 두고 알파만 168/255·127/255로 낮춰 그린다.
     */
    private const val NODE_WHITE = "#ffffff"

    /**
     * `MarkerDraw`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class MarkerDraw(val asset: String, val x: Float, val y: Float)

    /**
     * `yingchuanMarkers` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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
     * `append`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun append(log: RenderEventLog, shown: Boolean, box: BattleGridMiniMapBox) {
        val phase = "battle-mini-map-${if (shown) "shown" else "hidden"}"
        /**
         * `draw`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String, opacity: Float = 1f
        ) =
            log.draw(phase, "MiniMapLayer", path, type, x, y, w, h, asset, opacity, color = NODE_WHITE)
        if (shown) {
            draw("Canvas/Layer/bg", "sliced-sprite", 1244.372f, 556f, 244f, 244f, "box5")
            draw("Canvas/Layer/bg/map", "sprite", 1246.372f, 558f, 240f, 240f, "Smlmap_1-1", 168f / 255f)
            yingchuanMarkers.forEach { marker ->
                draw("Canvas/Layer/bg/map/tiled", "sprite", marker.x, marker.y, 16f, 16f, marker.asset)
            }
            draw("Canvas/Layer/bg/weather", "sprite", 1248.372f, 560f, 57.6f, 57.6f, "weather_0", 127f / 255f)
            draw("Canvas/Layer/bg/box", "sliced-sprite", box.x, box.y, box.width, box.height, "box6")
        }
        val buttonX = if (shown) 1174.372f else 1418.372f
        draw("Canvas/Layer/bg/btn/Background", "sliced-sprite", buttonX, 730f, 70f, 70f, "bg1")
        draw("Canvas/Layer/bg/btn/Background/tool11", "sprite", buttonX + .2f, 730.2f, 69.6f, 69.6f, "tool11")
    }


    /**
     * `jsonl`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun jsonl(shown: Boolean, box: BattleGridMiniMapBox): String =
        RenderEventLog().also { append(it, shown, box) }.jsonl()
}
