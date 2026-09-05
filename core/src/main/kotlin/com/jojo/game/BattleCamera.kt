package com.jojo.game

/**
 * Source map geometry after BattleScreen._loadBg has assigned the JSON map
 * width/height and the ScrollView layout has adopted the map sprite size.
 * TITLE_SIZE is 48 in the source and the battle canvas is rendered at 2x.
 */
internal object SourceBattleMapGeometry {
    private const val renderedTile = 96f
    private const val initialMapCenterX = 640f
    private const val initialMapCenterY = 864f

    /**
     * 공개 메서드 `boardLeft`
     *
     * ### 파라미터
    - `mapTilesWide` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `cameraDeltaX` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Float`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun boardLeft(mapTilesWide: Int, cameraDeltaX: Float): Float =
        initialMapCenterX - mapTilesWide * renderedTile / 2f + cameraDeltaX

    /** Bottom edge of source row zero; source Y increases down the map. */
    fun boardBottom(mapTilesHigh: Int, cameraDeltaY: Float): Float =
        initialMapCenterY + mapTilesHigh * renderedTile / 2f - renderedTile + cameraDeltaY

    /**
     * 공개 메서드 `mapBottom`
     *
     * ### 파라미터
    - `mapTilesHigh` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `cameraDeltaY` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Float`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

/** Source BattleScreen ScrollView follow contract (_contains/centerUnit). */
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

    /** Observable equivalent of BattleScreen's synchronous MAP_SCROLLING dispatch. */
    var mapScrollingDispatchCount: Int = 0
        private set

    /**
     * 공개 메서드 `reset`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun reset() {
        x = 0f; y = 0f
    }

    /** Absolute ScrollView.content.position recorded by the source trace. */
    val contentX: Float get() = initialContentX() + x
    val contentY: Float get() = initialContentY() + y

    /**
     * 공개 메서드 `configureViewport`
     *
     * ### 파라미터
    - `width` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `height` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun configureViewport(width: Float, height: Float) {
        viewportWidth = width
        viewportHeight = height
        clamp()
    }

    /** Mirrors dragging the source ScrollView content. */
    fun pan(deltaX: Float, deltaY: Float) {
        x += deltaX
        y += deltaY
        clamp()
    }
    /**
     * Source `_contains` for a screen-space map point.
     *
     * `BattleScreen` dispatches MAP_SCROLLING only from its `a != 0` branch.
     * Returning that observable mutation lets callers retain the source
     * centerUnit invocation while avoiding a synthetic camera transition when
     * the unit was already inside the 96 px edge band.
     */
    /**
     * 공개 메서드 `ensureVisible`
     *
     * ### 파라미터
    - `worldX` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `worldY` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
        // Source dispatches MAP_SCROLLING whenever `_contains` enters its
        // edge branch, even when ScrollView clamping leaves content.position
        // unchanged. Keep event semantics separate from the return value,
        // which reports only a camera-coordinate mutation to trace callers.
        if (outside) mapScrollingDispatchCount++
        return contentX != beforeX || contentY != beforeY
    }

    /** Source centerUnit(unit, 1): center even when the unit is already visible. */
    fun forceCenter(worldX: Float, worldY: Float) {
        x += viewportWidth / 2f - worldX
        y += viewportHeight / 2f - worldY
        clamp()
    }

    /** `_contains(convertToWorldSpaceAR(unit.node))` screen-space point. */
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

    /** Exact source BattleScreen.center(tileX,tileY), including equal-position dispatches. */
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
        // ScrollView clamps its absolute content.position. `x`/`y` are
        // deltas from the authored S_00 position, whose map centre is
        // (640,864), so their legal ranges are intentionally asymmetric.
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

/** Fixed camp slot ranges used by scenario battle instances. */
internal object BattleSlotLayout {
    const val mineCount = 20
    const val friendEnd = 40
    const val enemyStart = mineCount + friendEnd
    const val enemyBlockLength = 80
    const val enemyBlockCount = 3
    const val enemyEnd = enemyBlockCount * enemyBlockLength

    /** Maps a camp-local instance to its stable slot while preserving the sparse friend range. */
    fun slotFor(faction: ScenarioUnitFaction, instanceId: Int, enemyBlockStart: Int = enemyStart): Int =
        when (faction) {
            ScenarioUnitFaction.MINE -> instanceId
            ScenarioUnitFaction.FRIEND -> friendEnd + instanceId
            ScenarioUnitFaction.ENEMY -> enemyBlockStart + instanceId
        }

    /** Game IDs use a camp-local offset while retaining every stable battle slot. */
    fun battleId(faction: ScenarioUnitFaction, battleSlot: Int): String = when (faction) {
        ScenarioUnitFaction.MINE -> "mine-$battleSlot"
        ScenarioUnitFaction.FRIEND -> "friend-${battleSlot - friendEnd}"
        ScenarioUnitFaction.ENEMY -> "enemy-${battleSlot - enemyStart}"
    }

    /**
     * 공개 메서드 `stageKey`
     *
     * ### 파라미터
    - `faction` (`ScenarioUnitFaction`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `battleSlot` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun stageKey(faction: ScenarioUnitFaction, battleSlot: Int): String =
        "${faction.name}:${battleId(faction, battleSlot).substringAfter('-')}"

    /** IDs are the stable game key: mine-N/friend-N/enemy-N. */
    fun indexFor(unit: BattleUnit): Int? {
        unit.battleSlot?.let { return it }
        val local = unit.id.substringAfterLast('-').toIntOrNull() ?: return null
        return when (unit.baseFaction) {
            Faction.PLAYER -> local
            Faction.FRIEND -> friendEnd + local
            Faction.ENEMY, Faction.REINFORCEMENTS -> enemyStart + local
        }
    }

    /**
     * 공개 메서드 `rangeFor`
     *
     * ### 파라미터
    - `camp` (`Faction`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `IntRange`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun rangeFor(camp: Faction): IntRange = when (camp) {
        Faction.PLAYER -> 0 until mineCount
        Faction.FRIEND -> mineCount until friendEnd
        Faction.ENEMY, Faction.REINFORCEMENTS -> enemyStart until enemyEnd
    }
}

/** `_firstUnit` probes `_unitSet[i]`; it does not test visible/HP/effective faction. */
internal fun firstCampCameraUnit(units: Iterable<BattleUnit>, camp: Faction): BattleUnit? {
    val range = BattleSlotLayout.rangeFor(camp)
    return units.asSequence()
        .mapNotNull { unit -> BattleSlotLayout.indexFor(unit)?.let { it to unit } }
        .filter { (index, _) -> index in range }
        .minByOrNull { (index, _) -> index }
        ?.second
}

/**
 * Mirrors Cocos [CallbackTimer.update] for BattleUnit.move2's camera callback.
 *
 * This is deliberately not a catch-up cursor.  Cocos initializes `_elapsed`
 * on its first update and, when a later frame crosses the interval, invokes
 * the callback only once and resets `_elapsed` to zero.  Consequently a slow
 * frame observes the unit's *current* interpolated position and does not emit
 * every ideal movement-interval point which happened to be crossed.
 */
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
        // A same-tile scripted move resolves to only its origin. Source
        // BattleUnit.move does not enter move2 in that case, so no Animation
        // schedule/centerUnit callback exists. Completion sync can still see
        // a retained MOVE visual from the preceding command; clear the cursor
        // contractually by returning no ticks instead of constructing a
        // move2 timeline that requires an edge.
        if (path.size < 2) {
            lastElapsed = elapsed
            return emptyList()
        }
        val moveTimeline = requireNotNull(timeline) { "move2 path needs its authored timeline" }
        val previous = lastElapsed
        lastElapsed = elapsed
        // CallbackTimer's sentinel `_elapsed == -1` branch performs no
        // callback, irrespective of the first frame's dt.
        if (previous == null) return emptyList()
        timerElapsed += (elapsed - previous).coerceAtLeast(0f)
        if (processedTicks >= moveTimeline.movementTicks.size || timerElapsed < moveTimeline.secondsPerTile) {
            return emptyList()
        }
        timerElapsed = 0f
        processedTicks++
        val movementEndsAt = moveTimeline.idleAt - .1f
        val completingThisRender = elapsed > movementEndsAt + 1e-6f
        // Scheduler runs its due callback at the completion boundary while
        // the action manager can consume the remaining moveTo + delay and
        // remove the schedule in that same render. Its node read is therefore
        // the pre-final interpolation retained from the preceding render,
        // never the newly committed integer endpoint.
        val sampledElapsed = if (completingThisRender) previous else elapsed
        val sample = BattleUnitMoveTimeline.sample(path, moveTimeline, sampledElapsed.coerceAtMost(moveTimeline.idleAt))
        // The action manager reaches moveTo's endpoint and, after the .1s
        // delay, unschedules `_handle` before Scheduler can emit a coarse
        // catch-up callback for the held destination. Source accelerated
        // traces therefore retain the last in-flight centerUnit position
        // (for example S_00 actor258 y=6.57), not the integer endpoint.
        return if (sample.moving || (!completingThisRender && elapsed <= movementEndsAt + 1e-6f)) {
            listOf(sample)
        } else emptyList()
    }
}
