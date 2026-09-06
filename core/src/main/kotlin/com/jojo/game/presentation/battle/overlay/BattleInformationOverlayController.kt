// Battle
package com.jojo.game.presentation.battle.overlay

import com.badlogic.gdx.graphics.Texture
import com.jojo.game.presentation.shared.overlay.TerrainLayer
import com.jojo.game.presentation.shared.overlay.TerrainLayerInput
import com.jojo.game.presentation.shared.overlay.PropertyLayer
import com.jojo.game.presentation.shared.overlay.TreasureLayer

/** 전투 보물 목록의 제목, 행 데이터, 현재 선택 상태를 렌더러에 전달한다. */
internal data class BattleTreasureOverlayView(
    val title: String,
    val firstRow: Int,
    val rows: List<BattleTreasureRowView>,
)

/** 보물 하나의 식별자, 아이콘, 발견 여부와 선택 여부를 표현한다. */
internal data class BattleTreasureRowView(
    val id: Int,
    val name: String,
    val icon: Texture?,
    val discovered: Boolean,
    val selected: Boolean,
)
internal class BattleInformationOverlayController(
    private val propertyLayer: PropertyLayer,
    private val terrainLayer: TerrainLayer,
    private val treasureLayer: TreasureLayer,
    private val itemIcon: (Int) -> Texture?,
    private val terrainIcon: (Int) -> Texture?,
) {
    /** 현재 표시 중인 속성·지형·보물 정보의 종류를 구분한다. */
    enum class Mode { PROPERTY, TERRAIN, TREASURE }

    /** 속성·지형·보물 탭의 선택, 스크롤, 닫기를 요청하는 입력이다. */
    sealed interface Intent {
        data class Tap(val x: Float, val y: Float) : Intent
        data class Scroll(val rows: Int) : Intent
        data class SelectPropertyTab(val tab: PropertyLayer.Tab) : Intent
        data class SelectTerrainTab(val tab: TerrainLayer.Tab) : Intent
        data object Close : Intent
    }

    /** 정보 오버레이가 소비한 입력과 닫힌 정보 종류를 보고한다. */
    sealed interface Effect {
        data object None : Effect
        data class Closed(val mode: Mode) : Effect
    }
    data class DispatchResult(val consumed: Boolean, val effect: Effect = Effect.None)

    /** 선택된 정보 종류와 스크롤 행·보물 선택을 보관한다. */
    private sealed interface State {
        data object Hidden : State
        data class Open(
            val mode: Mode,
            val scrollRow: Int = 0,
            val selectedItemId: Int? = null,
        ) : State
    }

    private var state: State = State.Hidden

    fun openProperty() {
        propertyLayer.select(PropertyLayer.Tab.WEAPON)
        state = State.Open(Mode.PROPERTY)
    }

    fun openTerrain() {
        terrainLayer.select(TerrainLayer.Tab.RISE)
        state = State.Open(Mode.TERRAIN)
    }

    fun openTreasure() {
        state = State.Open(Mode.TREASURE)
    }

    fun propertyView(): BattlePropertyOverlayView? {
        val open = state.openFor(Mode.PROPERTY) ?: return null
        val rows = propertyLayer.rows()
        val first = open.scrollRow.coerceIn(0, (rows.size - PROPERTY_VISIBLE_ROWS).coerceAtLeast(0))
        state = open.copy(scrollRow = first)
        return BattlePropertyOverlayView(
            selectedTab = propertyLayer.selected.ordinal,
            firstRow = first,
            rows = rows.map { row ->
                BattlePropertyRowView(
                    icon = itemIcon(row.item.icon),
                    label = row.labels.joinToString("     "),
                    selected = row.item.id == open.selectedItemId,
                )
            },
        )
    }

    fun terrainView(): BattleTerrainOverlayView? {
        val open = state.openFor(Mode.TERRAIN) ?: return null
        val panel = terrainLayer.select(terrainLayer.selected ?: TerrainLayer.Tab.RISE)
        val first = open.scrollRow.coerceIn(0, (panel.rows.size - TERRAIN_VISIBLE_ROWS).coerceAtLeast(0))
        state = open.copy(scrollRow = first)
        return BattleTerrainOverlayView(
            armNames = panel.rows.firstOrNull()?.values.orEmpty().map { it.armName.take(2) },
            rows = panel.rows.drop(first).take(TERRAIN_VISIBLE_ROWS).map { row ->
                BattleTerrainRowView(
                    terrainName = row.terrainName,
                    icon = terrainIcon(row.iconIndex),
                    enabledSkills = row.enabledSkills.toList(),
                    values = row.values.map { BattleTerrainValueView(it.text, it.grade) },
                )
            },
        )
    }

    fun treasureView(): BattleTreasureOverlayView? {
        val open = state.openFor(Mode.TREASURE) ?: return null
        val first = open.scrollRow.coerceIn(0, (treasureLayer.rows.size - TREASURE_VISIBLE_ROWS).coerceAtLeast(0))
        state = open.copy(scrollRow = first)
        return BattleTreasureOverlayView(
            title = treasureLayer.title,
            firstRow = first,
            rows = treasureLayer.rows.map { row ->
                BattleTreasureRowView(
                    id = row.item.id,
                    name = row.item.name,
                    icon = itemIcon(row.item.icon),
                    discovered = row.discovered,
                    selected = row.item.id == open.selectedItemId,
                )
            },
        )
    }

    fun dispatch(intent: Intent): DispatchResult {
        val open = state as? State.Open ?: return DispatchResult(consumed = false)
        return when (intent) {
            Intent.Close -> close(open)
            is Intent.Scroll -> {
                state = open.copy(scrollRow = (open.scrollRow + intent.rows).coerceAtLeast(0))
                DispatchResult(consumed = true)
            }
            is Intent.SelectPropertyTab -> selectProperty(open, intent.tab)
            is Intent.SelectTerrainTab -> selectTerrain(open, intent.tab)
            is Intent.Tap -> tap(open, intent.x, intent.y)
        }
    }

    private fun selectProperty(open: State.Open, tab: PropertyLayer.Tab): DispatchResult {
        if (open.mode != Mode.PROPERTY) return DispatchResult(consumed = false)
        propertyLayer.onTabTouch(tab, TOUCH_END)
        state = open.copy(scrollRow = 0)
        return DispatchResult(consumed = true)
    }

    private fun selectTerrain(open: State.Open, tab: TerrainLayer.Tab): DispatchResult {
        if (open.mode != Mode.TERRAIN) return DispatchResult(consumed = false)
        terrainLayer.select(tab)
        state = open
        return DispatchResult(consumed = true)
    }

    private fun tap(open: State.Open, x: Float, y: Float): DispatchResult = when (open.mode) {
        Mode.PROPERTY -> propertyTap(open, x, y)
        Mode.TERRAIN -> terrainTap(open, x, y)
        Mode.TREASURE -> treasureTap(open, x, y)
    }

    private fun propertyTap(open: State.Open, x: Float, y: Float): DispatchResult {
        if (x !in PROPERTY_LEFT..PROPERTY_RIGHT || y !in PROPERTY_BOTTOM..PROPERTY_TOP) return DispatchResult(consumed = true)
        if (y in PROPERTY_TAB_BOTTOM..PROPERTY_TAB_TOP) {
            if (x in PROPERTY_CLOSE_LEFT..PROPERTY_CLOSE_RIGHT) return close(open)
            propertyTabAt(x)?.let { propertyLayer.onTabTouch(it, TOUCH_END) }
            state = open.copy(scrollRow = 0)
            return DispatchResult(consumed = true)
        }
        if (y in PROPERTY_ROWS_BOTTOM..PROPERTY_ROWS_TOP) {
            val row = ((PROPERTY_ROW_BASELINE - y) / PROPERTY_ROW_HEIGHT).toInt() + open.scrollRow
            val selected = propertyLayer.onRowTouch(row, TOUCH_END)
            state = open.copy(selectedItemId = selected ?: open.selectedItemId)
        }
        return DispatchResult(consumed = true)
    }

    private fun terrainTap(open: State.Open, x: Float, y: Float): DispatchResult {
        when (TerrainLayerInput.tap(x, y)) {
            TerrainLayerInput.Action.Rise -> terrainLayer.select(TerrainLayer.Tab.RISE)
            TerrainLayerInput.Action.Expend -> terrainLayer.select(TerrainLayer.Tab.EXPEND)
            TerrainLayerInput.Action.Close -> return close(open)
            else -> Unit
        }
        state = open
        return DispatchResult(consumed = true)
    }

    private fun treasureTap(open: State.Open, x: Float, y: Float): DispatchResult {
        if (x !in TREASURE_LEFT..TREASURE_RIGHT || y !in TREASURE_BOTTOM..TREASURE_TOP ||
            (x in TREASURE_CLOSE_LEFT..TREASURE_CLOSE_RIGHT && y in TREASURE_CLOSE_BOTTOM..TREASURE_CLOSE_TOP)
        ) return close(open)
        if (y !in TREASURE_ROWS_BOTTOM..TREASURE_ROWS_TOP) return DispatchResult(consumed = true)
        val column = if (x < TREASURE_COLUMN_SPLIT) 0 else 1
        val line = if (y >= TREASURE_SECOND_ROW_TOP) 0 else 1
        val row = treasureLayer.rows.getOrNull(line * 2 + column + open.scrollRow)
        val selected = row?.let { treasureLayer.select(it.item.id) }
        state = open.copy(selectedItemId = selected?.id ?: open.selectedItemId)
        return DispatchResult(consumed = true)
    }

    private fun close(open: State.Open): DispatchResult {
        if (open.mode == Mode.PROPERTY) propertyLayer.onCancel(TOUCH_END)
        state = State.Hidden
        return DispatchResult(consumed = true, effect = Effect.Closed(open.mode))
    }

    private fun State.openFor(mode: Mode): State.Open? = (this as? State.Open)?.takeIf { it.mode == mode }

    private fun propertyTabAt(x: Float): PropertyLayer.Tab? = when (((x - PROPERTY_TAB_START) / PROPERTY_TAB_WIDTH).toInt()) {
        0 -> PropertyLayer.Tab.WEAPON
        1 -> PropertyLayer.Tab.ARMOR
        2 -> PropertyLayer.Tab.AUXILIARY
        3 -> PropertyLayer.Tab.PROPERTY
        else -> null
    }

    private companion object {
        const val TOUCH_END = 2
        const val PROPERTY_VISIBLE_ROWS = 7
        const val TERRAIN_VISIBLE_ROWS = 6
        const val TREASURE_VISIBLE_ROWS = 4
        const val PROPERTY_LEFT = 247f
        const val PROPERTY_RIGHT = 1241f
        const val PROPERTY_BOTTOM = 48f
        const val PROPERTY_TOP = 754f
        const val PROPERTY_TAB_BOTTOM = 58f
        const val PROPERTY_TAB_TOP = 112f
        const val PROPERTY_TAB_START = 259f
        const val PROPERTY_TAB_WIDTH = 146f
        const val PROPERTY_CLOSE_LEFT = 1067f
        const val PROPERTY_CLOSE_RIGHT = 1223f
        const val PROPERTY_ROWS_BOTTOM = 120f
        const val PROPERTY_ROWS_TOP = 650f
        const val PROPERTY_ROW_BASELINE = 614f
        const val PROPERTY_ROW_HEIGHT = 72f
        const val TREASURE_LEFT = 259f
        const val TREASURE_RIGHT = 1229f
        const val TREASURE_BOTTOM = 84f
        const val TREASURE_TOP = 716f
        const val TREASURE_CLOSE_LEFT = 1071f
        const val TREASURE_CLOSE_RIGHT = 1222f
        const val TREASURE_CLOSE_BOTTOM = 91f
        const val TREASURE_CLOSE_TOP = 143f
        const val TREASURE_ROWS_BOTTOM = 288f
        const val TREASURE_ROWS_TOP = 671f
        const val TREASURE_COLUMN_SPLIT = 744f
        const val TREASURE_SECOND_ROW_TOP = 481f
    }
}
