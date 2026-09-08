import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer

// Desktop-render verification belongs here: this project owns the launcher
// and has the only test -> production dependency direction (verification -> core).
val verificationDesktopSourceSets = extensions.getByType<SourceSetContainer>()
val verificationDesktopRuntime = verificationDesktopSourceSets.named("main").get().runtimeClasspath
val winConditionsFixture = rootProject.file("tools/fixtures/win_conditions_layer_cases.json")
val winConditionsTraceDir = layout.buildDirectory.dir("verification/win-conditions")

val dumpWinConditionsGameTrace = tasks.register<JavaExec>("dumpWinConditionsGameTrace") {
    group = "verification"; dependsOn(tasks.named("classes")); classpath = verificationDesktopRuntime
    mainClass.set("com.jojo.game.verification.LayerTraceDump")
    args(winConditionsFixture.absolutePath, winConditionsTraceDir.get().file("game.json").asFile.absolutePath)
}
val verifyWinConditionsPairwise = tasks.register<Exec>("verifyWinConditionsPairwise") {
    group = "verification"; dependsOn(dumpWinConditionsGameTrace)
    inputs.files(winConditionsFixture, rootProject.file("tools/win_conditions_source_trace_harness.js"), rootProject.file("tools/verify_win_conditions_pairwise.mjs"))
    commandLine("node", rootProject.file("tools/verify_win_conditions_pairwise.mjs").absolutePath, winConditionsFixture.absolutePath, winConditionsTraceDir.get().file("source.json").asFile.absolutePath, winConditionsTraceDir.get().file("game.json").asFile.absolutePath)
}

val verifyYingchuanActorState = tasks.register<Exec>("verifyYingchuanActorState") {
    group = "verification"
    inputs.files(rootProject.file("tools/verify_yingchuan_actor_state.mjs"), rootProject.file("tools/verify_yingchuan_dialogue_fixture.py"), rootProject.file("tools/export_map_assets.py"), rootProject.file("core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules"))
    environment("JOJO_VERIFICATION_CLASSPATH", verificationDesktopRuntime.asPath)
    commandLine("node", rootProject.file("tools/verify_yingchuan_actor_state.mjs").absolutePath)
}

val verifyFreshBattleRenderParity = tasks.register<Exec>("verifyFreshBattleRenderParity") {
    group = "verification"; dependsOn(tasks.named("classes"))
    inputs.files(rootProject.file("tools/verify_fresh_battle_render_parity.mjs"), rootProject.file("tools/verify_yingchuan_actor_state.mjs"), rootProject.file("tools/compare_battle_render_frames.py"), rootProject.file("tools/compare_render_logs.py"))
    outputs.upToDateWhen { false }
    environment("JOJO_VERIFICATION_CLASSPATH", verificationDesktopRuntime.asPath)
    commandLine("node", rootProject.file("tools/verify_fresh_battle_render_parity.mjs").absolutePath)
}
// StartBattleLayer/BattleSortLayer/RewardLayer 상태 여덟 개는 desktop -> verification
// 모듈 분리 때 생산 태스크를 통째로 잃어버려, 손으로 돌린 낡은 산출물만 남아 있었다.
val verifyStartBattleRewardParity = tasks.register<Exec>("verifyStartBattleRewardParity") {
    group = "verification"; dependsOn(tasks.named("classes")); outputs.upToDateWhen { false }
    inputs.files(rootProject.file("tools/verify_start_battle_reward_parity.mjs"), rootProject.file("tools/compare_render_logs.py"))
    environment("JOJO_VERIFICATION_CLASSPATH", verificationDesktopRuntime.asPath)
    commandLine("node", rootProject.file("tools/verify_start_battle_reward_parity.mjs").absolutePath)
}
// `render_parity_scope.json`이 zero-draw로 넘기던 레이어들은 "운영 호출자가
// 없다"는 주장만 있고 확인은 없었다. 회수된 원본에서 그 주장을 다시 세운다.
val verifyNoRouteLayers = tasks.register<Exec>("verifyNoRouteLayers") {
    group = "verification"; outputs.upToDateWhen { false }
    inputs.files(rootProject.file("tools/verify_no_route_layers.py"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules"))
    commandLine("python3", rootProject.file("tools/verify_no_route_layers.py").absolutePath)
}
// 원본·이식본 양쪽에서 다시 뽑아 비교하는 화면 상태 목록이다.
val verifyRenderParityRoutes = tasks.register<Exec>("verifyRenderParityRoutes") {
    group = "verification"; dependsOn(tasks.named("classes")); outputs.upToDateWhen { false }
    inputs.files(rootProject.file("tools/verify_render_parity_routes.mjs"), rootProject.file("tools/render_parity_routes.json"), rootProject.file("tools/compare_render_logs.py"))
    environment("JOJO_VERIFICATION_CLASSPATH", verificationDesktopRuntime.asPath)
    commandLine("node", rootProject.file("tools/verify_render_parity_routes.mjs").absolutePath)
}
val verifyRenderParityScope = tasks.register<Exec>("verifyRenderParityScope") {
    group = "verification"; dependsOn(verifyFreshBattleRenderParity, verifyStartBattleRewardParity, verifyRenderParityRoutes, verifyNoRouteLayers); outputs.upToDateWhen { false }
    inputs.files(rootProject.file("tools/render_parity_scope.json"), rootProject.file("tools/render_layer_inventory.json"), rootProject.file("tools/verify_render_parity_scope.py"), rootProject.file("tools/verify_render_parity_reports.py"))
    commandLine("python3", rootProject.file("tools/verify_render_parity_scope.py").absolutePath, "--scope", rootProject.file("tools/render_parity_scope.json").absolutePath, "--repository", rootProject.projectDir.absolutePath)
}

// 캡처 상태 실행: --capture-state=/--capture= 는 검증 전용 경로라 운영 런처가 거부한다.
// 검증기들이 :desktop:run 대신 이 태스크를 쓰도록 해서 두 경계를 섞지 않는다.
tasks.register<JavaExec>("captureProductionState") {
    group = "verification"; dependsOn(tasks.named("classes")); classpath = verificationDesktopRuntime
    mainClass.set("com.jojo.game.verification.VerificationDesktopLauncher")
    if (System.getProperty("os.name").contains("Mac", true)) jvmArgs("-XstartOnFirstThread")
    jvmArgs("--enable-native-access=ALL-UNNAMED")
    val stateProperty = providers.gradleProperty("jojo.capture.state")
    val captureProperty = providers.gradleProperty("jojo.capture.png")
    val scenarioProperty = providers.gradleProperty("jojo.capture.scenario")
    argumentProviders.add {
        listOf(
            "--battle",
            "--scenario=${scenarioProperty.getOrElse("S_00")}",
            "--capture-state=${stateProperty.get()}",
            "--capture=${captureProperty.get()}",
        )
    }
}

val verifyYingchuanSelectionRender = tasks.register<Exec>("verifyYingchuanSelectionRender") {
    group = "verification"
    inputs.files(rootProject.file("tools/verify_yingchuan_selection_render.mjs"), rootProject.file("tools/export_map_assets.py"), rootProject.file("core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules"))
    commandLine("node", rootProject.file("tools/verify_yingchuan_selection_render.mjs").absolutePath)
}
val verifyYingchuanModalCaptures = tasks.register<Exec>("verifyYingchuanModalCaptures") {
    group = "verification"
    inputs.files(rootProject.file("tools/verify_yingchuan_modal_captures.mjs"), rootProject.file("core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt"))
    commandLine("node", rootProject.file("tools/verify_yingchuan_modal_captures.mjs").absolutePath)
}

val yingchuanBattleRegressionTrace = layout.buildDirectory.file("reports/yingchuan-battle-regression-trace.json")
val captureYingchuanBattleRegressionTrace = tasks.register<JavaExec>("captureYingchuanBattleRegressionTrace") {
    group = "verification"; dependsOn(tasks.named("classes")); classpath = verificationDesktopRuntime
    mainClass.set("com.jojo.game.verification.VerificationDesktopLauncher")
    if (System.getProperty("os.name").contains("Mac", true)) jvmArgs("-XstartOnFirstThread")
    jvmArgs("--enable-native-access=ALL-UNNAMED")
    args("--battle", "--scenario=S_00", "--full-battle-trace=${yingchuanBattleRegressionTrace.get().asFile.absolutePath}", "--full-battle-time-scale=8", "--full-battle-max-sim-seconds=600", "--full-battle-seed=1000", "--full-battle-math-seed=305419896")
    inputs.files(project(":core").extensions.getByType<SourceSetContainer>().named("main").get().allSource, rootProject.file("tools/verify_yingchuan_battle_regression.mjs")); outputs.file(yingchuanBattleRegressionTrace)
    doFirst { delete(yingchuanBattleRegressionTrace.get().asFile) }
    // 게임이 추적을 남기지 않고 종료 코드 0으로 빠지면 Gradle이 산출물 없는 태스크를 성공으로
    // 기록해 다음 실행에서 UP-TO-DATE로 건너뛴다. 그러면 실패가 검증 단계까지 밀려 원인이
    // 캡처인지 대조인지 흐려진다. 캡처 자리에서 바로 실패시킨다.
    doLast {
        val trace = yingchuanBattleRegressionTrace.get().asFile
        if (!trace.isFile) {
            throw GradleException(
                "전투 추적이 기록되지 않았다: ${trace.absolutePath}. " +
                    "전투는 끝났지만 스크립트가 완료 상태에 도달하지 못했을 수 있다.",
            )
        }
    }
}
val verifyYingchuanBattleRegression = tasks.register<Exec>("verifyYingchuanBattleRegression") {
    group = "verification"; dependsOn(captureYingchuanBattleRegressionTrace)
    inputs.files(yingchuanBattleRegressionTrace, rootProject.file("tools/verify_yingchuan_battle_regression.mjs"))
    commandLine("node", rootProject.file("tools/verify_yingchuan_battle_regression.mjs").absolutePath, yingchuanBattleRegressionTrace.get().asFile.absolutePath)
}

tasks.named("check") { dependsOn(verifyWinConditionsPairwise, verifyRenderParityScope, verifyYingchuanSelectionRender, verifyYingchuanModalCaptures, verifyYingchuanBattleRegression) }

// --- 대화창(SayLayer/DialogueLayer) 단계별 렌더 캡처 -------------------------
// 원본 캡처와 동일한 단계 구성으로 게임 화면을 캡처한다. 연속 단계를 차분하면
// 대화창 구성요소(패널/초상화/화자/본문)의 픽셀만 분리된다.
val dialogueStageDir = layout.buildDirectory.dir("verification/dialogue-stages")
val dialogueStages = listOf("characters", "panel", "portrait", "speaker", "text")
val dialogueStageCaptures = dialogueStages.map { stage ->
    tasks.register<JavaExec>("captureDialogueStage${stage.replaceFirstChar { it.uppercase() }}") {
        group = "verification"
        description = "Captures the street dialogue '$stage' stage as raw RGBA."
        dependsOn(tasks.named("classes"))
        classpath = verificationDesktopRuntime
        mainClass.set("com.jojo.game.verification.VerificationDesktopLauncher")
        if (System.getProperty("os.name").contains("Mac", true)) jvmArgs("-XstartOnFirstThread")
        jvmArgs("--enable-native-access=ALL-UNNAMED")
        val raw = dialogueStageDir.map { it.file("game-$stage.rgba") }
        val png = dialogueStageDir.map { it.file("game-$stage.png") }
        outputs.files(raw, png)
        doFirst {
            raw.get().asFile.parentFile.mkdirs()
            setArgs(
                listOf(
                    "--scenario=R_00",
                    "--capture-state=street-$stage",
                    "--capture-raw=${raw.get().asFile.absolutePath}",
                    "--capture=${png.get().asFile.absolutePath}",
                ),
            )
        }
    }
}

tasks.register("captureDialogueStages") {
    group = "verification"
    description = "Captures every street dialogue stage used by the dialogue geometry comparison."
    dependsOn(dialogueStageCaptures)
}

// --- 전투 대사창(SayLayer) 단계별 렌더 캡처 ---------------------------------
// 거리 대사와 같은 누적 구성이다. 원본은
// .verification-work/raw-framebuffer-common-space/dialogue-components/source-<stage>.rgba 이다.
val battleDialogueStageDir = layout.buildDirectory.dir("verification/battle-dialogue-stages")
val battleDialogueStages = listOf("panel", "portrait", "speaker", "text")
val battleDialogueStageCaptures = battleDialogueStages.map { stage ->
    tasks.register<JavaExec>("captureBattleDialogueStage${stage.replaceFirstChar { it.uppercase() }}") {
        group = "verification"
        description = "Captures the S_00 battle dialogue '$stage' stage as raw RGBA."
        dependsOn(tasks.named("classes"))
        classpath = verificationDesktopRuntime
        mainClass.set("com.jojo.game.verification.VerificationDesktopLauncher")
        if (System.getProperty("os.name").contains("Mac", true)) jvmArgs("-XstartOnFirstThread")
        jvmArgs("--enable-native-access=ALL-UNNAMED")
        val raw = battleDialogueStageDir.map { it.file("game-$stage.rgba") }
        val png = battleDialogueStageDir.map { it.file("game-$stage.png") }
        outputs.files(raw, png)
        doFirst {
            raw.get().asFile.parentFile.mkdirs()
            setArgs(
                listOf(
                    "--battle",
                    "--scenario=S_00",
                    "--capture-state=yingchuan-dialogue-components-$stage",
                    "--capture-raw=${raw.get().asFile.absolutePath}",
                    "--capture=${png.get().asFile.absolutePath}",
                ),
            )
        }
    }
}

tasks.register("captureBattleDialogueStages") {
    group = "verification"
    description = "Captures every battle dialogue stage used by the dialogue geometry comparison."
    dependsOn(battleDialogueStageCaptures)
}

// 검증 harness가 java를 직접 띄울 때 쓰는 런타임 classpath를 출력한다.
tasks.register("printVerificationClasspath") {
    group = "verification"
    description = "Prints the verification runtime classpath used by node/python harnesses."
    val classpath = verificationDesktopRuntime.asPath
    doLast { println(classpath) }
}
