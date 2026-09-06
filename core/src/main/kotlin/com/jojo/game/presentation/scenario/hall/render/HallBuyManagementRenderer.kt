// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.hall.HallBuyCatalogRenderer
import com.jojo.game.presentation.scenario.hall.HallBuyUnitSummaryRenderer

/** HallBuyManagementRenderer: 거점 Buy Management 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallBuyManagementRenderer {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallBuyManagementRenderView) {
        /**
         * `texture`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun texture(name: String) = assets.hallTexture("maps/ui/start-battle/$name.png")
        /**
         * `patch`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun patch(name: String, inset: Int = 3) = texture(name)?.let { NinePatch(it, inset, inset, inset, inset) }
        /**
         * `label`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun label(text: String, x: Float, y: Float, width: Float, centered: Boolean = false) { assets.bodyFont.color=Color.BLACK; assets.bodyFont.draw(batch,text,x,y,width,if(centered) Align.center else Align.left,false) }
        /**
         * `tiled`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun tiled(tex: com.badlogic.gdx.graphics.Texture, x: Float, y: Float, width: Float, height: Float) { val tw=tex.width*.86f; val th=tex.height*.86f; var dy=0f; while(dy<height-.01f) { val dh=minOf(th,height-dy); var dx=0f; while(dx<width-.01f) { val dw=minOf(tw,width-dx); batch.draw(tex,x+dx,y+dy,dw,dh,0,0,(dw/.86f).toInt().coerceIn(1,tex.width),(dh/.86f).toInt().coerceIn(1,tex.height),false,false); dx+=tw }; dy+=th } }
        val x=168.72f; val y=28.81f; val w=943.42f; val h=630.38f
        batch.color=Color.WHITE; texture("logo9")?.let { tiled(it,x,y,w,h) }; patch("box1")?.draw(batch,x,y,w,h); patch("title",5)?.draw(batch,x,y+h-43f,w,43f)
        val layout=GlyphLayout(assets.titleFont,"매입"); assets.titleFont.color=Color.BLACK; assets.titleFont.draw(batch,layout,x+(w-layout.width)/2f,y+h-5f)
        HallBuyCatalogRenderer.draw(assets,batch,view.catalog); patch("box1")?.draw(batch,673.77f,89.44f,414.52f,474.72f); HallBuyUnitSummaryRenderer.draw(assets,batch,view.summary)
        label("현금",x+22f,y+23f,240f); label(view.money,x+170f,y+23f,140f,true)
        listOf(Triple("종료",530.78f,120.4f),Triple("이전 무장",678.70f,146.2f),Triple("다음 무장",838.66f,146.2f)).forEach { (text,bx,bw) -> patch("button",9)?.draw(batch,bx,36.98f,bw,43f); label(text,bx,67.98f,bw,true) }
        view.notice?.let { assets.bodyFont.color=Color(.55f,.05f,.05f,1f); assets.bodyFont.draw(batch,it,x+18f,y+h-52f,w-36f,Align.right,false) }; batch.color=Color.WHITE
    }
}
