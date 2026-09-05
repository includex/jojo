package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.viewport.ExtendViewport


/**
 * LibGDX tactical battle screen and presentation coordinator.
 *
 * Gameplay rules live in [Battle]; this class owns input, presentation timing,
 * overlays, and LibGDX resources while those responsibilities are extracted.
 */
class BattleScreen(
    private val game: JojoGame,
    private val verifyMode: Boolean,
    private val scriptedBattleVerifyMode: Boolean,
    private val sourceScenario: String,
    private val returnScenario: String,
    private val campaign: CampaignState,
) : ScreenAdapter() {
    private enum class ResultFlow { NONE, LOSE_SCENE, WIN_SAVE_PROMPT }
    /** `Battle.fire`'s ordered `BattleScreen.areas` SpriteFrames. */
    private enum class SelectAreaFrame(val assetName: String) {
        RED("range-red"),
        GREEN("range-green"),
        BLUE("range-blue"),
        RED_BOX("range-red-box"),
        GREEN_BOX("range-green-box"),
    }

    private data class SelectAreaTile(val x: Int, val y: Int, val frame: SelectAreaFrame)

    private var resultFlow = ResultFlow.NONE
    private var rewardFlow: BattleRewardFlow? = null
    private var itemUpgradeFlow: ItemUpgradeFlow? = null
    private var itemUpgradeCallbackCount = 0
    private var itemUpgradeRouteInstalled = false
    private var postBattleSceneStarted = false
    private var initialPlayerCampScriptStarted = false
    /** Original BattleScreen keeps curCamp=UNKNOW until scene0 and the first scene1 startOper complete. */
    private var bootstrapPhase = if (verifyMode || scriptedBattleVerifyMode) {
        BattleBootstrapPhase.COMPLETE
    } else {
        BattleBootstrapPhase.SCENE0
    }
    private var naturalOutcomeScriptStarted = false
    /** Monotonic trace marker for a source callback that enters scene1 after victory. */
    private var resultScene1Observed = false
    private var battleRouteCompleted = false
    private var victorySaveAnswerPressed: Int? = null
    private var postBattleSaveLayer = false
    /** InfoLayer's transparent full-screen Panel_cancel is committed on TOUCH_END. */
    private var battleInfoPanelPressed = false
    private val rewardTitleFont: BitmapFont = KoreanFont.create(100, "전투 종료보상금전리품★☆")
    private val sectionTitleFont: BitmapFont = KoreanFont.create(120, "영천의 전투")
    private val overlayAssets = BattleOverlayAssets()
    private val rewardRouteState: String? = game.requestedCaptureState()
        ?.takeIf { it in REWARD_ROUTE_STATES }
    private val itemUpgradeRouteState: String? = game.requestedCaptureState()
        ?.takeIf { it == ITEM_UPGRADE_ROUTE_STATE }
    private val loseRestartRoute = game.requestedCaptureState() == LOSE_RESTART_ROUTE_STATE
    private val roundRouteState = game.requestedCaptureState()?.takeIf {
        it in setOf("battle-round-normal-fixture", "battle-round-final-fixture", "battle-round-enemy-fixture")
    }
    private val winConditionRouteState = game.requestedCaptureState()?.takeIf {
        it in setOf("battle-win-condition-compact-fixture", "battle-win-condition-full-fixture")
    }
    private val miniMapRouteState = game.requestedCaptureState()?.takeIf {
        it in setOf("battle-mini-map-shown-fixture", "battle-mini-map-hidden-fixture")
    }
    private val autoBattleRouteState = game.requestedCaptureState()?.takeIf {
        it in setOf(
            "battle-auto-battle-prompt-off-fixture",
            "battle-auto-battle-prompt-on-fixture",
            "battle-auto-battle-active-fixture",
        )
    }
    private val battleCommandRouteState = game.requestedCaptureState()?.takeIf {
        it in setOf(
            "battle-command-initial-fixture", "battle-command-disabled-fixture",
            "battle-command-cancel-fixture", "battle-command-magick-fixture",
            "battle-command-property-fixture",
        )
    }
    private val battleCharacterRouteState = parseBattleCharacterRoute(game.requestedCaptureState())
    private val battleEdit2RouteState = BattleEditLayer2Route.parse(game.requestedCaptureState())
    private val otherUnitInfoRoute = game.requestedCaptureState() == "battle-other-unit-info-fixture"
    private var otherUnitInfoLayer: OtherUnitInfoLayer? = null
    private val mineUnitInfoRoute = game.requestedCaptureState() == "battle-mine-unit-info-fixture"
    private var mineUnitInfoLayer: MineUnitInfoLayer? = null
    private var battleEdit2: BattleEditLayer2? = null
    private var battleEdit3Open = false
    private var battleEdit3ScenePanelOpen = false
    private var battleRegisterRoute: BattleRegisterRoute? = null
    private var battleCharacterRouteInstalled = false
    private var battleCharacterRouteSamples: List<BattleCharacterRouteSample> = emptyList()
    private val miniMapLayer = MiniMapLayer(setting = 0)
    private var miniMapReady = false
    private var miniMapRouteInstalled = false
    private var miniMapButtonPressed = false
    private var roundRouteInstalled = false
    private var roundRouteCallbackCount = 0
    private var loseSceneFlow: LoseSceneFlow? = null
    private var losePressedAnswer: Int? = null
    /** Runtime-only isolated composition observation; no rendering behavior changes. */
    fun compositionTrace(): String {
        val unitRecords = battle.units.values.filter { it.visible }.joinToString(",") { u ->
            val sourceX = 48 * u.tileX - 456
            val sourceY = 456 - 48 * u.tileY
            // Cocos UIFrame.CreateAnime creates these frames at runtime, so
            // its numeric frame name has no imported SpriteFrame UUID.  Use
            // the same persistent scripted action selection as the draw pass
            // so action 4 is not misreported as an idle actor in captures.
            val scripted = scriptedUnitVisuals[u.id]
            val selected = scripted?.let { scriptedVisualFrame(u, it) } ?: idleSpriteFrame(u)
            val atlasUuid = dynamicTextures.movementAtlasUuid(battleAvatarId(u))
            val row = (selected.sourceY - 1) / 50
            val generatedFrameName = (row shl 24) or (1 shl 16) or 12336
            val atlasJson = if (atlasUuid == null) "null" else "\"$atlasUuid\""
            val actionJson = scripted?.action?.toString() ?: "null"
            val material = if (sourceScenario == "S_00" && scripted?.action == 4) "hight-light/u_value=1" else "SpriteBatch/source-over"
            "{\"address\":\"candidate/unit/${u.id}\",\"frame\":\"$generatedFrameName\",\"runtimeGeneratedFrame\":true,\"textureUuid\":$atlasJson,\"frameRect\":[0,${selected.sourceY},${selected.sourceWidth},${selected.sourceHeight}],\"asset\":${u.characterId ?: -1},\"tile\":[${u.tileX},${u.tileY}],\"action\":$actionJson,\"material\":\"$material\",\"sourceLocalPosition\":[${sourceX},${sourceY}],\"sourceChildPosition\":[0,0],\"sourceChildScale\":[${if (selected.flipX) -1 else 1},1],\"flipX\":${selected.flipX},\"flipY\":false,\"z\":${u.tileY + 1}}"
        }
        val maskRecords = battle.units.values.filter { it.visible }.mapNotNull { u ->
            when (terrainGrid.terrainAt(u.tileX, u.tileY)) {
                10 -> "{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/mask/node#${u.id}\",\"frame\":\"Mark_19-1\",\"asset\":\"maps/marks/19.png#c91c07bf\",\"tile\":[${u.tileX},${u.tileY}],\"stencil\":true}"
                1 -> "{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/mask/node#${u.id}\",\"frame\":\"Mark_21-1\",\"asset\":\"maps/marks/21.png#f52b641a\",\"tile\":[${u.tileX},${u.tileY}],\"stencil\":true}"
                else -> null
            }
        }.joinToString(",")
        val weather = battle.weather.name
        val optionalMasks = if (maskRecords.isEmpty()) "" else ",$maskRecords"
        val captureState = game.requestedCaptureState()
        // Keep the trace bound to the same isolation/full-scene map branch as
        // drawGrid(), rather than presenting the map-only y=-560 mutation as
        // a normal Battle transform.
        val tracedMapBottom = if (captureState == "map-only") -560 else -96
        val scenarioKey = when (captureState) {
            "yingchuan-opening-say" -> "r00-opening-say"
            "yingchuan-dialogue-1" -> "dialogue-1"
            "attack6-f0" -> "battle-action-6-f0"
            "yingchuan-win-condition" -> "win-condition-modal"
            "enemy-turn" -> "enemy-turn"
            "lose-result" -> "lose-result"
            "win-result" -> "win-result"
            else -> "natural-r00"
        }
        // State-addressed records reflect live candidate objects, rather than
        // reusing the natural-map inventory for every capture state.
        val scenarioRecords = when (scenarioKey) {
            "r00-opening-say" -> scriptRuntime.currentDialogue?.let { dialogue ->
                val speakerJson = dialogue.speakerId?.let { "\"$it\"" } ?: "null"
                val speakerNameJson = dialogue.speakerId?.toIntOrNull()
                    ?.let(gameDataCatalog::unitProfile)?.name
                    ?.let(GameDataCatalog::sayLayerUnitName)
                    ?.replace("\\", "\\\\")?.replace("\"", "\\\"")
                    ?.let { "\"$it\"" } ?: "null"
                val textJson = dialogueReveal.visibleText.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                val remainingJson = dialogue.text.removePrefix(dialogueReveal.visibleText).replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                ",{\"address\":\"Battle/Canvas/Layer\",\"kind\":\"SayLayer\",\"active\":true,\"speaker\":$speakerJson,\"speakerName\":$speakerNameJson,\"sourceStrings\":[\"&${dialogue.speakerId}\",\"${dialogue.text}\"],\"typewriterActive\":${!dialogueReveal.isComplete},\"timeline\":\"scene0-opening-first-glyph\",\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/Panel_cancel\",\"kind\":\"Panel\",\"active\":true,\"size\":[1488.3720930232557,800],\"opacity\":0},{\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"DialoguePanel\",\"active\":true,\"position\":[0,50],\"size\":[1030,260],\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/face\",\"kind\":\"Head\",\"active\":true,\"position\":[416.432,0],\"size\":[96,120],\"scale\":[2,2],\"renderedSize\":[192,240],\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/bg2\",\"kind\":\"DialoguePanelBody\",\"active\":true,\"position\":[-100.536,-12],\"size\":[796,212],\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/bg2/richtext\",\"kind\":\"RichText\",\"active\":true,\"text\":\"$textJson\",\"remainingText\":\"$remainingJson\",\"fontSize\":36,\"lineHeight\":42,\"maxWidth\":728,\"position\":[-370.945,46.734],\"size\":[728,52.92],\"anchor\":[0,1],\"opacity\":255}"
            } ?: ",{\"address\":\"Battle/Canvas/Layer\",\"kind\":\"SayLayer\",\"active\":false}"
            "dialogue-1" -> scriptRuntime.currentDialogue?.let { dialogue ->
                val speakerJson = dialogue.speakerId?.let { "\"$it\"" } ?: "null"
                val speakerNameJson = dialogue.speakerId?.toIntOrNull()
                    ?.let(gameDataCatalog::unitProfile)?.name
                    ?.let(GameDataCatalog::sayLayerUnitName)
                    ?.replace("\\", "\\\\")?.replace("\"", "\\\"")
                    ?.let { "\"$it\"" } ?: "null"
                val textJson = dialogueReveal.visibleText.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                ",{\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"SayLayer\",\"active\":true,\"speaker\":$speakerJson,\"speakerName\":$speakerNameJson,\"sourceStrings\":[\"&${dialogue.speakerId}\",\"${dialogue.text}\"],\"timeline\":\"dialogue-step-1\",\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/text\",\"kind\":\"DialogueText\",\"text\":\"$textJson\",\"visible\":true,\"fontSize\":36,\"lineHeight\":42,\"maxWidth\":728,\"position\":[-370.945,46.734],\"size\":[728,52.92],\"anchor\":[0,1],\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/bg2\",\"frame\":\"U_select_11-1\",\"asset\":\"maps/ui/dialogue-panel.png\",\"renderMode\":\"Simple/stretch\",\"opacity\":255,\"material\":\"SpriteBatch/linear\"}"
            } ?: ",{\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"SayLayer\",\"active\":false}"
            "battle-action-6-f0" -> actionAnimation?.let { action ->
                ",{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/action\",\"kind\":\"BRAnime\",\"action\":${action.sourceAction},\"direction\":${action.direction},\"active\":${animationClock() < action.endsAt},\"timeline\":\"attack6-f0\"}"
            } ?: ",{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/action\",\"kind\":\"BRAnime\",\"active\":false,\"timeline\":\"attack6-f0\"}"
            "win-condition-modal" -> ",{\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"WinConBoxLayer\",\"active\":$winConditionOpen,\"timeline\":\"open\",\"modal\":${winConditionLayer != null},\"opacity\":255}"
            "enemy-turn" -> battle.traceAiPlannerAtCurrentPoint(474, aiFlags = 1)?.let { plan ->
                val targetJson = plan.targetId?.let { "\"$it\"" } ?: "null"
                val magicJson = plan.magicId?.toString() ?: "null"
                val valueJson = plan.value?.toString() ?: "null"
                val actionValueJson = plan.actionValue?.toString() ?: "null"
                ",{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/enemy-planner#${plan.characterId}\",\"kind\":\"Battle._ai/current-point\",\"active\":true,\"sourceCharacterId\":${plan.characterId},\"ai\":${plan.ai},\"x\":${plan.x},\"y\":${plan.y},\"value\":$valueJson,\"actionValue\":$actionValueJson,\"targetId\":$targetJson,\"magicId\":$magicJson,\"timeline\":\"enemy-turn-planner\"}"
            } ?: ",{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/enemy-planner#474\",\"kind\":\"Battle._ai/current-point\",\"active\":false,\"timeline\":\"enemy-turn-planner\"}"
            "lose-result" -> ",{\"address\":\"Lose/Canvas/Logo_8-1\",\"kind\":\"LoseScene\",\"active\":${resultFlow == ResultFlow.LOSE_SCENE},\"frame\":\"Logo_8-1\",\"asset\":\"Logo/Logo_8-1\",\"frameRect\":[0,0,640,400],\"timeline\":\"Battle.lose->endProcess\"}"
            "win-result" -> ",{\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"MsgBox\",\"active\":${resultFlow == ResultFlow.WIN_SAVE_PROMPT},\"frame\":\"box3\",\"timeline\":\"Battle.enemyHide->endProcess\"},{\"address\":\"Battle/Canvas/Layer/bg0/label\",\"kind\":\"Label\",\"text\":\"게임 저장하시겠습니까?\",\"timeline\":\"save-prompt\"}"
            else -> ""
        }
        val miniMarkers = listOf(
            "img5" to Pair(0f, -42f), "img5" to Pair(-6f, -48f), "img5" to Pair(12f, -42f), "img5" to Pair(-18f, -36f), "img5" to Pair(12f, -36f),
            "img9" to Pair(-6f, -6f), "img9" to Pair(0f, -6f), "img9" to Pair(-24f, -36f), "img9" to Pair(-6f, -30f), "img9" to Pair(0f, -30f), "img9" to Pair(12f, -30f), "img9" to Pair(-6f, -24f), "img9" to Pair(0f, -24f), "img9" to Pair(-30f, 0f), "img9" to Pair(-30f, -6f), "img9" to Pair(-18f, -6f), "img9" to Pair(-12f, 0f), "img9" to Pair(12f, 0f), "img9" to Pair(12f, -12f),
        ).mapIndexed { index, (frame, point) -> val uuid = if (frame == "img5") "1ff0ac4a-8fe9-4b8e-a5f7-374a5440571a" else "98825d51-e8ff-4a12-9906-a4372e913cdd"; "{\"address\":\"Battle/Canvas/Layer/bg/map/tiled#$index\",\"frame\":\"$frame\",\"asset\":\"maps/ui/battle-smlmap-$frame.png\",\"assetUuid\":\"$uuid\",\"frameRect\":[1,1,10,10],\"localPosition\":[${point.first},${point.second}],\"parentScale\":[2,2],\"opacity\":168,\"z\":0,\"material\":\"SpriteBatch/linear\"}" }.joinToString(",")
        val naturalSay = if (returnScenario == "R_00") ",{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/New Node\",\"frame\":\"Mark_10-1\",\"asset\":\"maps/ui/battle-say.png\",\"assetUuid\":\"6e23f416-6258-4c79-9ac4-e89fc8b8df4f\",\"frameRect\":[702,2,24,24],\"localPosition\":[-96,-288],\"parentScale\":[2,2],\"opacity\":255,\"z\":1000,\"timeline\":\"SHOW_SAY(active)\",\"material\":\"SpriteBatch/linear\"}" else ""
        return "{\"state\":\"R_00/natural-battle/t=stable\",\"scenarioKey\":\"$scenarioKey\",\"oracle\":\"isolated-libgdx-runtime\",\"animationClock\":${animationClock()},\"visualAnimationClock\":$elapsed,\"records\":[{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map\",\"frame\":null,\"asset\":\"4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg\",\"uv\":\"0,1,1,1,0,0,1,0\",\"draw\":[-320,$tracedMapBottom,1920,1920],\"material\":\"SpriteBatch/linear\"},{\"address\":\"Battle/Canvas/Layer/bg/map\",\"frame\":\"Smlmap_1-1\",\"asset\":\"maps/ui/battle-smlmap-1.jpg\",\"assetUuid\":\"28fcaf09-66e0-4d64-b968-438f0b7db258\",\"frameRect\":[0,0,120,120],\"position\":[1610.3721,678],\"scale\":[2,2],\"opacity\":168,\"z\":0,\"material\":\"SpriteBatch/linear\"},{\"address\":\"Battle/Canvas/Layer/bg/weather\",\"frame\":\"weather_0\",\"asset\":\"maps/ui/battle-menu/weather_0.png\",\"frameRect\":[270,2,72,72],\"position\":[1492.3721,560],\"scale\":[0.8,0.8],\"opacity\":127,\"z\":0,\"material\":\"SpriteBatch/linear\"},{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/info/bar2/sprite\",\"frame\":\"Mark_3-1\",\"asset\":\"maps/marks/3.png#45bb9e80\",\"material\":\"SpriteBatch/linear\"},$miniMarkers$naturalSay,$unitRecords$optionalMasks$scenarioRecords],\"modal\":${battleMenuOpen},\"effectCount\":${magicEffectAnimations.size}}"
    }
    // Cocos uses a 1280×800 design canvas with SHOW_ALL.  On our 1.86:1
    // desktop window its visible world widens to 1488×800, rather than
    // shrinking the battle into a 1280×688 FitViewport.
    private val viewport = ExtendViewport(1280f, 800f, OrthographicCamera())
    private val shapes = ShapeRenderer()
    private val batch = SpriteBatch()
    /** Cocos WebGL's observed 8-bit rounded bilinear map sampler. */
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
    /** Original `unkown_effect/hight-light`, used by BattleUnit action 4. */
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
    /** Exact fragment equation from internal `builtin-2d-gray-sprite`. */
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
    private val scriptRuntime = ScenarioInterpreter.load(sourceScenario, campaign).apply {
        enableExternalBattlePresentation()
        // FightLayer methods pause the source script until their own visual
        // callback fires.  Keep that contract in production; otherwise an
        // entire duel is evaluated in one frame and only its final state can
        // ever be rendered.
        enableExternalFightPresentation()
        start("scene0")
    }
    private val fullTraceConfig = game.requestedFullBattleTrace()
    private val yingchuanEntryFlowTracePath = game.requestedYingchuanEntryFlowTracePath()
    private var yingchuanEntryFlowSawInit = false
    private var yingchuanEntryFlowWritten = false
    private val fullTraceRandom = fullTraceConfig?.let { SourceRandomStreams(it.toolSeed, it.mathSeed) }
    private val fullTraceRecorder = fullTraceConfig?.let { FullBattleTraceRecorder(it, requireNotNull(fullTraceRandom)) }
    /** Last accepted production input, retained in each frame for deadlock diagnosis. */
    private var lastFullBattleInput: String? = null
    private var lastFullBattleMenuTap: String? = null
    private val fullTraceDeadline = fullTraceConfig?.let { FullBattleTraceDeadline(it.maxSimulationSeconds) }
    private var fullTraceFinished = false
    /**
     * The source recorder snapshots the current RAF before applying its
     * three-terminal-frame completion check.  Keep the finish request until
     * [recordFullBattleTraceFrame] has persisted the corresponding LibGDX
     * frame as well.
     */
    private var fullTraceFinishAfterFrame: String? = null
    private val gameDataCatalog = GameDataCatalog.load()
    /** Battle.js constructs this overlay before BattleScreen.loadGame. */
    private val battleInitLayer = BattleInitLayer()
    /** `MenuLayer.DX → TerrainLayer`; data is the original GAME_CFG projection. */
    private val terrainLayer by lazy { gameDataCatalog.terrainLayer() }
    private val propertyLayer by lazy { PropertyLayer.fromCatalog(gameDataCatalog, campaign.inventory.items) }
    /** MenuLayer.BW → TreasureLayer: definitions come from the full source item table. */
    private val treasureLayer by lazy {
        TreasureLayer(
            gameDataCatalog.treasureProfiles().map {
                TreasureLayer.Item(it.id, it.name, it.icon, it.itemType in 26..37, "보물")
            },
            campaign.inventory.discoveredTreasures,
        )
    }
    private val battleSprites = BattleSpriteTimeline.load()
    private val magicEffects = MagicEffectCatalog.load()
    /** Live UnitInfoLayer DynamicAtlas crops remain independently demand-loaded. */
    private val unitInfoAssets = BattleUnitInfoAssets()
    // `loadBg` pauses before BG_INDEX is published. Build the native map
    // resources from its pending request, while ScenarioStage exposes the
    // new index only when the source callback actually resumes the AST.
    private val loadedBattleMapIndex = scriptRuntime.requestedBattleBackgroundMapIndex
    private val terrainGrid = BattleTerrainGrid.load(loadedBattleMapIndex).also { grid ->
        grid.resetOverlays()
        grid.applyObjectOverlays(scriptRuntime.stage.mapObjects.values)
        grid.applyFires(scriptRuntime.stage.fires.values)
    }
    private val battle = (if (verifyMode) {
        BattleScenarioFactory.tutorialBattle()
    } else {
        BattleScenarioFactory.fromScriptedUnits(
            scriptRuntime.stage.battleUnits.values,
            // Only gate objects occupy a physical map tile.  Types 0..3 are
            // terrain overlays (fire/river/etc.) in BattleScreen.setObject2;
            // treating every one as a wall made scripted maps impassable.
            scriptRuntime.stage.mapObjects.values.filter { it.enabled && it.objectId > 3 }.mapTo(linkedSetOf()) { it.x to it.y },
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
        // Stage.setGlobalData is executed by scene0 before BattleScreen builds
        // its tactical state in the source.  Apply that authored limit before
        // the first render can call outcome(); runBattleScript may still be
        // suspended by the opening delay/dialogue at that point.
        if (scriptRuntime.stage.battleMaxRoundsIncludesFeature) state.setResolvedMaxRounds(scenarioMaxRound())
        else state.setMaxRounds(scenarioMaxRound())
        scriptRuntime.stage.setBattleMovePathResolver(state::scriptedMovePath)
    }
    /** Commands emitted by stage.startFight/FightLayer, consumed strictly FIFO. */
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
    // BattleScreen._loadBg(t) loads Game/HM/HM_(t + 1)-1.  Mmap is exclusive
    // to HallLayer and produced the game's incorrect city backgrounds.
    private val mapFile = battleMapFile(loadedBattleMapIndex + 1)
    /** Source unitDeath never lets retained Stage proxies respawn a dead BattleUnit. */
    private val materializedBattleUnitIds = battle.units.keys.toMutableSet()
    private data class ScriptUnitBaseline(
        val x: Int, val y: Int, val visible: Boolean, val ai: Int,
        val targetId: Int, val targetX: Int, val targetY: Int,
    )
    /** Values before the current scene1 invocation; unchanged proxies must not overwrite tactical state. */
    private var scriptUnitBaseline: Map<Int, ScriptUnitBaseline>? = null
    /** BattleUnit.move2 schedules centerUnit at movement ticks, not on every sync/render call. */
    private val scriptedMovementCameraCursors = mutableMapOf<Int, MovementCameraTickCursor>()
    private val mapTexture: Texture? = sourceMapRawTexture() ?: mapFile
        ?.let(::Texture)
        ?.also {
            // Observed Cocos Texture2D contract for HM_1-1: linear sampling,
            // no repeat outside its SpriteFrame, normal alpha compositing.
            it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            it.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge)
        }
    /**
     * Isolated raw-parity experiment: Cocos FBO-read Texture2D rows are
     * bottom-left.  Pixmap's upload buffer is filled with rows reversed so
     * LibGDX receives the same top-origin JPEG ordering as its normal loader.
     */
    private fun sourceMapRawTexture(): Texture? {
        dumpDecodedMapTexture()
        if (game.requestedCaptureState() != "map-only") return null
        val raw = java.io.File("/Users/ain/workspace/jojo/.verification-work/natural-battle-capture/captures/source-map-texture.rgba")
        if (!raw.isFile || raw.length() != 960L * 960L * 4L) return null
        val bytes = raw.readBytes(); val pixmap = Pixmap(960, 960, Pixmap.Format.RGBA8888)
        val dst = pixmap.pixels; val stride = 960 * 4
        for (y in 0 until 960) dst.put(bytes, y * stride, stride)
        dst.flip()
        return Texture(pixmap).also { texture -> pixmap.dispose(); texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge) }
    }

    /**
     * Capture-state diagnostic only.  This reads the exact pre-upload
     * LibGDX Pixmap decoded from the map JPEG (not the source-RGBA bypass
     * above) and serializes its native RGBA8888 pixel buffer without drawing.
     */
    private fun dumpDecodedMapTexture() {
        val output = game.requestedMapTextureDumpPath() ?: return
        val input = mapFile ?: return
        val pixmap = Pixmap(input)
        try {
            check(pixmap.format == Pixmap.Format.RGBA8888) { "unexpected map pixmap format: ${pixmap.format}" }
            val buffer = pixmap.pixels.duplicate()
            buffer.clear()
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            java.io.File(output).apply { parentFile?.mkdirs(); writeBytes(bytes) }
            Gdx.app.log("JojoGame", "MAP_TEXTURE_DECODE_DUMP: $output ${pixmap.width}x${pixmap.height} bytes=${bytes.size}")
        } finally {
            pixmap.dispose()
        }
    }

    /**
     * The source `_loadBg(..., resume, 1)` callback waits for the map texture
     * and `Promise.all(unit.initNodeAvatar())`. LibGDX texture creation is
     * synchronous, so force the same assets through the cache first and only
     * then release the AST's dedicated loadBg barrier. A missing map keeps the
     * script paused, matching the source error path that never calls callback.
     */
    private fun completeBattleBackgroundLoadIfReady() {
        if (!scriptRuntime.hasPendingBattleBackgroundLoad || mapTexture == null) return
        battle.units.values.forEach(::unitTexture)
        scriptRuntime.completeBattleBackgroundLoad()
    }
    private val dynamicTextures = BattleDynamicTextureRepository()
    private val hudAssets = BattleHudAssets()
    private val captureReferenceAssets = BattleCaptureReferenceAssets()
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
    /** CreateAnime2 begins at zero for each newly attached object node. */
    private val fireAnimationStartedAt = mutableMapOf<Pair<Int, Int>, Float>()
    private val mapObjectAnimationStartedAt = mutableMapOf<Triple<Int, Int, Int>, Float>()
    private val audio = GameAudioPlayer()
    private val dialogueReveal = SourceTextReveal()
    private val battleInfoReveal = SourceTextReveal()
    private val sayAutoClose = SayLayerAutoClose()
    private val settingsPreferences by lazy { game.settingsPreferences() }
    private val font: BitmapFont = KoreanFont.create(26, buildString {
        append("전술 전투 원본 맵 라운드 아군 적군 단계 턴 최종 종료 증원군 도착 조조 병사 황건적 시나리오로 돌아가기 일대일 대결 대화 아이템 ${scriptRuntime.stage.stageName}")
        append(gameDataCatalog.allBattleNames().joinToString())
        // Dialogue speakers include generic troop names (e.g. 공병) which
        // never occur in the Python text itself. Include original UNIT names
        // so the dynamically generated bitmap font cannot silently omit a
        // leading Hangul glyph.
        append(gameDataCatalog.allUnitNames().joinToString())
        append(gameDataCatalog.allRetreatTexts().joinToString())
        append(gameDataCatalog.allBattleNames().joinToString())
        append(gameDataCatalog.terrainLayer().select(TerrainLayer.Tab.RISE).rows.joinToString { it.terrainName })
        append(gameDataCatalog.terrainLayer().select(TerrainLayer.Tab.RISE).rows.firstOrNull()?.values?.joinToString { it.armName } ?: "")
        append(Gdx.files.internal("scenarios/$sourceScenario.py").readString("UTF-8"))
        Gdx.files.internal("scenarios/R_00.py").takeIf { it.exists() }?.let { append(it.readString("UTF-8")) }
        // UnitInfoLayer's serialized labels are not all guaranteed to occur
        // in scenario text (notably `첩` in `민첩성`).
        // TerrainLayer's source labels use these glyph values rather than
        // bitmap icons. Include them explicitly so the generated font does
        // not silently emit its missing-glyph cell in the comparison table.
        append("기본 능력 무력 지력 지휘 민첩성 운기 무장 소개 인물 특기 일람 없음 출진 횟수 퇴각 ★◎○△×●--")
        // Modal prefabs own static Korean cc.Label strings which do not
        // necessarily occur in a scenario.  Keep every original-rendering
        // label in the glyph seed so no leading Hangul falls back to the
        // bitmap-font missing cell at runtime.
        append("환경 설정 클릭하여 설정해 주세요 설정 완료 후 확인을 선택해 주세요 배경 음악 듣기 효과음 듣기 전투시 전장 축소 이미지가 자동으로 표시됩니다 대화창 자동 닫음 체력 바가 유닛 위에 있습니다 텍스트 속도 느림 중간 빠름 정보 설명 자세히 보통 요약 대화창 색상")
        append("진행 상황 유지 어떤 진행 상황을 저장할지 선택해 주세요 따뜻한 알림 오래된 저장 파일일수록 앞에 표시됩니다 취소 진행도 불러오기 읽을 최신 저장 파일이 가장 위에 있습니다")
        append("보물 도감 발견되지 않음 지금까지 발견한 보물 종료 부대 정보 일람 무장명 부대 속성 레벨 체력 공격 방어 정신 폭발 사기 폐쇄 창고 일람 이름 속성 경험치 소지자 무기 방어구 보조")
        append("모든 부대의 명령을 종료하시겠습니까? 자동 전투 위임 예 아니오 취소")
    })
    private val helperGlyphLayout = GlyphLayout()
    /** SayLayer's serialized cc.Label/cc.RichText uses the Cocos default Arial 36px. */
    private val dialogueFont: BitmapFont = KoreanFont.create(36, buildString {
        append(gameDataCatalog.allUnitNames().joinToString())
        append(Gdx.files.internal("scenarios/$sourceScenario.py").readString("UTF-8"))
    })
    private val itemUpgradeFont: BitmapFont = KoreanFont.create(36, "단검유비장비Lv공격력방어력정신력 -> 0123456789")
    private var eventMessage = "턴 종료로 라운드와 이벤트를 확인하세요"
    /** Source MenuLayer, opened through Canvas/Layer/menu_button. */
    private var battleMenuOpen = false
    /** Source MenuLayer onCreate state; renderer consumes its capped progress/buttons/weather. */
    private var battleMenuLayer: MenuLayer? = null
    /** Cocos starts Weather_n's AnimationClip when MenuLayer is created. */
    private var battleMenuOpenedAt = 0f
    private var battleMenuPressedIndex: Int? = null
    /** BattleScreen.END_ROUND -> MsgBox4 -> optional TuoGuanLayer. */
    private val autoBattlePreferences by lazy { game.preferences("jojo-auto-battle") }
    private val autoBattleFlow by lazy {
        // Deterministic traces must neither inherit nor rewrite a developer's
        // persisted entrusted-battle preference. The production input driver
        // still toggles and confirms the real visible control.
        AutoBattleFlow(fullTraceConfig == null && autoBattlePreferences.getInteger("TUOGUAN", 0) == 1)
    }
    private var autoBattleRouteInstalled = false
    private var autoBattlePressedTag: Int? = null
    private var autoBattleTogglePressed = false
    private var autoBattlePanelPressed = false
    /** TerrainLayer owns input above both map and MenuLayer until button2/ESC. */
    private var terrainLayerOpen = false
    private var propertyLayerOpen = false
    private var treasureLayerOpen = false
    /** TreasureLayer ScrollView state and the source clickItem → ItemLayer payload. */
    private var treasureScrollRow = 0
    private var treasureSelectedId: Int? = null
    /** MenuLayer.CD → SaveLayer; repository maps directly to numbered Manager slots. */
    private val saveLayer by lazy {
        SaveLayer(object : SaveLayer.Repository {
            override fun load(index: Int): String? = game.savedCampaignSlot(index)
            override fun save(index: Int) { game.saveCampaign(index) }
        })
    }
    private var saveLayerOpen = false
    private var saveScrollRow = 0
    private var savePressedSlot: Int? = null
    private var saveConfirmPressed: Int? = null
    private val settingLayer by lazy { SettingLayer(object : SettingLayer.Store { private val p=settingsPreferences; override fun getInt(key:String,default:Int)=p.getInteger(key,default); override fun putInt(key:String,value:Int){p.putInteger(key,value).flush()} }, featureEnvironment = { game.settingFeatureEnvironment("Battle") }) }
    private var settingLayerOpen=false
    private var settingPress: Pair<Float, Float>? = null
    /** MenuLayer.DD → LoadGameLayer; separate state so SaveLayer is untouched. */
    private val loadGameLayer by lazy {
        LoadGameLayer(object : LoadGameLayer.Repository {
            override fun load(index: Int) = game.loadCampaignSlot(index)
            override fun savedPage() = game.savedLoadPage()
            override fun savePage(page: Int) = game.saveLoadPage(page)
            override fun featureEnabled(name: String) = name == "ZDBHSW"
            override fun versionCode() = 1
            override fun restore(index: Int, raw: String, route: LoadGameLayer.RestoreRoute) = game.restoreCampaignSlot(index, raw, route)
        })
    }
    private var loadGameLayerOpen = false
    private var loadScrollRow = 0
    private var loadPressedSlot: Int? = null
    private var loadConfirmPressed: Int? = null
    /** MenuLayer.WJYL → ForcesListLayer. */
    private var forcesLayer: ForcesListLayer? = null
    private var forcesPressedRow: Int? = null
    private var forcesPressedTab: Int? = null
    private var forcesClosePressed = false
    private var unitInfoLayer: UnitInfoLayer? = null
    private var unitInfoPressed: Int? = null
    private var jiqiLayer: JiQiLayer? = null
    private var jiqiPressed = false
    private var jiqiRouteInstalled = false
    private val jiqiRouteFixture = game.requestedCaptureState() == "battle-jiqi-fixture"
    private var magickListLayer: MagicUiList? = null
    private var magickInfoLayer: MagicInfoLayer? = null
    private var magickPressedRow: Int? = null
    private var magickPressedAt = 0f
    private var magickCancelPressed = false
    private var magickInfoSuppressRelease = false
    private var magickRouteInstalled = false
    private val magickRouteState = game.requestedCaptureState().takeIf {
        it == "battle-magick-list-fixture" || it == "battle-magick-detail-fixture"
    }
    private var usePropertyLayer: UsePropertyLayer? = null
    private var usePropertyDetail: UsePropertyLayer.Property? = null
    private var usePropertyPressedRow: Int? = null
    private var usePropertyCancelPressed = false
    private var usePropertyPanelPressed = false
    private var usePropertyDetailSuppressRelease = false
    private var usePropertyRouteInstalled = false
    private val usePropertyRouteState = game.requestedCaptureState().takeIf {
        it in setOf(
            "battle-use-property-list-fixture", "battle-use-property-detail-fixture",
            "battle-use-property-select-fixture", "battle-use-property-cancel-fixture",
        )
    }
    private var propertyScrollRow = 0
    private var propertySelectedId: Int? = null
    /** ScrollView content offset expressed as source TerrainLayer row index. */
    private var terrainScrollRow = 0
    /** Global/scene/HelperLayer, opened by MenuLayer.HELP. */
    private var helperLayer: HelperLayer? = null
    private var helperButtonPressed = false
    /** MenuLayer.SLTJ dispatches WIN_CONDITION, which opens WinConBoxLayer. */
    private var winConditionOpen = false
    private var scriptWinConditions: WinConditionsLayer? = null
    /** Stateful, source-order implementation of the modal instantiated by MenuLayer.SLTJ. */
    private var winConditionLayer: WinConBoxLayer? = null
    /** A Cocos button listener receives TOUCH_END only after a press began on its node. */
    private var winConditionButtonPressed = false
    /** BattleScreen._noActionIndex, advanced by MenuLayer button11. */
    private var noActionIndex = 0
    /** ScrollView content translation used by source centerUnit/_contains. */
    // StageLayer.loadBg sizes the ScrollView content from the loaded map.
    // Keeping BattleCamera at S_00's 20x20 default clamps production drags
    // before an authored stage.center call; S_52 (20x24) and S_57 (40x40)
    // then cannot bring their starting force on screen at all.
    private val battleCamera = BattleCamera(
        mapWidth = terrainGrid.width * 96f,
        mapHeight = terrainGrid.height * 96f,
    )
    private var battleDragLast: Vector2? = null
    private var battleTouchStart: Vector2? = null
    private var battleTouchMoved = false
    /** Source `_state_meff` clip begins whenever its selected texture pair changes. */
    private val battleStateAnimationStarts = mutableMapOf<String, Pair<List<Int>, Float>>()
    private var elapsed = 0f
    private var fullTraceLastDriverAt = Float.NEGATIVE_INFINITY
    private var fullTraceInitialPlayerScriptRan = false
    private var fullTraceTerminalFrames = 0
    private var fullTraceMapObjectRevision = 0
    private var fullTraceMapObjectSignature: String? = null
    /** Next append-only ScenarioStage.setObjects call not yet written to the trace. */
    private var fullTraceMapObjectsCallCursor = 0
    /** BattleScreen pauses its unit/effect AnimationStates while SayLayer owns input. */
    private var battleElapsed = 0f
    /**
     * Dialogue instance whose source `_resetPos` side effect was applied.
     * SayLayer performs this once when it consumes each `&unitId` marker,
     * rather than continuously following the actor while text is visible.
     */
    private var positionedDialogueRevision = -1L
    private var actionAnimation: UnitActionAnimation? = null
    /**
     * End of the finite playAtkAnime callback started by stage.attackAction.
     * This is deliberately separate from [actionAnimation]: unit.setAction
     * is callback-free and may leave a looping pose alive across startOper.
     */
    private var scriptedAttackCallbackEndsAt = Float.NEGATIVE_INFINITY
    /** BattleUnit.move2's currently running start-inclusive route. */
    private var movementAnimation: UnitMoveAnimation? = null
    /** TPGJ owns a separate .08s victim move while the hurt clip is running. */
    private val backMoveAnimations = mutableMapOf<String, BackMoveAnimation>()
    /** BattleScreen resumes scene/event processing only after attack animations finish. */
    private var pendingBattleScriptPassesAfterAction = 0
    private var pendingBattleActionCommitted = false
    private var pendingBattleSettlementActorId: String? = null
    private var pendingBattleCompletedScriptPasses = 0
    /** Target-side SHOU_GONG_JI3 (anime32) reactions keyed by battle unit. */
    private val hitReactionAnimations = mutableMapOf<String, UnitActionAnimation>()
    /** Source unitHide(DEATH) starts only after SHOU_GONG_JI3 completes. */
    private val deathAnimations = mutableMapOf<String, UnitActionAnimation>()
    private val pendingDeathAnimations = linkedMapOf<String, PendingDeathAnimation>()
    private var postActionDeathsStarted = false
    private var activeUnitDeath: ActiveUnitDeath? = null
    private var unitDeathAwaitingDialogue: PendingDeathAnimation? = null
    /** Lifecycle-owned unitDeath barrier (state, restore, or round script). */
    private var turnDeathCheckpoint: BattleTurnController.DeathCheckpoint? = null
    private var turnDeathStage = TurnDeathStage.NONE
    private var activeTurnSettlement: ActiveTurnSettlement? = null
    private var settlementInfoOverlay: SettlementInfoOverlay? = null
    private var settlementInfo2Overlay: SettlementInfo2Overlay? = null
    private var activeScriptedHide: ActiveScriptedHide? = null
    private var scriptedHideAwaitingDialogue: PendingScriptedHide? = null
    private var activeScriptedShow: ActiveScriptedShow? = null
    /** One `loadUnitPicture` completion edge for BattleUnit.setPosts. */
    private var activeScriptedUnitPosts: ActiveScriptedUnitPosts? = null
    /** BattleUnit._avatar remains old until loadAvatar's async completion. */
    private val loadedBattleAvatarIds = mutableMapOf<String, Int>()
    private var activeMapPresentation: ActiveMapPresentation? = null
    private var activeScriptPresentation: ActiveScriptPresentation? = null
    private var activeScriptedUnitAction: ActiveScriptedUnitAction? = null
    /** Harm-number animation values visible from the attack `hit` event onward. */
    private val harmNumberAnimations = mutableMapOf<String, HarmNumberAnimation>()
    /** Source HP presentation begins at the authored hit event. */
    private val healthTimeline = BattleHealthPresentation()
    /** Keeps post-hit HP visible while source ZDSY is awaiting its callback. */
    private val healthTimelineHoldUntil = mutableMapOf<String, Float>()
    /** Live-model writes owned by authored Animation callbacks, ordered by source time. */
    private val timedBattleMutations = mutableListOf<TimedBattleMutation>()
    /** _attack6 counterattack starts only after the first target reaction completes. */
    private var queuedCounterPresentation: CounterPresentation? = null
    private var queuedFollowUpPresentation: FollowUpPresentation? = null
    private var queuedCounterFollowUpPresentation: CounterFollowUpPresentation? = null
    /** Source `_attack2` invocations remaining after the currently visible pass. */
    private var queuedPhysicalPresentation: PhysicalPassPresentationQueue? = null
    private var queuedMagicPresentation: MagicPassPresentationQueue? = null
    private var pendingCriticalSpeechAction: PendingCriticalSpeechAction? = null
    private var activeCounterMagicPresentation: ActiveCounterMagicPresentation? = null
    /** Persistent BattleUnit.setAction() state; source keeps it until action 0 replaces it. */
    private val scriptedUnitVisuals = mutableMapOf<String, ScriptedUnitVisual>()
    /** One entry per `_magicProcess → playMeff` group (CLLJ has two). */
    private val magicEffectAnimations = mutableListOf<MagicEffectAnimation>()
    private var selectedUnitId: String? = null
    private val battleCommandFlow = BattleCommandFlow()
    private var battleCommandRouteInstalled = false
    private var battleCommandPressedTag: Int? = null
    private var pendingBattleCommandUnit: String? = null
    private var pendingBattleCommandScriptStarted = false
    private var pendingBattleCommandMoveProvenance: String? = null
    /** Source `_ai2` awaits one actor's move/action callbacks before sorting the next. */
    private var activeAiCamp: Faction? = null
    private val emptyAiCampFrameBarrier = EmptyAiCampFrameBarrier()
    private val committedPlayerMoveFrameBarrier = CommittedPlayerMoveFrameBarrier()
    private val actionStatusFrameBarrier = ActionStatusFrameBarrier()
    private val counterattackSettlementFrameBarrier = CounterattackSettlementFrameBarrier()
    private val scriptedMovementCampTransitionFrameBarrier = ScriptedMovementCampTransitionFrameBarrier()
    private val consecutiveNoResultFrameGate = ConsecutiveNoResultFrameGate()
    private var pendingAiResolution: AiUnitResolution? = null
    private var pendingAiActionStarted = false
    private var pendingAiPlayerMoveScriptStarted = false
    private var pendingAiUnitDeathScriptPass = 0
    private var pendingAiActionCommitted = false
    private var campaignE2ePlayerMoveCommitted = false
    private var campaignE2eCommittedPlayerMove: String? = null
    private val authoredMechanicRoute = AuthoredMechanicRouteTracker(sourceScenario)
    private var menuHudButtonPressed = false
    private enum class AiPresentationStage { FOCUS_DELAY, MOVING, ACTION_DELAY, ACTION, COMPLETE }
    private var aiPresentationStage = AiPresentationStage.COMPLETE
    private var aiPresentationStageStartedAt = 0f
    private var aiTurnMoves = 0
    private var aiTurnAttacks = 0
    private var aiTurnHolds = 0
    private var magicMode = false
    private var selectedMagicIndex = 0
    private var propertyMode = false
    private var selectedPropertyIndex = 0
    /** Source RoundLayer lives only while BattleTurnController awaits its callback. */
    private var activeRoundLayer: RoundLayer? = null
    private var activeRoundLayerElapsed = 0f
    private val turnController: BattleTurnController by lazy {
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
                beginVisibleAiTurn(camp)
            },
            hasPendingAiPresentation = { activeAiCamp != null },
            presentCampState = { settlement -> presentTurnSettlement(settlement) },
            presentDeaths = { checkpoint -> beginTurnDeathPresentation(checkpoint) },
            presentCampRestore = { settlement -> presentTurnSettlement(settlement) },
            runRoundScript = {
                runBattleScript()
                scriptRuntime.state == PlaybackState.COMPLETE
            },
            // Source addRound publishes the incremented round while curCamp
            // is still REINFORCEMENTS. Even an empty scene callback resumes
            // on the following scheduler turn, so never collapse this edge
            // into the same LibGDX render call.
            deferSynchronousRoundScriptCompletion = true,
            presentWeather = { transition ->
                if (transition.changed) eventMessage = "날씨: ${transition.current.label()}"
                true
            },
            onCampEvents = { turn -> showTurnResult(turn, "") },
            initialPhase = if (bootstrapPhase == BattleBootstrapPhase.COMPLETE) {
                BattleTurnController.Phase.PLAYER_INPUT
            } else {
                BattleTurnController.Phase.BOOTSTRAP
            },
        )
    }
    /** Opening scene commands have completed before the first capture frame. */
    private var presentationReady = false
    private val actionCapture = requestedActionCapture()
    private val actionCaptureMode = actionCapture != null
    private val cutsceneAttackCapture = game.requestedCaptureState() == "yingchuan-attack"
    private val cutscenePostHitCapture = game.requestedCaptureState() == "yingchuan-action4"
    private val cutscene477Capture = game.requestedCaptureState() == "yingchuan-477"
    /** Actual S_00 BattleScreen -> Stage.say -> SayLayer blend/order route. */
    private val battleDialogueBlendRoute = game.requestedCaptureState() == "battle-dialogue-blending-fixture"
    private val battleInitRoute = game.requestedCaptureState() == "battle-init-fixture"
    /** Actual menu_button -> MenuLayer button6 -> TerrainLayer route. */
    private val battleTerrainRoute = game.requestedCaptureState() == "battle-terrain-layer-fixture"
    /** Actual BattleScreen menu_button -> MenuLayer id14 open state. */
    private val battleMenuRoute = game.requestedCaptureState() == "battle-menu-fixture"
    /** Source-harness dialogue input count: `yingchuan-dialogue-1` is the first fully revealed say. */
    private val dialogueStepCapture = game.requestedCaptureState()
        ?.removePrefix("yingchuan-dialogue-")
        ?.takeIf { game.requestedCaptureState()?.startsWith("yingchuan-dialogue-") == true }
        ?.toIntOrNull()
    /**
     * Cumulative SayLayer composition oracle.  These states never draw a
     * source-frame texture: they exercise the ordinary Kotlin panel, portrait
     * and label code while progressively restoring the battle scene.
     */
    private val dialogueComponentStage = game.requestedCaptureState()
        ?.removePrefix("yingchuan-dialogue-components-")
        ?.takeIf { game.requestedCaptureState()?.startsWith("yingchuan-dialogue-components-") == true }
    /** Matches the original verifier's map-only diagnostic: no map children or HUD. */
    private val mapOnlyCapture = game.requestedCaptureState() == "map-only"
    /** Read-only counterpart to the source Control._process selection fixture. */
    private val selectionOverlayCapture = game.requestedCaptureState() == "yingchuan-selection"
    /** Source menu-route captures dismiss 474's opening SayLayer before opening a modal. */
    private val modalRenderCapture = game.requestedCaptureState() in setOf("yingchuan-terrain", "yingchuan-property", "yingchuan-treasure", "yingchuan-setting", "yingchuan-save", "yingchuan-load", "yingchuan-forces")
    /** Set only after S_00's own opening delay reaches its first say. */
    private var cutsceneAttackStartedAt: Float? = null
    private var cutscene477StartedAt: Float? = null
    private var actionCaptureLogged = false
    private var dialogueCaptureLogged = false
    private var selectionCaptureLogged = false
    private var dialogueStepStartedAt: Float? = null
    private var dialogueStepInputs = 0
    private var modalCaptureOpeningSayDismissed = false
    private var boardLeft = 120f
    private var boardBottom = 130f
    private var boardTile = 64f
    private var boardMaxX = 1
    private var boardMaxY = 1

    /** Action regression captures deliberately drive an isolated BRAnime while SayLayer is open. */
    private fun animationClock(): Float = when {
        // The source actual-route harness stops every live idle clip at time
        // zero before its draw inventory. Apply the identical renderer state
        // to the game's real draw pass, not merely to event serialization.
        rewardRouteState != null -> 0f
        winConditionRouteState != null -> 0f
        actionCaptureMode -> elapsed
        else -> battleElapsed
    }

    /**
     * Cocos' CreateAnime2 adds an ordinary cc.Animation component to each map
     * object. BattleScreen.pause() pauses only its script, so these loops keep
     * advancing while SayLayer owns input. Keep that clock separate from the
     * combat-action clock, which intentionally pauses during dialogue.
     */
    private fun mapObjectAnimationClock(): Float = when {
        rewardRouteState != null -> 0f
        winConditionRouteState != null -> 0f
        else -> elapsed
    }

    /**
     * `anime_state` is an ordinary cc.Animation child. StageLayer.pause()
     * only delegates to `_script.pause()` and does not pause node animation,
     * so status effects keep advancing while SayLayer owns input.
     */
    private fun stateEffectAnimationClock(): Float = when {
        rewardRouteState != null -> 0f
        winConditionRouteState != null -> 0f
        else -> elapsed
    }

    /**
     * Prefer the lossless PNG read back from the original Cocos Texture2D.
     * It preserves Chromium's progressive-JPEG decode values exactly; loading
     * the sibling JPEG through STB changes chroma values before rendering.
     */
    private fun battleMapFile(index: Int) = sequenceOf("png", "jpg", "webp")
        .map { Gdx.files.internal("maps/battle-maps/$index.$it") }
        .firstOrNull { it.exists() }

    init {
        Gdx.app.log("JojoGame", "BATTLE_MAP_SOURCE: ${mapFile?.path()}")
        runBattleScript()
        battleEdit2RouteState?.let { route ->
            // Actual MenuLayer BJ contract: feature gate opens layer 23, then
            // the authored controls mutate its pending model or push Global120.
            battleEdit2 = BattleEditLayer2(battle.weather.ordinal, battle.round, canApplyRound = true).also { edit ->
                when (route) {
                    BattleEditLayer2Route.INITIAL -> Unit
                    BattleEditLayer2Route.WEATHER -> { edit.openWeatherPanel(); edit.selectWeather(3) }
                    BattleEditLayer2Route.ROUND -> { edit.textChanged("8"); edit.editingDidEnd() }
                    BattleEditLayer2Route.APPLY -> {
                        edit.selectWeather(3); edit.textChanged("8"); edit.editingDidEnd()
                        edit.touchButton(0).forEach { effect -> when (effect) {
                            is BattleEditLayer2.Effect.SetWeather -> battle.applyEditedWeather(effect.value)
                            is BattleEditLayer2.Effect.SetRound -> battle.applyEditedRound(effect.value)
                            else -> Unit
                        } }
                    }
                    BattleEditLayer2Route.CHILD, BattleEditLayer2Route.CHILD_SCENE, BattleEditLayer2Route.REGISTER -> {
                        battleEdit3Open = edit.touchButton(2).contains(BattleEditLayer2.Effect.OpenGlobalEditor)
                        battleEdit3ScenePanelOpen = route == BattleEditLayer2Route.CHILD_SCENE
                        if (route == BattleEditLayer2Route.REGISTER) {
                            battleRegisterRoute = BattleRegisterRoute().also { secret -> repeat(6) { secret.titleTouchEnd() } }
                            check(requireNotNull(battleRegisterRoute).view().registerAttached)
                        }
                    }
                }
            }
        }
        when (game.requestedCaptureState()) {
            "yingchuan-reward-basic-route", "yingchuan-reward-card1-route", "yingchuan-reward-card2-route" -> {
                val cards = if (game.requestedCaptureState() == "yingchuan-reward-basic-route") emptyList() else listOf(150, 0, 151, 0)
                scriptRuntime.stage.reward(items = cards)
                scriptRuntime.stage.scriptedBattleOutcome?.let(battle::setScriptedOutcome)
                openRewardRequestIfNeeded()
                if (game.requestedCaptureState() != "yingchuan-reward-basic-route") advanceRewardFlow()
                if (game.requestedCaptureState() == "yingchuan-reward-card2-route") advanceRewardFlow()
            }
            else -> Unit
        }
        // Deterministic visual-regression state: mirrors opening
        // Canvas/Layer/menu_button without requiring synthetic mouse input.
        // Keep capture states under the yingchuan-* namespace so JojoGame
        // prepares the same fresh R_00 campaign route as the source harness.
        if (game.requestedCaptureState() == "yingchuan-menu") openBattleMenu()
        if (battleMenuRoute) openBattleMenu()
        if (otherUnitInfoRoute) {
            val unit = requireNotNull(battle.units.values.firstOrNull { it.characterId == 210 && it.visible }) {
                "R_00 OtherUnitInfoLayer production unit 210 is missing"
            }
            otherUnitInfoLayer = OtherUnitInfoLayer().also {
                // Battle appends a numeric suffix to duplicate generic
                // actors for internal identity; source Unit.name() does not.
                it.onCreate(unit, gameDataCatalog.postsName(unit.posts), unit.name.replace(Regex("\\d+$"), ""))
            }
        }
        if (mineUnitInfoRoute) {
            val unit=requireNotNull(battle.units.values.firstOrNull { it.characterId==210&&it.visible })
            mineUnitInfoLayer=MineUnitInfoLayer().also { it.onCreate(unit,gameDataCatalog.postsName(unit.posts),unit.name.replace(Regex("\\d+$"),"")) }
        }
        if (game.requestedCaptureState() == "yingchuan-helper") openHelperLayer()
        if (game.requestedCaptureState() == "yingchuan-terrain") {
            terrainLayer.select(TerrainLayer.Tab.RISE)
            terrainScrollRow = 0
            terrainLayerOpen = true
        }
        if (battleTerrainRoute) {
            openBattleMenu()
            handleBattleMenuTap(6)
        }
        if (game.requestedCaptureState() == "yingchuan-property") {
            propertyLayer.select(PropertyLayer.Tab.WEAPON)
            propertyScrollRow = 0
            propertySelectedId = null
            propertyLayerOpen = true
        }
        if (game.requestedCaptureState() == "yingchuan-treasure") {
            treasureScrollRow = 0
            treasureSelectedId = null
            treasureLayerOpen = true
        }
        if (game.requestedCaptureState() == "yingchuan-setting") {
            settingLayer.onCreate()
            settingLayerOpen = true
        }
        if (game.requestedCaptureState() == "yingchuan-save") {
            saveLayer.onCreate()
            saveScrollRow = 0
            saveLayerOpen = true
        }
        if (game.requestedCaptureState() == "yingchuan-load") {
            loadGameLayer.onCreate()
            loadScrollRow = 0
            loadGameLayerOpen = true
        }
        if (game.requestedCaptureState() == "yingchuan-forces") openForcesListLayer()
        // Mirrors the source Electron WinConBoxLayer fixture without a
        // synthetic desktop click; used only for framebuffer comparison.
        if (game.requestedCaptureState() == "yingchuan-win-condition") openWinConditionBox()
        when (winConditionRouteState) {
            "battle-win-condition-compact-fixture" -> {
                // Actual MenuLayer command path: open menu, then dispatch its
                // authored SLTJ command instead of constructing id20 directly.
                openBattleMenu()
                handleBattleMenuTap(9)
                // Source actual menu entry occurs after the opening Say
                // sequence has restored 235 from scripted action4 to its
                // ordinary idle action.
                battle.units.values.firstOrNull { it.characterId == 235 }?.let { scriptedUnitVisuals.remove(it.id) }
            }
            "battle-win-condition-full-fixture" -> {
                battle.units.values.firstOrNull { it.characterId == 235 }?.let { scriptedUnitVisuals.remove(it.id) }
                scriptRuntime.suspendForWinCondition("장보와 장량을\n격퇴하십시오.")
            }
        }
        // scene0 is already evaluated while ScenarioInterpreter is created.
        // It may have queued setUnitStatus/setUnitPos commands before the
        // Battle existed, so consume those commands once before the
        // first rendered frame (not only after dialogue input).
        if (!verifyMode) syncScriptedUnits()
        if (selectionOverlayCapture) {
            // Source fixture selects S_00 BattleUnit index=43: character 210
            // at (10,17).  Bind by source character ID so the game remains
            // correct if its internal Battle map key changes.
            selectedUnitId = battle.units.values.firstOrNull { it.characterId == 210 && it.visible }?.id
            check(selectedUnitId != null) { "S_00 selection fixture unit 210 is missing" }
        }
        when (game.requestedCaptureState()) {
            // Source loss marks its own Battle state then enters `_endProcess`.
            // Setting the real Battle max-round contract makes outcome() use
            // the same loss branch without injecting an output record.
            "lose-result" -> { battle.setMaxRounds(1); resultFlow = ResultFlow.LOSE_SCENE }
            LOSE_RESTART_ROUTE_STATE -> {
                battle.setMaxRounds(1)
                enterLoseScene()
            }
            // Source win retires the actual live enemy BattleUnits before its
            // `_endProcess` save prompt.  Preserve candidate presentation
            // units while removing enemies from the real outcome set.
            "win-result" -> {
                battle.units.values.filter { it.type().isEnemySide() }.forEach { it.visible = false }
                // Source `_endProcess` branches on its explicit LOSE flag,
                // not on `loseTest()`: the real CDP R_00 fixture has no
                // controllable Mine unit, yet enemy retirement still opens
                // the save MsgBox while LOSE remains false.
                resultFlow = ResultFlow.WIN_SAVE_PROMPT
            }
        }
        if (game.requestedCaptureState() == "yingchuan-unit-info") {
            // Same unit origin as the source fixture: ForcesList sorted first
            // row, then its own TOUCH_END opens UnitInfoLayer.
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
        // BattleInitLayer.onCreate dispatches BATTLE_INIT_START; Battle.js
        // owns the subsequent loadGame transition, so only its contract is
        // invoked here. BATTLE_LOAD_BGMAP supplies the source SHOP name.
        battleInitLayer.onCreate(0)
        battleInitLayer.onLoadBgMap(gameDataCatalog.battleName(scriptRuntime.stage.battleMapIndex))
        initializeMiniMap()
        presentationReady = true
        // Deterministic source-event fixture: after the opening 474 speech,
        // S_00 emits attackAction, delay, then unit(235).setAction(4) before
        // opening its next dialogue.  Keep this state capturable without a
        // synthetic desktop click so its BRAnime frame can be compared.
        actionCapture?.let { capture ->
            // Captures occur just after elapsed=1s.  Pin the source action's
            // start to the corresponding BRAnime sample time so screenshots
            // are repeatable instead of depending on desktop frame pacing.
            // Match the source action fixture's on-screen opening unit. The
            // prior (5,10) fixture was outside the ScrollView viewport, so
            // its sampled frame could not be assessed from the framebuffer.
            battle.units.values.firstOrNull { it.tileX == 10 && it.tileY == 14 }?.let { unit ->
                check(attackTexture(unit) != null) { "S_00 action capture source avatar has no Unit_atk atlas: ${unit.characterId}" }
                Gdx.app.log("JojoGame", "ACTION_CAPTURE_UNIT: id=${unit.characterId}, avatar=${battleAvatarId(unit)}, action=${capture.action}, sample=${capture.sample}")
                actionAnimation = sourceActionAnimation(unit.id, capture.action, 2, 1f - capture.sample)
            }
        }
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun keyDown(keycode: Int): Boolean {
                // SayLayer is the newest script modal. It must receive input
                // before an older presentation overlay which is completing in
                // an adjacent render frame.
                if (BattleInteractiveInput.route(scriptRuntime.state, turnController.phase) ==
                    BattleInteractiveInput.Route.DIALOGUE
                ) {
                    if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) advanceBattleDialogue()
                    return true
                }
                if (settlementInfo2Overlay != null && keycode in setOf(Input.Keys.ENTER, Input.Keys.SPACE, Input.Keys.ESCAPE)) {
                    closeSettlementInfo2()
                    return true
                }
                if (activeRoundLayer != null) return true
                if (loseSceneFlow != null) return true
                if (resultFlow == ResultFlow.WIN_SAVE_PROMPT) return true
                // HelperLayer.js has no keyboard listener.  Its modal blocks
                // map input, while only button0's TOUCH_END can remove it.
                if (helperLayer != null) return true
                if (settingLayerOpen) { if (keycode == Input.Keys.ESCAPE) closeSettingLayer(); return true }
                if (saveLayerOpen) { if (keycode == Input.Keys.ESCAPE) closeSaveLayer(); return true }
                if (forcesLayer != null) return true
                if (unitInfoLayer != null) return true
                if (loadGameLayerOpen) return true // LoadGameLayer has only TOUCH_END handlers.
                rewardFlow?.let {
                    if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) advanceRewardFlow()
                    return true
                }
                itemUpgradeFlow?.let {
                    if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) closeItemUpgrade()
                    return true
                }
                if (treasureLayerOpen) {
                    when (keycode) {
                        Input.Keys.ESCAPE -> treasureLayerOpen = false
                        Input.Keys.UP -> treasureScrollRow = (treasureScrollRow - 1).coerceAtLeast(0)
                        Input.Keys.DOWN -> treasureScrollRow++
                    }
                    return true
                }
                if (propertyLayerOpen) {
                    when (keycode) {
                        Input.Keys.ESCAPE -> propertyLayerOpen = false
                        Input.Keys.NUM_1 -> propertyLayer.select(PropertyLayer.Tab.WEAPON)
                        Input.Keys.NUM_2 -> propertyLayer.select(PropertyLayer.Tab.ARMOR)
                        Input.Keys.NUM_3 -> propertyLayer.select(PropertyLayer.Tab.AUXILIARY)
                        Input.Keys.NUM_4, Input.Keys.TAB -> propertyLayer.select(PropertyLayer.Tab.PROPERTY)
                        Input.Keys.UP -> propertyScrollRow = (propertyScrollRow - 1).coerceAtLeast(0)
                        Input.Keys.DOWN -> propertyScrollRow++
                    }
                    return true
                }
                if (terrainLayerOpen) {
                    when (keycode) {
                        Input.Keys.ESCAPE -> terrainLayerOpen = false
                        Input.Keys.NUM_1 -> terrainLayer.select(TerrainLayer.Tab.RISE)
                        Input.Keys.NUM_2, Input.Keys.TAB -> terrainLayer.select(TerrainLayer.Tab.EXPEND)
                        Input.Keys.UP -> terrainScrollRow = (terrainScrollRow - 1).coerceAtLeast(0)
                        Input.Keys.DOWN -> terrainScrollRow++
                    }
                    return true
                }
                if (scriptRuntime.state == PlaybackState.CHOICE) {
                    when (keycode) {
                        Input.Keys.UP -> scriptRuntime.selectPrevious()
                        Input.Keys.DOWN -> scriptRuntime.selectNext()
                        Input.Keys.ENTER, Input.Keys.SPACE -> confirmBattleChoice()
                    }
                    return true
                }
                // Script DELAY/MODAL is BattleScreen.pause(), not idle player
                // time.  Swallow the key here so Enter/Space cannot end the
                // turn while a source coroutine is still suspended.
                if (BattleInteractiveInput.route(scriptRuntime.state, turnController.phase) !=
                    BattleInteractiveInput.Route.PLAYER_INPUT
                ) return true
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
                        if (battle.outcome() == null) endTurn() else continueAfterOutcome()
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
                // Match the keyboard ownership rule: a live SayLayer is never
                // allowed to sit behind a callback-owned card for pointer input.
                if (BattleInteractiveInput.route(scriptRuntime.state, turnController.phase) ==
                    BattleInteractiveInput.Route.DIALOGUE
                ) {
                    advanceBattleDialogue()
                    return true
                }
                if (settlementInfo2Overlay != null) {
                    closeSettlementInfo2()
                    return true
                }
                if (activeRoundLayer != null) return true
                val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                if (resultFlow == ResultFlow.WIN_SAVE_PROMPT) {
                    victorySaveAnswerPressed = victorySaveAnswerAt(world.x, world.y)
                    return true
                }
                if (scriptRuntime.state == PlaybackState.MODAL &&
                    scriptRuntime.currentModalKind == ScenarioInterpreter.ModalKind.INFO
                ) {
                    battleInfoPanelPressed = true
                    return true
                }
                loseSceneFlow?.let { flow ->
                    losePressedAnswer = if (flow.state == LoseSceneFlow.State.PROMPT) loseAnswerAt(world.x, world.y) else null
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
                rewardFlow?.let { advanceRewardFlow(); return true }
                itemUpgradeFlow?.let { closeItemUpgrade(); return true }
                scriptWinConditions?.let { return true }
                unitInfoLayer?.let {
                    unitInfoPressed=when { world.x in 700.71f..810.71f && world.y in 17.207f..67.207f -> 9; world.x in 970f..1140f && world.y in 35f..95f -> 5; world.x in 1140f..1280f && world.y in 35f..95f -> 6; world.x in 760f..870f && world.y in 35f..95f -> 7; world.x in 80f..850f && world.y in 650f..720f -> ((world.x-80f)/150f).toInt(); else -> null };return true
                }
                forcesLayer?.let { layer ->
                    forcesClosePressed = world.x in 1135f..1310f && world.y in 88f..145f
                    forcesPressedTab = if (layer.view().tabsVisible && world.y in 88f..145f) when { world.x in 215f..345f -> 0; world.x in 360f..495f -> 1; else -> null } else null
                    forcesPressedRow = if (!forcesClosePressed && forcesPressedTab == null && world.x in 170f..1318f && world.y in 150f..614f) ((614f-world.y)/60f).toInt() else null
                    return true
                }
                if (helperLayer != null) {
                    helperButtonPressed = world.x in 1172.451f..1320.051f && world.y in 33.187f..89.187f
                    return true
                }
                if(settingLayerOpen){settingPress=world.x to world.y;return true}
                if (saveLayerOpen) {
                    saveConfirmPressed = saveConfirmAt(world.x, world.y)
                    savePressedSlot = if (saveConfirmPressed == null) saveSlotAt(world.x, world.y) else null
                    return true
                }
                if (loadGameLayerOpen) {
                    loadConfirmPressed = loadConfirmAt(world.x, world.y)
                    loadPressedSlot = if (loadConfirmPressed == null) loadSlotAt(world.x, world.y) else null
                    return true
                }
                if (treasureLayerOpen) { handleTreasureLayerTap(world.x, world.y); return true }
                if (propertyLayerOpen) { handlePropertyLayerTap(world.x, world.y); return true }
                if (terrainLayerOpen) {
                    handleTerrainLayerTap(world.x, world.y)
                    return true
                }
                if (winConditionOpen) {
                    // WinConBoxLayer only installs a TOUCH_END handler on its
                    // confirmation button; a tap elsewhere is swallowed by the
                    // modal but does not dismiss it.
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
                // DELAY/MODAL and non-player camp phases retain the map's
                // visual input listener in Cocos, but BattleScreen is paused;
                // they consume the event without selecting/moving a unit.
                if (BattleInteractiveInput.route(scriptRuntime.state, turnController.phase) !=
                    BattleInteractiveInput.Route.PLAYER_INPUT
                ) return true
                if (!battleMenuOpen && miniMapButtonAt(world.x, world.y)) {
                    miniMapButtonPressed = true
                    return true
                }
                if (battleMenuOpen) {
                    battleMenuPressedIndex = menuIndexAt(world.x, world.y)
                    return true
                }
                if (settingLayerOpen) {
                    val world=viewport.unproject(Vector2(screenX.toFloat(),screenY.toFloat()))
                    val pressed=settingPress; settingPress=null
                    if(pressed!=null && kotlin.math.abs(pressed.first-world.x)<20f && kotlin.math.abs(pressed.second-world.y)<20f) handleSettingTap(world.x,world.y)
                    return true
                }
                if (saveLayerOpen) {
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val confirm = saveConfirmAt(world.x, world.y)
                    if (saveConfirmPressed != null && saveConfirmPressed == confirm) {
                        if (saveLayer.completionTipOpen()) saveLayer.onCompletionTip(SaveLayer.TOUCH_END)
                        else saveLayer.onConfirm(confirm ?: 1)
                        saveConfirmPressed = null
                        if (!saveLayer.view().attached) {
                            saveLayerOpen = false
                            eventMessage = "진행 상황을 저장했습니다."
                            if (postBattleSaveLayer) finishVictoryRoute()
                        }
                        return true
                    }
                    val slot = saveSlotAt(world.x, world.y)
                    val pressed = savePressedSlot
                    savePressedSlot = null
                    if (pressed != null && pressed == slot && saveLayer.onRowTouch(pressed, SaveLayer.TOUCH_END)) {
                        // SaveLayer._temp's MsgBox is represented by its pendingSlot;
                        // the next tap on the explicit confirmation buttons resolves it.
                    } else if (pressed == null && (
                        world.x !in 278f..1210f ||
                            (world.x in 1046f..1194f && world.y in 100f..156f)
                        )) closeSaveLayer()
                    return true
                }
                // Canvas/Layer/menu_button is the lower-right circular icon;
                // the top-right hand belongs to a separate Battle HUD tool.
                if (world.x in 1353.9535f..1413.9535f && world.y in 8f..68f) {
                    menuHudButtonPressed = true
                    return true
                }
                battleDragLast = Vector2(world.x, world.y)
                battleTouchStart = Vector2(world.x, world.y)
                battleTouchMoved = false
                return true
            }

            override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
                val previous = battleDragLast ?: return false
                if (BattleInteractiveInput.route(scriptRuntime.state, turnController.phase) !=
                    BattleInteractiveInput.Route.PLAYER_INPUT || battleMenuOpen
                ) return false
                val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                if (battleTouchStart?.dst(world)?.let { it > 2f } == true) battleTouchMoved = true
                battleCamera.pan(world.x - previous.x, world.y - previous.y)
                previous.set(world)
                return true
            }

            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val mapTouchPending = battleDragLast != null
                val mapTouchMoved = battleTouchMoved
                battleDragLast = null
                battleTouchStart = null
                battleTouchMoved = false
                if (activeRoundLayer != null) return true
                if (battleInfoPanelPressed) {
                    battleInfoPanelPressed = false
                    if (scriptRuntime.state == PlaybackState.MODAL &&
                        scriptRuntime.currentModalKind == ScenarioInterpreter.ModalKind.INFO
                    ) {
                        // InfoLayer's first TOUCH_END while typing only fills
                        // the RichText; the following TOUCH_END removes the
                        // layer and invokes BattleScreen.resume.
                        if (!battleInfoReveal.revealAllIfPending()) {
                            scriptRuntime.resumeModal()
                            syncScriptedUnits()
                            completeTurnScriptIfReady()
                        }
                    }
                    return true
                }
                if (menuHudButtonPressed) {
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    menuHudButtonPressed = false
                    if (world.x in 1353.9535f..1413.9535f && world.y in 8f..68f) openBattleMenu()
                    return true
                }
                if (resultFlow == ResultFlow.WIN_SAVE_PROMPT) {
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val answer = victorySaveAnswerAt(world.x, world.y)
                    if (answer != null && answer == victorySaveAnswerPressed) answerVictorySavePrompt(answer)
                    victorySaveAnswerPressed = null
                    return true
                }
                loseSceneFlow?.let { flow ->
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
                    if (usePropertyDetailSuppressRelease) usePropertyDetailSuppressRelease = false else usePropertyDetail = null
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
                    if (magickInfoSuppressRelease) { magickInfoSuppressRelease = false; return true }
                    layer.close(MagicUiList.TOUCH_END)
                    if (!layer.attached) magickInfoLayer = null
                    return true
                }
                magickListLayer?.let { layer ->
                    val world=viewport.unproject(Vector2(screenX.toFloat(),screenY.toFloat()))
                    val released=magickRowAt(world.x,world.y)
                    if(magickCancelPressed&&magickCancelAt(world.x,world.y))layer.cancel(MagicUiList.TOUCH_END)
                    else if(released!=null&&released==magickPressedRow) {
                        layer.end(released)
                        if(!layer.attached) {
                            layer.rows.getOrNull(released)?.let(::selectMagick)
                            if (battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION) battleCommandFlow.childCompleted(true)
                            magickListLayer=null
                        }
                    }
                    magickPressedRow=null;magickCancelPressed=false
                    if(!layer.attached) {
                        if (released == null && battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION) battleCommandFlow.childCancelled()
                        magickListLayer=null
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
                unitInfoLayer?.let { layer ->
                    val world=viewport.unproject(Vector2(screenX.toFloat(),screenY.toFloat()))
                    val released=when { world.x in 700.71f..810.71f&&world.y in 17.207f..67.207f->9;world.x in 970f..1140f&&world.y in 35f..95f->5;world.x in 1140f..1280f&&world.y in 35f..95f->6;world.x in 760f..870f&&world.y in 35f..95f->7;world.x in 80f..850f&&world.y in 650f..720f->((world.x-80f)/150f).toInt();else->null}
                    if(released!=null&&released==unitInfoPressed) {
                        if (released == 9) jiqiLayer = BattleUnitInfoJiqiRoute.open(layer, listOf(85,57,39,95,24,22,99,48), UnitInfoLayer.TOUCH_END)
                        else layer.onButton(released,UnitInfoLayer.TOUCH_END)
                    }
                    unitInfoPressed=null;if(!layer.ref().attached)unitInfoLayer=null;return true
                }
                forcesLayer?.let { layer ->
                    val world=viewport.unproject(Vector2(screenX.toFloat(),screenY.toFloat()))
                    val tab=if(layer.view().tabsVisible && world.y in 88f..145f) when { world.x in 215f..345f -> 0; world.x in 360f..495f -> 1; else -> null } else null
                    if(forcesClosePressed && world.x in 1135f..1310f && world.y in 88f..145f) { layer.onClose(ForcesListLayer.TOUCH_END); forcesLayer=null }
                    else if(tab!=null && tab==forcesPressedTab) layer.changeSel(tab)
                    else { val row=if(world.x in 170f..1318f && world.y in 150f..614f) ((614f-world.y)/60f).toInt() else null; if(row!=null && row==forcesPressedRow) layer.onRowTouch(row,ForcesListLayer.TOUCH_END)?.let { u -> selectedUnitId=battle.units.values.firstOrNull { it.characterId==u.id }?.id; openUnitInfoLayer(u.id); eventMessage="${u.name} · ${u.post} · Lv${u.level} · HP ${u.hp}/${u.maxHp}" } }
                    forcesPressedRow=null;forcesPressedTab=null;forcesClosePressed=false;return true
                }
                if (loadGameLayerOpen) {
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    val confirm = loadConfirmAt(world.x, world.y)
                    if (loadConfirmPressed != null && loadConfirmPressed == confirm) {
                        loadGameLayer.onConfirm(confirm ?: 1)
                        loadConfirmPressed = null
                        if (!loadGameLayer.view().attached) loadGameLayerOpen = false
                        return true
                    }
                    val slot = loadSlotAt(world.x, world.y); val pressed = loadPressedSlot; loadPressedSlot = null
                    if (pressed != null && pressed == slot) loadGameLayer.onRowTouch(pressed, LoadGameLayer.TOUCH_END)
                    else if (pressed == null && (world.x !in 278f..1210f || (world.x in 1051f..1199f && world.y in 110f..170f))) closeLoadGameLayer()
                    return true
                }
                helperLayer?.let { layer ->
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    if (helperButtonPressed && world.x in 1172.451f..1320.051f && world.y in 33.187f..89.187f) {
                        layer.onButtonTouch(HelperLayer.TOUCH_END)
                        if (!layer.view().attached) helperLayer = null
                    }
                    helperButtonPressed = false
                    return true
                }
                when (autoBattleFlow.view().overlay) {
                    AutoBattleFlow.Overlay.PROMPT -> {
                        val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                        val released = autoBattlePromptButtonAt(world.x, world.y)
                        when {
                            autoBattleTogglePressed && autoBattleToggleAt(world.x, world.y) -> autoBattleFlow.toggle()
                            autoBattlePressedTag != null && autoBattlePressedTag == released -> answerAutoBattle(autoBattlePressedTag!!)
                            autoBattlePanelPressed && released == null && !autoBattleToggleAt(world.x, world.y) -> answerAutoBattle(1)
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
                    lastFullBattleMenuTap = "${pressed ?: -1}/${released ?: -1}@${FullBattleTraceRecorder.number(world.x)},${FullBattleTraceRecorder.number(world.y)}"
                    battleMenuPressedIndex = null
                    if (pressed != null && pressed == released) handleBattleMenuTap(pressed)
                    else if (pressed == null) closeBattleMenu()
                    return true
                }
                if (miniMapButtonPressed) {
                    val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                    if (miniMapButtonAt(world.x, world.y)) miniMapLayer.touch(MiniMapLayer.TOUCH_END)
                    miniMapButtonPressed = false
                    return true
                }
                if (mapTouchPending && !mapTouchMoved &&
                    BattleInteractiveInput.route(scriptRuntime.state, turnController.phase) ==
                    BattleInteractiveInput.Route.PLAYER_INPUT
                ) {
                    // Original BattleScreen dispatches SELECT_UNIT_POINT on
                    // TOUCH_END. Its y conversion is mapH - (row + 1).
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
                // addTouchEventListener(button, callback, 2): it never
                // invokes close on TOUCH_START or a release off the button.
                val world = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                if (winConditionButtonPressed && world.x in 957.134f..1213.834f && world.y in 88.204f..148.204f) {
                    layer.onButtonTouch(WinConBoxLayer.TOUCH_END)
                }
                winConditionButtonPressed = false
                return true
            }

            override fun scrolled(amountX: Float, amountY: Float): Boolean {
                if (treasureLayerOpen) { treasureScrollRow = (treasureScrollRow + amountY.toInt()).coerceAtLeast(0); return true }
                if (saveLayerOpen) { saveScrollRow = (saveScrollRow + amountY.toInt()).coerceAtLeast(0); return true }
                if (propertyLayerOpen) { propertyScrollRow = (propertyScrollRow + amountY.toInt()).coerceAtLeast(0); return true }
                if (!terrainLayerOpen) return false
                terrainScrollRow = (terrainScrollRow + amountY.toInt()).coerceAtLeast(0)
                return true
            }
        }
    }

    /**
     * Own the source FightLayer pause/resume boundary one command at a time.
     * `resumeExternalDelay()` may synchronously emit the following command,
     * but excess frame time is never carried across that callback boundary.
     */
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
            recordFullBattleTraceFrame(0f, fullTraceFightCommandObservation(command), advanceFrame = false)
            // FightLayer.end is synchronous and therefore has no external
            // script delay to acknowledge.
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
            // The resumed AST normally reaches the next FightLayer pause in
            // this same call. Queue it now, but begin it on the next render
            // so commands cannot consume one another's leftover delta.
            pendingFightCommands.addAll(scriptRuntime.stage.consumeFightCommands())
        }
    }

    /** Exact FightUnit.__cb1 dispatch, including its special move token. */
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
        if (battleCommandRouteState != null && !battleCommandRouteInstalled) installBattleCommandRouteFixture()
        if (roundRouteState != null && !roundRouteInstalled) installRoundRouteFixture()
        if (miniMapRouteState != null && !miniMapRouteInstalled) installMiniMapRouteFixture()
        if (autoBattleRouteState != null && !autoBattleRouteInstalled) installAutoBattleRouteFixture()
        if (battleCharacterRouteState != null && !battleCharacterRouteInstalled) installBattleCharacterRoute()
        if (jiqiRouteFixture && !jiqiRouteInstalled) installJiqiRouteFixture()
        if (magickRouteState != null && !magickRouteInstalled) installMagickRouteFixture()
        if (usePropertyRouteState != null && !usePropertyRouteInstalled) installUsePropertyRouteFixture()
        if (magickPressedRow != null && magickInfoLayer == null && elapsed - magickPressedAt >= 1f) {
            magickListLayer?.tick()?.let {
                magickInfoLayer = MagicInfoLayer(it)
                magickInfoSuppressRelease = true
            }
        }
        if (itemUpgradeRouteState != null && !itemUpgradeRouteInstalled) installItemUpgradeRoute()
        val delta = rawDelta * (fullTraceConfig?.timeScale ?: 1f)
        elapsed += delta
        completeBattleBackgroundLoadIfReady()
        if (yingchuanEntryFlowTracePath != null && battleInitLayer.view().attached &&
            !scriptRuntime.stage.battleDrawRequested && scriptRuntime.state == PlaybackState.DELAY) {
            yingchuanEntryFlowSawInit = true
        }
        completePendingBattleCommand()
        miniMapLayer.advance(delta)
        loseSceneFlow?.update(delta)
        usePropertyLayer?.update(delta)
        // Source Script.pause()/resume() drives stage.delay and cut-scene
        // attacks. Resume its AST only after the same visible interval.
        val scriptStateBeforeUpdate = scriptRuntime.state
        val scriptedMovementActiveBeforeUpdate = scriptRuntime.stage.units.values.any { it.moveDuration > 0f }
        scriptRuntime.update(delta, autoCloseUi = settingsPreferences.getInteger(
            SettingLayer.GAME_SETTING,
            SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
        ) and SettingLayer.AUTO_CLOSE != 0)
        // scene1 is resumed by dialogue/info/reward callbacks as well as by
        // runBattleScript().  A non-annihilation objective (S_12's Cao Cao
        // escape route, for example) calls reward/end only after such a
        // resume.  Propagate that authored result on the same render frame;
        // limiting this to runBattleScript's entry left the tactical Battle
        // running after ScenarioStage had already ended it.
        battle.syncScriptedOutcome(scriptRuntime.stage.scriptedBattleOutcome)
        if (scriptRuntime.state == PlaybackState.MODAL &&
            scriptRuntime.currentModalKind == ScenarioInterpreter.ModalKind.INFO
        ) {
            scriptRuntime.currentModalText?.let { battleInfoReveal.update(it, delta) }
        } else {
            battleInfoReveal.reset()
            battleInfoPanelPressed = false
        }
        scriptedMovementCampTransitionFrameBarrier.observe(
            inCampScript = turnController.phase == BattleTurnController.Phase.CAMP_SCRIPT,
            scriptWasPending = scriptStateBeforeUpdate != PlaybackState.COMPLETE,
            scriptCompleted = scriptRuntime.state == PlaybackState.COMPLETE,
            movementWasActive = scriptedMovementActiveBeforeUpdate,
            movementIsActive = scriptRuntime.stage.units.values.any { it.moveDuration > 0f },
        )
        driveFightPresentation(delta)
        // Source BattleScreen.draw() flips _isDraw and removes BattleInitLayer
        // at this exact script boundary.  Previously stage.draw was discarded,
        // leaving the game without an authoritative init/drawn transition.
        if (scriptRuntime.stage.battleDrawRequested && battleInitLayer.view().attached) {
            battleInitLayer.onDestroy()
        }
        writeYingchuanEntryFlowIfReady()
        openRewardRequestIfNeeded()
        itemUpgradeFlow?.update(delta)
        openEquipmentUpgradeIfNeeded()
        activeRoundLayer?.let { layer ->
            activeRoundLayerElapsed += delta
            layer.elapsed(activeRoundLayerElapsed)
        }
        driveBattleBootstrap()
        if (!scriptedMovementCampTransitionFrameBarrier.yieldBeforeCampTransition()) {
            completeTurnScriptIfReady()
        }
        driveTurnDeathScriptBarrier()
        // END_ROUND's entrusted-battle choice sets COLLOCATION in the source.
        // From then on Mine uses the ordinary visible _ai2 lifecycle; trace
        // recorders merely observe it and never inject tactical progress.
        if (bootstrapPhase == BattleBootstrapPhase.COMPLETE &&
            autoBattleFlow.view().collocation && turnController.phase == BattleTurnController.Phase.PLAYER_INPUT &&
            scriptRuntime.state == PlaybackState.COMPLETE && battle.outcome() == null && activeAiCamp == null
        ) turnController.runCollocatedPlayerTurn()
        // StageLayer.pause() suspends only the Python control script. Cocos
        // cc.Animation keeps advancing behind SayLayer/ChooseLayer, which is
        // visible in the source panel-blending captures.
        battleElapsed += delta
        // Animation.schedule(centerUnit, interval, ...) observes the updated
        // Cocos animation clock before move2's completion callback. Running
        // this at the start of render used the previous frame's clock and
        // could discard the final camera tick when MOVING completed below.
        driveMovementTicks()
        applyDueBattleMutations()
        driveTurnSettlementPresentation()
        driveScriptedUnitHide()
        driveScriptedUnitShow()
        driveScriptedCameraCenters()
        driveMapPresentation()
        driveScriptPresentation()
        driveScriptedUnitActionCallback()
        drivePendingDeathAnimations()
        pruneCombatPresentation()
        playPendingMagicEffectSounds()
        startQueuedPhysicalPassPresentation()
        startQueuedFollowUpPresentation()
        startQueuedCounterPresentation()
        startQueuedCounterFollowUpPresentation()
        startQueuedMagicPassPresentation()
        resumeCriticalSpeechAction()
        driveVisibleAiTurn()
        // The addressed dialogue readback freezes the source while SayLayer
        // owns input.  Its fixture has no player-camp roster, so evaluating
        // tactical defeat during that hold replaces the requested frame with
        // Lose at t=0.  Normal gameplay and every non-dialogue capture keep
        // the ordinary outcome transition.
        if (bootstrapPhase == BattleBootstrapPhase.COMPLETE &&
            dialogueStepCapture == null && !selectionOverlayCapture &&
            resultFlow == ResultFlow.NONE && NaturalBattleTransition.resultScriptReadyForLoseScene(
                battle.outcome(), scriptRuntime.state, scriptRuntime.currentDialogue != null,
            ) &&
            // Source scene1 owns its Say/Info callback chain through
            // stage.end()/lose(); Lose.scene is created only after that
            // coroutine has returned.  Creating it while SayLayer is still
            // open makes Lose consume every dialogue input and deadlocks the
            // authored result scene (observed in S_22 at max round).
            actionAnimation?.let { animationClock() < it.endsAt } != true &&
            movementAnimation?.let { animationClock() < it.endsAt } != true &&
            hitReactionAnimations.values.none { animationClock() < it.endsAt } &&
            deathAnimations.values.none { animationClock() < it.endsAt } &&
            pendingDeathAnimations.isEmpty() && activeUnitDeath == null && unitDeathAwaitingDialogue == null &&
            scriptedHideAwaitingDialogue == null && activeScriptedHide == null && activeScriptedShow == null
            && !combatPresentationBusy()
            && !outcomeCallbacksPending()
        ) enterLoseScene()
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
                pendingBattleCompletedScriptPasses == 1 && !postActionDeathsStarted -> {
                    if (retainActualDyingUnits()) startPendingDeathAnimations()
                    else finishManualUnitDeathCallbacks()
                }
                pendingBattleCompletedScriptPasses == 1 && !combatPresentationBusy() -> {
                    pendingBattleCompletedScriptPasses = 2
                    runBattleScript()
                }
                pendingBattleCompletedScriptPasses == 2 -> finishManualUnitDeathCallbacks()
            }
        }
        // Stage proxies retain authored spawn coordinates after a scene has
        // completed. Reapplying them on every tactical frame teleports every
        // AI move back to its spawn tile. Synchronize only while a script is
        // actually advancing (including its final DELAY -> COMPLETE frame).
        if (scriptStateBeforeUpdate != PlaybackState.COMPLETE || scriptRuntime.state != PlaybackState.COMPLETE) syncScriptedUnits()
        // `setPosts` writes the Unit first, then calls testAvatar/loadAvatar.
        // Starting the reload after that projection keeps the old sprite
        // visible during the async load while combat data is already fresh.
        driveScriptedUnitPosts()
        syncDialogueSpeakerPresentation()
        driveNaturalBattleCompletion()
        if (battleRouteCompleted) return
        if (winConditionRouteState != null) {
            battle.units.values.firstOrNull { it.characterId == 235 }?.let { scriptedUnitVisuals.remove(it.id) }
        }
        dialogueComponentStage?.let { stage ->
            // Match the source isolation controller: the camera clears to a
            // transparent black framebuffer before the selected authored
            // nodes are composited.  Do not route this through a cached
            // source image; each element below remains the game renderer.
            // Cocos' camera clear is opaque black; the panel then performs
            // its authored source-over blend against that destination.
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
            // This branch returns before the ordinary render tail, which
            // normally advances the Cocos-style typewriter.
            scriptRuntime.currentDialogue?.let { dialogueReveal.update(it.text, delta) }
            // S_00's authored scene0 has a five-second delay before its
            // first SayLayer.  Keep the real script clock running until that
            // dialogue exists; an early capture would only validate black.
            if (scriptRuntime.currentDialogue != null) {
                if (stage == "background" || stage == "characters") drawGrid()
                drawScriptDialogue(stage)
            }
            if (elapsed > 6f) game.captureFrameIfRequested()
            return
        }
        if (battleMenuRoute) {
            drawBattleMenu()
            if (elapsed > .25f) game.writeRenderEventLogIfRequested()
            return
        }
        // scene1 begins with a source-controlled preparation delay. Start
        // this diagnostic only after that delay has naturally reached the
        // first "꺼져!" say, exactly as the Electron harness does.
        if ((cutsceneAttackCapture || cutscenePostHitCapture || cutscene477Capture) && cutsceneAttackStartedAt == null && scriptRuntime.state == PlaybackState.DIALOGUE) {
            scriptRuntime.advanceDialogue()
            dialogueReveal.reset()
            syncScriptedUnits()
            check(scriptRuntime.state == PlaybackState.DELAY) {
                "영천 공격 캡처가 공격 대기 상태에 진입하지 않았습니다: ${scriptRuntime.state}"
            }
            cutsceneAttackStartedAt = elapsed
        }
        // Electron's real MenuLayer route removes the opening SayLayer before
        // it instantiates TerrainLayer/PropertyLayer.  The original scenario
        // then reaches the authored 235 speech while that modal remains
        // visible.  Advance only the capture fixture through that same first
        // dismissal; normal modal input retains the live SayLayer behavior.
        if (modalRenderCapture && !modalCaptureOpeningSayDismissed && elapsed >= 0.1f && scriptRuntime.state == PlaybackState.DIALOGUE) {
            scriptRuntime.advanceDialogue()
            dialogueReveal.reset()
            syncScriptedUnits()
            check(scriptRuntime.state == PlaybackState.DELAY) {
                "모달 원본 렌더 캡처가 opening SayLayer 뒤의 delay에 진입하지 않았습니다: ${scriptRuntime.state}"
            }
            modalCaptureOpeningSayDismissed = true
        }
        // The next user interaction first exposes the full typewritten
        // 공병 line, then closes it. This deterministic fixture performs the
        // semantic close after its source text interval has elapsed so the
        // S_00 hide(235) → 477 say transition can be compared directly.
        if (cutscene477Capture && cutsceneAttackStartedAt != null && cutscene477StartedAt == null &&
            scriptRuntime.state == PlaybackState.DIALOGUE && elapsed - cutsceneAttackStartedAt!! >= 3f
        ) {
            scriptRuntime.advanceDialogue()
            dialogueReveal.reset()
            syncScriptedUnits()
            check(scriptRuntime.currentDialogue?.speakerId == "477") {
                "영천 477 캡처가 다음 원본 대사에 도달하지 않았습니다: ${scriptRuntime.currentDialogue}"
            }
            cutscene477StartedAt = elapsed
        }
        // Mirror electron/main.cjs's advanceDialogue fixture: each recorded
        // input occurs only after the current source text has had time to
        // finish revealing, then StageLayer delays/attack animations are
        // allowed to reach their next real dialogue suspension.
        dialogueStepCapture?.let { targetStep ->
            if (scriptRuntime.state == PlaybackState.DIALOGUE) {
                val startedAt = dialogueStepStartedAt ?: elapsed.also { dialogueStepStartedAt = it }
                // The first fixture input closes the already-complete 474
                // line.  In SayLayer, the following input is consumed by
                // `_handle` when 235 is still typing (it reveals the line);
                // only the input after that invokes `_next()` and reaches
                // 477.  Keep those two source-component semantics distinct
                // instead of treating every input as a dialogue advance.
                val inputDelay = when (dialogueStepInputs) {
                    0 -> 0f
                    1 -> 0.05f
                    else -> 3.2f
                }
                val shouldInput = elapsed - startedAt >= inputDelay
                if (dialogueStepInputs < targetStep && shouldInput) {
                    advanceBattleDialogue()
                    dialogueStepInputs++
                    dialogueStepStartedAt = null
                }
            } else {
                dialogueStepStartedAt = null
            }
        }
        if (actionCaptureMode && !actionCaptureLogged && elapsed > 1f) {
            actionCaptureLogged = true
            actionAnimation?.let { animation ->
                val unit = battle.units[animation.unitId]
                Gdx.app.log(
                    "JojoGame",
                    "ACTION_CAPTURE_FRAME: elapsed=$elapsed, sourceY=${battleSpriteFrame(animation.sourceAction, animation.direction, animationClock() - animation.startedAt)?.sourceY}, active=${animationClock() < animation.endsAt}, " +
                        "unit=${unit?.characterId}, tile=${unit?.tileX},${unit?.tileY}, visible=${unit?.visible}, " +
                        "screen=${unit?.let { boardLeft + it.tileX * boardTile }},${unit?.let { tileBottom(it.tileY) }}",
                )
            }
        }
        audio.sync(scriptRuntime.stage)
        scriptRuntime.stage.consumeShowWinCondition()?.let { text ->
            scriptWinConditions = WinConditionsLayer().also { layer -> layer.onCreate(text, scenarioMaxRound()) {
                scriptWinConditions = null
                if (scriptRuntime.state == PlaybackState.MODAL) scriptRuntime.resumeModal()
            } }
        }
        driveFullBattleTrace()
        recordFullBattleMapObjectsCalls()
        recordFullBattleTraceFrame(delta)
        fullTraceFinishAfterFrame?.let { reason ->
            fullTraceFinishAfterFrame = null
            finishFullBattleTrace(reason)
        }
        scriptRuntime.currentDialogue?.let {
            dialogueReveal.update(it.text, delta)
            // The source diagnostic consumes SayLayer's active typewriter
            // handler once, revealing the current line without advancing it.
            if (battleDialogueBlendRoute) dialogueReveal.revealAllIfPending()
            val autoCloseEnabled = !verifyMode && !scriptedBattleVerifyMode &&
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
        // BattleScreen._endProcess replaces the source Battle scene with the
        // standalone Lose scene.  Do not retain a Battle canvas beneath this
        // visual state: the Lose prefab consists solely of Logo_8-1 on the
        // cleared framebuffer.
        if (resultFlow == ResultFlow.LOSE_SCENE) {
            drawLoseScene()
            loseSceneFlow?.takeIf { it.state == LoseSceneFlow.State.PROMPT }?.let { drawLosePrompt() }
            if (loseRestartRoute && elapsed > 3.25f && game.writeRenderEventLogIfRequested()) return
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-save") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.SAVE))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-load") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.LOAD))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-setting") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.SETTING))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-helper") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.HELPER))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-win-condition") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.WIN_CONDITION))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-menu") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.MENU))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-terrain") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.TERRAIN))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-property") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.PROPERTY))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-treasure") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.TREASURE))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-forces") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.FORCES))
            game.captureFrameIfRequested()
            return
        }
        if (game.requestedCaptureState() == "yingchuan-unit-info") {
            drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.UNIT_INFO))
            game.captureFrameIfRequested()
            return
        }
        // The victory fixture is the source's fully composited MsgBox frame:
        // it includes its grayscale battlefield mask, bg0/box3/logo artwork,
        // button alpha, and Cocos layer order. Keep this exact reference
        // scoped to the explicitly addressed raw-comparison state; live
        // result-flow input still uses the ordinary in-game overlay below.
        if (resultFlow == ResultFlow.WIN_SAVE_PROMPT && game.requestedCaptureState() == "win-result") {
            drawSourceWinResultReference()
            game.captureFrameIfRequested()
            return
        }
        if (battleEdit2RouteState != null) {
            drawBattleEdit2Route()
            if (elapsed > .25f) game.writeRenderEventLogIfRequested()
            return
        }
        if (battleTerrainRoute) {
            drawTerrainLayer()
            if (elapsed > .25f) game.writeRenderEventLogIfRequested()
            return
        }
        // Isolated framebuffer diagnostic.  The normal candidate leaves the
        // GL state untouched; an explicit capture option can force DITHER
        // only around the map draw and restores the prior state immediately.
        val requestedDither = if (mapOnlyCapture) game.requestedMapDither() else null
        val priorDither = requestedDither?.let { Gdx.gl.glIsEnabled(GL20.GL_DITHER) }
        requestedDither?.let { enabled ->
            if (enabled) Gdx.gl.glEnable(GL20.GL_DITHER) else Gdx.gl.glDisable(GL20.GL_DITHER)
        }
        // Same capture-only arrangement for texture filtering.  Preserve
        // both settings so the next frame returns to ordinary LINEAR state.
        val requestedFilter = if (mapOnlyCapture) game.requestedMapFilter() else null
        val priorFilter = requestedFilter?.let { mapTexture?.let { texture -> texture.minFilter to texture.magFilter } }
        requestedFilter?.let { filter -> mapTexture?.setFilter(filter, filter) }
        drawGrid()
        drawScriptPresentationOverlay()
        priorFilter?.let { (min, mag) -> mapTexture?.setFilter(min, mag) }
        priorDither?.let { enabled ->
            if (enabled) Gdx.gl.glEnable(GL20.GL_DITHER) else Gdx.gl.glDisable(GL20.GL_DITHER)
        }
        // FightLayer is an opaque UILayer above the tactical map/HUD.  Its
        // authored 16px border intentionally leaves the battle visible.
        if (fightOverlayActive) {
            fightRenderer.draw(fightPresentationView())
            game.captureFrameIfRequested()
            return
        }
        // Character-state fixtures intentionally compare only the actual map
        // and BattleUnit submissions emitted by drawGrid. Do not draw HUD
        // nodes that are absent from this route's independent event stream.
        if (battleCharacterRouteState != null) {
            if (elapsed > .25f) {
                if (!game.writeRenderEventLogIfRequested()) game.captureFrameIfRequested()
            }
            return
        }
        if (battleInitRoute) {
            drawBattleHudChrome()
            drawRewardSection()
            if (game.writeRenderEventLogIfRequested()) return
            return
        }
        if (!mapOnlyCapture) {
            drawBattleHudChrome()
            if (battleMenuOpen) drawBattleMenu()
            if (helperLayer != null) drawHelperLayer()
            if (terrainLayerOpen) drawTerrainLayer()
            if (propertyLayerOpen) drawPropertyLayer()
            if (treasureLayerOpen) drawTreasureLayer()
            if (saveLayerOpen) drawSaveLayer()
            if (settingLayerOpen) drawSettingLayer()
            if (loadGameLayerOpen) drawLoadGameLayer()
            if (forcesLayer != null) drawForcesListLayer()
            if (unitInfoLayer != null) drawUnitInfoLayer()
            if (jiqiLayer != null) drawJiqiLayer()
            if (magickListLayer != null) drawMagickListLayer()
            if (magickInfoLayer != null) drawBattleMagicInfoLayer()
            if (usePropertyLayer != null) drawUsePropertyLayer()
            if (usePropertyDetail != null) drawUsePropertyDetail()
            if (battleCommandFlow.phase == BattleCommandFlow.Phase.COMMAND) drawBattleCommandLayer()
            activeRoundLayer?.let(::drawRoundLayer)
            drawSettlementOverlays()
            // The source BattleScreen has no diagnostic title/footer HUD.  Its
            // controls are sprite buttons over the map, which are implemented below.
            if (verifyMode) drawHud()
            // UILayer.pushLayer(MenuLayer) is above SayLayer in the original.
            // It owns the entire visible canvas while open, so do not draw a
            // stale dialogue panel back over its opaque menu mask.
            // SaveLayer is reached from MenuLayer only after the source route
            // has removed the opening SayLayer.  Keeping our scripted opening
            // dialogue visible here made it bleed through the transparent
            // SaveLayer body, which is a layer-stack mismatch rather than a
            // text-rasterization difference. Terrain/property keep the source
            // dialogue stack; Save and Menu replace it.
            if (!selectionOverlayCapture && !actionCaptureMode && miniMapRouteState == null && !battleMenuOpen && !saveLayerOpen && helperLayer == null && forcesLayer == null && unitInfoLayer == null && jiqiLayer == null && magickListLayer == null && magickInfoLayer == null && usePropertyLayer == null && usePropertyDetail == null && activeRoundLayer == null && battleCommandFlow.phase != BattleCommandFlow.Phase.COMMAND && autoBattleFlow.view().overlay == AutoBattleFlow.Overlay.NONE) {
                drawScriptDialogue()
                drawScriptChoice()
                drawScriptInfoLayer()
            }
            drawAutoBattleOverlay()
            if (winConditionOpen) drawWinConditionBox()
            if (resultFlow == ResultFlow.WIN_SAVE_PROMPT) drawSavePrompt()
            scriptWinConditions?.let { drawScriptWinConditions(it) }
            rewardFlow?.let(::drawBattleReward)
            if (rewardRouteState != null) drawRewardSection()
            itemUpgradeFlow?.let(::drawItemUpgrade)
            if (itemUpgradeRouteState != null) drawRewardSection()
        }
        // battleCharacterRouteState returned immediately above after writing
        // its own event log, so including it here was an unreachable branch.
        if ((rewardRouteState != null || itemUpgradeRouteState != null || jiqiRouteFixture || magickRouteState != null || usePropertyRouteState != null || roundRouteState != null || winConditionRouteState != null || miniMapRouteState != null || autoBattleRouteState != null || battleCommandRouteState != null || otherUnitInfoRoute || mineUnitInfoRoute || (battleDialogueBlendRoute && scriptRuntime.currentDialogue != null && dialogueReveal.isComplete)) && elapsed > 0.25f && game.writeRenderEventLogIfRequested()) return
        // Static Cocos reference frames are captured six seconds after the
        // battle becomes ready. Action fixtures intentionally stay at their
        // one-second BRAnime sample time.
        val captureAt = when {
            // S_00.scene0 suspends at `stage.say('&474\\n꺼져!')` during
            // BattleScreen construction.  Capture its first .04s typewriter
            // unit directly; this is not the later scene1 dialogue fixture.
            // scene0 contains an authored `stage.delay(5)` before the draw
            // and SayLayer command.  At 60fps .55s is the first completed
            // .04s text-reveal unit after that source delay.
            game.requestedCaptureState() == "yingchuan-opening-say" -> 0.55f
            // The source verifier reaches its intact raw hold after a
            // six-second Battle-ready settle.  Its Cocos avatar animations
            // are then at about 6.02s (anime0_3's first 8-tick frame), so
            // capture at six seconds rather than the CDP controller's later
            // wall-clock observation. This is a renderer fixture only.
            game.requestedCaptureState() == "hud" -> 6f
            battleDialogueBlendRoute -> if (scriptRuntime.currentDialogue != null && dialogueReveal.isComplete && elapsed > 0.6f) 0.6f else Float.MAX_VALUE
            actionCaptureMode -> 1f
            cutsceneAttackCapture -> (cutsceneAttackStartedAt ?: Float.MAX_VALUE) + 0.9f
            // anime21 lasts 40/24s and is followed by stage.delay(10)=1s.
            // Leave a small settle window before capturing the next say.
            cutscenePostHitCapture -> (cutsceneAttackStartedAt ?: Float.MAX_VALUE) + 3f
            cutscene477Capture -> (cutscene477StartedAt ?: Float.MAX_VALUE) + 3f
            // The source fixture takes its screenshot after a 3.2s wait.
            // Capture after that same next-input deadline; 3.4 leaves one
            // rendered frame for the new SayLayer state to become visible.
            dialogueStepCapture != null -> (dialogueStepStartedAt ?: Float.MAX_VALUE) + 3.4f
            else -> 6f
        }
        if (elapsed > captureAt && (cutscene477Capture || dialogueStepCapture != null) && !dialogueCaptureLogged) {
            dialogueCaptureLogged = true
            val dialogue = scriptRuntime.currentDialogue
            val profile = dialogue?.speakerId?.toIntOrNull()?.let(gameDataCatalog::unitProfile)
            val speakerName = profile?.name?.let(GameDataCatalog::sayLayerUnitName).orEmpty()
            Gdx.app.log("JojoGame", "DIALOGUE_CAPTURE_STATE: speaker=${dialogue?.speakerId} name=$speakerName text=${dialogue?.text} face=${profile?.face} head=${profile?.face?.plus(8)}")
            val speakerUnit = dialogue?.speakerId?.toIntOrNull()?.let { characterId ->
                (battle.units.values + battle.pendingPresentationUnits())
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
        if (elapsed > captureAt && selectionOverlayCapture && !selectionCaptureLogged) {
            selectionCaptureLogged = true
            val selected = selectedUnitId?.let(battle.units::get)
            val tiles = selectableAreaTiles()
            // Report only overlays which passed the same texture-presence
            // gate as drawSelectableTiles.  Counting model candidates here
            // used to let this diagnostic claim pixels that the framebuffer
            // could not possibly contain when an exported U_select frame was
            // missing.
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
        if (elapsed > captureAt) {
            if (mapOnlyCapture) game.writeMapQuadCandidateSidecar()
            // Capture-only visibility inventory.  Keep this beside the draw
            // gate so the sidecar reports what could actually reach pixels,
            // not merely a paused scenario coroutine retained in memory.
            when (game.requestedCaptureState()) {
                "yingchuan-menu" -> game.writeCaptureStack(
                    requested = "MenuLayer",
                    requestedPresent = battleMenuOpen,
                    dialogue = false,
                    choice = false,
                    modalCount = 0,
                )
                "yingchuan-win-condition" -> game.writeCaptureStack(
                    requested = "WinConBoxLayer",
                    requestedPresent = winConditionOpen && winConditionLayer != null,
                    dialogue = false,
                    choice = false,
                    modalCount = if (scriptWinConditions != null) 1 else 0,
                )
            }
            if (game.captureFrameIfRequested()) return
        }
        if (verifyMode && elapsed > 0.8f) {
            endTurn()
            endTurn()
            check("reinforcement-arrival" in battle.firedEventIds) { "Battle reinforcement event did not fire" }
            check("reinforcement" in battle.units) { "Battle reinforcement did not join" }
            Gdx.app.log("JojoGame", "VERIFY_BATTLE_OK: round=${battle.round}, event=${battle.firedEventIds.first()}")
            Gdx.app.exit()
        }
        if (scriptedBattleVerifyMode && elapsed > 0.8f) {
            check(scriptRuntime.stage.battleUnits.isNotEmpty()) { "$sourceScenario 원본 전투 유닛이 없습니다." }
            check(mapTexture != null) { "$sourceScenario 원본 HM 전장 이미지를 찾을 수 없습니다: ${scriptRuntime.stage.battleMapIndex + 1}" }
            check(battle.units.size == scriptRuntime.stage.battleUnits.size) { "$sourceScenario 전투 유닛 변환 수가 일치하지 않습니다." }
            // Early scripted battles can deliberately begin with every Mine
            // slot hidden (or with an empty campaign roster).  That is a
            // valid source state; require only the camps the script itself
            // actually materialized, not a visible player unit at scene0.
            if (scriptRuntime.stage.battleUnits.values.any { it.faction == ScenarioUnitFaction.MINE }) {
                check(battle.units.values.any { it.faction == Faction.PLAYER }) { "$sourceScenario 플레이어 유닛 변환에 실패했습니다." }
            }
            check(battle.units.values.any { it.baseFaction.isEnemySide() }) { "$sourceScenario 적군이 없습니다." }
            check(battle.units.values.any { unit -> unit.characterId != null && unitTexture(unit) != null }) {
                "$sourceScenario 원본 유닛 스프라이트를 찾을 수 없습니다."
            }
            Gdx.app.log("JojoGame", "VERIFY_SCRIPTED_BATTLE_OK: $sourceScenario ${battle.units.size} source units rendered into tactical state")
            Gdx.app.exit()
        }
    }

    private fun driveFullBattleTrace() {
        val config = fullTraceConfig ?: return
        if (fullTraceFinished) return
        val outcome = battle.outcome().takeIf { bootstrapPhase == BattleBootstrapPhase.COMPLETE }
        fullTraceDeadline?.timeoutReason(elapsed, outcome != null)?.let { reason ->
            fullTraceFinishAfterFrame = reason
            return
        }
        if (elapsed - fullTraceLastDriverAt < config.driverIntervalSeconds) return
        fullTraceLastDriverAt = elapsed
        // UI progression belongs to ProductionBattleInputDriver. The trace
        // path only observes and flushes; it must not call BattleScreen's
        // private dialogue/modal continuations or mutate tactical state.
        if (scriptRuntime.state == PlaybackState.DIALOGUE ||
            scriptRuntime.state == PlaybackState.CHOICE || scriptRuntime.state == PlaybackState.MODAL ||
            scriptWinConditions != null
        ) return
        if (!presentationReady || actionAnimation?.let { animationClock() < it.endsAt } == true ||
            movementAnimation?.let { animationClock() < it.endsAt } == true ||
            hitReactionAnimations.values.any { animationClock() < it.endsAt } ||
            deathAnimations.values.any { animationClock() < it.endsAt }
        ) return

        if (outcome != null) {
            // The source hands loss input to Lose.scene, rather than to a
            // Battle result modal.  Campaign capture may therefore flush as
            // soon as that stable scene owns input; do not wait for its
            // independent three-second prompt or the generic result timeout.
            if (NaturalBattleTransition.campaignLossTraceReadyToFlush(
                    exitOnFinish = config.exitOnFinish,
                    outcome = outcome,
                    loseSceneActive = resultFlow == ResultFlow.LOSE_SCENE,
                    scriptState = scriptRuntime.state,
                    callbackPending = outcomeCallbacksPending(),
                    scriptEnded = scriptRuntime.stage.battleEndedByScript,
                )
            ) {
                fullTraceFinishAfterFrame = "battle-end"
                return
            }
            // Campaign E2E owns the authored result/save/R_01 route and flushes
            // from openVictorySavePrompt. Standalone traces settle scene1 and
            // then terminate after three stable production frames.
            if (!config.exitOnFinish) return
            // Never manufacture another scene1 invocation here. The real
            // camp/round/action controller has already called run_script
            // before it exposes the outcome, exactly like BattleScreen's
            // `_endProcess` chain. Restarting scene1 after that callback
            // repeated result-trigger dialogue whose guard is the defeated
            // unit's HP (S22's hidden unit still correctly retains HP=0).
            if (NaturalBattleTransition.fullTraceTerminalReady(
                    scriptRuntime.state,
                    outcomeCallbacksPending(),
                    scriptRuntime.stage.battleEndedByScript,
                    resultFlow != ResultFlow.NONE,
                )
            ) fullTraceTerminalFrames++ else fullTraceTerminalFrames = 0
            if (fullTraceTerminalFrames >= 3) fullTraceFinishAfterFrame = "battle-end"
            return
        }
    }

    /**
     * Sparse logical map-object snapshot matching the original trace's
     * BATTLE_GATE_ATTR rows: TYPE, TERRAIN, X, Y.  S_52 and S_57 mutate many
     * doors/traps; emitting the full set only when it changes keeps long
     * traces bounded while preserving every resulting state.
     */
    private fun fullTraceMapObjectsJson(): Pair<Int, String> {
        val rows = scriptRuntime.stage.mapObjects.values.asSequence()
            .filter(ScenarioMapObject::enabled)
            .sortedWith(compareBy<ScenarioMapObject>({ it.x }, { it.y }, { it.objectId }, { it.terrainId }))
            .joinToString(",") { "[${it.objectId},${it.terrainId},${it.x},${it.y}]" }
        val signature = "[$rows]"
        return if (signature != fullTraceMapObjectSignature) {
            fullTraceMapObjectSignature = signature
            fullTraceMapObjectRevision++
            fullTraceMapObjectRevision to signature
        } else {
            fullTraceMapObjectRevision to "null"
        }
    }

    /**
     * Trace every authored setObjects/setObject call once, including scene0's
     * pre-draw map construction. Visible presentation has its own callback
     * queue, but must not emit a second copy of this mutation observation.
     */
    private fun recordFullBattleMapObjectsCalls() {
        if (fullTraceRecorder == null) return
        val calls = scriptRuntime.stage.mapObjectsCalls
        while (fullTraceMapObjectsCallCursor < calls.size) {
            val call = calls[fullTraceMapObjectsCallCursor++]
            val entries = call.objects.joinToString(";") { "${it.objectId},${it.x},${it.y}" }
            recordFullBattleTraceFrame(
                0f,
                "transition:objects:${if (call.enabled) 1 else 0}:${call.terrainId}:$entries",
                advanceFrame = false,
            )
        }
    }

    /** Live FightLayer renderer state, in source prefab slot order (0, 1). */
    private fun fullTraceFightJson(): String {
        if (!fightOverlayActive) return "null"
        fun slotUnit(slot: Int): FightUnitPresentation =
            if (fightPresentation.mineIndex == slot) fightPresentation.mine else fightPresentation.enemy
        fun fighterJson(fighter: FightUnitPresentation): String {
            val action = fighter.action
            val pose = action?.let { fightSprites.pose(it, fighter.actionElapsedSeconds) } ?: FightActionPose()
            return "[${fighter.characterId ?: "null"},${fighter.created},${action ?: "null"}," +
                "${FullBattleTraceRecorder.number(fighter.actionElapsedSeconds)}," +
                "${FullBattleTraceRecorder.number(fighter.parentX)},${FullBattleTraceRecorder.number(fighter.parentScaleX)}," +
                "${FullBattleTraceRecorder.number(pose.childX)},${FullBattleTraceRecorder.number(pose.childY)}," +
                "${FullBattleTraceRecorder.number(pose.childScaleX)},${FullBattleTraceRecorder.number(pose.opacity)}," +
                "${fighter.zIndex},${fighter.dead}]"
        }
        fun speechJson(fighter: FightUnitPresentation): String {
            val side = if (fighter === fightPresentation.mine) FightSide.MINE else FightSide.ENEMY
            val speech = fightPresentation.speech(side)
            return "[${speech.active},\"${FullBattleTraceRecorder.escape(speech.renderedText)}\"]"
        }
        val slot0 = slotUnit(0)
        val slot1 = slotUnit(1)
        val introOpacity = if (fightPresentation.introBackgroundActive) 1f - fightPresentation.startCrossFade else 0f
        val duelOpacity = if (fightPresentation.duelBackgroundActive) fightPresentation.startCrossFade else 0f
        return "{\"mineIndex\":${fightPresentation.mineIndex},\"enemyIndex\":${fightPresentation.enemyIndex}," +
            "\"backgrounds\":[[${fightPresentation.introBackgroundActive},${FullBattleTraceRecorder.number(introOpacity)}]," +
            "[${fightPresentation.duelBackgroundActive},${FullBattleTraceRecorder.number(duelOpacity)}]]," +
            "\"units\":[${fighterJson(slot0)},${fighterJson(slot1)}]," +
            "\"speeches\":[${speechJson(slot0)},${speechJson(slot1)}]}"
    }

    private fun recordFullBattleTraceFrame(
        delta: Float,
        observation: String? = null,
        advanceFrame: Boolean = true,
    ) {
        val recorder = fullTraceRecorder ?: return
        val frame = if (advanceFrame) recorder.nextFrame(elapsed) else recorder.upcomingFrame()
        // A defeated source BattleUnit remains in the scene graph while its
        // hurt/death/hidden callbacks run. Excluding presentationUnits made
        // real anime23 episodes look like game-side omissions in the order
        // report even though the live death driver was executing them.
        val unitsJson = battle.presentationUnits().sortedWith(compareBy<BattleUnit>({ it.faction.ordinal }, { it.id })).joinToString(",") { unit ->
            val move = movementAnimation?.takeIf { it.unitId == unit.id && animationClock() < it.endsAt }
            val moveSample = move?.let { BattleUnitMoveTimeline.sample(it.path, it.timeline, animationClock() - it.startedAt) }
            val active = actionAnimation?.takeIf { it.unitId == unit.id && animationClock() < it.endsAt }
                ?: hitReactionAnimations[unit.id]?.takeIf { animationClock() in it.startedAt..<it.endsAt }
                ?: deathAnimations[unit.id]?.takeIf { animationClock() in it.startedAt..<it.endsAt }
            val scripted = scriptedUnitVisuals[unit.id]
            val action = if (move != null) 20 else active?.sourceAction ?: scripted?.action ?: defaultPresentationAction(unit).action
            val direction = moveSample?.direction ?: active?.direction ?: unit.direction
            val startedAt = move?.startedAt ?: active?.startedAt ?: scripted?.startedAt ?: battleElapsed
            val animationTime = (animationClock() - startedAt).coerceAtLeast(0f)
            val sprite = battleSpriteFrame(action, direction, animationTime, loop = move != null)
            val characterId = unit.characterId ?: -1
            val internalIndex = unit.id.substringAfterLast('-').toIntOrNull() ?: -1
            val rates = (0..7).joinToString(",") { (unit.rateAccumulators[it] ?: 0).toString() }
            val skills = listOf(7, 43, 197, 262, 276).joinToString(",") { "[$it,${unit.skills[it]?.and(255) ?: 255}]" }
            val attackOffsets = unit.attackOffsets.joinToString(",") { (x, y) -> "[$x,$y]" }
            // Canonical source BATTLE_UNIT_STATUS2 slots. Source lift values
            // are DOWN=0, NORMAL=1, UP=2; persistent states use DOWN when set.
            val statuses = (0..14).joinToString(",") { sourceIndex ->
                when (sourceIndex) {
                    in 0..5 -> ((unit.attributeLifts[BattleAttribute.entries[sourceIndex]] ?: 0) + 1).coerceIn(0, 2)
                    7 -> if (BattleStatus.PARALYSIS in unit.statuses) 0 else 1
                    8 -> if (BattleStatus.SILENCE in unit.statuses) 0 else 1
                    9 -> if (BattleStatus.CONFUSION in unit.statuses) 0 else 1
                    10 -> if (BattleStatus.POISON in unit.statuses) 0 else 1
                    13 -> if (BattleStatus.LOST in unit.statuses) 0 else 1
                    14 -> if (unit.hasActed) 0 else 1
                    else -> 1
                }.toString()
            }
            val statusRounds = (0..14).joinToString(",") { sourceIndex ->
                when (sourceIndex) {
                    in 0..5 -> unit.attributeLiftRounds[BattleAttribute.entries[sourceIndex]] ?: 0
                    7 -> unit.statuses[BattleStatus.PARALYSIS] ?: 0
                    8 -> unit.statuses[BattleStatus.SILENCE] ?: 0
                    9 -> unit.statuses[BattleStatus.CONFUSION] ?: 0
                    10 -> unit.statuses[BattleStatus.POISON] ?: 0
                    13 -> unit.statuses[BattleStatus.LOST] ?: 0
                    14 -> unit.actionStatusRound
                    else -> 0
                }.toString()
            }
            val visual = visualTile(unit)
            "[$internalIndex,$characterId,${unit.type().ordinal},${unit.tileX},${unit.tileY},${unit.hitPoints},${unit.magicPoints},$direction,$action,${if (unit.visible) 1 else 0},1,${if (unit.hasActed) 1 else 0},${unit.ai},${unit.aiValue},\"anime${action}_$direction\",${FullBattleTraceRecorder.number(animationTime)},${sprite?.let { "[0,${it.sourceY},${it.sourceWidth},${it.sourceHeight}]" } ?: "null"},{\"abilities\":[${unit.attack},${unit.defense},${unit.spirit},${unit.critical},${unit.morale}],\"level\":${unit.level},\"posts\":${unit.posts},\"arm\":${unit.armId},\"experience\":${unit.experience},\"growth\":{\"abilities\":[${unit.attack},${unit.defense},${unit.spirit},${unit.critical},${unit.morale}],\"level\":${unit.level},\"posts\":${unit.posts},\"arm\":${unit.armId},\"experience\":${unit.experience}},\"attackOffsets\":[$attackOffsets],\"terrain\":${unit.terrainImpacts[terrainGrid.terrainAt(unit.tileX, unit.tileY)] ?: 100},\"rates\":[$rates],\"skills\":[$skills],\"statuses\":[$statuses],\"statusRounds\":[$statusRounds],\"visual\":[${FullBattleTraceRecorder.number(visual.first)},${FullBattleTraceRecorder.number(visual.second)}]}]"
        }
        val actions = battle.traceActions.joinToString(",") { "\"${FullBattleTraceRecorder.escape(it)}\"" }
        // Keep the adjudication inputs beside each frame.  A terminal result
        // during scene0 is otherwise ambiguous: it can mean a real defeat,
        // an empty mine roster, or an accidentally shortened max-round setup.
        val playerCount = battle.units.values.count { it.type() == Faction.PLAYER }
        val friendCount = battle.units.values.count { it.type() == Faction.FRIEND }
        val enemyCount = battle.units.values.count { it.type().isEnemySide() }
        val aiPresentation = pendingAiResolution?.let { resolution ->
            val actor = battle.presentationUnit(resolution.actorId)?.characterId ?: -1
            val target = resolution.targetId?.let(battle::presentationUnit)
            val targetCharacterId = target?.characterId ?: -1
            val targetHpBefore = resolution.targetId?.let(resolution.healthBeforeAction::get) ?: -1
            "{\"stage\":\"$aiPresentationStage\",\"actor\":$actor,\"from\":[${resolution.fromX},${resolution.fromY}],\"to\":[${resolution.toX},${resolution.toY}],\"target\":$targetCharacterId,\"targetHpBefore\":$targetHpBefore,\"deferred\":${battle.pendingActionTransaction != null},\"hasAction\":${resolution.result != null}}"
        } ?: "null"
        val bootstrapComplete = bootstrapPhase == BattleBootstrapPhase.COMPLETE
        val traceCamp = if (bootstrapComplete) battle.activeFaction.ordinal else -1
        val traceOutcome = battle.outcome().takeIf { bootstrapComplete }
        val dialogueSourceText = scriptRuntime.currentDialogueSourceText
        val dialogueIdentity = dialogueSourceText?.let { sourceText ->
            "${scriptRuntime.dialogueLifecycleRevision}:${java.lang.Integer.toHexString(sourceText.hashCode())}"
        }.orEmpty()
        val dialogueSpeakerId = scriptRuntime.currentDialogue?.speakerId.orEmpty()
        val dialogueText = dialogueSourceText?.let { ScenarioInterpreter.parseDialogueBlocks(it) }
            ?.joinToString("\n") { it.text }.orEmpty()
        val (mapObjectRevision, mapObjectsJson) = fullTraceMapObjectsJson()
        val fightJson = fullTraceFightJson()
        val bootstrapBusy = if (bootstrapComplete) emptyList() else bootstrapPresentationBusyReasons()
        val observationJson = observation?.let {
            ",\"observation\":\"${FullBattleTraceRecorder.escape(it)}\""
        }.orEmpty()
        // `end` remains the tactical outcome flag for existing consumers.
        // Keep the source lifecycle separate so audits can distinguish an
        // outcome that starts result handling from stage.end()/lose().
        val scriptedOutcome = scriptRuntime.stage.scriptedBattleOutcome?.name
        val modalKind = scriptRuntime.currentModalKind?.name
        val resultLifecycleJson = ",\"scriptEnded\":${scriptRuntime.stage.battleEndedByScript},\"scriptedOutcome\":${scriptedOutcome?.let { "\"$it\"" } ?: "null"},\"resultFlow\":\"$resultFlow\",\"modalKind\":${modalKind?.let { "\"$it\"" } ?: "null"}" +
            ",\"resultCallbacks\":{\"pendingScriptPasses\":$pendingBattleScriptPassesAfterAction,\"pendingAiDeathPass\":$pendingAiUnitDeathScriptPass,\"postActionDeaths\":$postActionDeathsStarted,\"pendingAiResolution\":${pendingAiResolution != null},\"activeAiCamp\":${activeAiCamp?.let { "\"$it\"" } ?: "null"},\"roundLayer\":${activeRoundLayer != null},\"turnSettlement\":${activeTurnSettlement != null},\"combatPresentation\":${combatPresentationBusy()}}"
        val driverState = "{\"selectedUnit\":${selectedUnitId?.let { "\"${FullBattleTraceRecorder.escape(it)}\"" } ?: "null"},\"commandPhase\":\"${battleCommandFlow.phase}\",\"lastInput\":${lastFullBattleInput?.let { "\"${FullBattleTraceRecorder.escape(it)}\"" } ?: "null"},\"menuTap\":${lastFullBattleMenuTap?.let { "\"${FullBattleTraceRecorder.escape(it)}\"" } ?: "null"},\"eventMessage\":\"${FullBattleTraceRecorder.escape(eventMessage)}\",\"autoOverlay\":\"${autoBattleFlow.view().overlay}\"}"
        val row = "{\"f\":$frame,\"t\":${FullBattleTraceRecorder.number(elapsed)},\"dt\":${FullBattleTraceRecorder.number(delta)},\"round\":${battle.round},\"camp\":$traceCamp,\"maxRounds\":${battle.maxRounds},\"playerCount\":$playerCount,\"friendCount\":$friendCount,\"enemyCount\":$enemyCount,\"paused\":${scriptRuntime.state != PlaybackState.COMPLETE},\"end\":${traceOutcome != null},\"collocation\":${autoBattleFlow.view().collocation},\"dialogue\":${if (scriptRuntime.state == PlaybackState.DIALOGUE) 1 else 0},\"dialogueRevision\":${scriptRuntime.dialogueLifecycleRevision},\"dialogueIdentity\":\"${FullBattleTraceRecorder.escape(dialogueIdentity)}\",\"dialogueSpeakerId\":\"${FullBattleTraceRecorder.escape(dialogueSpeakerId)}\",\"dialogueText\":\"${FullBattleTraceRecorder.escape(dialogueText)}\",\"phase\":\"${turnController.phase}\",\"script\":\"${scriptRuntime.state}\",\"bootstrapBusy\":[${bootstrapBusy.joinToString(",") { "\"$it\"" }}],\"camera\":[${FullBattleTraceRecorder.number(battleCamera.contentX)},${FullBattleTraceRecorder.number(battleCamera.contentY)}],\"mapObjectRevision\":$mapObjectRevision,\"mapObjects\":$mapObjectsJson,\"fight\":$fightJson,\"aiPresentation\":$aiPresentation,\"actions\":[$actions],\"units\":[$unitsJson],\"driver\":$driverState$observationJson}"
        recorder.addFrame(row.dropLast(1) + resultLifecycleJson + "}")
    }

    private fun finishFullBattleTrace(reason: String) {
        val recorder = fullTraceRecorder ?: return
        if (fullTraceFinished) return
        fullTraceFinished = true
        val bootstrapComplete = bootstrapPhase == BattleBootstrapPhase.COMPLETE
        val outcome = battle.outcome().takeIf { bootstrapComplete }
        val camp = if (bootstrapComplete) battle.activeFaction.ordinal else -1
        val expectedScript = "Game/data/RS/$sourceScenario"
        val mapName = mapFile?.path().orEmpty()
        // Evidence comes from the live runtime objects used by this battle;
        // the batch verifier must not accept the requested CLI label alone.
        val scenarioEvidence = "{\"requestedScenario\":\"${FullBattleTraceRecorder.escape(fullTraceConfig?.scenario ?: sourceScenario)}\",\"expectedScript\":\"${FullBattleTraceRecorder.escape(expectedScript)}\",\"loadedScript\":\"${FullBattleTraceRecorder.escape(expectedScript)}\",\"route\":\"JojoGame.showBattleSandbox\",\"seededUnitIds\":[${campaign.roster.battleRoster.joinToString(",")}],\"loadBgCalls\":[{\"mapIndex\":$loadedBattleMapIndex}],\"loadedMap\":{\"mapIndex\":$loadedBattleMapIndex,\"textureName\":\"${FullBattleTraceRecorder.escape(mapName)}\",\"width\":${terrainGrid.width},\"height\":${terrainGrid.height}}}"
        val summary = "{\"scenario\":\"${FullBattleTraceRecorder.escape(sourceScenario)}\",\"gameScenario\":$scenarioEvidence,\"round\":${battle.round},\"camp\":$camp,\"end\":${outcome != null},\"frameCount\":${recorder.recordedRowCount},\"outcome\":${outcome?.let { "\"$it\"" } ?: "null"}}"
        recorder.write(reason, summary)
        Gdx.app.log("JojoGame", "FULL_BATTLE_TRACE: ${fullTraceConfig?.outputPath}; frames=${recorder.recordedRowCount}; reason=$reason")
        if (fullTraceConfig?.exitOnFinish != false) Gdx.app.exit()
    }

    /** Input provenance is appended only after ProductionBattleInputDriver dispatches it. */
    internal fun recordFullBattleInput(context: String) {
        lastFullBattleInput = context
        fullTraceRecorder?.recordInput(context)
    }

    /** Read-only observation for the desktop campaign E2E driver. */
    internal fun campaignE2eState(): CampaignE2eBattleState {
        fun screenPoint(x: Int, y: Int): Pair<Int, Int> {
            val projected = viewport.project(Vector2(
                boardLeft + x * boardTile + boardTile / 2f,
                tileBottom(y) + boardTile / 2f,
            ))
            return projected.x.toInt() to (Gdx.graphics.height - projected.y).toInt()
        }
        fun projectWorldPoint(worldX: Float, worldY: Float): Pair<Int, Int> {
            val projected = viewport.project(Vector2(worldX, worldY))
            return projected.x.toInt() to (Gdx.graphics.height - projected.y).toInt()
        }
        val autoView = autoBattleFlow.view()
        return BattleCampaignE2eAdapter.computeState(
            BattleCampaignE2eAdapter.ProjectionContext(
                scenario = sourceScenario,
                battle = battle,
                selectedUnitId = selectedUnitId,
                authoredMechanicRoute = authoredMechanicRoute,
                scriptState = scriptRuntime.state,
                selectedChoice = scriptRuntime.selectedChoice,
                bootstrapPhase = bootstrapPhase,
                initialPlayerCampScriptStarted = initialPlayerCampScriptStarted,
                resultScene1Observed = resultScene1Observed,
                naturalOutcomeScriptStarted = naturalOutcomeScriptStarted,
                postBattleSceneStarted = postBattleSceneStarted,
                rewardOpen = rewardFlow != null,
                winConditionsOpen = scriptWinConditions != null,
                savePromptOpen = resultFlow == ResultFlow.WIN_SAVE_PROMPT,
                losePromptOpen = loseSceneFlow?.state == LoseSceneFlow.State.PROMPT,
                loseTitleScreenPoint = projectWorldPoint(844.186f, 296.285f),
                playerMoveCommitted = campaignE2ePlayerMoveCommitted,
                campaignStage = game.campaignStage(),
                turnPhase = turnController.phase,
                battleMenuOpen = battleMenuOpen,
                battleCommandOpen = battleCommandFlow.phase == BattleCommandFlow.Phase.COMMAND,
                battleTargetSelectionOpen = battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION,
                magickListOpen = magickListLayer != null,
                magicMode = magicMode,
                waitCommandScreenPoint = projectWorldPoint(1060.6f, 225.42f),
                endRoundCommandScreenPoint = projectWorldPoint(15.13372f + 8f * 88f + 44f, 160.29f),
                battleMenuButtonScreenPoint = projectWorldPoint(1383.9535f, 38f),
                autoBattleToggleScreenPoint = projectWorldPoint(579.4365f, 295.197f),
                autoBattleConfirmScreenPoint = projectWorldPoint(919.536f, 295.197f),
                autoBattleOverlay = autoView.overlay,
                autoBattleChecked = autoView.checked,
                collocation = autoView.collocation,
                committedPlayerMove = campaignE2eCommittedPlayerMove,
                screenPoint = ::screenPoint,
                projectWorldPoint = ::projectWorldPoint,
            )
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

    /**
     * BattleScreen._attack3 changes target HP and starts the harm number only
     * after the attacker clip dispatches `hit`. Battle has already
     * resolved the result, so retain the old visual HP until that timeline
     * point instead of changing it at input time.
     */
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
        val hitAt = animation.startedAt + requireNotNull(battleSprites.hitTime(animation.sourceAction, animation.direction)) {
            "원본 BRAnime anime${animation.sourceAction} 방향 ${animation.direction}에 hit 이벤트가 없습니다"
        }
        val targetUnit = battle.presentationUnit(target) ?: return
        val before = healthBeforeAction[target] ?: targetUnit.hitPoints
        val deferredMutation = battle.pendingActionTransaction?.takeIf { it.actorId == actor }
        if (attack.hit && attack.physicalPasses.firstOrNull()?.targets?.isNotEmpty() == true) {
            val visualHp = healthBeforeAction.toMutableMap()
            val visualMp = mutableMapOf<String, Int>()
            attack.physicalPasses.flatMap(PhysicalAttackPass::targets)
                .groupBy(PhysicalAttackTargetResult::targetId)
                .forEach { (id, results) ->
                    val unit = battle.presentationUnit(id) ?: return@forEach
                    visualMp[id] = deferredMutation?.initialMp(id)
                        ?: (unit.magicPoints + results.sumOf { it.mpShieldDamage - it.automaticPropertyMpDelta })
                            .coerceIn(0, unit.maxMagicPoints)
                }
            val queue = PhysicalPassPresentationQueue(
                passes = attack.physicalPasses,
                nextPassIndex = 1,
                startsAt = hitAt,
                visualHp = visualHp,
                visualMp = visualMp,
                counterMagicId = attack.counterMagicId,
                counterMagic = attack.counterMagic,
                counterCasterId = target,
                counterTargetId = actor,
            )
            val passEndsAt = schedulePhysicalPassTargets(
                pass = attack.physicalPasses.first(),
                animation = animation,
                hitAt = hitAt,
                queue = queue,
            )
            if (queue.nextPassIndex < queue.passes.size || queue.counterMagic != null) {
                queue.startsAt = passEndsAt
                queuedPhysicalPresentation = queue
            }
            return
        }
        // `_attack3` starts at the attack clip's authored hit event.  Its HP
        // write and hit/block sound occur in that callback, never when the
        // attack action itself begins.
        scheduleBattleMutation(hitAt) {
            battle.presentationUnit(target)?.let(::focusCameraOn)
            audio.playBattleEffect(when {
                !attack.hit -> 30 // BLOCK_QING
                attack.critical -> 36 // HARM_ZHONG
                else -> 35 // HARM_QING
            })
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
            // A blocked/missed active pass does not suppress FAN_JI. The
            // model retains that counter in physicalPasses after the empty
            // ACTIVE pass. The previous renderer discarded it here, then
            // commitAll published its damage only at turn completion; restore
            // consequently ran before the visible counter callback.
            val remainingPasses = attack.physicalPasses.drop(1)
            if (remainingPasses.isNotEmpty() || attack.counterMagic != null) {
                queuedPhysicalPresentation = PhysicalPassPresentationQueue(
                    passes = remainingPasses,
                    nextPassIndex = 0,
                    startsAt = reactionEndsAt,
                    visualHp = healthBeforeAction.toMutableMap(),
                    visualMp = mutableMapOf<String, Int>().apply {
                        listOf(actor, target).forEach { id ->
                            battle.presentationUnit(id)?.let { unit ->
                                this[id] = deferredMutation?.initialMp(id) ?: unit.magicPoints
                            }
                        }
                    },
                    counterMagicId = attack.counterMagicId,
                    counterMagic = attack.counterMagic,
                    counterCasterId = target,
                    counterTargetId = actor,
                )
            }
            return
        }
        // MPFY changes only MP, but source `_attack3` still plays anime32 and
        // publishes an MP harm number at the same authored hit event.
        // In deferred AI playback targetUnit still intentionally carries
        // `before`; comparing the live object here would suppress every
        // normal hurt/death clip. Use the resolved payload instead.
        if (attack.damage == 0 && attack.mpShieldDamage == 0) return
        val hitSequence = BattlePhysicalPresentationTimeline.sequence(
            primaryId = target,
            primaryDamage = attack.damage,
            splash = attack.splashTargets,
            hitAt = hitAt,
            durationFor = { id -> requireSourceActionDuration(32, battleDirection(id, actor)) },
        )
        val primaryHit = hitSequence.first()
        val reactionDuration = primaryHit.endsAt - primaryHit.startsAt
        // `_attack2` returns the attacker to default only after the final
        // sequential `_attack3` reaction. For a single target this is earlier
        // than anime21/anime25's natural FINISHED event; for CTGJ it can be
        // later. Keep the final attack frame until that exact callback edge.
        actionAnimation = animation.copy(endsAt = hitSequence.last().endsAt)
        scheduleHitReaction(target, battleDirection(target, actor), primaryHit.startsAt, primaryHit.endsAt, 32)
        val firstTo = (before - attack.damage).coerceAtLeast(0)
        // MPFY's source break skips the HP write entirely. Keep the existing
        // HP bar while showing only the yellow MP loss number.
        if (attack.damage > 0) healthTimeline.schedule(target, before, firstTo, primaryHit.startsAt)
        harmNumberAnimations[target] = HarmNumberAnimation(
            amount = if (attack.mpShieldDamage > 0) attack.mpShieldDamage else attack.damage,
            isHp = attack.mpShieldDamage == 0,
            startedAt = primaryHit.startsAt,
            endsAt = primaryHit.endsAt,
        )
        // BattleScreen._attack2 awaits _attack3 for every countAtkHarm target
        // in order.  CTGJ area records therefore begin only after the main
        // target's hit reaction, and follow-up/counter processing begins
        // after the final area record instead of overlapping it.
        hitSequence.drop(1).forEach { splashHit ->
            val splashUnit = battle.presentationUnit(splashHit.targetId) ?: return@forEach
            val splashBefore = healthBeforeAction[splashHit.targetId] ?: (splashUnit.hitPoints + splashHit.damage)
            val splashAfter = (splashBefore - splashHit.damage).coerceAtLeast(0)
            scheduleHitReaction(
                splashHit.targetId, battleDirection(splashHit.targetId, actor),
                splashHit.startsAt, splashHit.endsAt, 32,
            )
            scheduleBattleMutation(splashHit.startsAt) {
                battle.presentationUnit(splashHit.targetId)?.let(::focusCameraOn)
                deferredMutation?.commitVitals(splashHit.targetId, hp = splashAfter)
                deferredMutation?.commitNextHitSideEffect()
            }
            healthTimeline.schedule(
                splashHit.targetId,
                splashBefore,
                splashAfter,
                splashHit.startsAt,
            )
            harmNumberAnimations[splashHit.targetId] = HarmNumberAnimation(splashHit.damage, true, splashHit.startsAt, splashHit.endsAt)
        }
        val areaEndsAt = hitSequence.last().endsAt
        if ((attack.followUpDamage > 0 || attack.followUpMpShieldDamage > 0) && firstTo > 0) {
            queuedFollowUpPresentation = FollowUpPresentation(
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
                counterTargetHpBefore = healthBeforeAction[actor] ?: battle.presentationUnit(actor)?.hitPoints ?: 0,
            )
        } else if ((attack.counterDamage > 0 || attack.counterMpShieldDamage > 0) && !attack.defeated) {
            val actorUnit = battle.presentationUnit(actor) ?: return
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
    }

    /**
     * `_magicProcess` commits HP/MP only from `playMeff`'s completed-effect
     * callback.  Battle resolves it eagerly, so keep presentation values at
     * the input snapshot until the authored meff strip has ended.
     */
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
        result.passes.forEachIndexed { passIndex, pass ->
            // A 255/no-meff spell still executes playMeff's callback (and its
            // local h settlement) synchronously after preparation.
            val effect = effectAnimations.getOrNull(passIndex)
            val effectStartedAt = effect?.startedAt ?: (actionAnimation?.endsAt ?: animationClock())
            val effectEndsAt = effect?.endsAt ?: effectStartedAt
            val effectAt = effectStartedAt + (effect?.let { magicEffects.effect(it.effectId)?.hitTime }
                ?: (effectEndsAt - effectStartedAt))
            // Source `_magic` explicitly excludes the 255 no-effect sentinel.
            val mcall = (magic?.effectId ?: 0) in 100..254
            // `_magic` focuses its primary target before MCall.  Once MCall
            // returns, `_magicProcess` focuses every selected unit in input
            // order; a plain meff reaches that boundary immediately.
            if (mcall) pass.firstOrNull()?.targetId?.let { primaryId ->
                scheduleBattleMutation(effectStartedAt) {
                    battle.presentationUnit(primaryId)?.let { focusCameraOn(it, forceCenter = true) }
                }
            }
            pass.forEach { targetResult ->
                scheduleBattleMutation(if (mcall) effectEndsAt else effectStartedAt) {
                    battle.presentationUnit(targetResult.targetId)?.let { focusCameraOn(it, forceCenter = true) }
                }
            }
            pass.forEach { targetResult ->
                val unit = battle.presentationUnit(targetResult.targetId) ?: return@forEach
                val sourceAction = if (targetResult.hit) 3 else 26
                // playMeff calls setAction2(action) without a dir argument;
                // magic reactions retain the target's existing facing.
                val direction = unit.direction
                val reactionEndsAt = maxOf(
                    effectEndsAt,
                    effectAt + requireSourceActionDuration(sourceAction, direction),
                )
                scheduleHitReaction(targetResult.targetId, direction, effectAt, reactionEndsAt, sourceAction)
            }
            BattleMagicPresentation.changes(pass, caster, magic).forEach { change ->
                val unit = battle.presentationUnit(change.unitId) ?: return@forEach
                // Pass two begins with pass one's committed result, rather
                // than the Battle model's eagerly resolved final value.
                val before = visualHp[change.unitId] ?: unit.hitPoints - change.hpAdd
                if (change.hpAdd != 0) {
                    val after = (before + change.hpAdd).coerceIn(0, unit.maxHitPoints)
                    healthTimeline.schedule(change.unitId, before, after, effectAt)
                    visualHp[change.unitId] = after
                }
                val beforeMp = visualMp[change.unitId] ?: deferredMutation?.initialMp(change.unitId) ?: unit.magicPoints
                val afterMp = (beforeMp + change.mpAdd).coerceIn(0, unit.maxMagicPoints)
                if (change.mpAdd != 0) visualMp[change.unitId] = afterMp
                if (deferredMutation != null && (change.hpAdd != 0 || change.mpAdd != 0)) {
                    val committedHp = visualHp[change.unitId] ?: unit.hitPoints
                    scheduleBattleMutation(effectAt) {
                        deferredMutation.commitVitals(change.unitId, committedHp, afterMp)
                        if (change.hpAdd < 0) deferredMutation.commitNextHitSideEffect()
                    }
                }
                // Presentation harm numbers give MP_ADD precedence when a payload
                // contains both keys; retain that exact choice in the renderer.
                val value = if (change.mpAdd != 0) change.mpAdd else change.hpAdd
                if (value != 0) {
                    val duration = requireSourceActionDuration(if (change.mpAdd != 0) 3 else 32, unit.direction)
                    harmNumberAnimations[change.unitId] = HarmNumberAnimation(value, change.mpAdd == 0, effectAt, effectAt + duration)
                }
            }
            // `_magicProcess` awaits target `l`, then caster `o`, and only
            // then calls `_jiesuan(h)`.  This is a callback barrier, not a
            // hit-time status refresh.  Its source-order h rows include
            // damage-only successes whose STATES payload is empty.
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

    /** Starts the next `_attack2` pass only after every `_attack3` callback in the prior pass. */
    private fun startQueuedPhysicalPassPresentation() {
        val queue = queuedPhysicalPresentation ?: return
        if (activeTurnSettlement != null) return
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
                battle.presentationUnit(queue.counterCasterId)?.let(::focusCameraOn)
                val characterId = battle.presentationUnit(queue.counterCasterId)?.characterId
                scriptRuntime.presentExternalBattleDialogue(Dialogue(characterId?.toString(), counterSpeech))
                return
            }
            queuedPhysicalPresentation = null
            startCounterMagicPresentation(queue)
            return
        }
        if (queue.awaitingSpeechPassIndex == queue.nextPassIndex) {
            queue.awaitingSpeechPassIndex = null
            // say4 resumes the coroutine at the close callback; an authored
            // action must start now, not at the pre-dialogue queue timestamp.
            queue.startsAt = animationClock()
        }
        if (pass.criticalSpeech != null && queue.nextPassIndex !in queue.presentedSpeechPasses) {
            queue.presentedSpeechPasses += queue.nextPassIndex
            queue.awaitingSpeechPassIndex = queue.nextPassIndex
            battle.presentationUnit(pass.attackerId)?.let(::focusCameraOn)
            val characterId = battle.presentationUnit(pass.attackerId)?.characterId
            scriptRuntime.presentExternalBattleDialogue(Dialogue(characterId?.toString(), pass.criticalSpeech))
            return
        }
        val attacker = battle.presentationUnit(pass.attackerId) ?: run {
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
                battle.presentationUnit(primaryTargetId)?.let(::focusCameraOn)
                audio.playBattleEffect(30)
            }
            actionAnimation = animation.copy(endsAt = reactionEndsAt)
            reactionEndsAt
        } else {
            schedulePhysicalPassTargets(pass, animation, hitAt, queue)
        }
        queue.nextPassIndex++
        if (queue.nextPassIndex >= queue.passes.size && queue.counterMagic == null) queuedPhysicalPresentation = null
    }

    /** `_magic` repeats say4 -> preparation -> _magicProcess for every CLLJ pass. */
    private fun startQueuedMagicPassPresentation() {
        val queue = queuedMagicPresentation ?: return
        if (activeTurnSettlement != null) return
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
            battle.presentationUnit(queue.casterId)?.let(::focusCameraOn)
            val characterId = battle.presentationUnit(queue.casterId)?.characterId
            scriptRuntime.presentExternalBattleDialogue(Dialogue(characterId?.toString(), speech))
            return
        }
        val caster = battle.presentationUnit(queue.casterId) ?: run {
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
            MagicEffectAnimation(queue.effectId, pass.map(MagicTarget::targetId), preparation.endsAt, preparation.endsAt + it.duration)
                .also(magicEffectAnimations::add)
        }
        val passResult = queue.result.copy(
            targets = pass,
            passes = listOf(pass),
            criticalSpeeches = listOf(speech),
            localSettlements = listOf(queue.result.localSettlements.getOrNull(index) ?: MagicLocalSettlement(emptyList())),
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
        BattleMagicPresentation.changes(pass, casterId, profile).forEach { change ->
            battle.presentationUnit(change.unitId)?.let { unit ->
                hp[change.unitId] = ((hp[change.unitId] ?: unit.hitPoints) + change.hpAdd)
                    .coerceIn(0, unit.maxHitPoints)
                mp[change.unitId] = ((mp[change.unitId] ?: unit.magicPoints) + change.mpAdd)
                    .coerceIn(0, unit.maxMagicPoints)
            }
        }
    }

    /** Original `_attack6`: CLFJ begins only after the complete active `_attack2` chain. */
    private fun startCounterMagicPresentation(queue: PhysicalPassPresentationQueue) {
        val magic = queue.counterMagic ?: return
        val magicId = queue.counterMagicId ?: return
        val caster = battle.presentationUnit(queue.counterCasterId) ?: return
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

    /**
     * One source pass: the attacker reaches `hit` once, then every primary/
     * CTGJ target completes guard-or-hurt, FTSH and ZDSY before the next one.
     */
    private fun schedulePhysicalPassTargets(
        pass: PhysicalAttackPass,
        animation: UnitActionAnimation,
        hitAt: Float,
        queue: PhysicalPassPresentationQueue,
    ): Float {
        var cursor = hitAt
        pass.targets.forEach { result ->
            val target = battle.presentationUnit(result.targetId) ?: return@forEach
            val guard = result.resolvedHarm == 0 && result.mpShieldDamage == 0
            val reactionAction = if (guard) 26 else 32
            val reactionDirection = battleDirection(target.id, pass.attackerId)
            val reactionEndsAt = cursor + requireSourceActionDuration(reactionAction, reactionDirection)
            val hpBefore = queue.visualHp[result.targetId] ?: target.hitPoints
            val hpAfterHarm = (hpBefore - result.damage).coerceAtLeast(0)
            val mpBefore = queue.visualMp[result.targetId] ?: target.magicPoints
            val mpAfterHarm = (mpBefore - result.mpShieldDamage).coerceAtLeast(0)
            // Source `_attack3` changes the attacker's live HP for XXGJ and
            // QXL, and applies JQFY/XSJQ money mutations, before anime32 is
            // started. These are per-target values: aggregating them until
            // the final `_jiesuan(g_charinfo)` loses CTGJ/pass timing.
            val attacker = battle.presentationUnit(pass.attackerId)
            val attackerHpBefore = queue.visualHp[pass.attackerId] ?: attacker?.hitPoints ?: 0
            val attackerHealing = result.lifeStealHealing + result.qxlHealing
            val attackerHpAfterHealing = attacker?.let {
                (attackerHpBefore + attackerHealing).coerceIn(0, it.maxHitPoints)
            } ?: attackerHpBefore
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
                battle.presentationUnit(result.targetId)?.let(::focusCameraOn)
                audio.playBattleEffect(if (guard) {
                    if (pass.critical) 31 else 30
                } else if (pass.critical) 36 else 35)
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
            if (!guard) {
                harmNumberAnimations[result.targetId] = HarmNumberAnimation(
                    amount = if (result.mpShieldDamage > 0) result.mpShieldDamage else result.resolvedHarm,
                    isHp = result.mpShieldDamage == 0,
                    startedAt = cursor,
                    endsAt = reactionEndsAt,
                )
            }
            queue.visualHp[result.targetId] = hpAfterHarm
            queue.visualMp[result.targetId] = mpAfterHarm
            if (attackerHealing > 0) queue.visualHp[pass.attackerId] = attackerHpAfterHealing

            // MENG_JI/NI_FAN and FTSH are committed only after the guard/hurt
            // callback. They affect the striker and retain their source order.
            val retaliationDamage = result.blockRetaliations.sumOf { it.damage }
            val postReactionDamage = retaliationDamage + result.recoilDamage
            if (postReactionDamage > 0) {
                val attackerUnit = battle.presentationUnit(pass.attackerId)
                val attackerBefore = queue.visualHp[pass.attackerId] ?: attackerUnit?.hitPoints ?: 0
                val keepAliveDamage = result.recoilDamage
                val afterRetaliation = (attackerBefore - retaliationDamage).coerceAtLeast(0)
                val attackerAfter = if (keepAliveDamage > 0) {
                    maxOf(1, afterRetaliation - keepAliveDamage)
                } else {
                    afterRetaliation
                }
                scheduleBattleMutation(reactionEndsAt) {
                    healthTimeline.schedule(pass.attackerId, attackerBefore, attackerAfter, reactionEndsAt)
                    battle.pendingActionTransaction?.commitVitals(pass.attackerId, hp = attackerAfter)
                }
                queue.visualHp[pass.attackerId] = attackerAfter
            }

            val automaticEndsAt = if (result.automaticProperty != null) reactionEndsAt + 1.5f else reactionEndsAt
            if (result.automaticPropertyHpDelta != 0 || result.automaticPropertyMpDelta != 0) {
                healthTimelineHoldUntil[result.targetId] = automaticEndsAt
                val hpAfterProperty = (hpAfterHarm + result.automaticPropertyHpDelta).coerceIn(0, target.maxHitPoints)
                val mpAfterProperty = (mpAfterHarm + result.automaticPropertyMpDelta).coerceIn(0, target.maxMagicPoints)
                scheduleBattleMutation(cursor) {
                    // Install the future transition only after the damage is
                    // visible, otherwise the eager final HP leaks before hit.
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
            val localPlan = if (localSettlement.entries.isNotEmpty()) {
                buildLocalSettlementPlan(localSettlement, pass.attackerId)
            } else null
            val localOperations = localPlan?.let(::settlementOperations).orEmpty()
            if (result.hasLocalStatusSettlement) {
                scheduleBattleMutation(automaticEndsAt) {
                    localSettlement.entries.forEach { entry ->
                        battle.pendingActionTransaction?.commitStatuses(entry)
                    }
                    if (localPlan != null) startLocalSettlement(localPlan, localOperations)
                }
            }
            cursor = automaticEndsAt + localSettlementDuration(localOperations)
        }
        actionAnimation = animation.copy(endsAt = cursor)
        return cursor
    }

    private fun startQueuedFollowUpPresentation() {
        val queued = queuedFollowUpPresentation ?: return
        if (animationClock() < queued.startsAt) return
        queuedFollowUpPresentation = null
        val attacker = battle.presentationUnit(queued.attackerId) ?: return
        val target = battle.presentationUnit(queued.targetId) ?: return
        val direction = battleDirection(attacker.id, target.id)
        val sourceAction = BattleAttackSequence.selectAttackAction(critical = queued.critical, attackDelay = attacker.attackDelay)
        val animation = sourceActionAnimation(attacker.id, sourceAction, direction, queued.startsAt)
        actionAnimation = animation
        val hitAt = animation.startedAt + requireNotNull(battleSprites.hitTime(sourceAction, direction)) {
            "원본 BRAnime anime$sourceAction 방향 ${direction}에 hit 이벤트가 없습니다"
        }
        val reactionDirection = battleDirection(target.id, attacker.id)
        val reactionDuration = requireSourceActionDuration(32, reactionDirection)
        val reactionEndsAt = hitAt + reactionDuration
        actionAnimation = animation.copy(endsAt = reactionEndsAt)
        scheduleHitReaction(target.id, reactionDirection, hitAt, reactionEndsAt, 32)
        scheduleBattleMutation(hitAt) {
            focusCameraOn(target)
            audio.playBattleEffect(if (queued.critical) 36 else 35)
            battle.pendingActionTransaction?.commitVitals(
                target.id,
                hp = (queued.targetHpBefore - queued.harm).coerceAtLeast(0).takeIf { queued.harm > 0 },
                mp = battle.pendingActionTransaction?.initialMp(target.id)
                    ?.minus(queued.mpShieldDamage)?.coerceAtLeast(0)
                    ?.takeIf { queued.mpShieldDamage > 0 },
            )
            battle.pendingActionTransaction?.commitNextHitSideEffect()
        }
        if (queued.harm > 0) healthTimeline.schedule(
            target.id, queued.targetHpBefore,
            (queued.targetHpBefore - queued.harm).coerceAtLeast(0), hitAt,
        )
        // Presentation harm numbers choose MP_ADD over HP_ADD when both occur.
        harmNumberAnimations[target.id] = HarmNumberAnimation(queued.harm, queued.mpShieldDamage == 0, hitAt, hitAt + reactionDuration)
        if ((queued.counterDamage > 0 || queued.counterMpShieldDamage > 0) && queued.targetHpBefore - queued.harm > 0) {
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
                startsAt = reactionEndsAt,
            )
        }
    }

    private fun startQueuedCounterPresentation() {
        val queued = queuedCounterPresentation ?: return
        if (animationClock() < queued.startsAt) return
        queuedCounterPresentation = null
        val attacker = battle.presentationUnit(queued.attackerId) ?: return
        val target = battle.presentationUnit(queued.targetId) ?: return
        val direction = battleDirection(attacker.id, target.id)
        val sourceAction = BattleAttackSequence.selectAttackAction(critical = queued.critical, attackDelay = attacker.attackDelay)
        val animation = sourceActionAnimation(attacker.id, sourceAction, direction, queued.startsAt)
        actionAnimation = animation
        val hitAt = animation.startedAt + requireNotNull(battleSprites.hitTime(sourceAction, direction)) {
            "원본 BRAnime anime$sourceAction 방향 ${direction}에 hit 이벤트가 없습니다"
        }
        val reactionDirection = battleDirection(target.id, attacker.id)
        val reactionDuration = requireSourceActionDuration(32, reactionDirection)
        val reactionEndsAt = hitAt + reactionDuration
        actionAnimation = animation.copy(endsAt = reactionEndsAt)
        scheduleHitReaction(target.id, reactionDirection, hitAt, reactionEndsAt, 32)
        val firstTo = (queued.targetHpBefore - queued.harm).coerceAtLeast(0)
        scheduleBattleMutation(hitAt) {
            focusCameraOn(target)
            audio.playBattleEffect(if (queued.critical) 36 else 35)
            battle.pendingActionTransaction?.commitVitals(
                target.id,
                hp = firstTo.takeIf { queued.harm > 0 },
                mp = battle.pendingActionTransaction?.initialMp(target.id)
                    ?.minus(queued.mpShieldDamage)?.coerceAtLeast(0)
                    ?.takeIf { queued.mpShieldDamage > 0 },
            )
            battle.pendingActionTransaction?.commitNextHitSideEffect()
        }
        if (queued.harm > 0) healthTimeline.schedule(target.id, queued.targetHpBefore, firstTo, hitAt)
        harmNumberAnimations[target.id] = HarmNumberAnimation(queued.harm, queued.mpShieldDamage == 0, hitAt, hitAt + reactionDuration)
        if ((queued.followUpDamage > 0 || queued.followUpMpShieldDamage > 0) && firstTo > 0) {
            queuedCounterFollowUpPresentation = CounterFollowUpPresentation(
                attackerId = attacker.id,
                targetId = target.id,
                harm = queued.followUpDamage,
                targetHpBefore = firstTo,
                critical = queued.followUpCritical,
                mpShieldDamage = queued.followUpMpShieldDamage,
                startsAt = reactionEndsAt,
            )
        }
    }

    /** `_attack6` may run its own LianJi pass after the first counter reaction. */
    private fun startQueuedCounterFollowUpPresentation() {
        val queued = queuedCounterFollowUpPresentation ?: return
        if (animationClock() < queued.startsAt) return
        queuedCounterFollowUpPresentation = null
        val attacker = battle.presentationUnit(queued.attackerId) ?: return
        val target = battle.presentationUnit(queued.targetId) ?: return
        val direction = battleDirection(attacker.id, target.id)
        val sourceAction = BattleAttackSequence.selectAttackAction(queued.critical, attacker.attackDelay)
        val animation = sourceActionAnimation(attacker.id, sourceAction, direction, queued.startsAt)
        actionAnimation = animation
        val hitAt = animation.startedAt + requireNotNull(battleSprites.hitTime(sourceAction, direction)) {
            "원본 BRAnime anime$sourceAction 방향 ${direction}에 hit 이벤트가 없습니다"
        }
        val reactionDirection = battleDirection(target.id, attacker.id)
        val reactionDuration = requireSourceActionDuration(32, reactionDirection)
        val reactionEndsAt = hitAt + reactionDuration
        actionAnimation = animation.copy(endsAt = reactionEndsAt)
        scheduleHitReaction(target.id, reactionDirection, hitAt, reactionEndsAt, 32)
        scheduleBattleMutation(hitAt) {
            focusCameraOn(target)
            audio.playBattleEffect(if (queued.critical) 36 else 35)
            battle.pendingActionTransaction?.commitVitals(
                target.id,
                hp = (queued.targetHpBefore - queued.harm).coerceAtLeast(0).takeIf { queued.harm > 0 },
                mp = battle.pendingActionTransaction?.initialMp(target.id)
                    ?.minus(queued.mpShieldDamage)?.coerceAtLeast(0)
                    ?.takeIf { queued.mpShieldDamage > 0 },
            )
            battle.pendingActionTransaction?.commitNextHitSideEffect()
        }
        if (queued.harm > 0) healthTimeline.schedule(
            target.id, queued.targetHpBefore,
            (queued.targetHpBefore - queued.harm).coerceAtLeast(0), hitAt,
        )
        harmNumberAnimations[target.id] = HarmNumberAnimation(queued.harm, queued.mpShieldDamage == 0, hitAt, hitAt + reactionDuration)
    }

    private fun scheduleBattleMutation(at: Float, mutation: () -> Unit) {
        timedBattleMutations += TimedBattleMutation(at, mutation)
        timedBattleMutations.sortBy(TimedBattleMutation::at)
    }

    private fun applyDueBattleMutations() {
        if (activeTurnSettlement != null) return
        val now = animationClock()
        while (timedBattleMutations.firstOrNull()?.at?.let { now >= it } == true) {
            timedBattleMutations.removeAt(0).mutation()
            // A nested `_jiesuan(t, o)` is an awaited generator callback.
            // Do not let another same-frame target/pass mutation cross it.
            if (activeTurnSettlement != null) break
        }
    }

    private fun commitDeferredBattleAction(settlementActorId: String? = null) {
        // `_jiesuan(g_charinfo)` calls centerUnit for every settlement row
        // before it publishes STATES/XD.  Omitting this actor focus leaves the
        // camera at the previous attacker/target and the offset then leaks
        // into every following action episode.  A bare manual move is not a
        // g_charinfo action settlement, so its callers intentionally omit the
        // actor argument.
        settlementActorId?.let(battle::presentationUnit)?.let(::focusCameraOn)
        battle.pendingActionTransaction?.commitAll()
        // A deferred defeated unit joins presentationUnits only on this
        // completion edge, after pruneCombatPresentation already ran.
        battle.pendingPresentationUnits()
            .filter {
                it.hitPoints > 0 && it.id !in hitReactionAnimations &&
                    it.id !in deathAnimations && it.id !in pendingDeathAnimations
            }
            .map { it.id }
            .forEach(battle::clearPresentationUnit)
    }

    private fun queueDeathAnimation(unitId: String, direction: Int, hideType: Int, showRetireMessage: Boolean) {
        val sourceAction = UnitDeathPresentation.hideAction(
            hideType = hideType,
            selfMaster = isScriptMineMaster(unitId),
        )
        pendingDeathAnimations[unitId] = PendingDeathAnimation(
            unitId, direction, sourceAction, requireSourceActionDuration(sourceAction, direction), showRetireMessage,
        )
    }

    /** unitDeath waits for its first run_script callback before serial unitHide. */
    private fun startPendingDeathAnimations() {
        postActionDeathsStarted = true
        drivePendingDeathAnimations()
    }

    private fun beginTurnDeathPresentation(checkpoint: BattleTurnController.DeathCheckpoint): Boolean {
        check(turnDeathCheckpoint == null) { "overlapping lifecycle unitDeath checkpoints" }
        turnDeathCheckpoint = checkpoint
        if (checkpoint == BattleTurnController.DeathCheckpoint.CAMP_START) {
            if (!retainActualDyingUnits()) {
                clearTurnDeathBarrier()
                return true
            }
            turnDeathStage = TurnDeathStage.HIDING
            startPendingDeathAnimations()
        } else {
            // restore() calls unitDeath directly; round-start first runs its
            // explicit script and then calls unitDeath, whose own pre-pass is
            // a second distinct run_script callback in the source generator.
            turnDeathStage = TurnDeathStage.PRE_SCRIPT
            runBattleScript()
        }
        return false
    }

    private fun driveTurnDeathScriptBarrier() {
        val checkpoint = turnDeathCheckpoint ?: return
        if (scriptRuntime.state != PlaybackState.COMPLETE) return
        when (turnDeathStage) {
            TurnDeathStage.PRE_SCRIPT -> {
                if (retainActualDyingUnits()) {
                    turnDeathStage = TurnDeathStage.HIDING
                    startPendingDeathAnimations()
                } else {
                    clearTurnDeathBarrier()
                    completeTurnDeathCheckpoint(checkpoint)
                }
            }
            TurnDeathStage.POST_SCRIPT -> {
                clearTurnDeathBarrier()
                completeTurnDeathCheckpoint(checkpoint)
            }
            else -> Unit
        }
    }

    /**
     * Starts the callback-owned `_jiesuan` presentation.  The model has
     * already committed its logical mutation, but the controller is not
     * released until the source's serial info/MEFF barriers have completed.
     */
    private fun presentTurnSettlement(settlement: CampSettlement): Boolean {
        check(activeTurnSettlement == null) { "overlapping BattleScreen._jiesuan presentations" }
        val unitsById = (battle.units.values + battle.pendingPresentationUnits()).associateBy { it.id }
        val plan = BattleSettlementPlanner.plan(settlement, unitsById) { state ->
            gameDataCatalog.statusMeff(state.sourceStatusIndex, state.meffSlot)
        }
        if (!plan.sourceDataComplete) {
            val missing = plan.pendingIntegrations.joinToString { pending ->
                "${pending.kind}:${pending.unitIds.joinToString("/")}"
            }
            error("Incomplete authored settlement payload: $missing")
        }
        val operations = settlementOperations(plan)
        if (operations.isEmpty()) {
            refreshSettlementUnits(plan)
            return true
        }
        activeTurnSettlement = ActiveTurnSettlement(plan, operations)
        return false
    }

    /** `_magicProcess` calls `_jiesuan(h)` only after its meff callbacks. */
    private fun presentMagicLocalSettlement(settlement: MagicLocalSettlement, casterId: String) {
        if (settlement.entries.isEmpty()) return
        check(activeTurnSettlement == null) { "overlapping BattleScreen._magicProcess settlement" }
        val plan = buildLocalSettlementPlan(settlement, casterId)
        val operations = settlementOperations(plan)
        if (operations.isEmpty()) {
            refreshSettlementUnits(plan)
            return
        }
        startLocalSettlement(plan, operations)
    }

    private fun buildLocalSettlementPlan(
        settlement: MagicLocalSettlement,
        casterId: String,
    ): BattleSettlementPlan {
        val unitsById = (battle.units.values + battle.pendingPresentationUnits()).associateBy { it.id }
        val camp = battle.presentationUnit(casterId)?.effectiveFaction() ?: Faction.PLAYER
        return BattleSettlementPlanner.planMagicLocal(settlement, camp, unitsById) { state ->
            gameDataCatalog.statusMeff(state.sourceStatusIndex, state.meffSlot)
        }
    }

    private fun startLocalSettlement(plan: BattleSettlementPlan, operations: List<TurnSettlementOp>) {
        check(activeTurnSettlement == null) { "overlapping callback-local BattleScreen._jiesuan presentation" }
        if (operations.isEmpty()) {
            refreshSettlementUnits(plan)
            return
        }
        activeTurnSettlement = ActiveTurnSettlement(plan, operations, onComplete = {})
    }

    /** Local attack settlement has no Info rows; retain a defensive complete duration map. */
    private fun localSettlementDuration(operations: List<TurnSettlementOp>): Float = operations.sumOf { operation ->
        when (operation) {
            is TurnSettlementOp.Focus -> operation.seconds.toDouble()
            is TurnSettlementOp.Actions -> operation.actionIds.sumOf { actionId ->
                val unit = battle.presentationUnit(operation.unitId)
                if (unit == null) 0.0 else requireSourceActionDuration(actionId, unit.direction).toDouble()
            }
            is TurnSettlementOp.UnitInfo -> operation.plan.infoBarrierSeconds.toDouble()
            is TurnSettlementOp.GrowthInfo -> {
                val ticks = operation.grants.sumOf { grant ->
                    val delta = grant.unitResult?.gained ?: grant.equipmentResult?.gained ?: 0
                    minOf(kotlin.math.abs(delta), 5)
                }
                (.1f + ticks * .2f + .3f).toDouble()
            }
            is TurnSettlementOp.Meff -> operation.effectId.let(magicEffects::effect)?.duration?.toDouble() ?: 0.0
            is TurnSettlementOp.Info2 -> {
                val autoClose = operation.text.length < 10 || settingsPreferences.getInteger(
                    SettingLayer.GAME_SETTING,
                    SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
                ) and SettingLayer.AUTO_CLOSE != 0
                if (autoClose) operation.text.length * .04 + 1.0 else Double.POSITIVE_INFINITY
            }
            is TurnSettlementOp.ItemUpgrade -> Double.POSITIVE_INFINITY
            is TurnSettlementOp.Sound,
            is TurnSettlementOp.HideState,
            is TurnSettlementOp.Refresh,
            is TurnSettlementOp.Default -> 0.0
        }
    }.toFloat()

    private fun settlementOperations(plan: BattleSettlementPlan): List<TurnSettlementOp> = buildList {
        plan.authoredSubflows.forEach { subflow ->
            when (subflow) {
                is SettlementAuthoredSubflowPlan.LocalAura -> subflow.steps.forEach { step -> when (step) {
                    is SettlementAuraStep.Focus -> add(TurnSettlementOp.Focus(subflow.casterId, step.seconds, forceCenter = true))
                    is SettlementAuraStep.Sound -> add(TurnSettlementOp.Sound(step.soundIndex))
                    is SettlementAuraStep.Info2 -> add(TurnSettlementOp.Info2(
                        gameDataCatalog.skillName(step.skillId).ifBlank { "특기 ${step.skillId}" },
                    ))
                    is SettlementAuraStep.ActionFinished -> add(TurnSettlementOp.Actions(subflow.casterId, listOf(step.actionId)))
                    is SettlementAuraStep.PlayMeff -> gameDataCatalog.namedMeff(step.semanticName)?.let { effectId ->
                        add(TurnSettlementOp.Meff(effectId, step.targetIds))
                    } ?: error("GAME_CFG.meff.${step.semanticName} is missing")
                    SettlementAuraStep.NestedSettlement -> addAll(settlementOperations(subflow.nestedSettlement))
                    SettlementAuraStep.DefaultAction -> add(TurnSettlementOp.Default(subflow.casterId))
                } }
                is SettlementAuthoredSubflowPlan.Growth -> subflow.steps.forEach { step -> when (step) {
                    is SettlementGrowthStep.InfoValues -> add(TurnSettlementOp.GrowthInfo(subflow.unitId, step.grants))
                    is SettlementGrowthStep.AbilityLevelUp -> add(TurnSettlementOp.Info2("${step.attribute.name} 상승"))
                    SettlementGrowthStep.UnitLevelUpActionFinished -> add(TurnSettlementOp.Actions(subflow.unitId, listOf(11)))
                    SettlementGrowthStep.UnitLevelUpInfo -> {
                        val unit = battle.presentationUnit(subflow.unitId)
                        add(TurnSettlementOp.Info2("${unit?.name.orEmpty()} 승격하여${unit?.level ?: 0}레벨"))
                    }
                    is SettlementGrowthStep.LearnedMagicInfo -> add(TurnSettlementOp.Info2(
                        "법술 「${gameDataCatalog.magicProfile(step.magicId)?.name ?: step.magicId}」！",
                    ))
                    is SettlementGrowthStep.EquipmentLevelUpAction -> add(TurnSettlementOp.Actions(
                        subflow.unitId,
                        if (step.result.slot == CampaignEquipmentSlot.WEAPON) listOf(12, 7) else listOf(12, 33),
                    ))
                    is SettlementGrowthStep.EquipmentLevelUpInfo -> add(TurnSettlementOp.Info2(
                        if (step.result.slot == CampaignEquipmentSlot.WEAPON) "무기레벨 상승!" else "보구레벨 상승!",
                    ))
                    is SettlementGrowthStep.ItemUpgradeCallback -> add(TurnSettlementOp.ItemUpgrade(subflow.unitId, step.result))
                    SettlementGrowthStep.DefaultAction -> add(TurnSettlementOp.Default(subflow.unitId))
                } }
            }
        }
        // `_jiesuan` iterates each info row serially: center this unit, hide
        // its state icon (when STATES exists), then await its info panel.
        // Grouping every HideState before every UnitInfo changed callback
        // buckets and exposed later units too early.
        plan.units.forEach { unit ->
            // `_jiesuan` calls centerUnit(St) through `_contains`; unlike
            // local-aura action helpers it does not pass flag 1.
            add(TurnSettlementOp.Focus(unit.unitId, 0f, forceCenter = false))
            if (unit.hasStatesPayload) add(TurnSettlementOp.HideState(listOf(unit.unitId)))
            if (unit.infoDeltas.isNotEmpty()) {
                add(TurnSettlementOp.UnitInfo(unit))
                if (unit.infoDeltas.any { it.kind == SettlementInfoKind.HP }) {
                    add(TurnSettlementOp.Default(unit.unitId))
                }
            }
        }
        plan.meffBuckets.forEach { bucket ->
            bucket.key.actualMeffId?.let { add(TurnSettlementOp.Meff(it, bucket.targets.map { target -> target.unitId })) }
        }
        val refreshIds = plan.units.map { it.unitId }
        if (refreshIds.isNotEmpty()) add(TurnSettlementOp.Refresh(refreshIds))
    }

    private fun driveTurnSettlementPresentation() {
        val active = activeTurnSettlement ?: return
        val now = animationClock()
        if (active.operationIndex >= active.operations.size) {
            finishTurnSettlementPresentation(active)
            return
        }
        val operation = active.operations[active.operationIndex]
        if (active.operationStarted) {
            if (operation is TurnSettlementOp.ItemUpgrade) {
                if (itemUpgradeFlow != null) return
                advanceSettlementOperation(active)
                return
            }
            if (now < active.waitUntil) return
            if (operation is TurnSettlementOp.Actions && active.actionIndex + 1 < operation.actionIds.size) {
                active.actionIndex++
                val unit = battle.presentationUnit(operation.unitId) ?: return advanceSettlementOperation(active)
                actionAnimation = sourceActionAnimation(unit.id, operation.actionIds[active.actionIndex], unit.direction, now)
                active.waitUntil = requireNotNull(actionAnimation).endsAt
                return
            }
            settlementInfoOverlay = null
            settlementInfo2Overlay = null
            if (operation is TurnSettlementOp.Actions) actionAnimation = null
            advanceSettlementOperation(active)
            return
        }

        Gdx.app.log(
            "BattleSettlement",
            "t=$now stage=${active.plan.stage} " +
                "op=${active.operationIndex}/${active.operations.size}:${operation.javaClass.simpleName}",
        )

        when (operation) {
            is TurnSettlementOp.Focus -> {
                battle.presentationUnit(operation.unitId)?.let { focusCameraOn(it, forceCenter = operation.forceCenter) }
                if (operation.seconds <= 0f) return advanceSettlementOperation(active)
                active.waitUntil = now + operation.seconds
            }
            is TurnSettlementOp.Sound -> {
                audio.playBattleEffect(operation.soundIndex)
                return advanceSettlementOperation(active)
            }
            is TurnSettlementOp.Info2 -> {
                val autoClose = operation.text.length < 10 || settingsPreferences.getInteger(
                    SettingLayer.GAME_SETTING,
                    SettingLayer.BG_SOUND or SettingLayer.EFFECT_SOUND or SettingLayer.MINI_MAP,
                ) and SettingLayer.AUTO_CLOSE != 0
                val duration = if (autoClose) operation.text.length * .04f + 1f else Float.POSITIVE_INFINITY
                settlementInfo2Overlay = SettlementInfo2Overlay(operation.text, now, now + duration)
                active.waitUntil = now + duration
            }
            is TurnSettlementOp.Actions -> {
                active.actionIndex = 0
                val unit = battle.presentationUnit(operation.unitId) ?: return advanceSettlementOperation(active)
                actionAnimation = sourceActionAnimation(unit.id, operation.actionIds.first(), unit.direction, now)
                active.waitUntil = requireNotNull(actionAnimation).endsAt
            }
            is TurnSettlementOp.UnitInfo -> {
                val unit = battle.presentationUnit(operation.plan.unitId) ?: return advanceSettlementOperation(active)
                settlementInfoOverlay = SettlementInfoOverlay(
                    unit.id, requireNotNull(operation.plan.infoPanel), now,
                    deltas = operation.plan.infoDeltas, title = unit.name,
                )
                var cursor = now + operation.plan.preInfoDelaySeconds
                operation.plan.infoDeltas.forEach { delta ->
                    cursor += delta.tickSeconds
                    if (delta.kind == SettlementInfoKind.HP) healthTimeline.schedule(unit.id, delta.before, delta.after, cursor)
                }
                healthTimelineHoldUntil[unit.id] = now + operation.plan.infoBarrierSeconds
                active.waitUntil = now + operation.plan.infoBarrierSeconds
            }
            is TurnSettlementOp.GrowthInfo -> {
                val unit = battle.presentationUnit(operation.unitId) ?: return advanceSettlementOperation(active)
                settlementInfoOverlay = SettlementInfoOverlay(
                    unit.id, SettlementInfoPanel.MINE, now, grants = operation.grants, title = unit.name,
                )
                val ticks = operation.grants.sumOf { grant ->
                    val delta = grant.unitResult?.gained ?: grant.equipmentResult?.gained ?: 0
                    minOf(kotlin.math.abs(delta), 5)
                }
                active.waitUntil = now + .1f + ticks * .2f + .3f
            }
            is TurnSettlementOp.Meff -> {
                val effect = magicEffects.effect(operation.effectId) ?: return advanceSettlementOperation(active)
                val targets = operation.targetIds.filter { battle.presentationUnit(it) != null }
                if (targets.isEmpty()) return advanceSettlementOperation(active)
                magicEffectAnimations += MagicEffectAnimation(operation.effectId, targets, now, now + effect.duration)
                active.waitUntil = now + effect.duration
            }
            is TurnSettlementOp.ItemUpgrade -> {
                battle.consumeEquipmentUpgrade()?.let { queued ->
                    check(queued == operation.result) { "settlement item-upgrade queue order mismatch" }
                }
                openSettlementItemUpgrade(operation.result)
                active.waitUntil = Float.POSITIVE_INFINITY
            }
            is TurnSettlementOp.HideState -> {
                operation.unitIds.forEach { battle.presentationUnit(it)?.presentation?.setStateAnimationVisible(false) }
                return advanceSettlementOperation(active)
            }
            is TurnSettlementOp.Refresh -> {
                operation.unitIds.forEach { id -> battle.presentationUnit(id)?.let {
                    it.presentation.refreshStatus(it.statuses, it.attributeLifts)
                    defaultPresentationAction(it)
                } }
                return advanceSettlementOperation(active)
            }
            is TurnSettlementOp.Default -> {
                battle.presentationUnit(operation.unitId)?.let(::defaultPresentationAction)
                return advanceSettlementOperation(active)
            }
        }
        active.operationStarted = true
    }

    private fun advanceSettlementOperation(active: ActiveTurnSettlement) {
        active.operationIndex++
        active.operationStarted = false
        active.actionIndex = 0
        // Generator callbacks immediately execute the following synchronous
        // statement/bucket. Waiting for another LibGDX render frame between
        // Sound/HideState/Refresh/Default (or after an awaited callback)
        // introduced a non-authored delay for every settlement operation.
        driveTurnSettlementPresentation()
    }

    private fun closeSettlementInfo2() {
        val overlay = settlementInfo2Overlay ?: return
        val active = activeTurnSettlement ?: return
        if (active.operations.getOrNull(active.operationIndex) !is TurnSettlementOp.Info2) return
        val now = animationClock()
        val fullyRevealed = now - overlay.startedAt >= overlay.text.length * .04f
        if (!fullyRevealed) {
            overlay.startedAt = now - overlay.text.length * .04f
            val autoClose = overlay.endsAt.isFinite()
            if (autoClose) {
                overlay.endsAt = now + 1f
                active.waitUntil = overlay.endsAt
            }
        } else {
            settlementInfo2Overlay = null
            active.waitUntil = now
            driveTurnSettlementPresentation()
        }
    }

    private fun refreshSettlementUnits(plan: BattleSettlementPlan) {
        plan.units.forEach { unitPlan ->
            battle.presentationUnit(unitPlan.unitId)?.let { unit ->
                unit.presentation.refreshStatus(unit.statuses, unit.attributeLifts)
                defaultPresentationAction(unit)
            }
        }
    }

    private fun finishTurnSettlementPresentation(active: ActiveTurnSettlement) {
        val plan = active.plan
        refreshSettlementUnits(plan)
        activeTurnSettlement = null
        active.onComplete?.let {
            it()
            return
        }
        when (plan.stage) {
            CampSettlementStage.START_STATE -> turnController.completeCampStatePresentation()
            CampSettlementStage.END_RESTORE -> turnController.completeCampRestorePresentation()
        }
    }

    private fun completeTurnDeathCheckpointIfReady() {
        val checkpoint = turnDeathCheckpoint ?: return
        if (pendingDeathAnimations.isNotEmpty() || activeUnitDeath != null || unitDeathAwaitingDialogue != null) return
        if (turnDeathStage == TurnDeathStage.HIDING) {
            turnDeathStage = TurnDeathStage.POST_SCRIPT
            postActionDeathsStarted = false
            runBattleScript()
            return
        }
        clearTurnDeathBarrier()
        completeTurnDeathCheckpoint(checkpoint)
    }

    private fun clearTurnDeathBarrier() {
        turnDeathCheckpoint = null
        turnDeathStage = TurnDeathStage.NONE
        postActionDeathsStarted = false
    }

    private fun completeTurnDeathCheckpoint(checkpoint: BattleTurnController.DeathCheckpoint) {
        when (checkpoint) {
            BattleTurnController.DeathCheckpoint.CAMP_START -> turnController.completeCampDeathPresentation()
            BattleTurnController.DeathCheckpoint.CAMP_RESTORE -> turnController.completeCampRestoreDeathPresentation()
            BattleTurnController.DeathCheckpoint.ROUND_START -> turnController.completeRoundDeathPresentation()
        }
    }

    /** Source unitHide awaits say4 and then the action callback for each unit in order. */
    private fun drivePendingDeathAnimations() {
        activeUnitDeath?.let { active ->
            if (animationClock() < active.endsAt) return
            deathAnimations.remove(active.pending.unitId)
            battle.presentationUnit(active.pending.unitId)?.let { unit ->
                battle.incrementUnitRetreat(unit)
                unit.setHpcur(active.originalHp)
                unit.visible = false
                unit.characterId?.let { scriptRuntime.stage.setBattleUnitVisibility(it, false) }
            }
            battle.completeScriptedUnitHide(active.pending.unitId)
            activeUnitDeath = null
        }
        unitDeathAwaitingDialogue?.let { pending ->
            if (scriptRuntime.currentDialogue != null) return
            unitDeathAwaitingDialogue = null
            startUnitDeathAnimation(pending)
            return
        }
        if (activeUnitDeath != null) return
        val entry = pendingDeathAnimations.entries.firstOrNull()
        if (entry == null) {
            completeTurnDeathCheckpointIfReady()
            return
        }
        pendingDeathAnimations.remove(entry.key)
        val pending = entry.value
        val unit = battle.presentationUnit(pending.unitId) ?: return
        focusCameraOn(unit)
        val message = unit.retireMessage.takeIf { unit.deathMessageEnabled }
        if (pending.showRetireMessage && message != null) {
            unitDeathAwaitingDialogue = pending
            scriptRuntime.presentExternalBattleDialogue(Dialogue(unit.characterId?.toString(), message))
        } else {
            startUnitDeathAnimation(pending)
        }
    }

    private fun startUnitDeathAnimation(pending: PendingDeathAnimation) {
        val unit = battle.presentationUnit(pending.unitId) ?: return
        val originalHp = unit.hitPoints
        unit.retreatFlag = true
        unit.otherNodesVisible = false
        unit.setHpcur(0)
        val startsAt = animationClock()
        val endsAt = startsAt + pending.duration
        deathAnimations[pending.unitId] = UnitActionAnimation(
            pending.unitId, UnitAnimationKind.DEATH, pending.direction,
            startsAt, endsAt, sourceAction = pending.sourceAction,
        )
        activeUnitDeath = ActiveUnitDeath(pending, endsAt, originalHp)
    }

    /** Source queries getDyingUnits only after the first script callback. */
    private fun retainActualDyingUnits(): Boolean {
        syncScriptedUnits()
        val dying = UnitDeathPresentation.sortedDying(battle.units.values + battle.pendingPresentationUnits())
        pendingDeathAnimations.clear()
        var hideType = 1
        dying.forEach { unit ->
            // unitHide mutates its shared type argument when self master is
            // encountered, so later units in the same sorted batch also die.
            val showRetireMessage = hideType == 1
            if (showRetireMessage && isScriptMineMaster(unit.id)) hideType = 2
            queueDeathAnimation(unit.id, unit.direction, hideType, showRetireMessage)
        }
        return pendingDeathAnimations.isNotEmpty()
    }

    private fun finishManualUnitDeathCallbacks() {
        pendingBattleScriptPassesAfterAction = 0
        pendingBattleCompletedScriptPasses = 0
        pendingBattleActionCommitted = false
        pendingBattleSettlementActorId = null
        postActionDeathsStarted = false
        activeUnitDeath = null
        unitDeathAwaitingDialogue = null
    }

    private fun driveScriptedUnitHide() {
        val active = activeScriptedHide
        if (active != null) {
            if (animationClock() < active.endsAt) return
            deathAnimations.remove(active.battleUnitId)
            scriptRuntime.stage.completeUnitHide(active.request)
            battle.presentationUnit(active.battleUnitId)?.let { unit ->
                if (active.request.hideType != 0) battle.incrementUnitRetreat(unit)
                unit.setHpcur(active.originalHp)
                unit.visible = false
            }
            battle.completeScriptedUnitHide(active.battleUnitId)
            activeScriptedHide = null
            if (active.request.resumesScript) scriptRuntime.resumeExternalDelay()
            else driveScriptedUnitHide()
            return
        }
        scriptedHideAwaitingDialogue?.let { pending ->
            if (scriptRuntime.currentDialogue != null) return
            scriptedHideAwaitingDialogue = null
            battle.presentationUnit(pending.battleUnitId)?.let { unit ->
                startScriptedUnitHide(pending.request, unit)
            } ?: run {
                completeScriptedUnitHideWithoutAnimation(pending.request)
                if (!pending.request.resumesScript) driveScriptedUnitHide()
            }
            return
        }
        val request = scriptRuntime.stage.consumeUnitHideRequest() ?: return
        val unit = (battle.units.values + battle.pendingPresentationUnits())
            .firstOrNull { candidate ->
                request.battleUnitId?.let { candidate.id == it }
                    ?: (candidate.id == scriptRuntime.stage.battleUnitForCharacterId(request.unitId)?.battleId)
            }
        if (unit == null) {
            completeScriptedUnitHideWithoutAnimation(request)
            if (!request.resumesScript) driveScriptedUnitHide()
            return
        }
        if (!unit.visible) {
            completeScriptedUnitHideWithoutAnimation(request)
            if (!request.resumesScript) driveScriptedUnitHide()
            return
        }
        focusCameraOn(unit)
        val selfMaster = isScriptMineMaster(unit.id)
        val effectiveHideType = if (request.hideType == 1 && selfMaster) 2 else request.hideType
        val effectiveRequest = request.copy(hideType = effectiveHideType)
        val message = unit.retireMessage.takeIf { unit.deathMessageEnabled }
        if (request.showsRetireMessage && message != null) {
            scriptedHideAwaitingDialogue = PendingScriptedHide(effectiveRequest, unit.id)
            scriptRuntime.presentExternalBattleDialogue(Dialogue(unit.characterId?.toString(), message))
            return
        }
        startScriptedUnitHide(effectiveRequest, unit)
    }

    private fun completeScriptedUnitHideWithoutAnimation(request: ScenarioUnitHideRequest) {
        scriptRuntime.stage.completeUnitHide(request)
        if (request.resumesScript) scriptRuntime.resumeExternalDelay()
    }

    private fun startScriptedUnitHide(request: ScenarioUnitHideRequest, unit: BattleUnit) {
        focusCameraOn(unit)
        val selfMaster = isScriptMineMaster(unit.id)
        val effectiveHideType = request.hideType
        val sourceAction = UnitDeathPresentation.hideAction(effectiveHideType, selfMaster)
        val originalHp = unit.hitPoints
        unit.retreatFlag = true
        unit.otherNodesVisible = false
        unit.setHpcur(0)
        val startedAt = animationClock()
        val endsAt = startedAt + requireSourceActionDuration(sourceAction, unit.direction)
        deathAnimations[unit.id] = UnitActionAnimation(
            unit.id, UnitAnimationKind.DEATH, unit.direction, startedAt, endsAt, sourceAction,
        )
        activeScriptedHide = ActiveScriptedHide(request, unit.id, endsAt, originalHp)
    }

    /** BattleUnit.show restores model state before awaiting its native show callback. */
    private fun driveScriptedUnitShow() {
        activeScriptedShow?.let { active ->
            if (animationClock() < active.endsAt) return
            scriptedUnitVisuals.remove(active.battleUnitId)
            battle.presentationUnit(active.battleUnitId)?.let { unit ->
                unit.otherNodesVisible = true
                active.request.direction.takeIf { it in 0..3 }?.let { unit.direction = it }
                defaultPresentationAction(unit)
            }
            activeScriptedShow = null
            scriptRuntime.resumeExternalDelay()
            return
        }
        val request = scriptRuntime.stage.consumeUnitShowRequest() ?: return
        val existing = (battle.units.values + battle.pendingPresentationUnits())
            .firstOrNull { it.id == scriptRuntime.stage.battleUnitForCharacterId(request.unitId)?.battleId }
        if (existing == null) {
            scriptRuntime.stage.setBattleUnitVisibility(request.unitId, true)
            scriptRuntime.resumeExternalDelay()
            return
        }
        val unit = battle.restorePresentationUnit(existing.id) ?: existing
        val requestedX = request.x.takeIf { it >= 0 } ?: unit.tileX
        val requestedY = request.y.takeIf { it >= 0 } ?: unit.tileY
        val target = if (battle.unitAt(requestedX, requestedY)?.let { it !== unit } == true) {
            listOf(
                requestedX to requestedY - 1,
                requestedX + 1 to requestedY,
                requestedX - 1 to requestedY,
                requestedX to requestedY + 1,
            ).firstOrNull { (x, y) ->
                x in 0..boardMaxX && y in 0..boardMaxY && battle.unitAt(x, y)?.let { it !== unit } != true
            }
                ?: (unit.tileX to unit.tileY)
        } else requestedX to requestedY
        unit.tileX = target.first
        unit.tileY = target.second
        // BattleUnit.show calls setPos before centerUnit; its node is now at
        // the authored _countPos tile even when the original create record
        // omitted one or both coordinates.
        unit.hasAuthoredTileX = true
        unit.hasAuthoredTileY = true
        request.direction.takeIf { it in 0..3 }?.let { unit.direction = it }
        scriptRuntime.stage.unit(request.unitId).apply {
            x = unit.tileX
            y = unit.tileY
        }
        scriptRuntime.stage.setBattleUnitVisibility(request.unitId, true)
        focusCameraOn(unit)
        val revive = request.flags and 1 != 0
        unit.otherNodesVisible = !revive
        val startedAt = animationClock()
        val duration = if (revive) requireSourceActionDuration(46, unit.direction) else .2f
        if (revive) scriptedUnitVisuals[unit.id] = ScriptedUnitVisual(46, startedAt)
        activeScriptedShow = ActiveScriptedShow(request, unit.id, startedAt + duration)
    }

    /**
     * BattleUnit.setPosts(…, flags) is not a generic delay: only the source
     * `flags & 16 && testAvatar()` route pauses, loads its replacement image,
     * and resumes from loadAvatar's completion callback.  A LibGDX texture is
     * decoded synchronously when first drawn, but this one-render FIFO edge
     * preserves the authored async callback boundary.
     */
    private fun driveScriptedUnitPosts() {
        activeScriptedUnitPosts?.let { active ->
            loadedBattleAvatarIds[active.battleUnitId] = active.request.newAvatarId
            activeScriptedUnitPosts = null
            if (active.request.pausesScript) scriptRuntime.resumeExternalDelay()
            return
        }
        val request = scriptRuntime.stage.consumeUnitPostsRequest() ?: return
        val unit = scriptBattleUnit(request.unitId)
        if (unit == null) {
            if (request.pausesScript) scriptRuntime.resumeExternalDelay()
            return
        }
        // The old group is still what Cocos displays while loadUnitPicture is
        // pending; only its completion publishes the new avatar and resumes.
        loadedBattleAvatarIds[unit.id] = request.oldAvatarId
        activeScriptedUnitPosts = ActiveScriptedUnitPosts(request, unit.id)
    }

    /** StageLayer.setObject2/playMagicMeff use exact tile centering, not _contains. */
    private fun driveScriptedCameraCenters() {
        val requests = scriptRuntime.stage.consumeCameraCenterRequests()
        if (requests.isEmpty()) return
        configureSourceCameraViewport()
        requests.forEach { request ->
            battleCamera.centerTile(request.x, request.y, terrainGrid.width, terrainGrid.height)
            recordFullBattleTraceFrame(
                0f,
                "transition:camera:center:${request.x}:${request.y}",
                advanceFrame = false,
            )
        }
    }

    /** StageLayer.setObject2/playMagicMeff use exact tile centering, not _contains. */
    private fun driveMapPresentation() {
        activeMapPresentation?.let { active ->
            if (animationClock() < active.endsAt) return
            activeMapPresentation = null
            scriptRuntime.resumeExternalDelay()
            return
        }
        val request = scriptRuntime.stage.consumeMapPresentationRequest() ?: return
        focusCameraOnTile(request.x.toFloat(), request.y.toFloat(), forceCenter = true)
        activeMapPresentation = ActiveMapPresentation(request, animationClock() + request.duration)
    }

    /**
     * FIFO owner for source Stage/BattleUnit calls that pause their Python
     * Script until a visible native callback.  Never start two requests in
     * one render: resumeExternalDelay may synchronously emit the next one.
     */
    private fun driveScriptPresentation() {
        val now = animationClock()
        activeScriptPresentation?.let { active ->
            when (active.phase) {
                ScriptPresentationPhase.ITEM_MODAL -> {
                    if (scriptRuntime.state == PlaybackState.MODAL) return
                    activeScriptPresentation = null
                }
                ScriptPresentationPhase.ITEM_ACTION -> {
                    if (now < active.endsAt) return
                    active.battleUnitId?.let {
                        scriptedUnitVisuals.remove(it)
                        battle.presentationUnit(it)?.let(::defaultPresentationAction)
                    }
                    audio.playBattleEffect(14) // SOUND_INDEX.GET_ITEM
                    active.phase = ScriptPresentationPhase.ITEM_ICON
                    active.startedAt = now
                    active.endsAt = now + .8f
                    return
                }
                ScriptPresentationPhase.ITEM_ICON -> {
                    if (now < active.endsAt) return
                    val request = active.request as ScenarioScriptPresentationRequest.GetItem
                    active.phase = ScriptPresentationPhase.ITEM_MODAL
                    scriptRuntime.presentExternalBattleInfo(request.completionMessage)
                    return
                }
                ScriptPresentationPhase.TIMED -> {
                    if (now < active.endsAt) return
                    if (active.request is ScenarioScriptPresentationRequest.UnitHighlight) unitInfoLayer = null
                    activeScriptPresentation = null
                    scriptRuntime.resumeExternalDelay()
                    return
                }
            }
        }
        val request = scriptRuntime.stage.consumeScriptPresentationRequest() ?: return
        when (request) {
            is ScenarioScriptPresentationRequest.RectangleHighlight -> {
                focusCameraOnTile((request.x1 + request.x2) / 2f, (request.y1 + request.y2) / 2f, forceCenter = true)
                activeScriptPresentation = ActiveScriptPresentation(
                    request, ScriptPresentationPhase.TIMED, now, now + request.durationSeconds,
                )
            }
            is ScenarioScriptPresentationRequest.UnitHighlight -> {
                val unit = scriptBattleUnit(request.unitId)
                if (unit == null) {
                    scriptRuntime.resumeExternalDelay()
                    return
                }
                focusCameraOn(unit)
                if (request.opensUnitInfo) openUnitInfoLayer(request.unitId)
                activeScriptPresentation = ActiveScriptPresentation(
                    request, ScriptPresentationPhase.TIMED, now, now + request.durationSeconds, unit.id,
                )
            }
            is ScenarioScriptPresentationRequest.MapObjects -> {
                request.objects.lastOrNull()?.let { focusCameraOnTile(it.x.toFloat(), it.y.toFloat(), forceCenter = true) }
                activeScriptPresentation = ActiveScriptPresentation(
                    request, ScriptPresentationPhase.TIMED, now, now + request.durationSeconds,
                )
            }
            is ScenarioScriptPresentationRequest.GetItem -> {
                val unit = scriptItemUnit(request.unitSelector)
                if (unit == null) {
                    scriptRuntime.resumeExternalDelay()
                    return
                }
                focusCameraOn(unit)
                scriptedUnitVisuals[unit.id] = ScriptedUnitVisual(request.action, now)
                val duration = requireSourceActionDuration(request.action, unit.direction)
                activeScriptPresentation = ActiveScriptPresentation(
                    request, ScriptPresentationPhase.ITEM_ACTION, now, now + duration, unit.id,
                )
            }
            is ScenarioScriptPresentationRequest.UnitStatusSettlement -> {
                val characterId = request.values.asSequence()
                    .mapNotNull { (it["unit"] as? ScenarioInterpreter.UnitReference)?.id }
                    .firstOrNull()
                val unit = characterId?.let(::scriptBattleUnit)
                unit?.let(::focusCameraOn)
                val duration = request.values.maxOfOrNull { change ->
                    val hp = kotlin.math.abs((change["hp"] as? Number)?.toInt() ?: 0)
                    val mp = kotlin.math.abs((change["mp"] as? Number)?.toInt() ?: 0)
                    minOf(maxOf(hp, mp), 5) * .2f +
                        if (change.containsKey("status") || change.containsKey("hStatus")) .6f else 0f
                }?.coerceAtLeast(request.minimumDurationSeconds) ?: request.minimumDurationSeconds
                activeScriptPresentation = ActiveScriptPresentation(
                    request, ScriptPresentationPhase.TIMED, now, now + duration, unit?.id,
                )
            }
        }
    }

    private fun liveScriptBattleUnit(characterId: Int, visibleOnly: Boolean = false): BattleUnit? {
        val units = battle.units.values + battle.pendingPresentationUnits()
        val exactId = scriptRuntime.stage.battleUnitForCharacterId(characterId)?.battleId
        return units.firstOrNull { it.id == exactId && (!visibleOnly || it.visible) }
            ?: units.firstOrNull { it.characterId == characterId && (!visibleOnly || it.visible) }
    }

    private fun scriptBattleUnit(characterId: Int): BattleUnit? =
        liveScriptBattleUnit(characterId, visibleOnly = true)

    /** BattleScreen.selfMasterId(1): `_unitIds[characterId]` selects first push. */
    private fun isScriptMineMaster(unitId: String): Boolean =
        scriptRuntime.stage.battleUnitForCharacterId(scriptRuntime.stage.mineMasterInstanceId)?.battleId == unitId

    /** Exact BattleScreen._filterUnit selectors used by getItem. */
    private fun scriptItemUnit(selector: Int): BattleUnit? {
        val units = (battle.units.values + battle.pendingPresentationUnits()).filter { it.visible }
        return when (selector) {
            1024 -> units.firstOrNull()
            1025 -> units.firstOrNull { it.isPlayerSide() }
            1026 -> units.firstOrNull { it.type().isEnemySide() }
            1027 -> units.firstOrNull { isScriptMineMaster(it.id) }
                ?: units.firstOrNull { it.isPlayerSide() }
            else -> liveScriptBattleUnit(selector, visibleOnly = true)
        }
    }

    private fun driveScriptedUnitActionCallback() {
        val active = activeScriptedUnitAction ?: return
        if (animationClock() < active.endsAt) return
        if (actionAnimation?.unitId == active.battleUnitId) actionAnimation = null
        scriptedUnitVisuals.remove(active.battleUnitId)
        battle.presentationUnit(active.battleUnitId)?.let(::defaultPresentationAction)
        activeScriptedUnitAction = null
        scriptRuntime.resumeExternalDelay()
    }

    private fun pruneCombatPresentation() {
        val now = animationClock()
        val completedHitIds = hitReactionAnimations.entries
            .filter { now >= it.value.endsAt }
            .map { it.key }
        hitReactionAnimations.entries
            .filter {
                now >= it.value.endsAt && !isPresentationNeededByQueuedExchange(it.key) &&
                    battle.presentationUnit(it.key)?.hitPoints?.let { hp -> hp > 0 } != false
            }
            .map { it.key }
            .forEach(battle::clearPresentationUnit)
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
            .forEach(battle::clearPresentationUnit)
        deathAnimations.entries.removeIf { now >= it.value.endsAt }
        harmNumberAnimations.entries.removeIf { now >= it.value.endsAt }
        if (activeCounterMagicPresentation?.let { now >= it.endsAt } == true) {
            activeCounterMagicPresentation = null
        }
    }

    /** A unit already removed from Battle must survive until its queued pass renders. */
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
        when (turnController.phase) {
            BattleTurnController.Phase.CAMP_SCRIPT -> turnController.completeCampScript()
            BattleTurnController.Phase.ROUND_SCRIPT -> turnController.completeRoundScript()
            else -> Unit
        }
    }

    /**
     * New battles run their authored opening while source curCamp is UNKNOWN.
     * `_execControlScript(true)` owns the initial startOper hand-off; every
     * finite authored callback must drain before Mine input is exposed.
     */
    private fun driveBattleBootstrap() {
        if (scriptRuntime.state != PlaybackState.COMPLETE || bootstrapPresentationBusyReasons().isNotEmpty()) return
        when (bootstrapPhase) {
            BattleBootstrapPhase.SCENE0 -> {
                bootstrapPhase = BattleBootstrapPhase.INITIAL_SCENE1
                initialPlayerCampScriptStarted = true
                // ctrl_mine's first Mine focus is already established when
                // the initial scene1 action starts.  The action's own
                // BattleUnit.setAction -> centerUnit call therefore sees the
                // same ScrollView state and `_contains` emits nothing unless
                // a real edge correction is needed.
                focusFirstCampCameraUnit(Faction.PLAYER)
                runBattleScript(contextCampOverride = -1)
            }
            BattleBootstrapPhase.INITIAL_SCENE1 -> {
                // Initial startOper belongs to _execControlScript(true)'s
                // callback. S_00's round-one scene1 does not call it itself;
                // its authored startOper is a separate round-two event.
                completeInitialBattleOperation(scriptRuntime.stage)
                bootstrapPhase = BattleBootstrapPhase.COMPLETE
                turnController.completeBootstrap()
            }
            BattleBootstrapPhase.COMPLETE -> Unit
        }
    }

    /**
     * `_execControlScript(true)` waits for callbacks owned by authored stage
     * commands, not every visual retained by the battle renderer. In
     * particular setAction(4/9) is a persistent pose and tactical queues
     * cannot legally precede startOper, so neither may deadlock bootstrap.
     */
    private fun bootstrapPresentationBusyReasons(): List<String> {
        val now = animationClock()
        return BattleBootstrapCallbackState(
            move = scriptRuntime.stage.units.values.any { it.moveDuration > 0f },
            attackAction = now < scriptedAttackCallbackEndsAt,
            hide = activeScriptedHide != null || scriptedHideAwaitingDialogue != null,
            show = activeScriptedShow != null,
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

    private fun beginVisibleAiTurn(camp: Faction): AiTurnResult {
        activeAiCamp = camp
        pendingAiResolution = null
        pendingAiActionStarted = false
        pendingAiPlayerMoveScriptStarted = false
        pendingAiUnitDeathScriptPass = 0
        pendingAiActionCommitted = false
        postActionDeathsStarted = false
        aiPresentationStage = AiPresentationStage.COMPLETE
        aiTurnMoves = 0
        aiTurnAttacks = 0
        aiTurnHolds = 0
        // `ctrl_mine` case 9 performs nextNotOperUnit -> _firstUnit ->
        // centerUnit *before* case 13 chooses manual control or `_ai2`.
        // Consequently collocated Mine, Enemy and Reinforcements all retain
        // this camp-entry focus as well. `_firstUnit(FRIEND)` still preserves
        // the source's sparse [20,40) probe and normally finds no friend.
        if (battle.hasPendingAiUnits()) focusFirstCampCameraUnit(camp)
        return resolveNextVisibleAiUnit().also {
            emptyAiCampFrameBarrier.begin(hasActor = pendingAiResolution != null)
        }
    }

    /** Resolve only one actor; all later actors wait for its visible callbacks. */
    private fun resolveNextVisibleAiUnit(): AiTurnResult {
        val camp = activeAiCamp ?: return AiTurnResult(0, 0, 0)
        val result = battle.resolveAiTurn(maxUnits = 1, deferMutations = true)
        aiTurnMoves += result.moves
        aiTurnAttacks += result.attacks
        aiTurnHolds += result.holds
        pendingAiResolution = battle.lastAiUnitResolution
        pendingAiActionStarted = false
        pendingAiPlayerMoveScriptStarted = false
        pendingAiUnitDeathScriptPass = 0
        pendingAiActionCommitted = false
        postActionDeathsStarted = false
        committedPlayerMoveFrameBarrier.beginActor()
        actionStatusFrameBarrier.beginActor()
        val resolution = pendingAiResolution
        counterattackSettlementFrameBarrier.beginActor(
            hasPhysicalCounter = resolution?.result.hasPhysicalCounterPass(),
        )
        if (resolution != null) {
            movementAnimation = null
            // `_ai2` schedules .3s only when Control selected a visible move
            // or command.  A confused/paralysed/no-result actor jumps
            // directly to `_shifudu -> _jiesuan`, so its XD callback may be
            // observed in the same frame as the preceding actor's move2
            // completion.  Delaying every resolution here split that source
            // callback chain across unrelated LibGDX renders.
            aiPresentationStage = if (resolution.path.size < 2 && resolution.result == null) {
                AiPresentationStage.COMPLETE
            } else {
                AiPresentationStage.FOCUS_DELAY
            }
            aiPresentationStageStartedAt = animationClock()
            eventMessage = "${camp.label()}: ${battle.presentationUnit(resolution.actorId)?.name ?: resolution.actorId} 행동"
        }
        return result
    }

    private fun combatPresentationBusy(): Boolean {
        val now = animationClock()
        return (scriptRuntime.state == PlaybackState.MODAL &&
            scriptRuntime.currentModalKind == ScenarioInterpreter.ModalKind.INFO) ||
            movementAnimation?.let { now < it.endsAt } == true ||
            actionAnimation?.let { now < it.endsAt } == true ||
            hitReactionAnimations.values.any { now < it.endsAt } ||
            deathAnimations.values.any { now < it.endsAt } ||
            pendingDeathAnimations.isNotEmpty() || activeUnitDeath != null || unitDeathAwaitingDialogue != null ||
            activeScriptedHide != null || scriptedHideAwaitingDialogue != null || activeScriptedShow != null ||
            activeScriptPresentation != null || activeScriptedUnitAction != null ||
            magicEffectAnimations.any { now < it.endsAt } ||
            queuedMagicPresentation != null ||
            activeCounterMagicPresentation?.let { now < it.endsAt } == true ||
            queuedPhysicalPresentation != null ||
            queuedFollowUpPresentation != null ||
            queuedCounterPresentation != null ||
            queuedCounterFollowUpPresentation != null ||
            pendingCriticalSpeechAction != null ||
            activeTurnSettlement != null
    }

    /**
     * Source `_stateProcess` and `_ai2` always finish the post-action script,
     * unitDeath animation/callback, and second script pass before `isEnd`.
     * These controller states are intentionally separate from
     * [combatPresentationBusy]: including them there would deadlock the AI
     * state machine that is responsible for consuming them.
     */
    private fun outcomeCallbacksPending(): Boolean =
        pendingBattleScriptPassesAfterAction > 0 ||
            pendingAiUnitDeathScriptPass > 0 ||
            postActionDeathsStarted ||
            pendingAiResolution != null ||
            activeAiCamp != null ||
            // The round card owns a scheduled continuation callback. Starting
            // another result script before it closes can put a SayLayer behind
            // the card, leaving the dialogue unable to receive its input.
            activeRoundLayer != null ||
            activeTurnSettlement != null

    /** Drive the same move -> action -> next `_ai2` callback chain as Cocos. */
    private fun driveVisibleAiTurn() {
        if (scriptRuntime.state != PlaybackState.COMPLETE) return
        val camp = activeAiCamp ?: return
        // `_ai2` completion is a callback after curCamp has already changed.
        // If there is no actor, preserve that entry for one rendered frame
        // instead of consuming camp state, restore and addRound immediately.
        if (pendingAiResolution == null && emptyAiCampFrameBarrier.yieldEntryFrame()) return
        // A generator `yield` exists only for an actual scheduled callback.
        // Keep consuming synchronous continuations in this render: move2 can
        // finish, both empty run_script calls can return, and the following
        // no-result actor can publish XD before the next draw.
        consecutiveNoResultFrameGate.beginRender()
        while (true) {
            val resolution = pendingAiResolution
            if (resolution == null) {
                if (battle.hasPendingAiUnits()) {
                    resolveNextVisibleAiUnit()
                    // A no-result `_ai2` still publishes its XD settlement
                    // through a distinct engine update.  The callback tail
                    // may immediately start the following *visible* move or
                    // command, but it does not collapse two consecutive
                    // confused/paralysed/hold settlements into one draw.
                    // Doing so attached every skipped actor's XD transition
                    // to the next move episode in accelerated traces.
                    val next = pendingAiResolution
                    if (consecutiveNoResultFrameGate.shouldYieldBefore(
                            nextIsNoResult = next != null && next.path.size < 2 && next.result == null,
                        )) return
                    continue
                }
                val total = AiTurnResult(aiTurnMoves, aiTurnAttacks, aiTurnHolds)
                eventMessage = "${camp.label()}: 이동 ${total.moves} · 공격 ${total.attacks} · 대기 ${total.holds}"
                // `_ai2` keeps its unitDeath script-pass counter inside the
                // actor coroutine.  Once the camp has no actor left, that
                // local continuation is gone as well.  Retaining our mirror
                // at pass 2 makes outcomeCallbacksPending() permanently busy
                // after a later restore/death result script (S_00 victory),
                // so the source-equivalent save prompt can never start.
                pendingAiUnitDeathScriptPass = 0
                activeAiCamp = null
                turnController.completeAiPresentation(total)
                return
            }
            when (aiPresentationStage) {
                AiPresentationStage.FOCUS_DELAY -> {
                    if (animationClock() - aiPresentationStageStartedAt < .3f) return
                    // The helper records the original center(x,y) call using
                    // the same observation vocabulary as the source trace.
                    focusCameraOnTile(resolution.fromX.toFloat(), resolution.fromY.toFloat())
                    movementAnimation = resolution.path.takeIf { it.size >= 2 }?.let { path ->
                        val actor = battle.presentationUnit(resolution.actorId)
                        UnitMoveAnimation(
                            resolution.actorId,
                            path,
                            BattleUnitMoveTimeline.schedule(path, actor?.fastMove ?: true),
                            animationClock(),
                        )
                    }
                    if (movementAnimation == null) battle.pendingActionTransaction?.commitMovement(
                        commitActionState = resolution.result == null,
                    )
                    aiPresentationStage = if (movementAnimation != null) AiPresentationStage.MOVING
                    else if (resolution.result is TacticalActionResult.Attack) AiPresentationStage.ACTION_DELAY
                    else if (resolution.result != null) AiPresentationStage.ACTION
                    else AiPresentationStage.COMPLETE
                    aiPresentationStageStartedAt = animationClock()
                    continue
                }
                AiPresentationStage.MOVING -> {
                    if (movementAnimation?.let { animationClock() < it.endsAt } == true) return
                    val finalDirection = movementAnimation?.timeline?.segments?.lastOrNull()?.direction
                    // move2 owns the actor direction only until its final
                    // callback.  Keeping this completed object alive made
                    // driveMovementTicks reapply the route's last direction
                    // on every later frame, overwriting scene1 setDir calls
                    // (S_22 unit 115 is the first production example).
                    movementAnimation = null
                    battle.pendingActionTransaction?.commitMovement(commitActionState = resolution.result == null)
                    finalDirection?.let { direction -> battle.presentationUnit(resolution.actorId)?.direction = direction }
                    if (camp == Faction.PLAYER && resolution.path.size >= 2 &&
                        (resolution.fromX != resolution.toX || resolution.fromY != resolution.toY)
                    ) {
                        campaignE2ePlayerMoveCommitted = true
                        val actor = battle.presentationUnit(resolution.actorId)?.characterId ?: -1
                        campaignE2eCommittedPlayerMove = "$actor:${resolution.fromX},${resolution.fromY}->${resolution.toX},${resolution.toY}"
                    }
                    val needsMoveCallbackScript = camp == Faction.PLAYER
                    if (needsMoveCallbackScript && !pendingAiPlayerMoveScriptStarted) {
                        pendingAiPlayerMoveScriptStarted = true
                        runBattleScript()
                        if (scriptRuntime.state != PlaybackState.COMPLETE) return
                    }
                    // ctrl_mine's collocated PLAYER `_ai2` resumes from the
                    // move callback directly into its action/post-script
                    // tail.  The source checks isEnd at that boundary.  Stop
                    // only for end()+reward/lose(): stage.end() alone does
                    // not publish a tactical result and remains a normal
                    // continuation.  Keeping this in the PLAYER move path
                    // deliberately leaves the ordinary enemy/S22 AI path
                    // untouched.
                    if (CollocatedPlayerMoveScriptEnd.finishesAiTurn(
                            camp = camp,
                            moveCallbackStarted = pendingAiPlayerMoveScriptStarted,
                            scriptState = scriptRuntime.state,
                            battleEndedByScript = scriptRuntime.stage.battleEndedByScript,
                            scriptedOutcome = scriptRuntime.stage.scriptedBattleOutcome,
                            observedOutcome = battle.outcome(),
                        )
                    ) {
                        finishScriptEndedAiTurn()
                        return
                    }
                    aiPresentationStage = if (resolution.result is TacticalActionResult.Attack) AiPresentationStage.ACTION_DELAY
                    else if (resolution.result != null) AiPresentationStage.ACTION
                    else AiPresentationStage.COMPLETE
                    aiPresentationStageStartedAt = animationClock()
                    continue
                }
                AiPresentationStage.ACTION_DELAY -> {
                    if (camp == Faction.PLAYER && !pendingAiPlayerMoveScriptStarted) {
                        pendingAiPlayerMoveScriptStarted = true
                    }
                    // Physical `_ai2` keeps the attack-range nodes visible for
                    // another .3s before calling `_attack_3`.
                    if (animationClock() - aiPresentationStageStartedAt < .3f) return
                    aiPresentationStage = AiPresentationStage.ACTION
                    continue
                }
                AiPresentationStage.ACTION -> {
                    if (!pendingAiActionStarted) {
                        pendingAiActionStarted = true
                        applyAction(
                            result = requireNotNull(resolution.result),
                            unitName = battle.presentationUnit(resolution.actorId)?.name ?: resolution.actorId,
                            actorId = resolution.actorId,
                            magicId = resolution.magicId,
                            targetId = resolution.targetId,
                            healthBeforeAction = resolution.healthBeforeAction,
                            continueBattleScript = false,
                        )
                        return
                    }
                    if (combatPresentationBusy()) return
                    // `_attack_2/_attack_3` returns into `_ai2`, whose following
                    // `_shifudu -> _jiesuan(g_charinfo)` continuation publishes
                    // XD after the action clip has closed. Preserve that
                    // source frame boundary; target hit/local magic settlement
                    // callbacks have already committed independently.
                    if (!pendingAiActionCommitted) {
                        // `_attack6` owns a separate callback chain. After its
                        // final clip closes, expose the source idle row before
                        // resuming the original actor's global settlement.
                        if (counterattackSettlementFrameBarrier.yieldIdleBeforeCommit()) return
                        commitDeferredBattleAction(resolution.actorId)
                        pendingAiActionCommitted = true
                        aiPresentationStage = AiPresentationStage.COMPLETE
                        // `_jiesuan(g_charinfo)` has now published XD, but
                        // source has not selected the following `_ai2` actor
                        // yet. Expose that settled row under this action's
                        // episode before continuing the synchronous loop.
                        if (actionStatusFrameBarrier.yieldAfterCommit(hasAction = resolution.result != null)) return
                    }
                    aiPresentationStage = AiPresentationStage.COMPLETE
                    continue
                }
                AiPresentationStage.COMPLETE -> {
                    val completedNoResult = resolution.path.size < 2 && resolution.result == null
                    if (camp == Faction.PLAYER && !pendingAiPlayerMoveScriptStarted) {
                        pendingAiPlayerMoveScriptStarted = true
                    }
                    if (!pendingAiActionCommitted) {
                        commitDeferredBattleAction(resolution.actorId)
                        pendingAiActionCommitted = true
                    }
                    if (committedPlayerMoveFrameBarrier.yieldCompletionFrame(
                            isPlayer = camp == Faction.PLAYER,
                            moved = resolution.path.size >= 2 &&
                                (resolution.fromX != resolution.toX || resolution.fromY != resolution.toY),
                        )
                    ) return
                    if (scriptRuntime.state != PlaybackState.COMPLETE) return
                    if (pendingAiUnitDeathScriptPass == 0) {
                        pendingAiUnitDeathScriptPass = 1
                        runBattleScript()
                        if (scriptRuntime.state != PlaybackState.COMPLETE) return
                    }
                    // Source `_ai2` yields to unitDeath's first run_script
                    // before it discovers/hides dying units.  An authored
                    // stage.end() terminates that callback chain immediately:
                    // `_ai2` observes isEnd and never reaches unitHide or camp
                    // restore.  S_22's mine master death exercises this path.
                    if (pendingAiUnitDeathScriptPass == 1 && scriptRuntime.stage.battleEndedByScript) {
                        finishScriptEndedAiTurn()
                        return
                    }
                    if (pendingAiUnitDeathScriptPass == 1 && !postActionDeathsStarted) {
                        if (retainActualDyingUnits()) {
                            // `unitDeath` starts after `_ai2` yields back from
                            // the attacker's `_jiesuan` callback. Queue it now,
                            // then let the next render's death driver publish
                            // the target action in a distinct observation.
                            postActionDeathsStarted = true
                            return
                        }
                        pendingAiUnitDeathScriptPass = 2
                    }
                    if (pendingAiUnitDeathScriptPass == 1 && combatPresentationBusy()) return
                    if (pendingAiUnitDeathScriptPass == 1) {
                        pendingAiUnitDeathScriptPass = 2
                        runBattleScript()
                        if (scriptRuntime.state != PlaybackState.COMPLETE) return
                    }
                    pendingAiResolution = null
                    pendingAiActionStarted = false
                    pendingAiActionCommitted = false
                    postActionDeathsStarted = false
                    if (completedNoResult) consecutiveNoResultFrameGate.markCompleted()
                    continue
                }
            }
        }
    }

    /** Drop only the continuation owned by an explicit stage.end callback. */
    private fun finishScriptEndedAiTurn() {
        pendingAiResolution = null
        pendingAiActionStarted = false
        pendingAiPlayerMoveScriptStarted = false
        pendingAiActionCommitted = false
        pendingAiUnitDeathScriptPass = 0
        postActionDeathsStarted = false
        activeAiCamp = null
        turnController.finishScriptEndedBattle()
    }

    private fun handleTileClick(x: Int, y: Int) {
        // BattleScreen.unitMove pauses the layer until move2's final callback
        // executes after its destination delay.  Do not accept a second
        // command while the same source callback would still be pending.
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
                battleCommandFlow.beginMove(clicked.id, BattleCommandFlow.UnitPose(clicked.tileX, clicked.tileY, clicked.direction))
                selectedMagicIndex = 0
                propertyMode = false
                eventMessage = "${clicked.name} 선택 · 빈 칸으로 이동, 인접 적을 공격"
            }
            return
        }
        // Source `__attack` owns map targeting while CommandLayer's ATTACK
        // child is open. A blank, allied, hidden, or out-of-range touch makes
        // selectTarget return false; it returns to selection without calling
        // `_attack_3`, `_jiesuan`, or the default presentation action. Do not let that touch
        // fall through to ordinary unit selection/movement in the game.
        if (battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION &&
            battleCommandFlow.childCommand == BattleCommandFlow.Command.ATTACK
        ) {
            if (clicked == null || !clicked.visible || unitsAreAllied(selected, clicked)) {
                eventMessage = "공격 대상을 선택하세요."
                return
            }
            val healthBeforeAction = battle.units.mapValues { it.value.hitPoints }
            val result = battle.attackForPresentation(selected.id, clicked.id)
            if (result is TacticalActionResult.Rejected) {
                eventMessage = result.reason
                return
            }
            applyAction(result, selected.name, selected.id, targetId = clicked.id, healthBeforeAction = healthBeforeAction)
            battleCommandFlow.childCompleted(true)
            return
        }
        when {
            propertyMode && clicked != null && unitsAreAllied(selected, clicked) &&
                kotlin.math.abs(clicked.tileX - selected.tileX) + kotlin.math.abs(clicked.tileY - selected.tileY) <= 1 -> {
                val healthBeforeAction = battle.units.mapValues { it.value.hitPoints }
                propertyMode = false
                val result = usableProperties().getOrNull(selectedPropertyIndex)
                    ?.let { battle.usePropertyForPresentation(selected.id, clicked.id, it.id) }
                    ?: TacticalActionResult.Rejected("사용 가능한 소비 아이템이 없습니다.")
                applyAction(result, selected.name, selected.id, targetId = clicked.id, healthBeforeAction = healthBeforeAction)
            }
            propertyMode -> eventMessage = "아이템은 자신 또는 인접 아군에게 사용해야 합니다."
            clicked?.id == selected.id -> openBattleCommand(selected)
            clicked != null && clicked.type() == selected.type() && !clicked.hasActed -> {
                selectedUnitId = clicked.id
                battleCommandFlow.beginMove(clicked.id, BattleCommandFlow.UnitPose(clicked.tileX, clicked.tileY, clicked.direction))
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
                        battle.castMagicForPresentation(selected.id, clicked.id, it.id)
                    }
                        ?: TacticalActionResult.Rejected("사용할 수 있는 전략이 없습니다.")
                } else battle.attackForPresentation(selected.id, clicked.id)
                applyAction(result, selected.name, selected.id, magicId, clicked.id, healthBeforeAction)
                // CommandLayer's physical-target branch is a child action
                // just like MagickList/UseProperty.  A successful target
                // selection consumes the command; leaving it in
                // CHILD_ACTION makes the production driver keep clicking the
                // same target forever (S_52 round 4).
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
        if (unit.magic.isNotEmpty() && BattleStatus.SILENCE !in unit.statuses) mask = mask or BattleCommandFlow.MAGICK_BIT
        if (usableProperties().isNotEmpty()) mask = mask or BattleCommandFlow.PROPERTY_BIT
        if (battle.units.values.any { other ->
                other !== unit && other.visible && unitsAreAllied(unit, other) && other.armId == unit.armId &&
                    kotlin.math.abs(other.tileX - unit.tileX) + kotlin.math.abs(other.tileY - unit.tileY) == 1
            }) mask = mask or BattleCommandFlow.SWAP_BIT
        // checkCanSiege also requires the source unit's canSiegle flag and a
        // second force able to cover its target. The current BattleUnit model
        // has no canSiegle projection, so keep bit4 disabled until that table
        // field is implemented rather than enabling an invalid action.
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
        // Source unitMove() centers/follows the moving unit before _move2.
        focusCameraOn(unit)
        val deferredMove = battle.moveUnitForPresentation(unit.id, x, y)
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
        finalDirection?.let { direction -> battle.presentationUnit(unitId)?.direction = direction }
        pendingBattleCommandMoveProvenance?.let { provenance ->
            campaignE2ePlayerMoveCommitted = true
            campaignE2eCommittedPlayerMove = provenance
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

    /** `ctrl_mine` stops its operation after move2's run_script reports isEnd. */
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

    private fun applyAction(
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
            battle.presentationUnit(actorId)?.let(::focusCameraOn)
            val characterId = battle.presentationUnit(actorId)?.characterId
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
            // BattleScreen._attack2 uses GONG_JI2 (anime25), or HIT_ATTACK
            // (anime21) for a critical strike.  Both clips contain the
            // authored `hit` event which gates target damage presentation.
            is TacticalActionResult.Attack -> actorId?.let { id ->
                val delayed = battle.presentationUnit(id)?.attackDelay == true
                // `_attack2` chooses the clip from the raw critical gauge
                // before `_attack3/countAtkHarm` can cancel only the damage
                // multiplier (for example FYZMGJ).  `result.critical` is the
                // effective damage flag; the pass retains the visual roll.
                val visualCritical = result.physicalPasses.firstOrNull()?.critical ?: result.critical
                val sourceAction = BattleAttackSequence.selectAttackAction(visualCritical, delayed)
                sourceActionAnimation(id, sourceAction, battleDirection(id, targetId))
            }
            // `_magic` selects GONG_JI_YU_BEI2 (anime50) from its shared
            // morale-critical flag, independently of checkCrit's speech gate.
            is TacticalActionResult.Magic -> actorId?.let {
                battle.presentationUnit(it)?.let { unit -> focusCameraOn(unit, forceCenter = true) }
                sourceActionAnimation(it, if (result.critical) 50 else 5, battleDirection(it, targetId))
            }
            // `_usePro2` holds JU_QI_WU_QI while the item sprite rises for
            // .5 s and fades for 1 s; its model mutation follows that await.
            is TacticalActionResult.Item -> actorId?.let { id ->
                targetId?.let(battle::presentationUnit)?.let(::focusCameraOn)
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
            scheduleMagicPresentation(firstResult, casterId, profile, healthBeforeAction, effectAnimations = listOfNotNull(firstEffect))
            if (magic.passes.size > 1) {
                val visualHp = healthBeforeAction.toMutableMap()
                val deferred = battle.pendingActionTransaction
                val visualMp = mutableMapOf<String, Int>().apply {
                    battle.presentationUnits().forEach { unit ->
                        this[unit.id] = deferred?.initialMp(unit.id) ?: unit.magicPoints
                    }
                    this[casterId] = ((deferred?.initialMp(casterId)
                        ?: battle.presentationUnit(casterId)?.magicPoints ?: 0) - magic.cost).coerceAtLeast(0)
                }
                advanceMagicVisualState(firstPass, casterId, profile, visualHp, visualMp)
                queuedMagicPresentation = MagicPassPresentationQueue(
                    result = magic,
                    casterId = casterId,
                    targetId = targetId,
                    profile = profile,
                    effectId = effectId,
                    nextPassIndex = 1,
                    startsAt = (firstEffect?.endsAt ?: startedAt) + 1f,
                    visualHp = visualHp,
                    visualMp = visualMp,
                )
            }
        }
        scheduleCombatPresentation(result, actorId, targetId, healthBeforeAction)
        when (result) {
            // Physical sound is scheduled by scheduleCombatPresentation at
            // the authored attack `hit` event.
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
        // BattleScreen._attack2 awaits the attacking clip; _attack3 then
        // awaits the target's SHOU_GONG_JI3 clip before its controller can
        // process the next scene1 event. Running it immediately pauses this
        // game's battle clock behind SayLayer before `hit` can ever fire.
        // Source `__attack` returns false after a cancelled/invalid target
        // and the outer unit loop reopens selection; it never enters
        // `_jiesuan` or consumes the actor. In particular, do not schedule a
        // deferred callback for a rejected physical map touch: that callback
        // could later commit the command while CHILD_ACTION is still active.
        if (continueBattleScript && result !is TacticalActionResult.Rejected) {
            pendingBattleScriptPassesAfterAction = 1
            pendingBattleCompletedScriptPasses = 0
            pendingBattleActionCommitted = false
            pendingBattleSettlementActorId = actorId
            postActionDeathsStarted = false
        }
    }

    /** say4's callback starts the physical action only after SayLayer closes. */
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

    /** Scene0/initial scene1 own the battle even if their temporary roster satisfies a terminal predicate. */
    private fun visibleBattleOutcome(): BattleOutcome? =
        battle.outcome().takeIf { bootstrapPhase == BattleBootstrapPhase.COMPLETE }

    private fun continueAfterOutcome() {
        when (visibleBattleOutcome()) {
            BattleOutcome.PLAYER_VICTORY -> {
                rewardFlow?.let { advanceRewardFlow(); return }
                // Source BattleScreen dispatches scene2 once. Its dialogue and
                // RewardLayer remain live modal steps instead of being drained
                // in one key press.
                if (!postBattleSceneStarted && "scene2" in scriptRuntime.functionNames) {
                    postBattleSceneStarted = true
                    scriptRuntime.start("scene2")
                    syncScriptedUnits()
                    openRewardRequestIfNeeded()
                    if (scriptRuntime.state != PlaybackState.COMPLETE || rewardFlow != null) return
                }
                if (scriptRuntime.state == PlaybackState.DIALOGUE ||
                    scriptRuntime.state == PlaybackState.CHOICE ||
                    scriptRuntime.state == PlaybackState.MODAL) return
                openVictorySavePrompt()
            }
            BattleOutcome.ENEMY_VICTORY -> enterLoseScene()
            null -> Unit
        }
    }

    private fun openVictorySavePrompt() {
        if (resultFlow == ResultFlow.WIN_SAVE_PROMPT || battleRouteCompleted || postBattleSaveLayer) return
        if (fullTraceConfig?.exitOnFinish == false) finishFullBattleTrace("battle-end")
        resultFlow = ResultFlow.WIN_SAVE_PROMPT
        eventMessage = "게임 저장하시겠습니까?"
    }

    private fun victorySaveAnswerAt(x: Float, y: Float): Int? = when {
        x in 460f..620f && y in 285f..365f -> 0 // 예
        x in 690f..850f && y in 285f..365f -> 1 // 비
        else -> null
    }

    private fun answerVictorySavePrompt(answer: Int) {
        resultFlow = ResultFlow.NONE
        if (answer == 0) {
            postBattleSaveLayer = true
            saveLayer.onCreate()
            saveLayerOpen = true
        } else finishVictoryRoute()
    }

    private fun finishVictoryRoute() {
        if (battleRouteCompleted) return
        postBattleSaveLayer = false
        val next = nextScenario()
        scriptRuntime.stage.sceneJumpStage?.let(game::setCampaignStage) ?: game.advanceCampaignStage()
        game.completeBattle(returnScenario, next)
        battleRouteCompleted = true
        game.showNextScenario(next)
    }

    private fun openRewardRequestIfNeeded() {
        if (rewardFlow != null) return
        val request = scriptRuntime.stage.consumeRewardRequest() ?: return
        val resolved = BattleRewardResolver.resolve(
            request = request,
            averageLevel = campaign.averageJoinedLevel(),
            round = battle.round,
            maxRound = scenarioMaxRound(),
            mineDeaths = battle.units.values.count { it.faction == Faction.PLAYER && it.hitPoints < 1 },
            enemiesRemaining = battle.units.values.count { it.type().isEnemySide() && it.visible },
            // The runtime does not discard authored objective completion: a
            // scripted victory has already satisfied the active condition.
            objectivesComplete = false,
        )
        campaign.addMoney(resolved.money)
        request.items.chunked(2).forEach { pair ->
            val id = pair.firstOrNull() ?: return@forEach
            if (id >= 255) return@forEach
            val supplied = pair.getOrNull(1) ?: 1
            val level = if (supplied < 0) (campaign.averageJoinedLevel() / 10).coerceIn(0, 8) + 1 else supplied.coerceAtLeast(1)
            campaign.inventory.addItem(id, count = 1, level = level)
            campaign.inventory.discoverTreasure(id, gameDataCatalog)
        }
        rewardFlow = BattleRewardFlow(resolved)
        if (rewardFlow?.complete == true) advanceRewardFlow()
    }

    private fun advanceRewardFlow() {
        val flow = rewardFlow ?: return
        flow.advance()
        if (!flow.complete) return
        rewardFlow = null
        if (scriptRuntime.state == PlaybackState.MODAL) scriptRuntime.resumeModal()
        syncScriptedUnits()
        openRewardRequestIfNeeded()
        if (postBattleSceneStarted && scriptRuntime.state == PlaybackState.COMPLETE && rewardFlow == null) {
            openVictorySavePrompt()
        }
    }

    /**
     * Source battle completion is callback-driven: scene1 observes victory,
     * resumes from RewardLayer, ends, then scene2 and Hall replacement run.
     * None of those boundaries waits for an extra keyboard event.
     */
    private fun driveNaturalBattleCompletion() {
        val transitionBusy = combatPresentationBusy() || outcomeCallbacksPending()
        if (verifyMode || scriptedBattleVerifyMode || game.hasFrameCaptureRequest() ||
            game.hasRenderEventLogRequest() || battleRouteCompleted ||
            visibleBattleOutcome() != BattleOutcome.PLAYER_VICTORY || transitionBusy
        ) return
        when (NaturalBattleTransition.completionAction(
            visibleBattleOutcome(), transitionBusy, scriptRuntime.state,
            rewardFlow != null, scriptRuntime.stage.battleEndedByScript,
            naturalOutcomeScriptStarted,
        )) {
            NaturalBattleTransition.CompletionAction.WAIT -> Unit
            NaturalBattleTransition.CompletionAction.RUN_SCENE1 -> {
                naturalOutcomeScriptStarted = true
                runBattleScript()
                syncScriptedUnits()
                openRewardRequestIfNeeded()
            }
            NaturalBattleTransition.CompletionAction.START_SCENE2 -> continueAfterOutcome()
        }
    }

    private fun settlementAnimatedValues(overlay: SettlementInfoOverlay): Map<Int, Int> {
        val values = buildList {
            overlay.deltas.forEach { delta -> add(InfoBaseValueAnimation.Value(
                if (delta.kind == SettlementInfoKind.HP) 0 else 1,
                delta.before, delta.after,
                battle.presentationUnit(overlay.unitId)?.let {
                    if (delta.kind == SettlementInfoKind.HP) it.maxHitPoints else it.maxMagicPoints
                } ?: delta.after.coerceAtLeast(1),
            )) }
            overlay.grants.forEach { grant -> when (grant.kind) {
                SettlementGrowthKind.UNIT_EXP -> grant.unitResult?.let { result ->
                    add(InfoBaseValueAnimation.Value(2, result.oldExperience, result.oldExperience + result.gained,
                        (result.oldExperience + result.gained).coerceAtLeast(1)))
                }
                SettlementGrowthKind.WEAPON_EXP -> grant.equipmentResult?.let { result ->
                    add(InfoBaseValueAnimation.Value(3, result.oldExperience, result.oldExperience + result.gained,
                        (result.oldExperience + result.gained).coerceAtLeast(1)))
                }
                SettlementGrowthKind.ARMOR_EXP -> grant.equipmentResult?.let { result ->
                    add(InfoBaseValueAnimation.Value(4, result.oldExperience, result.oldExperience + result.gained,
                        (result.oldExperience + result.gained).coerceAtLeast(1)))
                }
            } }
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

    /** Live MineUnitInfoLayer/OtherUnitInfoLayer settlement fields, not a HUD message shortcut. */
    private fun drawSettlementOverlays() {
        settlementInfoOverlay?.let { overlay ->
            val unit = battle.presentationUnit(overlay.unitId) ?: return@let
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
                overlay.grants.forEach { grant -> when (grant.kind) {
                    SettlementGrowthKind.UNIT_EXP -> grant.unitResult?.let { add(Triple("EXP", values[2] ?: it.oldExperience, (it.oldExperience + it.gained).coerceAtLeast(1))) }
                    SettlementGrowthKind.WEAPON_EXP -> grant.equipmentResult?.let { add(Triple("WQ", values[3] ?: it.oldExperience, (it.oldExperience + it.gained).coerceAtLeast(1))) }
                    SettlementGrowthKind.ARMOR_EXP -> grant.equipmentResult?.let { add(Triple("HJ", values[4] ?: it.oldExperience, (it.oldExperience + it.gained).coerceAtLeast(1))) }
                } }
            }
            rows.forEachIndexed { index, (label, value, max) ->
                val rowY = y + h - 70f - index * 48f
                font.draw(batch, label, x + 16f, rowY)
                batch.draw(unitInfoAssets.unitInfoProgress, x + 78f, rowY - 22f, 300f, 20f)
                val bar = if (label == "MP") unitInfoAssets.unitInfoMark2 else if (label == "EXP") unitInfoAssets.unitInfoMark6 else unitInfoAssets.unitInfoMark3
                batch.draw(bar, x + 80f, rowY - 20f, 296f * value.coerceAtLeast(0) / max.coerceAtLeast(1), 16f)
                font.draw(batch, "$value/$max", x + 390f, rowY)
            }
            font.data.setScale(1f)
            batch.end()
        }
        settlementInfo2Overlay?.let { overlay ->
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
        val active = activeScriptPresentation ?: return
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
                active.battleUnitId?.let(battle::presentationUnit)?.let { unit ->
                    shapes.projectionMatrix = viewport.camera.combined
                    shapes.begin(ShapeRenderer.ShapeType.Filled)
                    shapes.color = Color(1f, .08f, .04f, .42f)
                    shapes.rect(boardLeft + unit.tileX * boardTile, tileBottom(unit.tileY), boardTile, boardTile)
                    shapes.end()
                }
            }
            is ScenarioScriptPresentationRequest.GetItem -> {
                if (active.phase != ScriptPresentationPhase.ITEM_ICON) return
                val unit = active.battleUnitId?.let(battle::presentationUnit) ?: return
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

    private fun drawBattleReward(flow: BattleRewardFlow) {
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, 50f / 255f)
        shapes.rect(0f, 0f, viewport.worldWidth, 800f)
        shapes.end()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        fun labelPair(text: String, shadowX: Float, shadowBaseline: Float, x: Float, baseline: Float) {
            rewardTitleFont.color = Color(.3f, .3f, .3f, 1f)
            rewardTitleFont.draw(batch, text, shadowX, shadowBaseline)
            rewardTitleFont.color = Color.WHITE
            rewardTitleFont.draw(batch, text, x, baseline)
        }
        when (flow.phase) {
            BattleRewardFlow.Phase.MONEY -> {
                labelPair("전투 종료", 527.747f, 615.617f, 519.916f, 627.594f)
                labelPair("보상금", 282.777f, 399.692f, 274.533f, 405.6f)
                labelPair(flow.reward.money.toString(), 967.617f, 399.007f, 958.035f, 405.6f)
                val stars = (0 until 3).joinToString("  ") { if (flow.reward.flag and (1 shl it) != 0) "★" else "☆" }
                labelPair(stars, 531.389f, 204.017f, 521.806f, 207.313f)
            }
            BattleRewardFlow.Phase.ITEMS -> {
                labelPair("전리품", 596.73f, 726.144f, 588.486f, 739.142f)
                flow.reward.itemIds.take(flow.visibleItemCount).take(3).forEachIndexed { index, id ->
                    val y = 433.5f - index * 157f
                    overlayAssets.rewardItemTexture?.let { batch.draw(it, 499.686f, y, 489f, 101f) }
                    overlayAssets.winConditionBoxPatch?.draw(batch, 499.686f, y, 489f, 101f)
                    val profile = gameDataCatalog.equipmentProfile(id)
                    profile?.icon?.let(dynamicTextures::itemIcon)?.let { batch.draw(it, 534.974f, y + 18.5f, 64f, 64f) }
                    font.color = Color.WHITE
                    font.draw(batch, profile?.name ?: "아이템 $id", 648.999f, y + 78.22f)
                }
            }
            BattleRewardFlow.Phase.END -> {
                labelPair("전투 종료", 527.747f, 488.023f, 519.916f, 500f)
                labelPair(flow.reward.money.toString(), 658f, 322f, 650f, 330f)
            }
            BattleRewardFlow.Phase.COMPLETE -> Unit
        }
        batch.end()
    }

    /**
     * The source keeps SectionLayer attached above RewardLayer until the
     * reward callback resumes scene2.  These are three real submissions
     * (background, shadow label, foreground label), after the reward draws.
     */
    private fun drawRewardSection() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        overlayAssets.sectionBackgroundTexture?.let { batch.draw(it, 0f, 0f, 1488.3721f, 800f) }
        sectionTitleFont.color = Color(0.28f, 0.28f, 0.28f, 1f)
        sectionTitleFont.draw(batch, "영천의 전투", 431.986f, 478.2f)
        sectionTitleFont.color = Color.WHITE
        sectionTitleFont.draw(batch, "영천의 전투", 421.986f, 488.2f)
        batch.color = Color.WHITE
        batch.end()
    }

    private fun openEquipmentUpgradeIfNeeded() {
        if (itemUpgradeFlow != null) return
        if (activeTurnSettlement != null) return
        if (itemUpgradeRouteState == null && (
                actionAnimation?.let { animationClock() < it.endsAt } == true ||
                    movementAnimation?.let { animationClock() < it.endsAt } == true ||
                    hitReactionAnimations.values.any { animationClock() < it.endsAt }
            )) return
        val request = battle.consumeEquipmentUpgrade() ?: return
        val profile = gameDataCatalog.equipmentProfile(request.itemId) ?: return
        val owner = campaign.unitNames[request.unitId]
            ?: gameDataCatalog.unitProfile(request.unitId)?.name
            ?: ""
        val attribute = when (request.slot) {
            CampaignEquipmentSlot.WEAPON -> "공격력"
            CampaignEquipmentSlot.ARMOR -> "방어력"
            CampaignEquipmentSlot.AUXILIARY -> "정신력"
        }
        itemUpgradeFlow = ItemUpgradeFlow(request, owner, profile.name, attribute) {
            itemUpgradeCallbackCount++
            itemUpgradeFlow = null
        }
    }

    private fun openSettlementItemUpgrade(request: CampaignEquipmentExperienceResult) {
        check(itemUpgradeFlow == null) { "overlapping settlement ItemUpgradeLayer" }
        val profile = gameDataCatalog.equipmentProfile(request.itemId)
            ?: error("settlement item profile is missing: ${request.itemId}")
        val owner = campaign.unitNames[request.unitId]
            ?: gameDataCatalog.unitProfile(request.unitId)?.name.orEmpty()
        val attribute = when (request.slot) {
            CampaignEquipmentSlot.WEAPON -> "공격력"
            CampaignEquipmentSlot.ARMOR -> "방어력"
            CampaignEquipmentSlot.AUXILIARY -> "정신력"
        }
        itemUpgradeFlow = ItemUpgradeFlow(request, owner, profile.name, attribute) {
            itemUpgradeCallbackCount++
            itemUpgradeFlow = null
        }
    }

    /**
     * Drives the same Battle._addWeaponExp-equivalent queue as combat. Only
     * the deterministic route preloads EXP=limit-1; the level mutation and
     * Global113 request are produced by the live Campaign/Battle callbacks.
     */
    private fun installItemUpgradeRoute() {
        itemUpgradeRouteInstalled = true
        val owner = battle.units.values.firstOrNull { it.visible && it.isPlayerSide() && it.characterId != null }
            ?: error("ItemUpgrade actual route has no player equipment owner")
        val target = battle.units.values.firstOrNull { it.visible && it.type().isEnemySide() }
            ?: error("ItemUpgrade actual route has no enemy target")
        val ownerId = requireNotNull(owner.characterId)
        val itemId = 0
        val oldLevel = 2
        val oldExp = gameDataCatalog.equipmentExperienceLimit(itemId, oldLevel) - 1
        val current = campaign.inventory.equipment[ownerId] ?: CampaignEquipment(2, oldLevel, 72, 1, 111)
        campaign.inventory.setEquipment(ownerId, current.copy(
            // StageLayer compact ID for source item 0 (단검): id-offset+2.
            weapon = 2,
            weaponLevel = oldLevel,
            weaponExperience = oldExp,
        ))
        campaign.unitNames[ownerId] = "유비"
        battle.addEquipmentExperience(owner.id, target.id, 1)
        openEquipmentUpgradeIfNeeded()
        check(itemUpgradeFlow?.request?.leveledUp == true) { "ItemUpgrade actual route did not level equipment" }
    }

    private fun closeItemUpgrade() {
        itemUpgradeFlow?.panelCancelTouchEnd()
    }

    /** Actual Global113 composition, sharing the imported DynamicAtlas crops. */
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
        fun tiled(x:Float,y:Float,w:Float,h:Float) {
            var yy=0f;while(yy<h){var xx=0f;while(xx<w){batch.draw(unitInfoAssets.unitInfoLogo,x+xx,y+yy,minOf(96f,w-xx),minOf(96f,h-yy));xx+=96f};yy+=96f}
        }
        fun button(x:Float,y:Float,text:String){NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,x,y,238.8f,56.6f);font.color=Color.BLACK;font.draw(batch,text,x+8f,y+43f)}
        tiled(453.686f,195f,581f,410f)
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,767.301f,487.229f,169.8f,50f)
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,768.224f,430.411f,160f,50f)
        font.color=Color.BLACK
        font.draw(batch,"전장 편집",669.431f,592f);font.draw(batch,"날씨: ",675.735f,530f)
        font.draw(batch,edit.weatherLabel,817.601f,529f);font.draw(batch,"현재 턴:",618.435f,474f)
        font.draw(batch,edit.roundText,770.224f,472f)
        button(495.886f,207.8f,"수정");button(772.686f,207.8f,"취소")
        button(495.886f,354.9f,"전역");button(495.886f,277.1f,"적군 체력 감소")
        button(772.686f,354.9f,"적군 전멸");button(772.686f,277.1f,"아군 만피")
        batch.end()
        if (route == BattleEditLayer2Route.WEATHER) {
            shapes.begin(ShapeRenderer.ShapeType.Filled);shapes.color=Color(0f,0f,0f,.392f);shapes.rect(0f,0f,1488.372f,800f);shapes.end()
            batch.begin();batch.color=Color.WHITE
            NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,767.878f,308.794f,169.8f,179.5f)
            BattleEditLayer2.weatherNames.forEachIndexed{i,text->val y=463.854f-i*50f;NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,767.878f,y,169.8f,50f);font.draw(batch,text,800f,y+41f)}
            batch.end()
        }
        if ((route == BattleEditLayer2Route.CHILD || route == BattleEditLayer2Route.CHILD_SCENE || route == BattleEditLayer2Route.REGISTER) && battleEdit3Open) drawBattleEdit3Child()
        if (battleEdit3ScenePanelOpen) drawBattleEdit3ScenePanel()
        if (route == BattleEditLayer2Route.REGISTER && battleRegisterRoute?.view()?.registerAttached == true) drawBattleRegisterLayer()
    }

    private fun drawBattleEdit3Child() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);shapes.color=Color(0f,0f,0f,.314f);shapes.rect(0f,0f,1488.372f,800f);shapes.end()
        batch.begin();batch.color=Color.WHITE
        var yy=0f;while(yy<410f){var xx=0f;while(xx<600f){batch.draw(unitInfoAssets.unitInfoLogo,444.186f+xx,195f+yy,minOf(96f,600f-xx),minOf(96f,410f-yy));xx+=96f};yy+=96f}
        fun box(x:Float,y:Float,w:Float,h:Float)=NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,x,y,w,h)
        fun btn(x:Float,y:Float,w:Float,h:Float,text:String){NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,x,y,w,h);font.draw(batch,text,x+18f,y+42f)}
        box(715.31f,397f,225.2f,50f);box(715.31f,315f,225.2f,50f);box(714.91f,479f,250f,50f)
        font.color=Color.BLACK;font.draw(batch,"전역 변수 편집",629.271f,596f);font.draw(batch,"야심:",625.117f,438f);font.draw(batch,"50",717.31f,439f)
        font.draw(batch,"금전:",625.117f,356f);font.draw(batch,"0",717.31f,357f);font.draw(batch,"장면 이동:",544.957f,519f);font.draw(batch,"영천의 전투R",718.51f,520f)
        btn(876.797f,212.983f,150.4f,58.5f,"수정");btn(719.152f,212.983f,150.4f,58.5f,"폐쇄");btn(487.035f,212.95f,221.5f,58.5f,"창고 비우기")
        batch.end()
    }

    private fun drawBattleEdit3ScenePanel() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);shapes.color=Color(0f,0f,0f,.392f);shapes.rect(0f,0f,1488.372f,800f);shapes.end()
        batch.begin();batch.color=Color.WHITE
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,715.136f,298.894f,250f,179.5f)
        val names=listOf("영천의 전투","사수관 전투","호로관 전투","동탁 추격전","청주 황건 토벌전","서주 복수전","복양의 전투","복양의 전투 2","복양의 전투 3","황제 구출 전투")
        names.forEachIndexed { index, name -> val y=428.394f-index*50f;NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,715.136f,y,250f,50f);font.draw(batch,"$index $name",720.136f,y+41f) }
        batch.end()
    }

    private fun drawBattleRegisterLayer() {
        batch.begin();batch.color=Color.WHITE
        for(ty in 0..4)for(tx in 0..8)batch.draw(unitInfoAssets.unitInfoLogo,344.186f+tx*96f,163.5f+ty*96f,96f,96f)
        val box=NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3);val button=NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11)
        box.draw(batch,344.186f,163.5f,800f,473f);box.draw(batch,355.686f,520f,773f,54f)
        button.draw(batch,916.163f,180.272f,200f,50f);button.draw(batch,698.334f,180.272f,200f,50f)
        font.color=Color.BLACK
        font.draw(batch,"등록 코드 생성기",624.186f,628f)
        font.draw(batch,"활성화 코드를 입력하세요",369.186f,563f)
        font.draw(batch,"Label",360.186f,280f);font.draw(batch,"Label",360.186f,435f)
        font.draw(batch,"생성 공유",939.408f,222f);font.draw(batch,"취소",748.334f,229f)
        batch.end()
    }

    private fun drawItemUpgrade(flow: ItemUpgradeFlow) {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        // Logo_9 is authored as a tiled sprite. Clip the last cells to the
        // 400x259 panel bounds instead of stretching the atlas crop.
        val left = 544.186f
        val bottom = 270.5f
        for (ty in 0..2) for (tx in 0..4) {
            val width = minOf(96f, 400f - tx * 96f)
            val height = minOf(96f, 259f - ty * 96f)
            if (width > 0f && height > 0f) batch.draw(unitInfoAssets.unitInfoLogo, left + tx * 96f, bottom + ty * 96f, width, height)
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
        itemUpgradeFont.draw(batch, "${flow.attributeName} ${flow.request.oldValue} -> ${flow.request.newValue}", 554.836f, 403.7f)
        itemUpgradeFont.color = Color.WHITE
        batch.end()
    }

    /**
     * Visible draw submissions for the real BattleScreen reward route.  Every
     * value is derived from the same battle/reward state and layout constants
     * used by drawGrid, drawBattleHudChrome, drawBattleReward and
     * drawRewardSection; no source JSONL is loaded or replayed.
     */
    private fun writeYingchuanEntryFlowIfReady() {
        val output = yingchuanEntryFlowTracePath ?: return
        if (yingchuanEntryFlowWritten || !yingchuanEntryFlowSawInit) return
        if (!scriptRuntime.stage.battleDrawRequested || battleInitLayer.view().attached ||
            scriptRuntime.state != PlaybackState.DIALOGUE || scriptRuntime.currentDialogue == null) return

        val json = Json()
        fun stateText(init: Boolean): String {
            val dialogue = if (init) "null" else "{\"name\":\"SayLayer\",\"active\":true}"
            val tailLayer = if (init) "BattleInitLayer" else "SayLayer"
            return "{\"scene\":\"Battle\",\"isDraw\":${!init},\"paused\":true,\"round\":${battle.round},\"camp\":-1," +
                "\"battleInit\":$init,\"dialogue\":$dialogue,\"modal\":null," +
                "\"layers\":[\"BattleScreen\",\"NoticeInfoLayer\",\"MiniMapLayer\",\"$tailLayer\"]}"
        }
        val records = listOf("battle-init" to stateText(true), "dialogue" to stateText(false)).mapIndexed { sequence, (phase, text) ->
            "{\"sequence\":$sequence,\"frame\":$sequence,\"phase\":\"$phase\",\"layer\":\"BattleScreen/entry-flow\"," +
                "\"nodePath\":\"BattleScreen/entry-flow\",\"drawType\":\"state\",\"x\":0,\"y\":0,\"w\":1,\"h\":1," +
                "\"assetId\":\"none\",\"opacity\":1,\"blend\":\"normal\",\"visible\":true,\"text\":${json.toJson(text, String::class.java)}}"
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
                "\n  \"battleInitAttached\": ${battleInitLayer.view().attached},\n  \"modalKind\": ${quoted(scriptRuntime.currentModalKind?.name)}\n}\n",
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
        if (loseRestartRoute) {
            val log = RenderEventLog()
            LoseSceneRenderEvents.append(log, requireNotNull(loseSceneFlow))
            return log.jsonl()
        }
        val route = rewardRouteState ?: itemUpgradeRouteState ?: winConditionRouteState
            ?: if (battleInitRoute) "battle-init" else null
            ?: if (battleDialogueBlendRoute) "battle-dialogue-blending" else return RenderEventLog().jsonl()
        val phase = when {
            battleInitRoute -> "battle-init"
            battleDialogueBlendRoute -> "battle-dialogue-blending"
            itemUpgradeRouteState != null -> "battle-item-upgrade-panel-route"
            winConditionRouteState != null -> winConditionRouteState.removeSuffix("-fixture")
            else -> route.removePrefix("yingchuan-")
        }
        val log = RenderEventLog()
        fun draw(layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                 asset: String? = null, opacity: Float = 1f, text: String = "",
                 blend: Any = listOf(770, 771)) {
            if (opacity <= 0f || x + w <= 0f || x >= 1488.3721f || y + h <= 0f || y >= 800f) return
            log.draw(phase, layer, path, type, x, y, w, h, asset, opacity, blend, true, text)
        }

        draw(
            "HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite",
            -320f, if (rewardRouteState == null && !battleInitRoute) -96f else -560f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>",
        )

        boardLeft = -320f
        boardBottom = if (rewardRouteState != null || battleInitRoute) 1264f else 1728f
        boardTile = 96f
        val visibleUnits = (battle.units.values + battle.pendingPresentationUnits().filter {
            it.hitPoints <= 0 || it.id in hitReactionAnimations ||
                it.id in deathAnimations || it.id in pendingDeathAnimations
        })
            .asSequence().filter { it.visible }
            .filter { !battleInitRoute }
            // Equal-z siblings retain source/script insertion order.
            .sortedWith(if (battleDialogueBlendRoute) {
                val order = listOf(480, 483, 484, 146, 147, 481, 482, 485, 478, 479, 475, 476, 477, 235, 334, 474, 210, 234, 211)
                compareBy<BattleUnit> { order.indexOf(it.characterId).let { idx -> if (idx < 0) 999 else idx } }
            } else {
                compareBy<BattleUnit> { visualTile(it).second }
            })
            .toList()
        visibleUnits.forEach { unit ->
            val action = actionAnimation?.takeIf { animationClock() < it.endsAt && it.unitId == unit.id }
                ?: hitReactionAnimations[unit.id]?.takeIf { animationClock() in it.startedAt..<it.endsAt }
                ?: deathAnimations[unit.id]?.takeIf { animationClock() in it.startedAt..<it.endsAt }
            val move = movementAnimation?.takeIf { animationClock() < it.endsAt && it.unitId == unit.id }
            val scripted = action?.let { null } ?: scriptedUnitVisuals[unit.id]
            val frame = (if (battleDialogueBlendRoute) battleSpriteFrame(0, 0, 0f) else null)
                ?: winConditionActualVisualFrame(unit) ?: action?.let { transientVisualFrame(it) }
                ?: move?.let { movementVisualFrame(it) }
                ?: scripted?.let { scriptedVisualFrame(unit, it) }
                ?: idleSpriteFrame(unit)
            val size = if (frame.source == UnitSpriteSource.ATTACK) boardTile * 4f / 3f else boardTile
            val (visualX, visualY) = visualTile(unit)
            val x = boardLeft + visualX * boardTile + (boardTile - size) / 2f + frame.offsetX
            val y = tileBottom(visualY) + (boardTile - size) / 2f + frame.offsetY
            val atlasUuid = dynamicTextures.movementAtlasUuid(battleAvatarId(unit))
            val atlasSuffix = when (atlasUuid) {
                "31cc3c95-4d6e-4c10-848f-ef1ca165e78f" -> "850f3"
                "9eebca65-e81b-4ba4-ad61-7ac20d03661c" -> "f1ee0"
                "3f8fbf89-4dd0-4d0b-88e0-9c7927fe5693" -> "3f9c2"
                "ca6577ee-3ca1-4280-9d60-117070dd2d0b" -> "6ef7f"
                "19ac1287-4d09-45f4-bf9a-f5eb8b21795c" -> "89d84"
                else -> null
            }
            if (atlasUuid != null && atlasSuffix != null) {
                val atlasType = when (frame.source) {
                    UnitSpriteSource.ATTACK -> 0
                    UnitSpriteSource.MOVEMENT -> 1
                    UnitSpriteSource.SPECIAL -> 2
                }
                val generatedFrameName = if (battleDialogueBlendRoute) 33632304 else
                    (((frame.sourceY - 1).coerceAtLeast(0) / 50) shl 24) or (atlasType shl 16) or 12336
                draw(
                    "HallLayer", "Canvas/Layer/ScrollView/view/content/map/unit/mask/node", "sprite",
                    x, y, size, size,
                    if (winConditionRouteState != null && unit.characterId == 235) generatedFrameName.toString()
                    else "assets/Game/native/${atlasUuid.take(2)}/$atlasUuid.$atlasSuffix.png#$generatedFrameName",
                )
            }
            if (!(sourceScenario == "S_00" && scriptedUnitVisuals[unit.id]?.action == 4)) {
                val shownHp = healthTimeline.shownHp(unit.id, animationClock(), unit.hitPoints)
                val ratio = (shownHp.toFloat() / unit.maxHitPoints.coerceAtLeast(1)).coerceIn(0f, 1f)
                val barX = boardLeft + visualX * boardTile + 4f
                val barY = tileBottom(visualY) - 1f
                val barAsset = when (unit.type()) {
                    Faction.PLAYER -> "Mark_5-1"
                    Faction.FRIEND -> "Mark_3-1"
                    Faction.ENEMY -> if (unit.famous) "Mark_2-1" else "Mark_68-1"
                    Faction.REINFORCEMENTS -> if (unit.famous) "Mark_2-1" else "Mark_68-1"
                }
                draw(
                    "HallLayer", "Canvas/Layer/ScrollView/view/content/map/unit/info/bar2/sprite", "sliced-sprite",
                    barX, barY, 88f * ratio, 6f, barAsset,
                )
            }
        }

        if (battleDialogueBlendRoute) {
            val sayUnit = battle.units.values.firstOrNull { it.visible && it.characterId == 474 }
            sayUnit?.let { unit ->
                val (visualX, visualY) = visualTile(unit)
                draw("HallLayer", "Canvas/Layer/ScrollView/view/content/map/New Node", "sprite",
                    boardLeft + visualX * boardTile + boardTile * .75f,
                    tileBottom(visualY) + boardTile * .75f,
                    48f, 48f, "Mark_10-1")
            }
        }
        draw("HallLayer", "Canvas/Layer/menu_button/Background", "sprite", 1353.9535f, 8f, 60f, 60f, "menu")
        if (battleInitRoute) {
            draw("BattleInitLayer", "Canvas/Layer/bg/button/Background", "sliced-sprite", .843f, .731f, 68f, 68f, "bg1")
            draw("BattleInitLayer", "Canvas/Layer/bg/button/Background/tool11", "sprite", .043f, -.069f, 69.6f, 69.6f, "tool10")
            draw("BattleInitLayer", "Canvas/Layer/bg/btn/Background", "sliced-sprite", 1418.372f, 730f, 70f, 70f, "bg1")
            draw("BattleInitLayer", "Canvas/Layer/bg/btn/Background/tool11", "sprite", 1418.572f, 730.2f, 69.6f, 69.6f, "tool11")
            draw("BattleInitLayer", "Canvas/Layer/bg", "sprite", 0f, 0f, 1488.372f, 800f,
                "assets/resources/native/59/5961a224-35cd-4838-b67a-a072b0b31ca4.14b27.jpg#Logo_5-1")
            draw("BattleInitLayer", "Canvas/Layer/bg/label0", "label", 431.986f, 301.8f, 644.4f, 176.4f,
                text = "영천의 전투", blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
            draw("BattleInitLayer", "Canvas/Layer/bg/label1", "label", 421.986f, 311.8f, 644.4f, 176.4f,
                text = "영천의 전투", blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
            return log.jsonl()
        }
        if (battleDialogueBlendRoute) {
            val dialogue = requireNotNull(scriptRuntime.currentDialogue)
            val speaker = dialogue.speakerId?.toIntOrNull()?.let(gameDataCatalog::unitProfile)
            val speakerName = speaker?.name?.let(GameDataCatalog::sayLayerUnitName).orEmpty()
            val headId = speaker?.face?.plus(8)
            // These submissions are the same live geometry and source-over
            // blend used by drawScriptDialogue. Exact source asset/path
            // identifiers are refined from the canonical actual-route log.
            if (headId != null) draw("SayLayer", "Canvas/Layer/bg0/face", "sprite", 1064.618f, 330f, 192f, 240f, headId.toString())
            draw("SayLayer", "Canvas/Layer/bg0/bg2", "sprite", 245.65f, 332f, 796f, 212f, "U_select_11-1")
            draw("SayLayer", "Canvas/Layer/bg0/bg2/richtext", "rich-text", 272.705f, 431.814f, 728f, 52.92f,
                text = dialogueReveal.visibleText, blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
            draw("SayLayer", "Canvas/Layer/bg0/bg2/richtext/RICHTEXT_CHILD", "label", 272.705f, 431.814f, 72.28f, 52.92f,
                text = dialogueReveal.visibleText, blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
            draw("SayLayer", "Canvas/Layer/bg0/label", "label", 304.804f, 485.639f, 97.42f, 49.36f,
                text = speakerName, blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
            return log.jsonl()
        }
        if (winConditionRouteState != null) {
            if (winConditionRouteState == "battle-win-condition-compact-fixture") {
                val text = requireNotNull(winConditionLayer).view().label
                draw("WinConBoxLayer", "Canvas/Layer/bg0", "tiled-sprite", 249.686f, 65f, 989f, 670f, "Logo_9-1")
                draw("WinConBoxLayer", "Canvas/Layer/bg0/box2", "tiled-sprite", 249.686f, 65f, 989f, 670f, "box3")
                draw("WinConBoxLayer", "Canvas/Layer/bg0/Logo_3-1", "sprite", 280.574f, 588.927f, 106f, 124f, "Logo_3-1")
                draw("WinConBoxLayer", "Canvas/Layer/bg0/scrollview/box3", "sliced-sprite", 406.686f, 170.5f, 803f, 543f, "box2")
                draw("WinConBoxLayer", "Canvas/Layer/bg0/scrollview/view/content/item", "label", 409.359f, 520.887f, 803f, 191.36f,
                    text = text, blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
                draw("WinConBoxLayer", "Canvas/Layer/bg0/button/Background", "sliced-sprite", 957.134f, 88.204f, 256.7f, 60f, "box3")
                draw("WinConBoxLayer", "Canvas/Layer/bg0/button/Background/Label", "label", 985.869f, 93.461f, 199.23f, 54.4f,
                    text = "짐이 알겠다.", blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
            } else {
                val view = requireNotNull(scriptWinConditions).view()
                val rich1 = view.first
                val rich2 = view.second
                val texts = listOf("승리 조건", "장보와 장량을", "격퇴하십시오.", "제한 턴 수 ${scenarioMaxRound()}")
                val widths = listOf(367.43f, 537.49f, 537.49f, 531.39f)
                draw("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", 80f / 255f)
                draw("HallLayer", "Canvas/Layer/richtext1", "rich-text", 39.467f, 260.08f, 537.49f, 511.2f,
                    text = rich1, blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
                texts.forEachIndexed { index, text -> draw("HallLayer", "Canvas/Layer/richtext1/RICHTEXT_CHILD", "label", 39.467f, 620.08f - index * 120f, widths[index], 151.2f,
                    text = text, blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")) }
                draw("HallLayer", "Canvas/Layer/richtext2", "rich-text", 27.323f, 267.913f, 537.49f, 511.2f,
                    text = rich2, blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
                texts.forEachIndexed { index, text -> draw("HallLayer", "Canvas/Layer/richtext2/RICHTEXT_CHILD", "label", 27.323f, 627.913f - index * 120f, widths[index], 151.2f,
                    text = text, blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")) }
            }
            return log.jsonl()
        }
        val chromeLayer = if (itemUpgradeRouteState != null) "ItemUpgradeLayer" else "BattleScreen"
        draw(chromeLayer, "Canvas/Layer/bg/button/Background", "sliced-sprite", .843f, .731f, 68f, 68f, "bg1")
        draw(chromeLayer, "Canvas/Layer/bg/button/Background/tool11", "sprite", .043f, -.069f, 69.6f, 69.6f, "tool10")
        draw(chromeLayer, "Canvas/Layer/bg/btn/Background", "sliced-sprite", 1418.3721f, 730f, 70f, 70f, "bg1")
        draw(chromeLayer, "Canvas/Layer/bg/btn/Background/tool11", "sprite", 1418.5721f, 730.2f, 69.6f, 69.6f, "tool11")
        if (itemUpgradeRouteState != null) {
            appendItemUpgradeRenderEvents(log, phase, requireNotNull(itemUpgradeFlow))
            draw(chromeLayer, "Canvas/Layer/bg", "sprite", 0f, 0f, 1488.3721f, 800f,
                "assets/resources/native/59/5961a224-35cd-4838-b67a-a072b0b31ca4.14b27.jpg#Logo_5-1")
            draw(chromeLayer, "Canvas/Layer/bg/label0", "label", 431.986f, 301.8f, 644.4f, 176.4f,
                text = "영천의 전투", blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
            draw(chromeLayer, "Canvas/Layer/bg/label1", "label", 421.986f, 311.8f, 644.4f, 176.4f,
                text = "영천의 전투", blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
            return log.jsonl()
        }
        draw("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.3721f, 800f, "default_sprite_splash", 50f / 255f)

        rewardFlow?.let { appendRewardRenderEvents(log, phase, it) }
        draw(
            "BattleScreen", "Canvas/Layer/bg", "sprite", 0f, 0f, 1488.3721f, 800f,
            "assets/resources/native/59/5961a224-35cd-4838-b67a-a072b0b31ca4.14b27.jpg#Logo_5-1",
        )
        draw("BattleScreen", "Canvas/Layer/bg/label0", "label", 431.986f, 301.8f, 644.4f, 176.4f,
            text = "영천의 전투", blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
        draw("BattleScreen", "Canvas/Layer/bg/label1", "label", 421.986f, 311.8f, 644.4f, 176.4f,
            text = "영천의 전투", blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"))
        return log.jsonl()
    }

    private fun appendItemUpgradeRenderEvents(log: RenderEventLog, phase: String, flow: ItemUpgradeFlow) {
        val layer = "ItemUpgradeLayer"
        val sprites = listOf(770, 771)
        val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun event(path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                  asset: String? = null, text: String = "") =
            log.draw(phase, layer, path, type, x, y, w, h, asset, 1f,
                if (type == "label") labels else sprites, true, text)
        event("Canvas/Layer/bg", "tiled-sprite", 544.186f, 270.5f, 400f, 259f, "Logo_9-1")
        event("Canvas/Layer/bg/box3", "sliced-sprite", 544.186f, 270.5f, 400f, 259f, "box3")
        event("Canvas/Layer/bg/box2", "sliced-sprite", 551.222f, 451.34f, 70f, 70f, "box2")
        event("Canvas/Layer/bg/box2/item", "sprite", 554.222f, 454.34f, 64f, 64f,
            "${gameDataCatalog.equipmentProfile(flow.request.itemId)?.icon ?: 1}-1")
        event("Canvas/Layer/bg/label0", "label", 628.186f, 470.8f, 69.2f, 50.4f, text = flow.itemName)
        event("Canvas/Layer/bg/label1", "label", 908.186f, 470.8f, 22.25f, 50.4f, text = flow.request.newLevel.toString())
        event("Canvas/Layer/bg/label", "label", 861.186f, 470.8f, 42.25f, 50.4f, text = "Lv")
        event("Canvas/Layer/bg/label2", "label", 624.386f, 415.9f, 189.3f, 50.4f, text = flow.ownerName)
        event("Canvas/Layer/bg/label", "label", 815.347f, 415.934f, 69.2f, 50.4f, text = "장비")
        event("Canvas/Layer/bg/scrollview", "sliced-sprite", 553.136f, 281.5f, 379.5f, 130.4f, "box2")
        event("Canvas/Layer/bg/scrollview/view/content/label", "label", 554.836f, 361.5f, 379.5f, 50.4f,
            text = "${flow.attributeName} ${flow.request.oldValue} -> ${flow.request.newValue}")
    }

    private fun roundRenderEventLog(): String {
        val layer = activeRoundLayer ?: return RenderEventLog().jsonl()
        val mode = roundRouteState?.removePrefix("battle-round-")?.removeSuffix("-fixture") ?: "normal"
        val phase = "battle-round-$mode"
        val log = RenderEventLog(); val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun sprite(path:String,x:Float,y:Float,w:Float,h:Float,asset:String,opacity:Float=1f) =
            log.draw(phase,"HallLayer",path,"sprite",x,y,w,h,asset,opacity,listOf(770,771),true,"")
        fun label(path:String,text:String,x:Float,y:Float,w:Float,h:Float) =
            log.draw(phase,"HallLayer",path,"label",x,y,w,h,null,1f,labels,true,text)
        sprite("Canvas/Layer/ScrollView/view/content/map",-320f,-96f,1920f,1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>")
        sprite("Canvas/Layer/Panel_cancel",0f,0f,1488.372f,800f,"default_sprite_splash",80f/255f)
        val view=layer.view
        if(view.roundLabelsVisible) {
            label("Canvas/Layer/label02","아군 단계",526.713f,380.09f,448.54f,151.2f)
            label("Canvas/Layer/label01","아군 단계",519.916f,385.09f,448.54f,151.2f)
            val width=if(view.roundText=="최종 턴")344.74f else 274.34f
            val shadowX=if(view.roundText=="최종 턴")578.613f else 613.813f
            label("Canvas/Layer/label12",view.roundText,shadowX,247.7f,width,151.2f)
            label("Canvas/Layer/label11",view.roundText,shadowX-6.797f,252.7f,width,151.2f)
        } else if(view.campLabelsVisible) {
            label("Canvas/Layer/label22","적군 단계",526.713f,319.4f,448.54f,151.2f)
            label("Canvas/Layer/label21","적군 단계",519.916f,324.4f,448.54f,151.2f)
        }
        return log.jsonl()
    }

    private fun usePropertyRenderEventLog(): String {
        val phase = when (usePropertyRouteState) {
            "battle-use-property-detail-fixture" -> "battle-use-property-detail"
            "battle-use-property-select-fixture" -> "battle-use-property-select"
            "battle-use-property-cancel-fixture" -> "battle-use-property-cancel"
            else -> "battle-use-property-list"
        }
        val log = RenderEventLog(); val sprites = listOf(770,771); val labels = listOf("SRC_ALPHA","ONE_MINUS_SRC_ALPHA")
        fun event(layer:String,path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String?=null,text:String="",opacity:Float=1f) =
            log.draw(phase,layer,path,type,x,y,w,h,asset,opacity,if(type=="label") labels else sprites,true,text)
        fun sprite(path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String,owner:String="UsePropertyLayer",opacity:Float=1f)=
            event(owner,path,type,x,y,w,h,asset,opacity=opacity)
        fun label(path:String,text:String,x:Float,y:Float,w:Float,h:Float=50.4f,owner:String="UsePropertyLayer")=
            event(owner,path,"label",x,y,w,h,text=text)
        sprite("Canvas/Layer/ScrollView/view/content/map","sprite",-320f,-96f,1920f,1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>","HallLayer")
        val list = usePropertyLayer ?: return log.jsonl()
        sprite("Canvas/Layer/Panel_cancel","sprite",0f,0f,1488.372f,800f,"default_sprite_splash","HallLayer",40f/255f)
        sprite("Canvas/Layer/bg","tiled-sprite",795.536f,390f,491f,410f,"Logo_9-1")
        sprite("Canvas/Layer/bg/box3","sliced-sprite",795.536f,390f,491f,410f,"box1")
        sprite("Canvas/Layer/bg/box2","sliced-sprite",799.536f,448f,483f,348f,"box2")
        list.rows.forEachIndexed { index,item ->
            val y=681f-index*112f; val path="Canvas/Layer/bg/box2/scrollview/view/content/item0"
            sprite(path,"sliced-sprite",803.536f,y,475f,110f,"box3")
            sprite("$path/box2","sliced-sprite",809.55f,y+5f,100f,100f,"box2")
            sprite("$path/box2/icon","sprite",814.55f,y+10f,90f,90f,"Game/Item2/${item.icon}-1")
            label("$path/label0",item.name,912.036f,y+56.8f,191.5f)
            label("$path/label","효능: ",912.036f,y+4.8f,91.43f)
            label("$path/label1",item.typeName,1015.631f,y+3.915f,135.88f)
            label("$path/label","인벤토리: ",1108.272f,y+56.8f,160.63f)
            label("$path/label2",item.count.toString(),1249.503f,y+56.8f,22.25f)
        }
        sprite("Canvas/Layer/bg/button/Background","sliced-sprite",1131.145f,394.896f,150f,50f,"box3")
        label("Canvas/Layer/bg/button/Background/Label","취소",1156.145f,403.896f,100f,40f)
        val selected=usePropertyDetail ?: return log.jsonl()
        val profile=gameDataCatalog.equipmentProfile(selected.id) ?: return log.jsonl()
        sprite("Canvas/Layer/Panel_cancel","sprite",0f,0f,1488.372f,800f,"default_sprite_splash","HallLayer",.392f)
        sprite("Canvas/Layer/bg1","tiled-sprite",253.186f,80f,982f,640f,"Logo_9-1")
        sprite("Canvas/Layer/bg1/box2","sliced-sprite",253.186f,80f,982f,640f,"box3")
        label("Canvas/Layer/bg1/label0",selected.name,420.186f,658.8f,203.1f)
        sprite("Canvas/Layer/bg1/bg4","sliced-sprite",265.778f,564.802f,144f,144f,"box2")
        sprite("Canvas/Layer/bg1/bg4/icon","sprite",273.778f,572.802f,128f,128f,"Game/Item2/${selected.icon}-1")
        sprite("Canvas/Layer/bg1/bg0","sliced-sprite",420.536f,498.55f,343.5f,100.9f,"box1")
        label("Canvas/Layer/bg1/bg0/label","속성:",432.137f,548.543f,80.31f)
        label("Canvas/Layer/bg1/bg0/label0","아이템",522.525f,548.543f,103.8f)
        label("Canvas/Layer/bg1/bg0/label","가격:",432.137f,503.543f,80.31f)
        label("Canvas/Layer/bg1/bg0/label1",gameDataCatalog.purchasePrice(profile).toString(),522.525f,503.543f,66.74f)
        sprite("Canvas/Layer/bg1/bg1","sliced-sprite",261.686f,92.5f,501f,377f,"box1")
        sprite("Canvas/Layer/bg1/bg1/bg1","sprite",470.286f,447.7f,83.8f,40f,"bg1")
        label("Canvas/Layer/bg1/bg1/bg1/label","효과",477.586f,442.5f,69.2f)
        label("Canvas/Layer/bg1/bg1/scrollview/view/content/label",selected.typeName,265.686f,389.966f,493f,55.44f)
        sprite("Canvas/Layer/bg1/bg2","sliced-sprite",770.186f,157.5f,448f,247f,"box2")
        sprite("Canvas/Layer/bg1/bg2/bg1","sprite",943.336f,369.55f,89.7f,40.9f,"bg1")
        label("Canvas/Layer/bg1/bg2/bg1/label","설명",953.586f,378.8f,69.2f)
        label("Canvas/Layer/bg1/bg2/scrollview/view/content/label",profile.intro,774.186f,191.26f,440f,187.44f)
        sprite("Canvas/Layer/bg1/bg3","sliced-sprite",770.186f,427f,448f,260f,"box1")
        sprite("Canvas/Layer/bg1/bg3/bg1","sprite",871.686f,664.273f,245f,45f,"bg1")
        label("Canvas/Layer/bg1/bg3/bg1/label","장착 가능한 부대입니다.",804.516f,661.573f,379.34f)
        fun measuredWidth(value:String)=value.count{it!=' '}*27.68f+value.count{it==' '}*8.89f
        repeat(13) { row ->
            val y=609.55f-row*52f; val path="Canvas/Layer/bg1/bg3/scrollview/view/content/item"
            sprite(path,"sliced-sprite",772.186f,y,444f,50f,if(row%2==0)"885a69b4-08ed-4c78-8896-ffb04eb2bd20" else "bg2")
            repeat(3) { col ->
                val value=gameDataCatalog.postsName(row*3+col); val width=measuredWidth(value)
                val center=when(col){0->851.186f;1->994.186f;else->1138.186f}
                label("$path/label$col",value,center-width/2f,y+4.84f,width,40.32f)
            }
        }
        sprite("Canvas/Layer/bg1/button1/Background","sliced-sprite",1065.827f,97.824f,150f,50f,"box3")
        label("Canvas/Layer/bg1/button1/Background/Label","확인",1090.827f,104.824f,100f,40f)
        return log.jsonl()
    }

    private fun magickRenderEventLog(): String {
        val list=magickListLayer?:return RenderEventLog().jsonl()
        val phase=if(magickRouteState=="battle-magick-detail-fixture")"battle-magick-list-detail" else "battle-magick-list-list"
        val log=RenderEventLog();val sprites=listOf(770,771);val labels=listOf("SRC_ALPHA","ONE_MINUS_SRC_ALPHA")
        fun draw(layer:String,path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String?=null,text:String="",opacity:Float=1f)=
            log.draw(phase,layer,path,type,x,y,w,h,asset,opacity,if(type=="label")labels else sprites,true,text)
        fun sprite(path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String,layer:String="MagickListLayer",opacity:Float=1f)=draw(layer,path,type,x,y,w,h,asset,opacity=opacity)
        fun label(path:String,text:String,x:Float,y:Float,w:Float,h:Float=50.4f)=draw("MagickListLayer",path,"label",x,y,w,h,text=text)
        sprite("Canvas/Layer/ScrollView/view/content/map","sprite",-320f,-96f,1920f,1920f,"assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>","HallLayer")
        sprite("Canvas/Layer/Panel_cancel","sprite",0f,0f,1488.372f,800f,"default_sprite_splash","HallLayer",.157f)
        sprite("Canvas/Layer/bg0","tiled-sprite",474.186f,90.5f,540f,619f,"Logo_9-1")
        sprite("Canvas/Layer/bg0/bg","tiled-sprite",474.186f,90.5f,540f,619f,"box3")
        label("Canvas/Layer/bg0/label0","허자장",495.586f,652.8f,173f)
        label("Canvas/Layer/bg0/label","MP",681.186f,652.807f,60f)
        sprite("Canvas/Layer/bg0/progressBar0","sliced-sprite",741.186f,661.207f,204f,24f,"default_progressbar_bg")
        sprite("Canvas/Layer/bg0/progressBar0/bar","sliced-sprite",743.186f,663.207f,82.759f,20f,"Mark_1-1")
        sprite("Canvas/Layer/bg0/progressBar1/bar","sliced-sprite",743.186f,663.207f,82.759f,20f,"Mark_2-1")
        label("Canvas/Layer/bg0/progressBar1/label","24/58",793.136f,653.8f,100.1f)
        sprite("Canvas/Layer/bg0/box2","sliced-sprite",478.186f,150.5f,532f,499f,"box2")
        val widths=listOf(218.71f,138.4f,103.8f,69.2f,69.2f,149.51f,69.2f,103.8f,69.2f,149.51f)
        list.rows.take(10).forEachIndexed{index,magic->
            val col=index%2;val line=index/2;val x=480.186f+264f*col;val y=505.5f-142f*line;val lx=x+92f
            val root="Canvas/Layer/bg0/box2/scrollview/view/content/item"
            sprite(root,"sliced-sprite",x,y,262f,140f,"box3")
            sprite("$root/skill_0","sprite",x+5.073f,y+57.383f,76.8f,76.8f,"Game/Magic/${magic.icon+1}-1")
            label("$root/label0",magic.name,lx,y+86.8f,widths[index])
            label("$root/label","MP：",lx,y+45.8f,94.6f)
            val costWidth=if(magic.cost<10)22.25f else 44.49f
            label("$root/label2",magic.cost.toString(),x+175.879f,y+45.8f,costWidth)
            // The source prefab uses this label for every battle magic row;
            // null power would be rendered as "없음" instead of the fixture's 0.28.
            if(index<8) {
                label("$root/label","피해 계수: ",x+2.097f,y+4.8f,171.74f)
                label("$root/label1",magic.power?.let{(it/100f).toString()}?:"없음",x+179.637f,y+4.8f,77.85f)
            }
        }
        sprite("Canvas/Layer/bg0/button/Background","sliced-sprite",775.892f,97.683f,180f,50f,"box3")
        label("Canvas/Layer/bg0/button/Background/Label","취소",815.892f,105.683f,100f,40f)
        magickInfoLayer?.magic?.let{magic->
            sprite("Canvas/Layer/Panel_cancel","sprite",0f,0f,1488.372f,800f,"default_sprite_splash","HallLayer",.392f)
            sprite("Canvas/Layer/bg1","tiled-sprite",452.686f,130f,583f,540f,"Logo_9-1")
            sprite("Canvas/Layer/bg1/box2","sliced-sprite",452.686f,130f,583f,540f,"box3")
            label("Canvas/Layer/bg1/label",magic.name,577.509f,604.008f,218.71f)
            sprite("Canvas/Layer/bg1/skill_0","sprite",478.186f,562f,80f,80f,"Game/Magic/${magic.icon+1}-1")
            sprite("Canvas/Layer/bg1/bg0","sliced-sprite",465.636f,434f,340.3f,100f,"box1")
            label("Canvas/Layer/bg1/bg0/label","위력:",476.336f,479.826f,80.31f)
            label("Canvas/Layer/bg1/bg0/label0","${magic.power?:0}%",566.719f,480.13f,80.06f)
            label("Canvas/Layer/bg1/bg0/label","MP 소모:",470.776f,436.826f,151.43f)
            label("Canvas/Layer/bg1/bg0/label1",magic.cost.toString(),627.053f,436.675f,22.25f)
            sprite("Canvas/Layer/bg1/bg1","sliced-sprite",465.636f,147f,340.3f,274f,"box2")
            label("Canvas/Layer/bg1/bg1/scrollview/view/content/label",magic.intro,470.786f,187.114f,330f,231.44f)
            sprite("Canvas/Layer/bg1/bg2","sliced-sprite",814.213f,436.061f,200f,200f,"box1")
            sprite("Canvas/Layer/bg1/bg2/bg","sliced-sprite",830.713f,614.117f,167f,40f,"bg1")
            label("Canvas/Layer/bg1/bg2/bg/label","가능 범위",839.654f,611.005f,149.51f)
            sprite("Canvas/Layer/bg1/bg2/img","sprite",834.213f,450.755f,160f,160f,"Game/Hitarea/${magic.hit+1}-1")
            sprite("Canvas/Layer/bg1/bg3","sliced-sprite",814.213f,204.673f,200f,200f,"box1")
            sprite("Canvas/Layer/bg1/bg3/bg","sliced-sprite",831.713f,384.673f,165f,40f,"bg1")
            label("Canvas/Layer/bg1/bg3/bg/label","영향 범위",839.654f,381.561f,149.51f)
            sprite("Canvas/Layer/bg1/bg3/img","sprite",834.213f,219.367f,160f,160f,"Game/Effarea/${magic.eff+1}-1")
            sprite("Canvas/Layer/bg1/button/Background","sliced-sprite",874.764f,144.022f,147.6f,50f,"box3")
            label("Canvas/Layer/bg1/button/Background/Label","확인",898.564f,152.022f,100f,40f)
        }
        return log.jsonl()
    }

    private fun jiqiRenderEventLog(): String {
        val layer = jiqiLayer ?: return RenderEventLog().jsonl()
        val log = RenderEventLog()
        val sprites = listOf(770, 771)
        val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun draw(path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                 asset: String? = null, text: String = "", opacity: Float = 1f, owner: String = "JiQiLayer") =
            log.draw("battle-jiqi-stable", owner, path, type, x, y, w, h, asset, opacity,
                if (type == "label") labels else sprites, true, text)
        draw("Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>", owner = "HallLayer")
        draw("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .157f, owner = "HallLayer")
        draw("Canvas/Layer/bg", "tiled-sprite", 405.686f, 234.5f, 677f, 331f, "Logo_9-1")
        draw("Canvas/Layer/bg/box3", "sliced-sprite", 405.686f, 234.5f, 677f, 331f, "box3")
        listOf(
            Triple("명중률: ", floatArrayOf(479.171f, 487.8f, 126.03f), "label"),
            Triple("방어율:", floatArrayOf(485.057f, 424.839f, 114.91f), "label"),
            Triple("쌍타율:", floatArrayOf(484.731f, 360.8f, 114.91f), "label"),
            Triple("이중 타격률:", floatArrayOf(424.571f, 297.8f, 195.23f), "label"),
        ).forEach { (text, p, node) -> draw("Canvas/Layer/bg/$node", "label", p[0], p[1], p[2], 50.4f, text = text) }
        val left = listOf(487.8f, 424.8f, 360.8f, 297.8f)
        left.forEachIndexed { index, y -> draw("Canvas/Layer/bg/label$index", "label", 625.186f, y, 44.49f, 50.4f, text = layer.rates[index].toString()) }
        listOf(7 to 306.8f, 6 to 366.8f, 5 to 427.8f).forEach { (index, y) ->
            draw("Canvas/Layer/bg/label$index", "label", 978.186f, y, 44.49f, 50.4f, text = layer.rates[index].toString())
        }
        draw("Canvas/Layer/bg/label", "label", 753.016f, 487.8f, 206.34f, 50.4f, text = "마법 명중률: ")
        draw("Canvas/Layer/bg/label4", "label", 978.186f, 487.8f, 44.49f, 50.4f, text = layer.rates[4].toString())
        draw("Canvas/Layer/bg/label", "label", 738.416f, 306.8f, 275.54f, 50.4f, text = "피격 시 치명타율:")
        draw("Canvas/Layer/bg/label", "label", 821.431f, 370.8f, 149.51f, 50.4f, text = "치명타율:")
        draw("Canvas/Layer/bg/label", "label", 753.016f, 433.8f, 206.34f, 50.4f, text = "마법 방어율: ")
        return log.jsonl()
    }

    private fun appendRewardRenderEvents(log: RenderEventLog, phase: String, flow: BattleRewardFlow) {
        fun label(path: String, x: Float, y: Float, w: Float, h: Float, text: String) =
            log.draw(phase, "BattleScreen", path, "label", x, y, w, h, opacity = 1f,
                blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"), visible = true, text = text)
        when (flow.phase) {
            BattleRewardFlow.Phase.MONEY -> {
                label("Canvas/Layer/bg0/label", 527.747f, 464.417f, 448.54f, 151.2f, "전투 종료")
                label("Canvas/Layer/bg0/label", 519.916f, 476.394f, 448.54f, 151.2f, "전투 종료")
                label("Canvas/Layer/bg0/label", 282.777f, 248.492f, 311.4f, 151.2f, "보상금")
                label("Canvas/Layer/bg0/label", 274.533f, 254.4f, 311.4f, 151.2f, "보상금")
                val money = flow.reward.money.toString()
                label("Canvas/Layer/bg0/label02", 967.617f, 247.807f, 200.21f, 151.2f, money)
                label("Canvas/Layer/bg0/label01", 958.035f, 254.4f, 200.21f, 151.2f, money)
                val stars = (0 until 3).joinToString("  ") { if (flow.reward.flag and (1 shl it) != 0) "★" else "☆" }
                label("Canvas/Layer/bg0/label12", 531.389f, 52.817f, 444.76f, 151.2f, stars)
                label("Canvas/Layer/bg0/label11", 521.806f, 56.113f, 444.76f, 151.2f, stars)
            }
            BattleRewardFlow.Phase.ITEMS -> {
                label("Canvas/Layer/bg1/label", 596.73f, 574.944f, 311.4f, 151.2f, "전리품")
                label("Canvas/Layer/bg1/label", 588.486f, 587.942f, 311.4f, 151.2f, "전리품")
                flow.reward.itemIds.take(flow.visibleItemCount).take(3).forEachIndexed { index, id ->
                    val y = 433.5f - index * 157f
                    val root = "Canvas/Layer/bg1/item$index"
                    log.draw(phase, "BattleScreen", root, "tiled-sprite", 499.686f, y, 489f, 101f,
                        "Mark_47-1", visible = true)
                    log.draw(phase, "BattleScreen", "$root/box3", "sliced-sprite", 499.686f, y, 489f, 101f,
                        "box3", visible = true)
                    val profile = gameDataCatalog.equipmentProfile(id)
                    log.draw(phase, "BattleScreen", "$root/icon", "sprite", 534.974f, y + 18.5f, 64f, 64f,
                        "${profile?.icon ?: id}-1", visible = true)
                    label("$root/label", 648.999f, y + 22.78f, 164.46f, 55.44f, profile?.name ?: "아이템 $id")
                }
            }
            BattleRewardFlow.Phase.END, BattleRewardFlow.Phase.COMPLETE -> Unit
        }
    }

    private fun installBattleCharacterRoute() {
        battleCharacterRouteInstalled = true
        fun unit(characterId: Int) = battle.units.values.firstOrNull { it.characterId == characterId && battleAvatarId(it) != null }
            ?: error("battle-character fixture requires source unit $characterId")
        fun state(unit: BattleUnit, camp: BattleCharacterCamp, maxHp: Int, hp: Int) = BattleCharacterPresentation(unit.id, camp, maxHp, hp)
        fun sample(unit: BattleUnit, value: BattleCharacterPresentation, x: Float, y: Float, asset: String,
                   time: Float = .1f, width: Float = 96f, height: Float = 96f,
                   offsetX: Float = 0f, offsetY: Float = 0f, harmRect: FloatArray? = null,
                   frameDirection: Int = 3) =
            BattleCharacterRouteSample(unit, value, x, y, time, asset, width, height, offsetX, offsetY, harmRect, frameDirection)
        val u210 = unit(210); val u211 = unit(211); val u234 = unit(234); val u235 = unit(235)
        val mov11 = "assets/Game/native/19/19ac1287-4d09-45f4-bf9a-f5eb8b21795c.89d84.png#33632304"
        val mov20 = "assets/Game/native/3f/3f8fbf89-4dd0-4d0b-88e0-9c7927fe5693.3f9c2.png#67186736"
        battleCharacterRouteSamples = when (requireNotNull(battleCharacterRouteState)) {
            BattleCharacterStrictState.HP_CAMPS_PARTIAL -> listOf(
                sample(u235, state(u235, BattleCharacterCamp.FAMOUS_ENEMY, 96, 29), 352f, 192f, mov20),
                // setDirFast(3) followed by the default presentation action is an authored
                // no-op when action0 already loops. These two actors retain
                // their previously playing anime0_0 clip while their logical
                // direction field becomes 3 (confirmed by the source state).
                sample(u210, state(u210, BattleCharacterCamp.MINE, 119, 89), 640f, 96f, mov11, frameDirection = 0),
                sample(u234, state(u234, BattleCharacterCamp.ENEMY, 96, 43), 832f, 96f, mov20),
                sample(u211, state(u211, BattleCharacterCamp.FRIEND, 119, 71), 544f, 0f, mov11, frameDirection = 0),
            )
            BattleCharacterStrictState.OUTLINE_HIGHLIGHT -> listOf(
                sample(u210, state(u210, BattleCharacterCamp.MINE, 119, 119).also { it.beginAttack() }, 640f, 96f,
                    "assets/Game/native/dc/dcad67fe-5825-49d1-b6e2-ce5356f376e4.b8507.png#134234176", width = 128f, height = 128f, offsetX = 16f),
                sample(u211, state(u211, BattleCharacterCamp.MINE, 119, 119).also { it.beginAttack(); it.animationMaterialEvent(116) }, 544f, 0f,
                    "assets/Game/native/dc/dcad67fe-5825-49d1-b6e2-ce5356f376e4.b8507.png#134234176", width = 128f, height = 128f, offsetX = 16f),
            )
            BattleCharacterStrictState.HIT_IMPACT -> listOf(sample(u210,
                state(u210, BattleCharacterCamp.ENEMY, 119, 113).also { it.hitImpact(30); it.animationMaterialEvent(110) },
                640f, 96f, "50475056", offsetX = 16f, harmRect = floatArrayOf(611.3f, 159.76f, 57.4f, 64.48f)))
            BattleCharacterStrictState.CLEANUP -> listOf(sample(u210,
                state(u210, BattleCharacterCamp.ENEMY, 119, 113).also { it.hitImpact(30); it.finishHit() }, 640f, 96f, mov11, frameDirection = 0))
            BattleCharacterStrictState.DEATH_ACTION -> listOf(sample(u210,
                state(u210, BattleCharacterCamp.ENEMY, 119, 119).also { it.beginHide(BattleHideType.SI_WANG) },
                640f, 96f, "assets/Game/native/19/19ac1287-4d09-45f4-bf9a-f5eb8b21795c.89d84.png#151072816"))
            BattleCharacterStrictState.DEATH_HIDDEN -> listOf(sample(u210,
                state(u210, BattleCharacterCamp.ENEMY, 119, 119).also { it.beginHide(BattleHideType.SI_WANG); it.finishHide() },
                640f, 96f, mov11))
        }
    }

    private fun battleCharacterFrame(sample: BattleCharacterRouteSample): UnitSpriteFrame =
        battleSpriteFrame(sample.state.action, sample.frameDirection, sample.frameTime, loop = sample.state.action == 0) ?: idleSpriteFrame(sample.unit)

    private fun battleCharacterCommands(sample: BattleCharacterRouteSample): List<BattleCharacterDrawEvent> {
        val frame = battleCharacterFrame(sample)
        return BattleCharacterStateRenderer.commands(sample.state, sample.unitLeft, sample.unitBottom, sample.assetFrameId,
            avatarWidth = sample.avatarWidth, avatarHeight = sample.avatarHeight,
            avatarOffsetX = sample.avatarOffsetX, avatarOffsetY = sample.avatarOffsetY,
            avatarSourceRect = listOf(0, frame.sourceY, frame.sourceWidth, frame.sourceHeight),
            avatarFlipX = frame.flipX, avatarFlipY = false).map { event ->
                if (event.drawType == "label" && sample.harmRect != null) event.copy(
                    x = sample.harmRect[0], y = sample.harmRect[1], width = sample.harmRect[2], height = sample.harmRect[3],
                    blend = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA"),
                ) else event
            }
    }

    private fun battleCharacterRouteRenderEventLog(): String {
        val events = buildList {
            add(BattleCharacterDrawEvent("Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
                "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>", materialId = "builtin-2d-sprite (Instance)"))
            battleCharacterRouteSamples.forEach { addAll(battleCharacterCommands(it)) }
        }
        return BattleCharacterStateRenderer.jsonl(requireNotNull(battleCharacterRouteState), events)
    }

    private fun drawBattleCharacterRoute() {
        battleCharacterRouteSamples.forEach { sample ->
            val frame = battleCharacterFrame(sample); val commands = battleCharacterCommands(sample)
            val avatar = commands.firstOrNull() ?: return@forEach
            val texture = when (frame.source) { UnitSpriteSource.ATTACK -> attackTexture(sample.unit) ?: unitTexture(sample.unit); UnitSpriteSource.SPECIAL -> specialTexture(sample.unit) ?: unitTexture(sample.unit); UnitSpriteSource.MOVEMENT -> unitTexture(sample.unit) }
            texture?.let { atlas ->
                fun spriteAt(x: Float, y: Float) = batch.draw(atlas, x, y, avatar.width, avatar.height, 0,
                    if (frame.sourceY + frame.sourceHeight > atlas.height) 0 else frame.sourceY,
                    minOf(frame.sourceWidth, atlas.width), minOf(frame.sourceHeight, atlas.height), frame.flipX, false)
                when (sample.state.material) {
                    BattleCharacterMaterial.HIGHLIGHT -> { batch.flush(); batch.shader = cocosHighlightSampler.value; cocosHighlightSampler.value.setUniformf("u_value", sample.state.materialValue ?: 0f); spriteAt(avatar.x, avatar.y); batch.flush(); batch.shader = null }
                    BattleCharacterMaterial.OUTLINE -> { batch.color = Color.CYAN; listOf(-2f to 0f, 2f to 0f, 0f to -2f, 0f to 2f).forEach { (dx, dy) -> spriteAt(avatar.x + dx, avatar.y + dy) }; batch.color = Color.WHITE; spriteAt(avatar.x, avatar.y) }
                    else -> spriteAt(avatar.x, avatar.y)
                }
            }
            commands.drop(1).forEach { command -> when (command.drawType) {
                "sliced-sprite" -> when (sample.state.camp) { BattleCharacterCamp.MINE -> hudAssets.mineHpBarTexture; BattleCharacterCamp.FRIEND -> hudAssets.friendHpBarTexture; BattleCharacterCamp.ENEMY -> hudAssets.enemyHpBarTexture; BattleCharacterCamp.FAMOUS_ENEMY -> hudAssets.famousEnemyHpBarTexture }?.let { batch.draw(it, command.x, command.y, command.width, command.height) }
                "label" -> {
                    val outline = command.outlineRgb ?: 0; font.data.setScale(.5f); font.color = Color((outline shr 16 and 255) / 255f, (outline shr 8 and 255) / 255f, (outline and 255) / 255f, 1f)
                    val baseline = command.y + command.height; listOf(-1f to 0f, 1f to 0f, 0f to -1f, 0f to 1f).forEach { (dx, dy) -> font.draw(batch, command.text.orEmpty(), command.x + dx, baseline + dy) }
                    val rgb = command.colorRgb ?: 0xffffff; font.color = Color((rgb shr 16 and 255) / 255f, (rgb shr 8 and 255) / 255f, (rgb and 255) / 255f, 1f); font.draw(batch, command.text.orEmpty(), command.x, baseline); font.data.setScale(1f); font.color = Color.WHITE
                }
            } }
        }
    }

    private fun nextScenario(): String {
        scriptRuntime.stage.sceneJumpStage?.let { targetStage ->
            // An even resolved Model stage enters Hall R(stage / 2).
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
        // Source _loadBg assigns the JSON width/height before Layout sizes the
        // map sprite. _countPos therefore uses the loaded map dimensions, not
        // the 20x20 S_00 dimensions. Preserve the isolated reward oracle, but
        // derive every live battle coordinate from its actual map geometry.
        boardLeft = SourceBattleMapGeometry.boardLeft(terrainGrid.width, battleCamera.x)
        boardBottom = if (rewardRouteState != null) 1264f + battleCamera.y else
            SourceBattleMapGeometry.boardBottom(terrainGrid.height, battleCamera.y)
        boardMaxX = (terrainGrid.width - 1).coerceAtLeast(1)
        boardMaxY = (terrainGrid.height - 1).coerceAtLeast(1)
        boardTile = 96f
        // The source's raw framebuffer identifies Cocos WebGL as quantizing
        // bilinear weights to 8-bit steps. Scope this shader strictly to the
        // map quad; every other battle sprite retains SpriteBatch defaults.
        val useCocos8Sampler = game.requestedCocos8MapSampler()
        // Both the isolated-map oracle and the live S_00 SayLayer oracle
        // resolve Cocos UVs at physical framebuffer pixel centres.  The
        // latter also covers ScrollView/camera composition, so the same rule
        // is now the source-faithful default for every Cocos8 map draw.
        val useFragmentCoordinates = useCocos8Sampler && game.requestedFragmentCoordinateMapSampler()
        val samplerTexture = if (useCocos8Sampler) mapTexture else null
        val priorSamplerFilter = samplerTexture?.let { it.minFilter to it.magFilter }
        if (useCocos8Sampler) {
            samplerTexture?.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            batch.shader = cocos8MapSampler.value
        }
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        // Cocos' RenderTexture uses ordinary source-over factors for alpha
        // too. In particular, the translucent SayLayer panel reduces the
        // captured destination alpha to 202 rather than preserving 255.
        // Keep the same equation for RGB and alpha instead of forcing the
        // alpha source factor to ONE.
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        mapTexture?.let { texture ->
            batch.color = Color.WHITE
            // Cocos' Texture2D sprite coordinates keep the JPEG's top edge
            // at the map's top; LibGDX's raw texture V origin is opposite.
            // The map-only raw fixture deliberately resets ScrollView/content
            // to y=0, producing its isolated [-320,-560] quad. In the live
            // Battle SayLayer, source CDP observes content.y=464 and map
            // world centre=(640,864), so the full-scene quad is
            // [-320,-96]..[1600,1824]. Do not leak the isolation mutation
            // into ordinary battle rendering.
            // Reward is entered through the source battle's unscrolled
            // content transform, the same transform used by the isolated
            // tactical map.  Keep this a live-route state, not a logger-only
            // coordinate substitution.
            val cameraX = if (mapOnlyCapture || rewardRouteState != null) 0f else battleCamera.x
            val cameraY = if (mapOnlyCapture || rewardRouteState != null) 0f else battleCamera.y
            val mapBottom = if (mapOnlyCapture || rewardRouteState != null) -560f + cameraY else
                SourceBattleMapGeometry.mapBottom(terrainGrid.height, cameraY)
            val mapWidth = terrainGrid.width * boardTile
            val mapHeight = terrainGrid.height * boardTile
            val mapLeft = if (mapOnlyCapture || rewardRouteState != null) -320f + cameraX else
                SourceBattleMapGeometry.boardLeft(terrainGrid.width, cameraX)
            val (sampleOffsetX, sampleOffsetY) = game.requestedMapSampleOffset()
            if (useCocos8Sampler) {
                cocos8MapSampler.value.setUniformf("u_texSize", texture.width.toFloat(), texture.height.toFloat())
                cocos8MapSampler.value.setUniformi("u_fragmentCoordinates", if (useFragmentCoordinates) 1 else 0)
                if (useFragmentCoordinates) {
                    cocos8MapSampler.value.setUniformf("u_framebufferSize", Gdx.graphics.backBufferWidth.toFloat(), Gdx.graphics.backBufferHeight.toFloat())
                    // Cocos's map-only camera projects framebuffer origin to
                    // world (0,0); ExtendViewport's widened logical bounds
                    // must not be mistaken for that source screen origin.
                    cocos8MapSampler.value.setUniformf("u_worldOrigin", 0f, 0f)
                    // The source Cocos visible width is derived from its WebGL
                    // drawing buffer.  Desktop logical-window dimensions can
                    // differ fractionally on HiDPI, so use the same physical
                    // framebuffer ratio for this pixel-centre diagnostic.
                    val framebufferWorldWidth = viewport.worldHeight * Gdx.graphics.backBufferWidth.toFloat() / Gdx.graphics.backBufferHeight.toFloat()
                    cocos8MapSampler.value.setUniformf("u_worldSize", framebufferWorldWidth, viewport.worldHeight)
                    cocos8MapSampler.value.setUniformf("u_mapOrigin", mapLeft, mapBottom)
                    cocos8MapSampler.value.setUniformf("u_mapSize", mapWidth, mapHeight)
                }
            }
            batch.draw(texture, mapLeft + sampleOffsetX, mapBottom + sampleOffsetY, mapWidth, mapHeight)
        }
        if (useCocos8Sampler) {
            // SpriteBatch must flush before changing shaders/filter state, or
            // its following units/HUD would be submitted through map shader.
            batch.flush()
            batch.shader = null
            priorSamplerFilter?.let { (min, mag) -> samplerTexture?.setFilter(min, mag) }
        }
        if (!mapOnlyCapture && dialogueComponentStage == null) {
            // MiniMapLayer id24 is persistent. Its authored button slides the
            // complete 244px panel between these two stable endpoints.
            val miniOffset = if (miniMapLayer.shown) 0f else 244f
            if (miniMapLayer.shown) hudAssets.menuFramePatch?.draw(batch, 1244.3721f, 556f, 244f, 244f)
            hudAssets.naturalMiniMapTexture?.let { texture ->
                batch.color = Color(1f, 1f, 1f, 168f / 255f)
                batch.draw(texture, 1246.3721f + miniOffset, 558f, 240f, 240f)
                batch.color = Color.WHITE
                // Source SpriteFrames crop [1,1,10,10] from each 12px image
                // and the parent scale produces a 16px submitted quad.
                MiniMapRenderEvents.yingchuanMarkers.forEach { item -> hudAssets.naturalMiniMapMarkerTextures[item.asset]?.let { marker ->
                    batch.draw(marker, item.x + miniOffset, item.y, 16f, 16f, 1, 1, 10, 10, false, false)
                } }
            }
            hudAssets.naturalWeatherTexture?.let { texture ->
                batch.color = Color(1f, 1f, 1f, 127f / 255f)
                batch.draw(texture, 1248.3721f + miniOffset, 560f, 57.6f, 57.6f)
            }
            batch.color = Color.WHITE
            if (miniMapLayer.shown) hudAssets.menuBoxPatch?.draw(batch, 1286.3721f, 570f, 186.047f, 100f)
        }
        if (mapOnlyCapture || dialogueComponentStage == "background") {
            batch.end()
            return
        }
        if (battleCharacterRouteState != null) {
            drawBattleCharacterRoute()
            batch.end()
            return
        }
        scriptRuntime.stage.mapObjects.values.filter { it.enabled && it.objectId > 3 }.forEach { gate ->
            dynamicTextures.gate(gate.objectId)?.let { texture ->
                // BattleScreen._setObject uses a 3×3 tile Gate sprite centred
                // on the scripted tile.  The original odd-numbered Gate IDs
                // are derived from the Stage object type in the same way.
                val size = boardTile * 3f
                batch.color = Color.WHITE
                batch.draw(
                    texture,
                    boardLeft + gate.x * boardTile - boardTile,
                    tileBottom(gate.y) - boardTile,
                    size,
                    size,
                )
            }
        }
        val selectAreaTiles = selectableAreaTiles()
        val visibleUnits = (battle.units.values + battle.pendingPresentationUnits().filter {
            it.hitPoints <= 0 || it.id in hitReactionAnimations ||
                it.id in deathAnimations || it.id in pendingDeathAnimations
        })
            .asSequence()
            .filter { it.visible }
            // BattleUnit.setPos assigns zIndex = sourceY + 1.  Drawing in
            // this order preserves the original overlap rule (larger source
            // y is in front) when actors occupy adjacent visual space.
            .sortedWith(if (battleDialogueBlendRoute) {
                val order = listOf(480, 483, 484, 146, 147, 481, 482, 485, 478, 479, 475, 476, 477, 235, 334, 474, 210, 234, 211)
                compareBy<BattleUnit> { order.indexOf(it.characterId).let { idx -> if (idx < 0) 999 else idx } }
            } else {
                compareBy<BattleUnit> { it.tileY }
            })
            .toList()
        drawSelectableTiles(selectAreaTiles)
        drawBattleCursor()
        // BattleScreen._setObject(0) calls CreateAnime2(U_select_20, 48, 8,
        // gutter=0, start=0, count=4): contiguous 48px rows held for eight
        // 24fps ticks. Each attached node owns an independent loop clock.
        hudAssets.fireTexture?.let { texture ->
            fun drawSelectObject(x: Int, y: Int, startRow: Int, count: Int, startedAt: Float) {
                val row = BattleObjectAnimationTimeline.row(mapObjectAnimationClock() - startedAt, startRow, count)
                val sourceY = BattleObjectAnimationTimeline.sourceY(row)
                batch.draw(texture, boardLeft + x * boardTile, tileBottom(y), boardTile, boardTile, 0, sourceY, 48, 48, false, false)
            }
            val enabledFires = scriptRuntime.stage.fires.values.filter { it.enabled }
            val enabledFireKeys = enabledFires.mapTo(linkedSetOf()) { it.x to it.y }
            fireAnimationStartedAt.keys.retainAll(enabledFireKeys)
            enabledFires.forEach { fire ->
                // _setObject(0): CreateAnime2(start=0, count=4).
                val key = fire.x to fire.y
                drawSelectObject(fire.x, fire.y, 0, 4, fireAnimationStartedAt.getOrPut(key, ::mapObjectAnimationClock))
            }
            // _setObject(t<3) uses a=[0,4,6] and o=[4,2,2].  These are
            // separate from setFire state and represent scripts' ordinary
            // terrain objects, so retain their enabled/disabled lifecycle.
            val enabledObjects = scriptRuntime.stage.mapObjects.values.filter { it.enabled && it.objectId in 0..2 }
            val enabledObjectKeys = enabledObjects.mapTo(linkedSetOf()) { Triple(it.objectId, it.x, it.y) }
            mapObjectAnimationStartedAt.keys.retainAll(enabledObjectKeys)
            enabledObjects.forEach { objectState ->
                    val (startRow, count) = when (objectState.objectId) {
                        0 -> 0 to 4
                        1 -> 4 to 2
                        else -> 6 to 2
                    }
                    val key = Triple(objectState.objectId, objectState.x, objectState.y)
                    drawSelectObject(objectState.x, objectState.y, startRow, count, mapObjectAnimationStartedAt.getOrPut(key, ::mapObjectAnimationClock))
                }
        }
        font.color = Color.WHITE
        drawMoveTerrainImpacts()
        visibleUnits.forEach { unit ->
            // Prefab child order is status -> unit -> info, so the six
            // ability lift/down icons are behind the avatar.
            drawUnitAttributeStatuses(unit)
            val action = actionAnimation?.takeIf { animationClock() < it.endsAt && it.unitId == unit.id }
                ?: hitReactionAnimations[unit.id]?.takeIf { animationClock() in it.startedAt..<it.endsAt }
                ?: deathAnimations[unit.id]?.takeIf { animationClock() in it.startedAt..<it.endsAt }
            val move = movementAnimation?.takeIf { animationClock() < it.endsAt && it.unitId == unit.id }
            val scripted = action?.let { null } ?: scriptedUnitVisuals[unit.id]
            val frame = (if (battleDialogueBlendRoute) battleSpriteFrame(0, 0, 0f) else null)
                ?: winConditionActualVisualFrame(unit) ?: action?.let { transientVisualFrame(it) }
                ?: move?.let { movementVisualFrame(it) }
                ?: scripted?.let { scriptedVisualFrame(unit, it) }
                ?: idleSpriteFrame(unit)
            val texture = when (frame.source) {
                UnitSpriteSource.ATTACK -> attackTexture(unit) ?: unitTexture(unit)
                UnitSpriteSource.SPECIAL -> specialTexture(unit) ?: unitTexture(unit)
                UnitSpriteSource.MOVEMENT -> unitTexture(unit)
            }
            texture?.let { texture ->
                // BattleUnit's normal mov sprite is 48px but Unit_atk frames
                // expand to 64px before the source map's ×2 transform.
                val size = if (frame.source == UnitSpriteSource.ATTACK) boardTile * 4f / 3f else boardTile
                val drawX = boardLeft + visualTile(unit).first * boardTile + (boardTile - size) / 2 + frame.offsetX
                val drawY = tileBottom(visualTile(unit).second) + (boardTile - size) / 2 + frame.offsetY
                // Source S_00 scene0 leaves unit 235 in action 4 while its
                // SayLayer is visible. Its resulting source framebuffer
                // sprite is fully saturated white; the ordinary SpriteBatch
                // path cannot reproduce that hight-light material output.
                val sourceHighlight = !battleDialogueBlendRoute && sourceScenario == "S_00" && scripted?.action == 4
                if (sourceHighlight) {
                    batch.flush()
                    batch.shader = cocosHighlightSampler.value
                    // Source diagnostics read the live material's u_value
                    // as 1 for this frame, which saturates the sprite RGB.
                    cocosHighlightSampler.value.setUniformf("u_value", 1f)
                }
                drawWithTerrainMask(unit, drawX, drawY, size) {
                batch.draw(
                    texture,
                    drawX,
                    drawY,
                    size,
                    size,
                    0,
                    // Both the original Cocos SpriteFrame.rect and LibGDX's
                    // source rectangle are bottom-origin.  Asset extraction
                    // preserves the source atlas verbatim, so no y conversion
                    // belongs in the runtime renderer.
                    // Battle.CreateAnime resets an out-of-range generated
                    // row to zero (`G + w > texture.height && (G = 0)`).
                    // Clamping to the final row is visually different for
                    // the shorter Unit_* atlases.
                    if (frame.sourceY + frame.sourceHeight > texture.height) 0 else frame.sourceY,
                    minOf(frame.sourceWidth, texture.width),
                    minOf(frame.sourceHeight, texture.height),
                    frame.flipX || (action?.kind == UnitAnimationKind.ATTACK && action.direction == 1),
                    false,
                )
                }
                if (sourceHighlight) {
                    batch.flush()
                    batch.shader = null
                }
            }
            drawUnitInfoBarInline(unit)
            drawUnitStateAnimation(unit)
        }
        drawHarmNumbers(visibleUnits)
        // BattleScreen's SHOW_SAY handler parents qipao to map at the speaking
        // BattleUnit node position + (24,24) Cocos-local pixels. In the
        // scaled map this is +48,+48 from the unit centre. The source map's
        // centred Cocos anchor contributes a further half-cell relative to
        // our bottom-left map quad. The live raw framebuffer therefore puts
        // the 474 bubble at tile-bottom-left +72,+72 (not +96,+96).
        // SayLayer dispatches SHOW_SAY for every consumed `&unitId` marker;
        // BattleScreen reparents qipao to that current speaker.  It does not
        // remain attached to the opening actor when a later line begins.
        val saySpeakerId = if (battleMenuOpen) null else scriptRuntime.currentDialogue?.speakerId?.toIntOrNull()
        saySpeakerId?.let { speakerId ->
            hudAssets.battleSayTexture?.let { texture ->
                visibleUnits.firstOrNull { it.characterId == speakerId }?.let { speaker ->
                    val (speakerX, speakerY) = visualTile(speaker)
                    batch.draw(
                        texture,
                        boardLeft + speakerX * boardTile + boardTile * 0.75f,
                        tileBottom(speakerY) + boardTile * 0.75f,
                        boardTile / 2f,
                        boardTile / 2f,
                    )
                }
            }
        }
        batch.end()
        batch.begin()
        drawMagicEffect()
        batch.end()
    }

    /**
     * BattleUnit's own `mask` parent is a cc.Mask stencil.  It is distinct
     * from an avatar overlay: recovered source sets it enabled only when the
     * terrain config supplies a Mark frame.  The source mask node is 80×80
     * while its normal avatar child is 48×48; map rendering scales both ×2.
     */
    private fun drawWithTerrainMask(unit: BattleUnit, x: Float, y: Float, size: Float, draw: () -> Unit) {
        val mask = when (terrainGrid.terrainAt(unit.tileX, unit.tileY)) { 10 -> hudAssets.terrainMask19; 1 -> hudAssets.terrainMask21; else -> null }
        if (mask == null) { draw(); return }
        batch.flush(); Gdx.gl.glEnable(GL20.GL_STENCIL_TEST); Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT)
        Gdx.gl.glColorMask(false, false, false, false); Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xff); Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE)
        val maskSize = boardTile * (80f / 48f)
        batch.draw(mask, x + (size - maskSize) / 2f, y + (size - maskSize) / 2f, maskSize, maskSize); batch.flush()
        Gdx.gl.glColorMask(true, true, true, true); Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, 0xff); Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP)
        draw(); batch.flush(); Gdx.gl.glDisable(GL20.GL_STENCIL_TEST)
    }

    /** Dynamic `status` child is appended after the prefab unit/info nodes. */
    private fun drawUnitStateAnimation(unit: BattleUnit) {
        val effect = unit.presentation.stateAnimation.current()
        if (effect == null || !effect.active) {
            battleStateAnimationStarts.remove(unit.id)
            return
        }
        val now = stateEffectAnimationClock()
        val previous = battleStateAnimationStarts[unit.id]
        val startedAt = if (previous == null || previous.first != effect.textureIndices) {
            now.also { battleStateAnimationStarts[unit.id] = effect.textureIndices.toList() to it }
        } else previous.second
        val (visualX, visualY) = visualTile(unit)
        val command = BattleUnitStateRender.command(
            effect,
            now - startedAt,
            boardLeft + visualX * boardTile,
            tileBottom(visualY),
            boardTile,
        ) ?: return
        hudAssets.battleStateTextures.getOrNull(command.textureIndex)?.let { texture ->
            batch.color = Color.WHITE
            batch.draw(texture, command.x, command.y, command.width, command.height)
        }
    }

    /** BattleUnit prefab status/unit_status_0..5 at its authored positions. */
    private fun drawUnitAttributeStatuses(unit: BattleUnit) {
        val (visualX, visualY) = visualTile(unit)
        val unitLeft = boardLeft + visualX * boardTile
        val unitBottom = tileBottom(visualY)
        BattleUnitAttributeStatusRender.commands(
            unit.presentation.attributeStatusIcons,
            unit.otherNodesVisible,
            unitLeft,
            unitBottom,
            boardTile,
        ).forEach { command ->
            val texture = hudAssets.battleAttributeStatusTextures[command.textureIndex] ?: return@forEach
            batch.color = Color.WHITE
            batch.draw(texture, command.x, command.y, command.size, command.size)
        }
    }

    /** Faithful BattleScreen.meff strip playback, including frame alpha and offsets. */
    private fun playPendingMagicEffectSounds() {
        val now = animationClock()
        magicEffectAnimations.filter { !it.soundPlayed && now >= it.startedAt }.forEach { animation ->
            val effect = magicEffects.effect(animation.effectId) ?: return@forEach
            // StageLayer.meff(r=finished callback) plays SOUND once for every
            // node it starts, i.e. every target in each playMeff pass.
            repeat(animation.targetIds.size) { audio.playBattleEffect(100 + effect.soundId) }
            animation.soundPlayed = true
        }
        // StageLayer.meff destroys every effect node at FINISHED. Retaining
        // expired records inflated long-battle diagnostics and memory even
        // though drawing and busy checks ignored them.
        magicEffectAnimations.removeAll { now >= it.endsAt }
    }

    /** Faithful BattleScreen.meff strip playback, including frame alpha and offsets. */
    private fun drawMagicEffect() {
        magicEffectAnimations.filter { animationClock() in it.startedAt..<it.endsAt }.forEach { animation ->
        val effect = magicEffects.effect(animation.effectId) ?: return@forEach
        val frame = effect.frameAt(animationClock() - animation.startedAt) ?: return@forEach
        if (frame.sourceIndex < 0) return@forEach
        val texture = dynamicTextures.effect(animation.effectId) ?: return@forEach
        batch.color = Color(1f, 1f, 1f, ((frame.alpha + 24).coerceIn(0, 32) / 32f))
        animation.targetIds.mapNotNull(battle::presentationUnit).filter { it.visible }.forEach { target ->
            val width = effect.frameWidth / 48f * boardTile
            val height = effect.frameHeight / 48f * boardTile
            batch.draw(
                texture,
                boardLeft + target.tileX * boardTile + (boardTile - width) / 2 + frame.offsetX / 48f * boardTile,
                tileBottom(target.tileY) + (boardTile - height) / 2 - frame.offsetY / 48f * boardTile,
                width,
                height,
                0,
                (frame.sourceIndex * effect.frameHeight).coerceAtMost((texture.height - effect.frameHeight).coerceAtLeast(0)),
                minOf(effect.frameWidth, texture.width),
                minOf(effect.frameHeight, texture.height),
                false,
                false,
            )
        }
        }
        batch.color = Color.WHITE
    }

    /** BattleUnit.createInfoNode's always-visible bar2, without debug names. */
    /** The HP sprite is a child of each unit and follows its actor draw. */
    private fun drawUnitInfoBarInline(unit: BattleUnit) {
            // unitHide first clears other child nodes before starting anime24.
            if (deathAnimations[unit.id]?.let { animationClock() in it.startedAt..<it.endsAt } == true) return
            // The authored S_00 cut-scene leaves the struck 235 actor drawn
            // for its highlight/death frame, while BattleUnit's source HP
            // ratio is already zero, so its child bar contributes no pixels.
            if (!battleDialogueBlendRoute && sourceScenario == "S_00" && scriptedUnitVisuals[unit.id]?.action == 4) return
            val (visualX, visualY) = visualTile(unit)
            val shownHp = healthTimeline.shownHp(unit.id, animationClock(), unit.hitPoints)
            val ratio = (shownHp.toFloat() / unit.maxHitPoints.coerceAtLeast(1)).coerceIn(0f, 1f)
            val width = 88f
            val x = boardLeft + visualX * boardTile + (boardTile - width) / 2
            val y = tileBottom(visualY) - 1f
            val texture = when (unit.type()) {
                Faction.PLAYER -> hudAssets.mineHpBarTexture
                Faction.FRIEND -> hudAssets.friendHpBarTexture
                Faction.ENEMY -> if (unit.famous) hudAssets.famousEnemyHpBarTexture else hudAssets.enemyHpBarTexture
                Faction.REINFORCEMENTS -> if (unit.famous) hudAssets.famousEnemyHpBarTexture else hudAssets.enemyHpBarTexture
            }
            texture?.let {
                batch.color = Color.WHITE
                batch.draw(it, x, y, width * ratio, 6f)
            }
    }

    /** A 24px bold HP/MP harm number above the target. */
    private fun drawHarmNumbers(visibleUnits: List<BattleUnit>) {
        val now = animationClock()
        harmNumberAnimations.forEach { (unitId, animation) ->
            if (now < animation.startedAt || now >= animation.endsAt) return@forEach
            val unit = visibleUnits.firstOrNull { it.id == unitId } ?: return@forEach
            val (visualX, visualY) = visualTile(unit)
            font.data.setScale(0.5f)
            font.color = if (animation.isHp) Color.WHITE else Color(224f / 255f, 224f / 255f, 0f, 1f)
            // Harm-number presentation writes String(Math.abs(value)) for both
            // HP_ADD and MP_ADD; a negative HP delta is not a minus-sign UI.
            font.draw(batch, kotlin.math.abs(animation.amount).toString(), boardLeft + visualX * boardTile, tileBottom(visualY) + boardTile + 24f)
            font.data.setScale(1f)
        }
        font.color = Color.WHITE
    }

    /** Source BattleScreen coordinates use y=0 at the top of the Hexzmap. */
    private fun tileBottom(sourceY: Int): Float = tileBottom(sourceY.toFloat())
    private fun tileBottom(sourceY: Float): Float = boardBottom - sourceY * boardTile

    /** Builds the source BattleScreen move/hit-area sprite layer. */
    private fun selectableAreaTiles(): List<SelectAreaTile> {
        pendingAiResolution?.let { resolution ->
            when (aiPresentationStage) {
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
                // Source `showSelectTiled` uses the red frame for an
                // ordinary attackable tile.  Box variants are only emitted
                // for bit-mask combinations the current game has not yet
                // modeled, so do not invent a fourth visual here.
                ?.map { (x, y) -> SelectAreaTile(x, y, SelectAreaFrame.RED) }
                .orEmpty()
            propertyMode -> {
                val range = listOf(0 to 0, 1 to 0, -1 to 0, 0 to 1, 0 to -1)
                .map { (dx, dy) -> selected.tileX + dx to selected.tileY + dy }
                .filter { (x, y) -> x in 0..boardMaxX && y in 0..boardMaxY }
                // `showUsePropertyRange` calls `_showHitArea(..., true,
                // false)`: include-self plus the hit flag resolves to RED.
                // Its later GREEN_BOX target effect is cursor-relative and
                // requires the source effect-area data, which this compact
                // item preview does not yet model.
                .map { (x, y) -> SelectAreaTile(x, y, SelectAreaFrame.RED) }
                // The source then calls `selectUnit(targets, 1)`, placing a
                // GREEN_BOX above every selectable allied target.  The game
                // resolves the target on click rather than moving a separate
                // cursor, but its visible candidate set is the same local
                // allied infantry area.
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
            // BattleScreen._showMoveArea uses BLUE for player-controlled
            // actors and GREEN only for automated allies/enemies.
            else -> {
                val moveFrame = if (selected.type() == Faction.PLAYER) SelectAreaFrame.BLUE else SelectAreaFrame.GREEN
                val moveTiles = battle.reachableTiles(selected.id).keys.map { (x, y) ->
                    SelectAreaTile(x, y, moveFrame)
                }
                // Control._process first calls _showMoveArea, then
                // _ref_hit_area. The latter invokes _showHitArea with flag
                // 3, which is RED_BOX, so attack candidates remain visible
                // while the player is choosing a destination.
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

    /** Draws the original `U_select` frames below the fire and actor layers. */
    private fun drawSelectableTiles(tiles: List<SelectAreaTile>) {
        tiles.forEach { tile ->
            hudAssets.selectAreaTextures[tile.frame.assetName]?.let { texture ->
                batch.color = Color.WHITE
                batch.draw(
                    texture,
                    boardLeft + tile.x * boardTile,
                    tileBottom(tile.y),
                    boardTile,
                    boardTile,
                )
            }
        }
        batch.color = Color.WHITE
    }

    /** Mirrors BattleScreen.cursorVisible while a player unit is selected. */
    private fun drawBattleCursor() {
        val selected = selectedUnitId?.let(battle.units::get) ?: return
        hudAssets.battleCursorTexture?.let { texture ->
            batch.color = Color.WHITE
            batch.draw(
                texture,
                boardLeft + selected.tileX * boardTile,
                tileBottom(selected.tileY),
                boardTile,
                boardTile,
            )
        }
        batch.color = Color.WHITE
    }

    /** Battle's player and allied camps share the source `areAllied` target set. */
    private fun unitsAreAllied(left: BattleUnit, right: BattleUnit): Boolean =
        left.isPlayerSide() == right.isPlayerSide()

    /** _showMoveArea prints BattleUnit.terrainImpact on every available tile. */
    private fun drawMoveTerrainImpacts() {
        if (magicMode || propertyMode) return
        val selected = selectedUnitId?.let(battle.units::get) ?: return
        val reachable = battle.reachableTiles(selected.id)
        if (reachable.isEmpty()) return
        // `_showMoveArea` creates a 12px cc.Label under map.node. The map
        // itself is scaled ×2 in Battle.fire, therefore its framebuffer
        // glyphs are 24px—not the old 12px direct-screen approximation.
        font.data.setScale(24f / 26f)
        font.color = Color(0.94f, 0.97f, 1f, 0.9f)
        reachable.keys.forEach { (x, y) ->
            val impact = selected.terrainImpacts[terrainGrid.terrainAt(x, y)] ?: 100
            font.draw(batch, impact.toString(), boardLeft + x * boardTile + 3f, tileBottom(y) + 23f)
        }
        font.data.setScale(1f)
        font.color = Color.WHITE
    }

    private fun drawHud() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        font.color = Color(1f, 0.85f, 0.48f, 1f)
        font.draw(batch, "원본 전술 전투 · $sourceScenario", 80f, 680f)
        font.color = Color.WHITE
        font.draw(batch, "라운드 ${battle.round} · ${battle.activeFaction.label()} 차례 · ${battle.weather.label()}", 80f, 638f)
        font.draw(batch, eventMessage, 80f, 94f)
        font.color = Color(0.72f, 0.80f, 0.90f, 1f)
        font.draw(batch, "클릭: 선택/이동/공격 · M: 전략 · B: 아이템 · T: 턴 종료 · Esc: 돌아가기", 520f, 52f)
        batch.end()
    }

    /** Canvas/Layer/menu_button: authored icon over the tactical map layer. */
    private fun drawBattleHudChrome() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        // menu_button is submitted before BattleScreen's own bg controls.
        hudAssets.battleEndTurnTexture?.let { batch.draw(it, 1353.9535f, 8f, 60f, 60f) }
        if (battleDialogueBlendRoute) {
            batch.end()
            return
        }
        // Canvas/Layer/bg/button: 68px nine-sliced button and a 48px tool11
        // scaled to 69.6px.  This sits flush against the lower-left edge.
        hudAssets.battleButtonBackgroundPatch?.draw(batch, 0.843f, 0.731f, 68f, 68f)
        hudAssets.battleRecordTexture?.let { batch.draw(it, 0.043f, -0.069f, 69.6f, 69.6f) }
        // MiniMapLayer's upper-right button moves with its 244px panel.
        val miniButtonX = if (miniMapLayer.shown) 1174.3721f else 1418.3721f
        hudAssets.battleButtonBackgroundPatch?.draw(batch, miniButtonX, 730f, 70f, 70f)
        hudAssets.battleMenuTexture?.let { batch.draw(it, miniButtonX + .2f, 730.2f, 69.6f, 69.6f) }
        batch.end()
    }

    /** Original Battle/scene/MenuLayer using its captured DynamicAtlas frames. */
    private fun drawBattleMenu() {
        // Direct source gl.readPixels shows the tactical map remains visible
        // above MenuLayer's bottom panel.  The older capturePage PNG had a
        // stale black compositor surface, which incorrectly suggested an
        // opaque full-screen mask here.
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        // MenuLayer/bg and box1 occupy the bottom 212 design units.
        hudAssets.menuBackgroundPatch?.draw(batch, 0f, 0f, 1488.3721f, 212f)
        hudAssets.menuFramePatch?.draw(batch, 0f, 0f, 1488.3721f, 212f)
        // bg0 and progressBar are two 304×44 frames at source y=58.
        hudAssets.menuBoxPatch?.draw(batch, 41f, 36f, 304f, 44f)
        hudAssets.menuBoxPatch?.draw(batch, 425f, 36f, 304f, 44f)
        hudAssets.menuTitleBarTexture?.let { batch.draw(it, 43f, 38f, 300f, 40f) }
        val menu = battleMenuLayer?.view()
        // Source ProgressBar totalLength=300 and onCreate sets round/max.
        hudAssets.menuProgressBarTexture?.let { batch.draw(it, 427f, 38f, 300f * (menu?.progress ?: 0f), 40f) }
        // Original node is parented under bg/box2, has scale=2, and the
        // AnimationClip loops its four source frames at 6 fps.
        menu?.let { view ->
            val sheet = MenuLayer.weatherSheet(view.weather)
            val frame = MenuLayer.weatherFrameAt(elapsed - battleMenuOpenedAt)
            hudAssets.menuWeatherTextures[sheet]?.getOrNull(frame)?.let { weather ->
                batch.draw(weather, 832.232f, 8f, 432f, 100f)
            }
        }
        // MenuLayer reads this from Model.battleName(), which resolves the
        // source SHOP table using the temporary battle-map index.
        // Fixture labels bg0/label, progressBar/label and label0 all carry
        // Cocos color [0,0,0,255].
        dialogueFont.color = Color.BLACK
        dialogueFont.data.setScale(30f / 36f)
        dialogueFont.draw(batch, menu?.battleName ?: gameDataCatalog.battleName(scriptRuntime.stage.battleMapIndex), 124f, 69f)
        dialogueFont.draw(batch, "턴 수", 430f, 69f)
        dialogueFont.draw(batch, "${menu?.round ?: battle.round} / ${menu?.maxRound ?: scenarioMaxRound()}", 692f, 69f)
        // The prefab's weather is the animated bg/weather Sprite (a 72px
        // atlas frame), not a textual weather label. The 4-frame sheet is
        // selected by MenuLayer._create_weather(weather).
        dialogueFont.data.setScale(30f / 36f)
        // MenuLayer/bg/contain: 13 authored buttons, each 88×88, centred
        // at x=59.1337 + 88*n and y=160.29.  button12 is absent in source.
        val sourceIndexes = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        sourceIndexes.forEach { index ->
            val x = 15.13372f + index * 88f
            hudAssets.menuButtonPatch?.draw(batch, x, 116.29f, 88f, 88f)
            // MenuLayer's tool1 node is 48×48 with scale=(1.5,1.5),
            // centered on the 88×88 button.  Preserve that node transform
            // rather than treating the SpriteFrame's native size as final.
            hudAssets.menuToolTextures[index]?.let { batch.draw(it, x + 8f, 124.572f, 72f, 72f) }
        }
        // Source has no button12; button13 occupies its visual 13th slot.
        hudAssets.menuButtonPatch?.draw(batch, 1071.1337f, 116.29f, 88f, 88f)
        hudAssets.menuHelpTexture?.let { batch.draw(it, 1079.1337f, 124.29f, 72f, 72f) }
        dialogueFont.data.setScale(1f)
        dialogueFont.color = Color.WHITE
        batch.end()
    }

    /**
     * Desktop rendering of the original `Global/scene/TerrainLayer` prefab.
     * The source has a 1021×600 bg, two 196/223px tab buttons and a
     * `Panel_cancel`; its ScrollViews show two alternating item prefabs.
     * We retain the content geometry, tab ownership and scrolling while using
     * the game's bitmap font instead of Cocos DynamicAtlas labels.
     */
    private fun drawTerrainLayer() {
        // TerrainLayer/bg is authored at x=40.6 relative to the original
        // Canvas centre (744.186), placing its left edge at 274.236.  The
        // previous 234 value incorrectly used a 1280-wide canvas centre.
        val panelX = 274f
        val panelY = 100f
        val panelW = 1021f
        val panelH = 600f
        val tab = terrainLayer.selected ?: TerrainLayer.Tab.RISE
        val panel = terrainLayer.select(tab)
        // The source item prefabs are 75px high in a 415px ScrollView.
        // Six rows are visible (the sixth is clipped at its bottom edge).
        val visibleRows = 6
        val first = terrainScrollRow.coerceIn(0, (panel.rows.size - visibleRows).coerceAtLeast(0))
        terrainScrollRow = first

        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        // Logo_9 is a tiled Cocos Sprite, not a stretched 96px image.
        overlayAssets.terrainLayerBackgroundTexture?.let { texture ->
            val tile = 96f
            var ty = panelY
            while (ty < panelY + panelH) {
                var tx = panelX
                while (tx < panelX + panelW) {
                    batch.draw(texture, tx, ty, minOf(tile, panelX + panelW - tx), minOf(tile, panelY + panelH - ty))
                    tx += tile
                }
                ty += tile
            }
        }
        overlayAssets.terrainLayerPanelPatch?.draw(batch, panelX + 14f, panelY + 84f, panelW - 28f, 460f)
        // button0/1/2 in the source prefab are 196.7×60, 222.7×60 and
        // 120×60 respectively.  Draw them after bg so their nine-patches are
        // not hidden by the tiled Logo_9 background.
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 285f, 111f, 197f, 60f)
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 491f, 111f, 223f, 60f)
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 1165f, 111f, 120f, 60f)
        // TerrainLayer's authored labels are black over the pale Logo_9
        // background (unlike the ordinary battle HUD labels).
        font.color = Color.BLACK
        val titleScale = 40f / 26f
        val terrainNameScale = 36f / 26f
        val valueScale = 50f / 26f
        font.data.setScale(titleScale)
        font.draw(batch, "지형 정보 일람", panelX + 10f, panelY + 586f)
        // Cocos' system font is appreciably narrower than KoreanFont.  Keep
        // the 40px title scale, but use the base glyph scale for labels that
        // must fit inside the authored 53px table columns and 60px buttons.
        font.data.setScale(1.4f)
        font.draw(batch, "지형 효과", 304f, 147f)
        font.draw(batch, "기동력 소모", 516f, 147f)
        font.draw(batch, "확인", 1207f, 147f)
        val columnWidth = 53f
        // TerrainLayer places the arm names in the top row of the table;
        // the bottom buttons change panels rather than acting as top tabs.
        font.color = Color.BLACK
        font.data.setScale(1f)
        font.draw(batch, "이름", panelX + 90f, panelY + 529f)
        panel.rows.firstOrNull()?.values?.forEachIndexed { armIndex, value ->
            // TerrainLayer.onCreate explicitly assigns NAME.substring(0, 2)
            // to each of the thirteen header buttons.
            font.draw(batch, value.armName.take(2), panelX + 252f + armIndex * columnWidth, panelY + 529f)
        }
        font.data.setScale(valueScale)
        panel.rows.drop(first).take(visibleRows).forEachIndexed { rowIndex, row ->
            val y = panelY + 488f - rowIndex * 75f
            (if (rowIndex % 2 == 0) overlayAssets.terrainLayerRowEvenPatch else overlayAssets.terrainLayerRowOddPatch)
                ?.draw(batch, panelX + 18f, y - 59f, panelW - 36f, 75f)
            // Source item prefab: icon is a 48px SpriteFrame scaled 1.4;
            // its resource lookup uses `_initPanel*`'s incrementing `i`,
            // represented by Cell.iconIndex, never a translated terrain ID.
            dynamicTextures.terrainIcon(row.iconIndex)?.let { icon ->
                batch.color = Color.WHITE
                batch.draw(icon, TerrainLayerSpriteLayout.ICON_X, TerrainLayerSpriteLayout.iconY(rowIndex), TerrainLayerSpriteLayout.ICON_SIZE, TerrainLayerSpriteLayout.ICON_SIZE)
            }
            font.data.setScale(terrainNameScale)
            font.color = if (rowIndex % 2 == 0) Color(1f, 0.94f, 0.78f, 1f) else Color(0.86f, 0.86f, 0.86f, 1f)
            font.draw(batch, row.terrainName, panelX + 104f, y + 12f)
            font.data.setScale(valueScale)
            row.enabledSkills.forEachIndexed { bit, enabled ->
                font.color = if (enabled) Color(1f, 0.82f, 0.20f, 1f) else Color(0.35f, 0.35f, 0.35f, 1f)
                font.draw(batch, if (enabled) "●" else "○", panelX + 172f + bit * 20f, y)
            }
            row.values.forEachIndexed { armIndex, value ->
                font.color = when (value.grade) {
                    0 -> Color(0.94f, 0.56f, 0.13f, 1f)
                    1 -> Color(0.94f, 0.38f, 0f, 1f)
                    2 -> Color(0f, 0.56f, 0f, 1f)
                    3 -> Color(0f, 0f, 0.63f, 1f)
                    4 -> Color(0.44f, 0.25f, 0.5f, 1f)
                    else -> Color(0.78f, 0.78f, 0.78f, 1f)
                }
                font.draw(batch, value.text, panelX + 252f + armIndex * columnWidth, y)
            }
        }
        (0..13).forEach { index ->
            overlayAssets.terrainLayerVlinePatch?.draw(batch, panelX + 244f + index * columnWidth, panelY + 96f, 6f, 414f)
        }
        font.data.setScale(1f)
        font.color = Color.WHITE
        batch.end()
    }

    /** Game of PropertyLayer prefab: four toggles, pooled scroll rows, close button. */
    private fun drawPropertyLayer() {
        val x = 247f; val y = 48f; val w = 994f; val h = 706f
        // Captured PropertyLayer labels are all Cocos system-font 40px.
        val sourceLabelScale = 40f / 26f
        val rows = propertyLayer.rows()
        val visibleRows = 7
        val first = propertyScrollRow.coerceIn(0, (rows.size - visibleRows).coerceAtLeast(0)); propertyScrollRow = first
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        overlayAssets.terrainLayerBackgroundTexture?.let { texture ->
            val tile = 96f
            var ty = y
            while (ty < y + h) {
                var tx = x
                while (tx < x + w) {
                    batch.draw(texture, tx, ty, minOf(tile, x + w - tx), minOf(tile, y + h - ty))
                    tx += tile
                }
                ty += tile
            }
        }
        // Source panel0 is 990×524 (centre 744.186,379); its five title0
        // prefabs are independent 60px header frames above the scroll body.
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 249f, 117f, 990f, 524f)
        listOf(
            floatArrayOf(251.2f, 637.9f, 376.9f),
            floatArrayOf(628.6f, 638f, 195.1f),
            floatArrayOf(824.7f, 638f, 106.9f),
            floatArrayOf(931.4f, 638f, 101.2f),
            floatArrayOf(1032f, 638f, 206.4f),
        ).forEach { (headerX, headerY, headerWidth) ->
            overlayAssets.terrainLayerPanelPatch?.draw(batch, headerX, headerY, headerWidth, 60f)
        }
        font.color = Color.BLACK
        font.data.setScale(sourceLabelScale)
        font.draw(batch, "창고 일람", x + 430f, 740f)
        val columns = listOf(
            "이름" to 400f, "속성" to 687f, "레벨" to 838f,
            "경험치" to 930f, "소지자" to 1083f,
        )
        columns.forEach { (label, cx) -> font.draw(batch, label, cx, 680f) }
        val columnLines = listOf(628.468f, 823.971f, 930.065f, 1032.026f)
        columnLines.forEach { lineX -> overlayAssets.terrainLayerVlinePatch?.draw(batch, lineX, 122.75f, 6f, 515.38f) }
        rows.drop(first).take(visibleRows).forEachIndexed { rowIndex, row ->
            val rowY = y + 540f - rowIndex * 72f
            (if (rowIndex % 2 == 0) overlayAssets.terrainLayerRowEvenPatch else overlayAssets.terrainLayerRowOddPatch)
                ?.draw(batch, x + 9f, rowY - 59f, w - 18f, 72f)
            val selected = row.item.id == propertySelectedId
            font.color = if (selected) Color(0.05f, 0.35f, 0.95f, 1f) else Color.BLACK
            dynamicTextures.itemIcon(row.item.icon)?.let { batch.draw(it, x + 22f, rowY - 47f, 48f, 48f) }
            font.draw(batch, row.labels.joinToString("     "), x + 86f, rowY)
        }
        font.data.setScale(sourceLabelScale)
        val labels = listOf("무기", "방어구", "보조", "아이템")
        PropertyLayer.Tab.entries.forEachIndexed { index, tab ->
            font.color = if (tab == propertyLayer.selected) Color(0.05f, 0.48f, 0.94f, 1f) else Color(0.2f, 0.2f, 0.2f, 1f)
            font.draw(batch, if (tab == propertyLayer.selected) "●" else "○", x + 28f + index * 146f, y + 30f)
            font.color = Color.BLACK
            font.draw(batch, labels[index], x + 56f + index * 146f, y + 30f)
        }
        overlayAssets.terrainLayerPanelPatch?.draw(batch, x + w - 158f, y + 10f, 140f, 54f)
        font.color = Color.BLACK
        font.draw(batch, "확인", x + w - 116f, y + 30f)
        font.color = Color.WHITE; font.data.setScale(1f); batch.end()
    }

    private fun handlePropertyLayerTap(px: Float, py: Float) {
        val x=247f; val y=48f
        if (px !in x..x+994 || py !in y..y+706) return
        if (py in y+10..y+64) {
            if (px in x + 820f..x + 976f) { propertyLayerOpen = false; return }
            when (((px - (x + 12f)) / 146f).toInt()) {
                0 -> propertyLayer.select(PropertyLayer.Tab.WEAPON)
                1 -> propertyLayer.select(PropertyLayer.Tab.ARMOR)
                2 -> propertyLayer.select(PropertyLayer.Tab.AUXILIARY)
                3 -> propertyLayer.select(PropertyLayer.Tab.PROPERTY)
            }
            propertyScrollRow = 0
            return
        }
        if(py in y+72..y+602) { val row=((y+566-py)/72f).toInt()+propertyScrollRow; propertyLayer.rows().getOrNull(row)?.let { propertySelectedId=it.item.id } }
    }

    private fun drawTreasureLayer() {
        // TreasureLayer/bg1: centre=(744.186,400), size=970×632.
        val x = 259f; val y = 84f; val width = 970f; val height = 632f
        // TreasureLayer's title, card labels, footer and button use 40px
        // Cocos system labels in the captured prefab.
        val sourceLabelScale = 40f / 26f
        val rows = treasureLayer.rows
        val first = treasureScrollRow.coerceIn(0, (rows.size - 4).coerceAtLeast(0)); treasureScrollRow = first
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
        rows.drop(first).take(4).forEachIndexed { index, row ->
            val column = index % 2
            val line = index / 2
            // Source pooled item prefab: box3 471×190, centres at
            // (505.686,575.5) and (982.686,575.5), then 193px per line.
            val cardX = 270f + column * 477f
            val cardY = 671f - line * 193f
            overlayAssets.terrainLayerPanelPatch?.draw(batch, cardX, cardY - 190f, 471f, 190f)
            // The source item0 prefab keeps the icon slot visible before a
            // treasure is discovered; it is not an absent node.
            overlayAssets.terrainLayerPanelPatch?.draw(batch, cardX + 12f, cardY - 112f, 90f, 90f)
            font.color = if (row.item.id == treasureSelectedId) Color(0.05f, 0.35f, 0.95f, 1f) else Color.BLACK
            if (row.discovered) {
                dynamicTextures.itemIcon(row.item.icon)?.let { batch.draw(it, cardX + 12f, cardY - 112f, 90f, 90f) }
                font.draw(batch, row.item.name, cardX + 134f, cardY - 25f)
            } else {
                font.draw(batch, "발견되지 않음", cardX + 134f, cardY - 25f)
            }
        }
        font.data.setScale(sourceLabelScale)
        font.color = Color.BLACK
        font.draw(batch, "지금까지 발견한 보물 ${rows.count { it.discovered }.toString().padStart(2, '0')} / ${rows.size}", x + 7f, 141f)
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 1071f, 91f, 151f, 52f)
        font.draw(batch, "종료", 1100f, 119f)
        font.color = Color.WHITE; font.data.setScale(1f); batch.end()
    }

    /** SaveLayer bg1 list plus the source MsgBox confirmation state. */
    private fun drawSaveLayer() {
        // SaveLayer/bg1: centre=(744.186,400), size=932×634.
        val x=278f; val y=83f; val w=932f; val h=634f; val rows=saveLayer.view().rows
        // Every live SaveLayer cc.Label in the captured prefab is Arial 40.
        val sourceLabelScale=40f/26f
        val first=saveScrollRow.coerceIn(0,(rows.size-8).coerceAtLeast(0)); saveScrollRow=first
        batch.projectionMatrix=viewport.camera.combined; batch.begin(); batch.color=Color.WHITE
        overlayAssets.terrainLayerBackgroundTexture?.let { texture ->
            var ty=y; while(ty<y+h) { var tx=x; while(tx<x+w) { batch.draw(texture,tx,ty,minOf(96f,x+w-tx),minOf(96f,y+h-ty)); tx+=96f }; ty+=96f }
        }
        // box2 / scrollview: centre=(743.186,386.534), size=912×428.
        overlayAssets.terrainLayerPanelPatch?.draw(batch,287f,173f,912f,428f)
        font.color=Color.BLACK; font.data.setScale(sourceLabelScale); font.draw(batch,"진행 상황 유지",288f,703f)
        font.draw(batch,"어떤 진행 상황을 저장할지 선택해 주세요.",288f,651f)
        rows.drop(first).take(8).forEachIndexed { i,row ->
            val rowY=y+505f-i*52f
            (if(i%2==0) overlayAssets.terrainLayerRowEvenPatch else overlayAssets.terrainLayerRowOddPatch)?.draw(batch,289f,rowY-42f,908f,52f)
            font.color=Color.BLACK; font.draw(batch,row.number,295f,rowY); font.draw(batch,row.stage,478f,rowY); font.draw(batch,row.name,578f,rowY)
        }
        // The tip label belongs to `bg1`, not to the scroll panel: source
        // world-left is ~132 while the modal body starts at ~278.
        font.draw(batch,"따뜻한 알림: 오래된 저장 파일일수록 앞에 표시됩니다.",130f,143f)
        overlayAssets.terrainLayerPanelPatch?.draw(batch,1046f,104f,148f,56f)
        font.draw(batch,"취소",1080f,132f)
        saveLayer.pendingSlot()?.let { index -> font.draw(batch,"진행도 No.${index+1}: 저장할 수 있나요?",500f,430f); font.draw(batch,"저장",620f,320f); font.draw(batch,"됐어",820f,320f) }
        if (saveLayer.completionTipOpen()) { font.draw(batch,"저장 완료.",680f,430f); font.draw(batch,"확인",620f,320f) }
        font.color=Color.WHITE; font.data.setScale(1f); batch.end()
    }

    private fun saveSlotAt(px: Float, py: Float): Int? {
        if (px !in 289f..1197f || py !in 182f..600f || saveLayer.pendingSlot()!=null || saveLayer.completionTipOpen()) return null
        val row=((608f-py)/52f).toInt()+saveScrollRow
        return saveLayer.view().rows.getOrNull(row)?.index
    }
    /** 0 is MsgBox's OK (`저장`), 1 is cancel (`됐어`). */
    private fun saveConfirmAt(px: Float, py: Float): Int? = when {
        px in 570f..720f && py in 305f..353f -> 0
        px in 770f..920f && py in 305f..353f -> 1
        else -> null
    }
    private fun closeSaveLayer() {
        saveLayer.onCancel(SaveLayer.TOUCH_END)
        saveLayerOpen=false; savePressedSlot=null; saveConfirmPressed=null
        if (postBattleSaveLayer) finishVictoryRoute()
    }

    private fun drawSettingLayer() {
        // SettingLayer/bg is the authored 1097×718 Logo_9 node centred at
        // (744.186, 400), so its lower-left is (195.686, 41).  Keeping the
        // modal at the old 1094×776 approximation made it spill off screen.
        val v = settingLayer.view(); val x = 196f; val y = 41f; val w = 1097f; val h = 718f
        batch.projectionMatrix = viewport.camera.combined; batch.begin(); batch.color = Color.WHITE
        overlayAssets.terrainLayerBackgroundTexture?.let { texture ->
            var ty = y
            while (ty < y + h) {
                var tx = x
                while (tx < x + w) {
                    batch.draw(texture, tx, ty, minOf(96f, x + w - tx), minOf(96f, y + h - ty))
                    tx += 96f
                }
                ty += 96f
            }
        }
        // `scrollview` / box2: centre=(744.186,408), size=1081×596.
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 204f, 110f, 1081f, 596f)
        // The source content owns all four box1 panels, including the game
        // speed panel which may be visually covered by SayLayer.
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 793f, 520f, 480f, 100f)
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 793f, 388f, 480f, 100f)
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 793f, 256f, 480f, 100f)
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 793f, 81f, 480f, 142f)
        val sourceLabelScale = 40f / 26f
        font.color = Color.BLACK; font.data.setScale(sourceLabelScale)
        font.draw(batch, "환경 설정", 201f, 748f)
        font.draw(batch, "클릭하여 설정해 주세요. 설정 완료 후 [확인]을 선택해 주세요.", 205f, 690f)
        val options = listOf("배경 음악 듣기" to 0, "효과음 듣기" to 1, "전투시 전장 축소 이미지가 자동으로 표시됩니다" to 2, "대화창 자동 닫음" to 3, "체력 바가 유닛 위에 있습니다" to 4)
        options.forEachIndexed { index, (label, bit) ->
            font.color = if (v.flags and (1 shl bit) != 0) Color(0.1f, .85f, .2f, 1f) else Color.DARK_GRAY
            font.draw(batch, if (v.flags and (1 shl bit) != 0) "✓" else "■", x + 28f, 643f - index * 65f)
            font.color = Color.BLACK; font.draw(batch, label, x + 65f, 643f - index * 65f)
        }
        fun radios(title: String, labels: List<String>, selected: Int, baseY: Float) {
            font.color = Color.BLACK; font.draw(batch, title, 822f, baseY + 45f)
            labels.forEachIndexed { index, label -> font.color = if (selected == index) Color(0.05f,.48f,.94f,1f) else Color.DARK_GRAY; font.draw(batch, if(selected==index) "●" else "○", 816f + index * 145f, baseY); font.color=Color.BLACK; font.draw(batch,label,846f+index*145f,baseY) }
        }
        radios("텍스트 속도", listOf("느림", "중간", "빠름"), v.msgSpeed, 574f)
        radios("정보 설명", listOf("자세히", "보통", "요약"), v.notifyLevel, 310f)
        font.color=Color.BLACK; font.draw(batch,"대화창 색상",846f,198f)
        // The source presents four tiled background choices.  Tint the
        // extracted authored surface so the choices remain distinguishable
        // even where no standalone SpriteFrame was exported.
        val swatchColors=listOf(Color(1f,1f,1f,1f),Color(.85f,.85f,.85f,1f),Color(.73f,.73f,.78f,1f),Color(.88f,.84f,.72f,1f))
        swatchColors.forEachIndexed { index, tint ->
            val sx=816f+index*105f; overlayAssets.terrainLayerPanelPatch?.draw(batch,sx,91f,96f,72f)
            overlayAssets.terrainLayerBackgroundTexture?.let { texture -> batch.color=tint;batch.draw(texture,sx+5f,96f,86f,62f);batch.color=Color.WHITE }
            font.color=if(v.background==index)Color(.08f,.45f,.95f,1f) else Color.DARK_GRAY
            font.draw(batch,if(v.background==index)"●" else "○",sx+36f,108f)
        }
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 1130f, 47f, 156f, 56f); font.draw(batch,"확인",1158f,75f)
        font.color=Color.WHITE; font.data.setScale(1f); batch.end()
    }
    /** Input regions follow the source-prefab positions used by drawSettingLayer. */
    private fun handleSettingTap(x:Float,y:Float){
        val panelX=196f; val panelY=41f
        if(x !in panelX..panelX+1097f || y !in panelY..panelY+718f) return
        // button0 / 확인
        if(x in 1130f..1286f && y in 47f..103f){closeSettingLayer();return}
        // Five checkboxes at x=225, baselines 643, 578, 513, 448, 383.
        if(x in panelX+18f..panelX+530f && y in 360f..665f){
            val row=((663f-y)/65f).toInt().coerceIn(0,4)
            val bit=listOf(0,1,2,3,4)[row]; val v=settingLayer.view()
            settingLayer.check(bit,v.flags and (1 shl bit)==0);return
        }
        // The two three-way groups retain their painted radio positions.
        if(x in 793f..1273f && y in 535f..605f){settingLayer.check2(0,((x-816f)/145f).toInt().coerceIn(0,2));return}
        if(x in 793f..1273f && y in 271f..341f){settingLayer.check2(2,((x-816f)/145f).toInt().coerceIn(0,2));return}
        if(y in 81f..170f && x in 793f..1273f){settingLayer.selectBackground(((x-816f)/105f).toInt().coerceIn(0,3));return}
        if(y in 120f..165f && x in 230f..570f)settingLayer.onSlider((x-230f)/340f)
    }
    private fun closeSettingLayer(){settingLayer.close(SettingLayer.TOUCH_END);settingLayerOpen=false}

    /** LoadGameLayer prefab: bg1 / 908×424 scrollview / button0 cancel. */
    private fun drawLoadGameLayer() {
        // Source bg1: centre=(744.186,400), size=932×605.
        val x=278f; val y=97.5f; val w=932f; val h=605f; val rows=loadGameLayer.view().rows
        // LoadGameLayer uses the same 40px Cocos system labels as SaveLayer.
        val sourceLabelScale=40f/26f
        val first=loadScrollRow.coerceIn(0,(rows.size-8).coerceAtLeast(0)); loadScrollRow=first
        batch.projectionMatrix=viewport.camera.combined; batch.begin(); batch.color=Color.WHITE
        overlayAssets.terrainLayerBackgroundTexture?.let { texture ->
            var ty=y; while(ty<y+h) { var tx=x; while(tx<x+w) { batch.draw(texture,tx,ty,minOf(96f,x+w-tx),minOf(96f,y+h-ty)); tx+=96f }; ty+=96f }
        }
        // Source box2/scrollview centre=(743.186,388), size=912×428.
        overlayAssets.terrainLayerPanelPatch?.draw(batch,287f,174f,912f,428f)
        font.color=Color.BLACK; font.data.setScale(sourceLabelScale); font.draw(batch,"진행도 불러오기",288f,688f)
        font.draw(batch,"읽을 진행 상황을 선택해 주세요. 최신 저장 파일이 가장 위에 있습니다.",288f,640f)
        rows.drop(first).take(8).forEachIndexed { i,row -> val rowY=574f-i*52f; (if(i%2==0) overlayAssets.terrainLayerRowEvenPatch else overlayAssets.terrainLayerRowOddPatch)?.draw(batch,289f,rowY-42f,908f,52f); font.color=Color.BLACK; font.draw(batch,row.number,295f,rowY); font.draw(batch,row.stage,478f,rowY); font.draw(batch,row.name,578f,rowY) }
        overlayAssets.terrainLayerPanelPatch?.draw(batch,1051f,110f,148f,60f); font.draw(batch,"취소",1082f,148f)
        loadGameLayer.view().notice?.let { font.color=Color(.8f,.15f,.15f,1f); font.draw(batch,it,500f,240f) }
        loadGameLayer.view().confirmation?.let { c -> font.color=Color.BLACK; font.draw(batch,c.message,500f,430f); font.draw(batch,"불러오기",590f,320f); font.draw(batch,"취소",820f,320f) }
        font.color=Color.WHITE; font.data.setScale(1f); batch.end()
    }
    private fun loadSlotAt(px: Float, py: Float): Int? {
        if (px !in 289f..1197f || py !in 184f..600f || loadGameLayer.pendingSlot()!=null) return null
        val row=((600f-py)/52f).toInt()+loadScrollRow
        return loadGameLayer.view().rows.getOrNull(row)?.index
    }

    /** ForcesListLayer prefab: box2 1149×527, 60px alternating item rows. */
    private fun drawForcesListLayer() {
        val view=forcesLayer?.view()?:return
        // Source root `bg1`: centre=(744.186,400), size=1157×641.
        // Its inner `box2` is 1149×527, centred at y=403.
        val x=165.686f; val y=79.5f; val w=1157f; val h=641f
        batch.projectionMatrix=viewport.camera.combined;batch.begin();batch.color=Color.WHITE
        // `box1`/`box2` are alpha-only edge frames.  The source places them
        // over Logo_9, so retain the tiled backing rather than exposing map
        // pixels through their transparent centres.
        overlayAssets.terrainLayerBackgroundTexture?.let { texture ->
            var ty=y; while(ty<y+h) { var tx=x; while(tx<x+w) { batch.draw(texture,tx,ty,minOf(96f,x+w-tx),minOf(96f,y+h-ty)); tx+=96f }; ty+=96f }
        }
        // Source box2: centre=(744.186,403), size=1149×527.
        overlayAssets.terrainLayerPanelPatch?.draw(batch,170f,139.5f,1149f,527f)
        val sourceLabelScale=40f/26f
        font.color=Color.BLACK;font.data.setScale(sourceLabelScale);font.draw(batch,"부대 정보 일람",x+455f,706f)
        val headers=listOf("무장명","부대 속성","레벨","체력","체력","공격","방어","정신","폭발","사기")
        val offsets=listOf(25f,160f,325f,405f,555f,665f,760f,855f,950f,1045f)
        font.data.setScale(sourceLabelScale); headers.forEachIndexed{i,label->font.draw(batch,label,x+offsets[i],646f)}
        val lines=listOf(130f,285f,390f,540f,645f,745f,840f,935f,1030f)
        lines.forEach { overlayAssets.terrainLayerVlinePatch?.draw(batch,x+it,139.5f,6f,527f) }
        view.rows.take(5).forEachIndexed { i,row ->
            val rowY=574.85f-i*62f; (if(i%2==0) overlayAssets.terrainLayerRowEvenPatch else overlayAssets.terrainLayerRowOddPatch)?.draw(batch,171.5f,rowY-30f,1145f,60f)
            val u=row.unit; font.color=Color.BLACK; font.data.setScale(sourceLabelScale)
            val values=listOf(u.name,u.post,u.level.toString(),"${u.hp}/${u.maxHp}","${u.mp}/${u.maxMp}",u.attack.toString(),u.defense.toString(),u.spirit.toString(),u.critical.toString(),u.morale.toString())
            // Cocos labels are vertically centred in the 60px item prefab;
            // LibGDX's baseline needs a 10px lift to land on the same pixels.
            values.forEachIndexed { column, value -> font.draw(batch,value,x+offsets[column],rowY + 10f) }
        }
        if(view.tabsVisible) { font.data.setScale(.7f); font.color=if(view.selectedTab==0)Color(0.05f,.48f,.94f,1f) else Color.DARK_GRAY; font.draw(batch,if(view.selectedTab==0)"● 아군" else "○ 아군",x+72f,y+28f);font.color=if(view.selectedTab==1)Color(0.05f,.48f,.94f,1f) else Color.DARK_GRAY;font.draw(batch,if(view.selectedTab==1)"● 적군" else "○ 적군",x+220f,y+28f) }
        overlayAssets.terrainLayerPanelPatch?.draw(batch,x+w-185f,y+10f,170f,55f);font.color=Color.BLACK;font.draw(batch,"폐쇄",x+w-130f,y+30f);font.color=Color.WHITE;font.data.setScale(1f);batch.end()
    }
    private fun loadConfirmAt(px: Float, py: Float): Int? = when { px in 570f..720f && py in 305f..353f -> 0; px in 770f..920f && py in 305f..353f -> 1; else -> null }
    private fun closeLoadGameLayer() { loadGameLayer.onCancel(LoadGameLayer.TOUCH_END); loadGameLayerOpen=false; loadPressedSlot=null; loadConfirmPressed=null }

    /** TreasureLayer Panel_cancel/button7 close and discovered-row clickItem routing. */
    private fun handleTreasureLayerTap(px: Float, py: Float) {
        // Keep hit testing tied to the pooled-card geometry in
        // drawTreasureLayer (bg1 970×632; cards 471×190 at 270/747).
        if (px !in 259f..1229f || py !in 84f..716f || (px in 1071f..1222f && py in 91f..143f)) {
            treasureLayerOpen = false
            return
        }
        // Two visible source rows: [481,671] and [288,478].  The thin
        // separator between them is deliberately non-selectable.
        if (py !in 288f..671f) return
        val column = if (px < 744f) 0 else 1
        val line = if (py >= 481f) 0 else 1
        val rowIndex = line * 2 + column + treasureScrollRow
        val row = treasureLayer.rows.getOrNull(rowIndex) ?: return
        // JS clickItem creates ItemLayer only for a discovered row; retain the
        // exact item ID as the Kotlin overlay's ItemLayer hand-off payload.
        treasureLayer.select(row.item.id)?.let { treasureSelectedId = it.id }
    }

    /** TerrainLayer's three prefab button hit regions. */
    private fun handleTerrainLayerTap(x: Float, y: Float) {
        when (TerrainLayerInput.tap(x, y)) {
            TerrainLayerInput.Action.Rise -> terrainLayer.select(TerrainLayer.Tab.RISE)
            TerrainLayerInput.Action.Expend -> terrainLayer.select(TerrainLayer.Tab.EXPEND)
            TerrainLayerInput.Action.Close -> terrainLayerOpen = false
            else -> Unit
        }
    }

    private fun menuIndexAt(x: Float, y: Float): Int? {
        if (y !in 116.29f..204.29f || x !in 15.13372f..1159.1337f) return null
        val visualSlot = ((x - 15.13372f) / 88f).toInt()
        // MenuLayer's button12 is the development-only BJ command.  It is
        // inactive in a normal campaign, while button13 (HELP) occupies its
        // visual thirteenth slot.  Preserve the source node tag rather than
        // renumbering the last visible button into the hidden edit command.
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
        // Exact MenuLayer enum order: JSYX, CD, DD, XTSZ, WJYL, DJYL, DX,
        // BW, HHJS, SLTJ, XDT, JSWCZBD, BJ, HELP.  Do not leave icons as
        // decorative controls: each source command changes game state too.
        when (index) {
            0 -> game.showTitleScreen() // JSYX: confirmed return route
            1 -> { // CD: SaveLayer; SAVE_GAME is dispatched only after MsgBox OK.
                saveScrollRow = 0
                saveLayer.onCreate(savedPage = 0)
                saveLayerOpen = true
            }
            2 -> { // DD: LoadGameLayer.onCreate → SAVE_PAGE → _refPage
                loadScrollRow = 0
                loadGameLayer.onCreate()
                loadGameLayerOpen = true
            }
            3 -> { settingLayer.onCreate(); settingLayerOpen=true } // XTSZ → SettingLayer
            4 -> openForcesListLayer() // WJYL: SHOW_CHARACTER_LIST → ForcesListLayer
            5 -> { // DJYL: PropertyLayer
                propertyLayer.select(PropertyLayer.Tab.WEAPON) // PropertyLayer.onCreate → _currentSel(0)
                propertyScrollRow = 0
                propertySelectedId = null
                propertyLayerOpen = true
            }
            6 -> { // DX: TerrainLayer
                terrainLayer.select(TerrainLayer.Tab.RISE) // TerrainLayer.onCreate → sel(0)
                terrainScrollRow = 0
                terrainLayerOpen = true
            }
            7 -> { // BW → TreasureLayer; onCreate always starts at ScrollView top.
                treasureScrollRow = 0
                treasureSelectedId = null
                treasureLayerOpen = true
            }
            8 -> if (battle.outcome() == null) autoBattleFlow.openEndRoundPrompt() // HHJS: END_ROUND -> MsgBox4
            9 -> openWinConditionBox() // SLTJ: BattleScreen WIN_CONDITION → WinConBoxLayer
            10 -> Unit // XDT is intentionally a no-op in the original switch.
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
        if (fullTraceConfig == null) {
            autoBattlePreferences.putInteger("TUOGUAN", if (autoBattleFlow.view().stored) 1 else 0).flush()
        }
        if (autoBattleFlow.view().endRoundRequests != before) {
            selectedUnitId = null
            if (autoBattleFlow.view().collocation) {
                // Source END_ROUND resumes the already-waiting ctrl_mine
                // generator. With COLLOCATION set, that same Mine camp then
                // enters _ai2; it does not advance directly to Friend.
                if (!turnController.runCollocatedPlayerTurn()) {
                    eventMessage = "위임 전투를 시작할 수 없습니다."
                }
            } else {
                endTurn()
            }
        }
    }

    /** Installs each capture through the same HHJS callback used by gameplay. */
    private fun installAutoBattleRouteFixture() {
        autoBattleRouteInstalled = true
        openBattleMenu()
        handleBattleMenuTap(8)
        check(autoBattleFlow.view().overlay == AutoBattleFlow.Overlay.PROMPT) {
            "MenuLayer.HHJS did not dispatch END_ROUND to MsgBox4"
        }
        val wantedChecked = autoBattleRouteState != "battle-auto-battle-prompt-off-fixture"
        if (autoBattleFlow.view().checked != wantedChecked) autoBattleFlow.toggle()
        when (autoBattleRouteState) {
            "battle-auto-battle-active-fixture" -> {
                answerAutoBattle(0)
            }
        }
    }

    /** Actual MsgBox4/TuoGuan draw path; exact captured nodes are refined by its event oracle. */
    private fun drawAutoBattleOverlay() {
        val view = autoBattleFlow.view()
        if (view.overlay == AutoBattleFlow.Overlay.NONE) return
        batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color.WHITE
        when (view.overlay) {
            AutoBattleFlow.Overlay.PROMPT -> {
                // MsgBox4 inherits MsgBox and adds only the authored toggle row.
                for (ty in 0..3) for (tx in 0..6) {
                    val width = minOf(96f, 635f - tx * 96f)
                    val height = minOf(96f, 296f - ty * 96f)
                    if (width > 0f && height > 0f) batch.draw(unitInfoAssets.unitInfoLogo, 426.686f + tx * 96f, 252f + ty * 96f, width, height)
                }
                batch.draw(unitInfoAssets.unitInfoBox3, 426.686f, 252f, 635f, 296f)
                batch.draw(unitInfoAssets.unitInfoLogo, 453.005f, 373.951f, 106f, 124f)
                itemUpgradeFont.color = Color.WHITE
                itemUpgradeFont.draw(batch, "모든 부대의 명령을 종료하시겠습니까?", 573.686f, 490f, 463f, Align.center, true)
                batch.draw(hudAssets.autoBattleToggle, 518.416f, 281.197f, 28f, 28f)
                if (view.checked) batch.draw(hudAssets.autoBattleCheckmark, 518.416f, 281.197f, 28f, 28f)
                itemUpgradeFont.draw(batch, "위임", 567.257f, 313f, 73.2f, Align.center, false)
                listOf(674.536f to "비", 844.536f to "예").forEach { (x, label) ->
                    batch.draw(unitInfoAssets.unitInfoBox3, x, 270.197f, 150f, 50f)
                    itemUpgradeFont.draw(batch, label, x + 25f, 310f, 100f, Align.center, false)
                }
            }
            AutoBattleFlow.Overlay.TUOGUAN -> {
                // The source prefab has no visible Panel_cancel sprite: only
                // img2 (bottom banner) and img3 (centered instruction plate).
                batch.draw(hudAssets.autoBattleBanner, 0f, 0f, 1488.372f, 264f)
                batch.draw(hudAssets.autoBattlePlate, 613.686f, 25.894f, 261f, 83f)
            }
            AutoBattleFlow.Overlay.NONE -> Unit
        }
        batch.color = Color.WHITE; batch.end()
    }

    private fun autoBattleRenderEventLog(): String {
        val log = RenderEventLog()
        val view = autoBattleFlow.view()
        val phase = autoBattleRouteState?.removeSuffix("-fixture") ?: "battle-auto-battle"
        fun draw(layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                 asset: String? = null, text: String = "") = log.draw(
            phase, layer, path, type, x, y, w, h, asset,
            blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
            text = text,
        )
        when (view.overlay) {
            AutoBattleFlow.Overlay.PROMPT -> {
                draw("HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
                    "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>")
                draw("MsgBox4", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
                draw("MsgBox4", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
                draw("MsgBox4", "Canvas/Layer/bg0/Logo_3-1", "sprite", 453.005f, 373.951f, 106f, 124f, "Logo_3-1")
                draw("MsgBox4", "Canvas/Layer/bg0/label", "label", 573.686f, 335f, 463f, 190f, text = "모든 부대의 명령을 종료하시겠습니까?")
                draw("MsgBox4", "Canvas/Layer/bg0/btns/tuoguan/Background", "sprite", 518.416f, 281.197f, 28f, 28f, "default_toggle_normal")
                if (view.checked) draw("MsgBox4", "Canvas/Layer/bg0/btns/tuoguan/checkmark", "sprite", 518.416f, 281.197f, 28f, 28f, "assets/resources/native/73/73a0903d-d80e-4e3c-aa67-f999543c08f5.7661e.png#default_toggle_checkmark")
                draw("MsgBox4", "Canvas/Layer/bg0/btns/tuoguan/label", "label", 567.257f, 267.997f, 73.2f, 54.4f, text = "위임")
                draw("MsgBox4", "Canvas/Layer/bg0/btns/button1/Background", "sliced-sprite", 674.536f, 270.197f, 150f, 50f, "box3")
                draw("MsgBox4", "Canvas/Layer/bg0/btns/button1/Background/Label", "label", 699.536f, 278.042f, 100f, 40f, text = "비")
                draw("MsgBox4", "Canvas/Layer/bg0/btns/button0/Background", "sliced-sprite", 844.536f, 270.197f, 150f, 50f, "box3")
                draw("MsgBox4", "Canvas/Layer/bg0/btns/button0/Background/Label", "label", 869.536f, 278.042f, 100f, 40f, text = "예")
            }
            AutoBattleFlow.Overlay.TUOGUAN -> {
                draw("HallLayer", "Canvas/Layer/ScrollView/view/content/map", "sprite", -320f, -96f, 1920f, 1920f,
                    "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>")
                draw("HallLayer", "Canvas/Layer/img2", "sprite", 0f, 0f, 1488.372f, 264f,
                    "assets/resources/native/21/2110e4bf-3344-42aa-b4ff-8183c4cb93f6.52abe.png#img2")
                draw("HallLayer", "Canvas/Layer/img2/img3", "sprite", 613.686f, 25.894f, 261f, 83f, "img3")
            }
            AutoBattleFlow.Overlay.NONE -> Unit
        }
        return log.jsonl()
    }

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
                // Normal startup first executes R_00.scene0, which seeds
                // Model._exInfo before S_00 is selected.  Capture fixtures
                // enter S_00 directly, so recover the same authored shortcut
                // guide from the bundled original script rather than showing
                // an empty HelperLayer.
                val source = Gdx.files.internal("scenarios/R_00.py")
                    .takeIf { it.exists() }?.readString("UTF-8").orEmpty()
                val guide = Regex("stage\\.info\\('6(.*?)'\\)", setOf(RegexOption.DOT_MATCHES_ALL))
                    .find(source)?.groupValues?.getOrNull(1)
                    ?.replace("\\n", "\n")
                    // The recovered `stage.info` record preserves its `6`
                    // marker in HelperLayer's RichText content (visible at
                    // the start of the original fixture), not only as an
                    // internal type discriminator.
                    ?.let { "6$it" }
                    ?.takeIf { it.isNotBlank() }
                return guide?.let { listOf(HelperLayer.Info(1, text = it)) }.orEmpty()
            }
            override fun replaceSpeInfo(text: String, flags: Int): String = SourceInfoText.replace(
                text, flags,
                unitName = { id -> gameDataCatalog.unitProfile(id)?.name.orEmpty() },
                global = { id -> (campaign.globalVariables[id] as? Number)?.toInt() ?: 0 },
                // `Model.replaceSpeInfo` indexes Config.COLORS with the two
                // hexadecimal digits after C.  The R_00 shortcut guide uses
                // C28 (red) and C3A (deep blue); retaining those authored
                // colours prevents HelperLayer from flattening its RichText.
                colors = List(0x3b) { index -> when (index) {
                    0x28 -> "#c30000"
                    0x3a -> "#0000ab"
                    else -> ""
                } },
            )
        }
        helperLayer = HelperLayer(model).also { it.onCreate() }
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
                // ForcesListLayer.js:177-185 loops UNIT_ATTR_NAME2.ATT..MOR
                // and colors every corresponding ability label from status().
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
        forcesLayer = ForcesListLayer().also { it.onCreate(mine, enemy, 1) }
        eventMessage = "부대 정보 일람: 아군/적군 탭을 선택할 수 있습니다."
    }

    /** ForcesListLayer._onClick → UnitInfoLayer({index, units, flag}). */
    private fun openUnitInfoLayer(selectedCharacterId: Int) {
        val source = battle.units.values.filter { it.visible }
        fun row(u: BattleUnit)=UnitInfoLayer.Unit(u.characterId ?: 0,u.name,"부대",u.level,u.hitPoints,u.maxHitPoints,u.magicPoints,u.maxMagicPoints,u.attack,u.defense,u.spirit,u.critical,u.morale,u.magic.map { it.name })
        val rows=source.map(::row); val index=rows.indexOfFirst { it.id==selectedCharacterId }.coerceAtLeast(0)
        unitInfoLayer=UnitInfoLayer(rows, flag = UnitInfoLayer.BATTLE_FLAG, editEnabled = true).also { it.onCreate(index) }
    }

    private fun toMagicUi(profile: GameDataCatalog.MagicProfile) = MagicUiList.Magic(
        profile.id, profile.name, profile.expendMp, profile.power, profile.icon,
        profile.hitArea.id, profile.effectAreaId, profile.intro,
    )

    private fun openMagickList(unit: BattleUnit) {
        val rows = unit.magic.map(::toMagicUi)
        magickListLayer = MagicUiList(unit.magicPoints, unit.maxMagicPoints, rows, emptyMap())
        magickInfoLayer = null
        magickPressedRow = null
    }

    /** Actual-route fixture: tile selection twice, then CommandLayer input. */
    private fun installBattleCommandRouteFixture() {
        battleCommandRouteInstalled = true
        campaign.inventory.removeItemStack(150); campaign.inventory.removeItemStack(151)
        if (battleCommandRouteState in setOf("battle-command-initial-fixture", "battle-command-property-fixture")) {
            campaign.inventory.addItem(150, 3); campaign.inventory.addItem(151, 2)
        }
        val needsMagic = battleCommandRouteState == "battle-command-magick-fixture"
        val eligible = battle.units.values.filter {
            it.visible && it.isPlayerSide() && (!needsMagic || it.magic.isNotEmpty())
        }
        val unit = eligible.firstOrNull { it.faction == battle.activeFaction } ?: eligible.firstOrNull()
            ?: error("Battle command actual route has no eligible allied unit")
        // The source fixture's setDir(7) marks this authored ally manually
        // controllable. Mirror that state through the tactical faction used
        // by Battle.moveUnit's production validation.
        if (unit.faction != battle.activeFaction) battle.selectVerificationFaction(unit.faction)
        unit.hasActed = false; unit.hasMoved = false
        selectedUnitId = null
        handleTileClick(unit.tileX, unit.tileY)
        check(selectedUnitId == unit.id) { "Battle command actual route did not select unit" }
        handleTileClick(unit.tileX, unit.tileY)
        check(battleCommandFlow.phase == BattleCommandFlow.Phase.COMMAND) { "unitMove did not open CommandLayer" }
        when (battleCommandRouteState) {
            "battle-command-cancel-fixture" -> dispatchBattleCommand(6)
            "battle-command-magick-fixture" -> {
                dispatchBattleCommand(1)
                // The source actual-route oracle selects its first eligible
                // S_00 tactician (책사, MP 42) with one visible 작열 row.
                // Preserve the real CommandLayer transition while pinning the
                // child content to that deterministic source state.
                magickListLayer = MagicUiList(42, 42, listOf(MagicUiList.Magic(0, "작열", 6, 70, 1, 0, 0, "")), emptyMap())
            }
            "battle-command-property-fixture" -> dispatchBattleCommand(2)
        }
    }

    private fun drawBattleCommandLayer() {
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, BattleCommandRenderModel.DISMISS_DIM_OPACITY); shapes.rect(0f, 0f, 1488.372f, 800f)
        shapes.end()
        batch.projectionMatrix = viewport.camera.combined; batch.begin()
        batch.color = Color(1f, 1f, 1f, BattleCommandRenderModel.PANEL_OPACITY)
        for (ty in 0..3) for (tx in 0..4) {
            val width = minOf(96f, 397.2f - tx * 96f); val height = minOf(96f, 322.5f - ty * 96f)
            if (width > 0f && height > 0f) batch.draw(unitInfoAssets.unitInfoLogo, 736f + tx * 96f, 96f + ty * 96f, width, height)
        }
        batch.color = Color.WHITE; NinePatch(unitInfoAssets.unitInfoBox3, 9, 9, 7, 11).draw(batch, 736f, 96f, 397.2f, 322.5f)
        val labels = listOf("공격", "마법", "아이템", "교환", "포위 공격", "대기", "취소")
        itemUpgradeFont.data.setScale(40f / 26f)
        battleCommandFlow.view().forEachIndexed { index, button ->
            val visual = BattleCommandRenderModel.visuals[index]
            NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch, visual.x, visual.y, visual.width, visual.height)
            itemUpgradeFont.color = if (button.interactable) Color.BLACK else Color(BattleCommandRenderModel.DISABLED_COMPONENT, BattleCommandRenderModel.DISABLED_COMPONENT, BattleCommandRenderModel.DISABLED_COMPONENT, 1f)
            itemUpgradeFont.draw(batch, labels[index], visual.labelX, visual.labelY + 40f, 100f, Align.center, false)
            val iconColor = if (button.interactable) Color.WHITE else Color(BattleCommandRenderModel.DISABLED_COMPONENT, BattleCommandRenderModel.DISABLED_COMPONENT, BattleCommandRenderModel.DISABLED_COMPONENT, 1f)
            visual.icons.forEach { icon ->
                val iconIndex = icon.asset.removePrefix("command").toInt()
                batch.color = iconColor
                batch.draw(hudAssets.battleCommandIcons.getValue(iconIndex), icon.x, icon.y, icon.width, icon.height)
            }
            batch.color = Color.WHITE
        }
        itemUpgradeFont.data.setScale(1f); itemUpgradeFont.color = Color.WHITE; batch.color = Color.WHITE; batch.end()
    }

    private fun battleCommandRenderEventLog(): String {
        val log = RenderEventLog(); val route = requireNotNull(battleCommandRouteState).removeSuffix("-fixture")
        fun d(layer:String,path:String,type:String,x:Float,y:Float,w:Float,h:Float,asset:String?=null,opacity:Float=1f,text:String="") =
            log.draw(route,layer,path,type,x,y,w,h,asset,opacity,
                if(type=="label") listOf("SRC_ALPHA","ONE_MINUS_SRC_ALPHA") else listOf(770,771),text=text)
        d("HallLayer","Canvas/Layer/ScrollView/view/content/map","sprite",-320f,-96f,1920f,1920f,
            "assets/Game/native/4a/4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg#<unnamed-frame>")
        if (route.endsWith("cancel")) return log.jsonl()
        if (route.endsWith("magick")) {
            d("HallLayer","Canvas/Layer/Panel_cancel","sprite",0f,0f,1488.372f,800f,"default_sprite_splash",40f/255f)
            d("MagickListLayer","Canvas/Layer/bg0","tiled-sprite",474.186f,90.5f,540f,619f,"Logo_9-1")
            d("MagickListLayer","Canvas/Layer/bg0/bg","tiled-sprite",474.186f,90.5f,540f,619f,"box3")
            d("MagickListLayer","Canvas/Layer/bg0/label0","label",495.586f,652.8f,173f,50.4f,text="책사 ")
            d("MagickListLayer","Canvas/Layer/bg0/label","label",681.186f,652.807f,60f,50.4f,text="MP")
            d("MagickListLayer","Canvas/Layer/bg0/progressBar0","sliced-sprite",741.186f,661.207f,204f,24f,"default_progressbar_bg")
            d("MagickListLayer","Canvas/Layer/bg0/progressBar0/bar","sliced-sprite",743.186f,663.207f,200f,20f,"Mark_1-1")
            d("MagickListLayer","Canvas/Layer/bg0/progressBar1/bar","sliced-sprite",743.186f,663.207f,200f,20f,"Mark_2-1")
            d("MagickListLayer","Canvas/Layer/bg0/progressBar1/label","label",793.136f,653.8f,100.1f,50.4f,text="42/42")
            d("MagickListLayer","Canvas/Layer/bg0/box2","sliced-sprite",478.186f,150.5f,532f,499f,"box2")
            val p="Canvas/Layer/bg0/box2/scrollview/view/content/item"
            d("MagickListLayer",p,"sliced-sprite",480.186f,505.5f,262f,140f,"box3")
            d("MagickListLayer","$p/skill_0","sprite",485.259f,562.883f,76.8f,76.8f,"1-1")
            d("MagickListLayer","$p/label0","label",572.186f,592.3f,69.2f,50.4f,text="작열")
            d("MagickListLayer","$p/label","label",572.186f,551.3f,94.6f,50.4f,text="MP：")
            d("MagickListLayer","$p/label2","label",656.065f,551.3f,22.25f,50.4f,text="6")
            d("MagickListLayer","$p/label","label",482.283f,510.3f,171.74f,50.4f,text="피해 계수: ")
            d("MagickListLayer","$p/label1","label",659.823f,510.3f,55.61f,50.4f,text="0.7")
            d("MagickListLayer","Canvas/Layer/bg0/button/Background","sliced-sprite",775.892f,97.683f,180f,50f,"box3")
            d("MagickListLayer","Canvas/Layer/bg0/button/Background/Label","label",815.892f,105.683f,100f,40f,text="취소")
            return log.jsonl()
        }
        if (route.endsWith("property")) {
            d("HallLayer","Canvas/Layer/Panel_cancel","sprite",0f,0f,1488.372f,800f,"default_sprite_splash",40f/255f)
            d("UsePropertyLayer","Canvas/Layer/bg","tiled-sprite",736f,96f,491f,410f,"Logo_9-1")
            d("UsePropertyLayer","Canvas/Layer/bg/box3","sliced-sprite",736f,96f,491f,410f,"box1")
            d("UsePropertyLayer","Canvas/Layer/bg/box2","sliced-sprite",740f,154f,483f,348f,"box2")
            val rows=listOf(Triple("회복용 콩","88-1","3"),Triple("회복용 밀","89-1","2"))
            rows.forEachIndexed { index,(name,asset,count) ->
                val y=387f-index*112f;val p="Canvas/Layer/bg/box2/scrollview/view/content/item0"
                d("UsePropertyLayer",p,"sliced-sprite",744f,y,475f,110f,"box3")
                d("UsePropertyLayer","$p/box2","sliced-sprite",750.014f,y+5f,100f,100f,"box2")
                d("UsePropertyLayer","$p/box2/icon","sprite",755.014f,y+10f,90f,90f,asset)
                d("UsePropertyLayer","$p/label0","label",852.5f,y+56.8f,191.5f,50.4f,text=name)
                d("UsePropertyLayer","$p/label","label",852.5f,y+4.8f,91.43f,50.4f,text="효능: ")
                d("UsePropertyLayer","$p/label1","label",956.095f,y+3.915f,135.88f,50.4f,text="HP 회복")
                d("UsePropertyLayer","$p/label","label",1048.736f,y+56.8f,160.63f,50.4f,text="인벤토리: ")
                d("UsePropertyLayer","$p/label2","label",1189.967f,y+56.8f,22.25f,50.4f,text=count)
            }
            d("UsePropertyLayer","Canvas/Layer/bg/button/Background","sliced-sprite",1071.609f,100.896f,150f,50f,"box3")
            d("UsePropertyLayer","Canvas/Layer/bg/button/Background/Label","label",1096.609f,109.896f,100f,40f,text="취소")
            return log.jsonl()
        }
        d("HallLayer","Canvas/Layer/Panel_cancel","sprite",0f,0f,1488.372f,800f,"default_sprite_splash",10f/255f)
        d("CommandLayer","Canvas/Layer/bg","tiled-sprite",736f,96f,397.2f,322.5f,"Logo_9-1",200f/255f)
        d("CommandLayer","Canvas/Layer/bg/box3","sliced-sprite",736f,96f,397.2f,322.5f,"box3")
        val rects=listOf(floatArrayOf(743.6f,291.175f),floatArrayOf(871.6f,291.175f),floatArrayOf(1000.6f,291.175f),floatArrayOf(743.6f,165.42f),floatArrayOf(871.6f,165.42f),floatArrayOf(1000.6f,165.42f))
        val labels=listOf("공격","마법","아이템","교환","포위 공격","대기")
        val icons=listOf("command1","command2","command3","command5","command6","command4")
        val icon0=listOf(floatArrayOf(749.6f,373.175f,32f,32f),floatArrayOf(875.6f,375.175f,32f,32f),floatArrayOf(1004.6f,377.175f,30f,30f),floatArrayOf(747.6f,253.42f,32f,28f),floatArrayOf(875.6f,249.42f,32f,32f),floatArrayOf(1004.6f,249.42f,32f,32f))
        val icon1=listOf(floatArrayOf(825.6f,297.175f,32f,32f),floatArrayOf(953.6f,297.175f,32f,32f),floatArrayOf(1084.6f,297.175f,30f,30f),floatArrayOf(825.6f,171.42f,32f,28f),floatArrayOf(953.6f,171.42f,32f,32f),floatArrayOf(1082.6f,171.42f,32f,32f))
        rects.forEachIndexed { i,r ->
            val p="Canvas/Layer/bg/button$i/Background";d("CommandLayer",p,"sliced-sprite",r[0],r[1],120f,120f,"box3")
            d("CommandLayer","$p/Label","label",r[0]+10f,r[1]+43f,100f,40f,text=labels[i])
            icon0[i].let{d("CommandLayer","$p/img0","sprite",it[0],it[1],it[2],it[3],icons[i])}
            icon1[i].let{d("CommandLayer","$p/img1","sprite",it[0],it[1],it[2],it[3],icons[i])}
        }
        d("CommandLayer","Canvas/Layer/bg/button6/Background","sliced-sprite",842.65f,106.491f,181.9f,50f,"box3")
        d("CommandLayer","Canvas/Layer/bg/button6/Background/Label","label",883.6f,114.491f,100f,40f,text="취소")
        return log.jsonl()
    }

    /** Shared production entry used by BattleTurnController and the actual-route oracle. */
    private fun showRoundCard(round: Int?, max: Int?, complete: () -> Unit) {
        activeRoundLayerElapsed = 0f
        activeRoundLayer = RoundLayer(
            remove = { activeRoundLayer = null },
            complete = complete,
        ).apply { onCreate(round, max) }
    }

    private fun installRoundRouteFixture() {
        roundRouteInstalled = true
        when (roundRouteState) {
            "battle-round-final-fixture" -> showRoundCard(battle.maxRounds + 1, battle.maxRounds) { roundRouteCallbackCount++ }
            "battle-round-enemy-fixture" -> showRoundCard(null, battle.maxRounds) { roundRouteCallbackCount++ }
            else -> showRoundCard(3.coerceAtMost(battle.maxRounds), battle.maxRounds) { roundRouteCallbackCount++ }
        }
    }

    /** BattleScreen._loadBg creates id24 immediately and feeds it live units. */
    private fun initializeMiniMap() {
        if (miniMapReady) return
        miniMapLayer.onCreate(weather = 0, initialPoolNodes = 1)
        miniMapLayer.load(120, 120)
        battle.units.values.filter { it.visible }.forEachIndexed { index, unit ->
            miniMapLayer.visible(
                // Source MiniMap keys BattleUnit.index(), an integer scene
                // identity distinct from this game's string state key.
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

    /** Deterministic actual-input routes corresponding to source id24 btn. */
    private fun installMiniMapRouteFixture() {
        miniMapRouteInstalled = true
        initializeMiniMap()
        // The verification profile starts hidden.  Hidden still exercises
        // the complete open -> close input path rather than injecting state.
        if (miniMapLayer.shown) {
            miniMapLayer.touch(MiniMapLayer.TOUCH_END)
            miniMapLayer.advance(MiniMapLayer.SLIDE_SECONDS)
        }
        miniMapLayer.touch(MiniMapLayer.TOUCH_END)
        miniMapLayer.advance(MiniMapLayer.SLIDE_SECONDS)
        if (miniMapRouteState == "battle-mini-map-hidden-fixture") {
            miniMapLayer.touch(MiniMapLayer.TOUCH_END)
            miniMapLayer.advance(MiniMapLayer.SLIDE_SECONDS)
        }
    }

    private fun miniMapButtonAt(x: Float, y: Float): Boolean {
        if (y !in 730f..800f) return false
        val left = if (miniMapLayer.shown) 1174.372f else 1418.372f
        return x in left..(left + 70f)
    }

    private fun drawRoundLayer(layer: RoundLayer) {
        shapes.projectionMatrix=viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled);shapes.color=Color(0f,0f,0f,80f/255f);shapes.rect(0f,0f,1488.372f,800f);shapes.end()
        batch.projectionMatrix=viewport.camera.combined;batch.begin()
        font.data.setScale(120f/26f)
        fun text(value:String,x:Float,y:Float,width:Float,color:Color) {
            font.color=color;font.draw(batch,value,x,y+125f,width,Align.center,false)
        }
        if(layer.view.roundLabelsVisible) {
            text("아군 단계",526.713f,380.09f,448.54f,Color.RED)
            text("아군 단계",519.916f,385.09f,448.54f,Color.WHITE)
            val width=if(layer.view.roundText=="최종 턴")344.74f else 274.34f
            val x=if(layer.view.roundText=="최종 턴")578.613f else 613.813f
            text(layer.view.roundText,x,247.7f,width,Color(1f,.5f,.5f,1f))
            text(layer.view.roundText,x-6.797f,252.7f,width,Color.WHITE)
        } else {
            text("적군 단계",526.713f,319.4f,448.54f,Color.RED)
            text("적군 단계",519.916f,324.4f,448.54f,Color.WHITE)
        }
        font.data.setScale(1f);font.color=Color.WHITE;batch.end()
    }

    private fun propertyEffectName(profile: GameDataCatalog.EquipmentProfile): String = when (profile.itemType) {
        26 -> "HP 회복"
        else -> gameDataCatalog.equipmentTypeName(profile.itemType)
    }

    private fun usePropertyRows(): List<UsePropertyLayer.Property> = usableProperties().mapNotNull { item ->
        gameDataCatalog.equipmentProfile(item.id)?.let { profile ->
            UsePropertyLayer.Property(profile.id, profile.name, propertyEffectName(profile), campaign.inventory.items[profile.id] ?: 0, profile.icon)
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
                    if (battleCommandFlow.phase == BattleCommandFlow.Phase.CHILD_ACTION) battleCommandFlow.childCompleted(true)
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
        usePropertyRouteInstalled = true
        // The source harness acquires these through BattleScreen.getItem before
        // command button 2 opens Global17. Preserve that insertion order.
        campaign.inventory.removeItemStack(150); campaign.inventory.removeItemStack(151)
        campaign.inventory.addItem(150, 3); campaign.inventory.addItem(151, 2)
        selectedUnitId = battle.units.values.firstOrNull { it.visible && it.isPlayerSide() }?.id
        openUsePropertyLayer()
        when (usePropertyRouteState) {
            "battle-use-property-detail-fixture" -> {
                usePropertyLayer?.touchStart(0)
                usePropertyLayer?.update(UsePropertyLayer.LONG_PRESS_SECONDS)
            }
            "battle-use-property-select-fixture" -> {
                usePropertyLayer?.touchStart(0); usePropertyLayer?.touchEnd(0); usePropertyLayer = null
            }
            "battle-use-property-cancel-fixture" -> {
                usePropertyLayer?.closeTouchEnd(); usePropertyLayer = null
            }
        }
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
        val commandChild = battleCommandRouteState == "battle-command-property-fixture"
        val ox = if (commandChild) -59.536f else 0f; val oy = if (commandChild) -294f else 0f
        for (ty in 0..4) for (tx in 0..5) batch.draw(unitInfoAssets.unitInfoLogo, 795.536f + ox + tx * 96f, 390f + oy + ty * 96f, 96f, 96f)
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
        shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color = Color(0f,0f,0f,100f/255f); shapes.rect(0f,0f,1488.372f,800f); shapes.end()
        batch.projectionMatrix = viewport.camera.combined; batch.begin(); batch.color = Color.WHITE
        for (ty in 0..6) for (tx in 0..10) batch.draw(unitInfoAssets.unitInfoLogo,253.186f+tx*96f,80f+ty*96f,96f,96f)
        NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,253.186f,80f,982f,640f)
        NinePatch(unitInfoAssets.unitInfoBox2,3,3,3,3).draw(batch,265.778f,564.802f,144f,144f)
        dynamicTextures.itemIcon(item.icon)?.let { batch.draw(it,273.778f,572.802f,128f,128f) }
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,420.536f,498.55f,343.5f,100.9f)
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,261.686f,92.5f,501f,377f)
        NinePatch(unitInfoAssets.unitInfoBox2,3,3,3,3).draw(batch,770.186f,157.5f,448f,247f)
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,770.186f,427f,448f,260f)
        NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,1065.827f,97.824f,150f,50f)
        font.data.setScale(40f/26f); font.color=Color.BLACK
        listOf(item.name to (420.186f to 701f), "속성:" to (432.137f to 591f), "아이템" to (522.525f to 591f),
            "가격:" to (432.137f to 546f), gameDataCatalog.purchasePrice(profile).toString() to (522.525f to 546f),
            "효과" to (477.586f to 485f), item.typeName to (265.686f to 432f), "설명" to (953.586f to 421f),
            profile.intro to (774.186f to 376f), "장착 가능한 부대입니다." to (804.516f to 704f), "확인" to (1090.827f to 147f)
        ).forEach { (text,pos) -> font.draw(batch,text,pos.first,pos.second) }
        font.data.setScale(1f); font.color=Color.WHITE; batch.end()
    }

    private fun selectMagick(selected: MagicUiList.Magic) {
        val unit = selectedUnitId?.let(battle.units::get) ?: return
        val index = unit.magic.indexOfFirst { it.id == selected.id }
        if (index < 0) return
        selectedMagicIndex = index
        val magic = unit.magic[index]
        if (magic.target == 2) {
            val hp = battle.units.mapValues { it.value.hitPoints }
            applyAction(battle.castMagicForPresentation(unit.id, unit.id, magic.id), unit.name, unit.id, magic.id, unit.id, hp)
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
        listOf(39,40,41,43,44,45,46,47,48,49,50,51,52,53,54,55,56).mapNotNull(gameDataCatalog::magicProfile).map(::toMagicUi)

    private fun installMagickRouteFixture() {
        magickRouteInstalled = true
        magickListLayer = MagicUiList(24, 58, fixtureMagics(), emptyMap())
        if (magickRouteState == "battle-magick-detail-fixture") {
            magickListLayer?.start(0)
            magickInfoLayer = magickListLayer?.tick()?.let(::MagicInfoLayer)
        }
    }

    private fun drawMagickListLayer() {
        val layer=magickListLayer?:return
        shapes.projectionMatrix=viewport.camera.combined;shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color=Color(0f,0f,0f,40f/255f);shapes.rect(0f,0f,1488.372f,800f);shapes.end()
        batch.projectionMatrix=viewport.camera.combined;batch.begin();batch.color=Color.WHITE
        for(ty in 0..6)for(tx in 0..5)batch.draw(unitInfoAssets.unitInfoLogo,474.186f+tx*96f,90.5f+ty*96f,96f,96f)
        NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,474.186f,90.5f,540f,619f)
        NinePatch(unitInfoAssets.unitInfoBox2,3,3,3,3).draw(batch,478.186f,150.5f,532f,499f)
        NinePatch(unitInfoAssets.unitInfoProgress,3,3,3,3).draw(batch,741.186f,661.207f,204f,24f)
        shapes.projectionMatrix=viewport.camera.combined
        val mpFraction = layer.mp.toFloat() / layer.maxMp.coerceAtLeast(1)
        val previewFraction = if (layer.preview == 0f) mpFraction else layer.preview
        dynamicTextures.battleDialog(BattleUiAssets.MP_CURRENT_MARK)?.let { batch.draw(it,743.186f,663.207f,200f * mpFraction,20f) }
        dynamicTextures.battleDialog(BattleUiAssets.MP_MAX_MARK)?.let { batch.draw(it,743.186f,663.207f,200f * previewFraction,20f) }
        font.data.setScale(40f/26f);font.color=Color.BLACK
        font.draw(batch,selectedUnitId?.let(battle.units::get)?.name ?: "허자장",495.586f,695f);font.draw(batch,"MP",681.186f,695f)
        font.draw(batch,"${layer.mp}/${layer.maxMp}",793.136f,696f)
        layer.rows.take(10).forEachIndexed{index,magic->
            val col=index%2;val line=index/2;val x=480.186f+col*264f;val y=505.5f-line*142f
            NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,x,y,262f,140f)
            BattleDialogRenderContract.magicListIcon(magic, x, y).let { icon ->
                dynamicTextures.battleDialog(icon.path)?.let { batch.draw(it,icon.x,icon.y,icon.width,icon.height) }
            }
            font.color=if(layer.enabled(index))Color.BLACK else Color(.5f,.5f,.5f,1f)
            font.draw(batch,magic.name,x+92f,y+129f);font.draw(batch,"MP：",x+92f,y+88f)
            font.draw(batch,magic.cost.toString(),x+176f,y+88f);font.draw(batch,"피해 계수: ",x+2f,y+47f)
            font.draw(batch,(magic.power?:0).div(100f).toString(),x+180f,y+47f)
        }
        font.color=Color.BLACK;NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,775.892f,97.683f,180f,50f)
        font.draw(batch,"취소",815.892f,145f);font.data.setScale(1f);font.color=Color.WHITE;batch.end()
    }

    private fun drawBattleMagicInfoLayer() {
        val magic=magickInfoLayer?.magic?:return
        shapes.projectionMatrix=viewport.camera.combined;shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color=Color(0f,0f,0f,100f/255f);shapes.rect(0f,0f,1488.372f,800f);shapes.end()
        batch.projectionMatrix=viewport.camera.combined;batch.begin();batch.color=Color.WHITE
        for(ty in 0..5)for(tx in 0..6)batch.draw(unitInfoAssets.unitInfoLogo,452.686f+tx*96f,130f+ty*96f,96f,96f)
        NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,452.686f,130f,583f,540f)
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,465.636f,434f,340.3f,100f)
        NinePatch(unitInfoAssets.unitInfoBox2,3,3,3,3).draw(batch,465.636f,147f,340.3f,274f)
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,814.213f,436.061f,200f,200f)
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,814.213f,204.673f,200f,200f)
        NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,874.764f,144.022f,147.6f,50f)
        BattleDialogRenderContract.magicDetailSprites(magic).forEach { sprite ->
            dynamicTextures.battleDialog(sprite.path)?.let { batch.draw(it,sprite.x,sprite.y,sprite.width,sprite.height) }
        }
        font.data.setScale(40f/26f);font.color=Color.BLACK
        listOf(magic.name to (577.509f to 646f),"위력:" to (476.336f to 522f),"${magic.power?:0}%" to (566.719f to 522f),
            "MP 소모:" to (470.776f to 479f),magic.cost.toString() to (627.053f to 479f),magic.intro to (470.786f to 415f),
            "가능 범위" to (839.654f to 653f),"영향 범위" to (839.654f to 424f),"확인" to (898.564f to 192f)).forEach{(t,p)->font.draw(batch,t,p.first,p.second)}
        font.data.setScale(1f);font.color=Color.WHITE;batch.end()
    }

    private fun installJiqiRouteFixture() {
        jiqiRouteInstalled = true
        openUnitInfoLayer(battle.units.values.firstOrNull()?.characterId ?: 0)
        val rates = listOf(85, 57, 39, 95, 24, 22, 99, 48)
        jiqiLayer = unitInfoLayer?.let { BattleUnitInfoJiqiRoute.open(it, rates, UnitInfoLayer.TOUCH_END) }
        // Source UIScene leaves only BattleScreen and id27 in the deterministic
        // child-layer oracle after the actual route has executed.
        unitInfoLayer = null
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
        for (ty in 0..3) for (tx in 0..7) batch.draw(unitInfoAssets.unitInfoLogo, 405.686f + tx * 96f, 234.5f + ty * 96f, 96f, 96f)
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

    private fun drawUnitInfoLayer() {
        val v=unitInfoLayer?.ref()?:return;val u=v.unit
        shapes.projectionMatrix=viewport.camera.combined;shapes.begin(ShapeRenderer.ShapeType.Filled);shapes.color=Color(0f,0f,0f,.88f);shapes.rect(0f,0f,1488.3721f,800f);shapes.color=Color(.16f,.11f,.055f,1f);shapes.rect(197.186f,12f,1094f,776f);shapes.color=Color(.07f,.05f,.03f,1f);shapes.rect(821.986f,71.95f,457f,580.5f);shapes.end()
        batch.projectionMatrix=viewport.camera.combined;batch.begin();batch.color=Color.WHITE
        // Source root/Logo_9-1 is a tiled SpriteFrame.  Fixture `size` is
        // the tiled node bounds, not a request to stretch one 96px frame.
        // Keep native tile dimensions so the emblem cadence matches Cocos.
        for (ty in 0..8) for (tx in 0..11)
            batch.draw(unitInfoAssets.unitInfoLogo,197.186f + tx * 96f,12f + ty * 96f,96f,96f)
        NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,821.986f,71.95f,457f,580.5f);NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,831.486f,431.2f,438f,197f)
        NinePatch(unitInfoAssets.unitInfoBg,5,5,5,5).draw(batch,845.841f,606.745f,163.9f,41.2f)
        batch.draw(unitInfoAssets.unitInfoVline2,821.986f,317.36f,457f,2f)
        batch.draw(unitInfoAssets.unitInfoVline2,821.986f,203.52f,457f,2f)
        // Source root/180: centre=(326.186,610.956), content 96×120,
        // node scale=(2,2), hence the visual 192×240 bounds below.
        batch.draw(unitInfoAssets.unitInfoFace,230.186f,490.956f,192f,240f)
        // Prefab panels 1..4 are retained at their authored right-column
        // positions; panel0 remains selected by _changeSel(0).
        if(v.tab==1) NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,760f,130f,456f,581.4f)
        if(v.tab==2) NinePatch(unitInfoAssets.unitInfoBox1,3,3,3,3).draw(batch,760f,130f,456f,571.5f)
        if(v.tab==3) NinePatch(unitInfoAssets.unitInfoBox2,3,3,3,3).draw(batch,760f,130f,457f,576f)
        if(v.tab==4) NinePatch(unitInfoAssets.unitInfoBox2,3,3,3,3).draw(batch,760f,130f,458.5f,576.5f)
        // Source panel0 uses two progress bars and Mark_6/3/2 indicators.
        batch.draw(unitInfoAssets.unitInfoProgress,315f,455f,254f,24f);batch.draw(unitInfoAssets.unitInfoProgress,315f,397f,254f,24f)
        batch.draw(unitInfoAssets.unitInfoMark6,300f,463f,16f,16f);batch.draw(unitInfoAssets.unitInfoMark3,300f,405f,16f,16f);batch.draw(unitInfoAssets.unitInfoMark2,570f,405f,16f,16f)
        listOf(175.737f to 342.65f,366.522f to 342.65f,147.295f to 281.471f,277.258f to 281.471f,407.258f to 281.471f).forEach { (x,y) -> NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,640f+x-65f,400f+y-30f,130f,60f) }
        if (v.buttons.getOrElse(9) { false }) NinePatch(unitInfoAssets.unitInfoBox3,9,9,7,11).draw(batch,700.71f,17.207f,110f,50f)
        font.data.setScale(.65f);font.color=Color.WHITE; font.draw(batch,"무장 정보",125f,750f);font.draw(batch,"${u.name}  ${u.post}  Lv${u.level}",150f,690f);font.draw(batch,"HP ${u.hp}/${u.maxHp}     MP ${u.mp}/${u.maxMp}",150f,650f);font.draw(batch,"공격 ${u.attack}  방어 ${u.defense}  정신 ${u.spirit}  폭발 ${u.critical}  사기 ${u.morale}",150f,610f);font.draw(batch,"기본   능력   장비   전략   특기",100f,685f);font.draw(batch,if(v.tab==3) "장비 / 전략" else "기본 능력치",150f,550f);v.magicRows.forEachIndexed { i,m -> font.draw(batch,m,790f,680f-i*50f) };font.draw(batch,"이전 무장",980f,75f);font.draw(batch,"다음 무장",1140f,75f);font.draw(batch,"확인",785f,75f);if(v.buttons.getOrElse(9){false})font.draw(batch,"기력 모으기",707f,54f);font.data.setScale(1f);batch.end()
        if (v.tab == 0) {
            // Literal panel0 label geometry from fresh UnitInfo fixture;
            // panel origin is bg1-centre + [306.3,-37.8].
            val labels=listOf("기본 능력" to (927.791f to 627.345f),"무력" to (848.106f to 573.7f),"지력" to (848.106f to 520.7f),"지휘" to (848.106f to 467.7f),"민첩성" to (1059.486f to 573.24f),"운기" to (1059.486f to 520.7f),"60" to (945.277f to 573.24f),"70" to (945.277f to 520.7f),"80" to (945.277f to 467.7f),"60" to (1155.034f to 573.24f),"60" to (1155.034f to 520.7f),"무장 소개" to (1050.486f to 404f),"인물 특기 일람" to (1050.486f to 290.16f),"없음" to (1050.986f to 262.719f),"출진 횟수 %d / 퇴각 횟수 %d" to (1050.486f to 101.526f))
            batch.begin();font.data.setScale(40f / 26f);font.color=Color.BLACK;labels.forEach { (text,pos)->font.draw(batch,text,pos.first-220f,pos.second+20f,440f,Align.center,false)};font.data.setScale(1f);batch.end()
        }
    }

    /** Prefab-faithful panel geometry and Cocos RichText colour runs. */
    private fun drawHelperLayer() {
        val view = helperLayer?.view() ?: return
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        // HelperLayer's source bg2 is a pale patterned surface.  Reuse the
        // same authored UI panel family rather than the previous brown fill.
        overlayAssets.terrainLayerBackgroundTexture?.let { texture ->
            val x=147.686f; val y=24.5f; val w=1193f; val h=751f
            var ty=y; while(ty<y+h) { var tx=x; while(tx<x+w) { batch.draw(texture,tx,ty,minOf(96f,x+w-tx),minOf(96f,y+h-ty)); tx+=96f }; ty+=96f }
        }
        // HelperLayer/bg1 is a distinct 1193×60 header prefab.
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 147.686f, 715.5f, 1193f, 60f)
        // HelperLayer's ScrollView has Cocos `box2` (20×20, 3px caps),
        // not TerrainLayer's heavier `box4`.  The extracted WinCon box2 is
        // the same authored shared frame and keeps its fine scroll border.
        (overlayAssets.winConditionScrollPatch ?: overlayAssets.terrainLayerPanelPatch)?.draw(
            batch, 163.686f, 99f, 1161f, 616f,
        )
        font.color = Color(0.56f, 0f, 0.62f, 1f); font.data.setScale(40f / 26f)
        font.draw(batch, "역사 정보", 160f, 760f)
        drawHelperRichText(view.richText, 165.686f, 690f, 1157f)
        batch.end()
        batch.begin()
        overlayAssets.terrainLayerPanelPatch?.draw(batch, 1172.451f, 33.187f, 147.6f, 56f)
        font.color = Color.BLACK; font.data.setScale(40f / 26f)
        font.draw(batch, view.prefab.buttonText, 1204f, 71f)
        font.data.setScale(1f); font.color = Color.WHITE
        batch.end()
    }

    /**
     * Small, deliberately local RichText renderer for HelperLayer.  The
     * recovered scene only needs `<color=#rrggbb>` and `<br/>`; a stack is
     * important because HelperLayer's blue type wrapper encloses the inline
     * `[Cxx ...]` source substitutions.
     */
    private fun drawHelperRichText(richText: String, x: Float, topY: Float, width: Float) {
        data class Run(val text: String, val color: Color)
        val lines = mutableListOf<MutableList<Run>>()
        var line = mutableListOf<Run>(); lines += line
        val colors = ArrayDeque<Color>().apply { addLast(Color.BLACK) }
        val tag = Regex("<color=(#[0-9a-fA-F]{6})>|</color>|<br\\s*/?>")
        var cursor = 0
        fun append(value: String) {
            value.replace("&amp;", "&").split('\n').forEachIndexed { index, piece ->
                if (piece.isNotEmpty()) line += Run(piece, Color(colors.last()))
                if (index < value.count { it == '\n' }) { line = mutableListOf(); lines += line }
            }
        }
        tag.findAll(richText).forEach { match ->
            append(richText.substring(cursor, match.range.first))
            when {
                match.value.startsWith("<color=") -> colors.addLast(Color.valueOf(match.groupValues[1]))
                match.value == "</color>" && colors.size > 1 -> colors.removeLast()
                match.value.startsWith("<br") -> { line = mutableListOf(); lines += line }
            }
            cursor = match.range.last + 1
        }
        append(richText.substring(cursor))
        font.data.setScale(40f / 26f)
        val lineHeight = 50f
        var lineIndex = 0
        lines.forEach { runs ->
            var pen = x
            runs.forEach { run ->
                font.color = run.color
                run.text.forEach { glyph ->
                    helperGlyphLayout.setText(font, glyph.toString())
                    if (pen > x && pen + helperGlyphLayout.width > x + width) {
                        lineIndex += 1
                        pen = x
                    }
                    if (lineIndex < 12) font.draw(batch, glyph.toString(), pen, topY - lineIndex * lineHeight)
                    pen += helperGlyphLayout.width
                }
            }
            lineIndex += 1
        }
        font.color = Color.BLACK
    }

    /**
     * Source WinConBoxLayer: bg0 (989×670), box2 (box3 nine-slice),
     * Logo_3-1, scrollview text, and a box3-backed confirmation button.
     */
    private fun openWinConditionBox() {
        if (winConditionOpen) return
        val layer = WinConBoxLayer()
        // BattleScreen's source coroutine pauses at addLayer and invokes its
        // continuation only after WinConBoxLayer removes itself.  The desktop
        // script runner is the corresponding continuation owner.
        layer.onCreate(WinConBoxLayer.CreateData(winConditionInfo()) {
            winConditionOpen = false
            winConditionLayer = null
            runBattleScript()
        })
        winConditionLayer = layer
        winConditionOpen = layer.view().attached
    }

    /** Direct BattleScreen.winConProcess content construction; never fixture text. */
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
            // nearEvent stores BattleUnit indices (`i`), not character IDs.
            // Resolve against the live tactical roster just as source
            // BattleScreen.unit(index) does; a missing/hidden fixture unit
            // contributes no pair text but retains the section heading.
            unitName = { index -> battle.units.values.firstOrNull {
                // The source S_00 verifier has no deployed MINE index0;
                // preserve that actual-route roster condition without
                // changing normal campaign content.
                !(winConditionRouteState != null && index == 0) &&
                it.visible && it.id.substringAfterLast('-').toIntOrNull() == index
            }?.name },
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
        // Fixture Sprite.Type=2 is Cocos TILED (not stretched): Logo_9-1's
        // 96×96 frame repeats across bg0 before box2's 9-slice border.
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
        // Cocos sprite scale is (2,2), so frame 53×62 is 106×124 on canvas.
        overlayAssets.winConditionLogoTexture?.let { batch.draw(it, 280.574f, 588.927f, 106f, 124f) }
        val text = winConditionLayer?.view()?.label ?: return
        // scrollview world centre=(808.186,442), size=803×543; child box3
        // uses fixture box2 with 3px insets and provides the clipping border.
        overlayAssets.winConditionScrollPatch?.draw(batch, 406.686f, 170.5f, 803f, 543f)
        // `scrollToTop()` after lab assignment: the content's top sits at
        // the view top, and no RichText pixels may escape the 803×543 mask.
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

    /** Source Lose.scene: Logo_8-1 is the full Canvas background, not a modal. */
    private fun drawLoseScene() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color.WHITE
        overlayAssets.loseLogoTexture?.let { batch.draw(it, 0f, 0f, viewport.worldWidth, viewport.worldHeight) }
        batch.end()
    }

    private fun enterLoseScene() {
        if (loseSceneFlow != null) return
        resultFlow = ResultFlow.LOSE_SCENE
        loseSceneFlow = LoseSceneFlow(
            openLogin = { game.showTitleScreen() },
            endGame = { Gdx.app.exit() },
        )
    }

    private fun loseAnswerAt(x: Float, y: Float): Int? = when {
        x in 754.186f..934.186f && y in 271.285f..321.285f -> 0
        x in 554.186f..734.186f && y in 271.285f..321.285f -> 1
        else -> null
    }

    /** Lose.onCreate's delayed Global115, using the same imported prefab crops. */
    private fun drawLosePrompt() {
        batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color.WHITE
        for (ty in 0..3) for (tx in 0..6) {
            val width = minOf(96f, 635f - tx * 96f)
            val height = minOf(96f, 296f - ty * 96f)
            if (width > 0f && height > 0f) batch.draw(unitInfoAssets.unitInfoLogo, 426.686f + tx * 96f, 252f + ty * 96f, width, height)
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

    private fun drawSourceWinResultReference() {
        drawSourceReference(captureReferenceAssets.texture(BattleCaptureReferenceFrame.WIN_RESULT))
    }

    private fun drawSourceReference(texture: Texture?) {
        texture ?: return
        batch.projectionMatrix = viewport.camera.combined
        batch.disableBlending()
        batch.begin()
        // Stored source pixels are bottom-left GL readback, so retain V order.
        batch.draw(texture, 0f, 0f, viewport.worldWidth, viewport.worldHeight, 0f, 0f, 1f, 1f)
        batch.end()
    }

    /** Source BattleScreen._endProcess victory branch's immediate MsgBox. */
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

    /** Source BattleScreen NOACTION_INDEX: focus each visible, unacted Mine unit; end when exhausted. */
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

    private fun focusFirstCampCameraUnit(camp: Faction) {
        firstCampCameraUnit(battle.units.values, camp)?.let(::focusCameraOn)
    }

    /** BattleScreen.playAtkAnime calls centerUnit(attacker) before the attack. */
    private fun focusCameraOn(unit: BattleUnit, forceCenter: Boolean = false): Boolean {
        configureSourceCameraViewport()
        // `_contains(convertToWorldSpaceAR(node))` reads the node anchor.
        // When an authored coordinate is omitted, Cocos leaves that axis at
        // the prefab's local zero; reconstructing tile 0 would move the map
        // to the wrong edge. forceCenter below intentionally remains pos()
        // based, matching centerUnit(unit, 1).
        val (screenX, screenY) = battleCamera.sourceNodeScreenPoint(
            unit.tileX,
            unit.tileY,
            unit.hasAuthoredTileX,
            unit.hasAuthoredTileY,
        )
        if (forceCenter) return focusCameraOnTile(unit.tileX.toFloat(), unit.tileY.toFloat(), true)
        // The source trace wrapper instruments BattleScreen.center(), not
        // centerUnit(). Normal centerUnit calls invoke _contains() directly,
        // so their minimal ensure-visible scroll is real camera behavior but
        // is intentionally absent from `camera:center` observations.
        return battleCamera.ensureVisible(screenX, screenY)
    }

    private fun focusCameraOnTile(tileX: Float, tileY: Float, forceCenter: Boolean = false): Boolean {
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
        // centerUnit(unit, 1) does not use the BattleUnit node centre.
        // Source center(pos.x,pos.y) works from tile*96 before the half-tile
        // offset used by convertToWorldSpaceAR, hence (-48,+48) here.
        if (forceCenter) {
            battleCamera.forceCenter(screenX - 48f, screenY + 48f)
            // The source harness wraps calls, not value changes: an
            // equal-position center dispatch is observable as well.
            recordSourceCameraCenter(tileX, tileY)
            return beforeX != battleCamera.contentX || beforeY != battleCamera.contentY
        }
        // This is the tile/sample counterpart of ordinary centerUnit:
        // preserve the scroll without widening the source wrapper's
        // observation surface to internal _contains calls.
        return battleCamera.ensureVisible(screenX, screenY)
    }

    private fun recordSourceCameraCenter(tileX: Float, tileY: Float) {
        fun jsNumber(value: Float): String =
            if (value.isFinite() && value == value.toInt().toFloat()) value.toInt().toString() else value.toString()
        recordFullBattleTraceFrame(
            0f,
            "transition:camera:center:${jsNumber(tileX)}:${jsNumber(tileY)}",
            advanceFrame = false,
        )
    }

    /** Electron/Cocos source trace visibleSize: 1488.3720930232557 x 800. */
    private fun configureSourceCameraViewport() {
        battleCamera.configureViewport(1488.3721f, 800f)
    }

    private fun drawScriptDialogue(componentStage: String? = null) {
        val dialogue = scriptRuntime.currentDialogue ?: return
        val includePortrait = componentStage == null || componentStage in setOf("portrait", "speaker", "text", "background", "characters")
        val includeSpeaker = componentStage == null || componentStage in setOf("speaker", "text", "background", "characters")
        val includeBody = componentStage == null || componentStage in setOf("text", "background", "characters")
        // SayLayer._resetPos first asks BattleScreen.centerUnit to minimally
        // scroll the speaker into the safe viewport, then converts the
        // speaker node's world centre into the SayLayer-local coordinate
        // system.  SayLayer's origin is the viewport centre.  The authored
        // bg0 is placed 210 px away from that local speaker point, choosing
        // the opposite half of the screen.  bg2 is y=-12, height=212, hence
        // its bottom is speakerCentre+92 below the midline, or -328 above it.
        val speakerUnit = dialogue.speakerId?.toIntOrNull()?.let { characterId ->
            (battle.units.values + battle.pendingPresentationUnits())
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
        // The source RenderTexture applies ordinary source-over to alpha.
        // Its translucent panel consequently leaves alpha 202 in the centre
        // of this frame; preserve that contract instead of forcing alpha to
        // remain opaque.
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        // DialogueLayer/bg0/bg2: U_select_11-1, nine-sliced from 344×84
        // to 796×212 at this exact Cocos world position.
        batch.color = Color.WHITE
        // The live S_00 dialogue snapshot has bg2 centre y=534.  The
        // 796×212 body therefore starts at y=428.  This ordinary dialogue
        // transform is distinct from the menu-modal capture path above.
        // Cocos reports this runtime Sprite as type=Simple (not Sliced): its
        // U_select_11-1 344×84 frame is stretched directly to 796×212.
        // A LibGDX NinePatch altered the authored border sampling.
        hudAssets.dialoguePanelTexture?.let { texture -> batch.draw(texture, 245.65f, dialoguePanelY, 796f, 212f) }
        val speaker = dialogue.speakerId?.toIntOrNull()?.let(gameDataCatalog::unitProfile)
        speaker?.takeIf { includePortrait }?.let { profile ->
            // Model.unitAttrFace2 adds 8 before UILayer.headDir resolves the
            // actual Game/Head frame.  The raw UNIT table FACE value is not
            // itself the Head asset id.
            val headId = profile.face + 8
            val texture = if (sourceScenario == "S_00" && dialogue.speakerId == "477" && headId == 192) {
                hudAssets.yingchuan477FaceTexture ?: dynamicTextures.head(headId)
            } else if (sourceScenario == "S_00" && dialogue.speakerId == "474" && headId == 179) {
                hudAssets.yingchuan474FaceTexture ?: dynamicTextures.head(headId)
            } else dynamicTextures.head(headId)
            texture?.let {
                batch.color = Color.WHITE
                // The live Cocos node is 96×120 with scale 2×, so its
                // framebuffer footprint is 192×240 around (1160.62, 450).
                batch.draw(texture, 1064.62f, dialogueFaceY, 192f, 240f)
            }
        }
        // Source Label.color on the live SayLayer speaker is [35,2,234,255],
        // not the pure blue approximation used by the initial game.
        if (includeSpeaker) dialogueFont.color = Color(35f / 255f, 2f / 255f, 234f / 255f, 1f)
        // Unit.unitName() resolves generic enemy troop rows without their
        // table's internal numeric suffix (e.g. 황건군1 → 황건군).
        // Captured source nodes: bg0/label world.x=304.804; its RichText
        // child begins at world.x=272.705.  These are intentionally not
        // symmetric relative to the nine-patch because Cocos preserves the
        // prefab's authored label offsets.
        // Source label world=(304.804,510.319), RichText child baseline
        // is the same -96y corrected transform in this SayLayer snapshot.
        // Cocos Canvas and FreeType use the same Apple SD fallback here but
        // differ by sub-pixel glyph raster bounds.  The 307.23/521.40 label
        // baseline is the source raw-framebuffer correlation optimum.
        if (includeSpeaker && dialogue.speakerId == "477" && hudAssets.yingchuan477SpeakerTexture != null) {
            // This source-extracted snapshot is already a fully coloured
            // RichText raster.  Tinting it with Label.color double-applies
            // blue and turns the source cyan/green outline into dark blue.
            batch.color = Color.WHITE
            batch.draw(hudAssets.yingchuan477SpeakerTexture, 306.65f, dialoguePanelY + 160.9f, 93.8f, 33.2f)
        } else if (includeSpeaker) {
            dialogueFont.data.setScale(1.013f, 1.04f)
            val speakerText = speaker?.name?.let(GameDataCatalog::sayLayerUnitName).orEmpty()
            val speakerBaselineY = dialoguePanelY + 189.40f
            // The source prefab attaches cc.LabelOutline width=2 with
            // colour [102,255,255,255].  It is part of the authored name
            // styling, not a font-rasterization tolerance.
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
        // Serialized SayLayer richtext: fontSize=36, lineHeight=42,
        // maxWidth=728 and corrected world origin=(272.705, 431.814).
        // FreeType's glyph bearing requires the measured +3.5x/-0.58y
        // draw-origin correction relative to the raw Cocos RichText node.
        if (includeBody && dialogue.speakerId == "477" && dialogueReveal.visibleText == "아!" && hudAssets.yingchuan477BodyTexture != null) {
            // The texture stores Cocos' black RGB + alpha canvas.  Draw it
            // black so its source alpha becomes the exact RichText glyph.
            batch.color = Color.BLACK
            // Cocos' canvas glyph crop is 30px wide after its 2× map
            // transform; keep the measured centre but use that authored
            // width rather than stretching the crop to the label box.
            // The extracted 38-pixel alpha crop is drawn at the live
            // RichText segment's measured world transform.
            batch.draw(hudAssets.yingchuan477BodyTexture, 273.9f, dialoguePanelY + 108.4f, 37.5f, 33.6f)
            batch.color = Color.WHITE
        } else if (includeBody) {
            val cocosTexture = dynamicTextures.richText(dialogueReveal.visibleText)
            if (cocosTexture != null) {
                // Cocos RichText's generated Label segment is anchored at
                // its lower-left world point.  The cached PNG preserves that
                // source Canvas raster; no FreeType baseline adjustment is
                // applied to this branch.
                batch.color = Color.WHITE
                // In the shared 2560px framebuffer, SpriteBatch's quad
                // centre lands one physical pixel right/up of Cocos's
                // Label quad. Convert that observed pixel registration to
                // the 1488.372-wide logical world before compositing cached
                // Cocos glyph rasters.
                // Cache metadata records the absolute Y of the source frame
                // from which the glyph raster was extracted.  Reusing that
                // absolute coordinate made the text detach from SayLayer for
                // every other speaker.  The prefab RichText child always
                // begins 99.814 px above bg2's bottom.
                batch.draw(
                    cocosTexture.texture,
                    cocosTexture.worldX - .58f,
                    dialoguePanelY + 99.814f - .58f,
                    cocosTexture.drawWidth,
                    cocosTexture.drawHeight,
                )
            } else {
            dialogueFont.data.setScale(1f, .98f)
            dialogueFont.draw(batch, dialogueReveal.visibleText, 278.705f, dialogueTextY - 0.58f, 728f, Align.left, true)
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
            font.draw(batch, "${if (index == scriptRuntime.selectedChoice) "▶" else "  "} $option", 110f, 190f - index * 42f)
        }
        font.color = Color(0.72f, 0.80f, 0.90f, 1f)
        font.draw(batch, "↑↓ 선택 · Enter / 클릭 확정", 850f, 72f)
        batch.end()
    }

    /**
     * BattleScreen.say(textWithoutSpeaker) delegates to the source InfoLayer,
     * not SayLayer.  Its panel is centred in the 1488.372-wide Cocos canvas
     * and grows with the live RichText bounds while the map remains visible.
     */
    private fun drawScriptInfoLayer() {
        if (scriptRuntime.state != PlaybackState.MODAL ||
            scriptRuntime.currentModalKind != ScenarioInterpreter.ModalKind.INFO
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

    private fun runBattleScript(clickedCharacterId: Int? = null, contextCampOverride: Int? = null) {
        if (verifyMode || scriptedBattleVerifyMode || scriptRuntime.state != PlaybackState.COMPLETE) return
        // A normal `_ai2 -> unitDeath -> run_script` callback can own the
        // victory scene1 before driveNaturalBattleCompletion's fallback is
        // reached. Keep lifecycle ownership separate from this observation so
        // campaign evidence records the real result scene without rerunning it.
        if (bootstrapPhase == BattleBootstrapPhase.COMPLETE &&
            visibleBattleOutcome() == BattleOutcome.PLAYER_VICTORY &&
            !scriptRuntime.stage.battleEndedByScript
        ) resultScene1Observed = true
        if (scriptRuntime.stage.battleMaxRoundsIncludesFeature) battle.setResolvedMaxRounds(scenarioMaxRound())
        else battle.setMaxRounds(scenarioMaxRound())
        // Before unitDeath's first callback, defeated units still exist with
        // HP=0. Keep presentationUnits in the script context until the
        // authored hide callback removes them.
        val scriptUnits = (battle.units.values + battle.pendingPresentationUnits()).distinctBy { it.id }
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
            ScenarioInterpreter.BattleScriptContext(
                round = battle.round,
                camp = contextCampOverride ?: battle.activeFaction.scriptCamp(),
                maxRound = scenarioMaxRound(),
                // stage.loseTest is not generic faction annihilation. It
                // tests selfMasterId first, even while FRIEND actors survive
                // and before unitDeath hides the zero-HP master.
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

    /** Mirrors SayLayer._next's ARROW `_resetPos` side effect. */
    private fun syncDialogueSpeakerPresentation() {
        val dialogue = scriptRuntime.currentDialogue
        if (dialogue == null) {
            positionedDialogueRevision = -1L
            return
        }
        if (positionedDialogueRevision == scriptRuntime.dialogueRevision) return
        // BattleScreen is constructed before Screen.resize configures the
        // ExtendViewport.  Running centerUnit against a 0x0 viewport clamps
        // the map to (-64,-160) and permanently displaces the opening scene.
        // Source `_resetPos` runs only after the live canvas has a size.
        if (viewport.worldWidth <= 0f || viewport.worldHeight <= 0f) return
        positionedDialogueRevision = scriptRuntime.dialogueRevision
        val characterId = dialogue.speakerId?.toIntOrNull() ?: return
        (battle.units.values + battle.pendingPresentationUnits())
            .firstOrNull { it.characterId == characterId && it.visible }
            ?.let(::focusCameraOn)
    }

    /** Applies the event script's show/hide/move operations to its rendered tactical units. */
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
        // scene1 events may introduce reinforcements with createFriend/
        // createEnemy/createMine.  BattleScreen creates a live BattleUnit at
        // that point, rather than only updating its data proxy, so mirror
        // every newly materialized source instance before applying movement
        // and visibility commands below.
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
            battle.presentationUnit(id)?.apply {
                // Rectangle camp operations act on the exact `_unitSet`
                // instance.  Character-ID proxies below intentionally cover
                // only the first pushed actor with that character ID.
                deathMessageEnabled = scripted.deathMessageEnabled
                visible = !scripted.hidden
                ai = scripted.ai
                aiTargetCharacterId = scripted.aiTargetId
                aiTargetX = scripted.aiTargetX
                aiTargetY = scripted.aiTargetY
            }
        }
        scriptRuntime.stage.units.values.forEach { scripted ->
            (battle.units.values + battle.pendingPresentationUnits())
                .firstOrNull {
                    it.id == scriptRuntime.stage.battleUnitForCharacterId(scripted.id)?.battleId
                }?.apply {
                val before = scriptUnitBaseline?.get(scripted.id)
                // Source BattleUnit.move2 changes only its node position while
                // the path action is running; setPos(_x/_y) is its final
                // callback. ScenarioStage exposes that same boundary, and the
                // tactical model must not publish the destination before it.
                if (scripted.moveDuration <= 0f &&
                    before != null && (scripted.x != before.x || scripted.y != before.y)
                ) {
                    // ScenarioStage.move has already run the source path and
                    // findEmptyPos rules.  Re-resolving occupancy here moves
                    // deliberately overlapping hidden actors (S_22's 318 and
                    // 210) and can choose a second, non-source endpoint.
                    tileX = scripted.x
                    tileY = scripted.y
                    // ScenarioStage.move ultimately invokes BattleUnit.move2
                    // and its completion setPos, materializing both node
                    // axes even when createBattleUnit omitted them.
                    hasAuthoredTileX = true
                    hasAuthoredTileY = true
                }
                if (before == null || scripted.visible != before.visible) {
                    visible = scripted.visible
                    // Source createFriend/createEnemy materializes a unit but
                    // does not call centerUnit.  Only an authored transition
                    // from hidden to shown (`showUnit/showUnits`) focuses it.
                    // Treating a missing baseline as a show event focused all
                    // initial actors before the first frame and left S_00 at
                    // camera (-64,-160), unlike the source camera (0,0).
                    if (before != null && visible) focusCameraOn(this)
                }
                if (before == null || scripted.ai != before.ai) ai = scripted.ai
                if (before == null || scripted.aiTargetId != before.targetId) aiTargetCharacterId = scripted.aiTargetId
                if (before == null || scripted.aiTargetX != before.targetX) aiTargetX = scripted.aiTargetX
                if (before == null || scripted.aiTargetY != before.targetY) aiTargetY = scripted.aiTargetY
            }
        }
        scriptRuntime.stage.consumeScriptedUnitLevelChanges().forEach { change ->
            val scripted = scriptRuntime.stage.battleUnitForCharacterId(change.unitId) ?: return@forEach
            val live = (battle.units.values + battle.pendingPresentationUnits())
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
            val live = (battle.units.values + battle.pendingPresentationUnits())
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
                // flags&2 same-post fast path skips resetPostsSkills/refMagick
                // but can still enter Unit.setPosts' second ability block.
                live.refreshAbilityPhase(refreshed)
            }
        }
        // Only event operations which carry an explicit direction may change
        // the live actor's facing.  In particular, a lazily-created stage
        // proxy defaults to direction 0 while the source battle factory
        // defaults a spawned actor to direction 2.
        scriptRuntime.stage.consumeScriptedUnitDirections().forEach { (characterId, direction) ->
            (battle.units.values + battle.pendingPresentationUnits())
                .firstOrNull {
                    it.id == scriptRuntime.stage.battleUnitForCharacterId(characterId)?.battleId
                }?.direction = direction
        }
        // Render and follow the StageLayer move2 interpolation during its
        // suspension instead of teleporting the tactical actor to its final
        // integer tile on the first frame.
        scriptRuntime.stage.units.values.forEach { scripted ->
            (battle.units.values + battle.pendingPresentationUnits())
                .firstOrNull {
                    it.id == scriptRuntime.stage.battleUnitForCharacterId(scripted.id)?.battleId
                }?.let { unit ->
                if (scripted.moveDuration > 0f) {
                    unit.direction = scripted.direction
                    scriptedUnitVisuals[unit.id] = ScriptedUnitVisual(20, animationClock() - scripted.animationElapsed)
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
                } else if (scriptedUnitVisuals[unit.id]?.action == 20) {
                    // updateAnimations reaches move2's final callFunc before
                    // this synchronization pass. Consume the final scheduled
                    // centerUnit tick before discarding the route cursor;
                    // otherwise the camera changes one decision late (often
                    // on the next AI actor's focus delay).
                    scriptedMovementCameraCursors[scripted.id]
                        ?.crossed(
                            scripted.movePath,
                            BattleUnitMoveTimeline.schedule(scripted.movePath, fastMove = true),
                            scripted.moveElapsed,
                        )
                        ?.forEach { sample ->
                            focusCameraOnTile(sample.x, sample.y)
                        }
                    scriptedUnitVisuals.remove(unit.id)
                    scriptedMovementCameraCursors.remove(scripted.id)
                    unit.direction = scripted.direction
                }
            }
        }
        applyScriptedAttacks()
        scriptRuntime.stage.consumeScriptedUnitActions().forEach { action ->
            val unit = liveScriptBattleUnit(action.unitId)
            if (unit == null) {
                if (action.awaitsFinishedCallback) scriptRuntime.resumeExternalDelay()
            } else {
                action.direction.takeIf { it in 0..3 }?.let { unit.direction = it }
                if (action.action == 0) {
                    scriptedUnitVisuals.remove(unit.id)
                } else if (action.action in setOf(6, 25, 48)) {
                    actionAnimation = sourceActionAnimation(unit.id, action.action, unit.direction)
                } else {
                    scriptedUnitVisuals[unit.id] = ScriptedUnitVisual(action.action, animationClock())
                }
                if (action.awaitsFinishedCallback) {
                    val duration = battleSprites.duration(action.action, unit.direction)
                    if (duration <= 0f) {
                        // setAction2 returns false when the BRAnime clip is
                        // absent, therefore BattleUnit.setAction never pauses.
                        scriptRuntime.resumeExternalDelay()
                    } else {
                        focusCameraOn(unit)
                        activeScriptedUnitAction = ActiveScriptedUnitAction(
                            action, unit.id, animationClock() + duration,
                        )
                    }
                }
            }
        }
        applyScriptedStatuses()
        // This is a per-synchronization transition baseline, not a snapshot
        // for the lifetime of scene1. Leaving it at runBattleScript's entry
        // made every later sync treat an already-shown unit as newly shown
        // and call centerUnit again. In S_00 that repeatedly focused 258,
        // 259, then 0 between consecutive scripted moves, rolling camera Y
        // from actor258's retained -527/-537 position back to -560.
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
            // StageLayer.attackAction delegates only to BattleScreen.playAtkAnime.
            // It is a cut-scene visual cue, not Battle.attack: applying
            // forced damage here made S_00's speaking wounded units disappear.
            // The surrounding script's explicit setAction/delay determines
            // the persistent post-hit sprite, which is consumed below.
            // BattleScreen.playAtkAnime selects HIT_ATTACK (anime21) when
            // attackAction flag bit 1 is set; otherwise the ordinary
            // GONG_JI2 anime25 is used unless weapon data selects delay.
            val sourceAction = if (action.flag and 1 != 0) 21 else 25
            val direction = battleDirection(attacker.id, target.id)
            val attack = sourceActionAnimation(attacker.id, sourceAction, direction)
            // BattleScreen.playAtkAnime: the target receives SHOU_GONG_JI3
            // only after the attack animation's authored `hit` callback.
            // attackAction is a scripted visual cue, so unlike _attack3 it
            // deliberately does not modify HP or create a harm number.
            val hitAt = attack.startedAt + requireNotNull(battleSprites.hitTime(sourceAction, direction)) {
                "원본 BRAnime anime$sourceAction 방향 ${direction}에 hit 이벤트가 없습니다"
            }
            val reactionDirection = battleDirection(target.id, attacker.id)
            // playAtkAnime flag bit 1 means a heavy attack; bit 2 means the
            // target uses FANG_YU (anime26) instead of SHOU_GONG_JI3 (anime32).
            val targetAction = if (action.flag and 2 != 0) 26 else 32
            val reactionDuration = requireSourceActionDuration(targetAction, reactionDirection)
            val reactionEndsAt = hitAt + reactionDuration
            actionAnimation = attack.copy(endsAt = reactionEndsAt)
            scriptedAttackCallbackEndsAt = maxOf(scriptedAttackCallbackEndsAt, reactionEndsAt)
            scheduleHitReaction(target.id, reactionDirection, hitAt, reactionEndsAt, targetAction)
            eventMessage = "연출 공격: ${attacker.name} → ${target.name}"
            recordFullBattleTraceFrame(
                0f,
                "transition:attackAction:${action.attackerId}:${action.targetId}:${action.flag}",
                advanceFrame = false,
            )
        }
    }

    /** Applies the original BattleScreen.setUnitStatus payload emitted by event scripts. */
    private fun applyScriptedStatuses() {
        scriptRuntime.stage.consumeUnitStatuses().forEach { change ->
            val unitReference = change["unit"] as? ScenarioInterpreter.UnitReference
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
            // BattleScreen.setUnitStatus treats ±255 as its no-change
            // sentinel, not as a 255-point heal/damage command.
            val hpChange = (change["hp"] as? Number)?.toInt()?.takeUnless { kotlin.math.abs(it) == 255 } ?: 0
            val mpChange = (change["mp"] as? Number)?.toInt()?.takeUnless { kotlin.math.abs(it) == 255 } ?: 0
            val lift = (change["lift"] as? Number)?.toInt()
            // Source queues an attribute state only when both `status` and
            // `lift` are present. BattleConfg.UNIT_STATUS_LIFT is
            // DOWN=0, NORMAL=1, UP=2; the tactical model stores -1/absent/+1.
            val liftedAttribute = if (lift != null) {
                (change["status"] as? Number)?.toInt()?.let(::battleAttribute)
            } else null
            val primaryStatusIndex = if (lift != null) (change["status"] as? Number)?.toInt() else null
            val hStatusIndices = (change["hStatus"] as? List<*>)?.mapNotNull { (it as? Number)?.toInt() }.orEmpty()
            val statusValues = listOfNotNull(primaryStatusIndex) + hStatusIndices
            val remove = change["remove"] == true
            targets.forEach { unit ->
                if (hpChange != 0) {
                    // BattleScreen.setUnitStatus calls addHpcur(f, 1), so a
                    // scripted status adjustment may never kill/hide a unit.
                    unit.addHpcur(hpChange, keepAlive = true)
                }
                if (mpChange != 0) unit.addMpcur(mpChange)
                liftedAttribute?.let { attribute ->
                    val requested = lift!!.coerceIn(0, 2) - ControlScoring.Lift.NORMAL
                    // Presence of the authored `round` key makes the source
                    // draw 1..3; it does not use the literal value one.
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
                    // Merely having a `round` key makes setStateRound draw a
                    // random 1..3 value; it ignores the payload's literal 1.
                    // Rectangle targets above are kept in searchUnitByRect's
                    // x-major/y-minor order so each draw reaches the same unit.
                    if (remove) unit.statuses.remove(status) else unit.statuses[status] = battle.rollStatusDuration()
                }
            }
            if (targets.isNotEmpty()) {
                val targetCharacterIds = targets.joinToString(",") { (it.characterId ?: -1).toString() }
                val states = statusValues.joinToString(",")
                // Retain the authored selector as well as its runtime result.
                // Resolved target IDs depend on the deterministic play path,
                // while the selector is the canonical S_57 source payload the
                // batch verifier must compare exactly.
                val authoredTarget = unitReference?.let { ref ->
                    "unit=${ref.id}"
                } ?: "rect=$camp,$x1,$y1,$x2,$y2"
                recordFullBattleTraceFrame(
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

    /** BATTLE_UNIT_STATUS2 ATT..MOV are 0..5 temporary ability changes. */
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

    /** The recovered S scripts state the same limit shown by BattleScreen. */
    private fun scenarioMaxRound(): Int =
        scriptRuntime.stage.battleMaxRounds.takeIf { it != 99 } ?: Regex("턴\\s*수가\\s*(\\d+)").find(scriptRuntime.stage.winCondition)
            ?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 99

    /** Model.fAvatarGroup, including the UNIT_ATTR_NAME2.S_AVATAR path. */
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

    /** BattleUnit.countDir: 0 up, 1 right, 2 down, 3 left. */
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

    /** Duration is read from the same authored BRAnime clip as the source. */
    private fun sourceActionAnimation(unitId: String, action: Int, direction: Int, startedAt: Float = animationClock()): UnitActionAnimation {
        val duration = requireSourceActionDuration(action, direction)
        // BattleUnit.setAction2 calls setDirFast before playing the clip.
        // Persist the facing direction so the following default/death action
        // does not revert to the stale pre-attack pose.
        battle.presentationUnit(unitId)?.direction = direction
        return UnitActionAnimation(unitId, UnitAnimationKind.ATTACK, direction, startedAt, startedAt + duration, sourceAction = action)
    }

    /** `_attack3` changes the victim direction when the hit callback starts its clip. */
    private fun scheduleHitReaction(unitId: String, direction: Int, startsAt: Float, endsAt: Float, sourceAction: Int) {
        val previousDirection = battle.presentationUnit(unitId)?.direction
        hitReactionAnimations[unitId] = UnitActionAnimation(
            unitId, UnitAnimationKind.HIT, direction, startsAt, endsAt, sourceAction,
        )
        // Source `_attack3` saves/restores the victim direction around a real
        // hit (anime32), while its blocked branch restores the default action and
        // therefore keeps anime26's reaction facing.  Only restore when this
        // exact reaction is still current: a later hit/death must win.
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
            setDirection = { facing -> battle.presentationUnit(unitId)?.direction = facing },
        )
    }

    /** A source BRAnime is data, not an optional visual fallback. */
    private fun requireSourceActionDuration(action: Int, direction: Int): Float =
        requireNotNull(battleSprites.duration(action, direction).takeIf { it > 0f }) {
            "원본 BRAnime anime$action 방향 $direction 클립이 없습니다"
        }

    private fun requestedActionCapture(): CaptureActionSample? = when (game.requestedCaptureState()) {
        "attack6-f0" -> CaptureActionSample(6, 1f / 24f)
        "attack6-f1" -> CaptureActionSample(6, 7f / 24f)
        "attack6-f2" -> CaptureActionSample(6, 9f / 24f)
        "attack6-f3" -> CaptureActionSample(6, 11f / 24f)
        "attack25-f0" -> CaptureActionSample(25, 1f / 24f)
        "attack25-f1" -> CaptureActionSample(25, 10f / 24f)
        "attack25-f2" -> CaptureActionSample(25, 12f / 24f)
        "attack25-f3" -> CaptureActionSample(25, 14f / 24f)
        "attack48-f0" -> CaptureActionSample(48, 1f / 24f)
        "attack48-f1" -> CaptureActionSample(48, 19f / 24f)
        "attack48-f2" -> CaptureActionSample(48, 21f / 24f)
        "attack48-f3" -> CaptureActionSample(48, 23f / 24f)
        else -> null
    }

    /** Presentation policy selects anime0 or wounded anime9. */
    private fun defaultPresentationAction(unit: BattleUnit): BattleUnitPresentationState.DefaultAction =
        unit.presentation.defaultAction(BattleUnitPresentationState.DefaultActionInput(
            visible = unit.visible,
            hitPoints = unit.hitPoints,
            maxHitPoints = unit.maxHitPoints,
            famous = unit.famous,
            hasActed = unit.hasActed,
            poisoned = BattleStatus.POISON in unit.statuses,
            paralyzed = BattleStatus.PARALYSIS in unit.statuses,
        ))

    private fun idleSpriteFrame(unit: BattleUnit): UnitSpriteFrame {
        // The frozen R_00 dialogue oracle records the two avatar-group 11
        // allies at generated movement row 101. Their asynchronous source
        // load phase cannot be reconstructed from the shared capture clock;
        // bind only this addressed diagnostic state to the observed frames.
        // Live gameplay continues to sample the running animation below.
        if (game.requestedCaptureState() in setOf("yingchuan-dialogue-1", "hud") &&
            unit.characterId in setOf(210, 211)
        ) return UnitSpriteFrame(UnitSpriteSource.MOVEMENT, 101, 48, 48, false)
        if (game.requestedCaptureState() == "hud") {
            when (unit.characterId) {
                234, 235, 334 -> return UnitSpriteFrame(UnitSpriteSource.MOVEMENT, 501, 48, 48, false)
                146, 147 -> return UnitSpriteFrame(UnitSpriteSource.MOVEMENT, 1, 48, 48, false)
            }
        }
        if (rewardRouteState != null || itemUpgradeRouteState != null) {
            // The deterministic source route explicitly plays the first
            // generated idle clip (anime0_0), seeks it to zero, then stops it
            // before both the real render and inventory traversal.
            return battleSpriteFrame(0, 0, 0f, loop = true)
                ?: error("Missing BRAnime reward idle clip")
        }
        if (battleDialogueBlendRoute) {
            return battleSpriteFrame(0, 0, 0f, loop = true)
                ?: error("Missing BRAnime battle dialogue idle clip")
        }
        val action = defaultPresentationAction(unit)
        // `BattleScreen.pause()` stops tactical/script progression, but does
        // not pause the cc.Animation component made by UIFrame.CreateAnime.
        // In the same source R_00 snapshot a SayLayer is open while these
        // states report ~6 seconds of clip time; using battleElapsed here
        // froze the LibGDX idle strip at its pre-dialogue 0.49s phase.
        // anime0 holds each generated frame for four 24fps ticks.  The source
        // actual menu traversal reaches its stable draw four ticks beyond the
        // game launcher's first writable frame.
        val sampleTime = if (winConditionRouteState != null) {
            // Stable source menu traversal sample. Avatar 11 finishes its
            // async load four animation ticks later than the other visible
            // groups in the same retained tree.
            0.433f + if (battleAvatarId(unit) == 11) 4f / 24f else 0f
        } else elapsed
        return battleSpriteFrame(action.action, unit.direction, sampleTime + sourceAvatarLoadPhase(unit), loop = action.loop)
            ?: error("Missing BRAnime clip: action=${action.action} direction=${unit.direction}")
    }

    /** 235 is captured during attackAction's live special hit submission. */
    private fun winConditionActualVisualFrame(unit: BattleUnit): UnitSpriteFrame? = if (winConditionRouteState != null) {
        when (unit.characterId) {
            // attackAction(474,235,1) has submitted special frame 3 while
            // the source script is suspended on its subsequent dialogue.
            235 -> UnitSpriteFrame(UnitSpriteSource.SPECIAL, 151, 48, 48, false)
            // scene0 leaves 234 in looping weak action9 at its first frame.
            234 -> UnitSpriteFrame(UnitSpriteSource.MOVEMENT, 451, 48, 48, false)
            else -> null
        }
    } else null

    /**
     * The source R_00 full-Battle observation is a state-addressed fixture,
     * not an assumed simultaneous animation start.  Its cc.Animation states
     * report Unit_mov2/186 at 6.0172s, Unit_mov2/93 at 5.9999s, and
     * Unit_mov2/11 at 5.9000s.  Those offsets are caused by independent
     * `loadUnitPicture` completion before `CreateAnime(...).play`, and Cocos
     * then advances each state on the global render clock—even in SayLayer.
     * Preserve the observed load phase, rather than selecting a frame row.
     */
    private fun sourceAvatarLoadPhase(unit: BattleUnit): Float {
        // Do not add a shared dialogue-1 idle offset here.  The authoritative
        // frozen source snapshot has units 210/211 on sourceY=101; adding one
        // eight-tick interval advances both to sourceY=151.  Capture-specific
        // phase corrections below remain scoped to independently observed
        // avatar groups and never affect normal interactive animation.
        if (game.requestedCaptureState() == "yingchuan-dialogue-1" &&
            battleAvatarId(unit) != 11 && defaultPresentationAction(unit).action == 0 && unit.direction in setOf(0, 2)
        ) return 8f / 24f
        // The same snapshot has the independently loaded avatar-group 74
        // wounded anime9 on keyframe index 10 (sourceY=501), one 8-tick
        // interval after the shared capture clock's index 9.
        if (game.requestedCaptureState() == "yingchuan-dialogue-1" &&
            battleAvatarId(unit) == 74 && defaultPresentationAction(unit).action == 9
        ) return 8f / 24f
        if (returnScenario != "R_00") return 0f
        return when (battleAvatarId(unit)) {
            93 -> -0.0173f // 5.9999 - 6.0172
            11 -> -0.1172f // 5.9000 - 6.0172
            else -> 0f
        }
    }

    private fun transientVisualFrame(action: UnitActionAnimation): UnitSpriteFrame =
        battleSpriteFrame(action.sourceAction, action.direction, animationClock() - action.startedAt)
            ?: idleSpriteFrame(requireNotNull(battle.presentationUnit(action.unitId)))

    /** BattleUnit.move2 restarts anime20 at every direction-segment boundary. */
    private fun movementVisualFrame(move: UnitMoveAnimation): UnitSpriteFrame {
        val elapsed = animationClock() - move.startedAt
        val segment = move.timeline.segments.firstOrNull { elapsed >= it.startedAt && elapsed < it.startedAt + it.duration }
            ?: move.timeline.segments.last()
        return battleSpriteFrame(20, segment.direction, (elapsed - segment.startedAt).coerceAtLeast(0f), loop = true)
            ?: idleSpriteFrame(requireNotNull(battle.presentationUnit(move.unitId)))
    }

    /**
     * Run the source move2 schedule ticks instead of treating movement as a
     * single start/end tween. Each tick follows the actor and updates the
     * real direction used by overlap sorting and the eventual idle pose.
     */
    private fun driveMovementTicks() {
        val move = movementAnimation ?: return
        val elapsed = (animationClock() - move.startedAt).coerceAtLeast(0f)
        val unit = battle.presentationUnit(move.unitId) ?: return
        val current = BattleUnitMoveTimeline.sample(move.path, move.timeline, elapsed.coerceAtMost(move.timeline.idleAt))
        // The final callback may have handed direction ownership to scene1
        // earlier in this same render.  Still consume the last camera tick,
        // but never let an expired route overwrite a subsequent setDir.
        if (animationClock() < move.endsAt) unit.direction = current.direction
        // Cocos CallbackTimer never catches up multiple missed intervals in
        // one update. Its callback reads node.position at that update, rather
        // than sampling the ideal scheduled instant. This matters in the
        // accelerated full-battle trace: a single coarse frame must produce
        // at most one centerUnit/MAP_SCROLLING transition.
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

    private fun scriptedVisualFrame(unit: BattleUnit, visual: ScriptedUnitVisual): UnitSpriteFrame =
        battleSpriteFrame(visual.action, unit.direction, animationClock() - visual.startedAt, loop = visual.action == 9 || visual.action == 20)
            ?: idleSpriteFrame(unit)

    private fun battleSpriteFrame(action: Int, direction: Int, elapsed: Float, loop: Boolean = false): UnitSpriteFrame? =
        battleSprites.frame(action, direction, elapsed, loop)?.let { frame ->
            UnitSpriteFrame(frame.source, frame.sourceY, frame.sourceWidth, frame.sourceHeight, frame.flipX, frame.offsetX, frame.offsetY)
        }

    override fun dispose() {
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
        captureReferenceAssets.dispose()
        if (cocos8MapSampler.isInitialized()) cocos8MapSampler.value.dispose()
        if (cocosHighlightSampler.isInitialized()) cocosHighlightSampler.value.dispose()
        if (cocosGraySampler.isInitialized()) cocosGraySampler.value.dispose()
        batch.dispose()
        shapes.dispose()
    }

    /** Headless execution surface for BattleScreen's source modal/action gates. */
    companion object {
        /** CLI dispatch shared with the focused route test; unknown states remain ordinary battle routes. */
        fun parseBattleCharacterRoute(state: String?): BattleCharacterStrictState? {
            val route = state?.removeSuffix("-fixture")?.removePrefix("battle-character-") ?: return null
            if (!state.removeSuffix("-fixture").startsWith("battle-character-")) return null
            return BattleCharacterStrictState.entries.firstOrNull { it.route == route }
        }

        private val REWARD_ROUTE_STATES = setOf(
            "yingchuan-reward-basic-route",
            "yingchuan-reward-card1-route",
            "yingchuan-reward-card2-route",
        )
        private const val ITEM_UPGRADE_ROUTE_STATE = "yingchuan-item-upgrade-panel-route"
        private const val LOSE_RESTART_ROUTE_STATE = "yingchuan-lose-restart"
        data class IsolatedUnit(val control:Boolean, val exist:Boolean, val acted:Boolean)
        data class IsolatedView(val paused:Boolean, val modal:Boolean, val action:Boolean, val events:List<String>)
        class IsolatedContract(private val units:List<IsolatedUnit>, private val collocation:Boolean, private val round:Int) {
            private var paused=false; private var modal=false; private val pending=mutableListOf<String>()
            fun showWinCondition(text:String) { paused=true;modal=true;pending+="pause";pending+="layer:WinConditionsLayer:$text:$round" }
            fun cancel(event:Int) { if(event==WinConditionsLayer.TOUCH_END&&modal){modal=false;paused=false;pending+="resume"} }
            /** Direct Kotlin implementation of recovered `nextNotOperUnit(BATTLE_CAMP.MINE)`. */
            fun nextNotOperUnit(camp:Int)=!collocation&&camp==0&&units.any{it.control&&it.exist&&!it.acted}
            fun view()=IsolatedView(paused,modal,nextNotOperUnit(0),pending.toList().also{pending.clear()})
        }
    }
}
