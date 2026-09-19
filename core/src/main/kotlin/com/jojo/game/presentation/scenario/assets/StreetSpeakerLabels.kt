package com.jojo.game.presentation.scenario.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.presentation.shared.dialogue.DialogueLabelTexture

/** Canvas-generated catalog labels; textures are loaded only when a speaker is shown. */
internal class StreetSpeakerLabels {
    private data class Entry(val file: String, val nodeWidth: Float, val nodeHeight: Float)
    private val entries by lazy {
        val root = JsonReader().parse(Gdx.files.internal("street-speaker-labels/manifest.json"))
        check(root.getInt("contractVersion") == 1) { "Unsupported speaker label contract" }
        root.get("entries").associate { row ->
            row.getString("text") to Entry(row.getString("file"), row.getFloat("nodeWidth"), row.getFloat("nodeHeight"))
        }
    }
    private val textures = mutableMapOf<String, DialogueLabelTexture>()
    private val missing = mutableSetOf<String>()

    fun get(text: String): DialogueLabelTexture? {
        val entry = entries[text]
        if (entry == null) {
            if (missing.add(text)) Gdx.app.log("StreetSpeakerLabels", "Uncached speaker uses fallback font: $text")
            return null
        }
        return textures.getOrPut(text) {
            val texture = Texture(Gdx.files.internal("street-speaker-labels/${entry.file}"))
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            DialogueLabelTexture(texture, entry.nodeWidth, entry.nodeHeight)
        }
    }

    fun dispose() {
        textures.values.forEach { it.texture.dispose() }
        textures.clear()
    }
}
