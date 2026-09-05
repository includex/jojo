package com.jojo.game

import kotlin.math.abs
import kotlin.math.max

/** Production implementation of InfoBaseLayer's queued five-step numeric label animation. */
class InfoBaseValueAnimation(entries:List<Value>) {
    data class Value(val index:Int,val source:Int,val destination:Int,val max:Int)
    data class Update(val index:Int,val text:String)

    private val queue=entries.toMutableList()
    var current:Value?=queue.removeFirstOrNull()
        private set
    val values:MutableList<Int> = current?.let(::steps)?:mutableListOf()

    /**
     * Mirrors `_next`, including delivery of JavaScript `undefined` when a
     * retained scheduled callback fires after the final value was consumed.
     */
    fun callback():Update? {
        val value=current?:return null
        val text=if(values.isNotEmpty())values.removeAt(0).toString() else "undefined"
        if(values.isEmpty()&&queue.isNotEmpty()) {
            current=queue.removeFirst()
            values+=steps(requireNotNull(current))
        }
        return Update(value.index,text)
    }

    fun pending():List<Value> = queue.toList()

    private fun steps(value:Value):MutableList<Int> {
        var remaining=abs(value.source-value.destination)
        val sign=if(value.destination>value.source)1 else -1
        val step=max(remaining/5,1)
        val deltas=mutableListOf<Int>()
        var count=0
        while(count<4&&remaining>0){deltas.add(0,step*sign);remaining-=step;count++}
        if(remaining>0)deltas.add(0,remaining*sign)
        var currentValue=value.source
        return deltas.mapTo(mutableListOf()){currentValue+=it;currentValue}
    }
}
