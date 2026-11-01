package com.jojo.game.domain.rules

/** A bounded integer resource owned by a game extension. */
data class ExtensionResourceDeclaration(
    val namespace: String,
    val key: String,
    val defaultValue: Int,
    val minValue: Int,
    val maxValue: Int,
    val perCharacter: Boolean = false,
) {
    val resourceKey: String get() = "$namespace.$key"

    init {
        require(namespace.matches(IDENTIFIER) && key.matches(IDENTIFIER)) { "Invalid extension resource key: $resourceKey" }
        require(minValue <= defaultValue && defaultValue <= maxValue) { "Default is outside resource bounds: $resourceKey" }
    }

    private companion object {
        val IDENTIFIER = Regex("[A-Za-z][A-Za-z0-9_-]*")
    }
}

/** Explicit values only; omitted values take the declaration's default. Character IDs are stable roster IDs. */
data class ExtensionStateSnapshot(
    val version: Int = ExtensionState.VERSION,
    val global: Map<String, Int> = emptyMap(),
    val characters: Map<Int, Map<String, Int>> = emptyMap(),
    val randomState: Int = 1,
)

/** Validates all writes and restores before replacing extension-owned campaign state. */
class ExtensionState(declarations: Iterable<ExtensionResourceDeclaration> = emptyList()) {
    private val resources = linkedMapOf<String, ExtensionResourceDeclaration>()
    private val globalValues = linkedMapOf<String, Int>()
    private val characterValues = linkedMapOf<Int, MutableMap<String, Int>>()
    private var randomState = 1

    init { declarations.forEach(::declare) }

    fun declare(declaration: ExtensionResourceDeclaration) {
        val old = resources[declaration.resourceKey]
        require(old == null || old == declaration) { "Conflicting extension resource: ${declaration.resourceKey}" }
        resources[declaration.resourceKey] = declaration
    }

    fun declarations(): List<ExtensionResourceDeclaration> = resources.values.toList()

    /** Advance the saved extension random stream once and return a value in 0 until bound. */
    fun nextRandomInt(bound: Int): Int {
        require(bound > 0) { "Random bound must be positive" }
        randomState = ((randomState.toLong() * 48_271L) % 2_147_483_647L).toInt()
        return randomState % bound
    }

    fun read(resourceKey: String, characterId: Int? = null): Int {
        val declaration = checkedResource(resourceKey, characterId)
        return if (characterId == null) globalValues[resourceKey] ?: declaration.defaultValue
        else characterValues[characterId]?.get(resourceKey) ?: declaration.defaultValue
    }

    fun write(resourceKey: String, value: Int, characterId: Int? = null) {
        val declaration = checkedResource(resourceKey, characterId)
        require(value in declaration.minValue..declaration.maxValue) { "Extension resource $resourceKey is outside declared bounds" }
        if (characterId == null) globalValues[resourceKey] = value
        else characterValues.getOrPut(characterId) { linkedMapOf() }[resourceKey] = value
    }

    fun snapshot(): ExtensionStateSnapshot = ExtensionStateSnapshot(
        global = globalValues.toMap(),
        characters = characterValues.mapValues { it.value.toMap() },
        randomState = randomState,
    )

    fun validate(snapshot: ExtensionStateSnapshot) {
        require(snapshot.version == VERSION) { "Unsupported extension state version ${snapshot.version}" }
        require(snapshot.randomState in 1..2_147_483_646) { "Invalid extension random state" }
        snapshot.global.forEach { (key, value) -> validateValue(key, value, null) }
        snapshot.characters.forEach { (characterId, values) ->
            require(characterId >= 0) { "Invalid character ID $characterId" }
            values.forEach { (key, value) -> validateValue(key, value, characterId) }
        }
    }

    fun restore(snapshot: ExtensionStateSnapshot) {
        validate(snapshot)
        globalValues.clear()
        globalValues.putAll(snapshot.global)
        characterValues.clear()
        snapshot.characters.forEach { (id, values) -> characterValues[id] = values.toMutableMap() }
        randomState = snapshot.randomState
    }

    fun reset() {
        globalValues.clear()
        characterValues.clear()
        randomState = 1
    }

    private fun validateValue(resourceKey: String, value: Int, characterId: Int?) {
        val declaration = checkedResource(resourceKey, characterId)
        require(value in declaration.minValue..declaration.maxValue) { "Extension resource $resourceKey is outside declared bounds" }
    }

    private fun checkedResource(resourceKey: String, characterId: Int?): ExtensionResourceDeclaration {
        val declaration = resources[resourceKey] ?: throw IllegalArgumentException("Unknown extension resource $resourceKey")
        require(declaration.perCharacter == (characterId != null)) { "Wrong scope for extension resource $resourceKey" }
        require(characterId == null || characterId >= 0) { "Invalid character ID $characterId" }
        return declaration
    }

    companion object { const val VERSION = 1 }
}
