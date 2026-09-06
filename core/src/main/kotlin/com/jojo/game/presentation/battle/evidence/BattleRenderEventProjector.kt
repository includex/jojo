// Battle
package com.jojo.game.presentation.battle.evidence

/** 전투 렌더 이벤트 투영 입력: 화면이 확정한 전투 상태를 캡처 전용 불변 값으로 전달한다. */
internal data class BattleRenderEventProjectionInput(
    val phase: String,
    val battleInitRoute: Boolean,
    val dialogueBlendRoute: Boolean,
    val winConditionRoute: BattleRenderEventProjectionWinRoute,
    val itemUpgradeRoute: Boolean,
    val rewardRoute: Boolean,
    val units: List<BattleRenderEventProjectionUnitInput>,
    val dialogueMarker: BattleRenderEventProjectionPoint? = null,
    val dialogue: BattleRenderEventProjectionDialogueInput? = null,
    val winConditions: BattleRenderEventProjectionWinConditionsInput? = null,
    val itemUpgrade: BattleRenderEventProjectionItemUpgradeInput? = null,
    val reward: BattleRenderEventProjectionRewardInput? = null,
)

/** 전투 렌더 이벤트 승리 조건 경로: 축약·전체 조건창의 표시 구성을 구분한다. */
internal enum class BattleRenderEventProjectionWinRoute { NONE, COMPACT, FULL }

/** 전투 렌더 이벤트 유닛 입력: 애니메이션이 반영된 타일·스프라이트·체력 상태를 보관한다. */
internal data class BattleRenderEventProjectionUnitInput(
    val sortOrder: Float,
    val visualX: Float,
    val visualY: Float,
    val characterId: Int?,
    val atlasUuid: String?,
    val spriteSource: BattleRenderEventProjectionSpriteSource,
    val sourceY: Int,
    val offsetX: Float,
    val offsetY: Float,
    val healthRatio: Float?,
    val healthBarAsset: String?,
)

/** 전투 렌더 이벤트 스프라이트 종류: 원본 아틀라스의 프레임 유형을 구분한다. */
internal enum class BattleRenderEventProjectionSpriteSource { MOVEMENT, ATTACK, SPECIAL }

/** 전투 렌더 이벤트 좌표: 타일 기준 표식의 위치를 보관한다. */
internal data class BattleRenderEventProjectionPoint(val x: Float, val y: Float)

/** 전투 렌더 이벤트 대화 입력: 화상·본문·화자 이름을 보관한다. */
internal data class BattleRenderEventProjectionDialogueInput(
    val face: Int?,
    val visibleText: String,
    val speakerName: String,
)

/** 전투 렌더 이벤트 승리 조건 입력: 조건 문장과 하위 목표 문구를 보관한다. */
internal data class BattleRenderEventProjectionWinConditionsInput(
    val first: String,
    val second: String,
    val childLabels: List<String> = emptyList(),
)

/** 전투 렌더 이벤트 장비 강화 입력: 강화 결과 패널의 표시 값을 보관한다. */
internal data class BattleRenderEventProjectionItemUpgradeInput(
    val icon: Int,
    val itemName: String,
    val newLevel: Int,
    val ownerName: String,
    val attributeName: String,
    val oldValue: Int,
    val newValue: Int,
)

/** 전투 렌더 이벤트 보상 입력: 현재 보상 단계와 지급 항목을 보관한다. */
internal data class BattleRenderEventProjectionRewardInput(
    val phase: BattleRenderEventProjectionRewardPhase,
    val money: Int = 0,
    val flag: Int = 0,
    val items: List<BattleRenderEventProjectionRewardItemInput> = emptyList(),
)

/** 전투 렌더 이벤트 보상 단계: 금전·아이템·종료 단계를 구분한다. */
internal enum class BattleRenderEventProjectionRewardPhase { MONEY, ITEMS, NONE }

/** 전투 렌더 이벤트 보상 항목 입력: 아이콘 번호와 표시 이름을 보관한다. */
internal data class BattleRenderEventProjectionRewardItemInput(val icon: Int, val name: String)

/** 전투 렌더 이벤트 투영기: 전투 상태 입력을 JSONL 기록기에 맞는 화면 모델로 조립한다. */
internal object BattleRenderEventProjector {
    /** 투영: 경로와 불변 상태를 원본 순서가 유지되는 렌더 이벤트 화면 모델로 변환한다. */
    fun project(input: BattleRenderEventProjectionInput): BattleRenderEventView {
        val route = when {
            input.battleInitRoute -> BattleRenderEventRoute.INIT
            input.dialogueBlendRoute -> BattleRenderEventRoute.DIALOGUE_BLEND
            input.winConditionRoute == BattleRenderEventProjectionWinRoute.COMPACT -> BattleRenderEventRoute.WIN_COMPACT
            input.winConditionRoute == BattleRenderEventProjectionWinRoute.FULL -> BattleRenderEventRoute.WIN_FULL
            input.itemUpgradeRoute -> BattleRenderEventRoute.ITEM_UPGRADE
            else -> BattleRenderEventRoute.REWARD
        }
        val boardBottom = if (input.rewardRoute || input.battleInitRoute) REWARD_BOARD_BOTTOM else BOARD_BOTTOM
        return BattleRenderEventView(
            phase = input.phase,
            route = route,
            mapBottom = if (input.rewardRoute || input.battleInitRoute) -560f else -96f,
            units = input.units.sortedBy(BattleRenderEventProjectionUnitInput::sortOrder).map { unit ->
                unitView(unit, boardBottom, input.dialogueBlendRoute, input.winConditionRoute != BattleRenderEventProjectionWinRoute.NONE)
            },
            dialogueMarker = input.dialogueMarker?.let { point ->
                BattleRenderEventMarkerView(tileLeft(point.x) + BOARD_TILE * .75f, tileBottom(point.y, boardBottom) + BOARD_TILE * .75f)
            },
            dialogue = input.dialogue?.let { dialogue ->
                BattleRenderEventDialogueView(dialogue.face?.plus(8)?.toString(), dialogue.visibleText, dialogue.speakerName)
            },
            winConditions = input.winConditions?.let { conditions ->
                BattleRenderEventWinConditionsView(conditions.first, conditions.second, conditions.childLabels)
            },
            itemUpgrade = input.itemUpgrade?.let { upgrade ->
                BattleRenderEventItemUpgradeView(
                    "${upgrade.icon}-1", upgrade.itemName, upgrade.newLevel, upgrade.ownerName,
                    upgrade.attributeName, upgrade.oldValue, upgrade.newValue,
                )
            },
            reward = input.reward?.let(::rewardView),
        )
    }

    /** 유닛 투영: 타일과 아틀라스 입력을 화면 좌표·체력 막대·스프라이트 자산으로 변환한다. */
    private fun unitView(
        input: BattleRenderEventProjectionUnitInput,
        boardBottom: Float,
        dialogueBlendRoute: Boolean,
        winConditionRoute: Boolean,
    ): BattleRenderEventUnitView {
        val size = if (input.spriteSource == BattleRenderEventProjectionSpriteSource.ATTACK) BOARD_TILE * 4f / 3f else BOARD_TILE
        return BattleRenderEventUnitView(
            x = tileLeft(input.visualX) + (BOARD_TILE - size) / 2f + input.offsetX,
            y = tileBottom(input.visualY, boardBottom) + (BOARD_TILE - size) / 2f + input.offsetY,
            size = size,
            spriteAsset = spriteAsset(input, dialogueBlendRoute, winConditionRoute),
            healthBar = input.healthRatio?.let { ratio -> input.healthBarAsset?.let { asset ->
                BattleRenderEventHealthBarView(tileLeft(input.visualX) + 4f, tileBottom(input.visualY, boardBottom) - 1f, 88f * ratio, asset)
            } },
        )
    }

    /** 스프라이트 자산: 아틀라스 식별자와 프레임 종류로 원본 자산 주소를 계산한다. */
    private fun spriteAsset(
        input: BattleRenderEventProjectionUnitInput,
        dialogueBlendRoute: Boolean,
        winConditionRoute: Boolean,
    ): String? {
        val atlasUuid = input.atlasUuid
        val suffix = when (atlasUuid) {
            "31cc3c95-4d6e-4c10-848f-ef1ca165e78f" -> "850f3"
            "9eebca65-e81b-4ba4-ad61-7ac20d03661c" -> "f1ee0"
            "3f8fbf89-4dd0-4d0b-88e0-9c7927fe5693" -> "3f9c2"
            "ca6577ee-3ca1-4280-9d60-117070dd2d0b" -> "6ef7f"
            "19ac1287-4d09-45f4-bf9a-f5eb8b21795c" -> "89d84"
            else -> null
        }
        if (atlasUuid == null || suffix == null) return null
        val atlasType = when (input.spriteSource) {
            BattleRenderEventProjectionSpriteSource.ATTACK -> 0
            BattleRenderEventProjectionSpriteSource.MOVEMENT -> 1
            BattleRenderEventProjectionSpriteSource.SPECIAL -> 2
        }
        val generatedFrameName = if (dialogueBlendRoute) 33632304 else {
            ((input.sourceY - 1).coerceAtLeast(0) / 50 shl 24) or (atlasType shl 16) or 12336
        }
        return if (winConditionRoute && input.characterId == 235) {
            generatedFrameName.toString()
        } else {
            "assets/Game/native/${atlasUuid.take(2)}/$atlasUuid.$suffix.png#$generatedFrameName"
        }
    }

    /** 보상 투영: 현재 단계의 원시 보상 값을 렌더 이벤트 보상 모델로 변환한다. */
    private fun rewardView(input: BattleRenderEventProjectionRewardInput): BattleRenderEventRewardView = when (input.phase) {
        BattleRenderEventProjectionRewardPhase.MONEY -> BattleRenderEventRewardView(BattleRenderEventRewardPhase.MONEY, input.money, input.flag)
        BattleRenderEventProjectionRewardPhase.ITEMS -> BattleRenderEventRewardView(
            BattleRenderEventRewardPhase.ITEMS,
            items = input.items.take(3).map { item -> BattleRenderEventRewardItemView("${item.icon}-1", item.name) },
        )
        BattleRenderEventProjectionRewardPhase.NONE -> BattleRenderEventRewardView(BattleRenderEventRewardPhase.NONE)
    }

    /** 타일 왼쪽 좌표: 전장 원점과 타일 폭을 적용해 화면 x 좌표를 계산한다. */
    private fun tileLeft(x: Float): Float = BOARD_LEFT + x * BOARD_TILE

    /** 타일 아래 좌표: 전장 기준 y 좌표와 타일 폭을 적용해 화면 y 좌표를 계산한다. */
    private fun tileBottom(y: Float, boardBottom: Float): Float = boardBottom - y * BOARD_TILE

    private const val BOARD_LEFT = -320f
    private const val BOARD_BOTTOM = 1728f
    private const val REWARD_BOARD_BOTTOM = 1264f
    private const val BOARD_TILE = 96f
}
