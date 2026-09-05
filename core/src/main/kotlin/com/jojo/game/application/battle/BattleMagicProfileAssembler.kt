package com.jojo.game.application.battle

import com.jojo.game.GameDataCatalog
import com.jojo.game.domain.battle.magic.BattleMagicHitAreaValue
import com.jojo.game.domain.battle.magic.BattleMagicProfileValue

/** The sole catalog-to-battle spell composition boundary. */
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
