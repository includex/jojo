// Battle Render
package com.jojo.game.presentation.battle.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.domain.battle.BattleObjectAnimationTimeline

/** 전투 맵 오브젝트 렌더 뷰: 게이트·화염·선택 오브젝트를 그릴 현재 좌표, 텍스처, 시각 스냅샷이다. */
internal data class BattleMapObjectRenderView(
    val boardLeft: Float,
    val boardBottom: Float,
    val tileSize: Float,
    val animationClock: Float,
    val gates: List<BattleMapGateRender>,
    val fires: List<BattleMapFireRender>,
    val objects: List<BattleMapAnimatedObjectRender>,
    val objectTexture: Texture?,
)

/** 게이트 렌더 입력: object ID가 선택한 게이트 텍스처와 중앙 타일 좌표를 정의한다. */
internal data class BattleMapGateRender(val objectId: Int, val x: Int, val y: Int, val texture: Texture)

/** 화염 렌더 입력: 활성 화염의 타일 좌표를 정의하며 animation 시작 시각은 renderer가 소유한다. */
internal data class BattleMapFireRender(val x: Int, val y: Int)

/** 선택 오브젝트 렌더 입력: object ID와 타일 좌표를 정의하며 atlas 행 선택은 renderer가 계산한다. */
internal data class BattleMapAnimatedObjectRender(val objectId: Int, val x: Int, val y: Int)

/** 전투 맵 오브젝트 계획기: 게이트 배치와 atlas 행 순환 규칙을 화면 의존성 없이 계산한다. */
internal object BattleMapObjectAnimationPlanner {
    /** 게이트 영역: 원본 3×3 타일 게이트의 왼쪽 아래 위치와 크기를 계산한다. */
    fun gateBounds(boardLeft: Float, boardBottom: Float, tileSize: Float, x: Int, y: Int): BattleMapObjectBounds =
        BattleMapObjectBounds(
            left = boardLeft + x * tileSize - tileSize,
            bottom = boardBottom - y * tileSize - tileSize,
            size = tileSize * 3f,
        )

    /** 화염 atlas 행: 네 행 화염 애니메이션의 현재 source Y를 계산한다. */
    fun fireSourceY(elapsed: Float): Int = sourceY(elapsed, startRow = 0, count = 4)

    /** 선택 오브젝트 atlas 행: object ID별 시작 행과 반복 행 수를 적용해 현재 source Y를 계산한다. */
    fun objectSourceY(objectId: Int, elapsed: Float): Int = when (objectId) {
        0 -> sourceY(elapsed, startRow = 0, count = 4)
        1 -> sourceY(elapsed, startRow = 4, count = 2)
        else -> sourceY(elapsed, startRow = 6, count = 2)
    }

    /**
     * `sourceY`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun sourceY(elapsed: Float, startRow: Int, count: Int): Int =
        BattleObjectAnimationTimeline.sourceY(BattleObjectAnimationTimeline.row(elapsed, startRow, count))
}

/** 전투 맵 오브젝트 영역: 게이트 스프라이트의 실제 출력 위치와 정사각형 크기를 보관한다. */
internal data class BattleMapObjectBounds(val left: Float, val bottom: Float, val size: Float)

/** 전투 맵 오브젝트 렌더러: 오브젝트별 애니메이션 시작 시각을 관리하며 SpriteBatch 출력 순서를 보존한다. */
internal class BattleMapObjectRenderer(private val batch: SpriteBatch) {
    /**
     * `fireAnimationStartedAt` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val fireAnimationStartedAt = mutableMapOf<Pair<Int, Int>, Float>()
    /**
     * `objectAnimationStartedAt` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val objectAnimationStartedAt = mutableMapOf<Triple<Int, Int, Int>, Float>()

    /** 그리기: 게이트를 먼저 출력한 뒤 활성 화염과 선택 오브젝트의 atlas 프레임을 순서대로 출력한다. */
    fun draw(view: BattleMapObjectRenderView) {
        drawGates(view)
        drawAnimatedObjects(view)
    }

    /** 게이트 그리기: actor layer 이전에 3×3 타일 게이트를 원본 위치에 출력한다. */
    fun drawGates(view: BattleMapObjectRenderView) {
        view.gates.forEach { gate ->
            val bounds = BattleMapObjectAnimationPlanner.gateBounds(
                view.boardLeft, view.boardBottom, view.tileSize, gate.x, gate.y,
            )
            batch.color = Color.WHITE
            batch.draw(gate.texture, bounds.left, bounds.bottom, bounds.size, bounds.size)
        }
        batch.color = Color.WHITE
    }

    /** 애니메이션 오브젝트 그리기: 선택 영역 뒤에 활성 화염과 선택 오브젝트 atlas 프레임을 출력한다. */
    fun drawAnimatedObjects(view: BattleMapObjectRenderView) {
        val texture = view.objectTexture ?: return
        val fireKeys = view.fires.mapTo(linkedSetOf()) { it.x to it.y }
        fireAnimationStartedAt.keys.retainAll(fireKeys)
        view.fires.forEach { fire ->
            val startedAt = fireAnimationStartedAt.getOrPut(fire.x to fire.y) { view.animationClock }
            drawAtlasTile(view, texture, fire.x, fire.y, BattleMapObjectAnimationPlanner.fireSourceY(view.animationClock - startedAt))
        }
        val objectKeys = view.objects.mapTo(linkedSetOf()) { Triple(it.objectId, it.x, it.y) }
        objectAnimationStartedAt.keys.retainAll(objectKeys)
        view.objects.forEach { objectState ->
            val startedAt = objectAnimationStartedAt.getOrPut(Triple(objectState.objectId, objectState.x, objectState.y)) {
                view.animationClock
            }
            drawAtlasTile(
                view,
                texture,
                objectState.x,
                objectState.y,
                BattleMapObjectAnimationPlanner.objectSourceY(objectState.objectId, view.animationClock - startedAt),
            )
        }
        batch.color = Color.WHITE
    }

    /** atlas 타일 출력: 현재 행의 48×48 source 영역을 전장 한 타일 크기로 변환해 출력한다. */
    private fun drawAtlasTile(view: BattleMapObjectRenderView, texture: Texture, x: Int, y: Int, sourceY: Int) {
        batch.color = Color.WHITE
        batch.draw(
            texture,
            view.boardLeft + x * view.tileSize,
            view.boardBottom - y * view.tileSize,
            view.tileSize,
            view.tileSize,
            0,
            sourceY,
            48,
            48,
            false,
            false,
        )
    }
}
