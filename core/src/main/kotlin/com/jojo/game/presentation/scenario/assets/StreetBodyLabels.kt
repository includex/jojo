package com.jojo.game.presentation.scenario.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.presentation.shared.dialogue.DialogueBodySegment

/** Source RichText layouts for independently rendered visible prefixes. */
internal class StreetBodyLabels {
    private data class Segment(val file: String, val x: Double, val y: Double)
    private val entries by lazy {
        val root = JsonReader().parse(Gdx.files.internal("street-body-labels/manifest.json"))
        check(root.getInt("contractVersion") == 1) { "Unsupported body label contract" }
        check(root.getString("styleContract") == "street-richtext-arial36-line42-black-v1") {
            "Unexpected street RichText style"
        }
        root.get("entries").associate { row ->
            check(row.getFloat("width") == 728f) { "Unexpected street RichText width" }
            row.getString("text") to row.get("segments").map { segment ->
                Segment(segment.getString("file"), segment.getDouble("x"), segment.getDouble("y"))
            }
        }
    }
    private val textures = mutableMapOf<String, Texture>()
    private val layouts = mutableMapOf<String, List<DialogueBodySegment>>()
    private val missing = mutableSetOf<String>()

    fun get(text: String): List<DialogueBodySegment>? {
        if (text.isEmpty()) return emptyList()
        val entry = entries[text]
        if (entry == null) {
            if (missing.add(text)) Gdx.app.log("StreetBodyLabels", "Uncached body prefix uses fallback font: $text")
            return null
        }
        return layouts.getOrPut(text) {
            entry.map { segment ->
                val texture = textures.getOrPut(segment.file) {
                    Texture(Gdx.files.internal("street-body-labels/${segment.file}")).also {
                        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                    }
                }
                DialogueBodySegment(texture, segment.x, segment.y)
            }
        }
    }

    fun dispose() {
        textures.values.forEach(Texture::dispose)
        textures.clear()
        layouts.clear()
    }
}
