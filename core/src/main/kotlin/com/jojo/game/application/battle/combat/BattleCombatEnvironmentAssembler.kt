package com.jojo.game.application.battle.combat

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.magic.MagicEnvironment
import com.jojo.game.domain.battle.combat.*

/**
 * Assembles combat-facing environments.  Physical, magic, and tactical
 * inputs stay together because they share the combat callbacks, but no turn,
 * movement, or AI state is owned here.
 */
internal object BattleCombatEnvironmentAssembler {
    fun tactical(battle: Battle): BattleTacticalActionEnvironment = BattleTacticalActionEnvironment(
        outcome = battle::outcome,
        units = { battle.units },
        activeFaction = { battle.activeFaction },
        areAllied = battle::areAllied,
        movementOffsets = battle.configuration.movementOffsets,
        propertyItems = battle.configuration.propertyItems,
            consumeSelectedProperty = { itemId ->
                battle.experience.consumeSelectedProperty(itemId, battle.configuration.consumeProperty)
            },
            notifyPermanentProperty = { item, target ->
                battle.experience.notifyPermanentProperty(item, target, battle.configuration.onPermanentProperty)
            },
        physicalCombatEnvironment = { physical(battle) },
        magicEnvironment = { magic(battle) },
    )

    fun physicalContext(battle: Battle): BattlePhysicalContextEnvironment = BattlePhysicalContextEnvironment(
        units = { battle.units.values },
        unitAt = battle::unitAt,
        terrain = battle.configuration.terrain,
        weather = { battle.weather },
        infantryOffsets = battle.configuration.infantryOffsets,
        skillTemp = battle::skillTemp,
        setSkillTemp = battle::setSkillTemp,
        incSkillTemp = battle::incSkillTemp,
        moveLength = battle.journal::currentMoveLength,
        backPosition = { defender, attacker -> battle.movement.backPosition(defender, attacker, battle::unitAt) },
        facingDirection = battle.movement::facingDirection,
        hasPhysicalEffectTargets = { attacker, target ->
            PhysicalAttackAreaResolver.hasPhysicalEffectTargets(attacker, target, battle::unitAt, battle::areAllied)
        },
        probabilityResolver = battle.probabilityResolver,
    )

    fun combatContext(battle: Battle): BattleCombatEnvironmentContext = BattleCombatEnvironmentContext(
        units = { battle.units.values },
        pendingPresentationUnits = { battle.battlefield.pendingPresentationUnits() },
        unitAt = battle::unitAt,
        areAllied = battle::areAllied,
        weather = { battle.weather },
        setWeather = battle::setWeatherFromCombat,
        terrain = battle.configuration.terrain,
        terrainMagicFlags = battle.configuration.terrainMagicFlags,
        activeFaction = { battle.activeFaction },
        isBattleEnded = { battle.outcome() != null },
        statusRoundFor = battle.configuration.statusRoundFor,
        probabilityResolver = battle.probabilityResolver,
        battleExperience = battle.experience::battleExperience,
        equipmentExperienceAmount = battle.experience::equipmentExperienceAmount,
        notifyBattleExperience = battle.experience::notifyBattleExperience,
        notifyEquipmentExperienceAward = battle.experience::notifyEquipmentExperienceAward,
        notifyPhysicalDamage = battle.experience::notifyPhysicalDamage,
        notifyUnitDefeated = battle.experience::notifyUnitDefeated,
        onDefeat = battle.battlefield::defeat,
        canAttack = battle::canAttack,
        backPosition = { defender, attacker -> battle.movement.backPosition(defender, attacker, battle::unitAt) },
        facingDirection = battle.movement::facingDirection,
        getPlayerMoney = { battle.playerMoney },
        setPlayerMoney = battle::setPlayerMoneyFromEnvironment,
        getEnemyMoney = { battle.enemyMoney },
        setEnemyMoney = battle::setEnemyMoneyFromEnvironment,
        propertyItem = battle.configuration.propertyItems::get,
        zdsyGlobalValue = battle.configuration.zdsyGlobalValue,
        notifyConsumeAutomaticProperty = { itemId ->
            battle.experience.notifyConsumeAutomaticProperty(itemId, battle.configuration.consumeAutomaticProperty)
        },
        incSkillTemp = { id, skill -> battle.incSkillTemp(id, skill) },
        applyProperty = { item, target, consume ->
            battle.combat.applyProperty(
                item,
                target,
                consume,
                { property, unit ->
                    battle.experience.notifyPermanentProperty(
                        property,
                        unit,
                        battle.configuration.onPermanentProperty,
                    )
                },
            )
        },
        visibleFamousPlayerCount = { BattlePhysicalContextBuilder.visibleFamousPlayerCount(physicalContext(battle)) },
        basePhysicalDamageContext = { attacker, target, splash, rule ->
            BattlePhysicalContextBuilder.basePhysicalDamageContext(
                attacker,
                target,
                splash,
                rule,
                physicalContext(battle)
            )
        },
        physicalDamageRateContext = { attacker, target ->
            BattlePhysicalContextBuilder.physicalDamageRateContext(attacker, target, physicalContext(battle))
        },
        physicalCriticalRateContext = { attacker, target, critical, counter, continuous, splash ->
            BattlePhysicalContextBuilder.physicalCriticalRateContext(
                attacker,
                target,
                critical,
                counter,
                continuous,
                splash,
                physicalContext(battle)
            )
        },
        flatPhysicalDamageContext = { attacker, activeAttack ->
            BattlePhysicalContextBuilder.flatPhysicalDamageContext(attacker, activeAttack, physicalContext(battle))
        },
        castReactionMagic = { caster, target, magicId ->
            battle.combat.castMagic(caster.id, target.id, magicId, reaction = true, bypassCondition = false)
                as? TacticalActionResult.Magic
        },
        consumeXuShiDamage = { attacker ->
            BattlePhysicalContextBuilder.consumeXuShiDamage(
                attacker,
                physicalContext(battle)
            )
        },
        consumeMpAttackSkill = BattlePhysicalContextBuilder::consumeMpAttackSkill,
        mrspDamage = { attacker, target ->
            BattlePhysicalContextBuilder.mrspDamage(
                attacker,
                target
            ) { battle.probabilityResolver.random100() }
        },
    )

    fun physical(battle: Battle): PhysicalCombatEnvironment =
        BattleCombatEnvironmentBuilder.buildPhysicalCombatEnvironment(combatContext(battle))

    fun magic(battle: Battle): MagicEnvironment =
        BattleCombatEnvironmentBuilder.buildMagicEnvironment(combatContext(battle))
}
