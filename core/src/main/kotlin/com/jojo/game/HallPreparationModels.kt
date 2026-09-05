package com.jojo.game

/** Lifecycle models for HallLayer and battle preparation factories. */
class HallPreparationFlow(private val featureSkip:Boolean=false) {
    var flag=2; val layers=mutableListOf<String>(); val actions=mutableListOf<String>(); var menuVisible=false
    fun onCreate(hallFlag:Int) { flag=hallFlag; menuVisible=flag and 1 != 0; if(featureSkip)layers += "SkipLayer" }
    fun command(value:Int) { when(value){-1->layers += "HallMenuLayer";0->{actions += "startBattle";layers += "startBattle"};1->{actions += "openStore";layers += "openStore"};2->{actions += "buyIn";layers += "buyIn"};3->{actions += "sellOut";layers += "sellOut"}} }
}
class BattleInitPresentationState { var flag=0;var attached=false;var zIndex=0;var sound=0;var dispatch=0;var stopped=false;val labels=MutableList(2){""}
    fun create(v:Int){flag=v;attached=true;zIndex=1;sound++;dispatch++};fun load(name:String){repeat(2){labels[it]=name+if(flag and 1!=0)" ▪ 훈련" else ""}};fun destroy(){stopped=true}
}
class BattleSortModel { var attached=true;var pos="10,20";val tags=(0..4).toList();val calls=mutableListOf<Int>();fun button(id:Int,event:Int){if(event==2&&id in 0..4){attached=false;calls+=id}};fun cancel(event:Int){if(event==2)attached=false} }
class BattleRosterModel { val slots=mutableListOf(-1,-1,-1);val fights=linkedSetOf<Int>();var label="";var ok=true;val events=mutableListOf<Int>();fun fight(id:Int){val n=slots.indexOf(-1);if(n>=0){slots[n]=id;fights+=id;ref()}};fun cancel(id:Int){val n=slots.indexOf(id);if(n>=0){slots.removeAt(n);slots+=-1;fights-=id;ref()}};private fun ref(){label="출진 무장 - ${fights.size}/3";ok=fights.size>=2;events+=fights.size}}
class BattleDeploymentRules { val max=3;val min=2;val must=listOf(0,2);val mustJoin=listOf(0,2,0);val units=listOf(0,1,2,3);val order=listOf(0,1,2,3);val button2=true;val sort=2;val descending=true }
