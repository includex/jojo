// Battle
package com.jojo.game.presentation.battle.render

/** BattleViewLayer: 전투 표시 정보 레이어이며, 화면에 필요한 전투 정보를 만들고 표시한다. */
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
            markers += Marker(x * 48 - 216, 168 - y * 48, (index + 1).toString(), null, null)
        }
        initialized = true
    }
    fun battleUnitN(index: Int) {
        markers.indices.forEach { i ->
            markers[i] = markers[i].copy(red = i == index, opacity = if (i == index) 255 else 128)
        }
    }


    fun markers(): List<Marker> = markers.toList()
}
