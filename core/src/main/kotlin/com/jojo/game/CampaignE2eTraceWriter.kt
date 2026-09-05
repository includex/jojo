package com.jojo.game

import com.badlogic.gdx.Gdx
import java.nio.file.Files
import java.nio.file.Path

/** Verification artifact writer; it owns the JSON marker contract, not screen driving. */
internal object CampaignE2eTraceWriter {
    data class Snapshot(
        val route: List<String>,
        val inputs: List<String>,
        val inputRecords: List<CampaignE2eInputRecord>,
        val transitionEnterCount: Int,
        val playerMoveBeforeScene1: Boolean,
        val committedPlayerMove: String?,
        val initialBattleScenes: Set<String>,
        val campaignStages: List<Int>,
        val battlePreparations: List<String>,
        val sawR01DepartureDialogue: Boolean,
    )

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

    private fun escape(value: String) = value.replace("\\", "\\\\").replace("\"", "\\\"")
}
