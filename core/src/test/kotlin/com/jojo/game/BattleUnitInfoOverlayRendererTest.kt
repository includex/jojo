// Test
package com.jojo.game

import com.jojo.game.presentation.battle.*
import com.jojo.game.presentation.battle.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleUnitInfoOverlayRendererTest {
    @Test
    fun `unit info snapshot preserves selected tab and unit presentation values`() {
        val view = BattleUnitInfoOverlayView(
            tab = 3,
            unit = BattleUnitInfoUnitView(
                name = "관우",
                post = "무장",
                level = 12,
                hp = 80,
                maxHp = 100,
                mp = 20,
                maxMp = 30,
                attack = 55,
                defense = 44,
                spirit = 33,
                critical = 22,
                morale = 11,
            ),
            buttons = listOf(false, true, true, false, true, true, true, true, false, true),
            magicRows = listOf("화계", "수계"),
        )

        assertEquals(3, view.tab)
        assertEquals("관우", view.unit.name)
        assertEquals(80, view.unit.hp)
        assertEquals(listOf("화계", "수계"), view.magicRows)
        assertEquals(true, view.buttons[9])
    }

    /**
     * 기본 탭은 예전에 60/70/80과 서식 문자열 `%d`가 박힌 스텁이었다. 원본 `_ref0`처럼
     * 유닛의 다섯 능력치와 출진·퇴각 횟수를 실제로 반영해야 한다.
     */
    @Test
    fun `기본 탭 문구는 유닛의 다섯 능력치와 출전 횟수를 쓴다`() {
        val unit = BattleUnitInfoUnitView(
            name = "하후돈", post = "무장", level = 5, hp = 90, maxHp = 90, mp = 8, maxMp = 8,
            attack = 42, defense = 53, spirit = 38, critical = 32, morale = 31,
            mine = true, battleCount = 7, retreatCount = 2,
        )

        val texts = BattleUnitInfoOverlayRenderer.baseLabels(unit).map { it.first }

        // 무력=attack, 지력=spirit, 지휘=defense, 민첩성=critical, 운기=morale
        assertEquals(listOf("42", "38", "53", "32", "31"), texts.filter { it.toIntOrNull() != null })
        assertEquals(true, texts.contains("출진 횟수 7 / 퇴각 횟수 2"))
        assertEquals(false, texts.any { it.contains("%d") })
    }

    /** 원본은 아군이 아닌 유닛에게는 출진·퇴각 횟수 줄을 켜지 않는다. */
    @Test
    fun `아군이 아니면 출전 횟수 줄을 내보내지 않는다`() {
        val unit = BattleUnitInfoUnitView(
            name = "장량", post = "무장", level = 5, hp = 90, maxHp = 90, mp = 8, maxMp = 8,
            attack = 1, defense = 2, spirit = 3, critical = 4, morale = 5,
            mine = false, battleCount = 7, retreatCount = 2,
        )

        assertEquals(
            false,
            BattleUnitInfoOverlayRenderer.baseLabels(unit).any { it.first.startsWith("출진 횟수") },
        )
    }
}
