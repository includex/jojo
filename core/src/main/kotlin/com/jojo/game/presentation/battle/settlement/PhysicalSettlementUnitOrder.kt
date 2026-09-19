package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.battle.TacticalActionResult

/** _attack2 records the attacker's feat before _attack3 records target vitals. */
internal fun physicalSettlementUnitOrder(
    attack: TacticalActionResult.Attack,
    actorId: String?,
    targetId: String?,
): List<String> = linkedSetOf<String>().apply {
    if (attack.physicalPasses.isNotEmpty()) {
        attack.physicalPasses.forEach { pass ->
            add(pass.attackerId)
            pass.targets.forEach { add(it.targetId) }
        }
    } else {
        actorId?.let(::add)
        targetId?.let(::add)
        attack.splashTargets.forEach { add(it.targetId) }
    }
}.toList()
