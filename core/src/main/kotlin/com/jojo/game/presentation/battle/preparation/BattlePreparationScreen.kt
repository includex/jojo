// Battle
package com.jojo.game.presentation.battle.preparation
import com.jojo.game.presentation.battle.render.*

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.JojoGame
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit
import com.jojo.game.presentation.battle.preparation.StartBattleSortRoute
import com.jojo.game.domain.campaign.*
import com.jojo.game.application.runtime.BattlePreparationRuntimeProbe
import com.jojo.game.application.runtime.BattlePreparationPresentation

import com.badlogic.gdx.*

/** BattlePreparationScreen: 전투 시작 전 편성 화면으로, 유닛 선택·배치·입력·전환 상태를 화면 수명주기와 함께 처리한다. */
class BattlePreparationScreen(
    /** `game` (JojoGame): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val game: JojoGame,
    /** `returnScenario` (String): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val returnScenario: String,
    /** `sourceScenario` (String): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val sourceScenario: String,
    /** `limit` (ScenarioJoinBattleLimit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val limit: ScenarioJoinBattleLimit,
    /** `campaign` (CampaignState): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val campaign: CampaignState,
    /** `backgroundId` (Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val backgroundId: Int,
) : ScreenAdapter() {
    /**
     * `data` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val data = GameDataCatalog.load()
    /**
     * `availableIds` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val availableIds = (campaign.joinedUnits + limit.requiredUnitIds)
        .filterNot { it in limit.excludedUnitIds }
        .distinct()
        .sortedWith(compareBy<Int> { data.unitProfile(it)?.armId ?: Int.MAX_VALUE }.thenBy { it })
    /**
     * `controller` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val controller = BattlePreparationController(
        availableIds, limit.requiredUnitIds, limit.minimum, limit.maximum,
    )
    /**
     * `stateFactory` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val stateFactory = BattlePreparationViewStateFactory(data, campaign::unitAttribute)
    /**
     * `units` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val units = stateFactory.units(availableIds)
    /**
     * `presentation` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val presentation = game.runtimeBattlePreparationDriver()?.presentation() ?: BattlePreparationPresentation()
    /**
     * `battleSort` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val battleSort = StartBattleSortRoute()
    /**
     * `battleView` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val battleView = BattleViewLayer().also {
        if (presentation.mapVisible) {
            it.onCreate(0, listOf(4 to 4, 5 to 4, 6 to 4, 7 to 4))
        }
    }
    /**
     * `assets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val assets = BattlePreparationAssets(
        backgroundId,
        units.joinToString("") { it.name + it.armName },
    )
    /**
     * `renderer` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val renderer = BattlePreparationRenderer(assets)
    /**
     * `inputProcessor` (InputProcessor): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val inputProcessor: InputProcessor = createInputProcessor()

    init {
        when (presentation.sortMenu) {
            BattlePreparationPresentation.SortMenuState.OPEN -> battleSort.openFromButton(865.186f, 321f, 50f, true)
            BattlePreparationPresentation.SortMenuState.SELECT_THIRD -> {
                battleSort.openFromButton(865.186f, 321f, 50f, true)
                battleSort.select(2, true)
            }
            BattlePreparationPresentation.SortMenuState.CANCELED -> {
                battleSort.openFromButton(865.186f, 321f, 50f, true)
                battleSort.cancel(true)
            }
            BattlePreparationPresentation.SortMenuState.CLOSED -> Unit
        }
    }

    /**
     * `show`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun show() = BattlePreparationInputConnection.install(Gdx.input, inputProcessor)

    /**
     * `render`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun render(delta: Float) {
        renderer.render(viewState())
    }

    /**
     * `resize`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun resize(width: Int, height: Int) = renderer.resize(width, height)

    /**
     * `hide`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun hide() = BattlePreparationInputConnection.release(Gdx.input, inputProcessor)

    /**
     * `dispose`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun dispose() {
        BattlePreparationInputConnection.release(Gdx.input, inputProcessor)
        renderer.dispose()
        assets.dispose()
    }

    /**
     * `createInputProcessor`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun createInputProcessor() = object : InputAdapter() {
        /**
         * `keyDown`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        override fun keyDown(keycode: Int): Boolean {
            when (keycode) {
                Input.Keys.LEFT -> controller.moveCursor(-1)
                Input.Keys.RIGHT -> controller.moveCursor(1)
                Input.Keys.UP -> controller.moveCursor(-6)
                Input.Keys.DOWN -> controller.moveCursor(6)
                Input.Keys.SPACE -> controller.toggle(controller.cursorId)
                Input.Keys.ENTER -> startBattle()
                Input.Keys.ESCAPE -> game.showScenario(returnScenario)
            }
            return true
        }

        /**
         * `touchDown`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, mouseButton: Int): Boolean {
            val (x, y) = renderer.screenToWorld(screenX, screenY)
            handle(controller.touch(x, y, battleSort.open))
            return true
        }
    }

    /**
     * `handle`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun handle(action: BattlePreparationAction) {
        when (action) {
            BattlePreparationAction.Start -> startBattle()
            BattlePreparationAction.Cancel -> game.showScenario(returnScenario)
            BattlePreparationAction.OpenSort -> battleSort.openFromButton(865.186f, 321f, 50f, true)
            BattlePreparationAction.CancelSort -> battleSort.cancel(true)
            is BattlePreparationAction.SelectSort -> battleSort.select(action.index, true)
            BattlePreparationAction.None, BattlePreparationAction.SelectionChanged -> Unit
        }
    }

    /**
     * `startBattle`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun startBattle() {
        val selectedIds = controller.commit() ?: return
        if (campaign.roster.setBattleRoster(selectedIds, limit)) {
            game.showBattleSandbox(sourceScenario, returnScenario)
        }
    }

    /**
     * `viewState`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun viewState() = BattlePreparationViewState(
        backgroundId = backgroundId,
        availableIds = availableIds,
        units = units,
        selectedIds = controller.selection,
        requiredIds = controller.requiredIds,
        requiredSlotCount = limit.requiredUnitIds.size,
        minimum = controller.minimum,
        maximum = controller.maximum,
        cursorId = controller.cursorId,
        canStart = controller.canStart,
        detailsVisible = presentation.detailsVisible,
        mapVisible = presentation.mapVisible,
        sortOpen = battleSort.open,
        battleViewMarkerCount = battleView.markers().size,
    )

    /** 읽기 전용 준비 상태를 런타임 검증 모델로 반환합니다. */
    internal fun runtimeProbe() = BattlePreparationRuntimeProbe(
        returnScenario = returnScenario,
        sourceScenario = sourceScenario,
        campaignStage = game.campaignStage(),
        selectedCount = controller.selection.size,
        minimum = controller.minimum,
        maximum = controller.maximum,
        cursorSelected = controller.cursorId in controller.selection,
        canStart = controller.canStart,
        view = viewState(),
    )
}
/**
 * `BattlePreparationInputConnection`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal object BattlePreparationInputConnection {
    /** 준비 화면 입력 프로세서를 등록합니다. */
    fun install(input: Input, processor: InputProcessor) {
        input.inputProcessor = processor
    }

    /** 등록한 준비 화면 입력 프로세서를 해제합니다. */
    fun release(input: Input, processor: InputProcessor) {
        if (input.inputProcessor === processor) input.inputProcessor = null
    }
}
