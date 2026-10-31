package com.jojo.engine.graphics

import kotlin.test.Test
import kotlin.test.assertEquals

class MaxDimensionImageFitTest {
    @Test
    fun `landscape source is centered while longest dimension uses anchor height`() {
        val bounds = MaxDimensionImageFit.fit(400, 200, 10f, 20f, 200f, 300f)
        assertEquals(MaxDimensionImageFit.Bounds(-40f, 95f, 300f, 150f), bounds)
    }

    @Test
    fun `portrait source preserves ratio and anchor center`() {
        val bounds = MaxDimensionImageFit.fit(100, 200, 10f, 20f, 200f, 300f)
        assertEquals(MaxDimensionImageFit.Bounds(35f, 20f, 150f, 300f), bounds)
    }
}
