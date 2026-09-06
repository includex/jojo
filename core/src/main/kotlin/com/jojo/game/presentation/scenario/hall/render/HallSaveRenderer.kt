// Scenario
package com.jojo.game.presentation.scenario.hall.render
import com.jojo.game.presentation.shared.overlay.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallSaveRenderer: 거점 저장 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallSaveRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallSaveRenderView) {
        fun texture(path: String) = assets.hallTexture(path)
        fun start(name: String) = texture("maps/ui/start-battle/$name.png")
        fun patch(name: String, inset: Int = 3) = start(name)?.let { NinePatch(it, inset, inset, inset, inset) }
        fun label(value: String, x: Float, y: Float, w: Float, centered: Boolean = false) { assets.bodyFont.color=Color.BLACK; assets.bodyFont.draw(batch,value,x*.86f,y*.86f+35f,w*.86f,if(centered) Align.center else Align.left,false) }
        fun tiled(tex: com.badlogic.gdx.graphics.Texture, x: Float, y: Float, w: Float, h: Float) {
            val px=x*.86f; val py=y*.86f; val width=w*.86f; val height=h*.86f; val tw=tex.width*.86f; val th=tex.height*.86f
            var dy=0f; while(dy<height-.01f) { var dx=0f; while(dx<width-.01f) { val dw=minOf(tw,width-dx); val dh=minOf(th,height-dy); batch.draw(tex,px+dx,py+dy,dw,dh,0,0,(dw/.86f).toInt().coerceAtLeast(1),(dh/.86f).toInt().coerceAtLeast(1),false,false); dx+=tw }; dy+=th }
        }
        batch.color=Color.WHITE
        start("logo9")?.let { tiled(it,278.186f,83f,932f,634f) }; patch("button",8)?.draw(batch,278.186f*.86f,83f*.86f,932f*.86f,634f*.86f)
        start("title")?.let { batch.draw(it,278.186f*.86f,667f*.86f,932f*.86f,43f) }; label("진행 상황 유지",288.186f,666.8f,229.83f); label("어떤 진행 상황을 저장할지 선택해 주세요.",286.785f,612.805f,654.88f)
        patch("box2")?.draw(batch,287.186f*.86f,172.534f*.86f,912f*.86f,428f*.86f)
        view.rows.take(8).forEachIndexed { index,row -> val y=HallRenderGeometry.saveRowY(index); assets.choiceRowTexture?.let { batch.draw(it,289.186f*.86f,y*.86f,908f*.86f,43f) }; label(row.number,295.448f,y-.2f,117.85f); label(row.stage,434.615f,y-.2f,124.49f); label(row.name,577.886f,y,616.3f) }
        texture("maps/ui/title/load/vline.png")?.let { line -> listOf(422.057f,566.695f).forEach { x -> batch.draw(line,x*.86f,174.634f*.86f,6f*.86f,423.8f*.86f) } }
        label("따뜻한 알림: 오래된 저장 파일일수록 앞에 표시됩니다.",131.555f,105.399f,850.11f); patch("button",8)?.draw(batch,1045.855f*.86f,100.162f*.86f,147.6f*.86f,56f*.86f); label("취소",1069.655f,108.162f,100f,true)
        view.pendingPrompt?.let { prompt ->
            start("logo9")?.let { tiled(it,426.686f,252f,635f,296f) }; patch("button",8)?.draw(batch,426.686f*.86f,252f*.86f,635f*.86f,296f*.86f)
            texture("maps/ui/title/load/eagle.png")?.let { batch.draw(it,453.005f*.86f,373.951f*.86f,106f*.86f,124f*.86f) }; label(prompt,573.686f,335f,463f)
            listOf(Triple(554.186f,"됐어",557.336f),Triple(754.186f,"저장",757.586f)).forEach { (x,value,tx) -> patch("button",8)?.draw(batch,x*.86f,271.285f*.86f,180f*.86f,43f); label(value,tx,279.085f,if(value=="됐어")168.1f else 169.4f,true) }
        }
        if(view.completionTipOpen) { start("logo9")?.let { tiled(it,426.686f,252f,635f,296f) }; patch("button",8)?.draw(batch,426.686f*.86f,252f*.86f,635f*.86f,296f*.86f); label("저장 완료.",573.686f,385f,463f,true); patch("button",8)?.draw(batch,654.186f*.86f,271.285f*.86f,180f*.86f,43f); label("확인",657.586f,279.085f,169.4f,true) }
        batch.color=Color.WHITE
    }
}
