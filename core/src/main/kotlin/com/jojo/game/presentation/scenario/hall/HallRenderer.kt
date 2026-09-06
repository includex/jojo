// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.presentation.scenario.overlay.*

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** HallViewState: 거점 기본 화면이 어떤 메뉴·패널을 표시하는지 나타내는 통합 view 상태다. */
internal data class HallViewState(
    val menuTexture: Texture?,
    val battleTexture: Texture?,
    val equipTexture: Texture?,
    val buyTexture: Texture?,
    val sellTexture: Texture?,
)

/** HallRenderer: 거점 배경, 메뉴, 관리 화면의 공통 레이어를 정해진 순서로 렌더링한다. */
internal object HallRenderer {
    /**
     * `drawMainCommands`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
