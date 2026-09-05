package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** Stateless HallMenuLayer renderer over an immutable render view. */
internal object HallMenuRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallMenuRenderView) {
        fun texture(name: String) = assets.hallTexture("maps/ui/hall-menu/$name.png")
        fun patch(name: String, l: Int, r: Int, t: Int, b: Int) = texture(name)?.let { NinePatch(it, l, r, t, b) }
        texture("panel")?.let { batch.draw(it, 0f, 0f, 1280f, 125.56f) }
        patch("inner", 3, 3, 3, 3)?.draw(batch, 0f, 0f, 1280f, 125.56f)
        patch("label-box", 3, 3, 3, 3)?.draw(batch, 99.72f, 4.25f, 261.44f, 37.84f)
        patch("label-mark", 1, 1, 3, 3)?.draw(batch, 101.44f, 5.97f, 258f, 34.4f)
        val showLabels = !(view.interactive && view.fixture == "menu")
        val layout = GlyphLayout(); assets.bodyFont.color = Color.WHITE
        if (showLabels) { layout.setText(assets.bodyFont, view.eventName); assets.bodyFont.draw(batch, layout, 230.44f-layout.width/2f, 23.17f+layout.height/2f) }
        patch("label-box", 3, 3, 3, 3)?.draw(batch, 366.95f, 4.23f, 278.64f, 37.84f)
        patch("label-mark", 1, 1, 3, 3)?.draw(batch, 368.67f, 5.95f, 275.2f, 34.4f)
        if (showLabels) { layout.setText(assets.bodyFont, view.stageName); assets.bodyFont.draw(batch, layout, 505.67f-layout.width/2f, 23.13f+layout.height/2f) }
        val to = view.ambitionTo.coerceIn(0,100); val from=view.ambitionFrom.coerceIn(0,100)
        val (root,value)=if(view.interactive) "bar-blue" to "bar-red" else when { to<16 -> "bar-red" to "bar-yellow"; to>84 -> "bar-yellow" to "bar-blue"; else -> "bar-blue" to "bar-red" }
        patch(root,1,1,1,1)?.draw(batch,717.4f,16.70f,258f,12.9f)
        val tween=((view.ambitionElapsedSeconds-1.2f)/1f).coerceIn(0f,1f); val amount=if(view.fixture=="ambition") to.toFloat() else from+(to-from)*tween
        patch(value,1,1,1,1)?.draw(batch,717.4f,16.70f,258f*amount/100f,12.9f)
        val visible=view.ambitionElapsedSeconds>=1.2f||((view.ambitionElapsedSeconds/.2f).toInt()and 1)==1
        if(!view.interactive&&view.fixture!="ambition"&&view.indicatorEnabled&&visible) {
            val decreasing=to<from; texture(if(decreasing) "flag-right" else "flag-left")?.let { batch.draw(it,(if(decreasing)1003.48f else 690.81f)-6.88f,16.70f,13.76f,12.9f) }
        } else if(view.interactive) { texture("flag-left")?.let { batch.draw(it,787.27f*.86f,11.917f*.86f,27.52f,25.8f) }; texture("flag-right")?.let { batch.draw(it,1150.837f*.86f,11.917f*.86f,27.52f,25.8f) } }
        val icons=listOf("tool1","tool2","tool3","tool4","tool5","tool6","tool7","tool8","help"); val button=patch("button",7,8,7,7)
        HallRenderGeometry.menuButtonCenters.zip(icons).forEachIndexed { index,(sourceX,icon) ->
            val x=sourceX*.86f; button?.draw(batch,x-37.84f,52.137f*.86f,75.68f,75.68f); texture(icon)?.let { batch.draw(it,x-30.96f,(if(index==icons.lastIndex)60.137f else 60.419f)*.86f,61.92f,61.92f) }
        }
        batch.color=Color.WHITE
    }
}
