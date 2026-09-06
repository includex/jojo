// Runtime Test
package com.jojo.game.application.runtime

import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import kotlin.test.Test
import kotlin.test.assertEquals

/** 전투 런타임 스냅샷 변환기가 자동 구동에 필요한 유닛 전술 정보를 보존하는지 확인한다. */
class BattleRuntimeSnapshotProjectorTest {
    /** 변환: 유닛의 진영·격자 위치·상태·공격 범위를 런타임 조회 모델에 복사한다. */
    @Test
    fun `유닛 전술 정보를 런타임 스냅샷으로 고정한다`() {
        val unit = BattleUnit("unit-7", "검증", Faction.PLAYER, 4, 9, hitPoints = 72, magicPoints = 18).apply {
            attackOffsets = setOf(1 to 0)
            statuses[BattleStatus.POISON] = 2
            hasActed = true
        }

        val snapshot = BattleRuntimeSnapshotProjector.project(3, Faction.PLAYER, listOf(unit))

        assertEquals(3, snapshot.round)
        assertEquals(Faction.PLAYER, snapshot.activeFaction)
        assertEquals(1, snapshot.units.size)
        assertEquals("unit-7", snapshot.units.single().id)
        assertEquals(setOf(RuntimeGridPoint(1, 0)), snapshot.units.single().attackOffsets)
        assertEquals(setOf(BattleStatus.POISON), snapshot.units.single().statuses)
        assertEquals(true, snapshot.units.single().hasActed)
    }
}
