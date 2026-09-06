// Test
package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.presentation.battle.edit.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** BattleEditLayer2Test: BattleEditLayer2의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleEditLayer2Test {
    @Test
    fun `weather panel exposes five source labels and keeps recovered revert typo`() {
        val layer = BattleEditLayer2(initialWeather = 0, initialRound = 3, canApplyRound = true)
        assertEquals(listOf("맑음", "어두움", "바람", "비", "설"), BattleEditLayer2.weatherNames)
        layer.openWeatherPanel()
        assertTrue(layer.weatherPanelVisible)
        layer.selectWeather(3)
        layer.selectWeather(0)
        assertEquals("맑음", layer.weatherLabel)
        assertEquals(mapOf(0 to 3), layer.pendingValues())
        layer.closeWeatherPanel()
        assertFalse(layer.weatherPanelVisible)
    }

    @Test
    fun `apply mutates weather and gated round then removes layer`() {
        val enabled = BattleEditLayer2(0, 3, canApplyRound = true)
        enabled.selectWeather(2)
        enabled.textChanged("8")
        enabled.editingDidEnd()
        assertEquals(listOf(
            BattleEditLayer2.Effect.SetWeather(2), BattleEditLayer2.Effect.SetRound(8), BattleEditLayer2.Effect.Remove,
        ), enabled.touchButton(0))
        assertTrue(enabled.removed)

        val gated = BattleEditLayer2(0, 3, canApplyRound = false)
        gated.textChanged("8")
        gated.editingDidEnd()
        assertEquals(listOf(
            BattleEditLayer2.Effect.Toast(BattleEditLayer2.ROUND_DISABLED_TOAST), BattleEditLayer2.Effect.Remove,
        ), gated.touchButton(0))
    }

    @Test
    fun `button routes preserve Battle EditLayer child and kill flags`() {
        val layer = BattleEditLayer2(0, 3, canApplyRound = true)
        assertEquals(listOf(BattleEditLayer2.Effect.OpenGlobalEditor), layer.touchButton(2))
        assertEquals(listOf(BattleEditLayer2.Effect.KillAll(3)), layer.touchButton(3))
        assertEquals(listOf(BattleEditLayer2.Effect.KillAll(1)), layer.touchButton(4))
        assertEquals(listOf(BattleEditLayer2.Effect.KillAll(0)), layer.touchButton(5))
        assertEquals(emptyList(), layer.touchButton(1))
        assertEquals(emptyList(), layer.touchButton(2, phase = 1))
    }

    @Test
    fun `ending unchanged round clears a pending edit`() {
        val layer = BattleEditLayer2(0, 3, canApplyRound = true)
        layer.textChanged("9")
        layer.editingDidEnd()
        layer.textChanged("3")
        layer.editingDidEnd()
        assertEquals(emptyMap(), layer.pendingValues())
    }

    @Test
    fun `actual route parser and render states preserve source draw counts`() {
        BattleEditLayer2Route.entries.forEach { route ->
            assertEquals(route, BattleEditLayer2Route.parse("battle-edit2-${route.key}-fixture"))
            if (route == BattleEditLayer2Route.REGISTER) {
                assertEquals(route, BattleEditLayer2Route.parse("battle-register-open-fixture"))
            }
            val model = BattleEditLayer2(0, 1, true)
            when (route) {
                BattleEditLayer2Route.WEATHER -> { model.openWeatherPanel(); model.selectWeather(3) }
                BattleEditLayer2Route.ROUND -> { model.textChanged("8"); model.editingDidEnd() }
                BattleEditLayer2Route.APPLY -> { model.selectWeather(3); model.textChanged("8"); model.editingDidEnd(); model.touchButton(0) }
                else -> Unit
            }
            val count = BattleEditLayer2RenderEvents.jsonl(route, model).lineSequence().count(String::isNotBlank)
            assertEquals(mapOf(
                BattleEditLayer2Route.INITIAL to 23, BattleEditLayer2Route.WEATHER to 36,
                BattleEditLayer2Route.ROUND to 23, BattleEditLayer2Route.APPLY to 1,
                BattleEditLayer2Route.CHILD to 42, BattleEditLayer2Route.CHILD_SCENE to 65,
                BattleEditLayer2Route.REGISTER to 54,
            ).getValue(route), count)
        }
    }

    @Test
    fun `render event writers retain route append order`() {
        fun trace(route: BattleEditLayer2Route) =
            BattleEditLayer2RenderEvents.jsonl(route, BattleEditLayer2(0, 1, true))

        val weather = trace(BattleEditLayer2Route.WEATHER)
        assertTrue(weather.indexOf("전장 편집") < weather.indexOf("맑음"))

        val childScene = trace(BattleEditLayer2Route.CHILD_SCENE)
        assertTrue(childScene.indexOf("전역 변수 편집") < childScene.indexOf("0 영천의 전투"))

        val register = trace(BattleEditLayer2Route.REGISTER)
        assertTrue(register.indexOf("전역 변수 편집") < register.indexOf("등록 코드 생성기"))
    }
}
