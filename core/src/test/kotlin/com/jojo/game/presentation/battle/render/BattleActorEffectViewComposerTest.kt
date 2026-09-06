// Battle Render Test
package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.graphics.Texture
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.presentation.battle.assets.MagicEffectDefinition
import com.jojo.game.presentation.battle.timeline.MagicEffectAnimation
import com.jojo.game.presentation.battle.timeline.UnitActionAnimation
import com.jojo.game.presentation.battle.unit.BattleUnitPresentationState
import com.jojo.game.presentation.battle.unit.BattleUnitStateAnimation
import com.jojo.game.presentation.battle.unit.ScriptedUnitVisual
import com.jojo.game.presentation.battle.unit.UnitSpriteFrame
import com.jojo.game.presentation.battle.unit.UnitSpriteSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/** Actor/effect view composer가 Screen Port의 조회 결과만으로 renderer 입력을 만드는지 검증한다. */
class BattleActorEffectViewComposerTest {
    @Test
    fun `composes actor coordinates hp and terrain values from port`() {
        val unit = BattleUnit("unit", "유닛", Faction.PLAYER, 2, 3, hitPoints = 25, maxHitPoints = 100)

        val view = BattleActorEffectViewComposer(FakePort()).compose(listOf(unit))

        assertEquals(10f, view.boardLeft)
        assertEquals(20f, view.boardBottom)
        assertEquals(48f, view.tileSize)
        assertEquals(1, view.actors.size)
        assertEquals("unit", view.actors.single().id)
        assertEquals(4f, view.actors.single().tileX)
        assertEquals(5f, view.actors.single().tileY)
        assertEquals(0.25f, view.actors.single().hpRatio)
        assertFalse(view.actors.single().showHpBar)
    }

    private class FakePort : BattleActorEffectViewComposer.Port {
        override fun boardLeft() = 10f
        override fun boardBottom() = 20f
        override fun tileSize() = 48f
        override fun animationClock() = 3f
        override fun stateEffectAnimationClock() = 3f
        override fun dialogueBlendRoute() = false
        override fun battleMenuOpen() = false
        override fun sourceScenario() = "S_01"
        override fun spriteFrame(unit: BattleUnit) = UnitSpriteFrame(UnitSpriteSource.MOVEMENT, sourceY = 12)
        override fun activeAction(unitId: String, now: Float): UnitActionAnimation? = null
        override fun deathAnimationActive(unitId: String, now: Float) = true
        override fun scriptedVisual(unitId: String): ScriptedUnitVisual? = null
        override fun texture(unit: BattleUnit, source: UnitSpriteSource): Texture? = null
        override fun visualTile(unit: BattleUnit) = 4f to 5f
        override fun terrainAt(unit: BattleUnit) = 10
        override fun terrainMask(terrain: Int): Texture? = null
        override fun hpTexture(unit: BattleUnit): Texture? = null
        override fun hpRatio(unit: BattleUnit, now: Float) = 0.25f
        override fun attributeStatuses(unit: BattleUnit): Map<BattleAttribute, BattleUnitPresentationState.AttributeStatusIcon> = emptyMap()
        override fun otherNodesVisible(unit: BattleUnit) = true
        override fun stateEffect(unit: BattleUnit): BattleUnitStateAnimation.Effect? = null
        override fun stateTexture(textureIndex: Int): Texture? = null
        override fun magicEffectAnimations(): List<MagicEffectAnimation> = emptyList()
        override fun magicEffect(effectId: Int): MagicEffectDefinition? = null
        override fun magicEffectTexture(effectId: Int): Texture? = null
        override fun presentationUnit(unitId: String): BattleUnit? = null
        override fun sayTexture(): Texture? = null
        override fun dialogueSpeakerId(): Int? = null
    }
}
