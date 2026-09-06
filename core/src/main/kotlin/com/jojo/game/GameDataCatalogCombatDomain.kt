package com.jojo.game

import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus

/** 전술 마법, 지형, 상태, 효과 범위 데이터를 해석한다. */
internal class GameDataCatalogCombatDomain(tables: GameDataTableBundle) : GameDataCatalogTableDomain(tables) {
    fun magicProfile(id: Int): GameDataCatalog.MagicProfile? {
        val value = magics.getOrNull(id) ?: return null
        val effectAreaId = value.int("4")
        return GameDataCatalog.MagicProfile(
            id, value.string("0") ?: "전략 $id", value.int("1"), value.int("2"),
            hitAreaProfile(value.int("3")) ?: return null, effectAreaId, effectAreaOffsets(effectAreaId),
            value.int("5"), value.int("9", 100), value.int("10"), value.int("14"),
            effectId = value.int("8", 255), condition = value.int("11", -1), aiUse = value.int("12"),
            hitRateLimit = value.int("13"), icon = value.int("6"), intro = value.string("7").orEmpty(),
        )
    }

    fun allMagicProfiles(): List<GameDataCatalog.MagicProfile> = magics.indices.mapNotNull(::magicProfile)
    fun magicLearnLevel(magicId: Int, postsId: Int): Int? =
        magics.getOrNull(magicId)?.get("15")?.get(postsId.toString())?.asInt()

    fun learnedMagicIds(postsId: Int, level: Int): List<Int> = magics.indices.filter { id ->
        magicLearnLevel(id, postsId)?.let { level >= it } == true
    }

    fun effectAreaOffsets(id: Int): Set<Pair<Int, Int>> =
        generateSequence(effectAreas.getOrNull(id)?.get("ps")?.child) { it.next }
            .mapNotNull { point -> point.child?.asInt()?.let { x -> point.child?.next?.asInt()?.let { y -> x to y } } }
            .toSet()

    fun upgradedEffectArea(id: Int): Pair<Int, Set<Pair<Int, Int>>> {
        val upgradedId = effectAreas.getOrNull(id)?.getInt("upgrade", id) ?: id
        return upgradedId to effectAreaOffsets(upgradedId)
    }

    fun hitAreaProfile(id: Int): GameDataCatalog.HitAreaProfile? {
        val value = hitAreas.getOrNull(id) ?: return null
        val offsets = generateSequence(value.get("ps")?.child) { it.next }
            .mapNotNull { point -> point.child?.asInt()?.let { x -> point.child?.next?.asInt()?.let { y -> x to y } } }
            .toSet()
        return GameDataCatalog.HitAreaProfile(id, offsets, value.get("flag")?.asInt() == 2, value.getInt("upgrade", id))
    }

    fun terrainMagicFlag(terrainId: Int): Int = terrainValue(terrainId, "flag")
    fun terrainResumeHp(terrainId: Int): Int = terrainValue(terrainId, "resumeHP")
    fun terrainResumeMp(terrainId: Int): Int = terrainValue(terrainId, "resumeMP")
    private fun terrainValue(terrainId: Int, field: String): Int =
        indexed(gameConfig.get("terrain"), terrainId)?.getInt(field, 0) ?: 0

    fun statusRound(status: BattleStatus, fallback: Int = 3): Int {
        val index = when (status) {
            BattleStatus.PARALYSIS -> 7; BattleStatus.SILENCE -> 8; BattleStatus.CONFUSION -> 9
            BattleStatus.POISON -> 10; BattleStatus.LOST -> 13
        }
        return indexed(gameConfig.get("status"), index)?.getInt("round", fallback) ?: fallback
    }

    fun attributeStatusRound(attribute: BattleAttribute, fallback: Int = 3): Int =
        indexed(gameConfig.get("status"), attribute.ordinal)?.getInt("round", fallback) ?: fallback

    fun statusMeff(sourceStatusIndex: Int, meffSlot: Int): Int? = indexed(gameConfig.get("status"), sourceStatusIndex)
        ?.get("meff${meffSlot.coerceIn(0, 2)}")?.takeUnless { it.isNull }?.asInt()

    fun namedMeff(name: String): Int? = gameConfig.get("meff")?.get(name)?.asInt()
    fun skillName(skillId: Int): String = defineSkills.getOrNull(skillId)?.getString("name", "") ?: ""
    fun skillIncrementType(skillId: Int): Int = defineSkills.getOrNull(skillId)?.getInt("incType", 0) ?: 0
    fun skillArgument(skillId: Int): Int = defineSkills.getOrNull(skillId)?.getInt("arg", 0) ?: 0
    fun passiveAbility(base: Int, skillId: Int, skills: Map<Int, Int>): Int {
        val value = skills[skillId]?.and(255) ?: return base
        if (value == 255) return base
        val bonus = if (skillArgument(skillId) != 0) base * value / 100 else value
        return (base + bonus).coerceAtLeast(0)
    }

    fun mergeSkills(vararg layers: Map<Int, Int>): Map<Int, Int> =
        mergeSkillEntries(layers.flatMap { it.entries.map { entry -> entry.key to entry.value } })

    fun mergeSkillEntries(entries: Iterable<Pair<Int, Int>>): Map<Int, Int> = buildMap {
        entries.forEach { (skillId, rawValue) ->
            val value = rawValue and 255
            val prior = get(skillId)
            put(
                skillId, when (skillIncrementType(skillId)) {
                    1 -> if (prior == null) value else (prior + value) and 254
                    2 -> if (prior == null) value else prior or value
                    else -> value
                }
            )
        }
    }
}
