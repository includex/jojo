// Infrastructure
package com.jojo.game.infrastructure.preferences

import com.badlogic.gdx.Preferences

/** GamePreferenceProvider: JojoGame 한 인스턴스가 사용하는 모든 환경설정 영역을 관리한다. 자동화 실행은 게임 수명 동안의 메모리 저장소를 사용하고, 대화형 실행은 플랫폼 환경설정에 위임한다. */
internal class GamePreferenceProvider(
    /**
     * `automatedRun` (Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val automatedRun: Boolean,
    /**
     * `persistentStore` ((String) -> Preferences,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val persistentStore: (String) -> Preferences,
) {
    /**
     * `stores` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val stores = linkedMapOf<String, Preferences>()


    /**
     * `get`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun get(name: String): Preferences = stores.getOrPut(name) {
        if (automatedRun) InMemoryPreferences() else persistentStore(name)
    }


    /**
     * `campaign`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun campaign(): Preferences = get(GamePreferenceNamespaces.CAMPAIGN)


    /**
     * `settings`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun settings(): Preferences = get(GamePreferenceNamespaces.SETTINGS)
}

/**
 * `GamePreferenceNamespaces` 싱글턴 객체: preferences 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object GamePreferenceNamespaces {
    /**
     * `CAMPAIGN` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val CAMPAIGN = "jojo-game-campaign"
    /**
     * `SETTINGS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val SETTINGS = "jojo-game-settings"
}

/** InMemoryPreferences: 프로세스 범위의 저장 동작을 제공하는 간단한 환경설정 구현체이다. */
internal class InMemoryPreferences : Preferences {
    /**
     * `values` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val values = linkedMapOf<String, Any>()

    /**
     * `putBoolean`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun putBoolean(key: String, value: Boolean) = apply { values[key] = value }
    /**
     * `putInteger`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun putInteger(key: String, value: Int) = apply { values[key] = value }
    /**
     * `putLong`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun putLong(key: String, value: Long) = apply { values[key] = value }
    /**
     * `putFloat`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun putFloat(key: String, value: Float) = apply { values[key] = value }
    /**
     * `putString`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun putString(key: String, value: String) = apply { values[key] = value }
    /**
     * `put`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun put(values: Map<String, *>) = apply {
        values.forEach { (key, value) ->
            require(value is Boolean || value is Int || value is Long || value is Float || value is String) {
                "Unsupported preference value for $key: ${value?.javaClass?.name ?: "null"}"
            }
            this.values[key] = value
        }
    }

    /**
     * `getBoolean`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getBoolean(key: String): Boolean = getBoolean(key, false)
    /**
     * `getInteger`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getInteger(key: String): Int = getInteger(key, 0)
    /**
     * `getLong`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getLong(key: String): Long = getLong(key, 0L)
    /**
     * `getFloat`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getFloat(key: String): Float = getFloat(key, 0f)
    /**
     * `getString`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getString(key: String): String = getString(key, "")
    /**
     * `getBoolean`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getBoolean(key: String, defValue: Boolean): Boolean = values[key] as? Boolean ?: defValue
    /**
     * `getInteger`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getInteger(key: String, defValue: Int): Int = values[key] as? Int ?: defValue
    /**
     * `getLong`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getLong(key: String, defValue: Long): Long = values[key] as? Long ?: defValue
    /**
     * `getFloat`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getFloat(key: String, defValue: Float): Float = values[key] as? Float ?: defValue
    /**
     * `getString`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun getString(key: String, defValue: String): String = values[key] as? String ?: defValue
    /**
     * `get`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun get(): Map<String, *> = values.toMap()
    /**
     * `contains`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun contains(key: String): Boolean = key in values
    /**
     * `clear`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun clear() = values.clear()
    /**
     * `remove`: 사용한 상태와 자원을 정리한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun remove(key: String) {
        values.remove(key)
    }

    /**
     * `flush`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun flush() = Unit
}
