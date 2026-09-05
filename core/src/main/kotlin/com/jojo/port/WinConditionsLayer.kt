package com.jojo.port
/** battle/WinConditionsLayer.js RichText and Panel_cancel contract. */
class WinConditionsLayer { data class View(val first:String,val second:String,val attached:Boolean); private var done:(()->Unit)?=null;private var v:View?=null
 fun onCreate(text:String,round:Int,onClose:()->Unit):View { done=onClose; val t=text.replaceFirst("\n","<br/>");return View("<b><color=#ff0000>승리 조건</c><br/><color=#777777>$t<br/>제한 턴 수 $round</c></b>","<b><color=#FFFFFF>승리 조건</c><br/><color=#FFFFFF>$t<br/>제한 턴 수 $round</c></b>",true).also{v=it} }
 fun view():View = v ?: View("", "", false)
 // The original registered callback has no attached guard: a direct second
 // TOUCH_END still invokes fn(), even though Cocos normally stops routing
 // events after removeFromParent.  Preserve the handler contract itself.
 fun cancel(event:Int):Boolean {if(event!=TOUCH_END)return false;done?.invoke();v=v?.copy(attached=false);return true}; companion object{const val TOUCH_END=2}}
