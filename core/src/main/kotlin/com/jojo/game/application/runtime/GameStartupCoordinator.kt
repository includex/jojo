// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.presentation.i18n.SystemMessage
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.infrastructure.data.GamePackage

import com.jojo.game.*

import com.jojo.game.application.scenario.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.campaign.*

/** GameStartupCoordinator: 시작 설정을 해석해 캠페인을 준비하고 제목·시나리오·전투 화면으로 분기한다. */
internal class GameStartupCoordinator(
    /**
     * `configuration` (GameLaunchConfiguration,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val configuration: GameLaunchConfiguration,
    /**
     * `campaignState` (CampaignState,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val campaignState: CampaignState,
    /**
     * `routeRuntimeStartup` (() -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val routeRuntimeStartup: () -> Boolean,
    /**
     * `showBattle` (() -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val showBattle: () -> Unit,
    /**
     * `showTitle` (() -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val showTitle: () -> Unit,
    /**
     * `showScenario` ((String) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val showScenario: (String) -> Unit,
    /**
     * `savedScenario` (() -> String,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val savedScenario: () -> String,
) {

    /**
     * `start`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun start() {
        if (configuration.yingchuanEntryFlowTracePath != null) campaignState.reset()
        if (routeRuntimeStartup()) return

        val directBattleScenario = GamePackage.load().battleModule(configuration.initialScenario)
        if (configuration.entryPoint == GameEntryPoint.BATTLE &&
            directBattleScenario in GamePackage.load().battleModules.values &&
            configuration.capture.state != "map-only"
        ) {
            if (configuration.battleTraceRuntime != null) {
                val entryLimit = if (directBattleScenario == GamePackage.load().defaultBattleModule) null else
                    ScenarioMetadataReader.loadLastJoinBattleLimit(GamePackage.load().returnModule(directBattleScenario))
                prepareDirectBattleCampaign(campaignState, directBattleScenario, entryLimit)
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
                    configuration.initialScenario == GamePackage.load().startModule
                ) savedScenario() else configuration.initialScenario
                showScenario(scenario)
            }
        }
    }

    /**
     * `prepareYingchuanBattleCampaign`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun prepareYingchuanBattleCampaign() {
        campaignState.reset()
        val prelude = ScenarioInterpreter.load(GamePackage.load().startModule, campaignState)
        prelude.start(GamePackage.load().resumeScene)
        var steps = 0
        while (prelude.state != PlaybackState.COMPLETE && steps++ < 10_000) {
            when (prelude.state) {
                PlaybackState.DIALOGUE -> prelude.advanceDialogue()
                PlaybackState.CHOICE -> {
                    prelude.currentChoice?.options
                        ?.indexOfFirst { it.contains(SystemMessage.S_62D82D76E7) }
                        ?.takeIf { it >= 0 }
                        ?.let(prelude::selectChoice)
                    prelude.confirmChoice()
                }

                PlaybackState.DELAY -> prelude.skipDelay()
                PlaybackState.MODAL -> prelude.resumeModal()
                PlaybackState.COMPLETE -> Unit
            }
        }
        check(prelude.state == PlaybackState.COMPLETE) { SystemMessage.S_1990A92813 }
        check(campaignState.joinedUnits.isNotEmpty()) { SystemMessage.S_4DD32031A6 }
        campaignState.roster.seedStartupRoster(
            if (configuration.capture.state?.startsWith("yingchuan-") != true) {
                campaignState.joinedUnits.take(15)
            } else {
                emptyList()
            },
        )
    }
}

/** prepareDirectBattleCampaign: 지정 시나리오의 전투를 바로 열 수 있도록 캠페인 명단과 전장 상태를 구성한다. */
internal fun prepareDirectBattleCampaign(
    state: CampaignState,
    scenario: String,
    entryLimit: ScenarioJoinBattleLimit? = null,
): List<Int> {
    /**
     * `match` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    require(scenario in GamePackage.load().battleModules.values) {
        "full-battle scenario must be in game.yaml battleModules: $scenario"
    }
    /**
     * `seeded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val seeded = if (scenario == GamePackage.load().defaultBattleModule) {
        listOf(GamePackage.load().protagonistId)
    } else {
        /**
         * `limit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val limit = requireNotNull(entryLimit) {
            "$scenario direct full-battle trace requires its authored R-module setJoinBattle contract"
        }
        /**
         * `excluded` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val excluded = limit.excludedUnitIds.toSet()
        /**
         * `mandatory` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mandatory = buildList {
            if (GamePackage.load().protagonistId !in excluded) add(GamePackage.load().protagonistId)
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
    /**
     * `data` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val data = GameDataCatalog.load()
    seeded.forEach {
        state.setUnitAttribute(it, 18, 3)
        state.inventory.ensureDefaultEquipment(it, data)
    }
    return seeded
}
