package com.jojo.game
import com.jojo.game.domain.campaign.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * class  `GameStartupCoordinatorTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class GameStartupCoordinatorTest {
    @Test fun `entry-flow reset and capture routing happen before globals and normal entry`() {
        val state = CampaignState().apply {
            joinedUnits += 99
            globalVariables[7] = 1
        }
        val events = mutableListOf<String>()
        val configuration = GameLaunchConfiguration(
            entryPoint = GameEntryPoint.TITLE,
            scenarioRun = ScenarioRunConfiguration(globals = mapOf(7 to 8)),
            yingchuanEntryFlowTracePath = "trace.json",
        )

        coordinator(configuration, state, events, routeCaptureFixture = {
            assertTrue(state.joinedUnits.isEmpty())
            assertEquals(null, state.globalVariables[7])
            events += "capture"
            true
        }).start()

        assertEquals(listOf("capture"), events)
        assertEquals(null, state.globalVariables[7])
    }

    @Test fun `persisted scenario is selected only after globals are applied`() {
        val state = CampaignState()
        val events = mutableListOf<String>()
        val configuration = GameLaunchConfiguration(
            entryPoint = GameEntryPoint.SCENARIO,
            scenarioRun = ScenarioRunConfiguration(globals = mapOf(12 to 34)),
        )

        coordinator(configuration, state, events, savedScenario = {
            events += "saved"
            "R_19"
        }, showScenario = {
            assertEquals(34, state.globalVariables[12])
            events += "scenario:$it"
        }).start()

        assertEquals(listOf("capture", "saved", "scenario:R_19"), events)
    }

    @Test fun `explicit scenario bypasses persisted route`() {
        val state = CampaignState()
        val events = mutableListOf<String>()
        val configuration = GameLaunchConfiguration(
            entryPoint = GameEntryPoint.SCENARIO,
            initialScenario = "R_08",
            initialScenarioExplicit = true,
        )

        coordinator(configuration, state, events, savedScenario = {
            error("saved route must not be read")
        }).start()

        assertEquals(listOf("capture", "scenario:R_08"), events)
    }

    @Test fun `battle verification keeps priority over title`() {
        val state = CampaignState()
        val battleEvents = mutableListOf<String>()
        coordinator(
            GameLaunchConfiguration(
                entryPoint = GameEntryPoint.TITLE,
                verification = VerificationConfiguration(battle = true),
            ),
            state,
            battleEvents,
        ).start()
        assertEquals(listOf("capture", "battle"), battleEvents)

    }

    @Test fun `normal entry priority is battle then title then scenario`() {
        val titleEvents = mutableListOf<String>()
        coordinator(GameLaunchConfiguration(entryPoint = GameEntryPoint.TITLE), CampaignState(), titleEvents).start()
        assertEquals(listOf("capture", "title"), titleEvents)

        val scenarioEvents = mutableListOf<String>()
        coordinator(
            GameLaunchConfiguration(entryPoint = GameEntryPoint.SCENARIO, initialScenario = "R_08", initialScenarioExplicit = true),
            CampaignState(),
            scenarioEvents,
        ).start()
        assertEquals(listOf("capture", "scenario:R_08"), scenarioEvents)
    }

    private fun coordinator(
        configuration: GameLaunchConfiguration,
        state: CampaignState,
        events: MutableList<String>,
        routeCaptureFixture: () -> Boolean = {
            events += "capture"
            false
        },
        savedScenario: () -> String = { "R_00" },
        showScenario: (String) -> Unit = { events += "scenario:$it" },
    ) = GameStartupCoordinator(
        configuration = configuration,
        campaignState = state,
        routeCaptureFixture = routeCaptureFixture,
        showBattle = { events += "battle" },
        showTitle = { events += "title" },
        showScenario = showScenario,
        savedScenario = savedScenario,
    )
}
