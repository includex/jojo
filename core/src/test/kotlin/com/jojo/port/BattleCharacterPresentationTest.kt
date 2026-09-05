package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BattleCharacterPresentationTest {
    @Test
    fun `BattleLayer dispatches every strict character route and rejects unrelated states`() {
        BattleCharacterStrictState.entries.forEach { route ->
            assertEquals(route, BattleLayer.parseBattleCharacterRoute("battle-character-${route.route}-fixture"))
            assertEquals(route, BattleLayer.parseBattleCharacterRoute("battle-character-${route.route}"))
        }
        assertNull(BattleLayer.parseBattleCharacterRoute("battle-auto-battle-active-fixture"))
        assertNull(BattleLayer.parseBattleCharacterRoute(null))
    }

    @Test
    fun `partial HP commands preserve all four source camp frames and top offset`() {
        val frames = BattleCharacterCamp.entries.mapIndexed { index, camp ->
            val state = BattleCharacterPresentation("u$index", camp, maxHp = 100, hp = 75 - index * 15)
            BattleCharacterStateRenderer.commands(state, index * 100f, 200f, "avatar-$index")[1]
        }
        assertEquals(listOf("Mark_5-1", "Mark_3-1", "Mark_68-1", "Mark_2-1"), frames.map { it.assetFrameId })
        listOf(66f, 52.8f, 39.6f, 26.4f).zip(frames).forEach { (expected, event) ->
            assertEquals(expected, event.width, .001f)
        }

        val top = BattleCharacterPresentation("top", BattleCharacterCamp.MINE, 100, 50)
        assertEquals(291f, BattleCharacterStateRenderer.commands(top, 0f, 200f, "avatar", barOnTop = true)[1].y)
    }

    @Test
    fun `outline and authored highlight events retain exact material value contract`() {
        val state = BattleCharacterPresentation("u", BattleCharacterCamp.MINE, 100)
        state.beginAttack(delayed = true)
        assertEquals(BattleCharacterPresentation.GONG_JI_DELAY, state.action)
        assertEquals(BattleCharacterMaterial.OUTLINE, state.material)
        state.animationMaterialEvent(116)
        assertEquals(BattleCharacterMaterial.HIGHLIGHT, state.material)
        assertEquals(1.6f, state.materialValue)
        state.animationMaterialEvent(4)
        assertEquals(BattleCharacterMaterial.HIGHLIGHT, state.material)
        assertEquals(.4f, state.materialValue)
        state.finishAttack()
        assertEquals(BattleCharacterMaterial.DEFAULT, state.material)
        assertNull(state.materialValue)
    }

    @Test
    fun `hit impact exposes partial HP and fully specified source harm label then cleans up`() {
        val state = BattleCharacterPresentation("u", BattleCharacterCamp.ENEMY, 100, 80)
        state.hitImpact(30)
        assertEquals(50, state.hp)
        assertEquals(BattleCharacterPresentation.SHOU_GONG_JI3, state.action)
        val commands = BattleCharacterStateRenderer.commands(state, 100f, 200f, "hurt-frame")
        assertEquals(3, commands.size)
        assertEquals(44f, commands[1].width)
        with(commands[2]) {
            assertEquals("30", text)
            assertEquals(100f, x)
            assertEquals(272f, y)
            assertEquals(9_212_044, outlineRgb)
            assertEquals(1, outlineWidth)
            assertEquals(999, zIndex)
        }
        state.finishHit()
        assertEquals(BattleCharacterPresentation.STAND, state.action)
        assertNull(state.harm)
        assertEquals(2, BattleCharacterStateRenderer.commands(state, 100f, 200f, "idle-frame").size)
    }

    @Test
    fun `death action hides child UI before action24 and completion hides actor`() {
        val state = BattleCharacterPresentation("u", BattleCharacterCamp.FRIEND, 100, 0)
        state.finishHit()
        state.beginHide(BattleHideType.SI_WANG)
        assertEquals(BattleCharacterPresentation.DEATH, state.action)
        assertTrue(state.visible)
        assertFalse(state.infoVisible)
        assertFalse(state.statusVisible)
        assertTrue(state.retreatFlag)
        val deathDraws = BattleCharacterStateRenderer.commands(state, 100f, 200f, "death-frame")
        assertEquals(1, deathDraws.size)
        assertEquals("death-frame", deathDraws.single().assetFrameId)

        state.finishHide()
        assertFalse(state.visible)
        assertEquals(1, state.retreatCount)
        assertEquals(0, state.hp)
        assertTrue(BattleCharacterStateRenderer.commands(state, 100f, 200f, "hidden").isEmpty())
    }

    @Test
    fun `extended JSONL schema records visual material and label outline`() {
        val state = BattleCharacterPresentation("u", BattleCharacterCamp.ENEMY, 100, 80)
        state.hitImpact(30)
        val jsonl = BattleCharacterStateRenderer.jsonl(
            BattleCharacterStrictState.HIT_IMPACT,
            BattleCharacterStateRenderer.commands(state, 100f, 200f, "hurt-frame"),
        )
        assertTrue(jsonl.contains("\"materialId\":\"builtin-2d-sprite (Instance)\""))
        assertTrue(jsonl.contains("\"outlineRgb\":9212044"))
        assertTrue(jsonl.contains("\"outlineWidth\":1"))
        assertTrue(jsonl.contains("\"phase\":\"battle-character-hit-impact\""))
    }

    @Test
    fun `avatar draw identity includes atlas crop and final mirror`() {
        val draw = BattleCharacterStateRenderer.commands(
            BattleCharacterPresentation("u", BattleCharacterCamp.ENEMY, 100),
            100f,
            200f,
            "movement-atlas",
            avatarSourceRect = listOf(0, 201, 48, 48),
            avatarFlipX = true,
            avatarFlipY = false,
        ).first()
        assertEquals(listOf(0, 201, 48, 48), draw.sourceRect)
        assertEquals(true, draw.flipX)
        assertEquals(false, draw.flipY)
        val jsonl = BattleCharacterStateRenderer.jsonl(BattleCharacterStrictState.HP_CAMPS_PARTIAL, listOf(draw))
        assertTrue(jsonl.contains("\"sourceRect\":[0, 201, 48, 48]"))
        assertTrue(jsonl.contains("\"flipX\":true"))
    }
}
