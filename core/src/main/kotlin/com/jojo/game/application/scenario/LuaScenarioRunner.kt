package com.jojo.game.application.scenario

import com.badlogic.gdx.utils.JsonValue
import com.jojo.engine.script.*
import com.jojo.game.domain.scenario.PlaybackState
import org.luaj.vm2.*
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*

/** Lua owns scenario control flow; game calls use the same host dispatch and completion callbacks. */
internal class LuaScenarioRunner(
    private val module: String,
    source: String,
    private val invoke: (JsonValue, ScriptFrame, String?) -> Any?,
    private val state: () -> PlaybackState,
    private val ended: () -> Boolean,
    private val choice: () -> Int?,
    bindings: Map<String, Any?>,
) {
    private val globals = Globals()
    private var thread: LuaThread? = null
    private var function = "<module>"
    private var remaining = 200_000
    init {
        require(source.toByteArray().size <= 2_000_000) { "$module: Lua source exceeds budget" }
        globals.load(BaseLib())
        globals.load(PackageLib())
        globals.load(TableLib())
        globals.load(StringLib())
        globals.load(CoroutineLib())
        globals.load(object : DebugLib() {
            override fun onInstruction(pc: Int, v: Varargs, top: Int) {
                check(--remaining >= 0) { "$module:$function: Lua instruction budget exceeded" }
                super.onInstruction(pc, v, top)
            }
        })
        listOf("dofile", "loadfile", "load", "loadstring", "collectgarbage", "print", "debug", "package", "require", "module").forEach { globals.set(it, LuaValue.NIL) }
        LuaC.install(globals)
        bindings.forEach { (name,value) -> globals.set(name, mapProxy(value)) }
        globals.set("stage", api("stage"))
        globals.set("model", api("model"))
        globals.set("label", fn { LuaValue.NONE })
        globals.load(source, "@$module.lua").call()
    }
    val functionNames: Set<String> get() = globals.keys().filter { globals.get(it).isfunction() && it.tojstring().startsWith("scene") || it.tojstring() in setOf("__dispatch__", "win_condition", "lose_condition") }.map { it.tojstring() }.toSet()
    fun start(name: String) {
        function = name
        val f=globals.get(name)
        require(f.isfunction()) { "$module: unknown Lua function $name" }
        thread=LuaThread(globals,f)
    }
    fun resume() {
        remaining=200_000
        val active=thread ?: return
        if (active.getStatus()=="dead") return
        val result=active.resume(LuaValue.NONE)
        check(result.arg1().toboolean()) { "$module:$function: ${result.arg(2).tojstring()}" }
    }
    private fun api(path: String, receiver: Any? = null): LuaValue = LuaTable().also { table ->
        table.setmetatable(LuaTable().also { meta -> meta.set("__index", fn { args ->
            val method=args.arg(2).checkjstring()
            fn { supplied ->
                val values=(1..supplied.narg()).map { fromLua(supplied.arg(it)) }
                val frame=ScriptFrame(ScriptFunction(function,emptyList(),emptyMap()))
                val func=if(receiver==null) attr(name(path),method) else {
                    frame.locals["__receiver"]=receiver
                    // Preserve the host API's reference path while resolving its actual receiver from locals.
                    attr(name("__receiver"),method)
                }
                val arguments=values.mapIndexed { i,value -> frame.locals["__arg$i"]=value;name("__arg$i") }
                val node=call(func,arguments)
                node.addChild("location",JsonValue(JsonValue.ValueType.`object`).also { it.addChild("line",JsonValue(1L)) })
                val result=invoke(node,frame,if(receiver==null) null else "$path.$method")
                if(state()!=PlaybackState.COMPLETE || ended()) globals.yield(LuaValue.NONE)
                if(path=="stage" && method=="choice") toLua(choice()) else toLua(result)
            }
        }) })
    }
    private fun mapProxy(value: Any?): LuaValue {
        if(value !is MutableMap<*,*> && value !is NamedIndexAccess) return toLua(value)
        return LuaTable().also { table -> table.setmetatable(LuaTable().also { meta ->
            meta.set("__index",fn { args ->
                val key=fromLua(args.arg(2));toLua(if(value is NamedIndexAccess && key is String) value.getNamedValue(key) else (value as? Map<*,*>)?.get(key) ?: 0)
            })
            meta.set("__newindex",fn { args ->
                val key=fromLua(args.arg(2));val item=fromLua(args.arg(3))
                if(value is NamedIndexAccess && key is String)value.setNamedValue(key,item)
                else {
                    @Suppress("UNCHECKED_CAST")
                    val map=value as MutableMap<Any?,Any?>
                    map[key]=item
                }
                LuaValue.NONE
            })
        }) }
    }
    private fun toLua(value: Any?): LuaValue = when(value) {
        null -> LuaValue.NIL
        is Boolean -> LuaValue.valueOf(value)
        is Number -> LuaValue.valueOf(value.toDouble())
        is String -> LuaValue.valueOf(value)
        is List<*> -> LuaTable().also { t -> value.forEachIndexed { i,v -> t.set(i+1,toLua(v)) } }
        is com.jojo.game.application.scenario.ScenarioUnitReference -> LuaValue.userdataOf(value,LuaTable().also { meta ->
            meta.set("__index",fn { args -> api("stage.unit()",value).get(args.arg(2)) })
        })
        is ScenarioFightReference -> LuaValue.userdataOf(value,LuaTable().also { meta ->
            meta.set("__index",fn { args -> api("fight",value).get(args.arg(2)) })
        })
        else -> LuaValue.userdataOf(value)
    }
    private fun fromLua(value: LuaValue): Any? = when {
        value.isnil() -> null
        value.isboolean() -> value.toboolean()
        value.isint() -> value.toint()
        value.isnumber() -> value.todouble()
        value.isstring() -> value.tojstring()
        value.istable() -> { val keys=value.checktable().keys();if(keys.all { it.isint() && it.toint() in 1..keys.size }) (1..keys.size).map { fromLua(value.get(it)) }.toMutableList() else keys.associate { it.tojstring() to fromLua(value.get(it)) }.toMutableMap() }
        value.isuserdata() -> value.touserdata()
        else -> error("$module: unsupported Lua argument")
    }
    private fun fn(body: (Varargs)->Varargs)=object:VarArgFunction(){override fun invoke(args:Varargs)=body(args)}
    private fun name(id:String)=ast("Name", mapOf("id" to JsonValue(id)))
    private fun attr(value:JsonValue,id:String)=ast("Attribute",mapOf("value" to value,"attr" to JsonValue(id)))
    private fun call(func:JsonValue,args:List<JsonValue>)=ast("Call",mapOf("func" to func,"args" to JsonValue(JsonValue.ValueType.array).also { a -> args.forEach { a.addChild(it) } }))
    private fun ast(type:String,fields:Map<String,JsonValue>)=JsonValue(JsonValue.ValueType.`object`).also { n -> n.addChild("type",JsonValue(type));n.addChild("fields",JsonValue(JsonValue.ValueType.`object`).also { f -> fields.forEach { (k,v)-> f.addChild(k,v) } }) }
}
