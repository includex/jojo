package com.jojo.game

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.JsonReader
import java.util.Base64
import kotlin.random.Random

/** A learned tactic injected by the original Model.infoTransfer(type = 4). */
data class CampaignMagic(val unitId: Int, val magicId: Int, val learnLevel: Int, val intro: String)
/** Original Model._exInfo entry, retained for HelperLayer / save-state parity. */
data class CampaignInfo(val type: Int, val reserved: String = "", var text: String)

/** A UNIT_TIANFU row mutation injected by Model.infoTransfer(type = 5). */
data class CampaignTalent(val talentIndex: Int, val slot: Int, val effect: Int, val intro: String)
data class CampaignEquipment(
    val weapon: Int,
    val weaponLevel: Int,
    val armor: Int,
    val armorLevel: Int,
    val auxiliary: Int,
    /** ItemStore WEAPON_ATTR_INDEX.EXP for the equipped weapon/armor. */
    val weaponExperience: Int = 0,
    val armorExperience: Int = 0,
) {
    fun asScriptValues(): List<Int> = listOf(weapon, weaponLevel, armor, armorLevel, auxiliary)
}
data class CampaignExperienceResult(
    val gained: Int,
    val level: Int,
    val experience: Int,
    val leveledUp: Boolean,
    val oldLevel: Int = level,
    val oldExperience: Int = 0,
    val learnedMagicIds: List<Int> = emptyList(),
)

/** Observable result of recovered Unit.addLv -> setLevel. */
data class CampaignUnitLevelChange(
    val unitId: Int,
    val oldLevel: Int,
    val newLevel: Int,
    val attributes: Map<Int, Int>,
    /** Source order after derived attributes: equipped items, unit, posts, magic. */
    val cacheRefreshOrder: List<String> = listOf("equipmentSkills", "unitSkills", "postsSkills", "magic"),
)

/**
 * Observable model half of recovered `Unit.setPosts(posts, flags)`.
 *
 * A BattleUnit may add an avatar-load callback around this mutation, but the
 * persistent Unit API itself only changes POSTS, invalidates its posts/magic
 * caches, and (for eligible Mine units) recalculates the seven phase values.
 */
data class CampaignUnitPostsChange(
    val unitId: Int,
    val oldPosts: Int,
    val newPosts: Int,
    val flags: Int,
    /** False for the source `flags & 2 && posts() == requested` fast path. */
    val postsWritten: Boolean,
    /** `resetPostsSkills()` followed by `refMagick()`, when POSTS was written. */
    val cacheRefreshOrder: List<String>,
    /** `refAbilityPhase()` values, in ATT..MP source order, when it ran. */
    val derivedAttributes: Map<Int, Int>,
)

/** Result retained by BattleScreen._addWeaponExp before Global113 is opened. */
data class CampaignEquipmentExperienceResult(
    val unitId: Int,
    val slot: CampaignEquipmentSlot,
    val itemId: Int,
    val gained: Int,
    val oldLevel: Int,
    val newLevel: Int,
    val oldExperience: Int,
    val newExperience: Int,
    val oldValue: Int,
    val newValue: Int,
) {
    val leveledUp: Boolean get() = newLevel > oldLevel
}

/**
 * Mutable counterpart of the original Model singleton.  Scenario interpreters
 * are short lived, while Model data deliberately survives event, battle and
 * hall transitions; keeping this state outside an individual ScenarioStage is
 * therefore essential for a campaign to behave like the source game.
 */
class CampaignState(private val randomSource: (Int) -> Int = { upperExclusive -> Random.Default.nextInt(upperExclusive) }) {
    private val injectedInfoTransferRandomValues = ArrayDeque<Int>()
    /** Model._exInfo, exposed by Model.getInfo(). */
    val extraInfo = mutableListOf<CampaignInfo>()
    val globalVariables = linkedMapOf<Int, Any?>()
    /** Model.property[MONEY], clamped by the source Model.setMoney(). */
    var money: Int = 0
        private set

    fun addMoney(delta: Int) { money = (money.toLong() + delta).coerceIn(0L, 9_999_999L).toInt() }

    /** Test-only source Tool.random values consumed by infoTransfer(type = 26). */
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
    val inventory = CampaignInventory(
        joinedUnitIds = { joinedUnits },
        unitAttribute = ::unitAttribute,
    )
    val equipmentProgression = CampaignEquipmentProgression(inventory)
    val roster = CampaignRoster { joinedUnits }
    var endingId: Int? = null
        private set

    /** Model.reset-equivalent used by the title screen's New Game action. */
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

    /** Exact persistent half of recovered `Unit.setPosts(t, e = 3)`. */
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

        // This is intentionally outside the `flags & 2` fast-path, matching
        // the source's second do/while block.  A same-post live refresh can
        // therefore still recompute Mine ability phases.
        val mine = unitAttribute(unitId, UNIT_ATTR_JOIN, 0) != 0
        val refreshAbility = flags and 8 == 0 && mine && (
            flags and 4 != 0 ||
                (globalVariables[GLOBAL_SJCS] as? Number)?.toInt() == 1 ||
                registeredFeatures and ENABLED_FEATURE_ZZSJCS != 0
            )
        val derived = if (refreshAbility) {
            val level = unitAttribute(unitId, UNIT_ATTR_LEVEL, profile.level).coerceAtLeast(1)
            data.unitLevelDerivedAttributes(unitId, unitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts), level, mine = true, campaign = this)
                .also { values -> values.forEach { (attribute, value) -> setUnitAttribute(unitId, attribute, value) } }
        } else emptyMap()
        return CampaignUnitPostsChange(
            unitId = unitId,
            oldPosts = oldPosts,
            newPosts = posts,
            flags = flags,
            postsWritten = postsWritten,
            cacheRefreshOrder = if (postsWritten) listOf("postsSkills", "magic") else emptyList(),
            derivedAttributes = derived,
        )
    }

    /** Exact model half of Unit.addLv(t) -> setLevel(lv()+t, 0). */
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

        // A source Unit always has a concrete POSTS field. Materialize the
        // table fallback so a later live-unit rebuild does not auto-promote it.
        if (unitAttributes[unitId]?.containsKey(UNIT_ATTR_POSTS) != true) {
            setUnitAttribute(unitId, UNIT_ATTR_POSTS, profile.posts)
        }
        // Unit.setLevel writes LV before either derived-ability path.
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
        // _addAbility/refAbilityPhase completes before every skill cache reset.
        attributes.forEach { (attribute, value) -> setUnitAttribute(unitId, attribute, value) }
        return CampaignUnitLevelChange(unitId, oldLevel, newLevel, attributes)
    }

    /** Model.averageLv: descending levels with one quarter trimmed per side. */
    fun averageJoinedLevel(): Int {
        if (joinedUnits.isEmpty()) return 1
        val levels = joinedUnits.map { unitAttribute(it, UNIT_ATTR_LEVEL, 1) }.sortedDescending()
        val trim = levels.size / 4
        return levels.subList(trim, levels.size - trim).sum() / (levels.size - trim * 2)
    }

    /** Direct Model.info(type, text): replace every empty-reserved row or append one. */
    fun info(type: Int, text: String) {
        val normalized = text.replace("\n", "<br/>")
        val open = extraInfo.filter { it.reserved.isEmpty() }
        if (open.isNotEmpty()) open.forEach { it.text = normalized }
        else extraInfo += CampaignInfo(type, "", normalized)
    }

    /** Persistent equivalent of Unit.setPosts after the original promotion action. */
    fun promote(unitId: Int, fallbackPosts: Int, fallbackLevel: Int, data: GameDataCatalog): Int? {
        val posts = unitAttribute(unitId, UNIT_ATTR_POSTS, fallbackPosts)
        val level = unitAttribute(unitId, UNIT_ATTR_LEVEL, fallbackLevel).coerceAtLeast(1)
        val upgraded = data.promotionTarget(posts, level) ?: return null
        setUnitAttribute(unitId, UNIT_ATTR_POSTS, upgraded)
        return upgraded
    }

    /** BattleScreen repeatedly feeds Unit.unitAddExp's unapplied remainder after each level-up. */
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
        return CampaignExperienceResult(
            gained, level, experience, level != oldLevel,
            oldLevel = oldLevel, oldExperience = oldExperience,
        )
    }

    fun applyInfoTransfer(type: Int, payload: String, selectedUnitId: Int = 0) {
        when (type) {
            // Model.infoTransfer: the selected hall record's display name.
            0 -> if (selectedUnitId >= 0) unitNames[selectedUnitId] = payload
            // Model.infoTransfer: raises every current ally below the trimmed average.
            18 -> normalizeJoinedUnitLevels()
            // Model.infoTransfer: magicId, learnLevel, unitId, optional introduction.
            4 -> payload.lines().let { values ->
                if (values.size >= 3) {
                    val magicId = values[0].toIntOrNull() ?: return@let
                    val level = values[1].toIntOrNull() ?: return@let
                    val unitId = values[2].toIntOrNull() ?: return@let
                    extraMagic[unitId to magicId] = CampaignMagic(unitId, magicId, level, values.drop(3).firstOrNull()?.ifBlank { DEFAULT_SKILL_INTRO } ?: DEFAULT_SKILL_INTRO)
                }
            }
            // Model.infoTransfer: unitId, talent slot, effect value, optional introduction.
            5 -> payload.lines().let { values ->
                if (values.size >= 3) {
                    val talentIndex = values[0].toIntOrNull() ?: return@let
                    val slot = values[1].toIntOrNull() ?: return@let
                    val effect = values[2].toIntOrNull() ?: return@let
                    talents[talentIndex to slot] = CampaignTalent(talentIndex, slot, effect, values.drop(3).lastOrNull().orEmpty())
                }
            }
            // Model.infoTransfer type 10 owns ZS/TZ talent tables. Preserve its
            // source payload until those battle skill tables are applied.
            10 -> formationTalents += payload
            22 -> endingId = payload.toIntOrNull()
            // Source chooses an integer uniformly from 0 through N - 1 and
            // writes it into global variable 4025.
            26 -> payload.toIntOrNull()?.takeIf { it > 0 }?.let { globalVariables[4025] = random(it) }
        }
    }

    private fun normalizeJoinedUnitLevels() {
        if (joinedUnits.isEmpty()) return
        val levels = joinedUnits.map { unitAttribute(it, UNIT_ATTR_LEVEL, 1) }.sortedDescending()
        val trim = levels.size / 4
        val middle = levels.subList(trim, levels.size - trim)
        val average = middle.sum() / middle.size
        joinedUnits.forEach { unitId ->
            if (unitAttribute(unitId, UNIT_ATTR_LEVEL, 1) < average) setUnitAttribute(unitId, UNIT_ATTR_LEVEL, average)
        }
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

/** Durable campaign cursor using the original UserDefault cipher envelope. */
class CampaignStore(
    private val preferences: Preferences,
) {
    data class Snapshot(
        val currentScenario: String = "R_00",
        val completed: Set<String> = emptySet(),
        val choices: Map<String, String> = emptyMap(),
        /** Model.incStage-compatible save metadata used by battle=2 loads. */
        val stage: Int = 0,
    )

    /** Shared live state for all event and battle screens in this Game instance. */
    val state = CampaignState()
    var snapshot: Snapshot = read()
        private set

    fun enter(scenario: String) {
        if (!scenario.startsWith("R_")) return
        snapshot = snapshot.copy(currentScenario = scenario)
        write()
    }

    /** New Game must not inherit any previous Model, roster, or branch state. */
    fun newGame() {
        state.reset()
        snapshot = Snapshot()
        write()
    }

    fun recordChoice(scenario: String, choice: String) {
        snapshot = snapshot.copy(choices = snapshot.choices + (scenario to choice))
        write()
    }

    fun complete(scenario: String, nextScenario: String) {
        snapshot = snapshot.copy(currentScenario = nextScenario, completed = snapshot.completed + scenario)
        write()
    }

    /** Flushes Model-equivalent live state before a screen transition. */
    fun persist() = write()
    fun incStage() { snapshot = snapshot.copy(stage = snapshot.stage + 1); write() }
    /** StageLayer.jumpScene writes the absolute Model stage before replacement. */
    fun setStage(stage: Int) {
        require(stage >= 0) { "campaign stage must be non-negative" }
        snapshot = snapshot.copy(stage = stage)
        write()
    }

    /** Numbered Manager.saveGame slot used by the source SaveLayer. */
    fun saveSlot(index: Int): String {
        require(index >= 0) { "save slot index must be non-negative" }
        write()
        val payload = preferences.getString(KEY, "")
        // SaveLayer is opened from BattleScreen, therefore preserve the
        // original battle-scene marker for LoadGameLayer._loadGame.
        val record = "{\"time\":${System.currentTimeMillis()},\"name\":${quote(snapshot.currentScenario)},\"model\":{\"version\":1,\"stage\":${snapshot.stage},\"property2\":[0,${snapshot.stage}]},\"battle\":1,\"payload\":${quote(payload)}}"
        preferences.putString("$SLOT_KEY_PREFIX$index", record).flush()
        return record
    }

    /** Manager.loadGame(index) representation consumed by SaveLayer. */
    fun loadSlot(index: Int): String? = preferences.getString("$SLOT_KEY_PREFIX$index", "").takeIf { it.isNotBlank() }
    fun savedPage(): Int = preferences.getInteger(SAVE_PAGE_KEY, 0)
    fun savePage(page: Int) { preferences.putInteger(SAVE_PAGE_KEY, page).flush() }

    /** Manager.resetGame + Model.loadGame for a numbered desktop slot. */
    fun restoreSlot(index: Int, raw: String): Boolean {
        if (loadSlot(index) != raw) return false
        val record = runCatching { JsonReader().parse(raw) }.getOrNull() ?: return false
        val encoded = record.getString("payload", "")
        val envelope = runCatching { String(Base64.getDecoder().decode(encoded), Charsets.ISO_8859_1) }.getOrNull() ?: return false
        val root = CampaignSaveCodec.decode(envelope)?.let { runCatching { JsonReader().parse(it) }.getOrNull() } ?: return false
        val scenario = root.getString("currentScenario", "").takeIf { it.startsWith("R_") } ?: return false
        val completed = generateSequence(root.get("completed")?.child) { it.next }.map { it.asString() }.toSet()
        val choices = generateSequence(root.get("choices")?.child) { it.next }.associate { it.name to it.asString() }
        state.reset()
        root.getString("runtime", "").takeIf { it.isNotBlank() }?.let(::restoreState)
        val stage = record.get("model")?.get("property2")?.get(1)?.asInt() ?: record.get("model")?.getInt("stage", 0) ?: 0
        snapshot = Snapshot(scenario, completed, choices, stage)
        write()
        return true
    }

    private fun read(): Snapshot {
        val encoded = preferences.getString(KEY, "")
        if (encoded.isBlank()) return Snapshot()
        val envelope = runCatching { String(Base64.getDecoder().decode(encoded), Charsets.ISO_8859_1) }.getOrNull() ?: return Snapshot()
        val root = CampaignSaveCodec.decode(envelope)?.let { runCatching { JsonReader().parse(it) }.getOrNull() } ?: return Snapshot()
        val completed = generateSequence(root.get("completed")?.child) { it.next }.map { it.asString() }.toSet()
        val choices = generateSequence(root.get("choices")?.child) { it.next }.associate { it.name to it.asString() }
        root.getString("runtime", "").takeIf { it.isNotBlank() }?.let(::restoreState)
        return Snapshot(root.getString("currentScenario", "R_00"), completed, choices, root.getInt("stage", 0))
    }

    private fun write() {
        val completed = snapshot.completed.sorted().joinToString(",") { quote(it) }
        val choices = snapshot.choices.toSortedMap().entries.joinToString(",") { "${quote(it.key)}:${quote(it.value)}" }
        val json = "{\"currentScenario\":${quote(snapshot.currentScenario)},\"completed\":[$completed],\"choices\":{$choices},\"stage\":${snapshot.stage},\"runtime\":${quote(Base64.getEncoder().encodeToString(runtimeJson().toByteArray(Charsets.UTF_8)))}}"
        val envelope = CampaignSaveCodec.encode(json)
        preferences.putString(KEY, Base64.getEncoder().encodeToString(envelope.toByteArray(Charsets.ISO_8859_1))).flush()
    }

    private fun runtimeJson(): String = buildString {
        append('{')
        append("\"globals\":").append(intMap(state.globalVariables.mapValues { it.value.toString().toIntOrNull() ?: 0 }))
        append(",\"money\":").append(state.money)
        append(",\"joined\":[").append(state.joinedUnits.joinToString(",")).append(']')
        append(",\"attributes\":{")
        append(state.unitAttributes.entries.joinToString(",") { (unitId, attributes) -> "${quote(unitId.toString())}:${intMap(attributes)}" })
        append('}')
        append(",\"names\":{").append(state.unitNames.entries.joinToString(",") { (id, name) -> "${quote(id.toString())}:${quote(name)}" }).append('}')
        append(",\"magic\":[").append(state.extraMagic.values.joinToString(",") { magic ->
            "{\"u\":${magic.unitId},\"m\":${magic.magicId},\"l\":${magic.learnLevel},\"i\":${quote(magic.intro)}}"
        }).append(']')
        append(",\"talents\":[").append(state.talents.values.joinToString(",") { talent ->
            "{\"u\":${talent.talentIndex},\"s\":${talent.slot},\"e\":${talent.effect},\"i\":${quote(talent.intro)}}"
        }).append(']')
        append(",\"formations\":[").append(state.formationTalents.joinToString(",") { quote(it) }).append(']')
        append(",\"items\":").append(intMap(state.inventory.items))
        append(",\"treasures\":[").append(state.inventory.discoveredTreasures.joinToString(",")).append(']')
        append(",\"itemLevels\":{").append(state.inventory.items.keys.filter { it !in 150..254 }.joinToString(",") { itemId ->
            val levels = state.inventory.itemLevels(itemId).joinToString(",")
            "${quote(itemId.toString())}:[${levels}]"
        }).append('}')
        append(",\"itemExperiences\":{").append(state.inventory.items.keys.filter { it !in 150..254 }.joinToString(",") { itemId ->
            val experiences = state.inventory.itemExperiences(itemId).joinToString(",")
            "${quote(itemId.toString())}:[${experiences}]"
        }).append('}')
        append(",\"roster\":[").append(state.roster.battleRoster.joinToString(",")).append(']')
        append(",\"equipment\":{").append(state.inventory.equipment.entries.joinToString(",") { (unitId, item) ->
            "${quote(unitId.toString())}:[${item.weapon},${item.weaponLevel},${item.armor},${item.armorLevel},${item.auxiliary},${item.weaponExperience},${item.armorExperience}]"
        }).append('}')
        state.endingId?.let { append(",\"ending\":").append(it) }
        append('}')
    }

    private fun restoreState(encoded: String) {
        val json = runCatching { String(Base64.getDecoder().decode(encoded), Charsets.UTF_8) }.getOrNull() ?: return
        val root = runCatching { JsonReader().parse(json) }.getOrNull() ?: return
        state.addMoney(root.getInt("money", 0))
        root.get("globals").intEntries().forEach { (id, value) -> state.globalVariables[id] = value }
        root.get("joined").children().forEach { state.joinedUnits += it.asInt() }
        root.get("attributes").children().forEach { unit ->
            unit.intEntries().forEach { (attribute, value) -> state.setUnitAttribute(unit.name.toIntOrNull() ?: return@forEach, attribute, value) }
        }
        root.get("names").children().forEach { entry -> entry.name.toIntOrNull()?.let { state.unitNames[it] = entry.asString() } }
        root.get("magic").children().forEach { entry ->
            val unitId = entry.getInt("u", -1)
            val magicId = entry.getInt("m", -1)
            if (unitId >= 0 && magicId >= 0) state.extraMagic[unitId to magicId] = CampaignMagic(unitId, magicId, entry.getInt("l", 1), entry.getString("i", "기본 설명"))
        }
        root.get("talents").children().forEach { entry ->
            val unitId = entry.getInt("u", -1)
            val slot = entry.getInt("s", -1)
            if (unitId >= 0 && slot >= 0) state.talents[unitId to slot] = CampaignTalent(unitId, slot, entry.getInt("e", 0), entry.getString("i", ""))
        }
        root.get("formations").children().forEach { state.formationTalents += it.asString() }
        state.inventory.restoreDiscoveredTreasures(
            root.get("treasures")?.children()?.map { it.asInt() }?.asIterable() ?: emptyList(),
        )
        val persistedLevels = root.get("itemLevels")
        val persistedExperiences = root.get("itemExperiences")
        root.get("items").intEntries().forEach { (itemId, count) ->
            val levels = persistedLevels?.get(itemId.toString())?.children()?.map { it.asInt() }?.toList().orEmpty()
            val experiences = persistedExperiences?.get(itemId.toString())?.children()?.map { it.asInt() }?.toList().orEmpty()
            if (itemId in 150..254) state.inventory.addItem(itemId, count)
            else {
                levels.take(count).forEachIndexed { index, level -> state.inventory.addItem(itemId, level = level, experience = experiences.getOrElse(index) { 0 }) }
                repeat((count - levels.size).coerceAtLeast(0)) { state.inventory.addItem(itemId) }
            }
        }
        state.roster.restoreBattleRoster(root.get("roster").children().map { it.asInt() }.asIterable())
        root.get("equipment").children().forEach { entry ->
            val values = entry.children().map { it.asInt() }.toList()
            entry.name.toIntOrNull()?.takeIf { values.size >= 5 }?.let { unitId ->
                state.inventory.setEquipment(
                    unitId,
                    CampaignEquipment(values[0], values[1], values[2], values[3], values[4], values.getOrElse(5) { 0 }, values.getOrElse(6) { 0 }),
                )
            }
        }
        root.get("ending")?.asInt()?.let { state.applyInfoTransfer(22, it.toString()) }
    }

    private fun intMap(values: Map<Int, Int>): String = values.entries.joinToString(",", "{", "}") { (key, value) -> "${quote(key.toString())}:$value" }
    private fun com.badlogic.gdx.utils.JsonValue?.children(): Sequence<com.badlogic.gdx.utils.JsonValue> = sequence {
        var value = this@children?.child
        while (value != null) { yield(value); value = value.next }
    }
    private fun com.badlogic.gdx.utils.JsonValue?.intEntries(): List<Pair<Int, Int>> = children().mapNotNull { entry ->
        entry.name.toIntOrNull()?.let { id -> id to entry.asInt() }
    }.toList()

    private fun quote(value: String): String = buildString {
        append('"')
        value.forEach { char -> when (char) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> append(char)
        } }
        append('"')
    }

    private companion object {
        const val KEY = "CAMPAIGN_STATE"
        const val SLOT_KEY_PREFIX = "save-slot-"
        const val SAVE_PAGE_KEY = "SAVE_PAGE"
    }
}
