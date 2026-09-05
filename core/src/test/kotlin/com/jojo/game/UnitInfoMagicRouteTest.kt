package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UnitInfoMagicRouteTest {
    private val magic = MagicUiList.Magic(
        id = 2,
        name = "회오리",
        cost = 6,
        power = 50,
        icon = 2,
        hit = 13,
        eff = 0,
        intro = "보통의 풍계는 단일 적군에게 소량의 피해를 줍니다.",
    )

    private fun unitInfo() = UnitInfoLayer(listOf(UnitInfoLayer.Unit(
        id = 0, name = "조조", post = "", level = 3,
        hp = 100, maxHp = 100, mp = 30, maxMp = 30,
        attack = 80, defense = 70, spirit = 65, critical = 60, morale = 75,
        magic = listOf(magic.name),
    ))).also { it.onCreate() }

    @Test
    fun `actual UnitInfo magic tab row route opens Global108 and both close targets remove it`() {
        val info = unitInfo()
        val layer = UnitInfoMagicRoute.open(info, listOf(magic), event = UnitInfoLayer.TOUCH_END)

        assertEquals(4, info.ref().tab)
        assertEquals(magic, layer?.magic)
        assertEquals(listOf("asset:Game/Magic/3-1", "asset:Game/Hitarea/14-1", "asset:Game/Effarea/1-1"), layer?.assets)
        assertTrue(requireNotNull(layer).attached)
        layer.close(UnitInfoLayer.TOUCH_END)
        assertFalse(layer.attached)

        val panelCancelLayer = requireNotNull(UnitInfoMagicRoute.open(unitInfo(), listOf(magic)))
        panelCancelLayer.close(UnitInfoLayer.TOUCH_END)
        assertFalse(panelCancelLayer.attached)
    }

    @Test
    fun `TOUCH_START never changes tab or opens child route`() {
        val info = unitInfo()
        assertNull(UnitInfoMagicRoute.open(info, listOf(magic), event = 0))
        assertEquals(0, info.ref().tab)
        assertTrue(info.takeRoutes().isEmpty())
    }
}
