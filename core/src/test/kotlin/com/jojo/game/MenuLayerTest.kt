package com.jojo.game
import com.jojo.game.presentation.battle.render.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import java.nio.file.Files
import java.nio.file.Path

/**
 * class  `MenuLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
        // MenuLayer.js TOUCH_END executes removeFromParent before its switch.
        assertFalse(menu.view().attached)
    }

    @Test fun `weather is original four frame looping animation at six fps`() {
        assertEquals(1, MenuLayer.weatherSheet(MenuLayer.Weather.QING))
        assertEquals(2, MenuLayer.weatherSheet(MenuLayer.Weather.YIN))
        assertEquals(3, MenuLayer.weatherSheet(MenuLayer.Weather.FENG))
        assertEquals(4, MenuLayer.weatherSheet(MenuLayer.Weather.HAO_YU))
        assertEquals(5, MenuLayer.weatherSheet(MenuLayer.Weather.XUE))
        // `_create_weather`: cc.rect(0, l*c, s, c), createWithSpriteFrames(a, 6), Loop.
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
