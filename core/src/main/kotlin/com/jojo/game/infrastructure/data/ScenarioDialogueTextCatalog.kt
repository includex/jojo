package com.jojo.game.infrastructure.data

import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/** In-memory catalog for all authored scenario dialogue strings. */
object ScenarioDialogueTextCatalog {
    private data class Catalog(
        val entries: Map<String, String>,
        val aliases: Map<String, String>,
    )

    private val catalog: Catalog by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val json = checkNotNull(javaClass.classLoader.getResourceAsStream("scenarios/dialogue-text.json")) {
            "Scenario dialogue text catalog is missing from the application resources."
        }.bufferedReader(Charsets.UTF_8).use { it.readText() }
        val root = JsonReader().parse(json)
        check(root.getString("format") == "jojo-scenario-dialogue-text/v1") {
            "Unsupported scenario dialogue text catalog format."
        }
        Catalog(
            entries = readStrings(checkNotNull(root.get("entries"))),
            aliases = readStrings(checkNotNull(root.get("aliases"))),
        )
    }

    /** Eagerly loads the catalog during application startup. Repeated calls are cheap. */
    fun load() {
        catalog.entries.size
    }

    /** Returns a dialogue by source key or a named alias using the in-memory catalog. */
    fun text(key: String): String = catalog.entries[key] ?: catalog.aliases[key]
        ?: error("Unknown scenario dialogue text key: $key")

    private fun readStrings(objectValue: JsonValue): Map<String, String> = buildMap {
        var child = objectValue.child
        while (child != null) {
            put(child.name, child.asString())
            child = child.next
        }
    }
}
