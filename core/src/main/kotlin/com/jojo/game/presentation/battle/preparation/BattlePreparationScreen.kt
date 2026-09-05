package com.jojo.game.presentation.battle.preparation

import com.jojo.game.BattleViewLayer
import com.jojo.game.GameDataCatalog
import com.jojo.game.JojoGame
import com.jojo.game.ScenarioJoinBattleLimit
import com.jojo.game.StartBattleSortRoute
import com.jojo.game.domain.campaign.*
import com.jojo.game.presentation.battle.preparation.evidence.BattlePreparationTraceRecorder

import com.badlogic.gdx.*

internal data class CampaignE2eBattlePreparationState(
    val returnScenario: String,
    val sourceScenario: String,
    val campaignStage: Int,
    val selectedCount: Int,
    val minimum: Int,
    val maximum: Int,
    val cursorSelected: Boolean,
    val canStart: Boolean,
)

/** Preparation lifecycle, input connection, navigation, and capture facade. */
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
    private val captureState = game.requestedCaptureState()
    private val fixture: BattlePreparationFixture = when {
        captureState == "start-battle-unit-info-fixture" -> BattlePreparationFixture.UnitInfo
        captureState == "battle-view-fixture" -> BattlePreparationFixture.BattleView
        captureState?.removeSuffix("-fixture")?.startsWith("start-battle-sort-") == true ->
            BattlePreparationFixture.BattleSort(captureState.removeSuffix("-fixture"))

        else -> BattlePreparationFixture.Standard
    }
    private val battleSort = StartBattleSortRoute()
    private val battleView = BattleViewLayer().also {
        if (fixture == BattlePreparationFixture.BattleView) {
            it.onCreate(0, listOf(4 to 4, 5 to 4, 6 to 4, 7 to 4))
        }
    }
    private val assets = BattlePreparationAssets(
        backgroundId,
        units.joinToString("") { it.name + it.armName },
    )
    private val renderer = BattlePreparationRenderer(assets)
    private val traceRecorder = BattlePreparationTraceRecorder()
    private val inputProcessor: InputProcessor = createInputProcessor()

    init {
        (fixture as? BattlePreparationFixture.BattleSort)?.route?.let { route ->
            battleSort.openFromButton(865.186f, 321f, 50f, true)
            when (route) {
                "start-battle-sort-select" -> battleSort.select(2, true)
                "start-battle-sort-cancel" -> battleSort.cancel(true)
            }
        }
    }

    override fun show() = BattlePreparationInputConnection.install(Gdx.input, inputProcessor)

    override fun render(delta: Float) {
        renderer.render(viewState())
        if (fixture == BattlePreparationFixture.BattleView) {
            game.writeRenderEventLogIfRequested()
            return
        }
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    /**
     * 공개 메서드 `renderEventLog`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun renderEventLog(): String = traceRecorder.renderEvents(viewState())

    /**
     * 공개 메서드 `compositionTrace`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun compositionTrace(): String = traceRecorder.composition(viewState())

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
        fixture = fixture,
        sortOpen = battleSort.open,
        battleViewMarkerCount = battleView.markers().size,
    )

    /** Read-only state for production E2E; changes still enter through the InputProcessor. */
    internal fun campaignE2eState() = CampaignE2eBattlePreparationState(
        returnScenario = returnScenario,
        sourceScenario = sourceScenario,
        campaignStage = game.campaignStage(),
        selectedCount = controller.selection.size,
        minimum = controller.minimum,
        maximum = controller.maximum,
        cursorSelected = controller.cursorId in controller.selection,
        canStart = controller.canStart,
    )
}

internal object BattlePreparationInputConnection {
    /**
     * 공개 메서드 `install`
     *
     * ### 파라미터
    - `input` (`Input`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `processor` (`InputProcessor`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun install(input: Input, processor: InputProcessor) {
        input.inputProcessor = processor
    }

    /**
     * 공개 메서드 `release`
     *
     * ### 파라미터
    - `input` (`Input`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `processor` (`InputProcessor`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun release(input: Input, processor: InputProcessor) {
        if (input.inputProcessor === processor) input.inputProcessor = null
    }
}
