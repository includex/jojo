package com.jojo.game.infrastructure.rules

import com.jojo.game.application.battle.BattleScenarioFactory
import com.jojo.game.domain.scenario.ScenarioBattleUnit
import com.jojo.game.domain.scenario.ScenarioUnitFaction
import java.nio.file.Files
import kotlin.test.*

class LuaCombatBindingsTest {
    @Test fun externalLuaIsBoundToBothCharactersAndEquippedItemsAndReloadsNextBattle() {
        val root = Files.createTempDirectory("jojo-lua-battle-")
        val previous = System.getProperty(LuaRulePackage.DIRECTORY_PROPERTY)
        try {
            Files.writeString(root.resolve("lua-rules.yaml"), """
                version: 1
                scripts: {test.combat: combat.lua}
                rules:
                  - {id: test.attacker, event: ATTACK, handler: test.combat, binding: {scope: character, id: 7}, parameters: {owner: 7, power: 10, damage: 5}}
                  - {id: test.weapon, event: ATTACK, handler: test.combat, binding: {scope: equipment, id: 0}, parameters: {owner: 7, power: 4, damage: 2}}
                  - {id: test.defender, event: HIT, handler: test.combat, binding: {scope: character, id: 8}, parameters: {owner: 8, power: -3, damage: -1}}
                  - {id: test.armor, event: HIT, handler: test.combat, binding: {scope: equipment, id: 70}, parameters: {owner: 8, power: -1, damage: -1}}
                  - {id: test.notEquipped, event: ATTACK, handler: test.combat, binding: {scope: equipment, id: 99}, parameters: {owner: 999}}
            """.trimIndent())
            fun source(multiplier: Int) = """return { evaluate=function(ctx,p)
                assert(ctx.actor.characterId==7 and ctx.target.characterId==8)
                assert(ctx.owner.characterId==tonumber(p.owner))
                if ctx.phase=='POWER' then
                    return {{type='AttackPowerModifier',amount=tonumber(p.power)*$multiplier}}
                end
                return {{type='DamageModifier',amount=tonumber(p.damage)*$multiplier}}
            end }"""
            Files.writeString(root.resolve("combat.lua"), source(1))
            System.setProperty(LuaRulePackage.DIRECTORY_PROPERTY, root.toString())
            fun battle() = BattleScenarioFactory.fromScriptedUnits(
                listOf(ScenarioBattleUnit(7, 7, ScenarioUnitFaction.ENEMY, 0, 0),
                    ScenarioBattleUnit(8, 8, ScenarioUnitFaction.ENEMY, 1, 0)),
                enemyEquipment = mapOf(7 to listOf(2, 1, 1, 1, 1), 8 to listOf(1, 1, 2, 1, 1)),
            )
            val first = battle()
            val attacker = first.units.values.first { it.characterId == 7 }
            val defender = first.units.values.first { it.characterId == 8 }
            val originalAttack = attacker.attack
            repeat(3) {
                assertEquals(90, attacker.onRuleAttackPower!!(defender, 80, "physical"))
                assertEquals(45, attacker.onRuleDamage!!(defender, 40, "physical"))
            }
            assertEquals(originalAttack, attacker.attack)
            // A running battle keeps its source snapshot; only the next battle sees edits.
            Files.writeString(root.resolve("combat.lua"), source(2))
            assertEquals(90, attacker.onRuleAttackPower!!(defender, 80, "physical"))
            val second = battle()
            val nextAttacker = second.units.values.first { it.characterId == 7 }
            val nextDefender = second.units.values.first { it.characterId == 8 }
            assertEquals(100, nextAttacker.onRuleAttackPower!!(nextDefender, 80, "physical"))
            assertEquals(50, nextAttacker.onRuleDamage!!(nextDefender, 40, "physical"))
        } finally {
            if (previous == null) System.clearProperty(LuaRulePackage.DIRECTORY_PROPERTY)
            else System.setProperty(LuaRulePackage.DIRECTORY_PROPERTY, previous)
            root.toFile().deleteRecursively()
        }
    }
}
