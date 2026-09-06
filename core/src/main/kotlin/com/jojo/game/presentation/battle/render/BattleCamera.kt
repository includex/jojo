// Battle
package com.jojo.game.presentation.battle.render

import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleUnitMoveTimeline

import com.jojo.game.domain.scenario.BattleSlotLayout
import com.jojo.game.domain.scenario.ScenarioUnitFaction

/** 원본 전장 좌표 기하: 96px 타일과 원본 맵 중심을 기준으로 보드·맵·타일 중심 위치를 계산한다. */
internal object SourceBattleMapGeometry {
    /**
     * `renderedTile` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val renderedTile = 96f
    /**
     * `initialMapCenterX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val initialMapCenterX = 640f
    /**
     * `initialMapCenterY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val initialMapCenterY = 864f


    /** 보드 왼쪽: 맵 가로 타일 수와 카메라 X 오프셋으로 표시 영역의 시작 X를 계산한다. */
    fun boardLeft(mapTilesWide: Int, cameraDeltaX: Float): Float =
        initialMapCenterX - mapTilesWide * renderedTile / 2f + cameraDeltaX
    /** 보드 아래: 맵 세로 타일 수와 카메라 Y 오프셋으로 표시 영역의 시작 Y를 계산한다. */
    fun boardBottom(mapTilesHigh: Int, cameraDeltaY: Float): Float =
        initialMapCenterY + mapTilesHigh * renderedTile / 2f - renderedTile + cameraDeltaY


    /** 맵 아래: 원본 전체 맵 텍스처를 출력할 아래쪽 기준 Y를 계산한다. */
    fun mapBottom(mapTilesHigh: Int, cameraDeltaY: Float): Float =
        initialMapCenterY - mapTilesHigh * renderedTile / 2f + cameraDeltaY

    /** 타일 중심: 격자 좌표를 카메라가 적용된 원본 보드 중심 좌표로 변환한다. */
    fun tileCenter(
        tileX: Float,
        tileY: Float,
        mapTilesWide: Int,
        mapTilesHigh: Int,
        cameraDeltaX: Float,
        cameraDeltaY: Float,
    ): Pair<Float, Float> =
        boardLeft(mapTilesWide, cameraDeltaX) + tileX * renderedTile + renderedTile / 2f to
                boardBottom(mapTilesHigh, cameraDeltaY) - tileY * renderedTile + renderedTile / 2f
}

/** 전투 카메라: 전장 이동·중심 맞춤·화면 경계 보정을 원본 맵 크기와 뷰포트 기준으로 유지한다. */
class BattleCamera(
    viewportWidth: Float = 1488.3721f,
    viewportHeight: Float = 800f,
    /** `mapWidth` (Float): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private var mapWidth: Float = 1920f,
    /** `mapHeight` (Float): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private var mapHeight: Float = 1920f,
    /** `edgeMargin` (Float): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val edgeMargin: Float = 96f,
    /** `unitHalfSize` (Float): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val unitHalfSize: Float = 48f,
    /** `initialMapCenterX` (Float): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val initialMapCenterX: Float = 640f,
    /** `initialMapCenterY` (Float): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val initialMapCenterY: Float = 864f,
) {
    /** 현재 뷰포트 너비: 카메라 이동 한계와 화면 안전 영역을 계산하는 기준이다. */
    private var viewportWidth = viewportWidth
    /** 현재 뷰포트 높이: 카메라 이동 한계와 화면 안전 영역을 계산하는 기준이다. */
    private var viewportHeight = viewportHeight
    /** 카메라 X 오프셋: 원본 보드 중심에서의 수평 이동량이다. */
    var x = 0f; private set
    /** 카메라 Y 오프셋: 원본 보드 중심에서의 수직 이동량이다. */
    var y = 0f; private set
    /** 맵 스크롤 통지 횟수: 시나리오 표현이 실제 카메라 이동을 관찰할 때 사용하는 누적값이다. */
    var mapScrollingDispatchCount: Int = 0
        private set


    /** 초기화: 누적 카메라 오프셋을 원본 맵 중심 위치로 되돌린다. */
    fun reset() {
        x = 0f; y = 0f
    }
    /** 콘텐츠 X: 원본 맵 중심을 포함한 실제 콘텐츠 수평 위치다. */
    val contentX: Float get() = initialContentX() + x
    /** 콘텐츠 Y: 원본 맵 중심을 포함한 실제 콘텐츠 수직 위치다. */
    val contentY: Float get() = initialContentY() + y


    /** 뷰포트 갱신: 창 크기 변경 뒤 새 화면 한계에 맞춰 현재 오프셋을 보정한다. */
    fun configureViewport(width: Float, height: Float) {
        viewportWidth = width
        viewportHeight = height
        clamp()
    }
    /** 이동: 입력 이동량을 카메라에 더한 뒤 맵 바깥으로 벗어나지 않도록 제한한다. */
    fun pan(deltaX: Float, deltaY: Float) {
        x += deltaX
        y += deltaY
        clamp()
    }

    /** 화면 보장: 지정 월드 좌표가 안전 여백 밖이면 화면 안으로 들어오도록 카메라를 이동한다. */
    fun ensureVisible(worldX: Float, worldY: Float): Boolean {
        val beforeX = contentX
        val beforeY = contentY
        val safeInset = edgeMargin + unitHalfSize
        var outside = false
        if (worldX < safeInset) {
            x += safeInset - worldX; outside = true
        } else if (worldX > viewportWidth - safeInset) {
            x -= worldX - (viewportWidth - safeInset); outside = true
        }
        if (worldY < safeInset) {
            y += safeInset - worldY; outside = true
        } else if (worldY > viewportHeight - safeInset) {
            y -= worldY - (viewportHeight - safeInset); outside = true
        }
        clamp()
        if (outside) mapScrollingDispatchCount++
        return contentX != beforeX || contentY != beforeY
    }
    /** 강제 중심 맞춤: 지정 월드 좌표를 뷰포트 중심으로 옮긴 뒤 맵 한계로 보정한다. */
    fun forceCenter(worldX: Float, worldY: Float) {
        x += viewportWidth / 2f - worldX
        y += viewportHeight / 2f - worldY
        clamp()
    }
    /** 원본 노드 좌표: authoring 타일 좌표를 원본 Cocos 노드의 화면 좌표로 변환한다. */
    fun sourceNodeScreenPoint(
        tileX: Int,
        tileY: Int,
        authoredX: Boolean = true,
        authoredY: Boolean = true,
    ): Pair<Float, Float> {
        /**
         * `localX` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val localX = if (authoredX) tileX * 96f + 48f - mapWidth / 2f else 0f
        /**
         * `localY` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val localY = if (authoredY) mapHeight / 2f - (tileY * 96f + 48f) else 0f
        return localX + contentX + viewportWidth / 2f to
                localY + contentY + viewportHeight / 2f
    }
    /** 타일 중심 맞춤: 맵 크기를 갱신한 뒤 지정 타일이 화면 중앙에 오도록 오프셋을 계산한다. */
    fun centerTile(tileX: Int, tileY: Int, mapTilesWide: Int, mapTilesHigh: Int) {
        mapWidth = mapTilesWide.coerceAtLeast(1) * 96f
        mapHeight = mapTilesHigh.coerceAtLeast(1) * 96f
        val horizontalLimit = ((mapWidth - viewportWidth) / 2f).coerceAtLeast(0f)
        val verticalLimit = ((mapHeight - viewportHeight) / 2f).coerceAtLeast(0f)
        val targetContentX = (mapWidth / 2f - tileX * 96f).coerceIn(-horizontalLimit, horizontalLimit)
        val targetContentY = (tileY * 96f - mapHeight / 2f).coerceIn(-verticalLimit, verticalLimit)
        x = targetContentX - initialContentX()
        y = targetContentY - initialContentY()
        mapScrollingDispatchCount++
    }

    /** 한계 보정: 현재 맵과 뷰포트 크기에서 허용되는 수평·수직 오프셋 범위로 제한한다. */
    private fun clamp() {
        val halfScrollableX = ((mapWidth - viewportWidth) / 2f).coerceAtLeast(0f)
        val halfScrollableY = ((mapHeight - viewportHeight) / 2f).coerceAtLeast(0f)
        val initialContentX = initialContentX()
        val initialContentY = initialContentY()
        x = x.coerceIn(-halfScrollableX - initialContentX, halfScrollableX - initialContentX)
        y = y.coerceIn(-halfScrollableY - initialContentY, halfScrollableY - initialContentY)
    }

    /**
     * `initialContentX`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun initialContentX(): Float = initialMapCenterX - viewportWidth / 2f
    /**
     * `initialContentY`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun initialContentY(): Float = initialMapCenterY - viewportHeight / 2f
}

/** 첫 진영 카메라 유닛: 진영 슬롯 순서에서 가장 앞선 실제 유닛을 찾아 시작 카메라 대상으로 반환한다. */
internal fun firstCampCameraUnit(units: Iterable<BattleUnit>, camp: Faction): BattleUnit? {
    val range = BattleSlotLayout.rangeFor(camp)
    return units.asSequence()
        .mapNotNull { unit -> battleSlotIndexFor(unit)?.let { it to unit } }
        .filter { (index, _) -> index in range }
        .minByOrNull { (index, _) -> index }
        ?.second
}

/** 전투 슬롯 인덱스: 명시 슬롯 또는 진영별 원본 ID 오프셋으로 유닛의 배치 순서를 계산한다. */
internal fun battleSlotIndexFor(unit: BattleUnit): Int? {
    unit.battleSlot?.let { return it }
    val local = unit.id.substringAfterLast('-').toIntOrNull() ?: return null
    return when (unit.baseFaction) {
        Faction.PLAYER -> local
        Faction.FRIEND -> BattleSlotLayout.friendEnd + local
        Faction.ENEMY, Faction.REINFORCEMENTS -> BattleSlotLayout.enemyStart + local
    }
}

/** 이동 카메라 tick 커서: 이동 timeline에서 아직 처리하지 않은 타일 경계 통과 샘플만 반환한다. */
internal class MovementCameraTickCursor {
    /**
     * `activePath` (List<Pair<Int, Int>>?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var activePath: List<Pair<Int, Int>>? = null
    /**
     * `activeTimeline` (BattleUnitMoveTimeline.Timeline?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var activeTimeline: BattleUnitMoveTimeline.Timeline? = null
    /**
     * `processedTicks` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var processedTicks = 0
    /**
     * `lastElapsed` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var lastElapsed: Float? = null
    /**
     * `timerElapsed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var timerElapsed = 0f

    /** 통과 샘플: 이전 렌더 이후 새로 지난 이동 tick이 있으면 그 시점의 카메라 추적 샘플을 반환한다. */
    fun crossed(
        path: List<Pair<Int, Int>>,
        timeline: BattleUnitMoveTimeline.Timeline?,
        elapsed: Float,
    ): List<BattleUnitMoveTimeline.Sample> {
        if (activePath !== path || activeTimeline != timeline || (lastElapsed?.let { elapsed < it } == true)) {
            activePath = path
            activeTimeline = timeline
            processedTicks = 0
            lastElapsed = null
            timerElapsed = 0f
        }
        if (path.size < 2) {
            lastElapsed = elapsed
            return emptyList()
        }
        /**
         * `moveTimeline` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val moveTimeline = requireNotNull(timeline) { "move2 path needs its authored timeline" }
        /**
         * `previous` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val previous = lastElapsed
        lastElapsed = elapsed
        if (previous == null) return emptyList()
        timerElapsed += (elapsed - previous).coerceAtLeast(0f)
        if (processedTicks >= moveTimeline.movementTicks.size || timerElapsed < moveTimeline.secondsPerTile) {
            return emptyList()
        }
        timerElapsed = 0f
        processedTicks++
        /**
         * `movementEndsAt` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val movementEndsAt = moveTimeline.idleAt - .1f
        /**
         * `completingThisRender` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val completingThisRender = elapsed > movementEndsAt + 1e-6f
        /**
         * `sampledElapsed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sampledElapsed = if (completingThisRender) previous else elapsed
        /**
         * `sample` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sample = BattleUnitMoveTimeline.sample(path, moveTimeline, sampledElapsed.coerceAtMost(moveTimeline.idleAt))
        return if (sample.moving || (!completingThisRender && elapsed <= movementEndsAt + 1e-6f)) {
            listOf(sample)
        } else emptyList()
    }
}
