package com.jojo.port

/** Cocos-independent, source-faithful core of ui/MiniMapLayer.js. */
class MiniMapLayer(private val setting: Int, private val callback: () -> Unit = {}) {
    data class Marker(var x: Int = 0, var y: Int = 0, var frame: String? = null)
    var shown = false; private set
    var size: Pair<Int, Int>? = null; private set
    var bgX = 0; private set; var bgY = 0; private set
    var boxX = 0; private set; var boxY = 0; private set
    var weatherFrame: String? = null; private set
    var pool = 0; private set; var baseDestroyed = false; private set
    var sliding = false; private set
    val map = linkedMapOf<Int, Marker>(); val highlights = mutableListOf<List<Int>>()
    private var width = 0; private var height = 0
    private var slideElapsed = 0f
    fun onCreate(weather: Int?, initialPoolNodes: Int) { pool = 0; shown = setting and 16 != 0; if (weather != null) setWeather(weather) }
    fun load(w: Int, h: Int) { size = 2*w to 2*h; width=2*w+4; height=2*h+4; bgX=if(shown) (800-width)/2 else (800+width)/2; bgY=(600-height)/2; callback() }
    fun scroll(x: Int, y: Int) { boxX = -x / 8; boxY = -y / 8 }
    fun setWeather(value: Int) { weatherFrame = "weather$value" }
    fun highlight(x:Int,y:Int,w:Int,h:Int) { highlights += listOf(x/8,y/8,w/8,h/8,17) }
    fun visible(id:Int,camp:String,action:String,status:String,famous:Boolean,x:Int,y:Int) { if(id !in map) { if(pool > 0) pool--; if(pool < 1) pool++; map[id]=Marker() }; ref(id,camp,action,status,famous); move(id,x,y) }
    fun hide(id:Int) { if(map.remove(id) != null) pool++ }
    fun move(id:Int,x:Int,y:Int) { map[id]?.apply { this.x=6*x-(size!!.first shr 2); this.y=(size!!.second shr 2)-6*y } }
    fun ref(id:Int,camp:String,action:String,status:String,famous:Boolean) { val base=when(camp){"mine"->1;"friend"->5;else->if(famous)13 else 9}; val offset=when { action=="action"->1; status=="ms"->3; status=="control"->2; else->0 }; map[id]?.frame="sf${base+offset-1}" }
    // click starts a Cocos move action; the node position is unchanged until that action advances.
    fun touch(event:Int) { if(event==2) { shown=!shown; slideElapsed=0f; sliding=size!=null } }
    /** Advances the authored 0.6-second quartic slide to its stable endpoint. */
    fun advance(seconds: Float) {
        if (!sliding || size == null) return
        slideElapsed += seconds.coerceAtLeast(0f)
        if (slideElapsed >= SLIDE_SECONDS) {
            bgX = if (shown) (800-width)/2 else (800+width)/2
            sliding = false
        }
    }
    fun destroy() { baseDestroyed=true; pool=0 }

    companion object { const val TOUCH_END = 2; const val SLIDE_SECONDS = .6f }
}

/**
 * Source Cocos MiniMapLayer submissions after its real 0.6-second slide has
 * reached an endpoint.  The marker ordering is BattleLayer insertion order,
 * not a coordinate sort, and therefore intentionally remains explicit.
 */
object MiniMapRenderEvents {
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

    fun append(log: RenderEventLog, shown: Boolean) {
        val phase = "battle-mini-map-${if (shown) "shown" else "hidden"}"
        fun draw(path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                 asset: String, opacity: Float = 1f) =
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

    fun jsonl(shown: Boolean): String = RenderEventLog().also { append(it, shown) }.jsonl()
}
