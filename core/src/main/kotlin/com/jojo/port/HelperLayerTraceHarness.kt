package com.jojo.port

import java.nio.file.Files
import java.nio.file.Path

/** Kotlin side of the recovered HelperLayer isolated source/port trace fixture. */
object HelperLayerTraceHarness {
    private data class Case(val name: String, val info: List<HelperLayer.Info>, val replacement: List<Pair<String, String>>, val events: List<String>)
    private fun json(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    private fun unjson(s: String) = s.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\")
    private fun balanced(text: String, from: Int): String { val open=text[from]; val close=if(open=='[')']' else '}';var level=0;var quoted=false;var escaped=false;for(i in from until text.length){val c=text[i];if(quoted){if(escaped)escaped=false else if(c=='\\')escaped=true else if(c=='\"')quoted=false}else if(c=='\"')quoted=true else if(c==open)level++ else if(c==close&&--level==0)return text.substring(from,i+1)};error("unclosed JSON block") }
    private fun fieldBlock(obj: String, key: String): String { val at=obj.indexOf("\"$key\"");require(at>=0);val start=obj.indexOfAny(charArrayOf('[','{'),at);return balanced(obj,start) }
    private fun splitObjects(array: String): List<String> { val out=mutableListOf<String>();var i=0;while(i<array.length){if(array[i]=='{'){val x=balanced(array,i);out+=x;i+=x.length}else i++};return out }
    @JvmStatic fun main(args: Array<String>) {
        val raw=Files.readString(Path.of(args[0]));val cases=splitObjects(fieldBlock(raw,"cases")).map { obj ->
            val name=Regex("\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").find(obj)!!.groupValues[1]
            val info=Regex("""\[\s*(\d+)\s*,\s*"([^"]*)"\s*,\s*"((?:\\.|[^"])*)"\s*]""").findAll(fieldBlock(obj,"info")).map { m -> HelperLayer.Info(m.groupValues[1].toInt(),unjson(m.groupValues[2]),unjson(m.groupValues[3])) }.toList()
            val replacement=Regex(""""((?:\\.|[^"])*)"\s*:\s*"((?:\\.|[^"])*)""").findAll(fieldBlock(obj,"replacement")).map { m -> unjson(m.groupValues[1]) to unjson(m.groupValues[2]) }.toList()
            val events=Regex("\\\"([^\\\"]+)\\\"").findAll(fieldBlock(obj,"events")).map { it.groupValues[1] }.toList()
            Case(name,info,replacement,events)
        }
        fun run(spec: Case): String { val calls=mutableListOf<Pair<String,Int>>();var removeCount=0;val layer=HelperLayer(object:HelperLayer.Model{override fun getInfo()=spec.info;override fun replaceSpeInfo(text:String,flags:Int):String{calls+=text to flags;return spec.replacement.fold(text){acc,(a,b)->acc.replace(a,b)}}}){removeCount++};layer.onCreate();fun snap(step:String):String{val v=layer.view();val callsJson=calls.joinToString(",","[","]"){ "[\"${json(it.first)}\",${it.second}]" };val routes=(0 until removeCount).joinToString(",","[","]"){ "\"removeFromParent\"" };return "{\"step\":\"${json(step)}\",\"backgrounds\":[\"${v.prefab.background}\"],\"richText\":\"${json(v.richText)}\",\"replaceCalls\":$callsJson,\"attached\":${v.attached},\"button\":{\"path\":\"${v.prefab.buttonPath}\",\"priority\":${v.prefab.listenerPriority}},\"tabs\":[],\"routes\":$routes}"};val out=mutableListOf(snap("create"));spec.events.forEach{event->val p=event.split(':');if(p[0]=="button")layer.onButtonTouch(p[1].toInt());out+=snap(event)};return out.joinToString(",","[","]")}
        val output=cases.joinToString(",","{","}"){"\"${json(it.name)}\":${run(it)}"};Files.createDirectories(Path.of(args[1]).parent);Files.writeString(Path.of(args[1]),output);println(output)
    }
}
