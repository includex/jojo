package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.domain.campaign.*
import java.util.Base64

class CampaignStore(
    private val preferences: Preferences,
) {
    /**
     * data class  `Snapshot`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * 공개 메서드 `enter`
     *
     * ### 파라미터
    - `scenario` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `recordChoice`
     *
     * ### 파라미터
    - `scenario` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `choice` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun recordChoice(scenario: String, choice: String) {
        snapshot = snapshot.copy(choices = snapshot.choices + (scenario to choice))
        write()
    }

    /**
     * 공개 메서드 `complete`
     *
     * ### 파라미터
    - `scenario` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `nextScenario` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun complete(scenario: String, nextScenario: String) {
        snapshot = snapshot.copy(currentScenario = nextScenario, completed = snapshot.completed + scenario)
        write()
    }

    /** Flushes Model-equivalent live state before a screen transition. */
    fun persist() = write()

    /**
     * 공개 메서드 `incStage`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun incStage() {
        snapshot = snapshot.copy(stage = snapshot.stage + 1); write()
    }

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
        val record =
            "{\"time\":${System.currentTimeMillis()},\"name\":${quote(snapshot.currentScenario)},\"model\":{\"version\":1,\"stage\":${snapshot.stage},\"property2\":[0,${snapshot.stage}]},\"battle\":1,\"payload\":${
                quote(payload)
            }}"
        preferences.putString("$SLOT_KEY_PREFIX$index", record).flush()
        return record
    }

    /** Manager.loadGame(index) representation consumed by SaveLayer. */
    fun loadSlot(index: Int): String? = preferences.getString("$SLOT_KEY_PREFIX$index", "").takeIf { it.isNotBlank() }

    /**
     * 공개 메서드 `savedPage`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun savedPage(): Int = preferences.getInteger(SAVE_PAGE_KEY, 0)

    /**
     * 공개 메서드 `savePage`
     *
     * ### 파라미터
    - `page` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun savePage(page: Int) {
        preferences.putInteger(SAVE_PAGE_KEY, page).flush()
    }

    /** Manager.resetGame + Model.loadGame for a numbered desktop slot. */
    fun restoreSlot(index: Int, raw: String): Boolean {
        if (loadSlot(index) != raw) return false
        val record = runCatching { JsonReader().parse(raw) }.getOrNull() ?: return false
        val encoded = record.getString("payload", "")
        val envelope =
            runCatching { String(Base64.getDecoder().decode(encoded), Charsets.ISO_8859_1) }.getOrNull() ?: return false
        val root = CampaignSaveCodec.decode(envelope)?.let { runCatching { JsonReader().parse(it) }.getOrNull() }
            ?: return false
        val scenario = root.getString("currentScenario", "").takeIf { it.startsWith("R_") } ?: return false
        val completed = generateSequence(root.get("completed")?.child) { it.next }.map { it.asString() }.toSet()
        val choices = generateSequence(root.get("choices")?.child) { it.next }.associate { it.name to it.asString() }
        state.reset()
        root.getString("runtime", "").takeIf { it.isNotBlank() }?.let(::restoreState)
        val stage =
            record.get("model")?.get("property2")?.get(1)?.asInt() ?: record.get("model")?.getInt("stage", 0) ?: 0
        snapshot = Snapshot(scenario, completed, choices, stage)
        write()
        return true
    }

    private fun read(): Snapshot {
        val encoded = preferences.getString(KEY, "")
        if (encoded.isBlank()) return Snapshot()
        val envelope = runCatching { String(Base64.getDecoder().decode(encoded), Charsets.ISO_8859_1) }.getOrNull()
            ?: return Snapshot()
        val root = CampaignSaveCodec.decode(envelope)?.let { runCatching { JsonReader().parse(it) }.getOrNull() }
            ?: return Snapshot()
        val completed = generateSequence(root.get("completed")?.child) { it.next }.map { it.asString() }.toSet()
        val choices = generateSequence(root.get("choices")?.child) { it.next }.associate { it.name to it.asString() }
        root.getString("runtime", "").takeIf { it.isNotBlank() }?.let(::restoreState)
        return Snapshot(root.getString("currentScenario", "R_00"), completed, choices, root.getInt("stage", 0))
    }

    private fun write() {
        val completed = snapshot.completed.sorted().joinToString(",") { quote(it) }
        val choices = snapshot.choices.toSortedMap().entries.joinToString(",") { "${quote(it.key)}:${quote(it.value)}" }
        val json =
            "{\"currentScenario\":${quote(snapshot.currentScenario)},\"completed\":[$completed],\"choices\":{$choices},\"stage\":${snapshot.stage},\"runtime\":${
                quote(Base64.getEncoder().encodeToString(runtimeJson().toByteArray(Charsets.UTF_8)))
            }}"
        val envelope = CampaignSaveCodec.encode(json)
        preferences.putString(KEY, Base64.getEncoder().encodeToString(envelope.toByteArray(Charsets.ISO_8859_1)))
            .flush()
    }

    private fun runtimeJson(): String = buildString {
        append('{')
        append("\"globals\":").append(intMap(state.globalVariables.mapValues {
            it.value.toString().toIntOrNull() ?: 0
        }))
        append(",\"money\":").append(state.money)
        append(",\"joined\":[").append(state.joinedUnits.joinToString(",")).append(']')
        append(",\"attributes\":{")
        append(state.unitAttributes.entries.joinToString(",") { (unitId, attributes) ->
            "${quote(unitId.toString())}:${
                intMap(
                    attributes
                )
            }"
        })
        append('}')
        append(",\"names\":{").append(state.unitNames.entries.joinToString(",") { (id, name) ->
            "${quote(id.toString())}:${
                quote(
                    name
                )
            }"
        }).append('}')
        append(",\"magic\":[").append(state.extraMagic.values.joinToString(",") { magic ->
            "{\"u\":${magic.unitId},\"m\":${magic.magicId},\"l\":${magic.learnLevel},\"i\":${quote(magic.intro)}}"
        }).append(']')
        append(",\"talents\":[").append(state.talents.values.joinToString(",") { talent ->
            "{\"u\":${talent.talentIndex},\"s\":${talent.slot},\"e\":${talent.effect},\"i\":${quote(talent.intro)}}"
        }).append(']')
        append(",\"formations\":[").append(state.formationTalents.joinToString(",") { quote(it) }).append(']')
        append(",\"items\":").append(intMap(state.inventory.items))
        append(",\"treasures\":[").append(state.inventory.discoveredTreasures.joinToString(",")).append(']')
        append(",\"itemLevels\":{").append(state.inventory.items.keys.filter { it !in 150..254 }
            .joinToString(",") { itemId ->
                val levels = state.inventory.itemLevels(itemId).joinToString(",")
                "${quote(itemId.toString())}:[${levels}]"
            }).append('}')
        append(",\"itemExperiences\":{").append(state.inventory.items.keys.filter { it !in 150..254 }
            .joinToString(",") { itemId ->
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
            unit.intEntries().forEach { (attribute, value) ->
                state.setUnitAttribute(
                    unit.name.toIntOrNull() ?: return@forEach,
                    attribute,
                    value
                )
            }
        }
        root.get("names").children()
            .forEach { entry -> entry.name.toIntOrNull()?.let { state.unitNames[it] = entry.asString() } }
        root.get("magic").children().forEach { entry ->
            val unitId = entry.getInt("u", -1)
            val magicId = entry.getInt("m", -1)
            if (unitId >= 0 && magicId >= 0) state.extraMagic[unitId to magicId] =
                CampaignMagic(unitId, magicId, entry.getInt("l", 1), entry.getString("i", "기본 설명"))
        }
        root.get("talents").children().forEach { entry ->
            val unitId = entry.getInt("u", -1)
            val slot = entry.getInt("s", -1)
            if (unitId >= 0 && slot >= 0) state.talents[unitId to slot] =
                CampaignTalent(unitId, slot, entry.getInt("e", 0), entry.getString("i", ""))
        }
        root.get("formations").children().forEach { state.formationTalents += it.asString() }
        state.inventory.restoreDiscoveredTreasures(
            root.get("treasures")?.children()?.map { it.asInt() }?.asIterable() ?: emptyList(),
        )
        val persistedLevels = root.get("itemLevels")
        val persistedExperiences = root.get("itemExperiences")
        root.get("items").intEntries().forEach { (itemId, count) ->
            val levels = persistedLevels?.get(itemId.toString())?.children()?.map { it.asInt() }?.toList().orEmpty()
            val experiences =
                persistedExperiences?.get(itemId.toString())?.children()?.map { it.asInt() }?.toList().orEmpty()
            if (itemId in 150..254) state.inventory.addItem(itemId, count)
            else {
                levels.take(count).forEachIndexed { index, level ->
                    state.inventory.addItem(
                        itemId,
                        level = level,
                        experience = experiences.getOrElse(index) { 0 })
                }
                repeat((count - levels.size).coerceAtLeast(0)) { state.inventory.addItem(itemId) }
            }
        }
        state.roster.restoreBattleRoster(root.get("roster").children().map { it.asInt() }.asIterable())
        root.get("equipment").children().forEach { entry ->
            val values = entry.children().map { it.asInt() }.toList()
            entry.name.toIntOrNull()?.takeIf { values.size >= 5 }?.let { unitId ->
                state.inventory.setEquipment(
                    unitId,
                    CampaignEquipment(
                        values[0],
                        values[1],
                        values[2],
                        values[3],
                        values[4],
                        values.getOrElse(5) { 0 },
                        values.getOrElse(6) { 0 }),
                )
            }
        }
        root.get("ending")?.asInt()?.let { state.applyInfoTransfer(22, it.toString()) }
    }

    private fun intMap(values: Map<Int, Int>): String =
        values.entries.joinToString(",", "{", "}") { (key, value) -> "${quote(key.toString())}:$value" }

    private fun com.badlogic.gdx.utils.JsonValue?.children(): Sequence<com.badlogic.gdx.utils.JsonValue> = sequence {
        var value = this@children?.child
        while (value != null) {
            yield(value); value = value.next
        }
    }

    private fun com.badlogic.gdx.utils.JsonValue?.intEntries(): List<Pair<Int, Int>> = children().mapNotNull { entry ->
        entry.name.toIntOrNull()?.let { id -> id to entry.asInt() }
    }.toList()

    private fun quote(value: String): String = buildString {
        append('"')
        value.forEach { char ->
            when (char) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> append(char)
            }
        }
        append('"')
    }

    private companion object {
        const val KEY = "CAMPAIGN_STATE"
        const val SLOT_KEY_PREFIX = "save-slot-"
        const val SAVE_PAGE_KEY = "SAVE_PAGE"
    }
}
