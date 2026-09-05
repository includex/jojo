package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleUnitEditLayerTest {
    @Test fun `production hall unit info route only opens edit on enabled button10 touch end`() {
        val route=HallBattleUnitEditRoute(true)
        assertFalse(route.unitInfoButton(10,true))
        assertTrue(route.selectUnit(true))
        assertFalse(route.unitInfoButton(9,true))
        assertFalse(route.unitInfoButton(10,false))
        assertTrue(route.unitInfoButton(10,true))
        assertEquals(HallBattleUnitEditRoute.State.EDIT,route.state)
    }

    @Test fun `apply mutates pending attack and closes`() {
        val edit=BattleUnitEditLayer()
        edit.editAttack(77)
        assertEquals(listOf(BattleUnitEditLayer.Effect.SetAttack(77),BattleUnitEditLayer.Effect.Close),edit.button(0))
        assertEquals(77,edit.attack)
    }

    @Test fun `posts panel and avatar gate match source`() {
        val edit=BattleUnitEditLayer()
        edit.openPosts();assertTrue(edit.postsPanelVisible)
        edit.closePosts();assertFalse(edit.postsPanelVisible)
        assertEquals(listOf(BattleUnitEditLayer.Effect.Toast(BattleUnitEditLayer.AVATAR_GATE_TOAST)),edit.button(2))
        assertEquals(listOf(BattleUnitEditLayer.Effect.OpenAvatarEditor),edit.button(2,true))
    }
}
