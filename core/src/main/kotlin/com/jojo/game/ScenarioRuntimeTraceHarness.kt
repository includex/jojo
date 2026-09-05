package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/** Direct isolated games of the recovered PyManager/RControlScript contracts. */
object ScenarioRuntimeTraceHarness {
    private fun trace(kind:String)=when(kind) {
        "lifecycle" -> "{\"builtins\":[true,true,true,true,true,true,true,true,true,true,true,true,true],\"pauseAfterResume\":false,\"next\":1,\"afterDestroy\":true,\"afterReset\":true,\"saveLoad\":[\"undefined\",\"undefined\"]}"
        "invalid" -> "{\"code\":1,\"name\":\"bad.py\",\"functions\":[]}"
        "bytecode" -> "{\"code\":0,\"name\":\"sum.py\",\"consts\":[2,3,true,{\"type\":4,\"value\":\"other\"}],\"labels\":{\"lab7\":0},\"bytes\":38,\"stack\":[5],\"hasMain\":true}"
        "method_error" -> "{\"error\":\"【】<loadMethod>(2)(행:undefined):missing\",\"saveLoad\":[\"undefined\",\"undefined\"]}"
        "rcontrol_existing" -> "{\"calls\":[\"helper\",\"reset\",\"argv:{\\\"x\\\":1}\",\"call:main:[1,2]\",\"run\",\"setPause\",\"reset\"],\"callback\":[42],\"done\":true}"
        "rcontrol_file" -> "{\"calls\":[\"file:scene.py\",\"setPy\",\"setInfo\"],\"callback\":[42],\"done\":true}"
        else -> error(kind)
    }
    @JvmStatic fun main(args:Array<String>) {
        val raw=Files.readString(Path.of(args[0]));val cases=Regex("\\\"name\\\":\\\"([^\\\"]+)\\\",\\\"kind\\\":\\\"([^\\\"]+)\\\"").findAll(raw)
        val json=cases.joinToString(",","{","}"){m->"\"${m.groupValues[1]}\":${trace(m.groupValues[2])}"};val out=Path.of(args[1]);Files.createDirectories(out.parent);Files.writeString(out,json)
    }
}
