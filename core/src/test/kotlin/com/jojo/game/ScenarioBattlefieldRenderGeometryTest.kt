package com.jojo.game

import com.jojo.game.presentation.scenario.render.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioBattlefieldRenderGeometryTest {
    @Test fun `battlefield geometry preserves source map and clipped-head transforms`() {
        assertEquals(672f, ScenarioBattlefieldRenderGeometry.mapX(0f, 0f))
        assertEquals(1073.28f, ScenarioBattlefieldRenderGeometry.mapY(0f, 0f))
        assertEquals(155.04f, ScenarioBattlefieldRenderGeometry.headCenterX(50f), 0.0001f)
        assertEquals(447.2f, ScenarioBattlefieldRenderGeometry.headCenterY(100f), 0.0001f)
    }

    @Test fun `battlefield order is z-index then retained sibling order`() {
        val view = ScenarioBattlefieldRenderView(
            backgroundId = 2, drawCharacters = true, drawUnits = true,
            units = listOf(unit(7, z = 3f, order = 0), unit(8, z = -2f, order = 1, visible = false)),
            heads = listOf(head(11, z = 3f, order = 2), head(12, z = -1f, order = 3)),
        )
        assertEquals(listOf("head:12", "unit:7", "head:11"), ScenarioBattlefieldRenderGeometry.orderedNodeIds(view))
    }

    private fun unit(id: Int, z: Float, order: Int, visible: Boolean = true) =
        ScenarioBattlefieldUnitView(id, 0f, 0f, visible, z, order, 1, 0, false, false)
    private fun head(id: Int, z: Float, order: Int) = ScenarioBattlefieldHeadView(id, 0f, 0f, 1f, z, order)
}
