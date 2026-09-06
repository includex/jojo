// Battle Unit Test
package com.jojo.game.presentation.battle.unit

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.presentation.battle.timeline.UnitActionAnimation
import com.jojo.game.presentation.battle.timeline.UnitAnimationKind
import com.jojo.game.presentation.battle.timeline.UnitMoveAnimation
import kotlin.test.Test
import kotlin.test.assertEquals

/** 유닛 sprite frame resolver가 기존 route와 animation 우선순위를 유지하는지 검증한다. */
class BattleUnitSpriteFrameResolverTest {
    @Test
    fun `win condition avatar frame overrides default timeline frame`() {
        val unit = BattleUnit("u-235", "유닛", Faction.PLAYER, 0, 0, characterId = 235)
        val port = FakePort(winCondition = true)

        val frame = BattleUnitSpriteFrameResolver(port).frame(unit)

        assertEquals(UnitSpriteSource.SPECIAL, frame.source)
        assertEquals(151, frame.sourceY)
    }

    @Test
    fun `active transient animation overrides scripted and idle frames`() {
        val unit = BattleUnit("u-1", "유닛", Faction.PLAYER, 0, 0, characterId = 1)
        val port = FakePort(
            clock = 2f,
            transient = UnitActionAnimation("u-1", UnitAnimationKind.ATTACK, 2, startedAt = 1f, endsAt = 3f, sourceAction = 6),
            scripted = ScriptedUnitVisual(action = 9, startedAt = 0f),
        )

        val frame = BattleUnitSpriteFrameResolver(port).frame(unit)

        assertEquals(UnitSpriteSource.ATTACK, frame.source)
        assertEquals(62, frame.sourceY)
    }

    private class FakePort(
        private val clock: Float = 0f,
        private val winCondition: Boolean = false,
        private val transient: UnitActionAnimation? = null,
        private val scripted: ScriptedUnitVisual? = null,
    ) : BattleUnitSpriteFrameResolver.Port {
        override fun dialogueOneRoute() = false
        override fun hudRoute() = false
        override fun rewardRouteActive() = false
        override fun itemUpgradeRouteActive() = false
        override fun battleDialogueBlendRoute() = false
        override fun winConditionRouteActive() = winCondition
        override fun animationClock() = clock
        override fun elapsed() = clock
        override fun returnScenario() = "R_00"
        override fun avatarId(unit: BattleUnit) = unit.characterId
        override fun defaultAction(unit: BattleUnit) = BattleUnitPresentationState.DefaultAction(action = 0, loop = true)
        override fun transientAnimation(unitId: String) = transient?.takeIf { it.unitId == unitId }
        override fun movementAnimation(unitId: String): UnitMoveAnimation? = null
        override fun scriptedVisual(unitId: String) = scripted?.takeIf { unitId == "u-1" }
        override fun presentationUnit(unitId: String) = BattleUnit(unitId, "유닛", Faction.PLAYER, 0, 0)
        override fun timelineFrame(action: Int, direction: Int, elapsed: Float, loop: Boolean) =
            BattleSpriteTimeline.Frame(
                source = if (action == 6) UnitSpriteSource.ATTACK else UnitSpriteSource.MOVEMENT,
                sourceY = action * 10 + direction,
                sourceWidth = 48,
                sourceHeight = 48,
                flipX = false,
            )
    }
}
