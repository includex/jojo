// Battle
package com.jojo.game.presentation.battle.render

/** BattleViewLayer: 전투 표시 정보 레이어이며, 화면에 필요한 전투 정보를 만들고 표시한다. */
class BattleViewLayer {
    /**
     * `Marker`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Marker(val x: Int, val y: Int, val label: String, val red: Boolean?, val opacity: Int?)

    /**
     * `mapPath` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var mapPath = ""
        private set
    /**
     * `markers` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val markers = mutableListOf<Marker>()
    /**
     * `initialized` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var initialized = false
        private set


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(map: Int, pos: List<Pair<Int, Int>>) {
        mapPath = "Game/HM/HM_${map + 1}-1"
        markers.clear()
        pos.forEachIndexed { index, (x, y) ->
            markers += Marker(x * 48 - 216, 168 - y * 48, (index + 1).toString(), null, null)
        }
        initialized = true
    }
    /**
     * `battleUnitN`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun battleUnitN(index: Int) {
        markers.indices.forEach { i ->
            markers[i] = markers[i].copy(red = i == index, opacity = if (i == index) 255 else 128)
        }
    }


    /**
     * `markers`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun markers(): List<Marker> = markers.toList()
}
