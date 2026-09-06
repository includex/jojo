// Battle
package com.jojo.game.presentation.battle.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
internal class BattleUnitInfoAssets : Disposable {
    private val bg = lazyTexture("bg1.png")
    private val box1 = lazyTexture("box1.png")
    private val box2 = lazyTexture("box2.png")
    private val box3 = lazyTexture("box3.png")
    private val progress = lazyTexture("progress.png")
    private val mark2 = lazyTexture("mark2.png")
    private val mark3 = lazyTexture("mark3.png")
    private val mark6 = lazyTexture("mark6.png")
    private val logo = lazy {
        Texture(Gdx.files.internal("maps/ui/unit-info/logo9.png")).also {
            it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
    }
    private val face = lazyTexture("face179.png")
    private val vline2 = lazyTexture("vline2.png")

    val unitInfoBg get() = bg.value
    val unitInfoBox1 get() = box1.value
    val unitInfoBox2 get() = box2.value
    val unitInfoBox3 get() = box3.value
    val unitInfoProgress get() = progress.value
    val unitInfoMark2 get() = mark2.value
    val unitInfoMark3 get() = mark3.value
    val unitInfoMark6 get() = mark6.value
    val unitInfoLogo get() = logo.value
    val unitInfoFace get() = face.value
    val unitInfoVline2 get() = vline2.value

    private fun lazyTexture(fileName: String) = lazy {
        Texture(Gdx.files.internal("maps/ui/unit-info/$fileName"))
    }

    override fun dispose() {
        listOf(bg, box1, box2, box3, progress, mark2, mark3, mark6, logo, face, vline2)
            .filter { it.isInitialized() }
            .forEach { it.value.dispose() }
    }
}
