package com.jojo.game.presentation.battle.preparation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport

/** LibGDX drawing boundary for immutable preparation snapshots. */
internal class BattlePreparationRenderer(private val assets: BattlePreparationAssets) {
    private val viewport = FitViewport(1280f, 688f, OrthographicCamera())
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()

    /**
     * 공개 메서드 `render`
     *
     * ### 파라미터
    - `state` (`BattlePreparationViewState`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun render(state: BattlePreparationViewState) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        assets.background?.let { batch.draw(it, 0f, 0f, 1280f, 688f) }
        batch.color = Color(1f, 1f, 1f, 30f / 255f)
        batch.draw(assets.dim, 0f, 0f, 1280f, 688f)
        if (state.fixture == BattlePreparationFixture.BattleView) {
            drawBattleView(state.battleViewMarkerCount)
            batch.end()
            return
        }
        batch.color = Color.WHITE
        assets.logo9?.let { drawTiled(it, 138.061f, 43f, 1003.878f, 602f) }
        assets.roster?.let { batch.draw(it, 143.78f, 323.79f, 688f, 312.18f, 0, 0, 400, 146, false, false) }
        assets.selected?.let { batch.draw(it, 143.91f, 52.57f, 688f, 220.16f) }
        assets.outerPatch?.draw(batch, 138.061f, 43f, 1003.878f, 602f)
        assets.box1Patch?.draw(batch, 834.575f, 96.793f, 298.85f, 479.966f)
        assets.titlePatch?.draw(batch, 857.565f, 557.487f, 139.062f, 34.658f)
        drawRoster(state)
        drawSelectedSlots(state)
        drawUnitInfo(state.units.firstOrNull { it.id == state.cursorId })
        drawButton(954.76f, 49.88f, "결정", state.canStart)
        drawButton(1049.36f, 49.88f, "취소", true)
        if (state.sortOpen) drawBattleSortMenu()
        if (state.fixture == BattlePreparationFixture.UnitInfo) drawUnitInfoFixture(state)
        batch.end()
    }

    /**
     * 공개 메서드 `screenToWorld`
     *
     * ### 파라미터
    - `screenX` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `screenY` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Pair<Float, Float>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun screenToWorld(screenX: Int, screenY: Int): Pair<Float, Float> =
        viewport.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f)).let { it.x to it.y }

    /**
     * 공개 메서드 `resize`
     *
     * ### 파라미터
    - `width` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `height` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun resize(width: Int, height: Int) = viewport.update(width, height, true)

    /**
     * 공개 메서드 `dispose`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun dispose() = batch.dispose()

    private fun drawRoster(state: BattlePreparationViewState) {
        state.availableIds.forEachIndexed { index, id ->
            val unit = state.units.firstOrNull { it.id == id } ?: return@forEachIndexed
            val cx = (233.686f + index % 6 * 133f) * SCALE
            val cy = (682.202f - index / 6 * 144f) * SCALE
            assets.avatar(unit.avatarId)?.let { texture ->
                batch.color = if (id in state.selectedIds) Color(.5f, .5f, .5f, 1f) else Color.WHITE
                batch.draw(texture, cx - 49.536f, cy - 49.536f, 99.072f, 99.072f, 0, 301, 48, 48, false, false)
            }
            batch.color = Color.WHITE
            assets.rosterFont.color = Color.WHITE
            assets.rosterFont.draw(
                batch,
                "Lv.",
                (168.744f + index % 6 * 133f) * SCALE,
                (669.555f - index / 6 * 144f) * SCALE + 14f
            )
            right(
                assets.rosterFont,
                unit.level.toString(),
                (299.048f + index % 6 * 133f) * SCALE,
                (667.398f - index / 6 * 144f) * SCALE + 14f
            )
            assets.rosterNameFont.color = Color.WHITE
            centered(assets.rosterNameFont, unit.name, cx, (625.287f - index / 6 * 144f) * SCALE + 13f)
        }
        assets.font.color = Color.BLACK
        assets.font.draw(batch, "출진 무장 - ${state.selectedIds.size}/${state.maximum}", 144f, 315f)
        drawButton(657.56f, 278.64f, "부대 속성", true, 172f)
    }

    private fun drawSelectedSlots(state: BattlePreparationViewState) {
        repeat(state.maximum) { index ->
            val centerX = (217.336f + index * 100f) * SCALE
            val frame = when {
                index < state.requiredSlotCount -> assets.slotRequired
                index < state.minimum -> assets.slotMinimum
                else -> assets.slotOpen
            }
            val frameHeight = if (index < state.requiredSlotCount) 51.6f else 55.04f
            frame?.let { batch.draw(it, centerX - 43f, 194.915f - frameHeight / 2f, 86f, frameHeight) }
            state.selectedIds.getOrNull(index)?.let { id ->
                val unit = state.units.firstOrNull { it.id == id }
                assets.avatar(unit?.avatarId)?.let { texture ->
                    batch.color = if (id in state.requiredIds) Color(.5f, .5f, .5f, 1f) else Color.WHITE
                    batch.draw(texture, centerX - 41.28f, 193.516f, 82.56f, 82.56f, 0, 301, 48, 48, false, false)
                    batch.color = Color.WHITE
                }
            }
        }
    }

    private fun drawUnitInfo(unit: BattlePreparationUnitView?) {
        unit ?: return
        assets.font.color = Color.BLACK
        assets.font.draw(batch, unit.name, 842.228f, 630f)
        right(assets.font, unit.armName, 1130.73f, 632f)
        centered(assets.font, "무장 정보", 927f, 590f)
        assets.face(unit.headId)?.let { batch.draw(it, 848.51f, 372.86f, 140.35f, 175.44f) }
        listOf("Lv" to unit.level, "EXP" to unit.experience, "HP:" to unit.maxHitPoints, "MP:" to unit.maxMagicPoints)
            .forEachIndexed { index, (label, value) ->
                val y = (613.15f - index * 51f) * SCALE + 13f
                assets.font.draw(batch, label, listOf(1026.3f, 993.6f, 1004.8f, 1001.4f)[index], y)
                right(assets.font, value.toString(), 1119.88f, y)
            }
        unit.traits.chunked(2).forEachIndexed { row, pair ->
            val y = (411.15f - row * 50.5f) * SCALE + 13f
            pair.forEachIndexed { column, (label, value) ->
                if (label.isNotEmpty()) {
                    assets.font.draw(batch, label, if (column == 0) 845.54f else 993.62f, y)
                    right(assets.font, value.toString(), if (column == 0) 981.42f else 1119.88f, y)
                }
            }
        }
    }

    private fun drawBattleSortMenu() {
        batch.color = Color(1f, 1f, 1f, 60f / 255f)
        batch.draw(assets.dim, 0f, 0f, 1280f, 688f)
        batch.color = Color.WHITE
        assets.box1Patch?.draw(batch, 658.06f, 32.25f, 172f, 243.81f)
        listOf("부대 속성", "공격력", "정신력", "방어력", "레벨").forEachIndexed { index, value ->
            assets.outerPatch?.draw(batch, 665.64f, 40.42f + (4 - index) * 46.44f, 155.57f, 43f)
            centered(assets.font, value, 743.43f, 70f + (4 - index) * 46.44f)
        }
    }

    private fun drawBattleView(markerCount: Int) {
        batch.color = Color(1f, 1f, 1f, .667f)
        batch.draw(assets.dim, 1008.372f * SCALE, 320f * SCALE, 480f * SCALE, 480f * SCALE)
        batch.color = Color.WHITE
        assets.battleViewMap?.let { batch.draw(it, 1008.372f * SCALE, 320f * SCALE, 480f * SCALE, 480f * SCALE) }
        repeat(markerCount) { index ->
            assets.outerPatch?.draw(batch, (1104.372f + index * 24f) * SCALE, 680f * SCALE, 24f * SCALE, 24f * SCALE)
            assets.font.color = Color.BLACK
            assets.font.data.setScale(.55f)
            assets.font.draw(batch, (index + 1).toString(), (1110.644f + index * 24f) * SCALE, 699f * SCALE)
            assets.font.data.setScale(1f)
        }
        assets.outerPatch?.draw(batch, 1008.372f * SCALE, 320f * SCALE, 480f * SCALE, 480f * SCALE)
    }

    private fun drawUnitInfoFixture(state: BattlePreparationViewState) {
        batch.color = Color(1f, 1f, 1f, 100f / 255f)
        batch.draw(assets.dim, 0f, 0f, 1280f, 688f)
        batch.color = Color.WHITE
        assets.logo9?.let { drawTiled(it, 197.186f * SCALE, 12f * SCALE, 1094f * SCALE, 776f * SCALE) }
        assets.unitInfoBg1?.let { batch.draw(it, 197.186f * SCALE, 738f * SCALE, 1094f * SCALE, 50f * SCALE) }
        state.units.firstOrNull { it.id == 0 }?.let { assets.face(it.headId) }
            ?.let { batch.draw(it, 230.186f * SCALE, 490.956f * SCALE, 192f * SCALE, 240f * SCALE) }
        assets.unitInfoBoxPatch?.draw(batch, 454.186f * SCALE, 509.642f * SCALE, 358f * SCALE, 144f * SCALE)
        assets.unitInfoBoxPatch?.draw(batch, 454.186f * SCALE, 339.359f * SCALE, 358f * SCALE, 144f * SCALE)
        assets.unitInfoBoxPatch?.draw(batch, 821.986f * SCALE, 71.95f * SCALE, 457f * SCALE, 580.5f * SCALE)
        listOf(
            Triple(825.923f, 712.65f, "무장 열전"), Triple(1014.008f, 712.65f, "부대 특성"),
            Triple(826.481f, 651.471f, "능력"), Triple(956.444f, 651.471f, "장비"), Triple(1086.444f, 651.471f, "마법")
        )
            .forEach { (x, y, text) ->
                assets.unitInfoButtonPatch?.draw(
                    batch,
                    x * SCALE,
                    y * SCALE,
                    (if (text.length > 2) 190f else 130f) * SCALE,
                    60f * SCALE
                )
                assets.font.color = Color.BLACK
                assets.font.draw(batch, text, (x + 12f) * SCALE, (y + 43f) * SCALE)
            }
        assets.font.color = Color.BLACK
        listOf(
            "무장 정보" to (202.186f to 780f), "조조" to (455.186f to 716f), "부대 속성" to (475.267f to 670f),
            "군웅        Lv     3" to (466.186f to 620f), "Exp                  0/100" to (466.186f to 561f),
            "상태" to (476.844f to 496f), "HP                 123/123" to (468.186f to 451f),
            "MP                 36/36" to (468.186f to 395f), "기본 능력" to (853.036f to 644f),
            "무력 82       민첩성 80\n지력 92       운기 84\n지휘 98" to (848.106f to 588f)
        )
            .forEach { (text, point) -> assets.font.draw(batch, text, point.first * SCALE, point.second * SCALE) }
        batch.color = Color.WHITE
    }

    private fun drawButton(x: Float, y: Float, text: String, enabled: Boolean, width: Float = 86f) {
        batch.color = if (enabled) Color.WHITE else Color(.55f, .55f, .55f, 1f)
        assets.outerPatch?.draw(batch, x, y, width, 43f)
        batch.color = Color.WHITE
        assets.font.color = if (enabled) Color.BLACK else Color.DARK_GRAY
        centered(assets.font, text, x + width / 2f, y + 31f)
    }

    private fun centered(font: BitmapFont, text: String, x: Float, y: Float) {
        layout.setText(font, text)
        font.draw(batch, text, x - layout.width / 2f, y)
    }

    private fun right(font: BitmapFont, text: String, x: Float, y: Float) {
        layout.setText(font, text)
        font.draw(batch, text, x - layout.width, y)
    }

    private fun drawTiled(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
        val tileWidth = texture.width * SCALE
        val tileHeight = texture.height * SCALE
        var dy = 0f
        while (dy < height - .01f) {
            val drawnHeight = minOf(tileHeight, height - dy)
            val sourceHeight = (drawnHeight / SCALE).toInt().coerceIn(1, texture.height)
            var dx = 0f
            while (dx < width - .01f) {
                val drawnWidth = minOf(tileWidth, width - dx)
                val sourceWidth = (drawnWidth / SCALE).toInt().coerceIn(1, texture.width)
                batch.draw(
                    texture,
                    x + dx,
                    y + dy,
                    drawnWidth,
                    drawnHeight,
                    0,
                    0,
                    sourceWidth,
                    sourceHeight,
                    false,
                    false
                )
                dx += tileWidth
            }
            dy += tileHeight
        }
    }

    private companion object {
        const val SCALE = .86f
    }
}
