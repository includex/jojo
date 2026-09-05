plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-headless:${property("gdxVersion")}")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${property("gdxVersion")}")
    implementation("com.badlogicgames.gdx:gdx-platform:${property("gdxVersion")}:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:${property("gdxVersion")}:natives-desktop")
}


tasks.register("printClasspath") {
    doLast {
        println(sourceSets["main"].runtimeClasspath.asPath)
    }
}


val winConditionsFixture = rootProject.file("tools/fixtures/win_conditions_layer_cases.json")
val winConditionsTraceDir = layout.buildDirectory.dir("verification/win-conditions")
val dumpWinConditionsGameTrace = tasks.register<JavaExec>("dumpWinConditionsGameTrace") {
    group = "verification"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.desktop.LayerTraceDump")
    args(winConditionsFixture.absolutePath, winConditionsTraceDir.get().file("game.json").asFile.absolutePath)
}
tasks.register<Exec>("verifyWinConditionsPairwise") {
    group = "verification"
    dependsOn(dumpWinConditionsGameTrace)
    inputs.file(winConditionsFixture)
    inputs.file(rootProject.file("tools/win_conditions_source_trace_harness.js"))
    inputs.file(rootProject.file("tools/verify_win_conditions_pairwise.mjs"))
    commandLine("node", rootProject.file("tools/verify_win_conditions_pairwise.mjs").absolutePath, winConditionsFixture.absolutePath, winConditionsTraceDir.get().file("source.json").asFile.absolutePath, winConditionsTraceDir.get().file("game.json").asFile.absolutePath)
}

/** Runs the recovered Electron battle through the same three dialogue inputs as the game. */
val verifyYingchuanActorState = tasks.register<Exec>("verifyYingchuanActorState") {
    group = "verification"
    description = "Compares original Cocos and LibGDX Yingchuan dialogue actors, text, and fixture geometry."
    inputs.file(rootProject.file("tools/verify_yingchuan_actor_state.mjs"))
    inputs.file(rootProject.file("tools/verify_yingchuan_dialogue_fixture.py"))
    inputs.file(rootProject.file("tools/export_map_assets.py"))
    inputs.file(rootProject.file("core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/electron/main.cjs"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/build/python-source-battle-verification-dialogue3.png"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules"))
    outputs.file(rootProject.layout.buildDirectory.file("yingchuan-actor-state.json"))
    commandLine("node", rootProject.file("tools/verify_yingchuan_actor_state.mjs").absolutePath)
}

/** Rebuilds the render evidence which is too timing-sensitive to accept from a prior check run. */
val verifyFreshBattleRenderParity = tasks.register<Exec>("verifyFreshBattleRenderParity") {
    group = "verification"
    description = "Freshly captures all Yingchuan dialogue PNGs and strict battle character/dialogue JSONL plus framebuffer pairs."
    dependsOn(tasks.classes)
    inputs.files(
        rootProject.file("tools/verify_fresh_battle_render_parity.mjs"),
        rootProject.file("tools/verify_yingchuan_actor_state.mjs"),
        rootProject.file("tools/verify_yingchuan_dialogue_fixture.py"),
        rootProject.file("tools/compare_battle_render_frames.py"),
        rootProject.file("tools/compare_render_logs.py"),
        rootProject.file("../jojo_mobile/sgccz-desktop/electron/main.cjs"),
    )
    // A passing report from yesterday is not evidence about today's renderer.
    outputs.upToDateWhen { false }
    environment("JOJO_DESKTOP_CLASSPATH", sourceSets.main.get().runtimeClasspath.asPath)
    commandLine("node", rootProject.file("tools/verify_fresh_battle_render_parity.mjs").absolutePath)
}

val verifyRenderParityScope = tasks.register<Exec>("verifyRenderParityScope") {
    group = "verification"
    description = "Validates the exhaustive render scope and requires selected battle evidence from this fresh capture run."
    dependsOn(verifyFreshBattleRenderParity)
    inputs.files(
        rootProject.file("tools/render_parity_scope.json"),
        rootProject.file("tools/render_layer_inventory.json"),
        rootProject.file("tools/verify_render_parity_scope.py"),
        rootProject.file("tools/verify_render_parity_reports.py"),
    )
    outputs.upToDateWhen { false }
    commandLine(
        "python3", rootProject.file("tools/verify_render_parity_scope.py").absolutePath,
        "--scope", rootProject.file("tools/render_parity_scope.json").absolutePath,
        "--repository", rootProject.projectDir.absolutePath,
    )
}

/** Compares the real source Control._process range/cursor overlay to the game framebuffer state. */
val verifyYingchuanSelectionRender = tasks.register<Exec>("verifyYingchuanSelectionRender") {
    group = "verification"
    description = "Compares original Cocos and LibGDX Yingchuan selection ranges, attack boxes, and cursor."
    inputs.file(rootProject.file("tools/verify_yingchuan_selection_render.mjs"))
    inputs.file(rootProject.file("tools/export_map_assets.py"))
    inputs.file(rootProject.file("core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/electron/main.cjs"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/recovered-js/modules"))
    outputs.file(rootProject.layout.buildDirectory.file("yingchuan-selection-render.json"))
    commandLine("node", rootProject.file("tools/verify_yingchuan_selection_render.mjs").absolutePath)
}

/** Ensures every original-menu modal still renders as a real desktop framebuffer. */
val verifyYingchuanModalCaptures = tasks.register<Exec>("verifyYingchuanModalCaptures") {
    group = "verification"
    description = "Checks original modal/SayLayer stacks and LibGDX modal capture states."
    inputs.file(rootProject.file("tools/verify_yingchuan_modal_captures.mjs"))
    inputs.file(rootProject.file("core/src/main/kotlin/com/jojo/game/presentation/battle/BattleScreen.kt"))
    inputs.file(rootProject.file("../jojo_mobile/sgccz-desktop/electron/main.cjs"))
    inputs.dir(rootProject.file("../jojo_mobile/sgccz-desktop/build"))
    outputs.file(rootProject.layout.buildDirectory.file("yingchuan-modal-captures.json"))
    commandLine("node", rootProject.file("tools/verify_yingchuan_modal_captures.mjs").absolutePath)
}

/**
 * Runs the production BattleScreen continuously and validates movement,
 * attack-hit-reaction timing, camera bounds and the terminal outcome. This is
 * intentionally separate from capture-state fixtures: its input is the live
 * frame trace emitted by the production S_00 BattleScreen after its explicit,
 * deterministic full-battle roster bootstrap.
 */
val yingchuanBattleRegressionTrace = layout.buildDirectory.file("reports/yingchuan-battle-regression-trace.json")
val captureYingchuanBattleRegressionTrace = tasks.register<JavaExec>("captureYingchuanBattleRegressionTrace") {
    group = "verification"
    description = "Runs the production S_00 BattleScreen and records its complete frame trace."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.desktop.DesktopLauncher")
    jvmArgs("-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED")
    args(
        "--battle",
        "--scenario=S_00",
        "--full-battle-trace=${yingchuanBattleRegressionTrace.get().asFile.absolutePath}",
        // Simulation timestamps, callback ordering and source-tick assertions
        // are invariant under this renderer time scale. Running at 1x emits
        // tens of thousands of large JSON frames and can take hours because
        // the recorder intentionally retains every frame for post-analysis.
        "--full-battle-time-scale=8",
        "--full-battle-max-sim-seconds=600",
        "--full-battle-seed=1000",
        "--full-battle-math-seed=305419896",
    )
    inputs.files(
        project(":core").sourceSets.main.get().allSource,
        rootProject.file("tools/verify_yingchuan_battle_regression.mjs"),
    )
    outputs.file(yingchuanBattleRegressionTrace)
    // Never allow a successful launcher exit that failed to emit a new trace
    // to reuse evidence from an earlier invocation.
    doFirst { delete(yingchuanBattleRegressionTrace.get().asFile) }
}
val verifyYingchuanBattleRegression = tasks.register<Exec>("verifyYingchuanBattleRegression") {
    group = "verification"
    description = "Validates the complete production S_00 battle trace without capture fixtures."
    dependsOn(captureYingchuanBattleRegressionTrace)
    inputs.file(yingchuanBattleRegressionTrace)
    inputs.file(rootProject.file("tools/verify_yingchuan_battle_regression.mjs"))
    commandLine(
        "node",
        rootProject.file("tools/verify_yingchuan_battle_regression.mjs").absolutePath,
        yingchuanBattleRegressionTrace.get().asFile.absolutePath,
    )
}

/**
 * Starts at the production TitleScreen and dispatches pointer/key events to
 * each screen's installed InputProcessor. The existing deterministic full
 * battle trace observes tactical turns only. The driver enables the source
 * entrusted-battle option through visible UI; production lifecycle advances it.
 */
val campaignScreenE2eTrace = layout.buildDirectory.file("reports/campaign-screen-e2e.json")
val campaignScreenE2eBattleTrace = layout.buildDirectory.file("reports/campaign-screen-e2e-battle.json")
val captureCampaignScreenE2e = tasks.register<JavaExec>("captureCampaignScreenE2e") {
    group = "verification"
    description = "Runs original Title -> R_00 -> S_00 -> save prompt -> R_01 through production screens and input."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("com.jojo.game.desktop.DesktopLauncher")
    jvmArgs("-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED")
    args(
        "--campaign-e2e-trace=${campaignScreenE2eTrace.get().asFile.absolutePath}",
        "--full-battle-trace=${campaignScreenE2eBattleTrace.get().asFile.absolutePath}",
        "--full-battle-time-scale=1",
        "--full-battle-max-sim-seconds=600",
        "--full-battle-seed=1000",
        "--full-battle-math-seed=305419896",
    )
    inputs.files(project(":core").sourceSets.main.get().allSource)
    outputs.files(campaignScreenE2eTrace, campaignScreenE2eBattleTrace)
    doFirst {
        delete(campaignScreenE2eTrace.get().asFile)
        delete(campaignScreenE2eBattleTrace.get().asFile)
    }
}
val verifyCampaignScreenE2e = tasks.register<Exec>("verifyCampaignScreenE2e") {
    group = "verification"
    description = "Validates actual screen classes and zero transition-boundary Enter presses."
    dependsOn(captureCampaignScreenE2e)
    inputs.files(campaignScreenE2eTrace, campaignScreenE2eBattleTrace, rootProject.file("tools/verify_campaign_screen_e2e.py"))
    commandLine(
        "python3",
        rootProject.file("tools/verify_campaign_screen_e2e.py").absolutePath,
        campaignScreenE2eTrace.get().asFile.absolutePath,
        campaignScreenE2eBattleTrace.get().asFile.absolutePath,
    )
}

// A desktop verification run is not valid unless this recovered-JS ↔ Kotlin
// lifecycle contract has also been compared.
tasks.named("check") { dependsOn("verifyWinConditionsPairwise", verifyRenderParityScope, verifyYingchuanSelectionRender, verifyYingchuanModalCaptures, verifyYingchuanBattleRegression, verifyCampaignScreenE2e) }
application {
    mainClass.set("com.jojo.game.desktop.DesktopLauncher")
    applicationDefaultJvmArgs = listOf(
        "-XstartOnFirstThread",
        "--enable-native-access=ALL-UNNAMED"
    )
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    options.release.set(17)
}
