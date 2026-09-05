package com.jojo.game
import com.jojo.game.application.scenario.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.campaign.*

/**
 * Coordinates campaign bootstrap and the final application entry point.
 * Screen construction remains behind narrow callbacks owned by [JojoGame].
 */
internal class GameStartupCoordinator(
    private val configuration: GameLaunchConfiguration,
    private val campaignState: CampaignState,
    private val routeRuntimeStartup: () -> Boolean,
    private val showBattle: () -> Unit,
    private val showTitle: () -> Unit,
    private val showScenario: (String) -> Unit,
    private val savedScenario: () -> String,
) {
    /**
     * 공개 메서드 `start`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun start() {
        if (configuration.yingchuanEntryFlowTracePath != null) campaignState.reset()
        if (routeRuntimeStartup()) return

        val directBattleScenario = configuration.initialScenario.replaceFirst("R_", "S_")
        if (configuration.entryPoint == GameEntryPoint.BATTLE &&
            Regex("S_(?:[0-4][0-9]|5[0-7])").matches(directBattleScenario) &&
            configuration.capture.state != "map-only"
        ) {
            if (configuration.fullBattleTrace != null) {
                val routeIndex = directBattleScenario.removePrefix("S_").toInt()
                val entryLimit = if (routeIndex == 0) null else
                    ScenarioMetadataReader.loadLastJoinBattleLimit("R_%02d".format(routeIndex))
                prepareDirectFullBattleTraceCampaign(campaignState, directBattleScenario, entryLimit)
            } else {
                prepareYingchuanBattleCampaign()
            }
        }

        configuration.scenarioRun.globals.forEach { (id, value) -> campaignState.globalVariables[id] = value }
        when {
            configuration.verification.battle ||
                    configuration.verification.scriptedBattle ||
                    configuration.entryPoint == GameEntryPoint.BATTLE -> showBattle()

            configuration.entryPoint == GameEntryPoint.TITLE -> showTitle()
            else -> {
                val scenario = if (!configuration.initialScenarioExplicit &&
                    configuration.initialScenario == "R_00"
                ) savedScenario() else configuration.initialScenario
                showScenario(scenario)
            }
        }
    }

    private fun prepareYingchuanBattleCampaign() {
        campaignState.reset()
        val prelude = ScenarioInterpreter.load("R_00", campaignState)
        prelude.start("scene1")
        var steps = 0
        while (prelude.state != PlaybackState.COMPLETE && steps++ < 10_000) {
            when (prelude.state) {
                PlaybackState.DIALOGUE -> prelude.advanceDialogue()
                PlaybackState.CHOICE -> {
                    prelude.currentChoice?.options
                        ?.indexOfFirst { it.contains("게임 시작") }
                        ?.takeIf { it >= 0 }
                        ?.let(prelude::selectChoice)
                    prelude.confirmChoice()
                }

                PlaybackState.DELAY -> prelude.skipDelay()
                PlaybackState.MODAL -> prelude.resumeModal()
                PlaybackState.COMPLETE -> Unit
            }
        }
        check(prelude.state == PlaybackState.COMPLETE) { "영천 캡처용 R_00 도입을 완료하지 못했습니다." }
        check(campaignState.joinedUnits.isNotEmpty()) { "영천 캡처용 아군 명단이 비어 있습니다." }
        campaignState.roster.seedStartupRoster(
            if (configuration.capture.state?.startsWith("yingchuan-") != true) {
                campaignState.joinedUnits.take(15)
            } else {
                emptyList()
            },
        )
    }
}

/**
 * Fresh-profile prerequisite used by direct full-battle diagnostics.
 * Yingchuan needs Cao Cao only. Later battles honor their authored
 * setJoinBattle maximum, required and excluded unit IDs.
 */
internal fun prepareDirectFullBattleTraceCampaign(
    state: CampaignState,
    scenario: String,
    entryLimit: ScenarioJoinBattleLimit? = null,
): List<Int> {
    val match = Regex("S_(\\d{2})").matchEntire(scenario)
    val index = match?.groupValues?.get(1)?.toIntOrNull()
    require(index != null && index in 0..57) { "full-battle scenario must be S_00 through S_57: $scenario" }
    val seeded = if (index == 0) {
        listOf(0)
    } else {
        val limit = requireNotNull(entryLimit) {
            "$scenario direct full-battle trace requires its authored R-module setJoinBattle contract"
        }
        val excluded = limit.excludedUnitIds.toSet()
        val mandatory = buildList {
            if (0 !in excluded) add(0)
            limit.requiredUnitIds.forEach { id -> if (id !in excluded && id !in this) add(id) }
        }
        require(mandatory.size <= limit.maximum) {
            "$scenario has ${mandatory.size} mandatory units but maximum is ${limit.maximum}"
        }
        (mandatory + (0..511).filter { it !in excluded && it !in mandatory }).take(limit.maximum)
    }
    state.reset()
    state.joinedUnits += seeded
    state.roster.seedStartupRoster(seeded)
    val data = GameDataCatalog.load()
    seeded.forEach {
        state.setUnitAttribute(it, 18, 3)
        // Joining has already created concrete equipment. Preserve its
        // initial level rather than recomputing it after a live unit level-up.
        state.inventory.ensureDefaultEquipment(it, data)
    }
    return seeded
}
