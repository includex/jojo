package com.jojo.game.presentation.battle.overlay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import com.jojo.game.application.battle.BattleCommandFlow
import com.jojo.game.application.battle.BattleCommandRenderModel

/** Compact, opaque command cards; hit testing uses the exact same model rectangles. */
class BattleCommandMenuRenderer(
    private val batch: SpriteBatch,
    private val shapes: ShapeRenderer,
    private val font: BitmapFont,
    private val icons: Map<Int, Texture>,
) {
    fun draw(buttons: List<BattleCommandFlow.Button>, offset: Pair<Float, Float>, unitName: String) {
        val model = BattleCommandRenderModel
        val left = model.PANEL_LEFT + offset.first
        val bottom = model.PANEL_BOTTOM + offset.second
        val previousColor = Color(batch.color)
        val previousFontColor = Color(font.color)
        val previousScaleX = font.data.scaleX
        val previousScaleY = font.data.scaleY
        shapes.projectionMatrix = batch.projectionMatrix
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f,0f,0f,.35f)
        shapes.rect(left+5f,bottom-5f,model.PANEL_WIDTH,model.PANEL_HEIGHT)
        shapes.color = GOLD
        shapes.rect(left,bottom,model.PANEL_WIDTH,model.PANEL_HEIGHT)
        shapes.color = PANEL
        shapes.rect(left+2f,bottom+2f,model.PANEL_WIDTH-4f,model.PANEL_HEIGHT-4f)
        shapes.color=BORDER
        shapes.rect(left+14f,bottom+model.PANEL_HEIGHT-42f,model.PANEL_WIDTH-28f,1f)
        shapes.rect(left+14f,model.visuals[6].y+offset.second+59f,model.PANEL_WIDTH-28f,1f)
        buttons.forEach { button ->
            val v=model.visuals[button.command.tag]
            val x=v.x+offset.first;val y=v.y+offset.second
            shapes.color=if(button.command==BattleCommandFlow.Command.INFO) GOLD else if(button.interactable) BORDER else DISABLED_BORDER
            shapes.rect(x,y,v.width,v.height)
            shapes.color=if(button.command==BattleCommandFlow.Command.INFO) INFO else if(button.interactable) CARD else DISABLED
            shapes.rect(x+1f,y+1f,v.width-2f,v.height-2f)
            if (button.interactable) {
                shapes.color=Color(.48f,.39f,.26f,1f)
                shapes.rect(x+2f,y+v.height-2f,v.width-4f,1f)
                shapes.color=Color(.10f,.075f,.04f,1f)
                shapes.rect(x+2f,y+1f,v.width-4f,1f)
            }
        }
        shapes.end()
        batch.begin()
        try {
            font.data.setScale(23f/36f)
            font.color=GOLD
            font.draw(batch,unitName,left+16f,bottom+model.PANEL_HEIGHT-13f,model.PANEL_WIDTH-32f,Align.left,false)
            buttons.forEach { button ->
                val tag=button.command.tag
                val v=model.visuals[tag]
                batch.color=if(button.interactable) Color.WHITE else Color(.42f,.45f,.48f,1f)
                v.icons.forEach { icon ->
                    icons[icon.asset.removePrefix("command").toInt()]?.let { texture ->
                        batch.draw(texture,icon.x+offset.first,icon.y+offset.second,icon.width,icon.height)
                    }
                }
                font.data.setScale(26f/36f)
                font.color=if(button.interactable) TEXT else MUTED
                val inset=if(v.icons.isEmpty()) 10f else 49f
                font.draw(batch,model.labels[tag],v.x+offset.first+inset,
                    v.y+offset.second+v.height/2f+font.capHeight/2f,
                    v.width-inset-8f, if(v.icons.isEmpty()) Align.center else Align.left,false)
            }
        } finally {
            font.data.setScale(previousScaleX,previousScaleY)
            font.color=previousFontColor
            batch.color=previousColor
            batch.end()
        }
    }
    private companion object {
        val PANEL=Color.valueOf("21190f")
        val CARD=Color.valueOf("352919")
        val BORDER=Color.valueOf("786346")
        val DISABLED=Color.valueOf("282219")
        val DISABLED_BORDER=Color.valueOf("493d2b")
        val INFO=Color.valueOf("493b24")
        val GOLD=Color.valueOf("b99b61")
        val TEXT=Color.valueOf("eee2c6")
        val MUTED=Color.valueOf("93836c")
    }
}
