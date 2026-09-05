package com.jojo.game.presentation.battle.evidence

import com.jojo.game.FullBattleTraceRecorder

/** Immutable projection of the live battle composition used by the evidence recorder. */
internal data class BattleCompositionEvidenceView(
    val scenarioKey: String,
    val animationClock: Float,
    val visualAnimationClock: Float,
    val tracedMapBottom: Int,
    val units: List<BattleCompositionUnit>,
    val masks: List<BattleCompositionMask>,
    val scenario: BattleCompositionScenario,
    val naturalSay: Boolean,
    val modal: Boolean,
    val effectCount: Int,
)

/** Immutable state hand-off from BattleScreen; it contains no screen/model references. */
internal data class BattleCompositionEvidenceInput(
    val captureState: String?,
    val sourceScenario: String,
    val returnScenario: String,
    val animationClock: Float,
    val visualAnimationClock: Float,
    val units: List<BattleCompositionUnit>,
    val masks: List<BattleCompositionMask>,
    val dialogue: BattleCompositionDialogueInput?,
    val action: BattleCompositionAction?,
    val winConditionOpen: Boolean,
    val winConditionModal: Boolean,
    val enemyPlanner: BattleCompositionEnemyPlanner?,
    val resultFlow: String,
    val modal: Boolean,
    val effectCount: Int,
)

internal data class BattleCompositionUnit(
    val id: String,
    val frame: Int,
    val textureUuid: String?,
    val sourceY: Int,
    val sourceWidth: Int,
    val sourceHeight: Int,
    val asset: Int,
    val tileX: Int,
    val tileY: Int,
    val action: Int?,
    val material: String,
    val sourceX: Int,
    val sourceYPosition: Int,
    val scaleX: Int,
)

internal data class BattleCompositionMask(
    val id: String,
    val frame: String,
    val asset: String,
    val tileX: Int,
    val tileY: Int,
)

internal data class BattleCompositionScenario(
    val dialogue: BattleCompositionDialogue? = null,
    val action: BattleCompositionAction? = null,
    val winConditionOpen: Boolean = false,
    val winConditionModal: Boolean = false,
    val enemyPlanner: BattleCompositionEnemyPlanner? = null,
    val loseActive: Boolean = false,
    val winPromptActive: Boolean = false,
)

internal data class BattleCompositionDialogue(
    val opening: Boolean,
    val speakerId: String?,
    val speakerName: String?,
    val sourceText: String,
    val visibleText: String,
    val remainingText: String,
    val typewriterActive: Boolean,
)

internal data class BattleCompositionDialogueInput(
    val speakerId: String?,
    val speakerName: String?,
    val sourceText: String,
    val visibleText: String,
    val remainingText: String,
    val typewriterActive: Boolean,
)

internal data class BattleCompositionAction(
    val sourceAction: Int,
    val direction: Int,
    val active: Boolean,
)

internal data class BattleCompositionEnemyPlanner(
    val characterId: Int,
    val ai: Int,
    val x: Int,
    val y: Int,
    val value: Int?,
    val actionValue: Int?,
    val targetId: String?,
    val magicId: Int?,
)

/** Projects immutable live-state values into the recorder's stable composition view. */
internal object BattleCompositionEvidenceProjector {
    fun project(input: BattleCompositionEvidenceInput): BattleCompositionEvidenceView {
        val scenarioKey = when (input.captureState) {
            "yingchuan-opening-say" -> "r00-opening-say"
            "yingchuan-dialogue-1" -> "dialogue-1"
            "attack6-f0" -> "battle-action-6-f0"
            "yingchuan-win-condition" -> "win-condition-modal"
            "enemy-turn" -> "enemy-turn"
            "lose-result" -> "lose-result"
            "win-result" -> "win-result"
            else -> "natural-r00"
        }
        val dialogue = input.dialogue?.let { value ->
            BattleCompositionDialogue(
                opening = scenarioKey == "r00-opening-say",
                speakerId = value.speakerId,
                speakerName = value.speakerName,
                sourceText = value.sourceText,
                visibleText = value.visibleText,
                remainingText = value.remainingText,
                typewriterActive = value.typewriterActive,
            )
        }
        return BattleCompositionEvidenceView(
            scenarioKey = scenarioKey,
            animationClock = input.animationClock,
            visualAnimationClock = input.visualAnimationClock,
            tracedMapBottom = if (input.captureState == "map-only") -560 else -96,
            units = input.units,
            masks = input.masks,
            scenario = BattleCompositionScenario(
                dialogue = dialogue,
                action = input.action.takeIf { scenarioKey == "battle-action-6-f0" },
                winConditionOpen = input.winConditionOpen,
                winConditionModal = input.winConditionModal,
                enemyPlanner = input.enemyPlanner.takeIf { scenarioKey == "enemy-turn" },
                loseActive = input.resultFlow == "LOSE_SCENE",
                winPromptActive = input.resultFlow == "WIN_SAVE_PROMPT",
            ),
            naturalSay = input.returnScenario == "R_00",
            modal = input.modal,
            effectCount = input.effectCount,
        )
    }
}

/** Deterministic serializer for the legacy battle composition trace schema. */
internal object BattleCompositionEvidenceRecorder {
    fun record(view: BattleCompositionEvidenceView): String {
        val units = view.units.joinToString(",") { unit ->
            val atlasJson = unit.textureUuid?.let { "\"$it\"" } ?: "null"
            val actionJson = unit.action?.toString() ?: "null"
            "{\"address\":\"candidate/unit/${unit.id}\",\"frame\":${unit.frame},\"runtimeGeneratedFrame\":true," +
                    "\"textureUuid\":$atlasJson,\"frameRect\":[0,${unit.sourceY},${unit.sourceWidth},${unit.sourceHeight}]," +
                    "\"asset\":${unit.asset},\"tile\":[${unit.tileX},${unit.tileY}],\"action\":$actionJson," +
                    "\"material\":\"${unit.material}\",\"sourceLocalPosition\":[${unit.sourceX},${unit.sourceYPosition}]," +
                    "\"sourceChildPosition\":[0,0],\"sourceChildScale\":[${unit.scaleX},1],\"flipX\":${unit.scaleX < 0}," +
                    "\"flipY\":false,\"z\":${unit.tileY + 1}}"
        }
        val masks = view.masks.joinToString(",") { mask ->
            "{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/mask/node#${mask.id}\"," +
                    "\"frame\":\"${mask.frame}\",\"asset\":\"${mask.asset}\",\"tile\":[${mask.tileX},${mask.tileY}],\"stencil\":true}"
        }
        val optionalMasks = if (masks.isEmpty()) "" else ",$masks"
        val scenarioRecords = scenarioRecords(view.scenarioKey, view.scenario)
        val miniMarkers = miniMarkers()
        val naturalSay = if (view.naturalSay) NATURAL_SAY else ""
        return "{\"state\":\"R_00/natural-battle/t=stable\",\"scenarioKey\":\"${view.scenarioKey}\",\"oracle\":\"isolated-libgdx-runtime\",\"animationClock\":${view.animationClock},\"visualAnimationClock\":${view.visualAnimationClock},\"records\":[{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map\",\"frame\":null,\"asset\":\"4afa0804-1ac2-4d59-97e4-1549a9425953.6295a.jpg\",\"uv\":\"0,1,1,1,0,0,1,0\",\"draw\":[-320,${view.tracedMapBottom},1920,1920],\"material\":\"SpriteBatch/linear\"},{\"address\":\"Battle/Canvas/Layer/bg/map\",\"frame\":\"Smlmap_1-1\",\"asset\":\"maps/ui/battle-smlmap-1.jpg\",\"assetUuid\":\"28fcaf09-66e0-4d64-b968-438f0b7db258\",\"frameRect\":[0,0,120,120],\"position\":[1610.3721,678],\"scale\":[2,2],\"opacity\":168,\"z\":0,\"material\":\"SpriteBatch/linear\"},{\"address\":\"Battle/Canvas/Layer/bg/weather\",\"frame\":\"weather_0\",\"asset\":\"maps/ui/battle-menu/weather_0.png\",\"frameRect\":[270,2,72,72],\"position\":[1492.3721,560],\"scale\":[0.8,0.8],\"opacity\":127,\"z\":0,\"material\":\"SpriteBatch/linear\"},{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/info/bar2/sprite\",\"frame\":\"Mark_3-1\",\"asset\":\"maps/marks/3.png#45bb9e80\",\"material\":\"SpriteBatch/linear\"},$miniMarkers$naturalSay$units$optionalMasks$scenarioRecords],\"modal\":${view.modal},\"effectCount\":${view.effectCount}}"
    }

    private fun scenarioRecords(key: String, scenario: BattleCompositionScenario): String = when (key) {
        "r00-opening-say" -> scenario.dialogue?.let { dialogue ->
            val speaker = dialogue.speakerId?.let { "\"$it\"" } ?: "null"
            val speakerName = dialogue.speakerName?.let { "\"${escape(it)}\"" } ?: "null"
            val text = escape(dialogue.visibleText)
            val remaining = escape(dialogue.remainingText)
            if (dialogue.opening) {
                ",{\"address\":\"Battle/Canvas/Layer\",\"kind\":\"SayLayer\",\"active\":true,\"speaker\":$speaker,\"speakerName\":$speakerName,\"sourceStrings\":[\"&${dialogue.speakerId}\",\"${dialogue.sourceText}\"],\"typewriterActive\":${dialogue.typewriterActive},\"timeline\":\"scene0-opening-first-glyph\",\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/Panel_cancel\",\"kind\":\"Panel\",\"active\":true,\"size\":[1488.3720930232557,800],\"opacity\":0},{\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"DialoguePanel\",\"active\":true,\"position\":[0,50],\"size\":[1030,260],\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/face\",\"kind\":\"Head\",\"active\":true,\"position\":[416.432,0],\"size\":[96,120],\"scale\":[2,2],\"renderedSize\":[192,240],\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/bg2\",\"kind\":\"DialoguePanelBody\",\"active\":true,\"position\":[-100.536,-12],\"size\":[796,212],\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/bg2/richtext\",\"kind\":\"RichText\",\"active\":true,\"text\":\"$text\",\"remainingText\":\"$remaining\",\"fontSize\":36,\"lineHeight\":42,\"maxWidth\":728,\"position\":[-370.945,46.734],\"size\":[728,52.92],\"anchor\":[0,1],\"opacity\":255}"
            } else {
                ", {\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"SayLayer\",\"active\":true,\"speaker\":$speaker,\"speakerName\":$speakerName,\"sourceStrings\":[\"&${dialogue.speakerId}\",\"${dialogue.sourceText}\"],\"timeline\":\"dialogue-step-1\",\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/text\",\"kind\":\"DialogueText\",\"text\":\"$text\",\"visible\":true,\"fontSize\":36,\"lineHeight\":42,\"maxWidth\":728,\"position\":[-370.945,46.734],\"size\":[728,52.92],\"anchor\":[0,1],\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/bg2\",\"frame\":\"U_select_11-1\",\"asset\":\"maps/ui/dialogue-panel.png\",\"renderMode\":\"Simple/stretch\",\"opacity\":255,\"material\":\"SpriteBatch/linear\"}"
            }
        } ?: ", {\"address\":\"Battle/Canvas/Layer\",\"kind\":\"SayLayer\",\"active\":false}"

        "dialogue-1" -> scenario.dialogue?.let { dialogue ->
            val speaker = dialogue.speakerId?.let { "\"$it\"" } ?: "null"
            val speakerName = dialogue.speakerName?.let { "\"${escape(it)}\"" } ?: "null"
            ", {\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"SayLayer\",\"active\":true,\"speaker\":$speaker,\"speakerName\":$speakerName,\"sourceStrings\":[\"&${dialogue.speakerId}\",\"${dialogue.sourceText}\"],\"timeline\":\"dialogue-step-1\",\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/text\",\"kind\":\"DialogueText\",\"text\":\"${escape(dialogue.visibleText)}\",\"visible\":true,\"fontSize\":36,\"lineHeight\":42,\"maxWidth\":728,\"position\":[-370.945,46.734],\"size\":[728,52.92],\"anchor\":[0,1],\"opacity\":255},{\"address\":\"Battle/Canvas/Layer/bg0/bg2\",\"frame\":\"U_select_11-1\",\"asset\":\"maps/ui/dialogue-panel.png\",\"renderMode\":\"Simple/stretch\",\"opacity\":255,\"material\":\"SpriteBatch/linear\"}"
        } ?: ", {\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"SayLayer\",\"active\":false}"

        "battle-action-6-f0" -> scenario.action?.let { action ->
            ", {\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/action\",\"kind\":\"BRAnime\",\"action\":${action.sourceAction},\"direction\":${action.direction},\"active\":${action.active},\"timeline\":\"attack6-f0\"}"
        } ?: ", {\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/action\",\"kind\":\"BRAnime\",\"active\":false,\"timeline\":\"attack6-f0\"}"

        "win-condition-modal" -> ", {\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"WinConBoxLayer\",\"active\":${scenario.winConditionOpen},\"timeline\":\"open\",\"modal\":${scenario.winConditionModal},\"opacity\":255}"
        "enemy-turn" -> scenario.enemyPlanner?.let { plan ->
            val target = plan.targetId?.let { "\"$it\"" } ?: "null"
            ", {\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/enemy-planner#${plan.characterId}\",\"kind\":\"Battle._ai/current-point\",\"active\":true,\"sourceCharacterId\":${plan.characterId},\"ai\":${plan.ai},\"x\":${plan.x},\"y\":${plan.y},\"value\":${plan.value ?: "null"},\"actionValue\":${plan.actionValue ?: "null"},\"targetId\":$target,\"magicId\":${plan.magicId ?: "null"},\"timeline\":\"enemy-turn-planner\"}"
        } ?: ", {\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/unit/enemy-planner#474\",\"kind\":\"Battle._ai/current-point\",\"active\":false,\"timeline\":\"enemy-turn-planner\"}"

        "lose-result" -> ", {\"address\":\"Lose/Canvas/Logo_8-1\",\"kind\":\"LoseScene\",\"active\":${scenario.loseActive},\"frame\":\"Logo_8-1\",\"asset\":\"Logo/Logo_8-1\",\"frameRect\":[0,0,640,400],\"timeline\":\"Battle.lose->endProcess\"}"
        "win-result" -> ", {\"address\":\"Battle/Canvas/Layer/bg0\",\"kind\":\"MsgBox\",\"active\":${scenario.winPromptActive},\"frame\":\"box3\",\"timeline\":\"Battle.enemyHide->endProcess\"},{\"address\":\"Battle/Canvas/Layer/bg0/label\",\"kind\":\"Label\",\"text\":\"게임 저장하시겠습니까?\",\"timeline\":\"save-prompt\"}"
        else -> ""
    }.replace(", {", ",{")

    private fun miniMarkers(): String = listOf(
        "img5" to Pair(0f, -42f), "img5" to Pair(-6f, -48f), "img5" to Pair(12f, -42f),
        "img5" to Pair(-18f, -36f), "img5" to Pair(12f, -36f), "img9" to Pair(-6f, -6f),
        "img9" to Pair(0f, -6f), "img9" to Pair(-24f, -36f), "img9" to Pair(-6f, -30f),
        "img9" to Pair(0f, -30f), "img9" to Pair(12f, -30f), "img9" to Pair(-6f, -24f),
        "img9" to Pair(0f, -24f), "img9" to Pair(-30f, 0f), "img9" to Pair(-30f, -6f),
        "img9" to Pair(-18f, -6f), "img9" to Pair(-12f, 0f), "img9" to Pair(12f, 0f),
        "img9" to Pair(12f, -12f),
    ).mapIndexed { index, (frame, point) ->
        val uuid = if (frame == "img5") "1ff0ac4a-8fe9-4b8e-a5f7-374a5440571a" else "98825d51-e8ff-4a12-9906-a4372e913cdd"
        "{\"address\":\"Battle/Canvas/Layer/bg/map/tiled#$index\",\"frame\":\"$frame\",\"asset\":\"maps/ui/battle-smlmap-$frame.png\",\"assetUuid\":\"$uuid\",\"frameRect\":[1,1,10,10],\"localPosition\":[${point.first},${point.second}],\"parentScale\":[2,2],\"opacity\":168,\"z\":0,\"material\":\"SpriteBatch/linear\"}"
    }.joinToString(",")

    private fun escape(value: String): String = FullBattleTraceRecorder.escape(value)

    private const val NATURAL_SAY = ",{\"address\":\"Battle/Canvas/Layer/ScrollView/view/content/map/New Node\",\"frame\":\"Mark_10-1\",\"asset\":\"maps/ui/battle-say.png\",\"assetUuid\":\"6e23f416-6258-4c79-9ac4-e89fc8b8df4f\",\"frameRect\":[702,2,24,24],\"localPosition\":[-96,-288],\"parentScale\":[2,2],\"opacity\":255,\"z\":1000,\"timeline\":\"SHOW_SAY(active)\",\"material\":\"SpriteBatch/linear\"}"
}
