// Battle
package com.jojo.game.presentation.battle.evidence

/** 전투 구성 증거에 필요한 화면 경로와 불변 스냅샷을 기록기 모델로 투영하는 입력이다. */
internal data class BattleCompositionEvidenceProjectionInput(
    val animationClock: Float,
    val visualAnimationClock: Float,
    val mapOnlyCapture: Boolean,
    val sourceScenario: String,
    val returnScenario: String,
    val battleMenuOpen: Boolean,
    val effectCount: Int,
    val openingSayRoute: Boolean,
    val dialogueOneRoute: Boolean,
    val actionCapture: BattleCompositionEvidenceActionCaptureInput?,
    val winModalRoute: Boolean,
    val enemyTurnRoute: Boolean,
    val loseResultRoute: Boolean,
    val winResultRoute: Boolean,
    val units: List<BattleCompositionEvidenceUnitInput>,
    val terrainAt: (x: Int, y: Int) -> Int,
    val dialogue: BattleCompositionEvidenceDialogueInput?,
    val speakerName: (speakerId: String) -> String?,
    val action: BattleCompositionEvidenceActionInput?,
    val winConditionOpen: Boolean,
    val winConditionModal: Boolean,
    val enemyPlanner: () -> BattleCompositionEvidenceEnemyPlannerInput?,
    val loseActive: Boolean,
    val winPromptActive: Boolean,
)

/** 유닛 한 개의 현재 프레임·타일·아틀라스 어댑터 값이다. */
internal data class BattleCompositionEvidenceUnitInput(
    val id: String,
    val visible: Boolean,
    val textureUuid: String?,
    val sourceY: Int,
    val sourceWidth: Int,
    val sourceHeight: Int,
    val characterId: Int?,
    val tileX: Int,
    val tileY: Int,
    val scriptedAction: Int?,
    val flipX: Boolean,
)

/** 액션 캡처 fixture가 제공하는 원본 액션·샘플 시점이다. */
internal data class BattleCompositionEvidenceActionCaptureInput(val action: Int, val sample: Float)

/** 현재 대화의 원문과 화면에 이미 공개한 문자열이다. */
internal data class BattleCompositionEvidenceDialogueInput(
    val speakerId: String?,
    val sourceText: String,
    val visibleText: String,
    val typewriterComplete: Boolean,
)

/** 현재 전투 액션의 원본 번호·방향·종료 시점이다. */
internal data class BattleCompositionEvidenceActionInput(
    val sourceAction: Int,
    val direction: Int,
    val endsAt: Float,
)

/** enemy-turn fixture가 필요한 planner 결과의 불변 형태다. */
internal data class BattleCompositionEvidenceEnemyPlannerInput(
    val characterId: Int,
    val ai: Int,
    val x: Int,
    val y: Int,
    val value: Int?,
    val actionValue: Int?,
    val targetId: String?,
    val magicId: Int?,
)

/** 전투 화면 스냅샷을 기존 composition evidence 기록기용 모델로 조립한다. */
internal object BattleCompositionEvidenceProjector {
    /** 투영: 유닛·terrain mask·시나리오 분기를 기존 순서와 값으로 조립한다. */
    fun project(input: BattleCompositionEvidenceProjectionInput): BattleCompositionEvidenceView {
        val visibleUnits = input.units.filter(BattleCompositionEvidenceUnitInput::visible)
        val scenarioKey = scenarioKey(input)
        return BattleCompositionEvidenceView(
            scenarioKey = scenarioKey,
            animationClock = input.animationClock,
            visualAnimationClock = input.visualAnimationClock,
            tracedMapBottom = if (input.mapOnlyCapture) -560 else -96,
            units = visibleUnits.map { unitView(it, input.sourceScenario) },
            masks = visibleUnits.mapNotNull { maskView(it, input.terrainAt(it.tileX, it.tileY)) },
            scenario = scenarioView(input, scenarioKey),
            naturalSay = input.returnScenario == "R_00",
            modal = input.battleMenuOpen,
            effectCount = input.effectCount,
        )
    }

    /** 유닛 투영: 선택된 소스 프레임과 타일 좌표를 기록기 형식으로 바꾼다. */
    private fun unitView(input: BattleCompositionEvidenceUnitInput, sourceScenario: String): BattleCompositionUnit {
        val row = (input.sourceY - 1) / 50
        return BattleCompositionUnit(
            id = input.id,
            frame = (row shl 24) or (1 shl 16) or 12336,
            textureUuid = input.textureUuid,
            sourceY = input.sourceY,
            sourceWidth = input.sourceWidth,
            sourceHeight = input.sourceHeight,
            asset = input.characterId ?: -1,
            tileX = input.tileX,
            tileY = input.tileY,
            action = input.scriptedAction,
            material = if (sourceScenario == "S_00" && input.scriptedAction == 4) {
                "hight-light/u_value=1"
            } else {
                "SpriteBatch/source-over"
            },
            sourceX = 48 * input.tileX - 456,
            sourceYPosition = 456 - 48 * input.tileY,
            scaleX = if (input.flipX) -1 else 1,
        )
    }

    /** terrain id가 원본 mask를 요구할 때만 mask evidence를 만든다. */
    private fun maskView(input: BattleCompositionEvidenceUnitInput, terrain: Int): BattleCompositionMask? = when (terrain) {
        10 -> BattleCompositionMask(input.id, "Mark_19-1", "maps/marks/19.png#c91c07bf", input.tileX, input.tileY)
        1 -> BattleCompositionMask(input.id, "Mark_21-1", "maps/marks/21.png#f52b641a", input.tileX, input.tileY)
        else -> null
    }

    /** capture route의 우선순위를 기존 화면 조립 순서 그대로 적용한다. */
    private fun scenarioKey(input: BattleCompositionEvidenceProjectionInput): String = when {
        input.openingSayRoute -> "r00-opening-say"
        input.dialogueOneRoute -> "dialogue-1"
        input.actionCapture?.let { it.action == 6 && it.sample == 1f / 24f } == true -> "battle-action-6-f0"
        input.winModalRoute -> "win-condition-modal"
        input.enemyTurnRoute -> "enemy-turn"
        input.loseResultRoute -> "lose-result"
        input.winResultRoute -> "win-result"
        else -> "natural-r00"
    }

    /** 시나리오 투영: 대화·액션·enemy planner와 종료 모달 상태를 조립한다. */
    private fun scenarioView(
        input: BattleCompositionEvidenceProjectionInput,
        scenarioKey: String,
    ): BattleCompositionScenario = BattleCompositionScenario(
        dialogue = input.dialogue?.let { dialogue ->
            BattleCompositionDialogue(
                opening = scenarioKey == "r00-opening-say",
                speakerId = dialogue.speakerId,
                speakerName = dialogue.speakerId?.let(input.speakerName),
                sourceText = dialogue.sourceText,
                visibleText = dialogue.visibleText,
                remainingText = dialogue.sourceText.removePrefix(dialogue.visibleText),
                typewriterActive = !dialogue.typewriterComplete,
            )
        },
        action = input.action?.takeIf { scenarioKey == "battle-action-6-f0" }?.let { action ->
            BattleCompositionAction(action.sourceAction, action.direction, input.animationClock < action.endsAt)
        },
        winConditionOpen = input.winConditionOpen,
        winConditionModal = input.winConditionModal,
        enemyPlanner = if (scenarioKey == "enemy-turn") input.enemyPlanner()?.let { plan ->
            BattleCompositionEnemyPlanner(
                characterId = plan.characterId,
                ai = plan.ai,
                x = plan.x,
                y = plan.y,
                value = plan.value,
                actionValue = plan.actionValue,
                targetId = plan.targetId,
                magicId = plan.magicId,
            )
        } else {
            null
        },
        loseActive = input.loseActive,
        winPromptActive = input.winPromptActive,
    )
}
