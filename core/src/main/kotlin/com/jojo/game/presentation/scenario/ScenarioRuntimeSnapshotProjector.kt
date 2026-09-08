// 시나리오 실행 증거 입력 투영
package com.jojo.game.presentation.scenario

import com.jojo.game.presentation.scenario.*

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.presentation.scenario.hall.FeatsLayer
import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.application.runtime.RuntimeScenarioPresentation
import com.jojo.game.application.runtime.RuntimeScenarioOverlay
import com.jojo.game.presentation.scenario.story.ScenarioStreetDialogueStages
import com.jojo.game.presentation.scenario.hall.HallManagement

/** ScenarioRuntimeSnapshotProjector: Screen의 현재 상태를 검증기가 소비할 프레임 증거 입력으로 읽어낸다. */
internal object ScenarioRuntimeSnapshotProjector {
    /** 증거에 독립적인 오버레이 상세를 포함해야 하는 runtime 종류다. */
    private val observableOverlays = setOf(
        RuntimeScenarioOverlay.INFO, RuntimeScenarioOverlay.GET_ITEM_EQUIPMENT,
        RuntimeScenarioOverlay.GET_ITEM_PROPERTY, RuntimeScenarioOverlay.ITEM_EQUIPMENT,
        RuntimeScenarioOverlay.ITEM_PROPERTY, RuntimeScenarioOverlay.ITEM_DISCARD_CONFIRM,
        RuntimeScenarioOverlay.MAP_INFO, RuntimeScenarioOverlay.CHOICE, RuntimeScenarioOverlay.AMBITION,
        RuntimeScenarioOverlay.ASK, RuntimeScenarioOverlay.COMMAND, RuntimeScenarioOverlay.MENU,
        RuntimeScenarioOverlay.SAVE, RuntimeScenarioOverlay.SAVE_CONFIRM, RuntimeScenarioOverlay.EXCLUSIVE,
        RuntimeScenarioOverlay.EXCLUSIVE_TAB1, RuntimeScenarioOverlay.MAGIC, RuntimeScenarioOverlay.FEATS,
        RuntimeScenarioOverlay.FEATS_HELP,
    )

    /** renderInput: 현재 대사·배경·유닛·거점 오버레이를 불변 프레임 입력으로 투영한다. */
    fun renderInput(screen: ScenarioScreen): ScenarioFrameEvidenceInput {
        val dialogue = screen.playback.currentDialogue
        if (screen.hallOverlayVariant == RuntimeScenarioOverlay.SKIP_OPEN) {
            val skip = requireNotNull(screen.hallSkipLayer) { "SKIP_OPEN 오버레이인데 SkipLayer 흐름이 없습니다." }
            check(skip.button && !skip.panel && skip.zIndex == 999)
        }
        val unitList = screen.hallUnitListLayer?.rows?.take(6)?.map { id ->
            val unit = screen.gameDataCatalog.unitProfile(id)
            ScenarioHallUnitListEvidenceRow(
                screen.campaign.unitNames[id] ?: if (id == 181) "병사 " else unit?.name ?: "무장",
                screen.gameDataCatalog.postsName(screen.campaign.unitAttribute(id, 17, unit?.posts ?: 0)),
            )
        }
        return ScenarioFrameEvidenceInput(
            variant = screen.hallOverlayVariant,
            palace = screen.runtimePresentation == RuntimeScenarioPresentation.PALACE,
            section = screen.runtimePresentation == RuntimeScenarioPresentation.SECTION,
            street = ScenarioStreetDialogueStages.nameAt(screen.runtimePresentationDetail)
                ?.takeIf { screen.runtimePresentation == RuntimeScenarioPresentation.STREET }?.let { stage -> ScenarioStoryEvidenceView.StreetDialogue(
                stage = stage,
                dialogueVisible = dialogue != null,
                visibleText = screen.scenarioViewState.dialogueVisibleText,
                speakerName = dialogue?.speakerId?.toIntOrNull()?.let { id ->
                    screen.gameDataCatalog.unitProfile(id)?.name?.takeIf(String::isNotBlank) ?: "유닛 $id"
                }.orEmpty(),
            ) },
            overlay = screen.hallOverlayVariant?.takeIf(observableOverlays::contains)?.let { overlayInput(screen, it) },
            hallInfo = screen.hallInfo?.let { ScenarioFrameHallInfo.valueOf(it.name) },
            background = ScenarioFrameBackgroundEvidence(
                screen.playback.stage.backgroundId,
                screen.hallManagement == HallManagement.EQUIP || screen.hallEquipConfirmation != null,
            ),
            units = screen.playback.stage.units.values.filter { it.visible }.map { unit ->
                ScenarioFrameUnitEvidence(
                    unit.id, unit.visualX, unit.visualY, unit.direction,
                    screen.gameDataCatalog.unitProfile(unit.id)?.mapAvatar ?: unit.id,
                )
            },
            management = screen.hallManagement?.takeIf { it != HallManagement.EQUIP }?.let { managementInput(screen, it) },
            equip = screen.hallManagement?.takeIf { it == HallManagement.EQUIP }?.let { equipInput(screen) },
            unitList = unitList,
            confirmation = screen.hallEquipConfirmation?.let { confirmation ->
                ScenarioEquipConfirmationEvidenceView(screen.hallOverlayVariant, confirmation.values, confirmation.actionLabel)
            },
            commandVisible = screen.hallInfo == null && screen.hallManagement == null &&
                screen.hallEquipConfirmation == null && screen.playback.state.name == "COMPLETE" &&
                screen.playback.stage.menuVisible,
        )
    }

    /**
     * `overlayInput`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun overlayInput(screen: ScenarioScreen, variant: RuntimeScenarioOverlay) = ScenarioHallOverlayEvidenceInput(
        variant = variant,
        featsRows = screen.hallFeatsLayer?.view()?.rows.orEmpty().map {
            ScenarioHallFeatEvidenceRow(it.title, it.ability, it.phaseLabel, it.progressRatio, it.progressLabel)
        },
        featsHelpText = FeatsLayer.HELP_TEXT,
        magic = screen.hallMagicLayer?.magic?.let {
            ScenarioHallMagicEvidence(it.name, it.power ?: 0, it.cost, it.intro, it.icon, it.hit, it.eff)
        },
        modalText = screen.playback.currentModalText.orEmpty().sanitizeEvidenceText(),
        items = listOf(0, 4, 150).mapNotNull { id -> screen.gameDataCatalog.equipmentProfile(id)?.let { item ->
            id to ScenarioHallOverlayItemEvidence(
                item.name, item.icon, screen.gameDataCatalog.equipmentTypeName(item.itemType),
                screen.gameDataCatalog.purchasePrice(item), item.intro,
            )
        } }.toMap(),
        postsNames = (0..80).map(screen.gameDataCatalog::postsName),
    )

    /**
     * `equipInput`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun equipInput(screen: ScenarioScreen): ScenarioHallEquipEvidenceInput {
        val unitId = screen.hallEquipUnitId()
        val catalog = screen.gameDataCatalog
        val unit = catalog.unitProfile(unitId) ?: catalog.unitProfile(0)
        screen.campaign.inventory.ensureDefaultEquipment(unitId, catalog)
        val level = screen.campaign.unitAttribute(unitId, 18, unit?.level ?: 1)
        val posts = screen.campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)
        val profile = unit?.let { catalog.battleProfile(it.id, (level - 1).coerceAtLeast(0), posts) }
        val bonus = screen.campaign.inventory.equipment[unitId]?.let {
            catalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1)
        } ?: GameDataCatalog.EquipmentBonus()
        val equipped = screen.campaign.inventory.equippedItems().filter { it.unitId == unitId }
        /**
         * `slot`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun slot(matches: (Int) -> Boolean): ScenarioHallEquipEvidenceSlot {
            val item = equipped.firstOrNull { catalog.equipmentProfile(it.itemId)?.itemType?.let(matches) == true }
            val itemProfile = item?.let { catalog.equipmentProfile(it.itemId) }
            return ScenarioHallEquipEvidenceSlot(itemProfile?.name ?: "없음", item?.level ?: 1, item?.experience ?: 0, itemProfile?.icon)
        }
        /**
         * `face` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val face = when (unitId) {
            0 -> (unit?.face ?: unitId).let { if (it <= 3) it + 1 else it }
            157 -> 214
            else -> unit?.face ?: unitId
        }
        return ScenarioHallEquipEvidenceInput(
            screen.hallOverlayVariant,
            screen.campaign.unitNames[unitId] ?: unit?.name ?: "조조",
            if (unitId == 0) "군웅" else catalog.armProfile(profile?.arm?.id ?: posts)?.name ?: "군웅",
            face,
            profile?.level ?: 1,
            listOf(profile?.maxHitPoints ?: 0, profile?.maxMagicPoints ?: 0, (profile?.attack ?: 0) + bonus.attack, (profile?.spirit ?: 0) + bonus.spirit, (profile?.defense ?: 0) + bonus.defense, profile?.critical ?: 0, profile?.morale ?: 0, profile?.movement ?: 0),
            listOf(slot { it < 20 }, slot { it in 20..25 }, ScenarioHallEquipEvidenceSlot("없음", 1, 0, null)),
        )
    }

    /**
     * `managementInput`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun managementInput(screen: ScenarioScreen, kind: HallManagement): ScenarioHallManagementEvidenceInput {
        val unitId = screen.hallEquipUnitId()
        val catalog = screen.gameDataCatalog
        val unit = catalog.unitProfile(unitId)
        val level = screen.campaign.unitAttribute(unitId, 18, unit?.level ?: 1)
        val profile = unit?.let { catalog.battleProfile(unitId, (level - 1).coerceAtLeast(0), screen.campaign.unitAttribute(unitId, 17, it.posts)) }
        screen.campaign.inventory.ensureDefaultEquipment(unitId, catalog)
        val bonus = screen.campaign.inventory.equipment[unitId]?.let {
            catalog.equipmentBonus(it.asScriptValues(), profile?.level ?: 1)
        } ?: GameDataCatalog.EquipmentBonus()
        // 원본은 무기 칸과 보구 칸을 나란히 제출한다. 보구 칸은 화면 아래로 밀려
        // 스크롤뷰가 잘라내지만 draw 자체는 남으므로, 이식본도 두 칸을 모두 낸다.
        val equippedSlots = screen.campaign.inventory.equippedItems()
            .filter { it.unitId == unitId }
            .mapNotNull { equipped ->
                catalog.equipmentProfile(equipped.itemId)?.let {
                    // 아이콘 프레임은 아이템 번호가 아니라 자료표의 아이콘 번호를 따른다.
                    ScenarioHallManagementEquipment(it.name, equipped.level, "${it.icon}-1")
                }
            }
        val weapon = equippedSlots.getOrNull(0)
        val treasure = equippedSlots.getOrNull(1)
        return ScenarioHallManagementEvidenceInput(
            ScenarioHallManagementEvidenceKind.valueOf(kind.name),
            screen.campaign.money,
            screen.hallViews.buyCandidates().take(SHOP_ROWS_ON_SCREEN).map { item ->
                ScenarioHallManagementBuyRow(item.name, catalog.equipmentTypeName(item.itemType), screen.campaign.inventory.items[item.id] ?: 0, catalog.purchasePrice(item))
            },
            ScenarioHallManagementUnitEvidence(
                unit?.name ?: "조조",
                catalog.postsName(screen.campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)).ifEmpty { "군웅" },
                level,
                listOf(profile?.maxHitPoints ?: 0, profile?.maxMagicPoints ?: 0, (profile?.attack ?: 0) + bonus.attack, profile?.spirit ?: 0, (profile?.defense ?: 0) + bonus.defense, profile?.critical ?: 0, profile?.morale ?: 0, profile?.movement ?: 0),
                weapon,
                treasure,
            ),
        )
    }

    /**
     * `SHOP_ROWS_ON_SCREEN` (상태 값): 상점 목록이 화면에 내놓는 줄 수를 보관한다.
     *
     * 원본 스크롤뷰는 화면에 조금이라도 걸치는 줄을 모두 제출하고 잘라내기는 GPU에
     * 맡긴다. 줄 높이 151.36, 간격 153.08, 첫 줄 y=369.069인 688 높이 화면에서
     * `y + 높이 > 0`을 만족하는 마지막 줄은 세 번째 다음 줄이다. 예전에는 이 값이
     * 그냥 3으로 적혀 있어 원본이 내놓는 마지막 줄 하나가 통째로 빠져 있었다.
     */
    private const val SHOP_ROWS_ON_SCREEN = 4
}

/**
 * `String`: 타입의 핵심 동작을 수행한다.
 * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

private fun String.sanitizeEvidenceText(): String = replace(Regex("\\[C[0-9A-Fa-f]+"), "").replace('☆', '★')
