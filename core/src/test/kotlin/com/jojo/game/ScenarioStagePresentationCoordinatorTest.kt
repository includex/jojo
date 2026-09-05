package com.jojo.game
import com.jojo.game.domain.campaign.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScenarioStagePresentationCoordinatorTest {
    @Test
    fun `presentation request queue preserves FIFO and drains only the consumed channel`() {
        val requests = ScenarioStagePresentationRequestQueue()
        val first = ScenarioScriptPresentationRequest.UnitHighlight(7)
        val second = ScenarioScriptPresentationRequest.RectangleHighlight(1, 2, 3, 4)

        requests.requestUnitHide(3, 9)
        requests.requestUnitHide(4, -1)
        requests.requestCameraCenter(10, 20)
        requests.requestCameraCenter(11, 20)
        requests.requestScriptPresentation(first)
        requests.requestScriptPresentation(second)

        assertEquals(ScenarioUnitHideRequest(3, 2), requests.consumeUnitHideRequest())
        assertEquals(ScenarioUnitHideRequest(4, 0), requests.consumeUnitHideRequest())
        assertNull(requests.consumeUnitHideRequest())
        assertEquals(listOf(ScenarioCameraCenterRequest(10, 20), ScenarioCameraCenterRequest(11, 20)), requests.consumeCameraCenterRequests())
        assertEquals(first, requests.consumeScriptPresentationRequest())
        assertEquals(listOf(second), requests.consumeScriptPresentationRequests())
    }

    @Test
    fun `scripted actions mutate the unit before queuing the render action`() {
        val actions = ScenarioStageScriptedActions()
        val unit = TacticalUnit(9, 2, 3)
        val events = mutableListOf<String>()

        actions.setUnitAction(9, action = 5, direction = 1, loop = true, unitProvider = { unit }) { _, direction ->
            events += "direction:$direction/action:${unit.action}"
            unit.direction = direction
        }
        actions.attack(9, 4, 2)

        assertEquals(listOf("direction:1/action:5"), events)
        assertEquals(1, unit.direction)
        assertEquals(listOf(ScriptedUnitAction(9, 5, 1, true)), actions.consumeUnitActions())
        assertEquals(listOf(ScriptedAttackAction(9, 4, 2)), actions.consumeAttacks())
        assertTrue(actions.consumeUnitActions().isEmpty())
    }

    @Test
    fun `campaign presentation changes keep model posts changes separate from avatar requests`() {
        val data = GameDataCatalog.load()
        val profile = requireNotNull(data.unitProfile(0))
        val campaign = CampaignState().also { it.setUnitAttribute(0, 17, profile.posts) }
        val changes = ScenarioStageCampaignPresentationChanges()
        val unit = TacticalUnit(0, 0, 0)

        val change = requireNotNull(
            changes.setModelUnitPosts(0, profile.posts + 1, 3, data, 0, campaign) { unit },
        )

        assertEquals(profile.posts + 1, unit.posts)
        assertEquals(listOf(change), changes.consumeUnitPostsChanges())
        assertNull(changes.consumeUnitPostsRequest())
    }
}
