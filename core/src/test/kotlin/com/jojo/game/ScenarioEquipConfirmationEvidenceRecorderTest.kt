package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioEquipConfirmationEvidenceRecorderTest {
    @Test fun `equipment confirmation retains byte exact dynamic label JSONL`() {
        val log = RenderEventLog()
        ScenarioEquipConfirmationEvidenceRecorder().append(
            log,
            ScenarioEquipConfirmationEvidenceView("equip-route", listOf(0, 1, 2, -5, 10, 0, 2, 1), "장착"),
        )
        val lines = log.jsonl().lineSequence().filter(String::isNotBlank).toList()

        assertEquals(31, lines.size)
        assertEquals(
            "{\"sequence\":0,\"frame\":0,\"timestamp\":0,\"phase\":\"hall-equip-route-stable\",\"layer\":\"HallLayer\",\"nodePath\":\"Canvas/Layer/Panel_cancel\",\"drawType\":\"sprite\",\"x\":0.000,\"y\":0.000,\"w\":1280.000,\"h\":688.000,\"assetId\":\"default_sprite_splash\",\"opacity\":0.157,\"blend\":[770, 771],\"visible\":true,\"text\":null}",
            lines.first(),
        )
        assertEquals(
            "{\"sequence\":28,\"frame\":0,\"timestamp\":0,\"phase\":\"hall-equip-route-stable\",\"layer\":\"HallLayer\",\"nodePath\":\"Canvas/Layer/baseInfo/button0/Background/Label\",\"drawType\":\"label\",\"x\":493.800,\"y\":223.515,\"w\":86.000,\"h\":34.400,\"assetId\":null,\"opacity\":1.000,\"blend\":[\"SRC_ALPHA\", \"ONE_MINUS_SRC_ALPHA\"],\"visible\":true,\"text\":\"장착\"}",
            lines[28],
        )
        assertEquals(
            "{\"sequence\":30,\"frame\":0,\"timestamp\":0,\"phase\":\"hall-equip-route-stable\",\"layer\":\"HallLayer\",\"nodePath\":\"Canvas/Layer/baseInfo/button1/Background/Label\",\"drawType\":\"label\",\"x\":700.200,\"y\":223.515,\"w\":86.000,\"h\":34.400,\"assetId\":null,\"opacity\":1.000,\"blend\":[\"SRC_ALPHA\", \"ONE_MINUS_SRC_ALPHA\"],\"visible\":true,\"text\":\"취소\"}",
            lines.last(),
        )
    }
}
