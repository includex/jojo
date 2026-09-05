package com.jojo.port

import java.nio.file.Files
import java.nio.file.Path

/** Kotlin execution half of the shared ChooseLayer/Choose2Layer/CommandLayer fixture. */
object ChoiceCommandTraceHarness {
    private data class Case(val name:String,val kind:String,val info:String,val replace:List<Pair<String,String>>,val face:Int,val mask:Int,val events:List<String>)
    private fun esc(s:String)=s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    /** Decode a JSON string fragment without collapsing a literal `\\n` into a newline. */
    private fun unesc(s:String):String { val out=StringBuilder();var i=0;while(i<s.length){if(s[i]=='\\'&&i+1<s.length){when(s[i+1]){'n'->out.append('\n');'"'->out.append('"');'\\'->out.append('\\');else->{out.append(s[i+1])}};i+=2}else{out.append(s[i]);i++}};return out.toString() }
    private fun balanced(s:String, at:Int):String { val open=s[at];val close=if(open=='{')'}' else ']';var d=0;var q=false;var e=false;for(i in at until s.length){val c=s[i];if(q){if(e)e=false else if(c=='\\')e=true else if(c=='\"')q=false}else if(c=='\"')q=true else if(c==open)d++ else if(c==close&&--d==0)return s.substring(at,i+1)};error("unclosed") }
    private fun objects(array:String):List<String>{val r=mutableListOf<String>();var i=0;while(i<array.length){if(array[i]=='{'){val x=balanced(array,i);r+=x;i+=x.length}else i++};return r}
    private fun string(obj:String,key:String):String { val m=Regex("\\\"$key\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"").find(obj)?:error(key);return unesc(m.groupValues[1]) }
    private fun int(obj:String,key:String)=Regex("\\\"$key\\\"\\s*:\\s*(-?\\d+)").find(obj)!!.groupValues[1].toInt()
    private fun block(obj:String,key:String):String {val at=obj.indexOf("\"$key\"");val p=obj.indexOfAny(charArrayOf('{','['),at);return balanced(obj,p)}
    private fun parse(raw:String):List<Case> = objects(block(raw,"cases")).map { obj ->
        val replace=Regex("\\\"((?:\\\\.|[^\\\"])*)\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"").findAll(block(obj,"replace")).map{unesc(it.groupValues[1]) to unesc(it.groupValues[2])}.toList()
        val events=Regex("\\\"((?:\\\\.|[^\\\"])*)\\\"").findAll(block(obj,"events")).map{unesc(it.groupValues[1])}.toList()
        Case(string(obj,"name"),string(obj,"kind"),string(obj,"info"),replace,int(obj,"face"),int(obj,"mask"),events)
    }
    @JvmStatic fun main(args:Array<String>) {
        fun run(c:Case):String {
            val calls=mutableListOf<Int>(); var removals=0
            val choice=if(c.kind=="command") null else ChoiceLayer(c.kind=="choose2")
            val command=if(c.kind=="command") CommandLayer() else null
            val replaced=c.replace.fold(c.info){a,(from,to)->a.replace(from,to)}
            if(choice!=null) choice.onCreate(replaced,c.face){ calls+=it;removals++ } else command!!.onCreate(c.mask){calls+=it;removals++}
            fun snap(step:String):String {
                val rows=if(choice!=null) choice.rows().joinToString(",","[","]"){ "[${it.tag},\"${esc(it.text)}\",${it.listenerPriority}]" } else command!!.buttons().joinToString(",","[","]"){ "[${it.tag},${it.interactable},${it.priority},${it.tag<5&&!it.interactable}]" }
                val face=if(c.kind=="choose") { val f=choice!!.requestedFace; "{\"request\":${if(f==null)"[]" else "[\"head/$f\"]"},\"size\":${if(f==null)"[0,0]" else "[60,120]"}}" } else "null"
                val attached=choice?.attached() ?: command!!.attached()
                val callsJson=calls.joinToString(",","[","]")
                return "{\"step\":\"${esc(step)}\",\"zIndex\":${choice?.zIndex?:0},\"rows\":$rows,\"face\":$face,\"attached\":$attached,\"removeCount\":$removals,\"calls\":$callsJson,\"cancelPriority\":${if(command==null)"null" else "2"},\"keyboardBindings\":0}"
            }
            val trace=mutableListOf(snap("create"));c.events.forEach { event -> val p=event.split(':');when(p[0]) {"row"->choice!!.onRowTouch(p[1].toInt(),p[2].toInt());"button"->command!!.onButtonTouch(p[1].toInt(),p[2].toInt());"cancel"->command!!.onCancelTouch(p[1].toInt())};trace+=snap(event) }
            return trace.joinToString(",","[","]")
        }
        val result=parse(Files.readString(Path.of(args[0]))).joinToString(",","{","}"){"\"${esc(it.name)}\":${run(it)}"};Files.createDirectories(Path.of(args[1]).parent);Files.writeString(Path.of(args[1]),result);println(result)
    }
}
