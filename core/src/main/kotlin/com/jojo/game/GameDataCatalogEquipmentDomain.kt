package com.jojo.game
import com.jojo.game.domain.campaign.*

/** Parses item, shop, equipment-growth and script-equipment data. */
internal class GameDataCatalogEquipmentDomain(tables: GameDataTableBundle) : GameDataCatalogTableDomain(tables) {
    fun equipmentProfile(id: Int): GameDataCatalog.EquipmentProfile? {
        val value = items.getOrNull(id) ?: return null
        return GameDataCatalog.EquipmentProfile(
            id, value.string("0") ?: "장비 $id", value.int("1", 255), value.int("3", 255),
            value.int("2", 255), value.int("5"), value.int("6"), value.int("8"), value.int("4") + 1,
            value.int("9") != 0, value.string("10") ?: ""
        )
    }

    fun allEquipmentProfiles(): List<GameDataCatalog.EquipmentProfile> = items.indices.mapNotNull(::equipmentProfile)
    fun hallBuyProfiles(stageIndex: Int, averageLevel: Int): List<GameDataCatalog.EquipmentProfile> {
        val explicit = generateSequence(shops.getOrNull(stageIndex)?.get("3")?.child) { it.next }.map { it.asInt() }
            .filter { it in items.indices && it != 255 }.mapNotNull(::equipmentProfile).toList()
        val transfers = config.get("transfer")
            ?.let { generateSequence(it.child) { node -> node.next }.map { node -> node.asInt() }.toList() }
            .orEmpty().ifEmpty { listOf(15, 30) }
        val phase = transfers.count { averageLevel >= it }
        val common = allEquipmentProfiles().filter { it.price != 0 && it.itemType in 0..25 && it.itemType % 2 == 0 }
            .groupBy { it.itemType }.toSortedMap().values.mapNotNull { it.getOrNull(phase.coerceAtMost(it.lastIndex)) }
        return (explicit + common).distinctBy { it.id }
    }

    fun equipmentCategory(item: GameDataCatalog.EquipmentProfile): Int = when {
        item.id in 150 until 200 -> 3; item.itemType <= 19 -> 0; item.itemType <= 25 -> 1; else -> 2
    }

    fun purchasePrice(item: GameDataCatalog.EquipmentProfile): Int = if (item.price == 255) 255 else item.price * 100
    fun sellingPrice(item: GameDataCatalog.EquipmentProfile): Int =
        if (item.price == 255) 255 else purchasePrice(item) * 3 / 4

    fun equipmentTypeName(itemType: Int): String = config.get("item")?.get(itemType.floorDiv(2))?.asString() ?: "아이템"
    fun treasureProfiles(): List<GameDataCatalog.EquipmentProfile> =
        allEquipmentProfiles().filter(GameDataCatalog.EquipmentProfile::treasure)

    fun battlePropertyItems(): List<GameDataCatalog.EquipmentProfile> =
        allEquipmentProfiles().filter { it.itemType in 26..37 || it.itemType in 42..43 }

    fun equipmentExperienceLimit(itemId: Int, level: Int): Int {
        val values = config.get(if ((equipmentProfile(itemId)?.itemType ?: 0) % 2 == 0) "comEquip" else "speEquip")
        return (values?.getInt("expLimit", 200) ?: 200) + if (level >= (values?.getInt("upgrade", 6) ?: 6)) 50 else 0
    }

    fun equipmentLevelLimit(itemId: Int): Int =
        config.get(if ((equipmentProfile(itemId)?.itemType ?: 0) % 2 == 0) "comEquip" else "speEquip")
            ?.getInt("lvLimit", 9) ?: 9

    fun equipmentBonus(scriptValues: List<Int>, unitLevel: Int): GameDataCatalog.EquipmentBonus {
        var attack = 0
        var defense = 0
        var spirit = 0
        listOf(
            itemId(scriptValues.getOrElse(0) { -1 }, 0) to scriptValues.getOrElse(1) { 0 },
            itemId(scriptValues.getOrElse(2) { -1 }, 70) to scriptValues.getOrElse(3) { 0 })
            .forEach { (id, suppliedLevel) ->
                val item = id?.let(::equipmentProfile) ?: return@forEach
                val amount = effectiveValue(item, suppliedLevel, unitLevel)
                when (item.itemType - item.itemType % 2) {
                    14, 16 -> spirit += amount; 18 -> {
                    attack += amount; spirit += amount
                }; 20, 22, 24 -> defense += amount; else -> attack += amount
                }
            }
        return GameDataCatalog.EquipmentBonus(attack, defense, spirit)
    }

    fun defaultEquipmentBonus(postsId: Int, unitLevel: Int): GameDataCatalog.EquipmentBonus =
        equipmentBonus(defaultEquipment(postsId, unitLevel).asScriptValues(), unitLevel)

    fun defaultEquipment(postsId: Int, unitLevel: Int): CampaignEquipment {
        val allowed = generateSequence(posts.getOrNull(postsId)?.get("10")?.child) { it.next }.map { it.asInt() }
            .filter { it % 2 == 0 }.toSet()
        val field = (config.get("unit")?.getInt("lvLimit", 50) ?: 50).floorDiv(10).coerceAtLeast(1)
        val itemLevel = (unitLevel / field).coerceIn(0, 8) + 1
        val phase = (unitLevel / (3 * field)).coerceIn(0, 2)
        fun select(type: Int): GameDataCatalog.EquipmentProfile? = allEquipmentProfiles().asSequence()
            .filter { item -> item.price != 255 && item.value >= 1 && item.itemType in allowed && if (type == 0) item.itemType < 20 else item.itemType >= 20 }
            .take(3).toList().let { it.getOrNull(minOf(phase, it.lastIndex)) }
        return CampaignEquipment(select(0)?.id?.plus(2) ?: 1, itemLevel, select(1)?.id?.minus(68) ?: 1, itemLevel, 1)
    }

    fun equipmentSkills(scriptValues: List<Int>, unitLevel: Int): Map<Int, Int> {
        val index = itemSkills.get("index") ?: return emptyMap()
        val definitions = itemSkills.get("define") ?: return emptyMap()
        return buildMap {
            listOf(
                itemId(scriptValues.getOrElse(0) { -1 }, 0) to scriptValues.getOrElse(1) { 0 },
                itemId(scriptValues.getOrElse(2) { -1 }, 70) to scriptValues.getOrElse(3) { 0 },
                itemId(scriptValues.getOrElse(4) { -1 }, 109) to 1
            ).forEach { (id, suppliedLevel) ->
                val item = id?.let(::equipmentProfile) ?: return@forEach
                val auxiliary = item.itemType > 60
                if (!auxiliary && item.itemType % 2 == 0) return@forEach
                val definitionIds =
                    index.get((if (auxiliary) item.itemType else item.specialType).toString()) ?: return@forEach
                val level = itemLevel(suppliedLevel, unitLevel)
                generateSequence(definitionIds.child) { it.next }.forEach loop@{ definitionId ->
                    val definition = definitions.get(definitionId.asInt().toString()) ?: return@loop
                    val skillId = definition.getInt("skillId", -1); if (skillId < 0) return@loop
                    var effect = definition.getInt("effval", if (auxiliary) item.value else item.effectValue)
                    val phase = definition.getInt("phase", 0)
                    if (level > 6 && definition.has("upgrade")) {
                        put(definition.getInt("upgrade"), effect and 255); return@loop
                    }
                    if (level <= 6 && phase and 32 != 0) return@loop
                    if (phase and 2 != 0) effect *= level; if (phase and 4 != 0) effect += level; if (phase and 8 != 0) effect *= level + 1; if (phase and 64 != 0) effect += level / 2; if (phase and 256 != 0) effect += unitLevel; if (level > 6 && phase and 1 != 0) effect += effect / 2; if (level > 6 && phase and 128 != 0) effect++
                    put(skillId, effect and 255)
                }
            }
        }
    }

    private fun itemId(value: Int, offset: Int): Int? {
        if (value <= 1) return null
        val id = value - 2 + offset; return if (id >= 150) id + 105 else id
    }

    private fun itemLevel(suppliedLevel: Int, unitLevel: Int): Int =
        if (suppliedLevel > 0) suppliedLevel else (unitLevel / levelField()).coerceIn(0, 8) + 1

    private fun effectiveValue(item: GameDataCatalog.EquipmentProfile, suppliedLevel: Int, unitLevel: Int): Int =
        item.value + (itemLevel(suppliedLevel, unitLevel) - 1) * item.upgradePerLevel

    private fun levelField(): Int = (config.get("unit")?.get("lvLimit")?.asInt() ?: 50).floorDiv(10).coerceAtLeast(1)
}
