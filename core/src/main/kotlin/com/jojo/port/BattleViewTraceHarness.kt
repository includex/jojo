package com.jojo.port

import java.nio.file.Files
import java.nio.file.Path

/** Canonical counterpart of tools/battle_view_source_trace_harness.js. */
object BattleViewTraceHarness {
    private fun block(s:String, at:Int):String { val open=s[at];val close=if(open=='{')'}' else ']';var d=0;var q=false;var esc=false;for(i in at until s.length){val c=s[i];if(q){if(!esc&&c=='"')q=false;esc=!esc&&c=='\\';continue};if(c=='"')q=true else if(c==open)d++ else if(c==close&&--d==0)return s.substring(at,i+1)};error("unclosed") }
    private fun objects(s:String):List<String>{val r=mutableListOf<String>();var i=0;while(i<s.length){if(s[i]=='{'){val b=block(s,i);r+=b;i+=b.length}else i++};return r}
    private fun str(s:String,k:String)=Regex("\\\"$k\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"").find(s)!!.groupValues[1]
    private fun int(s:String,k:String)=Regex("\\\"$k\\\"\\s*:\\s*(-?\\d+)").find(s)!!.groupValues[1].toInt()
    private fun arr(s:String,k:String)=block(s,s.indexOf('[',s.indexOf("\"$k\"")))
    private fun strings(s:String)=Regex("\\\"([^\\\"]+)\\\"").findAll(s).map{it.groupValues[1]}.toList()
    private fun q(s:String)="\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\""
    @JvmStatic fun main(a:Array<String>) {
        val raw=Files.readString(Path.of(a[0]));val cases=objects(arr(raw,"cases"));
        val result=cases.joinToString(",","{","}"){c-> val n=str(c,"name"); q(n)+":"+when(str(c,"kind")){"view"->view(c);"fight"->fight(c);else->dule(c)}}
        Files.createDirectories(Path.of(a[1]).parent);Files.writeString(Path.of(a[1]),result);println(result)
    }
    private fun view(c:String):String {val x=BattleViewLayer();val ps=Regex("\\[(-?\\d+),(-?\\d+)]").findAll(arr(c,"pos")).map{it.groupValues[1].toInt() to it.groupValues[2].toInt()}.toList();x.onCreate(int(c,"map"),ps);fun snap(step:String):String{val m=x.markers().joinToString(",","[","]"){ "{\"x\":${it.x},\"y\":${it.y},\"label\":${q(it.label)},\"red\":${it.red ?: "null"},\"opacity\":${it.opacity ?: "null"}}"};return "{\"step\":${q(step)},\"mapPath\":${q("frame")},\"markers\":$m,\"events\":[\"BATTLE_VIEW_INIT_OVER\"]}"};val trace=mutableListOf(snap("create"));strings(arr(c,"events")).forEach{e->if(e.startsWith("unit:"))x.battleUnitN(e.substringAfter(':').toInt());trace+=snap(e)};return trace.joinToString(",","[","]")}
    private fun fight(c:String):String {val p=Regex("\\[(-?\\d+),(-?\\d+),(-?\\d+)]").findAll(c).toList();val parent=p[0].groupValues;val node=p[1].groupValues;val x=FightUnit(parent[1].toInt(),parent[2].toInt(),parent[3].toInt(),node[1].toInt(),node[2].toInt(),node[3].toInt());val e=strings(arr(c,"events"));x.create(if("sound:321" in e)321 else -1,if("sound:321" in e)"321" else "yidong",e.filter{it.startsWith("shader:")}.map{it.substringAfter(':').toInt()});x.setActionDir(int(c,"action"),"finished" in e);return "[{\"step\":\"create\",\"parent\":[${x.parentX},${x.parentY},${x.parentScaleX}],\"node\":[${x.nodeX},${x.nodeY},${x.nodeScaleX}],\"action\":${x.action},\"animation\":${q(x.animation)},\"events\":[${x.events.joinToString(","){q(it)}}]}]"}
    private fun dule(c:String):String=strings(arr(c,"events")).joinToString(",","[","]"){e->"{\"step\":${q(e)},\"events\":[],\"attached\":true}"}
}
