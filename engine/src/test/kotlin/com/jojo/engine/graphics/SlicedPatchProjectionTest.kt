package com.jojo.engine.graphics

import com.badlogic.gdx.math.Matrix4
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SlicedPatchProjectionTest {
    @Test
    fun identityTranslationAndScaleUseSourceFloatThenOneFinalRounding() {
        assertEquals(1.1f, SlicedPatchProjection.project(1.1, 1f, 0f))
        assertEquals(3.25f, SlicedPatchProjection.project(1.5, 1f, 1.75f))
        assertEquals(4f, SlicedPatchProjection.project(1.5, 2f, 1f))

        val boundary = SlicedPatchProjection.project(1.1, 1.1f, -1f)
        assertEquals(0x3e570a41, boundary.toRawBits())
        assertFalse(boundary.toRawBits() == (1.1f * 1.1f - 1f).toRawBits())
    }

    @Test
    fun onlyAxisAlignedTwoDimensionalOrthographicMatricesUseCpuProjection() {
        val supported = Matrix4().apply {
            `val`[Matrix4.M00] = 0.5f
            `val`[Matrix4.M03] = -1f
            `val`[Matrix4.M11] = 0.25f
            `val`[Matrix4.M13] = 1f
            `val`[Matrix4.M22] = -0.02f
            `val`[Matrix4.M23] = -0.5f
        }
        assertTrue(SlicedPatchProjection.supportsCpuProjection(supported.`val`))
        val gpuProjection = Matrix4().apply {
            `val`[Matrix4.M00] = 7f
            `val`[Matrix4.M13] = 9f
        }
        SlicedPatchProjection.setIdentityXyProjection(gpuProjection, supported.`val`)
        assertEquals(1f, gpuProjection.`val`[Matrix4.M00])
        assertEquals(1f, gpuProjection.`val`[Matrix4.M11])
        assertEquals(0f, gpuProjection.`val`[Matrix4.M03])
        assertEquals(0f, gpuProjection.`val`[Matrix4.M13])
        assertEquals(-0.5f, gpuProjection.`val`[Matrix4.M23])
        assertEquals(1f, gpuProjection.`val`[Matrix4.M33])

        for (unsupported in listOf(
            Matrix4(supported).apply { `val`[Matrix4.M01] = 0.1f },
            Matrix4(supported).apply { `val`[Matrix4.M02] = 0.1f },
            Matrix4(supported).apply { `val`[Matrix4.M10] = 0.1f },
            Matrix4(supported).apply { `val`[Matrix4.M12] = 0.1f },
            Matrix4(supported).apply { `val`[Matrix4.M20] = 0.1f },
            Matrix4(supported).apply { `val`[Matrix4.M21] = 0.1f },
            Matrix4(supported).apply { `val`[Matrix4.M30] = 0.1f },
            Matrix4(supported).apply { `val`[Matrix4.M31] = 0.1f },
            Matrix4(supported).apply { `val`[Matrix4.M32] = 0.1f },
            Matrix4(supported).apply { `val`[Matrix4.M33] = 0f },
        )) {
            assertFalse(SlicedPatchProjection.supportsCpuProjection(unsupported.`val`))
        }
    }
}
