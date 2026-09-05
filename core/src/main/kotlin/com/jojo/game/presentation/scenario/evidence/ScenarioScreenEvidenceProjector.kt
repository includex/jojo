package com.jojo.game.presentation.scenario.evidence

import com.jojo.game.GameDataCatalog
import com.jojo.game.FeatsLayer
import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.application.runtime.RuntimeScenarioPresentation
import com.jojo.game.presentation.scenario.story.ScenarioStreetDialogueStages
import com.jojo.game.presentation.scenario.hall.HallManagement

/**
 * Converts the mutable screen state into the immutable input consumed by the
 * frame recorders.  The recorder boundary receives only value objects.
 */
internal object ScenarioScreenEvidenceProjector {
    private val overlayFixtures = setOf(
        "info", "get-item-equipment", "get-item-property", "item-equipment", "item-property",
        "item-discard-confirm", "map-info", "choice", "ambition", "ask", "command", "menu",
        "save", "save-confirm", "exclusive", "exclusive-tab1", "magic", "feats", "feats-help",
    )

    fun renderInput(screen: ScenarioScreen): ScenarioFrameEvidenceInput {
        val dialogue = screen.playback.currentDialogue
        if (screen.hallOverlayFixture == "skip-open") {
            check(requireNotNull(screen.hallSkipLayer).button && !screen.hallSkipLayer.panel && screen.hallSkipLayer.zIndex == 999)
        }
        val unitList = screen.hallUnitListLayer?.rows?.take(6)?.map { id ->
            val unit = screen.gameDataCatalog.unitProfile(id)
            ScenarioHallUnitListEvidenceRow(
                screen.campaign.unitNames[id] ?: if (id == 181) "병사 " else unit?.name ?: "무장",
                screen.gameDataCatalog.postsName(screen.campaign.unitAttribute(id, 17, unit?.posts ?: 0)),
            )
        }
        return ScenarioFrameEvidenceInput(
            fixture = screen.game.requestedCaptureState()?.removeSuffix("-fixture"),
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
            overlay = screen.hallOverlayFixture?.takeIf(overlayFixtures::contains)?.let { overlayInput(screen, it) },
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
                ScenarioEquipConfirmationEvidenceView(screen.hallOverlayFixture, confirmation.values, confirmation.actionLabel)
            },
            commandVisible = screen.hallInfo == null && screen.hallManagement == null &&
                screen.hallEquipConfirmation == null && screen.playback.state.name == "COMPLETE" &&
                screen.playback.stage.menuVisible,
        )
    }

    private fun overlayInput(screen: ScenarioScreen, fixture: String) = ScenarioHallOverlayEvidenceInput(
        fixture = fixture,
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
        fun slot(matches: (Int) -> Boolean): ScenarioHallEquipEvidenceSlot {
            val item = equipped.firstOrNull { catalog.equipmentProfile(it.itemId)?.itemType?.let(matches) == true }
            val itemProfile = item?.let { catalog.equipmentProfile(it.itemId) }
            return ScenarioHallEquipEvidenceSlot(itemProfile?.name ?: "없음", item?.level ?: 1, item?.experience ?: 0, itemProfile?.icon)
        }
        val face = when (unitId) {
            0 -> (unit?.face ?: unitId).let { if (it <= 3) it + 1 else it }
            157 -> 214
            else -> unit?.face ?: unitId
        }
        return ScenarioHallEquipEvidenceInput(
            screen.hallOverlayFixture,
            screen.campaign.unitNames[unitId] ?: unit?.name ?: "조조",
            if (unitId == 0) "군웅" else catalog.armProfile(profile?.arm?.id ?: posts)?.name ?: "군웅",
            face,
            profile?.level ?: 1,
            listOf(profile?.maxHitPoints ?: 0, profile?.maxMagicPoints ?: 0, (profile?.attack ?: 0) + bonus.attack, (profile?.spirit ?: 0) + bonus.spirit, (profile?.defense ?: 0) + bonus.defense, profile?.critical ?: 0, profile?.morale ?: 0, profile?.movement ?: 0),
            listOf(slot { it < 20 }, slot { it in 20..25 }, ScenarioHallEquipEvidenceSlot("없음", 1, 0, null)),
        )
    }

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
        val weapon = screen.campaign.inventory.equippedItems().firstOrNull { it.unitId == unitId }?.let { equipped ->
            catalog.equipmentProfile(equipped.itemId)?.let { ScenarioHallManagementEquipment(it.name, equipped.level) }
        }
        return ScenarioHallManagementEvidenceInput(
            ScenarioHallManagementEvidenceKind.valueOf(kind.name),
            screen.campaign.money,
            screen.hallViews.buyCandidates().take(3).map { item ->
                ScenarioHallManagementBuyRow(item.name, catalog.equipmentTypeName(item.itemType), screen.campaign.inventory.items[item.id] ?: 0, catalog.purchasePrice(item))
            },
            ScenarioHallManagementUnitEvidence(
                unit?.name ?: "조조",
                catalog.postsName(screen.campaign.unitAttribute(unitId, 17, unit?.posts ?: 0)).ifEmpty { "군웅" },
                level,
                listOf(profile?.maxHitPoints ?: 0, profile?.maxMagicPoints ?: 0, (profile?.attack ?: 0) + bonus.attack, profile?.spirit ?: 0, (profile?.defense ?: 0) + bonus.defense, profile?.critical ?: 0, profile?.morale ?: 0, profile?.movement ?: 0),
                weapon,
            ),
        )
    }
}

private fun String.sanitizeEvidenceText(): String = replace(Regex("\\[C[0-9A-Fa-f]+"), "").replace('☆', '★')
