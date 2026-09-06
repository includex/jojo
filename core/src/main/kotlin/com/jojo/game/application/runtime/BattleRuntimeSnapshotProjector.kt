// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction

/** 전투 런타임 스냅샷 변환기: 도메인 유닛의 전술 판단 정보를 자동 구동기 전용 읽기 모델로 고정한다. */
internal object BattleRuntimeSnapshotProjector {
    /** 변환: 현재 라운드·활성 진영·유닛 상태를 런타임 probe가 안전하게 조회할 불변 스냅샷으로 만든다. */
    fun project(round: Int, activeFaction: Faction, units: Collection<BattleUnit>): BattleRuntimeSnapshot =
        BattleRuntimeSnapshot(
            round = round,
            activeFaction = activeFaction,
            units = units.map(::projectUnit),
        )

    /** 유닛 변환: 이동·공격·마법 범위와 상태 이상을 런타임 격자 좌표로 보존한다. */
    private fun projectUnit(unit: BattleUnit): RuntimeBattleUnitSnapshot = RuntimeBattleUnitSnapshot(
        id = unit.id,
        faction = unit.faction,
        effectiveFaction = unit.effectiveFaction(),
        characterId = unit.characterId,
        x = unit.tileX,
        y = unit.tileY,
        hitPoints = unit.hitPoints,
        magicPoints = unit.magicPoints,
        level = unit.level,
        attack = unit.attack,
        defense = unit.defense,
        visible = unit.visible,
        hasActed = unit.hasActed,
        statuses = unit.statuses.keys.toSet(),
        attackOffsets = unit.attackOffsets.mapTo(linkedSetOf()) { RuntimeGridPoint(it.first, it.second) },
        attackAllScreen = unit.attackAllScreen,
        magic = unit.magic.map { magic ->
            RuntimeMagicSnapshot(
                magic.id, magic.target, magic.expendMp, magic.power, magic.category, magic.hitArea.allScreen,
                magic.hitArea.offsets.mapTo(linkedSetOf()) { RuntimeGridPoint(it.first, it.second) },
            )
        },
        retreatCount = unit.retreatCount,
        hasAuthoredX = unit.hasAuthoredTileX,
        hasAuthoredY = unit.hasAuthoredTileY,
    )
}
