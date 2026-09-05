package com.jojo.game

/** Game half of tools/battle_layer_source_trace_harness.js. */
object BattleScreenTraceHarness {
    private fun q(x:String)="\"$x\""
    @JvmStatic fun main(args:Array<String>) {
        // Fixture text is part of the source contract; do not erase spaces in it.
        val flat=java.nio.file.Files.readString(java.nio.file.Path.of(args[0]))
        val cases=Regex("\\{\\\"id\\\":\\\"([^\\\"]+)\\\",\\\"text\\\":\\\"([^\\\"]+)\\\",\\\"round\\\":(\\d+),(?:\\\"collocation\\\":(true|false),)?\\\"units\\\":\\[(.*?)],\\\"events\\\":\\[(.*?)]}").findAll(flat)
        val output=cases.joinToString(prefix="[",postfix="]") { m ->
            val units=Regex("\\{\\\"control\\\":(true|false),\\\"exist\\\":(true|false),\\\"acted\\\":(true|false)}")
                .findAll(m.groupValues[5]).map { u -> BattleScreenIsolatedUnit(u.groupValues[1]=="true",u.groupValues[2]=="true",u.groupValues[3]=="true") }.toList()
            val layer=BattleScreenIsolatedContract(units,m.groupValues[4]=="true",m.groupValues[3].toInt())
            val trace=mutableListOf<String>()
            fun snap(step:String) { val v=layer.view();trace+="{\"step\":${q(step)},\"paused\":${v.paused},\"modal\":${v.modal},\"action\":${v.action},\"events\":[${v.events.joinToString{q(it)}}]}" }
            snap("create")
            Regex("\\\"([^\\\"]+)\\\"").findAll(m.groupValues[6]).forEach { item ->
                val(kind,value)=item.groupValues[1].split(':')
                when(kind) { "show"->layer.showWinCondition(m.groupValues[2]); "cancel"->layer.cancel(value.toInt()); "action"->layer.nextNotOperUnit(value.toInt()) }
                snap("$kind-$value")
            }
            "{\"case\":${q(m.groupValues[1])},\"trace\":[${trace.joinToString()}]}"
        }
        java.nio.file.Files.writeString(java.nio.file.Path.of(args[1]),output);println(output)
    }
}
