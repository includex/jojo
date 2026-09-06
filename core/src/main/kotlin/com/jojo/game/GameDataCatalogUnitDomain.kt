package com.jojo.game

import com.jojo.game.domain.campaign.*

/** 유닛, 병과, 직위, 전투 정보, 캠페인 레벨 조회를 제공한다. */
internal class GameDataCatalogUnitDomain(
    tables: GameDataTableBundle,
    private val combat: GameDataCatalogCombatDomain,
) : GameDataCatalogTableDomain(tables) {
    fun skillsForUnit(characterId: Int, postsId: Int, campaign: CampaignState?): Map<Int, Int> {
        val basePosts = if (postsId >= 60) postsId else postsId - postsId % 3
        val upperPosts = if (postsId >= 60) postsId + 1 else basePosts + 3
        val postContributions = mutableListOf<Pair<Int, Int>>()
        val unitContributions = mutableListOf<Pair<Int, Int>>()
        unitPostSkills.forEachIndexed { index, raw ->
            val post = campaign?.talents?.get(index to 3)?.effect ?: raw.int("2", 255)
            val isPostSkill = post in basePosts until upperPosts
            val isUnitSkill = (0..2).any { slot ->
                (campaign?.talents?.get(index to slot)?.effect ?: raw.int((6 + slot).toString(), 1024)) == characterId
            }
            if (isPostSkill || isUnitSkill) {
                val skill = raw.int("3", 65536 or index) to raw.int("5", 255).coerceIn(0, 255)
                if (isPostSkill) postContributions += skill
                if (isUnitSkill) unitContributions += skill
            }
        }
        return combat.mergeSkillEntries(postContributions + unitContributions)
    }

    fun unitProfile(id: Int): GameDataCatalog.UnitProfile? {
        val value = units.getOrNull(id) ?: return null
        return GameDataCatalog.UnitProfile(
            id, value.string("0") ?: "유닛 $id", value.int("1"), value.int("2"), value.int("14"), value.int("21", -1),
            value.int("19") != 0, value.int("11"), value.int("12", value.int("lv", 1)).coerceAtLeast(1),
            value.int("4"), value.int("5"), value.int("6"), value.int("7"), value.int("8"),
            value.int("9", 100).coerceAtLeast(1), value.int("10").coerceAtLeast(0), criticalSpeechProfile(id, value),
        )
    }

    private fun criticalSpeechProfile(
        unitId: Int,
        unit: com.badlogic.gdx.utils.JsonValue
    ): GameDataCatalog.CriticalSpeechProfile {
        val custom = unit.get("15")?.let(::stringValues).orEmpty().filter(String::isNotEmpty)
        if (custom.isNotEmpty()) return GameDataCatalog.CriticalSpeechProfile(custom, true)
        val ids = config.get("criIds")?.let(::intValues).orEmpty()
        val configured = config.get("criTxt")?.let(::stringValues).orEmpty()
        val named = ids.indexOf(unitId)
        if (named >= 0) return GameDataCatalog.CriticalSpeechProfile(
            listOfNotNull(
                configured.getOrNull(named)?.takeIf(String::isNotEmpty)
            ), false
        )
        val group = configured.drop(unit.int("3") * 3 + 21).take(3).filter(String::isNotEmpty)
        return if (group.isNotEmpty()) GameDataCatalog.CriticalSpeechProfile(group, true)
        else GameDataCatalog.CriticalSpeechProfile(DEFAULT_CRITICAL_SPEECH, true, true)
    }

    fun allUnitNames(): List<String> = units.indices.mapNotNull(::unitProfile).map(GameDataCatalog.UnitProfile::name)
    fun allUnitIds(): List<Int> = units.indices.filter { unitProfile(it) != null }
    fun retreatText(unitId: Int): String? =
        config.get("retreatTxt")?.get(unitId)?.asString()?.takeIf(String::isNotEmpty)

    fun allRetreatTexts(): List<String> =
        generateSequence(config.get("retreatTxt")?.child) { it.next }.map { it.asString() }.toList()

    fun battleName(stageIndex: Int): String = shops.getOrNull(stageIndex)?.get("0")?.asString()?.trim().orEmpty()
    fun allBattleNames(): List<String> =
        shops.mapNotNull { it.get("0")?.asString()?.trim()?.takeIf(String::isNotEmpty) }

    fun postsName(postsId: Int): String = posts.getOrNull(postsId)?.string("0") ?: ""
    fun armProfile(id: Int): GameDataCatalog.ArmProfile? {
        val value = arms.getOrNull(id) ?: return null
        return GameDataCatalog.ArmProfile(
            id,
            value.string("0") ?: "병종 $id",
            value.int("5"),
            value.get("9")?.asBoolean() ?: false,
            value.int("6") != 0,
            value.int("10", 100),
            value.int("12", -1),
            numericChildren(value.get("1"), "arms"),
            numericChildren(value.get("8")?.get("expend")),
            numericChildren(value.get("8")?.get("rise")),
            value.int("3") == 0,
            value.int("2")
        )
    }

    fun terrainLayer(): TerrainLayer {
        val terrain = generateSequence(gameConfig.get("terrain")?.child) { it.next }.mapIndexed { id, value ->
            TerrainLayer.Terrain(
                id,
                value.getString("name", "지형 $id"),
                value.getInt("flag", 0),
                value.getInt("magic", 0)
            )
        }.toList()
        return TerrainLayer(terrain, arms.indices.mapNotNull(::armProfile).map { arm ->
            TerrainLayer.Arm(
                arm.id, arm.name,
                terrain.associate { it.id to (arm.terrainRiseForDisplay(it.id) ?: 100) },
                terrain.mapNotNull { entry -> arm.terrainExpendForDisplay(entry.id)?.let { entry.id to it } }.toMap()
            )
        })
    }

    fun battleProfile(unitId: Int, scriptLevel: Int, postsOverride: Int?): GameDataCatalog.BattleProfile? {
        val unit = unitProfile(unitId) ?: return null
        val level = if (scriptLevel > 0) scriptLevel + 1 else unit.level
        val finalPosts = postsOverride ?: turnPosts(unit.posts, level, 2)
        val post = posts.getOrNull(finalPosts)
        fun bonus(attribute: String) = post?.get(attribute)?.asInt() ?: 0
        fun ability(raw: Int, attribute: String) = raw + (abilityPhase(raw) + bonus(attribute)).floorDiv(2) * level
        val arm = armProfile(if (finalPosts < 60) finalPosts / 3 else finalPosts - 40) ?: return null
        return GameDataCatalog.BattleProfile(
            unit,
            level,
            finalPosts,
            bonus("1"),
            ability(unit.attack, "3"),
            ability(unit.defense, "4"),
            ability(unit.spirit, "5"),
            ability(unit.critical, "6"),
            ability(unit.morale, "7"),
            (unit.maxHitPoints + bonus("8") * level).coerceAtLeast(1),
            (unit.maxMagicPoints + bonus("9") * level).coerceAtLeast(0),
            arm,
            combat.hitAreaProfile(bonus("2")) ?: return null,
            combat.allMagicProfiles()
                .filter { magic -> combat.magicLearnLevel(magic.id, finalPosts)?.let { level >= it } == true })
    }

    fun unitExperienceLimit(level: Int): Int = (config.get("unit")?.getInt("expLimit", 100) ?: 100) +
            (config.get("transfer")
                ?.let { generateSequence(it.child) { node -> node.next }.map { node -> node.asInt() }.toList() }
                .orEmpty().take(2).count { level >= it } * 25)

    fun unitLevelLimit(): Int = config.get("unit")?.getInt("lvLimit", 50) ?: 50
    fun unitLevelGrowth(unitId: Int, postsId: Int, campaign: CampaignState?): LinkedHashMap<Int, Int> {
        val profile = unitProfile(unitId) ?: return linkedMapOf()
        val post = posts.getOrNull(postsId)
        val result = linkedMapOf<Int, Int>()
        listOf(
            profile.attack,
            profile.defense,
            profile.spirit,
            profile.critical,
            profile.morale
        ).forEachIndexed { index, fallback ->
            val aptitude = campaign?.unitAttribute(unitId, 9 + index, fallback) ?: fallback
            result[2 + index] = (abilityPhase(aptitude) + (post?.get((3 + index).toString())?.asInt() ?: 3)).floorDiv(2)
        }
        result[7] = post?.get("8")?.asInt() ?: 0; result[8] = post?.get("9")?.asInt() ?: 0; return result
    }

    fun unitLevelDerivedAttributes(
        unitId: Int,
        postsId: Int,
        level: Int,
        mine: Boolean,
        campaign: CampaignState?
    ): LinkedHashMap<Int, Int> {
        val profile = unitProfile(unitId) ?: return linkedMapOf()
        val growth = unitLevelGrowth(unitId, postsId, campaign)
        val result = linkedMapOf<Int, Int>()
        listOf(
            profile.attack,
            profile.defense,
            profile.spirit,
            profile.critical,
            profile.morale
        ).forEachIndexed { index, base ->
            val add = if (mine) campaign?.unitAttribute(unitId, 39 + index, 0) ?: 0 else 0; result[2 + index] =
            base + growth.getValue(2 + index) * level + add.coerceAtLeast(0)
        }
        listOf(profile.maxHitPoints, profile.maxMagicPoints).forEachIndexed { index, base ->
            val add = if (mine) campaign?.unitAttribute(unitId, 44 + index, 0) ?: 0 else 0; result[7 + index] =
            base + (growth.getValue(7 + index) * level + add).coerceAtLeast(0)
        }; return result
    }

    fun promotionTarget(postsId: Int, level: Int): Int? {
        if (postsId !in 0 until 60 || postsId % 3 !in 0..1) return null
        val rank = postsId % 3
        val threshold = indexed(config.get("transfer"), rank)?.asInt() ?: if (rank == 0) 15 else 30
        return (postsId + 1).takeIf { level >= threshold }
    }

    fun configTopLevelKeys(): String =
        generateSequence(config.child) { it.next }.take(20).joinToString { "${it.name ?: "#"}:${it.type()}" }

    private fun abilityPhase(raw: Int): Int = 5 - listOf(127, 45, 35, 25).count { raw < it }
    private fun turnPosts(posts: Int, level: Int, armLimit: Int): Int {
        if (posts >= 60) return posts
        val eligible = if (level >= 30) 2 else if (level >= 15) 1 else 0; return posts - posts % 3 + maxOf(
            posts % 3,
            minOf(eligible, armLimit)
        )
    }

    private companion object {
        val DEFAULT_CRITICAL_SPEECH = listOf(
            "음... 정말 한 방에 쓰러뜨릴 거야!",
            "길 막지 마! 길 막지 마!",
            "가르침을 내리노라!",
            "무명 병사! 빨리 물러서라!",
            "가로막는 자는 죽는다! 비켜라, 비켜라……!",
            "야헤야헤야……!",
            "오호호...!",
            "하아……!",
            "아악……!",
            "크윽……!",
            "음...!",
            "죽여라아...!",
            "기술을 보여주마...!",
            "나의 이 기술을 받아라!!",
            "죽여라...!",
            "죽어라!!!",
            "호호……!",
            "야호……!",
            "응응응...!",
            "으윽...!",
            "후우후……!",
            "응응!?",
            "흥!!",
            "응응응!",
            "아이쿠!!",
            "나를 봐라!",
            "모든 것이 이 한 번의 공격에 달렸어!",
            "반드시 당신과 우열을 가려야 해!\n절대로 질 수 없어!",
            "이 치명타를 받아라!",
            "받아치기 준비해라!!",
            "죽을 준비를 해라!",
            "나 왔다, 나 왔다, 나 왔다!!"
        )
    }
}
