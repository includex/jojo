package com.jojo.port

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/**
 * Read-only view of the original game's encrypted Global tables.  Numeric
 * property names deliberately mirror the original UNIT_ATTR_NAME enum.
 */
class OriginalGameData private constructor(
    private val units: List<JsonValue>,
    private val arms: List<JsonValue>,
    private val posts: List<JsonValue>,
    private val hitareas: List<JsonValue>,
    private val effareas: List<JsonValue>,
    private val magics: List<JsonValue>,
    private val items: List<JsonValue>,
    private val itemSkills: JsonValue,
    private val unitPostSkills: List<JsonValue>,
    private val defineSkills: List<JsonValue>,
    private val shops: List<JsonValue>,
    private val config: JsonValue,
    private val gameConfig: JsonValue,
) {
    data class UnitProfile(
        val id: Int,
        val name: String,
        /** UNIT_ATTR_NAME2.FACE; DialogueLayer converts this to a Head asset. */
        val face: Int,
        /** UNIT_ATTR_NAME.RAVATAR, used by HallUnit/Pmapobj2. */
        val mapAvatar: Int,
        /** UNIT_ATTR_NAME.SAVATAR, used by BattleUnit/Model.fAvatarGroup. */
        val battleAvatar: Int,
        /** UNIT_ATTR_NAME.SAVATAR_TYPE; gates battle-avatar compatibility. */
        val battleAvatarType: Int,
        /** UNIT_ATTR_NAME.FAMOUS.  BattleUnit uses this for its enemy HP bar. */
        val famous: Boolean,
        val posts: Int,
        val level: Int,
        val attack: Int,
        val defense: Int,
        val spirit: Int,
        val critical: Int,
        val morale: Int,
        val maxHitPoints: Int,
        val maxMagicPoints: Int,
        /** Unit.getCritTxt(): exact source text pool and RNG stream selection. */
        val criticalSpeech: CriticalSpeechProfile,
    ) {
        /** Model.postsToArm() from the original client. */
        val armId: Int get() = if (posts < 60) posts / 3 else posts - 40
    }

    data class CriticalSpeechProfile(
        val texts: List<String>,
        /** Named criIds entries contain one fixed line and consume no Tool.random call. */
        val randomized: Boolean,
        /** Only the source's final hard-coded fallback uses Tool.random flag=1. */
        val flagRandom: Boolean = false,
    )

    data class ArmProfile(
        val id: Int,
        val name: String,
        /** ARM_ATTR_NAME.TYPE: 0 all-rounder, 1 civil, 2 martial. */
        val type: Int,
        val remote: Boolean,
        /** ARM_ATTR_NAME.ATTACKDELAY (arms[6]). */
        val attackDelay: Boolean,
        val magicHarmRate: Int,
        /** ARM_ATTR_NAME.SAVATAR_TYPE. */
        val battleAvatarType: Int,
        private val restraints: Map<Int, Int>,
        private val terrainExpend: Map<Int, Int>,
        private val terrainRise: Map<Int, Int>,
        /** ARM_ATTR_NAME.MOVESPEED. Zero selects BattleUnit.move2's .08s step. */
        val fastMove: Boolean = true,
        /** ARM_ATTR_NAME.MOVESOUND, used by defender KZQB's horse-only reduction. */
        val moveSound: Int = 0,
    ) {
        /** Original Model.armRestraintAttr(): unspecified pairs are exactly 100%. */
        fun restraintAgainst(defenderArmId: Int): Int = restraints[defenderArmId] ?: 100
        /** Original BattleUnit.terrainImpact() baseline before skills. */
        fun terrainImpact(terrainId: Int): Int = terrainRise[terrainId] ?: 100
        /** BattleUnit.getArmTerrain(terrain, 1): absent terrain is impassable (255). */
        fun terrainMoveCost(terrainId: Int): Int = terrainExpend[terrainId] ?: 255
        /** TerrainLayer._initPanel0 distinguishes an absent rise entry from its 100% default. */
        fun terrainRiseForDisplay(terrainId: Int): Int? = terrainRise[terrainId]
        /** TerrainLayer._initPanel1 renders an absent/over-200 expenditure as `--`. */
        fun terrainExpendForDisplay(terrainId: Int): Int? = terrainExpend[terrainId]
    }

    data class BattleProfile(
        val unit: UnitProfile,
        val level: Int,
        val posts: Int,
        val movement: Int,
        val attack: Int,
        val defense: Int,
        val spirit: Int,
        val critical: Int,
        val morale: Int,
        val maxHitPoints: Int,
        val maxMagicPoints: Int,
        val arm: ArmProfile,
        val hitArea: HitAreaProfile,
        val magic: List<MagicProfile>,
    )

    data class HitAreaProfile(
        val id: Int,
        val offsets: Set<Pair<Int, Int>>,
        val allScreen: Boolean = false,
        /** Model.hitareaUpgrade(id), or this ID when the source has no upgrade. */
        val upgradeId: Int = id,
    )

    /** Original MAGIC_ATTR_NAME2 fields needed by the tactical resolver. */
    data class MagicProfile(
        val id: Int,
        val name: String,
        val type: Int,
        val target: Int,
        val hitArea: HitAreaProfile,
        val effectAreaId: Int,
        val effectOffsets: Set<Pair<Int, Int>>,
        val expendMp: Int,
        val power: Int,
        val harmType: Int,
        val category: Int,
        /** MAGIC_ATTR_NAME2.MEFF: source target-effect index, 255 for none. */
        val effectId: Int = 255,
        /** Original MAGIC_ATTR_NAME.CONDITION (magicConditionTest). */
        val condition: Int = -1,
        /** MAGIC_ATTR_NAME.AIUSE; 13 bypasses magicConditionTest in Control._AIProcess. */
        val aiUse: Int = 0,
        /** Original MAGIC_ATTR_NAME.HITRATELIMIT. */
        val hitRateLimit: Int = 0,
        /** MAGIC_ATTR_NAME.ICON (raw field 6), used by Global108 MagicLayer. */
        val icon: Int = 0,
        /** MAGIC_ATTR_NAME.INTRO (raw field 7), used by Global108 MagicLayer. */
        val intro: String = "",
    )

    /** Original ITEM_ATTR_NAME values used by battle-script equipment. */
    data class EquipmentProfile(
        val id: Int,
        val name: String,
        val itemType: Int,
        /** ITEM_ATTR_NAME.PRICE (raw item-table field 2). */
        val price: Int,
        val specialType: Int,
        val value: Int,
        val effectValue: Int,
        val upgradePerLevel: Int,
        /** Item.icon(): ITEM_ATTR_NAME2.ICON + 1; source path is Item/<icon>-1. */
        val icon: Int,
        /** ITEM_ATTR_NAME.TREASURE (raw item-table field 9). */
        val treasure: Boolean,
        /** ITEM_ATTR_NAME.INTRO (raw item-table field 10). */
        val intro: String = "",
    )

    data class EquipmentBonus(val attack: Int = 0, val defense: Int = 0, val spirit: Int = 0)
    /** Decoded UNIT_POSTS_SKILL entry after original Tianfu overrides. */
    data class SkillProfile(val index: Int, val skillId: Int, val effect: Int, val name: String)

    /**
     * Unit._postsSkills + Unit._unitSkills.  The source uses a three-rank
     * posts window (base posts through its two promotions) and then overlays
     * Model.infoTransfer(5) Tianfu values.
     */
    fun skillsForUnit(characterId: Int, postsId: Int, campaign: CampaignState? = null): Map<Int, Int> {
        val basePosts = if (postsId >= 60) postsId else postsId - postsId % 3
        val upperPosts = if (postsId >= 60) postsId + 1 else basePosts + 3
        val postContributions = mutableListOf<Pair<Int, Int>>()
        val unitContributions = mutableListOf<Pair<Int, Int>>()
        unitPostSkills.forEachIndexed { index, raw ->
            val postOverride = campaign?.talents?.get(index to 3)?.effect
            val post = postOverride ?: raw.int("2", 255)
            val isPostSkill = post in basePosts until upperPosts
            val isUnitSkill = (0..2).any { slot ->
                (campaign?.talents?.get(index to slot)?.effect ?: raw.int((6 + slot).toString(), 1024)) == characterId
            }
            if (!isPostSkill && !isUnitSkill) return@forEachIndexed
            val skillId = raw.int("3", 65536 or index)
            val effect = raw.int("5", 255).coerceIn(0, 255)
            if (isPostSkill) postContributions += skillId to effect
            if (isUnitSkill) unitContributions += skillId to effect
        }
        // Unit.skills() inserts post skills first, then individual skills.
        return mergeSkillEntries(postContributions + unitContributions)
    }

    fun unitProfile(id: Int): UnitProfile? {
        val value = units.getOrNull(id) ?: return null
        // Unit._refAbilityPhase2: the five raw values are the level-1 base
        // abilities before rank/equipment/save-game modifiers are applied.
        return UnitProfile(
            id = id,
            name = value.string("0") ?: "유닛 $id",
            face = value.int("1"),
            mapAvatar = value.int("2"),
            battleAvatar = value.int("14"),
            battleAvatarType = value.int("21", -1),
            famous = value.int("19") != 0,
            posts = value.int("11"),
            level = value.int("12", value.int("lv", 1)).coerceAtLeast(1),
            attack = value.int("4"),
            defense = value.int("5"),
            spirit = value.int("6"),
            critical = value.int("7"),
            morale = value.int("8"),
            maxHitPoints = value.int("9", 100).coerceAtLeast(1),
            maxMagicPoints = value.int("10", 0).coerceAtLeast(0),
            criticalSpeech = criticalSpeechProfile(id, value),
        )
    }

    /** Exact Unit.getCritTxt lookup before BattleUnit.checkCrit's alternating gate. */
    private fun criticalSpeechProfile(unitId: Int, unit: JsonValue): CriticalSpeechProfile {
        val custom = unit.get("15")?.let { stringValues(it) }.orEmpty().filter(String::isNotEmpty)
        if (custom.isNotEmpty()) return CriticalSpeechProfile(custom, randomized = true)
        val ids = config.get("criIds")?.let { intValues(it) }.orEmpty()
        val configured = config.get("criTxt")?.let { stringValues(it) }.orEmpty()
        val namedIndex = ids.indexOf(unitId)
        if (namedIndex >= 0) {
            return CriticalSpeechProfile(listOfNotNull(configured.getOrNull(namedIndex)?.takeIf(String::isNotEmpty)), randomized = false)
        }
        val groupStart = unit.int("3", 0) * 3 + 21
        val group = configured.drop(groupStart).take(3).filter(String::isNotEmpty)
        if (group.isNotEmpty()) return CriticalSpeechProfile(group, randomized = true)
        return CriticalSpeechProfile(DEFAULT_CRITICAL_SPEECH, randomized = true, flagRandom = true)
    }

    /** Names rendered by Battle SayLayer, including generic troop labels. */
    fun allUnitNames(): List<String> = units.indices.mapNotNull(::unitProfile).map(UnitProfile::name)

    /** Every populated original UNIT table row, for renderer conformance scans. */
    fun allUnitIds(): List<Int> = units.indices.filter { unitProfile(it) != null }

    /** Model.cfgRetreatTxt(unitId), indexed directly by the source character id. */
    fun retreatText(unitId: Int): String? = config.get("retreatTxt")
        ?.get(unitId)
        ?.asString()
        ?.takeIf(String::isNotEmpty)

    fun allRetreatTexts(): List<String> = config.get("retreatTxt")
        ?.let { values -> generateSequence(values.child) { it.next }.map { it.asString() }.toList() }
        .orEmpty()

    /** Original Model.battleName(): SHOP[temporaryStageIndex].NAME.trim(). */
    fun battleName(stageIndex: Int): String = shops.getOrNull(stageIndex)
        ?.get("0")?.asString()?.trim()
        .orEmpty()

    fun allBattleNames(): List<String> = shops.mapNotNull { it.get("0")?.asString()?.trim()?.takeIf(String::isNotEmpty) }

    /** Exact data feed for the original TerrainLayer's two 28×13 panels. */
    fun terrainLayer(): TerrainLayer {
        val terrain = generateSequence(gameConfig.get("terrain")?.child) { it.next }.mapIndexed { id, value ->
            TerrainLayer.Terrain(
                id = id,
                name = value.getString("name", "지형 $id"),
                flag = value.getInt("flag", 0),
                magic = value.getInt("magic", 0),
            )
        }.toList()
        val arms = arms.indices.mapNotNull(::armProfile).map { arm ->
            TerrainLayer.Arm(
                id = arm.id,
                name = arm.name,
                terrainRise = terrain.associate { it.id to (arm.terrainRiseForDisplay(it.id) ?: 100) },
                // Retain only defined entries: source missing expenditure is 0, not 255.
                terrainExpend = terrain.mapNotNull { entry ->
                    arm.terrainExpendForDisplay(entry.id)?.let { entry.id to it }
                }.toMap(),
            )
        }
        return TerrainLayer(terrain, arms)
    }

    fun armProfile(id: Int): ArmProfile? {
        val value = arms.getOrNull(id) ?: return null
        val restraint = numericChildren(value.get("1"), "arms")
        val terrainExpend = numericChildren(value.get("8")?.get("expend"))
        val terrainRise = numericChildren(value.get("8")?.get("rise"))
        return ArmProfile(
            id = id,
            name = value.string("0") ?: "병종 $id",
            type = value.int("5"),
            remote = value.get("9")?.asBoolean() ?: false,
            fastMove = value.int("3") == 0,
            attackDelay = value.int("6") != 0,
            magicHarmRate = value.int("10", 100),
            battleAvatarType = value.int("12", -1),
            restraints = restraint,
            terrainExpend = terrainExpend,
            terrainRise = terrainRise,
            moveSound = value.int("2"),
        )
    }

    /** Mirrors Model.createUnitById + Unit.refAbilityPhase for a fresh battle unit. */
    fun battleProfile(unitId: Int, scriptLevel: Int, postsOverride: Int? = null): BattleProfile? {
        val unit = unitProfile(unitId) ?: return null
        // Source level values are zero-based: createUnitById(r) starts at r+1.
        // A zero source level uses the campaign average; a new campaign starts at 1.
        val level = if (scriptLevel > 0) scriptLevel + 1 else unit.level
        val finalPosts = postsOverride ?: turnPosts(unit.posts, level, armLimit = 2)
        val post = posts.getOrNull(finalPosts)
        fun bonus(attribute: String) = post?.get(attribute)?.asInt() ?: 0
        fun abilityPhase(raw: Int): Int {
            var phase = 5
            // Model.initPropertyProxy replaces Config.ability with this exact
            // table before Unit.abilityPhase reads it.  In particular, the
            // leading 127 keeps ordinary values in phase 4 or below.
            val thresholds = listOf(127, 45, 35, 25)
            for (threshold in thresholds) {
                if (raw >= threshold) break
                phase--
            }
            return phase
        }
        fun baseAbility(raw: Int, postAttribute: String): Int =
            raw + (abilityPhase(raw) + bonus(postAttribute)).floorDiv(2) * level
        val arm = armProfile(if (finalPosts < 60) finalPosts / 3 else finalPosts - 40) ?: return null
        val hitArea = hitAreaProfile(bonus("2")) ?: return null
        val learnedMagic = magics.mapIndexedNotNull { id, _ -> magicProfile(id) }
            .filter { magic -> magicLearnLevel(magic.id, finalPosts)?.let { level >= it } == true }
        return BattleProfile(
            unit = unit,
            level = level,
            posts = finalPosts,
            movement = bonus("1"),
            attack = baseAbility(unit.attack, "3"),
            defense = baseAbility(unit.defense, "4"),
            spirit = baseAbility(unit.spirit, "5"),
            critical = baseAbility(unit.critical, "6"),
            morale = baseAbility(unit.morale, "7"),
            maxHitPoints = (unit.maxHitPoints + bonus("8") * level).coerceAtLeast(1),
            maxMagicPoints = (unit.maxMagicPoints + bonus("9") * level).coerceAtLeast(0),
            arm = arm,
            hitArea = hitArea,
            magic = learnedMagic,
        )
    }

    fun magicProfile(id: Int): MagicProfile? {
        val value = magics.getOrNull(id) ?: return null
        val effectAreaId = value.int("4")
        return MagicProfile(
            id = id,
            name = value.string("0") ?: "전략 $id",
            type = value.int("1"),
            target = value.int("2"),
            hitArea = hitAreaProfile(value.int("3")) ?: return null,
            effectAreaId = effectAreaId,
            effectOffsets = effectAreaOffsets(effectAreaId),
            expendMp = value.int("5"),
            power = value.int("9", 100),
            harmType = value.int("10"),
            category = value.int("14"),
            // Raw field 7 is the Korean intro string; the packed data keeps
            // that legacy text slot between ICON(6) and MEFF(8).
            effectId = value.int("8", 255),
            condition = value.int("11", -1),
            aiUse = value.int("12"),
            hitRateLimit = value.int("13", 0),
            icon = value.int("6"),
            intro = value.string("7").orEmpty(),
        )
    }

    fun allMagicProfiles(): List<MagicProfile> = magics.indices.mapNotNull(::magicProfile)

    /** Original GAME_CFG.terrain[n].flag, used by BattleUnit.getMagicTerrainRate(). */
    fun terrainMagicFlag(terrainId: Int): Int = gameConfig.get("terrain")
        ?.let { terrains -> generateSequence(terrains.child) { it.next }.elementAtOrNull(terrainId) }
        ?.getInt("flag", 0)
        ?: 0

    /** Original GAME_CFG.terrain[n].resumeHP, used by Control._cxpl. */
    fun terrainResumeHp(terrainId: Int): Int = gameConfig.get("terrain")
        ?.let { terrains -> generateSequence(terrains.child) { it.next }.elementAtOrNull(terrainId) }
        ?.getInt("resumeHP", 0)
        ?: 0

    /** Original GAME_CFG.terrain[n].resumeMP, applied by BattleLayer._stateProcess. */
    fun terrainResumeMp(terrainId: Int): Int = gameConfig.get("terrain")
        ?.let { terrains -> generateSequence(terrains.child) { it.next }.elementAtOrNull(terrainId) }
        ?.getInt("resumeMP", 0)
        ?: 0

    /** Model.stateExInfoByIdx(status, STATUS_ATTR_NAME.ROUND, fallback=3). */
    fun statusRound(status: BattleStatus, fallback: Int = 3): Int {
        val sourceIndex = when (status) {
            BattleStatus.PARALYSIS -> 7 // MB
            BattleStatus.SILENCE -> 8   // JZ
            BattleStatus.CONFUSION -> 9 // HL
            BattleStatus.POISON -> 10   // ZD
            BattleStatus.LOST -> 13     // MS
        }
        return gameConfig.get("status")
            ?.let { entries -> generateSequence(entries.child) { it.next }.elementAtOrNull(sourceIndex) }
            ?.getInt("round", fallback)
            ?: fallback
    }

    /** Model.stateExInfoByIdx for packed ATT..MOV status slots 0..5. */
    fun attributeStatusRound(attribute: BattleAttribute, fallback: Int = 3): Int = gameConfig.get("status")
        ?.let { entries -> generateSequence(entries.child) { it.next }.elementAtOrNull(attribute.ordinal) }
        ?.getInt("round", fallback)
        ?: fallback

    /** Model.stateExInfoByIdx(status, `meff{lift}`), null when unauthored. */
    fun statusMeff(sourceStatusIndex: Int, meffSlot: Int): Int? = gameConfig.get("status")
        ?.let { entries -> generateSequence(entries.child) { it.next }.elementAtOrNull(sourceStatusIndex) }
        ?.get("meff${meffSlot.coerceIn(0, 2)}")
        ?.takeUnless { it.isNull }
        ?.asInt()

    fun namedMeff(name: String): Int? = gameConfig.get("meff")?.get(name)?.asInt()

    fun skillName(skillId: Int): String = defineSkills.getOrNull(skillId)?.getString("name", "") ?: ""

    fun configTopLevelKeys(): String = generateSequence(config.child) { it.next }
        .take(20)
        .joinToString { "${it.name ?: "#"}:${it.type()}" }

    /** Unit.expLimit(): base limit plus the original transfer thresholds. */
    fun unitExperienceLimit(level: Int): Int {
        val base = config.get("unit")?.getInt("expLimit", 100) ?: 100
        val thresholds = config.get("transfer")?.let { values -> generateSequence(values.child) { it.next }.map { it.asInt() }.toList() }.orEmpty()
        return base + thresholds.take(2).count { level >= it } * 25
    }
    fun unitLevelLimit(): Int = config.get("unit")?.getInt("lvLimit", 50) ?: 50

    /**
     * Per-level values used by Unit.setLevel's incremental `_addAbility` path.
     * Keys are UNIT_ATTR_NAME2 ATT..MP (2..8), in the source mutation order.
     */
    fun unitLevelGrowth(unitId: Int, postsId: Int, campaign: CampaignState? = null): LinkedHashMap<Int, Int> {
        val profile = unitProfile(unitId) ?: return linkedMapOf()
        val post = posts.getOrNull(postsId)
        val raw = listOf(profile.attack, profile.defense, profile.spirit, profile.critical, profile.morale)
        val result = linkedMapOf<Int, Int>()
        raw.forEachIndexed { index, fallback ->
            val aptitude = campaign?.unitAttribute(unitId, 9 + index, fallback) ?: fallback
            var phase = 5
            listOf(127, 45, 35, 25).forEach { threshold -> if (aptitude < threshold) phase-- }
            // UNIT_ATTR ATT..MOR (2..6) map to posts growth fields 3..7.
            // Field 2 is the hit-area id, not attack growth.
            val postPhase = post?.get((3 + index).toString())?.asInt() ?: 3
            result[2 + index] = (phase + postPhase).floorDiv(2)
        }
        result[7] = post?.get("8")?.asInt() ?: 0
        result[8] = post?.get("9")?.asInt() ?: 0
        return result
    }

    /** Unit.refAbilityPhase after LV is written, including Mine-only ADD_* values. */
    fun unitLevelDerivedAttributes(
        unitId: Int,
        postsId: Int,
        level: Int,
        mine: Boolean,
        campaign: CampaignState? = null,
    ): LinkedHashMap<Int, Int> {
        val profile = unitProfile(unitId) ?: return linkedMapOf()
        val bases = listOf(profile.attack, profile.defense, profile.spirit, profile.critical, profile.morale)
        val growth = unitLevelGrowth(unitId, postsId, campaign)
        val result = linkedMapOf<Int, Int>()
        bases.forEachIndexed { index, base ->
            val add = if (mine) campaign?.unitAttribute(unitId, 39 + index, 0) ?: 0 else 0
            result[2 + index] = base + growth.getValue(2 + index) * level + add.coerceAtLeast(0)
        }
        listOf(profile.maxHitPoints, profile.maxMagicPoints).forEachIndexed { index, base ->
            val add = if (mine) campaign?.unitAttribute(unitId, 44 + index, 0) ?: 0 else 0
            result[7 + index] = base + (growth.getValue(7 + index) * level + add).coerceAtLeast(0)
        }
        return result
    }

    /** Unit.canUpgreadArm: only the first two ranks of a normal post family promote. */
    fun promotionTarget(postsId: Int, level: Int): Int? {
        if (postsId >= 60 || postsId < 0) return null
        val rank = postsId % 3
        if (rank !in 0..1) return null
        val threshold = config.get("transfer")?.let { values ->
            generateSequence(values.child) { it.next }.elementAtOrNull(rank)?.asInt()
        } ?: if (rank == 0) 15 else 30
        return if (level >= threshold) postsId + 1 else null
    }

    /** Item.expLimit/maxLV: common and special equipment use separate config rows. */
    fun equipmentExperienceLimit(itemId: Int, level: Int): Int {
        val section = if ((equipmentProfile(itemId)?.itemType ?: 0) % 2 == 0) "comEquip" else "speEquip"
        val values = config.get(section)
        val base = values?.getInt("expLimit", 200) ?: 200
        val upgradeAt = values?.getInt("upgrade", 6) ?: 6
        return base + if (level >= upgradeAt) 50 else 0
    }

    fun equipmentLevelLimit(itemId: Int): Int {
        val section = if ((equipmentProfile(itemId)?.itemType ?: 0) % 2 == 0) "comEquip" else "speEquip"
        return config.get(section)?.getInt("lvLimit", 9) ?: 9
    }

    fun equipmentProfile(id: Int): EquipmentProfile? {
        val value = items.getOrNull(id) ?: return null
        return EquipmentProfile(
            id = id,
            name = value.string("0") ?: "장비 $id",
            itemType = value.int("1", 255),
            price = value.int("3", 255),
            specialType = value.int("2", 255),
            value = value.int("5"),
            effectValue = value.int("6"),
            upgradePerLevel = value.int("8"),
            icon = value.int("4") + 1,
            // Model._initItemAttr maps ITEM_ATTR_NAME.TREASURE (enum 9) to
            // ITEM_ATTR_NAME2.TREASURE.  Presence alone is insufficient: the
            // source TreasureLayer explicitly requires a non-zero value.
            treasure = value.int("9") != 0,
            intro = value.string("10") ?: "",
        )
    }

    fun allEquipmentProfiles(): List<EquipmentProfile> = items.indices.mapNotNull(::equipmentProfile)

    fun postsName(postsId: Int): String = posts.getOrNull(postsId)?.string("0") ?: ""

    /** HallLayer._buyIn: chapter properties plus one level-phase item per ordinary equipment type. */
    fun hallBuyProfiles(stageIndex: Int, averageLevel: Int): List<EquipmentProfile> {
        val explicit = generateSequence(shops.getOrNull(stageIndex)?.get("3")?.child) { it.next }
            .map { it.asInt() }
            // 255 occupies a real padding row in the extracted 256-row
            // table, but BuyLayer treats that numeric id as its sentinel.
            .filter { it in items.indices && it != 255 }
            .mapNotNull(::equipmentProfile)
            .toList()
        // HallLayer uses cfgPostsTransfer(), not fixed unit levels, when it
        // chooses which of the three common shop grades is available.
        val transferLevels = config.get("transfer")?.let { values ->
            generateSequence(values.child) { it.next }.map { it.asInt() }.toList()
        }.orEmpty().ifEmpty { listOf(15, 30) }
        val phase = transferLevels.count { averageLevel >= it }
        val common = allEquipmentProfiles()
            // The source checks that PRICE exists and is non-zero.  255 is
            // deliberately retained: BuyLayer renders it as an unpriced item
            // and refuses the purchase in its confirmation path.
            .filter { it.price != 0 && it.itemType in 0..25 && it.itemType % 2 == 0 }
            .groupBy { it.itemType }
            .toSortedMap()
            .values
            .mapNotNull { candidates -> candidates.getOrNull(phase.coerceAtMost(candidates.lastIndex)) }
        return (explicit + common).distinctBy { it.id }
    }

    /** Item constructor's logical ITEM_TYPE classification. */
    fun equipmentCategory(item: EquipmentProfile): Int = when {
        item.id in 150 until 200 -> 3 // ITEM_TYPE.PROPERTY
        item.itemType <= 19 -> 0      // ITEM_TYPE.WEAPONS
        item.itemType <= 25 -> 1      // ITEM_TYPE.ARMOR
        else -> 2                     // ITEM_TYPE.AUXILIARY
    }

    /** Item.price()/Item.sellPrice() monetary units used by BuyLayer and SellLayer. */
    fun purchasePrice(item: EquipmentProfile): Int = if (item.price == 255) 255 else item.price * 100
    fun sellingPrice(item: EquipmentProfile): Int = if (item.price == 255) 255 else purchasePrice(item) * 3 / 4

    /** Config.item[Math.floor(Item.itemType()/2)] used by PropertyLayer. */
    fun equipmentTypeName(itemType: Int): String = config.get("item")
        ?.get(itemType.floorDiv(2))
        ?.asString()
        ?: "아이템"

    /** ui/TreasureLayer.js: Model.itemIter rows with a truthy TREASURE field. */
    fun treasureProfiles(): List<EquipmentProfile> = allEquipmentProfiles().filter(EquipmentProfile::treasure)

    /**
     * The complete original item table subset accepted by BattleLayer._usePro2.
     * This must not be derived from the current inventory: skills such as
     * ZDSY hold an item ID and can invoke it for an enemy with no ItemStore.
     */
    fun battlePropertyItems(): List<EquipmentProfile> = items.indices.mapNotNull(::equipmentProfile)
        .filter { it.itemType in 26..37 || it.itemType in 42..43 }

    /**
     * StageLayer._setEquip translates compact scenario values to the original
     * item table.  Weapon/armor bonuses are applied by Unit._baseBility.
     */
    fun equipmentBonus(scriptValues: List<Int>, unitLevel: Int): EquipmentBonus {
        fun itemId(value: Int, offset: Int): Int? {
            if (value <= 1) return null
            val id = value - 2 + offset
            return if (id >= 150) id + 105 else id
        }
        fun effectiveValue(item: EquipmentProfile, suppliedLevel: Int): Int {
            // Unit.countEquipLevel(): floor(level / floor(lvLimit / 10)) + 1.
            val levelField = (config.get("unit")?.get("lvLimit")?.asInt() ?: 50).floorDiv(10).coerceAtLeast(1)
            val itemLevel = if (suppliedLevel > 0) suppliedLevel else (unitLevel / levelField).coerceIn(0, 8) + 1
            return item.value + (itemLevel - 1) * item.upgradePerLevel
        }
        var attack = 0
        var defense = 0
        var spirit = 0
        listOf(
            itemId(scriptValues.getOrElse(0) { -1 }, 0) to scriptValues.getOrElse(1) { 0},
            itemId(scriptValues.getOrElse(2) { -1 }, 70) to scriptValues.getOrElse(3) { 0},
        ).forEach { (id, suppliedLevel) ->
            val item = id?.let(::equipmentProfile) ?: return@forEach
            val amount = effectiveValue(item, suppliedLevel)
            when (item.itemType - item.itemType % 2) {
                14, 16 -> spirit += amount // fan / sword: intelligence equipment
                18 -> { attack += amount; spirit += amount } // general's sword
                20, 22, 24 -> defense += amount // armor, robe, cloak
                else -> attack += amount
            }
        }
        return EquipmentBonus(attack, defense, spirit)
    }

    /** Unit.equipDefaultWeapon/countDefEquip for freshly created source units. */
    fun defaultEquipmentBonus(postsId: Int, unitLevel: Int): EquipmentBonus {
        val equipment = defaultEquipment(postsId, unitLevel)
        return equipmentBonus(equipment.asScriptValues(), unitLevel)
    }

    /** Exact Unit.equipDefaultWeapon/countDefEquip selection for a new ally. */
    fun defaultEquipment(postsId: Int, unitLevel: Int): CampaignEquipment {
        val post = posts.getOrNull(postsId)
        val allowedTypes = generateSequence(post?.get("10")?.child) { it.next }
            .map { it.asInt() }
            .filter { it % 2 == 0 }
            .toSet()
        val levelField = (config.get("unit")?.getInt("lvLimit", 50) ?: 50).floorDiv(10).coerceAtLeast(1)
        val itemLevel = (unitLevel / levelField).coerceIn(0, 8) + 1
        val phase = (unitLevel / (3 * levelField)).coerceIn(0, 2)
        fun select(type: Int): EquipmentProfile? {
            val candidates = items.indices.asSequence().mapNotNull(::equipmentProfile)
                .filter { item ->
                    item.price != 255 && item.value >= 1 && item.itemType in allowedTypes &&
                        if (type == 0) item.itemType < 20 else item.itemType >= 20
                }
                .take(3)
                .toList()
            return candidates.getOrNull(minOf(phase, candidates.lastIndex))
        }
        val weapon = select(0)?.id?.let { it + 2 } ?: 1
        val armor = select(1)?.id?.let { it - 70 + 2 } ?: 1
        return CampaignEquipment(weapon, itemLevel, armor, itemLevel, 1)
    }

    /** Mirrors Item._refSkill() for scenario and campaign equipment. */
    fun equipmentSkills(scriptValues: List<Int>, unitLevel: Int): Map<Int, Int> {
        fun itemId(value: Int, offset: Int): Int? {
            if (value <= 1) return null
            val id = value - 2 + offset
            return if (id >= 150) id + 105 else id
        }
        fun itemLevel(suppliedLevel: Int): Int {
            val levelField = (config.get("unit")?.get("lvLimit")?.asInt() ?: 50).floorDiv(10).coerceAtLeast(1)
            return if (suppliedLevel > 0) suppliedLevel else (unitLevel / levelField).coerceIn(0, 8) + 1
        }
        val index = itemSkills.get("index") ?: return emptyMap()
        val definitions = itemSkills.get("define") ?: return emptyMap()
        return buildMap {
            listOf(
                itemId(scriptValues.getOrElse(0) { -1 }, 0) to scriptValues.getOrElse(1) { 0},
                itemId(scriptValues.getOrElse(2) { -1 }, 70) to scriptValues.getOrElse(3) { 0},
                itemId(scriptValues.getOrElse(4) { -1 }, 109) to 1,
            ).forEach { (id, suppliedLevel) ->
                val item = id?.let(::equipmentProfile) ?: return@forEach
                val auxiliary = item.itemType > 60
                if (!auxiliary && item.itemType % 2 == 0) return@forEach
                val definitionIds = index.get((if (auxiliary) item.itemType else item.specialType).toString()) ?: return@forEach
                val level = itemLevel(suppliedLevel)
                generateSequence(definitionIds.child) { it.next }.forEach definitionLoop@{ definitionId ->
                    val definition = definitions.get(definitionId.asInt().toString()) ?: return@definitionLoop
                    val skillId = definition.getInt("skillId", -1)
                    if (skillId < 0) return@definitionLoop
                    var effect = definition.getInt("effval", if (auxiliary) item.value else item.effectValue)
                    val phase = definition.getInt("phase", 0)
                    if (level > 6 && definition.has("upgrade")) {
                        put(definition.getInt("upgrade"), effect and 255)
                        return@definitionLoop
                    }
                    if (level <= 6 && phase and 32 != 0) return@definitionLoop
                    if (phase and 2 != 0) effect *= level
                    if (phase and 4 != 0) effect += level
                    if (phase and 8 != 0) effect *= level + 1
                    if (phase and 64 != 0) effect += level / 2
                    if (phase and 256 != 0) effect += unitLevel
                    if (level > 6 && phase and 1 != 0) effect += effect / 2
                    if (level > 6 && phase and 128 != 0) effect++
                    put(skillId, effect and 255)
                }
            }
        }
    }

    /** Unit._pushSkill(): additive skills sum, flag skills OR, others override. */
    fun mergeSkills(vararg layers: Map<Int, Int>): Map<Int, Int> =
        mergeSkillEntries(layers.flatMap { layer -> layer.entries.map { it.key to it.value } })

    private fun mergeSkillEntries(entries: Iterable<Pair<Int, Int>>): Map<Int, Int> = buildMap {
        entries.forEach { (skillId, rawValue) ->
            val value = rawValue and 255
            val prior = get(skillId)
            put(skillId, when (skillIncrementType(skillId)) {
                1 -> if (prior == null) value else (prior + value) and 254
                2 -> if (prior == null) value else prior or value
                else -> value
            })
        }
    }

    /** Unit._baseBility's FZ* passive ability adjustment. */
    fun passiveAbility(base: Int, skillId: Int, skills: Map<Int, Int>): Int {
        val value = skills[skillId]?.and(255) ?: return base
        if (value == 255) return base
        val bonus = if (skillArgument(skillId) != 0) base * value / 100 else value
        return (base + bonus).coerceAtLeast(0)
    }

    private fun magicLearnLevel(magicId: Int, postsId: Int): Int? =
        magics.getOrNull(magicId)?.get("15")?.get(postsId.toString())?.asInt()

    /** Unit.magics() IDs at a given post/level, used by settlement level-up callbacks. */
    fun learnedMagicIds(postsId: Int, level: Int): List<Int> = magics.indices.filter { magicId ->
        magicLearnLevel(magicId, postsId)?.let { level >= it } == true
    }

    /** Model.effareaAttr(id, PS): physical and magic effect-area offsets. */
    fun effectAreaOffsets(id: Int): Set<Pair<Int, Int>> =
        generateSequence(effareas.getOrNull(id)?.get("ps")?.child) { it.next }
            .mapNotNull { point ->
                val x = point.child?.asInt() ?: return@mapNotNull null
                val y = point.child?.next?.asInt() ?: return@mapNotNull null
                x to y
            }.toSet()

    /** BattleUnit.magicEffarea(): Model.effareaUpgrade() for ZJXGFW_CL. */
    fun upgradedEffectArea(id: Int): Pair<Int, Set<Pair<Int, Int>>> {
        val upgradedId = effareas.getOrNull(id)?.getInt("upgrade", id) ?: id
        return upgradedId to effectAreaOffsets(upgradedId)
    }

    fun hitAreaProfile(id: Int): HitAreaProfile? {
        val value = hitareas.getOrNull(id) ?: return null
        val flag = value.get("flag")?.asInt() ?: 0
        val offsets = generateSequence(value.get("ps")?.child) { it.next }
            .mapNotNull { point ->
                val x = point.child?.asInt() ?: return@mapNotNull null
                val y = point.child?.next?.asInt() ?: return@mapNotNull null
                x to y
            }.toSet()
        return HitAreaProfile(id, offsets, allScreen = flag == 2, upgradeId = value.getInt("upgrade", id))
    }

    private fun turnPosts(posts: Int, level: Int, armLimit: Int): Int {
        if (posts >= 60) return posts
        val base = posts - posts % 3
        val eligible = when {
            level >= 30 -> 2
            level >= 15 -> 1
            else -> 0
        }
        return base + maxOf(posts % 3, minOf(eligible, armLimit))
    }

    private fun skillIncrementType(skillId: Int): Int = defineSkills.getOrNull(skillId)?.getInt("incType", 0) ?: 0
    private fun skillArgument(skillId: Int): Int = defineSkills.getOrNull(skillId)?.getInt("arg", 0) ?: 0

    companion object {
        private val DEFAULT_CRITICAL_SPEECH = listOf(
            "음... 정말 한 방에 쓰러뜨릴 거야!", "길 막지 마! 길 막지 마!", "가르침을 내리노라!",
            "무명 병사! 빨리 물러서라!", "가로막는 자는 죽는다! 비켜라, 비켜라……!", "야헤야헤야……!",
            "오호호...!", "하아……!", "아악……!", "크윽……!", "음...!", "죽여라아...!",
            "기술을 보여주마...!", "나의 이 기술을 받아라!!", "죽여라...!", "죽어라!!!", "호호……!",
            "야호……!", "응응응...!", "으윽...!", "후우후……!", "응응!?", "흥!!", "응응응!",
            "아이쿠!!", "나를 봐라!", "모든 것이 이 한 번의 공격에 달렸어!",
            "반드시 당신과 우열을 가려야 해!\n절대로 질 수 없어!", "이 치명타를 받아라!", "받아치기 준비해라!!",
            "죽을 준비를 해라!", "나 왔다, 나 왔다, 나 왔다!!",
        )

        /**
         * `Unit.unitName(id, true)` from the recovered client.  The source
         * removes from the first digit, rather than only a trailing numeric
         * suffix.  Several generic units use a separator before their
         * instance number (for example, `황건군 1`), so a `$`-anchored regex
         * leaves a visible name that does not match SayLayer.
         */
        fun sayLayerUnitName(rawName: String): String = rawName.takeWhile { !it.isDigit() }

        fun load(): OriginalGameData {
            // Source tables must be usable by the headless conformance suite
            // as well as the LibGDX desktop renderer.
            fun raw(name: String): ByteArray = OriginalGameData::class.java.classLoader
                .getResourceAsStream("maps/data/$name")
                ?.use { it.readBytes() }
                ?: Gdx.files.internal("maps/data/$name").readBytes()
            val raw = raw("unit.bin")
            val decoded = requireNotNull(OriginalDataTableCodec.decode(raw)) { "원본 unit 테이블 검증 실패" }
            val root = JsonReader().parse(decoded)
            require(root.isArray) { "원본 unit 테이블 형식이 배열이 아닙니다." }
            val rawArms = raw("arms.bin")
            val decodedArms = requireNotNull(OriginalDataTableCodec.decode(rawArms)) { "원본 arms 테이블 검증 실패" }
            val armsRoot = JsonReader().parse(decodedArms)
            require(armsRoot.isArray) { "원본 arms 테이블 형식이 배열이 아닙니다." }
            val rawPosts = raw("posts.bin")
            val decodedPosts = requireNotNull(OriginalDataTableCodec.decode(rawPosts)) { "원본 posts 테이블 검증 실패" }
            val postsRoot = JsonReader().parse(decodedPosts)
            require(postsRoot.isArray) { "원본 posts 테이블 형식이 배열이 아닙니다." }
            val rawHitareas = raw("hitarea.bin")
            val decodedHitareas = requireNotNull(OriginalDataTableCodec.decode(rawHitareas)) { "원본 hitarea 테이블 검증 실패" }
            val hitareasRoot = JsonReader().parse(decodedHitareas)
            require(hitareasRoot.isArray) { "원본 hitarea 테이블 형식이 배열이 아닙니다." }
            val rawEffareas = raw("effarea.bin")
            val decodedEffareas = requireNotNull(OriginalDataTableCodec.decode(rawEffareas)) { "원본 effarea 테이블 검증 실패" }
            val effareasRoot = JsonReader().parse(decodedEffareas)
            require(effareasRoot.isArray) { "원본 effarea 테이블 형식이 배열이 아닙니다." }
            val rawMagics = raw("magic.bin")
            val decodedMagics = requireNotNull(OriginalDataTableCodec.decode(rawMagics)) { "원본 magic 테이블 검증 실패" }
            val magicsRoot = JsonReader().parse(decodedMagics)
            require(magicsRoot.isArray) { "원본 magic 테이블 형식이 배열이 아닙니다." }
            val rawItems = raw("item.bin")
            val decodedItems = requireNotNull(OriginalDataTableCodec.decode(rawItems)) { "원본 item 테이블 검증 실패" }
            val itemsRoot = JsonReader().parse(decodedItems)
            val rawSkills = raw("unitPostsSkill.bin")
            val decodedSkills = requireNotNull(OriginalDataTableCodec.decode(rawSkills)) { "원본 unitPostsSkill 테이블 검증 실패" }
            val skillsRoot = JsonReader().parse(decodedSkills)
            val rawItemSkills = raw("itemSkills.bin")
            val decodedItemSkills = requireNotNull(OriginalDataTableCodec.decode(rawItemSkills)) { "원본 itemSkills 테이블 검증 실패" }
            val itemSkillsRoot = JsonReader().parse(decodedItemSkills)
            val rawDefineSkills = raw("defineSkill.bin")
            val decodedDefineSkills = requireNotNull(OriginalDataTableCodec.decode(rawDefineSkills)) { "원본 defineSkill 테이블 검증 실패" }
            val defineSkillsRoot = JsonReader().parse(decodedDefineSkills)
            require(itemsRoot.isArray) { "원본 item 테이블 형식이 배열이 아닙니다." }
            val rawConfig = raw("config.bin")
            val decodedConfig = requireNotNull(OriginalDataTableCodec.decode(rawConfig)) { "원본 config 테이블 검증 실패" }
            val configRoot = JsonReader().parse(decodedConfig)
            val rawGameConfig = raw("gameConfig.bin")
            val decodedGameConfig = requireNotNull(OriginalDataTableCodec.decode(rawGameConfig)) { "원본 gameConfig 테이블 검증 실패" }
            val gameConfigRoot = JsonReader().parse(decodedGameConfig)
            val rawShops = raw("shop.bin")
            val decodedShops = requireNotNull(OriginalDataTableCodec.decode(rawShops)) { "원본 shop 테이블 검증 실패" }
            val shopsRoot = JsonReader().parse(decodedShops)
            require(shopsRoot.isArray) { "원본 shop 테이블 형식이 배열이 아닙니다." }
            return OriginalGameData(
                generateSequence(root.child) { it.next }.toList(),
                generateSequence(armsRoot.child) { it.next }.toList(),
                generateSequence(postsRoot.child) { it.next }.toList(),
                generateSequence(hitareasRoot.child) { it.next }.toList(),
                generateSequence(effareasRoot.child) { it.next }.toList(),
                generateSequence(magicsRoot.child) { it.next }.toList(),
                generateSequence(itemsRoot.child) { it.next }.toList(),
                itemSkillsRoot,
                generateSequence(skillsRoot.child) { it.next }.toList(),
                generateSequence(defineSkillsRoot.child) { it.next }.toList(),
                generateSequence(shopsRoot.child) { it.next }.toList(),
                configRoot,
                gameConfigRoot,
            )
        }

        private fun JsonValue.string(key: String): String? = get(key)?.asString()

        private fun stringValues(value: JsonValue): List<String> = when {
            value.isArray -> generateSequence(value.child) { it.next }.map { it.asString() }.toList()
            value.isString -> listOf(value.asString())
            else -> emptyList()
        }

        private fun intValues(value: JsonValue): List<Int> = when {
            value.isArray -> generateSequence(value.child) { it.next }.map { it.asInt() }.toList()
            else -> emptyList()
        }
        private fun JsonValue.int(key: String, fallback: Int = 0): Int = get(key)?.asInt() ?: fallback
        private fun numericChildren(value: JsonValue?, prefix: String = ""): Map<Int, Int> =
            generateSequence(value?.child) { it.next }
                .mapNotNull { child ->
                    child.name.removePrefix(prefix).toIntOrNull()?.let { it to child.asInt() }
                }
                .toMap()
    }
}
