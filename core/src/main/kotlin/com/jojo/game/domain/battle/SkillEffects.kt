package com.jojo.game.domain.battle

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

/** Semantic battle rule bindings loaded from authored content. */
object SkillEffects {
    private val bindings: Map<String, Int> by lazy {
        val path = "maps/data/skillEffects.json"
        val bytes = SkillEffects::class.java.classLoader.getResourceAsStream(path)?.use { it.readBytes() }
            ?: Gdx.files.internal(path).readBytes()
        parse(bytes.decodeToString())
    }

    fun id(effect: String): Int = requireNotNull(bindings[effect]) { "Unknown battle skill effect: $effect" }

    fun value(skills: Map<Int, Int>, effect: String): Int? =
        skills[id(effect)]?.and(255)?.takeIf { it != 255 }

    fun active(skills: Map<Int, Int>, effect: String): Boolean = value(skills, effect) != null

    internal fun parse(json: String): Map<String, Int> {
        val root = JsonReader().parse(json)
        require(root.isObject) { "Battle skill effects must be an object" }
        val result = linkedMapOf<String, Int>()
        var entry = root.child
        while (entry != null) {
            require(entry.isNumber && entry.asInt() >= 0) { "Invalid battle skill effect: ${entry.name}" }
            require(result.put(entry.name, entry.asInt()) == null) { "Duplicate battle skill effect: ${entry.name}" }
            entry = entry.next
        }
        require(result.isNotEmpty()) { "Battle skill effects are empty" }
        return result
    }
}
