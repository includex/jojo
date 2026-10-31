package com.jojo.game.infrastructure.data

import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/** Original Chinese and previous Korean table text mapped to reviewed Korean. */
internal object ReviewedKoreanText {
    private val tables: JsonValue? by lazy {
        ReviewedKoreanText::class.java.classLoader.getResourceAsStream("localization/table-reviewed.json")
            ?.bufferedReader(Charsets.UTF_8)?.use { JsonReader().parse(it.readText()) }
    }
    private val translations: Map<String, String> by lazy {
        val stream = ReviewedKoreanText::class.java.classLoader
            .getResourceAsStream("localization/ko-reviewed.json")
        if (stream == null) emptyMap() else {
            val root = stream.bufferedReader(Charsets.UTF_8).use { JsonReader().parse(it.readText()) }
            buildMap {
                var child = root.child
                while (child != null) {
                    put(child.name, child.asString())
                    child = child.next
                }
            }
        }
    }

    fun text(original: String): String = translations[original] ?: original

    fun apply(root: JsonValue, fileName: String = "", path: String = ""): JsonValue {
        if (root.isString) {
            val patch = tables?.get(fileName)?.get(path)
            if (patch != null && root.asString() == patch.getString("before")) root.set(patch.getString("after"))
            else translations[root.asString()]?.let { root.set(it) }
        }
        var child = root.child
        var index = 0
        while (child != null) {
            val segment = if (root.isArray) index.toString() else child.name.replace("~", "~0").replace("/", "~1")
            apply(child, fileName, "$path/$segment")
            child = child.next
            index++
        }
        return root
    }
}
