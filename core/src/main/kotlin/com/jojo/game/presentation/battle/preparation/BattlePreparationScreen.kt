package com.jojo.game.presentation.battle.preparation
import com.jojo.game.presentation.battle.render.*

import com.jojo.game.GameDataCatalog
import com.jojo.game.JojoGame
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit
import com.jojo.game.presentation.battle.preparation.StartBattleSortRoute
import com.jojo.game.domain.campaign.*
import com.jojo.game.application.runtime.BattlePreparationRuntimeProbe
import com.jojo.game.application.runtime.BattlePreparationPresentation

import com.badlogic.gdx.*

/** 전투 준비 화면의 생명주기·입력·이동·런타임 상태를 관리합니다. */
class BattlePreparationScreen(
    private val game: JojoGame,
    private val returnScenario: String,
    private val sourceScenario: String,
    private val limit: ScenarioJoinBattleLimit,
    private val campaign: CampaignState,
    private val backgroundId: Int,
) : ScreenAdapter() {
    private val data = GameDataCatalog.load()
    private val availableIds = (campaign.joinedUnits + limit.requiredUnitIds)
        .filterNot { it in limit.excludedUnitIds }
        .distinct()
        .sortedWith(compareBy<Int> { data.unitProfile(it)?.armId ?: Int.MAX_VALUE }.thenBy { it })
    private val controller = BattlePreparationController(
        availableIds, limit.requiredUnitIds, limit.minimum, limit.maximum,
    )
    private val stateFactory = BattlePreparationViewStateFactory(data, campaign::unitAttribute)
    private val units = stateFactory.units(availableIds)
    private val presentation = game.runtimeBattlePreparationDriver()?.presentation() ?: BattlePreparationPresentation()
    private val battleSort = StartBattleSortRoute()
    private val battleView = BattleViewLayer().also {
        if (presentation.mapVisible) {
            it.onCreate(0, listOf(4 to 4, 5 to 4, 6 to 4, 7 to 4))
        }
    }
    private val assets = BattlePreparationAssets(
        backgroundId,
        units.joinToString("") { it.name + it.armName },
    )
    private val renderer = BattlePreparationRenderer(assets)
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

    override fun show() = BattlePreparationInputConnection.install(Gdx.input, inputProcessor)

    override fun render(delta: Float) {
        renderer.render(viewState())
    }

    override fun resize(width: Int, height: Int) = renderer.resize(width, height)

    override fun hide() = BattlePreparationInputConnection.release(Gdx.input, inputProcessor)

    override fun dispose() {
        BattlePreparationInputConnection.release(Gdx.input, inputProcessor)
        renderer.dispose()
        assets.dispose()
    }

    private fun createInputProcessor() = object : InputAdapter() {
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

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, mouseButton: Int): Boolean {
            val (x, y) = renderer.screenToWorld(screenX, screenY)
            handle(controller.touch(x, y, battleSort.open))
            return true
        }
    }

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

    private fun startBattle() {
        val selectedIds = controller.commit() ?: return
        if (campaign.roster.setBattleRoster(selectedIds, limit)) {
            game.showBattleSandbox(sourceScenario, returnScenario)
        }
    }

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
