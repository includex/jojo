package com.jojo.game.presentation.scenario.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.presentation.shared.dialogue.DialogueBodySegment
import com.jojo.game.presentation.shared.dialogue.DialogueInfoLabel

/** Original InfoLayer RichText textures and independently measured prefix layouts. */
internal class InfoLabels {
    private data class Segment(val file: String, val x: Double, val y: Double)
    private data class Entry(val width: Double, val height: Double, val segments: List<Segment>)
    private val entries by lazy {
        val root = JsonReader().parse(Gdx.files.internal("info-labels/manifest.json"))
        check(root.getInt("contractVersion") == 1)
        check(root.getString("styleContract") == "info-richtext-arial40-line50-black-v1")
        root.get("entries").associate { row ->
            row.getString("text") to Entry(row.getDouble("width"), row.getDouble("height"), row.get("segments").map {
                Segment(it.getString("file"), it.getDouble("x"), it.getDouble("y"))
            })
        }
    }
    private val textures = mutableMapOf<String, Texture>()
    private val layouts = mutableMapOf<String, DialogueInfoLabel>()
    private val missing = mutableSetOf<String>()

    fun get(text: String): DialogueInfoLabel? {
        val entry = entries[text] ?: run {
            if (missing.add(text)) Gdx.app.log("InfoLabels", "Uncached InfoLayer text uses fallback font: $text")
            return null
        }
        return layouts.getOrPut(text) {
            DialogueInfoLabel(entry.width, entry.height, entry.segments.map { segment ->
                val texture = textures.getOrPut(segment.file) {
                    Texture(Gdx.files.internal("info-labels/${segment.file}")).also {
                        it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                    }
                }
                DialogueBodySegment(texture, segment.x, segment.y)
            })
        }
    }

    fun dispose() {
        textures.values.forEach(Texture::dispose)
        textures.clear()
        layouts.clear()
    }
}
