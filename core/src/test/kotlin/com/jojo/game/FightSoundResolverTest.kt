package com.jojo.game

import com.jojo.game.presentation.battle.fight.FightSoundResolver
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * class  `FightSoundResolverTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class FightSoundResolverTest {
    @Test
    fun `yidong maps source arm move types and preserves silent values`() {
        assertEquals(24, FightSoundResolver.resolve("yidong", 0).effectId)
        assertEquals(25, FightSoundResolver.resolve("yidong", 1).effectId)
        assertEquals(23, FightSoundResolver.resolve("yidong", 2).effectId)
        assertNull(FightSoundResolver.resolve("yidong", 3).effectId)
        assertNull(FightSoundResolver.resolve("yidong", -1).effectId)
        assertEquals(24, FightSoundResolver.resolve("yidong", 4).effectId)
    }

    @Test
    fun `numeric callback routes effects and greater-than-300 background IDs`() {
        assertEquals(8, FightSoundResolver.resolve("8").effectId)
        assertNull(FightSoundResolver.resolve("8").backgroundId)
        assertEquals(19, FightSoundResolver.resolve("319").backgroundId)
        assertNull(FightSoundResolver.resolve("319").effectId)
    }
}
