// Battle
package com.jojo.game.application.battle

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.battle.magic.BattleMagicHitAreaValue
import com.jojo.game.domain.battle.magic.BattleMagicProfileValue

/** toBattleMagicProfile: 게임 데이터의 마법 정의를 전투 계산에 사용하는 불변 마법 프로필로 변환한다. */
internal fun GameDataCatalog.MagicProfile.toBattleMagicProfile() = BattleMagicProfileValue(
    id = id,
    name = name,
    type = type,
    target = target,
    hitArea = BattleMagicHitAreaValue(hitArea.id, hitArea.offsets, hitArea.allScreen, hitArea.upgradeId),
    effectAreaId = effectAreaId,
    effectOffsets = effectOffsets.toSet(),
    expendMp = expendMp,
    power = power,
    harmType = harmType,
    category = category,
    effectId = effectId,
    condition = condition,
    aiUse = aiUse,
    hitRateLimit = hitRateLimit,
    icon = icon,
    intro = intro,
)
