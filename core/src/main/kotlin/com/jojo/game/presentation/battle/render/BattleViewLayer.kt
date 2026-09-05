package com.jojo.game.presentation.battle.render

/** State implementation of recovered battle/BattleViewLayer.js. */
class BattleViewLayer {
    /**
     * data class  `Marker`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Marker(val x: Int, val y: Int, val label: String, val red: Boolean?, val opacity: Int?)

    var mapPath = ""
        private set
    private val markers = mutableListOf<Marker>()
    var initialized = false
        private set

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `map` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `pos` (`List<Pair<Int, Int>>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
        markers.indices.forEach { i ->
            markers[i] = markers[i].copy(red = i == index, opacity = if (i == index) 255 else 128)
        }
    }

    /**
     * 공개 메서드 `markers`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Marker>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun markers(): List<Marker> = markers.toList()
}
