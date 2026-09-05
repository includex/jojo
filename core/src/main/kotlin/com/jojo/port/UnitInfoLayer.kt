package com.jojo.port

/** Behavioural port of recovered `ui/UnitInfoLayer.js`; event 2 is Cocos TOUCH_END. */
class UnitInfoLayer(
    private val units: List<Unit>, private val flag: Int = 0, private val editEnabled: Boolean = false,
    private val defaultTab: Int = 0, private val featsEnabled: Boolean = false, private val singleValueMode: Boolean = false,
) {
    data class Equipment(val name: String)
    data class Unit(
        val id:Int,val name:String,val post:String,val level:Int,val hp:Int,val maxHp:Int,val mp:Int,val maxMp:Int,
        val attack:Int,val defense:Int,val spirit:Int,val critical:Int,val morale:Int,val magic:List<String> = emptyList(),
        val mine:Boolean=true,val battleCount:Int=0,val retreatCount:Int=0,val skillIntro:String="",val unitIntro:String="",
        val equipment: List<Equipment?> = listOf(null, null, null),
    )
    enum class Route { FEATS, JIQI, EDIT, ITEM, MAGIC }
    data class RouteRequest(val route:Route,val index:Int,val value:String="")
    data class View(val index:Int,val tab:Int,val unit:Unit,val attached:Boolean,val panels:List<Boolean>,val interactable:List<Boolean>,val buttons:List<Boolean>,val values:List<Int>,val showRecord:Boolean,val magicRows:List<String>)
    private var index=0; private var tab=0; private var attached=false
    /** Original m_ud[unit_DEF_IDX]; changing a tab persists across unit switches. */
    private var persistedTab=defaultTab.takeIf { it in 0..4 } ?: 0
    private val routes=mutableListOf<RouteRequest>()
    fun onCreate(index:Int=0):View { require(units.isNotEmpty()); this.index=index; attached=true; refUnit(); return ref() }
    fun onCancel(event:Int):Boolean { if(!attached||event!=TOUCH_END)return false; attached=false;return true }
    fun onButton(button:Int,event:Int):Boolean {
        if(!attached||event!=TOUCH_END||button !in 0 until buttonCount())return false
        when(button){ in 0..4->{tab=button;persistedTab=button};5->{index--;refUnit()};6->{index++;refUnit()};7->attached=false
            8->if(current().mine&&featsEnabled)routes+=RouteRequest(Route.FEATS,button)
            9->if(isBattle())routes+=RouteRequest(Route.JIQI,button)
            10->if(editEnabled)routes+=RouteRequest(Route.EDIT,button,if(isBattle())"battleUnit" else "unit") }
        return true
    }
    fun onEquipment(slot:Int,event:Int):Boolean { if(!attached||event!=TOUCH_END||slot !in 0..2)return false; current().equipment.getOrNull(slot)?.let{routes+=RouteRequest(Route.ITEM,slot,it.name)};return true }
    fun onMagic(row:Int,event:Int):Boolean { if(!attached||event!=TOUCH_END||row !in current().magic.indices)return false;routes+=RouteRequest(Route.MAGIC,row,current().magic[row]);return true }
    fun takeRoutes()=routes.toList().also{routes.clear()}
    fun ref():View { val u=current(); val values=listOf(u.attack,u.defense,u.spirit,u.critical,u.morale).map{if(singleValueMode)it shl 1 else it};return View(index,tab,u,attached,List(5){it==tab},List(5){it!=tab},List(buttonCount()){when(it){8->u.mine&&featsEnabled;9->isBattle();else->true}},values,!isBattle()||u.mine,u.magic) }
    private fun refUnit(){index=((index%units.size)+units.size)%units.size;tab=persistedTab}
    private fun current()=units[index]; private fun isBattle()=flag and BATTLE_FLAG!=0; private fun buttonCount()=if(editEnabled)11 else 9
    companion object { const val TOUCH_END=2; const val BATTLE_FLAG=1 }
}
