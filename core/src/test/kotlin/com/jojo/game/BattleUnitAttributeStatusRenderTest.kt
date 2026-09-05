package com.jojo.game

import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.presentation.battle.unit.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * class  `BattleUnitAttributeStatusRenderTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleUnitAttributeStatusRenderTest {
    @Test
    fun `six prefab positions and down-up textures are rendered at map scale two`() {
        val statuses = BattleAttribute.entries.associateWith { attribute ->
            BattleUnitPresentationState.AttributeStatusIcon(active = true, down = attribute.ordinal % 2 == 0)
        }
        val commands = BattleUnitAttributeStatusRender.commands(statuses, true, 100f, 200f, 96f)

        assertEquals(6, commands.size)
        assertEquals(BattleUnitAttributeStatusRender.Command(BattleAttribute.ATTACK, 0, 100f, 268f, 24f), commands[0])
        assertEquals(BattleUnitAttributeStatusRender.Command(BattleAttribute.DEFENSE, 1, 100f, 238f, 24f), commands[1])
        assertEquals(BattleUnitAttributeStatusRender.Command(BattleAttribute.MOVEMENT, 1, 172f, 206f, 24f), commands[5])
    }

    @Test
    fun `normal and hidden other-node states emit no icon`() {
        val active = mapOf(BattleAttribute.ATTACK to BattleUnitPresentationState.AttributeStatusIcon(true, false))
        assertTrue(BattleUnitAttributeStatusRender.commands(emptyMap(), true, 0f, 0f, 96f).isEmpty())
        assertTrue(BattleUnitAttributeStatusRender.commands(active, false, 0f, 0f, 96f).isEmpty())
    }
}
