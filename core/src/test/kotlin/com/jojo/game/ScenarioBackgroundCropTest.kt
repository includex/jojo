package com.jojo.game

import com.jojo.game.presentation.scenario.render.ScenarioBackgroundCrop
import kotlin.test.*

class ScenarioBackgroundCropTest {
    @Test fun `native aspect keeps the complete background upright`() {
        assertEquals(ScenarioBackgroundCrop.Uv(0f,0f,1f,1f),ScenarioBackgroundCrop.cover(1280,688))
    }
    @Test fun `provided backgrounds cover the stage without stretching and stay centered`() {
        for((width,height) in listOf(1671 to 941,1448 to 1086,2048 to 512)) {
            val crop=ScenarioBackgroundCrop.cover(width,height)
            assertEquals(1f,crop.left+crop.right,.00001f)
            assertEquals(1f,crop.top+crop.bottom,.00001f)
            assertTrue(crop.left>=0f && crop.top>=0f && crop.right<=1f && crop.bottom<=1f)
            assertTrue(crop.bottom>crop.top)
            val sampledAspect=(crop.right-crop.left)*width/((crop.bottom-crop.top)*height)
            assertEquals(1280f/688f,sampledAspect,.00001f)
            assertTrue(crop.left==0f || crop.top==0f)
        }
    }
    @Test fun `palace retains horizontal throne center while cropping equally above and below`() {
        val crop=ScenarioBackgroundCrop.cover(1448,1086)
        assertEquals(0f,crop.left)
        assertEquals(1f,crop.right)
        assertEquals(.5f,(crop.top+crop.bottom)/2f)
        assertEquals(153.85f,crop.top*1086f,.1f)
    }
}
