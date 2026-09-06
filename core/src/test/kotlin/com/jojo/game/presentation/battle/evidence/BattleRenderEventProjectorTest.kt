// Battle
package com.jojo.game.presentation.battle.evidence

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 전투 렌더 이벤트 투영기 검증: 상태 스냅샷이 경로별 캡처 화면 모델로 변환되는지 확인한다. */
class BattleRenderEventProjectorTest {
    /** 대화 투영: 유닛 정렬·고정 프레임·화상·대화 표식 좌표를 함께 조립한다. */
    @Test
    fun `dialogue projection assembles ordered units marker and dialogue`() {
        val view = BattleRenderEventProjector.project(
            BattleRenderEventProjectionInput(
                phase = "battle-dialogue-blending",
                battleInitRoute = false,
                dialogueBlendRoute = true,
                winConditionRoute = BattleRenderEventProjectionWinRoute.NONE,
                itemUpgradeRoute = false,
                rewardRoute = false,
                units = listOf(
                    unit(sortOrder = 2f, visualX = 2f, visualY = 3f),
                    unit(sortOrder = 1f, visualX = 1f, visualY = 2f),
                ),
                dialogueMarker = BattleRenderEventProjectionPoint(3f, 4f),
                dialogue = BattleRenderEventProjectionDialogueInput(120, "대사", "조조"),
            )
        )

        assertEquals(BattleRenderEventRoute.DIALOGUE_BLEND, view.route)
        assertEquals(-96f, view.mapBottom)
        assertEquals(-224f, view.units.first().x)
        assertTrue(view.units.first().spriteAsset!!.endsWith("#33632304"))
        assertEquals("128", view.dialogue?.headAsset)
        assertEquals(40f, view.dialogueMarker?.x)
        assertEquals(1416f, view.dialogueMarker?.y)
    }

    /** 보상 투영: 보상 전용 전장 높이와 금전·아이템 화면 모델을 유지한다. */
    @Test
    fun `reward projection uses reward board coordinates and reward contents`() {
        val money = BattleRenderEventProjector.project(
            BattleRenderEventProjectionInput(
                phase = "yingchuan-reward",
                battleInitRoute = false,
                dialogueBlendRoute = false,
                winConditionRoute = BattleRenderEventProjectionWinRoute.NONE,
                itemUpgradeRoute = false,
                rewardRoute = true,
                units = listOf(unit(visualY = 0f)),
                reward = BattleRenderEventProjectionRewardInput(BattleRenderEventProjectionRewardPhase.MONEY, money = 500, flag = 5),
            )
        )
        val items = BattleRenderEventProjector.project(
            BattleRenderEventProjectionInput(
                phase = "yingchuan-reward",
                battleInitRoute = false,
                dialogueBlendRoute = false,
                winConditionRoute = BattleRenderEventProjectionWinRoute.NONE,
                itemUpgradeRoute = false,
                rewardRoute = true,
                units = emptyList(),
                reward = BattleRenderEventProjectionRewardInput(
                    BattleRenderEventProjectionRewardPhase.ITEMS,
                    items = listOf(BattleRenderEventProjectionRewardItemInput(17, "청룡언월도")),
                ),
            )
        )

        assertEquals(BattleRenderEventRoute.REWARD, money.route)
        assertEquals(-560f, money.mapBottom)
        assertEquals(1264f, money.units.single().y)
        assertEquals(500, money.reward?.money)
        assertEquals("17-1", items.reward?.items?.single()?.iconAsset)
        assertEquals("청룡언월도", items.reward?.items?.single()?.name)
    }

    /** 장비 강화 투영: 아이콘 번호와 장비 능력치 전후 값을 강화 패널 모델로 조립한다. */
    @Test
    fun `item upgrade projection assembles upgrade panel values`() {
        val view = BattleRenderEventProjector.project(
            BattleRenderEventProjectionInput(
                phase = "battle-item-upgrade-panel-route",
                battleInitRoute = false,
                dialogueBlendRoute = false,
                winConditionRoute = BattleRenderEventProjectionWinRoute.NONE,
                itemUpgradeRoute = true,
                rewardRoute = false,
                units = emptyList(),
                itemUpgrade = BattleRenderEventProjectionItemUpgradeInput(12, "철검", 3, "조조", "공격력", 15, 19),
            )
        )

        assertEquals(BattleRenderEventRoute.ITEM_UPGRADE, view.route)
        assertEquals("12-1", view.itemUpgrade?.iconAsset)
        assertEquals("공격력", view.itemUpgrade?.attributeName)
        assertEquals(15, view.itemUpgrade?.oldValue)
        assertEquals(19, view.itemUpgrade?.newValue)
    }

    /** 승리 조건 투영: 235번 유닛은 원본과 같이 아틀라스 주소 대신 생성 프레임 번호를 사용한다. */
    @Test
    fun `win condition projection preserves character 235 generated frame asset`() {
        val view = BattleRenderEventProjector.project(
            BattleRenderEventProjectionInput(
                phase = "battle-win-condition-full",
                battleInitRoute = false,
                dialogueBlendRoute = false,
                winConditionRoute = BattleRenderEventProjectionWinRoute.FULL,
                itemUpgradeRoute = false,
                rewardRoute = false,
                units = listOf(unit(characterId = 235, spriteSource = BattleRenderEventProjectionSpriteSource.SPECIAL, sourceY = 151)),
                winConditions = BattleRenderEventProjectionWinConditionsInput("승리", "패배"),
            )
        )

        assertEquals(BattleRenderEventRoute.WIN_FULL, view.route)
        assertEquals("50475056", view.units.single().spriteAsset)
        assertEquals("승리", view.winConditions?.first)
    }

    /** 유닛 입력 생성: 투영 사례에서 반복되는 아틀라스·체력 막대 기본값을 제공한다. */
    private fun unit(
        sortOrder: Float = 0f,
        visualX: Float = 0f,
        visualY: Float = 0f,
        characterId: Int? = 1,
        spriteSource: BattleRenderEventProjectionSpriteSource = BattleRenderEventProjectionSpriteSource.MOVEMENT,
        sourceY: Int = 101,
    ) = BattleRenderEventProjectionUnitInput(
        sortOrder = sortOrder,
        visualX = visualX,
        visualY = visualY,
        characterId = characterId,
        atlasUuid = "31cc3c95-4d6e-4c10-848f-ef1ca165e78f",
        spriteSource = spriteSource,
        sourceY = sourceY,
        offsetX = 0f,
        offsetY = 0f,
        healthRatio = 1f,
        healthBarAsset = "Mark_5-1",
    )
}
