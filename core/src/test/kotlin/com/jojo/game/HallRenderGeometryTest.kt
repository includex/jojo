package com.jojo.game

import com.jojo.game.presentation.scenario.hall.render.HallRenderGeometry
import kotlin.test.Test
import kotlin.test.assertEquals

class HallRenderGeometryTest {
    @Test fun `hall menu preserves authored button order and centers`() {
        assertEquals(9, HallRenderGeometry.menuButtonCenters.size)
        assertEquals(55.107f, HallRenderGeometry.menuButtonCenters.first())
        assertEquals(789.44f, HallRenderGeometry.menuButtonCenters.last())
    }

    @Test fun `save rows retain the source descending 52 pixel traversal`() {
        assertEquals(547.534f, HallRenderGeometry.saveRowY(0))
        assertEquals(495.534f, HallRenderGeometry.saveRowY(1))
        assertEquals(183.534f, HallRenderGeometry.saveRowY(7))
    }
}
