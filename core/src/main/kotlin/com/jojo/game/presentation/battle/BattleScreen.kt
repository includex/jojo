// Battle
package com.jojo.game.presentation.battle
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.infrastructure.data.ScenarioCatalog
import com.jojo.game.presentation.shared.overlay.*
import com.jojo.game.presentation.shared.KoreanFont
import com.jojo.game.presentation.shared.InfoBaseValueAnimation

import com.jojo.game.presentation.battle.overlay.*

import com.jojo.game.presentation.scenario.overlay.*
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.domain.battle.*
import com.jojo.game.presentation.battle.assets.*
import com.jojo.game.presentation.battle.bootstrap.BattleBootstrapCallbackState
import com.jojo.game.presentation.battle.bootstrap.BattleInitLayer
import com.jojo.game.presentation.battle.bootstrap.completeInitialBattleOperation
import com.jojo.game.presentation.battle.input.*
import com.jojo.game.presentation.battle.render.*
import com.jojo.game.presentation.battle.route.BattlePresentationConfiguration
import com.jojo.game.presentation.battle.evidence.*
import com.jojo.game.presentation.battle.fixture.BattleCaptureFixtureCoordinator
import com.jojo.game.presentation.battle.fixture.BattleCaptureFixtureConfiguration
import com.jojo.game.presentation.battle.fixture.BattleCaptureRouteCoordinator
import com.jojo.game.presentation.battle.fixture.BattleAutoBattleRouteFixtureController
import com.jojo.game.presentation.battle.fixture.BattleCharacterRouteFixtureCoordinator
import com.jojo.game.presentation.battle.fixture.BattleCharacterRouteFixturePort
import com.jojo.game.presentation.battle.fixture.BattleCommandRouteFixtureController
import com.jojo.game.presentation.battle.fixture.BattleItemUpgradeRouteFixtureController
import com.jojo.game.presentation.battle.fixture.BattleJiqiRouteFixtureController
import com.jojo.game.presentation.battle.fixture.BattleMagickRouteFixtureController
import com.jojo.game.presentation.battle.fixture.BattleRouteFixtureController
import com.jojo.game.presentation.battle.fixture.BattleUsePropertyRouteFixtureController
import com.jojo.game.domain.battle.command.*
import com.jojo.game.application.battle.*
import com.jojo.game.domain.battle.turn.*
import com.jojo.game.presentation.battle.edit.*
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioBattleScriptContext
import com.jojo.game.application.battle.bootstrap.BattleBootstrapPhase
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.application.scenario.ScenarioUnitReference
import com.jojo.game.application.battle.BattleRewardFlow
import com.jojo.game.application.runtime.RuntimeBattleCommand
import com.jojo.game.application.runtime.RuntimeBattleFrame
import com.jojo.game.application.runtime.RuntimeBattleRoute
import com.jojo.game.domain.battle.BattleObjectAnimationTimeline
import com.jojo.game.domain.battle.BattleUnitMoveTimeline
import com.jojo.game.application.runtime.BattleRuntimeScreenProbe
import com.jojo.game.application.runtime.RuntimeGridPoint
import com.jojo.game.application.runtime.BattleVerificationRuntime
import com.jojo.game.application.runtime.RuntimeBattleTraceDialogueInput
import com.jojo.game.application.runtime.RuntimeBattleTraceFrameInput
import com.jojo.game.*
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattlePropertyItem
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.MagicLocalSettlement
import com.jojo.game.domain.battle.MagicTarget
import com.jojo.game.domain.battle.magic.BattleMagicProfile
import com.jojo.game.domain.battle.PhysicalAttackPass
import com.jojo.game.domain.battle.PhysicalAttackTargetResult
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.domain.battle.isEnemySide
import com.jojo.game.domain.battle.isPlayerSide
import com.jojo.game.domain.battle.settlement.*
import com.jojo.game.domain.campaign.CampaignEquipment
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.*
import com.jojo.game.presentation.battle.timeline.BattleDeathPresentationTimeline
import com.jojo.game.presentation.battle.timeline.BattleCharacterCamp
import com.jojo.game.presentation.battle.timeline.BattleCharacterMaterial
import com.jojo.game.presentation.battle.timeline.BattleCharacterStrictState
import com.jojo.game.presentation.battle.timeline.BattleMagicPresentation
import com.jojo.game.presentation.battle.timeline.BattlePhysicalPresentationTimeline
import com.jojo.game.presentation.battle.timeline.UnitDeathPresentation
import com.jojo.game.presentation.battle.timeline.hitCallbackEconomyDelta
import com.jojo.game.presentation.battle.timeline.BackMoveAnimation
import com.jojo.game.presentation.battle.timeline.BattleScreenHitReactionDirectionScheduler
import com.jojo.game.presentation.battle.timeline.BattleScreenLoseCondition
import com.jojo.game.presentation.battle.timeline.HarmNumberAnimation
import com.jojo.game.presentation.battle.timeline.MagicEffectAnimation
import com.jojo.game.presentation.battle.timeline.TimedBattleMutation
import com.jojo.game.presentation.battle.timeline.UnitActionAnimation
import com.jojo.game.presentation.battle.timeline.UnitAnimationKind
import com.jojo.game.presentation.battle.timeline.UnitMoveAnimation
import com.jojo.game.presentation.battle.combat.*
import com.jojo.game.presentation.battle.settlement.BattleSettlementPresentationController
import com.jojo.game.presentation.battle.settlement.BattleSettlementOperationCoordinator
import com.jojo.game.presentation.battle.settlement.BattleSettlementOperationPort
import com.jojo.game.presentation.battle.settlement.SettlementInfoView
import com.jojo.game.presentation.battle.settlement.SettlementInfo2View
import com.jojo.game.presentation.battle.settlement.TurnSettlementOp
import com.jojo.game.presentation.battle.outcome.BattleOutcomePresentationCoordinator
import com.jojo.game.presentation.battle.outcome.LoseSceneFlow
import com.jojo.game.presentation.battle.outcome.LoseSceneRenderEvents
import com.jojo.game.presentation.battle.script.ScriptPresentationTimeline
import com.jojo.game.presentation.battle.script.ScriptedUnitPresentationLifecycle
import com.jojo.game.presentation.battle.script.ScriptedUnitCallbackCoordinator
import com.jojo.game.presentation.battle.script.ScriptedUnitTimedCoordinator
import com.jojo.game.presentation.battle.script.ScriptedUnitActionCoordinator
import com.jojo.game.presentation.battle.script.ScriptedPresentationCoordinator
import com.jojo.game.presentation.battle.script.ScriptedUnitTargetSelector
import com.jojo.game.presentation.battle.ai.AiPresentationCoordinator
import com.jojo.game.presentation.battle.ai.AiPresentationState
import com.jojo.game.presentation.battle.ai.AiPresentationStage
import com.jojo.game.presentation.battle.unit.BattleUnitAttributeStatusRender
import com.jojo.game.presentation.battle.unit.BattleUnitPresentationStore
import com.jojo.game.presentation.battle.unit.BattleUnitPresentationState
import com.jojo.game.presentation.battle.unit.BattleUnitStateRender
import com.jojo.game.presentation.battle.fight.*
import com.jojo.game.presentation.battle.overlay.BattleHelperOverlayController
import com.jojo.game.presentation.battle.overlay.BattleInformationOverlayController
import com.jojo.game.presentation.battle.evidence.BattleFightTraceSerializer
import com.jojo.game.presentation.battle.trace.BattleTraceUnitPresentationInput
import com.jojo.game.presentation.battle.trace.BattleRuntimeScreenProbeInput
import com.jojo.game.presentation.battle.trace.BattleRuntimeProbeCoordinator
import com.jojo.game.presentation.battle.trace.BattleRuntimeProbePort
import com.jojo.game.presentation.battle.trace.BattleRuntimeTraceCoordinator
import com.jojo.game.presentation.battle.verification.BattleScreenVerificationCoordinator
import com.jojo.game.presentation.battle.verification.BattleScreenVerificationInput
import com.jojo.game.presentation.battle.overlay.ItemUpgradeFlow
import com.jojo.game.presentation.shared.overlay.PropertyLayer
import com.jojo.game.presentation.shared.overlay.TreasureLayer
import com.jojo.game.presentation.shared.overlay.UnitInfoLayer
import com.jojo.game.presentation.shared.overlay.MagicInfoLayer
import com.jojo.game.presentation.shared.overlay.MagicUiList
import com.jojo.game.infrastructure.audio.GameAudioPlayer
import com.jojo.game.presentation.battle.overlay.BattleSaveLoadOverlayController
import com.jojo.game.presentation.battle.overlay.BattleSettingsOverlayController
import com.jojo.game.presentation.battle.overlay.BattleTreasureOverlayView
import com.jojo.game.presentation.battle.overlay.BattleForcesOverlayController
import com.jojo.game.presentation.battle.overlay.BattleUnitInfoOverlayController
import com.jojo.game.presentation.battle.unit.BattleSpriteTimeline
import com.jojo.game.presentation.battle.unit.BattleUnitSpriteFrameResolver
import com.jojo.game.presentation.battle.unit.ScriptedUnitVisual
import com.jojo.game.presentation.battle.unit.UnitSpriteFrame
import com.jojo.game.presentation.battle.unit.UnitSpriteSource
import com.jojo.game.presentation.battle.render.BattleGridMapSurface
import com.jojo.game.presentation.battle.render.BattleGridMapSurfaceRenderer
import com.jojo.game.presentation.battle.render.BattleGridRenderView
import com.jojo.game.presentation.battle.render.BattleGridMiniMapMarker
import com.jojo.game.presentation.battle.render.BattleGridMiniMapView

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.viewport.ExtendViewport

/** 전투 화면: 입력·전장 렌더링·전술 행동·모달·시나리오 표현을 한 프레임 흐름으로 연결하는 최상위 조정자다. */

class BattleScreen(
    private val game: JojoGame,
    private val verification: BattleVerificationRuntime,
    private val sourceScenario: String,
    private val returnScenario: String,
    private val campaign: CampaignState,
    private val loadTerrain: (Int) -> BattleTerrainGrid,
) : ScreenAdapter() {
    private fun CampaignEquipmentSlot.attributeLabel() = when (this) {
        CampaignEquipmentSlot.WEAPON -> "공격력"
        CampaignEquipmentSlot.ARMOR -> "방어력"
        CampaignEquipmentSlot.AUXILIARY -> "정신력"
    }
    private enum class SelectAreaFrame(val assetName: String) {
        RED("range-red"),
        GREEN("range-green"),
        BLUE("range-blue"),
        RED_BOX("range-red-box"),
        GREEN_BOX("range-green-box"),
    }
    private data class SelectAreaTile(val x: Int, val y: Int, val frame: SelectAreaFrame)

    private var initialPlayerCampScriptStarted = false
    private var bootstrapPhase = if (verification.active) {
        BattleBootstrapPhase.COMPLETE
    } else {
        BattleBootstrapPhase.SCENE0
    }
    private var resultScene1Observed = false
    private var battleRouteCompleted = false
    private var battleInfoPanelPressed = false
    private val rewardTitleFont: BitmapFont = KoreanFont.create(100, "전투 종료보상금전리품★☆")
    private val sectionTitleFont: BitmapFont = KoreanFont.create(120, "영천의 전투")
    private val overlayAssets = BattleOverlayAssets()
    private val runtimeBattlePresentation = game.runtimeBattlePresentation()
    private val presentationConfiguration = BattlePresentationConfiguration(runtimeBattlePresentation)
    private val captureFixtureConfiguration = BattleCaptureFixtureConfiguration(runtimeBattlePresentation)
    private var otherUnitInfoLayer: OtherUnitInfoLayer? = null
    private var mineUnitInfoLayer: MineUnitInfoLayer? = null
    private var battleEdit2: BattleEditLayer2? = null
    private var battleEdit3Open = false
    private var battleEdit3ScenePanelOpen = false
    private var battleRegisterRoute: BattleRegisterRoute? = null
    private val battleCharacterRouteFixtureCoordinator = BattleCharacterRouteFixtureCoordinator()
    private val battleCharacterRouteFixturePort = object : BattleCharacterRouteFixturePort {
        override fun unit(characterId: Int): BattleUnit? = battle.units.values.firstOrNull {
            it.characterId == characterId && battleAvatarId(it) != null
        }

        override fun spriteFrame(action: Int, direction: Int, elapsed: Float, loop: Boolean): UnitSpriteFrame? =
            unitSpriteFrameResolver.clipFrame(action, direction, elapsed, loop)

        override fun idleSpriteFrame(unit: BattleUnit): UnitSpriteFrame = unitSpriteFrameResolver.idleFrame(unit)
    }
    private val itemUpgradeRouteFixtureController = BattleItemUpgradeRouteFixtureController()
    private val miniMapLayer = MiniMapLayer(setting = 0)
    private var miniMapReady = false
    private var miniMapRouteInstalled = false
    private var roundRouteInstalled = false
    private var roundRouteCallbackCount = 0
    private var losePressedAnswer: Int? = null
    fun compositionTrace(): String =
        BattleCompositionEvidenceRecorder.record(compositionEvidenceView())

    /** 증거 화면 구성: 현재 전투·대화·경로 상태를 증거 투영기가 소비할 불변 입력으로 묶는다. */
    private fun compositionEvidenceView(): BattleCompositionEvidenceView =
        BattleCompositionEvidenceProjector.project(
            BattleCompositionEvidenceProjectionInput(
                animationClock = animationClock(),
                visualAnimationClock = elapsed,
                mapOnlyCapture = mapOnlyCapture,
                sourceScenario = sourceScenario,
                returnScenario = returnScenario,
                battleMenuOpen = battleMenuOpen,
                effectCount = magicEffectAnimations.size,
                openingSayRoute = openingSayRoute,
                dialogueOneRoute = dialogueOneRoute,
                actionCapture = actionCapture?.let { capture ->
                    BattleCompositionEvidenceActionCaptureInput(capture.action, capture.sample)
                },
                winModalRoute = winModalRoute,
                enemyTurnRoute = enemyTurnRoute,
                loseResultRoute = loseResultRoute,
                winResultRoute = winResultRoute,
                units = battle.units.values.map { unit ->
                    compositionEvidenceUnitInput(unit)
                },
                terrainAt = terrainGrid::terrainAt,
                dialogue = scriptRuntime.currentDialogue?.let { current ->
                    BattleCompositionEvidenceDialogueInput(
                        speakerId = current.speakerId,
                        sourceText = current.text,
                        visibleText = dialogueReveal.visibleText,
                        typewriterComplete = dialogueReveal.isComplete,
                    )
                },
                speakerName = { speakerId ->
                    speakerId.toIntOrNull()
                        ?.let(gameDataCatalog::unitProfile)
                        ?.name
                        ?.let(GameDataCatalog::sayLayerUnitName)
                },
                action = actionAnimation?.let { action ->
                    BattleCompositionEvidenceActionInput(action.sourceAction, action.direction, action.endsAt)
                },
                winConditionOpen = winConditionOpen,
                winConditionModal = winConditionLayer != null,
                enemyPlanner = {
                    battle.ai.tracePlanner(474, aiFlags = 1)?.let { plan ->
                        BattleCompositionEvidenceEnemyPlannerInput(
                            characterId = plan.characterId,
                            ai = plan.ai,
                            x = plan.x,
                            y = plan.y,
                            value = plan.value,
                            actionValue = plan.actionValue,
                            targetId = plan.targetId,
                            magicId = plan.magicId,
                        )
                    }
                },
                loseActive = outcomePresentation.loseSceneActive,
                winPromptActive = outcomePresentation.winPromptActive,
            )
        )

    /** 증거 유닛 구성: 현재 스프라이트·프레임 조회 결과에서 캡처에 필요한 유닛 값만 고정한다. */
    private fun compositionEvidenceUnitInput(unit: BattleUnit): BattleCompositionEvidenceUnitInput {
        val scripted = scriptedUnitPresentation.visual(unit.id)
        val selected = scripted?.let { unitSpriteFrameResolver.scriptedFrame(unit, it) }
            ?: unitSpriteFrameResolver.idleFrame(unit)
        return BattleCompositionEvidenceUnitInput(
            id = unit.id,
            visible = unit.visible,
            textureUuid = dynamicTextures.movementAtlasUuid(battleAvatarId(unit)),
            sourceY = selected.sourceY,
            sourceWidth = selected.sourceWidth,
            sourceHeight = selected.sourceHeight,
            characterId = unit.characterId,
            tileX = unit.tileX,
            tileY = unit.tileY,
            scriptedAction = scripted?.action,
            flipX = selected.flipX,
        )
    }

    // Cocos는 SHOW_ALL 방식의 1280×800 기준 캔버스를 사용하므로, 1.86:1 데스크톱 창에서는
    // 전투를 1280×688 FitViewport로 줄이지 않고 보이는 월드를 1488×800으로 넓힌다.
    private val viewport = ExtendViewport(1280f, 800f, OrthographicCamera())
    private val shapes = ShapeRenderer()
    private val batch = SpriteBatch()
    private val cocos8MapSampler = lazy {
        ShaderProgram(
            """attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
void main() { v_color = a_color; v_color.a *= 255.0 / 254.0; v_texCoords = a_texCoord0; gl_Position = u_projTrans * a_position; }""",
            """#ifdef GL_ES
precision highp float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec2 u_texSize;
uniform int u_fragmentCoordinates;
uniform vec2 u_framebufferSize;
uniform vec2 u_worldOrigin;
uniform vec2 u_worldSize;
uniform vec2 u_mapOrigin;
uniform vec2 u_mapSize;
vec4 texel(vec2 index) { return texture2D(u_texture, (index + 0.5) / u_texSize); }
void main() {
  vec2 coordinates = v_texCoords;
  if (u_fragmentCoordinates != 0) {
    vec2 world = u_worldOrigin + gl_FragCoord.xy / u_framebufferSize * u_worldSize;
    coordinates = vec2((world.x - u_mapOrigin.x) / u_mapSize.x, 1.0 - (world.y - u_mapOrigin.y) / u_mapSize.y);
  }
  vec2 samplePoint = coordinates * u_texSize - 0.5;
  vec2 base = floor(samplePoint);
  vec2 fraction = floor(fract(samplePoint) * 256.0 + 0.5) / 256.0;
  vec4 low = mix(texel(base), texel(base + vec2(1.0, 0.0)), fraction.x);
  vec4 high = mix(texel(base + vec2(0.0, 1.0)), texel(base + vec2(1.0, 1.0)), fraction.x);
  vec4 sampled = floor((mix(low, high, fraction.y) * 255.0) + 0.5) / 255.0;
  gl_FragColor = sampled * v_color;
}"""
        ).also { check(it.isCompiled) { "Cocos8 map shader failed: ${it.log}" } }
    }
    private val cocosHighlightSampler = lazy {
        ShaderProgram(
            """attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
void main() { v_color = a_color; v_texCoords = a_texCoord0; gl_Position = u_projTrans * a_position; }""",
            """#ifdef GL_ES
precision highp float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_value;
void main() {
  vec4 color = texture2D(u_texture, v_texCoords) * v_color;
  gl_FragColor = vec4(min(color.rgb + vec3(u_value), vec3(1.0)), color.a);
}""",
        ).also { check(it.isCompiled) { "Cocos highlight shader failed: ${it.log}" } }
    }
    private val cocosGraySampler = lazy {
        ShaderProgram(
            """attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
void main() { v_color = a_color; v_texCoords = a_texCoord0; gl_Position = u_projTrans * a_position; }""",
            """#ifdef GL_ES
precision highp float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
void main() {
  vec4 color = texture2D(u_texture, v_texCoords) * v_color;
  float gray = 0.2126 * color.r + 0.7152 * color.g + 0.0722 * color.b;
  gl_FragColor = vec4(gray, gray, gray, color.a);
}""",
        ).also { check(it.isCompiled) { "Cocos gray shader failed: ${it.log}" } }
    }
    internal val scriptRuntime = ScenarioInterpreter.load(sourceScenario, campaign).apply {
        enableExternalBattlePresentation()
        enableExternalFightPresentation()
        start("scene0")
    }
    private val battleTraceCoordinator = game.requestedBattleTraceRuntime()?.let { configuration ->
        BattleRuntimeTraceCoordinator(configuration, game.runtimeBattleObserver())
    }
    private val yingchuanEntryFlowTracePath = game.requestedYingchuanEntryFlowTracePath()
    private var yingchuanEntryFlowSawInit = false
    private var yingchuanEntryFlowWritten = false
    private val fullTraceRandom get() = battleTraceCoordinator?.randomSource
    private val gameDataCatalog = GameDataCatalog.load()
    private val battleInitLayer = BattleInitLayer()
    private val terrainLayer by lazy { gameDataCatalog.terrainLayer() }
    private val propertyLayer by lazy { PropertyLayer.fromCatalog(gameDataCatalog, campaign.inventory.items) }
    private val treasureLayer by lazy {
        TreasureLayer(
            gameDataCatalog.treasureProfiles().map {
                TreasureLayer.Item(it.id, it.name, it.icon, it.itemType in 26..37, "보물")
            },
            campaign.inventory.discoveredTreasures,
        )
    }
    private val battleSprites = BattleSpriteTimeline.load()
    /** 유닛 frame 판정은 resolver에 위임하고, Screen은 현재 route·animation·unit 조회만 제공한다. */
    private val unitSpriteFrameResolver = BattleUnitSpriteFrameResolver(
        object : BattleUnitSpriteFrameResolver.Port {
            override fun dialogueOneRoute(): Boolean = dialogueOneRoute
            override fun hudRoute(): Boolean = hudRoute
            override fun rewardRouteActive(): Boolean = rewardRouteState != null
            override fun itemUpgradeRouteActive(): Boolean = itemUpgradeRouteState != null
            override fun battleDialogueBlendRoute(): Boolean = battleDialogueBlendRoute
            override fun winConditionRouteActive(): Boolean = winConditionRouteState != null
            override fun animationClock(): Float = this@BattleScreen.animationClock()
            override fun elapsed(): Float = elapsed
            override fun returnScenario(): String = returnScenario
            override fun avatarId(unit: BattleUnit): Int? = battleAvatarId(unit)
            override fun defaultAction(unit: BattleUnit) = unitPresentationStore.stateFor(unit).defaultAction(
                BattleUnitPresentationState.DefaultActionInput(
                    visible = unit.visible,
                    hitPoints = unit.hitPoints,
                    maxHitPoints = unit.maxHitPoints,
                    famous = unit.famous,
                    hasActed = unit.hasActed,
                    poisoned = BattleStatus.POISON in unit.statuses,
                    paralyzed = BattleStatus.PARALYSIS in unit.statuses,
                ),
            )
            override fun transientAnimation(unitId: String): UnitActionAnimation? {
                val now = animationClock()
                return actionAnimation?.takeIf { now < it.endsAt && it.unitId == unitId }
                    ?: hitReactionAnimations[unitId]?.takeIf { now in it.startedAt..<it.endsAt }
                    ?: deathAnimations[unitId]?.takeIf { now in it.startedAt..<it.endsAt }
            }
            override fun movementAnimation(unitId: String): UnitMoveAnimation? =
                movementAnimation?.takeIf { animationClock() < it.endsAt && it.unitId == unitId }
            override fun scriptedVisual(unitId: String): ScriptedUnitVisual? = scriptedUnitPresentation.visual(unitId)
            override fun presentationUnit(unitId: String): BattleUnit? = battle.presentation.presentationUnit(unitId)
            override fun timelineFrame(action: Int, direction: Int, elapsed: Float, loop: Boolean) =
                battleSprites.frame(action, direction, elapsed, loop)
        },
    )
    private val magicEffects = MagicEffectCatalog.load()
    private val unitInfoAssets = BattleUnitInfoAssets()

    private val loadedBattleMapIndex = scriptRuntime.requestedBattleBackgroundMapIndex
    private val terrainGrid = loadTerrain(loadedBattleMapIndex).also { grid ->
        grid.resetOverlays()
        grid.applyObjectOverlays(scriptRuntime.stage.mapObjects.values)
        grid.applyFires(scriptRuntime.stage.fires.values)
    }
    internal val battle = (if (verification.usesTutorialBattle) {
        BattleScenarioFactory.tutorialBattle()
    } else {
        BattleScenarioFactory.fromScriptedUnits(
            scriptRuntime.stage.battleUnits.values,
            scriptRuntime.stage.mapObjects.values.filter { it.enabled && it.objectId > 3 }
                .mapTo(linkedSetOf()) { it.x to it.y },
            gameDataCatalog,
            terrainGrid,
            scriptRuntime.stage.enemyMasterInstanceId,
            scriptRuntime.stage.initialBattleWeather(),
            scriptRuntime.stage.battleWeatherSchedule(),
            scriptRuntime.stage.battleWeatherOffset,
            scriptRuntime.stage.enemyEquipment,
            campaign,
            fullTraceRandom,
        )
    }).also { state ->
        if (scriptRuntime.stage.battleMaxRoundsIncludesFeature) state.setResolvedMaxRounds(scenarioMaxRound())
        else state.setMaxRounds(scenarioMaxRound())
        scriptRuntime.stage.setBattleMovePathResolver(state.movement::scriptedMovePath)
    }
    private val unitPresentationStore = BattleUnitPresentationStore()
    private val pendingFightCommands = ArrayDeque<ScenarioFightCommand>()
    private val fightSprites by lazy { FightSpriteTimeline.load() }
    private val fightPresentation by lazy {
        FightPresentationState(
            isMineUnit = { characterId ->
                liveScriptBattleUnit(characterId)?.isPlayerSide()
            },
            actionPoseAt = fightSprites::pose,
            actionSoundsCrossed = fightSprites::soundEventsCrossed,
        )
    }
    private var activeFightCommand: ScenarioFightCommand? = null
    private var fightCommandSequence = 0L
    private var fightOverlayActive = false

    private val mapFile = battleMapFile(loadedBattleMapIndex + 1)
    private val materializedBattleUnitIds = battle.units.keys.toMutableSet()
    private data class ScriptUnitBaseline(
        val x: Int, val y: Int, val visible: Boolean, val ai: Int,
        val targetId: Int, val targetX: Int, val targetY: Int,
    )
    private var scriptUnitBaseline: Map<Int, ScriptUnitBaseline>? = null
    private val scriptedMovementCameraCursors = mutableMapOf<Int, MovementCameraTickCursor>()
    private val mapTexture: Texture? = mapFile
        ?.let(::Texture)
        ?.also {
            it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            it.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge)
        }
    /** 전장 배경 로드 완료: 지도 텍스처가 준비되면 유닛 텍스처를 선적하고 대기 중인 스크립트를 재개한다. */
    private fun completeBattleBackgroundLoadIfReady() {
        if (!scriptRuntime.hasPendingBattleBackgroundLoad || mapTexture == null) return
        battle.units.values.forEach(::unitTexture)
        scriptRuntime.completeBattleBackgroundLoad()
    }

    private val dynamicTextures = BattleDynamicTextureRepository()
    private val informationOverlay by lazy {
        BattleInformationOverlayController(
            propertyLayer = propertyLayer,
            terrainLayer = terrainLayer,
            treasureLayer = treasureLayer,
            itemIcon = dynamicTextures::itemIcon,
            terrainIcon = dynamicTextures::terrainIcon,
        )
    }
    private val hudAssets = BattleHudAssets()
    private val battleMapRenderer by lazy {
        BattleMapRenderer(
            batch = batch,
            font = font,
            assets = BattleMapRendererAssets(
                selectionTextures = hudAssets.selectAreaTextures,
                cursorTexture = hudAssets.battleCursorTexture,
            ),
        )
    }
    private val battleMapObjectRenderer by lazy { BattleMapObjectRenderer(batch) }
    private val battleGridMapSurfaceRenderer by lazy { BattleGridMapSurfaceRenderer(batch) }
    private val battleActorEffectRenderer by lazy {
        BattleActorEffectRenderer(batch, hudAssets) { cocosHighlightSampler.value }
    }
    /** Actor/effect renderer 입력은 live 상태를 이 Port로만 노출해 composer가 조립한다. */
    private val battleActorEffectViewComposer = BattleActorEffectViewComposer(
        object : BattleActorEffectViewComposer.Port {
            override fun boardLeft(): Float = this@BattleScreen.boardLeft
            override fun boardBottom(): Float = this@BattleScreen.boardBottom
            override fun tileSize(): Float = boardTile
            override fun animationClock(): Float = this@BattleScreen.animationClock()
            override fun stateEffectAnimationClock(): Float = this@BattleScreen.stateEffectAnimationClock()
            override fun dialogueBlendRoute(): Boolean = battleDialogueBlendRoute
            override fun battleMenuOpen(): Boolean = this@BattleScreen.battleMenuOpen
            override fun sourceScenario(): String = this@BattleScreen.sourceScenario
            override fun spriteFrame(unit: BattleUnit): UnitSpriteFrame = unitSpriteFrameResolver.frame(unit)
            override fun activeAction(unitId: String, now: Float): UnitActionAnimation? =
                actionAnimation?.takeIf { now < it.endsAt && it.unitId == unitId }
                    ?: hitReactionAnimations[unitId]?.takeIf { now in it.startedAt..<it.endsAt }
                    ?: deathAnimations[unitId]?.takeIf { now in it.startedAt..<it.endsAt }

            override fun deathAnimationActive(unitId: String, now: Float): Boolean =
                deathAnimations[unitId]?.let { now in it.startedAt..<it.endsAt } == true

            override fun scriptedVisual(unitId: String): ScriptedUnitVisual? = scriptedUnitPresentation.visual(unitId)
            override fun texture(unit: BattleUnit, source: UnitSpriteSource): Texture? = when (source) {
                UnitSpriteSource.ATTACK -> attackTexture(unit) ?: unitTexture(unit)
                UnitSpriteSource.SPECIAL -> specialTexture(unit) ?: unitTexture(unit)
                UnitSpriteSource.MOVEMENT -> unitTexture(unit)
            }

            override fun visualTile(unit: BattleUnit): Pair<Float, Float> = this@BattleScreen.visualTile(unit)
            override fun terrainAt(unit: BattleUnit): Int = terrainGrid.terrainAt(unit.tileX, unit.tileY)
            override fun terrainMask(terrain: Int): Texture? = when (terrain) {
                10 -> hudAssets.terrainMask19
                1 -> hudAssets.terrainMask21
                else -> null
            }

            override fun hpTexture(unit: BattleUnit): Texture? = when (unit.type()) {
                Faction.PLAYER -> hudAssets.mineHpBarTexture
                Faction.FRIEND -> hudAssets.friendHpBarTexture
                Faction.ENEMY, Faction.REINFORCEMENTS ->
                    if (unit.famous) hudAssets.famousEnemyHpBarTexture else hudAssets.enemyHpBarTexture
            }

            override fun hpRatio(unit: BattleUnit, now: Float): Float =
                (healthTimeline.shownHp(unit.id, now, unit.hitPoints).toFloat() /
                    unit.maxHitPoints.coerceAtLeast(1)).coerceIn(0f, 1f)

            override fun attributeStatuses(unit: BattleUnit) = unitPresentationStore.stateFor(unit).attributeStatusIcons
            override fun otherNodesVisible(unit: BattleUnit): Boolean = unit.otherNodesVisible
            override fun stateEffect(unit: BattleUnit) = unitPresentationStore.stateFor(unit).stateAnimation.current()
            override fun stateTexture(textureIndex: Int): Texture? = hudAssets.battleStateTextures.getOrNull(textureIndex)
            override fun magicEffectAnimations(): List<MagicEffectAnimation> = magicEffectAnimations.toList()
            override fun magicEffect(effectId: Int) = magicEffects.effect(effectId)
            override fun magicEffectTexture(effectId: Int): Texture? = dynamicTextures.effect(effectId)
            override fun presentationUnit(unitId: String): BattleUnit? = battle.presentation.presentationUnit(unitId)
            override fun sayTexture(): Texture? = hudAssets.battleSayTexture
            override fun dialogueSpeakerId(): Int? = scriptRuntime.currentDialogue?.speakerId?.toIntOrNull()
        },
    )
    private val fightRenderer by lazy {
        BattleFightRenderer(
            batch = batch,
            font = font,
            dialogueFont = dialogueFont,
            viewport = viewport,
            hudAssets = hudAssets,
            dynamicTextures = dynamicTextures,
            timeline = fightSprites,
            highlightShader = { cocosHighlightSampler.value },
            grayShader = { cocosGraySampler.value },
        )
    }
    private val audio = GameAudioPlayer()
    private val dialogueReveal = SourceTextReveal()
    private val battleInfoReveal = SourceTextReveal()
    private val sayAutoClose = SayLayerAutoClose()
    private val settingsPreferences by lazy { game.settingsPreferences() }
    private val font: BitmapFont = KoreanFont.create(26, buildString {
        append("전술 전투 원본 맵 라운드 아군 적군 단계 턴 최종 종료 증원군 도착 조조 병사 황건적 시나리오로 돌아가기 일대일 대결 대화 아이템 ${scriptRuntime.stage.stageName}")
        append(gameDataCatalog.allBattleNames().joinToString())
        // 대화 화자에는 공병처럼 데이터 카탈로그의 일반 병종명도 포함된다.
        append(gameDataCatalog.allUnitNames().joinToString())
        append(gameDataCatalog.allRetreatTexts().joinToString())
        append(gameDataCatalog.allBattleNames().joinToString())
        append(gameDataCatalog.terrainLayer().select(TerrainLayer.Tab.RISE).rows.joinToString { it.terrainName })
        append(
            gameDataCatalog.terrainLayer()
                .select(TerrainLayer.Tab.RISE).rows.firstOrNull()?.values?.joinToString { it.armName } ?: "")
        append(Gdx.files.internal("scenarios/$sourceScenario.py").readString("UTF-8"))
        Gdx.files.internal("scenarios/R_00.py").takeIf { it.exists() }?.let { append(it.readString("UTF-8")) }
        append("기본 능력 무력 지력 지휘 민첩성 운기 무장 소개 인물 특기 일람 없음 출진 횟수 퇴각 ★◎○△×●--")
        append("환경 설정 클릭하여 설정해 주세요 설정 완료 후 확인을 선택해 주세요 배경 음악 듣기 효과음 듣기 전투시 전장 축소 이미지가 자동으로 표시됩니다 대화창 자동 닫음 체력 바가 유닛 위에 있습니다 텍스트 속도 느림 중간 빠름 정보 설명 자세히 보통 요약 대화창 색상")
        append("진행 상황 유지 어떤 진행 상황을 저장할지 선택해 주세요 따뜻한 알림 오래된 저장 파일일수록 앞에 표시됩니다 취소 진행도 불러오기 읽을 최신 저장 파일이 가장 위에 있습니다")
        append("보물 도감 발견되지 않음 지금까지 발견한 보물 종료 부대 정보 일람 무장명 부대 속성 레벨 체력 공격 방어 정신 폭발 사기 폐쇄 창고 일람 이름 속성 경험치 소지자 무기 방어구 보조")
        append("모든 부대의 명령을 종료하시겠습니까? 자동 전투 위임 예 아니오 취소")
    })
    private val dialogueFont: BitmapFont = KoreanFont.create(36, buildString {
        append(gameDataCatalog.allUnitNames().joinToString())
        append(Gdx.files.internal("scenarios/$sourceScenario.py").readString("UTF-8"))
    })
    private val itemUpgradeFont: BitmapFont = KoreanFont.create(36, "단검유비장비Lv공격력방어력정신력 -> 0123456789")
    private val battleRewardOverlayRenderer by lazy {
        BattleRewardOverlayRenderer(
            batch = batch,
            shapes = shapes,
            titleFont = rewardTitleFont,
            bodyFont = font,
            sectionTitleFont = sectionTitleFont,
            assets = BattleRewardOverlayAssets(
                rewardItemTexture = overlayAssets.rewardItemTexture,
                winConditionBoxPatch = overlayAssets.winConditionBoxPatch,
                sectionBackgroundTexture = overlayAssets.sectionBackgroundTexture,
            ),
        )
    }
    private val battleAutoOverlayRenderer by lazy {
        BattleAutoOverlayRenderer(
            batch = batch,
            labelFont = itemUpgradeFont,
            assets = BattleAutoOverlayAssets(
                unitInfoLogo = unitInfoAssets.unitInfoLogo,
                unitInfoBox = unitInfoAssets.unitInfoBox3,
                toggle = hudAssets.autoBattleToggle,
                checkmark = hudAssets.autoBattleCheckmark,
                banner = hudAssets.autoBattleBanner,
                plate = hudAssets.autoBattlePlate,
            ),
        )
    }
    private val battleTerrainOverlayRenderer by lazy {
        BattleTerrainOverlayRenderer(
            batch = batch,
            font = font,
            assets = BattleTerrainOverlayAssets(
                background = overlayAssets.terrainLayerBackgroundTexture,
                panel = overlayAssets.terrainLayerPanelPatch,
                rowEven = overlayAssets.terrainLayerRowEvenPatch,
                rowOdd = overlayAssets.terrainLayerRowOddPatch,
                verticalLine = overlayAssets.terrainLayerVlinePatch,
            ),
        )
    }
    private val battleSettingsOverlayRenderer by lazy {
        BattleSettingsOverlayRenderer(
            batch = batch,
            font = font,
            assets = BattleSettingsOverlayAssets(
                background = overlayAssets.terrainLayerBackgroundTexture,
                panel = overlayAssets.terrainLayerPanelPatch,
            ),
        )
    }
    private val battleUnitInfoOverlayRenderer by lazy {
        BattleUnitInfoOverlayRenderer(
            batch = batch,
            shapes = shapes,
            font = font,
            assets = BattleUnitInfoOverlayAssets(
                logo = unitInfoAssets.unitInfoLogo,
                box1 = unitInfoAssets.unitInfoBox1,
                box2 = unitInfoAssets.unitInfoBox2,
                box3 = unitInfoAssets.unitInfoBox3,
                background = unitInfoAssets.unitInfoBg,
                verticalLine = unitInfoAssets.unitInfoVline2,
                face = unitInfoAssets.unitInfoFace,
                progress = unitInfoAssets.unitInfoProgress,
                mark2 = unitInfoAssets.unitInfoMark2,
                mark3 = unitInfoAssets.unitInfoMark3,
                mark6 = unitInfoAssets.unitInfoMark6,
            ),
        )
    }
    private val battleHelperOverlayRenderer by lazy {
        BattleHelperOverlayRenderer(
            batch = batch,
            font = font,
            glyphLayout = GlyphLayout(),
            assets = BattleHelperOverlayAssets(
                background = overlayAssets.terrainLayerBackgroundTexture,
                header = overlayAssets.terrainLayerPanelPatch,
                scroll = overlayAssets.winConditionScrollPatch ?: overlayAssets.terrainLayerPanelPatch,
            ),
        )
    }
    private val battleSaveLoadOverlayRenderer by lazy {
        BattleSaveLoadOverlayRenderer(
            batch = batch,
            font = font,
            assets = BattleSaveLoadOverlayAssets(
                background = overlayAssets.terrainLayerBackgroundTexture,
                panel = overlayAssets.terrainLayerPanelPatch,
                rowEven = overlayAssets.terrainLayerRowEvenPatch,
                rowOdd = overlayAssets.terrainLayerRowOddPatch,
            ),
        )
    }
    private val battleForcesOverlayRenderer by lazy {
        BattleForcesOverlayRenderer(
            batch = batch,
            font = font,
            assets = BattleForcesOverlayAssets(
                background = overlayAssets.terrainLayerBackgroundTexture,
                panel = overlayAssets.terrainLayerPanelPatch,
                rowEven = overlayAssets.terrainLayerRowEvenPatch,
                rowOdd = overlayAssets.terrainLayerRowOddPatch,
                verticalLine = overlayAssets.terrainLayerVlinePatch,
            ),
        )
    }
    private val battlePropertyOverlayRenderer by lazy {
        BattlePropertyOverlayRenderer(
            batch = batch,
            font = font,
            assets = BattlePropertyOverlayAssets(
                background = overlayAssets.terrainLayerBackgroundTexture,
                panel = overlayAssets.terrainLayerPanelPatch,
                rowEven = overlayAssets.terrainLayerRowEvenPatch,
                rowOdd = overlayAssets.terrainLayerRowOddPatch,
                verticalLine = overlayAssets.terrainLayerVlinePatch,
            ),
        )
    }
    internal var eventMessage = "턴 종료로 라운드와 이벤트를 확인하세요"
    private var battleMenuOpen = false
    private var battleMenuLayer: MenuLayer? = null
    private var battleMenuOpenedAt = 0f
    private var battleMenuPressedIndex: Int? = null
    private val autoBattlePreferences by lazy { game.preferences("jojo-auto-battle") }
    private val autoBattleFlow by lazy {
        AutoBattleFlow(battleTraceCoordinator == null && autoBattlePreferences.getInteger("TUOGUAN", 0) == 1)
    }
    private val autoBattleRouteFixtureController = BattleAutoBattleRouteFixtureController()
    private var autoBattlePressedTag: Int? = null
    private var autoBattleTogglePressed = false
    private var autoBattlePanelPressed = false
    private val saveLoadOverlay = BattleSaveLoadOverlayController(
        saveRepository = object : SaveLayer.Repository {
            override fun load(index: Int): String? = game.savedCampaignSlot(index)
            override fun save(index: Int) {
                game.saveCampaign(index)
            }
        },
        loadRepository = object : LoadGameLayer.Repository {
            override fun load(index: Int) = game.loadCampaignSlot(index)
            override fun savedPage() = game.savedLoadPage()
            override fun savePage(page: Int) = game.saveLoadPage(page)
            override fun featureEnabled(name: String) = name == "ZDBHSW"
            override fun versionCode() = 1
            override fun restore(index: Int, raw: String, route: LoadGameLayer.RestoreRoute) =
                game.restoreCampaignSlot(index, raw, route)
        },
    )
    private val settingLayer by lazy {
        SettingLayer(object : SettingLayer.Store {
            private val p = settingsPreferences
            override fun getInt(key: String, default: Int) = p.getInteger(key, default)
            override fun putInt(key: String, value: Int) {
                p.putInteger(key, value).flush()
            }
        }, featureEnvironment = { game.settingFeatureEnvironment("Battle") })
    }
    private val settingsOverlay by lazy { BattleSettingsOverlayController(settingLayer) }
    private val forcesOverlay = BattleForcesOverlayController()
    private val unitInfoOverlay = BattleUnitInfoOverlayController()
    private var jiqiLayer: JiQiLayer? = null
    private var jiqiPressed = false
    private val jiqiRouteFixture = presentationConfiguration.jiqiRoute
    private val jiqiRouteFixtureController = BattleJiqiRouteFixtureController()
    private var magickListLayer: MagicUiList? = null
    private var magickInfoLayer: MagicInfoLayer? = null
    private var magickPressedRow: Int? = null
    private var magickPressedAt = 0f
    private var magickCancelPressed = false
    private var magickInfoSuppressRelease = false
    private val magickRouteFixtureController = BattleMagickRouteFixtureController()
    private val magickRouteState get() = presentationConfiguration.magickRoute
    private var usePropertyLayer: UsePropertyLayer? = null
    private var usePropertyDetail: UsePropertyLayer.Property? = null
    private var usePropertyPressedRow: Int? = null
    private var usePropertyCancelPressed = false
    private var usePropertyPanelPressed = false
    private var usePropertyDetailSuppressRelease = false
    private val usePropertyRouteFixtureController = BattleUsePropertyRouteFixtureController()
    private val usePropertyRouteState get() = presentationConfiguration.usePropertyRoute
    private val helperOverlay = BattleHelperOverlayController()
    private var winConditionOpen = false
    private var scriptWinConditions: WinConditionsLayer? = null
    private var winConditionLayer: WinConBoxLayer? = null
    private var winConditionButtonPressed = false
    private var noActionIndex = 0
    private val battleCamera = BattleCamera(
        mapWidth = terrainGrid.width * 96f,
        mapHeight = terrainGrid.height * 96f,
    )
    private val battleInputRouter = BattleInputRouter()
    private var elapsed = 0f
    private var battleElapsed = 0f
    private var positionedDialogueRevision = -1L
    private var actionAnimation: UnitActionAnimation? = null
    private var scriptedAttackCallbackEndsAt = Float.NEGATIVE_INFINITY
    internal var movementAnimation: UnitMoveAnimation? = null
    private val backMoveAnimations = mutableMapOf<String, BackMoveAnimation>()
    private var pendingBattleScriptPassesAfterAction = 0
    private var pendingBattleActionCommitted = false
    private var pendingBattleSettlementActorId: String? = null
    private var pendingBattleCompletedScriptPasses = 0
    private val hitReactionAnimations = mutableMapOf<String, UnitActionAnimation>()
    private val deathAnimations = mutableMapOf<String, UnitActionAnimation>()
    internal val deathTimeline = BattleDeathPresentationTimeline(object : BattleDeathPresentationTimeline.Port {
        override val now: Float get() = animationClock()
        override val scriptComplete: Boolean get() = scriptRuntime.state == PlaybackState.COMPLETE
        override val dialogueActive: Boolean get() = scriptRuntime.currentDialogue != null

        override fun collectDyingUnits(): List<BattleDeathPresentationTimeline.DeathUnit> =
            collectDyingPresentationUnits()

        override fun runScript() {
            runBattleScript()
        }

        override fun focusUnit(unitId: String) {
            battle.presentation.presentationUnit(unitId)?.let(::focusCameraOn)
        }

        override fun presentRetireDialogue(unit: BattleDeathPresentationTimeline.DeathUnit) {
            scriptRuntime.presentExternalBattleDialogue(
                Dialogue(unit.dialogueCharacterId, requireNotNull(unit.retireMessage)),
            )
        }

        override fun startDeathAnimation(
            unit: BattleDeathPresentationTimeline.DeathUnit,
            startsAt: Float,
            endsAt: Float,
        ) {
            val battleUnit = battle.presentation.presentationUnit(unit.unitId) ?: return
            battleUnit.retreatFlag = true
            battleUnit.otherNodesVisible = false
            battleUnit.setHpcur(0)
            deathAnimations[unit.unitId] = UnitActionAnimation(
                unit.unitId,
                UnitAnimationKind.DEATH,
                unit.direction,
                startsAt,
                endsAt,
                sourceAction = unit.sourceAction,
            )
        }

        override fun completeDeathAnimation(unit: BattleDeathPresentationTimeline.DeathUnit) {
            deathAnimations.remove(unit.unitId)
            battle.presentation.presentationUnit(unit.unitId)?.let { battleUnit ->
                battle.presentation.incrementUnitRetreat(battleUnit)
                battleUnit.setHpcur(unit.originalHp)
                battleUnit.visible = false
                battleUnit.characterId?.let { characterId ->
                    scriptRuntime.stage.setBattleUnitVisibility(characterId, false)
                }
            }
            battle.presentation.completeScriptedUnitHide(unit.unitId)
        }

        override fun completeCheckpoint(checkpoint: BattleDeathPresentationTimeline.Checkpoint) {
            when (checkpoint) {
                BattleDeathPresentationTimeline.Checkpoint.CAMP_START ->
                    turnController.completeCampDeathPresentation()
                BattleDeathPresentationTimeline.Checkpoint.CAMP_RESTORE ->
                    turnController.completeCampRestoreDeathPresentation()
                BattleDeathPresentationTimeline.Checkpoint.ROUND_START ->
                    turnController.completeRoundDeathPresentation()
            }
        }
    })
    private val settlementPresentation = BattleSettlementPresentationController()
    private val settlementOperationCoordinator = BattleSettlementOperationCoordinator()
    private val settlementOperationPort = object : BattleSettlementOperationPort {
        override fun unitsById(): Map<String, BattleUnit> =
            (battle.units.values + battle.presentation.pendingPresentationUnits()).associateBy { it.id }

        override fun presentationUnit(unitId: String): BattleUnit? = battle.presentation.presentationUnit(unitId)
        override fun statusMeff(sourceStatusIndex: Int, meffSlot: Int): Int? =
            gameDataCatalog.statusMeff(sourceStatusIndex, meffSlot)
        override fun skillName(skillId: Int): String = gameDataCatalog.skillName(skillId)
        override fun magicName(magicId: Int): String? = gameDataCatalog.magicProfile(magicId)?.name
        override fun namedMeff(name: String): Int? = gameDataCatalog.namedMeff(name)
        override fun actionDuration(actionId: Int, direction: Int): Float = requireSourceActionDuration(actionId, direction)
        override fun meffDuration(effectId: Int): Float? = magicEffects.effect(effectId)?.duration
        override fun autoCloseInfo2(text: String): Boolean = text.length < 10 || settingsPreferences.getInteger(
            SettingLayer.GAME_SETTING,
            SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
        ) and SettingLayer.AUTO_CLOSE != 0
    }
    private var settlementMeffEndsAt: Float? = null
    private var settlementItemUpgradeStarted = false
    private val scriptedUnitPresentation = ScriptedUnitPresentationLifecycle()
    private val scriptedUnitCallbacks = ScriptedUnitCallbackCoordinator(
        scriptedUnitPresentation,
        object : ScriptedUnitCallbackCoordinator.Port {
            override fun now() = animationClock()
            override fun consumeHide() = scriptRuntime.stage.consumeUnitHideRequest()
            override fun consumeShow() = scriptRuntime.stage.consumeUnitShowRequest()
            override fun consumePosts() = scriptRuntime.stage.consumeUnitPostsRequest()
            override fun dialogueIsActive() = scriptRuntime.currentDialogue != null
            override fun presentDialogue(dialogue: Dialogue) = scriptRuntime.presentExternalBattleDialogue(dialogue)
            override fun hideUnit(request: ScenarioUnitHideRequest): BattleUnit? =
                (battle.units.values + battle.presentation.pendingPresentationUnits()).firstOrNull { candidate ->
                    request.battleUnitId?.let { candidate.id == it }
                        ?: (candidate.id == scriptRuntime.stage.battleUnitForCharacterId(request.unitId)?.battleId)
                }
            override fun showUnit(request: ScenarioUnitShowRequest): BattleUnit? =
                (battle.units.values + battle.presentation.pendingPresentationUnits()).firstOrNull {
                    it.id == scriptRuntime.stage.battleUnitForCharacterId(request.unitId)?.battleId
                }
            override fun postsUnit(request: ScenarioUnitPostsRequest): BattleUnit? = scriptBattleUnit(request.unitId)
            override fun isMineMaster(unitId: String) = isScriptMineMaster(unitId)
            override fun focus(unit: BattleUnit) { focusCameraOn(unit) }
            override fun sourceActionDuration(action: Int, direction: Int) = requireSourceActionDuration(action, direction)
            override fun beginHideModel(unit: BattleUnit, request: ScenarioUnitHideRequest, originalHp: Int) {
                unit.retreatFlag = true
                unit.otherNodesVisible = false
                unit.setHpcur(0)
            }
            override fun registerHideAnimation(unit: BattleUnit, sourceAction: Int, startedAt: Float, endsAt: Float) {
                deathAnimations[unit.id] = UnitActionAnimation(
                    unit.id, UnitAnimationKind.DEATH, unit.direction, startedAt, endsAt, sourceAction,
                )
            }
            override fun removeHideAnimation(unitId: String) { deathAnimations.remove(unitId) }
            override fun completeHideModel(unit: BattleUnit, request: ScenarioUnitHideRequest, originalHp: Int) {
                if (request.hideType != 0) battle.presentation.incrementUnitRetreat(unit)
                unit.setHpcur(originalHp)
                unit.visible = false
                battle.presentation.completeScriptedUnitHide(unit.id)
            }
            override fun completeUnitHide(request: ScenarioUnitHideRequest) = scriptRuntime.stage.completeUnitHide(request)
            override fun prepareShow(unit: BattleUnit, request: ScenarioUnitShowRequest): ScriptedUnitCallbackCoordinator.ShowStart {
                val restored = battle.presentation.restorePresentationUnit(unit.id) ?: unit
                val requestedX = request.x.takeIf { it >= 0 } ?: restored.tileX
                val requestedY = request.y.takeIf { it >= 0 } ?: restored.tileY
                val target = if (battle.unitAt(requestedX, requestedY)?.let { it !== restored } == true) {
                    listOf(
                        requestedX to requestedY - 1,
                        requestedX + 1 to requestedY,
                        requestedX - 1 to requestedY,
                        requestedX to requestedY + 1,
                    ).firstOrNull { (x, y) ->
                        x in 0..boardMaxX && y in 0..boardMaxY && battle.unitAt(x, y)?.let { it !== restored } != true
                    } ?: (restored.tileX to restored.tileY)
                } else requestedX to requestedY
                restored.tileX = target.first
                restored.tileY = target.second
                restored.hasAuthoredTileX = true
                restored.hasAuthoredTileY = true
                request.direction.takeIf { it in 0..3 }?.let { restored.direction = it }
                scriptRuntime.stage.unit(request.unitId).apply {
                    x = restored.tileX
                    y = restored.tileY
                }
                scriptRuntime.stage.setBattleUnitVisibility(request.unitId, true)
                focus(restored)
                val revive = request.flags and 1 != 0
                restored.otherNodesVisible = !revive
                val startedAt = animationClock()
                val duration = if (revive) requireSourceActionDuration(46, restored.direction) else .2f
                if (revive) scriptedUnitPresentation.setVisual(restored.id, ScriptedUnitVisual(46, startedAt))
                return ScriptedUnitCallbackCoordinator.ShowStart(restored.id, duration)
            }
            override fun finishShow(unitId: String, request: ScenarioUnitShowRequest) {
                battle.presentation.presentationUnit(unitId)?.let { unit ->
                    unit.otherNodesVisible = true
                    request.direction.takeIf { it in 0..3 }?.let { unit.direction = it }
                    unitSpriteFrameResolver.defaultAction(unit)
                }
            }
            override fun setVisibleWhenShowUnitMissing(unitId: Int) = scriptRuntime.stage.setBattleUnitVisibility(unitId, true)
            override fun setOldAvatar(unitId: String, avatarId: Int) { loadedBattleAvatarIds[unitId] = avatarId }
            override fun publishLoadedAvatar(unitId: String, avatarId: Int) { loadedBattleAvatarIds[unitId] = avatarId }
            override fun resumeScript() = scriptRuntime.resumeExternalDelay()
        },
    )
    private val scriptedUnitTimed = ScriptedUnitTimedCoordinator(
        scriptedUnitPresentation,
        object : ScriptedUnitTimedCoordinator.Port {
            override fun now() = animationClock()
            override fun consumeMap() = scriptRuntime.stage.consumeMapPresentationRequest()
            override fun focusMap(x: Int, y: Int) { focusCameraOnTile(x.toFloat(), y.toFloat(), forceCenter = true) }
            override fun consumeCameraCenters() = scriptRuntime.stage.consumeCameraCenterRequests().map {
                ScriptedUnitTimedCoordinator.CameraCenter(it.x, it.y)
            }
            override fun centerCamera(request: ScriptedUnitTimedCoordinator.CameraCenter) {
                configureSourceCameraViewport()
                battleCamera.centerTile(request.x, request.y, terrainGrid.width, terrainGrid.height)
                recordBattleTraceFrame(
                    0f,
                    "transition:camera:center:${request.x}:${request.y}",
                    advanceFrame = false,
                )
            }
            override fun resumeScript() = scriptRuntime.resumeExternalDelay()
        },
    )
    private val scriptedUnitActions = ScriptedUnitActionCoordinator(
        scriptedUnitPresentation,
        object : ScriptedUnitActionCoordinator.Port {
            override fun now() = animationClock()
            override fun consumeActions() = scriptRuntime.stage.consumeScriptedUnitActions()
            override fun unit(action: ScriptedUnitAction) = liveScriptBattleUnit(action.unitId)
            override fun applyDirection(unit: BattleUnit, direction: Int) { unit.direction = direction }
            override fun clearVisual(unitId: String) { scriptedUnitPresentation.clearVisual(unitId) }
            override fun setVisual(unitId: String, action: Int, startedAt: Float) {
                scriptedUnitPresentation.setVisual(unitId, ScriptedUnitVisual(action, startedAt))
            }
            override fun startSourceAction(unit: BattleUnit, action: Int) {
                actionAnimation = sourceActionAnimation(unit.id, action, unit.direction)
            }
            override fun actionDuration(action: Int, direction: Int) = battleSprites.duration(action, direction)
            override fun focus(unit: BattleUnit) { focusCameraOn(unit) }
            override fun clearSourceAction(unitId: String) { if (actionAnimation?.unitId == unitId) actionAnimation = null }
            override fun defaultAction(unitId: String) {
                battle.presentation.presentationUnit(unitId)?.let(unitSpriteFrameResolver::defaultAction)
            }
            override fun resumeScript() = scriptRuntime.resumeExternalDelay()
        },
    )
    private val loadedBattleAvatarIds = mutableMapOf<String, Int>()
    private val scriptPresentationTimeline = ScriptPresentationTimeline()
    private val scriptedUnitTargetSelector = ScriptedUnitTargetSelector(
        visibleUnits = { (battle.units.values + battle.presentation.pendingPresentationUnits()).filter { it.visible } },
        isMineMaster = ::isScriptMineMaster,
        byCharacter = { liveScriptBattleUnit(it, visibleOnly = true) },
    )
    private val scriptedPresentation = ScriptedPresentationCoordinator(
        scriptPresentationTimeline,
        object : ScriptedPresentationCoordinator.Port {
            override fun now() = animationClock()
            override fun modalActive() = scriptRuntime.state == PlaybackState.MODAL
            override fun consumeRequest() = scriptRuntime.stage.consumeScriptPresentationRequest()
            override fun clearVisual(unitId: String) { scriptedUnitPresentation.clearVisual(unitId) }
            override fun defaultAction(unitId: String) {
                battle.presentation.presentationUnit(unitId)?.let(unitSpriteFrameResolver::defaultAction)
            }
            override fun playGetItemSound() { audio.playBattleEffect(14) }
            override fun presentItemMessage(message: String) { scriptRuntime.presentExternalBattleInfo(message) }
            override fun dismissUnitInfo() { unitInfoOverlay.dispatch(BattleUnitInfoOverlayController.Intent.Dismiss) }
            override fun resumeScript() { scriptRuntime.resumeExternalDelay() }
            override fun focusRectangle(x1: Int, y1: Int, x2: Int, y2: Int) {
                focusCameraOnTile((x1 + x2) / 2f, (y1 + y2) / 2f, forceCenter = true)
            }
            override fun unitTarget(unitId: Int) = scriptBattleUnit(unitId)?.let {
                ScriptedPresentationCoordinator.Target(it.id, it.direction)
            }
            override fun focusUnit(unitId: String) {
                (battle.units.values + battle.presentation.pendingPresentationUnits())
                    .firstOrNull { it.id == unitId }?.let(::focusCameraOn)
            }
            override fun openUnitInfo(unitId: Int) { openUnitInfoLayer(unitId) }
            override fun itemTarget(selector: Int) = scriptedUnitTargetSelector.select(selector)?.let {
                ScriptedPresentationCoordinator.Target(it.id, it.direction)
            }
            override fun setVisual(unitId: String, action: Int, startedAt: Float) {
                scriptedUnitPresentation.setVisual(unitId, ScriptedUnitVisual(action, startedAt))
            }
            override fun sourceActionDuration(action: Int, direction: Int) = requireSourceActionDuration(action, direction)
            override fun focusMapObjects(request: ScenarioScriptPresentationRequest.MapObjects) {
                request.objects.lastOrNull()?.let { focusCameraOnTile(it.x.toFloat(), it.y.toFloat(), forceCenter = true) }
            }
            override fun statusTarget(values: List<Map<String, Any?>>): ScriptedPresentationCoordinator.Target? =
                values.asSequence().mapNotNull { (it["unit"] as? ScenarioUnitReference)?.id }
                    .firstOrNull()?.let(::scriptBattleUnit)?.let {
                        ScriptedPresentationCoordinator.Target(it.id, it.direction)
                    }
        },
    )
    private val outcomePresentation = BattleOutcomePresentationCoordinator(
        object : BattleOutcomePresentationCoordinator.Port {
            override fun visibleOutcome() = visibleBattleOutcome()
            override fun rewardRequest(): ResolvedBattleReward? {
                val request = scriptRuntime.stage.consumeRewardRequest() ?: return null
                val resolved = BattleRewardResolver.resolve(
                    request, campaign.averageJoinedLevel(), battle.round, scenarioMaxRound(),
                    battle.units.values.count { it.faction == Faction.PLAYER && it.hitPoints < 1 },
                    battle.units.values.count { it.type().isEnemySide() && it.visible },
                    objectivesComplete = false,
                )
                campaign.addMoney(resolved.money)
                request.items.chunked(2).forEach { pair ->
                    val id = pair.firstOrNull() ?: return@forEach
                    if (id >= 255) return@forEach
                    val supplied = pair.getOrNull(1) ?: 1
                    val level = if (supplied < 0) {
                        (campaign.averageJoinedLevel() / 10).coerceIn(0, 8) + 1
                    } else supplied.coerceAtLeast(1)
                    campaign.inventory.addItem(id, 1, level)
                    campaign.inventory.discoverTreasure(id, gameDataCatalog)
                }
                return resolved
            }
            override fun resumeRewardModal() { if (scriptRuntime.state == PlaybackState.MODAL) scriptRuntime.resumeModal() }
            override fun syncScriptedUnits() { this@BattleScreen.syncScriptedUnits() }
            override fun scene2Available() = "scene2" in scriptRuntime.functionNames
            override fun startScene2() { scriptRuntime.start("scene2") }
            override fun scriptIsBlocked() = scriptRuntime.state != PlaybackState.COMPLETE
            override fun scriptState() = scriptRuntime.state
            override fun openSaveLayer() { saveLoadOverlay.openSave() }
            override fun nextScenario(): String = this@BattleScreen.nextScenario()
            override fun completeBattle(nextScenario: String) {
                scriptRuntime.stage.sceneJumpStage?.let(game::setCampaignStage) ?: game.advanceCampaignStage()
                game.completeBattle(returnScenario, nextScenario)
                battleRouteCompleted = true
            }
            override fun showNextScenario(nextScenario: String) { game.showNextScenario(nextScenario) }
            override fun finishTrace() {
                if (battleTraceCoordinator?.exitOnFinish == false) battleTraceCoordinator.finish("battle-end")
            }
            override fun showVictoryPrompt() { eventMessage = "게임 저장하시겠습니까?" }
            override fun campaignEquipmentUpgrade(): BattleOutcomePresentationCoordinator.UpgradePresentation? {
                val request = battle.experience.consumeEquipmentUpgrade() ?: return null
                val profile = gameDataCatalog.equipmentProfile(request.itemId) ?: return null
                val owner = campaign.unitNames[request.unitId]
                    ?: gameDataCatalog.unitProfile(request.unitId)?.name.orEmpty()
                return BattleOutcomePresentationCoordinator.UpgradePresentation(
                    request, owner, profile.name, request.slot.attributeLabel(),
                )
            }
            override fun equipmentUpgradeAllowed() = !settlementPresentation.isActive() &&
                (itemUpgradeRouteState != null || (
                    actionAnimation?.let { animationClock() < it.endsAt } != true &&
                        movementAnimation?.let { animationClock() < it.endsAt } != true &&
                        hitReactionAnimations.values.none { animationClock() < it.endsAt }
                    ))
            override fun settlementUpgrade(request: CampaignEquipmentExperienceResult) =
                BattleOutcomePresentationCoordinator.UpgradePresentation(
                    request,
                    campaign.unitNames[request.unitId] ?: gameDataCatalog.unitProfile(request.unitId)?.name.orEmpty(),
                    gameDataCatalog.equipmentProfile(request.itemId)?.name
                        ?: error("settlement item profile is missing: ${request.itemId}"),
                    request.slot.attributeLabel(),
                )
            override fun itemUpgradeCompleted() = Unit
            override fun createLoseScene() = LoseSceneFlow(openLogin = game::showTitleScreen, endGame = { Gdx.app.exit() })
            override fun transitionBusy() = combatPresentationBusy() || outcomeCallbacksPending()
            override fun naturalTransitionAllowed() =
                !verification.active && !game.hasFrameCaptureRequest() &&
                    !game.hasRenderEventLogRequest()
            override fun routeCompleted() = battleRouteCompleted
            override fun battleEndedByScript() = scriptRuntime.stage.battleEndedByScript
            override fun runNaturalScene1() { runBattleScript() }
        },
    )
    private val harmNumberAnimations = mutableMapOf<String, HarmNumberAnimation>()
    private val healthTimeline = BattleHealthPresentation()
    private val healthTimelineHoldUntil = mutableMapOf<String, Float>()
    private val timedBattleMutations = mutableListOf<TimedBattleMutation>()
    private var queuedCounterPresentation: CounterPresentation? = null
    private var queuedFollowUpPresentation: FollowUpPresentation? = null
    private var queuedCounterFollowUpPresentation: CounterFollowUpPresentation? = null
    private var queuedPhysicalPresentation: PhysicalPassPresentationQueue? = null
    private var queuedMagicPresentation: MagicPassPresentationQueue? = null
    private var pendingCriticalSpeechAction: PendingCriticalSpeechAction? = null
    private var activeCounterMagicPresentation: ActiveCounterMagicPresentation? = null
    private val magicEffectAnimations = mutableListOf<MagicEffectAnimation>()
    private var selectedUnitId: String? = null
    private val battleCommandFlow = BattleCommandFlow()
    private val battleCommandRouteFixture = BattleCommandRouteFixtureController()
    private var battleCommandPressedTag: Int? = null
    private var pendingBattleCommandUnit: String? = null
    private var pendingBattleCommandScriptStarted = false
    private var pendingBattleCommandMoveProvenance: String? = null
    internal val emptyAiCampFrameBarrier = EmptyAiCampFrameBarrier()
    internal val committedPlayerMoveFrameBarrier = CommittedPlayerMoveFrameBarrier()
    internal val actionStatusFrameBarrier = ActionStatusFrameBarrier()
    internal val counterattackSettlementFrameBarrier = CounterattackSettlementFrameBarrier()
    private val scriptedMovementCampTransitionFrameBarrier = ScriptedMovementCampTransitionFrameBarrier()
    internal val consecutiveNoResultFrameGate = ConsecutiveNoResultFrameGate()
    internal var playerMoveCommitted = false
    internal var committedPlayerMove: String? = null

    private var magicMode = false
    private var selectedMagicIndex = 0
    private var propertyMode = false
    private var selectedPropertyIndex = 0
    private var activeRoundLayer: RoundLayer? = null
    private var activeRoundLayerElapsed = 0f
    internal val turnController: BattleTurnController by lazy {
        BattleTurnController(
            battle = battle,
            showCamp = { card ->
                showRoundCard(card.turn.round.takeIf { card.showsRoundNumber }, battle.maxRounds) {
                    turnController.completeCampCard()
                }
            },
            runCampScript = {
                runBattleScript()
                scriptRuntime.state == PlaybackState.COMPLETE
            },
            runAi = { camp ->
                aiPresentation.beginCamp(camp)
            },
            hasPendingAiPresentation = { aiPresentation.hasActiveCamp },
            presentCampState = { settlement -> presentTurnSettlement(settlement) },
            presentDeaths = { checkpoint -> deathTimeline.begin(checkpoint.toDeathTimelineCheckpoint()) },
            presentCampRestore = { settlement -> presentTurnSettlement(settlement) },
            runRoundScript = {
                runBattleScript()
                scriptRuntime.state == PlaybackState.COMPLETE
            },
            deferSynchronousRoundScriptCompletion = true,
            presentWeather = { transition ->
                if (transition.changed) eventMessage = "날씨: ${transition.current.label()}"
                true
            },
            onCampEvents = { turn -> showTurnResult(turn, "") },
            initialPhase = if (bootstrapPhase == BattleBootstrapPhase.COMPLETE) {
                BattleTurnPhase.PLAYER_INPUT
            } else {
                BattleTurnPhase.BOOTSTRAP
            },
        )
    }
    private val aiPresentation = AiPresentationCoordinator(
        { coordinator -> BattleAiPresentationPort(this, coordinator) },
    )
    private var presentationReady = false
    private val actionCapture = captureFixtureConfiguration.actionSample
    private val captureFixtureCoordinator = BattleCaptureFixtureCoordinator(captureFixtureConfiguration, presentationConfiguration)
    private val captureFixturePort = object : BattleCaptureFixtureCoordinator.Port {
        override fun advanceFixtureDialogue() = advanceCaptureFixtureDialogue()
        override fun advanceDialogueStep() = advanceBattleDialogue()
        override fun playbackState(): PlaybackState = scriptRuntime.state
        override fun dialogueSpeakerId(): String? = scriptRuntime.currentDialogue?.speakerId
    }
    private val rewardRouteState get() = presentationConfiguration.rewardRouteState
    private val itemUpgradeRouteState get() = presentationConfiguration.itemUpgradeRouteState
    private val loseRestartRoute get() = presentationConfiguration.loseRestartRoute
    private val roundRouteState get() = presentationConfiguration.roundRouteState
    private val winConditionRouteState get() = presentationConfiguration.winConditionRouteState
    private val miniMapRouteState get() = presentationConfiguration.miniMapRouteState
    private val autoBattleRouteState get() = presentationConfiguration.autoBattleRouteState
    private val battleCommandRouteState get() = presentationConfiguration.battleCommandRouteState
    private val battleCharacterRouteState get() = presentationConfiguration.battleCharacterRouteState
    private val battleEdit2RouteState get() = presentationConfiguration.battleEdit2RouteState
    private val otherUnitInfoRoute get() = presentationConfiguration.otherUnitInfoRoute
    private val mineUnitInfoRoute get() = presentationConfiguration.mineUnitInfoRoute
    private val actionCaptureMode get() = captureFixtureConfiguration.actionSampleMode
    private val cutsceneAttackCapture get() = captureFixtureConfiguration.cutsceneAttackCapture
    private val cutscenePostHitCapture get() = captureFixtureConfiguration.cutscenePostHitCapture
    private val cutscene477Capture get() = captureFixtureConfiguration.cutscene477Capture
    private val battleDialogueBlendRoute get() = presentationConfiguration.battleDialogueBlendRoute
    private val battleInitRoute get() = presentationConfiguration.battleInitRoute
    private val battleTerrainRoute get() = presentationConfiguration.battleTerrainRoute
    private val battleMenuRoute get() = presentationConfiguration.battleMenuRoute
    private val helperRoute get() = presentationConfiguration.helperRoute
    private val winModalRoute get() = presentationConfiguration.winModalRoute
    private val unitInfoRoute get() = presentationConfiguration.unitInfoRoute
    private val loseResultRoute get() = presentationConfiguration.loseResultRoute
    private val winResultRoute get() = presentationConfiguration.winResultRoute
    private val openingSayRoute get() = presentationConfiguration.openingSayRoute
    private val hudRoute get() = presentationConfiguration.hudRoute
    private val dialogueOneRoute get() = presentationConfiguration.dialogueOneRoute
    private val enemyTurnRoute get() = presentationConfiguration.enemyTurnRoute
    private val dialogueStepCapture get() = captureFixtureConfiguration.dialogueStepCapture
    private val dialogueComponentStage get() = presentationConfiguration.dialogueComponentStage
    private val mapOnlyCapture get() = captureFixtureConfiguration.mapOnlyCapture
    private val selectionOverlayCapture get() = captureFixtureConfiguration.selectionOverlayCapture
    private val initialModalRoute get() = captureFixtureConfiguration.initialModalRoute
    private val modalRenderCapture get() = captureFixtureConfiguration.modalRenderCapture
    private var boardLeft = 120f
    private var boardBottom = 130f
    private var boardTile = 64f
    private var boardMaxX = 1
    private var boardMaxY = 1
    internal fun animationClock(): Float = presentationConfiguration.animationClock(elapsed, battleElapsed, actionCaptureMode)
    private fun mapObjectAnimationClock(): Float = presentationConfiguration.mapObjectAnimationClock(elapsed)
    private fun stateEffectAnimationClock(): Float = presentationConfiguration.mapObjectAnimationClock(elapsed)
    private fun battleMapFile(index: Int) = sequenceOf("png", "jpg", "webp")
        .map { Gdx.files.internal("maps/battle-maps/$index.$it") }
        .firstOrNull { it.exists() }

    init {
        Gdx.app.log("JojoGame", "BATTLE_MAP_SOURCE: ${mapFile?.path()}")
        runBattleScript()
        battleEdit2RouteState?.let { route ->
            battleEdit2 = BattleEditLayer2(battle.weather.ordinal, battle.round, canApplyRound = true).also { edit ->
                when (route) {
                    BattleEditLayer2Route.INITIAL -> Unit
                    BattleEditLayer2Route.WEATHER -> {
                        edit.openWeatherPanel(); edit.selectWeather(3)
                    }

                    BattleEditLayer2Route.ROUND -> {
                        edit.textChanged("8"); edit.editingDidEnd()
                    }

                    BattleEditLayer2Route.APPLY -> {
                        edit.selectWeather(3); edit.textChanged("8"); edit.editingDidEnd()
                        edit.touchButton(0).forEach { effect ->
                            when (effect) {
                                is BattleEditLayer2.Effect.SetWeather -> battle.applyEditedWeather(effect.value)
                                is BattleEditLayer2.Effect.SetRound -> battle.applyEditedRound(effect.value)
                                else -> Unit
                            }
                        }
                    }

                    BattleEditLayer2Route.CHILD, BattleEditLayer2Route.CHILD_SCENE, BattleEditLayer2Route.REGISTER -> {
                        battleEdit3Open = edit.touchButton(2).contains(BattleEditLayer2.Effect.OpenGlobalEditor)
                        battleEdit3ScenePanelOpen = route == BattleEditLayer2Route.CHILD_SCENE
                        if (route == BattleEditLayer2Route.REGISTER) {
                            battleRegisterRoute =
                                BattleRegisterRoute().also { secret -> repeat(6) { secret.titleTouchEnd() } }
                            check(requireNotNull(battleRegisterRoute).view().registerAttached)
                        }
                    }
                }
            }
        }
        when (rewardRouteState) {
            RuntimeBattleRoute.REWARD_BASIC, RuntimeBattleRoute.REWARD_CARD1, RuntimeBattleRoute.REWARD_CARD2 -> {
                val cards = if (rewardRouteState == RuntimeBattleRoute.REWARD_BASIC) emptyList() else listOf(150, 0, 151, 0)
                scriptRuntime.stage.reward(items = cards)
                scriptRuntime.stage.scriptedBattleOutcome?.let(battle::setScriptedOutcome)
                outcomePresentation.openRewardRequestIfNeeded()
                if (rewardRouteState != RuntimeBattleRoute.REWARD_BASIC) outcomePresentation.advanceRewardFlow()
                if (rewardRouteState == RuntimeBattleRoute.REWARD_CARD2) outcomePresentation.advanceRewardFlow()
            }

            else -> Unit
        }
        if (battleMenuRoute) openBattleMenu()
        if (otherUnitInfoRoute) {
            val unit = requireNotNull(battle.units.values.firstOrNull { it.characterId == 210 && it.visible }) {
                "R_00 OtherUnitInfoLayer production unit 210 is missing"
            }
            otherUnitInfoLayer = OtherUnitInfoLayer().also {
                it.onCreate(unit, gameDataCatalog.postsName(unit.posts), unit.name.replace(Regex("\\d+$"), ""))
            }
        }
        if (mineUnitInfoRoute) {
            val unit = requireNotNull(battle.units.values.firstOrNull { it.characterId == 210 && it.visible })
            mineUnitInfoLayer = MineUnitInfoLayer().also {
                it.onCreate(
                    unit,
                    gameDataCatalog.postsName(unit.posts),
                    unit.name.replace(Regex("\\d+$"), "")
                )
            }
        }
        if (helperRoute) openHelperLayer()
        when (initialModalRoute) {
            RuntimeBattleRoute.MODAL_TERRAIN -> informationOverlay.openTerrain()
            RuntimeBattleRoute.MODAL_PROPERTY -> informationOverlay.openProperty()
            RuntimeBattleRoute.MODAL_TREASURE -> informationOverlay.openTreasure()
            RuntimeBattleRoute.MODAL_SETTING -> settingsOverlay.open()
            RuntimeBattleRoute.MODAL_SAVE -> saveLoadOverlay.openSave()
            RuntimeBattleRoute.MODAL_LOAD -> saveLoadOverlay.openLoad()
            RuntimeBattleRoute.MODAL_FORCES -> openForcesListLayer()
            else -> Unit
        }
        if (battleTerrainRoute) {
            openBattleMenu()
            handleBattleMenuTap(6)
        }
        if (winModalRoute) openWinConditionBox()
        when (winConditionRouteState) {
            RuntimeBattleRoute.WIN_COMPACT -> {
                openBattleMenu()
                handleBattleMenuTap(9)
                battle.units.values.firstOrNull { it.characterId == 235 }?.let { scriptedUnitPresentation.clearVisual(it.id) }
            }

            RuntimeBattleRoute.WIN_FULL -> {
                battle.units.values.firstOrNull { it.characterId == 235 }?.let { scriptedUnitPresentation.clearVisual(it.id) }
                scriptRuntime.suspendForWinCondition("장보와 장량을\n격퇴하십시오.")
            }

            else -> Unit
        }
        if (!verification.usesTutorialBattle) syncScriptedUnits()
        if (selectionOverlayCapture) {
            selectedUnitId = battle.units.values.firstOrNull { it.characterId == 210 && it.visible }?.id
            check(selectedUnitId != null) { "S_00 selection fixture unit 210 is missing" }
        }
        when {
            loseResultRoute -> {
                battle.setMaxRounds(1); outcomePresentation.enterLoseScene()
            }

            loseRestartRoute -> {
                battle.setMaxRounds(1)
                    outcomePresentation.enterLoseScene()
            }
            winResultRoute -> {
                battle.units.values.filter { it.type().isEnemySide() }.forEach { it.visible = false }
                outcomePresentation.openVictorySavePrompt()
            }
        }
        if (unitInfoRoute) {
            val first = battle.units.values.filter { it.visible && it.isPlayerSide() }
                .sortedBy { it.characterId ?: Int.MAX_VALUE }.firstOrNull()
            first?.characterId?.let(::openUnitInfoLayer)
        }
        Gdx.app.log(
            "JojoGame",
            "BATTLE_AVATAR_GROUPS: " + battle.units.values.joinToString(",") { unit ->
                "${unit.characterId}:${battleAvatarId(unit)}"
            },
        )
        battleInitLayer.onCreate(0)
        battleInitLayer.onLoadBgMap(gameDataCatalog.battleName(scriptRuntime.stage.battleMapIndex))
        initializeMiniMap()
        presentationReady = true
        actionCapture?.let { capture ->
            battle.units.values.firstOrNull { it.tileX == 10 && it.tileY == 14 }?.let { unit ->
                check(attackTexture(unit) != null) { "S_00 action capture source avatar has no Unit_atk atlas: ${unit.characterId}" }
                Gdx.app.log(
                    "JojoGame",
                    "ACTION_CAPTURE_UNIT: id=${unit.characterId}, avatar=${battleAvatarId(unit)}, action=${capture.action}, sample=${capture.sample}"
                )
                actionAnimation = sourceActionAnimation(unit.id, capture.action, 2, 1f - capture.sample)
            }
        }
        Gdx.input.inputProcessor = object : InputAdapter() {
            private fun inputSurface(): BattleInputSurface = BattleInputSurface(
                dialogue = BattleInteractiveInput.route(scriptRuntime.state, turnController.snapshot.phase) ==
                        BattleInteractiveInput.Route.DIALOGUE,
                settlementInfo = settlementPresentation.info2View() != null,
                roundLayer = activeRoundLayer != null,
                resultPrompt = outcomePresentation.winPromptActive,
                modalInfo = scriptRuntime.state == PlaybackState.MODAL &&
                        scriptRuntime.currentModalKind == ScenarioModalKind.INFO,
                loseScene = outcomePresentation.loseSceneActive,
                command = battleCommandFlow.phase == BattleCommandFlow.Phase.COMMAND,
                usePropertyDetail = usePropertyDetail != null,
                useProperty = usePropertyLayer != null,
                magicInfo = magickInfoLayer != null,
                magicList = magickListLayer != null,
                jiqi = jiqiLayer != null,
                reward = outcomePresentation.rewardActive,
                itemUpgrade = outcomePresentation.itemUpgradeActive,
                scriptWinConditions = scriptWinConditions != null,
                unitInfo = unitInfoOverlay.isVisible(),
                forces = forcesOverlay.isVisible(),
                helper = helperOverlay.view() != null,
                setting = settingsOverlay.view() != null,
                save = saveLoadOverlay.view(BattleSaveLoadOverlayController.Mode.SAVE) != null,
                load = saveLoadOverlay.view(BattleSaveLoadOverlayController.Mode.LOAD) != null,
                treasure = informationOverlay.treasureView() != null,
                property = informationOverlay.propertyView() != null,
                terrain = informationOverlay.terrainView() != null,
                winCondition = winConditionOpen,
                autoPrompt = autoBattleFlow.view().overlay == AutoBattleFlow.Overlay.PROMPT,
                autoTuoGuan = autoBattleFlow.view().overlay == AutoBattleFlow.Overlay.TUOGUAN,
                choice = scriptRuntime.state == PlaybackState.CHOICE,
                battleMenu = battleMenuOpen,
                miniMap = !battleMenuOpen,
                menuHud = true,
                interactiveRoute = BattleInteractiveInput.route(scriptRuntime.state, turnController.snapshot.phase),
                hitRegions = listOf(
                    BattleInputHitRegion(BattleInputTarget.MENU_HUD, 1353.9535f, 8f, 1413.9535f, 68f),
                    BattleInputHitRegion(
                        BattleInputTarget.MINI_MAP,
                        if (miniMapLayer.shown) 1174.372f else 1418.372f,
                        730f,
                        if (miniMapLayer.shown) 1244.372f else 1488.372f,
                        800f,
                    ),
                ),
            )

            override fun keyDown(keycode: Int): Boolean {
                val keyboardIntent = battleInputRouter.keyDown(keycode, inputSurface())
                if (keyboardIntent.capture == BattleInputCapture.DIALOGUE) {
                    if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) advanceBattleDialogue()
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.SETTLEMENT_INFO && keycode in setOf(
                        Input.Keys.ENTER,
                        Input.Keys.SPACE,
                        Input.Keys.ESCAPE
                    )
                ) {
                    closeSettlementInfo2()
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.ROUND) return true
                if (keyboardIntent.capture == BattleInputCapture.LOSE) return true
                if (keyboardIntent.capture == BattleInputCapture.RESULT) return true
                if (keyboardIntent.capture == BattleInputCapture.HELPER) return true
                if (keyboardIntent.capture == BattleInputCapture.SETTING) {
                    if (keycode == Input.Keys.ESCAPE) settingsOverlay.dispatch(BattleSettingsOverlayController.Intent.Close)
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.SAVE) {
                    if (keycode == Input.Keys.ESCAPE) handleSaveLoadEffect(saveLoadOverlay.dispatch(BattleSaveLoadOverlayController.Intent.Cancel).effect)
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.FORCES) return true
                if (keyboardIntent.capture == BattleInputCapture.UNIT_INFO) return true
                if (keyboardIntent.capture == BattleInputCapture.LOAD) {
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.REWARD) {
                    if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) outcomePresentation.advanceRewardFlow()
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.ITEM_UPGRADE) {
                    if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) outcomePresentation.closeItemUpgrade()
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.TREASURE) {
                    when (keycode) {
                        Input.Keys.ESCAPE -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.Close)
                        Input.Keys.UP -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.Scroll(-1))
                        Input.Keys.DOWN -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.Scroll(1))
                    }
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.PROPERTY) {
                    when (keycode) {
                        Input.Keys.ESCAPE -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.Close)
                        Input.Keys.NUM_1 -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.SelectPropertyTab(PropertyLayer.Tab.WEAPON))
                        Input.Keys.NUM_2 -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.SelectPropertyTab(PropertyLayer.Tab.ARMOR))
                        Input.Keys.NUM_3 -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.SelectPropertyTab(PropertyLayer.Tab.AUXILIARY))
                        Input.Keys.NUM_4, Input.Keys.TAB -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.SelectPropertyTab(PropertyLayer.Tab.PROPERTY))
                        Input.Keys.UP -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.Scroll(-1))
                        Input.Keys.DOWN -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.Scroll(1))
                    }
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.TERRAIN) {
                    when (keycode) {
                        Input.Keys.ESCAPE -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.Close)
                        Input.Keys.NUM_1 -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.SelectTerrainTab(TerrainLayer.Tab.RISE))
                        Input.Keys.NUM_2, Input.Keys.TAB -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.SelectTerrainTab(TerrainLayer.Tab.EXPEND))
                        Input.Keys.UP -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.Scroll(-1))
                        Input.Keys.DOWN -> informationOverlay.dispatch(BattleInformationOverlayController.Intent.Scroll(1))
                    }
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.CHOICE) {
                    when (keycode) {
                        Input.Keys.UP -> scriptRuntime.selectPrevious()
                        Input.Keys.DOWN -> scriptRuntime.selectNext()
                        Input.Keys.ENTER, Input.Keys.SPACE -> confirmBattleChoice()
                    }
                    return true
                }
                if (keyboardIntent.capture == BattleInputCapture.SCRIPT_PAUSED) return true
                when (keycode) {
                    Input.Keys.ESCAPE -> if (usePropertyDetail != null) {
                        usePropertyDetail = null
                        return true
                    } else if (usePropertyLayer != null) {
                        usePropertyLayer?.closeTouchEnd()
                        usePropertyLayer = null
                        return true
                    } else game.showScenario(returnScenario)

                    Input.Keys.T, Input.Keys.SPACE, Input.Keys.ENTER -> {
                        if (battle.outcome() == null) endTurn() else outcomePresentation.continueAfterOutcome()
                    }

                    Input.Keys.M -> {
                        val unit = selectedUnitId?.let { battle.units[it] }
                        if (unit?.magic?.isNotEmpty() == true) {
                            openMagickList(unit)
                        } else eventMessage = "선택한 유닛은 사용할 수 있는 전략이 없습니다."
                    }

                    Input.Keys.B -> {
                        val unit = selectedUnitId?.let { battle.units[it] }
                        val properties = usableProperties()
                        if (unit == null) eventMessage = "먼저 아이템을 사용할 아군을 선택하세요."
                        else if (properties.isEmpty()) eventMessage = "사용 가능한 소비 아이템이 없습니다."
                        else openUsePropertyLayer()
                    }
                }
                return true
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                val pointerIntent = battleInputRouter.pointerDown(world.x, world.y, inputSurface())
                if (pointerIntent.capture == BattleInputCapture.DIALOGUE) {
                    advanceBattleDialogue()
                    return true
                }
                if (pointerIntent.capture == BattleInputCapture.SETTLEMENT_INFO) {
                    closeSettlementInfo2()
                    return true
                }
                if (pointerIntent.capture == BattleInputCapture.ROUND) return true
                if (outcomePresentation.winPromptActive) {
                    outcomePresentation.victorySaveAnswerPressed = victorySaveAnswerAt(world.x, world.y)
                    return true
                }
                if (scriptRuntime.state == PlaybackState.MODAL &&
                    scriptRuntime.currentModalKind == ScenarioModalKind.INFO
                ) {
                    battleInfoPanelPressed = true
                    return true
                }
                outcomePresentation.loseSceneFlow?.let { flow ->
                    losePressedAnswer =
                        if (flow.state == LoseSceneFlow.State.PROMPT) loseAnswerAt(world.x, world.y) else null
                    return true
                }
                if (battleCommandFlow.phase == BattleCommandFlow.Phase.COMMAND) {
                    battleCommandPressedTag = battleCommandTagAt(world.x, world.y)
                    return true
                }
                usePropertyDetail?.let { return true }
                usePropertyLayer?.let { layer ->
                    usePropertyCancelPressed = usePropertyCancelAt(world.x, world.y)
                    usePropertyPanelPressed = !usePropertyPanelAt(world.x, world.y)
                    usePropertyPressedRow = if (!usePropertyCancelPressed) usePropertyRowAt(world.x, world.y) else null
                    usePropertyPressedRow?.let(layer::touchStart)
                    return true
                }
                magickInfoLayer?.let { return true }
                magickListLayer?.let { layer ->
                    magickCancelPressed = magickCancelAt(world.x, world.y)
                    magickPressedRow = if (!magickCancelPressed) magickRowAt(world.x, world.y) else null
                    magickPressedRow?.takeIf(layer::enabled)?.let { row ->
                        layer.start(row)
                        magickPressedAt = elapsed
                    }
                    return true
                }
                jiqiLayer?.let { jiqiPressed = true; return true }
                if (outcomePresentation.rewardActive) { outcomePresentation.advanceRewardFlow(); return true }
                if (outcomePresentation.itemUpgradeActive) { outcomePresentation.closeItemUpgrade(); return true }
                scriptWinConditions?.let { return true }
                if (unitInfoOverlay.dispatch(BattleUnitInfoOverlayController.Intent.PointerDown(world.x, world.y)).consumed) return true
                if (forcesOverlay.dispatch(BattleForcesOverlayController.Intent.PointerDown(world.x, world.y)).consumed) return true
                if (helperOverlay.dispatch(BattleHelperOverlayController.Intent.PointerDown(world.x, world.y))) return true
                if (settingsOverlay.dispatch(BattleSettingsOverlayController.Intent.PointerDown(world.x, world.y)).consumed) return true
                if (saveLoadOverlay.dispatch(BattleSaveLoadOverlayController.Intent.PointerDown(world.x, world.y)).consumed) return true
                if (informationOverlay.dispatch(BattleInformationOverlayController.Intent.Tap(world.x, world.y)).consumed) return true
                if (winConditionOpen) {
                    winConditionButtonPressed = world.x in 957.134f..1213.834f && world.y in 88.204f..148.204f
                    return true
                }
                when (autoBattleFlow.view().overlay) {
                    AutoBattleFlow.Overlay.PROMPT -> {
                        autoBattlePressedTag = autoBattlePromptButtonAt(world.x, world.y)
                        autoBattleTogglePressed = autoBattleToggleAt(world.x, world.y)
                        autoBattlePanelPressed = autoBattlePressedTag == null && !autoBattleTogglePressed
                        return true
                    }

                    AutoBattleFlow.Overlay.TUOGUAN -> {
                        autoBattlePanelPressed = true
                        return true
                    }

                    AutoBattleFlow.Overlay.NONE -> Unit
                }
                if (scriptRuntime.state == PlaybackState.CHOICE) {
                    confirmBattleChoice()
                    return true
                }
                if (BattleInteractiveInput.route(scriptRuntime.state, turnController.snapshot.phase) !=
                    BattleInteractiveInput.Route.PLAYER_INPUT
                ) return true
                if (pointerIntent.capture == BattleInputCapture.MINI_MAP) {
                    return true
                }
                if (pointerIntent.capture == BattleInputCapture.BATTLE_MENU) {
                    battleMenuPressedIndex = menuIndexAt(world.x, world.y)
                    return true
                }
                if (pointerIntent.capture == BattleInputCapture.MENU_HUD) {
                    return true
                }
                return true
            }

            override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
                val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                val intent = battleInputRouter.pointerDragged(world.x, world.y, inputSurface())
                if (!intent.moved) return false
                battleCamera.pan(intent.deltaX, intent.deltaY)
                return true
            }

            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                val pointerIntent = battleInputRouter.pointerUp(world.x, world.y, inputSurface())
                val mapTouchPending = pointerIntent.pressedCapture == BattleInputCapture.MAP
                val mapTouchMoved = pointerIntent.moved
                if (activeRoundLayer != null) return true
                if (battleInfoPanelPressed) {
                    battleInfoPanelPressed = false
                    if (scriptRuntime.state == PlaybackState.MODAL &&
                        scriptRuntime.currentModalKind == ScenarioModalKind.INFO
                    ) {
                        if (!battleInfoReveal.revealAllIfPending()) {
                            scriptRuntime.resumeModal()
                            syncScriptedUnits()
                            completeTurnScriptIfReady()
                        }
                    }
                    return true
                }
                if (pointerIntent.pressedCapture == BattleInputCapture.MENU_HUD) {
                    if (pointerIntent.releasedTarget == BattleInputTarget.MENU_HUD) openBattleMenu()
                    return true
                }
                if (outcomePresentation.winPromptActive) {
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val answer = victorySaveAnswerAt(world.x, world.y)
                    if (answer != null && answer == outcomePresentation.victorySaveAnswerPressed) {
                        outcomePresentation.answerVictorySavePrompt(answer)
                    }
                    outcomePresentation.victorySaveAnswerPressed = null
                    return true
                }
                outcomePresentation.loseSceneFlow?.let { flow ->
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val released = loseAnswerAt(world.x, world.y)
                    if (released != null && released == losePressedAnswer) flow.answer(released)
                    losePressedAnswer = null
                    return true
                }
                if (battleCommandFlow.phase == BattleCommandFlow.Phase.COMMAND) {
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val released = battleCommandTagAt(world.x, world.y)
                    if (released != null && released == battleCommandPressedTag) dispatchBattleCommand(released)
                    battleCommandPressedTag = null
                    return true
                }
                usePropertyDetail?.let {
                    if (usePropertyDetailSuppressRelease) usePropertyDetailSuppressRelease =
                        false else usePropertyDetail = null
                    return true
                }
                usePropertyLayer?.let { layer ->
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val released = usePropertyRowAt(world.x, world.y)
                    if (usePropertyCancelPressed && usePropertyCancelAt(world.x, world.y)) layer.closeTouchEnd()
                    else if (usePropertyPanelPressed && !usePropertyPanelAt(world.x, world.y)) layer.closeTouchEnd()
                    else if (released != null && released == usePropertyPressedRow) layer.touchEnd(released)
                    else layer.touchCancel()
                    usePropertyPressedRow = null
                    usePropertyCancelPressed = false
                    usePropertyPanelPressed = false
                    if (!layer.attached) usePropertyLayer = null
                    return true
                }
                magickInfoLayer?.let { layer ->
                    if (magickInfoSuppressRelease) {
                        magickInfoSuppressRelease = false; return true
                    }
                    layer.close(MagicUiList.TOUCH_END)
                    if (!layer.attached) magickInfoLayer = null
                    return true
                }
                magickListLayer?.let { layer ->
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val released = magickRowAt(world.x, world.y)
                    if (magickCancelPressed && magickCancelAt(world.x, world.y)) layer.cancel(MagicUiList.TOUCH_END)
                    else if (released != null && released == magickPressedRow) {
                        layer.end(released)
                        if (!layer.attached) {
                            layer.rows.getOrNull(released)?.let(::selectMagick)
                            if (battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION) battleCommandFlow.childCompleted(
                                true
                            )
                            magickListLayer = null
                        }
                    }
                    magickPressedRow = null; magickCancelPressed = false
                    if (!layer.attached) {
                        if (released == null && battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION) battleCommandFlow.childCancelled()
                        magickListLayer = null
                    }
                    return true
                }
                jiqiLayer?.let { layer ->
                    if (jiqiPressed) layer.onCancel(JiQiLayer.TOUCH_END)
                    jiqiPressed = false
                    if (!layer.attached) jiqiLayer = null
                    return true
                }
                scriptWinConditions?.let { layer ->
                    layer.cancel(WinConditionsLayer.TOUCH_END)
                    return true
                }
                val unitInfoResult = unitInfoOverlay.dispatch(BattleUnitInfoOverlayController.Intent.PointerUp(world.x, world.y))
                if (unitInfoResult.consumed) {
                    handleUnitInfoOverlayEffect(unitInfoResult.effect)
                    return true
                }
                val forcesResult = forcesOverlay.dispatch(BattleForcesOverlayController.Intent.PointerUp(world.x, world.y))
                if (forcesResult.consumed) {
                    handleForcesOverlayEffect(forcesResult.effect)
                    return true
                }
                val settingsResult = settingsOverlay.dispatch(BattleSettingsOverlayController.Intent.PointerUp(world.x, world.y))
                if (settingsResult.consumed) return true
                val saveLoadResult = saveLoadOverlay.dispatch(BattleSaveLoadOverlayController.Intent.PointerUp(world.x, world.y))
                if (saveLoadResult.consumed) {
                    handleSaveLoadEffect(saveLoadResult.effect)
                    return true
                }
                viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat())).let { world ->
                    if (helperOverlay.dispatch(BattleHelperOverlayController.Intent.PointerUp(world.x, world.y))) return true
                }
                when (autoBattleFlow.view().overlay) {
                    AutoBattleFlow.Overlay.PROMPT -> {
                        val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                        val released = autoBattlePromptButtonAt(world.x, world.y)
                        when {
                            autoBattleTogglePressed && autoBattleToggleAt(world.x, world.y) -> autoBattleFlow.toggle()
                            autoBattlePressedTag != null && autoBattlePressedTag == released -> answerAutoBattle(
                                autoBattlePressedTag!!
                            )

                            autoBattlePanelPressed && released == null && !autoBattleToggleAt(
                                world.x,
                                world.y
                            ) -> answerAutoBattle(1)
                        }
                        autoBattlePressedTag = null; autoBattleTogglePressed = false; autoBattlePanelPressed = false
                        return true
                    }

                    AutoBattleFlow.Overlay.TUOGUAN -> {
                        if (autoBattlePanelPressed) autoBattleFlow.cancelTuoGuan(AutoBattleFlow.TOUCH_END)
                        autoBattlePanelPressed = false
                        return true
                    }

                    AutoBattleFlow.Overlay.NONE -> Unit
                }
                if (battleMenuOpen) {
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val released = menuIndexAt(world.x, world.y)
                    val pressed = battleMenuPressedIndex
                    battleTraceCoordinator?.recordMenuTap(pressed, released, world.x, world.y)
                    battleMenuPressedIndex = null
                    if (pressed != null && pressed == released) handleBattleMenuTap(pressed)
                    else if (pressed == null) closeBattleMenu()
                    return true
                }
                if (pointerIntent.pressedCapture == BattleInputCapture.MINI_MAP) {
                    if (pointerIntent.releasedTarget == BattleInputTarget.MINI_MAP) miniMapLayer.touch(MiniMapLayer.TOUCH_END)
                    return true
                }
                if (mapTouchPending && !mapTouchMoved &&
                    BattleInteractiveInput.route(scriptRuntime.state, turnController.snapshot.phase) ==
                    BattleInteractiveInput.Route.PLAYER_INPUT
                ) {
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val tile = BattleTileInput.tileAt(
                        world.x, world.y, boardLeft, boardBottom, boardTile, terrainGrid.height,
                    )
                    if (tile.x in 0..boardMaxX && tile.y in 0..boardMaxY) {
                        handleTileClick(tile.x, tile.y)
                    }
                    return true
                }
                val layer = winConditionLayer ?: return false
                if (winConditionButtonPressed && world.x in 957.134f..1213.834f && world.y in 88.204f..148.204f) {
                    layer.onButtonTouch(WinConBoxLayer.TOUCH_END)
                }
                winConditionButtonPressed = false
                return true
            }

            override fun scrolled(amountX: Float, amountY: Float): Boolean {
                if (saveLoadOverlay.dispatch(BattleSaveLoadOverlayController.Intent.Scroll(amountY.toInt())).consumed) return true
                return informationOverlay.dispatch(BattleInformationOverlayController.Intent.Scroll(amountY.toInt())).consumed
            }
        }
    }

    /** 컷신 명령 진행: 대기 Fight 명령을 순서대로 시작하고 경과 시간의 효과음·완료 이벤트를 처리한다. */
    private fun driveFightPresentation(delta: Float) {
        pendingFightCommands.addAll(scriptRuntime.stage.consumeFightCommands())
        if (activeFightCommand == null && pendingFightCommands.isNotEmpty()) {
            val command = pendingFightCommands.removeFirst()
            activeFightCommand = command
            if (command is ScenarioFightCommand.Start) fightOverlayActive = true
            fightCommandSequence++
            Gdx.app.log(
                "JojoGame",
                "FIGHT_FIFO_BEGIN: seq=$fightCommandSequence command=$command pending=${pendingFightCommands.size} runtime=${scriptRuntime.state}",
            )
            fightPresentation.begin(command)
            recordBattleTraceFrame(0f, fullTraceFightCommandObservation(command), advanceFrame = false)
            if (fightPresentation.commandComplete) finishFightCommand(command)
        }
        val command = activeFightCommand ?: return
        val completed = fightPresentation.advance(delta).any { event ->
            when (event) {
                is FightPresentationEvent.Sound -> playFightSound(event)
                else -> Unit
            }
            event is FightPresentationEvent.CommandCompleted && event.command == command
        }
        if (completed) finishFightCommand(command)
    }

    private fun fullTraceFightCommandObservation(command: ScenarioFightCommand): String = when (command) {
        is ScenarioFightCommand.Start ->
            "transition:fight:start:${command.firstUnitId}:${command.secondUnitId}:${command.backgroundIndex}"

        is ScenarioFightCommand.ShowUnit ->
            "transition:fight:showUnit:${command.mine}:${command.text}:${command.entryAction}"

        is ScenarioFightCommand.ShowStart -> "transition:fight:showStart:"
        is ScenarioFightCommand.SetAction -> "transition:fight:setAction:${command.mine}:${command.action}"
        is ScenarioFightCommand.Say -> "transition:fight:say:${command.mine}:${command.text}:${command.flag}"
        is ScenarioFightCommand.Attack2 ->
            "transition:fight:attack2:${command.mine}:${command.style}:${command.defended}"

        is ScenarioFightCommand.Attack1 ->
            "transition:fight:attack1:${command.mine}:${command.style}:${command.critical}"

        is ScenarioFightCommand.Death -> "transition:fight:death:${command.enemy}"
        is ScenarioFightCommand.End -> "transition:fight:end:"
    }

    private fun finishFightCommand(command: ScenarioFightCommand) {
        if (activeFightCommand != command) return
        activeFightCommand = null
        if (command is ScenarioFightCommand.End) fightOverlayActive = false
        Gdx.app.log(
            "JojoGame",
            "FIGHT_FIFO_COMPLETE: seq=$fightCommandSequence command=$command pending=${pendingFightCommands.size} runtime=${scriptRuntime.state}",
        )
        if (command !is ScenarioFightCommand.End) {
            check(scriptRuntime.state == PlaybackState.DELAY) {
                "FightLayer callback completed outside its source pause: $command / ${scriptRuntime.state}"
            }
            scriptRuntime.resumeExternalDelay()
            Gdx.app.log(
                "JojoGame",
                "FIGHT_FIFO_RESUME: seq=$fightCommandSequence command=$command runtime=${scriptRuntime.state}",
            )
            pendingFightCommands.addAll(scriptRuntime.stage.consumeFightCommands())
        }
    }
    private fun playFightSound(event: FightPresentationEvent.Sound) {
        val characterId = fightPresentation.unit(event.side).characterId
        val moveType = characterId?.let(::liveScriptBattleUnit)?.armMoveSound ?: -1
        val dispatch = FightSoundResolver.resolve(event.value, moveType)
        dispatch.backgroundId?.let(scriptRuntime.stage::setBackgroundSound)
        dispatch.effectId?.let(audio::playBattleEffect)
        Gdx.app.log(
            "JojoGame",
            "FIGHT_SOUND: side=${event.side} action=${event.action} at=${event.atActionSeconds} raw=${event.value} resolved=${dispatch.resolvedId}",
        )
    }

    override fun render(rawDelta: Float) {
        updateBattleFrame(rawDelta)?.let(::renderBattleRoutes)
    }

    /** 전투 프레임 갱신: 경로 검증 설정 설치, 시간 누적, 스크립트·애니메이션·전술 대기열을 한 프레임만 진행한다. */
    private fun updateBattleFrame(rawDelta: Float): Float? {
        if (battleCommandRouteState != null) installBattleCommandRouteFixture()
        if (roundRouteState != null && !roundRouteInstalled) installRoundRouteFixture()
        if (miniMapRouteState != null && !miniMapRouteInstalled) installMiniMapRouteFixture()
        installAutoBattleRouteFixture()
        if (battleCharacterRouteState != null) installBattleCharacterRoute()
        if (jiqiRouteFixture) installJiqiRouteFixture()
        installMagickRouteFixture()
        installUsePropertyRouteFixture()
        if (magickPressedRow != null && magickInfoLayer == null && elapsed - magickPressedAt >= 1f) {
            magickListLayer?.tick()?.let {
                magickInfoLayer = MagicInfoLayer(it)
                magickInfoSuppressRelease = true
            }
        }
        if (itemUpgradeRouteState != null && !outcomePresentation.itemUpgradeRouteInstalled) installItemUpgradeRoute()
        val delta = rawDelta * (battleTraceCoordinator?.timeScale ?: 1f)
        elapsed += delta
        game.runtimeBattleDriver()?.commands(RuntimeBattleFrame(delta, elapsed), runtimeProbe())
            ?.forEach(::applyRuntimeBattleCommand)
        completeBattleBackgroundLoadIfReady()
        if (yingchuanEntryFlowTracePath != null && battleInitLayer.view().attached &&
            !scriptRuntime.stage.battleDrawRequested && scriptRuntime.state == PlaybackState.DELAY
        ) {
            yingchuanEntryFlowSawInit = true
        }
        completePendingBattleCommand()
        miniMapLayer.advance(delta)
        outcomePresentation.loseSceneFlow?.update(delta)
        usePropertyLayer?.update(delta)
        val scriptStateBeforeUpdate = scriptRuntime.state
        val scriptedMovementActiveBeforeUpdate = scriptRuntime.stage.units.values.any { it.moveDuration > 0f }
        scriptRuntime.update(
            delta, autoCloseUi = settingsPreferences.getInteger(
                SettingLayer.GAME_SETTING,
                SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
            ) and SettingLayer.AUTO_CLOSE != 0
        )
        battle.syncScriptedOutcome(scriptRuntime.stage.scriptedBattleOutcome)
        if (scriptRuntime.state == PlaybackState.MODAL &&
            scriptRuntime.currentModalKind == ScenarioModalKind.INFO
        ) {
            scriptRuntime.currentModalText?.let { battleInfoReveal.update(it, delta) }
        } else {
            battleInfoReveal.reset()
            battleInfoPanelPressed = false
        }
        scriptedMovementCampTransitionFrameBarrier.observe(
            inCampScript = turnController.snapshot.phase == BattleTurnPhase.CAMP_SCRIPT,
            scriptWasPending = scriptStateBeforeUpdate != PlaybackState.COMPLETE,
            scriptCompleted = scriptRuntime.state == PlaybackState.COMPLETE,
            movementWasActive = scriptedMovementActiveBeforeUpdate,
            movementIsActive = scriptRuntime.stage.units.values.any { it.moveDuration > 0f },
        )
        driveFightPresentation(delta)
        if (scriptRuntime.stage.battleDrawRequested && battleInitLayer.view().attached) {
            battleInitLayer.onDestroy()
        }
        writeYingchuanEntryFlowIfReady()
        outcomePresentation.openRewardRequestIfNeeded()
        outcomePresentation.itemUpgradeFlow?.update(delta)
        outcomePresentation.openEquipmentUpgradeIfNeeded()
        activeRoundLayer?.let { layer ->
            activeRoundLayerElapsed += delta
            layer.elapsed(activeRoundLayerElapsed)
        }
        driveBattleBootstrap()
        if (!scriptedMovementCampTransitionFrameBarrier.yieldBeforeCampTransition()) {
            completeTurnScriptIfReady()
        }
        deathTimeline.driveScriptBarrier()
        if (bootstrapPhase == BattleBootstrapPhase.COMPLETE &&
            autoBattleFlow.view().collocation && turnController.snapshot.phase == BattleTurnPhase.PLAYER_INPUT &&
            scriptRuntime.state == PlaybackState.COMPLETE && battle.outcome() == null && !aiPresentation.hasActiveCamp
        ) turnController.runCollocatedPlayerTurn()
        battleElapsed += delta
        driveMovementTicks()
        applyDueBattleMutations()
        driveSettlementPresentationController()
        scriptedUnitCallbacks.driveHide()
        scriptedUnitCallbacks.driveShow()
        scriptedUnitTimed.driveCameraCenters()
        scriptedUnitTimed.driveMap()
        driveScriptPresentation()
        scriptedUnitActions.driveCallback()
        deathTimeline.tick(animationClock())
        pruneCombatPresentation()
        playPendingMagicEffectSounds()
        startQueuedPhysicalPassPresentation()
        startQueuedFollowUpPresentation()
        startQueuedCounterPresentation()
        startQueuedCounterFollowUpPresentation()
        startQueuedMagicPassPresentation()
        resumeCriticalSpeechAction()
        aiPresentation.drive()
        if (bootstrapPhase == BattleBootstrapPhase.COMPLETE &&
            dialogueStepCapture == null && !selectionOverlayCapture &&
            !outcomePresentation.loseSceneActive && NaturalBattleTransition.resultScriptReadyForLoseScene(
                battle.outcome(), scriptRuntime.state, scriptRuntime.currentDialogue != null,
            ) &&
            actionAnimation?.let { animationClock() < it.endsAt } != true &&
            movementAnimation?.let { animationClock() < it.endsAt } != true &&
            hitReactionAnimations.values.none { animationClock() < it.endsAt } &&
            deathAnimations.values.none { animationClock() < it.endsAt } &&
            !deathTimeline.isBusy() &&
            !scriptedUnitCallbacks.hideBusy && !scriptedUnitCallbacks.showBusy
            && !combatPresentationBusy()
            && !outcomeCallbacksPending()
        ) outcomePresentation.enterLoseScene()
        if (pendingBattleScriptPassesAfterAction > 0 && scriptRuntime.state == PlaybackState.COMPLETE) {
            if (!pendingBattleActionCommitted) {
                commitDeferredBattleAction(pendingBattleSettlementActorId)
                pendingBattleActionCommitted = true
            }
            when {
                pendingBattleCompletedScriptPasses == 0 && !combatPresentationBusy() -> {
                    pendingBattleCompletedScriptPasses = 1
                    runBattleScript()
                }

                pendingBattleCompletedScriptPasses == 1 && !deathTimeline.startedPostActionDeaths() -> {
                    if (deathTimeline.queuePostAction(collectDyingPresentationUnits()))
                        Unit
                    else finishManualUnitDeathCallbacks()
                }

                pendingBattleCompletedScriptPasses == 1 && !combatPresentationBusy() -> {
                    pendingBattleCompletedScriptPasses = 2
                    runBattleScript()
                }

                pendingBattleCompletedScriptPasses == 2 -> finishManualUnitDeathCallbacks()
            }
        }
        if (scriptStateBeforeUpdate != PlaybackState.COMPLETE || scriptRuntime.state != PlaybackState.COMPLETE) syncScriptedUnits()
        scriptedUnitCallbacks.drivePosts()
        syncDialogueSpeakerPresentation()
        outcomePresentation.driveNaturalBattleCompletion()
        unitPresentationStore.synchronize(battle.presentation.presentationUnits())
        if (battleRouteCompleted) return null
        return delta
    }

    private fun applyRuntimeBattleCommand(command: RuntimeBattleCommand) {
        when (command) {
            RuntimeBattleCommand.AdvanceDialogue -> advanceBattleDialogue()
            is RuntimeBattleCommand.Tap -> handleTileClick(command.x.toInt(), command.y.toInt())
            RuntimeBattleCommand.EndTurn -> if (!turnController.endPlayerTurn()) {
                eventMessage = "턴 전환을 시작할 수 없습니다."
            }
        }
    }
    private fun renderBattleRoutes(delta: Float) {
        if (renderDedicatedBattleRoute(delta)) return
        renderBattlefieldRoute(delta)
    }
    private fun renderDedicatedBattleRoute(delta: Float): Boolean {
        if (winConditionRouteState != null) {
            battle.units.values.firstOrNull { it.characterId == 235 }?.let { scriptedUnitPresentation.clearVisual(it.id) }
        }
        dialogueComponentStage?.let { stage ->
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
            scriptRuntime.currentDialogue?.let { dialogueReveal.update(it.text, delta) }
            if (scriptRuntime.currentDialogue != null) {
                if (stage == RuntimeBattleRoute.DIALOGUE_COMPONENT_BACKGROUND ||
                    stage == RuntimeBattleRoute.DIALOGUE_COMPONENT_CHARACTERS
                ) drawGrid()
                drawScriptDialogue(stage.name.removePrefix("DIALOGUE_COMPONENT_").lowercase())
            }
            if (elapsed > 6f) game.captureFrameIfRequested()
            return true
        }
        if (battleMenuRoute) {
            drawBattleMenu()
            if (elapsed > .25f) game.writeRenderEventLogIfRequested()
            return true
        }
        captureFixtureCoordinator.update(
            BattleCaptureFixtureCoordinator.Frame(
                elapsed = elapsed,
                dialogueState = scriptRuntime.state,
                dialogueVisible = scriptRuntime.currentDialogue != null,
                dialogueComplete = dialogueReveal.isComplete,
            ),
            captureFixturePort,
        )
        if (captureFixtureCoordinator.consumeActionLog(elapsed)) {
            actionAnimation?.let { animation ->
                val unit = battle.units[animation.unitId]
                Gdx.app.log(
                    "JojoGame",
                    "ACTION_CAPTURE_FRAME: elapsed=$elapsed, sourceY=${
                        unitSpriteFrameResolver.clipFrame(
                            animation.sourceAction,
                            animation.direction,
                            animationClock() - animation.startedAt
                        )?.sourceY
                    }, active=${animationClock() < animation.endsAt}, " +
                            "unit=${unit?.characterId}, tile=${unit?.tileX},${unit?.tileY}, visible=${unit?.visible}, " +
                            "screen=${unit?.let { boardLeft + it.tileX * boardTile }},${unit?.let { tileBottom(it.tileY) }}",
                )
            }
        }
        audio.sync(scriptRuntime.stage)
        scriptRuntime.stage.consumeShowWinCondition()?.let { text ->
            scriptWinConditions = WinConditionsLayer().also { layer ->
                layer.onCreate(text, scenarioMaxRound()) {
                    scriptWinConditions = null
                    if (scriptRuntime.state == PlaybackState.MODAL) scriptRuntime.resumeModal()
                }
            }
        }
        recordBattleTraceFrame(delta)
        if (battleTraceCoordinator?.isFinished == false && battle.outcome() != null && scriptRuntime.state == PlaybackState.COMPLETE) {
            battleTraceCoordinator.finish("battle-end")
        }
        scriptRuntime.currentDialogue?.let {
            dialogueReveal.update(it.text, delta)
            if (battleDialogueBlendRoute) dialogueReveal.revealAllIfPending()
            val autoCloseEnabled = !verification.active &&
                    !game.hasFrameCaptureRequest() && !game.hasRenderEventLogRequest() &&
                    settingsPreferences.getInteger(
                        SettingLayer.GAME_SETTING,
                        SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
                    ) and SettingLayer.AUTO_CLOSE != 0
            if (sayAutoClose.update(dialogueReveal.isComplete, autoCloseEnabled, delta)) {
                advanceBattleDialogue()
            }
        }
        Gdx.gl.glClearColor(0.10f, 0.14f, 0.12f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        if (outcomePresentation.loseSceneActive) {
            drawLoseScene()
            outcomePresentation.loseSceneFlow?.takeIf { it.state == LoseSceneFlow.State.PROMPT }?.let { drawLosePrompt() }
            if (loseRestartRoute && elapsed > 3.25f && game.writeRenderEventLogIfRequested()) return true
            game.captureFrameIfRequested()
            return true
        }
        if (battleEdit2RouteState != null) {
            drawBattleEdit2Route()
            if (elapsed > .25f) game.writeRenderEventLogIfRequested()
            return true
        }
        if (battleTerrainRoute) {
            batch.projectionMatrix = viewport.camera.combined
            informationOverlay.terrainView()?.let(battleTerrainOverlayRenderer::draw)
            if (elapsed > .25f) game.writeRenderEventLogIfRequested()
            return true
        }
        return false
    }
    private fun renderBattlefieldRoute(delta: Float) {
        val requestedDither = if (mapOnlyCapture) game.requestedMapDither() else null
        val priorDither = requestedDither?.let { Gdx.gl.glIsEnabled(GL20.GL_DITHER) }
        requestedDither?.let { enabled ->
            if (enabled) Gdx.gl.glEnable(GL20.GL_DITHER) else Gdx.gl.glDisable(GL20.GL_DITHER)
        }
        val requestedFilter = if (mapOnlyCapture) game.requestedMapFilter() else null
        val priorFilter = requestedFilter?.let { mapTexture?.let { texture -> texture.minFilter to texture.magFilter } }
        requestedFilter?.let { filter -> mapTexture?.setFilter(filter, filter) }
        drawGrid()
        drawScriptPresentationOverlay()
        priorFilter?.let { (min, mag) -> mapTexture?.setFilter(min, mag) }
        priorDither?.let { enabled ->
            if (enabled) Gdx.gl.glEnable(GL20.GL_DITHER) else Gdx.gl.glDisable(GL20.GL_DITHER)
        }
        if (fightOverlayActive) {
            fightRenderer.draw(fightPresentationView())
            game.captureFrameIfRequested()
            return
        }
        if (battleCharacterRouteState != null) {
            if (elapsed > .25f) {
                if (!game.writeRenderEventLogIfRequested()) game.captureFrameIfRequested()
            }
            return
        }
        if (battleInitRoute) {
            drawBattleHudChrome()
            drawRewardSectionOverlay()
            if (game.writeRenderEventLogIfRequested()) return
            return
        }
        if (!mapOnlyCapture) {
            drawBattleHudChrome()
            if (battleMenuOpen) drawBattleMenu()
            helperOverlay.view()?.let { view ->
                batch.projectionMatrix = viewport.camera.combined
                battleHelperOverlayRenderer.draw(view)
            }
            informationOverlay.terrainView()?.let { view ->
                batch.projectionMatrix = viewport.camera.combined
                battleTerrainOverlayRenderer.draw(view)
            }
            informationOverlay.propertyView()?.let { view ->
                batch.projectionMatrix = viewport.camera.combined
                battlePropertyOverlayRenderer.draw(view)
            }
            informationOverlay.treasureView()?.let(::drawTreasureLayer)
            saveLoadOverlay.view(BattleSaveLoadOverlayController.Mode.SAVE)?.let { view ->
                batch.projectionMatrix = viewport.camera.combined
                battleSaveLoadOverlayRenderer.draw(view)
            }
            settingsOverlay.view()?.let { view ->
                batch.projectionMatrix = viewport.camera.combined
                battleSettingsOverlayRenderer.draw(view)
            }
            saveLoadOverlay.view(BattleSaveLoadOverlayController.Mode.LOAD)?.let { view ->
                batch.projectionMatrix = viewport.camera.combined
                battleSaveLoadOverlayRenderer.draw(view)
            }
            forcesOverlay.view()?.let { view ->
                batch.projectionMatrix = viewport.camera.combined
                battleForcesOverlayRenderer.draw(view)
            }
            unitInfoOverlay.view()?.let { view ->
                batch.projectionMatrix = viewport.camera.combined
                battleUnitInfoOverlayRenderer.draw(view)
            }
            if (jiqiLayer != null) drawJiqiLayer()
            if (magickListLayer != null) drawMagickListLayer()
            if (magickInfoLayer != null) drawBattleMagicInfoLayer()
            if (usePropertyLayer != null) drawUsePropertyLayer()
            if (usePropertyDetail != null) drawUsePropertyDetail()
            if (battleCommandFlow.phase == BattleCommandFlow.Phase.COMMAND) drawBattleCommandLayer()
            activeRoundLayer?.let(::drawRoundLayer)
            drawSettlementOverlays()
            if (verification.usesTutorialBattle) drawHud()
            if (!selectionOverlayCapture && !actionCaptureMode && miniMapRouteState == null && !battleMenuOpen && saveLoadOverlay.view(BattleSaveLoadOverlayController.Mode.SAVE) == null && helperOverlay.view() == null && !forcesOverlay.isVisible() && !unitInfoOverlay.isVisible() && jiqiLayer == null && magickListLayer == null && magickInfoLayer == null && usePropertyLayer == null && usePropertyDetail == null && activeRoundLayer == null && battleCommandFlow.phase != BattleCommandFlow.Phase.COMMAND && autoBattleFlow.view().overlay == AutoBattleFlow.Overlay.NONE) {
                drawScriptDialogue()
                drawScriptChoice()
                drawScriptInfoLayer()
            }
            batch.projectionMatrix = viewport.camera.combined
            battleAutoOverlayRenderer.draw(battleAutoOverlayView())
            if (winConditionOpen) drawWinConditionBox()
            if (outcomePresentation.winPromptActive) drawSavePrompt()
            scriptWinConditions?.let { drawScriptWinConditions(it) }
            if (outcomePresentation.rewardActive || rewardRouteState != null) {
                batch.projectionMatrix = viewport.camera.combined
                battleRewardOverlayRenderer.draw(battleRewardOverlayView())
            }
            outcomePresentation.itemUpgradeFlow?.let(::drawItemUpgrade)
            if (itemUpgradeRouteState != null) drawRewardSectionOverlay()
        }
        val dedicatedCaptureRoute = rewardRouteState != null ||
            itemUpgradeRouteState != null ||
            jiqiRouteFixture ||
            magickRouteState != null ||
            usePropertyRouteState != null ||
            roundRouteState != null ||
            winConditionRouteState != null ||
            miniMapRouteState != null ||
            autoBattleRouteState != null ||
            battleCommandRouteState != null ||
            otherUnitInfoRoute ||
            mineUnitInfoRoute ||
            (battleDialogueBlendRoute && scriptRuntime.currentDialogue != null && dialogueReveal.isComplete)
        if (BattleCaptureRouteCoordinator.shouldWriteRenderEventLog(
                BattleCaptureRouteCoordinator.RenderEventLogInput(elapsed, dedicatedCaptureRoute),
            ) && game.writeRenderEventLogIfRequested()
        ) return
        val captureAt = captureFixtureCoordinator.captureAt(
            BattleCaptureFixtureCoordinator.Frame(
                elapsed = elapsed,
                dialogueState = scriptRuntime.state,
                dialogueVisible = scriptRuntime.currentDialogue != null,
                dialogueComplete = dialogueReveal.isComplete,
            ),
        )
        if (captureFixtureCoordinator.consumeDialogueLog(elapsed, captureAt)) {
            val dialogue = scriptRuntime.currentDialogue
            val profile = dialogue?.speakerId?.toIntOrNull()?.let(gameDataCatalog::unitProfile)
            val speakerName = profile?.name?.let(GameDataCatalog::sayLayerUnitName).orEmpty()
            Gdx.app.log(
                "JojoGame",
                "DIALOGUE_CAPTURE_STATE: speaker=${dialogue?.speakerId} name=$speakerName text=${dialogue?.text} face=${profile?.face} head=${
                    profile?.face?.plus(8)
                }"
            )
            val speakerUnit = dialogue?.speakerId?.toIntOrNull()?.let { characterId ->
                (battle.units.values + battle.presentation.pendingPresentationUnits())
                    .firstOrNull { it.characterId == characterId && it.visible }
            }
            val speakerVisual = speakerUnit?.let(::visualTile)
            val speakerCenterY = speakerVisual?.let { 1776f + battleCamera.y - it.second * 96f }
            val panelY = speakerCenterY?.let { if (it < viewport.worldHeight / 2f) it + 92f else it - 328f }
            Gdx.app.log(
                "JojoGame",
                "DIALOGUE_LAYOUT_CAPTURE: revision=${scriptRuntime.dialogueRevision} camera=${battleCamera.x},${battleCamera.y} " +
                        "visual=$speakerVisual centerY=$speakerCenterY panelY=$panelY",
            )
            Gdx.app.log("JojoGame", "DIALOGUE_CAPTURE_UNITS: " + battle.units.values.joinToString(";") { unit ->
                "${unit.id}/${unit.characterId}@${unit.tileX},${unit.tileY}/d${unit.direction}/v${unit.visible}"
            })
        }
        if (captureFixtureCoordinator.consumeSelectionLog(elapsed, captureAt)) {
            val selected = selectedUnitId?.let(battle.units::get)
            val tiles = selectableAreaTiles()
            val count = { frame: SelectAreaFrame ->
                if (hudAssets.selectAreaTextures[frame.assetName] == null) 0 else tiles.count { it.frame == frame }
            }
            val cursorRendered = selected != null && hudAssets.battleCursorTexture != null
            Gdx.app.log(
                "JojoGame",
                "SELECTION_CAPTURE_STATE: unit=${selected?.characterId}@${selected?.tileX},${selected?.tileY} " +
                        "move=${count(SelectAreaFrame.BLUE) + count(SelectAreaFrame.GREEN)} " +
                        "moveFrame=${if (count(SelectAreaFrame.BLUE) > 0) "blue" else "green"} " +
                        "attack=${count(SelectAreaFrame.RED_BOX)} " +
                        "cursor=$cursorRendered",
            )
        }
        BattleCaptureRouteCoordinator.frameCaptureCommands(
            BattleCaptureRouteCoordinator.FrameCaptureInput(
                elapsed = elapsed,
                captureAt = captureAt,
                mapOnlyCapture = mapOnlyCapture,
                battleMenuRoute = battleMenuRoute,
                winModalRoute = winModalRoute,
                battleMenuOpen = battleMenuOpen,
                winConditionOpen = winConditionOpen,
                winConditionLayerPresent = winConditionLayer != null,
                scriptWinConditionModalCount = if (scriptWinConditions != null) 1 else 0,
            ),
        ).forEach { command ->
            when (command) {
                BattleCaptureRouteCoordinator.Command.WriteMapQuadCandidateSidecar ->
                    game.writeMapQuadCandidateSidecar()

                is BattleCaptureRouteCoordinator.Command.WriteCaptureStack -> game.writeCaptureStack(
                    requested = command.requested,
                    requestedPresent = command.requestedPresent,
                    dialogue = command.dialogue,
                    choice = command.choice,
                    modalCount = command.modalCount,
                )

                BattleCaptureRouteCoordinator.Command.CaptureFrame ->
                    if (game.captureFrameIfRequested()) return
            }
        }
        BattleScreenVerificationCoordinator.validateAndFinish(
            verification,
            BattleScreenVerificationInput(
                elapsed, sourceScenario, scriptRuntime.stage.battleUnits.size, scriptRuntime.stage.battleMapIndex,
                mapTexture != null, battle,
                scriptRuntime.stage.battleUnits.values.any { it.faction == ScenarioUnitFaction.MINE },
                { unit -> unit.characterId != null && unitTexture(unit) != null }, ::endTurn,
            ),
        )
    }

    private fun battleTraceFightJson(): String =
        BattleFightTraceSerializer.serialize(fightOverlayActive, fightPresentation, fightSprites)

    private fun recordBattleTraceFrame(
        delta: Float,
        observation: String? = null,
        advanceFrame: Boolean = true,
    ) {
        val coordinator = battleTraceCoordinator ?: return
        val bootstrapComplete = bootstrapPhase == BattleBootstrapPhase.COMPLETE
        val traceCamp = if (bootstrapComplete) battle.activeFaction.ordinal else -1
        val traceOutcome = battle.outcome().takeIf { bootstrapComplete }
        val dialogueSourceText = scriptRuntime.currentDialogueSourceText
        val dialogueText = dialogueSourceText?.let { ScenarioInterpreter.parseDialogueBlocks(it) }
            ?.joinToString("\n") { it.text }.orEmpty()
        val aiResolution = aiPresentation.resolution
        val aiTrace = aiResolution?.let { resolution ->
            val actor = battle.presentation.presentationUnit(resolution.actorId)?.characterId ?: -1
            val target = resolution.targetId?.let(battle.presentation::presentationUnit)
            coordinator.projectAiPresentation(
                aiPresentation.stage.toString(), resolution, actor, target?.characterId ?: -1,
                resolution.targetId?.let(resolution.healthBeforeAction::get) ?: -1,
                battle.pendingActionTransaction != null,
            )
        }
        val units = battle.presentation.presentationUnits()
            .sortedWith(compareBy<BattleUnit>({ it.faction.ordinal }, { it.id }))
            .map { unit ->
                coordinator.projectUnitPresentation(
                    BattleTraceUnitPresentationInput(
                        unit, animationClock(), movementAnimation, actionAnimation, hitReactionAnimations[unit.id],
                        deathAnimations[unit.id], scriptedUnitPresentation.visual(unit.id),
                        terrainGrid.terrainAt(unit.tileX, unit.tileY), visualTile(unit),
                        unitSpriteFrameResolver.defaultAction(unit).action, battleElapsed,
                    ),
                    unitSpriteFrameResolver::clipFrame,
                )
            }
        coordinator.recordFrame(
            RuntimeBattleTraceFrameInput(
                0L, elapsed, delta, battle.round, traceCamp, battle.maxRounds,
                battle.units.values.count { it.type() == Faction.PLAYER },
                battle.units.values.count { it.type() == Faction.FRIEND },
                battle.units.values.count { it.type().isEnemySide() },
                scriptRuntime.state != PlaybackState.COMPLETE, traceOutcome != null,
                autoBattleFlow.view().collocation,
                RuntimeBattleTraceDialogueInput(
                    scriptRuntime.state == PlaybackState.DIALOGUE, scriptRuntime.dialogueLifecycleRevision,
                    dialogueSourceText, scriptRuntime.currentDialogue?.speakerId.orEmpty(), dialogueText,
                ),
                turnController.snapshot.phase.toString(), scriptRuntime.state.toString(),
                if (bootstrapComplete) emptyList() else bootstrapPresentationBusyReasons(),
                battleCamera.contentX, battleCamera.contentY, 0, "null", battleTraceFightJson(),
                aiTrace, battle.traceActions.toList(), units,
                coordinator.driverInput(
                    selectedUnitId,
                    battleCommandFlow.phase.toString(),
                    eventMessage,
                    autoBattleFlow.view().overlay.toString(),
                ),
                observation, scriptRuntime.stage.battleEndedByScript, scriptRuntime.stage.scriptedBattleOutcome?.name,
                outcomePresentation.resultFlow.toString(), scriptRuntime.currentModalKind?.name,
                pendingBattleScriptPassesAfterAction, aiPresentation.unitDeathScriptPass, deathTimeline.startedPostActionDeaths(),
                aiPresentation.resolution != null, aiPresentation.activeCamp?.toString(), activeRoundLayer != null,
                settlementPresentation.isActive(), combatPresentationBusy(),
            ),
            advanceFrame,
        )
    }

    internal fun recordFullBattleInput(context: String) {
        battleTraceCoordinator?.recordInput(context)
    }
    internal fun runtimeProbe(): BattleRuntimeScreenProbe {
        fun screenPoint(x: Int, y: Int): Pair<Int, Int> {
            val projected = viewport.project(
                Vector2(
                    boardLeft + x * boardTile + boardTile / 2f,
                    tileBottom(y) + boardTile / 2f,
                )
            )
            return projected.x.toInt() to (Gdx.graphics.height - projected.y).toInt()
        }

        fun projectWorldPointAt(worldX: Float, worldY: Float): Pair<Int, Int> {
            val projected = viewport.project(Vector2(worldX, worldY))
            return projected.x.toInt() to (Gdx.graphics.height - projected.y).toInt()
        }

        val autoView = autoBattleFlow.view()
        return BattleRuntimeProbeCoordinator.create(
            BattleRuntimeScreenProbeInput(
                sourceScenario, scriptRuntime.state, battle.outcome(), bootstrapPhase == BattleBootstrapPhase.COMPLETE,
                initialPlayerCampScriptStarted, resultScene1Observed || outcomePresentation.naturalOutcomeScriptStarted,
                outcomePresentation.postBattleSceneStarted, outcomePresentation.rewardActive, scriptWinConditions != null,
                outcomePresentation.winPromptActive, outcomePresentation.loseSceneFlow?.state == LoseSceneFlow.State.PROMPT,
                projectWorldPointAt(844.186f, 296.285f), playerMoveCommitted, game.campaignStage(), turnController.snapshot.phase.name,
                battleMenuOpen, battleCommandFlow.phase == BattleCommandFlow.Phase.COMMAND,
                battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION, magickListLayer != null, magicMode,
                projectWorldPointAt(1060.6f, 225.42f), projectWorldPointAt(15.13372f + 8f * 88f + 44f, 160.29f),
                projectWorldPointAt(1383.9535f, 38f), projectWorldPointAt(579.4365f, 295.197f),
                projectWorldPointAt(919.536f, 295.197f),
                autoView.overlay.name, autoView.checked, autoView.collocation, committedPlayerMove,
                scriptRuntime.selectedChoice, selectedUnitId,
            ),
            object : BattleRuntimeProbePort {
                override val round get() = battle.round
                override val activeFaction get() = battle.activeFaction
                override fun units() = battle.units.values
                override fun reachableTiles(unitId: String): Set<RuntimeGridPoint> =
                    battle.movement.reachableTiles(unitId).keys.mapTo(linkedSetOf()) { RuntimeGridPoint(it.first, it.second) }
                override fun canEnterTilesIgnoringEnemyWithinMoves(
                    unitId: String,
                    ignoredEnemyId: String,
                    start: RuntimeGridPoint,
                    targetTiles: Set<RuntimeGridPoint>,
                    moves: Int,
                ) = battle.movement.canEnterTilesIgnoringEnemyWithinMoves(
                    unitId, ignoredEnemyId, start.x to start.y,
                    targetTiles.mapTo(linkedSetOf()) { it.x to it.y }, moves,
                )
                override fun physicalDamagePreview(attackerId: String, targetId: String) =
                    battle.combat.physicalDamagePreview(attackerId, targetId)
                override fun screenPoint(tile: RuntimeGridPoint) = screenPoint(tile.x, tile.y)
                    .let { RuntimeGridPoint(it.first, it.second) }
                override fun projectWorldPoint(x: Float, y: Float) = projectWorldPointAt(x, y)
                    .let { RuntimeGridPoint(it.first, it.second) }
            },
        )
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        configureSourceCameraViewport()
    }

    private fun endTurn() {
        battle.outcome()?.let {
            eventMessage = outcomeText(it)
            return
        }
        selectedUnitId = null
        if (!turnController.endPlayerTurn()) eventMessage = "턴 전환을 시작할 수 없습니다."
    }
    private fun scheduleCombatPresentation(
        result: TacticalActionResult,
        actorId: String?,
        targetId: String?,
        healthBeforeAction: Map<String, Int>,
    ) {
        val attack = result as? TacticalActionResult.Attack ?: return
        val actor = actorId ?: return
        val target = targetId ?: return
        val animation = actionAnimation ?: return
        val hitAt =
            animation.startedAt + requireNotNull(battleSprites.hitTime(animation.sourceAction, animation.direction)) {
                "원본 BRAnime anime${animation.sourceAction} 방향 ${animation.direction}에 hit 이벤트가 없습니다"
            }
        val targetUnit = battle.presentation.presentationUnit(target) ?: return
        val before = healthBeforeAction[target] ?: targetUnit.hitPoints
        val deferredMutation = battle.pendingActionTransaction?.takeIf { it.actorId == actor }
        val unitVisualStates = combatPresentationUnitVisualStates()
        val deferredInitialMp = unitVisualStates.keys.associateWith { id -> deferredMutation?.initialMp(id) }
        BattleCombatPresentationQueueCoordinator.hitPhysicalQueuePlan(
            attack, actor, target, healthBeforeAction, deferredInitialMp, unitVisualStates,
        )?.let { plan ->
            val queue = PhysicalPassPresentationQueue(
                passes = plan.passes,
                nextPassIndex = plan.nextPassIndex,
                startsAt = hitAt,
                visualHp = plan.visualState.hitPoints.toMutableMap(),
                visualMp = plan.visualState.magicPoints.toMutableMap(),
                counterMagicId = plan.counterMagicId,
                counterMagic = plan.counterMagic,
                counterCasterId = plan.counterCasterId,
                counterTargetId = plan.counterTargetId,
            )
            val passEndsAt = schedulePhysicalPassTargets(
                pass = plan.passes.first(),
                animation = animation,
                hitAt = hitAt,
                queue = queue,
            )
            if (plan.continuesAfterCurrentPass()) {
                queue.startsAt = passEndsAt
                queuedPhysicalPresentation = queue
            }
            return
        }
        scheduleBattleMutation(hitAt) {
            battle.presentation.presentationUnit(target)?.let(::focusCameraOn)
            audio.playBattleEffect(
                when {
                    !attack.hit -> 30 // BLOCK_QING
                    attack.critical -> 36 // HARM_ZHONG
                    else -> 35 // HARM_QING
                }
            )
            if (attack.hit) {
                deferredMutation?.commitVitals(
                    target,
                    hp = (before - attack.damage).coerceAtLeast(0),
                    mp = deferredMutation.initialMp(target)?.minus(attack.mpShieldDamage)?.coerceAtLeast(0),
                )
                deferredMutation?.commitNextHitSideEffect()
            }
        }
        if (!attack.hit) {
            val direction = battleDirection(target, actor)
            val reactionEndsAt = hitAt + requireSourceActionDuration(26, direction)
            actionAnimation = animation.copy(endsAt = reactionEndsAt)
            scheduleHitReaction(target, direction, hitAt, reactionEndsAt, 26)
            BattleCombatPresentationQueueCoordinator.missedPhysicalQueuePlan(
                attack, actor, target, healthBeforeAction, deferredInitialMp, unitVisualStates,
            )?.let { plan ->
                queuedPhysicalPresentation = PhysicalPassPresentationQueue(
                    passes = plan.passes,
                    nextPassIndex = plan.nextPassIndex,
                    startsAt = reactionEndsAt,
                    visualHp = plan.visualState.hitPoints.toMutableMap(),
                    visualMp = plan.visualState.magicPoints.toMutableMap(),
                    counterMagicId = plan.counterMagicId,
                    counterMagic = plan.counterMagic,
                    counterCasterId = plan.counterCasterId,
                    counterTargetId = plan.counterTargetId,
                )
            }
            return
        }
        if (attack.damage == 0 && attack.mpShieldDamage == 0) return
        val hitSequence = BattlePhysicalPresentationTimeline.sequence(
            primaryId = target,
            primaryDamage = attack.damage,
            splash = attack.splashTargets,
            hitAt = hitAt,
            durationFor = { id -> requireSourceActionDuration(32, battleDirection(id, actor)) },
        )
        val primaryHit = hitSequence.first()
        primaryHit.endsAt - primaryHit.startsAt
        actionAnimation = animation.copy(endsAt = hitSequence.last().endsAt)
        scheduleHitReaction(target, battleDirection(target, actor), primaryHit.startsAt, primaryHit.endsAt, 32)
        val firstTo = (before - attack.damage).coerceAtLeast(0)
        if (attack.damage > 0) healthTimeline.schedule(target, before, firstTo, primaryHit.startsAt)
        harmNumberAnimations[target] = HarmNumberAnimation(
            amount = if (attack.mpShieldDamage > 0) attack.mpShieldDamage else attack.damage,
            isHp = attack.mpShieldDamage == 0,
            startedAt = primaryHit.startsAt,
            endsAt = primaryHit.endsAt,
        )
        hitSequence.drop(1).forEach { splashHit ->
            val splashUnit = battle.presentation.presentationUnit(splashHit.targetId) ?: return@forEach
            val splashBefore = healthBeforeAction[splashHit.targetId] ?: (splashUnit.hitPoints + splashHit.damage)
            val splashAfter = (splashBefore - splashHit.damage).coerceAtLeast(0)
            scheduleHitReaction(
                splashHit.targetId, battleDirection(splashHit.targetId, actor),
                splashHit.startsAt, splashHit.endsAt, 32,
            )
            scheduleBattleMutation(splashHit.startsAt) {
                battle.presentation.presentationUnit(splashHit.targetId)?.let(::focusCameraOn)
                deferredMutation?.commitVitals(splashHit.targetId, hp = splashAfter)
                deferredMutation?.commitNextHitSideEffect()
            }
            healthTimeline.schedule(
                splashHit.targetId,
                splashBefore,
                splashAfter,
                splashHit.startsAt,
            )
            harmNumberAnimations[splashHit.targetId] =
                HarmNumberAnimation(splashHit.damage, true, splashHit.startsAt, splashHit.endsAt)
        }
        val areaEndsAt = hitSequence.last().endsAt
        when (BattlePhysicalPresentationPlanner.followUpOrCounter(
            BattleFollowUpCounterPlanInput(
                hasFollowUp = attack.followUpDamage > 0 || attack.followUpMpShieldDamage > 0,
                hasCounter = attack.counterDamage > 0 || attack.counterMpShieldDamage > 0,
                targetHpAfterPrimary = firstTo,
                defeated = attack.defeated,
            ),
        )) {
            BattleFollowUpCounterDecision.FOLLOW_UP -> queuedFollowUpPresentation = FollowUpPresentation(
                attackerId = actor,
                targetId = target,
                harm = attack.followUpDamage,
                targetHpBefore = firstTo,
                critical = attack.followUpCritical,
                mpShieldDamage = attack.followUpMpShieldDamage,
                startsAt = areaEndsAt,
                counterDamage = attack.counterDamage,
                counterMpShieldDamage = attack.counterMpShieldDamage,
                counterCritical = attack.counterCritical,
                counterLifeStealHealing = attack.counterLifeStealHealing,
                counterTargetHpBefore = healthBeforeAction[actor] ?: battle.presentation.presentationUnit(actor)?.hitPoints ?: 0,
            )

            BattleFollowUpCounterDecision.COUNTER -> {
                val actorUnit = battle.presentation.presentationUnit(actor) ?: return
                val actorBefore = healthBeforeAction[actor] ?: actorUnit.hitPoints
                queuedCounterPresentation = CounterPresentation(
                    attackerId = target,
                    targetId = actor,
                    harm = attack.counterDamage,
                    targetHpBefore = actorBefore,
                    critical = attack.counterCritical,
                    mpShieldDamage = attack.counterMpShieldDamage,
                    followUpDamage = attack.counterFollowUpDamage,
                    followUpMpShieldDamage = attack.counterFollowUpMpShieldDamage,
                    followUpCritical = attack.counterFollowUpCritical,
                    startsAt = areaEndsAt,
                )
            }

            BattleFollowUpCounterDecision.NONE -> Unit
        }
    }
    private fun scheduleMagicPresentation(
        result: TacticalActionResult.Magic,
        casterId: String?,
        magic: GameDataCatalog.MagicProfile?,
        healthBeforeAction: Map<String, Int>,
        initialMp: Map<String, Int> = emptyMap(),
        effectAnimations: List<MagicEffectAnimation> = magicEffectAnimations,
        reaction: Boolean = false,
    ) {
        val caster = casterId ?: return
        val visualHp = healthBeforeAction.toMutableMap()
        val deferredMutation = battle.pendingActionTransaction?.takeIf { reaction || it.actorId == caster }
        val visualMp = initialMp.toMutableMap()
        val fallbackMagicPoints = battle.presentation.presentationUnits().associate { unit ->
            unit.id to (deferredMutation?.initialMp(unit.id) ?: unit.magicPoints)
        }
        result.passes.forEachIndexed { passIndex, pass ->
            val effect = effectAnimations.getOrNull(passIndex)
            val effectStartedAt = effect?.startedAt ?: (actionAnimation?.endsAt ?: animationClock())
            val effectEndsAt = effect?.endsAt ?: effectStartedAt
            val targetInputs = pass.mapNotNull { target ->
                val unit = battle.presentation.presentationUnit(target.targetId) ?: return@mapNotNull null
                val sourceAction = if (target.hit) 3 else 26
                BattleMagicPresentationPlanner.TargetInput(
                    target,
                    requireSourceActionDuration(sourceAction, unit.direction),
                )
            }
            val plan = BattleMagicPresentationPlanner.plan(
                BattleMagicPresentationPlanner.Input(
                    casterId = caster,
                    profile = magic,
                    effectStartedAt = effectStartedAt,
                    effectEndsAt = effectEndsAt,
                    effectHitOffset = effect?.let { magicEffects.effect(it.effectId)?.hitTime },
                    targets = targetInputs,
                    visualState = BattleCombatPresentationQueueCoordinator.VisualState(visualHp, visualMp),
                    units = magicPresentationPlannerUnitStates(fallbackMagicPoints),
                ),
            )
            if (plan.mcall) plan.primaryFocusId?.let { primaryId ->
                scheduleBattleMutation(effectStartedAt) {
                    battle.presentation.presentationUnit(primaryId)?.let { focusCameraOn(it, forceCenter = true) }
                }
            }
            plan.targetIds.forEach { targetId ->
                scheduleBattleMutation(plan.targetFocusAt) {
                    battle.presentation.presentationUnit(targetId)?.let { focusCameraOn(it, forceCenter = true) }
                }
            }
            plan.reactions.forEach { reactionPlan ->
                scheduleHitReaction(
                    reactionPlan.targetId,
                    reactionPlan.direction,
                    reactionPlan.startsAt,
                    reactionPlan.endsAt,
                    reactionPlan.sourceAction,
                )
            }
            plan.changes.forEach { change ->
                val unit = battle.presentation.presentationUnit(change.unitId) ?: return@forEach
                if (change.hpAdd != 0) {
                    healthTimeline.schedule(change.unitId, change.hpBefore, change.hpAfter, plan.effectAt)
                }
                if (deferredMutation != null && (change.hpAdd != 0 || change.mpAdd != 0)) {
                    scheduleBattleMutation(plan.effectAt) {
                        deferredMutation.commitVitals(change.unitId, change.hpAfter, change.mpAfter)
                        if (change.hpAdd < 0) deferredMutation.commitNextHitSideEffect()
                    }
                }
                if (change.harmNumberValue != 0) {
                    val duration = requireSourceActionDuration(change.harmNumberAction, unit.direction)
                    harmNumberAnimations[change.unitId] =
                        HarmNumberAnimation(
                            change.harmNumberValue,
                            change.harmNumberIsHp,
                            plan.effectAt,
                            plan.effectAt + duration,
                        )
                }
            }
            visualHp.clear()
            visualHp.putAll(plan.nextVisualState.hitPoints)
            visualMp.clear()
            visualMp.putAll(plan.nextVisualState.magicPoints)
            val localSettlement = result.localSettlements.getOrNull(passIndex)
                ?: MagicLocalSettlement(emptyList())
            scheduleBattleMutation(effectEndsAt) {
                localSettlement.entries.forEach { entry ->
                    deferredMutation?.commitStatuses(entry)
                }
                presentMagicLocalSettlement(localSettlement, caster)
            }
        }
    }

    /** 물리 공격 패스 시작: 대사 대기와 시각 효과를 반영해 다음 공격 패스를 재생하거나 반격 마법으로 넘긴다. */
    private fun startQueuedPhysicalPassPresentation() {
        val queue = queuedPhysicalPresentation ?: return
        if (settlementPresentation.isActive()) return
        if (scriptRuntime.state != PlaybackState.COMPLETE) return
        if (animationClock() < queue.startsAt) return
        val pass = queue.passes.getOrNull(queue.nextPassIndex)
        if (pass == null) {
            val counterSpeech = queue.counterMagic?.criticalSpeeches?.firstOrNull()
            if (queue.counterMagicAwaitingSpeech) {
                queue.counterMagicAwaitingSpeech = false
                queue.startsAt = animationClock()
            }
            if (counterSpeech != null && !queue.counterMagicSpeechPresented) {
                queue.counterMagicSpeechPresented = true
                queue.counterMagicAwaitingSpeech = true
                battle.presentation.presentationUnit(queue.counterCasterId)?.let(::focusCameraOn)
                val characterId = battle.presentation.presentationUnit(queue.counterCasterId)?.characterId
                scriptRuntime.presentExternalBattleDialogue(Dialogue(characterId?.toString(), counterSpeech))
                return
            }
            queuedPhysicalPresentation = null
            startCounterMagicPresentation(queue)
            return
        }
        if (queue.awaitingSpeechPassIndex == queue.nextPassIndex) {
            queue.awaitingSpeechPassIndex = null
            queue.startsAt = animationClock()
        }
        if (pass.criticalSpeech != null && queue.nextPassIndex !in queue.presentedSpeechPasses) {
            queue.presentedSpeechPasses += queue.nextPassIndex
            queue.awaitingSpeechPassIndex = queue.nextPassIndex
            battle.presentation.presentationUnit(pass.attackerId)?.let(::focusCameraOn)
            val characterId = battle.presentation.presentationUnit(pass.attackerId)?.characterId
            scriptRuntime.presentExternalBattleDialogue(Dialogue(characterId?.toString(), pass.criticalSpeech))
            return
        }
        val attacker = battle.presentation.presentationUnit(pass.attackerId) ?: run {
            queuedPhysicalPresentation = null
            return
        }
        val primary = pass.targets.firstOrNull()
        val primaryTargetId = primary?.targetId ?: pass.primaryTargetId ?: run {
            queue.nextPassIndex++
            return
        }
        val direction = battleDirection(attacker.id, primaryTargetId)
        val sourceAction = BattleAttackSequence.selectAttackAction(pass.critical, attacker.attackDelay)
        val animation = sourceActionAnimation(attacker.id, sourceAction, direction, queue.startsAt)
        actionAnimation = animation
        val hitAt = animation.startedAt + requireNotNull(battleSprites.hitTime(sourceAction, direction)) {
            "원본 BRAnime anime$sourceAction 방향 ${direction}에 hit 이벤트가 없습니다"
        }
        queue.startsAt = if (primary == null) {
            val reactionDirection = battleDirection(primaryTargetId, attacker.id)
            val reactionEndsAt = hitAt + requireSourceActionDuration(26, reactionDirection)
            scheduleHitReaction(primaryTargetId, reactionDirection, hitAt, reactionEndsAt, 26)
            scheduleBattleMutation(hitAt) {
                battle.presentation.presentationUnit(primaryTargetId)?.let(::focusCameraOn)
                audio.playBattleEffect(30)
            }
            actionAnimation = animation.copy(endsAt = reactionEndsAt)
            reactionEndsAt
        } else {
            schedulePhysicalPassTargets(pass, animation, hitAt, queue)
        }
        queue.nextPassIndex++
        if (!BattlePhysicalPresentationPlanner.shouldQueuePhysicalPass(
                queue.nextPassIndex, queue.passes.size, queue.counterMagic != null,
            )
        ) queuedPhysicalPresentation = null
    }

    /** 마법 공격 패스 시작: 대사·시전자 행동·대상 효과를 순서대로 예약하고 다음 패스 시각을 갱신한다. */
    private fun startQueuedMagicPassPresentation() {
        val queue = queuedMagicPresentation ?: return
        if (settlementPresentation.isActive()) return
        if (scriptRuntime.state != PlaybackState.COMPLETE) return
        if (animationClock() < queue.startsAt) return
        val index = queue.nextPassIndex
        val pass = queue.result.passes.getOrNull(index) ?: run {
            queuedMagicPresentation = null
            return
        }
        if (queue.awaitingSpeechPassIndex == index) {
            queue.awaitingSpeechPassIndex = null
            queue.startsAt = animationClock()
        }
        val speech = queue.result.criticalSpeeches.getOrNull(index)
        if (speech != null && index !in queue.presentedSpeechPasses) {
            queue.presentedSpeechPasses += index
            queue.awaitingSpeechPassIndex = index
            battle.presentation.presentationUnit(queue.casterId)?.let(::focusCameraOn)
            val characterId = battle.presentation.presentationUnit(queue.casterId)?.characterId
            scriptRuntime.presentExternalBattleDialogue(Dialogue(characterId?.toString(), speech))
            return
        }
        val caster = battle.presentation.presentationUnit(queue.casterId) ?: run {
            queuedMagicPresentation = null
            return
        }
        focusCameraOn(caster, forceCenter = true)
        val preparation = sourceActionAnimation(
            caster.id,
            if (queue.result.critical) 50 else 5,
            battleDirection(caster.id, queue.targetId),
            queue.startsAt,
        )
        actionAnimation = preparation
        val effect = magicEffects.effect(queue.effectId)
        val effectAnimation = effect?.let {
            MagicEffectAnimation(
                queue.effectId,
                pass.map(MagicTarget::targetId),
                preparation.endsAt,
                preparation.endsAt + it.duration
            )
                .also(magicEffectAnimations::add)
        }
        val passResult = queue.result.copy(
            targets = pass,
            passes = listOf(pass),
            criticalSpeeches = listOf(speech),
            localSettlements = listOf(
                queue.result.localSettlements.getOrNull(index) ?: MagicLocalSettlement(emptyList())
            ),
        )
        scheduleMagicPresentation(
            passResult, caster.id, queue.profile, queue.visualHp, queue.visualMp,
            effectAnimations = listOfNotNull(effectAnimation),
            reaction = queue.reaction,
        )
        advanceMagicVisualState(pass, caster.id, queue.profile, queue.visualHp, queue.visualMp)
        queue.nextPassIndex++
        queue.startsAt = (effectAnimation?.endsAt ?: preparation.endsAt) + 1f
        if (queue.nextPassIndex >= queue.result.passes.size) queuedMagicPresentation = null
    }

    private fun advanceMagicVisualState(
        pass: List<MagicTarget>,
        casterId: String,
        profile: GameDataCatalog.MagicProfile?,
        hp: MutableMap<String, Int>,
        mp: MutableMap<String, Int>,
    ) {
        val next = BattleCombatPresentationQueueCoordinator.advanceMagicVisualState(
            pass,
            casterId,
            profile,
            BattleCombatPresentationQueueCoordinator.VisualState(hp, mp),
            combatPresentationUnitVisualStates(),
        )
        hp.clear()
        hp.putAll(next.hitPoints)
        mp.clear()
        mp.putAll(next.magicPoints)
    }

    /** 현재 전투 유닛의 HP·MP를 immutable combat queue planner 입력으로 고정한다. */
    private fun combatPresentationUnitVisualStates() = battle.presentation.presentationUnits().associate { unit ->
        unit.id to BattleCombatPresentationQueueCoordinator.UnitVisualState(
            unit.hitPoints,
            unit.maxHitPoints,
            unit.magicPoints,
            unit.maxMagicPoints,
        )
    }

    /** 마법 time-line planner가 누락된 MP snapshot에 사용할 현재 유닛 상태·방향을 고정한다. */
    private fun magicPresentationPlannerUnitStates(fallbackMagicPoints: Map<String, Int>) =
        battle.presentation.presentationUnits().associate { unit ->
            unit.id to BattleMagicPresentationPlanner.UnitState(
                unit.hitPoints,
                unit.maxHitPoints,
                fallbackMagicPoints[unit.id] ?: unit.magicPoints,
                unit.maxMagicPoints,
                unit.direction,
            )
        }

    /** 반격 마법 시작: 반격자의 기력 소모·행동·마법 효과를 예약하고 대상 패스 대기열을 만든다. */
    private fun startCounterMagicPresentation(queue: PhysicalPassPresentationQueue) {
        val magic = queue.counterMagic ?: return
        val magicId = queue.counterMagicId ?: return
        val caster = battle.presentation.presentationUnit(queue.counterCasterId) ?: return
        focusCameraOn(caster, forceCenter = true)
        val profile = gameDataCatalog.magicProfile(magicId) ?: return
        val direction = battleDirection(caster.id, queue.counterTargetId)
        val preparation = sourceActionAnimation(caster.id, if (magic.critical) 50 else 5, direction, queue.startsAt)
        actionAnimation = preparation
        val deferred = battle.pendingActionTransaction
        val casterMpBefore = queue.visualMp[caster.id] ?: deferred?.initialMp(caster.id) ?: caster.magicPoints
        val casterMpAfter = (casterMpBefore - magic.cost).coerceAtLeast(0)
        scheduleBattleMutation(queue.startsAt) { deferred?.commitVitals(caster.id, mp = casterMpAfter) }
        queue.visualMp[caster.id] = casterMpAfter

        magicEffectAnimations.clear()
        val firstPass = magic.passes.firstOrNull().orEmpty()
        val effectAnimation = magicEffects.effect(profile.effectId)?.let { effect ->
            MagicEffectAnimation(
                profile.effectId, firstPass.map(MagicTarget::targetId),
                preparation.endsAt, preparation.endsAt + effect.duration,
            ).also(magicEffectAnimations::add)
        }
        val firstResult = magic.copy(
            targets = firstPass,
            passes = listOf(firstPass),
            criticalSpeeches = listOf(magic.criticalSpeeches.firstOrNull()),
            localSettlements = listOf(magic.localSettlements.firstOrNull() ?: MagicLocalSettlement(emptyList())),
        )
        scheduleMagicPresentation(
            result = firstResult,
            casterId = caster.id,
            magic = profile,
            healthBeforeAction = queue.visualHp,
            initialMp = queue.visualMp,
            effectAnimations = listOfNotNull(effectAnimation),
            reaction = true,
        )
        advanceMagicVisualState(firstPass, caster.id, profile, queue.visualHp, queue.visualMp)
        val completedAt = (effectAnimation?.endsAt ?: preparation.endsAt) + 1f
        if (magic.passes.size > 1) {
            queuedMagicPresentation = MagicPassPresentationQueue(
                result = magic,
                casterId = caster.id,
                targetId = queue.counterTargetId,
                profile = profile,
                effectId = profile.effectId,
                nextPassIndex = 1,
                startsAt = completedAt,
                visualHp = queue.visualHp,
                visualMp = queue.visualMp,
                reaction = true,
            )
        }
        activeCounterMagicPresentation = ActiveCounterMagicPresentation(
            unitIds = buildSet {
                add(caster.id)
                magic.passes.flatten().mapTo(this, MagicTarget::targetId)
            },
            endsAt = completedAt,
        )
    }
    private fun schedulePhysicalPassTargets(
        pass: PhysicalAttackPass,
        animation: UnitActionAnimation,
        hitAt: Float,
        queue: PhysicalPassPresentationQueue,
    ): Float {
        var cursor = hitAt
        pass.targets.forEach { result ->
            val target = battle.presentation.presentationUnit(result.targetId) ?: return@forEach
            val reactionDirection = battleDirection(target.id, pass.attackerId)
            val hpBefore = queue.visualHp[result.targetId] ?: target.hitPoints
            val mpBefore = queue.visualMp[result.targetId] ?: target.magicPoints
            val attacker = battle.presentation.presentationUnit(pass.attackerId)
            val attackerHpBefore = queue.visualHp[pass.attackerId] ?: attacker?.hitPoints ?: 0
            val attackerHealing = result.lifeStealHealing + result.qxlHealing
            val targetPlan = BattlePhysicalPresentationPlanner.target(
                BattlePhysicalPassTargetPlanInput(
                    resolvedHarm = result.resolvedHarm,
                    damage = result.damage,
                    mpShieldDamage = result.mpShieldDamage,
                    attackerHealing = attackerHealing,
                    retaliationDamage = result.blockRetaliations.sumOf { it.damage },
                    recoilDamage = result.recoilDamage,
                    automaticPropertyPresent = result.automaticProperty != null,
                    automaticPropertyHpDelta = result.automaticPropertyHpDelta,
                    automaticPropertyMpDelta = result.automaticPropertyMpDelta,
                    targetHpBefore = hpBefore,
                    targetMpBefore = mpBefore,
                    targetMaxHp = target.maxHitPoints,
                    targetMaxMp = target.maxMagicPoints,
                    attackerHpBefore = attackerHpBefore,
                    attackerMaxHp = attacker?.maxHitPoints ?: attackerHpBefore,
                    reactionStartedAt = cursor,
                    reactionDuration = requireSourceActionDuration(
                        if (result.resolvedHarm == 0 && result.mpShieldDamage == 0) 26 else 32,
                        reactionDirection,
                    ),
                ),
            )
            val guard = targetPlan.guard
            val reactionAction = targetPlan.reactionAction
            val reactionEndsAt = targetPlan.reactionEndsAt
            val hpAfterHarm = targetPlan.targetHpAfterHarm
            val mpAfterHarm = targetPlan.targetMpAfterHarm
            val attackerHpAfterHealing = targetPlan.attackerHpAfterHealing
            val (playerMoneyDelta, enemyMoneyDelta) =
                result.hitCallbackEconomyDelta(target.isPlayerSide())
            scheduleHitReaction(target.id, reactionDirection, cursor, reactionEndsAt, reactionAction)
            result.backMove?.let { move ->
                val moveEndsAt = cursor + move.durationSeconds
                backMoveAnimations[result.targetId] = BackMoveAnimation(result.targetId, move, cursor, moveEndsAt)
                scheduleBattleMutation(moveEndsAt) {
                    battle.pendingActionTransaction?.commitPosition(result.targetId, move.toX, move.toY)
                    backMoveAnimations.remove(result.targetId)
                }
            }
            scheduleBattleMutation(cursor) {
                battle.presentation.presentationUnit(result.targetId)?.let(::focusCameraOn)
                audio.playBattleEffect(
                    if (guard) {
                        if (pass.critical) 31 else 30
                    } else if (pass.critical) 36 else 35
                )
                battle.pendingActionTransaction?.commitVitals(
                    result.targetId,
                    hp = hpAfterHarm.takeIf { result.damage > 0 },
                    mp = mpAfterHarm.takeIf { result.mpShieldDamage > 0 },
                )
                battle.pendingActionTransaction?.commitVitals(
                    pass.attackerId,
                    hp = attackerHpAfterHealing.takeIf { attackerHealing > 0 },
                )
                battle.pendingActionTransaction?.commitEconomy(playerMoneyDelta, enemyMoneyDelta)
                battle.pendingActionTransaction?.commitNextHitSideEffect()
            }
            if (result.damage > 0) healthTimeline.schedule(result.targetId, hpBefore, hpAfterHarm, cursor)
            if (targetPlan.showsHarmNumber) {
                harmNumberAnimations[result.targetId] = HarmNumberAnimation(
                    amount = targetPlan.harmNumberAmount(result.resolvedHarm, result.mpShieldDamage),
                    isHp = targetPlan.harmNumberIsHp(result.mpShieldDamage),
                    startedAt = cursor,
                    endsAt = reactionEndsAt,
                )
            }
            queue.visualHp[result.targetId] = hpAfterHarm
            queue.visualMp[result.targetId] = mpAfterHarm
            if (attackerHealing > 0) queue.visualHp[pass.attackerId] = attackerHpAfterHealing

            val retaliationDamage = result.blockRetaliations.sumOf { it.damage }
            val postReactionDamage = retaliationDamage + result.recoilDamage
            if (postReactionDamage > 0) {
                val attackerBefore = attackerHpAfterHealing
                val attackerAfter = targetPlan.attackerHpAfterRetaliation
                scheduleBattleMutation(reactionEndsAt) {
                    healthTimeline.schedule(pass.attackerId, attackerBefore, attackerAfter, reactionEndsAt)
                    battle.pendingActionTransaction?.commitVitals(pass.attackerId, hp = attackerAfter)
                }
                queue.visualHp[pass.attackerId] = attackerAfter
            }

            val automaticEndsAt = targetPlan.automaticEndsAt
            if (result.automaticPropertyHpDelta != 0 || result.automaticPropertyMpDelta != 0) {
                healthTimelineHoldUntil[result.targetId] = automaticEndsAt
                val hpAfterProperty = targetPlan.targetHpAfterAutomaticProperty
                val mpAfterProperty = targetPlan.targetMpAfterAutomaticProperty
                scheduleBattleMutation(cursor) {
                    if (result.automaticPropertyHpDelta != 0) {
                        healthTimeline.schedule(result.targetId, hpAfterHarm, hpAfterProperty, automaticEndsAt)
                    }
                }
                scheduleBattleMutation(automaticEndsAt) {
                    battle.pendingActionTransaction?.commitVitals(
                        result.targetId,
                        hp = hpAfterProperty.takeIf { result.automaticPropertyHpDelta != 0 },
                        mp = mpAfterProperty.takeIf { result.automaticPropertyMpDelta != 0 },
                    )
                }
                queue.visualHp[result.targetId] = hpAfterProperty
                queue.visualMp[result.targetId] = mpAfterProperty
            }
            if (result.automaticPropertyCallbackCount > 0) {
                scheduleBattleMutation(automaticEndsAt) {
                    repeat(result.automaticPropertyCallbackCount) {
                        battle.pendingActionTransaction?.commitNextHitSideEffect()
                    }
                }
            }
            val localSettlement = result.localStatusSettlement
            val localOperationPlan = if (localSettlement.entries.isNotEmpty()) {
                settlementOperationCoordinator.magicLocalSettlement(
                    localSettlement, pass.attackerId, settlementOperationPort,
                )
            } else null
            val localPlan = localOperationPlan?.settlementPlan
            val localOperations = localOperationPlan?.operations.orEmpty()
            if (result.hasLocalStatusSettlement) {
                scheduleBattleMutation(automaticEndsAt) {
                    localSettlement.entries.forEach { entry ->
                        battle.pendingActionTransaction?.commitStatuses(entry)
                    }
                    if (localPlan != null) startLocalSettlement(localPlan, localOperations)
                }
            }
            cursor = automaticEndsAt + settlementOperationCoordinator.localDuration(localOperations, settlementOperationPort)
        }
        actionAnimation = animation.copy(endsAt = cursor)
        return cursor
    }

    private fun startQueuedFollowUpPresentation() {
        val queued = queuedFollowUpPresentation ?: return
        if (animationClock() < queued.startsAt) return
        queuedFollowUpPresentation = null
        val attacker = battle.presentation.presentationUnit(queued.attackerId) ?: return
        val target = battle.presentation.presentationUnit(queued.targetId) ?: return
        val direction = battleDirection(attacker.id, target.id)
        val sourceAction = BattleFollowUpPresentationPlanner.sourceAction(queued.critical, attacker.attackDelay)
        val animation = sourceActionAnimation(attacker.id, sourceAction, direction, queued.startsAt)
        actionAnimation = animation
        val reactionDirection = battleDirection(target.id, attacker.id)
        val timeline = BattleFollowUpPresentationPlanner.plan(
            BattleFollowUpPresentationPlanInput(
                critical = queued.critical,
                attackDelay = attacker.attackDelay,
                animationStartedAt = animation.startedAt,
                hitOffset = requireNotNull(battleSprites.hitTime(sourceAction, direction)) {
                    "원본 BRAnime anime$sourceAction 방향 ${direction}에 hit 이벤트가 없습니다"
                },
                reactionDuration = requireSourceActionDuration(32, reactionDirection),
                targetHpBefore = queued.targetHpBefore,
                harm = queued.harm,
                targetMpBefore = battle.pendingActionTransaction?.initialMp(target.id),
                mpShieldDamage = queued.mpShieldDamage,
            ),
        )
        actionAnimation = animation.copy(endsAt = timeline.reactionEndsAt)
        scheduleHitReaction(target.id, reactionDirection, timeline.hitAt, timeline.reactionEndsAt, 32)
        scheduleBattleMutation(timeline.hitAt) {
            focusCameraOn(target)
            audio.playBattleEffect(if (queued.critical) 36 else 35)
            battle.pendingActionTransaction?.commitVitals(
                target.id,
                hp = timeline.targetHpAfter.takeIf { timeline.commitsHp(queued.harm) },
                mp = timeline.targetMpAfter.takeIf { timeline.commitsMp(queued.mpShieldDamage) },
            )
            battle.pendingActionTransaction?.commitNextHitSideEffect()
        }
        if (timeline.commitsHp(queued.harm)) healthTimeline.schedule(
            target.id, queued.targetHpBefore, timeline.targetHpAfter, timeline.hitAt,
        )
        harmNumberAnimations[target.id] =
            HarmNumberAnimation(queued.harm, queued.mpShieldDamage == 0, timeline.hitAt, timeline.reactionEndsAt)
        if (BattleFollowUpPresentationPlanner.shouldQueueNext(
                queued.counterDamage, queued.counterMpShieldDamage, timeline.targetHpAfter,
            )
        ) {
            queuedCounterPresentation = CounterPresentation(
                attackerId = target.id,
                targetId = attacker.id,
                harm = queued.counterDamage,
                targetHpBefore = queued.counterTargetHpBefore,
                critical = queued.counterCritical,
                mpShieldDamage = queued.counterMpShieldDamage,
                followUpDamage = 0,
                followUpMpShieldDamage = 0,
                followUpCritical = false,
                startsAt = timeline.reactionEndsAt,
            )
        }
    }

    private fun startQueuedCounterPresentation() {
        val queued = queuedCounterPresentation ?: return
        if (animationClock() < queued.startsAt) return
        queuedCounterPresentation = null
        val attacker = battle.presentation.presentationUnit(queued.attackerId) ?: return
        val target = battle.presentation.presentationUnit(queued.targetId) ?: return
        val direction = battleDirection(attacker.id, target.id)
        val sourceAction = BattleFollowUpPresentationPlanner.sourceAction(queued.critical, attacker.attackDelay)
        val animation = sourceActionAnimation(attacker.id, sourceAction, direction, queued.startsAt)
        actionAnimation = animation
        val reactionDirection = battleDirection(target.id, attacker.id)
        val timeline = BattleFollowUpPresentationPlanner.plan(
            BattleFollowUpPresentationPlanInput(
                critical = queued.critical,
                attackDelay = attacker.attackDelay,
                animationStartedAt = animation.startedAt,
                hitOffset = requireNotNull(battleSprites.hitTime(sourceAction, direction)) {
                    "원본 BRAnime anime$sourceAction 방향 ${direction}에 hit 이벤트가 없습니다"
                },
                reactionDuration = requireSourceActionDuration(32, reactionDirection),
                targetHpBefore = queued.targetHpBefore,
                harm = queued.harm,
                targetMpBefore = battle.pendingActionTransaction?.initialMp(target.id),
                mpShieldDamage = queued.mpShieldDamage,
            ),
        )
        actionAnimation = animation.copy(endsAt = timeline.reactionEndsAt)
        scheduleHitReaction(target.id, reactionDirection, timeline.hitAt, timeline.reactionEndsAt, 32)
        scheduleBattleMutation(timeline.hitAt) {
            focusCameraOn(target)
            audio.playBattleEffect(if (queued.critical) 36 else 35)
            battle.pendingActionTransaction?.commitVitals(
                target.id,
                hp = timeline.targetHpAfter.takeIf { timeline.commitsHp(queued.harm) },
                mp = timeline.targetMpAfter.takeIf { timeline.commitsMp(queued.mpShieldDamage) },
            )
            battle.pendingActionTransaction?.commitNextHitSideEffect()
        }
        if (timeline.commitsHp(queued.harm)) {
            healthTimeline.schedule(target.id, queued.targetHpBefore, timeline.targetHpAfter, timeline.hitAt)
        }
        harmNumberAnimations[target.id] =
            HarmNumberAnimation(queued.harm, queued.mpShieldDamage == 0, timeline.hitAt, timeline.reactionEndsAt)
        if (BattleFollowUpPresentationPlanner.shouldQueueNext(
                queued.followUpDamage, queued.followUpMpShieldDamage, timeline.targetHpAfter,
            )
        ) {
            queuedCounterFollowUpPresentation = CounterFollowUpPresentation(
                attackerId = attacker.id,
                targetId = target.id,
                harm = queued.followUpDamage,
                targetHpBefore = timeline.targetHpAfter,
                critical = queued.followUpCritical,
                mpShieldDamage = queued.followUpMpShieldDamage,
                startsAt = timeline.reactionEndsAt,
            )
        }
    }

    /** 반격 추가타 시작: 예약 시각이 되면 추가타 행동·피격·체력 반영을 시간선에 등록한다. */
    private fun startQueuedCounterFollowUpPresentation() {
        val queued = queuedCounterFollowUpPresentation ?: return
        if (animationClock() < queued.startsAt) return
        queuedCounterFollowUpPresentation = null
        val attacker = battle.presentation.presentationUnit(queued.attackerId) ?: return
        val target = battle.presentation.presentationUnit(queued.targetId) ?: return
        val direction = battleDirection(attacker.id, target.id)
        val sourceAction = BattleFollowUpPresentationPlanner.sourceAction(queued.critical, attacker.attackDelay)
        val animation = sourceActionAnimation(attacker.id, sourceAction, direction, queued.startsAt)
        actionAnimation = animation
        val reactionDirection = battleDirection(target.id, attacker.id)
        val timeline = BattleFollowUpPresentationPlanner.plan(
            BattleFollowUpPresentationPlanInput(
                critical = queued.critical,
                attackDelay = attacker.attackDelay,
                animationStartedAt = animation.startedAt,
                hitOffset = requireNotNull(battleSprites.hitTime(sourceAction, direction)) {
                    "원본 BRAnime anime$sourceAction 방향 ${direction}에 hit 이벤트가 없습니다"
                },
                reactionDuration = requireSourceActionDuration(32, reactionDirection),
                targetHpBefore = queued.targetHpBefore,
                harm = queued.harm,
                targetMpBefore = battle.pendingActionTransaction?.initialMp(target.id),
                mpShieldDamage = queued.mpShieldDamage,
            ),
        )
        actionAnimation = animation.copy(endsAt = timeline.reactionEndsAt)
        scheduleHitReaction(target.id, reactionDirection, timeline.hitAt, timeline.reactionEndsAt, 32)
        scheduleBattleMutation(timeline.hitAt) {
            focusCameraOn(target)
            audio.playBattleEffect(if (queued.critical) 36 else 35)
            battle.pendingActionTransaction?.commitVitals(
                target.id,
                hp = timeline.targetHpAfter.takeIf { timeline.commitsHp(queued.harm) },
                mp = timeline.targetMpAfter.takeIf { timeline.commitsMp(queued.mpShieldDamage) },
            )
            battle.pendingActionTransaction?.commitNextHitSideEffect()
        }
        if (timeline.commitsHp(queued.harm)) healthTimeline.schedule(
            target.id, queued.targetHpBefore, timeline.targetHpAfter, timeline.hitAt,
        )
        harmNumberAnimations[target.id] =
            HarmNumberAnimation(queued.harm, queued.mpShieldDamage == 0, timeline.hitAt, timeline.reactionEndsAt)
    }

    private fun scheduleBattleMutation(at: Float, mutation: () -> Unit) {
        timedBattleMutations += TimedBattleMutation(at, mutation)
        timedBattleMutations.sortBy(TimedBattleMutation::at)
    }

    private fun applyDueBattleMutations() {
        if (settlementPresentation.isActive()) return
        val now = animationClock()
        while (timedBattleMutations.firstOrNull()?.at?.let { now >= it } == true) {
            timedBattleMutations.removeAt(0).mutation()
            if (settlementPresentation.isActive()) break
        }
    }

    internal fun commitDeferredBattleAction(settlementActorId: String? = null) {
        settlementActorId?.let(battle.presentation::presentationUnit)?.let(::focusCameraOn)
        battle.pendingActionTransaction?.commitAll()
        battle.presentation.pendingPresentationUnits()
            .filter {
                it.hitPoints > 0 && it.id !in hitReactionAnimations &&
                        it.id !in deathAnimations && !deathTimeline.containsPending(it.id)
            }
            .map { it.id }
            .forEach(battle.presentation::clearPresentationUnit)
    }
    internal fun collectDyingPresentationUnits(): List<BattleDeathPresentationTimeline.DeathUnit> {
        syncScriptedUnits()
        val dying = UnitDeathPresentation.sortedDying(battle.units.values + battle.presentation.pendingPresentationUnits())
        var hideType = 1
        return dying.map { unit ->
            val showRetireMessage = hideType == 1
            if (showRetireMessage && isScriptMineMaster(unit.id)) hideType = 2
            val sourceAction = UnitDeathPresentation.hideAction(
                hideType = hideType,
                selfMaster = isScriptMineMaster(unit.id),
            )
            BattleDeathPresentationTimeline.DeathUnit(
                unitId = unit.id,
                direction = unit.direction,
                sourceAction = sourceAction,
                duration = requireSourceActionDuration(sourceAction, unit.direction),
                originalHp = unit.hitPoints,
                showRetireMessage = showRetireMessage && unit.deathMessageEnabled,
                dialogueCharacterId = unit.characterId?.toString(),
                retireMessage = unit.retireMessage.takeIf { unit.deathMessageEnabled },
            )
        }
    }
    private fun presentTurnSettlement(settlement: CampSettlement): Boolean {
        check(!settlementPresentation.isActive()) { "overlapping BattleScreen._jiesuan presentations" }
        val operationPlan = settlementOperationCoordinator.turnSettlement(settlement, settlementOperationPort)
        val plan = operationPlan.settlementPlan
        val operations = operationPlan.operations
        if (operations.isEmpty()) {
            refreshSettlementUnits(plan)
            return true
        }
        return settlementPresentation.start(plan, operations, local = false).also { immediate ->
            if (immediate) refreshSettlementUnits(plan)
        }
    }
    private fun presentMagicLocalSettlement(settlement: MagicLocalSettlement, casterId: String) {
        if (settlement.entries.isEmpty()) return
        check(!settlementPresentation.isActive()) { "overlapping BattleScreen._magicProcess settlement" }
        val operationPlan = settlementOperationCoordinator.magicLocalSettlement(settlement, casterId, settlementOperationPort)
        val plan = operationPlan.settlementPlan
        val operations = operationPlan.operations
        if (operations.isEmpty()) {
            refreshSettlementUnits(plan)
            return
        }
        startLocalSettlement(plan, operations)
    }

    private fun startLocalSettlement(plan: BattleSettlementPlan, operations: List<TurnSettlementOp>) {
        check(!settlementPresentation.isActive()) { "overlapping callback-local BattleScreen._jiesuan presentation" }
        if (operations.isEmpty()) {
            refreshSettlementUnits(plan)
            return
        }
        if (settlementPresentation.start(plan, operations, local = true)) refreshSettlementUnits(plan)
    }
    private fun driveSettlementPresentationController() {
        val now = animationClock()
        actionAnimation?.takeIf { it.endsAt <= now }?.let {
            actionAnimation = null
            settlementPresentation.actionCompleted()
        }
        settlementMeffEndsAt?.takeIf { now >= it }?.let {
            settlementMeffEndsAt = null
            settlementPresentation.meffCompleted()
        }
        if (settlementItemUpgradeStarted && !outcomePresentation.itemUpgradeActive) {
            settlementItemUpgradeStarted = false
            settlementPresentation.itemUpgradeCompleted()
        }
        val autoClose: (String) -> Boolean = { text ->
            text.length < 10 || settingsPreferences.getInteger(
                SettingLayer.GAME_SETTING,
                SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
            ) and SettingLayer.AUTO_CLOSE != 0
        }
        settlementPresentation.tick(now, autoClose).forEach { effect ->
            when (effect) {
                is BattleSettlementPresentationController.Effect.Focus ->
                    battle.presentation.presentationUnit(effect.unitId)?.let { focusCameraOn(it, effect.forceCenter) }
                is BattleSettlementPresentationController.Effect.Sound -> audio.playBattleEffect(effect.index)
                is BattleSettlementPresentationController.Effect.Info2 -> Unit
                is BattleSettlementPresentationController.Effect.Actions -> {
                    battle.presentation.presentationUnit(effect.unitId)?.let { unit ->
                        actionAnimation = sourceActionAnimation(unit.id, effect.actionId, unit.direction, now)
                    } ?: settlementPresentation.actionCompleted()
                }
                is BattleSettlementPresentationController.Effect.UnitInfo -> {
                    battle.presentation.presentationUnit(effect.plan.unitId)?.let { unit ->
                        settlementPresentation.setInfoTitle(unit.name)
                        var cursor = now + effect.plan.preInfoDelaySeconds
                        effect.plan.infoDeltas.forEach { delta ->
                            cursor += delta.tickSeconds
                            if (delta.kind == SettlementInfoKind.HP) healthTimeline.schedule(unit.id, delta.before, delta.after, cursor)
                        }
                        healthTimelineHoldUntil[unit.id] = now + effect.plan.infoBarrierSeconds
                    }
                }
                is BattleSettlementPresentationController.Effect.GrowthInfo ->
                    battle.presentation.presentationUnit(effect.unitId)?.let { settlementPresentation.setInfoTitle(it.name) }
                is BattleSettlementPresentationController.Effect.Meff -> {
                    val animation = magicEffects.effect(effect.effectId)
                    val targets = effect.targetIds.filter { battle.presentation.presentationUnit(it) != null }
                    if (animation == null || targets.isEmpty()) settlementPresentation.meffCompleted() else {
                        magicEffectAnimations += MagicEffectAnimation(effect.effectId, targets, now, now + animation.duration)
                        settlementMeffEndsAt = now + animation.duration
                    }
                }
                is BattleSettlementPresentationController.Effect.ItemUpgrade -> {
                    battle.experience.consumeEquipmentUpgrade()?.let { queued ->
                        check(queued == effect.result) { "settlement item-upgrade queue order mismatch" }
                    }
                    settlementItemUpgradeStarted = true
                    outcomePresentation.openSettlementItemUpgrade(effect.result)
                }
                is BattleSettlementPresentationController.Effect.HideState -> effect.unitIds.forEach { id ->
                    battle.presentation.presentationUnit(id)?.let { unitPresentationStore.stateFor(it).setStateAnimationVisible(false) }
                }
                is BattleSettlementPresentationController.Effect.Refresh -> effect.unitIds.forEach { id ->
                    battle.presentation.presentationUnit(id)?.let { unit -> unitPresentationStore.refresh(unit); unitSpriteFrameResolver.defaultAction(unit) }
                }
                is BattleSettlementPresentationController.Effect.Default ->
                    battle.presentation.presentationUnit(effect.unitId)?.let(unitSpriteFrameResolver::defaultAction)
                is BattleSettlementPresentationController.Effect.Finished -> {
                    refreshSettlementUnits(effect.plan)
                    if (!effect.local) when (effect.plan.stage) {
                        CampSettlementStage.START_STATE -> turnController.completeCampStatePresentation()
                        CampSettlementStage.END_RESTORE -> turnController.completeCampRestorePresentation()
                    }
                }
            }
        }
    }

    private fun closeSettlementInfo2() {
        if (settlementPresentation.dismissInfo2(animationClock())) driveSettlementPresentationController()
    }

    private fun refreshSettlementUnits(plan: BattleSettlementPlan) {
        plan.units.forEach { unitPlan ->
            battle.presentation.presentationUnit(unitPlan.unitId)?.let { unit ->
                unitPresentationStore.refresh(unit)
                unitSpriteFrameResolver.defaultAction(unit)
            }
        }
    }

    private fun finishManualUnitDeathCallbacks() {
        pendingBattleScriptPassesAfterAction = 0
        pendingBattleCompletedScriptPasses = 0
        pendingBattleActionCommitted = false
        pendingBattleSettlementActorId = null
        deathTimeline.finishPostActionCallbacks()
    }

    private fun driveScriptPresentation() = scriptedPresentation.drive()

    private fun liveScriptBattleUnit(characterId: Int, visibleOnly: Boolean = false): BattleUnit? {
        val units = battle.units.values + battle.presentation.pendingPresentationUnits()
        val exactId = scriptRuntime.stage.battleUnitForCharacterId(characterId)?.battleId
        return units.firstOrNull { it.id == exactId && (!visibleOnly || it.visible) }
            ?: units.firstOrNull { it.characterId == characterId && (!visibleOnly || it.visible) }
    }

    private fun scriptBattleUnit(characterId: Int): BattleUnit? =
        liveScriptBattleUnit(characterId, visibleOnly = true)
    private fun isScriptMineMaster(unitId: String): Boolean =
        scriptRuntime.stage.battleUnitForCharacterId(scriptRuntime.stage.mineMasterInstanceId)?.battleId == unitId

    private fun pruneCombatPresentation() {
        val now = animationClock()
        val completedHitIds = hitReactionAnimations.entries
            .filter { now >= it.value.endsAt }
            .map { it.key }
        hitReactionAnimations.entries
            .filter {
                now >= it.value.endsAt && !isPresentationNeededByQueuedExchange(it.key) &&
                        battle.presentation.presentationUnit(it.key)?.hitPoints?.let { hp -> hp > 0 } != false
            }
            .map { it.key }
            .forEach(battle.presentation::clearPresentationUnit)
        hitReactionAnimations.entries.removeIf { now >= it.value.endsAt }
        completedHitIds
            .filter { healthTimelineHoldUntil[it]?.let { until -> now >= until } != false }
            .forEach(healthTimeline::clear)
        healthTimelineHoldUntil.entries
            .filter { now >= it.value }
            .map { it.key }
            .forEach { id ->
                healthTimeline.clear(id)
                healthTimelineHoldUntil.remove(id)
            }
        deathAnimations.entries
            .filter { now >= it.value.endsAt }
            .map { it.key }
            .forEach(battle.presentation::clearPresentationUnit)
        deathAnimations.entries.removeIf { now >= it.value.endsAt }
        harmNumberAnimations.entries.removeIf { now >= it.value.endsAt }
        if (activeCounterMagicPresentation?.let { now >= it.endsAt } == true) {
            activeCounterMagicPresentation = null
        }
    }
    private fun isPresentationNeededByQueuedExchange(id: String): Boolean =
        queuedPhysicalPresentation?.passes?.drop(queuedPhysicalPresentation?.nextPassIndex ?: 0)
            ?.any { pass -> pass.attackerId == id || pass.targets.any { it.targetId == id } } == true ||
                queuedPhysicalPresentation?.let { queue ->
                    queue.counterMagic != null && (queue.counterCasterId == id || queue.counterTargetId == id)
                } == true ||
                activeCounterMagicPresentation?.unitIds?.contains(id) == true ||
                queuedFollowUpPresentation?.targetId == id ||
                queuedCounterPresentation?.targetId == id ||
                queuedCounterFollowUpPresentation?.targetId == id ||
                deathAnimations[id]?.let { animationClock() < it.endsAt } == true

    private fun completeTurnScriptIfReady() {
        if (scriptRuntime.state != PlaybackState.COMPLETE) return
        when (turnController.snapshot.phase) {
            BattleTurnPhase.CAMP_SCRIPT -> turnController.completeCampScript()
            BattleTurnPhase.ROUND_SCRIPT -> turnController.completeRoundScript()
            else -> Unit
        }
    }

    /** 전투 초기화 진행: 초기 장면 스크립트가 끝난 뒤 첫 진영 장면과 전투 턴 시작을 순서대로 확정한다. */
    private fun driveBattleBootstrap() {
        if (scriptRuntime.state != PlaybackState.COMPLETE || bootstrapPresentationBusyReasons().isNotEmpty()) return
        when (bootstrapPhase) {
            BattleBootstrapPhase.SCENE0 -> {
                bootstrapPhase = BattleBootstrapPhase.INITIAL_SCENE1
                initialPlayerCampScriptStarted = true
                focusFirstCampCameraUnit(Faction.PLAYER)
                runBattleScript(contextCampOverride = -1)
            }

            BattleBootstrapPhase.INITIAL_SCENE1 -> {
                completeInitialBattleOperation(scriptRuntime.stage)
                bootstrapPhase = BattleBootstrapPhase.COMPLETE
                turnController.completeBootstrap()
            }

            BattleBootstrapPhase.COMPLETE -> Unit
        }
    }
    private fun bootstrapPresentationBusyReasons(): List<String> {
        val now = animationClock()
        return BattleBootstrapCallbackState(
            move = scriptRuntime.stage.units.values.any { it.moveDuration > 0f },
            attackAction = now < scriptedAttackCallbackEndsAt,
            hide = scriptedUnitCallbacks.hideBusy,
            show = scriptedUnitCallbacks.showBusy,
            fight = activeFightCommand != null || pendingFightCommands.isNotEmpty(),
        ).blockingReasons()
    }

    private fun showTurnResult(result: TurnResult, prefix: String) {
        eventMessage = if (result.firedEvents.isEmpty()) {
            "${prefix}라운드 ${result.round} · ${result.activeFaction.label()} 차례"
        } else {
            "이벤트 실행: ${result.firedEvents.joinToString()} · 증원군이 합류했습니다"
        }
    }

    internal fun combatPresentationBusy(): Boolean {
        val now = animationClock()
        return (scriptRuntime.state == PlaybackState.MODAL &&
                scriptRuntime.currentModalKind == ScenarioModalKind.INFO) ||
                movementAnimation?.let { now < it.endsAt } == true ||
                actionAnimation?.let { now < it.endsAt } == true ||
                hitReactionAnimations.values.any { now < it.endsAt } ||
                deathAnimations.values.any { now < it.endsAt } ||
                deathTimeline.isBusy() ||
                scriptedUnitCallbacks.hideBusy || scriptedUnitCallbacks.showBusy ||
                scriptedUnitTimed.busy || scriptPresentationTimeline.isActive() || scriptedUnitActions.busy ||
                magicEffectAnimations.any { now < it.endsAt } ||
                queuedMagicPresentation != null ||
                activeCounterMagicPresentation?.let { now < it.endsAt } == true ||
                queuedPhysicalPresentation != null ||
                queuedFollowUpPresentation != null ||
                queuedCounterPresentation != null ||
                queuedCounterFollowUpPresentation != null ||
                pendingCriticalSpeechAction != null ||
                settlementPresentation.isActive()
    }
    private fun outcomeCallbacksPending(): Boolean =
        pendingBattleScriptPassesAfterAction > 0 ||
                aiPresentation.unitDeathScriptPass > 0 ||
                deathTimeline.startedPostActionDeaths() ||
                aiPresentation.resolution != null ||
                aiPresentation.hasActiveCamp ||
                activeRoundLayer != null ||
                settlementPresentation.isActive()

    private fun handleTileClick(x: Int, y: Int) {
        if (movementAnimation?.let { animationClock() < it.endsAt } == true) return
        battle.outcome()?.let {
            eventMessage = outcomeText(it)
            return
        }
        val clicked = battle.unitAt(x, y)
        val selected = selectedUnitId?.let { battle.units[it] }
        if (selected == null) {
            if (clicked?.visible == true && clicked.type() == battle.activeFaction && !clicked.hasActed) {
                selectedUnitId = clicked.id
                battleCommandFlow.beginMove(
                    clicked.id,
                    BattleCommandFlow.UnitPose(clicked.tileX, clicked.tileY, clicked.direction)
                )
                selectedMagicIndex = 0
                propertyMode = false
                eventMessage = "${clicked.name} 선택 · 빈 칸으로 이동, 인접 적을 공격"
            }
            return
        }
        if (battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION &&
            battleCommandFlow.childCommand == BattleCommandFlow.Command.ATTACK
        ) {
            if (clicked == null || !clicked.visible || unitsAreAllied(selected, clicked)) {
                eventMessage = "공격 대상을 선택하세요."
                return
            }
            val healthBeforeAction = battle.units.mapValues { it.value.hitPoints }
            val result = battle.presentation.attack(selected.id, clicked.id)
            if (result is TacticalActionResult.Rejected) {
                eventMessage = result.reason
                return
            }
            applyAction(
                result,
                selected.name,
                selected.id,
                targetId = clicked.id,
                healthBeforeAction = healthBeforeAction
            )
            battleCommandFlow.childCompleted(true)
            return
        }
        when {
            propertyMode && clicked != null && unitsAreAllied(selected, clicked) &&
                    kotlin.math.abs(clicked.tileX - selected.tileX) + kotlin.math.abs(clicked.tileY - selected.tileY) <= 1 -> {
                val healthBeforeAction = battle.units.mapValues { it.value.hitPoints }
                propertyMode = false
                val result = usableProperties().getOrNull(selectedPropertyIndex)
                    ?.let { battle.presentation.useProperty(selected.id, clicked.id, it.id) }
                    ?: TacticalActionResult.Rejected("사용 가능한 소비 아이템이 없습니다.")
                applyAction(
                    result,
                    selected.name,
                    selected.id,
                    targetId = clicked.id,
                    healthBeforeAction = healthBeforeAction
                )
            }

            propertyMode -> eventMessage = "아이템은 자신 또는 인접 아군에게 사용해야 합니다."
            clicked?.id == selected.id -> openBattleCommand(selected)
            clicked != null && clicked.type() == selected.type() && !clicked.hasActed -> {
                selectedUnitId = clicked.id
                battleCommandFlow.beginMove(
                    clicked.id,
                    BattleCommandFlow.UnitPose(clicked.tileX, clicked.tileY, clicked.direction)
                )
                selectedMagicIndex = 0
                propertyMode = false
                eventMessage = "${clicked.name} 선택"
            }

            clicked != null -> {
                val healthBeforeAction = battle.units.mapValues { it.value.hitPoints }
                var magicId: Int? = null
                val result = if (magicMode) {
                    magicMode = false
                    selected.magic.getOrNull(selectedMagicIndex)?.let {
                        magicId = it.id
                        battle.presentation.castMagic(selected.id, clicked.id, it.id)
                    }
                        ?: TacticalActionResult.Rejected("사용할 수 있는 전략이 없습니다.")
                } else battle.presentation.attack(selected.id, clicked.id)
                applyAction(result, selected.name, selected.id, magicId, clicked.id, healthBeforeAction)
                if (result !is TacticalActionResult.Rejected &&
                    battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION
                ) {
                    battleCommandFlow.childCompleted(true)
                }
            }

            else -> moveSelectedForCommand(selected, x, y)
        }
    }

    private fun battleCommandMask(unit: BattleUnit): Int {
        var mask = 0
        if (battle.units.values.any { target ->
                target.visible && !unitsAreAllied(unit, target) &&
                        (unit.attackAllScreen || (target.tileX - unit.tileX to target.tileY - unit.tileY) in unit.attackOffsets)
            }) mask = mask or BattleCommandFlow.ATTACK_BIT
        if (unit.magic.isNotEmpty() && BattleStatus.SILENCE !in unit.statuses) mask =
            mask or BattleCommandFlow.MAGICK_BIT
        if (usableProperties().isNotEmpty()) mask = mask or BattleCommandFlow.PROPERTY_BIT
        if (battle.units.values.any { other ->
                other !== unit && other.visible && unitsAreAllied(unit, other) && other.armId == unit.armId &&
                        kotlin.math.abs(other.tileX - unit.tileX) + kotlin.math.abs(other.tileY - unit.tileY) == 1
            }) mask = mask or BattleCommandFlow.SWAP_BIT
        return mask
    }

    private fun openBattleCommand(unit: BattleUnit) {
        if (battleCommandFlow.phase == BattleCommandFlow.Phase.IDLE ||
            battleCommandFlow.phase == BattleCommandFlow.Phase.COMMITTED ||
            battleCommandFlow.phase == BattleCommandFlow.Phase.ROLLED_BACK
        ) battleCommandFlow.beginMove(unit.id, BattleCommandFlow.UnitPose(unit.tileX, unit.tileY, unit.direction))
        if (battleCommandFlow.phase == BattleCommandFlow.Phase.MOVING) {
            battleCommandFlow.movementCompleted(
                BattleCommandFlow.UnitPose(unit.tileX, unit.tileY, unit.direction),
                battleCommandMask(unit),
            )
        }
        pendingBattleCommandUnit = null
        eventMessage = "${unit.name} 명령 선택"
    }

    private fun moveSelectedForCommand(unit: BattleUnit, x: Int, y: Int) {
        focusCameraOn(unit)
        val deferredMove = battle.presentation.moveUnit(unit.id, x, y)
        when (val result = deferredMove.result) {
            TacticalActionResult.Success -> {
                val path = deferredMove.path
                movementAnimation = if (path.size < 2) null else {
                    val timeline = BattleUnitMoveTimeline.schedule(path, unit.fastMove)
                    UnitMoveAnimation(unit.id, path, timeline, animationClock())
                }
                pendingBattleCommandUnit = unit.id
                pendingBattleCommandScriptStarted = false
                pendingBattleCommandMoveProvenance = path.takeIf { it.size >= 2 }?.let {
                    val from = it.first()
                    val to = it.last()
                    "${unit.characterId}:${from.first},${from.second}->${to.first},${to.second}"
                }
                eventMessage = "${unit.name} 이동 완료 · 명령 선택"
                if (movementAnimation == null) {
                    commitDeferredBattleAction()
                    pendingBattleCommandScriptStarted = true
                    runBattleScript(unit.characterId)
                    completeMoveScriptCommand(unit.id)
                }
            }

            is TacticalActionResult.Rejected -> eventMessage = result.reason
            else -> Unit
        }
    }

    private fun completePendingBattleCommand() {
        val unitId = pendingBattleCommandUnit ?: return
        if (movementAnimation?.let { animationClock() < it.endsAt } == true) return
        val finalDirection = movementAnimation?.takeIf { it.unitId == unitId }
            ?.timeline?.segments?.lastOrNull()?.direction
        movementAnimation = null
        commitDeferredBattleAction()
        finalDirection?.let { direction -> battle.presentation.presentationUnit(unitId)?.direction = direction }
        pendingBattleCommandMoveProvenance?.let { provenance ->
            playerMoveCommitted = true
            committedPlayerMove = provenance
            pendingBattleCommandMoveProvenance = null
        }
        battle.units[unitId]?.let { unit ->
            if (!pendingBattleCommandScriptStarted) {
                pendingBattleCommandScriptStarted = true
                runBattleScript(unit.characterId)
            }
            completeMoveScriptCommand(unit.id)
        }
    }

    /** 이동 스크립트 완료 처리: 스크립트 종료 여부에 따라 이동 명령을 폐기하거나 해당 유닛 명령 창을 연다. */
    private fun completeMoveScriptCommand(unitId: String) {
        if (!BattleMoveScriptContinuation.shouldOpenCommand(
                scriptRuntime.state,
                scriptRuntime.stage.battleEndedByScript,
            )
        ) {
            if (scriptRuntime.stage.battleEndedByScript) {
                pendingBattleCommandUnit = null
                pendingBattleCommandScriptStarted = false
                pendingBattleCommandMoveProvenance = null
                battleCommandFlow.abandonMoveForScriptEnd()
            }
            return
        }
        battle.units[unitId]?.let(::openBattleCommand)
    }

    private fun battleCommandTagAt(x: Float, y: Float): Int? = when {
        x in 743.6f..863.6f && y in 291.175f..411.175f -> 0
        x in 871.6f..991.6f && y in 291.175f..411.175f -> 1
        x in 1000.6f..1120.6f && y in 291.175f..411.175f -> 2
        x in 743.6f..863.6f && y in 165.42f..285.42f -> 3
        x in 871.6f..991.6f && y in 165.42f..285.42f -> 4
        x in 1000.6f..1120.6f && y in 165.42f..285.42f -> 5
        x in 842.65f..1024.55f && y in 106.491f..156.491f -> 6
        else -> null
    }

    private fun dispatchBattleCommand(tag: Int) {
        val selected = selectedUnitId?.let(battle.units::get) ?: return
        when (val result = battleCommandFlow.touch(tag, BattleCommandFlow.TOUCH_END)) {
            is BattleCommandFlow.Result.OpenChild -> when (result.command) {
                BattleCommandFlow.Command.MAGICK -> openMagickList(selected)
                BattleCommandFlow.Command.PROPERTY -> openUsePropertyLayer()
                BattleCommandFlow.Command.ATTACK -> eventMessage = "공격 대상을 선택하세요."
                BattleCommandFlow.Command.SWAP -> eventMessage = "교환할 아군을 선택하세요."
                BattleCommandFlow.Command.SIEGE -> eventMessage = "포위 공격 대상을 선택하세요."
                else -> Unit
            }

            is BattleCommandFlow.Result.Commit -> {
                selected.markActionComplete()
                selectedUnitId = null
                eventMessage = "${selected.name} 대기"
            }

            is BattleCommandFlow.Result.Rollback -> {
                selected.tileX = result.pose.x; selected.tileY = result.pose.y
                selected.direction = result.pose.direction; selected.hasMoved = false
                selectedUnitId = null
                eventMessage = "명령 선택을 취소했습니다."
            }

            BattleCommandFlow.Result.Ignored -> Unit
        }
    }

    internal fun applyAction(
        result: TacticalActionResult,
        unitName: String,
        actorId: String? = null,
        magicId: Int? = null,
        targetId: String? = null,
        healthBeforeAction: Map<String, Int> = emptyMap(),
        moveActorId: String? = null,
        continueBattleScript: Boolean = true,
        criticalSpeechPresented: Boolean = false,
    ) {
        val firstCriticalSpeech = when (result) {
            is TacticalActionResult.Attack -> result.physicalPasses.firstOrNull()?.criticalSpeech
            is TacticalActionResult.Magic -> result.criticalSpeeches.firstOrNull()
            else -> null
        }
        if (!criticalSpeechPresented && firstCriticalSpeech != null && actorId != null) {
            pendingCriticalSpeechAction = PendingCriticalSpeechAction(
                result, unitName, actorId, magicId, targetId, healthBeforeAction, moveActorId, continueBattleScript,
            )
            battle.presentation.presentationUnit(actorId)?.let(::focusCameraOn)
            val characterId = battle.presentation.presentationUnit(actorId)?.characterId
            scriptRuntime.presentExternalBattleDialogue(Dialogue(characterId?.toString(), firstCriticalSpeech))
            return
        }
        movementAnimation = if (result == TacticalActionResult.Success && moveActorId != null) {
            val path = battle.lastMovePath(moveActorId)
            if (path.size < 2) null else {
                val timeline = BattleUnitMoveTimeline.schedule(path, battle.units[moveActorId]?.fastMove ?: true)
                UnitMoveAnimation(moveActorId, path, timeline, animationClock())
            }
        } else if (result !is TacticalActionResult.Rejected) null else movementAnimation
        actionAnimation = when (result) {
            is TacticalActionResult.Attack -> actorId?.let { id ->
                val delayed = battle.presentation.presentationUnit(id)?.attackDelay == true
                val visualCritical = result.physicalPasses.firstOrNull()?.critical ?: result.critical
                val sourceAction = BattleAttackSequence.selectAttackAction(visualCritical, delayed)
                sourceActionAnimation(id, sourceAction, battleDirection(id, targetId))
            }
            is TacticalActionResult.Magic -> actorId?.let {
                battle.presentation.presentationUnit(it)?.let { unit -> focusCameraOn(unit, forceCenter = true) }
                sourceActionAnimation(it, if (result.critical) 50 else 5, battleDirection(it, targetId))
            }
            is TacticalActionResult.Item -> actorId?.let { id ->
                targetId?.let(battle.presentation::presentationUnit)?.let(::focusCameraOn)
                UnitActionAnimation(
                    id, UnitAnimationKind.SPECIAL, battleDirection(id, targetId),
                    animationClock(), animationClock() + 1.5f, sourceAction = 1,
                )
            }

            else -> null
        }
        magicEffectAnimations.clear()
        (result as? TacticalActionResult.Magic)?.let { magic ->
            actorId?.let { casterId ->
                battle.pendingActionTransaction
                    ?.takeIf { it.actorId == casterId }
                    ?.let { deferred ->
                        deferred.commitVitals(
                            casterId,
                            mp = deferred.initialMp(casterId)?.minus(magic.cost)?.coerceAtLeast(0),
                        )
                    }
            }
            val casterId = actorId ?: return@let
            val profile = magicId?.let(gameDataCatalog::magicProfile)
            val effectId = profile?.effectId ?: 255
            val effect = magicEffects.effect(effectId)
            val firstPass = magic.passes.firstOrNull().orEmpty()
            val firstResult = magic.copy(
                targets = firstPass,
                passes = listOf(firstPass),
                criticalSpeeches = listOf(magic.criticalSpeeches.firstOrNull()),
                localSettlements = listOf(magic.localSettlements.firstOrNull() ?: MagicLocalSettlement(emptyList())),
            )
            val startedAt = actionAnimation?.endsAt ?: animationClock()
            val firstEffect = effect?.let {
                MagicEffectAnimation(effectId, firstPass.map(MagicTarget::targetId), startedAt, startedAt + it.duration)
                    .also(magicEffectAnimations::add)
            }
            scheduleMagicPresentation(
                firstResult,
                casterId,
                profile,
                healthBeforeAction,
                effectAnimations = listOfNotNull(firstEffect)
            )
            val unitVisualStates = combatPresentationUnitVisualStates()
            val deferred = battle.pendingActionTransaction
            val visualMp = unitVisualStates.mapValues { (id, unit) -> deferred?.initialMp(id) ?: unit.magicPoints }
                .toMutableMap()
            visualMp[casterId] = ((deferred?.initialMp(casterId)
                ?: unitVisualStates[casterId]?.magicPoints ?: 0) - magic.cost).coerceAtLeast(0)
            BattleCombatPresentationQueueCoordinator.deferredMagicQueuePlan(
                magic,
                casterId,
                profile,
                BattleCombatPresentationQueueCoordinator.VisualState(healthBeforeAction, visualMp),
                unitVisualStates,
            )?.let { plan ->
                queuedMagicPresentation = MagicPassPresentationQueue(
                    result = magic,
                    casterId = casterId,
                    targetId = targetId,
                    profile = profile,
                    effectId = effectId,
                    nextPassIndex = plan.nextPassIndex,
                    startsAt = (firstEffect?.endsAt ?: startedAt) + 1f,
                    visualHp = plan.visualState.hitPoints.toMutableMap(),
                    visualMp = plan.visualState.magicPoints.toMutableMap(),
                )
            }
        }
        scheduleCombatPresentation(result, actorId, targetId, healthBeforeAction)
        when (result) {
            is TacticalActionResult.Item -> audio.playBattleEffect(39) // HUIFU
            else -> Unit
        }
        eventMessage = when (result) {
            TacticalActionResult.Success -> "$unitName 이동 완료"
            is TacticalActionResult.Attack -> "$unitName 공격 · ${result.damage + result.followUpDamage} 피해${if (result.followUpDamage > 0) " (연격 ${result.followUpDamage})" else ""}${if (result.counterDamage + result.counterFollowUpDamage > 0) " · 반격 ${result.counterDamage + result.counterFollowUpDamage}" else ""}${if (result.defeated) " · 격파" else ""}"
            is TacticalActionResult.Magic -> "$unitName ${result.name} · MP ${result.cost} · ${if (result.targets.sumOf { it.healing } > 0) "${result.targets.sumOf { it.healing }} 회복" else "${result.targets.sumOf { it.damage }} 피해"}${if (result.targets.any { it.defeated }) " · 격파" else ""}"
            is TacticalActionResult.Item -> "$unitName ${result.name} · ${result.effect}"
            is TacticalActionResult.Rejected -> result.reason
        }
        if (result !is TacticalActionResult.Rejected) selectedUnitId = null
        if (result !is TacticalActionResult.Rejected) magicMode = false
        if (result !is TacticalActionResult.Rejected) propertyMode = false
        battle.outcome()?.let { eventMessage = outcomeText(it) }
        if (continueBattleScript && result !is TacticalActionResult.Rejected) {
            pendingBattleScriptPassesAfterAction = 1
            pendingBattleCompletedScriptPasses = 0
            pendingBattleActionCommitted = false
            pendingBattleSettlementActorId = actorId
            deathTimeline.finishPostActionCallbacks()
        }
    }
    private fun resumeCriticalSpeechAction() {
        val pending = pendingCriticalSpeechAction ?: return
        if (scriptRuntime.state != PlaybackState.COMPLETE || scriptRuntime.currentDialogue != null) return
        pendingCriticalSpeechAction = null
        applyAction(
            pending.result, pending.unitName, pending.actorId, pending.magicId, pending.targetId,
            pending.healthBeforeAction, pending.moveActorId, pending.continueBattleScript,
            criticalSpeechPresented = true,
        )
    }

    private fun usableProperties(): List<BattlePropertyItem> = campaign.inventory.items.keys.mapNotNull { itemId ->
        gameDataCatalog.equipmentProfile(itemId)
            ?.takeIf { (it.itemType in 26..37 || it.itemType in 42..43) && (campaign.inventory.items[itemId] ?: 0) > 0 }
            ?.let { BattlePropertyItem(it.id, it.name, it.itemType, it.value) }
    }

    private fun outcomeText(outcome: BattleOutcome): String = when (outcome) {
        BattleOutcome.PLAYER_VICTORY -> "승리! Enter로 다음 시나리오 · Esc로 돌아가기"
        BattleOutcome.ENEMY_VICTORY -> "패배… Enter로 전투 재시작 · Esc로 돌아가기"
    }
    private fun visibleBattleOutcome(): BattleOutcome? =
        battle.outcome().takeIf { bootstrapPhase == BattleBootstrapPhase.COMPLETE }

    private fun victorySaveAnswerAt(x: Float, y: Float): Int? = when {
        x in 460f..620f && y in 285f..365f -> 0 // 예
        x in 690f..850f && y in 285f..365f -> 1 // 비
        else -> null
    }

    private fun settlementAnimatedValues(overlay: SettlementInfoView): Map<Int, Int> {
        val values = buildList {
            overlay.deltas.forEach { delta ->
                add(
                    InfoBaseValueAnimation.Value(
                        if (delta.kind == SettlementInfoKind.HP) 0 else 1,
                    delta.before, delta.after,
                    battle.presentation.presentationUnit(overlay.unitId)?.let {
                        if (delta.kind == SettlementInfoKind.HP) it.maxHitPoints else it.maxMagicPoints
                    } ?: delta.after.coerceAtLeast(1),
                ))
            }
            overlay.grants.forEach { grant ->
                when (grant.kind) {
                    SettlementGrowthKind.UNIT_EXP -> grant.unitResult?.let { result ->
                        add(
                            InfoBaseValueAnimation.Value(
                                2, result.oldExperience, result.oldExperience + result.gained,
                                (result.oldExperience + result.gained).coerceAtLeast(1)
                            )
                        )
                    }

                    SettlementGrowthKind.WEAPON_EXP -> grant.equipmentResult?.let { result ->
                        add(
                            InfoBaseValueAnimation.Value(
                                3, result.oldExperience, result.oldExperience + result.gained,
                                (result.oldExperience + result.gained).coerceAtLeast(1)
                            )
                        )
                    }

                    SettlementGrowthKind.ARMOR_EXP -> grant.equipmentResult?.let { result ->
                        add(
                            InfoBaseValueAnimation.Value(
                                4, result.oldExperience, result.oldExperience + result.gained,
                                (result.oldExperience + result.gained).coerceAtLeast(1)
                            )
                        )
                    }
                }
            }
        }
        val current = values.associate { it.index to it.source }.toMutableMap()
        if (values.isEmpty()) return current
        val animation = InfoBaseValueAnimation(values)
        val callbacks = (((animationClock() - overlay.startedAt - .1f) / .2f).toInt()).coerceAtLeast(0)
        repeat(callbacks) {
            animation.callback()?.let { update -> update.text.toIntOrNull()?.let { current[update.index] = it } }
        }
        return current
    }
    private fun drawSettlementOverlays() {
        settlementPresentation.infoView()?.let { overlay ->
            val unit = battle.presentation.presentationUnit(overlay.unitId) ?: return@let
            val values = settlementAnimatedValues(overlay)
            val mine = overlay.panel == SettlementInfoPanel.MINE
            val x = 736f
            val y = 96f
            val h = if (mine) 258f else 194f
            batch.projectionMatrix = viewport.camera.combined
            batch.begin()
            batch.color = Color.WHITE
            batch.draw(unitInfoAssets.unitInfoBox1, x, y, 471f, h)
            font.data.setScale(32f / 26f)
            font.color = Color.WHITE
            font.draw(batch, overlay.title, x + 12f, y + h - 18f)
            font.draw(batch, "Lv ${unit.level}  ${gameDataCatalog.postsName(unit.posts)}", x + 205f, y + h - 18f)
            val rows = buildList {
                overlay.deltas.forEach { delta ->
                    val index = if (delta.kind == SettlementInfoKind.HP) 0 else 1
                    val max = if (index == 0) unit.maxHitPoints else unit.maxMagicPoints
                    add(Triple(delta.kind.name, values[index] ?: delta.before, max))
                }
                overlay.grants.forEach { grant ->
                    when (grant.kind) {
                        SettlementGrowthKind.UNIT_EXP -> grant.unitResult?.let {
                            add(
                                Triple(
                                    "EXP",
                                    values[2] ?: it.oldExperience,
                                    (it.oldExperience + it.gained).coerceAtLeast(1)
                                )
                            )
                        }

                        SettlementGrowthKind.WEAPON_EXP -> grant.equipmentResult?.let {
                            add(
                                Triple(
                                    "WQ",
                                    values[3] ?: it.oldExperience,
                                    (it.oldExperience + it.gained).coerceAtLeast(1)
                                )
                            )
                        }

                        SettlementGrowthKind.ARMOR_EXP -> grant.equipmentResult?.let {
                            add(
                                Triple(
                                    "HJ",
                                    values[4] ?: it.oldExperience,
                                    (it.oldExperience + it.gained).coerceAtLeast(1)
                                )
                            )
                        }
                    }
                }
            }
            rows.forEachIndexed { index, (label, value, max) ->
                val rowY = y + h - 70f - index * 48f
                font.draw(batch, label, x + 16f, rowY)
                batch.draw(unitInfoAssets.unitInfoProgress, x + 78f, rowY - 22f, 300f, 20f)
                val bar =
                    if (label == "MP") unitInfoAssets.unitInfoMark2 else if (label == "EXP") unitInfoAssets.unitInfoMark6 else unitInfoAssets.unitInfoMark3
                batch.draw(bar, x + 80f, rowY - 20f, 296f * value.coerceAtLeast(0) / max.coerceAtLeast(1), 16f)
                font.draw(batch, "$value/$max", x + 390f, rowY)
            }
            font.data.setScale(1f)
            batch.end()
        }
        settlementPresentation.info2View()?.let { overlay ->
            val elapsed = (animationClock() - overlay.startedAt).coerceAtLeast(0f)
            val visibleChars = (elapsed / .04f).toInt().coerceIn(0, overlay.text.length)
            val text = overlay.text.take(visibleChars)
            val width = (text.length * 40f + 40f).coerceAtLeast(90f)
            batch.projectionMatrix = viewport.camera.combined
            batch.begin()
            batch.color = Color.WHITE
            batch.draw(unitInfoAssets.unitInfoBox1, (1488.372f - width) / 2f, 365f, width, 83f)
            font.data.setScale(40f / 26f)
            font.color = Color.WHITE
            font.draw(batch, text, (1488.372f - width) / 2f + 20f, 425f)
            font.data.setScale(1f)
            batch.end()
        }
    }

    private fun drawScriptPresentationOverlay() {
        val active = scriptPresentationTimeline.snapshot() ?: return
        val elapsed = (animationClock() - active.startedAt).coerceAtLeast(0f)
        when (val request = active.request) {
            is ScenarioScriptPresentationRequest.RectangleHighlight -> {
                if ((elapsed / .3f).toInt() % 2 != 0) return
                val minX = minOf(request.x1, request.x2)
                val maxX = maxOf(request.x1, request.x2)
                val minY = minOf(request.y1, request.y2)
                val maxY = maxOf(request.y1, request.y2)
                shapes.projectionMatrix = viewport.camera.combined
                shapes.begin(ShapeRenderer.ShapeType.Filled)
                shapes.color = Color(1f, .08f, .04f, .42f)
                shapes.rect(
                    boardLeft + minX * boardTile,
                    tileBottom(maxY),
                    (maxX - minX + 1) * boardTile,
                    (maxY - minY + 1) * boardTile,
                )
                shapes.end()
            }

            is ScenarioScriptPresentationRequest.UnitHighlight -> {
                if ((elapsed / .3f).toInt() % 2 != 0) return
                active.battleUnitId?.let(battle.presentation::presentationUnit)?.let { unit ->
                    shapes.projectionMatrix = viewport.camera.combined
                    shapes.begin(ShapeRenderer.ShapeType.Filled)
                    shapes.color = Color(1f, .08f, .04f, .42f)
                    shapes.rect(boardLeft + unit.tileX * boardTile, tileBottom(unit.tileY), boardTile, boardTile)
                    shapes.end()
                }
            }

            is ScenarioScriptPresentationRequest.GetItem -> {
                if (active.phase != ScriptPresentationTimeline.Phase.ITEM_ICON) return
                val unit = active.battleUnitId?.let(battle.presentation::presentationUnit) ?: return
                val rise = (elapsed / .3f).coerceIn(0f, 1f) * (boardTile / 2f)
                val iconIndex = gameDataCatalog.equipmentProfile(request.itemId)?.icon ?: return
                val icon = dynamicTextures.itemIcon(iconIndex) ?: return
                batch.projectionMatrix = viewport.camera.combined
                batch.begin()
                batch.color = Color.WHITE
                batch.draw(
                    icon,
                    boardLeft + unit.tileX * boardTile,
                    tileBottom(unit.tileY) + rise - boardTile / 4f,
                    boardTile,
                    boardTile,
                )
                batch.end()
            }

            is ScenarioScriptPresentationRequest.MapObjects -> Unit
            is ScenarioScriptPresentationRequest.UnitStatusSettlement -> Unit
        }
    }

    private fun battleRewardOverlayView(): BattleRewardOverlayView {
        val flow = outcomePresentation.rewardFlow
        val phase = flow?.phase?.let {
            when (it) {
                BattleRewardFlow.Phase.MONEY -> BattleRewardOverlayPhase.MONEY
                BattleRewardFlow.Phase.ITEMS -> BattleRewardOverlayPhase.ITEMS
                BattleRewardFlow.Phase.END -> BattleRewardOverlayPhase.END
                BattleRewardFlow.Phase.COMPLETE -> BattleRewardOverlayPhase.COMPLETE
            }
        }
        val items = flow?.let {
            it.reward.itemIds.take(it.visibleItemCount).take(3).map { id ->
                val profile = gameDataCatalog.equipmentProfile(id)
                BattleRewardItemView(
                    name = profile?.name ?: "아이템 $id",
                    icon = profile?.icon?.let(dynamicTextures::itemIcon),
                )
            }
        }.orEmpty()
        return BattleRewardOverlayView(
            worldWidth = viewport.worldWidth,
            worldHeight = 800f,
            phase = phase,
            money = flow?.reward?.money ?: 0,
            stars = flow?.reward?.let { reward ->
                (0 until 3).joinToString("  ") { if (reward.flag and (1 shl it) != 0) "★" else "☆" }
            }.orEmpty(),
            items = items,
            sectionVisible = rewardRouteState != null,
        )
    }
    private fun drawRewardSectionOverlay() {
        batch.projectionMatrix = viewport.camera.combined
        battleRewardOverlayRenderer.draw(
            BattleRewardOverlayView(
                worldWidth = viewport.worldWidth,
                worldHeight = 800f,
                phase = null,
                sectionVisible = true,
            ),
        )
    }
    private fun installItemUpgradeRoute() {
        itemUpgradeRouteFixtureController.install(
            itemUpgradeRouteState,
            battle.units.values.map { unit ->
                BattleItemUpgradeRouteFixtureController.Unit(
                    id = unit.id,
                    characterId = unit.characterId,
                    visible = unit.visible,
                    playerSide = unit.isPlayerSide(),
                    enemySide = unit.type().isEnemySide(),
                )
            },
            object : BattleItemUpgradeRouteFixtureController.Commands {
                override fun markRouteInstalled() {
                    outcomePresentation.markItemUpgradeRouteInstalled()
                }

                override fun seedUpgrade(
                    owner: BattleItemUpgradeRouteFixtureController.Unit,
                    target: BattleItemUpgradeRouteFixtureController.Unit,
                    sample: BattleItemUpgradeRouteFixtureController.UpgradeSample,
                ) {
                    val ownerId = requireNotNull(owner.characterId)
                    val oldExperience = gameDataCatalog.equipmentExperienceLimit(sample.itemId, sample.oldLevel) - 1
                    val current = campaign.inventory.equipment[ownerId] ?: CampaignEquipment(
                        sample.weaponStorageId,
                        sample.oldLevel,
                        sample.fallbackArmor,
                        sample.fallbackArmorLevel,
                        sample.fallbackAuxiliary,
                    )
                    campaign.inventory.setEquipment(
                        ownerId,
                        current.copy(
                            weapon = sample.weaponStorageId,
                            weaponLevel = sample.oldLevel,
                            weaponExperience = oldExperience,
                        ),
                    )
                    campaign.unitNames[ownerId] = sample.ownerName
                    battle.experience.addEquipmentExperience(owner.id, target.id, sample.gainedExperience)
                }

                override fun openUpgrade() {
                    outcomePresentation.openEquipmentUpgradeIfNeeded()
                }

                override fun upgradeOpened(): Boolean = outcomePresentation.itemUpgradeFlow?.request?.leveledUp == true
            },
        )
    }
    private fun drawBattleEdit2Route() {
        val route = battleEdit2RouteState ?: return
        val edit = battleEdit2 ?: return
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        mapTexture?.let { batch.draw(it, -320f, -96f, 1920f, 1920f) }
        batch.end()
        if (route == BattleEditLayer2Route.APPLY) return

        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, .314f)
        shapes.rect(0f, 0f, 1488.372f, 800f)
        shapes.end()
        batch.begin()
        batch.color = Color.WHITE

        fun tiled(x: Float, y: Float, w: Float, h: Float) {
            var yy = 0f; while (yy < h) {
                var xx = 0f; while (xx < w) {
                    batch.draw(
                        unitInfoAssets.unitInfoLogo,
                        x + xx,
                        y + yy,
                        minOf(96f, w - xx),
                        minOf(96f, h - yy)
                    ); xx += 96f
                }; yy += 96f
            }
        }


        fun button(x: Float, y: Float, text: String) {
            NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, x, y, 238.8f, 56.6f); font.color =
                Color.BLACK; font.draw(batch, text, x + 8f, y + 43f)
        }
        tiled(453.686f, 195f, 581f, 410f)
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 767.301f, 487.229f, 169.8f, 50f)
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 768.224f, 430.411f, 160f, 50f)
        font.color = Color.BLACK
        font.draw(batch, "전장 편집", 669.431f, 592f); font.draw(batch, "날씨: ", 675.735f, 530f)
        font.draw(batch, edit.weatherLabel, 817.601f, 529f); font.draw(batch, "현재 턴:", 618.435f, 474f)
        font.draw(batch, edit.roundText, 770.224f, 472f)
        button(495.886f, 207.8f, "수정"); button(772.686f, 207.8f, "취소")
        button(495.886f, 354.9f, "전역"); button(495.886f, 277.1f, "적군 체력 감소")
        button(772.686f, 354.9f, "적군 전멸"); button(772.686f, 277.1f, "아군 만피")
        batch.end()
        if (route == BattleEditLayer2Route.WEATHER) {
            shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color = Color(0f, 0f, 0f, .392f); shapes.rect(
                0f,
                0f,
                1488.372f,
                800f
            ); shapes.end()
            batch.begin(); batch.color = Color.WHITE
            NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 767.878f, 308.794f, 169.8f, 179.5f)
            BattleEditLayer2.weatherNames.forEachIndexed { i, text ->
                val y = 463.854f - i * 50f; NinePatch(
                unitInfoAssets.unitInfoBox1,
                3,
                3,
                3,
                3
            ).draw(batch, 767.878f, y, 169.8f, 50f); font.draw(batch, text, 800f, y + 41f)
            }
            batch.end()
        }
        if ((route == BattleEditLayer2Route.CHILD || route == BattleEditLayer2Route.CHILD_SCENE || route == BattleEditLayer2Route.REGISTER) && battleEdit3Open) drawBattleEdit3Child()
        if (battleEdit3ScenePanelOpen) drawBattleEdit3ScenePanel()
        if (route == BattleEditLayer2Route.REGISTER && battleRegisterRoute?.view()?.registerAttached == true) drawBattleRegisterLayer()
    }

    private fun drawBattleEdit3Child() {
        shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color = Color(0f, 0f, 0f, .314f); shapes.rect(
            0f,
            0f,
            1488.372f,
            800f
        ); shapes.end()
        batch.begin(); batch.color = Color.WHITE
        var yy = 0f; while (yy < 410f) {
            var xx = 0f; while (xx < 600f) {
                batch.draw(
                    unitInfoAssets.unitInfoLogo,
                    444.186f + xx,
                    195f + yy,
                    minOf(96f, 600f - xx),
                    minOf(96f, 410f - yy)
                ); xx += 96f
            }; yy += 96f
        }

        fun box(x: Float, y: Float, w: Float, h: Float) =
            NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, x, y, w, h)


        fun btn(x: Float, y: Float, w: Float, h: Float, text: String) {
            NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, x, y, w, h); font.draw(
                batch,
                text,
                x + 18f,
                y + 42f
            )
        }
        box(715.31f, 397f, 225.2f, 50f); box(715.31f, 315f, 225.2f, 50f); box(714.91f, 479f, 250f, 50f)
        font.color = Color.BLACK; font.draw(batch, "전역 변수 편집", 629.271f, 596f); font.draw(
            batch,
            "야심:",
            625.117f,
            438f
        ); font.draw(batch, "50", 717.31f, 439f)
        font.draw(batch, "금전:", 625.117f, 356f); font.draw(batch, "0", 717.31f, 357f); font.draw(
            batch,
            "장면 이동:",
            544.957f,
            519f
        ); font.draw(batch, "영천의 전투R", 718.51f, 520f)
        btn(876.797f, 212.983f, 150.4f, 58.5f, "수정"); btn(719.152f, 212.983f, 150.4f, 58.5f, "폐쇄"); btn(
            487.035f,
            212.95f,
            221.5f,
            58.5f,
            "창고 비우기"
        )
        batch.end()
    }

    private fun drawBattleEdit3ScenePanel() {
        shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color = Color(0f, 0f, 0f, .392f); shapes.rect(
            0f,
            0f,
            1488.372f,
            800f
        ); shapes.end()
        batch.begin(); batch.color = Color.WHITE
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 715.136f, 298.894f, 250f, 179.5f)
        val names = listOf(
            "영천의 전투",
            "사수관 전투",
            "호로관 전투",
            "동탁 추격전",
            "청주 황건 토벌전",
            "서주 복수전",
            "복양의 전투",
            "복양의 전투 2",
            "복양의 전투 3",
            "황제 구출 전투"
        )
        names.forEachIndexed { index, name ->
            val y = 428.394f - index * 50f; NinePatch(
            unitInfoAssets.unitInfoBox1,
            3,
            3,
            3,
            3
        ).draw(batch, 715.136f, y, 250f, 50f); font.draw(batch, "$index $name", 720.136f, y + 41f)
        }
        batch.end()
    }

    private fun drawBattleRegisterLayer() {
        batch.begin(); batch.color = Color.WHITE
        for (ty in 0..4) for (tx in 0..8) batch.draw(
            unitInfoAssets.unitInfoLogo,
            344.186f + tx * 96f,
            163.5f + ty * 96f,
            96f,
            96f
        )
        val box = NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3)
        val button = NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11)
        box.draw(batch, 344.186f, 163.5f, 800f, 473f); box.draw(batch, 355.686f, 520f, 773f, 54f)
        button.draw(batch, 916.163f, 180.272f, 200f, 50f); button.draw(batch, 698.334f, 180.272f, 200f, 50f)
        font.color = Color.BLACK
        font.draw(batch, "등록 코드 생성기", 624.186f, 628f)
        font.draw(batch, "활성화 코드를 입력하세요", 369.186f, 563f)
        font.draw(batch, "Label", 360.186f, 280f); font.draw(batch, "Label", 360.186f, 435f)
        font.draw(batch, "생성 공유", 939.408f, 222f); font.draw(batch, "취소", 748.334f, 229f)
        batch.end()
    }

    private fun drawItemUpgrade(flow: ItemUpgradeFlow) {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        val left = 544.186f
        val bottom = 270.5f
        for (ty in 0..2) for (tx in 0..4) {
            val width = minOf(96f, 400f - tx * 96f)
            val height = minOf(96f, 259f - ty * 96f)
            if (width > 0f && height > 0f) batch.draw(
                unitInfoAssets.unitInfoLogo,
                left + tx * 96f,
                bottom + ty * 96f,
                width,
                height
            )
        }
        batch.draw(unitInfoAssets.unitInfoBox3, left, bottom, 400f, 259f)
        batch.draw(unitInfoAssets.unitInfoBox2, 551.222f, 451.34f, 70f, 70f)
        dynamicTextures.itemIcon(gameDataCatalog.equipmentProfile(flow.request.itemId)?.icon ?: 1)?.let {
            batch.draw(it, 554.222f, 454.34f, 64f, 64f)
        }
        batch.draw(unitInfoAssets.unitInfoBox2, 553.136f, 281.5f, 379.5f, 130.4f)
        itemUpgradeFont.color = Color.BLACK
        itemUpgradeFont.draw(batch, flow.itemName, 628.186f, 513f)
        itemUpgradeFont.draw(batch, "Lv", 861.186f, 513f)
        itemUpgradeFont.draw(batch, flow.request.newLevel.toString(), 908.186f, 513f)
        itemUpgradeFont.draw(batch, flow.ownerName, 624.386f, 458f)
        itemUpgradeFont.draw(batch, "장비", 815.347f, 458f)
        itemUpgradeFont.draw(
            batch,
            "${flow.attributeName} ${flow.request.oldValue} -> ${flow.request.newValue}",
            554.836f,
            403.7f
        )
        itemUpgradeFont.color = Color.WHITE
        batch.end()
    }
    private fun writeYingchuanEntryFlowIfReady() {
        val output = yingchuanEntryFlowTracePath ?: return
        if (yingchuanEntryFlowWritten || !yingchuanEntryFlowSawInit) return
        if (!scriptRuntime.stage.battleDrawRequested || battleInitLayer.view().attached ||
            scriptRuntime.state != PlaybackState.DIALOGUE || scriptRuntime.currentDialogue == null
        ) return

        val json = Json()


        fun stateText(init: Boolean): String {
            val dialogue = if (init) "null" else "{\"name\":\"SayLayer\",\"active\":true}"
            val tailLayer = if (init) "BattleInitLayer" else "SayLayer"
            return "{\"scene\":\"Battle\",\"isDraw\":${!init},\"paused\":true,\"round\":${battle.round},\"camp\":-1," +
                    "\"battleInit\":$init,\"dialogue\":$dialogue,\"modal\":null," +
                    "\"layers\":[\"BattleScreen\",\"NoticeInfoLayer\",\"MiniMapLayer\",\"$tailLayer\"]}"
        }

        val records = listOf(
            "battle-init" to stateText(true),
            "dialogue" to stateText(false)
        ).mapIndexed { sequence, (phase, text) ->
            "{\"sequence\":$sequence,\"frame\":$sequence,\"phase\":\"$phase\",\"layer\":\"BattleScreen/entry-flow\"," +
                    "\"nodePath\":\"BattleScreen/entry-flow\",\"drawType\":\"state\",\"x\":0,\"y\":0,\"w\":1,\"h\":1," +
                    "\"assetId\":\"none\",\"opacity\":1,\"blend\":\"normal\",\"visible\":true,\"text\":${
                        json.toJson(
                            text,
                            String::class.java
                        )
                    }}"
        }
        val file = Gdx.files.absolute(output)
        file.parent().mkdirs()
        file.writeString(records.joinToString("\n") + "\n", false)
        val stateOutput = output.removeSuffix(".jsonl") + ".state.json"


        fun quoted(value: String?): String = value?.let { json.toJson(it, String::class.java) } ?: "null"
        Gdx.files.absolute(stateOutput).writeString(
            "{\n  \"route\": \"actual-r00-ui-to-s00\",\n  \"input\": \"ScenarioScreen dialogue/choice input callbacks\"," +
                    "\n  \"scriptState\": ${quoted(scriptRuntime.state.name)},\n  \"speaker\": ${quoted(scriptRuntime.currentDialogue?.speakerId)}," +
                    "\n  \"text\": ${quoted(scriptRuntime.currentDialogue?.text)},\n  \"drawRequested\": ${scriptRuntime.stage.battleDrawRequested}," +
                    "\n  \"battleInitAttached\": ${battleInitLayer.view().attached},\n  \"modalKind\": ${
                        quoted(
                            scriptRuntime.currentModalKind?.name
                        )
                    }\n}\n",
            false,
        )
        yingchuanEntryFlowWritten = true
        Gdx.app.log("JojoGame", "YINGCHUAN_ENTRY_FLOW_OK: $output")
        Gdx.app.exit()
    }


    fun renderEventLog(): String {
        if (mineUnitInfoRoute) return MineUnitInfoRenderEvents.jsonl(requireNotNull(mineUnitInfoLayer).view())
        if (otherUnitInfoRoute) return OtherUnitInfoRenderEvents.jsonl(requireNotNull(otherUnitInfoLayer).view())
        if (battleMenuRoute) return BattleMenuRenderEvents.jsonl(requireNotNull(battleMenuLayer).view())
        if (battleTerrainRoute) return TerrainLayerRenderEvents.jsonl(terrainLayer)
        battleEdit2RouteState?.let { return BattleEditLayer2RenderEvents.jsonl(it, requireNotNull(battleEdit2)) }
        if (battleCommandRouteState != null) return battleCommandRenderEventLog()
        if (roundRouteState != null) return roundRenderEventLog()
        if (miniMapRouteState != null) return MiniMapRenderEvents.jsonl(miniMapLayer.shown)
        if (autoBattleRouteState != null) return autoBattleRenderEventLog()
        if (battleCharacterRouteState != null) return battleCharacterRouteRenderEventLog()
        if (jiqiRouteFixture) return jiqiRenderEventLog()
        if (magickRouteState != null) return magickRenderEventLog()
        if (usePropertyRouteState != null) return usePropertyRenderEventLog()
        if (loseRestartRoute) return RenderEventLog().also {
            LoseSceneRenderEvents.append(it, requireNotNull(outcomePresentation.loseSceneFlow))
        }.jsonl()

        val winRoute = winConditionRouteState
        if (rewardRouteState == null && itemUpgradeRouteState == null && winRoute == null &&
            !battleInitRoute && !battleDialogueBlendRoute
        ) return RenderEventLog().jsonl()
        val phase = when {
            battleInitRoute -> "battle-init"
            battleDialogueBlendRoute -> "battle-dialogue-blending"
            itemUpgradeRouteState != null -> "battle-item-upgrade-panel-route"
            winRoute == RuntimeBattleRoute.WIN_COMPACT -> "battle-win-condition-compact"
            winRoute == RuntimeBattleRoute.WIN_FULL -> "battle-win-condition-full"
            else -> "yingchuan-reward"
        }
        return battleRenderEventLog(phase)
    }

    /** 화면 상태 Port를 evidence coordinator에 연결하고 보드 정책을 적용한 JSONL을 반환한다. */
    private fun battleRenderEventLog(phase: String): String {
        val coordinator = battleRenderEventEvidenceCoordinator()
        val projection = coordinator.projection(
            phase,
            BattleRenderEventEvidenceCoordinator.RouteState(
                battleInit = battleInitRoute,
                dialogueBlend = battleDialogueBlendRoute,
                winConditionRoute = winConditionRouteState,
                itemUpgrade = itemUpgradeRouteState != null,
                reward = rewardRouteState != null,
            ),
        )
        boardLeft = projection.boardLeft
        boardBottom = projection.boardBottom
        boardTile = projection.boardTile
        return coordinator.jsonl(projection)
    }

    /** 현재 화면 객체를 evidence coordinator가 소비하는 불변 입력 Port로만 노출한다. */
    private fun battleRenderEventEvidenceCoordinator() = BattleRenderEventEvidenceCoordinator(
        object : BattleRenderEventEvidenceCoordinator.Port {
            override fun unitInputs() = battleRenderEventUnitInputs()
            override fun dialogueMarker() = battleDialogueMarkerInput()
            override fun dialogue() = battleDialogueRenderEventInput()
            override fun winConditions(route: BattleRenderEventProjectionWinRoute) =
                battleWinConditionsRenderEventInput(route)

            override fun itemUpgrade() = itemUpgradeRenderEventInput()
            override fun reward() = rewardRenderEventInput()
            override fun roundView() = activeRoundLayer?.view
            override fun usePropertyView() = BattleUsePropertyRenderEventView(
                route = usePropertyRouteState,
                rows = usePropertyLayer?.rows?.map { BattleUsePropertyRowView(it.name, it.typeName, it.count, it.icon) },
                detail = usePropertyDetail?.let { BattleUsePropertyDetailView(it.name, it.typeName, it.icon) },
                profile = usePropertyDetail?.let { selected ->
                    gameDataCatalog.equipmentProfile(selected.id)?.let { profile ->
                        BattleUsePropertyProfileView(gameDataCatalog.purchasePrice(profile), profile.intro)
                    }
                },
                postNames = (0 until 39).map(gameDataCatalog::postsName),
            )

            override fun magickView() = BattleMagickRenderEventView(
                route = magickRouteState,
                list = magickListLayer?.let { layer ->
                    BattleMagickListView(layer.rows.map { magic ->
                        BattleMagickRowView(magic.name, magic.cost, magic.power, magic.icon)
                    })
                },
                detail = magickInfoLayer?.magic?.let { magic ->
                    BattleMagickDetailView(magic.name, magic.cost, magic.power, magic.icon, magic.hit, magic.eff, magic.intro)
                },
            )

            override fun jiqiRates() = jiqiLayer?.rates?.toList()
        },
    )

    /** 전투 유닛 이벤트 입력: 진행 중인 애니메이션을 포함한 유닛 표시 상태를 값으로 고정한다. */
    private fun battleRenderEventUnitInputs(): List<BattleRenderEventProjectionUnitInput> {
        val visibleUnits = (battle.units.values + battle.presentation.pendingPresentationUnits().filter {
            it.hitPoints <= 0 || it.id in hitReactionAnimations ||
                it.id in deathAnimations || deathTimeline.containsPending(it.id)
        }).asSequence()
            .filter { it.visible }
            .filter { !battleInitRoute }
            .sortedWith(
                if (battleDialogueBlendRoute) {
                    val order = listOf(480, 483, 484, 146, 147, 481, 482, 485, 478, 479, 475, 476, 477, 235, 334, 474, 210, 234, 211)
                    compareBy<BattleUnit> { order.indexOf(it.characterId).let { index -> if (index < 0) 999 else index } }
                } else compareBy<BattleUnit> { visualTile(it).second }
            ).toList()
        val dialogueOrder = listOf(480, 483, 484, 146, 147, 481, 482, 485, 478, 479, 475, 476, 477, 235, 334, 474, 210, 234, 211)
        return visibleUnits.map { unit ->
            val frame = unitSpriteFrameResolver.frame(unit)
            val (visualX, visualY) = visualTile(unit)
            val healthVisible = !(sourceScenario == "S_00" && scriptedUnitPresentation.visual(unit.id)?.action == 4)
            BattleRenderEventProjectionUnitInput(
                sortOrder = if (battleDialogueBlendRoute) {
                    dialogueOrder.indexOf(unit.characterId).let { index -> if (index < 0) 999 else index }.toFloat()
                } else visualY,
                visualX = visualX,
                visualY = visualY,
                characterId = unit.characterId,
                atlasUuid = dynamicTextures.movementAtlasUuid(battleAvatarId(unit)),
                spriteSource = when (frame.source) {
                    UnitSpriteSource.MOVEMENT -> BattleRenderEventProjectionSpriteSource.MOVEMENT
                    UnitSpriteSource.ATTACK -> BattleRenderEventProjectionSpriteSource.ATTACK
                    UnitSpriteSource.SPECIAL -> BattleRenderEventProjectionSpriteSource.SPECIAL
                },
                sourceY = frame.sourceY,
                offsetX = frame.offsetX,
                offsetY = frame.offsetY,
                healthRatio = if (healthVisible) {
                    (healthTimeline.shownHp(unit.id, animationClock(), unit.hitPoints).toFloat() /
                        unit.maxHitPoints.coerceAtLeast(1)).coerceIn(0f, 1f)
                } else null,
                healthBarAsset = if (healthVisible) when (unit.type()) {
                    Faction.PLAYER -> "Mark_5-1"
                    Faction.FRIEND -> "Mark_3-1"
                    Faction.ENEMY, Faction.REINFORCEMENTS -> if (unit.famous) "Mark_2-1" else "Mark_68-1"
                } else null,
            )
        }
    }

    /** 대화 표식 이벤트 입력: 대화 연출에서 474번 유닛 타일에 표시할 표식을 고정한다. */
    private fun battleDialogueMarkerInput(): BattleRenderEventProjectionPoint? {
        if (!battleDialogueBlendRoute) return null
        val unit = battle.units.values.firstOrNull { it.visible && it.characterId == 474 } ?: return null
        val (visualX, visualY) = visualTile(unit)
        return BattleRenderEventProjectionPoint(visualX, visualY)
    }

    /** 대화 이벤트 입력: 현재 대화의 화상 번호·표시 본문·화자 이름을 캡처 값으로 고정한다. */
    private fun battleDialogueRenderEventInput(): BattleRenderEventProjectionDialogueInput? {
        if (!battleDialogueBlendRoute) return null
        val dialogue = requireNotNull(scriptRuntime.currentDialogue)
        val speaker = dialogue.speakerId?.toIntOrNull()?.let(gameDataCatalog::unitProfile)
        return BattleRenderEventProjectionDialogueInput(
            speaker?.face, dialogueReveal.visibleText, speaker?.name?.let(GameDataCatalog::sayLayerUnitName).orEmpty(),
        )
    }

    /** 승리 조건 이벤트 입력: 조건창 종류별 본문과 고정 하위 목표 문구를 캡처 값으로 고정한다. */
    private fun battleWinConditionsRenderEventInput(
        route: BattleRenderEventProjectionWinRoute,
    ): BattleRenderEventProjectionWinConditionsInput? = when (route) {
        BattleRenderEventProjectionWinRoute.COMPACT -> BattleRenderEventProjectionWinConditionsInput(requireNotNull(winConditionLayer).view().label, "")
        BattleRenderEventProjectionWinRoute.NONE -> null
        else -> requireNotNull(scriptWinConditions).view().let {
            BattleRenderEventProjectionWinConditionsInput(it.first, it.second, listOf("승리 조건", "장보와 장량을", "격퇴하십시오.", "제한 턴 수 " + scenarioMaxRound()))
        }
    }

    /** 장비 강화 이벤트 입력: 강화 흐름의 장비 정보와 능력치 전후 값을 캡처 값으로 고정한다. */
    private fun itemUpgradeRenderEventInput(): BattleRenderEventProjectionItemUpgradeInput? {
        val flow = outcomePresentation.itemUpgradeFlow ?: return null
        return BattleRenderEventProjectionItemUpgradeInput(
            gameDataCatalog.equipmentProfile(flow.request.itemId)?.icon ?: 1,
            flow.itemName, flow.request.newLevel, flow.ownerName, flow.attributeName,
            flow.request.oldValue, flow.request.newValue,
        )
    }

    /** 보상 이벤트 입력: 보상 공개 단계와 현재 노출된 항목만 캡처 값으로 고정한다. */
    private fun rewardRenderEventInput(): BattleRenderEventProjectionRewardInput? = outcomePresentation.rewardFlow?.let { flow ->
        when (flow.phase) {
            BattleRewardFlow.Phase.MONEY -> BattleRenderEventProjectionRewardInput(BattleRenderEventProjectionRewardPhase.MONEY, flow.reward.money, flow.reward.flag)
            BattleRewardFlow.Phase.ITEMS -> BattleRenderEventProjectionRewardInput(
                BattleRenderEventProjectionRewardPhase.ITEMS,
                items = flow.reward.itemIds.take(flow.visibleItemCount).take(3).map { id ->
                    gameDataCatalog.equipmentProfile(id).let { profile ->
                        BattleRenderEventProjectionRewardItemInput(profile?.icon ?: id, profile?.name ?: "아이템 " + id)
                    }
                },
            )
            BattleRewardFlow.Phase.END, BattleRewardFlow.Phase.COMPLETE -> BattleRenderEventProjectionRewardInput(BattleRenderEventProjectionRewardPhase.NONE)
        }
    }

    /** 라운드 입력을 coordinator가 기존 evidence recorder JSONL로 조립한다. */
    private fun roundRenderEventLog(): String = battleRenderEventEvidenceCoordinator().roundJsonl(roundRouteState)

    /** 소비 아이템 화면 입력을 coordinator가 기존 evidence recorder JSONL로 조립한다. */
    private fun usePropertyRenderEventLog(): String = battleRenderEventEvidenceCoordinator().usePropertyJsonl()

    /** 마법 화면 입력을 coordinator가 기존 evidence recorder JSONL로 조립한다. */
    private fun magickRenderEventLog(): String = battleRenderEventEvidenceCoordinator().magickJsonl()

    /** JiQi 화면 입력을 coordinator가 기존 evidence recorder JSONL로 조립한다. */
    private fun jiqiRenderEventLog(): String = battleRenderEventEvidenceCoordinator().jiqiJsonl()

    private fun installBattleCharacterRoute() {
        battleCharacterRouteFixtureCoordinator.install(battleCharacterRouteState, battleCharacterRouteFixturePort)
    }

    private fun battleCharacterRouteRenderEventLog(): String = battleCharacterRouteFixtureCoordinator.jsonl(
        requireNotNull(battleCharacterRouteState), battleCharacterRouteFixturePort,
    )

    private fun drawBattleCharacterRoute() {
        battleCharacterRouteFixtureCoordinator.drawSamples(battleCharacterRouteFixturePort).forEach { drawSample ->
            val sample = drawSample.sample
            val frame = drawSample.frame
            val commands = drawSample.commands
            val avatar = commands.firstOrNull() ?: return@forEach
            val texture = when (frame.source) {
                UnitSpriteSource.ATTACK -> attackTexture(sample.unit)
                    ?: unitTexture(sample.unit); UnitSpriteSource.SPECIAL -> specialTexture(sample.unit) ?: unitTexture(
                    sample.unit
                ); UnitSpriteSource.MOVEMENT -> unitTexture(sample.unit)
            }
            texture?.let { atlas ->

                fun spriteAt(x: Float, y: Float) = batch.draw(
                    atlas, x, y, avatar.width, avatar.height, 0,
                    if (frame.sourceY + frame.sourceHeight > atlas.height) 0 else frame.sourceY,
                    minOf(frame.sourceWidth, atlas.width), minOf(frame.sourceHeight, atlas.height), frame.flipX, false
                )
                when (sample.state.material) {
                    BattleCharacterMaterial.HIGHLIGHT -> {
                        batch.flush(); batch.shader =
                            cocosHighlightSampler.value; cocosHighlightSampler.value.setUniformf(
                            "u_value",
                            sample.state.materialValue ?: 0f
                        ); spriteAt(avatar.x, avatar.y); batch.flush(); batch.shader = null
                    }

                    BattleCharacterMaterial.OUTLINE -> {
                        batch.color = Color.CYAN; listOf(
                            -2f to 0f,
                            2f to 0f,
                            0f to -2f,
                            0f to 2f
                        ).forEach { (dx, dy) -> spriteAt(avatar.x + dx, avatar.y + dy) }; batch.color =
                            Color.WHITE; spriteAt(avatar.x, avatar.y)
                    }

                    else -> spriteAt(avatar.x, avatar.y)
                }
            }
            commands.drop(1).forEach { command ->
                when (command.drawType) {
                    "sliced-sprite" -> when (sample.state.camp) {
                        BattleCharacterCamp.MINE -> hudAssets.mineHpBarTexture; BattleCharacterCamp.FRIEND -> hudAssets.friendHpBarTexture; BattleCharacterCamp.ENEMY -> hudAssets.enemyHpBarTexture; BattleCharacterCamp.FAMOUS_ENEMY -> hudAssets.famousEnemyHpBarTexture
                    }?.let { batch.draw(it, command.x, command.y, command.width, command.height) }

                    "label" -> {
                        val outline = command.outlineRgb ?: 0; font.data.setScale(.5f); font.color = Color(
                            (outline shr 16 and 255) / 255f,
                            (outline shr 8 and 255) / 255f,
                            (outline and 255) / 255f,
                            1f
                        )
                        val baseline = command.y + command.height; listOf(
                            -1f to 0f,
                            1f to 0f,
                            0f to -1f,
                            0f to 1f
                        ).forEach { (dx, dy) ->
                            font.draw(
                                batch,
                                command.text.orEmpty(),
                                command.x + dx,
                                baseline + dy
                            )
                        }
                        val rgb = command.colorRgb ?: 0xffffff; font.color = Color(
                            (rgb shr 16 and 255) / 255f,
                            (rgb shr 8 and 255) / 255f,
                            (rgb and 255) / 255f,
                            1f
                        ); font.draw(
                            batch,
                            command.text.orEmpty(),
                            command.x,
                            baseline
                        ); font.data.setScale(1f); font.color = Color.WHITE
                    }
                }
            }
        }
    }

    private fun nextScenario(): String {
        scriptRuntime.stage.sceneJumpStage?.let { targetStage ->
            val target = targetStage / 2
            val routed = "R_${target.toString().padStart(2, '0')}"
            if (routed in ScenarioCatalog.rModuleNames()) return routed
        }
        val number = returnScenario.removePrefix("R_").toIntOrNull() ?: return returnScenario
        val candidate = "R_${(number + 1).toString().padStart(2, '0')}"
        return candidate.takeIf { it in ScenarioCatalog.rModuleNames() } ?: returnScenario
    }

    private fun fightPresentationView(): FightPresentationView {
        val snapshot = fightPresentation.renderSnapshot()


        fun identity(fighter: FightFighterSnapshot): FightUnitRenderIdentity {
            val characterId = fighter.characterId
                ?: return FightUnitRenderIdentity(
                    name = null,
                    introName = null,
                    portraitFaceId = null,
                    avatarId = null,
                )
            val profile = gameDataCatalog.unitProfile(characterId)
            return FightUnitRenderIdentity(
                name = profile?.name,
                introName = profile?.name ?: characterId.toString(),
                portraitFaceId = profile?.face?.plus(8),
                avatarId = if (fighter.created && fighter.action != null) fightAvatarId(characterId) else null,
            )
        }
        return FightPresentationViewBuilder.build(
            snapshot = snapshot,
            mineIdentity = identity(snapshot.mine),
            enemyIdentity = identity(snapshot.enemy),
        )
    }

    private fun fightAvatarId(characterId: Int): Int? =
        battle.units.values.firstOrNull { it.characterId == characterId }?.let(::battleAvatarId)
            ?: gameDataCatalog.unitProfile(characterId)?.battleAvatar

    private fun drawGrid() {
        configureSourceCameraViewport()
        boardLeft = SourceBattleMapGeometry.boardLeft(terrainGrid.width, battleCamera.x)
        boardBottom = if (rewardRouteState != null) 1264f + battleCamera.y else
            SourceBattleMapGeometry.boardBottom(terrainGrid.height, battleCamera.y)
        boardMaxX = (terrainGrid.width - 1).coerceAtLeast(1)
        boardMaxY = (terrainGrid.height - 1).coerceAtLeast(1)
        boardTile = 96f
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        battleGridMapSurfaceRenderer.draw(battleGridMapSurfaceView())
        if (mapOnlyCapture || dialogueComponentStage == RuntimeBattleRoute.DIALOGUE_COMPONENT_BACKGROUND) {
            batch.end()
            return
        }
        if (battleCharacterRouteState != null) {
            drawBattleCharacterRoute()
            batch.end()
            return
        }
        drawBattleGridActorLayer()
        batch.end()
        batch.begin()
        drawMagicEffect()
        batch.end()
    }
    private fun battleGridMapSurfaceView(): BattleGridRenderView {
        val useCocos8Sampler = game.requestedCocos8MapSampler()
        val useFragmentCoordinates = useCocos8Sampler && game.requestedFragmentCoordinateMapSampler()
        val cameraX = if (mapOnlyCapture || rewardRouteState != null) 0f else battleCamera.x
        val cameraY = if (mapOnlyCapture || rewardRouteState != null) 0f else battleCamera.y
        val mapLeft = if (mapOnlyCapture || rewardRouteState != null) -320f + cameraX else
            SourceBattleMapGeometry.boardLeft(terrainGrid.width, cameraX)
        val mapBottom = if (mapOnlyCapture || rewardRouteState != null) -560f + cameraY else
            SourceBattleMapGeometry.mapBottom(terrainGrid.height, cameraY)
        val mapWidth = terrainGrid.width * boardTile
        val mapHeight = terrainGrid.height * boardTile
        val (sampleOffsetX, sampleOffsetY) = game.requestedMapSampleOffset()
        val map = mapTexture?.let { texture ->
            BattleGridMapSurface(
                texture = texture,
                left = mapLeft,
                bottom = mapBottom,
                width = mapWidth,
                height = mapHeight,
                sampleOffsetX = sampleOffsetX,
                sampleOffsetY = sampleOffsetY,
                cocos8Sampler = if (useCocos8Sampler) cocos8MapSampler.value else null,
                fragmentCoordinates = useFragmentCoordinates,
                framebufferWorldWidth = viewport.worldHeight * Gdx.graphics.backBufferWidth.toFloat() /
                    Gdx.graphics.backBufferHeight.toFloat(),
                framebufferWorldHeight = viewport.worldHeight,
            )
        }
        val miniMap = if (mapOnlyCapture || dialogueComponentStage != null) null else BattleGridMiniMapView(
            shown = miniMapLayer.shown,
            framePatch = hudAssets.menuFramePatch,
            boxPatch = hudAssets.menuBoxPatch,
            mapTexture = hudAssets.naturalMiniMapTexture,
            weatherTexture = hudAssets.naturalWeatherTexture,
            markers = MiniMapRenderEvents.yingchuanMarkers.mapNotNull { item ->
                hudAssets.naturalMiniMapMarkerTextures[item.asset]?.let { marker ->
                    BattleGridMiniMapMarker(marker, item.x, item.y)
                }
            },
        )
        return BattleGridRenderView(map, miniMap)
    }
    private fun drawBattleGridActorLayer() {
        val mapObjectView = battleMapObjectRenderView()
        battleMapObjectRenderer.drawGates(mapObjectView)
        val selectAreaTiles = selectableAreaTiles()
        val actorLayerUnits = battle.units.values + battle.presentation.pendingPresentationUnits().filter {
            it.hitPoints <= 0 || it.id in hitReactionAnimations ||
                    it.id in deathAnimations || deathTimeline.containsPending(it.id)
        }
        val visibleUnits = BattleActorLayerProjector.visibleSourceIndexes(
            candidates = actorLayerUnits.mapIndexed { index, unit ->
                BattleActorLayerCandidate(index, unit.characterId, unit.tileY, unit.visible)
            },
            dialogueBlendRoute = battleDialogueBlendRoute,
        ).map(actorLayerUnits::get)
        val mapView = battleMapView(selectAreaTiles, visibleUnits)
        battleMapRenderer.drawSelection(mapView)
        battleMapObjectRenderer.drawAnimatedObjects(mapObjectView)
        font.color = Color.WHITE
        battleMapRenderer.drawTerrainImpacts(mapView)
        val actorView = battleActorEffectRenderView(visibleUnits)
        battleActorEffectRenderer.drawActors(actorView)
        battleMapRenderer.drawHarmNumbers(mapView)
        battleActorEffectRenderer.drawSayMarker(actorView)
    }

    /** 맵 오브젝트 렌더 뷰: 현재 활성 게이트·화염·선택 오브젝트와 화면 좌표를 renderer 입력으로 고정한다. */
    private fun battleMapObjectRenderView(): BattleMapObjectRenderView = BattleMapObjectRenderView(
        boardLeft = boardLeft,
        boardBottom = boardBottom,
        tileSize = boardTile,
        animationClock = mapObjectAnimationClock(),
        gates = scriptRuntime.stage.mapObjects.values
            .filter { it.enabled && it.objectId > 3 }
            .mapNotNull { gate ->
                dynamicTextures.gate(gate.objectId)?.let { texture ->
                    BattleMapGateRender(gate.objectId, gate.x, gate.y, texture)
                }
            },
        fires = scriptRuntime.stage.fires.values
            .filter { it.enabled }
            .map { fire -> BattleMapFireRender(fire.x, fire.y) },
        objects = scriptRuntime.stage.mapObjects.values
            .filter { it.enabled && it.objectId in 0..2 }
            .map { objectState -> BattleMapAnimatedObjectRender(objectState.objectId, objectState.x, objectState.y) },
        objectTexture = hudAssets.fireTexture,
    )
    /** Actor/effect renderer view는 composer에 위임하고 Screen은 현재 live-state adapter만 제공한다. */
    private fun battleActorEffectRenderView(visibleUnits: List<BattleUnit>): BattleActorEffectRenderView =
        battleActorEffectViewComposer.compose(visibleUnits)
    private fun playPendingMagicEffectSounds() {
        val now = animationClock()
        magicEffectAnimations.filter { !it.soundPlayed && now >= it.startedAt }.forEach { animation ->
            val effect = magicEffects.effect(animation.effectId) ?: return@forEach
            repeat(animation.targetIds.size) { audio.playBattleEffect(100 + effect.soundId) }
            animation.soundPlayed = true
        }
        magicEffectAnimations.removeAll { now >= it.endsAt }
    }
    private fun drawMagicEffect() {
        battleActorEffectRenderer.drawEffects(battleActorEffectRenderView(emptyList()))
    }
    private fun tileBottom(sourceY: Int): Float = tileBottom(sourceY.toFloat())
    private fun tileBottom(sourceY: Float): Float = boardBottom - sourceY * boardTile

    /** 선택 가능 타일 계산: AI 연출·마법·아이템·일반 명령 상태에 맞는 선택 테두리 목록을 만든다. */
    private fun selectableAreaTiles(): List<SelectAreaTile> {
        aiPresentation.resolution?.let { resolution ->
            when (aiPresentation.stage) {
                AiPresentationStage.FOCUS_DELAY -> return resolution.moveArea
                    .filter { (x, y) -> x in 0..boardMaxX && y in 0..boardMaxY }
                    .map { (x, y) -> SelectAreaTile(x, y, SelectAreaFrame.GREEN) }

                AiPresentationStage.ACTION_DELAY -> return resolution.actionArea
                    .filter { (x, y) -> x in 0..boardMaxX && y in 0..boardMaxY }
                    .map { (x, y) -> SelectAreaTile(x, y, SelectAreaFrame.RED) }

                else -> Unit
            }
        }
        val selected = selectedUnitId?.let(battle.units::get) ?: return emptyList()
        return when {
            magicMode -> selected.magic.getOrNull(selectedMagicIndex)
                ?.hitArea
                ?.offsets
                ?.map { (dx, dy) -> selected.tileX + dx to selected.tileY + dy }
                ?.filter { (x, y) -> x in 0..boardMaxX && y in 0..boardMaxY }
                ?.map { (x, y) -> SelectAreaTile(x, y, SelectAreaFrame.RED) }
                .orEmpty()

            propertyMode -> {
                val range = listOf(0 to 0, 1 to 0, -1 to 0, 0 to 1, 0 to -1)
                    .map { (dx, dy) -> selected.tileX + dx to selected.tileY + dy }
                    .filter { (x, y) -> x in 0..boardMaxX && y in 0..boardMaxY }
                    .map { (x, y) -> SelectAreaTile(x, y, SelectAreaFrame.RED) }
                val targets = battle.units.values.asSequence()
                    .filter { it.visible && unitsAreAllied(selected, it) }
                    .filter { candidate ->
                        val offset = candidate.tileX - selected.tileX to candidate.tileY - selected.tileY
                        offset == (0 to 0) || offset in setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
                    }
                    .map { SelectAreaTile(it.tileX, it.tileY, SelectAreaFrame.GREEN_BOX) }
                    .toList()
                range + targets
            }
            else -> {
                val moveFrame = if (selected.type() == Faction.PLAYER) SelectAreaFrame.BLUE else SelectAreaFrame.GREEN
                val moveTiles = battle.movement.reachableTiles(selected.id).keys.map { (x, y) ->
                    SelectAreaTile(x, y, moveFrame)
                }
                val attackTiles = if (selected.attackAllScreen) {
                    (0..boardMaxX).flatMap { x ->
                        (0..boardMaxY).map { y -> SelectAreaTile(x, y, SelectAreaFrame.RED) }
                    }
                } else selected.attackOffsets.mapNotNull { (dx, dy) ->
                    val x = selected.tileX + dx
                    val y = selected.tileY + dy
                    SelectAreaTile(x, y, SelectAreaFrame.RED_BOX)
                        .takeIf { x in 0..boardMaxX && y in 0..boardMaxY }
                }
                moveTiles + attackTiles
            }
        }
    }

    private fun battleMapView(
        selectionTiles: List<SelectAreaTile>,
        visibleUnits: List<BattleUnit>,
    ): BattleMapView {
        val selected = selectedUnitId?.let(battle.units::get)
        val terrainImpacts = if (magicMode || propertyMode || selected == null) {
            emptyList()
        } else {
            battle.movement.reachableTiles(selected.id).keys.map { (x, y) ->
                BattleMapTerrainImpact(x, y, selected.terrainImpacts[terrainGrid.terrainAt(x, y)] ?: 100)
            }
        }
        val now = animationClock()
        val harmNumbers = harmNumberAnimations.mapNotNull { (unitId, animation) ->
            if (now < animation.startedAt || now >= animation.endsAt) return@mapNotNull null
            val unit = visibleUnits.firstOrNull { it.id == unitId } ?: return@mapNotNull null
            val (visualX, visualY) = visualTile(unit)
            BattleMapHarmNumber(visualX, visualY, animation.amount, animation.isHp)
        }
        return BattleMapView(
            boardLeft = boardLeft,
            boardBottom = boardBottom,
            tileSize = boardTile,
            selectionTiles = selectionTiles.map { BattleMapSelection(it.x, it.y, it.frame.assetName) },
            cursor = selected?.let { BattleMapCursor(it.tileX, it.tileY) },
            terrainImpacts = terrainImpacts,
            harmNumbers = harmNumbers,
        )
    }
    private fun unitsAreAllied(left: BattleUnit, right: BattleUnit): Boolean =
        left.isPlayerSide() == right.isPlayerSide()

    private fun drawHud() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        font.color = Color(1f, 0.85f, 0.48f, 1f)
        font.draw(batch, "원본 전술 전투 · $sourceScenario", 80f, 680f)
        font.color = Color.WHITE
        font.draw(
            batch,
            "라운드 ${battle.round} · ${battle.activeFaction.label()} 차례 · ${battle.weather.label()}",
            80f,
            638f
        )
        font.draw(batch, eventMessage, 80f, 94f)
        font.color = Color(0.72f, 0.80f, 0.90f, 1f)
        font.draw(batch, "클릭: 선택/이동/공격 · M: 전략 · B: 아이템 · T: 턴 종료 · Esc: 돌아가기", 520f, 52f)
        batch.end()
    }
    private fun drawBattleHudChrome() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        hudAssets.battleEndTurnTexture?.let { batch.draw(it, 1353.9535f, 8f, 60f, 60f) }
        if (battleDialogueBlendRoute) {
            batch.end()
            return
        }
        hudAssets.battleButtonBackgroundPatch?.draw(batch, 0.843f, 0.731f, 68f, 68f)
        hudAssets.battleRecordTexture?.let { batch.draw(it, 0.043f, -0.069f, 69.6f, 69.6f) }
        val miniButtonX = if (miniMapLayer.shown) 1174.3721f else 1418.3721f
        hudAssets.battleButtonBackgroundPatch?.draw(batch, miniButtonX, 730f, 70f, 70f)
        hudAssets.battleMenuTexture?.let { batch.draw(it, miniButtonX + .2f, 730.2f, 69.6f, 69.6f) }
        batch.end()
    }
    private fun drawBattleMenu() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        hudAssets.menuBackgroundPatch?.draw(batch, 0f, 0f, 1488.3721f, 212f)
        hudAssets.menuFramePatch?.draw(batch, 0f, 0f, 1488.3721f, 212f)
        // bg0와 progressBar는 원본 y=58에 있는 두 개의 304×44 프레임이다.
        hudAssets.menuBoxPatch?.draw(batch, 41f, 36f, 304f, 44f)
        hudAssets.menuBoxPatch?.draw(batch, 425f, 36f, 304f, 44f)
        hudAssets.menuTitleBarTexture?.let { batch.draw(it, 43f, 38f, 300f, 40f) }
        val menu = battleMenuLayer?.view()
        hudAssets.menuProgressBarTexture?.let { batch.draw(it, 427f, 38f, 300f * (menu?.progress ?: 0f), 40f) }
        menu?.let { view ->
            val sheet = MenuLayer.weatherSheet(view.weather)
            val frame = MenuLayer.weatherFrameAt(elapsed - battleMenuOpenedAt)
            hudAssets.menuWeatherTextures[sheet]?.getOrNull(frame)?.let { weather ->
                batch.draw(weather, 832.232f, 8f, 432f, 100f)
            }
        }
        dialogueFont.color = Color.BLACK
        dialogueFont.data.setScale(30f / 36f)
        dialogueFont.draw(
            batch,
            menu?.battleName ?: gameDataCatalog.battleName(scriptRuntime.stage.battleMapIndex),
            124f,
            69f
        )
        dialogueFont.draw(batch, "턴 수", 430f, 69f)
        dialogueFont.draw(batch, "${menu?.round ?: battle.round} / ${menu?.maxRound ?: scenarioMaxRound()}", 692f, 69f)
        dialogueFont.data.setScale(30f / 36f)
        // MenuLayer/bg/contain에는 중앙 정렬된 88×88 작성 버튼 13개가 있다.
        val sourceIndexes = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        sourceIndexes.forEach { index ->
            val x = 15.13372f + index * 88f
            hudAssets.menuButtonPatch?.draw(batch, x, 116.29f, 88f, 88f)
            // MenuLayer의 tool1 노드는 48×48에 scale=(1.5,1.5)이며 88×88 버튼 중앙에 있다.
            // 원본 노드 변환을 유지한다.
            hudAssets.menuToolTextures[index]?.let { batch.draw(it, x + 8f, 124.572f, 72f, 72f) }
        }
        hudAssets.menuButtonPatch?.draw(batch, 1071.1337f, 116.29f, 88f, 88f)
        hudAssets.menuHelpTexture?.let { batch.draw(it, 1079.1337f, 124.29f, 72f, 72f) }
        dialogueFont.data.setScale(1f)
        dialogueFont.color = Color.WHITE
        batch.end()
    }

    private fun drawTreasureLayer(view: BattleTreasureOverlayView) {
        // TreasureLayer/bg1의 중심은 (744.186,400), 크기는 970×632이다.
        val x = 259f
        val y = 84f
        val width = 970f
        val height = 632f
        val sourceLabelScale = 40f / 26f
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        overlayAssets.terrainLayerBackgroundTexture?.let { texture ->
            val tile = 96f
            var ty = y
            while (ty < y + height) {
                var tx = x
                while (tx < x + width) {
                    batch.draw(texture, tx, ty, minOf(tile, x + width - tx), minOf(tile, y + height - ty))
                    tx += tile
                }
                ty += tile
            }
        }
        font.color = Color.BLACK
        font.data.setScale(sourceLabelScale)
        font.draw(batch, "보물 도감", x + 7f, y + 618f)
        view.rows.drop(view.firstRow).take(4).forEachIndexed { index, row ->
            val column = index % 2
            val line = index / 2
            // 원본 재사용 아이템 프리팹은 box3 471×190이며 다음 위치를 중심으로 둔다.
            val cardX = 270f + column * 477f
            val cardY = 671f - line * 193f
            overlayAssets.terrainLayerPanelPatch?.draw(batch, cardX, cardY - 190f, 471f, 190f)
            overlayAssets.terrainLayerPanelPatch?.draw(batch, cardX + 12f, cardY - 112f, 90f, 90f)
            font.color = if (row.selected) Color(0.05f, 0.35f, 0.95f, 1f) else Color.BLACK
            if (row.discovered) {
                row.icon?.let { batch.draw(it, cardX + 12f, cardY - 112f, 90f, 90f) }
                font.draw(batch, row.name, cardX + 134f, cardY - 25f)
            } else {
                font.draw(batch, "발견되지 않음", cardX + 134f, cardY - 25f)
            }
        }
        font.data.setScale(sourceLabelScale)
        font.color = Color.BLACK
        font.draw(
            batch,
            view.title,
            x + 7f,
            141f
        )
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 1071f, 91f, 151f, 52f)
        font.draw(batch, "종료", 1100f, 119f)
        font.color = Color.WHITE; font.data.setScale(1f); batch.end()
    }

    private fun handleSaveLoadEffect(effect: BattleSaveLoadOverlayController.Effect) {
        if (effect !is BattleSaveLoadOverlayController.Effect.Closed || effect.mode != BattleSaveLoadOverlayController.Mode.SAVE) return
        if (effect.saved) eventMessage = "진행 상황을 저장했습니다."
        if (outcomePresentation.postBattleSaveLayer) outcomePresentation.finishVictoryRoute()
    }

    private fun handleForcesOverlayEffect(effect: BattleForcesOverlayController.Effect) {
        val selected = effect as? BattleForcesOverlayController.Effect.UnitSelected ?: return
        val unit = selected.unit
        selectedUnitId = battle.units.values.firstOrNull { it.characterId == unit.characterId }?.id
        openUnitInfoLayer(unit.characterId)
        eventMessage = "${unit.name} · ${unit.post} · Lv${unit.level} · HP ${unit.hp}/${unit.maxHp}"
    }

    private fun handleUnitInfoOverlayEffect(effect: BattleUnitInfoOverlayController.Effect) {
        if (effect is BattleUnitInfoOverlayController.Effect.JiqiOpened) jiqiLayer = effect.layer
    }

    private fun menuIndexAt(x: Float, y: Float): Int? {
        if (y !in 116.29f..204.29f || x !in 15.13372f..1159.1337f) return null
        val visualSlot = ((x - 15.13372f) / 88f).toInt()
        return if (visualSlot >= 12) 13 else visualSlot
    }

    private fun closeBattleMenu() {
        battleMenuLayer?.onCancel(MenuLayer.TOUCH_END)
        battleMenuLayer = null
        battleMenuOpen = false
    }

    private fun handleBattleMenuTap(index: Int) {
        val command = MenuLayer.Command.entries[index]
        if (battleMenuLayer?.onCommand(command, MenuLayer.TOUCH_END) == null) return
        battleMenuOpen = false
        battleMenuLayer = null
        when (index) {
            0 -> game.showTitleScreen() // JSYX: 원본 분기에서 타이틀 화면으로 복귀한다.
            1 -> { // CD: SaveLayer; SAVE_GAME is dispatched only after MsgBox OK.
                saveLoadOverlay.openSave(savedPage = 0)
            }

            2 -> { // DD: LoadGameLayer.onCreate → SAVE_PAGE → _refPage
                saveLoadOverlay.openLoad()
            }

            3 -> {
                settingsOverlay.open()
            } // XTSZ → SettingLayer
            4 -> openForcesListLayer() // WJYL: SHOW_CHARACTER_LIST → ForcesListLayer
            5 -> { // DJYL: PropertyLayer
                informationOverlay.openProperty()
            }

            6 -> { // DX: TerrainLayer
                informationOverlay.openTerrain()
            }

            7 -> { // BW → TreasureLayer; onCreate always starts at ScrollView top.
                informationOverlay.openTreasure()
            }

            8 -> if (battle.outcome() == null) autoBattleFlow.openEndRoundPrompt() // HHJS: END_ROUND -> MsgBox4
            9 -> openWinConditionBox() // SLTJ: BattleScreen WIN_CONDITION → WinConBoxLayer
            10 -> Unit // XDT: 원본 분기에서 의도적으로 아무 동작도 하지 않는다.
            11 -> focusNextNoActionUnit() // JSWCZBD: NOACTION_INDEX
            12 -> eventMessage = "전투 중 편집 기능은 원본과 동일하게 개발 기능이 활성화된 경우에만 사용할 수 있습니다."
            13 -> openHelperLayer() // HELP → Global/scene/HelperLayer
        }
    }

    private fun openBattleMenu() {
        battleMenuLayer = MenuLayer().also { it.onCreate(menuCreateData()) }
        battleMenuOpenedAt = elapsed
        battleMenuOpen = true
    }

    private fun battleAutoOverlayView(): BattleAutoOverlayView {
        val state = autoBattleFlow.view()
        val overlay = when (state.overlay) {
            AutoBattleFlow.Overlay.NONE -> BattleAutoOverlayKind.NONE
            AutoBattleFlow.Overlay.PROMPT -> BattleAutoOverlayKind.PROMPT
            AutoBattleFlow.Overlay.TUOGUAN -> BattleAutoOverlayKind.TUOGUAN
        }
        return BattleAutoOverlayView(overlay = overlay, checked = state.checked)
    }

    private fun autoBattlePromptButtonAt(x: Float, y: Float): Int? = when {
        x in 844.536f..994.536f && y in 270.197f..320.197f -> 0
        x in 674.536f..824.536f && y in 270.197f..320.197f -> 1
        else -> null
    }

    private fun autoBattleToggleAt(x: Float, y: Float): Boolean =
        x in 518.416f..640.457f && y in 267.997f..322.397f

    private fun answerAutoBattle(tag: Int) {
        val before = autoBattleFlow.view().endRoundRequests
        if (!autoBattleFlow.answer(tag, AutoBattleFlow.TOUCH_END)) return
        if (battleTraceCoordinator == null) {
            autoBattlePreferences.putInteger("TUOGUAN", if (autoBattleFlow.view().stored) 1 else 0).flush()
        }
        if (autoBattleFlow.view().endRoundRequests != before) {
            selectedUnitId = null
            if (autoBattleFlow.view().collocation) {
                if (!turnController.runCollocatedPlayerTurn()) {
                    eventMessage = "위임 전투를 시작할 수 없습니다."
                }
            } else {
                endTurn()
            }
        }
    }
    private fun installAutoBattleRouteFixture() {
        autoBattleRouteFixtureController.install(
            autoBattleRouteState,
            object : BattleAutoBattleRouteFixtureController.Commands {
                override fun openBattleMenu() {
                    this@BattleScreen.openBattleMenu()
                }

                override fun tapAutoBattleMenu() {
                    this@BattleScreen.handleBattleMenuTap(8)
                }

                override fun view(): AutoBattleFlow.View = autoBattleFlow.view()

                override fun togglePrompt() {
                    this@BattleScreen.autoBattleFlow.toggle()
                }

                override fun confirmPrompt() {
                    this@BattleScreen.answerAutoBattle(0)
                }
            },
        )
    }

    /** 자동 전투 증거: 현재 자동 전투 경로와 표시 상태를 전용 기록기에 전달한다. */
    private fun autoBattleRenderEventLog(): String = BattleAutoRenderEventRecorder.jsonl(
        BattleAutoRenderEventInput(requireNotNull(autoBattleRouteState), autoBattleFlow.view()),
    )

    private fun menuCreateData() = MenuLayer.CreateData(
        weather = when (battle.weather) {
            BattleWeather.CLEAR -> MenuLayer.Weather.QING
            BattleWeather.CLOUDY -> MenuLayer.Weather.YIN
            BattleWeather.WINDY -> MenuLayer.Weather.FENG
            BattleWeather.HEAVY_RAIN -> MenuLayer.Weather.HAO_YU
            BattleWeather.SNOW -> MenuLayer.Weather.XUE
        },
        round = battle.round,
        maxRound = scenarioMaxRound(),
        battleName = gameDataCatalog.battleName(scriptRuntime.stage.battleMapIndex),
        editEnabled = false,
    )

    private fun openHelperLayer() {
        val model = object : HelperLayer.Model {
            override fun getInfo(): Iterable<HelperLayer.Info> {
                val live = campaign.extraInfo.map { HelperLayer.Info(it.type, it.reserved, it.text) }
                if (live.isNotEmpty()) return live
                val source = Gdx.files.internal("scenarios/R_00.py")
                    .takeIf { it.exists() }?.readString("UTF-8").orEmpty()
                val guide = Regex("stage\\.info\\('6(.*?)'\\)", setOf(RegexOption.DOT_MATCHES_ALL))
                    .find(source)?.groupValues?.getOrNull(1)
                    ?.replace("\\n", "\n")
                    ?.let { "6$it" }
                    ?.takeIf { it.isNotBlank() }
                return guide?.let { listOf(HelperLayer.Info(1, text = it)) }.orEmpty()
            }

            override fun replaceSpeInfo(text: String, flags: Int): String = SourceInfoText.replace(
                text, flags,
                unitName = { id -> gameDataCatalog.unitProfile(id)?.name.orEmpty() },
                global = { id -> (campaign.globalVariables[id] as? Number)?.toInt() ?: 0 },
                colors = List(0x3b) { index ->
                    when (index) {
                        0x28 -> "#c30000"
                        0x3a -> "#0000ab"
                        else -> ""
                    }
                },
            )
        }
        helperOverlay.open(model)
    }

    private fun openForcesListLayer() {

        fun asSource(unit: BattleUnit): ForcesListLayer.Unit {

            fun liftStatus(attribute: BattleAttribute) = when {
                (unit.attributeLifts[attribute] ?: 0) < 0 -> 0
                (unit.attributeLifts[attribute] ?: 0) > 0 -> 1
                else -> -1
            }
            return ForcesListLayer.Unit(
                id = unit.characterId ?: 0, name = unit.name, post = "부대", level = unit.level,
                hp = unit.hitPoints, maxHp = unit.maxHitPoints, mp = unit.magicPoints, maxMp = unit.maxMagicPoints,
                attack = unit.attack, defense = unit.defense, spirit = unit.spirit, critical = unit.critical,
                morale = unit.morale, famous = unit.famous,
                status = mapOf(
                    0 to liftStatus(BattleAttribute.ATTACK),
                    1 to liftStatus(BattleAttribute.DEFENSE),
                    2 to liftStatus(BattleAttribute.SPIRIT),
                    3 to liftStatus(BattleAttribute.CRITICAL),
                    4 to liftStatus(BattleAttribute.MORALE),
                ),
            )
        }

        val mine = battle.units.values.filter { it.visible && it.isPlayerSide() }.map(::asSource)
        val enemy = battle.units.values.filter { it.visible && it.type().isEnemySide() }.map(::asSource)
        forcesOverlay.open(mine, enemy, 1)
        eventMessage = "부대 정보 일람: 아군/적군 탭을 선택할 수 있습니다."
    }

    /** 유닛 정보 창 열기: 선택 유닛과 같은 진영의 표시 행을 만들고 정보 오버레이를 활성화한다. */
    private fun openUnitInfoLayer(selectedCharacterId: Int) {
        val source = battle.units.values.filter { it.visible }


        fun row(u: BattleUnit) = UnitInfoLayer.Unit(
            u.characterId ?: 0,
            u.name,
            "부대",
            u.level,
            u.hitPoints,
            u.maxHitPoints,
            u.magicPoints,
            u.maxMagicPoints,
            u.attack,
            u.defense,
            u.spirit,
            u.critical,
            u.morale,
            u.magic.map { it.name })

        val rows = source.map(::row)
        val index = rows.indexOfFirst { it.id == selectedCharacterId }.coerceAtLeast(0)
        unitInfoOverlay.open(rows, index)
    }

    private fun toMagicUi(profile: BattleMagicProfile) = MagicUiList.Magic(
        profile.id, profile.name, profile.expendMp, profile.power, profile.icon,
        profile.hitArea.id, profile.effectAreaId, profile.intro,
    )

    private fun openMagickList(unit: BattleUnit) {
        val rows = unit.magic.map(::toMagicUi)
        magickListLayer = MagicUiList(unit.magicPoints, unit.maxMagicPoints, rows, emptyMap())
        magickInfoLayer = null
        magickPressedRow = null
    }
    private fun installBattleCommandRouteFixture() {
        battleCommandRouteFixture.install(
            route = battleCommandRouteState,
            units = battle.units.values.map { unit ->
                BattleCommandRouteFixtureController.Unit(
                    id = unit.id,
                    faction = unit.faction,
                    visible = unit.visible,
                    hasMagic = unit.magic.isNotEmpty(),
                )
            },
            activeFaction = battle.activeFaction,
            commands = object : BattleCommandRouteFixtureController.Commands {
                override fun clearInventory() {
                    campaign.inventory.removeItemStack(150)
                    campaign.inventory.removeItemStack(151)
                }

                override fun seedInventory() {
                    campaign.inventory.addItem(150, 3)
                    campaign.inventory.addItem(151, 2)
                }

                override fun openCommand(unit: BattleCommandRouteFixtureController.Unit) {
                    val battleUnit = battle.units.getValue(unit.id)
                    if (battleUnit.faction != battle.activeFaction) battle.selectVerificationFaction(battleUnit.faction)
                    battleUnit.hasActed = false
                    battleUnit.hasMoved = false
                    selectedUnitId = null
                    handleTileClick(battleUnit.tileX, battleUnit.tileY)
                    check(selectedUnitId == battleUnit.id) { "Battle command actual route did not select unit" }
                    handleTileClick(battleUnit.tileX, battleUnit.tileY)
                    check(battleCommandFlow.phase == BattleCommandFlow.Phase.COMMAND) { "unitMove did not open CommandLayer" }
                }

                override fun cancelCommand() = dispatchBattleCommand(6)

                override fun openMagicCommand() {
                    dispatchBattleCommand(1)
                    magickListLayer = MagicUiList(
                        42,
                        42,
                        listOf(MagicUiList.Magic(0, "작열", 6, 70, 1, 0, 0, "")),
                        emptyMap(),
                    )
                }

                override fun openPropertyCommand() = dispatchBattleCommand(2)
            },
        )
    }

    private fun drawBattleCommandLayer() {
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, BattleCommandRenderModel.DISMISS_DIM_OPACITY); shapes.rect(
            0f,
            0f,
            1488.372f,
            800f
        )
        shapes.end()
        batch.projectionMatrix = viewport.camera.combined; batch.begin()
        batch.color = Color(1f, 1f, 1f, BattleCommandRenderModel.PANEL_OPACITY)
        for (ty in 0..3) for (tx in 0..4) {
            val width = minOf(96f, 397.2f - tx * 96f)
            val height = minOf(96f, 322.5f - ty * 96f)
            if (width > 0f && height > 0f) batch.draw(
                unitInfoAssets.unitInfoLogo,
                736f + tx * 96f,
                96f + ty * 96f,
                width,
                height
            )
        }
        batch.color = Color.WHITE; NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(
            batch,
            736f,
            96f,
            397.2f,
            322.5f
        )
        val labels = listOf("공격", "마법", "아이템", "교환", "포위 공격", "대기", "취소")
        itemUpgradeFont.data.setScale(40f / 26f)
        battleCommandFlow.view().forEachIndexed { index, button ->
            val visual = BattleCommandRenderModel.visuals[index]
            NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(
                batch,
                visual.x,
                visual.y,
                visual.width,
                visual.height
            )
            itemUpgradeFont.color = if (button.interactable) Color.BLACK else Color(
                BattleCommandRenderModel.DISABLED_COMPONENT,
                BattleCommandRenderModel.DISABLED_COMPONENT,
                BattleCommandRenderModel.DISABLED_COMPONENT,
                1f
            )
            itemUpgradeFont.draw(batch, labels[index], visual.labelX, visual.labelY + 40f, 100f, Align.center, false)
            val iconColor = if (button.interactable) Color.WHITE else Color(
                BattleCommandRenderModel.DISABLED_COMPONENT,
                BattleCommandRenderModel.DISABLED_COMPONENT,
                BattleCommandRenderModel.DISABLED_COMPONENT,
                1f
            )
            visual.icons.forEach { icon ->
                val iconIndex = icon.asset.removePrefix("command").toInt()
                batch.color = iconColor
                batch.draw(hudAssets.battleCommandIcons.getValue(iconIndex), icon.x, icon.y, icon.width, icon.height)
            }
            batch.color = Color.WHITE
        }
        itemUpgradeFont.data.setScale(1f); itemUpgradeFont.color = Color.WHITE; batch.color = Color.WHITE; batch.end()
    }

    /** 명령 화면 증거: 현재 자동 실행 경로를 전용 기록기에 전달한다. */
    private fun battleCommandRenderEventLog(): String =
        BattleCommandRenderEventRecorder.jsonl(requireNotNull(battleCommandRouteState))
    private fun showRoundCard(round: Int?, max: Int?, complete: () -> Unit) {
        activeRoundLayerElapsed = 0f
        activeRoundLayer = RoundLayer(
            remove = { activeRoundLayer = null },
            complete = complete,
        ).apply { onCreate(round, max) }
    }

    private fun installRoundRouteFixture() {
        roundRouteInstalled = true
        BattleRouteFixtureController.roundCard(roundRouteState, battle.maxRounds).let { card ->
            showRoundCard(card.round, card.maxRounds) { roundRouteCallbackCount++ }
        }
    }

    /** 미니맵 초기화: 날씨·맵 크기·현재 가시 유닛 마커를 한 번만 미니맵 레이어에 등록한다. */
    private fun initializeMiniMap() {
        if (miniMapReady) return
        miniMapLayer.onCreate(weather = 0, initialPoolNodes = 1)
        miniMapLayer.load(120, 120)
        battle.units.values.filter { it.visible }.forEachIndexed { index, unit ->
            miniMapLayer.visible(
                id = index,
                camp = when (unit.type()) {
                    Faction.PLAYER -> "mine"
                    Faction.FRIEND -> "friend"
                    Faction.ENEMY -> "enemy"
                    Faction.REINFORCEMENTS -> "enemy"
                },
                action = "normal",
                status = "normal",
                famous = unit.famous,
                x = unit.tileX,
                y = unit.tileY,
            )
        }
        miniMapReady = true
    }
    private fun installMiniMapRouteFixture() {
        miniMapRouteInstalled = true
        initializeMiniMap()
        BattleRouteFixtureController.applyMiniMap(miniMapRouteState, miniMapLayer)
    }

    private fun miniMapButtonAt(x: Float, y: Float): Boolean {
        if (y !in 730f..800f) return false
        val left = if (miniMapLayer.shown) 1174.372f else 1418.372f
        return x in left..(left + 70f)
    }

    private fun drawRoundLayer(layer: RoundLayer) {
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color = Color(0f, 0f, 0f, 80f / 255f); shapes.rect(
            0f,
            0f,
            1488.372f,
            800f
        ); shapes.end()
        batch.projectionMatrix = viewport.camera.combined; batch.begin()
        font.data.setScale(120f / 26f)

        fun text(value: String, x: Float, y: Float, width: Float, color: Color) {
            font.color = color; font.draw(batch, value, x, y + 125f, width, Align.center, false)
        }
        if (layer.view.roundLabelsVisible) {
            text("아군 단계", 526.713f, 380.09f, 448.54f, Color.RED)
            text("아군 단계", 519.916f, 385.09f, 448.54f, Color.WHITE)
            val width = if (layer.view.roundText == "최종 턴") 344.74f else 274.34f
            val x = if (layer.view.roundText == "최종 턴") 578.613f else 613.813f
            text(layer.view.roundText, x, 247.7f, width, Color(1f, .5f, .5f, 1f))
            text(layer.view.roundText, x - 6.797f, 252.7f, width, Color.WHITE)
        } else {
            text("적군 단계", 526.713f, 319.4f, 448.54f, Color.RED)
            text("적군 단계", 519.916f, 324.4f, 448.54f, Color.WHITE)
        }
        font.data.setScale(1f); font.color = Color.WHITE; batch.end()
    }

    private fun propertyEffectName(profile: GameDataCatalog.EquipmentProfile): String = when (profile.itemType) {
        26 -> "HP 회복"
        else -> gameDataCatalog.equipmentTypeName(profile.itemType)
    }

    private fun usePropertyRows(): List<UsePropertyLayer.Property> = usableProperties().mapNotNull { item ->
        gameDataCatalog.equipmentProfile(item.id)?.let { profile ->
            UsePropertyLayer.Property(
                profile.id,
                profile.name,
                propertyEffectName(profile),
                campaign.inventory.items[profile.id] ?: 0,
                profile.icon
            )
        }
    }

    private fun openUsePropertyLayer() {
        val rows = usePropertyRows()
        if (rows.isEmpty()) return
        usePropertyDetail = null
        usePropertyLayer = UsePropertyLayer(
            rows,
            onSelect = { selected ->
                if (selected != null) {
                    if (battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION) battleCommandFlow.childCompleted(
                        true
                    )
                    selectedPropertyIndex = usableProperties().indexOfFirst { it.id == selected.id }.coerceAtLeast(0)
                    propertyMode = true
                    eventMessage = "${selected.name} 선택 (${selected.count}개) · 자신 또는 인접 아군을 선택"
                } else {
                    if (battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION) battleCommandFlow.childCancelled()
                    propertyMode = false
                    eventMessage = "아이템 사용을 취소했습니다."
                }
            },
            onInspect = { selected ->
                usePropertyDetail = selected
                usePropertyDetailSuppressRelease = true
            },
        )
        usePropertyPressedRow = null
        usePropertyCancelPressed = false
        usePropertyPanelPressed = false
    }

    private fun usePropertyCancelAt(x: Float, y: Float) = x in 1131.145f..1281.145f && y in 394.896f..444.896f
    private fun usePropertyPanelAt(x: Float, y: Float) = x in 795.536f..1286.536f && y in 390f..800f

    private fun usePropertyRowAt(x: Float, y: Float): Int? {
        if (x !in 803.536f..1278.536f || y !in 448f..796f) return null
        return ((791f - y) / 112f).toInt().coerceAtLeast(0).takeIf { it in 0 until (usePropertyLayer?.rows?.size ?: 0) }
    }

    private fun installUsePropertyRouteFixture() {
        usePropertyRouteFixtureController.install(
            usePropertyRouteState,
            object : BattleUsePropertyRouteFixtureController.Commands {
                override fun seedInventory() {
                    campaign.inventory.removeItemStack(150)
                    campaign.inventory.removeItemStack(151)
                    campaign.inventory.addItem(150, 3)
                    campaign.inventory.addItem(151, 2)
                }

                override fun selectPlayerUnit() {
                    selectedUnitId = battle.units.values.firstOrNull { it.visible && it.isPlayerSide() }?.id
                }

                override fun openPropertyLayer() = openUsePropertyLayer()

                override fun inspectFirstProperty() {
                    usePropertyLayer?.touchStart(0)
                    usePropertyLayer?.update(UsePropertyLayer.LONG_PRESS_SECONDS)
                }

                override fun selectFirstProperty() {
                    usePropertyLayer?.touchStart(0)
                    usePropertyLayer?.touchEnd(0)
                    usePropertyLayer = null
                }

                override fun cancelPropertyLayer() {
                    usePropertyLayer?.closeTouchEnd()
                    usePropertyLayer = null
                }
            },
        )
    }

    private fun drawUsePropertyLayer() {
        val layer = usePropertyLayer ?: return
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, 40f / 255f)
        shapes.rect(0f, 0f, 1488.372f, 800f)
        shapes.end()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color.WHITE
        val commandChild = battleCommandRouteState == RuntimeBattleRoute.COMMAND_PROPERTY
        val ox = if (commandChild) -59.536f else 0f
        val oy = if (commandChild) -294f else 0f
        for (ty in 0..4) for (tx in 0..5) batch.draw(
            unitInfoAssets.unitInfoLogo,
            795.536f + ox + tx * 96f,
            390f + oy + ty * 96f,
            96f,
            96f
        )
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 795.536f + ox, 390f + oy, 491f, 410f)
        NinePatch(unitInfoAssets.unitInfoBox2, 3, 3, 3, 3).draw(batch, 799.536f + ox, 448f + oy, 483f, 348f)
        font.data.setScale(40f / 26f); font.color = Color.BLACK
        layer.rows.take(3).forEachIndexed { index, item ->
            val y = 681f + oy - index * 112f
            NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, 803.536f + ox, y, 475f, 110f)
            NinePatch(unitInfoAssets.unitInfoBox2, 3, 3, 3, 3).draw(batch, 809.55f + ox, y + 5f, 100f, 100f)
            dynamicTextures.itemIcon(item.icon)?.let { batch.draw(it, 814.55f + ox, y + 10f, 90f, 90f) }
            font.draw(batch, item.name, 912.036f + ox, y + 99f)
            font.draw(batch, "효능: ", 912.036f + ox, y + 47f)
            font.draw(batch, item.typeName, 1015.631f + ox, y + 47f)
            font.draw(batch, "인벤토리: ", 1108.272f + ox, y + 99f)
            font.draw(batch, item.count.toString(), 1249.503f + ox, y + 99f)
        }
        NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, 1131.145f + ox, 394.896f + oy, 150f, 50f)
        font.draw(batch, "취소", 1156.145f + ox, 442f + oy)
        font.data.setScale(1f); font.color = Color.WHITE; batch.end()
    }

    private fun drawUsePropertyDetail() {
        val item = usePropertyDetail ?: return
        val profile = gameDataCatalog.equipmentProfile(item.id) ?: return
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color = Color(0f, 0f, 0f, 100f / 255f); shapes.rect(
            0f,
            0f,
            1488.372f,
            800f
        ); shapes.end()
        batch.projectionMatrix = viewport.camera.combined; batch.begin(); batch.color = Color.WHITE
        for (ty in 0..6) for (tx in 0..10) batch.draw(
            unitInfoAssets.unitInfoLogo,
            253.186f + tx * 96f,
            80f + ty * 96f,
            96f,
            96f
        )
        NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, 253.186f, 80f, 982f, 640f)
        NinePatch(unitInfoAssets.unitInfoBox2, 3, 3, 3, 3).draw(batch, 265.778f, 564.802f, 144f, 144f)
        dynamicTextures.itemIcon(item.icon)?.let { batch.draw(it, 273.778f, 572.802f, 128f, 128f) }
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 420.536f, 498.55f, 343.5f, 100.9f)
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 261.686f, 92.5f, 501f, 377f)
        NinePatch(unitInfoAssets.unitInfoBox2, 3, 3, 3, 3).draw(batch, 770.186f, 157.5f, 448f, 247f)
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 770.186f, 427f, 448f, 260f)
        NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, 1065.827f, 97.824f, 150f, 50f)
        font.data.setScale(40f / 26f); font.color = Color.BLACK
        listOf(
            item.name to (420.186f to 701f), "속성:" to (432.137f to 591f), "아이템" to (522.525f to 591f),
            "가격:" to (432.137f to 546f), gameDataCatalog.purchasePrice(profile).toString() to (522.525f to 546f),
            "효과" to (477.586f to 485f), item.typeName to (265.686f to 432f), "설명" to (953.586f to 421f),
            profile.intro to (774.186f to 376f), "장착 가능한 부대입니다." to (804.516f to 704f), "확인" to (1090.827f to 147f)
        ).forEach { (text, pos) -> font.draw(batch, text, pos.first, pos.second) }
        font.data.setScale(1f); font.color = Color.WHITE; batch.end()
    }

    private fun selectMagick(selected: MagicUiList.Magic) {
        val unit = selectedUnitId?.let(battle.units::get) ?: return
        val index = unit.magic.indexOfFirst { it.id == selected.id }
        if (index < 0) return
        selectedMagicIndex = index
        val magic = unit.magic[index]
        if (magic.target == 2) {
            val hp = battle.units.mapValues { it.value.hitPoints }
            applyAction(
                battle.presentation.castMagic(unit.id, unit.id, magic.id),
                unit.name,
                unit.id,
                magic.id,
                unit.id,
                hp
            )
        } else {
            magicMode = true
            eventMessage = "${magic.name} 선택 · 범위 내 대상을 클릭"
        }
    }

    private fun magickCancelAt(x: Float, y: Float) = x in 775.892f..955.892f && y in 97.683f..147.683f

    private fun magickRowAt(x: Float, y: Float): Int? {
        if (x !in 480.186f..1006.186f || y !in 0f..645.5f) return null
        val line = ((645.5f - y) / 142f).toInt().coerceAtLeast(0)
        val column = if (x < 743.186f) 0 else 1
        return (line * 2 + column).takeIf { it in 0 until (magickListLayer?.rows?.size ?: 0) }
    }

    private fun fixtureMagics(): List<MagicUiList.Magic> =
        listOf(
            39,
            40,
            41,
            43,
            44,
            45,
            46,
            47,
            48,
            49,
            50,
            51,
            52,
            53,
            54,
            55,
            56
        ).mapNotNull(gameDataCatalog::magicProfile).map(::toMagicUi)

    private fun installMagickRouteFixture() {
        magickRouteFixtureController.install(magickRouteState, ::fixtureMagics)?.let { state ->
            magickListLayer = state.list
            magickInfoLayer = state.info
        }
    }

    private fun drawMagickListLayer() {
        val layer = magickListLayer ?: return
        shapes.projectionMatrix = viewport.camera.combined; shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, 40f / 255f); shapes.rect(0f, 0f, 1488.372f, 800f); shapes.end()
        batch.projectionMatrix = viewport.camera.combined; batch.begin(); batch.color = Color.WHITE
        for (ty in 0..6) for (tx in 0..5) batch.draw(
            unitInfoAssets.unitInfoLogo,
            474.186f + tx * 96f,
            90.5f + ty * 96f,
            96f,
            96f
        )
        NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, 474.186f, 90.5f, 540f, 619f)
        NinePatch(unitInfoAssets.unitInfoBox2, 3, 3, 3, 3).draw(batch, 478.186f, 150.5f, 532f, 499f)
        NinePatch(unitInfoAssets.unitInfoProgress, 3, 3, 3, 3).draw(batch, 741.186f, 661.207f, 204f, 24f)
        shapes.projectionMatrix = viewport.camera.combined
        val mpFraction = layer.mp.toFloat() / layer.maxMp.coerceAtLeast(1)
        val previewFraction = if (layer.preview == 0f) mpFraction else layer.preview
        dynamicTextures.battleDialog(BattleUiAssets.MP_CURRENT_MARK)
            ?.let { batch.draw(it, 743.186f, 663.207f, 200f * mpFraction, 20f) }
        dynamicTextures.battleDialog(BattleUiAssets.MP_MAX_MARK)
            ?.let { batch.draw(it, 743.186f, 663.207f, 200f * previewFraction, 20f) }
        font.data.setScale(40f / 26f); font.color = Color.BLACK
        font.draw(batch, selectedUnitId?.let(battle.units::get)?.name ?: "허자장", 495.586f, 695f); font.draw(
            batch,
            "MP",
            681.186f,
            695f
        )
        font.draw(batch, "${layer.mp}/${layer.maxMp}", 793.136f, 696f)
        layer.rows.take(10).forEachIndexed { index, magic ->
            val col = index % 2
            val line = index / 2
            val x = 480.186f + col * 264f
            val y = 505.5f - line * 142f
            NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, x, y, 262f, 140f)
            BattleDialogRenderContract.magicListIcon(magic, x, y).let { icon ->
                dynamicTextures.battleDialog(icon.path)?.let { batch.draw(it, icon.x, icon.y, icon.width, icon.height) }
            }
            font.color = if (layer.enabled(index)) Color.BLACK else Color(.5f, .5f, .5f, 1f)
            font.draw(batch, magic.name, x + 92f, y + 129f); font.draw(batch, "MP：", x + 92f, y + 88f)
            font.draw(batch, magic.cost.toString(), x + 176f, y + 88f); font.draw(batch, "피해 계수: ", x + 2f, y + 47f)
            font.draw(batch, (magic.power ?: 0).div(100f).toString(), x + 180f, y + 47f)
        }
        font.color = Color.BLACK; NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(
            batch,
            775.892f,
            97.683f,
            180f,
            50f
        )
        font.draw(batch, "취소", 815.892f, 145f); font.data.setScale(1f); font.color = Color.WHITE; batch.end()
    }

    private fun drawBattleMagicInfoLayer() {
        val magic = magickInfoLayer?.magic ?: return
        shapes.projectionMatrix = viewport.camera.combined; shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, 100f / 255f); shapes.rect(0f, 0f, 1488.372f, 800f); shapes.end()
        batch.projectionMatrix = viewport.camera.combined; batch.begin(); batch.color = Color.WHITE
        for (ty in 0..5) for (tx in 0..6) batch.draw(
            unitInfoAssets.unitInfoLogo,
            452.686f + tx * 96f,
            130f + ty * 96f,
            96f,
            96f
        )
        NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, 452.686f, 130f, 583f, 540f)
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 465.636f, 434f, 340.3f, 100f)
        NinePatch(unitInfoAssets.unitInfoBox2, 3, 3, 3, 3).draw(batch, 465.636f, 147f, 340.3f, 274f)
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 814.213f, 436.061f, 200f, 200f)
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(batch, 814.213f, 204.673f, 200f, 200f)
        NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, 874.764f, 144.022f, 147.6f, 50f)
        BattleDialogRenderContract.magicDetailSprites(magic).forEach { sprite ->
            dynamicTextures.battleDialog(sprite.path)
                ?.let { batch.draw(it, sprite.x, sprite.y, sprite.width, sprite.height) }
        }
        font.data.setScale(40f / 26f); font.color = Color.BLACK
        listOf(
            magic.name to (577.509f to 646f),
            "위력:" to (476.336f to 522f),
            "${magic.power ?: 0}%" to (566.719f to 522f),
            "MP 소모:" to (470.776f to 479f),
            magic.cost.toString() to (627.053f to 479f),
            magic.intro to (470.786f to 415f),
            "가능 범위" to (839.654f to 653f),
            "영향 범위" to (839.654f to 424f),
            "확인" to (898.564f to 192f)
        ).forEach { (t, p) -> font.draw(batch, t, p.first, p.second) }
        font.data.setScale(1f); font.color = Color.WHITE; batch.end()
    }

    private fun installJiqiRouteFixture() {
        jiqiRouteFixtureController.install(
            RuntimeBattleRoute.JIQI,
            battle.units.values.firstOrNull()?.characterId ?: 0,
            object : BattleJiqiRouteFixtureController.Commands {
                override fun openUnitInfo(characterId: Int) = openUnitInfoLayer(characterId)

                override fun openJiqi() {
                    val result = unitInfoOverlay.dispatch(BattleUnitInfoOverlayController.Intent.OpenJiqi)
                    handleUnitInfoOverlayEffect(result.effect)
                }

                override fun dismissUnitInfo() {
                    unitInfoOverlay.dispatch(BattleUnitInfoOverlayController.Intent.Dismiss)
                }
            },
        )
    }

    private fun drawJiqiLayer() {
        val layer = jiqiLayer ?: return
        val labels = listOf(
            Triple("명중률: ", 479.171f, 487.8f), Triple("방어율:", 485.057f, 424.839f),
            Triple("쌍타율:", 484.731f, 360.8f), Triple("이중 타격률:", 424.571f, 297.8f),
            Triple("마법 명중률: ", 753.016f, 487.8f), Triple("피격 시 치명타율:", 738.416f, 306.8f),
            Triple("치명타율:", 821.431f, 370.8f), Triple("마법 방어율: ", 753.016f, 433.8f),
        )
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, 40f / 255f)
        shapes.rect(0f, 0f, 1488.372f, 800f)
        shapes.end()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        for (ty in 0..3) for (tx in 0..7) batch.draw(
            unitInfoAssets.unitInfoLogo,
            405.686f + tx * 96f,
            234.5f + ty * 96f,
            96f,
            96f
        )
        NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, 405.686f, 234.5f, 677f, 331f)
        font.data.setScale(40f / 26f); font.color = Color.BLACK
        labels.forEach { (text, x, y) -> font.draw(batch, text, x, y + 42f) }
        val valuePositions = listOf(
            625.186f to 487.8f, 625.186f to 424.8f, 625.186f to 360.8f, 625.186f to 297.8f,
            978.186f to 487.8f, 978.186f to 427.8f, 978.186f to 366.8f, 978.186f to 306.8f,
        )
        layer.rates.forEachIndexed { index, value ->
            val (x, y) = valuePositions[index]
            font.draw(batch, value.toString(), x, y + 42f)
        }
        font.data.setScale(1f); font.color = Color.WHITE
        batch.end()
    }

    /** 승리 조건 창 열기: 조건 본문을 생성하고 닫힘 콜백에서 대기 중인 전투 스크립트를 재개한다. */
    private fun openWinConditionBox() {
        if (winConditionOpen) return
        val layer = WinConBoxLayer()
        layer.onCreate(WinConBoxLayer.CreateData(winConditionInfo()) {
            winConditionOpen = false
            winConditionLayer = null
            runBattleScript()
        })
        winConditionLayer = layer
        winConditionOpen = layer.view().attached
    }
    private fun winConditionInfo(): String {
        val stage = scriptRuntime.stage
        val hidden = stage.itemVariables.flatMap { (variables, positions) ->
            positions.mapIndexedNotNull { index, position ->
                variables.getOrNull(index)?.let { WinConditionContent.HiddenItem(it, position) }
            }
        }
        return WinConditionContent.build(
            text = stage.winCondition.takeIf { it.isNotEmpty() },
            vs = stage.winConditionVs,
            talk = stage.winConditionTalk,
            items = hidden.takeIf { it.isNotEmpty() },
            unitName = { index ->
                battle.units.values.firstOrNull {
                    !(winConditionRouteState != null && index == 0) &&
                            it.visible && it.id.substringAfterLast('-').toIntOrNull() == index
                }?.name
            },
            variable = { id -> (campaign.globalVariables[id] as? Number)?.toInt() ?: 0 },
        ).ifEmpty { "적군을 전멸시키십시오." }
    }

    private fun drawScriptWinConditions(layer: WinConditionsLayer) {
        val lines = layer.view().second.replace("<br/>", "\n").replace(Regex("<[^>]+>"), "").lines()
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, 80f / 255f)
        shapes.rect(0f, 0f, 1488.3721f, 800f)
        shapes.end()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        font.data.setScale(98.3f / 26f)
        lines.forEachIndexed { index, text ->
            font.color = if (index == 0) Color.RED else Color(0.467f, 0.467f, 0.467f, 1f)
            font.draw(batch, text, 39.467f, 771.28f - index * 120f)
        }
        lines.forEachIndexed { index, text ->
            font.color = Color.WHITE
            font.draw(batch, text, 27.323f, 779.113f - index * 120f)
        }
        font.data.setScale(1f)
        font.color = Color.WHITE
        batch.end()
    }

    private fun drawWinConditionBox() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        overlayAssets.winConditionBackgroundTexture?.let { texture ->
            var ty = 65f
            while (ty < 735f) {
                var tx = 249.686f
                val h = minOf(96f, 735f - ty)
                while (tx < 1238.686f) {
                    val w = minOf(96f, 1238.686f - tx)
                    batch.draw(texture, tx, ty, w, h)
                    tx += 96f
                }
                ty += 96f
            }
        }
        overlayAssets.winConditionBoxPatch?.draw(batch, 249.686f, 65f, 989f, 670f)
        // Cocos 스프라이트 배율이 (2,2)이므로 53×62 프레임은 캔버스에서 106×124가 된다.
        overlayAssets.winConditionLogoTexture?.let { batch.draw(it, 280.574f, 588.927f, 106f, 124f) }
        val text = winConditionLayer?.view()?.label ?: return
        // 스크롤 뷰의 월드 중심은 (808.186,442), 크기는 803×543이며 자식 box3는 다음 조건을 따른다.
        overlayAssets.winConditionScrollPatch?.draw(batch, 406.686f, 170.5f, 803f, 543f)
        val scroll = Rectangle(406.686f, 170.5f, 803f, 543f)
        val scissors = Rectangle()
        ScissorStack.calculateScissors(viewport.camera, batch.transformMatrix, scroll, scissors)
        batch.flush()
        if (ScissorStack.pushScissors(scissors)) {
            dialogueFont.color = Color(0f, 0.25f, 1f, 1f)
            dialogueFont.data.setScale(32f / 36f) // prefab RichText: 32px / 36px line height
            dialogueFont.draw(batch, text, 421f, 692f, 773f, Align.left, true)
            dialogueFont.data.setScale(1f)
            batch.flush()
            ScissorStack.popScissors()
        }
        dialogueFont.color = Color(0f, 0.82f, 0f, 1f)
        overlayAssets.winConditionBoxPatch?.draw(batch, 957.134f, 88.204f, 256.7f, 60f)
        dialogueFont.draw(batch, "짐이 알겠다.", 982f, 107f)
        dialogueFont.color = Color.WHITE
        batch.end()
    }
    private fun drawLoseScene() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color.WHITE
        overlayAssets.loseLogoTexture?.let { batch.draw(it, 0f, 0f, viewport.worldWidth, viewport.worldHeight) }
        batch.end()
    }

    private fun loseAnswerAt(x: Float, y: Float): Int? = when {
        x in 754.186f..934.186f && y in 271.285f..321.285f -> 0
        x in 554.186f..734.186f && y in 271.285f..321.285f -> 1
        else -> null
    }
    private fun drawLosePrompt() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color.WHITE
        for (ty in 0..3) for (tx in 0..6) {
            val width = minOf(96f, 635f - tx * 96f)
            val height = minOf(96f, 296f - ty * 96f)
            if (width > 0f && height > 0f) batch.draw(
                unitInfoAssets.unitInfoLogo,
                426.686f + tx * 96f,
                252f + ty * 96f,
                width,
                height
            )
        }
        batch.draw(unitInfoAssets.unitInfoBox3, 426.686f, 252f, 635f, 296f)
        overlayAssets.winConditionLogoTexture?.let { batch.draw(it, 453.005f, 373.951f, 106f, 124f) }
        dialogueFont.color = Color.WHITE
        dialogueFont.draw(batch, LoseSceneFlow.PROMPT_TEXT, 573.686f, 500f, 463f, Align.center, true)
        batch.draw(unitInfoAssets.unitInfoBox3, 554.186f, 271.285f, 180f, 50f)
        dialogueFont.draw(batch, "비", 557.336f, 312f, 168.1f, Align.center, false)
        batch.draw(unitInfoAssets.unitInfoBox3, 754.186f, 271.285f, 180f, 50f)
        dialogueFont.draw(batch, "예", 757.586f, 312f, 169.4f, Align.center, false)
        batch.end()
    }

    private fun drawSavePrompt() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color(0f, 0f, 0f, .65f)
        batch.draw(overlayAssets.winConditionBackgroundTexture, 220f, 250f, 840f, 300f)
        batch.color = Color.WHITE
        overlayAssets.winConditionBoxPatch?.draw(batch, 300f, 280f, 680f, 230f)
        dialogueFont.color = Color.WHITE
        dialogueFont.draw(batch, "게임 저장하시겠습니까?", 520f, 430f)
        dialogueFont.draw(batch, "예", 510f, 330f); dialogueFont.draw(batch, "비", 740f, 330f)
        batch.end()
    }
    private fun focusNextNoActionUnit() {
        val candidates = battle.units.values.filter { it.type() == Faction.PLAYER && it.visible && !it.hasActed }
        if (candidates.isEmpty()) {
            if (battle.outcome() == null) endTurn()
            return
        }
        val unit = candidates[noActionIndex % candidates.size]
        noActionIndex++
        selectedUnitId = unit.id
        focusCameraOn(unit)
        eventMessage = "미행동 부대: ${unit.name}"
    }

    internal fun focusFirstCampCameraUnit(camp: Faction) {
        firstCampCameraUnit(battle.units.values, camp)?.let(::focusCameraOn)
    }
    private fun focusCameraOn(unit: BattleUnit, forceCenter: Boolean = false): Boolean {
        configureSourceCameraViewport()
        val (screenX, screenY) = battleCamera.sourceNodeScreenPoint(
            unit.tileX,
            unit.tileY,
            unit.hasAuthoredTileX,
            unit.hasAuthoredTileY,
        )
        if (forceCenter) return focusCameraOnTile(unit.tileX.toFloat(), unit.tileY.toFloat(), true)
        return battleCamera.ensureVisible(screenX, screenY)
    }

    internal fun focusCameraOnTile(tileX: Float, tileY: Float, forceCenter: Boolean = false): Boolean {
        configureSourceCameraViewport()
        val beforeX = battleCamera.contentX
        val beforeY = battleCamera.contentY
        val (screenX, screenY) = SourceBattleMapGeometry.tileCenter(
            tileX,
            tileY,
            terrainGrid.width,
            terrainGrid.height,
            battleCamera.x,
            battleCamera.y,
        )
        if (forceCenter) {
            battleCamera.forceCenter(screenX - 48f, screenY + 48f)
            recordSourceCameraCenter(tileX, tileY)
            return beforeX != battleCamera.contentX || beforeY != battleCamera.contentY
        }
        return battleCamera.ensureVisible(screenX, screenY)
    }

    private fun recordSourceCameraCenter(tileX: Float, tileY: Float) {

        fun jsNumber(value: Float): String =
            if (value.isFinite() && value == value.toInt().toFloat()) value.toInt().toString() else value.toString()
        recordBattleTraceFrame(
            0f,
            "transition:camera:center:${jsNumber(tileX)}:${jsNumber(tileY)}",
            advanceFrame = false,
        )
    }
    private fun configureSourceCameraViewport() {
        battleCamera.configureViewport(1488.3721f, 800f)
    }

    private fun drawScriptDialogue(componentStage: String? = null) {
        val dialogue = scriptRuntime.currentDialogue ?: return
        val includePortrait =
            componentStage == null || componentStage in setOf("portrait", "speaker", "text", "background", "characters")
        val includeSpeaker =
            componentStage == null || componentStage in setOf("speaker", "text", "background", "characters")
        val includeBody = componentStage == null || componentStage in setOf("text", "background", "characters")
        val speakerUnit = dialogue.speakerId?.toIntOrNull()?.let { characterId ->
            (battle.units.values + battle.presentation.pendingPresentationUnits())
                .firstOrNull { it.characterId == characterId && it.visible }
        }
        val speakerScreenCenterY = speakerUnit?.let { unit ->
            val (_, visualY) = visualTile(unit)
            1776f + battleCamera.y - visualY * 96f
        }
        val dialoguePanelY = speakerScreenCenterY?.let { centerY ->
            if (centerY < viewport.worldHeight / 2f) centerY + 92f else centerY - 328f
        } ?: 282f
        val dialogueFaceY = dialoguePanelY - 2f
        val dialogueTextY = dialoguePanelY + 40.314f
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        // DialogueLayer/bg0/bg2는 이 Cocos 월드 위치에서 U_select_11-1을 344×84에서 796×212로 나인 슬라이스한다.
        batch.color = Color.WHITE
        hudAssets.dialoguePanelTexture?.let { texture -> batch.draw(texture, 245.65f, dialoguePanelY, 796f, 212f) }
        val speaker = dialogue.speakerId?.toIntOrNull()?.let(gameDataCatalog::unitProfile)
        speaker?.takeIf { includePortrait }?.let { profile ->
            val headId = profile.face + 8
            val texture = if (sourceScenario == "S_00" && dialogue.speakerId == "477" && headId == 192) {
                hudAssets.yingchuan477FaceTexture ?: dynamicTextures.head(headId)
            } else if (sourceScenario == "S_00" && dialogue.speakerId == "474" && headId == 179) {
                hudAssets.yingchuan474FaceTexture ?: dynamicTextures.head(headId)
            } else dynamicTextures.head(headId)
            texture?.let {
                batch.color = Color.WHITE
                // 실제 Cocos 노드는 96×120에 2배 배율이므로, 프레임버퍼에서 (1160.62, 450) 주변의 192×240 영역을 차지한다.
                batch.draw(texture, 1064.62f, dialogueFaceY, 192f, 240f)
            }
        }
        if (includeSpeaker) dialogueFont.color = Color(35f / 255f, 2f / 255f, 234f / 255f, 1f)
        if (includeSpeaker && dialogue.speakerId == "477" && hudAssets.yingchuan477SpeakerTexture != null) {
            batch.color = Color.WHITE
            batch.draw(hudAssets.yingchuan477SpeakerTexture, 306.65f, dialoguePanelY + 160.9f, 93.8f, 33.2f)
        } else if (includeSpeaker) {
            dialogueFont.data.setScale(1.013f, 1.04f)
            val speakerText = speaker?.name?.let(GameDataCatalog::sayLayerUnitName).orEmpty()
            val speakerBaselineY = dialoguePanelY + 189.40f
            dialogueFont.color = Color(102f / 255f, 1f, 1f, 1f)
            listOf(
                -2f to 0f, 2f to 0f, 0f to -2f, 0f to 2f,
                -1.414f to -1.414f, -1.414f to 1.414f,
                1.414f to -1.414f, 1.414f to 1.414f,
            ).forEach { (dx, dy) ->
                dialogueFont.draw(batch, speakerText, 307.23f + dx, speakerBaselineY + dy)
            }
            dialogueFont.color = Color(35f / 255f, 2f / 255f, 234f / 255f, 1f)
            dialogueFont.draw(
                batch,
                speakerText,
                307.23f,
                speakerBaselineY,
            )
        }
        dialogueFont.color = Color.BLACK
        if (includeBody && dialogue.speakerId == "477" && dialogueReveal.visibleText == "아!" && hudAssets.yingchuan477BodyTexture != null) {
            batch.color = Color.BLACK
            // Cocos 캔버스 글리프 잘라내기는 2배 맵 변환 뒤 폭이 30px이다.
            batch.draw(hudAssets.yingchuan477BodyTexture, 273.9f, dialoguePanelY + 108.4f, 37.5f, 33.6f)
            batch.color = Color.WHITE
        } else if (includeBody) {
            val cocosTexture = dynamicTextures.richText(dialogueReveal.visibleText)
            if (cocosTexture != null) {
                batch.color = Color.WHITE
                batch.draw(
                    cocosTexture.texture,
                    cocosTexture.worldX - .58f,
                    dialoguePanelY + 99.814f - .58f,
                    cocosTexture.drawWidth,
                    cocosTexture.drawHeight,
                )
            } else {
                dialogueFont.data.setScale(1f, .98f)
                dialogueFont.draw(
                    batch,
                    dialogueReveal.visibleText,
                    278.705f,
                    dialogueTextY - 0.58f,
                    728f,
                    Align.left,
                    true
                )
                dialogueFont.data.setScale(1f)
            }
        }
        batch.end()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    }

    private fun advanceBattleDialogue() {
        if (dialogueReveal.revealAllIfPending()) {
            sayAutoClose.reset()
            return
        }
        sayAutoClose.reset()
        scriptRuntime.advanceDialogue(deferCloseCallbackFrame = true)
        dialogueReveal.reset()
        syncScriptedUnits()
        syncDialogueSpeakerPresentation()
        completeTurnScriptIfReady()
    }

    /** 캡처 대사 진행: 원본 캡처 경로처럼 대사를 즉시 넘기고 화면 유닛 상태만 동기화한다. */
    private fun advanceCaptureFixtureDialogue() {
        scriptRuntime.advanceDialogue()
        dialogueReveal.reset()
        syncScriptedUnits()
    }

    private fun confirmBattleChoice() {
        scriptRuntime.confirmChoice()
        syncScriptedUnits()
        completeTurnScriptIfReady()
    }

    private fun drawScriptChoice() {
        val choice = scriptRuntime.currentChoice ?: return
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0.03f, 0.05f, 0.09f, 0.92f)
        shapes.rect(70f, 46f, 1140f, 220f)
        shapes.color = Color(0.90f, 0.70f, 0.30f, 1f)
        shapes.rect(70f, 264f, 1140f, 3f)
        shapes.end()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        font.color = Color(1f, 0.85f, 0.48f, 1f)
        font.draw(batch, "전술 선택", 94f, 234f)
        choice.options.forEachIndexed { index, option ->
            font.color = if (index == scriptRuntime.selectedChoice) Color(1f, 0.86f, 0.43f, 1f) else Color.WHITE
            font.draw(
                batch,
                "${if (index == scriptRuntime.selectedChoice) "▶" else "  "} $option",
                110f,
                190f - index * 42f
            )
        }
        font.color = Color(0.72f, 0.80f, 0.90f, 1f)
        font.draw(batch, "↑↓ 선택 · Enter / 클릭 확정", 850f, 72f)
        batch.end()
    }
    private fun drawScriptInfoLayer() {
        if (scriptRuntime.state != PlaybackState.MODAL ||
            scriptRuntime.currentModalKind != ScenarioModalKind.INFO
        ) return
        val text = battleInfoReveal.visibleText
        val sourceCanvasWidth = 1488.3721f
        val centreX = sourceCanvasWidth / 2f
        val centreY = 400f
        font.data.setScale(40f / 26f)
        val layout = GlyphLayout(font, text)
        val panelWidth = (layout.width + 5.4f).coerceAtLeast(74.6f)
        val panelHeight = (layout.height + 20f).coerceAtLeast(83f)
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        NinePatch(unitInfoAssets.unitInfoBox1, 3, 3, 3, 3).draw(
            batch,
            centreX - panelWidth / 2f,
            centreY - panelHeight * .28f,
            panelWidth,
            panelHeight,
        )
        font.color = Color.WHITE
        font.draw(batch, text, centreX - layout.width / 2f, centreY + 18.5f)
        font.data.setScale(1f)
        batch.end()
    }

    internal fun runBattleScript(clickedCharacterId: Int? = null, contextCampOverride: Int? = null) {
        if (verification.active || scriptRuntime.state != PlaybackState.COMPLETE) return
        if (bootstrapPhase == BattleBootstrapPhase.COMPLETE &&
            visibleBattleOutcome() == BattleOutcome.PLAYER_VICTORY &&
            !scriptRuntime.stage.battleEndedByScript
        ) resultScene1Observed = true
        if (scriptRuntime.stage.battleMaxRoundsIncludesFeature) battle.setResolvedMaxRounds(scenarioMaxRound())
        else battle.setMaxRounds(scenarioMaxRound())
        val scriptUnits = (battle.units.values + battle.presentation.pendingPresentationUnits()).distinctBy { it.id }
        val positions = scriptUnits
            .filter { it.visible && it.characterId != null }
            .associate { it.characterId!! to (it.tileX to it.tileY) }
        val stagePositions = scriptUnits
            .filter { it.characterId != null }
            .associate { it.characterId!! to (it.tileX to it.tileY) }
        val positionsByCamp = scriptUnits.filter { it.visible }.groupBy(
            keySelector = { it.type().scriptCamp() },
            valueTransform = { it.tileX to it.tileY },
        )
        val attributes = scriptUnits.mapNotNull { unit ->
            unit.characterId?.let { it to mapOf(7 to unit.hitPoints, 8 to unit.magicPoints) }
        }.toMap()
        val mineMasterBattleId = scriptRuntime.stage
            .battleUnitForCharacterId(scriptRuntime.stage.mineMasterInstanceId)?.battleId
        scriptRuntime.setBattleContext(
            ScenarioBattleScriptContext(
                round = battle.round,
                camp = contextCampOverride ?: battle.activeFaction.scriptCamp(),
                maxRound = scenarioMaxRound(),
                playerDefeated = BattleScreenLoseCondition.defeated(scriptUnits, mineMasterBattleId),
                enemyDefeated = visibleBattleOutcome() == BattleOutcome.PLAYER_VICTORY,
                clickedCharacterId = clickedCharacterId,
                positions = positions,
                stagePositions = stagePositions,
                positionsByCamp = positionsByCamp,
                campByCharacterId = scriptUnits.filter { it.visible }.mapNotNull { unit ->
                    unit.characterId?.let { it to unit.type().scriptCamp() }
                }.toMap(),
                attackOffsets = scriptUnits.mapNotNull { unit ->
                    unit.characterId?.let { it to unit.attackOffsets }
                }.toMap(),
                activeCharacterIds = scriptUnits.filter { it.visible }.mapNotNull { it.characterId }.toSet(),
                attributes = attributes,
                enabledFeatures = battle.enabledFeatureMask(),
            )
        )
        scriptUnitBaseline = scriptRuntime.stage.units.values.associate { scripted ->
            scripted.id to ScriptUnitBaseline(
                scripted.x, scripted.y, scripted.visible, scripted.ai,
                scripted.aiTargetId, scripted.aiTargetX, scripted.aiTargetY,
            )
        }
        scriptRuntime.start("scene1")
        syncScriptedUnits()
        scriptRuntime.stage.scriptedBattleOutcome?.let(battle::setScriptedOutcome)
    }
    private fun syncDialogueSpeakerPresentation() {
        val dialogue = scriptRuntime.currentDialogue
        if (dialogue == null) {
            positionedDialogueRevision = -1L
            return
        }
        if (positionedDialogueRevision == scriptRuntime.dialogueRevision) return
        if (viewport.worldWidth <= 0f || viewport.worldHeight <= 0f) return
        positionedDialogueRevision = scriptRuntime.dialogueRevision
        val characterId = dialogue.speakerId?.toIntOrNull() ?: return
        (battle.units.values + battle.presentation.pendingPresentationUnits())
            .firstOrNull { it.characterId == characterId && it.visible }
            ?.let(::focusCameraOn)
    }
    private fun syncScriptedUnits() {
        if (scriptRuntime.stage.battleMaxRoundsIncludesFeature) battle.setResolvedMaxRounds(scenarioMaxRound())
        else battle.setMaxRounds(scenarioMaxRound())
        terrainGrid.resetOverlays()
        terrainGrid.applyObjectOverlays(scriptRuntime.stage.mapObjects.values)
        terrainGrid.applyFires(scriptRuntime.stage.fires.values)
        battle.setBlockedTiles(
            scriptRuntime.stage.mapObjects.values
                .filter { it.enabled && it.objectId > 3 }
                .map { it.x to it.y },
        )
        scriptRuntime.stage.battleUnits.values.forEach { scripted ->
            val id = scripted.battleId
            if (id !in materializedBattleUnitIds) {
                val materialized = BattleScenarioFactory.fromScriptedUnits(
                    units = listOf(scripted),
                    gameDataCatalog = gameDataCatalog,
                    terrain = terrainGrid,
                    enemyMasterInstanceId = scriptRuntime.stage.enemyMasterInstanceId,
                    initialWeather = scriptRuntime.stage.initialBattleWeather(),
                    weatherSchedule = scriptRuntime.stage.battleWeatherSchedule(),
                    weatherOffset = scriptRuntime.stage.battleWeatherOffset,
                    enemyEquipment = scriptRuntime.stage.enemyEquipment,
                    campaign = campaign,
                    sourceRandomStreams = fullTraceRandom,
                ).units.getValue(id)
                battle.addUnit(materialized)
                materializedBattleUnitIds += id
            }
            battle.presentation.presentationUnit(id)?.apply {
                deathMessageEnabled = scripted.deathMessageEnabled
                visible = !scripted.hidden
                ai = scripted.ai
                aiTargetCharacterId = scripted.aiTargetId
                aiTargetX = scripted.aiTargetX
                aiTargetY = scripted.aiTargetY
            }
        }
        scriptRuntime.stage.units.values.forEach { scripted ->
            (battle.units.values + battle.presentation.pendingPresentationUnits())
                .firstOrNull {
                    it.id == scriptRuntime.stage.battleUnitForCharacterId(scripted.id)?.battleId
                }?.apply {
                    val before = scriptUnitBaseline?.get(scripted.id)
                    if (scripted.moveDuration <= 0f &&
                        before != null && (scripted.x != before.x || scripted.y != before.y)
                    ) {
                        tileX = scripted.x
                        tileY = scripted.y
                        hasAuthoredTileX = true
                        hasAuthoredTileY = true
                    }
                    if (before == null || scripted.visible != before.visible) {
                        visible = scripted.visible
                        if (before != null && visible) focusCameraOn(this)
                    }
                    if (before == null || scripted.ai != before.ai) ai = scripted.ai
                    if (before == null || scripted.aiTargetId != before.targetId) aiTargetCharacterId =
                        scripted.aiTargetId
                    if (before == null || scripted.aiTargetX != before.targetX) aiTargetX = scripted.aiTargetX
                    if (before == null || scripted.aiTargetY != before.targetY) aiTargetY = scripted.aiTargetY
                }
        }
        scriptRuntime.stage.consumeScriptedUnitLevelChanges().forEach { change ->
            val scripted = scriptRuntime.stage.battleUnitForCharacterId(change.unitId) ?: return@forEach
            val live = (battle.units.values + battle.presentation.pendingPresentationUnits())
                .firstOrNull { it.id == scripted.battleId } ?: return@forEach
            val refreshed = BattleScenarioFactory.fromScriptedUnits(
                units = listOf(scripted),
                gameDataCatalog = gameDataCatalog,
                terrain = terrainGrid,
                enemyMasterInstanceId = scriptRuntime.stage.enemyMasterInstanceId,
                initialWeather = scriptRuntime.stage.initialBattleWeather(),
                weatherSchedule = scriptRuntime.stage.battleWeatherSchedule(),
                weatherOffset = scriptRuntime.stage.battleWeatherOffset,
                enemyEquipment = scriptRuntime.stage.enemyEquipment,
                campaign = campaign,
                sourceRandomStreams = fullTraceRandom,
                enabledFeatures = battle.enabledFeatureMask(),
            ).units.values.single()
            live.refreshLevelDerivedState(refreshed)
        }
        scriptRuntime.stage.consumeScriptedUnitPostsChanges().forEach { change ->
            val scripted = scriptRuntime.stage.battleUnitForCharacterId(change.unitId) ?: return@forEach
            val live = (battle.units.values + battle.presentation.pendingPresentationUnits())
                .firstOrNull { it.id == scripted.battleId } ?: return@forEach
            val refreshed = BattleScenarioFactory.fromScriptedUnits(
                units = listOf(scripted),
                gameDataCatalog = gameDataCatalog,
                terrain = terrainGrid,
                enemyMasterInstanceId = scriptRuntime.stage.enemyMasterInstanceId,
                initialWeather = scriptRuntime.stage.initialBattleWeather(),
                weatherSchedule = scriptRuntime.stage.battleWeatherSchedule(),
                weatherOffset = scriptRuntime.stage.battleWeatherOffset,
                enemyEquipment = scriptRuntime.stage.enemyEquipment,
                campaign = campaign,
                sourceRandomStreams = fullTraceRandom,
                enabledFeatures = battle.enabledFeatureMask(),
            ).units.values.single()
            if (change.postsWritten) {
                live.refreshPostsDerivedState(refreshed, refreshAbilityPhase = change.derivedAttributes.isNotEmpty())
            } else if (change.derivedAttributes.isNotEmpty()) {
                live.refreshAbilityPhase(refreshed)
            }
        }
        scriptRuntime.stage.consumeScriptedUnitDirections().forEach { (characterId, direction) ->
            (battle.units.values + battle.presentation.pendingPresentationUnits())
                .firstOrNull {
                    it.id == scriptRuntime.stage.battleUnitForCharacterId(characterId)?.battleId
                }?.direction = direction
        }
        scriptRuntime.stage.units.values.forEach { scripted ->
            (battle.units.values + battle.presentation.pendingPresentationUnits())
                .firstOrNull {
                    it.id == scriptRuntime.stage.battleUnitForCharacterId(scripted.id)?.battleId
                }?.let { unit ->
                    if (scripted.moveDuration > 0f) {
                        unit.direction = scripted.direction
                        scriptedUnitPresentation.setVisual(
                            unit.id,
                            ScriptedUnitVisual(20, animationClock() - scripted.animationElapsed),
                        )
                        scriptedMovementCameraCursors
                            .getOrPut(scripted.id, ::MovementCameraTickCursor)
                            .crossed(
                                scripted.movePath,
                                BattleUnitMoveTimeline.schedule(scripted.movePath, fastMove = true),
                                scripted.moveElapsed,
                            )
                            .forEach { sample ->
                                focusCameraOnTile(sample.x, sample.y)
                            }
                    } else if (scriptedUnitPresentation.visual(unit.id)?.action == 20) {
                        scriptedMovementCameraCursors[scripted.id]
                            ?.crossed(
                                scripted.movePath,
                                BattleUnitMoveTimeline.schedule(scripted.movePath, fastMove = true),
                                scripted.moveElapsed,
                            )
                            ?.forEach { sample ->
                                focusCameraOnTile(sample.x, sample.y)
                            }
                        scriptedUnitPresentation.clearVisual(unit.id)
                        scriptedMovementCameraCursors.remove(scripted.id)
                        unit.direction = scripted.direction
                    }
                }
        }
        applyScriptedAttacks()
        scriptedUnitActions.consumeStarts()
        applyScriptedStatuses()
        scriptUnitBaseline = scriptRuntime.stage.units.values.associate { scripted ->
            scripted.id to ScriptUnitBaseline(
                scripted.x, scripted.y, scripted.visible, scripted.ai,
                scripted.aiTargetId, scripted.aiTargetX, scripted.aiTargetY,
            )
        }
    }

    private fun applyScriptedAttacks() {
        scriptRuntime.stage.consumeScriptedAttacks().forEach { action ->
            val attacker = liveScriptBattleUnit(action.attackerId) ?: return@forEach
            val target = liveScriptBattleUnit(action.targetId) ?: return@forEach
            focusCameraOn(attacker)
            val sourceAction = if (action.flag and 1 != 0) 21 else 25
            val direction = battleDirection(attacker.id, target.id)
            val attack = sourceActionAnimation(attacker.id, sourceAction, direction)
            val hitAt = attack.startedAt + requireNotNull(battleSprites.hitTime(sourceAction, direction)) {
                "원본 BRAnime anime$sourceAction 방향 ${direction}에 hit 이벤트가 없습니다"
            }
            val reactionDirection = battleDirection(target.id, attacker.id)
            val targetAction = if (action.flag and 2 != 0) 26 else 32
            val reactionDuration = requireSourceActionDuration(targetAction, reactionDirection)
            val reactionEndsAt = hitAt + reactionDuration
            actionAnimation = attack.copy(endsAt = reactionEndsAt)
            scriptedAttackCallbackEndsAt = maxOf(scriptedAttackCallbackEndsAt, reactionEndsAt)
            scheduleHitReaction(target.id, reactionDirection, hitAt, reactionEndsAt, targetAction)
            eventMessage = "연출 공격: ${attacker.name} → ${target.name}"
        recordBattleTraceFrame(
                0f,
                "transition:attackAction:${action.attackerId}:${action.targetId}:${action.flag}",
                advanceFrame = false,
            )
        }
    }
    private fun applyScriptedStatuses() {
        scriptRuntime.stage.consumeUnitStatuses().forEach { change ->
            val unitReference = change["unit"] as? ScenarioUnitReference
            val camp = (change["camp"] as? Number)?.toInt()
            val x1 = (change["x1"] as? Number)?.toInt()
            val y1 = (change["y1"] as? Number)?.toInt()
            val x2 = (change["x2"] as? Number)?.toInt()
            val y2 = (change["y2"] as? Number)?.toInt()
            val targets = unitReference?.let { ref ->
                listOfNotNull(liveScriptBattleUnit(ref.id))
            } ?: run {
                if (camp == null || x1 == null || y1 == null || x2 == null || y2 == null) emptyList() else {
                    battle.units.values.filter {
                        val matchesCamp = when (camp) {
                            4 -> it.isPlayerSide()
                            5 -> it.type().isEnemySide()
                            else -> it.type().scriptCamp() == camp
                        }
                        matchesCamp && it.tileX in x1..x2 && it.tileY in y1..y2
                    }.sortedWith(compareBy<BattleUnit> { it.tileX }.thenBy { it.tileY })
                }
            }
            // BattleScreen.setUnitStatus는 ±255를 상태 변경 없음 값으로 처리한다.
            val hpChange = (change["hp"] as? Number)?.toInt()?.takeUnless { kotlin.math.abs(it) == 255 } ?: 0
            val mpChange = (change["mp"] as? Number)?.toInt()?.takeUnless { kotlin.math.abs(it) == 255 } ?: 0
            val lift = (change["lift"] as? Number)?.toInt()
            val liftedAttribute = if (lift != null) {
                (change["status"] as? Number)?.toInt()?.let(::battleAttribute)
            } else null
            val primaryStatusIndex = if (lift != null) (change["status"] as? Number)?.toInt() else null
            val hStatusIndices = (change["hStatus"] as? List<*>)?.mapNotNull { (it as? Number)?.toInt() }.orEmpty()
            val statusValues = listOfNotNull(primaryStatusIndex) + hStatusIndices
            val remove = change["remove"] == true
            targets.forEach { unit ->
                if (hpChange != 0) {
                    unit.addHpcur(hpChange, keepAlive = true)
                }
                if (mpChange != 0) unit.addMpcur(mpChange)
                liftedAttribute?.let { attribute ->
                    val requested = lift!!.coerceIn(0, 2) - ControlScoring.Lift.NORMAL
                    unit.applyAttributeLift(attribute, requested, battle.rollStatusDuration())
                }
                if (liftedAttribute == null && primaryStatusIndex != null) {
                    battleStatus(primaryStatusIndex)?.let { status ->
                        val rounds = battle.rollStatusDuration()
                        if (lift!!.coerceIn(0, 2) == ControlScoring.Lift.NORMAL) unit.statuses.remove(status)
                        else unit.statuses[status] = rounds
                    }
                }
                hStatusIndices.mapNotNull(::battleStatus).forEach { status ->
                    if (remove) unit.statuses.remove(status) else unit.statuses[status] = battle.rollStatusDuration()
                }
            }
            if (targets.isNotEmpty()) {
                val targetCharacterIds = targets.joinToString(",") { (it.characterId ?: -1).toString() }
                val states = statusValues.joinToString(",")
                val authoredTarget = unitReference?.let { ref ->
                    "unit=${ref.id}"
                } ?: "rect=$camp,$x1,$y1,$x2,$y2"
        recordBattleTraceFrame(
                    0f,
                    "transition:setUnitStatus:$authoredTarget:hp=$hpChange:mp=$mpChange:states=$states:resolved=$targetCharacterIds",
                    advanceFrame = false,
                )
            }
        }
    }

    private fun battleStatus(sourceState: Int): BattleStatus? = when (sourceState) {
        7 -> BattleStatus.PARALYSIS
        8 -> BattleStatus.SILENCE
        9 -> BattleStatus.CONFUSION
        10 -> BattleStatus.POISON
        13 -> BattleStatus.LOST
        else -> null
    }
    private fun battleAttribute(sourceState: Int): BattleAttribute? = BattleAttribute.entries.getOrNull(sourceState)

    private fun Faction.label(): String = when (this) {
        Faction.PLAYER -> "아군"
        Faction.FRIEND -> "우군"
        Faction.ENEMY -> "적군"
        Faction.REINFORCEMENTS -> "적 증원군"
    }

    private fun Faction.scriptCamp(): Int = when (this) {
        Faction.PLAYER -> 0
        Faction.FRIEND -> 1
        Faction.ENEMY -> 2
        Faction.REINFORCEMENTS -> 3
    }

    private fun BattleWeather.label(): String = when (this) {
        BattleWeather.CLEAR -> "맑음"
        BattleWeather.CLOUDY -> "흐림"
        BattleWeather.WINDY -> "바람"
        BattleWeather.HEAVY_RAIN -> "호우"
        BattleWeather.SNOW -> "눈"
    }
    private fun scenarioMaxRound(): Int =
        scriptRuntime.stage.battleMaxRounds.takeIf { it != 99 }
            ?: Regex("턴\\s*수가\\s*(\\d+)").find(scriptRuntime.stage.winCondition)
                ?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 99
    private fun battleAvatarId(unit: BattleUnit): Int? {
        loadedBattleAvatarIds[unit.id]?.let { return it }
        val characterId = unit.characterId ?: return null
        return BattleAvatarResolver.resolve(gameDataCatalog, characterId, unit.posts, unit.armId, unit.type())
            ?.also { loadedBattleAvatarIds[unit.id] = it }
    }

    private fun unitTexture(unit: BattleUnit): Texture? =
        battleAvatarId(unit)?.let(dynamicTextures::unitMovement)

    private fun attackTexture(unit: BattleUnit): Texture? =
        battleAvatarId(unit)?.let(dynamicTextures::attack)

    private fun specialTexture(unit: BattleUnit): Texture? =
        battleAvatarId(unit)?.let(dynamicTextures::special)
    private fun battleDirection(actorId: String, targetId: String?): Int {
        val actor = battle.units[actorId] ?: return 2
        val target = targetId?.let(battle.units::get) ?: return 2
        if (actor.id == target.id) return actor.direction
        val dx = target.tileX - actor.tileX
        val dy = target.tileY - actor.tileY
        return if (kotlin.math.abs(dy) > kotlin.math.abs(dx)) {
            if (actor.tileY > target.tileY) 0 else 2
        } else if (actor.tileX > target.tileX) 3 else 1
    }
    private fun sourceActionAnimation(
        unitId: String,
        action: Int,
        direction: Int,
        startedAt: Float = animationClock()
    ): UnitActionAnimation {
        val duration = requireSourceActionDuration(action, direction)
        battle.presentation.presentationUnit(unitId)?.direction = direction
        return UnitActionAnimation(
            unitId,
            UnitAnimationKind.ATTACK,
            direction,
            startedAt,
            startedAt + duration,
            sourceAction = action
        )
    }
    private fun scheduleHitReaction(unitId: String, direction: Int, startsAt: Float, endsAt: Float, sourceAction: Int) {
        val previousDirection = battle.presentation.presentationUnit(unitId)?.direction
        hitReactionAnimations[unitId] = UnitActionAnimation(
            unitId, UnitAnimationKind.HIT, direction, startsAt, endsAt, sourceAction,
        )
        BattleScreenHitReactionDirectionScheduler.schedule(
            sourceAction = sourceAction,
            reactionDirection = direction,
            previousDirection = previousDirection,
            startsAt = startsAt,
            endsAt = endsAt,
            schedule = ::scheduleBattleMutation,
            isCurrentReaction = {
                val current = hitReactionAnimations[unitId]
                current?.startedAt == startsAt && current.endsAt == endsAt && current.sourceAction == sourceAction
            },
            setDirection = { facing -> battle.presentation.presentationUnit(unitId)?.direction = facing },
        )
    }
    private fun requireSourceActionDuration(action: Int, direction: Int): Float =
        requireNotNull(battleSprites.duration(action, direction).takeIf { it > 0f }) {
            "원본 BRAnime anime$action 방향 $direction 클립이 없습니다"
        }
    /** 이동 카메라 틱 진행: 현재 이동 시간선의 방향을 유닛에 반영하고 새 타일 경과 시 카메라를 이동한다. */
    private fun driveMovementTicks() {
        val move = movementAnimation ?: return
        val elapsed = (animationClock() - move.startedAt).coerceAtLeast(0f)
        val unit = battle.presentation.presentationUnit(move.unitId) ?: return
        val current =
            BattleUnitMoveTimeline.sample(move.path, move.timeline, elapsed.coerceAtMost(move.timeline.idleAt))
        if (animationClock() < move.endsAt) unit.direction = current.direction
        move.cameraTickCursor.crossed(move.path, move.timeline, elapsed)
            .forEach { sample ->
                focusCameraOnTile(sample.x, sample.y)
            }
    }

    private fun visualTile(unit: BattleUnit): Pair<Float, Float> {
        backMoveAnimations[unit.id]
            ?.takeIf { animationClock() < it.endsAt }
            ?.let { animation ->
                val fraction = ((animationClock() - animation.startedAt) /
                        (animation.endsAt - animation.startedAt)).coerceIn(0f, 1f)
                return (animation.move.fromX + (animation.move.toX - animation.move.fromX) * fraction) to
                        (animation.move.fromY + (animation.move.toY - animation.move.fromY) * fraction)
            }
        movementAnimation
            ?.takeIf { it.unitId == unit.id && animationClock() < it.endsAt }
            ?.let { move -> BattleUnitMoveTimeline.sample(move.path, move.timeline, animationClock() - move.startedAt) }
            ?.let { return it.x to it.y }
        unit.characterId?.let(scriptRuntime.stage.units::get)
            ?.takeIf { it.moveDuration > 0f }
            ?.let { return it.visualX to it.visualY }
        return unit.tileX.toFloat() to unit.tileY.toFloat()
    }

    override fun dispose() {
        unitPresentationStore.clear()
        audio.dispose()
        font.dispose()
        dialogueFont.dispose()
        rewardTitleFont.dispose()
        sectionTitleFont.dispose()
        mapTexture?.dispose()
        overlayAssets.dispose()
        unitInfoAssets.dispose()
        hudAssets.dispose()
        dynamicTextures.dispose()
        if (cocos8MapSampler.isInitialized()) cocos8MapSampler.value.dispose()
        if (cocosHighlightSampler.isInitialized()) cocosHighlightSampler.value.dispose()
        if (cocosGraySampler.isInitialized()) cocosGraySampler.value.dispose()
        batch.dispose()
        shapes.dispose()
    }

}

private fun BattleDeathCheckpoint.toDeathTimelineCheckpoint(): BattleDeathPresentationTimeline.Checkpoint =
    when (this) {
        BattleDeathCheckpoint.CAMP_START -> BattleDeathPresentationTimeline.Checkpoint.CAMP_START
        BattleDeathCheckpoint.CAMP_RESTORE -> BattleDeathPresentationTimeline.Checkpoint.CAMP_RESTORE
        BattleDeathCheckpoint.ROUND_START -> BattleDeathPresentationTimeline.Checkpoint.ROUND_START
    }
