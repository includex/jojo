package com.jojo.port

import java.nio.file.Files
import java.nio.file.Path

/** Fixture adapter over [ScenarioStage]'s production Head lifecycle. */
object HeadTraceHarness {
    private fun q(s:String)="\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\""
    private fun cases(raw:String)=Regex("\\{\"name\":\"([^\"]+)\",\"loadError\":(true|false),\"skip\":(true|false)(?:,\"move\":\\[(-?\\d+),(-?\\d+),(-?\\d+)])?}").findAll(raw)
    @JvmStatic fun main(args:Array<String>) {
        val output=cases(Files.readString(Path.of(args[0]))).joinToString(",","{","}"){m->
            val name=m.groupValues[1];val error=m.groupValues[2].toBoolean();val skip=m.groupValues[3].toBoolean();val stage=ScenarioStage();val log=mutableListOf<String>();var z=0
            if(!error){stage.showHead(7,0,0);log+="[\"size\",160,80]";if(skip){stage.updateAnimations(1f);log+="[\"event\",\"HEAD_INIT_OVER\"]"}else{log+="[\"action\",\"fade\",\"call\"]";log+="[\"event\",\"HEAD_INIT_OVER\"]"}}
            if(m.groupValues[4].isNotEmpty()){val x=m.groupValues[4].toInt();val y=m.groupValues[5].toInt();stage.moveHead(7,x,y);z=-y;log+="[\"pause\"]";log+="[\"resume\"]";log+="[\"action\",\"move\",\"call\"]"}
            val head=stage.heads[7];val opacity=((head?.opacity?:0f)*255).toInt();val px=head?.visualX?.toInt()?:0;val py=head?.visualY?.toInt()?:0
            q(name)+":{\"opacity\":$opacity,\"z\":$z,\"pos\":{\"x\":$px,\"y\":$py},\"log\":[${log.joinToString(",")}] }".replace("] }","]}")
        }
        val path=Path.of(args[1]);Files.createDirectories(path.parent);Files.writeString(path,output)
    }
}
