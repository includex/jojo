// Infrastructure
package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.domain.campaign.CampaignState
import java.util.Base64

/** 캠페인 스냅샷과 번호가 있는 저장 슬롯의 영속화를 담당한다. */
internal class CampaignStorePersistence(
    private val preferences: Preferences,
    private val state: CampaignState,
) {
    private val runtime = CampaignRuntimeStateCodec(state)

    /** 환경설정에 저장된 캠페인 스냅샷을 읽는다. */
    fun read(): CampaignStore.Snapshot {
        val encoded = preferences.getString(KEY, "")
        if (encoded.isBlank()) return CampaignStore.Snapshot()
        val root = decodeEnvelope(encoded) ?: return CampaignStore.Snapshot()
        root.getString("runtime", "").takeIf { it.isNotBlank() }?.let(runtime::restore)
        return snapshotFrom(root)
    }

    /** 캠페인 스냅샷과 런타임 상태를 저장한다. */
    fun write(snapshot: CampaignStore.Snapshot) {
        val completed = snapshot.completed.sorted().joinToString(",") { quote(it) }
        val choices = snapshot.choices.toSortedMap().entries.joinToString(",") { "${quote(it.key)}:${quote(it.value)}" }
        val json =
            "{\"currentScenario\":${quote(snapshot.currentScenario)},\"completed\":[$completed],\"choices\":{$choices},\"stage\":${snapshot.stage},\"runtime\":${quote(runtime.encode())}}"
        val envelope = CampaignSaveCodec.encode(json)
        preferences.putString(KEY, Base64.getEncoder().encodeToString(envelope.toByteArray(Charsets.ISO_8859_1))).flush()
    }

    /** 현재 상태를 번호 슬롯 레코드로 만들고 저장한다. */
    fun saveSlot(index: Int, snapshot: CampaignStore.Snapshot): String {
        write(snapshot)
        val payload = preferences.getString(KEY, "")
        val record =
            "{\"time\":${System.currentTimeMillis()},\"name\":${quote(snapshot.currentScenario)},\"model\":{\"version\":1,\"stage\":${snapshot.stage},\"property2\":[0,${snapshot.stage}]},\"battle\":1,\"payload\":${quote(payload)}}"
        preferences.putString("$SLOT_KEY_PREFIX$index", record).flush()
        return record
    }

    /** 번호 슬롯의 원본 레코드를 읽고 비어 있으면 null을 반환한다. */
    fun loadSlot(index: Int): String? = preferences.getString("$SLOT_KEY_PREFIX$index", "").takeIf { it.isNotBlank() }

    /** 슬롯 레코드를 검증해 캠페인 스냅샷으로 복원한다. */
    fun restoreSlot(index: Int, raw: String): CampaignStore.Snapshot? {
        if (loadSlot(index) != raw) return null
        val record = runCatching { JsonReader().parse(raw) }.getOrNull() ?: return null
        val encoded = record.getString("payload", "")
        val envelope = runCatching { String(Base64.getDecoder().decode(encoded), Charsets.ISO_8859_1) }.getOrNull() ?: return null
        val root = CampaignSaveCodec.decode(envelope)?.let { runCatching { JsonReader().parse(it) }.getOrNull() } ?: return null
        val scenario = root.getString("currentScenario", "").takeIf { it.startsWith("R_") } ?: return null
        val stage = record.get("model")?.get("property2")?.get(1)?.asInt()
            ?: record.get("model")?.getInt("stage", 0) ?: 0
        state.reset()
        root.getString("runtime", "").takeIf { it.isNotBlank() }?.let(runtime::restore)
        val snapshot = snapshotFrom(root).copy(stage = stage)
        write(snapshot)
        return snapshot.copy(currentScenario = scenario)
    }

    private fun decodeEnvelope(encoded: String) = runCatching {
        val envelope = String(Base64.getDecoder().decode(encoded), Charsets.ISO_8859_1)
        CampaignSaveCodec.decode(envelope)?.let { JsonReader().parse(it) }
    }.getOrNull()

    private fun snapshotFrom(root: com.badlogic.gdx.utils.JsonValue): CampaignStore.Snapshot {
        val completed = root.get("completed").children().map { it.asString() }.toSet()
        val choices = root.get("choices").children().associate { it.name to it.asString() }
        return CampaignStore.Snapshot(root.getString("currentScenario", "R_00"), completed, choices, root.getInt("stage", 0))
    }

    private fun com.badlogic.gdx.utils.JsonValue?.children(): Sequence<com.badlogic.gdx.utils.JsonValue> = sequence {
        var value = this@children?.child
        while (value != null) {
            yield(value)
            value = value.next
        }
    }

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
    }
}
