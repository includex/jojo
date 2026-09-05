package com.jojo.game

/** Renderer-independent contract of Battle/scene/EditLayer2 (layer id 23). */
class BattleEditLayer2(
    initialWeather: Int,
    initialRound: Int,
    private val canApplyRound: Boolean,
) {
    sealed interface Effect {
        data class SetWeather(val value: Int) : Effect
        data class SetRound(val value: Int) : Effect
        data class Toast(val text: String) : Effect
        /** Instance.LAYER.EditLayer, id 120, prefab Global/scene/EditLayer3. */
        data object OpenGlobalEditor : Effect
        data class KillAll(val flag: Int) : Effect
        data object Remove : Effect
    }

    companion object {
        val weatherNames = listOf("맑음", "어두움", "바람", "비", "설")
        const val ROUND_DISABLED_TOAST = "'활성화' 미적용. 난이도가 한 단계 낮아질 때마다 최대 턴 수가 늘어남 기능"
    }

    private val original = mapOf(0 to initialWeather, 1 to initialRound)
    private val pending = linkedMapOf<Int, Int>()
    private var editChanged = false

    var weatherLabel: String = weatherNames[initialWeather]
        private set
    var roundText: String = initialRound.toString()
        private set
    var weatherPanelVisible: Boolean = false
        private set
    var removed: Boolean = false
        private set

    fun openWeatherPanel() { weatherPanelVisible = true }
    fun closeWeatherPanel() { weatherPanelVisible = false }

    fun selectWeather(value: Int) {
        require(value in weatherNames.indices)
        // Preserve the recovered source typo: selecting the original value
        // deletes `_data.key`, so an already pending weather value survives.
        if (value != original.getValue(0)) pending[0] = value
        weatherLabel = weatherNames[value]
    }

    fun textChanged(value: String) {
        roundText = value
        editChanged = true
    }

    fun editingDidEnd() {
        if (!editChanged) return
        editChanged = false
        val value = roundText.toDoubleOrNull()?.toInt() ?: 0
        if (value == original.getValue(1)) pending.remove(1) else pending[1] = value
    }

    fun touchButton(tag: Int, phase: Int = 2): List<Effect> {
        // Cocos removal detaches the node but does not invalidate a retained
        // callback reference; the recovered handler still dispatches if such
        // a callback is delivered after removal.
        if (phase != 2) return emptyList()
        return when (tag) {
            0 -> buildList {
                pending.forEach { (key, value) -> when (key) {
                    0 -> add(Effect.SetWeather(value))
                    1 -> if (canApplyRound) add(Effect.SetRound(value)) else add(Effect.Toast(ROUND_DISABLED_TOAST))
                } }
                removed = true
                add(Effect.Remove)
            }
            2 -> listOf(Effect.OpenGlobalEditor)
            3 -> listOf(Effect.KillAll(3))
            4 -> listOf(Effect.KillAll(1))
            5 -> listOf(Effect.KillAll(0))
            else -> emptyList()
        }
    }

    fun pendingValues(): Map<Int, Int> = pending.toMap()
}

enum class BattleEditLayer2Route(val key: String) {
    INITIAL("initial"), WEATHER("weather"), ROUND("round"), APPLY("apply"), CHILD("child"), CHILD_SCENE("child-scene"), REGISTER("register");

    companion object {
        fun parse(state: String?): BattleEditLayer2Route? {
            val normalized = state?.removeSuffix("-fixture") ?: return null
            if (normalized == "battle-register-open") return REGISTER
            val key = normalized.removePrefix("battle-edit2-")
            if (!normalized.startsWith("battle-edit2-")) return null
            return entries.firstOrNull { it.key == key }
        }
    }
}

/** Visible draw submissions of the actual Menu(BJ) -> EditLayer2 route. */
object BattleEditLayer2RenderEvents {
    private val alphaBlend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")

    fun jsonl(route: BattleEditLayer2Route, model: BattleEditLayer2): String {
        val log = RenderEventLog()
        val phase = if (route == BattleEditLayer2Route.REGISTER) "battle-register-open" else "battle-edit2-${route.key}"
        fun draw(layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                 asset: String? = null, text: String = "", opacity: Float = 1f, blend: Any = listOf(770, 771)) =
            log.draw(phase, layer, path, type, x, y, w, h, asset, opacity, blend, true, text)
        draw("HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>")
        if (route == BattleEditLayer2Route.APPLY) return log.jsonl()
        draw("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .314f)
        val childRoute = route == BattleEditLayer2Route.CHILD || route == BattleEditLayer2Route.CHILD_SCENE || route == BattleEditLayer2Route.REGISTER
        val overlayLayer = when (route) {
            BattleEditLayer2Route.REGISTER -> "RegisterLayer"
            BattleEditLayer2Route.CHILD, BattleEditLayer2Route.CHILD_SCENE -> "EditLayer3"
            else -> "EditLayer2"
        }
        appendEdit2(log, phase, overlayLayer, model)
        if (route == BattleEditLayer2Route.WEATHER) appendWeather(log, phase)
        if (childRoute) appendChild(log, phase, overlayLayer)
        if (route == BattleEditLayer2Route.CHILD_SCENE) appendChildScenePanel(log, phase)
        if (route == BattleEditLayer2Route.REGISTER) appendRegister(log, phase)
        return log.jsonl()
    }

    private fun appendEdit2(log: RenderEventLog, phase: String, layer: String, model: BattleEditLayer2) {
        fun d(path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String?=null,text:String="",blend:Any=listOf(770,771))=
            log.draw(phase,layer,path,type,x,y,w,h,asset,blend=blend,text=text)
        d("Canvas/Layer/bg","tiled-sprite",453.686f,195f,581f,410f,"Logo_9-1")
        d("Canvas/Layer/bg/bg1","sprite",453.686f,546.55f,581f,58.5f,"bg1")
        d("Canvas/Layer/bg/bg1/label","label",669.431f,550.6f,149.51f,50.4f,text="전장 편집",blend=alphaBlend)
        d("Canvas/Layer/bg/label","label",675.735f,488.8f,91.43f,50.4f,text="날씨: ",blend=alphaBlend)
        d("Canvas/Layer/bg/bg2","sliced-sprite",767.301f,487.229f,169.8f,50f,"box1")
        val weatherWidth=if(model.weatherLabel.length==1)34.6f else if(model.weatherLabel.length==2)69.2f else 103.8f
        d("Canvas/Layer/bg/bg2/label","label",852.201f-weatherWidth/2f,487.029f,weatherWidth,50.4f,text=model.weatherLabel,blend=alphaBlend)
        d("Canvas/Layer/bg/label","label",618.435f,432.8f,126.03f,50.4f,text="현재 턴:",blend=alphaBlend)
        d("Canvas/Layer/bg/editbox0/BACKGROUND_SPRITE","sliced-sprite",768.224f,430.411f,160f,50f,"box1")
        d("Canvas/Layer/bg/editbox0/TEXT_LABEL","label",770.224f,430.411f,158f,50f,text=model.roundText,blend=alphaBlend)
        val buttons=listOf(
            floatArrayOf(495.886f,207.8f,580.686f,210.9f) to "수정",
            floatArrayOf(772.686f,207.8f,857.486f,210.9f) to "취소",
            floatArrayOf(495.886f,354.9f,580.686f,358f) to "전역",
            floatArrayOf(495.886f,277.1f,500.371f,280.2f) to "적군 체력 감소",
            floatArrayOf(772.686f,354.9f,817.331f,358f) to "적군 전멸",
            floatArrayOf(772.686f,277.1f,817.331f,280.2f) to "아군 만피")
        buttons.forEachIndexed{index,(r,text)->
            d("Canvas/Layer/bg/button$index/Background","sliced-sprite",r[0],r[1],238.8f,56.6f,"box3")
            val w=when(index){3->229.83f;4,5->149.51f;else->69.2f}
            d("Canvas/Layer/bg/button$index/Background/Label","label",r[2],r[3],w,50.4f,text=text,blend=alphaBlend)
        }
    }

    private fun appendWeather(log:RenderEventLog,phase:String){
        fun d(layer:String,path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String?=null,text:String="",opacity:Float=1f,blend:Any=listOf(770,771))=
            log.draw(phase,layer,path,type,x,y,w,h,asset,opacity,blend,true,text)
        d("HallLayer","Canvas/Layer/panel0/bg","sprite",0f,0f,1488.372f,800f,"default_sprite_splash",opacity=.392f)
        d("HallLayer","Canvas/Layer/panel0/list0","sliced-sprite",767.878f,308.794f,169.8f,179.5f,"box1")
        d("HallLayer","Canvas/Layer/panel0/list0/scrollview","tiled-sprite",767.878f,308.794f,169.8f,179.5f,"Logo_12-1")
        BattleEditLayer2.weatherNames.forEachIndexed{index,text->val y=463.854f-index*50f;val w=when(text.length){1->34.6f;2->69.2f;else->103.8f}
            d("HallLayer","Canvas/Layer/panel0/list0/scrollview/view/content/item","sliced-sprite",767.878f,y,169.8f,50f,"box1")
            d("HallLayer","Canvas/Layer/panel0/list0/scrollview/view/content/item/label","label",852.778f-w/2f,y-.2f,w,50.4f,text=text,blend=alphaBlend)}
    }

    private fun appendChild(log:RenderEventLog,phase:String,layer:String="EditLayer3"){
        fun d(path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String?=null,text:String="",opacity:Float=1f,blend:Any=listOf(770,771))=
            log.draw(phase,layer,path,type,x,y,w,h,asset,opacity,blend,true,text)
        d("Canvas/Layer/Panel_cancel","sprite",0f,0f,1488.372f,800f,"default_sprite_splash",opacity=.314f)
        d("Canvas/Layer/bg","tiled-sprite",444.186f,195f,600f,410f,"Logo_9-1")
        d("Canvas/Layer/bg/bg1","sprite",444.186f,555f,600f,50f,"bg1")
        d("Canvas/Layer/bg/bg1/label","label",629.271f,554.8f,229.83f,50.4f,text="전역 변수 편집",blend=alphaBlend)
        d("Canvas/Layer/bg/label","label",625.117f,396.8f,80.31f,50.4f,text="야심:",blend=alphaBlend)
        d("Canvas/Layer/bg/editbox0/BACKGROUND_SPRITE","sliced-sprite",715.31f,397f,225.2f,50f,"box1")
        d("Canvas/Layer/bg/editbox0/TEXT_LABEL","label",717.31f,397f,223.2f,50f,text="50",blend=alphaBlend)
        d("Canvas/Layer/bg/label","label",625.117f,314.8f,80.31f,50.4f,text="금전:",blend=alphaBlend)
        d("Canvas/Layer/bg/editbox1/BACKGROUND_SPRITE","sliced-sprite",715.31f,315f,225.2f,50f,"box1")
        d("Canvas/Layer/bg/editbox1/TEXT_LABEL","label",717.31f,315f,223.2f,50f,text="0",blend=alphaBlend)
        d("Canvas/Layer/bg/label","label",544.957f,477.8f,160.63f,50.4f,text="장면 이동:",blend=alphaBlend)
        d("Canvas/Layer/bg/bg3","sliced-sprite",714.91f,479f,250f,50f,"box1")
        d("Canvas/Layer/bg/bg3/label","label",718.51f,478.8f,243.4f,50.4f,text="영천의 전투R",blend=alphaBlend)
        val buttons=listOf(floatArrayOf(876.797f,212.983f,150.4f,58.5f,901.997f,222.233f,100f,40f) to "수정",floatArrayOf(719.152f,212.983f,150.4f,58.5f,744.352f,222.233f,100f,40f) to "폐쇄",floatArrayOf(487.035f,212.95f,221.5f,58.5f,505.73f,217f,184.11f,50.4f) to "창고 비우기")
        buttons.forEachIndexed{i,(r,t)->d("Canvas/Layer/bg/button$i/Background","sliced-sprite",r[0],r[1],r[2],r[3],"box3");d("Canvas/Layer/bg/button$i/Background/Label","label",r[4],r[5],r[6],r[7],text=t,blend=alphaBlend)}
    }

    private fun appendRegister(log: RenderEventLog, phase: String) {
        fun d(path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String?=null,text:String="",blend:Any=listOf(770,771)) =
            log.draw(phase,"RegisterLayer",path,type,x,y,w,h,asset,blend=blend,text=text)
        d("Canvas/Layer/bg0","tiled-sprite",344.186f,163.5f,800f,473f,"Logo_12-1")
        d("Canvas/Layer/bg0/bg1","sprite",344.186f,586.5f,800f,50f,"bg1")
        d("Canvas/Layer/bg0/bg1/label","label",624.186f,586.3f,264.43f,50.4f,text="등록 코드 생성기",blend=alphaBlend)
        d("Canvas/Layer/bg0/box3","sliced-sprite",344.186f,163.5f,800f,473f,"box1")
        d("Canvas/Layer/bg0/box1","sliced-sprite",355.686f,520f,773f,54f,"box1")
        d("Canvas/Layer/bg0/box1/editbox/PLACEHOLDER_LABEL","label",369.186f,522f,748f,50f,text="활성화 코드를 입력하세요",blend=alphaBlend)
        d("Canvas/Layer/bg0/label","label",360.186f,239f,768f,118f,text="Label",blend=alphaBlend)
        d("Canvas/Layer/bg0/button0/Background","sliced-sprite",916.163f,180.272f,200f,50f,"box3")
        d("Canvas/Layer/bg0/button0/Background/Label","label",939.408f,181.071f,153.51f,54.4f,text="생성 공유",blend=alphaBlend)
        d("Canvas/Layer/bg0/button1/Background","sliced-sprite",698.334f,180.272f,200f,50f,"box3")
        d("Canvas/Layer/bg0/button1/Background/Label","label",748.334f,188.271f,100f,40f,text="취소",blend=alphaBlend)
        d("Canvas/Layer/bg0/label0","label",360.186f,393.717f,768f,118f,text="Label",blend=alphaBlend)
    }

    private fun appendChildScenePanel(log:RenderEventLog, phase: String) {
        fun d(path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String?=null,text:String="",opacity:Float=1f,blend:Any=listOf(770,771))=
            log.draw(phase,"HallLayer",path,type,x,y,w,h,asset,opacity,blend,true,text)
        d("Canvas/Layer/panel0/bg","sprite",0f,0f,1488.372f,800f,"default_sprite_splash",opacity=.392f)
        d("Canvas/Layer/panel0/list0","sliced-sprite",715.136f,298.894f,250f,179.5f,"box1")
        d("Canvas/Layer/panel0/list0/scrollview","tiled-sprite",715.136f,298.894f,250f,179.5f,"Logo_12-1")
        val names=listOf("영천의 전투","사수관 전투","호로관 전투","동탁 추격전","청주 황건 토벌전","서주 복수전","복양의 전투","복양의 전투 2","복양의 전투 3","황제 구출 전투")
        names.forEachIndexed { index, name ->
            val y=428.394f-index*50f
            d("Canvas/Layer/panel0/list0/scrollview/view/content/item","sliced-sprite",715.136f,y,250f,50f,"box1")
            d("Canvas/Layer/panel0/list0/scrollview/view/content/item/label","label",720.136f,y+9.88f,240f,30.24f,text="$index $name",blend=alphaBlend)
        }
    }
}
