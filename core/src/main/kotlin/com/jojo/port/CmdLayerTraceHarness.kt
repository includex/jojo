package com.jojo.port

import java.nio.file.Files
import java.nio.file.Path

/** Kotlin execution half of the recovered CmdLayer feature/store side-effect fixture. */
object CmdLayerTraceHarness {
    private data class Case(val name:String,val rFlag:Int,val eFlag:Int,val deviceId:String,val units:Int,val inventory:List<CmdLayer.Item>,val events:List<String>)
    private fun block(s:String, at:Int):String { val open=s[at];val close=if(open=='{')'}' else ']';var depth=0;var quote=false;var escaped=false;for(i in at until s.length){val c=s[i];if(quote){if(escaped)escaped=false else if(c=='\\')escaped=true else if(c=='"')quote=false}else if(c=='"')quote=true else if(c==open)depth++ else if(c==close&&--depth==0)return s.substring(at,i+1)};error("unclosed") }
    private fun objs(a:String):List<String>{val result=mutableListOf<String>();var i=0;while(i<a.length){if(a[i]=='{'){val x=block(a,i);result+=x;i+=x.length}else i++};return result}
    private fun str(s:String,key:String)=Regex("\\\"$key\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"").find(s)!!.groupValues[1].replace("\\\"","\"").replace("\\n","\n").replace("\\\\","\\")
    private fun int(s:String,key:String)=Regex("\\\"$key\\\"\\s*:\\s*(-?\\d+)").find(s)!!.groupValues[1].toInt()
    private fun field(s:String,key:String):String {val p=s.indexOf("\"$key\"");val a=s.indexOfAny(charArrayOf('[','{'),p);return block(s,a)}
    private fun parse(raw:String):List<Case> = objs(field(raw,"cases")).map { o ->
        val inv=objs(field(o,"inventory")).map { CmdLayer.Item(int(it,"id"), Regex("\\\"treasure\\\"\\s*:\\s*true").containsMatchIn(it), Regex("\\\"property\\\"\\s*:\\s*true").containsMatchIn(it)) }
        val events=Regex("\\\"((?:\\\\.|[^\\\"])*)\\\"").findAll(field(o,"events")).map { it.groupValues[1] }.toList()
        Case(str(o,"name"),int(o,"rFlag"),int(o,"eFlag"),str(o,"deviceId"),objs(field(o,"units")).size,inv,events)
    }
    private fun q(s:String)=buildString { append('"');s.forEach { when(it){'\\'->append("\\\\");'"'->append("\\\"");'\n'->append("\\n");else->append(it)} };append('"') }
    private fun any(x:Any?):String=when(x){null->"null";is String->q(x);is Boolean,is Int,is Long->x.toString();is Double->if(x%1.0==0.0)x.toInt().toString() else x.toString();is List<*>->x.joinToString(",","[","]"){any(it)};is Map<*,*>->x.entries.joinToString(",","{","}"){q(it.key.toString())+":"+any(it.value)};else->q(x.toString())}
    @JvmStatic fun main(args:Array<String>) {
        fun snap(l:CmdLayer,step:String):String {
            val fields=linkedMapOf<String,Any?>(
                "step" to step,"eFlag" to l.eFlag,"rFlag" to l.rFlag,"sFlag" to l.sFlag,"label" to l.label,
                "selected" to l.selected,"checked" to l.checked,"buttons" to listOf(true,true,true,true,true),"toasts" to l.toasts,"writes" to l.writes,
                "props" to l.props,"weapons" to l.weapons,"urls" to l.urls,"dispatch" to l.dispatch.map { listOf(it[0],it[1]) },
                "layers" to l.layers.map { linkedMapOf("layer" to it.layer,"args" to linkedMapOf("flag" to it.flag,"txt" to it.txt)) },"events" to l.events,"restart" to l.restart)
            return any(fields)
        }
        val result=linkedMapOf<String,String>()
        parse(Files.readString(Path.of(args[0]))).forEach { c ->
            val l=CmdLayer(c.rFlag,c.eFlag,c.deviceId,c.units,c.inventory);l.onCreate();val trace=mutableListOf(snap(l,"create"))
            c.events.forEach { e -> val p=e.split(':');when(p[0]){"item"->l.item(p[1].toInt(),p[2].toInt());"button"->l.button(p[1].toInt(),p[2].toInt());"prompt","cancelPrompt"->l.answer(p[1].toInt())};trace+=snap(l,e) }
            result[c.name]=trace.joinToString(",","[","]")
        }
        val output=result.entries.joinToString(",","{","}"){q(it.key)+":"+it.value};Files.createDirectories(Path.of(args[1]).parent);Files.writeString(Path.of(args[1]),output);println(output)
    }
}
