package com.jojo.game

/**
 * Data-only implementation of recovered-js/modules/battle/TerrainLayer.js.
 *
 * The Cocos layer has two lazy-created panels.  Keeping the complete panel
 * payload apart from widgets makes its source table projection testable and
 * lets the desktop screen render the same 28 terrain rows later.
 */

class TerrainLayer(
    private val terrain: List<Terrain>,
    private val arms: List<Arm>,
) {

    data class Terrain(
        val id: Int,
        val name: String,
        /** 네 개의 물리 스킬 표시등에 쓰는 지형 플래그이다. */
        val flag: Int = 0,
        /** 네 개의 전략 스킬 표시등에 쓰는 지형 마법 플래그이다. */
        val magic: Int = 0,
    )


    data class Arm(
        val id: Int,
        val name: String,
        val terrainRise: Map<Int, Int> = emptyMap(),
        val terrainExpend: Map<Int, Int> = emptyMap(),
    )


    enum class Tab { RISE, EXPEND }


    data class Cell(
        val terrainId: Int,
        val terrainName: String,
        /** 지형 화면에 표시할 자원 식별자이다. */
        val iconIndex: Int,
        val enabledSkills: List<Boolean>,
        /** 설정 순서대로 정렬한 처음 열세 병과 목록이다. */
        val values: List<Value>,
    )


    data class Value(val armId: Int, val armName: String, val text: String, val grade: Int? = null)


    data class Panel(val tab: Tab, val rows: List<Cell>)

    private val initialized = mutableSetOf<Tab>()
    private val panels = mutableMapOf<Tab, Panel>()
    var selected: Tab? = null
        private set

    /** 첫 지형 선택 시에만 상세 창을 지연 생성한다. */
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
            // `_initPanel1`: 값이 없거나 비용이 200을 넘으면 0으로 표시한다.
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

/** 전투 입력과 분리된 지형 화면 버튼 요청이다. */
object TerrainLayerInput {
    // 지형 창 배경은 원본 1488.372 너비 캔버스를 기준으로 오프셋된다.
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

    /** 세 버튼 요청을 구분하며 창 내부 입력은 모달이 소비한다. */
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

/** 아이템 아이콘의 표시 위치와 크기를 나타낸다. */
object TerrainLayerSpriteLayout {
    const val PANEL_X = TerrainLayerInput.PANEL_X
    const val PANEL_Y = TerrainLayerInput.PANEL_Y
    const val ICON_X = PANEL_X + 17f

    // 원본 아이템 아이콘은 배율 1.4의 48픽셀 스프라이트다.
    const val ICON_SIZE = 67f
    const val FIRST_ROW_BASELINE_Y = PANEL_Y + 488f
    const val ROW_STEP = 75f


    fun iconY(row: Int): Float = FIRST_ROW_BASELINE_Y - row * ROW_STEP - 57f
}
