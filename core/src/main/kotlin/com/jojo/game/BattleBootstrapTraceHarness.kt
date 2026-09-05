package com.jojo.game

import java.nio.file.Files
import java.nio.file.Path

/** Event adapter over [BattleSceneCoordinator]; constants/registry are source inventory, not game output. */
object BattleBootstrapTraceHarness {
    private fun q(s:String)="\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\""
    @JvmStatic fun main(args:Array<String>) {
        val raw=Files.readString(Path.of(args[0]))
        val cases=Regex("\"name\":\"([^\"]+)\",\"events\":\\[(.*?)]").findAll(raw)
        val json=cases.joinToString(",","{","}"){caseMatch->
            val name=caseMatch.groupValues[1]
            val events=Regex("\"([^\"]+)\"").findAll(caseMatch.groupValues[2]).map{it.groupValues[1]}.toList()
            val resources=mutableListOf<String>();val layers=mutableListOf<String>();val log=mutableListOf<String>();val saves=mutableListOf<String>();val callbacks=mutableListOf<String>()
            val battleLayer=object:BattleSceneCoordinator.BattleScreen {
                override fun save(out:MutableMap<String,Any?>){out["battleSaved"]=true;log+="battle.save"}
                override fun filterUnits(flag:Int):List<Any?> = if(flag==1187)listOf("m1","m2") else listOf("e1")
            }
            val factory=object:BattleSceneCoordinator.Factory {
                override fun addBattleScreen(data:Any?):BattleSceneCoordinator.BattleScreen {layers+="{\"id\":[1,\"Battle/scene/BattleScreen\"],\"args\":{\"ms\":null,\"es\":null,\"flag\":null}}";return battleLayer}
                override fun addForcesList(mine:List<Any?>,enemy:List<Any?>,flag:Int){layers+="{\"id\":\"ForcesListLayer\",\"args\":{\"ms\":[${mine.joinToString(","){q(it.toString())}}],\"es\":[${enemy.joinToString(","){q(it.toString())}}],\"flag\":$flag}}"}
                override fun stringify(value:Map<String,Any?>)="{\"battleSaved\":true,\"model\":{\"modelSaved\":true}}"
            }
            val scene=BattleSceneCoordinator(factory,{out->out["modelSaved"]=true;saves+="model.save"},{index,payload->log+="manager.saveGame:$index:$payload"},"prefab:battle","prefab:init","prefab:mini","prefab:notice")
            scene.onCreate(mapOf("scenario" to 1))
            events.forEach{event->val kind=event.substringBefore(':');val value=event.substringAfter(':');when(kind){
                "resource"->{val layer=when(value){"Battle/scene/BattleScreen"->BattleSceneCoordinator.Layer.BATTLE_LAYER;"Battle/scene/BattleInitLayer"->BattleSceneCoordinator.Layer.BATTLE_INIT_LAYER;"Battle/scene/MiniMapLayer"->BattleSceneCoordinator.Layer.MINI_MAP_LAYER;"Battle/scene/NoticeInfoLayer"->BattleSceneCoordinator.Layer.NOTICE_INFO_LAYER;else->null};val resource=layer?.let(scene::getResource);resources+="[${q(value)},${resource?.let{q(it.toString())}?:"null"}]"}
                "save"->scene.saveGame(BattleSceneCoordinator.SaveRequest(value.toInt()){callbacks+="save.callback"})
                "forces"->scene.showCharacterList()
            }}
            q(name)+":{\"resources\":[${resources.joinToString(",")}],\"layers\":[${layers.joinToString(",")}],\"log\":[${log.joinToString(",",transform=::q)}],\"modelSaves\":[${saves.joinToString(",",transform=::q)}],\"callbacks\":[${callbacks.joinToString(",",transform=::q)}]}"
        }
        val out=Path.of(args[1]);Files.createDirectories(out.parent);Files.writeString(out,json)
    }
}
