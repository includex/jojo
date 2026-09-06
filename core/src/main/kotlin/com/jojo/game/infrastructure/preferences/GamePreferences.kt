// Infrastructure
package com.jojo.game.infrastructure.preferences

import com.badlogic.gdx.Preferences

/** GamePreferenceProvider: JojoGame 한 인스턴스가 사용하는 모든 환경설정 영역을 관리한다. 자동화 실행은 게임 수명 동안의 메모리 저장소를 사용하고, 대화형 실행은 플랫폼 환경설정에 위임한다. */
internal class GamePreferenceProvider(
    private val automatedRun: Boolean,
    private val persistentStore: (String) -> Preferences,
) {
    private val stores = linkedMapOf<String, Preferences>()


    fun get(name: String): Preferences = stores.getOrPut(name) {
        if (automatedRun) InMemoryPreferences() else persistentStore(name)
    }


    fun campaign(): Preferences = get(GamePreferenceNamespaces.CAMPAIGN)


    fun settings(): Preferences = get(GamePreferenceNamespaces.SETTINGS)
}

internal object GamePreferenceNamespaces {
    const val CAMPAIGN = "jojo-game-campaign"
    const val SETTINGS = "jojo-game-settings"
}

/** InMemoryPreferences: 프로세스 범위의 저장 동작을 제공하는 간단한 환경설정 구현체이다. */
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
    override fun remove(key: String) {
        values.remove(key)
    }

    override fun flush() = Unit
}
