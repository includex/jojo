// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.jojo.game.presentation.scenario.overlay.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** HallCommandRenderer: 거점에서 가능한 명령 버튼과 취소 동작 영역을 렌더링한다. */
internal object HallCommandRenderer {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(batch: SpriteBatch, view: HallCommandRenderView) {
        batch.color = Color.WHITE
        view.menuTexture?.let { batch.draw(it, 31f, 318.2f, 51.6f, 51.6f) }
        listOf(view.battleTexture to 936.86f, view.equipTexture to 1019.42f, view.buyTexture to 1101.98f, view.sellTexture to 1184.54f).forEach { (texture, x) -> texture?.let { batch.draw(it, x - 41.28f, 1.72f, 82.56f, 82.56f) } }
    }
}
