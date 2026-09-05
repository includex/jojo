package com.jojo.port

/** Exact state/event port of battle/BattleInitLayer.js; prefab owns its animation. */
class BattleInitLayer(private val effects: Effects = Effects.NONE) {
    interface Effects { fun playInitBattle(); fun stopAllEffects(); companion object { val NONE=object:Effects{override fun playInitBattle()=Unit;override fun stopAllEffects()=Unit} } }
    data class View(val flag:Int,val attached:Boolean,val labels:List<String>)
    private var flag=0; private var attached=false; private var labels=listOf("","")
    fun onCreate(value:Int):View { flag=value;attached=true;effects.playInitBattle();return view() }
    /** BATTLE_LOAD_BGMAP listener updates both prefab labels. */
    fun onLoadBgMap(name:String):View { labels=List(2){name+if(flag and 1!=0)" ▪ 훈련" else ""};return view() }
    fun onDestroy(){attached=false;effects.stopAllEffects()}; fun view()=View(flag,attached,labels)
}
