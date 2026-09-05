package com.jojo.port

import com.badlogic.gdx.Preferences

/**
 * Owns every preference namespace used by one JojoGame instance.
 *
 * Automated desktop routes must exercise the real persistence callbacks without
 * reading or overwriting a player's LibGDX preferences.  Their stores therefore
 * live only for the lifetime of the Game, while an interactive Game delegates to
 * the ordinary platform Preferences implementation.
 */
internal class GamePreferenceProvider(
    private val automatedRun: Boolean,
    private val persistentStore: (String) -> Preferences,
) {
    private val stores = linkedMapOf<String, Preferences>()

    fun get(name: String): Preferences = stores.getOrPut(name) {
        if (automatedRun) InMemoryPreferences() else persistentStore(name)
    }
}

/** Minimal LibGDX Preferences implementation with process-local flush semantics. */
internal class InMemoryPreferences : Preferences {
    private val values = linkedMapOf<String, Any>()

    override fun putBoolean(key: String, value: Boolean) = apply { values[key] = value }
    override fun putInteger(key: String, value: Int) = apply { values[key] = value }
    override fun putLong(key: String, value: Long) = apply { values[key] = value }
    override fun putFloat(key: String, value: Float) = apply { values[key] = value }
    override fun putString(key: String, value: String) = apply { values[key] = value }
    override fun put(values: Map<String, *>) = apply {
        values.forEach { (key, value) ->
            require(value is Boolean || value is Int || value is Long || value is Float || value is String) {
                "Unsupported preference value for $key: ${value?.javaClass?.name ?: "null"}"
            }
            this.values[key] = value
        }
    }

    override fun getBoolean(key: String): Boolean = getBoolean(key, false)
    override fun getInteger(key: String): Int = getInteger(key, 0)
    override fun getLong(key: String): Long = getLong(key, 0L)
    override fun getFloat(key: String): Float = getFloat(key, 0f)
    override fun getString(key: String): String = getString(key, "")
    override fun getBoolean(key: String, defValue: Boolean): Boolean = values[key] as? Boolean ?: defValue
    override fun getInteger(key: String, defValue: Int): Int = values[key] as? Int ?: defValue
    override fun getLong(key: String, defValue: Long): Long = values[key] as? Long ?: defValue
    override fun getFloat(key: String, defValue: Float): Float = values[key] as? Float ?: defValue
    override fun getString(key: String, defValue: String): String = values[key] as? String ?: defValue
    override fun get(): Map<String, *> = values.toMap()
    override fun contains(key: String): Boolean = key in values
    override fun clear() = values.clear()
    override fun remove(key: String) { values.remove(key) }
    override fun flush() = Unit
}
