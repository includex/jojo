package com.jojo.port

/** Direct state port of recovered ui/SectionLayer.js. */
class SectionLayer(private val setting:Int) {
    data class View(val label:String,val count:Int,val scheduled:List<Int>,val callbacks:Int,val attached:Boolean)
    private var name=""; private var index=0; private var count=0; private var label=""; private var callbacks=0; private var attached=true; private val scheduled=mutableListOf<Int>(); private var fn:(()->Unit)?=null
    fun onCreate(idx:Int,name:String,callback:()->Unit):View { this.index=idx;this.name=name;fn=callback;label=if(idx==0)"서막" else chapter(idx);if(setting and AUTO_CLOSE !=0)scheduled+=3;return view() }
    private fun chapter(n:Int):String { val r=listOf("십","일","2","삼","넷","다섯","육","칠","팔","구");var i=n;var v="장막";while(i>0){v=r[i%10]+v;i/=10};return "제$v" }
    fun next(event:Int):View { if(event==TOUCH_END) next();return view() }
    fun next(){count++;if(count==1)label=name else if(count==2){fn?.invoke();callbacks++;attached=false}}
    fun auto():View { next();return view() }
    fun skip():View { fn?.invoke();callbacks++;attached=false;return view() }
    fun view()=View(label,count,scheduled.toList(),callbacks,attached)
    companion object { const val TOUCH_END=2; const val AUTO_CLOSE=8 }
}
