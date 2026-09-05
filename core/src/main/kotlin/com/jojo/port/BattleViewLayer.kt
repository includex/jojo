package com.jojo.port

/** State port of recovered battle/BattleViewLayer.js. */
class BattleViewLayer {
    data class Marker(val x: Int, val y: Int, val label: String, val red: Boolean?, val opacity: Int?)
    var mapPath = ""
        private set
    private val markers = mutableListOf<Marker>()
    var initialized = false
        private set

    fun onCreate(map: Int, pos: List<Pair<Int, Int>>) {
        mapPath = "Game/HM/HM_${map + 1}-1"
        markers.clear()
        pos.forEachIndexed { index, (x, y) ->
            // `x * TITLE_SIZE - mapWidth / 2 + TITLE_SIZE / 2`, and its
            // inverted source-y counterpart; the recovered texture is 480x384.
            markers += Marker(x * 48 - 216, 168 - y * 48, (index + 1).toString(), null, null)
        }
        initialized = true
    }

    /** Source listener runs for every value, including no selection and out-of-range selection. */
    fun battleUnitN(index: Int) {
        markers.indices.forEach { i -> markers[i] = markers[i].copy(red = i == index, opacity = if (i == index) 255 else 128) }
    }
    fun markers(): List<Marker> = markers.toList()
}
