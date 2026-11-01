package com.jojo.game.infrastructure.rules

import com.jojo.game.domain.rules.*
import org.luaj.vm2.*
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.BaseLib
import org.luaj.vm2.lib.DebugLib
import org.luaj.vm2.lib.VarArgFunction

/** External source is interpreted in a fresh, capability-free environment for every invocation. */
class LuaRuleHandler(
    override val id: String,
    private val source: String,
    private val sourceName: String,
) : RuleHandler {
    init {
        require(id.isNotBlank())
        require(source.toByteArray(Charsets.UTF_8).size <= MAX_SOURCE_BYTES) { "$sourceName [$id]: Lua source budget exceeded" }
        validate(emptyMap())
    }

    override fun validate(parameters: Map<String, String>) {
        guarded { module() }
    }

    override fun evaluate(context: RuleContext, parameters: Map<String, String>, random: RuleRandom): List<RuleEffect> = guarded {
        val module = module()
        val ctx = contextTable(context)
        ctx.set("random", function { args ->
            val bound = integer(args.arg1())
            require(bound > 0) { "Random bound must be positive" }
            LuaValue.valueOf(random.nextInt(bound))
        })
        val params = LuaTable()
        parameters.forEach { (key, value) -> params.set(key, value) }
        val result = module.get("evaluate").call(ctx, params).checktable()
        val keys = result.keys()
        require(keys.size <= MAX_EFFECTS) { "Lua effect budget exceeded" }
        require(keys.all { it.isint() && it.toint() in 1..keys.size }) { "Effects must be a dense array" }
        (1..keys.size).map { index -> effect(result.get(index).checktable()) }
    }

    private fun module(): LuaTable {
        val globals = Globals()
        globals.load(BaseLib())
        val allowed = setOf("assert", "error", "ipairs", "pairs", "next", "select", "tonumber", "tostring", "type")
        globals.keys().filter { it.tojstring() !in allowed }.forEach { globals.set(it, LuaValue.NIL) }
        val math = LuaTable()
        mapOf<String, (Double) -> Double>("floor" to { kotlin.math.floor(it) }, "ceil" to { kotlin.math.ceil(it) }, "abs" to { kotlin.math.abs(it) }).forEach { (name, operation) ->
            math.set(name, function { LuaValue.valueOf(operation(it.checkdouble(1))) })
        }
        math.set("min", function { args -> require(args.narg() > 0); LuaValue.valueOf((1..args.narg()).minOf { args.checkdouble(it) }) })
        math.set("max", function { args -> require(args.narg() > 0); LuaValue.valueOf((1..args.narg()).maxOf { args.checkdouble(it) }) })
        globals.set("math", math)
        val budget = object : DebugLib() {
            private var remaining = MAX_INSTRUCTIONS
            private var depth = 0
            private val frames = ArrayList<Array<out LuaValue?>>()
            override fun onInstruction(pc: Int, v: Varargs, top: Int) {
                require(--remaining >= 0) { "Lua instruction budget exceeded" }
                frames.lastOrNull()?.forEach { value ->
                    require(value == null || value.type() != LuaValue.TSTRING || value.rawlen() <= MAX_SOURCE_BYTES) { "Lua string budget exceeded" }
                }
                super.onInstruction(pc, v, top)
            }
            override fun onCall(c: LuaClosure, varargs: Varargs, stack: Array<LuaValue>) {
                require(++depth <= MAX_CALL_DEPTH) { "Lua call depth exceeded" }
                frames.add(stack)
                super.onCall(c, varargs, stack)
            }
            override fun onCall(f: LuaFunction) {
                require(++depth <= MAX_CALL_DEPTH) { "Lua call depth exceeded" }
                frames.add(emptyArray())
                super.onCall(f)
            }
            override fun onReturn() { depth--; if (frames.isNotEmpty()) frames.removeAt(frames.lastIndex); super.onReturn() }
        }
        globals.set("package", LuaTable().apply { set("loaded", LuaTable()) })
        globals.load(budget)
        globals.set("package", LuaValue.NIL)
        globals.set("debug", LuaValue.NIL)
        LuaC.install(globals)
        // Text only: no binary loader and no Lua-visible compiler or filesystem access.
        val module = globals.load(source.reader(), "@$sourceName").call().checktable()
        require(module.get("id").isnil() || (module.get("id").type() == LuaValue.TSTRING && module.get("id").tojstring() == id)) { "Lua module id must be $id" }
        require(module.get("evaluate").isfunction()) { "Lua module evaluate function missing" }
        return module
    }

    private fun contextTable(context: RuleContext): LuaTable = LuaTable().apply {
        set("event", context.event.name)
        context.combatPhase?.let { set("phase", it.name) }
        context.attackPower?.let { set("attackPower", it) }
        context.damageKind?.let { set("damageKind", it) }
        context.sourceId?.let { set("sourceId", it) }
        set("ownerId", context.ownerId ?: context.actorId)
        set("actorId", context.actorId)
        context.targetId?.let { set("targetId", it) }
        context.baseDamage?.let { set("baseDamage", it) }
        val units = LuaTable()
        context.units.forEach { (id, unit) -> units.set(id, unitTable(unit)) }
        set("units", units)
        set("actor", units.get(context.actorId))
        set("owner", units.get(context.ownerId ?: context.actorId))
        context.targetId?.let { set("target", units.get(it)) }
    }

    private fun unitTable(unit: RuleUnitSnapshot): LuaTable = LuaTable().apply {
        set("id", unit.id)
        unit.characterId?.let { set("characterId", it) }
        set("equipmentIds", LuaTable().apply { unit.equipmentIds.forEachIndexed { index, id -> set(index + 1, LuaValue.valueOf(id)) } })
        set("attack", unit.attack); set("defense", unit.defense)
        set("spirit", unit.spirit); set("level", unit.level)
        set("hitPoints", unit.hitPoints); set("maxHitPoints", unit.maxHitPoints)
        set("magicPoints", unit.magicPoints); set("maxMagicPoints", unit.maxMagicPoints)
        set("resources", LuaTable().apply { unit.resources.forEach { (key, value) -> set(key, value) } })
        set("statuses", LuaTable().apply { unit.statuses.sorted().forEachIndexed { index, status -> set(index + 1, status) } })
    }

    private fun effect(value: LuaTable): RuleEffect {
        fun text(key: String): String {
            val item = value.get(key)
            require(item.type() == LuaValue.TSTRING) { "Effect $key must be a string" }
            return item.tojstring().also { require(it.length <= MAX_STRING_LENGTH) { "Effect string budget exceeded" } }
        }
        fun amount() = integer(value.get("amount"))
        return when (val type = text("type")) {
            "AttackPowerModifier" -> RuleEffect.AttackPowerModifier(amount())
            "DamageModifier" -> RuleEffect.DamageModifier(amount())
            "HpDelta" -> RuleEffect.HpDelta(text("unitId"), amount())
            "MpDelta" -> RuleEffect.MpDelta(text("unitId"), amount())
            "ResourceDelta" -> RuleEffect.ResourceDelta(text("unitId"), text("resourceId"), amount())
            "StatusAdd" -> RuleEffect.StatusAdd(text("unitId"), text("statusId"))
            "StatusRemove" -> RuleEffect.StatusRemove(text("unitId"), text("statusId"))
            else -> error("Unknown Lua effect: $type")
        }
    }

    private fun integer(value: LuaValue): Int {
        require(value.type() == LuaValue.TNUMBER) { "Expected integer" }
        val number = value.todouble()
        require(number.isFinite() && number >= Int.MIN_VALUE && number <= Int.MAX_VALUE && number == number.toInt().toDouble()) { "Expected 32-bit integer" }
        return number.toInt()
    }

    private fun function(body: (Varargs) -> Varargs): LuaValue = object : VarArgFunction() {
        override fun invoke(args: Varargs): Varargs = body(args)
    }

    private fun <T> guarded(body: () -> T): T = try { body() } catch (error: StackOverflowError) {
        throw IllegalArgumentException("$sourceName [$id]: Lua nesting budget exceeded", error)
    } catch (error: Exception) {
        throw IllegalArgumentException("$sourceName [$id]: ${error.message}", error)
    }

    companion object {
        const val MAX_SOURCE_BYTES = 64 * 1024
        const val MAX_INSTRUCTIONS = 50_000
        const val MAX_EFFECTS = 128
        private const val MAX_CALL_DEPTH = 64
        private const val MAX_STRING_LENGTH = 256
    }
}
