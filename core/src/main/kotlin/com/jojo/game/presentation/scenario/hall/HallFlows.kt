// Scenario
package com.jojo.game.presentation.scenario.hall
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.scenario.overlay.*

/** HallRoute: 거점 경로이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
data class HallRoute(val layer: String, val payload: String?)


/**
 * `HallMenuFlow`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class HallMenuFlow(private val edit: Boolean) {
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
    /**
     * `zIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var zIndex = 0
    /**
     * `callbackCount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var callbackCount = 0
    /**
     * `cancelPriority` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var cancelPriority: Int? = null
    /**
     * `labels` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val labels = arrayOf("", "")
    /**
     * `active` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val active = MutableList(10) { true }
    /**
     * `tags` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val tags = MutableList(10) { 0 }
    /**
     * `listeners` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val listeners = MutableList(10) { false }
    /**
     * `outerFrame` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var outerFrame: String? = null
    /**
     * `innerFrame` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var innerFrame: String? = null
    /**
     * `startWidth` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var startWidth = 100
    /**
     * `endWidth` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var endWidth = 100
    /**
     * `flagActions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val flagActions = MutableList(2) { 0 }
    /**
     * `routes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val routes = mutableListOf<HallRoute>()
    /**
     * `msgBoxPending` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var msgBoxPending = false


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(eventName: String, stageName: String, ambition: Pair<Int, Int>?) {
        zIndex = 99
        if (ambition != null) {
            val (from, to) = ambition
            if (to < 16) {
                innerFrame = "yellow"; outerFrame = "red"
            } else if (to > 84) {
                innerFrame = "blue"; outerFrame = "yellow"
            }
            startWidth = from; endWidth = to; flagActions[if (to < from) 1 else 0]++; attached =
                false; callbackCount++; return
        }
        labels[0] = eventName; labels[1] = stageName; startWidth = 50; endWidth = 50; cancelPriority = 2
        repeat(10) { tags[it] = it; listeners[it] = true; active[it] = true }; if (!edit) active[8] = false
    }


    /**
     * `canDeliverButton`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun canDeliverButton(id: Int) = id in 0..9 && active[id] && listeners[id]


    /**
     * `canDeliverCancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun canDeliverCancel() = cancelPriority != null


    /**
     * `button`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun button(id: Int, event: Int) {
        if (event != 2 || id !in 0..9) return; attached = false
        val route = listOf(
            "MsgBox",
            "SaveLayer",
            "LoadGameLayer",
            "SettingLayer",
            "ForcesListLayer",
            "PropertyLayer",
            "TerrainLayer",
            "TreasureLayer",
            "EditLayer4",
            "HelperLayer"
        )[id]
        val payload = when (id) {
            0 -> "{\"txt\":\"시작 화면으로 돌아가시겠습니까?\"}"; 4 -> "{\"ms\":[\"unit-a\",\"unit-b\"],\"flag\":0}"; else -> null
        }
        routes += HallRoute(route, payload); if (id == 0) msgBoxPending = true
    }


    /**
     * `msgBox`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun msgBox(result: Int) {
        if (msgBoxPending && result == 0) routes += HallRoute("scene:Login", null)
    }


    /**
     * `cancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun cancel(event: Int) {
        if (event == 2) attached = false
    }
}


/**
 * `HallCommandFlow`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

class HallCommandFlow {
    /**
     * `active` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val active = MutableList(6) { false }
    /**
     * `tags` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val tags = MutableList(5) { 0 }
    /**
     * `listeners` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val listeners = MutableList(6) { false }
    /**
     * `priorities` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val priorities = MutableList<Int?>(6) { null }
    /**
     * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val events = mutableListOf<Int>()
    /**
     * `callbackCount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var callbackCount = 0
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(flag: Int) {
        active[0] = flag and 1 != 0; repeat(5) { id -> active[id + 1] = flag and 2 != 0; tags[id] = id }; active[5] =
            false; repeat(6) { listeners[it] = true; priorities[it] = 1 }; callbackCount++
    }


    /**
     * `canDeliverMenu`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun canDeliverMenu() = active[0] && listeners[0]


    /**
     * `canDeliverButton`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun canDeliverButton(id: Int) = id in 0..4 && active[id + 1] && listeners[id + 1]


    /**
     * `menu`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun menu(event: Int) {
        if (event == 2) events += -1
    }


    /**
     * `button`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun button(id: Int, event: Int) {
        if (event == 2 && id in 0..4) events += tags[id]
    }
}
