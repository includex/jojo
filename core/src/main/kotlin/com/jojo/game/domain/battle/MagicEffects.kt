package com.jojo.game.domain.battle

import com.badlogic.gdx.Gdx

/** Semantic IDs for active-magic types, categories, and named spells. */
object MagicEffects {
    private val bindings: Map<String, Int> by lazy {
        val path = "maps/data/magicEffects.json"
        val bytes = MagicEffects::class.java.classLoader.getResourceAsStream(path)?.use { it.readBytes() }
            ?: Gdx.files.internal(path).readBytes()
        SkillEffects.parse(bytes.decodeToString())
    }

    fun id(effect: String): Int = requireNotNull(bindings[effect]) { "Unknown magic effect: $effect" }

    val elementalTypes: Set<Int> by lazy {
        setOf("typeFireDamage", "typeWaterDamage", "typeEarthDamage", "typeWindDamage").mapTo(linkedSetOf(), ::id)
    }

    val hinderingTypes: Set<Int> by lazy {
        setOf(
            "typeSpiritualDamage", "typeHealthAbsorption", "typeMagicAbsorption", "typeAbilityReduction",
            "typeAgitation", "typeSlowTroops", "typeAvoidBattle", "typeAbilityIncrease",
            "typeStrongFormation", "typeTroopTraining", "typeGrandMorale", "typeFalseReport",
            "typePoisonCloud", "typeArmyOrder", "typeSealSpell",
        ).mapTo(linkedSetOf(), ::id)
    }
}
