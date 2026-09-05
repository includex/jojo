package com.jojo.port

/** Combined port trace: FightLayer callbacks + BattleUnit harm UI equivalents. */
object FightPresentationTraceHarness {
    private fun q(s:String)="\"$s\""
    @JvmStatic fun main(args:Array<String>) {
        val text=java.nio.file.Files.readString(java.nio.file.Path.of(args[0]))
        val cases=Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"mode\\\":(\\d+),\\\"critical\\\":(true|false),\\\"hp\\\":(\\d+),\\\"maxHp\\\":(\\d+),\\\"damage\\\":(\\d+),\\\"events\\\":\\[(.*?)]}").findAll(text)
        val output=cases.joinToString(prefix="[",postfix="]") { m -> run(m) }
        java.nio.file.Files.writeString(java.nio.file.Path.of(args[1]),output);println(output)
    }
    private fun run(m:MatchResult):String {
        val id=m.groupValues[1];val mode=m.groupValues[2].toInt();val critical=m.groupValues[3]=="true";val hp=m.groupValues[4].toInt();val max=m.groupValues[5].toInt();val damage=m.groupValues[6].toInt()
        val health=BattleHealthPresentation();val events=mutableListOf<String>();var hit=false;var bars=BattleHarmBar.View();val trace=mutableListOf<String>()
        fun snap(step:String) { val shown=health.shownHp("target",if(hit)1f else 0f,hp);val raw=if(!hit)listOf(null,null,null)else if(damage==0)listOf(bars.bar0,bars.bar1,bars.bar2)else listOf(bars.bar0?:0f,bars.bar1?:0f,bars.bar2?:0f);val values=raw.joinToString(prefix="[",postfix="]"){it?.toString()?:"null"};trace+="{\"step\":${q(step)},\"hpBefore\":$hp,\"hpAfter\":$shown,\"bars\":$values,\"amount\":${if(hit)q(damage.toString())else"null"},\"events\":[${events.joinToString{q(it)}}]}";events.clear() }
        events+="pause";events+="action:attacker:"+if(critical)"attackCritical" else "attack";snap("start")
        Regex("\\\"([^\\\"]+)\\\"").findAll(m.groupValues[7]).forEach { e -> when(e.groupValues[1]) {
            "hit" -> { hit=true;health.schedule("target",hp,(hp-damage).coerceAtLeast(0),1f);bars=BattleHarmBar.show(hp,max,0,1,hpAdd=-damage);val pair=when(mode){0->if(critical)"harmHeavy" to "hurt" else "harmLight" to "hurt";1->if(critical)"blockHeavy" to "guard" else "blockLight" to "guard";2->if(critical)"blockHeavy" to "block" else "blockLight" to "block";3->"miss" to "miss";else->"miss" to "evade"};events+="sound:${pair.first}";events+="pause";events+="action:defender:${pair.second}" }
            "death" -> { events+="pause";events+="action:defender:death" }
            else -> events+="resume"
        };snap(e.groupValues[1]) }
        return "{\"case\":${q(id)},\"trace\":[${trace.joinToString()}]}"
    }
}
