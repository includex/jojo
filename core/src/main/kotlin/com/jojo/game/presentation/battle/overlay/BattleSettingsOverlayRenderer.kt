// Battle
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** 전투 설정 표시 정보: 음향·미니맵 등 비트 설정과 텍스트·안내·배경 선택 값을 정의한다. */
data class BattleSettingsOverlayView(
    val flags: Int,
    val msgSpeed: Int,
    val notifyLevel: Int,
    val background: Int,
)

/** 전투 설정 자산: 설정 창 패널과 배경 색상 견본을 그릴 그래픽을 보관한다. */
data class BattleSettingsOverlayAssets(
    val background: Texture?,
    val panel: NinePatch?,
)

/** 전투 설정 렌더러: 설정 비트와 선택 인덱스를 체크·라디오·색상 견본으로 출력한다. */
class BattleSettingsOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `font` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val font: BitmapFont,
    /** `assets` (BattleSettingsOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattleSettingsOverlayAssets,
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(view: BattleSettingsOverlayView) {
        batch.begin()
        batch.color = Color.WHITE
        drawTiledBackground()
        assets.panel?.draw(batch, 204f, 110f, 1081f, 596f)
        assets.panel?.draw(batch, 793f, 520f, 480f, 100f)
        assets.panel?.draw(batch, 793f, 388f, 480f, 100f)
        assets.panel?.draw(batch, 793f, 256f, 480f, 100f)
        assets.panel?.draw(batch, 793f, 81f, 480f, 142f)

        font.color = Color.BLACK
        font.data.setScale(40f / 26f)
        font.draw(batch, "환경 설정", 201f, 748f)
        font.draw(batch, "클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.", 205f, 690f)
        val options = listOf(
            "배경 음악 듣기" to 0,
            "효과음 듣기" to 1,
            "전투시 전장 축소 이미지가 자동으로 표시됩니다" to 2,
            "대화창 자동 닫음" to 3,
            "체력 바가 유닛 위에 있습니다" to 4,
        )
        options.forEachIndexed { index, (label, bit) ->
            val enabled = view.flags and (1 shl bit) != 0
            font.color = if (enabled) Color(0.1f, .85f, .2f, 1f) else Color.DARK_GRAY
            font.draw(batch, if (enabled) "✓" else "■", 224f, 643f - index * 65f)
            font.color = Color.BLACK
            font.draw(batch, label, 261f, 643f - index * 65f)
        }
        drawRadios("텍스트 속도", listOf("느림", "중간", "빠름"), view.msgSpeed, 574f)
        drawRadios("정보 설명", listOf("자세히", "보통", "요약"), view.notifyLevel, 310f)
        font.color = Color.BLACK
        font.draw(batch, "대화창 색상", 846f, 198f)
        val swatchColors = listOf(
            Color(1f, 1f, 1f, 1f), Color(.85f, .85f, .85f, 1f),
            Color(.73f, .73f, .78f, 1f), Color(.88f, .84f, .72f, 1f),
        )
        swatchColors.forEachIndexed { index, tint ->
            val x = 816f + index * 105f
            assets.panel?.draw(batch, x, 91f, 96f, 72f)
            assets.background?.let {
                batch.color = tint
                batch.draw(it, x + 5f, 96f, 86f, 62f)
                batch.color = Color.WHITE
            }
            font.color = if (view.background == index) Color(.08f, .45f, .95f, 1f) else Color.DARK_GRAY
            font.draw(batch, if (view.background == index) "●" else "○", x + 36f, 108f)
        }
        assets.panel?.draw(batch, 1130f, 47f, 156f, 56f)
        font.color = Color.BLACK
        font.draw(batch, "확인", 1158f, 75f)
        font.color = Color.WHITE
        font.data.setScale(1f)
        batch.color = Color.WHITE
        batch.end()
    }

    /**
     * `drawRadios`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawRadios(title: String, labels: List<String>, selected: Int, baseY: Float) {
        font.color = Color.BLACK
        font.draw(batch, title, 822f, baseY + 45f)
        labels.forEachIndexed { index, label ->
            val active = selected == index
            font.color = if (active) Color(0.05f, .48f, .94f, 1f) else Color.DARK_GRAY
            font.draw(batch, if (active) "●" else "○", 816f + index * 145f, baseY)
            font.color = Color.BLACK
            font.draw(batch, label, 846f + index * 145f, baseY)
        }
    }

    /**
     * `drawTiledBackground`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawTiledBackground() {
        assets.background?.let { texture ->
            var y = 41f
            while (y < 759f) {
                var x = 196f
                while (x < 1293f) {
                    batch.draw(texture, x, y, minOf(96f, 1293f - x), minOf(96f, 759f - y))
                    x += 96f
                }
                y += 96f
            }
        }
    }
}
