// 시나리오 거점 전용 장비 화면 렌더링
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallExclusiveRenderer: 전용·세트 장비 탭의 고정 패널, 열 머리글과 확인 버튼을 원본 좌표로 그린다. */
internal object HallExclusiveRenderer {
    /** 원본 UI 좌표를 LibGDX 화면 배율로 환산하는 공통 비율이다. */
    private const val SCALE = .86f

    /** draw: 선택 탭에 맞는 열 구조를 그리고 장비 목록이 올라갈 배경 영역을 구성한다. */
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallExclusiveView) {
        /**
         * `texture`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun texture(name: String): Texture? = assets.hallTexture("maps/ui/start-battle/$name.png")
        /**
         * `patch`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun patch(name: String): NinePatch? = texture(name)?.let { NinePatch(it, 3, 3, 3, 3) }
        /**
         * `tiled`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun tiled(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
            val tileWidth = texture.width * SCALE
            val tileHeight = texture.height * SCALE
            var dy = 0f
            while (dy < height - .01f) {
                val drawHeight = minOf(tileHeight, height - dy)
                val sourceHeight = (drawHeight / SCALE).toInt().coerceIn(1, texture.height)
                var dx = 0f
                while (dx < width - .01f) {
                    val drawWidth = minOf(tileWidth, width - dx)
                    val sourceWidth = (drawWidth / SCALE).toInt().coerceIn(1, texture.width)
                    batch.draw(texture, x + dx, y + dy, drawWidth, drawHeight, 0, 0, sourceWidth, sourceHeight, false, false)
                    dx += tileWidth
                }
                dy += tileHeight
            }
        }
        /**
         * `label`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun label(value: String, x: Float, y: Float, width: Float) {
            assets.bodyFont.color = Color.BLACK
            assets.bodyFont.draw(batch, value, x * SCALE, (y + 43f) * SCALE, width * SCALE, Align.center, false)
        }
        /**
         * `header`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun header(x: Float, y: Float, width: Float, value: String) {
            patch("box4")?.draw(batch, x * SCALE, y * SCALE, width * SCALE, 60f * SCALE)
            label(value, x, y + 3f, width)
        }
        /**
         * `button`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun button(x: Float, value: String, labelWidth: Float) {
            patch("box3")?.draw(batch, x * SCALE, 54.533f * SCALE, 200f * SCALE, 54f * SCALE)
            label(value, x + (200f - labelWidth) / 2f, 59.533f, labelWidth)
        }

        batch.color = Color.WHITE
        texture("logo9")?.let { tiled(it, 136.186f * SCALE, 47f * SCALE, 1216f * SCALE, 706f * SCALE) }
        patch("box1")?.draw(batch, 136.186f * SCALE, 47f * SCALE, 1216f * SCALE, 706f * SCALE)
        texture("title")?.let { batch.draw(it, 136.186f * SCALE, 703f * SCALE, 1216f * SCALE, 50f * SCALE) }
        label("장비 정보", 669.431f, 702.8f, 149.51f)
        if (view.selectedTab == HallExclusiveView.Tab.SET_LIST) {
            patch("box4")?.draw(batch, 138.186f * SCALE, 117.5f * SCALE, 1212f * SCALE, 585f * SCALE)
            texture("vline")?.let { line ->
                listOf(371.375f, 604.197f, 840.498f).forEach { x ->
                    batch.draw(line, x * SCALE, 120.254f * SCALE, 6f * SCALE, 524.8f * SCALE)
                }
            }
            header(138.586f, 642.1f, 236f, "무기")
            header(374.586f, 642.1f, 233f, "보구")
            header(607.636f, 642.1f, 236.5f, "보조")
            header(844.036f, 642.1f, 506.1f, "특수 효과")
        } else {
            patch("box4")?.draw(batch, 140.186f * SCALE, 117.45f * SCALE, 1208f * SCALE, 585.7f * SCALE)
            texture("vline")?.let { line ->
                listOf(321.257f to 119.319f, 565.153f to 119.205f).forEach { (x, y) ->
                    batch.draw(line, x * SCALE, y * SCALE, 6f * SCALE, 524.8f * SCALE)
                }
            }
            header(140.85f, 643.3f, 185f, "소지자")
            header(324.236f, 643.3f, 243.9f, "이름")
            header(568.186f, 643.3f, 780f, "특수 효과")
        }
        button(354.241f, "전용 목록", 167f)
        button(147.282f, "세트 목록", 167f)
        button(1141.864f, "확인", 100f)
        batch.color = Color.WHITE
    }
}
