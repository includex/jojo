package com.jojo.game.domain.campaign

import com.jojo.game.GameDataCatalog
import kotlin.collections.ArrayDeque
import kotlin.random.Random

/** 캠페인 진행에 필요한 가변 상태를 모은 집합체이다. */
class CampaignState(private val randomSource: (Int) -> Int = { upperExclusive -> Random.nextInt(upperExclusive) }) {
    private val injectedInfoTransferRandomValues = ArrayDeque<Int>()

    val extraInfo = mutableListOf<CampaignInfo>()
    val globalVariables = linkedMapOf<Int, Any?>()
    var money: Int = 0
        private set

    fun addMoney(delta: Int) {
        money = (money.toLong() + delta).coerceIn(0L, 9_999_999L).toInt()
    }

    fun setInfoTransferRandomSequence(values: Iterable<Int>) {
        injectedInfoTransferRandomValues.clear()
        values.forEach { injectedInfoTransferRandomValues.addLast(it) }
    }

    private fun random(upperExclusive: Int): Int {
        if (injectedInfoTransferRandomValues.isEmpty()) return randomSource(upperExclusive)
        return injectedInfoTransferRandomValues.removeFirst().also {
            require(it in 0 until upperExclusive) { "infoTransfer random value $it is outside 0..${upperExclusive - 1}" }
        }
    }

    val unitAttributes = linkedMapOf<Int, MutableMap<Int, Int>>()
    val unitNames = linkedMapOf<Int, String>()
    val joinedUnits = linkedSetOf<Int>()
    val extraMagic = linkedMapOf<Pair<Int, Int>, CampaignMagic>()
    val talents = linkedMapOf<Pair<Int, Int>, CampaignTalent>()
    val formationTalents = mutableListOf<String>()
    val inventory = CampaignInventory(joinedUnitIds = { joinedUnits }, unitAttribute = ::unitAttribute)
    val equipmentProgression = CampaignEquipmentProgression(inventory)
    val roster = CampaignRoster { joinedUnits }
    var endingId: Int? = null
        private set

    fun reset() {
        money = 0
        globalVariables.clear()
        extraInfo.clear()
        unitAttributes.clear()
        unitNames.clear()
        joinedUnits.clear()
        extraMagic.clear()
        talents.clear()
        formationTalents.clear()
        inventory.reset()
        roster.reset()
        endingId = null
    }

    fun unitAttribute(unitId: Int, attribute: Int, default: Int = 0): Int =
        unitAttributes[unitId]?.get(attribute) ?: default

    fun setUnitAttribute(unitId: Int, attribute: Int, value: Int) {
        unitAttributes.getOrPut(unitId) { linkedMapOf() }[attribute] = value
    }

    fun setUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 3,
        data: GameDataCatalog,
        registeredFeatures: Int = 0,
    ): CampaignUnitPostsChange? {
        val profile = data.unitProfile(unitId) ?: return null
        val oldPosts = unitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts)
        val postsWritten = flags and 2 == 0 || oldPosts != posts
        if (postsWritten) setUnitAttribute(unitId, UNIT_ATTR_POSTS, posts)
        val mine = unitAttribute(unitId, UNIT_ATTR_JOIN, 0) != 0
        val refreshAbility = flags and 8 == 0 && mine && (
            flags and 4 != 0 ||
                (globalVariables[GLOBAL_SJCS] as? Number)?.toInt() == 1 ||
                registeredFeatures and ENABLED_FEATURE_ZZSJCS != 0
            )
        val derived = if (refreshAbility) {
            val level = unitAttribute(unitId, UNIT_ATTR_LEVEL, profile.level).coerceAtLeast(1)
            data.unitLevelDerivedAttributes(
                unitId,
                unitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts),
                level,
                mine = true,
                campaign = this,
            ).also { values -> values.forEach { (attribute, value) -> setUnitAttribute(unitId, attribute, value) } }
        } else emptyMap()
        return CampaignUnitPostsChange(
            unitId, oldPosts, posts, flags, postsWritten,
            if (postsWritten) listOf("postsSkills", "magic") else emptyList(), derived,
        )
    }

    fun addUnitLevels(
        unitId: Int,
        delta: Int,
        data: GameDataCatalog,
        registeredFeatures: Int = 0,
    ): CampaignUnitLevelChange? {
        val profile = data.unitProfile(unitId) ?: return null
        val oldLevel = unitAttribute(unitId, UNIT_ATTR_LEVEL, profile.level)
        val newLevel = (oldLevel + delta).coerceIn(1, data.unitLevelLimit())
        if (newLevel == oldLevel) return null
        if (unitAttributes[unitId]?.containsKey(UNIT_ATTR_POSTS) != true) setUnitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts)
        setUnitAttribute(unitId, UNIT_ATTR_LEVEL, newLevel)
        val posts = unitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts)
        val mine = unitAttribute(unitId, UNIT_ATTR_JOIN, 0) != 0
        val refreshAll = mine && (
            (globalVariables[GLOBAL_SJCS] as? Number)?.toInt() == 1 ||
                registeredFeatures and ENABLED_FEATURE_ZZSJCS != 0
            )
        val attributes = if (refreshAll) {
            data.unitLevelDerivedAttributes(unitId, posts, newLevel, mine = true, campaign = this)
        } else {
            val growth = data.unitLevelGrowth(unitId, posts, this)
            val defaults = data.unitLevelDerivedAttributes(unitId, posts, oldLevel, mine, this)
            linkedMapOf<Int, Int>().apply {
                growth.forEach { (attribute, perLevel) ->
                    put(attribute, unitAttribute(unitId, attribute, defaults.getValue(attribute)) + perLevel * (newLevel - oldLevel))
                }
            }
        }
        attributes.forEach { (attribute, value) -> setUnitAttribute(unitId, attribute, value) }
        return CampaignUnitLevelChange(unitId, oldLevel, newLevel, attributes)
    }

    fun averageJoinedLevel(): Int {
        if (joinedUnits.isEmpty()) return 1
        val levels = joinedUnits.map { unitAttribute(it, UNIT_ATTR_LEVEL, 1) }.sortedDescending()
        val trim = levels.size / 4
        return levels.subList(trim, levels.size - trim).sum() / (levels.size - trim * 2)
    }

    fun info(type: Int, text: String) {
        val normalized = text.replace("\n", "<br/>")
        val open = extraInfo.filter { it.reserved.isEmpty() }
        if (open.isNotEmpty()) open.forEach { it.text = normalized } else extraInfo += CampaignInfo(type, "", normalized)
    }

    fun promote(unitId: Int, fallbackPosts: Int, fallbackLevel: Int, data: GameDataCatalog): Int? {
        val posts = unitAttribute(unitId, UNIT_ATTR_POSTS, fallbackPosts)
        val level = unitAttribute(unitId, UNIT_ATTR_LEVEL, fallbackLevel).coerceAtLeast(1)
        val upgraded = data.promotionTarget(posts, level) ?: return null
        setUnitAttribute(unitId, UNIT_ATTR_POSTS, upgraded)
        return upgraded
    }

    fun grantExperience(unitId: Int, fallbackLevel: Int, amount: Int, data: GameDataCatalog): CampaignExperienceResult {
        var level = unitAttribute(unitId, UNIT_ATTR_LEVEL, fallbackLevel).coerceAtLeast(1)
        var experience = unitAttribute(unitId, UNIT_ATTR_EXPERIENCE, 0).coerceAtLeast(0)
        val oldLevel = level
        val oldExperience = experience
        var remaining = amount.coerceAtLeast(0)
        var gained = 0
        while (remaining > 0) {
            val limit = data.unitExperienceLimit(level).coerceAtLeast(1)
            val applied = minOf(remaining, (limit - experience).coerceAtLeast(0))
            experience += applied
            gained += applied
            remaining -= applied
            if (experience >= limit && level < data.unitLevelLimit()) {
                level++
                experience = 0
            } else break
        }
        setUnitAttribute(unitId, UNIT_ATTR_LEVEL, level)
        setUnitAttribute(unitId, UNIT_ATTR_EXPERIENCE, experience)
        return CampaignExperienceResult(gained, level, experience, level != oldLevel, oldLevel, oldExperience)
    }

    fun applyInfoTransfer(type: Int, payload: String, selectedUnitId: Int = 0) {
        when (type) {
            0 -> if (selectedUnitId >= 0) unitNames[selectedUnitId] = payload
            18 -> normalizeJoinedUnitLevels()
            4 -> payload.lines().let { values ->
                if (values.size >= 3) {
                    val magicId = values[0].toIntOrNull() ?: return@let
                    val level = values[1].toIntOrNull() ?: return@let
                    val unitId = values[2].toIntOrNull() ?: return@let
                    extraMagic[unitId to magicId] = CampaignMagic(unitId, magicId, level, values.drop(3).firstOrNull()?.ifBlank { DEFAULT_SKILL_INTRO } ?: DEFAULT_SKILL_INTRO)
                }
            }
            5 -> payload.lines().let { values ->
                if (values.size >= 3) {
                    val talentIndex = values[0].toIntOrNull() ?: return@let
                    val slot = values[1].toIntOrNull() ?: return@let
                    val effect = values[2].toIntOrNull() ?: return@let
                    talents[talentIndex to slot] = CampaignTalent(talentIndex, slot, effect, values.drop(3).lastOrNull().orEmpty())
                }
            }
            10 -> formationTalents += payload
            22 -> endingId = payload.toIntOrNull()
            26 -> payload.toIntOrNull()?.takeIf { it > 0 }?.let { globalVariables[4025] = random(it) }
        }
    }

    private fun normalizeJoinedUnitLevels() {
        if (joinedUnits.isEmpty()) return
        val levels = joinedUnits.map { unitAttribute(it, UNIT_ATTR_LEVEL, 1) }.sortedDescending()
        val trim = levels.size / 4
        val middle = levels.subList(trim, levels.size - trim)
        val average = middle.sum() / middle.size
        joinedUnits.forEach { unitId -> if (unitAttribute(unitId, UNIT_ATTR_LEVEL, 1) < average) setUnitAttribute(unitId, UNIT_ATTR_LEVEL, average) }
    }

    private companion object {
        const val UNIT_ATTR_LEVEL = 18
        const val UNIT_ATTR_EXPERIENCE = 19
        const val UNIT_ATTR_POSTS = 17
        const val UNIT_ATTR_JOIN = 16
        const val GLOBAL_SJCS = 4094
        const val ENABLED_FEATURE_ZZSJCS = 4
        const val DEFAULT_SKILL_INTRO = "기본 설명"
    }
}
