// Test
package com.jojo.game
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.battle.render.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import java.nio.file.Files
import java.nio.file.Path

/** MenuLayerTest: MenuLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class MenuLayerTest {
    @Test fun `onCreate caps round and maps weather sheet and edit visibility`() {
        val view = MenuLayer().onCreate(MenuLayer.CreateData(MenuLayer.Weather.FENG, 21, 20, "영천", editEnabled = true))
        assertEquals(20, view.round); assertEquals(1f, view.progress); assertEquals(listOf(3), view.weatherFrames)
        assertTrue(view.editingButtonVisible); assertTrue(view.buttons.getValue(MenuLayer.Command.BJ))
    }
    @Test fun `flag only enables return and load while weather change disables all`() {
        val menu = MenuLayer()
        val locked = menu.onCreate(MenuLayer.CreateData(MenuLayer.Weather.QING, 1, 20, "x", flag = 1))
        assertTrue(locked.buttons.getValue(MenuLayer.Command.JSYX)); assertTrue(locked.buttons.getValue(MenuLayer.Command.DD)); assertFalse(locked.buttons.getValue(MenuLayer.Command.HELP))
        val changing = menu.onCreate(MenuLayer.CreateData(MenuLayer.Weather.QING, 1, 20, "x", switchWeather = MenuLayer.Weather.XUE))
        assertTrue(changing.buttons.values.none { it })
    }
    @Test fun `command dispatches only end and removes first`() {
        val menu = MenuLayer(); menu.onCreate(MenuLayer.CreateData(MenuLayer.Weather.YIN, 2, 20, "x"))
        assertNull(menu.onCommand(MenuLayer.Command.HELP, 1))
        assertEquals(MenuLayer.Command.HELP, menu.onCommand(MenuLayer.Command.HELP, 2))
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택 (TOUCH_END)을 검증한다.
        assertFalse(menu.view().attached)
    }

    @Test fun `weather is original four frame looping animation at six fps`() {
        assertEquals(1, MenuLayer.weatherSheet(MenuLayer.Weather.QING))
        assertEquals(2, MenuLayer.weatherSheet(MenuLayer.Weather.YIN))
        assertEquals(3, MenuLayer.weatherSheet(MenuLayer.Weather.FENG))
        assertEquals(4, MenuLayer.weatherSheet(MenuLayer.Weather.HAO_YU))
        assertEquals(5, MenuLayer.weatherSheet(MenuLayer.Weather.XUE))
        // 테스트 근거: 연출 프레임과 콜백 처리 순서을 검증한다.
        assertEquals(0, MenuLayer.weatherFrameAt(0f))
        assertEquals(0, MenuLayer.weatherFrameAt(1f / 6f - .0001f))
        assertEquals(1, MenuLayer.weatherFrameAt(1f / 6f))
        assertEquals(3, MenuLayer.weatherFrameAt(.5f))
        assertEquals(0, MenuLayer.weatherFrameAt(2f / 3f))
    }

    @Test fun `weather export retains source atlas and all sheet frames`() {
        val root = Path.of("build/resources/main/maps/ui/battle-menu")
        assertTrue(Files.exists(root.resolve("weather_0.png")))
        assertTrue(Files.exists(root.resolve("weather-frames.json")))
        (1..5).forEach { sheet -> (0 until 4).forEach { frame ->
            assertTrue(Files.exists(root.resolve("weather_${sheet}_${frame}.png")))
        } }
    }

    @Test fun `actual open route inventory includes hidden edit gap and help slot`() {
        val view = MenuLayer().onCreate(MenuLayer.CreateData(MenuLayer.Weather.QING, 1, 20, "영천의 전투"))
        val events = BattleMenuRenderEvents.jsonl(view).lineSequence().filter { it.isNotBlank() }.toList()
        assertEquals(39, events.size)
        assertTrue(events.any { it.contains("\"text\":\"영천의 전투\"") })
        assertTrue(events.any { it.contains("\"w\":15") && it.contains("progressBar/bar") })
        assertFalse(events.any { it.contains("contain/button12/") })
        assertTrue(events.last().contains("contain/button13/Background/edit"))
    }
}
