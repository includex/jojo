package com.jojo.game.domain.scenario

import com.jojo.engine.script.NamedIndexAccess

/** Content-owned name, type and optional legacy address. Names are persistent save identifiers. */
data class GlobalVariableDeclaration(
    val name: String,
    val type: String,
    val defaultValue: Any,
    val description: String,
    val legacyId: Int? = null,
) {
    init {
        require(name.matches(Regex("[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)+"))) { "Invalid global variable name: $name" }
        require(type in setOf("int", "bool")) { "Unsupported variable type: $type" }
        require(description.isNotBlank()) { "Missing description: $name" }
        require(legacyId == null || legacyId >= 0) { "Invalid legacy ID: $name" }
        validate(defaultValue)
    }

    fun validate(value: Any?): Any {
        require(if (type == "int") value is Int else value is Boolean) { "$name requires $type, got $value" }
        return value!!
    }
}

/** Named access and the legacy integer map share one value, including indexed legacy writes. */
class GlobalVariableStore(declarations: Iterable<GlobalVariableDeclaration>) : AbstractMutableMap<Int, Any?>(), NamedIndexAccess {
    val declarations = declarations.toList()
    private val byName = this.declarations.associateBy { it.name }
    private val byId = this.declarations.filter { it.legacyId != null }.associateBy { it.legacyId!! }
    private val legacy = linkedMapOf<Int, Any?>()
    private val named = linkedMapOf<String, Any>()

    init {
        require(byName.size == this.declarations.size) { "Duplicate variable name" }
        require(byId.size == this.declarations.count { it.legacyId != null }) { "Duplicate variable legacy ID" }
    }

    override val entries: MutableSet<MutableMap.MutableEntry<Int, Any?>> get() = object : AbstractMutableSet<MutableMap.MutableEntry<Int, Any?>>() {
        override val size get() = legacy.size
        override fun add(element: MutableMap.MutableEntry<Int, Any?>): Boolean = throw UnsupportedOperationException()
        override fun iterator(): MutableIterator<MutableMap.MutableEntry<Int, Any?>> {
            val source = legacy.entries.iterator()
            return object : MutableIterator<MutableMap.MutableEntry<Int, Any?>> {
                override fun hasNext() = source.hasNext()
                override fun remove() = source.remove()
                override fun next(): MutableMap.MutableEntry<Int, Any?> {
                    val entry = source.next()
                    return object : MutableMap.MutableEntry<Int, Any?> by entry {
                        override fun setValue(newValue: Any?): Any? {
                            byId[entry.key]?.let { it.validate(fromLegacy(it, newValue)) }
                            return entry.setValue(newValue)
                        }
                    }
                }
            }
        }
    }
    override fun get(key: Int): Any? = legacy[key] ?: byId[key]?.let { toLegacy(it.defaultValue) }
    override fun put(key: Int, value: Any?): Any? {
        val declaration = byId[key]
        if (declaration != null) declaration.validate(fromLegacy(declaration, value))
        return legacy.put(key, value)
    }
    override fun remove(key: Int): Any? = legacy.remove(key)
    override fun clear() { legacy.clear(); named.clear() }

    operator fun get(name: String): Any = getNamedValue(name)
    operator fun set(name: String, value: Any?) = setNamedValue(name, value)
    override fun getNamedValue(name: String): Any {
        val declaration = declaration(name)
        return declaration.legacyId?.let { id -> fromLegacy(declaration, legacy[id] ?: toLegacy(declaration.defaultValue)) }
            ?: named[name] ?: declaration.defaultValue
    }
    override fun setNamedValue(name: String, value: Any?) {
        val declaration = declaration(name)
        val checked = declaration.validate(value)
        if (declaration.legacyId != null) legacy[declaration.legacyId] = toLegacy(checked)
        else named[name] = checked
    }
    fun namedSnapshot(): Map<String, Any> = declarations.associate { it.name to getNamedValue(it.name) }
    fun legacySnapshot(): Map<Int, Any?> = legacy.filterKeys { it !in byId }

    /** Builds a validated replacement before touching live state; old saves need no named section. */
    fun validateRestore(oldValues: Map<Int, Int>, namedValues: Map<String, Any>): GlobalVariableStore {
        val candidate = GlobalVariableStore(declarations)
        oldValues.forEach { (id, value) -> candidate[id] = value }
        namedValues.forEach { (name, value) ->
            val declaration = declaration(name)
            declaration.validate(value)
            if (declaration.legacyId in oldValues) {
                require(candidate[name] == value) { "Conflicting legacy and named variable: $name" }
            }
            candidate[name] = value
        }
        return candidate
    }
    fun restore(validated: GlobalVariableStore) {
        require(declarations == validated.declarations)
        legacy.clear(); legacy.putAll(validated.legacy)
        named.clear(); named.putAll(validated.named)
    }
    private fun declaration(name: String) = requireNotNull(byName[name]) { "Undeclared global variable: $name" }
    private fun toLegacy(value: Any): Any = if (value is Boolean) if (value) 1 else 0 else value
    private fun fromLegacy(declaration: GlobalVariableDeclaration, value: Any?): Any {
        if (declaration.type == "bool") {
            require(value == 0 || value == 1) { "${declaration.name} requires legacy 0 or 1" }
            return value == 1
        }
        return declaration.validate(value)
    }
}
