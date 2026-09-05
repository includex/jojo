package com.jojo.game.presentation.scenario.evidence

import com.jojo.game.RenderEventLog
import com.jojo.game.HallUnitRender

internal data class ScenarioFrameUnitEvidence(val id: Int, val visualX: Float, val visualY: Float, val direction: Int, val avatar: Int)
internal data class ScenarioFrameBackgroundEvidence(val id: Int, val equipFixture: Boolean)
internal enum class ScenarioFrameHallInfo { FORCES, HELPER, PROPERTY, TERRAIN, TREASURE }
internal data class ScenarioFrameEvidenceInput(
    val fixture: String?,
    val palace: Boolean,
    val section: Boolean,
    val street: ScenarioStoryEvidenceView.StreetDialogue?,
    val overlay: ScenarioHallOverlayEvidenceInput?,
    val hallInfo: ScenarioFrameHallInfo?,
    val background: ScenarioFrameBackgroundEvidence,
    val units: List<ScenarioFrameUnitEvidence>,
    val management: ScenarioHallManagementEvidenceInput?,
    val equip: ScenarioHallEquipEvidenceInput?,
    val unitList: List<ScenarioHallUnitListEvidenceRow>?,
    val confirmation: ScenarioEquipConfirmationEvidenceView?,
    val commandVisible: Boolean,
)

/** Coordinates small evidence recorders over a completed frame snapshot. */
internal class ScenarioFrameEvidenceRecorder(
    private val story: ScenarioStoryEvidenceRecorder,
    private val staticInfo: ScenarioStaticHallInfoEvidenceRecorder,
    private val property: ScenarioPropertyEvidenceRecorder,
    private val terrain: ScenarioTerrainEvidenceRecorder,
    private val treasure: ScenarioTreasureEvidenceRecorder,
) {
    fun record(input: ScenarioFrameEvidenceInput): String {
        when {
            input.fixture == "street-walk-direction" -> return HallUnitRender.walkingRenderEventLog()
            input.fixture == "street-walk-motion" -> return HallUnitRender.walkingMotionRenderEventLog()
            input.palace -> return story.record(ScenarioStoryEvidenceView.Palace)
            input.section -> return story.record(ScenarioStoryEvidenceView.Section)
            input.street != null -> return story.record(input.street)
        }
        val log = RenderEventLog()
        if (input.fixture == "hall-skip-open") {
            log.draw("hall-skip-open", "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f, "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>")
            log.draw("hall-skip-open", "HallLayer", "Canvas/Layer/button/Background", "sprite", 1386.356f, 361f, 92f, 78f, "skip")
            return log.jsonl()
        }
        input.overlay?.let { ScenarioHallOverlayEvidenceRecorder(it).append(log); return log.jsonl() }
        input.hallInfo?.let { appendInfo(log, it) } ?: appendBackground(log, input.background, input.units, input.management == null && input.equip == null && input.confirmation == null)
        input.management?.let { ScenarioHallManagementEvidenceRecorder(it).append(log) }
        input.equip?.let { equip ->
            val unitList = input.unitList
            ScenarioHallEquipEvidenceRecorder(equip).append(log, if (unitList == null) "hall-equip-stable" else "hall-unit-list-stable", if (unitList == null) "EquipLayer" else "UnitListLayer")
            unitList?.let { ScenarioHallUnitListEvidenceRecorder(it).append(log) }
        }
        input.confirmation?.let { ScenarioEquipConfirmationEvidenceRecorder().append(log, it) }
        if (input.commandVisible) appendCommands(log)
        return log.jsonl()
    }

    private fun appendInfo(log: RenderEventLog, kind: ScenarioFrameHallInfo) {
        log.draw("hall-info", "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1280f, 688f, "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>")
        log.draw("hall-info", "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1280f, 688f, "default_sprite_splash", opacity = .392f)
        when (kind) {
            ScenarioFrameHallInfo.FORCES -> staticInfo.appendForces(log)
            ScenarioFrameHallInfo.HELPER -> staticInfo.appendHelper(log)
            ScenarioFrameHallInfo.PROPERTY -> property.append(log, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.PROPERTY))
            ScenarioFrameHallInfo.TERRAIN -> terrain.append(log, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.TERRAIN))
            ScenarioFrameHallInfo.TREASURE -> treasure.append(log, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.TREASURE))
        }
    }

    private fun appendBackground(log: RenderEventLog, background: ScenarioFrameBackgroundEvidence, units: List<ScenarioFrameUnitEvidence>, drawUnits: Boolean) {
        log.draw("background", "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1280f, 688f, if (background.equipFixture && background.id == 71) "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>" else "maps/${background.id}.jpg", blend = if (background.equipFixture) listOf(770, 771) else "DISABLED")
        if (drawUnits) units.forEach { unit -> log.draw("characters", "HallLayer", "Canvas/Layer/map/unit-${unit.id}", "sprite", (unit.visualX - unit.visualY + 42) * 16f - 41.28f, 1073.28f - (unit.visualX + unit.visualY) * 6.88f - 55.04f, 82.56f, 110.08f, "map-avatar:${unit.avatar}:direction:${unit.direction}") }
    }

    private fun appendCommands(log: RenderEventLog) {
        log.draw("controls", "HallCommandLayer", "Canvas/HallCommandLayer/menu", "sprite", 31f, 318.2f, 51.6f, 51.6f, "maps/ui/hall-command/menu.png")
        listOf("battle", "equip", "buy", "sell").forEachIndexed { index, name -> log.draw("controls", "HallCommandLayer", "Canvas/HallCommandLayer/$name", "sprite", 895.58f + index * 82.56f, 1.72f, 82.56f, 82.56f, "maps/ui/hall-command/$name.png") }
    }
}
