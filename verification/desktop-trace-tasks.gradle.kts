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
val verifyRenderParityScope = tasks.register<Exec>("verifyRenderParityScope") {
    group = "verification"; dependsOn(verifyFreshBattleRenderParity); outputs.upToDateWhen { false }
    inputs.files(rootProject.file("tools/render_parity_scope.json"), rootProject.file("tools/render_layer_inventory.json"), rootProject.file("tools/verify_render_parity_scope.py"), rootProject.file("tools/verify_render_parity_reports.py"))
    commandLine("python3", rootProject.file("tools/verify_render_parity_scope.py").absolutePath, "--scope", rootProject.file("tools/render_parity_scope.json").absolutePath, "--repository", rootProject.projectDir.absolutePath)
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
