// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.presentation.scenario.overlay.*

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** HallViewState: 거점 표시 정보 상태이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallViewState(
    val menuTexture: Texture?,
    val battleTexture: Texture?,
    val equipTexture: Texture?,
    val buyTexture: Texture?,
    val sellTexture: Texture?,
)

/** HallRenderer: 거점 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallRenderer {
    fun drawMainCommands(batch: SpriteBatch, view: HallViewState) {
        batch.color.set(1f, 1f, 1f, 1f)
        view.menuTexture?.let { batch.draw(it, 31f, 318.2f, 51.6f, 51.6f) }
        listOf(
            view.battleTexture to 936.86f,
            view.equipTexture to 1019.42f,
            view.buyTexture to 1101.98f,
            view.sellTexture to 1184.54f,
        ).forEach { (texture, centerX) ->
            texture?.let { batch.draw(it, centerX - 41.28f, 1.72f, 82.56f, 82.56f) }
        }
    }
}
