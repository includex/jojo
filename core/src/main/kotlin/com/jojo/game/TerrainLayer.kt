package com.jojo.game

/**
 * Data-only implementation of recovered-js/modules/battle/TerrainLayer.js.
 *
 * The Cocos layer has two lazy-created panels.  Keeping the complete panel
 * payload apart from widgets makes its source table projection testable and
 * lets the desktop screen render the same 28 terrain rows later.
 */
/**
 * class  `TerrainLayer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class TerrainLayer(
    private val terrain: List<Terrain>,
    private val arms: List<Arm>,
) {
    /**
     * data class  `Terrain`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Terrain(
        val id: Int,
        val name: String,
        /** CFG terrain `flag`, shown as the four physical skill lights. */
        val flag: Int = 0,
        /** CFG terrain `magic`, shown as the four strategy skill lights. */
        val magic: Int = 0,
    )

    /**
     * data class  `Arm`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Arm(
        val id: Int,
        val name: String,
        val terrainRise: Map<Int, Int> = emptyMap(),
        val terrainExpend: Map<Int, Int> = emptyMap(),
    )

    /**
     * enum class  `Tab`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Tab { RISE, EXPEND }

    /**
     * data class  `Cell`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Cell(
        val terrainId: Int,
        val terrainName: String,
        /** Source `Game/Terrain/${index}`; the view owns asset loading. */
        val iconIndex: Int,
        val enabledSkills: List<Boolean>,
        /** Exactly the first thirteen source arms, in cfg arm iteration order. */
        val values: List<Value>,
    )

    /**
     * data class  `Value`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Value(val armId: Int, val armName: String, val text: String, val grade: Int? = null)

    /**
     * data class  `Panel`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Panel(val tab: Tab, val rows: List<Cell>)

    private val initialized = mutableSetOf<Tab>()
    private val panels = mutableMapOf<Tab, Panel>()
    var selected: Tab? = null
        private set

    /** TerrainLayer.sel: lazily construct only on first selection. */
    fun select(tab: Tab): Panel {
        selected = tab
        return panels.getOrPut(tab) {
            initialized += tab
            when (tab) {
                Tab.RISE -> Panel(
                    tab,
                    terrain.take(TERRAIN_LIMIT).mapIndexed { index, source -> riseCell(source, index) })

                Tab.EXPEND -> Panel(
                    tab,
                    terrain.take(TERRAIN_LIMIT).mapIndexed { index, source -> expendCell(source, index) })
            }
        }
    }

    /**
     * 공개 메서드 `isInitialized`
     *
     * ### 파라미터
    - `tab` (`Tab`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun isInitialized(tab: Tab): Boolean = tab in initialized

    private fun riseCell(source: Terrain, iconIndex: Int): Cell = Cell(
        terrainId = source.id,
        terrainName = source.name,
        iconIndex = iconIndex,
        enabledSkills = skillBits(source.flag),
        values = arms.take(ARM_LIMIT).map { arm ->
            val rise = arm.terrainRise[source.id] ?: 100
            Value(arm.id, arm.name, RISE_TEXT[riseGrade(rise)], riseGrade(rise))
        },
    )

    private fun expendCell(source: Terrain, iconIndex: Int): Cell = Cell(
        terrainId = source.id,
        terrainName = source.name,
        iconIndex = iconIndex,
        enabledSkills = skillBits(source.magic),
        values = arms.take(ARM_LIMIT).map { arm ->
            // `_initPanel1`: missing is 0, and costs over 200 become 0.
            val cost = (arm.terrainExpend[source.id] ?: 0).let { if (it > 200) 0 else it }
            Value(arm.id, arm.name, EXPEND_TEXT.getOrElse(cost) { "--" })
        },
    )

    private fun skillBits(flags: Int): List<Boolean> = List(4) { bit -> flags and (1 shl bit) != 0 }

    /**
     * `_initPanel0` performs `rise > 130 ? 5 : floor(range(rise,90,110)/10)-9`.
     * `Instance.range` is max(90,min(rise,110)); keep that odd source bucket
     * behaviour rather than replacing it with a hand-authored grade scale.
     */
    private fun riseGrade(rise: Int): Int =
        if (rise > 130) 5 else ((rise.coerceIn(90, 110) / 10) - 9).coerceIn(0, 5)

    companion object {
        const val TERRAIN_LIMIT = 28
        const val ARM_LIMIT = 13
        private val RISE_TEXT = listOf("★", "◎", "○", "△", "×", "--")
        private val EXPEND_TEXT = listOf("--", "1", "2", "3", "4", "5")
    }
}

/** Source prefab button routing, separated so BattleScreen input has no hidden UI-only branch. */
object TerrainLayerInput {
    // TerrainLayer/bg is offset from the original 1488.372-wide Canvas.
    const val PANEL_X = 274f
    const val PANEL_Y = 100f
    const val PANEL_WIDTH = 1021f
    const val PANEL_HEIGHT = 600f

    sealed interface Action {
        data object Rise : Action
        data object Expend : Action
        data object Close : Action
        data object Consume : Action
    }

    /** button0/button1/button2 respectively; any in-panel touch is consumed by the modal. */
    fun tap(x: Float, y: Float): Action? {
        if (x !in PANEL_X..PANEL_X + PANEL_WIDTH || y !in PANEL_Y..PANEL_Y + PANEL_HEIGHT) return null
        if (y !in PANEL_Y + 11f..PANEL_Y + 71f) return Action.Consume
        return when {
            x in 285f..482f -> Action.Rise
            x in 491f..714f -> Action.Expend
            x in 1165f..1285f -> Action.Close
            else -> Action.Consume
        }
    }
}

/** Item prefab's icon node: 48×48 SpriteFrame at local x=-460, scale 1.4. */
object TerrainLayerSpriteLayout {
    const val PANEL_X = TerrainLayerInput.PANEL_X
    const val PANEL_Y = TerrainLayerInput.PANEL_Y
    const val ICON_X = PANEL_X + 17f

    // Source item icon: 48px sprite at scale 1.4.
    const val ICON_SIZE = 67f
    const val FIRST_ROW_BASELINE_Y = PANEL_Y + 488f
    const val ROW_STEP = 75f

    /**
     * 공개 메서드 `iconY`
     *
     * ### 파라미터
    - `row` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Float`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun iconY(row: Int): Float = FIRST_ROW_BASELINE_Y - row * ROW_STEP - 57f
}
