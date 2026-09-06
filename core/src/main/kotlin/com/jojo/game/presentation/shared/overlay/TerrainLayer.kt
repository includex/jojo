// Battle
package com.jojo.game.presentation.shared.overlay

/** TerrainLayer: 지형별 병과 능력치 표를 지연 생성해 전투 지형 창에 제공한다. */

class TerrainLayer(
    /** `terrain` (List<Terrain>): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val terrain: List<Terrain>,
    /** `arms` (List<Arm>): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val arms: List<Arm>,
) {

    /** Terrain: 지형 식별자와 물리·전략 스킬 적용 플래그를 보관한다. */
    data class Terrain(
        /**
         * `id` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String,
        /** 네 개의 물리 스킬 표시등에 쓰는 지형 플래그이다. */
        val flag: Int = 0,
        /** 네 개의 전략 스킬 표시등에 쓰는 지형 마법 플래그이다. */
        val magic: Int = 0,
    )
    /** Arm: 병과별 지형 상승치와 이동 비용 표를 보관한다. */
    data class Arm(
        /**
         * `id` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int,
        /**
         * `name` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val name: String,
        /**
         * `terrainRise` (Map<Int, Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val terrainRise: Map<Int, Int> = emptyMap(),
        /**
         * `terrainExpend` (Map<Int, Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val terrainExpend: Map<Int, Int> = emptyMap(),
    )
    /** Tab: 지형 상승치와 이동 비용 중 표시할 표 종류를 구분한다. */
    enum class Tab { RISE, EXPEND }


    /** Cell: 지형 한 행에 표시할 아이콘·스킬·병과 수치를 묶는다. */
    data class Cell(
        /**
         * `terrainId` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val terrainId: Int,
        /**
         * `terrainName` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val terrainName: String,
        /** 지형 화면에 표시할 자원 식별자이다. */
        val iconIndex: Int,
        /**
         * `enabledSkills` (List<Boolean>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val enabledSkills: List<Boolean>,
        /** 설정 순서대로 정렬한 처음 열세 병과 목록이다. */
        val values: List<Value>,
    )
    /** Value: 특정 병과의 지형 수치를 화면용 문자열과 등급으로 표현한다. */
    data class Value(val armId: Int, val armName: String, val text: String, val grade: Int? = null)
    /** Panel: 선택한 탭에 맞는 지형 행 목록을 보관한다. */
    data class Panel(val tab: Tab, val rows: List<Cell>)

    /**
     * `initialized` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val initialized = mutableSetOf<Tab>()
    /**
     * `panels` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val panels = mutableMapOf<Tab, Panel>()
    /**
     * `selected` (Tab?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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


    /** isInitialized: 지정한 탭의 상세 표가 생성되었는지 반환한다. */
    fun isInitialized(tab: Tab): Boolean = tab in initialized

    /**
     * `riseCell`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun riseCell(source: Terrain, iconIndex: Int): Cell = Cell(
        terrainId = source.id,
        terrainName = source.name,
        iconIndex = iconIndex,
        enabledSkills = skillBits(source.flag),
        values = arms.take(ARM_LIMIT).map { arm ->
            /**
             * `rise` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val rise = arm.terrainRise[source.id] ?: 100
            Value(arm.id, arm.name, RISE_TEXT[riseGrade(rise)], riseGrade(rise))
        },
    )

    /**
     * `expendCell`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `skillBits`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun skillBits(flags: Int): List<Boolean> = List(4) { bit -> flags and (1 shl bit) != 0 }

    /** riseGrade: 원본 지형 상승치 규칙에 따라 표시용 등급을 계산한다. */
    private fun riseGrade(rise: Int): Int =
        if (rise > 130) 5 else ((rise.coerceIn(90, 110) / 10) - 9).coerceIn(0, 5)

    companion object {
        /**
         * `TERRAIN_LIMIT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TERRAIN_LIMIT = 28
        /**
         * `ARM_LIMIT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val ARM_LIMIT = 13
        /**
         * `RISE_TEXT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private val RISE_TEXT = listOf("★", "◎", "○", "△", "×", "--")
        /**
         * `EXPEND_TEXT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private val EXPEND_TEXT = listOf("--", "1", "2", "3", "4", "5")
    }
}

/** TerrainLayerInput: 지형 창 안의 탭·닫기 버튼 터치를 동작으로 변환한다. */
object TerrainLayerInput {
    /** PANEL_X: 원본 화면 좌표계에서 지형 창이 시작하는 가로 위치이다. */
    const val PANEL_X = 274f
    /**
     * `PANEL_Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_Y = 100f
    /**
     * `PANEL_WIDTH` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_WIDTH = 1021f
    /**
     * `PANEL_HEIGHT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_HEIGHT = 600f

    /**
     * `Action`: 관련 상태와 동작을 묶는 interface다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    sealed interface Action {
        /**
         * `Rise`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Rise : Action
        /**
         * `Expend`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Expend : Action
        /**
         * `Close`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Close : Action
        /**
         * `Consume`: 관련 상태와 동작을 묶는 object다.
         * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
         */

        data object Consume : Action
    }

    /** tap: 지형 창의 터치 좌표를 탭 전환·닫기·입력 소비 동작으로 판별한다. */
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

/** TerrainLayerSpriteLayout: 지형 창 아이콘의 좌표와 크기를 계산한다. */
object TerrainLayerSpriteLayout {
    /**
     * `PANEL_X` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_X = TerrainLayerInput.PANEL_X
    /**
     * `PANEL_Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val PANEL_Y = TerrainLayerInput.PANEL_Y
    /**
     * `ICON_X` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val ICON_X = PANEL_X + 17f

    /** ICON_SIZE: 원본 아이콘 배율을 반영한 화면 표시 크기이다. */
    const val ICON_SIZE = 67f
    /**
     * `FIRST_ROW_BASELINE_Y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val FIRST_ROW_BASELINE_Y = PANEL_Y + 488f
    /**
     * `ROW_STEP` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    const val ROW_STEP = 75f


    /** iconY: 행 번호로부터 아이콘의 세로 표시 좌표를 계산한다. */
    fun iconY(row: Int): Float = FIRST_ROW_BASELINE_Y - row * ROW_STEP - 57f
}
