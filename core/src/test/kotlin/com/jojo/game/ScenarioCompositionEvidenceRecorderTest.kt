package com.jojo.game

import com.jojo.game.presentation.scenario.evidence.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioCompositionEvidenceRecorderTest {
    private val recorder = ScenarioCompositionEvidenceRecorder()

    @Test fun `serializes immutable story and hall evidence in the source contract`() {
        val trace = recorder.record(
            ScenarioEvidenceView(
                moduleName = "R_00",
                playbackState = "DIALOGUE",
                backgroundId = 71,
                units = listOf(ScenarioEvidenceUnit(9, 10f, 2f, 3, 20, 15)),
                heads = listOf(ScenarioEvidenceHead(18, 90f, 100f, .5f)),
                dialogue = ScenarioEvidenceDialogue(0, true, 18, "a\"b\nc"),
                modal = ScenarioEvidenceModal("INFO", "x\\y"),
                hallMenu = ScenarioEvidenceHallMenu(4, 80, 25f),
                hallCommandVisible = true,
                hallManagement = ScenarioEvidenceHallManagement.EQUIP,
                hallInfo = ScenarioEvidenceHallInfo("forces", listOf(ScenarioEvidenceRect(1f, 2f, 3f, 4f))),
            ),
        )

        assertEquals(
            "{\"state\":\"R_00/DIALOGUE\",\"viewport\":[1280,688],\"backgroundId\":71," +
                "\"units\":[{\"id\":9,\"script\":[10.000,2.000],\"direction\":3,\"action\":20,\"asset\":32,\"rect\":[758.720,935.680,82.560,110.080]}]," +
                "\"heads\":[{\"id\":18,\"script\":[90.000,100.000],\"opacity\":0.500,\"rect\":[180.000,378.400,110.080,137.600]}]," +
                "\"dialogue\":{\"side\":0,\"top\":true,\"speakerId\":18,\"panelRect\":[274.541,428.710,686.280,164.260],\"faceRect\":[84.820,426.560,165.120,206.400],\"speakerBaseline\":[323.447,575.740],\"textBaseline\":[328.939,536.740],\"text\":\"a\\\"b\\nc\"}," +
                "\"modal\":{\"kind\":\"INFO\",\"text\":\"x\\\\y\",\"screenRect\":[0.000,0.000,1280.000,688.000],\"contentCenter\":[640,344]}," +
                "\"hallCommand\":{\"menuRect\":[31.000,318.200,51.600,51.600],\"battleRect\":[895.580,1.720,82.560,82.560],\"equipRect\":[978.140,1.720,82.560,82.560],\"buyRect\":[1060.700,1.720,82.560,82.560],\"sellRect\":[1143.260,1.720,82.560,82.560]}," +
                "\"hallMenu\":{\"panelRect\":[0.000,0.000,1280.000,125.560],\"buttons\":[[9.552,44.300,75.680,75.680],[85.454,44.300,75.680,75.680],[161.548,44.300,75.680,75.680],[237.996,44.300,75.680,75.680],[326.213,44.300,75.680,75.680],[402.115,44.300,75.680,75.680],[478.208,44.300,75.680,75.680],[555.939,44.300,75.680,75.680],[641.078,44.300,75.680,75.680]],\"eventRect\":[99.720,4.250,261.440,37.840],\"stageRect\":[366.950,4.230,278.640,37.840],\"barRect\":[717.400,16.700,258.000,12.900],\"valueWidth\":64.500,\"from\":4,\"to\":80}," +
                "\"hallManagement\":{\"kind\":\"equip\",\"rootRect\":[118.840,28.810,1042.320,630.380]},\"hallInfo\":{\"kind\":\"forces\",\"rootRect\":[142.490,68.370,995.020,551.260],\"contentRects\":[[1.000,2.000,3.000,4.000]]}}",
            trace,
        )
    }
}
