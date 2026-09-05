package com.jojo.game

/** Direct state implementations for the recovered choice/command Cocos layers. */
class ChoiceLayer(private val plainNewline: Boolean) {
    data class Row(val tag: Int, val text: String, val listenerPriority: Int = 1)
    private var attached = true
    private var callback: ((Int) -> Unit)? = null
    private val rows = mutableListOf<Row>()
    var zIndex = 0; private set
    /**
     * Source ChooseLayer only loads the portrait when face != -1.  The image
     * itself remains an engine concern; retaining this request in the game is
     * important because it changes the observable resource/lifecycle contract.
     */
    var requestedFace: Int? = null; private set
    fun onCreate(info: String, face: Int, fn: (Int)->Unit) {
        attached = true
        rows.clear()
        zIndex=100 // Config.Z_INDEX.CHOICE_LAYER.
        requestedFace = face.takeUnless { it == -1 }
        callback=fn
        val values=if(plainNewline) info.replace("\\n","\n").split("\n") else info.split("<br/>")
        values.forEachIndexed { index,text -> rows += Row(index+1,text) }
    }
    // The original listener does not guard against a detached layer.  A
    // direct repeated TOUCH_END therefore removes/calls again as well.
    fun onRowTouch(tag: Int, event: Int) { if(event==2) { attached=false; callback?.invoke(tag) } }
    fun rows()=rows.toList(); fun attached()=attached
}

class CommandLayer {
    data class Button(val tag:Int,val interactable:Boolean,val priority:Int)
    private var attached=true; private var callback:((Int)->Unit)?=null
    private val buttons=mutableListOf<Button>()
    fun onCreate(enabledMask:Int, fn:(Int)->Unit) { attached=true; callback=fn; buttons.clear(); repeat(7){ i->buttons += Button(i, i>=5 || (enabledMask and (1 shl i))!=0,1) } }
    // Same direct-listener semantics as CommandLayer.js: no attached check.
    fun onButtonTouch(tag:Int,event:Int) { if(event==2) { attached=false; callback?.invoke(tag) } }
    fun onCancelTouch(event:Int) { if(event==2) { attached=false; callback?.invoke(6) } }
    fun buttons()=buttons.toList();fun attached()=attached
}
