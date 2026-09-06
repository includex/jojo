// Scenario
package com.jojo.game.presentation.scenario.hall
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.scenario.overlay.*

/** HallRoute: 거점 경로이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
data class HallRoute(val layer: String, val payload: String?)


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


    fun canDeliverButton(id: Int) = id in 0..9 && active[id] && listeners[id]


    fun canDeliverCancel() = cancelPriority != null


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


    fun msgBox(result: Int) {
        if (msgBoxPending && result == 0) routes += HallRoute("scene:Login", null)
    }


    fun cancel(event: Int) {
        if (event == 2) attached = false
    }
}


class HallCommandFlow {
    val active = MutableList(6) { false }
    val tags = MutableList(5) { 0 }
    val listeners = MutableList(6) { false }
    val priorities = MutableList<Int?>(6) { null }
    val events = mutableListOf<Int>()
    var callbackCount = 0
    var attached = true


    fun onCreate(flag: Int) {
        active[0] = flag and 1 != 0; repeat(5) { id -> active[id + 1] = flag and 2 != 0; tags[id] = id }; active[5] =
            false; repeat(6) { listeners[it] = true; priorities[it] = 1 }; callbackCount++
    }


    fun canDeliverMenu() = active[0] && listeners[0]


    fun canDeliverButton(id: Int) = id in 0..4 && active[id + 1] && listeners[id + 1]


    fun menu(event: Int) {
        if (event == 2) events += -1
    }


    fun button(id: Int, event: Int) {
        if (event == 2 && id in 0..4) events += tags[id]
    }
}
