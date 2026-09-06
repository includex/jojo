// Verification
package com.jojo.game.verification.campaign

import com.badlogic.gdx.Gdx
import java.nio.file.Files
import java.nio.file.Path

/** CampaignE2eTraceWriter: 화면 구동이 아닌 JSON 표식 계약을 소유하는 검증 산출물 작성기이다. */
internal object CampaignE2eTraceWriter {
    /** Snapshot: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    data class Snapshot(
        /** route: 검증 실행 계획을 담는다. */
        val route: List<String>,
        /** inputs: 검증 입력 목록을 담는다. */
        val inputs: List<String>,
        /** inputRecords: 검증 대상 목록을 담는다. */
        val inputRecords: List<CampaignE2eInputRecord>,
        /** transitionEnterCount: 검증 누적 횟수를 담는다. */
        val transitionEnterCount: Int,
        /** playerMoveBeforeScene1: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
        val playerMoveBeforeScene1: Boolean,
        /** committedPlayerMove: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
        val committedPlayerMove: String?,
        /** initialBattleScenes: 검증 대상 목록을 담는다. */
        val initialBattleScenes: Set<String>,
        /** campaignStages: 검증 대상 목록을 담는다. */
        val campaignStages: List<Int>,
        /** battlePreparations: 검증 대상 목록을 담는다. */
        val battlePreparations: List<String>,
        /** sawR01DepartureDialogue: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
        val sawR01DepartureDialogue: Boolean,
    )

    /** write: 검증 이벤트와 산출물을 기록한다. */
    fun write(
        config: CampaignE2eTraceConfig,
        snapshot: Snapshot,
        actualModule: String,
        actualSceneIndex: Int,
        forwardOvershoot: Boolean,
    ) {
        val expected = listOf(
            "TitleScreen",
            "ScenarioScreen:R_00",
            "ScenarioScreen:R_00:scene0",
            "ScenarioScreen:R_00:scene1",
            "ScenarioScreen:R_00:scene2",
            "ScenarioScreen:R_00:scene3",
            "BattleScreen:S_00",
            "BattleScreen:S_00:scene1",
            "BattleScreen:S_00:result-scene1",
            "BattleScreen:S_00:scene2",
            "BattleScreen:S_00:save-prompt",
            "ScenarioScreen:R_01",
            "ScenarioScreen:R_01:scene0",
            "ScenarioScreen:R_01:scene1",
        )
        if (config.requireYingchuanBootstrapContract) check(snapshot.route == expected) { "campaign E2E route mismatch: ${snapshot.route}" }
        check(snapshot.transitionEnterCount == 0) { "campaign E2E required ${snapshot.transitionEnterCount} extra Enter inputs" }
        if (snapshot.initialBattleScenes.isNotEmpty()) checkNotNull(snapshot.committedPlayerMove) { "missing committed player move provenance" }
        val output = Path.of(config.outputPath).toAbsolutePath()
        output.parent?.let(Files::createDirectories)
        val route = snapshot.route.joinToString(",") { "\"${escape(it)}\"" }
        val inputs = snapshot.inputs.joinToString(",") { "\"${escape(it)}\"" }
        val inputRecords = snapshot.inputRecords.joinToString(",") { record ->
            "{\"event\":\"${escape(record.event)}\",\"accepted\":${record.accepted}," +
                    "\"before\":\"${escape(record.before)}\",\"after\":\"${escape(record.after)}\"}"
        }
        val move = snapshot.committedPlayerMove?.let { "\"${escape(it)}\"" } ?: "null"
        val preparations = snapshot.battlePreparations.joinToString(",") { "\"${escape(it)}\"" }
        Files.writeString(
            output,
            """{"format":"jojo-campaign-screen-e2e/v1","route":[$route],"inputs":[$inputs],"inputRecords":[$inputRecords],"transitionEnterCount":${snapshot.transitionEnterCount},"screenClassesVerified":true,"playerMoveBeforeScene1":${snapshot.playerMoveBeforeScene1},"committedPlayerMove":$move,"campaignStages":[${
                snapshot.campaignStages.joinToString(
                    ","
                )
            }],"stopPoint":{"module":"${escape(config.stopAt.module)}","sceneIndex":${config.stopAt.sceneIndex}},"actualStopPoint":{"module":"${
                escape(
                    actualModule
                )
            }","sceneIndex":$actualSceneIndex},"completion":"${if (forwardOvershoot) "forward-overshoot" else "checkpoint"}","battlePreparations":[$preparations],"sawR01DepartureDialogue":${snapshot.sawR01DepartureDialogue}}""",
        )
        val marker = if (forwardOvershoot) "CAMPAIGN_SCREEN_E2E_OVERSHOOT" else "CAMPAIGN_SCREEN_E2E_OK"
        Gdx.app.log(
            "JojoGame",
            "$marker: $output; requested=${config.stopAt.module}:scene${config.stopAt.sceneIndex}; actual=$actualModule:scene$actualSceneIndex; transitionEnterCount=0"
        )
    }

    /** escape: JSON 특수 문자를 이스케이프한다. */
    private fun escape(value: String) = value.replace("\\", "\\\\").replace("\"", "\\\"")
}
