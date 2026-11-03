package com.jojo.game.presentation.battle.overlay

/** The same compact cards drive both magic-list rendering and selection. */
object BattlePickerGeometry {
    data class Rect(val x:Float,val y:Float,val width:Float,val height:Float) {
        fun contains(px:Float,py:Float)=px in x..x+width && py in y..y+height
    }
    fun magicCard(index:Int):Rect {
        require(index in 0..9)
        return Rect(480.186f+index%2*264f,551f-index/2*94f,262f,88f)
    }
    fun magicAt(x:Float,y:Float,count:Int):Int? =
        (0 until count.coerceIn(0,10)).firstOrNull { magicCard(it).contains(x,y) }
}
