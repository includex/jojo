// Infrastructure
package com.jojo.game.infrastructure.data

import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.domain.campaign.CampaignMagic
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.campaign.CampaignTalent
import java.util.Base64

/** 캠페인 저장 데이터에 포함되는 런타임 상태를 직렬화하고 복원한다. */
internal class CampaignRuntimeStateCodec(private val state: CampaignState) {
    /** 현재 캠페인 런타임 상태를 Base64 JSON으로 인코딩한다. */
    fun encode(): String = Base64.getEncoder().encodeToString(runtimeJson().toByteArray(Charsets.UTF_8))

    /** 손상되거나 형식이 다른 입력은 무시하고 저장된 런타임 상태를 적용한다. */
    fun restore(encoded: String) {
        val json = runCatching { String(Base64.getDecoder().decode(encoded), Charsets.UTF_8) }.getOrNull() ?: return
        val root = runCatching { JsonReader().parse(json) }.getOrNull() ?: return
        state.addMoney(root.getInt("money", 0))
        root.get("globals").intEntries().forEach { (id, value) -> state.globalVariables[id] = value }
        root.get("joined").children().forEach { state.joinedUnits += it.asInt() }
        root.get("attributes").children().forEach { unit ->
            unit.intEntries().forEach { (attribute, value) ->
                state.setUnitAttribute(unit.name.toIntOrNull() ?: return@forEach, attribute, value)
            }
        }
        root.get("names").children().forEach { entry ->
            entry.name.toIntOrNull()?.let { state.unitNames[it] = entry.asString() }
        }
        root.get("magic").children().forEach { entry ->
            val unitId = entry.getInt("u", -1)
            val magicId = entry.getInt("m", -1)
            if (unitId >= 0 && magicId >= 0) {
                state.extraMagic[unitId to magicId] = CampaignMagic(
                    unitId, magicId, entry.getInt("l", 1), entry.getString("i", "기본 설명"),
                )
            }
        }
        root.get("talents").children().forEach { entry ->
            val unitId = entry.getInt("u", -1)
            val slot = entry.getInt("s", -1)
            if (unitId >= 0 && slot >= 0) {
                state.talents[unitId to slot] = CampaignTalent(
                    unitId, slot, entry.getInt("e", 0), entry.getString("i", ""),
                )
            }
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
                levels.take(count).forEachIndexed { index, level ->
                    state.inventory.addItem(itemId, level = level, experience = experiences.getOrElse(index) { 0 })
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
                    com.jojo.game.domain.campaign.CampaignEquipment(
                        values[0], values[1], values[2], values[3], values[4],
                        values.getOrElse(5) { 0 }, values.getOrElse(6) { 0 },
                    ),
                )
            }
        }
        root.get("ending")?.asInt()?.let { state.applyInfoTransfer(22, it.toString()) }
    }

    /**
     * `runtimeJson`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun runtimeJson(): String = buildString {
        append('{')
        append("\"globals\":").append(intMap(state.globalVariables.mapValues { it.value.toString().toIntOrNull() ?: 0 }))
        append(",\"money\":").append(state.money)
        append(",\"joined\":[").append(state.joinedUnits.joinToString(",")).append(']')
        append(",\"attributes\":{")
        append(state.unitAttributes.entries.joinToString(",") { (unitId, attributes) ->
            "${quote(unitId.toString())}:${intMap(attributes)}"
        }).append('}')
        append(",\"names\":{").append(state.unitNames.entries.joinToString(",") { (id, name) ->
            "${quote(id.toString())}:${quote(name)}"
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
        append(",\"itemLevels\":{").append(state.inventory.items.keys.filter { it !in 150..254 }.joinToString(",") { itemId ->
            "${quote(itemId.toString())}:[${state.inventory.itemLevels(itemId).joinToString(",")}]"
        }).append('}')
        append(",\"itemExperiences\":{").append(state.inventory.items.keys.filter { it !in 150..254 }.joinToString(",") { itemId ->
            "${quote(itemId.toString())}:[${state.inventory.itemExperiences(itemId).joinToString(",")}]"
        }).append('}')
        append(",\"roster\":[").append(state.roster.battleRoster.joinToString(",")).append(']')
        append(",\"equipment\":{").append(state.inventory.equipment.entries.joinToString(",") { (unitId, item) ->
            "${quote(unitId.toString())}:[${item.weapon},${item.weaponLevel},${item.armor},${item.armorLevel},${item.auxiliary},${item.weaponExperience},${item.armorExperience}]"
        }).append('}')
        state.endingId?.let { append(",\"ending\":").append(it) }
        append('}')
    }

    /**
     * `intMap`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun intMap(values: Map<Int, Int>): String =
        values.entries.joinToString(",", "{", "}") { (key, value) -> "${quote(key.toString())}:$value" }

    /**
     * `com`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun com.badlogic.gdx.utils.JsonValue?.children(): Sequence<com.badlogic.gdx.utils.JsonValue> = sequence {
        var value = this@children?.child
        while (value != null) {
            yield(value)
            value = value.next
        }
    }

    /**
     * `com`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun com.badlogic.gdx.utils.JsonValue?.intEntries(): List<Pair<Int, Int>> = children().mapNotNull { entry ->
        entry.name.toIntOrNull()?.let { id -> id to entry.asInt() }
    }.toList()

    /**
     * `quote`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
}
