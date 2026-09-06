// Scenario
package com.jojo.game.presentation.scenario

import com.jojo.game.presentation.scenario.*

import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.presentation.scenario.hall.HallInfo
import com.jojo.game.presentation.scenario.hall.HallPropertyTab

/** 한 프레임의 화면 상태를 검증용 구성 모델로 투영합니다. */
internal object ScenarioRuntimeCompositionProjector {
    /** 시나리오 화면의 전투·대화·홀 상태를 증거 모델로 변환합니다. */
    fun project(screen: ScenarioScreen): ScenarioEvidenceView =
        ScenarioEvidenceView(
            moduleName = screen.moduleName,
            playbackState = screen.playback.state.toString(),
            backgroundId = screen.playback.stage.backgroundId,
            units = screen.playback.stage.units.values.filter { it.visible }.map { unit ->
                ScenarioEvidenceUnit(
                    unit.id, unit.visualX, unit.visualY, unit.direction, unit.action,
                    screen.gameDataCatalog.unitProfile(unit.id)?.mapAvatar ?: unit.id,
                )
            },
            heads = screen.playback.stage.heads.values.filter { it.opacity > 0f }.map {
                ScenarioEvidenceHead(it.characterId, it.visualX, it.visualY, it.opacity)
            },
            dialogue = screen.playback.currentDialogue?.let {
                ScenarioEvidenceDialogue(
                    screen.playback.currentDialogueSide,
                    screen.playback.currentDialogueAtTop,
                    it.speakerId?.toIntOrNull(),
                    screen.scenarioViewState.dialogueVisibleText,
                )
            },
            modal = screen.playback.currentModalKind?.takeIf { screen.playback.state.name == "MODAL" }?.let {
                ScenarioEvidenceModal(it.toString(), screen.playback.currentModalText.orEmpty())
            },
            hallMenu = hallMenu(screen),
            hallCommandVisible = screen.playback.state.name == "COMPLETE" && screen.playback.stage.menuVisible,
            hallManagement = screen.hallManagement?.let { ScenarioEvidenceHallManagement.valueOf(it.name) },
            hallInfo = hallInfo(screen),
        )

    private fun hallMenu(screen: ScenarioScreen): ScenarioEvidenceHallMenu? {
        val ambition = screen.playback.state.name == "MODAL" && screen.playback.currentModalKind?.name == "AMBITION"
        if (!screen.hallMenuOpen && !ambition) return null
        val tween = ((screen.playback.ambitionElapsedSeconds - 1.2f) / 1f).coerceIn(0f, 1f)
        return ScenarioEvidenceHallMenu(
            screen.playback.ambitionFrom,
            screen.playback.ambitionTo,
            if (screen.hallMenuOpen) screen.playback.stage.ambition.toFloat()
            else screen.playback.ambitionFrom + (screen.playback.ambitionTo - screen.playback.ambitionFrom) * tween,
        )
    }

    private fun hallInfo(screen: ScenarioScreen): ScenarioEvidenceHallInfo? = screen.hallInfo?.let { kind ->
        val rows = when (kind) {
            HallInfo.FORCES -> screen.campaign.joinedUnits.take(7).indices.map {
                ScenarioEvidenceRect(147.49f, 469.63f - it * 49f, 985.02f, 49f)
            }
            HallInfo.PROPERTY -> propertyRows(screen)
            HallInfo.TERRAIN -> (0 until 6).map { ScenarioEvidenceRect(249f, 453.56f - it * 64.5f, 854.07f, 64.5f) }
            HallInfo.TREASURE -> (0 until 6).map { ScenarioEvidenceRect(232.10f + it % 2 * 410.22f, 413.23f - it / 2 * 165.98f, 405.06f, 163.40f) }
            HallInfo.HELPER -> listOf(ScenarioEvidenceRect(139f, 103.07f, 1001.98f, 494.86f))
        }
        ScenarioEvidenceHallInfo(kind.name.lowercase(), rows)
    }

    private fun propertyRows(screen: ScenarioScreen): List<ScenarioEvidenceRect> {
        fun accepts(id: Int): Boolean = screen.gameDataCatalog.equipmentProfile(id)?.itemType?.let { type ->
            when (screen.hallPropertyTab) {
                HallPropertyTab.WEAPON -> type < 20
                HallPropertyTab.ARMOR -> type in 20..25
                HallPropertyTab.AUXILIARY -> type > 45 && id < 150
                HallPropertyTab.PROPERTY -> id >= 150 || type in 26..45
            }
        } ?: false
        val equipped = if (screen.hallPropertyTab == HallPropertyTab.PROPERTY) 0 else {
            screen.campaign.inventory.equippedItems().count { accepts(it.itemId) }
        }
        val count = (equipped + screen.campaign.inventory.items.count { accepts(it.key) }).coerceAtMost(7)
        return (0 until count).map { ScenarioEvidenceRect(217.42f, 481.58f - it * 67.08f, 846.56f, 65.36f) }
    }
}
