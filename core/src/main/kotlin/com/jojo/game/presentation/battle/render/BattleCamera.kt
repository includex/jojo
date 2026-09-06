// Battle
package com.jojo.game.presentation.battle.render

import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleUnitMoveTimeline

import com.jojo.game.domain.scenario.BattleSlotLayout
import com.jojo.game.domain.scenario.ScenarioUnitFaction
internal object SourceBattleMapGeometry {
    private const val renderedTile = 96f
    private const val initialMapCenterX = 640f
    private const val initialMapCenterY = 864f


    fun boardLeft(mapTilesWide: Int, cameraDeltaX: Float): Float =
        initialMapCenterX - mapTilesWide * renderedTile / 2f + cameraDeltaX
    fun boardBottom(mapTilesHigh: Int, cameraDeltaY: Float): Float =
        initialMapCenterY + mapTilesHigh * renderedTile / 2f - renderedTile + cameraDeltaY


    fun mapBottom(mapTilesHigh: Int, cameraDeltaY: Float): Float =
        initialMapCenterY - mapTilesHigh * renderedTile / 2f + cameraDeltaY

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
class BattleCamera(
    viewportWidth: Float = 1488.3721f,
    viewportHeight: Float = 800f,
    private var mapWidth: Float = 1920f,
    private var mapHeight: Float = 1920f,
    private val edgeMargin: Float = 96f,
    private val unitHalfSize: Float = 48f,
    private val initialMapCenterX: Float = 640f,
    private val initialMapCenterY: Float = 864f,
) {
    private var viewportWidth = viewportWidth
    private var viewportHeight = viewportHeight
    var x = 0f; private set
    var y = 0f; private set
    var mapScrollingDispatchCount: Int = 0
        private set


    fun reset() {
        x = 0f; y = 0f
    }
    val contentX: Float get() = initialContentX() + x
    val contentY: Float get() = initialContentY() + y


    fun configureViewport(width: Float, height: Float) {
        viewportWidth = width
        viewportHeight = height
        clamp()
    }
    fun pan(deltaX: Float, deltaY: Float) {
        x += deltaX
        y += deltaY
        clamp()
    }

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
    fun forceCenter(worldX: Float, worldY: Float) {
        x += viewportWidth / 2f - worldX
        y += viewportHeight / 2f - worldY
        clamp()
    }
    fun sourceNodeScreenPoint(
        tileX: Int,
        tileY: Int,
        authoredX: Boolean = true,
        authoredY: Boolean = true,
    ): Pair<Float, Float> {
        val localX = if (authoredX) tileX * 96f + 48f - mapWidth / 2f else 0f
        val localY = if (authoredY) mapHeight / 2f - (tileY * 96f + 48f) else 0f
        return localX + contentX + viewportWidth / 2f to
                localY + contentY + viewportHeight / 2f
    }
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

    private fun clamp() {
        val halfScrollableX = ((mapWidth - viewportWidth) / 2f).coerceAtLeast(0f)
        val halfScrollableY = ((mapHeight - viewportHeight) / 2f).coerceAtLeast(0f)
        val initialContentX = initialContentX()
        val initialContentY = initialContentY()
        x = x.coerceIn(-halfScrollableX - initialContentX, halfScrollableX - initialContentX)
        y = y.coerceIn(-halfScrollableY - initialContentY, halfScrollableY - initialContentY)
    }

    private fun initialContentX(): Float = initialMapCenterX - viewportWidth / 2f
    private fun initialContentY(): Float = initialMapCenterY - viewportHeight / 2f
}
internal fun firstCampCameraUnit(units: Iterable<BattleUnit>, camp: Faction): BattleUnit? {
    val range = BattleSlotLayout.rangeFor(camp)
    return units.asSequence()
        .mapNotNull { unit -> battleSlotIndexFor(unit)?.let { it to unit } }
        .filter { (index, _) -> index in range }
        .minByOrNull { (index, _) -> index }
        ?.second
}
internal fun battleSlotIndexFor(unit: BattleUnit): Int? {
    unit.battleSlot?.let { return it }
    val local = unit.id.substringAfterLast('-').toIntOrNull() ?: return null
    return when (unit.baseFaction) {
        Faction.PLAYER -> local
        Faction.FRIEND -> BattleSlotLayout.friendEnd + local
        Faction.ENEMY, Faction.REINFORCEMENTS -> BattleSlotLayout.enemyStart + local
    }
}
internal class MovementCameraTickCursor {
    private var activePath: List<Pair<Int, Int>>? = null
    private var activeTimeline: BattleUnitMoveTimeline.Timeline? = null
    private var processedTicks = 0
    private var lastElapsed: Float? = null
    private var timerElapsed = 0f

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
        val moveTimeline = requireNotNull(timeline) { "move2 path needs its authored timeline" }
        val previous = lastElapsed
        lastElapsed = elapsed
        if (previous == null) return emptyList()
        timerElapsed += (elapsed - previous).coerceAtLeast(0f)
        if (processedTicks >= moveTimeline.movementTicks.size || timerElapsed < moveTimeline.secondsPerTile) {
            return emptyList()
        }
        timerElapsed = 0f
        processedTicks++
        val movementEndsAt = moveTimeline.idleAt - .1f
        val completingThisRender = elapsed > movementEndsAt + 1e-6f
        val sampledElapsed = if (completingThisRender) previous else elapsed
        val sample = BattleUnitMoveTimeline.sample(path, moveTimeline, sampledElapsed.coerceAtMost(moveTimeline.idleAt))
        return if (sample.moving || (!completingThisRender && elapsed <= movementEndsAt + 1e-6f)) {
            listOf(sample)
        } else emptyList()
    }
}
