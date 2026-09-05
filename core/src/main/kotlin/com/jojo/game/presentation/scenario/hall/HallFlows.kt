package com.jojo.game.presentation.scenario.hall
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.scenario.overlay.*

/** State flows for recovered ui/HallMenuLayer.js and HallCommandLayer.js. */
data class HallRoute(val layer: String, val payload: String?)

/**
 * class  `HallMenuFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class HallMenuFlow(private val edit: Boolean) {
    var attached = true
    var zIndex = 0
    var callbackCount = 0
    var cancelPriority: Int? = null
    val labels = arrayOf("", "")
    val active = MutableList(10) { true }
    val tags = MutableList(10) { 0 }
    val listeners = MutableList(10) { false }
    var outerFrame: String? = null
    var innerFrame: String? = null
    var startWidth = 100
    var endWidth = 100
    val flagActions = MutableList(2) { 0 }
    val routes = mutableListOf<HallRoute>()
    var msgBoxPending = false

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `eventName` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `stageName` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `ambition` (`Pair<Int, Int>?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `canDeliverButton`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canDeliverButton(id: Int) = id in 0..9 && active[id] && listeners[id]

    /**
     * 공개 메서드 `canDeliverCancel`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canDeliverCancel() = cancelPriority != null

    /**
     * 공개 메서드 `button`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `msgBox`
     *
     * ### 파라미터
    - `result` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun msgBox(result: Int) {
        if (msgBoxPending && result == 0) routes += HallRoute("scene:Login", null)
    }

    /**
     * 공개 메서드 `cancel`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun cancel(event: Int) {
        if (event == 2) attached = false
    }
}

/**
 * class  `HallCommandFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class HallCommandFlow {
    val active = MutableList(6) { false }
    val tags = MutableList(5) { 0 }
    val listeners = MutableList(6) { false }
    val priorities = MutableList<Int?>(6) { null }
    val events = mutableListOf<Int>()
    var callbackCount = 0
    var attached = true

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `flag` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCreate(flag: Int) {
        active[0] = flag and 1 != 0; repeat(5) { id -> active[id + 1] = flag and 2 != 0; tags[id] = id }; active[5] =
            false; repeat(6) { listeners[it] = true; priorities[it] = 1 }; callbackCount++
    }

    /**
     * 공개 메서드 `canDeliverMenu`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canDeliverMenu() = active[0] && listeners[0]

    /**
     * 공개 메서드 `canDeliverButton`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canDeliverButton(id: Int) = id in 0..4 && active[id + 1] && listeners[id + 1]

    /**
     * 공개 메서드 `menu`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun menu(event: Int) {
        if (event == 2) events += -1
    }

    /**
     * 공개 메서드 `button`
     *
     * ### 파라미터
    - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun button(id: Int, event: Int) {
        if (event == 2 && id in 0..4) events += tags[id]
    }
}
