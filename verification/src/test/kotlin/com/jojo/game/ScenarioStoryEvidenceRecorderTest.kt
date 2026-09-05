package com.jojo.game

import com.jojo.game.presentation.scenario.*
import com.jojo.game.verification.scenario.evidence.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScenarioStoryEvidenceRecorderTest {
    private val recorder = ScenarioStoryEvidenceRecorder()

    @Test fun `panel fixture retains its byte exact JSONL contract`() {
        assertEquals(
            "{\"sequence\":0,\"frame\":0,\"timestamp\":0,\"phase\":\"hall-panel-stable\",\"layer\":\"HallLayer\",\"nodePath\":\"Canvas/Layer/Panel_cancel\",\"drawType\":\"sprite\",\"x\":0.000,\"y\":0.000,\"w\":1280.000,\"h\":688.000,\"assetId\":\"assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash\",\"opacity\":0.000,\"blend\":[770, 771],\"visible\":false,\"text\":null}\n" +
                "{\"sequence\":1,\"frame\":0,\"timestamp\":0,\"phase\":\"hall-panel-stable\",\"layer\":\"DialogueLayer\",\"nodePath\":\"Canvas/Layer/bg0/bg2\",\"drawType\":\"sprite\",\"x\":274.540,\"y\":55.470,\"w\":686.280,\"h\":164.260,\"assetId\":\"U_select_10-1\",\"opacity\":1.000,\"blend\":[770, 771],\"visible\":true,\"text\":null}\n",
            recorder.record(ScenarioStoryEvidenceView.StreetDialogue("panel", false, "", "")),
        )
    }

    @Test fun `characters fixture preserves source order and escaped dialogue`() {
        val lines = recorder.record(
            ScenarioStoryEvidenceView.StreetDialogue("characters", true, "가\n\"나\"", "조조"),
        ).lineSequence().filter(String::isNotBlank).toList()

        assertEquals(9, lines.size)
        assertTrue(lines[0].contains("Canvas/Layer/map\""))
        assertTrue(lines[1].contains("map/head/face"))
        assertTrue(lines[2].contains("map/head/face"))
        assertTrue(lines[6].contains("richtext") && lines[6].contains("가\\n\\\"나\\\""))
        assertTrue(lines.last().contains("Canvas/Layer/bg0/label") && lines.last().contains("조조"))
    }

    @Test fun `palace and section fixtures retain authored record counts`() {
        assertEquals(7, recorder.record(ScenarioStoryEvidenceView.Palace).lineSequence().count { it.isNotBlank() })
        assertEquals(6, recorder.record(ScenarioStoryEvidenceView.Section).lineSequence().count { it.isNotBlank() })
    }
}
